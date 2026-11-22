/**
 * 仓储接口（依赖倒置锚点）
 *
 * <p>面向领域概念定义持久化契约（方法签名只出现实体/值对象，不出现 DO/Wrapper 等持久化类型）；
 * 实现类放 {@code com.zone.boot.infrastructure.persistence.repository}，由装配期注入。
 * 应用服务只依赖此包接口 —— 这是本模板依赖倒置的核心。
 */
package com.zone.boot.domain.repository;
