package org.kingsmao.exchange.disruptor.settlement.event;

import lombok.Data;
import org.kingsmao.exchange.entity.ExTrade;
import org.kingsmao.exchange.entity.RemainOrder;

@Data
public class SettlementEvent {

    private SettlementEventType settlementEventType;

    private ExTrade trade;

    private RemainOrder remainOrder;

    private Long exceptionOid;

    private Long depthOid;

    private String symbol;

    public void clear(){
        this.settlementEventType = null;
        this.trade = null;
        this.remainOrder = null;
        this.exceptionOid = null;
        this.depthOid = null;
        this.symbol = null;
    }


}
