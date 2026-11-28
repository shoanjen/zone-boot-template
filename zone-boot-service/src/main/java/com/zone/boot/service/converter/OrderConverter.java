package com.zone.boot.service.converter;

import com.zone.boot.domain.entity.Order;
import com.zone.boot.domain.event.OrderPaidEvent;
import com.zone.boot.domain.valueobject.OrderStatus;
import com.zone.boot.service.response.OrderCreateRes;
import com.zone.boot.service.response.OrderDetailRes;
import com.zone.boot.service.response.OrderSummaryRes;
import com.zone.boot.service.response.PayOrderRes;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * 订单转换器（示例链路演示用）
 * 领域对象 → 出参 Res 的转换收敛在此（无状态）；同一实体按用例裁剪出
 * 详情全量视图 / 列表瘦视图 两种 VO；与 infrastructure 的 DO↔实体 Translator 严格分开
 */
@Component
public class OrderConverter {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /** 详情用例：全量视图（含状态描述、金额展示、能否支付等面向展示的加工字段） */
    public OrderDetailRes toDetailRes(Order order) {
        return OrderDetailRes.builder()
                .orderId(order.getOrderId())
                .userId(order.getUserId())
                .statusName(order.getStatus().name())
                .statusDescription(order.getStatus().getDescription())
                .totalAmountDisplay(order.getTotalAmount().toYuanDisplay())
                .payableAmountDisplay(order.getPayableAmount().toYuanDisplay())
                .payable(order.canPay())
                .createdTimeDisplay(formatTime(order.getCreatedTime()))
                .build();
    }

    /** 列表用例：瘦视图（仅列表页字段） */
    public OrderSummaryRes toSummaryRes(Order order) {
        return OrderSummaryRes.builder()
                .orderId(order.getOrderId())
                .statusName(order.getStatus().name())
                .payableAmountDisplay(order.getPayableAmount().toYuanDisplay())
                .build();
    }

    /** 创建用例：新建订单 → 创建响应 */
    public OrderCreateRes toCreateRes(Order order) {
        return OrderCreateRes.builder()
                .orderId(order.getOrderId())
                .statusName(order.getStatus().name())
                .statusDescription(order.getStatus().getDescription())
                .totalAmountDisplay(order.getTotalAmount().toYuanDisplay())
                .payableAmountDisplay(order.getPayableAmount().toYuanDisplay())
                .createdTimeDisplay(formatTime(order.getCreatedTime()))
                .build();
    }

    /** 支付用例：支付事件（实体 pay() 产出）→ 支付响应 */
    public PayOrderRes toPayRes(OrderPaidEvent event) {
        return PayOrderRes.builder()
                .orderId(event.orderId())
                .statusName(OrderStatus.PAID.name())
                .statusDescription(OrderStatus.PAID.getDescription())
                .payableAmountDisplay(event.payableAmount().toYuanDisplay())
                .paidTimeDisplay(formatTime(event.paidTime()))
                .build();
    }

    private String formatTime(long epochMillis) {
        return Instant.ofEpochMilli(epochMillis).atZone(ZoneId.systemDefault()).format(TIME_FORMATTER);
    }
}
