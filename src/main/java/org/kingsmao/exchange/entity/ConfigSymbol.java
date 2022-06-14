package org.kingsmao.exchange.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 币对配置表
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ConfigSymbol implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 币对编码：大写：ETHBTC
     */
    private String symbol;

    /**
     * 基准货币，symbol的前半段
     */
    private String base;

    /**
     * 计价货币，symbol的后半段
     */
    private String quote;

    /**
     * 是否开放交易，0否，1是
     */
    private Integer isOpen;

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
     * 价格精度
     */
    private Integer pricePre;

    /**
     * 数量精度
     */
    private Integer volumePre;

    /**
     * 限价最小价
     */
    private BigDecimal limitPriceMin;

    /**
     * 限价最小数量
     */
    private BigDecimal limitVolumeMin;

    /**
     * 市价买最小值
     */
    private BigDecimal marketBuyMin;

    /**
     * 市价卖最小值
     */
    private BigDecimal marketSellMin;

    /**
     * 排序
     */
    private Integer sort;

    private LocalDateTime ctime;

    private LocalDateTime mtime;


}
