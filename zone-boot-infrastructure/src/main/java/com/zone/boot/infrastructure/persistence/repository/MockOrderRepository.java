package com.zone.boot.infrastructure.persistence.repository;

import com.zone.boot.domain.entity.Order;
import com.zone.boot.domain.repository.OrderRepository;
import com.zone.boot.domain.valueobject.Money;
import com.zone.boot.domain.valueobject.OrderStatus;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 订单仓储实现 —— mock 版（示例链路演示用）
 * 内存 Map 顶替 MySQL，验证「依赖倒置 + 完整链路」而不触库；
 * 派生项目接入真实库时替换为「Mapper（persistence.mapper）+ Converter（persistence.converter）
 * + 本包实现」组合：Mapper 查 DO → Converter 转 Order → 交还应用层
 *
 * @author ZoneBoot
 * @since 2026-11-22
 */
@Repository
public class MockOrderRepository implements OrderRepository {

    /** mock 订单库：orderId → 领域对象（直接以领域形态存储，跳过 DO 转换环节） */
    private static final Map<String, Order> MOCK_ORDERS = new ConcurrentHashMap<>();

    static {
        // 记录锚点：2026-11-21 20:00:00 GMT+8 = 1795262400000
        MOCK_ORDERS.put("ORDER-20261121-001", Order.of(
                "ORDER-20261121-001", "user-1001", OrderStatus.CREATED,
                Money.ofFen(19900), Money.ofFen(18900), 1795262400000L));
        MOCK_ORDERS.put("ORDER-20261120-002", Order.of(
                "ORDER-20261120-002", "user-1001", OrderStatus.PAID,
                Money.ofFen(5900), Money.ofFen(5900), 1795176000000L));
        MOCK_ORDERS.put("ORDER-20261119-003", Order.of(
                "ORDER-20261119-003", "user-1002", OrderStatus.CANCELLED,
                Money.ofFen(12800), Money.ofFen(12800), 1795089600000L));
    }

    @Override
    public Optional<Order> findById(String orderId) {
        return Optional.ofNullable(MOCK_ORDERS.get(orderId));
    }

    @Override
    public List<Order> findByUserId(String userId) {
        return MOCK_ORDERS.values().stream()
                .filter(order -> order.getUserId().equals(userId))
                .sorted(Comparator.comparingLong(Order::getCreatedTime).reversed())
                .toList();
    }
}
