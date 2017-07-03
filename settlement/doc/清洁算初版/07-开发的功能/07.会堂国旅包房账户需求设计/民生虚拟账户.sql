/*
*民生虚拟账户渠道
*/
insert into `CHANNEL_INFO` (`CHANNEL_NO`, `CHANNEL_NAME`, `CHANNEL_HOME`, `LOW_AMOUNT`, `HIGH_AMOUNT`, `BATCH_LOW_AMOUNT`, `BATCH_HIGH_AMOUNT`, `LIMIT_COUNT`, `CHANNEL_FEE`, `FEE_RATE`, `CHANNEL_SERVICE`, `CHANNEL_GROUP`, `CHANNEL_METHOD`, `CHANNEL_VERSION`, `STATUS_ID`) 
values('160721','民生虚拟账户管理','CMBC_BankPayment','1','1','1','1','1','0',NULL,'com.rkylin.gateway.service.GatewayCmbcAccountService','CMBC_BankPayment','subAcctManage','1.0.0','1');

insert into `CHANNEL_INFO` (`CHANNEL_NO`, `CHANNEL_NAME`, `CHANNEL_HOME`, `LOW_AMOUNT`, `HIGH_AMOUNT`, `BATCH_LOW_AMOUNT`, `BATCH_HIGH_AMOUNT`, `LIMIT_COUNT`, `CHANNEL_FEE`, `FEE_RATE`, `CHANNEL_SERVICE`, `CHANNEL_GROUP`, `CHANNEL_METHOD`, `CHANNEL_VERSION`, `STATUS_ID`) 
values('160722','民生虚拟账户批量添加','CMBC_BankPayment','1','1','1','1','1','0',NULL,'com.rkylin.gateway.service.GatewayCmbcAccountService','CMBC_BankPayment','batchAddSubAcct','1.0.0','1');

insert into `CHANNEL_INFO` (`CHANNEL_NO`, `CHANNEL_NAME`, `CHANNEL_HOME`, `LOW_AMOUNT`, `HIGH_AMOUNT`, `BATCH_LOW_AMOUNT`, `BATCH_HIGH_AMOUNT`, `LIMIT_COUNT`, `CHANNEL_FEE`, `FEE_RATE`, `CHANNEL_SERVICE`, `CHANNEL_GROUP`, `CHANNEL_METHOD`, `CHANNEL_VERSION`, `STATUS_ID`) 
values('160723','民生虚拟账户间转账','CMBC_BankPayment','1','9999999999','1','1','1','0',NULL,'com.rkylin.gateway.service.GatewayCmbcAccountService','CMBC_BankPayment','subAcctTransfer','1.0.0','1');

insert into `CHANNEL_INFO` (`CHANNEL_NO`, `CHANNEL_NAME`, `CHANNEL_HOME`, `LOW_AMOUNT`, `HIGH_AMOUNT`, `BATCH_LOW_AMOUNT`, `BATCH_HIGH_AMOUNT`, `LIMIT_COUNT`, `CHANNEL_FEE`, `FEE_RATE`, `CHANNEL_SERVICE`, `CHANNEL_GROUP`, `CHANNEL_METHOD`, `CHANNEL_VERSION`, `STATUS_ID`) 
values('160724','民生虚拟账户关联账户管理','CMBC_BankPayment','1','1','1','1','1','0',NULL,'com.rkylin.gateway.service.GatewayCmbcAccountService','CMBC_BankPayment','subAcctRelate','1.0.0','1');

insert into `CHANNEL_INFO` (`CHANNEL_NO`, `CHANNEL_NAME`, `CHANNEL_HOME`, `LOW_AMOUNT`, `HIGH_AMOUNT`, `BATCH_LOW_AMOUNT`, `BATCH_HIGH_AMOUNT`, `LIMIT_COUNT`, `CHANNEL_FEE`, `FEE_RATE`, `CHANNEL_SERVICE`, `CHANNEL_GROUP`, `CHANNEL_METHOD`, `CHANNEL_VERSION`, `STATUS_ID`) 
values('160725','民生虚拟账户对外转账','CMBC_BankPayment','1','9999999999','1','1','1','0',NULL,'com.rkylin.gateway.service.GatewayCmbcAccountService','CMBC_BankPayment','subAcctPayment','1.0.0','1');

