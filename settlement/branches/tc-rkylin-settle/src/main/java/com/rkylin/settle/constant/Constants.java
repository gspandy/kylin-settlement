package com.rkylin.settle.constant;

public class Constants {

	/** 日期格式 */
	// yyyy-MM-dd HH:mm:ss
	public static final String DATE_FORMAT_YYYYMMDDHHMMSS = "yyyy-MM-dd HH:mm:ss";
	// yyyy-MM-dd HH:mm:ss
	public static final String DATE_FORMAT_YYYYMMDDHHMMSSSSS = "yyyy-MM-dd HH:mm:ss.SSS";
	public static final String DATE_FORMAT_YYYYMMDD = "yyyy-MM-dd";
	/** 字符集 */
	// UTF-8
	public static final String CHARSET_UTF8 = "UTF-8";
	// GBK
	public static final String CHARSET_GBK = "GBK";

	/** 数据格式 */
	// XML
	public static final String DATA_PROTOCOL_TYPE_XML = "xml";
	// JSON
	public static final String DATA_PROTOCOL_TYPE_JSON = "json";
	
	/** 签名方式 */
	// MD5
	public static final String SIGN_TYPE_MD5 = "md5";

	/** 系统参数名 */
	// method
	public static final String SYS_PARAM_METHOD = "method";
	// format
	public static final String SYS_PARAM_FORMAT = "format";
	// session
	public static final String SYS_PARAM_SESSION = "session";
	// timestamp
	public static final String SYS_PARAM_TIMESTAMP = "timestamp";
	// app_key
	public static final String SYS_PARAM_APP_KEY = "app_key";
	// sign
	public static final String SYS_PARAM_SIGN = "sign";
	
	// 协议状态_失效
	public static final int AGMT_STATUS_0 = 0;
	// 协议状态_生效
	public static final int AGMT_STATUS_1 = 1;
	
	// 融数ID
	public static final String RS_ID = "M00000X";
	// 丰年ID
	public static final String FN_ID = "M000001";
	// P2PID
	public static final String P2P_ID = "M000002";
	// 会唐ID
	public static final String HT_ID = "M000003";
	// 课栈ID
	public static final String KZ_ID = "M000004";
	// 会唐----会场云
	public static final String HT_CLOUD_ID = "M0000031";
	/**
	 * 君融贷机构号ID
	 */
	public static final String JRD_ID="M000005";
	/**
	 * 食全食美机构号ID
	 */
	public static final String SQSM_ID="M000006";
	/**
	 * 棉庄机构号ID
	 */
	public static final String MZ_ID="M000007";
	/**
	 * 展酷机构号ID
	 */
	public static final String ZK_ID="M000011";
	/**
	 * 指尖ID
	 */
	public static final String ZJ_ID="M000012";
	/**
	 * 卖家云机构号ID
	 */
	public static final String MJY_ID="M000013";
	/**
	 * 通信运维ID
	 */
	public static final String TXYW_ID="M000014";
	/**
	 * 融德ID
	 */
	public static final String RD_ID = "RONGDE";
	/**
	 * 天下房仓ID
	 */
	public static final String TXFC_ID="M000016";
	/**
	 * 企业白条ID
	 */
	public static final String QYBT_ID="M000017";
	/**
	 * 帮帮助学ID
	 */
	public static final String BBZX_ID="M000020";
	/**
	 * 领客科技ID
	 */
	public static final String LKKJ_ID="M000024";
	/**
	 * 悦视觉ID
	 */
	public static final String YSJ_ID="M000025";
	/**
	 * 融数钱包ID
	 */
	public static final String RSQB_ID="M666666";
	/**
	 * 融单平台ID
	 */
	public static final String RDPT_ID="M000033";
	/**
	 * 沃雷特ID
	 */
	public static final String WLT_ID="MX00001";
	/**
	 * 丰盈ID
	 */
	public static final String FYBL_ID="N000001";
	/**
	 * FN 主账户
	 */
	public static final String FN_PRODUCT = "P000002";
	/**
	 * P2P授信子账户
	 */
	public static final String USER_SUB_ACCOUNT = "P000003";
	/**
	 * 预付金子账户
	 */
	public static final String ADVANCE_ACCOUNT = "P000004";
	/**
	 * 会唐主账户
	 */
	public static final String HT_PRODUCT="P000005";
	/**
	 * 会唐授信子账户
	 */
	public static final String HT_CREDIT_ACCOUNT="P000006";

