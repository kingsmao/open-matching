package org.kingsmao.exchange;

import org.junit.Test;
import org.kingsmao.exchange.entity.Order;
import org.kingsmao.exchange.enums.OrderType;
import org.kingsmao.exchange.enums.Side;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class OrderTest {

    @Test
    public void add_filled_quantity() {
        BigDecimal init = new BigDecimal("100");
        BigDecimal filled = new BigDecimal("34");
        Order order = new Order();
        order.setVolume(init);
        order.setSide(Side.SELL);

        assertThat(order.getUnfilledQuantity()).isEqualTo(order.getVolume());
        order.addFilledQuantity(filled);
        assertThat(order.getUnfilledQuantity()).isEqualTo(order.getVolume().subtract(filled));
    }

    @Test
    public void limit_order_not_filled() {
        Order order = new Order();
        order.setVolume(new BigDecimal("100"));
        order.setSide(Side.SELL);

        assertThat(order.getUnfilledQuantity()).isEqualTo(order.getVolume());
    }

    @Test
    public void traded_quantity_should_greater_zero() {
        Order order = new Order();

        assertThatThrownBy(() -> {
            order.addFilledQuantity(new BigDecimal("-234"));
        }).isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> {
            order.addFilledQuantity(BigDecimal.ZERO);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void limit_order_filled_test(){
        Order order = new Order();
        order.setVolume(new BigDecimal("100"));
        order.setSide(Side.SELL);
        order.setType(OrderType.LIMIT.getValue());
        assertThat(order.isFilled()).isFalse();

        order.addFilledQuantity(new BigDecimal("99.9999"));
        assertThat(order.isFilled()).isFalse();

        order.addFilledQuantity(new BigDecimal("99.999999999999999"));
        assertThat(order.isFilled()).isTrue();
    }

    @Test
    public void buy_price_crosses_at_lower_quote(){
        Order order = new Order();
        order.setPrice(new BigDecimal("100"));
        order.setSide(Side.BUY);

        BigDecimal thatPrice1 = new BigDecimal("80");
        assertThat(order.crossesAt(thatPrice1)).isTrue();

        BigDecimal thatPrice2 = new BigDecimal("100");
        assertThat(order.crossesAt(thatPrice2)).isTrue();

        BigDecimal thatPrice3 = new BigDecimal("101");
        assertThat(order.crossesAt(thatPrice3)).isFalse();
    }

    @Test
    public void sell_price_crosses_at_higher_quote(){
        Order order = new Order();
        order.setPrice(new BigDecimal("100"));
        order.setSide(Side.SELL);

        BigDecimal thatPrice1 = new BigDecimal("80");
        assertThat(order.crossesAt(thatPrice1)).isFalse();

        BigDecimal thatPrice2 = new BigDecimal("100");
        assertThat(order.crossesAt(thatPrice2)).isTrue();

        BigDecimal thatPrice3 = new BigDecimal("101");
        assertThat(order.crossesAt(thatPrice3)).isTrue();
    }
}
