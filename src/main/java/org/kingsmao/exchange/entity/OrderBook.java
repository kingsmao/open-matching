package org.kingsmao.exchange.entity;

import com.google.common.base.Preconditions;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.kingsmao.exchange.common.PriceComparator;
import org.kingsmao.exchange.enums.Side;

import java.math.BigDecimal;
import java.util.*;

@Data
public class OrderBook implements Iterable<ExOrder> {

    private Side side;

    /**
     * OrderBook使用TreeMap作为数据结构，其中价格作为key，同一价格水平对应一个订单列表LinkedList
     * 价格优先：价格排序按照买卖盘使用相反的价格比较器
     * 时间优先：同一价格下订单列表使用先进先出规则
     */
    private TreeMap<BigDecimal, LinkedList<ExOrder>> limitBook;

    public OrderBook(Side side) {
        this.side = side;
        this.limitBook = new TreeMap<>(PriceComparator.forSide(side));
    }


    private static void tradedQuantityNotNull(BigDecimal tradedQuantity) {
        Preconditions.checkNotNull(tradedQuantity, "成交量不能为空");
    }

    private static void tradedQuantityShouldGreaterZero(BigDecimal tradedQuantity) {
        Preconditions.checkArgument(!tradedQuantity.equals(BigDecimal.ZERO), "成交量必须大于0");
    }

    /**
     * 匹配成功的挂单量，需要小于等于内存中订单的挂单量
     */
    private static void checkMatchingVolume(int compareRet) {
        //队首订单数量和匹配成交数量比较，比较值对应于：-1 队首数量<成交数量， 0 相等， 1 队首数量>成交数量
        Preconditions.checkState(compareRet >= 0, "匹配成功的挂单量，需要小于等于内存中订单的挂单量");
    }

    public static void checkMatchingVolume(BigDecimal matchingVolume) {
        tradedQuantityNotNull(matchingVolume);
        tradedQuantityShouldGreaterZero(matchingVolume);
    }

    private static boolean matchingVolumeEqualsTopOrderVolume(int compareRet) {
        return compareRet == 0;
    }

    public Side getSide() {
        return this.side;
    }

    public boolean isEmpty() {
        return this.limitBook.isEmpty();
    }

    public void addLimitOrder(ExOrder order) {
        BigDecimal limitPrice = order.getPrice();
        if (containsPrice(limitPrice)) {
            appendExistsPrice(limitPrice, order);
            return;
        }
        appendNewPrice(limitPrice, order);
    }

    public boolean containsPrice(BigDecimal price) {
        return this.limitBook.containsKey(price);
    }

    /**
     * 添加限价单到已经存在价格深度上，新订单添加到订单列表的末尾
     */
    public void appendExistsPrice(BigDecimal limitPrice, ExOrder order) {
        LinkedList<ExOrder> orders = this.limitBook.get(limitPrice);
        orders.add(order);
        this.limitBook.put(limitPrice, orders);
    }

    /**
     * 添加限价单到新的价格深度上
     */
    public void appendNewPrice(BigDecimal limitPrice, ExOrder order) {
        LinkedList<ExOrder> orders = new LinkedList<>();
        orders.add(order);
        this.limitBook.put(limitPrice, orders);
    }

    public void addMarketOrder(ExOrder order) {
        throw new IllegalArgumentException("" +
                "市价单的本质是挂单价为0的现价单，市价单是依据对手盘的价格来成交，所以不存在添加市价单方法");
    }

    /**
     * 向盘口中增加订单
     * <p>
     * 问：什么情况订单会加入盘口（停留在盘口）
     * 答：当前订单没有匹配到对手盘时才会进入盘口，如果市价单没有对手盘，则市价单会自动撤单不会进入盘口
     * 问：为什么市价单不会进入盘口？
     * 答：市价单的本质是挂单价为0的限价单，挂单为0的订单不可能展示在盘口中，所以市价单没有对手盘时会被撤单
     *
     * @param order
     */
    public void add(ExOrder order) {
        if (!sameSide(order.getSide())) {
            throw new IllegalArgumentException("新添加订单方向必须与本盘口方向一致");
        }
        addLimitOrder(order);
    }

