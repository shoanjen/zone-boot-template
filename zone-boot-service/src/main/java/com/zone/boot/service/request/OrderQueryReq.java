package com.zone.boot.service.request;

import com.zone.middleware.common.request.BaseRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 订单详情查询请求（示例链路演示用）
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "订单详情查询请求")
public class OrderQueryReq extends BaseRequest {

    @Schema(description = "订单号", example = "ORDER-20261121-001")
    @NotBlank(message = "订单号不能为空")
    private String orderId;
}
