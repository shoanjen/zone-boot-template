package com.zone.boot.domain.valueobject;

/**
 * 订单状态枚举（示例链路演示用）
 * 业务判断（能否支付/是否终态）内聚在枚举行为里，不散落在服务层 if-else
 */
public enum OrderStatus {

    CREATED("已创建"),
    PAID("已支付"),
    SHIPPED("已发货"),
    COMPLETED("已完成"),
    CANCELLED("已取消");

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }

    /** 中文描述（出参组装用） */
    public String getDescription() {
        return description;
    }

    /** 领域规则：仅「已创建」可发起支付 */
    public boolean canPay() {
        return this == CREATED;
    }

    /** 领域规则：终态订单不再流转 */
    public boolean isTerminal() {
        return this == COMPLETED || this == CANCELLED;
    }
}
