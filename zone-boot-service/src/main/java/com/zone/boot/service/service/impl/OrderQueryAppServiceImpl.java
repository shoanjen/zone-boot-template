package com.zone.boot.service.service.impl;

import com.zone.boot.domain.entity.Order;
import com.zone.boot.domain.repository.OrderRepository;
import com.zone.boot.service.converter.OrderConverter;
import com.zone.boot.service.request.OrderListReq;
import com.zone.boot.service.request.OrderQueryReq;
import com.zone.boot.service.response.OrderDetailRes;
import com.zone.boot.service.response.OrderSummaryRes;
import com.zone.boot.service.service.OrderQueryAppService;
import com.zone.middleware.common.error.BusinessError;
import com.zone.middleware.common.exception.ZoneBusinessException;
import com.zone.middleware.log.ZoneLog;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 订单查询应用服务实现（示例链路演示用）
 * 实现 service 包的顶层接口 {@link OrderQueryAppService}；
 * 查询侧用例编排（detail / list 两个用例；写侧见 {@code OrderCommandAppService}，读写职责分开）：
 * 查仓储接口 → 领域行为参与装配 → Converter 出 VO；事务边界归应用层（写路径用编程式
 * TransactionTemplate，本类只读链路不包事务），
 * 只依赖 domain 接口（OrderRepository），实现由 bootstrap 装配注入
 *
 * @author ZoneBoot
 * @since 2026-11-22
 */
@Service
@RequiredArgsConstructor
public class OrderQueryAppServiceImpl implements OrderQueryAppService {

    private final OrderRepository orderRepository;
    private final OrderConverter orderConverter;

    /** 用例一：订单详情（不存在 → 业务异常由全局异常处理器统一转 ZoneResult） */
    @Override
    public OrderDetailRes getOrderDetail(OrderQueryReq request) {
        ZoneLog.info("OrderQueryAppServiceImpl", "getOrderDetail", "订单详情查询：orderId={}", request.getOrderId());
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new ZoneBusinessException(BusinessError.DATA_NOT_FOUND, "订单不存在"));
        OrderDetailRes result = orderConverter.toDetailRes(order);
        ZoneLog.info("OrderQueryAppServiceImpl", "getOrderDetail", "订单详情查询完成：orderId={}, status={}",
                result.getOrderId(), result.getStatusName());
        return result;
    }

    /** 用例二：用户订单列表（无订单返回空列表，非异常） */
    @Override
    public List<OrderSummaryRes> listUserOrders(OrderListReq request) {
        ZoneLog.info("OrderQueryAppServiceImpl", "listUserOrders", "订单列表查询：userId={}", request.getUserId());
        List<OrderSummaryRes> orders = orderRepository.findByUserId(request.getUserId()).stream()
                .map(orderConverter::toSummaryRes)
                .toList();
        ZoneLog.info("OrderQueryAppServiceImpl", "listUserOrders", "订单列表查询完成：userId={}, count={}", request.getUserId(), orders.size());
        return orders;
    }
}
