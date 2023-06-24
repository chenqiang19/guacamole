/*
 Navicat Premium Data Transfer

 Source Server         : 192.20.4.200-skyline
 Source Server Type    : MariaDB
 Source Server Version : 100328
 Source Host           : 192.20.4.200:3306
 Source Schema         : guacamole

 Target Server Type    : MariaDB
 Target Server Version : 100328
 File Encoding         : 65001

 Date: 24/06/2023 10:22:24
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for guacamole_connection
-- ----------------------------
DROP TABLE IF EXISTS `guacamole_connection`;
CREATE TABLE `guacamole_connection`  (
  `connection_id` int(11) NOT NULL AUTO_INCREMENT,
  `connection_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  `parent_id` int(11) NULL DEFAULT NULL,
  `protocol` varchar(32) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  `proxy_port` int(11) NULL DEFAULT NULL,
  `proxy_hostname` varchar(512) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  `proxy_encryption_method` varchar(32) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  `max_connections` int(11) NULL DEFAULT NULL,
  `max_connections_per_user` int(11) NULL DEFAULT NULL,
  `connection_weight` int(11) NULL DEFAULT NULL,
  `failover_only` int(1) NULL DEFAULT NULL,
  PRIMARY KEY (`connection_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 22 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for guacamole_connection_group
-- ----------------------------
DROP TABLE IF EXISTS `guacamole_connection_group`;
CREATE TABLE `guacamole_connection_group`  (
  `connection_group_id` int(11) NOT NULL AUTO_INCREMENT,
  `parent_id` int(11) NULL DEFAULT NULL,
  `connection_group_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  `type` varchar(32) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  `max_connections` int(11) NULL DEFAULT NULL,
  `max_connections_per_user` int(11) NULL DEFAULT NULL,
  `enable_session_affinity` int(1) NULL DEFAULT NULL,
  PRIMARY KEY (`connection_group_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for guacamole_connection_parameter
-- ----------------------------
DROP TABLE IF EXISTS `guacamole_connection_parameter`;
CREATE TABLE `guacamole_connection_parameter`  (
  `connection_id` int(11) NOT NULL,
  `parameter_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `parameter_value` varchar(4096) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  PRIMARY KEY (`connection_id`, `parameter_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for guacamole_entity
-- ----------------------------
DROP TABLE IF EXISTS `guacamole_entity`;
CREATE TABLE `guacamole_entity`  (
  `entity_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `type` enum('USER','USER_GROUP') CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`entity_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for guacamole_user
-- ----------------------------
DROP TABLE IF EXISTS `guacamole_user`;
CREATE TABLE `guacamole_user`  (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `entity_id` int(11) NOT NULL,
  `password_hash` binary(32) NOT NULL,
  `password_salt` binary(32) NULL DEFAULT NULL,
  `password_date` datetime(0) NOT NULL,
  `disabled` int(1) NOT NULL,
  `expired` int(1) NOT NULL,
  `access_window_start` time(0) NULL DEFAULT NULL,
  `access_window_end` time(0) NULL DEFAULT NULL,
  `valid_from` date NULL DEFAULT NULL,
  `valid_until` date NULL DEFAULT NULL,
  `timezone` varchar(64) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  `full_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  `email_address` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  `organization` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  `organizational_role` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL,
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `entity_id`(`entity_id`) USING BTREE,
  CONSTRAINT `guacamole_user_entity` FOREIGN KEY (`entity_id`) REFERENCES `guacamole_entity` (`entity_id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8 COLLATE = utf8_unicode_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
