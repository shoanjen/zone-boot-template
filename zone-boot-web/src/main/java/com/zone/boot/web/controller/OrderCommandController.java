package com.zone.boot.web.controller;

import com.zone.boot.service.request.OrderCreateReq;
import com.zone.boot.service.request.PayOrderReq;
import com.zone.boot.service.response.OrderCreateRes;
import com.zone.boot.service.response.PayOrderRes;
import com.zone.boot.service.service.OrderCommandAppService;
import com.zone.middleware.common.result.ZoneResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 订单命令接口（示例链路演示用 · 写侧链路）
 * Controller 只做三件事：接参数（@Valid 触发校验）→ 调应用服务 → 包 ZoneResult；
 * 写链路示范：manager 通用组件 / 领域服务 / 防腐网关 / 领域事件 / 编程式事务全链路
 *
 * @author ZoneBoot
 * @since 2026-11-28
 */
@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderCommandController {

    private final OrderCommandAppService orderCommandAppService;

    /**
     * 创建订单
     * POST /api/v1/orders
     */
    @PostMapping
    public ZoneResult<OrderCreateRes> create(@RequestBody @Valid OrderCreateReq request) {
        return ZoneResult.ok(orderCommandAppService.createOrder(request));
    }

    /**
     * 支付订单
     * POST /api/v1/orders/pay
     */
    @PostMapping("/pay")
    public ZoneResult<PayOrderRes> pay(@RequestBody @Valid PayOrderReq request) {
        return ZoneResult.ok(orderCommandAppService.payOrder(request));
    }
}
