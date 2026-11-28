package com.zone.boot.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zone.boot.infrastructure.persistence.dataobject.OrderDO;

/**
 * 订单 Mapper（示例链路演示用 · 真实库版）
 * 继承 MyBatis-Plus BaseMapper 获得通用 CRUD；主类 @MapperScan 扫描本包注册，
 * 复杂查询可写本接口方法 + src/main/resources/mapper 下的 XML
 *
 * @author ZoneBoot
 * @since 2026-11-28
 */
public interface OrderMapper extends BaseMapper<OrderDO> {
}
