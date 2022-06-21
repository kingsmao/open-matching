package org.kingsmao.exchange.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.kingsmao.exchange.entity.ExOrder;

import java.util.List;

/**
 * <p>
 * 委托订单 服务类
 * </p>
 */
public interface IExOrderService extends IService<ExOrder> {

    /**
     * 获取第一个未结束订单id
     *
     * @param symbol 委托币对
     */
    Long getFirstUnFilledOrderId(String symbol);

    /**
     * 获取orderId之后未结束的委托单，（不包含orderId）
     *
     * @param symbol  委托币对
     * @param orderId 最大orderId
     * @return 未结束的委托单
     */
    List<ExOrder> loadUnFilledOrder(String symbol, Long orderId);

    /**
     * 将撮合未启动状态下，下的订单取消
     *
     * @param symbol 委托币对
     */
    void cancelUnLoadOrder(String symbol);



}
