package org.kingsmao.exchange.service.impl;

import org.kingsmao.exchange.entity.Account;
import org.kingsmao.exchange.repository.AccountMapper;
import org.kingsmao.exchange.service.IAccountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 账户明细 服务实现类
 * </p>
 */
@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements IAccountService {

}
