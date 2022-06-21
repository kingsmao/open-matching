package org.kingsmao.exchange.entity;

import lombok.Data;
import org.kingsmao.exchange.enums.Side;

import java.math.BigDecimal;

@Data
public class RemainOrder {
    private Boolean marketOrder;
    private Long oid;
    private Long uid;
    private BigDecimal remainAmount;
    private Side side;

}
