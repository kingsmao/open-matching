package org.kingsmao.exchange.service;

import org.kingsmao.exchange.entity.ConfigSymbol;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 币对配置表 服务类
 * </p>
 */
public interface IConfigSymbolService extends IService<ConfigSymbol> {

    List<ConfigSymbol> getOpenSymbol();
}
