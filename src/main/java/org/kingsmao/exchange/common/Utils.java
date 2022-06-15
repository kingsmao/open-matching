package org.kingsmao.exchange.common;

import com.google.common.collect.Lists;
import org.kingsmao.exchange.entity.SymbolConfig;
import org.kingsmao.exchange.enums.OrderStatus;

import java.math.BigDecimal;
import java.util.List;

public class Utils {
    public static final List<Integer> UN_FILLED_ORDER = Lists.newArrayList(
            OrderStatus.INIT.getValue(),OrderStatus.NEW.getValue(),OrderStatus.PART_FILLED.getValue(),OrderStatus.PENDING_CANCEL.getValue());

    public static final List<Integer> NEED_MATCH = Lists.newArrayList(
            OrderStatus.INIT.getValue(),OrderStatus.NEW.getValue(),OrderStatus.PART_FILLED.getValue());

    public static List<Integer> getUnFilledOrderStatus(){
        return UN_FILLED_ORDER;
    }

    public static boolean unFilledOrder(Integer status){
        return UN_FILLED_ORDER.contains(status);
    }

    public static boolean needMatch(Integer status){
        return NEED_MATCH.contains(status);
    }

    public static boolean filledOrder(Integer status){
        return !UN_FILLED_ORDER.contains(status);
    }

    /**
     * 两个数相除返回余数
     * @param dividend
     * @param divisor
     * @return  余数
     */
    public static BigDecimal divideForRemainder(BigDecimal dividend, BigDecimal divisor) {
        BigDecimal[] divResult = dividend.divideAndRemainder(divisor);
        return divResult[1];
    }
    /**
     * 获除指定除数后的余数的pow
     *
     * @param valuePow
     * @return
     */
    public static BigDecimal divedReminderPowByConfig(BigDecimal valuePow) {
        BigDecimal fValue = valuePow;
        BigDecimal[] divResult = fValue.divideAndRemainder(SymbolConfig.MIN_TRADE_VOL);
        //获取
        BigDecimal reminder = divResult[1];
        return reminder;
    }

    /**
     * 按照系统的精度要求和指数要求，将两个数整除后返回系统指定的次数的pow
     *
     * @param valuePow   被除数
     * @param divisorPow 除数
     * @return valuePow / divisorPow * DEFAULT_PRICE_AMOUNT_POW_FLOAT
     */
    public static BigDecimal divedToPow(BigDecimal valuePow, BigDecimal divisorPow) {
        return valuePow.divide(divisorPow, SymbolConfig.DEFAULT_PRECISION, BigDecimal.ROUND_HALF_EVEN);
    }
}
