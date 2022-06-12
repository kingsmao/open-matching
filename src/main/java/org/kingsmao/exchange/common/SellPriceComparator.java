package org.kingsmao.exchange.common;

import java.math.BigDecimal;
import java.util.Comparator;

/**
 * 卖单出价越低越靠前
 * 例如出价：
 *      1
 *      3
 *      5
 *      6
 *      9
 */
public class SellPriceComparator implements Comparator<BigDecimal> {

    @Override
    public int compare(BigDecimal o1, BigDecimal o2) {
        return o1.compareTo(o2);
    }
}
