CREATE PROCEDURE `PRO_SETTLE_INVOICE_HIS`(IN iv_status_id             VARCHAR(20))
BEGIN
  #***********************************************************
  # 版权所有：
  # 未经本公司授权允许，任何人不得私自拷贝、发布或使用
  # 作者: 孙锐斌
  # 日期: 2016/03/02
  # 程序名: PRO_SETTLE_INVOICE_HIS
  # 功能描述: 将结算表和汇总表中符合条件的数据移到历史表中
  # 修改时间       修改人       程序版本       修改原因
  # 2016/03/02     孙锐斌        V1.0          创建
  # 2017/05/12     孙锐斌        V1.1          新架构修改
  #************************************************************
#--------------------1、定义接收游标数据的变量
  DECLARE lv_job_name                 VARCHAR(90);         #任务名称
  DECLARE lt_app_segment              TINYINT   UNSIGNED;  #段号
  DECLARE lm_err_code                 SMALLINT  UNSIGNED;  #错误编码
  DECLARE lv_err_message              VARCHAR(255);        #错误内容
  DECLARE ld_begin_time               DATETIME;            #执行开始时间
  DECLARE ld_end_time                 DATETIME;            #执行结束时间
  DECLARE lv_sql                      VARCHAR(4000);       # sql语句
  DECLARE lv_status_id                VARCHAR(20);         # 输入状态
  DECLARE EXIT HANDLER FOR SQLEXCEPTION,SQLWARNING,NOT FOUND
  begin
    rollback;
    SET ld_end_time         = CURRENT_TIME();
    SET lm_err_code         = 4000;
    SET lv_err_message      = CONCAT('执行错误,段号:',lt_app_segment);
    SET on_err_code         = lm_err_code;
    CALL PRO_LOG(lv_job_name,lt_app_segment,lm_err_code,lv_err_message,ld_begin_time,ld_end_time);
    COMMIT;
  end;

#--------------------1、初始化参数
  SET lv_job_name           = 'PRO_SETTLE_INVOICE_HIS';
  SET lt_app_segment        = 1;
  SET ld_begin_time         = NOW();
  SET lv_status_id          = iv_status_id;

#--------------------2、将数据移到历史表中 
#--2.1、插入汇总表历史数据
  START TRANSACTION;
  SET lt_app_segment        = 2;
  SET lv_sql=CONCAT('INSERT INTO SETTLE_TRANS_SUMMARY_HIS(',
                    'TRANS_SUM_ID,ROOT_INST_CD,USER_ID,FUNC_CODE,ACCOUNT_DATE,BUSINESS_TYPE',
                    ',ORDER_NO,BATCH_NO,AMOUNT,STATUS_ID,OBLIGATE1,OBLIGATE2,OBLIGATE3',
                    ',REMARK,CREATED_TIME,UPDATED_TIME)',
                    'SELECT STS.TRANS_SUM_ID,STS.ROOT_INST_CD,STS.USER_ID,STS.FUNC_CODE',
                    ',STS.ACCOUNT_DATE,STS.BUSINESS_TYPE,STS.ORDER_NO,STS.BATCH_NO',
                    ',STS.AMOUNT,STS.STATUS_ID,STS.OBLIGATE1,STS.OBLIGATE2,STS.OBLIGATE3',
                    ',STS.REMARK,STS.CREATED_TIME,STS.UPDATED_TIME',
                    ' FROM SETTLE_TRANS_SUMMARY STS',
                    ' JOIN SETTLE_TRANS_INVOICE STI ON (STS.BATCH_NO = STI.BATCH_NO AND STI.PROCESS_RESULT IN (13,15))',
                    ' WHERE STS.STATUS_ID in (',lv_status_id,')');
  SET @sql = lv_sql;
  PREPARE stmt FROM @sql;
  EXECUTE stmt;

