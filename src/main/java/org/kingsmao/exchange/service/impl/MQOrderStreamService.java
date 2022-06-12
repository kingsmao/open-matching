package org.kingsmao.exchange.service.impl;

import org.kingsmao.exchange.entity.Order;
import org.kingsmao.exchange.service.OrderStreamService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MQOrderStreamService implements OrderStreamService {

    @Override
    public List<Order> loadOrder() {
        return null;
    }
}
