package com.rkylin.settle.constant;

import com.rkylin.settle.util.PathPropertiesUtil;

public class SettleConstants {
	// 账期一期
	public static final String ACCOUNTDATE_OLD = "AccountTerm";
	// 账期一期parameterType
	public static final String ACCOUNTDATE_TYPE_OLD = "0";
	// 账期
	public static final String ACCOUNTDATE = "sysAccountDate";
	// 账期parameterType
	public static final String ACCOUNTDATE_TYPE = "1";
	// 账单日
	public static final String BILLDAY = "BillDay";
	// 还款日
	public static final String REPAYMENTDAY = "RepaymentDay";
	// 账户一期 日终标记
	public static final String DAYEND_OLD = "DayEnd";
	// 账户一期 日终标记类型
	public static final String DAYEND_TYPE_OLD = "0";
	// 日终标记
	public static final String DAYEND = "sysFlag";
	// 会计日终标记类型
	public static final String DAYEND_TYPE = "1";

	// 授信商户---丰年
	public static final String CON_FN = "FN";
	// 授信机构---君融贷
	public static final String CON_JRD = "JRD";
	
	// 授信种类-额度
	public static final String LIMIT_CN = "额度";
	public static final String LIMIT = "1";
	public static final int CREDIT_TYPE_ID_LIMIT = 101;

	// 授信种类-单笔
	public static final String SINGLE_CN = "单笔";
	public static final String SINGLE = "";
	public static final int CREDIT_TYPE_ID_SINGLE = 102;

	// 审核结果-通过
	public static final String PASS = "OK";

	// 审核结果-失败
	public static final String FAIL = "NG";

	// 审核结果-需再审
	public static final String RETRIAL = "需再审";

	// 对账每日时间
	public static final String DEDT_ACCOUNT_DATE = "{YMD} 23:30:00";
	

	// 结算种类-债权包
	public static final int SETTLE_TYPE_1 = 1;
	// 结算种类-提现
	public static final int SETTLE_TYPE_2 = 2;
	// 结算种类-还款
	public static final int SETTLE_TYPE_3 = 3;
	// 结算种类-预付金
	public static final int SETTLE_TYPE_4 = 4;
	// 结算种类-分润
	public static final int SETTLE_TYPE_5 = 5;
	// 清算状态-完成
	public static final int SETTLE_STU_1 = 1;
	// 清算状态-失败
	public static final int SETTLE_STU_0 = 0;
	// 还款逾期
	public static final int SETTLE_STU_2 = 2;
	
