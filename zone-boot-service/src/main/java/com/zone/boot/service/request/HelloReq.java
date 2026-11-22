package com.zone.boot.service.request;

import com.zone.middleware.common.request.BaseRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 问候请求（示例链路演示用）
 * 接口契约对象：校验注解在此，Controller 用 @Valid 触发；
 * 通用契约字段继承二方包 BaseRequest
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "问候请求")
public class HelloReq extends BaseRequest {

    @Schema(description = "访客名", example = "Zone")
    @NotBlank(message = "访客名不能为空")
    private String visitorName;
}
