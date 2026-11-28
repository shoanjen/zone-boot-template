package com.zone.boot.service.request;

import com.zone.middleware.common.request.BaseRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 支付订单请求（示例链路演示用 · 写侧链路）
 * 接口契约对象：校验注解在此，Controller 用 @Valid 触发；通用契约字段继承二方包 BaseRequest
 *
 * @author ZoneBoot
 * @since 2026-11-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "支付订单请求")
public class PayOrderReq extends BaseRequest {

    @Schema(description = "订单号", example = "ORDER-20261121-001")
    @NotBlank(message = "订单号不能为空")
    private String orderId;
}
