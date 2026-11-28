-- 示例链路建表迁移（V1）：t_order 订单表
-- 派生项目：删除本脚本后，将自己的建表脚本按 V1__xxx.sql 序号放入本目录；
-- 已有数据的库请加 spring.flyway.baseline-on-migrate=true
CREATE TABLE IF NOT EXISTS `t_order`
(
    `order_id`           VARCHAR(64) NOT NULL COMMENT '订单号（业务主键，应用层生成）',
    `user_id`            VARCHAR(64) NOT NULL COMMENT '用户ID',
    `status`             VARCHAR(32) NOT NULL COMMENT '订单状态：CREATED/PAID/SHIPPED/COMPLETED/CANCELLED',
    `total_amount_fen`   BIGINT      NOT NULL COMMENT '订单总额（分）',
    `payable_amount_fen` BIGINT      NOT NULL COMMENT '应付金额（分）',
    `created_time`       BIGINT      NOT NULL COMMENT '下单时间（epoch 毫秒）',
    PRIMARY KEY (`order_id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT = '订单表（示例链路）';
