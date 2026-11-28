package com.zone.boot.domain.gateway;

/**
 * 用户信息防腐网关接口（示例链路演示用 · 写侧链路；依赖倒置锚点）
 * 外部用户服务能力经本接口进入应用层，方法签名只出现领域概念；
 * 实现由 {@code com.zone.boot.infrastructure.gateway} 提供（真实实现出站走 ZoneHttpTemplate，
 * 模板内置 mock 实现供演示）
 *
 * @author ZoneBoot
 * @since 2026-11-28
 */
public interface UserGateway {

    /**
     * 判断用户是否存在（下单前校验）
     *
     * @param userId 用户ID
     * @return 存在返回 true
     */
    boolean exists(String userId);
}
