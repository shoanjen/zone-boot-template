/**
 * DTO / VO 转换器
 *
 * <p>领域对象 → 出参 DTO（含按用例裁剪的多视图）与入参 Req → 领域对象的转换收敛在此
 * （Converter 一律无状态）；DO↔实体的转换则在 infrastructure.persistence.translator，
 * 两类转换器不要混放。
 */
package com.zone.boot.service.converter;
