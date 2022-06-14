package org.kingsmao.exchange.entity;

import lombok.Data;
import org.kingsmao.exchange.enums.Side;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class Trade {
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
    private Long bidUserId;
    /**
     * 卖方用户id
     */
    private Long askUserId;
    /**
     * 买单支付手续费的币种
     */
    private String buyFeeCoin;

    private BigDecimal buyFee;
    /**
     * 卖单支付手续费的币种
     */
    private String sellFeeCoin;

    private Date ctime;

    private ExOrder takerOrder;

    private ExOrder makerOrder;

    /**
     * 订单流转的唯一标识
     */
    private String tradeNonce;
    private boolean buyFeeByAward;
    private BigDecimal sellFee;
    private boolean sellFeeByAward;
    private Boolean takerUseFeeCoin;
    private Boolean makerUseFeeCoin;
    private String takerFeeCoin;
    private Boolean takerUseFeeByAward;
    private Integer takerFeeAccountAward;
    private Integer takerFeeAccountNormal;
    private String makerFeeCoin;
    private Boolean makerUseFeeByAward;
    private Integer makerFeeAccountNormal;
    private Integer makerFeeAccountAward;
    private boolean sellUsePlatformCoin;
    private Integer takerSysFeeAccount;
    private Integer takerSysFeeLeverAcc;
    private Integer makerSysFeeAccount;
    private Integer makerSysFeeLeverAcc;
    private boolean buyUsePlatformCoin;
    private Date mtime;

}
