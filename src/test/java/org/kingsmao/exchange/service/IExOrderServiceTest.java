package org.kingsmao.exchange.service;

import org.junit.Test;
import org.kingsmao.exchange.BaseUnitTest;
import org.kingsmao.exchange.disruptor.order.event.OrderEvent;
import org.kingsmao.exchange.entity.ConfigSymbol;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class IExOrderServiceTest extends BaseUnitTest {

    @Autowired
    private IExOrderService exOrderService;

    @Autowired
    private IConfigSymbolService configSymbolService;

    @Autowired
    private OrderValidateService orderValidateService;

    @Test
    public void dynamic_table(){
        Long btcusdt = exOrderService.getLastOrderId("BTCUSDT");
        Long ethusdt = exOrderService.getLastOrderId("ETHUSDT");
        Long ltcusdt = exOrderService.getLastOrderId("LTCUSDT");

        List<ConfigSymbol> list = configSymbolService.list();
        System.out.println(list.size());
    }

    @Test
    public void dddd(){
        OrderEvent orderEvent = new OrderEvent();
        orderEvent.setPipelineLog("1111");
        orderValidateService.validate(orderEvent);

        System.out.println(orderEvent);
    }

}