package org.kingsmao.exchange.enums;

/**
 * 订单状态
 */
public enum OrderStatus {
    /**
     * 初始订单
     */
    INIT(0, "初始订单，未进入撮合引擎，不能被盘口读取"),

    /**
     * 新订单
     */
    NEW(1, "新订单，已经进入撮合引擎，能够被盘口读取"),

    /**
     * 完全成交
     */
    FILLED(2, "完全成交"),

    /**
     * 部分成交
     */
    PART_FILLED(3, "部分成交"),

    /**
     * 已取消
     */
    CANCELED(4, "已取消"),

    /**
     * 待取消
     */
    PENDING_CANCEL(5, "待取消"),

    /**
     * 已过期
     */
    EXPIRED(6, "已过期");

    private int value;
    private String description;

    OrderStatus(int value, String description) {
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
