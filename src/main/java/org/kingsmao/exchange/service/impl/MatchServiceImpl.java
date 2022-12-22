package org.kingsmao.exchange.service.impl;

import lombok.extern.log4j.Log4j2;
import org.kingsmao.exchange.common.OrderBookManager;
import org.kingsmao.exchange.common.Utils;
import org.kingsmao.exchange.entity.ExOrder;
import org.kingsmao.exchange.entity.ExTrade;
import org.kingsmao.exchange.entity.OrderBook;
import org.kingsmao.exchange.entity.SymbolConfig;
import org.kingsmao.exchange.enums.Side;
import org.kingsmao.exchange.service.MatchService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

@Log4j2
@Service
public class MatchServiceImpl implements MatchService {

    private void pushRemainEvent(ExOrder order, BigDecimal remainAmount) {
       /* OutputEvent outputEvent = new OutputEvent();
        outputEvent.setSymbol(order.getSymbol());
        outputEvent.setOutputEventType(OutputEventType.ORDER_REMAIN);
        RemainOrder remainOrder = new RemainOrder();
        remainOrder.setOid(order.getId());
        remainOrder.setUid(order.getUserId());
        remainOrder.setRemainAmount(remainAmount);
        remainOrder.setMarketOrder(order.isMarketOrder());
        remainOrder.setSide(order.getSide());
        outputEvent.setRemainOrder(remainOrder);
        this.outputEventPublisher.publish(outputEvent);*/
    }

    private void returnRemain(ExOrder order) {
        BigDecimal remainAmount;
        if (order.isLimitOrder()) {
            if (order.getSide() == Side.BUY) {
                remainAmount = order.getPrice().multiply(order.getVolume()).subtract(order.getDealMoney());
            } else {
                remainAmount = order.getUnfilledQuantity();
            }
        } else if (order.getSide() == Side.BUY) {
            remainAmount = order.getVolume().subtract(order.getDealMoney());
        } else {
            remainAmount = order.getVolume().subtract(order.getDealVolume());
        }
        remainAmount.setScale(16, 1);
        if (remainAmount.compareTo(BigDecimal.ZERO) > 0) {
            pushRemainEvent(order, remainAmount);
        }
    }

    public void changeTakerOrder(ExOrder order, ExTrade trade) {
        order.addFilledQuantity(trade.getVolume());
        BigDecimal tradeQuoteAmount = trade.getVolume().multiply(trade.getPrice());
        order.addFilledMoney(tradeQuoteAmount);
        BigDecimal orderAvgPrice = order.getDealMoney().divide(order.getDealVolume(), 12, 6);
        order.setAvgPrice(orderAvgPrice);
    }

    /**
     * 组装市价单trade
     */
    private Optional<ExTrade> createMarketTrade(ExOrder order, ExOrder counterOrder, BigDecimal tradePrice) {
        //对手单剩余数量
        BigDecimal counterOrderUnfilledVolume = counterOrder.getUnfilledQuantity();
        //对手单剩余金额
        BigDecimal counterOrderUnfilledAmount = counterOrderUnfilledVolume.multiply(tradePrice);
        ExTrade trade = new ExTrade();
        trade.setTradeNonce(Utils.getGuid());
        trade.setPrice(tradePrice);
        if (Side.BUY.equals(order.getSide())) {
            BigDecimal canTradeVolume, orderUnfilledQuoteAmount = order.getVolume().subtract(order.getDealMoney());
            if (counterOrderUnfilledAmount.compareTo(orderUnfilledQuoteAmount) <= 0) {
                canTradeVolume = counterOrderUnfilledVolume;
            } else {
                canTradeVolume = Utils.divedToPow(orderUnfilledQuoteAmount, tradePrice);
                BigDecimal overReminderVol = Utils.divedReminderPowByConfig(canTradeVolume);
                if (overReminderVol.compareTo(BigDecimal.ZERO) > 0) {
                    canTradeVolume = canTradeVolume.subtract(overReminderVol);
                }
            }
            if (BigDecimal.ZERO.compareTo(canTradeVolume) >= 0) {
                return Optional.empty();
            }
            trade.setVolume(canTradeVolume);
            trade.setBidId(order.getId());
            trade.setBidUserId(order.getUserId());
            trade.setAskId(counterOrder.getId());
            trade.setAskUserId(counterOrder.getUserId());
        } else {
            BigDecimal orderUnfilledVolume = order.getUnfilledQuantity();
            BigDecimal canTradeVolume = orderUnfilledVolume.min(counterOrderUnfilledVolume);
            trade.setVolume(canTradeVolume);
            trade.setBidId(counterOrder.getId());
            trade.setBidUserId(counterOrder.getUserId());
            trade.setAskId(order.getId());
            trade.setAskUserId(order.getUserId());
        }
        trade.setTrendSide(order.getSide());
        trade.setCtime(new Date());
        trade.setBuyFee(BigDecimal.ZERO);
        trade.setSellFee(BigDecimal.ZERO);
        return Optional.of(trade);
    }

