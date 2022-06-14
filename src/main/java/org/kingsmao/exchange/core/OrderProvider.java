package org.kingsmao.exchange.core;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections4.CollectionUtils;
import org.kingsmao.exchange.common.MatchDataManager;
import org.kingsmao.exchange.common.OrderOffsetComparator;
import org.kingsmao.exchange.disruptor.order.publisher.OrderEventPublisher;
import org.kingsmao.exchange.entity.ExOrder;
import org.kingsmao.exchange.enums.OrderStatus;
import org.kingsmao.exchange.service.IExOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.TreeSet;

@Log4j2
@Component
public class OrderProvider {

    @Autowired
    private IExOrderService exOrderService;

    @Autowired
    private OrderEventPublisher publisher;


    public void loadOrder(String symbol){
        log.info("开始加载{}委托订单",symbol);
        Long lastOrderId = exOrderService.getLastOrderId(symbol);
        MatchDataManager matchDataManager = MatchDataManager.get(symbol);

        /**
         * 死循环，不断从DB中加载委托订单，将订单打到MatchDataManager缓存中
         */
        for (; ; ) {
            //撮合启动的时候内存中是没有订单的，需要从DB/MQ中加载订单，下一个for循环就会有订单，订单将会通过disruptor打到撮合引擎中
            ExOrder order = matchDataManager.pollFirstOrder();
            if (cacheHasOrder(order)) {
                //从内存中取到委托订单，通过disruptor分发给撮合引擎
                publisher.publish(symbol,order);
                continue;
            }
            //从DB/MQ中搂取订单推到内存中
            pushOrderToCache(matchDataManager,symbol,lastOrderId);
            sleep(50);
        }
    }

    private void pushOrderToCache(MatchDataManager matchDataManager,String symbol,Long lastOrderId){
        Long loadInitOffset = matchDataManager.getLoadInitOffset();
        TreeSet<ExOrder> roundOrders = matchDataManager.getRoundOrders();
        roundOrders.clear();
        TreeSet<ExOrder> pendingCancelOrders = new TreeSet<>(new OrderOffsetComparator());//要取消的委托单
        //从DB中搂订单
        List<ExOrder> exOrders = exOrderService.loadUnFilledOrder(symbol, lastOrderId);
        for (ExOrder exOrder : exOrders) {
            //订单防重
            if (matchDataManager.isRepeat(exOrder.getId())) {
                continue;
            }
            loadInitOffset = exOrder.getId();
            if (exOrder.getStatus() == OrderStatus.PENDING_CANCEL.getValue()) {
                pendingCancelOrders.add(exOrder);
                continue;
            }
            roundOrders.add(exOrder);
        }
        matchDataManager.setLoadInitOffset(loadInitOffset);
        //待撤销订单批量处理
        if (CollectionUtils.isNotEmpty(pendingCancelOrders)) {
            ExOrder cancelOrder = new ExOrder();
            cancelOrder.setSymbol(symbol);
            cancelOrder.setCancel(Boolean.TRUE);
            cancelOrder.setCancelOrders(pendingCancelOrders);
            cancelOrder.setId(loadInitOffset);
            roundOrders.add(cancelOrder);
        }
    }


    private boolean cacheHasOrder(ExOrder order){
        return null != order;
    }

    private void sleep(long l) {
        try {
            Thread.sleep(l);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
