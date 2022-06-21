package org.kingsmao.exchange.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.google.common.base.Preconditions;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.log4j.Log4j2;
import org.kingsmao.exchange.enums.OrderType;
import org.kingsmao.exchange.enums.Side;

import java.math.BigDecimal;
import java.util.Date;
import java.util.TreeSet;

/**
 * <p>
 * 委托订单
 * </p>
 */
@Log4j2
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("ex_order")
public class ExOrder implements Cloneable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Integer userId;

    /**
     * 买卖方向
     */
    private Side side;

    /**
     * 限价单挂单价格
     */
    private BigDecimal price;

    /**
     * 现价单：挂单数量
     * 市价单：
     * 买方向：挂单金额
     * 卖方向：挂单数量
     */
    private BigDecimal volume;

    /**
     * 挂单手续费率
     */
    private Double feeRateMaker;

    /**
     * 吃单手续费率
     */
    private Double feeRateTaker;

    /**
     * 手续费
     */
    private BigDecimal fee;

    /**
     * 成交数量
     */
    private BigDecimal dealVolume = BigDecimal.ZERO;
    ;

    /**
     * 已成交金额
     */
    private BigDecimal dealMoney = BigDecimal.ZERO;
    ;

    /**
     * 成交均价
     */
    private BigDecimal avgPrice;

    /**
     * 订单状态：0 init，1 new，2 filled，3 part_filled，4 canceled，5 pending_cancel，6 expired
     */
    private Integer status;

    /**
     * 委托类型：1 limit，2 market，3 stop
     */
    private Integer type;

    /**
     * 创建时间
     */
    private Date ctime;

    /**
     * 更新时间
     */
    private Date mtime;

    /**
     * 订单来源：1web，2app，3api
     */
    private Integer source;

    @TableField(exist = false)
    private String symbol;

    /**
     * 是否是最后一条Order
     */
    @TableField(exist = false)
    private Long rebuildOffset;

    /**
     * 是否为取消单
     */
    @TableField(exist = false)
    private boolean isCancel = Boolean.FALSE;

    /**
     * 取消单列表
     */
    @TableField(exist = false)
    private TreeSet<ExOrder> cancelOrders;

    /**
     * 主动单定序ID
     */
    @TableField(exist = false)
    private Long offset;

    /**
     * 订单剩余数量
     */
    @TableField(exist = false)
    private BigDecimal remainAmount = BigDecimal.ZERO;

    @Override
    public ExOrder clone() {
        ExOrder o = null;
        try {
            o = (ExOrder) super.clone();
        } catch (CloneNotSupportedException e) {
            log.error("clone order error, e=", e);
        }
        return o;
    }

    /**
     * 未成交的量
     */
    public BigDecimal getUnfilledQuantity() {
        return this.volume.subtract(this.dealVolume);
    }

    /**
     * 校验是否已全部成交
     * 现价单：挂单数量
     * 市价单：
     *  买方向：挂单金额
     *  卖方向：挂单数量
     */
    public boolean isFilled() {
        if (this.isLimitOrder()) {
            return getUnfilledQuantity().compareTo(SymbolConfig.MIN_TRADE_VOL) < 0;
        }
        if (this.isMarketOrder()) {
            if (Side.BUY.equals(this.getSide())) {
                BigDecimal unfilledAmount = this.volume.subtract(this.dealMoney);
                return unfilledAmount.compareTo(SymbolConfig.MIN_TRADE_AMOUNT) < 0;
            }
            return getUnfilledQuantity().compareTo(SymbolConfig.MIN_TRADE_VOL) < 0;
        }
        //TODO 其他类型订单
        return Boolean.FALSE;
    }

    public boolean isLimitOrder() {
        return this.type == OrderType.LIMIT.getValue();
    }

    public boolean isMarketOrder() {
        return this.type == OrderType.MARKET.getValue();
    }

    /**
     * 判断两个价格是否有交叉，交叉是成交的条件
     */
    public boolean crossesAt(BigDecimal thatPrice) {
        if (this.side == Side.BUY) {
            return this.getPrice().compareTo(thatPrice) >= 0;
        }
        return this.getPrice().compareTo(thatPrice) <= 0;
    }

    /**
     * 成交时调用，增加当前订单已成交数量 deal_volume
     *
     * @param tradedQuantity
     * @return
     */
    public void addFilledQuantity(BigDecimal tradedQuantity) {
        Preconditions.checkArgument(tradedQuantityShouldGreaterZero(tradedQuantity), "成交量需要大于0");
        this.dealVolume = this.dealVolume.add(tradedQuantity);
    }

    private boolean tradedQuantityShouldGreaterZero(BigDecimal tradedQuantity) {
        return tradedQuantity.compareTo(BigDecimal.ZERO) > 0;
    }

    /**
     * 成交时调用，增加当前订单已成交金额 deal_money
     *
     * @param tradedQuoteAmount
     * @return
     */
    public void addFilledMoney(BigDecimal tradedQuoteAmount) {
        Preconditions.checkArgument(tradedQuantityShouldGreaterZero(tradedQuoteAmount), "成交额需要大于0");
        this.dealMoney = this.dealMoney.add(tradedQuoteAmount);
    }


}
