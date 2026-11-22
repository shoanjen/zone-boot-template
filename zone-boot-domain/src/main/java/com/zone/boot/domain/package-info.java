/**
 * DDD 领域层（框架无关）
 *
 * <p>只放业务本质：实体、值对象、领域服务、领域事件、仓储接口、防腐接口。
 * 红线（ArchUnit 守护）：
 * <ul>
 *   <li>禁止依赖 Spring / MyBatis-Plus / Servlet / zone.middleware —— 领域不认识任何框架</li>
 *   <li>持久化与外部系统的实现一律定义接口在此、实现下沉 {@code com.zone.boot.infrastructure}</li>
 * </ul>
 */
package com.zone.boot.domain;