    /**
     * 获取OrderBook队首的第一个订单
     *  买盘：返回价格最大订单
     *  买盘：返回价格最小订单
     */
    public Optional<ExOrder> getTopOrder() {
        if (this.limitBook.isEmpty()) {
            return Optional.empty();
        }
        BigDecimal firstPrice = this.limitBook.firstKey();
        LinkedList<ExOrder> firstOrders = limitBook.get(firstPrice);
        if (firstOrders.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(firstOrders.get(0));
    }

    private boolean sameSide(Side side) {
        return getSide().equals(side);
    }

    /**
     * 从OrderBook中移除订单，订单异常时调用
     */
    public void removeOrder(ExOrder order) {
        if (isEmpty()) {
            return;
        }
        BigDecimal priceLevel = order.getPrice();
        LinkedList<ExOrder> priceOrders = this.limitBook.get(order.getPrice());
        if (CollectionUtils.isEmpty(priceOrders)) {
            return;
        }
        //removeIf 远比迭代器高效
        priceOrders.removeIf(itemOrder -> itemOrder.getId().equals(order.getId()));
        if (priceOrders.size() > 0) {
            this.limitBook.put(priceLevel, priceOrders);
            return;
        }
        //删除该订单后，如果该深度没有订单了，该深度也要删除掉
        this.limitBook.remove(priceLevel);
    }

    /**
     * 更新order自身属性：已成交量，已成交金额，平均价
     *
     * @param topLimitOrder limitBook中的order（被动单，maker）
     * @param trade         主动单（taker）
     */
    public void changeOrderItself(final ExOrder topLimitOrder, final ExTrade trade) {
        BigDecimal tradeBaseVolume = trade.getVolume();
        BigDecimal tradeQuoteAmount = tradeBaseVolume.multiply(trade.getPrice());
        topLimitOrder.addFilledQuantity(tradeBaseVolume);
        topLimitOrder.addFilledMoney(tradeQuoteAmount);
        BigDecimal orderAvgPrice = topLimitOrder.getDealMoney().divide(topLimitOrder.getDealVolume(), 12, BigDecimal.ROUND_HALF_EVEN);
        topLimitOrder.setAvgPrice(orderAvgPrice);
    }

    /**
     * 在内存中处理被动单，更新本次成交部分
     *
     * @param matching
     */
    public ExOrder changeMakerOrder(final ExTrade matching) {
        BigDecimal tradedQuantity = matching.getVolume();
        checkMatchingVolume(tradedQuantity);
        if (this.limitBook.isEmpty()) {
            throw new IllegalStateException("无法匹配到更新单");
        }
        //获取OrderBook队首价格
        BigDecimal topPriceLevel = limitBook.firstKey();
        //OrderBook队首价格必须和成交价格一致
        Preconditions.checkState(topPriceLevel.equals(matching.getPrice()), "成交订单到价格必须与盘口中的一档价格一致");
        LinkedList<ExOrder> topLimitOrders = limitBook.get(topPriceLevel);
        if (topLimitOrders.isEmpty()) {
            return null;
        }
        //获取队首订单
        ExOrder topLimitOrder = topLimitOrders.peek();
        //队首订单数量和匹配成交数量比较，比较值对应于：-1 队首数量<成交数量， 0 相等， 1 队首数量>成交数量
        int compareRet = topLimitOrder.getUnfilledQuantity().compareTo(tradedQuantity);
        checkMatchingVolume(compareRet);
        //该价位订单是否只有一个订单
        boolean priceLevelCompletelyFilled = topLimitOrders.size() == 1;
        this.changeOrderItself(topLimitOrder, matching);
        if (!matchingVolumeEqualsTopOrderVolume(compareRet)) {
            return topLimitOrder;
        }
        //如果队首订单只有一个且被成交，需要把该价格档位删除
        if (priceLevelCompletelyFilled) {
            limitBook.remove(topPriceLevel);
            return topLimitOrder;
        }
        topLimitOrders.poll();
        limitBook.put(topPriceLevel, topLimitOrders);
        return topLimitOrder;
    }


    public Optional<ExOrder> getOrderById(Long OrderId, BigDecimal limitPrice) {
        if (isEmpty()) {
            return Optional.empty();
        }
        LinkedList<ExOrder> limitOrders = limitBook.get(limitPrice);
        if (CollectionUtils.isEmpty(limitOrders)) {
            return Optional.empty();
        }
        for (ExOrder itemOrder : limitOrders) {
            if (itemOrder.getId().equals(OrderId)) {
                return Optional.of(itemOrder);
            }
        }
        return Optional.empty();
    }

    @Override
    public Iterator<ExOrder> iterator() {
        class MyIterator implements Iterator<ExOrder> {
            BigDecimal priceLevel;

            /** 一个priceLevel对应的List<Order>中的位置 */
            int index;

            public MyIterator() {
                index = 0;
                if (limitBook.size() > 0) {
                    priceLevel = limitBook.firstKey();
                }
            }

            @Override
            public boolean hasNext() {
                if (priceLevel != null && limitBook.containsKey(priceLevel)) {
                    List<ExOrder> orderList = limitBook.get(priceLevel);
                    if (index < orderList.size()) {
                        return true;
                    }
                }
                return false;
            }

            @Override
            public ExOrder next() {
                if (hasNext()) {
                    List<ExOrder> orderList = limitBook.get(priceLevel);
                    int orderSize = orderList.size();
                    if (index < orderSize) {
                        ExOrder o = orderList.get(index);
                        if (index == orderSize - 1) {
                            priceLevel = limitBook.higherKey(priceLevel);
                            index = 0;
                        } else {
                            index++;
                        }
                        return o;
                    } else {
                        return null;
                    }
                }

                return null;
            }
        }

        return new MyIterator();
    }
}
