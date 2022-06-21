package org.kingsmao.exchange.disruptor.order.processor;

import com.lmax.disruptor.EventHandler;
import lombok.extern.log4j.Log4j2;
import org.kingsmao.exchange.disruptor.order.event.OrderEvent;
import org.kingsmao.exchange.disruptor.order.event.OrderEventType;
import org.kingsmao.exchange.entity.ExOrder;
import org.kingsmao.exchange.service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class MatchProcessor implements EventHandler<OrderEvent> {

    @Autowired
    private MatchService matchService;

    @Override
    public void onEvent(OrderEvent orderEvent, long sequence, boolean endOfBatch) throws Exception {
        log.info("MatchProcessor::onEvent ---> orderEvent:{},sequence:{},endOfBatch:{}",orderEvent,sequence,endOfBatch);
        OrderEventType orderEventType = orderEvent.getOrderEventType();
        ExOrder executedOrder = orderEvent.getExecutedOrder();
        String symbol = orderEvent.getSymbol();

        if (orderEventType.equals(OrderEventType.NORMAL)) {
            matchService.match(executedOrder);
        }


        orderEvent.setPipelineLog(orderEvent.getPipelineLog() + " | Match");

    }
}
