package org.kingsmao.exchange.enums;

/**
 * 订单类型
 */
public enum OrderType {
    /**
     * 限价委托订单类型
     */
    LIMIT(1, "限价委托"),

    /**
     * 市价委托订单类型
     */
    MARKET(2, "市价委托"),

    /**
     * 止盈止损委托订单类型
     */
    STOP(3,"止盈止损委托");

    private int value;
    private String description;

    OrderType(int value, String description) {
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
    }
}
