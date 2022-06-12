package org.kingsmao.exchange.disruptor.order.event;

public enum OrderEventType {
    //正常
    NORMAL,

    //需要取消
    CANCEL,

    //异常订单
    INVALID,

    //退还未成交的市价单
    MARKET_ORDER_RETURN,

    //定时任务
    REFRESH_REBUILD,

    //清除内存异常单(异常单时清除内存的订单)
    CLEAN_MEMORY_INVALID
}
