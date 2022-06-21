package org.kingsmao.exchange.service.impl;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.log4j.Log4j2;
import org.kingsmao.exchange.common.MatchDataManager;
import org.kingsmao.exchange.core.OrderProvider;
import org.kingsmao.exchange.disruptor.DisruptorFactory;
import org.kingsmao.exchange.entity.ConfigSymbol;
import org.kingsmao.exchange.service.ExchangeService;
import org.kingsmao.exchange.service.IConfigSymbolService;
import org.kingsmao.exchange.service.IExOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Log4j2
@Service
public class ExchangeServiceImpl implements ExchangeService {

    private static ExecutorService executor = Executors.newCachedThreadPool(new ThreadFactoryBuilder().setNameFormat("order-%d").build());

    @Autowired
    private DisruptorFactory disruptorFactory;

    @Autowired
    private IConfigSymbolService configSymbolService;

    @Autowired
    private OrderProvider orderProvider;

    @Autowired
    private IExOrderService exOrderService;

    @Override
    public void start() {
        List<ConfigSymbol> openSymbols = configSymbolService.getOpenSymbol();
        List<String> symbols = openSymbols.stream().map(ConfigSymbol::getSymbol).collect(Collectors.toList());
        this.starOne(symbols);
    }

    @Override
    public void starOne(List<String> symbols) {
        //一个币对分配一个线程
        Map<String, Future<?>> futureMap = new HashMap<>(512);
        for (String symbol : symbols) {
            try {
                Future<?> future = executor.submit(() -> startMatchThread(symbol));
                futureMap.put(symbol, future);
            } catch (Exception e) {
                log.warn("撮合币对:{}启动失败！", symbol, e);
            }
        }
    }

    /**
     * 一个撮合币对一个线程
     * 初始化币对配置，加载订单
     *
     * @param symbol 撮合币对
     */
    private void startMatchThread(String symbol) {
        //TODO 初始化币对配置，对应币对账户，深度
        MatchDataManager.init(symbol);
        disruptorFactory.initOrderDisruptor(symbol);

        //将撮合未启动状态下进行委托的订单取消
        exOrderService.cancelUnLoadOrder(symbol);

        //开始加载数据库订单
        orderProvider.loadOrder(symbol);
    }
}
