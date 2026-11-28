/**
 * 应用服务实现（service 包的 impl 子包）
 *
 * <p>实现同模块 {@code com.zone.boot.service.service} 顶层接口：注入仓储/网关**接口**编排用例，
 * 事务边界在此（编程式 TransactionTemplate，只包写路径原子片段，禁 @Transactional）。
 * Controller 只依赖顶层接口，不认识本包实现（装配根运行期注入）。
 */
package com.zone.boot.service.service.impl;
