### 2.基于电商交易场景（用户、商品、订单），设计一套简单的表结构，提交 DDL 的 SQL 文件到 Github
```sql
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_user_info
-- ----------------------------
DROP TABLE IF EXISTS `t_user_info`;
CREATE TABLE `t_user_info` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `username` varchar(32) NOT NULL COMMENT '账号',
  `password` varchar(64) DEFAULT NULL COMMENT '密码',
  `real_name` varchar(32) DEFAULT NULL COMMENT '真实姓名',
  `phone` varchar(32) DEFAULT NULL COMMENT '手机',
  `email` varchar(32) DEFAULT NULL COMMENT '邮箱',
  `status` tinyint(1) DEFAULT NULL COMMENT '状态',
  `create_user` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `update_user` bigint(20) DEFAULT NULL COMMENT '更新人',
  `update_time` bigint(20) DEFAULT NULL COMMENT '更新时间',
  `is_delete` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除标志：0 正常，1 已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户信息表';

SET FOREIGN_KEY_CHECKS = 1;

-- ----------------------------
-- Table structure for t_order_info
-- ----------------------------
DROP TABLE IF EXISTS `t_order_info`;
CREATE TABLE `t_order_info` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `no` varchar(32) DEFAULT NULL COMMENT '订单编号',
  `type` tinyint(4) DEFAULT NULL COMMENT '订单类型',
  `status` tinyint(4) DEFAULT NULL COMMENT '状态',
  `goods_snapshot_id` bigint(20) DEFAULT NULL COMMENT '商品快照',
  `uid` bigint(20) DEFAULT NULL COMMENT '用户信息',
  `pay_id` bigint(20) DEFAULT NULL COMMENT '支付信息',
  `desc` varchar(255) DEFAULT NULL COMMENT '描述',
  `create_user` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `update_user` bigint(20) DEFAULT NULL COMMENT '更新人',
  `update_time` bigint(20) DEFAULT NULL COMMENT '更新时间',
  `is_delete` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除标志：0 正常，1 已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='订单基础信息表';

SET FOREIGN_KEY_CHECKS = 1;

-- ----------------------------
-- Table structure for t_goods_info
-- ----------------------------
DROP TABLE IF EXISTS `t_goods_info`;
CREATE TABLE `t_goods_info` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `name` varchar(255) DEFAULT NULL COMMENT '商品名称',
  `no` varchar(32) DEFAULT NULL COMMENT '商品编码',
  `type` tinyint(4) DEFAULT NULL COMMENT '类别',
  `status` tinyint(4) DEFAULT NULL COMMENT '状态',
  `brand_id` bigint(20) DEFAULT NULL COMMENT '品牌',
  `price` decimal(10,2) DEFAULT NULL COMMENT '价格',
  `num` int(10) NOT NULL COMMENT '库存数量',
  `desc` varchar(255) DEFAULT NULL COMMENT '描述',
  `create_user` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` bigint(20) DEFAULT NULL COMMENT '创建时间',
  `update_user` bigint(20) DEFAULT NULL COMMENT '更新人',
  `update_time` bigint(20) DEFAULT NULL COMMENT '更新时间',
  `is_delete` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除标志：0 正常，1 已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品基础信息表';

SET FOREIGN_KEY_CHECKS = 1;

```