/*
Navicat MySQL Data Transfer

Source Server         : settle_coding
Source Server Version : 50623
Source Host           : 121.40.166.38:3316
Source Database       : settle

Target Server Type    : MYSQL
Target Server Version : 50623
File Encoding         : 65001

Date: 2016-04-26 15:47:21
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for SETTLE_COLLECT_RULE
-- ----------------------------
DROP TABLE IF EXISTS `SETTLE_COLLECT_RULE`;
CREATE TABLE `SETTLE_COLLECT_RULE` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '汇总规则ID',
  `PROFIT_RULE_NAME` varchar(36) COLLATE utf8_bin NOT NULL COMMENT '汇总规则名称',
  `ROOT_INST_CD` varchar(20) COLLATE utf8_bin NOT NULL COMMENT '机构代码',
  `PAY_CHANNEL_ID` varchar(20) COLLATE utf8_bin NOT NULL COMMENT '渠道代码',
  `FUNC_CODE` varchar(20) COLLATE utf8_bin NOT NULL COMMENT '功能码',
  `KERNEL_FUNC_CODE` varchar(20) COLLATE utf8_bin NOT NULL COMMENT '会计功能码',
  `ACCOUNT_NAME1` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '科目名称1',
  `FIN_ACCOUNT_ID1` varchar(20) COLLATE utf8_bin NOT NULL COMMENT '账户ID1',
  `ACCOUNT_NAME2` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '科目名称2',
  `FIN_ACCOUNT_ID2` varchar(20) COLLATE utf8_bin NOT NULL COMMENT '账户ID2',
  `OBLIGATE1` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '预留1',
  `OBLIGATE2` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '预留2',
  `OBLIGATE3` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '预留3',
  `COLLECT_TYPE` tinyint(3) unsigned NOT NULL DEFAULT '1' COMMENT '汇总类型 1:日切汇总, 2:对账汇总',
  `STATUS_ID` tinyint(3) unsigned NOT NULL DEFAULT '1' COMMENT '状态',
  `REMARK` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '备注',
  `CREATED_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
  `UPDATED_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录更新时间',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=185 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='汇总规则主表';
