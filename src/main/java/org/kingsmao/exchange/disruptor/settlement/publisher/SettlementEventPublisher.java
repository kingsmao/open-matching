package org.kingsmao.exchange.disruptor.settlement.publisher;

import org.anair.disruptor.DisruptorConfig;
import org.anair.disruptor.publisher.EventPublisher;
import org.kingsmao.exchange.disruptor.settlement.event.SettlementEvent;

public class SettlementEventPublisher{


    public void publish(SettlementEvent settlementEvent) {
        //this.disruptorConfig.publish(new OutputEventTranslator(outputEvent));
    }
}
