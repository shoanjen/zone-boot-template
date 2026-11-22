package com.zone.boot.service.request;

import com.zone.middleware.common.request.BaseRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户订单列表查询请求（示例链路演示用）
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "用户订单列表查询请求")
public class OrderListReq extends BaseRequest {

    @Schema(description = "用户ID", example = "user-1001")
    @NotBlank(message = "用户ID不能为空")
    private String userId;
}
