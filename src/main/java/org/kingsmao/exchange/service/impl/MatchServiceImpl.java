package org.kingsmao.exchange.service.impl;

import lombok.extern.log4j.Log4j2;
import org.kingsmao.exchange.disruptor.order.event.OrderEvent;
import org.kingsmao.exchange.service.MatchService;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class MatchServiceImpl implements MatchService {

    @Override
    public void check(OrderEvent orderEvent) {
        log.info(orderEvent.toString());
    }
}

