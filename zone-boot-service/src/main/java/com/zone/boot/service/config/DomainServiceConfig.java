package com.zone.boot.service.config;

import com.zone.boot.domain.service.OrderPricingService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 领域服务装配（示例链路演示用 · 写侧链路）
 * 领域服务是纯 Java 对象（domain 框架无关，不能用 @Service 声明 Bean），
 * 由使用方所在的应用层模块就近注册（本类），应用层经构造器注入使用；
 * 跨模块的装配（聚合 infrastructure 实现等）仍归装配根 bootstrap；
 * 派生项目删除写侧示例时一并删除本类
 *
 * @author ZoneBoot
 * @since 2026-11-28
 */
@Configuration
public class DomainServiceConfig {

    @Bean
    public OrderPricingService orderPricingService() {
        return new OrderPricingService();
    }
}
