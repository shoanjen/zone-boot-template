/**
 * 领域实体（充血模型）
 *
 * <p>实体 = 身份 + 状态 + 业务行为：业务规则（校验、状态流转、计算）写成实体方法，
 * 而不是散落在应用服务里操作纯数据袋。仅可用 lombok；禁止任何持久化注解
 * （@TableName/@TableField 等属于 infrastructure 的 DO）。
 */
package com.zone.boot.domain.entity;