#--2.3、删除汇总表数据
  SET lt_app_segment        = 3;
  SET lv_sql=CONCAT('DELETE STS.* FROM SETTLE_TRANS_SUMMARY STS
											WHERE EXISTS (SELECT STI.BATCH_NO FROM SETTLE_TRANS_INVOICE STI WHERE STS.BATCH_NO = STI.BATCH_NO AND STI.PROCESS_RESULT IN (13,15))
											AND STATUS_ID in (',lv_status_id,')');
  SET @sql = lv_sql;
  PREPARE stmt FROM @sql;
  EXECUTE stmt;
  COMMIT;

#--2.3、插入结算表历史数据
  SET lt_app_segment        = 4;
  START TRANSACTION;
  INSERT INTO SETTLE_TRANS_INVOICE_HIS(  INVOICE_NO
                                        ,REQUEST_NO
                                        ,BUSSINESS_CODE
																				,MERCHANT_CODE
                                        ,ROOT_INST_CD
                                        ,FUNC_CODE
                                        ,ORDER_TYPE
                                        ,ORDER_NO
                                        ,BATCH_NO
                                        ,GENE_SEQ
                                        ,USER_ID
                                        ,BANK_CODE
                                        ,ACCOUNT_TYPE
                                        ,ACCOUNT_NO
                                        ,ACCOUNT_NAME
                                        ,ACCOUNT_PROPERTY
                                        ,PROVINCE
                                        ,CITY
                                        ,OPEN_BANK_NAME
                                        ,PAY_BANK_CODE
                                        ,AMOUNT
                                        ,CURRENCY
                                        ,CERTIFICATE_TYPE
                                        ,CERTIFICATE_NUMBER
                                        ,DATA_SOURCE
                                        ,PROCESS_RESULT
                                        ,SEND_TYPE
                                        ,ERROR_CODE
                                        ,SEND_TIMES
                                        ,STATUS_ID
                                        ,ACCOUNT_DATE
                                        ,REAL_TIME_FLAG
                                        ,REMARK
                                        ,CREATED_TIME
                                        ,UPDATED_TIME
                                      )
  SELECT STI.INVOICE_NO
        ,STI.REQUEST_NO
        ,STI.BUSSINESS_CODE
				,STI.MERCHANT_CODE
        ,STI.ROOT_INST_CD
        ,STI.FUNC_CODE
        ,STI.ORDER_TYPE
        ,STI.ORDER_NO
        ,STI.BATCH_NO
        ,STI.GENE_SEQ
        ,STI.USER_ID
        ,STI.BANK_CODE
        ,STI.ACCOUNT_TYPE
        ,STI.ACCOUNT_NO
        ,STI.ACCOUNT_NAME
        ,STI.ACCOUNT_PROPERTY
        ,STI.PROVINCE
        ,STI.CITY
        ,STI.OPEN_BANK_NAME
        ,STI.PAY_BANK_CODE
        ,STI.AMOUNT
        ,STI.CURRENCY
        ,STI.CERTIFICATE_TYPE
        ,STI.CERTIFICATE_NUMBER
        ,STI.DATA_SOURCE
        ,STI.PROCESS_RESULT
        ,STI.SEND_TYPE
        ,STI.ERROR_CODE
        ,STI.SEND_TIMES
        ,STI.STATUS_ID
        ,STI.ACCOUNT_DATE
        ,STI.REAL_TIME_FLAG
        ,STI.REMARK
        ,STI.CREATED_TIME
        ,STI.UPDATED_TIME
   FROM SETTLE_TRANS_INVOICE STI 
  WHERE  
STI.DATA_SOURCE = 1 
 AND NOT EXISTS (SELECT STS.BATCH_NO FROM SETTLE_TRANS_SUMMARY STS WHERE STS.BATCH_NO=STI.BATCH_NO)
    AND STI.PROCESS_RESULT IN (lv_status_id);

#--2.4、删除结算表数据
  SET lt_app_segment        = 5;
  DELETE STI.*
    FROM SETTLE_TRANS_INVOICE STI
   WHERE STI.DATA_SOURCE = 1 
	 AND NOT EXISTS (SELECT STS.BATCH_NO FROM SETTLE_TRANS_SUMMARY STS WHERE STS.BATCH_NO=STI.BATCH_NO) 
   AND STI.PROCESS_RESULT IN (lv_status_id); 
  COMMIT;

#--2.5、插入结算表历史数据(文件导入数据)
  SET lt_app_segment        = 6;
  START TRANSACTION;
  INSERT INTO SETTLE_TRANS_INVOICE_HIS(  INVOICE_NO
                                        ,REQUEST_NO
                                        ,BUSSINESS_CODE
																				,MERCHANT_CODE
                                        ,ROOT_INST_CD
                                        ,FUNC_CODE
                                        ,ORDER_TYPE
                                        ,ORDER_NO
                                        ,BATCH_NO
                                        ,GENE_SEQ
                                        ,USER_ID
                                        ,BANK_CODE
                                        ,ACCOUNT_TYPE
                                        ,ACCOUNT_NO
                                        ,ACCOUNT_NAME
                                        ,ACCOUNT_PROPERTY
                                        ,PROVINCE
                                        ,CITY
                                        ,OPEN_BANK_NAME
                                        ,PAY_BANK_CODE
                                        ,AMOUNT
                                        ,CURRENCY
                                        ,CERTIFICATE_TYPE
                                        ,CERTIFICATE_NUMBER
                                        ,DATA_SOURCE
                                        ,PROCESS_RESULT
                                        ,SEND_TYPE
                                        ,ERROR_CODE
                                        ,SEND_TIMES
                                        ,STATUS_ID
                                        ,ACCOUNT_DATE
                                        ,REAL_TIME_FLAG
                                        ,REMARK
                                        ,CREATED_TIME
                                        ,UPDATED_TIME
                                      )
  SELECT STI.INVOICE_NO
        ,STI.REQUEST_NO
        ,STI.BUSSINESS_CODE
				,STI.MERCHANT_CODE
        ,STI.ROOT_INST_CD
        ,STI.FUNC_CODE
        ,STI.ORDER_TYPE
        ,STI.ORDER_NO
        ,STI.BATCH_NO
        ,STI.GENE_SEQ
        ,STI.USER_ID
        ,STI.BANK_CODE
        ,STI.ACCOUNT_TYPE
        ,STI.ACCOUNT_NO
        ,STI.ACCOUNT_NAME
        ,STI.ACCOUNT_PROPERTY
        ,STI.PROVINCE
        ,STI.CITY
        ,STI.OPEN_BANK_NAME
        ,STI.PAY_BANK_CODE
        ,STI.AMOUNT
        ,STI.CURRENCY
        ,STI.CERTIFICATE_TYPE
        ,STI.CERTIFICATE_NUMBER
        ,STI.DATA_SOURCE
        ,STI.PROCESS_RESULT
        ,STI.SEND_TYPE
        ,STI.ERROR_CODE
        ,STI.SEND_TIMES
        ,STI.STATUS_ID
        ,STI.ACCOUNT_DATE
        ,STI.REAL_TIME_FLAG
        ,STI.REMARK
        ,STI.CREATED_TIME
        ,STI.UPDATED_TIME
   FROM SETTLE_TRANS_INVOICE STI  
  WHERE  
STI.DATA_SOURCE = 0  
 AND  STI.PROCESS_RESULT IN (lv_status_id); 

#--2.6、删除结算表数据
  SET lt_app_segment        = 7;
  DELETE STI.*
    FROM SETTLE_TRANS_INVOICE STI 
   WHERE STI.DATA_SOURCE = 0  
	 AND  STI.PROCESS_RESULT IN (lv_status_id);  



#--------------------3、过程结束
  SET ld_end_time           = NOW();
  SET lt_app_segment        = 0;
  SET lm_err_code           = 0;
  SET lv_err_message        = '正确完成';
  SET on_err_code           = lm_err_code;
  CALL PRO_LOG(lv_job_name,lt_app_segment,lm_err_code,lv_err_message,ld_begin_time,ld_end_time);
  COMMIT;
SELECT on_err_code;
END;

