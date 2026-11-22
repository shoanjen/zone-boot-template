/**
 * DDD 适配层 - REST Controller
 *
 * <p>Controller 只做三件事：接参数（触发校验）、调应用服务（zone-boot-service）、
 * 组返回（ZoneResult + HTTP 语义）。红线（ArchUnit 守护）：
 * <ul>
 *   <li>字段只允许注入应用服务，禁止注入仓储/Mapper/DO 等基础设施组件</li>
 *   <li>不写业务逻辑、不做 DTO→VO 转换（应用服务直接返回 Res）</li>
 * </ul>
 */
package com.zone.boot.web.controller;
