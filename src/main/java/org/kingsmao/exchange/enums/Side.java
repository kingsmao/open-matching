package org.kingsmao.exchange.enums;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.Validate;

/**
 * 订单方向
 */
public enum Side {
    //买单方向
    BUY,

    //卖单方向
    SELL
    ;

    public static Side fromString(String side) {
        Validate.notBlank(side,"订单方向不能为空");
        String upperCaseSide = side.toUpperCase();
        Preconditions.checkArgument(mustBuyOrSell(upperCaseSide), "订单方向必须是SELL或BUY");
        return Side.valueOf(upperCaseSide);
    }

    public static Side getOpposite(Side oneSide) {
        return Side.BUY.equals(oneSide) ? Side.SELL : Side.BUY;
    }

    private static boolean mustBuyOrSell(String side){
        return side.equals(Side.BUY.toString()) || side.equals(Side.SELL.toString());
    }
}
