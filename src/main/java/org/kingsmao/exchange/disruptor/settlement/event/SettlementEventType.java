package org.kingsmao.exchange.disruptor.settlement.event;

public enum SettlementEventType {
    //正常成交

    ORDER_TRADED,

    //订单取消
    ORDER_CANCELLED,

    //退残渣：限价单更优价残渣，市价单残渣
    ORDER_REMAIN,

    //流水线处理异常，设置为EXCEPTION的事件下游处理器不再可用
    ORDER_EXCEPTION,

    //市价单完全成交，退残渣
    MARKET_ORDER_RETURN,

    //没有任何成交的订单进入深度
    ORDER_DEPTH,_DEPTH
}
