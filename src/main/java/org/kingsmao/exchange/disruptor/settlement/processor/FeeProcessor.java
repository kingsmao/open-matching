package org.kingsmao.exchange.disruptor.settlement.processor;

import com.lmax.disruptor.EventHandler;
import lombok.extern.log4j.Log4j2;
import org.kingsmao.exchange.disruptor.settlement.event.SettlementEvent;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class FeeProcessor implements EventHandler<SettlementEvent> {

    @Override
    public void onEvent(SettlementEvent settlementEvent, long sequence, boolean endOfBatch) throws Exception {
        log.info("FeeProcessor::onEvent ---> settlementEvent:{},sequence:{},endOfBatch:{}", settlementEvent, sequence, endOfBatch);

    }
}
