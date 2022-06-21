package org.kingsmao.exchange.disruptor.settlement.translator;

import com.lmax.disruptor.EventTranslator;
import org.kingsmao.exchange.disruptor.settlement.event.SettlementEvent;

public class SettlementEventTranslator implements EventTranslator<SettlementEvent> {

    private SettlementEvent settlementEvent;

    public SettlementEventTranslator(SettlementEvent settlementEvent) {
        this.settlementEvent = settlementEvent;
    }

    @Override
    public void translateTo(SettlementEvent settlementEvent, long l) {
        settlementEvent.clear();
        settlementEvent.setSettlementEventType(this.settlementEvent.getSettlementEventType());
        settlementEvent.setTrade(this.settlementEvent.getTrade());
        //settlementEvent.setCancelledOrder(this.settlementEvent.getCancelledOrder());
        settlementEvent.setRemainOrder(this.settlementEvent.getRemainOrder());
        settlementEvent.setExceptionOid(this.settlementEvent.getExceptionOid());
        settlementEvent.setDepthOid(this.settlementEvent.getDepthOid());
        settlementEvent.setSymbol(this.settlementEvent.getSymbol());
    }
}
