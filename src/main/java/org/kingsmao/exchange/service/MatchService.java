package org.kingsmao.exchange.service;

import org.kingsmao.exchange.disruptor.order.event.OrderEvent;

public interface MatchService {

    void check(OrderEvent orderEvent);
}
