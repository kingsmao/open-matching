package org.kingsmao.exchange.service;

import org.kingsmao.exchange.disruptor.order.event.OrderEvent;
import org.kingsmao.exchange.entity.ExOrder;

public interface MatchService {

    /**
     * 撮合
     */
    void match(ExOrder exOrder);

    void cancel(ExOrder exOrder);

    void returnUnFilledMarketOrder(ExOrder exOrder);

    void exception(ExOrder exOrder);
}
