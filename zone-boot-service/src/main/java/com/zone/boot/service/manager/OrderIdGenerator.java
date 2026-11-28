package com.zone.boot.service.manager;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 订单号生成器（示例链路演示用 · 写侧链路 · manager 通用业务组件）
 * 可复用业务能力示范：生成规则集中一处、跨用例复用（下单/补录/导入等）；
 * manager 包只依赖 domain 与 common —— 封装第三方协议/中间件的能力一律下沉 infrastructure.gateway；
 * 类名业务贴合（OrderIdGenerator），不用 XxxManager 泛称
 *
 * @author ZoneBoot
 * @since 2026-11-28
 */
@Component
public class OrderIdGenerator {

    private static final DateTimeFormatter DATE = DateTimeFormatter.BASIC_ISO_DATE;
    /** 进程内自增序号（示例用；真实项目按号段/雪花等分布式方案实现） */
    private static final AtomicInteger SEQUENCE = new AtomicInteger(0);

    /**
     * 生成下一订单号：ORDER-yyyyMMdd-三位序号
     */
    public String nextOrderId() {
        return "ORDER-" + LocalDate.now().format(DATE) + "-" + String.format("%03d", SEQUENCE.incrementAndGet());
    }
}
