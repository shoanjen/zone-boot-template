package com.zone.boot.infrastructure.gateway;

import com.zone.boot.domain.gateway.UserGateway;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 用户网关 mock 实现（示例链路演示用 · 写侧链路）
 * 返回固定名单、不发起网络调用；接入真实用户服务时在 infrastructure.gateway 新增
 * ZoneHttpTemplate 实现（出站 HTTP 统一走 zone-middleware-http，外部响应模型翻译为领域概念，
 * 见二方包接入手册 §8），替换本实现即可 —— domain 与应用层零改动
 *
 * @author ZoneBoot
 * @since 2026-11-28
 */
@Component
public class MockUserGateway implements UserGateway {

    /** mock 用户名单：仅 user-1001 / user-1002 存在 */
    private static final Set<String> EXISTING_USERS = Set.of("user-1001", "user-1002");

    @Override
    public boolean exists(String userId) {
        return userId != null && EXISTING_USERS.contains(userId);
    }
}
