package com.zone.boot.domain.repository;

import com.zone.boot.domain.entity.Greeting;

import java.util.Optional;

/**
 * 问候语仓储接口（示例链路演示用；依赖倒置锚点）
 * 实现由 infrastructure.persistence.repository 提供，bootstrap 装配期注入；
 * 方法签名只出现领域对象，不出现 DO/Wrapper 等持久化类型
 *
 * @author ZoneBoot
 * @since 2026-11-21
 */
public interface GreetingRepository {

    /**
     * 取默认问候模板
     *
     * @return 默认问候语，不存在返回 empty
     */
    Optional<Greeting> findDefault();
}