insert into `CHANNEL_INFO` (`CHANNEL_NO`, `CHANNEL_NAME`, `CHANNEL_HOME`, `LOW_AMOUNT`, `HIGH_AMOUNT`, `BATCH_LOW_AMOUNT`, `BATCH_HIGH_AMOUNT`, `LIMIT_COUNT`, `CHANNEL_FEE`, `FEE_RATE`, `CHANNEL_SERVICE`, `CHANNEL_GROUP`, `CHANNEL_METHOD`, `CHANNEL_VERSION`, `STATUS_ID`) 
values('160726','民生虚拟账户线下充值','CMBC_BankPayment','1','9999999999','1','1','1','0',NULL,'com.rkylin.gateway.service.GatewayCmbcAccountService','CMBC_BankPayment','offLinePayment','1.0.0','1');

insert into `CHANNEL_INFO` (`CHANNEL_NO`, `CHANNEL_NAME`, `CHANNEL_HOME`, `LOW_AMOUNT`, `HIGH_AMOUNT`, `BATCH_LOW_AMOUNT`, `BATCH_HIGH_AMOUNT`, `LIMIT_COUNT`, `CHANNEL_FEE`, `FEE_RATE`, `CHANNEL_SERVICE`, `CHANNEL_GROUP`, `CHANNEL_METHOD`, `CHANNEL_VERSION`, `STATUS_ID`) 
values('160727','民生虚拟账户查询','CMBC_BankPayment','1','1','1','1','1','0',NULL,'com.rkylin.gateway.service.GatewayCmbcAccountService','CMBC_BankPayment','subAcctQuery','1.0.0','1');

insert into `CHANNEL_INFO` (`CHANNEL_NO`, `CHANNEL_NAME`, `CHANNEL_HOME`, `LOW_AMOUNT`, `HIGH_AMOUNT`, `BATCH_LOW_AMOUNT`, `BATCH_HIGH_AMOUNT`, `LIMIT_COUNT`, `CHANNEL_FEE`, `FEE_RATE`, `CHANNEL_SERVICE`, `CHANNEL_GROUP`, `CHANNEL_METHOD`, `CHANNEL_VERSION`, `STATUS_ID`) 
values('160728','民生虚拟账户交易查询','CMBC_BankPayment','1','1','1','1','1','0',NULL,'com.rkylin.gateway.service.GatewayCmbcAccountService','CMBC_BankPayment','subAcctTransQuery','1.0.0','1');

insert into `CHANNEL_INFO` (`CHANNEL_NO`, `CHANNEL_NAME`, `CHANNEL_HOME`, `LOW_AMOUNT`, `HIGH_AMOUNT`, `BATCH_LOW_AMOUNT`, `BATCH_HIGH_AMOUNT`, `LIMIT_COUNT`, `CHANNEL_FEE`, `FEE_RATE`, `CHANNEL_SERVICE`, `CHANNEL_GROUP`, `CHANNEL_METHOD`, `CHANNEL_VERSION`, `STATUS_ID`) 
values('160729','民生虚拟账户关联账户查询','CMBC_BankPayment','1','1','1','1','1','0',NULL,'com.rkylin.gateway.service.GatewayCmbcAccountService','CMBC_BankPayment','subAcctRelQuery','1.0.0','1');

/*
*民生虚拟账户机构号
*/
INSERT INTO `multi_gate`.`ORG_INFO` (`ORG_NO`, `ORG_CODE`, `ORG_NUM`, `ORG_REMARK`, `CHANNEL_HOME`, `PRIVATE_KEY`, `PUBLIC_KEY`, `CHANNEL_PKEY`, `CHANNEL_WSDL`, `CHANNEL_URL1`, `CHANNEL_URL2`, `NOTIFY_TYPE`,
 `NOTIFY_URL1`, `NOTIFY_URL2`, `USERNAME`, `PASSWORD`, `DATA_FORMAT`, `TIMEOUT`, `VERSION`, `CHARSET`, `LANGUAGE`, `SIGN_TYPE`, `ACCOUNT_NO`, `ACCOUNT_TYPE`, `ACCOUNT_NAME`, `BANK_NO`, `BANK_NAME`, `DISTRICT_CODE`, 
 `PROVINCE`, `CITY`, `EXPAND1`, `EXPAND2`, `EXPAND3`, `EXPAND4`, `STATUS_ID`) VALUES ('M666666', '2200003220', 'M00000X', '融数子账户测试', 'CMBC_BankPayment', NULL, NULL, NULL, NULL, 
 'http://121.40.112.168:8686/eweb/b2e/connect.do', NULL, NULL, NULL, NULL, '2200003220001', '111111', 'xml', NULL, '201', 'GBK', 'chs', '0', '600033029', NULL, '厦门测试2200003220', NULL, NULL, NULL, NULL, NULL, NULL, NULL,
  '[{\"subAccount\":\"112233\",\"orgNo\":\"M666666\",\"userId\":\"14623543519624388\",\"productId\":\"P000221\"}]', NULL, '1');
