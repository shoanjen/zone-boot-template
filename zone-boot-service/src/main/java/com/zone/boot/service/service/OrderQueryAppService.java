package com.zone.boot.service.service;

import com.zone.boot.service.request.OrderListReq;
import com.zone.boot.service.request.OrderQueryReq;
import com.zone.boot.service.response.OrderDetailRes;
import com.zone.boot.service.response.OrderSummaryRes;

import java.util.List;

/**
 * 订单查询应用服务接口（示例链路演示用）
 * 应用层契约：顶层接口放 service 包，实现放 service.impl 子包（{@code OrderQueryAppServiceImpl}）；
 * 查询侧双用例（detail / list），写侧另见 {@link OrderCommandAppService}（读写职责分开）
 *
 * @author ZoneBoot
 * @since 2026-11-28
 */
public interface OrderQueryAppService {

    /**
     * 订单详情用例（不存在 → 业务异常）
     *
     * @param request 查询请求
     * @return 订单详情全量视图
     */
    OrderDetailRes getOrderDetail(OrderQueryReq request);

    /**
     * 用户订单列表用例（无订单返回空列表，非异常）
     *
     * @param request 列表请求
     * @return 订单瘦视图列表
     */
    List<OrderSummaryRes> listUserOrders(OrderListReq request);
}
