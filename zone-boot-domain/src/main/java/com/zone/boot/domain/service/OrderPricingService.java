package com.zone.boot.domain.service;

import com.zone.boot.domain.valueobject.Money;

import java.util.Objects;

/**
 * 订单计价领域服务（示例链路演示用 · 写侧链路）
 * 跨值对象的计价规则（应付 = 总额 - 优惠）内聚在领域服务，纯 Java 零框架依赖；
 * 无状态对象 —— domain 不能用 @Service 声明 Bean，由装配根（bootstrap）注册后注入应用服务
 *
 * @author ZoneBoot
 * @since 2026-11-28
 */
public class OrderPricingService {

    /**
     * 计算应付金额：应付 = 总额 - 优惠（优惠不允许超过总额）
     *
     * @param totalAmount    订单总额
     * @param discountAmount 优惠金额
     * @return 应付金额
     */
    public Money calcPayable(Money totalAmount, Money discountAmount) {
        Objects.requireNonNull(totalAmount, "订单总额不能为空");
        Objects.requireNonNull(discountAmount, "优惠金额不能为空");
        if (discountAmount.isGreaterThan(totalAmount)) {
            throw new IllegalArgumentException("优惠金额不能超过订单总额："
                    + discountAmount.toYuanDisplay() + " > " + totalAmount.toYuanDisplay());
        }
        return totalAmount.subtract(discountAmount);
    }
}
