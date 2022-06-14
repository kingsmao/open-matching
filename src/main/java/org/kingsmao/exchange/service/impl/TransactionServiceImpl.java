package org.kingsmao.exchange.service.impl;

import org.kingsmao.exchange.entity.Transaction;
import org.kingsmao.exchange.repository.TransactionMapper;
import org.kingsmao.exchange.service.ITransactionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 交易流水 服务实现类
 * </p>
 */
@Service
public class TransactionServiceImpl extends ServiceImpl<TransactionMapper, Transaction> implements ITransactionService {

}
