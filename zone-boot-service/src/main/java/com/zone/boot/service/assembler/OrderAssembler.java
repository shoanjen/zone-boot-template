package com.zone.boot.service.assembler;

import com.zone.boot.domain.entity.Order;
import com.zone.boot.service.response.OrderDetailRes;
import com.zone.boot.service.response.OrderSummaryRes;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * 订单装配器（示例链路演示用）
 * 领域对象 → 出参 Res 的转换收敛在此（无状态）；同一实体按用例裁剪出
 * 详情全量视图 / 列表瘦视图 两种 VO；与 infrastructure 的 DO↔实体 Converter 严格分开
 */
@Component
public class OrderAssembler {

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

    private String formatTime(long epochMillis) {
        return Instant.ofEpochMilli(epochMillis).atZone(ZoneId.systemDefault()).format(TIME_FORMATTER);
    }
}
