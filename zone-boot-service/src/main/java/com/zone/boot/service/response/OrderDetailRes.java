package com.zone.boot.service.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * 订单详情响应（示例链路演示用）
 * 详情用例的全量视图：字段由 OrderAssembler 从领域对象裁剪组装
 */
@Data
@Builder
@Schema(description = "订单详情响应")
public class OrderDetailRes {

    @Schema(description = "订单号", example = "ORDER-20261121-001")
    private String orderId;

    @Schema(description = "用户ID", example = "user-1001")
    private String userId;

    @Schema(description = "订单状态编码", example = "CREATED")
    private String statusName;

    @Schema(description = "订单状态描述", example = "已创建")
    private String statusDescription;

    @Schema(description = "订单总额（元）", example = "199.00")
    private String totalAmountDisplay;

    @Schema(description = "应付金额（元）", example = "189.00")
    private String payableAmountDisplay;

    @Schema(description = "是否可发起支付")
    private Boolean payable;

    @Schema(description = "下单时间", example = "2026-11-21 20:00:00")
    private String createdTimeDisplay;
}
