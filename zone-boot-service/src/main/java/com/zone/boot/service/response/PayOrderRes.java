package com.zone.boot.service.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * 支付订单响应（示例链路演示用 · 写侧链路）
 * 由支付领域事件（实体 pay() 产出）组装，金额/时间为展示加工字段
 *
 * @author ZoneBoot
 * @since 2026-11-28
 */
@Data
@Builder
@Schema(description = "支付订单响应")
public class PayOrderRes {

    @Schema(description = "订单号", example = "ORDER-20261121-001")
    private String orderId;

    @Schema(description = "订单状态编码", example = "PAID")
    private String statusName;

    @Schema(description = "订单状态描述", example = "已支付")
    private String statusDescription;

    @Schema(description = "实付金额（元）", example = "189.00")
    private String payableAmountDisplay;

    @Schema(description = "支付时间", example = "2026-11-28 12:00:00")
    private String paidTimeDisplay;
}
