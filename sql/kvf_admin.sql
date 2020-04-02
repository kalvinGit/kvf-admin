/*
 Navicat Premium Data Transfer

 Source Server         : local
 Source Server Type    : MySQL
 Source Server Version : 80012
 Source Host           : localhost:3306
 Source Schema         : kvf_admin

 Target Server Type    : MySQL
 Target Server Version : 80012
 File Encoding         : 65001

 Date: 12/05/2019 18:15:45
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

DROP DATABASE IF EXISTS kvf_admin;
CREATE DATABASE kvf_admin DEFAULT CHARSET utf8mb4;
USE kvf_admin;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `dept_id` bigint(20) NOT NULL COMMENT '归属部门',
  `username` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '登录名',
  `password` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
  `realname` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '姓名',
  `sex` tinyint(2) NOT NULL DEFAULT '0' COMMENT '性别。0：未知；1：男；2：女',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '手机号码',
  `tel` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '固定电话',
  `email` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '邮箱',
  `avatar` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户头像',
  `job_title` varchar(30) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '职务名称',
  `status` tinyint(2) NOT NULL DEFAULT '0' COMMENT '用户状态。0：正常；1：禁用',
  `sort` int(11) NOT NULL DEFAULT '0' COMMENT '排序。值越小越靠前',
  `del_flag` int(11) NOT NULL DEFAULT '0' COMMENT '删除标识。0：未删除；1：已删除',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=COMPACT COMMENT='用户表';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 3, 'admin', 'e10adc3949ba59abbe56e057f20f883e', '系统管理员', 1, '18218798428', '123', '123', 'static/image/avatar/tz.jpeg', '超级管理员', 0, 0, 0, NULL, '2019-05-19 17:39:12', '2019-04-30 22:44:17');
INSERT INTO `sys_user` VALUES (2, 2, 'dev', 'e10adc3949ba59abbe56e057f20f883e', '开发人员', 2, '18218798428', '131', '123', NULL, '开发人员', 0, 1, 0, NULL, '2019-05-19 18:34:16', '2019-05-02 12:22:38');
INSERT INTO `sys_user` VALUES (3, 2, 'test', 'e10adc3949ba59abbe56e057f20f883e', '测试账号', 2, '18218798428', '131', '123', NULL, '测试', 0, 1, 0, NULL, '2019-05-19 18:34:16', '2019-05-02 12:22:38');


-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `parent_id` bigint(20) NOT NULL COMMENT '上级部门ID。一级部门为0',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '部门名称',
  `type` tinyint(2) NOT NULL COMMENT '类型。0：公司；1：部门；2：科室/小组',
  `area_id` bigint(20) NULL DEFAULT NULL COMMENT '所在区域ID',
  `sort` int(11) NOT NULL DEFAULT 0 COMMENT '排序值。越小越靠前',
  `status` tinyint(2) NOT NULL DEFAULT 0 COMMENT '状态。0：正常；1：禁用',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '部门表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept` VALUES (1, 0, 'Kalvin', 0, 1, 0, 0, '2019-05-11 15:35:24', 1, '2019-05-01 16:40:08');
INSERT INTO `sys_dept` VALUES (2, 1, 'IT部', 1, 1, 0, 0, '2019-05-01 17:12:54', NULL, '2019-05-01 17:12:54');
INSERT INTO `sys_dept` VALUES (3, 2, '研发组', 2, 1, 0, 0, '2019-05-01 17:13:48', NULL, '2019-05-01 17:13:48');


-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `parent_id` bigint(20) NOT NULL COMMENT '父菜单ID。一级菜单为0',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单名称',
  `url` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单URL',
  `permission` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '授权标识。多个用逗号分隔，如：user:list,user:create',
  `type` tinyint(2) NULL DEFAULT NULL COMMENT '类型。0：目录；1：菜单；2：按钮；3：导航菜单',
  `icon` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单图标',
  `status` tinyint(2) NOT NULL DEFAULT 0 COMMENT '状态。0：正常；1：禁用',
  `sort` int(11) NOT NULL DEFAULT 0 COMMENT '排序值。越小越靠前',
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '菜单表' ROW_FORMAT = Compact;


-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, 0, '系统管理', NULL, '', 0, 'fa fa-cogs', 0, 0, NULL, '2019-05-06 21:46:33');
INSERT INTO `sys_menu` VALUES (2, 1, '用户管理', 'sys/user/index', 'sys:user:index', 1, NULL, 0, 0, NULL, '2019-05-06 21:46:47');
INSERT INTO `sys_menu` VALUES (3, 1, '菜单管理', 'sys/menu/index', 'sys:menu:index', 1, NULL, 0, 2, NULL, '2019-05-11 11:50:57');
INSERT INTO `sys_menu` VALUES (4, 1, '角色管理', 'sys/role/index', 'sys:role:index', 1, NULL, 0, 3, NULL, '2019-05-11 11:51:31');
INSERT INTO `sys_menu` VALUES (5, 1, '部门管理', 'sys/dept/index', 'sys:dept:index', 1, NULL, 0, 1, NULL, '2019-05-11 11:51:58');
INSERT INTO `sys_menu` VALUES (7, 0, '系统审计', NULL, '', 0, 'fa fa-industry', 0, 1, NULL, '2019-05-11 12:04:09');
INSERT INTO `sys_menu` VALUES (9, 7, '操作日志', 'sys/log/index', 'sys:log:index', 1, NULL, 0, 0, NULL, '2019-05-11 13:26:02');
INSERT INTO `sys_menu` VALUES (10, 2, '添加', NULL, 'sys:user:add', 2, NULL, 0, 0, NULL, '2019-05-07 21:25:46');
INSERT INTO `sys_menu` VALUES (11, 2, '删除', NULL, 'sys:user:del', 2, NULL, 0, 2, NULL, '2019-05-11 14:26:54');
INSERT INTO `sys_menu` VALUES (12, 2, '编辑', NULL, 'sys:user:edit', 2, NULL, 0, 1, NULL, '2019-05-11 14:28:06');
INSERT INTO `sys_menu` VALUES (13, 3, '添加', NULL, 'sys:menu:add', 2, NULL, 0, 0, NULL, '2019-05-11 14:28:59');
INSERT INTO `sys_menu` VALUES (14, 3, '编辑', NULL, 'sys:menu:edit', 2, NULL, 0, 1, NULL, '2019-05-11 14:29:35');
INSERT INTO `sys_menu` VALUES (15, 3, '删除', NULL, 'sys:menu:del', 2, NULL, 0, 2, NULL, '2019-05-11 14:29:55');
INSERT INTO `sys_menu` VALUES (16, 4, '添加', NULL, 'sys:role:add', 2, NULL, 0, 0, NULL, '2019-05-11 14:30:07');
INSERT INTO `sys_menu` VALUES (17, 4, '编辑', NULL, 'sys:role:edit', 2, NULL, 0, 1, NULL, '2019-05-11 14:30:28');
INSERT INTO `sys_menu` VALUES (18, 4, '删除', NULL, 'sys:role:del', 2, NULL, 0, 2, NULL, '2019-05-11 14:30:37');
INSERT INTO `sys_menu` VALUES (19, 4, '权限设置', NULL, 'sys:role:permission', 2, NULL, 0, 3, NULL, '2019-05-11 14:30:37');
INSERT INTO `sys_menu` VALUES (20, 5, '添加', NULL, 'sys:dept:add', 2, NULL, 0, 0, NULL, '2019-05-11 14:30:47');
INSERT INTO `sys_menu` VALUES (21, 5, '编辑', NULL, 'sys:dept:edit', 2, NULL, 0, 1, NULL, '2019-05-11 14:30:58');
INSERT INTO `sys_menu` VALUES (22, 5, '删除', NULL, 'sys:dept:del', 2, NULL, 0, 2, NULL, '2019-05-11 14:31:13');
INSERT INTO `sys_menu` VALUES (23, 2, '重置密码', NULL, 'sys:user:reset', 2, NULL, 0, 3, NULL, '2019-05-12 18:01:10');
INSERT INTO `sys_menu` VALUES (24, 0, 'Druid监控', 'druid/index.html', NULL, 3, 'fa fa-eye', 0, 0, NULL, '2019-05-20 22:43:09');
INSERT INTO `sys_menu` VALUES (25, 0, '代码生成', '', NULL, 0, 'fa fa-bolt', 0, 2, NULL, '2019-06-10 22:47:31');
INSERT INTO `sys_menu` VALUES (26, 25, '生成管理', 'generator/table/index', 'gen:table:index', 1, NULL, 0, 0, NULL, '2019-06-10 22:50:09');
INSERT INTO `sys_menu` VALUES (27, 1, '字典管理', 'sys/dict/index', 'sys:dict:index', 1, NULL, 0, 4, NULL, '2019-08-12 09:47:31');
INSERT INTO `sys_menu` VALUES (28, 27, '添加', NULL, 'sys:dict:add', 2, NULL, 0, 0, NULL, '2019-08-12 09:48:03');
INSERT INTO `sys_menu` VALUES (29, 27, '编辑', NULL, 'sys:dict:edit', 2, NULL, 0, 0, NULL, '2019-08-12 09:48:20');
INSERT INTO `sys_menu` VALUES (30, 27, '删除', NULL, 'sys:dict:del', 2, NULL, 0, 0, NULL, '2019-08-12 09:48:45');
INSERT INTO `sys_menu` VALUES (31, 1, '定时任务', 'schedule/job/index', 'schedule:job:index', 1, NULL, 0, 5, NULL, '2019-08-17 18:13:22');
INSERT INTO `sys_menu` VALUES (32, 31, '添加', NULL, 'schedule:job:add', 2, NULL, 0, 0, NULL, '2019-08-17 18:14:20');
INSERT INTO `sys_menu` VALUES (33, 31, '编辑', NULL, 'schedule:job:edit', 2, NULL, 0, 0, NULL, '2019-08-17 18:14:36');
INSERT INTO `sys_menu` VALUES (34, 31, '删除', NULL, 'schedule:job:del', 2, NULL, 0, 0, NULL, '2019-08-17 18:14:55');
INSERT INTO `sys_menu` VALUES (35, 31, '暂停', NULL, 'schedule:job:pause', 2, NULL, 0, 0, NULL, '2019-08-17 18:16:10');
INSERT INTO `sys_menu` VALUES (36, 31, '恢复', NULL, 'schedule:job:resume', 2, NULL, 0, 0, NULL, '2019-08-17 18:16:28');
INSERT INTO `sys_menu` VALUES (37, 0, '组件管理', NULL, NULL, 0, 'fa fa-th-large', 0, 3, NULL, '2020-03-31 11:14:02');
INSERT INTO `sys_menu` VALUES (38, 37, '富文本', 'sys/component/ueditor/index', 'component:ueditor:index', 1, NULL, 0, 1, NULL, '2020-03-31 11:17:55');
INSERT INTO `sys_menu` VALUES (39, 37, '图标库', 'sys/component/icons/index', 'component:icons:index', 1, NULL, 0, 0, NULL, '2020-03-31 11:22:09');




-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色名称',
  `parent_id` bigint(20) NOT NULL COMMENT '父级ID',
  `type` tinyint(2) NOT NULL COMMENT '类型。0：分类；1：角色',
  `remarks` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=COMPACT COMMENT='角色表';

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '系统角色', 0, 0, '', NULL, '2019-05-19 22:32:38', '2019-05-08 22:07:14');
INSERT INTO `sys_role` VALUES (2, '研发组', 0, 0, '', NULL, '2019-05-19 22:32:54', '2019-05-08 22:17:09');
INSERT INTO `sys_role` VALUES (3, '系统管理员', 1, 1, NULL, NULL, '2019-05-19 22:33:12', '2019-05-19 22:33:12');
INSERT INTO `sys_role` VALUES (4, '项目组长', 2, 1, NULL, NULL, '2019-05-19 22:33:32', '2019-05-19 22:33:32');
INSERT INTO `sys_role` VALUES (5, '开发者', 0, 1, '', NULL, '2019-06-07 19:42:14', '2019-06-07 19:42:14');



-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `menu_id` bigint(20) NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色菜单表' ROW_FORMAT = Compact;


-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (1, 3, 7);
INSERT INTO `sys_role_menu` VALUES (2, 3, 9);
INSERT INTO `sys_role_menu` VALUES (3, 3, 1);
INSERT INTO `sys_role_menu` VALUES (4, 3, 2);
INSERT INTO `sys_role_menu` VALUES (5, 3, 10);
INSERT INTO `sys_role_menu` VALUES (6, 3, 12);
INSERT INTO `sys_role_menu` VALUES (7, 3, 11);
INSERT INTO `sys_role_menu` VALUES (8, 3, 23);
INSERT INTO `sys_role_menu` VALUES (9, 3, 5);
INSERT INTO `sys_role_menu` VALUES (10, 3, 20);
INSERT INTO `sys_role_menu` VALUES (11, 3, 21);
INSERT INTO `sys_role_menu` VALUES (12, 3, 22);
INSERT INTO `sys_role_menu` VALUES (13, 3, 3);
INSERT INTO `sys_role_menu` VALUES (14, 3, 13);
INSERT INTO `sys_role_menu` VALUES (15, 3, 14);
INSERT INTO `sys_role_menu` VALUES (16, 3, 15);
INSERT INTO `sys_role_menu` VALUES (17, 3, 4);
INSERT INTO `sys_role_menu` VALUES (18, 3, 16);
INSERT INTO `sys_role_menu` VALUES (19, 3, 17);
INSERT INTO `sys_role_menu` VALUES (20, 3, 18);
INSERT INTO `sys_role_menu` VALUES (21, 3, 19);
INSERT INTO `sys_role_menu` VALUES (22, 3, 24);
INSERT INTO `sys_role_menu` VALUES (23, 3, 25);
INSERT INTO `sys_role_menu` VALUES (24, 3, 26);
INSERT INTO `sys_role_menu` VALUES (25, 5, 1);
INSERT INTO `sys_role_menu` VALUES (26, 5, 2);
INSERT INTO `sys_role_menu` VALUES (27, 5, 5);
INSERT INTO `sys_role_menu` VALUES (28, 5, 3);
INSERT INTO `sys_role_menu` VALUES (29, 5, 4);
INSERT INTO `sys_role_menu` VALUES (30, 5, 7);
INSERT INTO `sys_role_menu` VALUES (31, 5, 25);
INSERT INTO `sys_role_menu` VALUES (32, 5, 26);
INSERT INTO `sys_role_menu` VALUES (33, 5, 24);
INSERT INTO `sys_role_menu` VALUES (34, 3, 27);
INSERT INTO `sys_role_menu` VALUES (35, 3, 30);
INSERT INTO `sys_role_menu` VALUES (36, 3, 28);
INSERT INTO `sys_role_menu` VALUES (37, 3, 29);
INSERT INTO `sys_role_menu` VALUES (38, 3, 31);
INSERT INTO `sys_role_menu` VALUES (39, 3, 32);
INSERT INTO `sys_role_menu` VALUES (40, 3, 33);
INSERT INTO `sys_role_menu` VALUES (41, 3, 34);
INSERT INTO `sys_role_menu` VALUES (42, 3, 35);
INSERT INTO `sys_role_menu` VALUES (43, 3, 36);
INSERT INTO `sys_role_menu` VALUES (44, 4, 7);
INSERT INTO `sys_role_menu` VALUES (45, 4, 9);
INSERT INTO `sys_role_menu` VALUES (46, 3, 37);
INSERT INTO `sys_role_menu` VALUES (47, 3, 39);
INSERT INTO `sys_role_menu` VALUES (48, 3, 38);

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户ID',
  `role_id` bigint(20) NULL DEFAULT NULL COMMENT '角色ID',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户与角色对应关系' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (1, 1, 3, '2019-05-09 22:06:16');
INSERT INTO `sys_user_role` VALUES (2, 2, 5, '2019-05-10 21:25:08');
INSERT INTO `sys_user_role` VALUES (3, 3, 4, '2019-05-10 21:25:08');


-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log`  (
  `id` bigint(18) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint(18) NOT NULL COMMENT '用户ID',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '登录名称',
  `operation` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '操作功能',
  `forward_action` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '操作uri',
  `ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'IP',
  `browser` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '浏览器',
  `os` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '系统',
  `time` bigint(20) NOT NULL DEFAULT 0 COMMENT '请求耗时。毫秒',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '日志表' ROW_FORMAT = Dynamic;


-- ----------------------------
-- Table structure for sys_dict
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `parent_id` bigint(20) NOT NULL COMMENT '父级ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典名称',
  `type` tinyint(2) NOT NULL DEFAULT 0 COMMENT '类型。0：字典类别；1：字典项；',
  `code` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典码',
  `value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字典值',
  `status` tinyint(2) NOT NULL DEFAULT 0 COMMENT '状态。0：有效；1：无效',
  `sort` int(11) NOT NULL DEFAULT 0 COMMENT '排序值。越小越靠前',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '字典表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dict
-- ----------------------------
INSERT INTO `sys_dict` VALUES (1, 0, '根目录', 0, 'ROOT', '', 0, 0, NULL);
INSERT INTO `sys_dict` VALUES (2, 1, '性别', 0, 'SEX', '', 0, 0, NULL);
INSERT INTO `sys_dict` VALUES (3, 2, '未知', 1, 'SEX_UNKNOWN', '0', 0, 0, NULL);
INSERT INTO `sys_dict` VALUES (4, 2, '男', 1, 'SEX_MAN', '1', 0, 0, NULL);
INSERT INTO `sys_dict` VALUES (5, 2, '女', 1, 'SEX_LADY', '2', 0, 0, NULL);


-- ----------------------------
-- Table structure for schedule_job
-- ----------------------------
DROP TABLE IF EXISTS `schedule_job`;
CREATE TABLE `schedule_job`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `bean` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'bean名称',
  `method` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '方法名',
  `params` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '参数',
  `cron` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'cron表达式',
  `status` tinyint(2) NOT NULL DEFAULT 1 COMMENT '状态。0：运行中；1：已暂停；2：已完成；3：运行失败；',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '定时任务表' ROW_FORMAT = Dynamic;

INSERT INTO `schedule_job` VALUES (1, 'helloJob', NULL, NULL, '0 0 12 * * ?', 1, '无参测试', '2019-08-18 13:30:08');


SET FOREIGN_KEY_CHECKS = 1;
