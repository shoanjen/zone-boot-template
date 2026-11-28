package com.zone.boot.infrastructure.persistence.dataobject;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 订单 DO（示例链路演示用 · 真实库版）
 * t_order 表映射对象：MyBatis-Plus 注解只允许出现在 infrastructure 的 DO 上，
 * 领域实体（com.zone.boot.domain.entity.Order）不沾任何持久化痕迹；
 * DO 与实体的互转收敛在 persistence.translator.OrderTranslator
 *
 * @author ZoneBoot
 * @since 2026-11-28
 */
@Data
@TableName("t_order")
public class OrderDO {

    /** 订单号（业务主键，由应用层 OrderIdGenerator 生成后传入，非自增） */
    @TableId(value = "order_id", type = IdType.INPUT)
    private String orderId;

    /** 用户ID */
    private String userId;

    /** 订单状态：CREATED/PAID/SHIPPED/COMPLETED/CANCELLED */
    private String status;

    /** 订单总额（分） */
    private Long totalAmountFen;

    /** 应付金额（分） */
    private Long payableAmountFen;

    /** 下单时间（epoch 毫秒） */
    private Long createdTime;
}
