package org.kingsmao.exchange.disruptor.order.event;

import lombok.Data;
import org.kingsmao.exchange.entity.Order;

@Data
public class OrderEvent {
    private Order order;
}
