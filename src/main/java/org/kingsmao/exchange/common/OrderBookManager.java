package org.kingsmao.exchange.common;


import org.kingsmao.exchange.entity.OrderBook;
import org.kingsmao.exchange.enums.Side;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 订单薄内存管理工具
 */
public class OrderBookManager {

    private static final ConcurrentHashMap<String, OrderBook> ORDER_BOOK = new ConcurrentHashMap<>();

    public static OrderBook getOrderBook(String symbol, Side side) {
        if (!ORDER_BOOK.containsKey((symbol + side.name()).toUpperCase())) {
            ORDER_BOOK.put((symbol + side.name()).toUpperCase(), new OrderBook(side));
        }
        return ORDER_BOOK.get((symbol + side.name()).toUpperCase());
    }

    public static void setOrderBook(String symbol, Side side, OrderBook orderBook) {
        ORDER_BOOK.put((symbol + side.name()).toUpperCase(), orderBook);
    }

    public static void delOrderBook(String symbol) {
        ORDER_BOOK.entrySet().removeIf(entry -> entry.getKey().equalsIgnoreCase((symbol + Side.BUY.name()).toUpperCase()) || entry.getKey().equalsIgnoreCase((symbol + Side.SELL.name()).toUpperCase()));
    }
}

