package org.kingsmao.exchange;

import org.assertj.core.util.Lists;
import org.junit.Test;
import org.kingsmao.exchange.common.PriceComparator;
import org.kingsmao.exchange.enums.Side;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PriceComparatorTest {

    @Test
    public void buy_side(){
        Comparator<BigDecimal> buyComparator = PriceComparator.forSide(Side.BUY);
        List<BigDecimal> prices = Lists.newArrayList(new BigDecimal("9923"), new BigDecimal("306"),new BigDecimal("999"));

        prices.sort(buyComparator);

        BigDecimal p0 = prices.get(0);
        BigDecimal p1 = prices.get(1);
        BigDecimal p2 = prices.get(2);
        //买单，大价格排在前
        assertThat(p0.compareTo(p1) >= 0).isTrue();
        assertThat(p1.compareTo(p2) >= 0).isTrue();
    }

    @Test
    public void sell_side(){
        Comparator<BigDecimal> sellComparator = PriceComparator.forSide(Side.SELL);
        List<BigDecimal> prices = Lists.newArrayList(new BigDecimal("694"), new BigDecimal("999"),new BigDecimal("5566"));

        prices.sort(sellComparator);

        BigDecimal p0 = prices.get(0);
        BigDecimal p1 = prices.get(1);
        BigDecimal p2 = prices.get(2);
        //卖单，小价格排在前
        assertThat(p0.compareTo(p1) <= 0).isTrue();
        assertThat(p1.compareTo(p2) <= 0).isTrue();
    }
}
