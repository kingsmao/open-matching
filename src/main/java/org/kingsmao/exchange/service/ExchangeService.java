package org.kingsmao.exchange.service;

import java.util.List;

public interface ExchangeService {

    /**
     * 撮合引擎启动总入口
     */
    void start();

    /**
     * 多线程开启币对撮合
     * @param symbols   要开启撮合的币对
     */
    void starOne(List<String> symbols);
}
