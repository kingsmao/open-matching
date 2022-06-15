package org.kingsmao.exchange.service.impl;

import lombok.extern.log4j.Log4j2;
import org.kingsmao.exchange.common.Utils;
import org.kingsmao.exchange.disruptor.order.event.OrderEvent;
import org.kingsmao.exchange.disruptor.order.event.OrderEventType;
import org.kingsmao.exchange.entity.ExOrder;
import org.kingsmao.exchange.entity.SymbolConfig;
import org.kingsmao.exchange.enums.OrderStatus;
import org.kingsmao.exchange.enums.Side;
import org.kingsmao.exchange.service.OrderValidateService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static org.kingsmao.exchange.common.Utils.needMatch;

@Log4j2
@Service
public class OrderValidateServiceImpl implements OrderValidateService {

    @Override
    public boolean validate(OrderEvent orderEvent) {
        ExOrder order = orderEvent.getExecutedOrder();
        if (order.isCancel()) {
            orderEvent.setOrderEventType(OrderEventType.CANCEL);
            orderEvent.setMessage(orderEvent.getMessage() + " | 委托单 撤销订单");
            return Boolean.TRUE;
        }
        if (order.getStatus() == OrderStatus.PENDING_CANCEL.getValue()) {
            orderEvent.setOrderEventType(OrderEventType.CANCEL);
            orderEvent.setMessage(orderEvent.getMessage() + " | 委托单 撤销订单");
            return Boolean.TRUE;
        }
        if (!needMatch(order.getStatus())) {
            log.warn("ValidateProcessor 检验失败 , 订单状态错误  order={}", order);
            orderEvent.setOrderEventType(OrderEventType.INVALID);
            orderEvent.setMessage(orderEvent.getMessage() + " | 委托单 订单状态错误，不应进入撮合引擎");
            return Boolean.FALSE;
        }
        if (order.isMarketOrder() && !checkMarketOrder(orderEvent, order)) {
            return Boolean.FALSE;
        }
        if (order.isLimitOrder() && !checkLimitOrder(orderEvent, order)) {
            return Boolean.FALSE;
        }
        orderEvent.setOrderEventType(OrderEventType.NORMAL);
        return Boolean.TRUE;
    }

    /**
     * 校验订单数量、价格精度
     *
     * @param orderEvent 委托订单输入事件
     * @return Boolean
     */
    private Boolean checkPrecision(OrderEvent orderEvent, ExOrder order) {
        if (order.isLimitOrder()) {
            if (hasRemainder(order.getPrice().multiply(order.getVolume()))) {
                log.warn("精度校验失败, price * volume 精度错误, order={}", order.toString());
                orderEvent.setOrderEventType(OrderEventType.INVALID);
                orderEvent.setMessage(orderEvent.getMessage() + " | precision error");
                return Boolean.FALSE;
            }
        }
        if (hasRemainder(order.getVolume())) {
            log.warn("精度校验失败, order volume 精度错误, order={}", order.toString());
            orderEvent.setOrderEventType(OrderEventType.INVALID);
            orderEvent.setMessage(orderEvent.getMessage() + " | precision error");
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    /**
     * 与系统规定最小精度相除，是否有余数，如果有就说明超精度了
     *
     * @return true：有余数，false：没有
     */
    private Boolean hasRemainder(BigDecimal check) {
        BigDecimal remainder = Utils.divideForRemainder(check, SymbolConfig.MIN_PRECISION);
        return remainder.compareTo(BigDecimal.ZERO) > 0;
    }

    /**
     * 校验限价单
     *
     * @param orderEvent 输入事件
     * @return Boolean
     */
    private Boolean checkLimitOrder(OrderEvent orderEvent, ExOrder order) {
        //下单量小于最小成交量
        if (order.getVolume().compareTo(SymbolConfig.MIN_TRADE_VOL) < 0) {
            log.warn("精度校验失败, 委托单下单volume 精度错误 {}", order);
            orderEvent.setOrderEventType(OrderEventType.INVALID);
            orderEvent.setMessage(orderEvent.getMessage() + " | 精度校验失败, 委托单 下单volume 精度错误");
            return Boolean.FALSE;
        }
        //未成交量不足单次最小成交量
        if (order.getUnfilledQuantity().compareTo(SymbolConfig.MIN_TRADE_VOL) < 0) {
            log.warn("精度校验失败, 委托单未成交量不足已成交 order={}", order);
            orderEvent.setOrderEventType(OrderEventType.INVALID);
            orderEvent.setMessage(orderEvent.getMessage() + " | 委托单未成交量不足已成交");
            return Boolean.FALSE;
        }
        //校验精度
        if (!this.checkPrecision(orderEvent, order)) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }


    /**
     * 校验市价单
     *
     * @param orderEvent 委托订单输入事件
     * @return Boolean
     */
    private Boolean checkMarketOrder(OrderEvent orderEvent, ExOrder order) {
        //已经有成交量的市价单取消(重启撮合的时候会出现这种情况）
        if (order.getDealVolume().compareTo(BigDecimal.ZERO) > 0) {
            orderEvent.setOrderEventType(OrderEventType.MARKET_ORDER_RETURN);
            orderEvent.setMessage(orderEvent.getMessage() + " | market order be traded");
            return Boolean.TRUE;
        }
        //校验最小成交额
        if (order.getSide().equals(Side.BUY) && order.getVolume().compareTo(SymbolConfig.MIN_TRADE_AMOUNT) < 0) {
            log.warn("精度错误, 市价单买单最小成交额不足, order={}", order);
            orderEvent.setOrderEventType(OrderEventType.INVALID);
            orderEvent.setMessage(orderEvent.getMessage() + " | 精度错误, 市价单最买单小成交额不足");
            return Boolean.FALSE;
        }
        //校验最小成交量
        if (order.getSide().equals(Side.SELL) && order.getVolume().compareTo(SymbolConfig.MIN_TRADE_VOL) < 0) {
            log.warn("精度错误, 市价单卖单最小成交量不足, order={}", order);
            orderEvent.setOrderEventType(OrderEventType.INVALID);
            orderEvent.setMessage(orderEvent.getMessage() + " | 精度错误, 市价单卖单最小成交量不足");
            return Boolean.FALSE;
        }
        //校验精度
        if (!this.checkPrecision(orderEvent, order)) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
}
