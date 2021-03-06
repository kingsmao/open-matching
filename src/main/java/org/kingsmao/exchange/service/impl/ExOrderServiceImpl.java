package org.kingsmao.exchange.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import org.kingsmao.exchange.config.MybatisPlusConfig;
import org.kingsmao.exchange.entity.ExOrder;
import org.kingsmao.exchange.enums.OrderStatus;
import org.kingsmao.exchange.repository.ExOrderMapper;
import org.kingsmao.exchange.service.IExOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.kingsmao.exchange.common.Utils.UN_FILLED_ORDER;

/**
 * <p>
 * 委托订单 服务实现类
 * </p>
 */
@Service
public class ExOrderServiceImpl extends ServiceImpl<ExOrderMapper, ExOrder> implements IExOrderService {

    public static final String ORDER_PREFIX = "ex_order_";

    @Autowired
    private ExOrderMapper orderMapper;

    @Override
    public Long getFirstUnFilledOrderId(String symbol) {
        MybatisPlusConfig.dynamicTableName.set(symbol.toLowerCase());
        LambdaQueryWrapper<ExOrder> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.in(ExOrder::getStatus,UN_FILLED_ORDER);
        queryWrapper.last("order by id asc limit 1");
        ExOrder order = this.getOne(queryWrapper);
        return null == order ? 0L : order.getId() -1L;
    }

    @Override
    public List<ExOrder> loadUnFilledOrder(String symbol, Long orderId) {
        MybatisPlusConfig.dynamicTableName.set(symbol.toLowerCase());
        LambdaQueryWrapper<ExOrder> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.gt(ExOrder::getId,orderId);
        queryWrapper.in(ExOrder::getStatus, UN_FILLED_ORDER);
        queryWrapper.last("limit 5000");
        return this.list(queryWrapper);
    }

    @Override
    public void cancelUnLoadOrder(String symbol) {
        orderMapper.updateOrderStatusToCancel(ORDER_PREFIX + symbol);
    }

}
