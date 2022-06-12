package org.kingsmao.exchange.common;

import java.math.BigDecimal;
import java.util.Comparator;

/**
 * 买单出价越高越靠前
 * 例如出价：
 *      9
 *      6
 *      5
 *      4
 *      1
 */
public class BuyPriceComparator implements Comparator<BigDecimal> {

    @Override
    public int compare(BigDecimal o1, BigDecimal o2) {
        return o2.compareTo(o1);
    }
}
