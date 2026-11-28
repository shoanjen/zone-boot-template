/**
 * 应用服务顶层接口（应用层契约）
 *
 * <p>一个接口 = 一类用例的契约（Query / Command 读写分开）；实现统一放
 * {@code com.zone.boot.service.impl}（XxxAppServiceImpl，用例编排 + 事务边界）。
 * 上层（Controller）只依赖本包接口 —— 面向接口编程，便于 mock 单测与实现替换。
 */
package com.zone.boot.service.service;
