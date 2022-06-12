package org.kingsmao.exchange.disruptor.order.publisher;

import lombok.extern.log4j.Log4j2;
import org.anair.disruptor.DefaultDisruptorConfig;
import org.kingsmao.exchange.disruptor.DisruptorFactory;
import org.kingsmao.exchange.disruptor.order.event.OrderEvent;
import org.kingsmao.exchange.disruptor.order.translator.OrderEventTranslator;
import org.kingsmao.exchange.entity.Order;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class OrderEventPublisher {

    public void publish(String symbol, Order order){
        log.info("OrderEventPublisher::publish ---> symbol:{},order:{}",symbol,order);
        DefaultDisruptorConfig<OrderEvent> defaultDisruptorConfig = DisruptorFactory.getDefaultDisruptorConfig(symbol);
        OrderEvent orderEvent = new OrderEvent();
        orderEvent.setOrder(order);
        defaultDisruptorConfig.publish(new OrderEventTranslator(orderEvent));
    }
}
