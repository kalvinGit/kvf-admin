-- 工作流OA版本需要执行此脚本，否则不用

-- ----------------------------
-- Table structure for wf_form
-- ----------------------------
DROP TABLE IF EXISTS `wf_form`;
CREATE TABLE `wf_form`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '表单代号',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '表单名称',
  `type` tinyint(2) NOT NULL DEFAULT 0 COMMENT '表单类型。0：简单表单；1：复杂表单；',
  `theme` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '表单主题。不配置默认为表单名称',
  `design_data` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '表单设计数据。',
  `js_code` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '表单js代码。仅当复杂表单才有',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '表单设计表' ROW_FORMAT = Dynamic;


-- ----------------------------
-- Table structure for wf_process_form
-- ----------------------------
DROP TABLE IF EXISTS `wf_process_form`;
CREATE TABLE `wf_process_form`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `model_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '流程模型ID',
  `form_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '表单代号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '流程表单关联表' ROW_FORMAT = Dynamic;



-- 系统菜单表插入工作流程相关菜单数据
INSERT INTO `sys_menu` VALUES (40, 0, '工作流程', '', NULL, 0, 'fa fa-google-wallet', 0, 0, NULL, '2020-04-20 20:19:38');
INSERT INTO `sys_menu` VALUES (41, 40, '流程管理', 'workflow/process/index', 'workflow:process:index', 1, NULL, 0, 0, NULL, '2020-04-20 20:21:03');
INSERT INTO `sys_menu` VALUES (42, 40, '我的待办', 'workflow/mytodo/index', 'workflow:mytodo:index', 1, NULL, 0, 3, NULL, '2020-04-20 20:22:33');
INSERT INTO `sys_menu` VALUES (43, 40, '我的已办', 'workflow/mydone/index', 'workflow:mydone:index', 1, NULL, 0, 4, NULL, '2020-04-20 20:24:15');
INSERT INTO `sys_menu` VALUES (44, 40, '我的申请', 'workflow/myapply/index', 'workflow:myapply:index', 1, NULL, 0, 5, NULL, '2020-04-20 20:27:01');
INSERT INTO `sys_menu` VALUES (45, 40, '表单管理', 'workflow/form/index', 'workflow:form:index', 1, NULL, 0, 1, NULL, '2020-04-20 20:29:59');
INSERT INTO `sys_menu` VALUES (46, 40, '我的流程', 'workflow/myprocess/index', 'workflow:myprocess:index', 1, NULL, 0, 2, NULL, '2020-04-20 20:30:45');
INSERT INTO `sys_menu` VALUES (47, 41, '创建', NULL, 'workflow:process:add', 2, NULL, 0, 0, NULL, '2020-05-10 23:40:52');
INSERT INTO `sys_menu` VALUES (48, 41, '设计', NULL, 'workflow:process:designer', 2, NULL, 0, 1, NULL, '2020-05-10 23:41:55');
INSERT INTO `sys_menu` VALUES (49, 41, '删除', NULL, 'workflow:process:delete', 2, NULL, 0, 2, NULL, '2020-05-10 23:42:28');
INSERT INTO `sys_menu` VALUES (50, 41, '挂起', NULL, 'workflow:process:suspend', 2, NULL, 0, 3, NULL, '2020-05-10 23:43:35');
INSERT INTO `sys_menu` VALUES (51, 41, '激活', NULL, 'workflow:process:activate', 2, NULL, 0, 4, NULL, '2020-05-10 23:43:56');
INSERT INTO `sys_menu` VALUES (52, 41, '导出', NULL, 'workflow:process:export', 2, NULL, 0, 5, NULL, '2020-05-10 23:44:41');
INSERT INTO `sys_menu` VALUES (53, 41, '导入', NULL, 'workflow:process:import', 2, NULL, 0, 6, NULL, '2020-05-10 23:44:55');
INSERT INTO `sys_menu` VALUES (54, 41, '配置', NULL, 'workflow:process:setting', 2, NULL, 0, 7, NULL, '2020-05-10 23:45:24');
INSERT INTO `sys_menu` VALUES (55, 41, '发布', NULL, 'workflow:process:push', 2, NULL, 0, 8, NULL, '2020-05-10 23:45:55');
INSERT INTO `sys_menu` VALUES (56, 41, '启动', NULL, 'workflow:process:start', 2, NULL, 0, 9, NULL, '2020-05-10 23:46:09');
INSERT INTO `sys_menu` VALUES (57, 45, '添加', NULL, 'workflow:form:add', 2, NULL, 0, 0, NULL, '2020-05-10 23:46:53');
INSERT INTO `sys_menu` VALUES (58, 45, '编辑', NULL, 'workflow:form:edit', 2, NULL, 0, 1, NULL, '2020-05-10 23:47:20');
INSERT INTO `sys_menu` VALUES (59, 45, '删除', NULL, 'workflow:form:delete', 2, NULL, 0, 2, NULL, '2020-05-10 23:47:47');
INSERT INTO `sys_menu` VALUES (60, 45, '预览', NULL, 'workflow:form:prev', 2, NULL, 0, 3, NULL, '2020-05-10 23:48:17');