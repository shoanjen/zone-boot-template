package com.zone.boot.service.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * 创建订单响应（示例链路演示用 · 写侧链路）
 * 应用服务组装并返回给 Controller 的视图对象，金额/时间为展示加工字段
 *
 * @author ZoneBoot
 * @since 2026-11-28
 */
@Data
@Builder
@Schema(description = "创建订单响应")
public class OrderCreateRes {

    @Schema(description = "订单号", example = "ORDER-20261128-001")
    private String orderId;

    @Schema(description = "订单状态编码", example = "CREATED")
    private String statusName;

    @Schema(description = "订单状态描述", example = "已创建")
    private String statusDescription;

    @Schema(description = "订单总额（元）", example = "198.00")
    private String totalAmountDisplay;

    @Schema(description = "应付金额（元）", example = "188.00")
    private String payableAmountDisplay;

    @Schema(description = "下单时间", example = "2026-11-28 12:00:00")
    private String createdTimeDisplay;
}
