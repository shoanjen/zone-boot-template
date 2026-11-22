package com.zone.boot.domain.entity;

import com.zone.boot.domain.valueobject.Money;
import com.zone.boot.domain.valueobject.OrderStatus;

import java.util.Objects;

/**
 * 订单实体（示例链路演示用）
 * 充血模型：不变式校验（应付不超总额、状态/金额非空）与业务判断（canPay）内聚在实体，
 * 应用服务只编排不写规则；纯 POJO，不依赖任何框架
 *
 * @author ZoneBoot
 * @since 2026-11-22
 */
public class Order {

    private final String orderId;
    private final String userId;
    private final OrderStatus status;
    /** 订单总额 */
    private final Money totalAmount;
    /** 应付金额（优惠后） */
    private final Money payableAmount;
    /** 下单时间（epoch 毫秒） */
    private final long createdTime;

    private Order(String orderId, String userId, OrderStatus status,
                  Money totalAmount, Money payableAmount, long createdTime) {
        this.orderId = requireText(orderId, "orderId 不能为空");
        this.userId = requireText(userId, "userId 不能为空");
        this.status = Objects.requireNonNull(status, "订单状态不能为空");
        this.totalAmount = Objects.requireNonNull(totalAmount, "订单总额不能为空");
        this.payableAmount = Objects.requireNonNull(payableAmount, "应付金额不能为空");
        if (payableAmount.isGreaterThan(totalAmount)) {
            throw new IllegalArgumentException("应付金额不能超过订单总额："
                    + payableAmount.toYuanDisplay() + " > " + totalAmount.toYuanDisplay());
        }
        this.createdTime = createdTime;
    }

    public static Order of(String orderId, String userId, OrderStatus status,
                           Money totalAmount, Money payableAmount, long createdTime) {
        return new Order(orderId, userId, status, totalAmount, payableAmount, createdTime);
    }

    /** 领域行为：当前是否可发起支付（状态规则在枚举里，实体做语义收口） */
    public boolean canPay() {
        return status.canPay();
    }

    public String getOrderId() {
        return orderId;
    }

    public String getUserId() {
        return userId;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public Money getTotalAmount() {
        return totalAmount;
    }

    public Money getPayableAmount() {
        return payableAmount;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    private static String requireText(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value;
    }
}
