package com.zone.boot.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zone.boot.domain.entity.Order;
import com.zone.boot.domain.repository.OrderRepository;
import com.zone.boot.infrastructure.persistence.dataobject.OrderDO;
import com.zone.boot.infrastructure.persistence.mapper.OrderMapper;
import com.zone.boot.infrastructure.persistence.translator.OrderTranslator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 订单仓储实现（示例链路演示用 · 真实库版）
 * 实现 domain.repository.OrderRepository 接口（依赖倒置：接口在 domain、实现在 infra、装配根注入）：
 * 编排 Mapper（取数）+ Translator（DO↔实体互转），方法签名只出现领域对象；
 * 派生项目照此「Mapper + Translator + 仓储实现」三件套模式接入自己的表
 *
 * @author ZoneBoot
 * @since 2026-11-28
 */
@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderMapper orderMapper;
    private final OrderTranslator orderTranslator;

    @Override
    public Optional<Order> findById(String orderId) {
        OrderDO orderDO = orderMapper.selectById(orderId);
        return Optional.ofNullable(orderDO).map(orderTranslator::toEntity);
    }

    @Override
    public List<Order> findByUserId(String userId) {
        return orderMapper.selectList(new LambdaQueryWrapper<OrderDO>()
                        .eq(OrderDO::getUserId, userId)
                        .orderByDesc(OrderDO::getCreatedTime))
                .stream()
                .map(orderTranslator::toEntity)
                .toList();
    }

    @Override
    public Order save(Order order) {
        OrderDO orderDO = orderTranslator.toDO(order);
        if (orderMapper.selectById(order.getOrderId()) == null) {
            orderMapper.insert(orderDO);
        } else {
            orderMapper.updateById(orderDO);
        }
        return order;
    }
}