	// 对账状态-平账
	public static final int COLLATE_STU_1 = 1;
	// 对账状态-错帐
	public static final int COLLATE_STU_0 = 0;
	// 对账状态-长款
	public static final int COLLATE_STU_2 = 2;
	// 对账状态-短款
	public static final int COLLATE_STU_3 = 3;
	// 对账文件生成路径
	public static final String FILE_PATH = PathPropertiesUtil.getPathProperties().getProperty("fileDPath");
	public static final String FILE_UP_PATH = PathPropertiesUtil.getPathProperties().getProperty("fileUPath");
	public static final String TOMCAT_ROOT_PATH = PathPropertiesUtil.getPathProperties().getProperty("tomcatPath");
	// 商户平台文件目录
	public static final String SHPT_PATH = PathPropertiesUtil.getPathProperties().getProperty("shptPath");
	public static final String SHPT_EXCEL_MODEL = "shpt_excel_model.xlsx";
	// 邮件附件
	public static final String MAIL_FILE_PATH = PathPropertiesUtil.getPathProperties().getProperty("mailFilePath");
	public static final String FILE_XML = "xml";
	public static final String FILE_CSV = "csv";
	public static final String FILE_XLS = "xls";
	public static final String FILE_XLSX = "xlsx";
	// 杏仁现金贷
	public static final String MODELS_PATH = PathPropertiesUtil.getPathProperties().getProperty("modelsPath");
	public static final String XR_XJD_PATH = PathPropertiesUtil.getPathProperties().getProperty("xrXjdPath");
	public static final String XR_XJD_MODEL = "现金贷-{YMD}-{NUM}.xls";
	// 债权包取得时间
	public static final String DEDT_DATE = "{YMD} 23:30:00";
	//文件列分隔符
	public static final String DEDT_SPLIT = "|";
	public static final String DEDT_SPLIT2 = ",";
	public static final String DEDT_SPLIT3 = " ";
	public static final String DEDT_SPLIT4 = "|$|";
	public static final String DEDT_SPLIT5 = "|::|";
	public static final String DEDT_SPLIT6 = "&";
	//清算订单类型
	/**
	 * 债权包
	 */
	public static final int ORDER_BOND_PACKAGE= 1;
	/**
	 * 提现
	 */
	public static final int ORDER_WITHDRAW=2;
	/**
	 * 信用还款
	 */
	public static final int ORDER_REPAYMENT=3;
	/**
	 * 预付金
	 */
	public static final int ORDER_ADVANCE=4;
	/**
	 * 代收
	 */
	public static final int ORDER_COLLECTION=5;
	/**
	 * 代付
	 */
	public static final int ORDER_WITHHOLD=6;
	/**
	 * 贷款还款
	 */
	public static final int ORDER_LOAN_REPAYMENT=7;	
	/*发送类型*/
	/**
	 * 正常
	 */
	public static final int SEND_NORMAL=0;
	/**
	 * 代扣失败
	 */
	public static final int SEND_DEFEAT =1;
	/**
	 * 代扣延迟
	 */
	public static final int SEND_DELAY=2;
	/*状态*/
	/**
	 * 失效
	 */
	public static final int INVALIDATION =0;
	/** 
	 * 生效
	 */
	public static final int TAKE_EFFECT =1;
	/**
	 * 通联-实时/批量代收业务代码
	 */
	public static final String COLLECTION_CODE ="19900";
	/**
	 * 通联-实时代付业务代码
	 */
	public static final String WITHHOLD_CODE ="09900";
	/**
	 * 通联-批量代付业务代码
	 */
	public static final String WITHHOLD_BATCH_CODE ="09500";
	/**
	 * ROP-代扣/代收明细文件批次号
	 */	
	public static final String ROP_COLLECTION_BATCH_CODE ="10000001";
	/**
	 * ROP-还款明细文件批次号
	 */	
	public static final String ROP_REPAYMENT_BATCH_CODE ="10000002";	
	/**
	 * ROP-代收文件批次号
	 */	
	public static final String ROP_RECEIVE_BATCH_CODE ="10000003";
	/**
	 * ROP-代付文件批次号
	 */	
	public static final String ROP_PAYMENT_BATCH_CODE ="10000004";
	
	/************清洁算调用多渠道签名密码**************/
	public static final String MULTI_KEY = "qjsxt100011";
	public static final String MULTI_HAED = "qjsxt001";
	public static final String MULTI_TRANS_CODE = "16001"; //账户信息查询（其他账户余额查询）（交易编码）
	public static final String MULTI_BUSI_CODE = "16011"; //银企直联（业务编码）
	public static final String MULTI_CHANNAL_NO_04 = "160604"; //平安银企账户平安银企主账户余额查询
	public static final String MULTI_CHANNAL_NO_05 = "160605"; //平安银企账户平安银企子账户余额查询
	public static final String MULTI_CHANNAL_NO_06 = "160606"; //平安银企账户平安银企主子账户关系查询
	public static final String MULTI_CHANNAL_NO_07 = "160607"; //平安银企账户当日历史交易明细查询（渠道号）
	public static final String MULTI_CHANNAL_NO_08 = "160608"; //平安银企账户台账查询
	public static final String MULTI_CHANNAL_NO_12 = "160612"; //平安银企账户历史余额查询
	
