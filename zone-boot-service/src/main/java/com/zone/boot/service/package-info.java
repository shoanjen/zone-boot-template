/**
 * DDD 应用层（用例编排）
 *
 * <p>一个应用服务 = 一个用例：组织仓储/网关接口（domain 定义）完成业务流程、
 * 管理事务边界、DTO↔领域对象转换。规则：
 * <ul>
 *   <li>禁止依赖 zone-boot-infrastructure 与 MyBatis-Plus（ArchUnit 守护）——只依赖领域接口</li>
 *   <li>返回 DTO/VO，禁止返回 ZoneResult 等接入层协议（统一响应体由 Controller 组装）</li>
 *   <li>业务规则尽量下沉实体/领域服务，应用层只做编排</li>
 * </ul>
 */
package com.zone.boot.service;
