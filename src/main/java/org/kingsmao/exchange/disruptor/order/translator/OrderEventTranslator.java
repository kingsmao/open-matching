package org.kingsmao.exchange.disruptor.order.translator;

import com.lmax.disruptor.EventTranslator;
import lombok.extern.slf4j.Slf4j;
import org.kingsmao.exchange.disruptor.order.event.OrderEvent;

/**
 * ringbuffer接收的是EventTranslator,外部传入的event需要封装一下
 */
@Slf4j
public class OrderEventTranslator implements EventTranslator<OrderEvent> {

    private OrderEvent orderEvent;

    public OrderEventTranslator(OrderEvent orderEvent) {
        this.orderEvent = orderEvent;
    }

    /**
     * 外部传入的Event对象会经过一层包装，最终成为Ringbuffer处理的对象
     * @param orderEvent
     * @param sequence
     */
    @Override
    public void translateTo(OrderEvent orderEvent, long sequence) {
        log.info("OrderEventTranslator::translateTo ---> orderEvent={},sequence:{}",orderEvent,sequence);
        orderEvent.clean();
        orderEvent.setExecutedOrder(this.orderEvent.getExecutedOrder());
        orderEvent.setPipelineLog(this.orderEvent.getPipelineLog());
        orderEvent.setMessage(this.orderEvent.getMessage());
        orderEvent.setSymbol(this.orderEvent.getSymbol());
        orderEvent.setOrderEventType(this.orderEvent.getOrderEventType());
    }
}