	/************清洁算数据状态 start**************/
	public static final int STATUS_FAILD =0; //交易失败
	public static final int STATUS_SUCCESS =1; //交易成功
	public static final int STATUS_UNKOWN =2; //交易不明
	public static final int STATUS_PROFIT_FALID =10; //清分失败
	public static final int STATUS_PROFIT_SUCCESS =11; //清分成功
	public static final int STATUS_COLLATE_ERROR =20; //对账失败 异常
	public static final int STATUS_COLLATE_SUCCESS =21; //对账成功 平账
	public static final int STATUS_COLLATE_FIALD =22; //错帐	
	public static final int STATUS_COLLATE_GAIN =23; //上游渠道有数据 咱们没有  长款
	public static final int STATUS_COLLATE_LOSS =24; //上游渠道没有数据 咱们有  短款
	public static final int STATUS_SETTLE_FAILD =40; //清算失败
	public static final int STATUS_SETTLE_SUCCESS =41; //清算成功
	/************清洁算数据状态 end****************/
	
	/************机构号 start****************/
	public static final String PAY_CHANNEL_ID_TL ="01"; //上游渠道-通联
	public static final String PAY_CHANNEL_ID_ALI ="02"; //上游渠道-支付宝
	public static final String PAY_CHANNEL_ID_WECHAT ="03"; //上游渠道-微信
	public static final String PAY_CHANNEL_ID_LIANLIAN ="04"; //上游渠道-连连支付
	public static final String PAY_CHANNEL_ID_UMPAY ="05"; //上游渠道-联动优势
	public static final String PAY_CHANNEL_ID_CHANPAY ="S01"; //上游渠道-畅捷支付
	public static final String PAY_CHANNEL_ID_PAB ="Y01"; //上游渠道-平安银行银企直联
	public static final String PAY_CHANNEL_ID_CMBC ="Y02"; //上游渠道-民生银行银企直联
	public static final String PAY_CHANNEL_ID_RB ="S02"; //上游渠道-融宝
	public static final String PAY_CHANNEL_ID_YB ="S03"; //上游渠道-易宝
	public static final String PAY_CHANNEL_ID_LYCHEE ="S04"; //上游渠道-快付通
	public static final String PAY_CHANNEL_ID_REPAY ="P01"; //上游渠道-快付通
	/************机构号 end****************/
	
	/************对账文件特殊列 start******************/
	public static final String SPECIAL_COLUMN_ACCOUNT = "accountDate";//账期
	public static final String SPECIAL_COLUMN_TOTAL_AMOUNT = "totalAmount";//金额汇总
	public static final String SPECIAL_COLUMN_TOTAL_FCAMOUNT = "totalFCAmount";//最终金额对账汇总
	public static final String SPECIAL_COLUMN_TOTAL_COUNT = "totalCount";//总条数
	/************对账文件特殊列 end ******************/
	
