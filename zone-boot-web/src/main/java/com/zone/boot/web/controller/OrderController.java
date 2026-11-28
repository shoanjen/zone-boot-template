package com.zone.boot.web.controller;

import com.zone.boot.service.service.OrderQueryAppService;
import com.zone.boot.service.request.OrderListReq;
import com.zone.boot.service.request.OrderQueryReq;
import com.zone.boot.service.response.OrderDetailRes;
import com.zone.boot.service.response.OrderSummaryRes;
import com.zone.middleware.common.result.ZoneResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 订单查询接口（示例链路演示用）
 * Controller 只做三件事：接参数（@Valid 触发校验）→ 调应用服务 → 包 ZoneResult；
 * 不写业务、不做 DTO→VO 转换（应用服务直接返回 Res）
 *
 * @author ZoneBoot
 * @since 2026-11-22
 */
@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderQueryAppService orderQueryAppService;

    /**
     * 订单详情
     * GET /api/v1/orders/detail?orderId=ORDER-20261121-001
     */
    @GetMapping("/detail")
    public ZoneResult<OrderDetailRes> orderDetail(@Valid OrderQueryReq request) {
        return ZoneResult.ok(orderQueryAppService.getOrderDetail(request));
    }

    /**
     * 用户订单列表
     * GET /api/v1/orders/list?userId=user-1001
     */
    @GetMapping("/list")
    public ZoneResult<List<OrderSummaryRes>> orderList(@Valid OrderListReq request) {
        return ZoneResult.ok(orderQueryAppService.listUserOrders(request));
    }
}
