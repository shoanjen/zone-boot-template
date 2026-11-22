/**
 * DDD 基础设施层（技术实现）
 *
 * <p>实现领域层定义的仓储/防腐接口：persistence（MyBatis-Plus + DO + 仓储实现 +
 * DO↔实体转换）与 gateway（第三方服务，出站 HTTP 走 ZoneHttpTemplate）。
 * 红线（ArchUnit 守护）：禁止反向依赖 web / service（应用层）。
 */
package com.zone.boot.infrastructure;