    /**
     * 组装现价单trade
     * @param order         委托订单
     * @param counterOrder  盘口中的最佳成交单
     * @param tradePrice    成交价格（来源于counterOrder的价格）
     * @return
     */
    private Optional<ExTrade> createLimitTrade(ExOrder order, ExOrder counterOrder, BigDecimal tradePrice) {
        //待吃单量
        BigDecimal orderQuantity = order.getUnfilledQuantity();
        //对手单剩余数量
        BigDecimal counterOrderQuantity = counterOrder.getUnfilledQuantity();
        //最大成交量
        BigDecimal tradeQuantity = orderQuantity.min(counterOrderQuantity);
        ExTrade trade = new ExTrade();
        trade.setTradeNonce(Utils.getGuid());
        trade.setPrice(tradePrice);
        trade.setVolume(tradeQuantity);
        if (order.getSide() == Side.BUY) {
            trade.setBidId(order.getId());
            trade.setBidUserId(order.getUserId());
            trade.setAskId(counterOrder.getId());
            trade.setAskUserId(counterOrder.getUserId());
        } else {
            trade.setBidId(counterOrder.getId());
            trade.setBidUserId(counterOrder.getUserId());
            trade.setAskId(order.getId());
            trade.setAskUserId(order.getUserId());
        }
        trade.setTrendSide(order.getSide());
        trade.setCtime(new Date());
        trade.setMtime(new Date());
        trade.setBuyFee(BigDecimal.ZERO);
        trade.setSellFee(BigDecimal.ZERO);
        return Optional.of(trade);
    }

    private boolean checkSide(ExOrder order, ExOrder counterOrder) {
        return Side.getOpposite(order.getSide()).equals(counterOrder.getSide());
    }

    private Optional<ExTrade> tryToTrade(ExOrder order, ExOrder counterOrder) {
        //盘口中不可能有存量的市价单
        if (!counterOrder.isLimitOrder()) {
            return Optional.empty();
        }
        BigDecimal counterPrice = counterOrder.getPrice();
        if (order.isLimitOrder()) {
            if (checkSide(order, counterOrder) && order.crossesAt(counterPrice)) {
                return createLimitTrade(order, counterOrder, counterPrice);
            }
            return Optional.empty();
        }
        return createMarketTrade(order, counterOrder, counterPrice);
    }

    private Optional<ExOrder> tryMatch(ExOrder order, OrderBook counterBook) {
        if (order.isFilled()) {
            return Optional.of(order);
        }
        //对手盘中的top订单
        Optional<ExOrder> bestCounterOrderOpt = counterBook.getTopOrder();
        if (!bestCounterOrderOpt.isPresent()) {
            return Optional.of(order);
        }

        ExOrder bestCounterOrder = bestCounterOrderOpt.get();
        Optional<ExTrade> tradeOpt = tryToTrade(order, bestCounterOrder);
        if (!tradeOpt.isPresent()) {
            //匹配不到对手单
            return Optional.of(order);
        }

        ExTrade trade = tradeOpt.get();
        //更新taker主动单（本次委托订单）
        changeTakerOrder(order, trade);
        trade.setTakerOrder(order.clone());
        //更新maker被动单（盘口被吃的订单）
        bestCounterOrder = counterBook.changeMakerOrder(trade);
        trade.setMakerOrder(bestCounterOrder.clone());
        //清算
        //pushTradeEvent(trade);
        //被动单退残渣
        if (bestCounterOrder.getUnfilledQuantity().compareTo(SymbolConfig.MIN_TRADE_VOL) < 0) {
            returnRemain(bestCounterOrder);
        }
        //更新后的委托单会继续进行撮合，直到完全成交
        return tryMatch(order, counterBook);
    }

    /**
     * 获取对手盘
     *
     * @param symbol 撮合币对
     * @param side   挂单方向
     */
    private OrderBook getCounterBook(String symbol, Side side) {
        return (side == Side.BUY) ? OrderBookManager.getOrderBook(symbol, Side.SELL) : OrderBookManager.getOrderBook(symbol, Side.BUY);
    }

    @Override
    public void match(ExOrder order) {
        Side side = order.getSide();
        String symbol = order.getSymbol();
        OrderBook counterBook = getCounterBook(symbol, side);//不可能为空
        //如果匹配不到对手单就返回原始order
        Optional<ExOrder> exOrder = tryMatch(order, counterBook);

    }

    @Override
    public void cancel(ExOrder exOrder) {

    }

    @Override
    public void returnUnFilledMarketOrder(ExOrder exOrder) {

    }

    @Override
    public void exception(ExOrder exOrder) {

    }
}

