CREATE TABLE `h2test`.`test`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(20)  NOT NULL DEFAULT '' COMMENT '名称',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `creator` bigint(20) UNSIGNED NOT NULL DEFAULT 1 COMMENT '创建人',
  `modify_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  `modifier` bigint(20) UNSIGNED NOT NULL DEFAULT 1 COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_create_time`(`create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = 'test' ROW_FORMAT = Compact;