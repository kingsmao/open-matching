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
     * 获取最大orderId
     *
     * @param symbol 委托币对
     * @return 最大orderId
     */
    Long getLastOrderId(String symbol);

    /**
     * 获取orderId之后未结束的委托单，（不包含orderId）
     *
     * @param symbol  委托币对
     * @param orderId 最大orderId
     * @return 未结束的委托单
     */
    List<ExOrder> loadUnFilledOrder(String symbol, Long orderId);


}
