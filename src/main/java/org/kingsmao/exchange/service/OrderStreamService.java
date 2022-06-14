package org.kingsmao.exchange.service;

import org.kingsmao.exchange.entity.ExOrder;

import java.util.List;

public interface OrderStreamService {

    /**
     * 加载订单
     */
    List<ExOrder> loadOrder();

}
