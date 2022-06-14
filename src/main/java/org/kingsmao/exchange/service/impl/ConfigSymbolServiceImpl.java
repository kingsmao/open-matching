package org.kingsmao.exchange.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.math.NumberUtils;
import org.kingsmao.exchange.entity.ConfigSymbol;
import org.kingsmao.exchange.repository.ConfigSymbolMapper;
import org.kingsmao.exchange.service.IConfigSymbolService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 币对配置表 服务实现类
 * </p>
 */
@Service
public class ConfigSymbolServiceImpl extends ServiceImpl<ConfigSymbolMapper, ConfigSymbol> implements IConfigSymbolService {

    @Override
    public List<ConfigSymbol> getOpenSymbol() {
        LambdaQueryWrapper<ConfigSymbol> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(ConfigSymbol::getIsOpen, NumberUtils.INTEGER_ONE);
        return this.list(queryWrapper);
    }
}
