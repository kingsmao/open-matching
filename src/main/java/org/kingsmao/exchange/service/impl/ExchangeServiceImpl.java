package org.kingsmao.exchange.service.impl;

import lombok.extern.log4j.Log4j2;
import org.kingsmao.exchange.disruptor.DisruptorFactory;
import org.kingsmao.exchange.service.ExchangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class ExchangeServiceImpl implements ExchangeService {

    @Autowired
    private DisruptorFactory disruptorFactory;

    @Override
    public void start() {

    }
}
