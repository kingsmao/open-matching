package org.kingsmao.exchange;

import lombok.extern.log4j.Log4j2;
import org.anair.disruptor.DefaultDisruptorConfig;
import org.kingsmao.exchange.disruptor.DisruptorFactory;
import org.kingsmao.exchange.disruptor.order.event.OrderEvent;
import org.kingsmao.exchange.disruptor.order.publisher.OrderEventPublisher;
import org.kingsmao.exchange.entity.Order;
import org.kingsmao.exchange.service.ExchangeService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@Log4j2
@MapperScan("org.kingsmao.exchange.repository")
@SpringBootApplication
public class OpenMatchingApplication implements ApplicationRunner {

    @Autowired
    ExchangeService exchangeService;

    public static void main(String[] args) {
        SpringApplication.run(OpenMatchingApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("撮合启动！");
        exchangeService.start();
    }
}
