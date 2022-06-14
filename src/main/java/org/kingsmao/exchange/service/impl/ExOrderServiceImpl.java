package org.kingsmao.exchange.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.kingsmao.exchange.config.MybatisPlusConfig;
import org.kingsmao.exchange.entity.ExOrder;
import org.kingsmao.exchange.repository.ExOrderMapper;
import org.kingsmao.exchange.service.IExOrderService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 委托订单 服务实现类
 * </p>
 */
@Service
public class ExOrderServiceImpl extends ServiceImpl<ExOrderMapper, ExOrder> implements IExOrderService {

    @Override
    public Long getLastOrderId(String symbol) {
        MybatisPlusConfig.dynamicTableName.set(symbol.toLowerCase());
        LambdaQueryWrapper<ExOrder> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.last("order by id desc limit 1");
        ExOrder order = this.getOne(queryWrapper);
        return null == order ? null : order.getId();
    }
}
