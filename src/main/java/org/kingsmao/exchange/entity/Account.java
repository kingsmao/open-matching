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
 * 账户明细
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Account implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * UID，10000以内保留，作为公司账户
     */
    private Integer uid;

    /**
     * 类型
     */
    private Integer type;

    /**
     * 余额
     */
    private BigDecimal balance;

    /**
     * 标签，冗余，只帮助直观反馈
     */
    private String tag;

    /**
     * 创建时间
     */
    private LocalDateTime ctime;

    /**
     * 修改时间
     */
    private LocalDateTime mtime;


}