	/************交易类型编号 start******************/
	public static final String ACCOUNT_TYPE_CHANNEL ="01"; //通联 - 网关支付
	public static final String ACCOUNT_TYPE_GENERATE ="02"; //通联 - 代收付
	public static final String ACCOUNT_TYPE_MOBILE ="03"; //通联 - 移动支付
	public static final String ACCOUNT_TYPE_LLKJ ="04"; //连连支付 - 移动快捷支付
	public static final String ACCOUNT_TYPE_PAB ="05"; //平安银行 - 银企直联
	public static final String ACCOUNT_TYPE_WXMOBILE ="06"; //通联 - 微信支付
	public static final String ACCOUNT_TYPE_CMBC ="07"; //民生银行 - 银企直联
	public static final String ACCOUNT_TYPE_TLSDK ="08"; //通联 - SDK
	public static final String ACCOUNT_TYPE_WXAPP ="09"; //通联 -	微信APP
	public static final String ACCOUNT_TYPE_POS ="10"; //pos收单交易万众
	public static final String ACCOUNT_TYPE_PABKHKF ="11"; //平安银行 - 跨行快付
	public static final String ACCOUNT_TYPE_PABB2B ="12"; //平安银行 - B2B现货
	public static final String ACCOUNT_TYPE_YB_AUTH ="AUTH"; //易宝-鉴权绑卡
	public static final String ACCOUNT_TYPE_YB_PAYMENT ="PAYMENT"; //易宝-支付
	public static final String ACCOUNT_TYPE_YB_WITHDRAW ="WITHDRAW"; //易宝-提现
	public static final String ACCOUNT_TYPE_YB_CHANGECARD ="CHANGECARD"; //易宝-换卡
	public static final String ACCOUNT_TYPE_YB_REFUND ="REFUND"; //易宝-退款
	public static final String ACCOUNT_TYPE_YB_PAYDIVIDE ="PAYDIVIDE"; //易宝-分账
	public static final String ACCOUNT_TYPE_CMBC_AgentPay ="AgentPay"; //民生银行 - 单笔联机代付
	public static final String ACCOUNT_TYPE_CMBC_AgentPay_DL ="AgentPay_DL"; //民生银行 - 单笔联机代付(代理)
	public static final String ACCOUNT_TYPE_CMBC_AgentPay_FDL ="AgentPay_FDL"; //民生银行 - 单笔联机代付(非代理)
	public static final String ACCOUNT_TYPE_CMBC_Refund ="Refund"; //民生银行 - 单笔联机代付退票
	public static final String ACCOUNT_TYPE_CMBC_Refund_DL ="Refund_DL"; //民生银行 - 单笔联机代付退票(代理)
	public static final String ACCOUNT_TYPE_CMBC_Refund_FDL ="Refund_FDL"; //民生银行 - 单笔联机代付退票(非代理)
	public static final String ACCOUNT_TYPE_POS_HT = "13"; //pos收单交易会唐国旅
	public static final String ACCOUNT_TYPE_YB_DF ="DF"; //易宝-代付
	public static final String ACCOUNT_TYPE_RB_DF ="RB_DF"; //融宝代付
	/************交易类型编号 end******************/
	
	/************交易类型编码 start******************/
	public static final String ACCOUNT_COED_CHANNEL ="WG"; //通联 - 网关支付
	public static final String ACCOUNT_COED_GENERATE ="ZF"; //通联 - 代收付
	public static final String ACCOUNT_COED_GENERATE_F ="ZFF"; //通联 - 代付
	public static final String ACCOUNT_COED_GENERATE_Z ="ZFZ"; //通联 - 代收直联
	public static final String ACCOUNT_COED_GENERATE_J ="ZFJ"; //通联 - 代收间联
	public static final String ACCOUNT_COED_MOBILE ="YD"; //通联 - 移动支付
	public static final String ACCOUNT_COED_WXMOBILE ="WX"; //通联 - 微信支付
	public static final String ACCOUNT_COED_TLSDK ="TLSDK"; //通联 - SDK
	public static final String ACCOUNT_COED_WXAPP ="WXAPP"; //通联 - 微信APP
	public static final String ACCOUNT_COED_LLKJ ="LLKJ"; //连连支付 - 移动快捷支付
	public static final String ACCOUNT_COED_PAB ="PAYQ"; //平安银行 - 银企直联
	public static final String ACCOUNT_COED_CMBC ="MSYQ"; //民生银行 - 银企直联
	public static final String ACCOUNT_COED_PABKHKF ="PABKHKF"; //平安银行 - 跨行快付
	public static final String ACCOUNT_COED_PABB2B ="PABB2B"; //平安银行 - B2B现货
	public static final String ACCOUNT_COED_CMBC_AgentPay ="AgentPay"; //民生银行 -单笔联机代付
	public static final String ACCOUNT_COED_CMBC_AgentPay_DL ="AgentPay_DL"; //民生银行 -单笔联机代付(代理)
	public static final String ACCOUNT_COED_CMBC_AgentPay_FDL ="AgentPay_FDL"; //民生银行 -单笔联机代付(非代理)
	public static final String ACCOUNT_COED_CMBC_Refund ="Refund"; //民生银行 -单笔联机代退票
	public static final String ACCOUNT_COED_CMBC_Refund_DL ="Refund_DL"; //民生银行 -单笔联机代退票(代理)
	public static final String ACCOUNT_COED_CMBC_Refund_FDL ="Refund_FDL"; //民生银行 -单笔联机代退票(非代理)
	public static final String ACCOUNT_COED_YB_DF ="DF"; //易宝-代付
	public static final String ACCOUNT_COED_RB_DF ="RB_DF"; //融宝代付
	
