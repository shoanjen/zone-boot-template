package com.zone.boot.domain.event;

import com.zone.boot.domain.valueobject.Money;

/**
 * 订单已支付领域事件（示例链路演示用 · 写侧链路）
 * 表达「订单已完成支付」这一领域事实：实体 pay() 行为产出，应用服务在事务提交后发布；
 * 只携带领域概念（纯 Java record），不携带框架类型，发布/订阅机制由应用层与基础设施实现
 *
 * @param orderId       订单号
 * @param userId        用户ID
 * @param payableAmount 实付金额
 * @param paidTime      支付时间（epoch 毫秒）
 * @author ZoneBoot
 * @since 2026-11-28
 */
public record OrderPaidEvent(String orderId, String userId, Money payableAmount, long paidTime) {
}
