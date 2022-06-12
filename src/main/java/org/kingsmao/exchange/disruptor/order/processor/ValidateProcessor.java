package org.kingsmao.exchange.disruptor.order.processor;

import com.lmax.disruptor.EventHandler;
import lombok.extern.log4j.Log4j2;
import org.kingsmao.exchange.disruptor.order.event.OrderEvent;
import org.kingsmao.exchange.entity.Order;
import org.kingsmao.exchange.service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class ValidateProcessor implements EventHandler<OrderEvent> {

    @Override
    public void onEvent(OrderEvent orderEvent, long sequence, boolean endOfBatch) throws Exception {
      log.info("ValidateProcessor::onEvent ---> orderEvent:{}",orderEvent);
    }
}
