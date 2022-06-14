package org.kingsmao.exchange.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.kingsmao.exchange.entity.ExOrder;

/**
 * <p>
 * 委托订单 服务类
 * </p>
 */
public interface IExOrderService extends IService<ExOrder> {

    Long getLastOrderId(String symbol);



}
