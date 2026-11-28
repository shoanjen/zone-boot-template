package com.zone.boot.service.service.impl;

import com.zone.boot.domain.entity.Order;
import com.zone.boot.domain.event.OrderPaidEvent;
import com.zone.boot.domain.gateway.UserGateway;
import com.zone.boot.domain.repository.OrderRepository;
import com.zone.boot.domain.service.OrderPricingService;
import com.zone.boot.domain.valueobject.Money;
import com.zone.boot.domain.valueobject.OrderStatus;
import com.zone.boot.service.converter.OrderConverter;
import com.zone.boot.service.manager.OrderIdGenerator;
import com.zone.boot.service.request.OrderCreateReq;
import com.zone.boot.service.request.PayOrderReq;
import com.zone.boot.service.response.OrderCreateRes;
import com.zone.boot.service.response.PayOrderRes;
import com.zone.boot.service.service.OrderCommandAppService;
import com.zone.middleware.common.error.BusinessError;
import com.zone.middleware.common.exception.ZoneBusinessException;
import com.zone.middleware.log.ZoneLog;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * 订单命令应用服务实现（示例链路演示用 · 写侧链路）
 * 实现 service 包的顶层接口 {@link OrderCommandAppService}；
 * 读写职责分开：本类承载写侧用例（创建/支付），查询侧见 {@code OrderQueryAppService}；
 * 事务边界在实现 —— 编程式 TransactionTemplate 只包写库原子片段（禁 @Transactional，
 * 见 zone-coding-standards skill Spring Boot 惯例三），事件发布在事务提交之后（消息不进事务边界）；
 * 只依赖 domain 接口与领域服务（装配根注入），manager 通用组件负责订单号生成
 *
 * @author ZoneBoot
 * @since 2026-11-28
 */
@Service
@RequiredArgsConstructor
public class OrderCommandAppServiceImpl implements OrderCommandAppService {

    private final OrderRepository orderRepository;
    private final UserGateway userGateway;
    private final OrderIdGenerator orderIdGenerator;
    private final OrderPricingService orderPricingService;
    private final OrderConverter orderConverter;
    private final TransactionTemplate transactionTemplate;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * 创建订单用例：校验用户（防腐网关）→ 领域服务计价 → 实体不变式守卫 → 仓储保存
     */
    @Override
    public OrderCreateRes createOrder(OrderCreateReq request) {
        ZoneLog.info("OrderCommandAppServiceImpl", "createOrder", "创建订单：userId={}, unitAmountFen={}, quantity={}, discountAmountFen={}",
                request.getUserId(), request.getUnitAmountFen(), request.getQuantity(), request.getDiscountAmountFen());
        if (!userGateway.exists(request.getUserId())) {
            throw new ZoneBusinessException(BusinessError.DATA_NOT_FOUND, "用户不存在：" + request.getUserId());
        }
        Order order = transactionTemplate.execute(status -> {
            Money totalAmount = Money.ofFen(request.getUnitAmountFen()).multiply(request.getQuantity());
            Money discountAmount = Money.ofFen(request.getDiscountAmountFen() == null ? 0L : request.getDiscountAmountFen());
            Order created = Order.of(orderIdGenerator.nextOrderId(), request.getUserId(),
                    OrderStatus.CREATED, totalAmount,
                    orderPricingService.calcPayable(totalAmount, discountAmount),
                    System.currentTimeMillis());
            return orderRepository.save(created);
        });
        ZoneLog.info("OrderCommandAppServiceImpl", "createOrder", "创建订单完成：orderId={}, payable={}",
                order.getOrderId(), order.getPayableAmount().toYuanDisplay());
        return orderConverter.toCreateRes(order);
    }

    /**
     * 支付订单用例：实体领域行为 pay()（状态流转守卫 + 事件产出）→ 仓储保存 → 事务提交后发布事件
     */
    @Override
    public PayOrderRes payOrder(PayOrderReq request) {
        ZoneLog.info("OrderCommandAppServiceImpl", "payOrder", "支付订单：orderId={}", request.getOrderId());
        OrderPaidEvent event = transactionTemplate.execute(status -> {
            Order order = orderRepository.findById(request.getOrderId())
                    .orElseThrow(() -> new ZoneBusinessException(BusinessError.DATA_NOT_FOUND, "订单不存在"));
            try {
                OrderPaidEvent paidEvent = order.pay(System.currentTimeMillis());
                orderRepository.save(order);
                return paidEvent;
            } catch (IllegalStateException ex) {
                // 实体状态守卫异常 → 应用层翻译为业务异常（二方包全局异常处理器转业务码）
                throw new ZoneBusinessException(BusinessError.DATA_CONFLICT, ex.getMessage());
            }
        });
        eventPublisher.publishEvent(event);
        ZoneLog.info("OrderCommandAppServiceImpl", "payOrder", "支付订单完成：orderId={}, payable={}",
                event.orderId(), event.payableAmount().toYuanDisplay());
        return orderConverter.toPayRes(event);
    }
}