	/************交易类型编码 end******************/
	
	/************PARAMETER_INFO 字典表配置 start******************/
	public static final String PARAMETER_TYPE_SETTLETIME ="1000000001";//上游交易时间取得code
	public static final String PARAMETER_TYPE_MERCHANT ="1000000002";//机构号对应关系
	public static final String PARAMETER_TYPE_FUNC ="1000000003";//功能编码字典
	public static final String FUNC_CODE_TRANS_TYPE ="1000000004";//多渠道和清算交易类型关系
	public static final String READ_TYPE_TRANS_TYPE ="1000000005";//多渠道和代收/付对应关系
	public static final String PARAMETER_TYPE_TRANS_STATUS ="1000000006";//交易状态常量 - 读取通联代收付对账文件的功能编码
	public static final String PARAMETER_TYPE_ACCOUNT_STATUS ="1000000007";//账户交易信息常量 - 账户交易信息状态，清结算状态对应关系
	public static final String PARAMETER_TYPE_PAY_CHANNEL ="1000000008";//渠道名称和编码的对应关系
	public static final String PARAMETER_TYPE_ACCOUNT_ORDER_NO ="1000000009";//订单号对应规则 账户系统
	public static final String PARAMETER_TYPE_ACCOUNT_AMOUNT="1000000010";//金额对应规则 账户系统
	public static final String ROOT_ENTERPRISE_ACCOUNT_USER_ID="1000000012";//金额对应规则 账户系统
	public static final String PARAMETER_DOWN_MERCHANT="1000000013";//商户编码信息
	public static final String PARAMETER_TYPE_FUN_CODE="1000000014";//功能编码对应规则(清算FUNC_CODE对应账户DEAL_PRODUCT_CODE)
	public static final String PARAMETER_TYPE_EMERGENCY_CONTACT_MOBILE="1000000015";//紧急联系人手机号
	public static final String PARAMETER_TYPE_STATUS_IDS="1000000016";//紧急联系人手机号
	public static final String PARAMETER_TYPE_DSF_ONOFF="2000000001";//汇总开关
	public static final String PARAMETER_TYPE_DSF_MERCHANT ="1000000017";//代收付业务机构号对应关系
	public static final String PARAMETER_TYPE_UNKNOW_STATUS ="1000000018";//渠道号信息
	public static final String PARAMETER_TYPE_FUN_INFO ="1000000019";//交易编码信息
	public static final String PARAMETER_TYPE_ACCOUNT_INFO ="1000000022";//贷款还款账户信息
	public static final String PARAMETER_TYPE_COLLECT_MQKEY = "1000000023";	//会计汇总记账ActiveMQ的Key
	public static final String PARAMETER_TYPE_MAIL_TO = "1000000024";	//发送邮件To对象
	public static final String PARAMETER_TYPE_MAIL_CC = "1000000025";	//发送邮件Cc对象
	public static final String PARAMETER_TYPE_CMB_B= "1000000026";	//银行对应关系
	public static final String PARAMETER_TYPE_CMB_P = "1000000027";	//省对应关系
	public static final String PARAMETER_TYPE_CMB_C = "1000000028";	//市对应关心
	
	public static final String PROFIT_FUNC_CODES = "PROFIT_FUNC_CODES";//分润的功能码
	public static final String BILL_FUNC_CODES = "BILL_FUNC_CODES";//挂账的功能码
	public static final String CORR_FUNC_CODE = "10012_c";//冲正交易funcCode
	public static final String WIPEOUT_FUNC_CODE = "10012_m";//抹账交易funcCode
	public static final String SENDBACK_FUNC_CODE = "5024";//退票交易funcCode
	public static final String PROFIT_STATUS_IDS = "PROFIT_STATUS_IDS";//退票交易funcCode
	/************PARAMETER_INFO 字典表配置 end******************/
	
