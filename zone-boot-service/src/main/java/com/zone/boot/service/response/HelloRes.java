package com.zone.boot.service.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * 问候响应（示例链路演示用）
 * 应用服务直接组装返回；Controller 只包 ZoneResult，不做任何转换
 */
@Data
@Builder
@Schema(description = "问候响应")
public class HelloRes {

    @Schema(description = "问候语ID", example = "greeting-001")
    private String greetingId;

    @Schema(description = "渲染后的问候语", example = "Hello, Zone!")
    private String greetingMessage;
}
