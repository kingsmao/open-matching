package org.kingsmao.exchange.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 币种配置表
 * </p>
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ConfigCoinSymbol implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 币种简称，大写：BTC
     */
    private String coinSymbol;

    private LocalDateTime ctime;

    private LocalDateTime mtime;

    /**
     * 是否开启；0关闭，1开启
     */
    private Integer status;


}