	/************代收付的常量配置  start******************/
	public static  final String DSF_FUNC_CODES = ",4013,4014,4016,";
	public static final String BATCH_DS_FUN_CODE ="4013";//批量代收 func_code
	public static final String BATCH_DF_FUN_CODE ="4014";//批量代付 func_code
	public static final String BATCH_DF_YFQ_FUN_CODE ="4014_1";//一分钱代付
	public static final String BATCH_TX_FUN_CODE ="4016";//提现 func_code
	public static final String BATCH_DF3_FUN_CODE ="40143";//民生 func_code
	public static final String BATCH_DF4_FUN_CODE ="40144";//民生 func_code
	public static final String BATCH_DF7_FUN_CODE ="40147";//民生 func_code
	public static final String BATCH_DF9_FUN_CODE ="40149";//民生 func_code
	public static final int DSF_NOT_DEAL =0;//未处理
	public static final int DSF_HAS_SEND =1;//发送成功
	public static final int DSF_SEND_FAIL =2;//发送失败
	public static final int DSF_DEAL_SUC =3;//处理成功
	public static final int DSF_HAS_RETURN =5;//已经返回
	public static final int DSF_HAS_WRITE =6;//已回写
	public static final int DSF_DEAL_FAIL =4;//处理失败
	public static final int EXCEPTION =99;//异常
	public static final int EXCEPTION_96 =96;//代收付返回成功，但是发送账户异常
	public static final int EXCEPTION_94 =94;//代收付返回失败，但是发送账户异常
	public static final int DSF_DATA_SOURCE_FILE =0;//文件导入
	public static final int DSF_DATA_SOURCE_SYSTEM =1;//系统推送
	public static final int DSF_DATA_SOURCE_MANUAL =2;//手工重发
	public static final int DSF_SEND_TYPE_0 =0;//0正常
	public static final int DSF_SEND_TYPE_1 =1;//1代扣失败
	public static final int DSF_SEND_TYPE_2 =2;//2代扣延迟
	public static final int STATUSID_0 =0;//未处理
	public static final int STATUSID_1 =1;//处理中
	public static final int STATUSID_15 =15;//成功
	public static final int STATUSID_13 =13;//失败
	public static final String REFUND_DEAL_PRODUCT_CODE ="PROD_00_RS_0020";//账户侧默认的退票的deal_product_code
	/************代收付的常量配置  end******************/

	/************汇总  start******************/
	public static final int COLLECT_TYPE_1 = 1;//多渠道数据
	public static final int COLLECT_TYPE_2 = 2;//账户二期数据
	public static final int COLLECT_TYPE_3 = 3;
	public static final int COLLECT_TYPE_4 = 4;
	public static final int COLLECT_TYPE_5 = 5;
	/************汇总  end******************/
	
	/************mtaegis 交易产品号 start******************/
	public static final String PROFIT_BALANCE_DPC = "PROD_00_RS_0032";//分润交易产品号
	public static final String BILL_PAY_DPC = "PROD_00_RS_0043";//挂账后支付产品号
	public static final String ACC_OF_DPC = "PROD_00_RS_0021";//冲正or抹账产品号
	public static final String REFUND_DPC = "PROD_00_RS_0019";//消费后退款产品号
	public static final String BALANCE_QUERY_DPI = "PROD_00_RS_0044";//查询账户余额dealProductId
	/************mtaegis 交易产品号 end******************/
	
	/************银企直联银行账号配置 start******************/
	public static final String PAB_ACCOUNT_NO1 ="PAYQ"; //平安银行 - 银企直联
	public static final String PAB_ACCOUNT_NO_MAIN1 ="11015089146001";
//	public static final String PAB_ACCOUNT_NO_MAIN1 ="11002873383101";
	public static final String CMBC_ACCOUNT_NO1 ="MSYQ"; //民生银行 - 银企直联
	public static final String CMBC_ACCOUNT_NO_MAIN1 ="11015089146001";
//	public static final String CMBC_ACCOUNT_NO_MAIN1 ="11002873383101";
	/************银企直联银行账号配置 字典表配置 end******************/
	/*
	 * 默认的UserIpAddress
	 */
	public static final String LOCALHOST_URL = "127.0.0.1";
}
