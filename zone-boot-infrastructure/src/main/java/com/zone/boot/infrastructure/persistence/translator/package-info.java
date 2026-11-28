/**
 * DO ↔ 领域实体转换器（Translator）
 *
 * <p>无状态转换；字段映射、枚举/时间戳等持久化格式差异在此消化，
 * 保证领域实体不沾任何持久化痕迹。
 *
 * <p>与应用层 converter（契约对象 ↔ 实体）命名区分：本包只做持久化模型翻译。
 */
package com.zone.boot.infrastructure.persistence.translator;
