package com.zone.boot.service.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * 订单列表项响应（示例链路演示用）
 * 列表用例的瘦视图：同一 Order 实体在列表场景只暴露列表页需要的字段——
 * 裁剪逻辑在 OrderConverter，不另建「列表专用领域模型」
 */
@Data
@Builder
@Schema(description = "订单列表项响应")
public class OrderSummaryRes {

    @Schema(description = "订单号", example = "ORDER-20261121-001")
    private String orderId;

    @Schema(description = "订单状态编码", example = "CREATED")
    private String statusName;

    @Schema(description = "应付金额（元）", example = "189.00")
    private String payableAmountDisplay;
}
