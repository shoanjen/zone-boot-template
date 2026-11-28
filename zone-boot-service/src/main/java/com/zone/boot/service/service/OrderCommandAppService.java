package com.zone.boot.service.service;

import com.zone.boot.service.request.OrderCreateReq;
import com.zone.boot.service.request.PayOrderReq;
import com.zone.boot.service.response.OrderCreateRes;
import com.zone.boot.service.response.PayOrderRes;

/**
 * 订单命令应用服务接口（示例链路演示用 · 写侧链路）
 * 应用层契约：顶层接口放 service 包，实现放 service.impl 子包（{@code OrderCommandAppServiceImpl}，事务边界在实现）；
 * 写侧双用例（创建 / 支付），查询侧另见 {@link OrderQueryAppService}（读写职责分开）
 *
 * @author ZoneBoot
 * @since 2026-11-28
 */
public interface OrderCommandAppService {

    /**
     * 创建订单用例：校验用户（防腐网关）→ 领域服务计价 → 实体不变式守卫 → 仓储保存
     *
     * @param request 创建请求
     * @return 创建结果（含新订单号）
     */
    OrderCreateRes createOrder(OrderCreateReq request);

    /**
     * 支付订单用例：实体领域行为 pay()（状态流转守卫 + 事件产出）→ 仓储保存 → 事务提交后发布事件
     *
     * @param request 支付请求
     * @return 支付结果
     */
    PayOrderRes payOrder(PayOrderReq request);
}
