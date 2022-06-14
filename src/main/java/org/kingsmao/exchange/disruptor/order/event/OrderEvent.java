package org.kingsmao.exchange.disruptor.order.event;

import lombok.Data;
import org.kingsmao.exchange.entity.ExOrder;

@Data
public class OrderEvent {
    /**
     * 输入的执行订单
     */
    private ExOrder executedOrder;

    /**
     * 输入事件类型。通过验证订单确认
     */
    private OrderEventType orderEventType;

    /**
     * 事件类型消息
     */
    private String message = "";

    /**
     * 流水线处理日志
     */
    private String flowLog = "";

    /**
     * 当前线程币对
     */
    private String symbol;

    public void clean(){
        this.executedOrder = null;
        this.orderEventType = null;
        this.message = "";
        this.flowLog = "";
        this.symbol = "";
    }
}
