/**
 * 外部网关实现（防腐层实现）
 *
 * <p>实现 {@code com.zone.boot.domain.gateway} 接口：出站 HTTP 统一走
 * ZoneHttpTemplate（zone-middleware-http），外部响应模型在此翻译为领域概念，
 * 禁止外泄给领域层。
 */
package com.zone.boot.infrastructure.gateway;
