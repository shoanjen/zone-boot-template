package com.zone.boot.infrastructure.persistence.translator;

import com.zone.boot.domain.entity.Order;
import com.zone.boot.domain.valueobject.Money;
import com.zone.boot.domain.valueobject.OrderStatus;
import com.zone.boot.infrastructure.persistence.dataobject.OrderDO;
import org.springframework.stereotype.Component;

/**
 * 订单 Translator（示例链路演示用 · 真实库版）
 * DO ↔ 领域实体的双向翻译：金额「分」字段 ↔ Money 值对象、状态字符串 ↔ OrderStatus 枚举等
 * 持久化格式差异在此消化，保证领域实体不沾任何持久化痕迹（无状态）；
 * 与应用层 converter（领域对象 → 出参 Res）严格分工，互不代做
 *
 * @author ZoneBoot
 * @since 2026-11-28
 */
@Component
public class OrderTranslator {

    /** DO → 领域实体 */
    public Order toEntity(OrderDO orderDO) {
        return Order.of(orderDO.getOrderId(), orderDO.getUserId(),
                OrderStatus.valueOf(orderDO.getStatus()),
                Money.ofFen(orderDO.getTotalAmountFen()), Money.ofFen(orderDO.getPayableAmountFen()),
                orderDO.getCreatedTime());
    }

    /** 领域实体 → DO */
    public OrderDO toDO(Order order) {
        OrderDO orderDO = new OrderDO();
        orderDO.setOrderId(order.getOrderId());
        orderDO.setUserId(order.getUserId());
        orderDO.setStatus(order.getStatus().name());
        orderDO.setTotalAmountFen(order.getTotalAmount().amountFen());
        orderDO.setPayableAmountFen(order.getPayableAmount().amountFen());
        orderDO.setCreatedTime(order.getCreatedTime());
        return orderDO;
    }
}
