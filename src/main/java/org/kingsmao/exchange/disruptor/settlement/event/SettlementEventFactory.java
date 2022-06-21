package org.kingsmao.exchange.disruptor.settlement.event;

import com.lmax.disruptor.EventFactory;

public class SettlementEventFactory implements EventFactory<SettlementEvent> {

    @Override
    public SettlementEvent newInstance() {
        return new SettlementEvent();
    }
}
