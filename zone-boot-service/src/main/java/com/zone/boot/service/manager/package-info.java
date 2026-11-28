/**
 * 通用业务组件（Manager）
 *
 * <p>可复用的业务能力组件（如业务编号生成、金额分摊、通用业务校验），
 * 与具体用例解耦，供本层应用服务复用。规则：
 * <ul>
 *   <li>只依赖 domain（仓储/网关接口、领域服务、值对象）与 common —— 禁止触达
 *       infrastructure 与中间件能力包（ArchUnit 守护，与应用服务同边界）</li>
 *   <li>封装第三方协议 / 出站 HTTP / Redis 的能力一律下沉 infrastructure.gateway（防腐实现）</li>
 *   <li>类名业务贴合（如 OrderIdGenerator），避免 XxxManager 泛称泛滥</li>
 * </ul>
 */
package com.zone.boot.service.manager;
