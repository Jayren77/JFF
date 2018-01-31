/*
Navicat MySQL Data Transfer

Source Server         : 本地MySql
Source Server Version : 50714
Source Host           : localhost:3306
Source Database       : jff

Target Server Type    : MYSQL
Target Server Version : 50714
File Encoding         : 65001

Date: 2018-01-30 22:10:55
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for role_info
-- ----------------------------
DROP TABLE IF EXISTS `role_info`;
CREATE TABLE `role_info` (
  `id` int(2) NOT NULL,
  `role_type` varchar(255) NOT NULL COMMENT '角色类型',
  `role_desc` varchar(255) NOT NULL COMMENT '角色描述',
  `role_grade` int(2) NOT NULL COMMENT '角色等级',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for user_info
-- ----------------------------
DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(255) NOT NULL,
  `user_password` varchar(255) NOT NULL,
  `user_role` int(2) NOT NULL COMMENT '用户角色权限',
  `user_cre_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `user_location` varchar(255) DEFAULT NULL COMMENT '用户位置信息',
  `user_wx_name` varchar(255) DEFAULT NULL COMMENT '用户微信名',
  `user_header` varchar(255) DEFAULT NULL COMMENT '用户头像',
  PRIMARY KEY (`user_name`,`id`),
  KEY `fk_user_role` (`user_role`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
