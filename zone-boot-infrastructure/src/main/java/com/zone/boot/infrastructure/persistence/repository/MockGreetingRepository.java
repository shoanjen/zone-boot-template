package com.zone.boot.infrastructure.persistence.repository;

import com.zone.boot.domain.entity.Greeting;
import com.zone.boot.domain.repository.GreetingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 问候语仓储实现 —— mock 版（示例链路演示用）
 * 返回固定 mock 数据，不触达 MySQL；派生项目接入真实库时替换为
 * 「Mapper（persistence.mapper）+ Converter（persistence.converter）+ 本包实现」组合
 *
 * @author ZoneBoot
 * @since 2026-11-21
 */
@Repository
public class MockGreetingRepository implements GreetingRepository {

    @Override
    public Optional<Greeting> findDefault() {
        Greeting mockGreeting = Greeting.of(
                "greeting-001",
                "Hello, {name}!（本条数据来自 infrastructure mock，未查询数据库）");
        return Optional.of(mockGreeting);
    }
}
