package org.kingsmao.exchange.core;

import lombok.extern.log4j.Log4j2;
import org.kingsmao.exchange.entity.ConfigSymbol;
import org.kingsmao.exchange.service.IConfigSymbolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Log4j2
@Component
public class MatchRunnable{

    @Autowired
    private IConfigSymbolService configSymbolService;

    public void run(String symbol){
        List<ConfigSymbol> openSymbol = configSymbolService.getOpenSymbol();
        log.info("获取到MatchRunnable symbol：{}",symbol);
        log.info("获取到MatchRunnable size：{}",openSymbol.size());
    }


}
