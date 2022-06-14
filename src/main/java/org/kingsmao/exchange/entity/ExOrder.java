package org.kingsmao.exchange.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 委托订单
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("ex_order")
public class ExOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Integer userId;

    /**
     * 买卖方向
     */
    private String side;

    /**
     * 限价单挂单价格
     */
    private BigDecimal price;

    /**
     * 挂单总数量
     */
    private BigDecimal volume;

    /**
     * 挂单手续费率
     */
    private Double feeRateMaker;

    /**
     * 吃单手续费率
     */
    private Double feeRateTaker;

    /**
     * 手续费
     */
    private BigDecimal fee;

    /**
     * 成交数量
     */
    private BigDecimal dealVolume;

    /**
     * 已成交金额
     */
    private BigDecimal dealMoney;

    /**
     * 成交均价
     */
    private BigDecimal avgPrice;

    /**
     * 订单状态：0 init，1 new，2 filled，3 part_filled，4 canceled，5 pending_cancel，6 expired
     */
    private Integer status;

    /**
     * 委托类型：1 limit，2 market，3 stop
     */
    private Integer type;

    /**
     * 创建时间
     */
    private LocalDateTime ctime;

    /**
     * 更新时间
     */
    private LocalDateTime mtime;

    /**
     * 订单来源：1web，2app，3api
     */
    private Integer source;

    /**
     * 订单类型1:常规订单，2 杠杆订单
     */
    private Integer orderType;


}
