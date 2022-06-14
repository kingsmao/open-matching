package org.kingsmao.exchange.entity;

import lombok.Data;
import org.kingsmao.exchange.common.SymbolConstants;

import java.math.BigDecimal;

@Data
public class SymbolConfig {
    /**
     * 币对
     */
    private String symbol;

    /**
     * 默认精度位数
     */
    public static final int DEFAULT_PRECISION = 16;

    /**
     * 最小精度(16位)
     */
    public static final BigDecimal MIN_PRECISION = new BigDecimal("0.0000000000000001");

    /**
     * 允许的最小成交量(8位)
     */
    public static BigDecimal MIN_TRADE_VOL = new BigDecimal("0.00000001");

    /**
     * 允许的最小成交价(8位)
     */
    public static BigDecimal MIN_TRADE_AMOUNT = new BigDecimal("0.00000001");

    /**
     * 深度精度计算时使用；1：深度精确位数算整数位数，0：深度精确位数算小数位数
     */
    private Byte isFiat;

    /**
     * 深度精度0
     */
    private Integer depth0Pre;

    /**
     * 深度精度1
     */
    private Integer depth1Pre;

    /**
     * 深度精度2
     */
    private Integer depth2Pre;

    /**
     * 获取order表名
     */
    public String getOrderTable() {
        return SymbolConstants.TABLE_ORDER_PREFIX.getDescription() + this.symbol.toLowerCase();
    }

    /**
     * 获取trade表名
     */
    public String getTradeTable() {
        return SymbolConstants.TABLE_TRADE_PREFIX.getDescription() + this.symbol.toLowerCase();
    }
}
