package org.kingsmao.exchange.common;

public enum SymbolConstants {

    /**
     * 获取订单表前缀
     */
    TABLE_ORDER_PREFIX(3, "ex_order_"),

    /**
     * 获取成交表前缀
     */
    TABLE_TRADE_PREFIX(4, "ex_trade_"),

    /**
     * 获取订单 topic 前缀
     */
    TOPIC_NAME_PREFIX(5, "EXCHANGE_ORDER_TOPIC_"),
    ;

    private int value;
    private String description;

    SymbolConstants(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }}
