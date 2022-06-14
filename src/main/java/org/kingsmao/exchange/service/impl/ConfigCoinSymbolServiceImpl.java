package org.kingsmao.exchange.service.impl;

import org.kingsmao.exchange.entity.ConfigCoinSymbol;
import org.kingsmao.exchange.repository.ConfigCoinSymbolMapper;
import org.kingsmao.exchange.service.IConfigCoinSymbolService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 币种配置表 服务实现类
 * </p>
 */
@Service
public class ConfigCoinSymbolServiceImpl extends ServiceImpl<ConfigCoinSymbolMapper, ConfigCoinSymbol> implements IConfigCoinSymbolService {

}
