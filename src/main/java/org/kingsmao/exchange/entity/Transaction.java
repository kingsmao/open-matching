package org.kingsmao.exchange.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 交易流水
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Transaction implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 转出账户ID
     */
    private Integer fromUid;

    /**
     * 转出账户type
     */
    private Integer fromType;

    /**
     * 转出后账户余额
     */
    private BigDecimal fromBalance;

    /**
     * 转入账户ID
     */
    private Integer toUid;

    /**
     * 转入账户type
     */
    private Integer toType;

    /**
     * 转入后账户余额
     */
    private BigDecimal toBalance;

    /**
     * 发生额
     */
    private BigDecimal amount;

    private String meta;

    /**
     * 场景，用于连接上下文
     */
    private String scene;

    /**
     * 转账时涉及的主业务表名，或者特性名，不一定准确
     */
    private String refType;

    /**
     * 转账时涉及的主业务表ID
     */
    private Long refId;

    /**
     * 创建时间
     */
    private LocalDateTime ctime;

    /**
     * 修改时间
     */
    private LocalDateTime mtime;

    /**
     * 防篡改指纹
     */
    private String fingerprint;


}
