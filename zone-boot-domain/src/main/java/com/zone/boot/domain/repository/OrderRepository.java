package com.zone.boot.domain.repository;

import com.zone.boot.domain.entity.Order;

import java.util.List;
import java.util.Optional;

/**
 * 订单仓储接口（示例链路演示用；依赖倒置锚点）
 * 实现由 infrastructure.persistence.repository 提供，bootstrap 装配期注入；
 * 方法签名只出现领域对象（含 List/Optional 集合语义），不出现 DO/Wrapper 等持久化类型
 *
 * @author ZoneBoot
 * @since 2026-11-22
 */
public interface OrderRepository {

    /** 按订单号取单笔订单，不存在返回 empty */
    Optional<Order> findById(String orderId);

    /** 按用户取订单列表（按下单时间倒序），无订单返回空列表 */
    List<Order> findByUserId(String userId);

    /** 保存订单（新增或按订单号覆盖更新），返回保存后的领域对象 */
    Order save(Order order);
}
