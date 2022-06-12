package org.kingsmao.exchange.disruptor;

import com.google.common.collect.Maps;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.dsl.ProducerType;
import lombok.extern.log4j.Log4j2;
import org.anair.disruptor.DefaultDisruptorConfig;
import org.anair.disruptor.EventHandlerChain;
import org.anair.disruptor.WaitStrategyType;
import org.apache.commons.lang3.Validate;
import org.kingsmao.exchange.disruptor.order.event.OrderEvent;
import org.kingsmao.exchange.disruptor.order.event.OrderEventFactory;
import org.kingsmao.exchange.disruptor.order.processor.MatchProcessor;
import org.kingsmao.exchange.disruptor.order.processor.ValidateProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Log4j2
@Component
public class DisruptorFactory {

    private static final int RINGBUFFER_SIZE = 1024;

    public static final Map<String, DefaultDisruptorConfig<OrderEvent>> SymbolDisruptorFactory = Maps.newConcurrentMap();

    @Autowired
    private MatchProcessor matchProcessor;

    @Autowired
    private ValidateProcessor validateProcessor;
    /**
     * 订单输入处理链
     */
    public void initOrderDisruptor(String symbol) {
        Validate.notBlank(symbol);
        DefaultDisruptorConfig<OrderEvent> defaultDisruptorConfig = SymbolDisruptorFactory.get(symbol);
        if (null != defaultDisruptorConfig) {
            return;
        }
        EventHandlerChain<OrderEvent> eventHandlerChain =
                new EventHandlerChain(new EventHandler[]{validateProcessor}, new EventHandler[]{matchProcessor});
        defaultDisruptorConfig = new DefaultDisruptorConfig();
        defaultDisruptorConfig.setEventHandlerChain(new EventHandlerChain[]{eventHandlerChain});
        defaultDisruptorConfig.setRingBufferSize(RINGBUFFER_SIZE);
        defaultDisruptorConfig.setProducerType(ProducerType.SINGLE); //每个币对用单线程发布订单
        defaultDisruptorConfig.setEventFactory(new OrderEventFactory());
        defaultDisruptorConfig.setThreadName("Order_".concat(symbol));
        defaultDisruptorConfig.setWaitStrategyType(WaitStrategyType.BLOCKING);
        defaultDisruptorConfig.init();
        SymbolDisruptorFactory.put(symbol,defaultDisruptorConfig);
    }

    public static DefaultDisruptorConfig<OrderEvent> getDefaultDisruptorConfig(String symbol){
        Validate.notBlank(symbol);
        return SymbolDisruptorFactory.get(symbol);
    }


}
