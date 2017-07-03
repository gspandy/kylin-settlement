/*
Navicat MySQL Data Transfer

Source Server         : settle_coding
Source Server Version : 50623
Source Host           : 121.40.166.38:3316
Source Database       : settle

Target Server Type    : MYSQL
Target Server Version : 50623
File Encoding         : 65001

Date: 2016-04-26 15:47:30
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for SETTLE_KERNEL_ENTRY
-- ----------------------------
DROP TABLE IF EXISTS `SETTLE_KERNEL_ENTRY`;
CREATE TABLE `SETTLE_KERNEL_ENTRY` (
  `ID` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `TRANS_ENTRY_SA_ID` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '会计交易流水ID',
  `TRANS_DATE` datetime DEFAULT NULL COMMENT '交易日期',
  `REQUEST_ID_FROM` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '交易记录来源',
  `TRANS_ID` bigint(20) DEFAULT NULL COMMENT '交易流水ID',
  `TRANS_NUMBER` tinyint(3) unsigned NOT NULL COMMENT '交易流水条数',
  `TRANS_NO` tinyint(3) unsigned NOT NULL COMMENT '流水序号',
  `FUNC_CODE` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '基础功能码',
  `FIN_ACCOUNT_ID1` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '账户ID1',
  `PAYMENT_AMOUNT1` decimal(18,0) unsigned DEFAULT '0' COMMENT '发生额1',
  `CURRENCY1` varchar(20) COLLATE utf8_bin DEFAULT 'CNY' COMMENT '币种1',
  `FIN_ACCOUNT_ID2` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '账户ID2',
  `PAYMENT_AMOUNT2` decimal(18,0) unsigned DEFAULT '0' COMMENT '发生额2',
  `CURRENCY2` varchar(20) COLLATE utf8_bin DEFAULT 'CNY' COMMENT '币种2',
  `ACCOUNTING_STATUS` tinyint(3) unsigned DEFAULT '1' COMMENT '分录状态',
  `REVERSE_NUMBER` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '冲正流水号',
  `REFER_ENTRY_ID` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '套录号',
  `ACCOUNT_DATE` datetime NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '账期',
  `REMARK1` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '备注1',
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
  `SETTLE_NO` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '结算单号',
  `REMARK2` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '备注2',
  `STATUS_ID` tinyint(3) DEFAULT '1' COMMENT '清结算状态ID',
  `READ_STATUS_ID` tinyint(3) unsigned DEFAULT '1' COMMENT '会计系统状态ID',
  `RS_MSG` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '备注2',
  `OBLIGATE1` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '预留1',
  `OBLIGATE2` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '预留2',
  `OBLIGATE3` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '预留3',
  `CREATED_TIME` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
  `UPDATED_TIME` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录更新时间',
  PRIMARY KEY (`ID`,`ACCOUNT_DATE`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='交易流水表';
