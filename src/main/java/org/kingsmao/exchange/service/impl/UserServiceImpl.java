package org.kingsmao.exchange.service.impl;

import org.kingsmao.exchange.entity.User;
import org.kingsmao.exchange.repository.UserMapper;
import org.kingsmao.exchange.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
