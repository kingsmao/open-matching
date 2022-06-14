package org.kingsmao.exchange.service.impl;

import org.kingsmao.exchange.entity.ExTrade;
import org.kingsmao.exchange.repository.ExTradeMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.kingsmao.exchange.service.IExTradeService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * BTC_USDT 成交记录 服务实现类
 * </p>
 *
 * @author QX
 * @since 2022-06-13
 */
@Service
public class ExTradeServiceImpl extends ServiceImpl<ExTradeMapper, ExTrade> implements IExTradeService {

}
