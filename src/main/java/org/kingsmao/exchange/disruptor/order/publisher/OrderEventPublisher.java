package org.kingsmao.exchange.disruptor.order.publisher;

import lombok.extern.log4j.Log4j2;
import org.anair.disruptor.DefaultDisruptorConfig;
import org.kingsmao.exchange.disruptor.DisruptorFactory;
import org.kingsmao.exchange.disruptor.order.event.OrderEvent;
import org.kingsmao.exchange.disruptor.order.event.OrderEventType;
import org.kingsmao.exchange.disruptor.order.translator.OrderEventTranslator;
import org.kingsmao.exchange.entity.ExOrder;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class OrderEventPublisher {

    public void publish(String symbol, ExOrder order){
        log.info("OrderEventPublisher::publish ---> symbol:{},order:{}",symbol,order);
        DefaultDisruptorConfig<OrderEvent> defaultDisruptorConfig = DisruptorFactory.getDefaultDisruptorConfig(symbol);
        OrderEvent orderEvent = new OrderEvent();
        orderEvent.setSymbol(symbol);
        orderEvent.setExecutedOrder(order);
        defaultDisruptorConfig.publish(new OrderEventTranslator(orderEvent));
    }

    public void publish(String symbol, OrderEventType orderEventType){
        DefaultDisruptorConfig<OrderEvent> defaultDisruptorConfig = DisruptorFactory.getDefaultDisruptorConfig(symbol);
        OrderEvent orderEvent = new OrderEvent();
        orderEvent.setSymbol(symbol);
        orderEvent.setOrderEventType(orderEventType);
        defaultDisruptorConfig.publish(new OrderEventTranslator(orderEvent));
    }
}
