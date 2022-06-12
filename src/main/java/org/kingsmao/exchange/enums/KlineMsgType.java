package org.kingsmao.exchange.enums;

/**
 * k线消息
 */
public enum KlineMsgType {
    /**
     * 重新构建，开始
     */
    REBUILD_TRADE,

    /**
     * 重新构建，结束
     */
    REBUILD_DEPTH0,
    REBUILD_DEPTH1,
    REBUILD_DEPTH2,

    /**
     * Order更新（新订单，取消订单，取消订单异常，成交异常相关的买卖单）
     */
    ORDER,

    /**
     * Order更新（新订单）
     */
    NEW,

    /**
     * Order更新（取消订单）
     */
    CANCEL,

    /**
     * Order更新（取消订单异常）
     */
    CANCELFAIL,

    /**
     * Order更新（成交异常相关的买卖单）
     */
    TRADEFAIL,

    /**
     * Trade更新（撮合成功、撮合异常）
     */
    TRADE,

    /**
     * Trade更新（撮合成功）
     */
    SUCC,

    /**
     * Trade更新（撮合异常）
     */
    FAIL,
    ;
}
