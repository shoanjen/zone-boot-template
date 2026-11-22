package com.zone.boot.web.controller;

import com.zone.boot.service.HelloAppService;
import com.zone.boot.service.request.HelloReq;
import com.zone.boot.service.response.HelloRes;
import com.zone.middleware.common.result.ZoneResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 问候接口（示例链路演示用）
 * Controller 只做三件事：接参数（@Valid 触发校验）→ 调应用服务 → 包 ZoneResult；
 * 不写业务、不做 DTO→VO 转换（应用服务直接返回 Res）
 *
 * @author ZoneBoot
 * @since 2026-11-21
 */
@RestController
@RequestMapping("/api/v1/hello")
@RequiredArgsConstructor
public class HelloController {

    private final HelloAppService helloAppService;

    /**
     * 问候接口
     * GET /api/v1/hello?visitorName=Zone
     */
    @GetMapping
    public ZoneResult<HelloRes> greet(@Valid HelloReq request) {
        return ZoneResult.ok(helloAppService.greet(request));
    }
}
