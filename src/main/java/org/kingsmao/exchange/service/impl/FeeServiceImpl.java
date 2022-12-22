package org.kingsmao.exchange.service.impl;

import lombok.extern.log4j.Log4j2;
import org.kingsmao.exchange.disruptor.settlement.event.SettlementEvent;
import org.kingsmao.exchange.entity.ExTrade;
import org.kingsmao.exchange.entity.SymbolConfig;
import org.kingsmao.exchange.service.FeeService;
import org.kingsmao.exchange.service.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Log4j2
@Service
public class FeeServiceImpl implements FeeService {

    @Autowired
    private IAccountService accountService;


    @Override
    public void calculate(SettlementEvent settlementEvent) {
        BigDecimal baseFeeVolume = BigDecimal.ZERO;
        BigDecimal quoteFeeAmount = BigDecimal.ZERO;
        ExTrade trade = settlementEvent.getTrade();
        String symbol = settlementEvent.getSymbol();
        SymbolConfig
    }
}
