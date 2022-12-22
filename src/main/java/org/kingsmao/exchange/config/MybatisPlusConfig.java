package org.kingsmao.exchange.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.handler.TableNameHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.DynamicTableNameInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.google.common.collect.Maps;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class MybatisPlusConfig {
    public final static ThreadLocal<String> dynamicTableName = new ThreadLocal<>();

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();

        DynamicTableNameInnerInterceptor dynamicTableNameInnerInterceptor = new DynamicTableNameInnerInterceptor();
        Map<String, TableNameHandler> tableNameHandlerMap = Maps.newHashMap();
        tableNameHandlerMap.put("ex_order", new DynamicTableNameHandler());
        tableNameHandlerMap.put("ex_trade", new DynamicTableNameHandler());
        dynamicTableNameInnerInterceptor.setTableNameHandlerMap(tableNameHandlerMap);

        mybatisPlusInterceptor.addInnerInterceptor(dynamicTableNameInnerInterceptor);
        mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        return mybatisPlusInterceptor;
    }

    /**
     * 订单表动态表明设置
     */
    class DynamicTableNameHandler implements TableNameHandler {

        @Override
        public String dynamicTableName(String sql, String tableName) {
            return tableName+"_" + dynamicTableName.get();
        }
    }
}
