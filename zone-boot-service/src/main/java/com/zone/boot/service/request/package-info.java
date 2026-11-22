/**
 * 应用层入参 DTO（Req）
 *
 * <p>接口契约对象，携带校验注解（jakarta.validation）与文档注解（@Schema），
 * 由 Controller 接参后原样传给应用服务 —— 不再做一层 request→command 转换。
 * 通用契约字段（reqId/platform/ip/deviceId/os/version/timestamp）继承二方包
 * {@code com.zone.middleware.common.request.BaseRequest}（zone-middleware-common，
 * 经 zone-middleware-log 传递；sign 字段已废弃，签名经请求头承载）。
 */
package com.zone.boot.service.request;