	/**
	 * 会唐场地方子账户
	 */
	public static  final String HT_CHANGDIFANG_ACCOUNT="P000007";
	/**
	 *课栈主账户
	 */
	public static final String KZ_PRODUCT="P000008";
	/**
	 * 课栈授信子账户
	 */
	public static final String KZ_CREDIT_ACCOUNT="P000009";
	/**
	 * 丰年红包子账户
	 */
	public static final String FN_RED_PACKET="P000010";
	//账号目的:
	/**
	 * 1结算卡
	 */
	public static final String ACCOUNT_PURPOSE_1 = "1";
	/**
	 * 2其他卡
	 */
	public static final String ACCOUNT_PURPOSE_2 = "2";
	/**
	 * 3提现卡
	 */
	public static final String ACCOUNT_PURPOSE_3 = "3";
	/**
	 * 4提现卡,结算卡 同一张
	 */
	public static final String ACCOUNT_PURPOSE_4 = "4";
	/**
	 * 代收付结果正常
	 */
	public static final int GENERATION_PAYMENT_SEND_TYPE_0 = 0;
	/**
	 * 代收付结果失败
	 */
	public static final int GENERATION_PAYMENT_SEND_TYPE_1 = 1;
	/**
	 * 代收付状态失效
	 */
	public static final int GENERATION_PAYMENT_STATUS_0 = 0;
	/**
	 * 代收付状态生效
	 */
	public static final int GENERATION_PAYMENT_STATUS_1 = 1;
	/**
	 * 丰年代收批次号00
	 */
	public static final String FN_FILE_BATCH_00="00";
	/**
	 * 丰年代付批次号01
	 */
	public static final String FN_FILE_BATCH_01="01";
	/**
	 * 会堂代收批次号02
	 */
	public static final String HT_FILE_BATCH_02="02";
	/**
	 * 会堂代付批次号03
	 */
	public static final String HT_FILE_BATCH_03="03";
	/**
	 * 会堂文件类型
	 */
	public static final String HT_FILE_TYPE_6="6";
	
	/**
	 * 银行卡状态失效
	 */
	public static final int ACCOUNT_NUM_STATRS_0=0;
	/**
	 * 银行卡状态生效
	 */
	public static final int ACCOUNT_NUM_STATRS_1=1;
	/**
	 * 对公账户确认时,银行卡待审核,需要一分钱完成
	 */
	public static final int ACCOUNT_NUM_STATRS_2=2;
	/**
	 * 定时任务将银行卡状态设置为审核中
	 */
   public static final int ACCOUNT_NUM_STATRS_3=3;
     /**
      * 订单返回失败
      */
	public static final int ACCOUNT_NUM_STATRS_4=4;
	/**
     *查询状态为1，2，3，4的数据用5
     */
	public static final int ACCOUNT_NUM_STATRS_OK_ALL=5;
	
	/**
	 * 预付金余额查询-以商户Id为主 查询相关联的台长余额信息
	 */
	public static final String QUERY_BALANCE_TYPE_1="1";
	/**
	 * 预付金余额查询-以台长Id为主 查询相关联的商户预付金余额信息
	 */
	public static final String QUERY_BALANCE_TYPE_2="2";
	/**
	 * 预付金余额查询-商户类型
	 */
	public static final int ADVANCE_BALANCE_TYPE_0=0;
	/**
	 * 预付金余额查询-台长类型
	 */
	public static final int ADVANCE_BALANCE_TYPE_1=1;
	/**
	 * 预付金余额查询-当日金额类型
	 */
	public static final int ADVANCE_BALANCE_TYPE_2=2;
	/**
	 * FinanaceCompany basetype 
	 */
	public static final int BASE_FINANACECOMPANY_TYPE = 1;
	/**
	 * FinanacePerson baseType
	 */
	public static  final  int BASE_FINANACEPERSON_TYPE = 1;
	/**
	 * 卡属性为对公
	 */
	public static  final String ACCOUNT_PROPERTY_PUBLIC = "1";
	/**
	 * 卡属性为对私
	 */
	public static  final String ACCOUNT_PROPERTY_PRIVATE = "2";
	/**
	 * 对公一分钱，调用代付接口，备注字段
	 */
	public static  final String TRANS_ORDER_REMARK = "qjs_tuikuan";
	
	/**
	 * 短信编号0
	 */
	public static final int SMS_NUMBER_0 = 0; //"备付金余额不足"
	
	/**
	 * 短信编号1
	 */
	public static final int SMS_NUMBER_1 = 1; //"退票发送账户失败或异常"

	/**
	 * 短信编号2
	 */
	public static final int SMS_NUMBER_2 = 2; //"通联代付给平安银行交易生成失败或异常"
	/**
	 * 短信编号3
	 */
	public static final int SMS_NUMBER_3 = 3; //"会计系统,系统日切失败"
}
