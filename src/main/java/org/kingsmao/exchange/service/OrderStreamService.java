package org.kingsmao.exchange.service;

import org.kingsmao.exchange.entity.Order;

import java.util.List;

public interface OrderStreamService {

    /**
     * 加载订单
     */
    List<Order> loadOrder();

}
