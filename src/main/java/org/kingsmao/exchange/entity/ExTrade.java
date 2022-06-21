package org.kingsmao.exchange.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.kingsmao.exchange.enums.Side;

/**
 * <p>
 * 成交记录
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("ex_trade")
public class ExTrade implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 成交价格
     */
    private BigDecimal price;

    /**
     * 成交数量
     */
    private BigDecimal volume;

    /**
     * 买单id
     */
    private Long bidId;

    /**
     * 卖单id
     */
    private Long askId;

    /**
     * 主动单方向
     */
    private Side trendSide;

    /**
     * 买方用户id
     */
    private Integer bidUserId;

    /**
     * 卖方用户id
     */
    private Integer askUserId;

    /**
     * 买单手续费
     */
    private BigDecimal buyFee;

    /**
     * 卖单手续费
     */
    private BigDecimal sellFee;

    /**
     * 买单支付手续费的币种
     */
    private String buyFeeCoin;

    /**
     * 卖单支付手续费的币种
     */
    private String sellFeeCoin;

    /**
     * 创建时间
     */
    private Date ctime;

    /**
     * 最后更新时间
     */
    private Date mtime;

    /**
     * 订单流转的唯一标识
     */
    private String tradeNonce;

    private ExOrder takerOrder;

    private ExOrder makerOrder;


}
