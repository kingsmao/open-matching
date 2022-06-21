package org.kingsmao.exchange.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.kingsmao.exchange.entity.ExOrder;

import java.util.List;

/**
 * <p>
 * 委托订单 Mapper 接口
 * </p>
 */
public interface ExOrderMapper extends BaseMapper<ExOrder> {

    int updateOrderStatusToCancel(@Param("tableName") String tableName);


}
