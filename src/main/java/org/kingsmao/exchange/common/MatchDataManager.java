package org.kingsmao.exchange.common;

import com.google.common.collect.Maps;
import lombok.Data;
import org.apache.rocketmq.common.message.Message;
import org.kingsmao.exchange.entity.ExOrder;
import org.kingsmao.exchange.entity.OrderBook;
import org.kingsmao.exchange.entity.SymbolConfig;
import org.kingsmao.exchange.enums.Side;
import org.roaringbitmap.RoaringBitmap;
import org.roaringbitmap.longlong.Roaring64NavigableMap;

import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 撮合数据管理工具
 */
@Data
public class MatchDataManager {

    private static final Map<String, MatchDataManager> MATCH_DATA_MANAGER = Maps.newConcurrentMap();

    private String symbol;

    /**
     * 设置 ThreadMonitor 线程监控的起始时间
     */
    private Long threadMonitor;

    /**
     * 撮合通用配置
     */
    private SymbolConfig symbolConfig;

    /**
     * 买盘盘口
     */
    private OrderBook orderBookBuy;

    /**
     * 卖盘盘口
     */
    private OrderBook orderBookSell;

    /**
     * 撮合在MQ中的消费位点
     */
    private Long lastOffset;

    /**
     * 存放订单
     */
    private TreeSet<ExOrder> roundOrders;

    /**
     * 每次拉取订单的起始订单,分页使用 数据库加载时用id分页
     */
    private Long loadInitOffset;

    /**
     * 去重队列，存放 orderId 用于去重
     */
    private RoaringBitmap roaringBitmap1;
    private Roaring64NavigableMap roaringBitmap;

    /**
     * 取消订单集合
     */
    private ConcurrentHashMap cancelMap;

    /**
     * 1: 表示已经重建；0：表示重建还未结束
     */
    private Integer rebuildEnd;

    /**
     * 终止撮合的标志位
     * true: 表示持续撮合；false：表示停止撮合
     */
    private Boolean matchingFlag;

    /**
     * 给高频发送消息使用的消息队列
     */
    //private ConcurrentLinkedDeque<AllTypeMessage> highFrequencyQueue;

    //private DefaultDisruptorConfig<InputEvent> inputDisruptor;
    //private DefaultDisruptorConfig<OutputEvent> outputDisruptor;

    /**
     * 撮合output消息先积攒，然后批量发送
     */
    private List<Message> batchMsgs;

    /**
     * 开启启动一个币对的撮合时，初始化前置数据
     *
     * @param symbol 撮合币对
     * @return
     */
    public static void init(String symbol) {
        MatchDataManager manager = new MatchDataManager();
        manager.setSymbolConfig(new SymbolConfig());
        manager.setOrderBookBuy(new OrderBook(Side.BUY));
        manager.setOrderBookSell(new OrderBook(Side.SELL));
        manager.setRoundOrders(new TreeSet<>(new OrderOffsetComparator()));
        manager.setLoadInitOffset(0L);
        manager.setLastOffset(0L);
        manager.setRebuildEnd(0);
        manager.setRoaringBitmap(new Roaring64NavigableMap());
        manager.setCancelMap(new ConcurrentHashMap());
        set(symbol, manager);
    }

    private static void set(String symbol, MatchDataManager manager) {
        MATCH_DATA_MANAGER.put(symbol, manager);
    }

    public static MatchDataManager get(String symbol) {
        return MATCH_DATA_MANAGER.get(symbol);
    }

    public static void remove(String symbol) {
        MATCH_DATA_MANAGER.entrySet().removeIf(entry -> entry.getKey().equals(symbol));
    }

    public void addBatchMsg(Message msg) {
        this.batchMsgs.add(msg);
    }

    public void clearBatchMsgs() {
        this.batchMsgs.clear();
    }

    /**
     * 弹出本轮订单队首订单
     */
    public ExOrder pollFirstOrder() {
        if (roundOrders.isEmpty()) {
            return null;
        }
        return roundOrders.pollFirst();
    }

    /**
     * 检测新进入orderbook的订单是否重复，如果之前不包含就加进去
     *
     * @return true 重复，false 没有重复
     */
    public boolean isRepeat(long orderId) {
        if (roaringBitmap.contains(orderId)) {
            return Boolean.TRUE;
        }
        roaringBitmap.addLong(orderId);
        return Boolean.FALSE;
    }
}
