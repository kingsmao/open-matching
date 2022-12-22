package org.kingsmao.exchange.service;

import org.kingsmao.exchange.disruptor.settlement.event.SettlementEvent;

public interface FeeService {

    void calculate(SettlementEvent settlementEvent);
}
