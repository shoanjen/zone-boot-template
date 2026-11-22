/**
 * DTO 装配器
 *
 * <p>领域对象 → 出参 DTO 的转换收敛在此（Assembler 一律无状态）；
 * DO↔实体的转换则在 infrastructure.persistence.converter，两者不要混放。
 */
package com.zone.boot.service.assembler;
