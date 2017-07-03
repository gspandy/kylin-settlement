/*
Navicat MySQL Data Transfer

Source Server         : mtaegis_coding
Source Server Version : 50623
Source Host           : 121.40.166.38:3316
Source Database       : mtkernel

Target Server Type    : MYSQL
Target Server Version : 50623
File Encoding         : 65001

Date: 2016-03-23 10:18:26
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for REGISTER_FALUT
-- ----------------------------
DROP TABLE IF EXISTS `REGISTER_FALUT`;
CREATE TABLE `REGISTER_FALUT` (
  `ID` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `REFER_ENTRY_ID` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '套录号',
  `ROOT_INST_CD` varchar(20) COLLATE utf8_bin NOT NULL COMMENT '机构号',
  `BUSI_TYPE_CODE` varchar(10) COLLATE utf8_bin DEFAULT NULL COMMENT '业务种类编码',
  `MERCHANT_NO` varchar(10) COLLATE utf8_bin DEFAULT NULL COMMENT '商户号',
  `ACCOUNT_NO` varchar(30) COLLATE utf8_bin DEFAULT NULL COMMENT '账号',
  `SETTLE_TYPE` varchar(10) COLLATE utf8_bin DEFAULT NULL COMMENT '结算类型',
  `PAY_TYPE` varchar(10) COLLATE utf8_bin DEFAULT NULL COMMENT '付款类型',
  `SETTLE_CYCLE` varchar(10) COLLATE utf8_bin DEFAULT NULL COMMENT '结算周期',
  `T_N` varchar(10) COLLATE utf8_bin DEFAULT NULL COMMENT 'T+n',
  `SETTLE_DAY` datetime DEFAULT NULL COMMENT '结算日',
  `ACTUAL_SETTLE_DAY` datetime DEFAULT NULL COMMENT '实际结算日',
  `SETTLE_NO` varchar(10) COLLATE utf8_bin DEFAULT NULL COMMENT '结算单号',
  `REMARK` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '备注',
  `CREATED_TIME` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
  `UPDATED_TIME` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录更新时间',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='登记簿错误记录表';
