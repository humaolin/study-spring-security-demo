/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80029
 Source Host           : localhost:3306
 Source Schema         : study

 Target Server Type    : MySQL
 Target Server Version : 80029
 File Encoding         : 65001

 Date: 23/04/2023 18:04:57
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for user_bind_third_login
-- ----------------------------
DROP TABLE IF EXISTS `user_bind_third_login`;
CREATE TABLE `user_bind_third_login`  (
  `id` bigint(0) NOT NULL COMMENT '主键ID',
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '平台类型',
  `user_id` bigint(0) NULL DEFAULT NULL COMMENT '用户表主键ID',
  `uuid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户在第三方平台的唯一ID',
  `nickname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '昵称',
  `head_sculpture` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '第三方头像',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '绑定时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_bind_third_login
-- ----------------------------
INSERT INTO `user_bind_third_login` VALUES (35, 'gitee', 1, '5263136', '明珠科技', NULL, '2023-04-23 15:18:53');

SET FOREIGN_KEY_CHECKS = 1;
