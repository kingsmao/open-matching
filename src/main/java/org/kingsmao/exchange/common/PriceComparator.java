package org.kingsmao.exchange.common;


import org.kingsmao.exchange.enums.Side;

import java.math.BigDecimal;
import java.util.Comparator;

public class PriceComparator {
    /**
     * 买单和卖单价格比较器刚好相反
     */
    private static final Comparator<BigDecimal> BUY_PRICE_COMPARATOR = new BuyPriceComparator();
    private static final Comparator<BigDecimal> SELL_PRICE_COMPARATOR = new SellPriceComparator();

    public static Comparator<BigDecimal> forSide(Side side) {
        return (side == Side.BUY) ? BUY_PRICE_COMPARATOR : SELL_PRICE_COMPARATOR;
    }
}
