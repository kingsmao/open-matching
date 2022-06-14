package org.kingsmao.exchange;

import lombok.extern.log4j.Log4j2;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Log4j2
@MapperScan("org.kingsmao.exchange.repository")
@SpringBootApplication
class SpringBootApplicationTests {


}
