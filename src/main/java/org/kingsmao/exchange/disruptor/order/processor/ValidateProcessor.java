package org.kingsmao.exchange.disruptor.order.processor;

import com.lmax.disruptor.EventHandler;
import lombok.extern.log4j.Log4j2;
import org.kingsmao.exchange.disruptor.order.event.OrderEvent;
import org.kingsmao.exchange.disruptor.order.event.OrderEventType;
import org.kingsmao.exchange.service.OrderValidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 订单输入前置校验器
 */
@Log4j2
@Component
public class ValidateProcessor implements EventHandler<OrderEvent> {

    @Autowired
    private OrderValidateService orderValidateService;

    @Override
    public void onEvent(OrderEvent orderEvent, long sequence, boolean endOfBatch) throws Exception {
        log.info("ValidateProcessor::onEvent ---> orderEvent:{}", orderEvent);
        if (needCheck(orderEvent.getOrderEventType())) {
            if (!orderValidateService.validate(orderEvent)) {
                //log.warn("ValidateProcessor 检验失败 {}", orderEvent);
            }
        }
        orderEvent.setPipelineLog(orderEvent.getPipelineLog() + " | Validate");
    }

    /**
     * 异常单和重建k线事件不需要校验
     */
    private static boolean needCheck(OrderEventType orderEventType){
        return !OrderEventType.INVALID.equals(orderEventType) && !OrderEventType.REFRESH_REBUILD.equals(orderEventType);
    }
}
