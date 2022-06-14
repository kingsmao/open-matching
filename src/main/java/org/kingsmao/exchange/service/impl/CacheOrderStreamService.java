package org.kingsmao.exchange.service.impl;

import org.kingsmao.exchange.entity.ExOrder;
import org.kingsmao.exchange.service.OrderStreamService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CacheOrderStreamService implements OrderStreamService {

    @Override
    public List<ExOrder> loadOrder() {
        return null;
    }
}
