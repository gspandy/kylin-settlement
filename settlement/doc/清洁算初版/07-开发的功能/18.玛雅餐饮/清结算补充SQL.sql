alter table SETTLE_TRANS_INVOICE modify column ACCOUNT_NAME varchar(100) COMMENT '账号名';
alter table SETTLE_TRANS_INVOICE modify column OPEN_BANK_NAME varchar(200) COMMENT '开户行名称';
alter table SETTLE_TRANS_INVOICE modify column ERROR_CODE varchar(100) COMMENT '错误码';
alter table SETTLE_TRANS_INVOICE modify column REMARK varchar(255) COMMENT '用户备注';
alter table SETTLE_TRANS_INVOICE_HIS modify column ACCOUNT_NAME varchar(100) COMMENT '账号名';
alter table SETTLE_TRANS_INVOICE_HIS modify column OPEN_BANK_NAME varchar(200) COMMENT '开户行名称';
alter table SETTLE_TRANS_INVOICE_HIS modify column ERROR_CODE varchar(100) COMMENT '错误码';
alter table SETTLE_TRANS_INVOICE_HIS modify column REMARK varchar(255) COMMENT '用户备注';