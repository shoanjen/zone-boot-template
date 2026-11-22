/**
 * 防腐层接口（外部系统契约）
 *
 * <p>第三方服务（支付、短信、微信等）在此定义领域视角的接口，
 * 隔离外部模型对领域的侵蚀；实现放 {@code com.zone.boot.infrastructure.gateway}
 * （出站 HTTP 统一走 ZoneHttpTemplate）。
 */
package com.zone.boot.domain.gateway;
