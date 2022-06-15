package org.kingsmao.exchange.service;

import org.kingsmao.exchange.disruptor.order.event.OrderEvent;

public interface OrderValidateService {

    /**
     * 验证输入的订单
     */
    boolean validate(OrderEvent orderEvent);
}
