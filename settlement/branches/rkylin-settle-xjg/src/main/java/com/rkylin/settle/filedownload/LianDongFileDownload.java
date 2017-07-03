package com.rkylin.settle.filedownload;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rkylin.file.txt.TxtWriter;
import com.rkylin.settle.constant.SettleConstants;
import com.rkylin.settle.manager.ParameterInfoManager;
import com.rkylin.settle.pojo.ParameterInfo;
import com.rkylin.settle.pojo.ParameterInfoQuery;
import com.rkylin.settle.util.HttpProtocal;
import com.rkylin.settle.util.SFTPUtil;
import com.rkylin.settle.util.SettlementUtil;
import com.rkylin.utils.RkylinMailUtil;

@Component("lianDongFileDownload")
public class LianDongFileDownload extends CheckfileDownload {
	private static Logger logger = LoggerFactory.getLogger(LianDongFileDownload.class);
    @Autowired
    Properties fileProperties;
    @Autowired
	SettlementUtil settlementUtil;
	@Autowired
	SFTPUtil ftp;
	@Autowired
	ParameterInfoManager parameterInfoManager;
    /**
     * 下载联动优势对账文件【无账期】: 下载T-1日交易对账文件
     * @param merchantCode
     * @param readType
     * @return
     */
    public Map<String, String> lianDongFileDown(String merchantCode, String fileType) {
    	return this.lianDongFileDown(null, merchantCode, fileType);
    }
    /**
     * 下载联动优势对账文件
     * @param invoicedate
     * @param merchantCode
     * @param readType
     * @return
     */
    public Map<String, String> lianDongFileDown(String invoicedate, String merchantCode, String fileType) {
    	logger.info("下载 【联动优势】 ["+ merchantCode +"]协议 & ["+ fileType +"]交易类型 对账文件 ————————————START————————————");
    	//返回信息map
    	Map<String, String> rtnMap = new HashMap<String, String>();
    	//默认信息码
		rtnMap.put("errCode", "0000");
		//默认信息
		rtnMap.put("errMsg", "成功");
		/**
    	 * 判断日中是否正常结束
    	 */
		//配置表query对象
//    	ParameterInfoQuery keyList =  new ParameterInfoQuery();
    	//查询日终的条件
//    	keyList.setParameterCode(SettleConstants.DAYEND);
    	//查询结果List
//    	List<ParameterInfo> parameterInfo = parameterInfoManager.queryList(keyList);
    	//判断结果,0标示正常日终
		if (!settlementUtil.cutDayIsSuccess4Account()) {
    		rtnMap.put("errCode", "0001");
    		rtnMap.put("errMsg", "日终没有正常结束！");
    		RkylinMailUtil.sendMailThread("清洁算开始异常","日终没有正常结束，不能开始清洁算操作", "21401233@qq.com");
    		return rtnMap;
    	}
    	/**
    	 * 获取账期
    	 */
    	//settle封装的日期工具类
    	String accountDate = "";	//T-1日账期字符串
    	String accountDate2 = "";	//T日账期字符串
    	try {
	    	if (invoicedate == null || "".equals(invoicedate)) {
//	    		keyList.setParameterCode(SettleConstants.ACCOUNTDATE);
//	    		invoicedate = parameterInfoManager.queryList(keyList).get(0).getParameterValue();
	    		invoicedate = String.valueOf(settlementUtil.getAccountDate("yyyy-MM-dd", 0, "String"));
	    		accountDate = String.valueOf(settlementUtil.getAccountDate(invoicedate, "yyyyMMdd", -1, "String"));
				accountDate2 = String.valueOf(settlementUtil.getAccountDate(invoicedate, "yyyyMMdd", 0, "String"));
	    	} else {
	    		accountDate = invoicedate.replace("-", "");
	    		accountDate2 = String.valueOf(settlementUtil.getAccountDate(invoicedate, "yyyyMMdd", 1, "String"));
	    	}
	    } catch (Exception e2) {
	    	logger.error("计算账期异常！" + e2.getMessage());
			rtnMap.put("errCode", "0001");
			rtnMap.put("errMsg", "计算账期异常！");
			return rtnMap;
	    }
    	logger.info(">>> >>> 【指定账期下载联动优势支付对账文件】  取得的账期为 :"+ accountDate);
    	// 上游渠道对账文件取得方式待定
    	String filenameProfix = "LD_" + fileType + "_" + accountDate + "_" + merchantCode;
		String bakfilename = filenameProfix + "_FOREMOST.txt";
		String filename = filenameProfix + ".txt";
    	String oldPath = SettleConstants.FILE_PATH + accountDate2 + File.separator + "ld_bak" + File.separator;
    	String newPath = SettleConstants.FILE_PATH + accountDate2 + File.separator;
    	File file = new File(oldPath);
    	if (!file.exists()) {
    		file.mkdirs();
    	}
		logger.info("获取对账文件，调用P2P接口");
		try {
			boolean fileflg = makeLianDongproperties(merchantCode, fileType);
			if (!fileflg) {
	    		rtnMap.put("errCode", "0001");
	    		rtnMap.put("errMsg", "联动优势对账文件配置失败！");
				return rtnMap;
			}
			fileflg = downloadFile(oldPath + bakfilename, accountDate, fileType);
			if (!fileflg) {
	    		rtnMap.put("errCode", "0001");
	    		rtnMap.put("errMsg", "联动优势对账文件获取失败！");
				return rtnMap;
			}
		} catch (Exception e1) {
			logger.error("获取联动优势对账文件异常！" + e1.getMessage());
			return rtnMap;
		}
		logger.info("下载 联动优势 ["+ merchantCode +"]协议 & ["+ fileType +"]交易类型 对账文件  下载完成!");
		try {
			boolean isSuccess = false;
    		if(SettleConstants.ACCOUNT_COED_CHANNEL.equals(fileType)) {
    			isSuccess = this.editNewLianDongWGCollateFile(oldPath, newPath, filenameProfix);
    		} else if(SettleConstants.ACCOUNT_COED_GENERATE.equals(fileType)) {
    			isSuccess = this.editNewLianDongZFCollateFile(oldPath, newPath, filenameProfix);
    		} else {
    			throw new Exception("下载联动对账文件异常: 错误的支付类型 - " + fileType);
    		}
    		if(!isSuccess) {
    			logger.error(">>> >>> >>> 编辑【联动】支付对账文件 执行失败!");
    			rtnMap.put("errCode", "0001");
    			rtnMap.put("errMsg", "编辑【联动】支付对账文件 执行失败!");
    			return rtnMap;
    		}
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 编辑【联动】支付对账文件 执行失败!" + e.getMessage());
			e.printStackTrace();
			rtnMap.put("errCode", "0001");
			rtnMap.put("errMsg", "编辑【联动】支付对账文件 执行失败!");
			return rtnMap;
		}
		logger.info("下载 【联动优势】["+ merchantCode +"]协议 & ["+ fileType +"]交易类型 对账文件 ————————————END————————————");
    	return rtnMap;
    }
    /***
     * 下载联动优势对账文件逻辑
     * @param filePath
     * @param dateStr
     * @return
     */
    private static boolean downloadFile(String filePath ,String dateStr, String fileType) {
		//下载
		com.umpay.api.common.ReqData reqData;
		Map<String, String> ht = new HashMap<String, String>();
		ht.put("service", "download_settle_file");
		ht.put("charset", "UTF-8");
		ht.put("version", "4.0");
		ht.put("mer_id", "8789");    
		ht.put("sign_type", "RSA");
		ht.put("settle_date", dateStr);
		if(SettleConstants.ACCOUNT_COED_GENERATE.equals(fileType)) ht.put("settle_type", "ENPAY");
		
		 try{
			reqData = com.umpay.api.paygate.v40.Mer2Plat_v40.ReqDataByGet(ht);
			String url = reqData.getUrl();				//访问此URL可实现交易记录查询
			logger.info("发出对账的url：" + url);
			HttpProtocal httpProtocal = new HttpProtocal();
			httpProtocal.httpMerInfoProStrB(url, ht, filePath);
			return true;
		 }catch(Exception e){
			 logger.error(e.getMessage());
		 }
		 return false;		
	}
    /**
     * 编辑联动优势下载properties文件
     * @return
     */
    private boolean makeLianDongproperties(String merchantCode, String fileType) throws Exception {
		//动态获取系统绝对路径
    	String certpath = this.getClass().getResource("/").toString().replace("file:","");
    	String tomcatRootPath = SettleConstants.TOMCAT_ROOT_PATH;
    	//变量名前缀
    	String thePrefixStr = merchantCode + "_" + SettleConstants.PAY_CHANNEL_ID_UMPAY + "_" + fileType + "_";
		//下载联动对账文件的属性文件名称:SignVerProp.properties
    	String propertisFileName = fileProperties.getProperty(thePrefixStr + "PROPERTIESFILENAME");
    	//创建SignVerProp.properties文件对象
		File propertisFile = new File(certpath + propertisFileName);
		//如果文件不存在创建此文件
		if (!propertisFile.exists()) {
			propertisFile.createNewFile();
		}
		//获取联动文件下载长量信息
		//编辑SignVerProp.properties的映射结构
		Map<String, String> configMap = new HashMap<String, String>();
		configMap.put("FILE", certpath + propertisFileName);
		List<Map<String, String>> detailList = new ArrayList<Map<String, String>>();
		Map<String, String> subMap = new HashMap<String, String>();
		subMap.put("F_1", "plat.url=" + fileProperties.getProperty(thePrefixStr + "PLAT_URL"));
		detailList.add(subMap);
		subMap = new HashMap<String, String>();
		subMap.put("F_1", "plat.wap.url=" + fileProperties.getProperty(thePrefixStr + "PLAT_WAP_URL"));
		detailList.add(subMap);
		subMap = new HashMap<String, String>();
		subMap.put("F_1", "8789.mer.prikey.path=" + fileProperties.getProperty(thePrefixStr + "MER_PRIKEY_PATH").replace("{PATH}", tomcatRootPath));
		detailList.add(subMap);
		subMap = new HashMap<String, String>();
		subMap.put("F_1", "plat.cert.path=" + fileProperties.getProperty(thePrefixStr + "PLAT_CERT_PATH").replace("{PATH}", tomcatRootPath));
		detailList.add(subMap);
		subMap = new HashMap<String, String>();
		subMap.put("F_1", "plat.pay.product.name=" + fileProperties.getProperty(thePrefixStr + "PLAT_PAY_PRODUCT_NAME"));
		detailList.add(subMap);
		subMap = new HashMap<String, String>();
		subMap.put("F_1", "plat.micropay.product.name=" + fileProperties.getProperty(thePrefixStr + "PLAT_MICROPAY_PRODUCT_NAME"));
		detailList.add(subMap);
		subMap = new HashMap<String, String>();
		subMap.put("F_1", "plat.wap.pay.product.name=" + fileProperties.getProperty(thePrefixStr + "PLAT_WAP_PAY_PRODUCT_NAME"));
		detailList.add(subMap);
		
		try {
			TxtWriter.WriteTxt(detailList,configMap,",","UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(">>> >>> 异常: 生成" + propertisFileName + "文件失败 " + e.getMessage());
		}
		return true;
	}
    /***
     * 编辑新联动对账文件
     * @param oldPath
     * @param newPath
     * @param filename
     * @return
     */
    public boolean editNewLianDongWGCollateFile(String oldPath, String newPath, String filename) {
    	logger.info(">>> >>> >>> 读取 并 编辑联动 WG 对账文件");
    	List<Map<String, String>> analyzedList = new ArrayList<Map<String, String>>();
    	//联动优势原始对账文件目录+名称
    	String targetFile = null;
    	//写入文件是时需要的配置映射结构体
    	Map<String, Object> configMap = new HashMap<String, Object>();
    	//新对账文件名称
    	String newFilename;
    	/*
    	 * 读取并解析联动优势对账文件 return List<Map<String, String>>
    	 */
    	try {
    		targetFile = oldPath + filename + "_FOREMOST.txt";
        	analyzedList = super.analyzeOriCollateFile(targetFile);
        	if (null == analyzedList || 1 >= analyzedList.size()) {
    			logger.error(">>> >>> >>> 对账文件里面的内容为空！");
    			return false;
    		}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(">>> >>> >>>异常: 读取并解析联动原始对账文件异常！");
			return false;
		}
    	/*
		 * 编辑上游对账文件数据结构
		 */
    	/****************************************
		 * 输出：分析导入行  
		 * NO.	字段	名称	说明
		 * 头信息（第一行）: TRADEDETAIL-START,商户号（mer_id）,清算日期（settle_date）,版本号（version）,返回码（ret_code）,返回信息（ret_msg）
			1	mer_id	商户号	平台统一分配的商户号
			2	goods_id	商品号	商户下单时提交的goodsId
			3	mobile_id	手机号码	用户支付时使用的手机号码
			4	order_id	订单号	商户下单时提交的orderId
			5	mer_date	订单日期	商户下单时提交的merDate
			6	pay_date	支付日期	用户确认支付的日期
			7	amount	金额	交易金额，以分为单位
			8	amt_type	付款类型	01：人民币 02：移动话费 03：移动积分
			9	gate_id	银行类型	网银类交易用户使用的银行
			10	settle_date	清算日期	交易的清算日期
			11	trans_type	交易类型	01：消费 20：退款
			12	trans_state	交易状态	0：初始 1：成功 -1：失败（一般情况下对账文件只出成功交易）
			13	bank_check	银行对账状态	0：初始 1：对账成功 -1：对账失败
			14	product_id	支付产品编号	联动定义的支付产品
			15	refund_no	退款流水	退款交易此字段有值。退款时由商户生成的退款流水
			16	trans_time	交易成功时间	每笔交易支付成功的时间（24小时制，时分秒例如：192830）
			汇总信息（最后一行）：TRADEDETAIL-END,商户号（mer_id）,交易日期（settle_date）,总笔数,总金额
		 ****************************************/	
		List<Map<String,String>> newFileMap = new ArrayList<Map<String,String>>();
		String tmpdate = null;	//交易日期  + 时间
		for(int i = 0; i < analyzedList.size(); i ++) {
			Map<String, String> bean = analyzedList.get(i);
			if(i == 0 || i == analyzedList.size() - 1) {//表头和表尾不做编辑
				Map<String, String> newBean = super.editNewCollateFileRow(bean);
				newFileMap.add(newBean); 
				continue;
			}
			//打印'行'信息
			//logger.info(">>> '行'信息: " + bean.toString());
			//判断交易结果转换业务编码
			if ("01".equals(bean.get("L_10").toString())) {
				bean.put("L_10","4015");
			} else if ("20".equals(bean.get("L_10").toString())) {
				bean.put("L_10","4017");
			}
			tmpdate = bean.get("L_5").toString() + " " + bean.get("L_15").toString();
			bean.put("L_5", tmpdate);
			/*
			 * 编辑对账文件交易状态对应关系
			 */
			if("1".equals(bean.get("L_12").toString())) {
				bean.put("L_12", "16");
			} else if("-1".equals(bean.get("L_12").toString())) {
				bean.put("L_12", "14");
			} else if("0".equals(bean.get("L_12").toString())) {
				logger.error(">>> >>> >>> >>> 联动优势 WG读取对账文件交易 此交易["+ bean.get("L_4").toString() +"]  交易状态为 0, 清算将次交易状态记为12[处理中]");
				bean.put("L_12", "12");
			}
			/*
			 * 编辑新联动优势文件的结构体
			 */
			Map<String, String> newBean = super.editNewCollateFileRow(bean);
			newFileMap.add(newBean);
		}
		//获取新对账文件名称 并 存入结构体
		newFilename = filename + ".txt";
		configMap.put("FILE", newPath + newFilename);
		//生成新对账文件
		try {
			TxtWriter.WriteTxt(newFileMap, configMap, ",", "UTF-8");
		} catch (Exception e) {	
			logger.info(">>> 异常: 生成新对账文件失败" + e.getMessage());
			e.printStackTrace();
			return false;
		}
    	return true;
    }
    
    /***
     * 编辑新联动对账文件
     * @param oldPath
     * @param newPath
     * @param filename
     * @return
     */
    public boolean editNewLianDongZFCollateFile(String oldPath, String newPath, String filename) {
    	logger.info(">>> >>> >>> 读取 并 编辑联动 ZF 对账文件");
    	List<Map<String, String>> analyzedList = new ArrayList<Map<String, String>>();
    	//联动优势原始对账文件目录+名称
    	String targetFile = null;
    	//写入文件是时需要的配置映射结构体
    	Map<String, Object> configMap = new HashMap<String, Object>();
    	//新对账文件名称
    	String newFilename;
    	/*
    	 * 读取并解析联动优势对账文件 return List<Map<String, String>>
    	 */
    	try {
    		targetFile = oldPath + filename + "_FOREMOST.txt";
        	analyzedList = super.analyzeOriCollateFile(targetFile);
        	if (null == analyzedList || 1 >= analyzedList.size()) {
    			logger.error(">>> >>> >>> 对账文件里面的内容为空！");
    			return false;
    		}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(">>> >>> >>>异常: 读取并解析联动原始对账文件异常！");
			return false;
		}
    	/*
		 * 编辑上游对账文件数据结构
		 */
    	/*1.	mer_id	商户号 						L_0	
    	2.		fun_code	交易类型 trans_type		L_1	TRANS_TYPE 		1
    	3.		order_id	订单号order_no			L_2	ORDER_NO 		1
    	4.		mer_date	订单日期 					L_3 REQUEST_TIME 	3	yyyyMMdd hhmmss
    	5.		amount	金额 trans_amount				L_4 TRANS_AMOUNT	1
    	6.		com_amt	手续费 user_Fee				L_5	FEE_AMOUNT		1
    	7.		transfer_settle_date	对账日期		L_6 
    	8.		transfer_acc_date	记账日期			L_7
    	9.		trans_state	交易状态 					L_8 REMARK			1
    	10.		plat_trace	联动的流水号				L_9 
    	11.		com_amt_type	手续费承担方			L_10
    	*/			
		List<Map<String,String>> newFileMap = new ArrayList<Map<String,String>>();
		String tmpdate = null;	//交易日期  + 时间
		for(int i = 0; i < analyzedList.size(); i ++) {
			Map<String, String> bean = analyzedList.get(i);
			if(i == 0 || i == analyzedList.size() - 1) {//表头和表尾不做编辑
				Map<String, String> newBean = super.editNewCollateFileRow(bean);
				newFileMap.add(newBean); 
				continue;
			}
			//判断交易类型 如果是退单 修改批次号 使其对成【长款】
			if(bean.get("L_1").equals("T500")) {
				String orderNo = "t" + bean.get("L_2");
				bean.put("L_2", orderNo);
			}
			//判断交易状态
			if(bean.get("L_8").equals("交易成功")) {
				bean.put("L_8", "16");
			}
			//打印'行'信息
			//判断交易结果转换业务编码
			tmpdate = bean.get("L_3").toString() + " " + "235959";
			bean.put("L_3", tmpdate);
			/*
			 * 编辑新联动优势文件的结构体
			 */
			Map<String, String> newBean = super.editNewCollateFileRow(bean);
			newFileMap.add(newBean);
		}
		//获取新对账文件名称 并 存入结构体
		newFilename = filename + ".txt";
		configMap.put("FILE", newPath + newFilename);
		//生成新对账文件
		try {
			TxtWriter.WriteTxt(newFileMap, configMap, ",", "UTF-8");
		} catch (Exception e) {	
			logger.info(">>> 异常: 生成新对账文件失败" + e.getMessage());
			e.printStackTrace();
			return false;
		}
    	return true;
    }
    
    /*
     * 查询通联账户余额
     * @param rootInstId 机构号
     * @param fileType 代收付标识
     * @return 金额（分）
     */
	public String merAcctBalance(String rootInstId,String fileType) {
		String balance = "余额取得失败！";
		try {
			boolean fileflg = makeLianDongproperties(rootInstId, fileType);
			if (!fileflg) {
				return balance;
			}
			balance = merAcctBalanceToUMP();
		} catch (Exception e1) {
			logger.error("余额取得失败！" + e1.getMessage());
			return "余额取得失败！";
		}
		return balance;
	}
	
    /***
     * 查询联动优势帐户余额
     * @param filePath
     * @param dateStr
     * @return
     */
    private String merAcctBalanceToUMP() {
		//下载
		com.umpay.api.common.ReqData reqData;
		Map<String, String> ht = new HashMap<String, String>();
		Map<String, String> rtn = new HashMap<String, String>();
		ht.put("service", "query_account_balance");
		ht.put("charset", "UTF-8");
		ht.put("version", "4.0");
		ht.put("res_format","HTML");
		ht.put("mer_id", "8789");    
		ht.put("sign_type", "RSA");
		
		 try{
			reqData = com.umpay.api.paygate.v40.Mer2Plat_v40.makeReqDataByGet(ht);
			String url = reqData.getUrl();				//访问此URL可实现交易记录查询
			logger.info("发出对账的url：" + url);
			HttpProtocal httpProtocal = new HttpProtocal();
			rtn = httpProtocal.httpMerInfoPro(url, ht);
			System.out.println(rtn);
			return rtn.get("bal_sign").toString();
		 }catch(Exception e){
			 logger.error(e.getMessage());
		 }
		 return "余额取得失败！";
	}
}
