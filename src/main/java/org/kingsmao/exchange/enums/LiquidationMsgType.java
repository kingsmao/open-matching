package org.kingsmao.exchange.enums;

/**
 * 清算消息
 */
public enum LiquidationMsgType {
    /**
     * 正常成交
     */
    ORDER_TRADED,

    /**
     * 订单取消
     */
    ORDER_CANCELLED,

    /**
     * 退残渣
     */
    ORDER_REMAIN,

    /**
     * 异常
     */
    ORDER_EXCEPTION,

    /**
     * 没有任何成交的订单进入深度
     */
    ORDER_DEPTH,
    ;
}
