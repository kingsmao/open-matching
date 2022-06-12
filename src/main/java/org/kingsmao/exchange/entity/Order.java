package org.kingsmao.exchange.entity;

import com.google.common.base.Preconditions;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.kingsmao.exchange.enums.OrderType;
import org.kingsmao.exchange.enums.Side;

import java.math.BigDecimal;
import java.util.Date;
import java.util.TreeSet;

@Log4j2
@Data
public class Order implements Cloneable {

    private Long id;

    private Long userId;
    /**
     * 订单方向
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

    private BigDecimal dealVolume = BigDecimal.ZERO;

    private BigDecimal dealMoney = BigDecimal.ZERO;
    /**
     * 成交均价
     */
    private BigDecimal avgPrice;

    /**
     * 委托类型：1 limit，2 market，3 stop
     */
    private int type;

    private Date ctime;

    private String symbol;

    /**
     * 是否是最后一条Order
     */
    private Long rebuildOffset;

    /**
     * 是否为取消单
     */
    private boolean isCancel = false;

    /**
     * 取消单列表
     */
    private TreeSet<Order> cancelOrders;

    /**
     * 主动单定序ID
     */
    private Long offset;

    /**
     * 订单剩余数量
     */
    private BigDecimal remainAmount = BigDecimal.ZERO;

    private int status;

    @Override
    public Order clone() {
        Order o = null;
        try {
            o = (Order) super.clone();
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
     * 现价单：挂单数量
     * 市价单：
     * 买方向：挂单金额
     * 卖方向：挂单数量
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
