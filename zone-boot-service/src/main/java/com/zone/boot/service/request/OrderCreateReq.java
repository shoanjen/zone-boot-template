package com.zone.boot.service.request;

import com.zone.middleware.common.request.BaseRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 创建订单请求（示例链路演示用 · 写侧链路）
 * 接口契约对象：校验注解在此，Controller 用 @Valid 触发；金额以「分」承载（与 Money 值对象同口径）；
 * 通用契约字段继承二方包 BaseRequest
 *
 * @author ZoneBoot
 * @since 2026-11-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "创建订单请求")
public class OrderCreateReq extends BaseRequest {

    @Schema(description = "用户ID", example = "user-1001")
    @NotBlank(message = "用户ID不能为空")
    private String userId;

    @Schema(description = "单价（分）", example = "9900")
    @NotNull(message = "单价不能为空")
    @Positive(message = "单价必须为正数")
    private Long unitAmountFen;

    @Schema(description = "数量", example = "2")
    @NotNull(message = "数量不能为空")
    @Positive(message = "数量必须为正数")
    private Integer quantity;

    @Schema(description = "优惠金额（分，可空默认 0）", example = "1000")
    @PositiveOrZero(message = "优惠金额不允许为负数")
    private Long discountAmountFen;
}
