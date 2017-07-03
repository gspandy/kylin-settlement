package com.rkylin.settle.filedownload;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rkylin.settle.constant.SettleConstants;
import com.rkylin.settle.manager.ParameterInfoManager;
import com.rkylin.settle.pojo.ParameterInfoQuery;
import com.rkylin.settle.util.FtpDownload;
import com.rkylin.settle.util.SettlementUtil;
import com.rkylin.settle.util.TxtReader;
import com.rkylin.settle.util.TxtWriter;
import com.rkylin.settle.util.XlsxReader;
import com.rkylin.utils.RkylinMailUtil;

@Component("multiFileDown")
public class MultiFileDown extends CheckfileDownload {
	protected static Logger logger = LoggerFactory.getLogger(MultiFileDown.class);
	
	@Autowired
	ParameterInfoManager parameterInfoManager;
	@Autowired
    Properties fileProperties;
	@Autowired
	Multi_PAB multi_PAB;
	@Autowired
	SettlementUtil settlementUtil;
	@Autowired
    Properties ftpProperties;
	
	/***
	 * 下载多渠道对账文件
	 * @param 当前账期
	 * @param merchantCode
	 * @param fileType
	 * @return
	 */
	public Map<String, String> multiFileDown(String merchantCode, String fileType) throws Exception {
		return this.multiFileDown(null, merchantCode, fileType);
	}
	/**
	 * 指定账期下载多渠道对账文件
	 * @param invoicedate
	 * @param merchantCode
	 * @param fileType
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> multiFileDown(String invoicedate, String merchantCode, String fileType) throws Exception {
		return this.multiFileDown(invoicedate, merchantCode, fileType, null);
	}
	/**
     * 指定账期下载多渠道对账文件
     * @param invoicedate 上游账期
     * @param rootInstId 机构号
     * @return
     */
    public Map<String, String> multiFileDown(String invoicedate, String merchantCode, String fileType,String payChannelId) throws Exception {
    	logger.info("下载多渠道网关对账文件 ["+ merchantCode +"]协议 & ["+ fileType +"]交易类型 ————————————START————————————");
    	try{
    	   	if(payChannelId.equals(SettleConstants.PAY_CHANNEL_ID_CMBC) && ",AgentPay,AgentPay_DL,AgentPay_FDL,Refund,Refund_DL,Refund_FDL,".indexOf(","+fileType+",")!=-1){
        		return this.cmbcMultiFileDown(invoicedate, merchantCode, fileType, payChannelId);
        	}
    	}catch(Exception e){
    		logger.info("民生单笔对账发生异常,e="+e);
    	}
 
    	Map<String, String> rtnMap = new HashMap<String, String>();
		rtnMap.put("errCode", "0000");
		rtnMap.put("errMsg", "成功");
		
		/**
    	 * 判断日中是否正常结束
    	 */
//    	ParameterInfoQuery keyList =  new ParameterInfoQuery();
//   	keyList.setParameterCode(SettleConstants.DAYEND);
//   	List<ParameterInfo> parameterInfo = parameterInfoManager.queryList(keyList);
//		if (!"0".equals(parameterInfo.get(0).getParameterValue())) {
		if (!settlementUtil.cutDayIsSuccess4Account()) {
    		rtnMap.put("errCode", "0001");
    		rtnMap.put("errMsg", "日终没有正常结束！");
    		RkylinMailUtil.sendMailThread("清洁算开始异常","日终没有正常结束，不能开始清洁算操作", "21401233@qq.com");
    		return rtnMap;
    	}
    	String accountDate = "";
    	String accountDate2 = "";
    	try {
	    	if (invoicedate == null || "".equals(invoicedate)) {
//	    		keyList.setParameterCode(SettleConstants.ACCOUNTDATE);
//	    		invoicedate = parameterInfoManager.queryList(keyList).get(0).getParameterValue();
	    		invoicedate = String.valueOf(settlementUtil.getAccountDate("yyyy-MM-dd", 0, "String"));
				/*
				 * settlementUtil.getAccountDate(String fromat,int step,String returnType)
				 * 注入 parameterInfoManager对账失败
				 */
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
    	logger.info(">>> >>> 【指定账期下载多渠道网关对账文件】  取得的账期为 :"+ accountDate);
		
    	String localPath = null;
    	FtpDownload ftpDownload = new FtpDownload();
    	boolean flg = false;
    	
    	logger.info(">>> >>> 连接融数金服FTP");
    	try {
    		String addr = ftpProperties.getProperty("MULTIFTP_URL");
    		int port = Integer.parseInt(ftpProperties.getProperty("MULTIFTP_PORT"));
    		String username = ftpProperties.getProperty("MULTIFTP_NAME");
    		String password = ftpProperties.getProperty("MULTIFTP_PASS");
    		
    		flg = ftpDownload.connect(addr, port, username, password);
    		if (!flg) {
    			Thread.sleep(3000);
    			flg = ftpDownload.connect(addr, port, username, password);

        		if (!flg) {
	    			logger.error("FTP连接失败" + flg);
	    			rtnMap.put("errCode", "0001");
	    			rtnMap.put("errMsg", "FTP连接失败");
	    			return rtnMap;
        		}
    		}
    	} catch (Exception e) {
    		logger.error("异常【下载多渠道网关对账文件】连接FTP" + e.getMessage());
			rtnMap.put("errCode", "0001");
			rtnMap.put("errMsg", "异常【下载多渠道网关对账文件】");
			e.printStackTrace();
			return rtnMap;
    	}
    	logger.info(">>> >>> 连接融数金服FTP结果"+flg);
    	
    	logger.info(">>> >>> 从FTP上下载文件");
    	List<String> fileNameL = new ArrayList<String>();
    	String fileName = null;
    	if (SettleConstants.ACCOUNT_COED_PAB.equals(fileType)) {//PAYQ 平安银企
        	localPath = SettleConstants.FILE_PATH +accountDate2 + File.separator + "PABbak" + File.separator;
    		fileName = fileProperties.getProperty("PAYQ4004_M00000X").replace("{YMD}", accountDate);
    		fileNameL.add(fileName);
    		fileName = fileProperties.getProperty("PAYQ4014_M00000X").replace("{YMD}", accountDate);
    		fileNameL.add(fileName);
    		fileName = fileProperties.getProperty("PAYQ4018_M00000X").replace("{YMD}", accountDate);
    		fileNameL.add(fileName);
//    		fileName = fileProperties.getProperty("PAYQC005_M00000X").replace("{YMD}", accountDate);
//    		fileNameL.add(fileName);
//    		fileName = fileProperties.getProperty("PAYQC004_M00000X").replace("{YMD}", accountDate);
//    		fileNameL.add(fileName);
//    		fileName = fileProperties.getProperty("PAYQ4047_M00000X").replace("{YMD}", accountDate);
//    		fileNameL.add(fileName);
    	} else if (SettleConstants.ACCOUNT_COED_WXMOBILE.equals(fileType)) {//WX 微信
        	localPath = SettleConstants.FILE_PATH + accountDate2 + File.separator + "TLWXbak" + File.separator;
    		fileName = fileProperties.getProperty("TLWX_"+merchantCode).replace("{YMD}", accountDate);
    		fileNameL.add(fileName);
    	} else if (SettleConstants.ACCOUNT_COED_CMBC.equals(fileType)) {//MSYQ 民生银企
        	localPath = SettleConstants.FILE_PATH + accountDate2 + File.separator + "CMBCbak" + File.separator;
    		fileName = fileProperties.getProperty("MSYQ_"+merchantCode).replace("{YMD}", accountDate);
    		fileNameL.add(fileName);
    	} else if (SettleConstants.ACCOUNT_COED_TLSDK.equals(fileType)) {//TLSDK 通联SDK
        	localPath = SettleConstants.FILE_PATH + accountDate2 + File.separator;
    		fileName = fileProperties.getProperty("TLSDK_"+merchantCode).replace("{YMD}", accountDate);
    		fileNameL.add(fileName);
    	} else if (SettleConstants.ACCOUNT_COED_WXAPP.equals(fileType)) {//WXAPP 通联微信APP
    		localPath = SettleConstants.FILE_PATH + accountDate2 + File.separator + "TLWXAPPbak" + File.separator;
    		fileName = fileProperties.getProperty("TLWXAPP_"+merchantCode).replace("{YMD}", accountDate);
    		fileNameL.add(fileName);
    	} else if (SettleConstants.ACCOUNT_COED_CHANNEL.equals(fileType) && SettleConstants.PAY_CHANNEL_ID_RB.equals(payChannelId) ) {//融宝网关
    		localPath = SettleConstants.FILE_PATH + accountDate2 + File.separator;
    		fileName = fileProperties.getProperty("RBWG_"+merchantCode).replace("{YMD}", accountDate);
    		fileNameL.add(fileName);
    	} else if (SettleConstants.ACCOUNT_COED_PABKHKF.equals(fileType)) {
    		localPath = SettleConstants.FILE_PATH +accountDate2 + File.separator + "PAB_KHKF_bak" + File.separator;
    		fileName = fileProperties.getProperty("PABKHKF01").replace("{YMD}", accountDate).replace("{MCC}", merchantCode);
    		fileNameL.add(fileName);
    		fileName = fileProperties.getProperty("PABKHKF02").replace("{YMD}", accountDate).replace("{MCC}", merchantCode);
    		fileNameL.add(fileName);
    	}else if (SettleConstants.ACCOUNT_TYPE_YB_AUTH.equals(fileType)) {//易宝鉴权绑卡
    		localPath = SettleConstants.FILE_PATH + accountDate2 + File.separator;
    		fileName = fileProperties.getProperty("YB_AUTH_"+merchantCode).replace("{YMD}", accountDate);
    		fileNameL.add(fileName);
    	}else if (SettleConstants.ACCOUNT_TYPE_YB_PAYMENT.equals(fileType)) {//易宝支付
    		localPath = SettleConstants.FILE_PATH + accountDate2 + File.separator;
    		fileName = fileProperties.getProperty("YB_PAYMENT_"+merchantCode).replace("{YMD}", accountDate);
    		fileNameL.add(fileName);
    	}else if (SettleConstants.ACCOUNT_TYPE_YB_WITHDRAW.equals(fileType)) {//易宝提现
    		localPath = SettleConstants.FILE_PATH + accountDate2 + File.separator;
    		fileName = fileProperties.getProperty("YB_WITHDRAW_"+merchantCode).replace("{YMD}", accountDate);
    		fileNameL.add(fileName);
    	}else if (SettleConstants.ACCOUNT_TYPE_YB_CHANGECARD.equals(fileType)) {//易宝换卡
    		localPath = SettleConstants.FILE_PATH + accountDate2 + File.separator;
    		fileName = fileProperties.getProperty("YB_CHANGECARD_"+merchantCode).replace("{YMD}", accountDate);
    		fileNameL.add(fileName);
    	}else if (SettleConstants.ACCOUNT_TYPE_YB_REFUND.equals(fileType)) {//易宝退款
    		localPath = SettleConstants.FILE_PATH + accountDate2 + File.separator;
    		fileName = fileProperties.getProperty("YB_REFUND_"+merchantCode).replace("{YMD}", accountDate);
    		fileNameL.add(fileName);
    	}else if (SettleConstants.ACCOUNT_TYPE_YB_PAYDIVIDE.equals(fileType)) {//易宝分账
    		localPath = SettleConstants.FILE_PATH + accountDate2 + File.separator;
    		fileName = fileProperties.getProperty("YB_PAYDIVIDE_"+merchantCode).replace("{YMD}", accountDate);
    		fileNameL.add(fileName);
    	}else if (SettleConstants.ACCOUNT_TYPE_CMBC_AgentPay_DL.equals(fileType)) {//民生银行 -单笔联机代付(代理支付)
        	localPath = SettleConstants.FILE_PATH + accountDate2 + File.separator+"CMBC_AgentPay_bak" + File.separator;
    		fileName = fileProperties.getProperty("CMBC_AgentPay_DL_"+merchantCode).replace("{YMD}", accountDate);
    		fileNameL.add(fileName);
    	}else if (SettleConstants.ACCOUNT_TYPE_CMBC_AgentPay_FDL.equals(fileType)) {//民生银行 -单笔联机代付(非代理支付)
        	localPath = SettleConstants.FILE_PATH + accountDate2 + File.separator+"CMBC_AgentPay_bak" + File.separator;
    		fileName = fileProperties.getProperty("CMBC_AgentPay_FDL_"+merchantCode).replace("{YMD}", accountDate);
    		fileNameL.add(fileName);
    	}else if (SettleConstants.ACCOUNT_TYPE_CMBC_Refund_DL.equals(fileType)) {//民生银行 -单笔联机代付退票(代理支付)
        	localPath = SettleConstants.FILE_PATH + accountDate2 + File.separator+"CMBC_Refund_bak" + File.separator;
    		fileName = fileProperties.getProperty("CMBC_Refund_DL_"+merchantCode).replace("{YMD}", accountDate);
    		fileNameL.add(fileName);
    	}else if (SettleConstants.ACCOUNT_TYPE_CMBC_Refund_FDL.equals(fileType)) {//民生银行 -单笔联机代付退票(非代理支付)
        	localPath = SettleConstants.FILE_PATH + accountDate2 + File.separator+"CMBC_Refund_bak" + File.separator;
    		fileName = fileProperties.getProperty("CMBC_Refund_FDL_"+merchantCode).replace("{YMD}", accountDate);
    		fileNameL.add(fileName);
    	}else if (SettleConstants.ACCOUNT_TYPE_YB_DF.equals(fileType)) {//易宝代付
        	localPath = SettleConstants.FILE_PATH + accountDate2 + File.separator+"YB_DF_bak" + File.separator;
    		fileName = fileProperties.getProperty("YB_DF_"+merchantCode).replace("{YMD}", accountDate);
    		fileNameL.add(fileName);
    	}else if (SettleConstants.ACCOUNT_TYPE_RB_DF.equals(fileType)) {//融宝代付
        	localPath = SettleConstants.FILE_PATH + accountDate2 + File.separator+"RB_DF_bak" + File.separator;
    		fileName = fileProperties.getProperty("RB_DF_"+merchantCode).replace("{YMD}", accountDate);
    		fileNameL.add(fileName);
    	}else {
    		fileNameL = null;
    		logger.error("异常【下载多渠道网关对账文件】fileType不认识" + fileType);
    		rtnMap.put("errCode", "0001");
    		rtnMap.put("errMsg", "fileType不认识");
    		return rtnMap;
    	}
    	try {
    		for (int i=0;i<fileNameL.size();i++) {
    			fileName = fileNameL.get(i);
	    		flg = ftpDownload.download(accountDate, fileName, localPath, "");
	    		if (!flg) {
	    			logger.error("FTP文件下载失败" + flg + "|" + fileName);
	    		} else {
	    			logger.error("FTP文件下载成功" + flg + "|" + fileName);
	    		}
    		}
    	} catch (Exception e) {
    		logger.error("异常【下载多渠道网关对账文件】FTP文件下载" + e.getMessage());
			rtnMap.put("errCode", "0001");
			rtnMap.put("errMsg", "异常【下载多渠道网关对账文件】");
			e.printStackTrace();
			return rtnMap;
    	}
    	logger.info(">>> >>> 从FTP上下载文件完毕");
    	logger.info(">>> >>> 关闭FTP连接");
    	try {
    		flg = ftpDownload.disConnect();
    		if (!flg) {
    			logger.error("关闭FTP连接失败" + flg);
    		}
    	} catch (Exception e) {
    		logger.error("异常【下载多渠道网关对账文件】关闭FTP连接" + e.getMessage());
			rtnMap.put("errCode", "0001");
			rtnMap.put("errMsg", "异常【下载多渠道网关对账文件】");
			e.printStackTrace();
			return rtnMap;
    	}
    	logger.info(">>> >>> 关闭FTP连接完毕");
    	
    	if (fileNameL != null) {
    		if (SettleConstants.ACCOUNT_COED_PAB.equals(fileType)) {
    	    	logger.info(">>> >>> 编辑平安银行对账文件");
        		editNewCollateFile(localPath,fileNameL,accountDate2,accountDate,merchantCode);
    		} else if (SettleConstants.ACCOUNT_COED_WXMOBILE.equals(fileType)) {
    	    	logger.info(">>> >>> 编辑微信对账文件");
        		editNewCollateFile_WX(localPath,fileNameL,accountDate2,accountDate,merchantCode);
    		} else if (SettleConstants.ACCOUNT_COED_CMBC.equals(fileType)) {
    	    	logger.info(">>> >>> 编辑民生银行对账文件");
        		editNewCollateFile_MS(localPath,fileNameL,accountDate2,accountDate,merchantCode);
    		} else if (SettleConstants.ACCOUNT_COED_WXAPP.equals(fileType)) {
    	    	logger.info(">>> >>> 编辑微信APP对账文件");
        		editNewCollateFile_WX(localPath,fileNameL,accountDate2,accountDate,merchantCode,SettleConstants.ACCOUNT_COED_WXAPP);
    		} else if(SettleConstants.ACCOUNT_COED_PABKHKF.equals(fileType)) {
    			logger.info(">>> >>> 编辑平安银行_跨行快付 对账文件");
    			editNewCollateFile_KHKF(localPath, fileNameL, accountDate2, accountDate, merchantCode);
    		}else if(SettleConstants.ACCOUNT_TYPE_CMBC_AgentPay_DL.equals(fileType)) {
    			logger.info(">>> >>> 编辑民生单笔代付 对账文件(代理)");
    			editNewCollateFile_CMBC(localPath, fileNameL, accountDate2, accountDate, merchantCode);
    		}else if(SettleConstants.ACCOUNT_TYPE_CMBC_AgentPay_FDL.equals(fileType)) {
    			logger.info(">>> >>> 编辑民生单笔代付 对账文件(非代理)");
    			editNewCollateFile_CMBC(localPath, fileNameL, accountDate2, accountDate, merchantCode);
    		}else if(SettleConstants.ACCOUNT_TYPE_CMBC_Refund_DL.equals(fileType)) {
    			logger.info(">>> >>> 编辑民生单笔代付退票 对账文件(代理)");
    			editNewCollateFile_CMBC(localPath, fileNameL, accountDate2, accountDate, merchantCode);
    		}else if(SettleConstants.ACCOUNT_TYPE_CMBC_Refund_FDL.equals(fileType)) {
    			logger.info(">>> >>> 编辑民生单笔代付 退票对账文件(非代理)");
    			editNewCollateFile_CMBC(localPath, fileNameL, accountDate2, accountDate, merchantCode);
    		}else if(SettleConstants.ACCOUNT_TYPE_YB_DF.equals(fileType)) {
    			logger.info(">>> >>> 编辑易宝代付对账文件");
    			editNewCollateFile_YB_DF(localPath, fileNameL, accountDate2, accountDate, merchantCode);
    		}else if(SettleConstants.ACCOUNT_TYPE_RB_DF.equals(fileType)) {
    			logger.info(">>> >>> 编辑融宝代付对账文件");
    			editNewCollateFile_RB_DF(localPath, fileNameL, accountDate2, accountDate, merchantCode);
    		}
    	}
		return rtnMap;
    }

	@SuppressWarnings({ "rawtypes", "unchecked", "static-access" })
	public void editNewCollateFile_MS(String localPath,List<String> fileNameL,String accountDate2,String accountDate,String merchantCode) {
		String fileName = "";
		File collFile = null;
		TxtReader txtReader = new TxtReader();
		TxtWriter txtWriter = new TxtWriter();
		List<Map> fileList = new ArrayList<Map>();
		List<Map> headList = new ArrayList<Map>();
		List<Map> tailList = new ArrayList<Map>();
		Map fileMap;
		List<Map> fileListSub;
		Map fileMapSub;
		Map<String,Map> keyMap = new HashMap<String,Map>();
		int jj = 0;
		int zz = 1;
		
		for (int i =0;i<fileNameL.size();i++) {
			fileName = fileNameL.get(i);
			fileListSub = new ArrayList<Map>();
			collFile = new File(localPath+fileName);
			if (!collFile.exists()) {
				continue;
			}
			try {
				txtReader.setEncode("UTF-8");
				fileListSub = txtReader.txtreader(collFile , SettleConstants.DEDT_SPLIT);
			} catch(Exception e) {
				logger.error(">>> >>>对账文件读取异常！" + e.getMessage());
				return;
			}

			if (fileListSub == null || fileListSub.size() ==0) {
				continue;
			}
			fileMapSub = new HashMap();
			keyMap = new HashMap<String,Map>();
			for (int j=1;j<fileListSub.size();j++) {
				fileMap = new HashMap();
				// 去除标题行
//				交易日期
//				主机交易流水号
//				借方发生额
//				贷方发生额
//				账户余额
//				凭证种类
//				凭证号
//				摘要
//				交易地点
//				对方账号
//				对方账号名称
//				对方开户行
//				交易时间戳
				fileMapSub = fileListSub.get(j);
				if ("手续费".equals(fileMapSub.get("L_7").toString().trim())) {
					continue;
				}
				fileMap.put("F_1",fileMapSub.get("L_0")); //交易日期
				fileMap.put("F_2",fileMapSub.get("L_1")); //主机交易流水号
				if (!"0.00".equals(fileMapSub.get("L_2").toString().trim())) {
					fileMap.put("F_3",fileMapSub.get("L_2").toString().trim().replaceAll("-", "")); //交易金额
					fileMap.put("F_4",SettleConstants.CMBC_ACCOUNT_NO_MAIN1); //付款账号
					fileMap.put("F_5",fileMapSub.get("L_9").toString().trim()); //收款账号
					fileMap.put("F_9","4014"); //交易类型
				} else {
					fileMap.put("F_3",fileMapSub.get("L_3").toString().trim().replaceAll("-", "")); //交易金额
					fileMap.put("F_4",fileMapSub.get("L_9").toString().trim()); //付款账号
					fileMap.put("F_5",SettleConstants.CMBC_ACCOUNT_NO_MAIN1); //收款账号
					fileMap.put("F_9","4015"); //交易类型
				}
				if (fileMapSub.get("L_6").toString() == null || "".equals(fileMapSub.get("L_6").toString())) {
					if (fileMapSub.get("L_7").toString().trim().length() < 9) {
						fileMap.put("F_6",fileMapSub.get("L_7").toString().trim());
					} else {
						fileMap.put("F_6",fileMapSub.get("L_7").toString().trim().substring(0, 8)); //交易订单号
					}
				} else {
					fileMap.put("F_6",fileMapSub.get("L_6").toString().trim());//交易订单号
				}
				fileMap.put("F_7",fileMapSub.get("L_12").toString().trim().substring(0, 14)); //交易时间
				fileMap.put("F_8","16"); //交易状态
				fileMap.put("F_10",fileMapSub.get("L_7").toString().trim()); //备注
				fileMap.put("F_11","0"); //手续费
				keyMap.put(fileMapSub.get("L_1").toString(), fileMap);
			}
			// 手续费回写
			for (int j=1;j<fileListSub.size();j++) {
				fileMap = new HashMap();
				fileMapSub = fileListSub.get(j);
				if ("手续费".equals(fileMapSub.get("L_7").toString().trim())) {
					if (keyMap.containsKey(fileMapSub.get("L_1").toString())) {
						fileMap = keyMap.get(fileMapSub.get("L_1").toString());
						fileMap.put("F_11", fileMapSub.get("L_2").toString().trim().replaceAll("-", ""));
					} else {
						fileMap.put("F_1",fileMapSub.get("L_0")); //交易日期
						fileMap.put("F_2",fileMapSub.get("L_1")); //主机交易流水号
						if (!"0.00".equals(fileMapSub.get("L_2").toString().trim())) {
							fileMap.put("F_3",fileMapSub.get("L_2").toString().trim().replaceAll("-", "")); //交易金额
							fileMap.put("F_4",SettleConstants.CMBC_ACCOUNT_NO_MAIN1); //付款账号
							fileMap.put("F_5",fileMapSub.get("L_9").toString().trim()); //收款账号
							fileMap.put("F_9","4014"); //交易类型
						} else {
							fileMap.put("F_3",fileMapSub.get("L_3").toString().trim().replaceAll("-", "")); //交易金额
							fileMap.put("F_4",fileMapSub.get("L_9").toString().trim()); //付款账号
							fileMap.put("F_5",SettleConstants.CMBC_ACCOUNT_NO_MAIN1); //收款账号
							fileMap.put("F_9","4015"); //交易类型
						}
						if (fileMapSub.get("L_6").toString() == null || "".equals(fileMapSub.get("L_6").toString())) {
							if (fileMapSub.get("L_7").toString().trim().length() < 9) {
								fileMap.put("F_6",fileMapSub.get("L_7").toString().trim());
							} else {
								fileMap.put("F_6",fileMapSub.get("L_7").toString().trim().substring(0, 8)); //交易订单号
							}
						} else {
							fileMap.put("F_6",fileMapSub.get("L_6").toString().trim());//交易订单号
						}
						fileMap.put("F_7",fileMapSub.get("L_12").toString().trim().substring(0, 14)); //交易时间
						fileMap.put("F_8","16"); //交易状态
						fileMap.put("F_10",fileMapSub.get("L_7").toString().trim()); //备注
						fileMap.put("F_11","0"); //手续费
						keyMap.put(fileMapSub.get("L_1").toString(), fileMap);
					}
				}
			}
			Iterator<Map.Entry<String,Map>> entries = keyMap.entrySet().iterator();
			String keyStr = "";
			while (entries.hasNext()) {
				Map.Entry<String,Map> entry = entries.next();
			    keyStr = entry.getKey();
			    fileList.add(keyMap.get(keyStr));
			}
		}
		
		Map configMap = new HashMap();
		String localfilennew = SettleConstants.FILE_PATH +accountDate2 + File.separator + accountDate +"_CMBC_BankPayment_ZF_"+merchantCode+".txt";
		File pathFile = new File(SettleConstants.FILE_PATH +accountDate2 + File.separator);
		if (!pathFile.exists()) {
			pathFile.mkdirs();
		}
		configMap.put("FILE", localfilennew);
		
		fileMap = new HashMap();
		fileMap.put("F_1", "START");
		headList.add(fileMap);
		configMap.put("REPORT-HEAD", headList);
		
		fileMap = new HashMap();
		fileMap.put("F_1", "END");
		tailList.add(fileMap);
		configMap.put("REPORT-TAIL", tailList);
		
		try {
			txtWriter.WriteTxt(fileList, configMap, SettleConstants.DEDT_SPLIT, "UTF-8");
		} catch(Exception e) {
			logger.error(">>> >>>对账文件写入异常！" + e.getMessage());
			return;
		}
	}	
	/***
	 * 编辑通联对账文件 默认编辑WX
	 * @param localPath
	 * @param fileNameL
	 * @param accountDate2
	 * @param accountDate
	 * @param merchantCode
	 */
	public void editNewCollateFile_WX(String localPath,List<String> fileNameL,String accountDate2,String accountDate,String merchantCode) {
		this.editNewCollateFile_WX(localPath, fileNameL, accountDate2, accountDate, merchantCode, "WX");
	}
	/***
	 * 编辑通联对账文件 
	 * @param localPath
	 * @param fileNameL
	 * @param accountDate2
	 * @param accountDate
	 * @param merchantCode
	 * @param accountType	文件类型
	 */
	@SuppressWarnings({ "rawtypes", "unchecked", "static-access" })
	public void editNewCollateFile_WX(String localPath,List<String> fileNameL,String accountDate2,String accountDate,String merchantCode, String accountType) {
		String fileName = "";
		File collFile = null;
    	XlsxReader xlsxReader = new XlsxReader();
    	TxtWriter txtWriter = new TxtWriter();
    	List<Map> fileList = new ArrayList<Map>();
    	List<Map> headList = new ArrayList<Map>();
    	List<Map> tailList = new ArrayList<Map>();
    	Map fileMap = new HashMap();
    	Map<String, List<Map<String, Object>>> rtnFileMap;
		List<Map<String,Object>> subList = new ArrayList<Map<String,Object>>();
		Map<String,Object> subMap = new HashMap<String,Object>();
    	int jj = 0;
    	int zz = 1;
		
    	if (fileNameL.size() >1) {
			logger.error(">>> >>>微信对账文件数大于1！");
    	}
    	
		for (int i =0;i<fileNameL.size();i++) {
			fileName = fileNameL.get(i);
			rtnFileMap = new HashMap<String, List<Map<String, Object>>>();
			collFile = new File(localPath+fileName);
			if (!collFile.exists()) {
				continue;
			}
			try {
				rtnFileMap = xlsxReader.file_reader(collFile);
			} catch(Exception e) {
				logger.error(">>> >>>对账文件读取异常！" + e.getMessage());
				e.getStackTrace();
				return;
			}
			
			if (rtnFileMap == null || rtnFileMap.size() ==0) {
				continue;
			}
			if (rtnFileMap.size()>1) {
				logger.error(">>> >>>微信对账文件Sheet数大于1！");
			}
			
			boolean readflg = false;
			String derName = "";
			Iterator entries = rtnFileMap.entrySet().iterator();  
			while (entries.hasNext()) {
			    Map.Entry entry = (Map.Entry) entries.next();
			    String key = (String)entry.getKey();
			    subList = rtnFileMap.get(key);
			    for (int j=0;j<subList.size();j++) {
			    	subMap = subList.get(j);
			    	
			    	if (readflg) {
				    	if ("终端号".equals(subMap.get("L_0").toString().trim()) || ("".equals(subMap.get("L_0").toString().trim()) && "小计".equals(subMap.get("L_1").toString().trim()))
				    			|| "合计".equals(subMap.get("L_0").toString().trim()) || "汇总".equals(subMap.get("L_0").toString().trim())) {
				    		continue;
				    	}
				    	
				    	if (!"".equals(subMap.get("L_0").toString()) && "".equals(subMap.get("L_11").toString())) {
				    		derName = subMap.get("L_0").toString();//门店
				    	} else {
				    		fileMap.put("F_1", derName);//门店
				    		fileMap.put("F_2", subMap.get("L_0"));///终端号
//				    		fileMap.put("F_3", subMap.get("L_9").toString()+subMap.get("L_1").toString());//交易时间+交易日期
//				    		if ("微信支付".equals(subMap.get("L_2").toString().trim())) {
//					    		fileMap.put("F_4", "4015");//交易类型
//				    		} else {
//					    		fileMap.put("F_4", "4017");//交易类型
//				    		}
//				    		fileMap.put("F_5", subMap.get("L_3"));//凭证号
//				    		fileMap.put("F_6", subMap.get("L_4"));//卡号
//				    		fileMap.put("F_7", subMap.get("L_5"));//卡类别
//				    		fileMap.put("F_8", subMap.get("L_6"));//发卡行名称
//				    		fileMap.put("F_9", subMap.get("L_7").toString().replace("-", ""));//交易金额
//				    		fileMap.put("F_10", subMap.get("L_8").toString().replace("-", ""));//手续费
//				    		fileMap.put("F_11", subMap.get("L_10"));//流水号
//				    		fileMap.put("F_12", subMap.get("L_11"));//订单号
//				    		fileMap.put("F_13", subMap.get("L_12"));//商户备注
				    		fileMap.put("F_3", subMap.get("L_11").toString()+subMap.get("L_1").toString());//交易时间+交易日期
				    		if ("微信支付".equals(subMap.get("L_2").toString().trim())  || "支付宝支付".equals(subMap.get("L_2").toString().trim())) {
					    		fileMap.put("F_4", "4015");//交易类型
				    		} else {
					    		fileMap.put("F_4", "4017");//交易类型
				    		}
				    		fileMap.put("F_5", subMap.get("L_4"));//凭证号
				    		fileMap.put("F_6", subMap.get("L_6"));//卡号
				    		fileMap.put("F_7", subMap.get("L_7"));//卡类别
				    		fileMap.put("F_8", subMap.get("L_8"));//发卡行名称
				    		fileMap.put("F_9", subMap.get("L_9").toString().replace("-", ""));//交易金额
				    		fileMap.put("F_10", subMap.get("L_10").toString().replace("-", ""));//手续费
				    		fileMap.put("F_11", subMap.get("L_12"));//流水号
				    		fileMap.put("F_12", subMap.get("L_13"));//订单号
				    		fileMap.put("F_13", subMap.get("L_14"));//商户备注
				    		fileMap.put("F_14", "16");//交易状态
				    	}
			    	}
			    	if ("交易对账明细".equals(subMap.get("L_0").toString().trim())) {
			    		readflg = true;
			    	}
			    	if (fileMap != null && fileMap.size() !=0) {
					    fileList.add(fileMap);
					    fileMap = new HashMap();
			    	}
			    }
			}
		}
		
		Map configMap = new HashMap();//TL_M000003_ZF_20160531.csv
		String localfilennew = SettleConstants.FILE_PATH +accountDate2 + File.separator +"TL_" + merchantCode + "_"+ accountType +"_" + accountDate +".csv";
		File pathFile = new File(SettleConstants.FILE_PATH +accountDate2 + File.separator);
		if (!pathFile.exists()) {
			pathFile.mkdirs();
		}
		configMap.put("FILE", localfilennew);
		
		fileMap = new HashMap();
		fileMap.put("F_1", "START");
		headList.add(fileMap);
		configMap.put("REPORT-HEAD", headList);
		
		fileMap = new HashMap();
		fileMap.put("F_1", "END");
		tailList.add(fileMap);
		configMap.put("REPORT-TAIL", tailList);
		
		try {
			txtWriter.WriteTxt(fileList, configMap, SettleConstants.DEDT_SPLIT2, "UTF-8");
		} catch(Exception e) {
			logger.error(">>> >>>对账文件写入异常！" + e.getMessage());
			return;
		}
	}
    
	@SuppressWarnings({ "rawtypes", "unchecked", "static-access" })
	public void editNewCollateFile(String localPath,List<String> fileNameL,String accountDate2,String accountDate,String merchantCode) {
		String fileName = "";
		File collFile = null;
    	TxtReader txtReader = new TxtReader();
    	TxtWriter txtWriter = new TxtWriter();
    	List<Map> fileList = new ArrayList<Map>();
    	List<Map> headList = new ArrayList<Map>();
    	List<Map> tailList = new ArrayList<Map>();
    	Map fileMap;
    	List<Map> fileListSub;
    	Map fileMapSub;
    	int jj = 0;
    	int zz = 1;
		
		for (int i =0;i<fileNameL.size();i++) {
			fileName = fileNameL.get(i);
			fileListSub = new ArrayList<Map>();
			collFile = new File(localPath+fileName);
			if (!collFile.exists()) {
				continue;
			}
			try {
				txtReader.setEncode("GBK");
				fileListSub = txtReader.txtreader(collFile , SettleConstants.DEDT_SPLIT4);
			} catch(Exception e) {
				logger.error(">>> >>>对账文件读取异常！" + e.getMessage());
				return;
			}
			
			if (fileListSub == null || fileListSub.size() ==0) {
				continue;
			}
			fileMapSub = new HashMap();
			for (int j=1;j<=fileListSub.size();j++) {
				fileMap = new HashMap();
				fileMapSub = fileListSub.get(j-1);
				if (fileMapSub.size() ==4 || fileMapSub.size() ==1) {
					continue;
				}
				jj = 0;
				zz = 1;
				for (int z=0;z<13;z++) {
					if ("4047".equals(fileName)) {
						fileMap.put("F_"+zz, fileMapSub.get("L_"+jj)==null?"":fileMapSub.get("L_"+jj));
					} else {
						if (jj == 4) {
							fileMap.put("F_"+zz, "");
							zz++;
							fileMap.put("F_"+zz, fileMapSub.get("L_"+jj)==null?"":fileMapSub.get("L_"+jj));
						} else {
							fileMap.put("F_"+zz, fileMapSub.get("L_"+jj)==null?"":fileMapSub.get("L_"+jj));
						}
					}
					jj++;
					zz++;
				}
				fileMap.put("F_"+zz, fileName.substring(fileName.length()-16, fileName.length()-12));
				fileList.add(fileMap);
			}
		}
		
		Map configMap = new HashMap();
		String localfilennew = SettleConstants.FILE_PATH +accountDate2 + File.separator + accountDate +"_PAB_BankPayment_ZF_"+merchantCode+".txt";
		configMap.put("FILE", localfilennew);
		
		fileMap = new HashMap();
		fileMap.put("F_1", "START");
		headList.add(fileMap);
		configMap.put("REPORT-HEAD", headList);
		
		fileMap = new HashMap();
		fileMap.put("F_1", "END");
		tailList.add(fileMap);
		configMap.put("REPORT-TAIL", tailList);
		
		try {
			txtWriter.WriteTxt(fileList, configMap, SettleConstants.DEDT_SPLIT4, "GBK");
		} catch(Exception e) {
			logger.error(">>> >>>对账文件写入异常！" + e.getMessage());
			return;
		}
	}

	/***
	 * 下载多渠道对账文件
	 * @param 当前账期
	 * @param merchantCode
	 * @param fileType
	 * @return
	 */
	public  Map<String, String> multi_4013C006(String merchantCode, String fileType) throws Exception {
		return this.multi_4013C006(null, merchantCode, fileType);
	}
	
	/**
     * 指定账期下载多渠道对账文件
     * @param invoicedate 上游账期
     * @param rootInstId 机构号
     * @return
     */
    public Map<String, String> multi_4013C006(String invoicedate, String merchantCode, String fileType) {
    	logger.info("调用多渠道网关4013C006接口 ["+ merchantCode +"]协议 & ["+ fileType +"]交易类型 ————————————START————————————");
    	Map<String, String> rtnMap = new HashMap<String, String>();
		rtnMap.put("errCode", "0000");
		rtnMap.put("errMsg", "成功");
		
		/**
    	 * 获取账期
    	 */
    	ParameterInfoQuery keyList =  new ParameterInfoQuery();
    	String accountDate = "";
    	String accountDate2 = "";
    	try {
	    	if (invoicedate == null || "".equals(invoicedate)) {
//	    		keyList.setParameterCode(SettleConstants.ACCOUNTDATE);
//	    		invoicedate = parameterInfoManager.queryList(keyList).get(0).getParameterValue();
	    		invoicedate = String.valueOf(settlementUtil.getAccountDate("yyyy-MM-dd", 0, "String"));
				/*
				 * settlementUtil.getAccountDate(String fromat,int step,String returnType)
				 * 注入 parameterInfoManager对账失败
				 */
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
    	logger.info(">>> >>> 【指定账期下载多渠道网关对账文件】  取得的账期为 :"+ accountDate);

    	boolean flg_4013 = true;
    	boolean flg_C006 = true;
    	String dateforin = accountDate.substring(0, 4) + "-" + accountDate.substring(4, 6) + "-" + accountDate.substring(6, 8);
		Map<String,Map<String,String>> rtnMap_4013 = new HashMap<String,Map<String,String>>();
    	try {
    		rtnMap_4013 = multi_PAB.pab_4013(dateforin, merchantCode, SettleConstants.PAB_ACCOUNT_NO_MAIN1);
		} catch (Exception e) {
	    	logger.error("调用4013接口异常！" + e.getMessage());
			e.printStackTrace();
		}
		
    	if (rtnMap_4013 == null || rtnMap_4013.size() <= 0) {
    		flg_4013 = false;
	    	logger.info("调用4013接口返回为NULL！");
    	} else if (rtnMap_4013.containsKey("rtnCode")) {
    		flg_4013 = false;
	    	logger.info("调用4013接口返回:"+rtnMap_4013.get("rtnCode"));
    	}
    	logger.info(">>> >>> 调用4013结束，开始调用C006接口");
    	
    	Map<String,Map<String,String>> rtnMap_C006 = new HashMap<String,Map<String,String>>();
    	try {
    		rtnMap_C006 = multi_PAB.pab_C006(dateforin, merchantCode, SettleConstants.PAB_ACCOUNT_NO_MAIN1);
		} catch (Exception e) {
	    	logger.error("调用C006接口异常！" + e.getMessage());
			e.printStackTrace();
		}
		
    	if (rtnMap_C006 == null || rtnMap_C006.size() <= 0) {
    		flg_C006 = false;
	    	logger.info("调用C006接口返回为NULL！");
    	} else if (rtnMap_C006.containsKey("rtnCode")) {
    		flg_C006 = false;
	    	logger.info("调用C006接口返回:"+rtnMap_C006.get("rtnCode"));
    	}
    	
    	logger.info(">>> >>> 开始去重操作");
    	
    	if (flg_4013 && flg_C006) {
    		Iterator entries = rtnMap_C006.entrySet().iterator();  
    		while (entries.hasNext()) {  
    		    Map.Entry entry = (Map.Entry) entries.next();
    		    String key = (String)entry.getKey();  
    		    if (!rtnMap_4013.containsKey(key)) {
    		    	rtnMap_4013.put(key, rtnMap_C006.get(key));
    		    }
    		}
    	} else if (!flg_4013 && flg_C006) {
    		rtnMap_4013 = rtnMap_C006;
    	}
    	
    	if (!flg_4013 && !flg_C006) {
    		rtnMap.put("errCode", "0001");
    		rtnMap.put("errMsg", "上游返回信息为空！");
    		return rtnMap;
    	}

    	logger.info(">>> >>> 开始生成上游对账文件");
		Map configMap = new HashMap();
		List<Map> writerList = new ArrayList<Map>();
		List<Map> headList = new ArrayList<Map>();
    	TxtWriter txtWriter = new TxtWriter();
		List<Map> tailList = new ArrayList<Map>();
		String localPath = SettleConstants.FILE_PATH +accountDate2 + File.separator;
		String localfilennew = localPath + accountDate +"_PAB_BankPayment_ZF_M00000X.txt";
		File filepath = new File(localPath);
		if (!filepath.exists()) {
			filepath.mkdirs();
		}
		configMap.put("FILE", localfilennew);
		
		Map fileMap = new HashMap();
		fileMap.put("F_1", "START");
		headList.add(fileMap);
		configMap.put("REPORT-HEAD", headList);
		
		fileMap = new HashMap();
		fileMap.put("F_1", "END");
		tailList.add(fileMap);
		configMap.put("REPORT-TAIL", tailList);
		
		Iterator entries = rtnMap_4013.entrySet().iterator();  
		while (entries.hasNext()) {
		    Map.Entry entry = (Map.Entry) entries.next();
		    String key = (String)entry.getKey();
		    writerList.add(rtnMap_4013.get(key));
		}
		
		
		try {
			txtWriter.WriteTxt(writerList, configMap, SettleConstants.DEDT_SPLIT4, "GBK");
		} catch(Exception e) {
			logger.error(">>> >>>对账文件写入异常！" + e.getMessage());
			rtnMap.put("errCode", "0001");
			rtnMap.put("errMsg", "文件写入失败！"+e.getMessage());
			return rtnMap;
		}
		return rtnMap;
    }
    /**
     * 编辑 平安银行跨行快付 原始对账文件
     * @param localPath
     * @param fileNameL
     * @param accountDate2
     * @param accountDate
     * @param merchantCode
     */
    @SuppressWarnings({ "rawtypes", "unchecked", "static-access" })
	public void editNewCollateFile_KHKF(String localPath,List<String> fileNameL,String accountDate2,String accountDate,String merchantCode) throws Exception {
    	logger.info(">>> >>> >>> 开始  编辑平安银行_跨行快付 对账文件");
    	String fileName = "";
    	File collFile = null;
    	TxtReader txtReader = new TxtReader();
    	TxtWriter txtWriter = new TxtWriter();
    	List<Map> fileList = new ArrayList<Map>();
//    	List<Map> headList = new ArrayList<Map>();
//    	List<Map> tailList = new ArrayList<Map>();
    	Map fileMap;
    	List<Map> fileListSub;
    	Map fileMapSub;
    	String amountStrY = null;
    	String amountStrF = null;
    	String transDateStr = null;
    	String transTimeStr = null;
    	final String feeAmount = "80";
    	
		for (int i = 0; i < fileNameL.size(); i ++) {
			fileName = fileNameL.get(i);
			fileListSub = new ArrayList<Map>();
			collFile = new File(localPath+fileName);
			
			if (!collFile.exists()) {
				continue;
			}
			
			try {
				txtReader.setEncode("GBK");
				fileListSub = txtReader.txtreader(collFile , SettleConstants.DEDT_SPLIT5);
			} catch(Exception e) {
				logger.error(">>> >>>对账文件读取异常！" + e.getMessage(), e);
				return;
			}
			
			if (fileListSub == null || fileListSub.size() ==0) {
				continue;
			}
			
			if(fileName.indexOf("KHKF01") > -1) {
			  /*L_0		交易日期
				L_1		交易时间
				L_2		请算日期
				L_3		订单号
				L_4		批次号
				L_5		收款借记卡/账号
				L_6		金额
				L_7		实收手续费
				L_8		记账日期
				L_9		记账流水号
				L_10	错误码
				L_11	错误消息
				L_12	备注*/
				for (int j = 1; j < fileListSub.size(); j ++) {
					fileMap = new HashMap();
					fileMapSub = fileListSub.get(j);
					for(int z = 0; z < 13; z ++) {
						//复制列中信息
						fileMap.put("F_" + (z + 1), fileMapSub.get("L_" + z) == null ? "" : fileMapSub.get("L_" + z));
					}
					//原对账文件的金额单位为【元】 更改为【分】
					amountStrY = String.valueOf(fileMap.get("F_7"));
					amountStrF = super.getAmountStrF(amountStrY);
					fileMap.put("F_7", amountStrF);
					//状态 逻辑设计依照 SETTLE_PARAMETER_INFO PARAMETER=1000000006配置
					String readStatusId = String.valueOf(fileMap.get("F_11"));
					String statusId = "0000".equals(readStatusId) ? "16" : "14";
					fileMap.put("F_11", statusId);	
					//FUNC_CODE
					fileMap.put("F_14", "4014");
					//手续费 FEE_AMOUNT
					amountStrY = String.valueOf(fileMap.get("F_8"));
					if(amountStrY != null && !amountStrY.trim().equals("")) {
						amountStrF = super.getAmountStrF(amountStrY);
					} else {
						amountStrF = feeAmount;
					}
					fileMap.put("F_8", amountStrF);
					fileMap.put("F_15", amountStrF);
					//拼接交易日期时间
					transDateStr = String.valueOf(fileMap.get("F_1"));
					transTimeStr = String.valueOf(fileMap.get("F_2"));
					fileMap.put("F_2", transDateStr + transTimeStr);
					
					//存入List
					fileList.add(fileMap);
				}
			} else if(fileName.indexOf("KHKF02") > -1) {
			  /*L_0		差错退回日期
				L_1		原交易清算日期
				L_2		收款人帐号
				L_3		收款人名称
				L_4		银行交易流水
				L_5		金额
				L_6		请求方交易日期
				L_7		请求方交易流水号
				L_8		原送盘批次号
				L_9		差错状态
				L_10	原代付交易附言
				L_11	备注*/
				for (int j = 1; j < fileListSub.size(); j ++) {
					fileMap = new HashMap();
					fileMapSub = fileListSub.get(j);
//					for(int z = 0; z < 7; z ++) {
//						//复制列中信息
//						fileMap.put("F_" + (z + 1), fileMapSub.get("L_" + z) == null ? "" : fileMapSub.get("L_" + z));
//					}
//					for(int z = 8; z < 13; z ++) {
//						//复制列中信息
//						fileMap.put("F_" + (z + 1), fileMapSub.get("L_" + (z - 1)) == null ? "" : fileMapSub.get("L_" + (z - 1)));
//					}
					fileMap.put("F_1", fileMapSub.get("L_0") == null ? "" : fileMapSub.get("L_0"));
					fileMap.put("F_2", fileMapSub.get("L_1") == null ? "" : fileMapSub.get("L_1") + "000000");
					fileMap.put("F_3", fileMapSub.get("L_6") == null ? "" : fileMapSub.get("L_6"));
					fileMap.put("F_4", fileMapSub.get("L_7") == null ? "" : fileMapSub.get("L_7"));
					fileMap.put("F_5", fileMapSub.get("L_8") == null ? "" : fileMapSub.get("L_8"));
					fileMap.put("F_6", fileMapSub.get("L_2") == null ? "" : fileMapSub.get("L_2"));
					//原对账文件的金额单位为【元】 更改为【分】
					amountStrY = String.valueOf(fileMapSub.get("L_5"));
					amountStrF = this.getAmountStrF(amountStrY);
					fileMap.put("F_7", amountStrF);
					fileMap.put("F_8", "");
					fileMap.put("F_9", fileMapSub.get("L_6") == null ? "" : fileMapSub.get("L_6"));
					fileMap.put("F_10", "");
					fileMap.put("F_11", "16");
					fileMap.put("F_12", fileMapSub.get("L_10") == null ? "" : fileMapSub.get("L_10"));
					fileMap.put("F_13", fileMapSub.get("L_11") == null ? "" : fileMapSub.get("L_11"));
					//FUNC_CODE
					fileMap.put("F_14", "5024");
					//
					fileMap.put("F_15", "0");
					//存入List
					fileList.add(fileMap);
				}
			}
		}
		
		Map configMap = new HashMap();
		String localfilennew = SettleConstants.FILE_PATH +accountDate2 + File.separator + accountDate + "_PAB_BankPayment_KHKF_" + merchantCode + ".txt";
		configMap.put("FILE", localfilennew);
//定义头文件		
//		fileMap = new HashMap();
//		fileMap.put("F_1", "START");
//		headList.add(fileMap);
//		configMap.put("REPORT-HEAD", headList);
//定义尾文件
//		fileMap = new HashMap();
//		fileMap.put("F_1", "END");
//		tailList.add(fileMap);
//		configMap.put("REPORT-TAIL", tailList);		
		try {
			txtWriter.WriteTxt(fileList, configMap, SettleConstants.DEDT_SPLIT5, "GBK");
		} catch(Exception e) {
			logger.error(">>> >>>对账文件写入异常！" + e.getMessage(), e);
			return;
		}
		logger.info("<<< <<< <<< 结束  编辑平安银行_跨行快付 对账文件");
	}
    
    /**
     * 编辑 民生单笔代付 原始对账文件
     * @param localPath
     * @param fileNameL
     * @param accountDate2
     * @param accountDate
     * @param merchantCode
     */
    @SuppressWarnings({ "rawtypes", "unchecked", "static-access" })
	public void editNewCollateFile_CMBC(String localPath,List<String> fileNameL,String accountDate2,String accountDate,String merchantCode) throws Exception {
    	logger.info(">>> >>> >>> 开始  编辑民生单笔代付 原始对账文件");
    	String fileName = "";
    	File collFile = null;
    	TxtReader txtReader = new TxtReader();
    	TxtWriter txtWriter = new TxtWriter();
    	List<Map> fileList = new ArrayList<Map>();
    	Map fileMap;
    	List<Map> fileListSub;
    	Map fileMapSub;
    	
		for (int i = 0; i < fileNameL.size(); i ++) {
			fileName = fileNameL.get(i);
			logger.info("localPath+fileName="+localPath+fileName);
			fileListSub = new ArrayList<Map>();
			collFile = new File(localPath+fileName);
			
			if (!collFile.exists()) {
				continue;
			}
			
			try {
				txtReader.setEncode("UTF-8");
				fileListSub = txtReader.txtreader(collFile , SettleConstants.DEDT_SPLIT);
			} catch(Exception e) {
				logger.error(">>> >>>对账文件读取异常！" + e.getMessage(), e);
				return;
			}
			
			if (fileListSub == null || fileListSub.size() ==0) {
				continue;
			}
			
			if(fileName.indexOf("DF_SHJJSYBL_DG_DZWJ") > -1) {
			  /*L_0		交易服务码
				L_1		合作方流水号
				L_2		银行处理流水号
				L_3		收款人账户号
				L_4		收款人账户名
				L_5		交易金额(单位为分)
				L_6		应答码类型
				L_7		应答码
				L_8		应答描述
				L_9		银行对账日期
				文件尾：########
				*/
				for (int j = 0; j < fileListSub.size()-1; j ++) {
					fileMap = new HashMap();
					fileMapSub = fileListSub.get(j);
					for(int z = 0; z < 10; z ++) {
						//复制列中信息
						fileMap.put("F_" + (z+1), fileMapSub.get("L_" + z) == null ? "" : fileMapSub.get("L_" + z));
					}
					//状态 逻辑设计依照 SETTLE_PARAMETER_INFO PARAMETER=1000000006配置
					String readStatusId = String.valueOf(fileMap.get("F_8"));
					String statusId = "00".equals(readStatusId) ? "16" : "14";
					fileMap.put("F_12", statusId);
					//FUNC_CODE
					fileMap.put("F_11", "4014");
					fileMap.put("F_13", 99);
					
					//存入List
					fileList.add(fileMap);
					
				}
			}else if(fileName.indexOf("DF_SHJJSYBL_DZWJ") > -1) {
			  /*L_0		交易服务码
				L_1		合作方流水号
				L_2		银行处理流水号
				L_3		收款人账户号
				L_4		收款人账户名
				L_5		交易金额(单位为分)
				L_6		应答码类型
				L_7		应答码
				L_8		应答描述
				L_9		银行对账日期
				文件尾：########
				*/
				for (int j = 0; j < fileListSub.size()-1; j ++) {
					fileMap = new HashMap();
					fileMapSub = fileListSub.get(j);
					for(int z = 0; z < 10; z ++) {
						//复制列中信息
						fileMap.put("F_" + (z+1), fileMapSub.get("L_" + z) == null ? "" : fileMapSub.get("L_" + z));
					}
					//状态 逻辑设计依照 SETTLE_PARAMETER_INFO PARAMETER=1000000006配置
					String readStatusId = String.valueOf(fileMap.get("F_8"));
					String statusId = "00".equals(readStatusId) ? "16" : "14";
					fileMap.put("F_12", statusId);
					//FUNC_CODE
					fileMap.put("F_11", "4014");
					fileMap.put("F_13", 99);
					
					//存入List
					fileList.add(fileMap);
					
				}
			}else if(fileName.indexOf("DF_SHJJSYBL_DG_THWJ") > -1) {
				  /*L_0		交易服务码
					L_1		合作方流水号
					L_2		银行处理流水号
					L_3		收款人账户号
					L_4		收款人账户名
					L_5		交易金额(单位为分)
					L_6		应答码类型
					L_7		应答码
					L_8		应答描述
					L_9		银行对账日期
					L_10	退汇日期
					文件尾：########
					*/
					for (int j = 0; j < fileListSub.size()-1; j ++) {
						fileMap = new HashMap();
						fileMapSub = fileListSub.get(j);
						for(int z = 0; z < 11; z ++) {
							//复制列中信息
							fileMap.put("F_" + (z+1), fileMapSub.get("L_" + z) == null ? "" : fileMapSub.get("L_" + z));
						}
						//状态 逻辑设计依照 SETTLE_PARAMETER_INFO PARAMETER=1000000006配置
						String readStatusId = String.valueOf(fileMap.get("F_8"));
						String statusId = "00".equals(readStatusId) ? "16" : "14";
						fileMap.put("F_13", statusId);
						//FUNC_CODE
						fileMap.put("F_12", "4014");
						fileMap.put("F_14", 99);
						
						//存入List
						fileList.add(fileMap);
						
					}
				}else if(fileName.indexOf("DF_SHJJSYBL_THWJ") > -1) {
					  /*L_0		交易服务码
						L_1		合作方流水号
						L_2		银行处理流水号
						L_3		收款人账户号
						L_4		收款人账户名
						L_5		交易金额(单位为分)
						L_6		应答码类型
						L_7		应答码
						L_8		应答描述
						L_9		银行对账日期
						L_10	退汇日期
						文件尾：########
						*/
						for (int j = 0; j < fileListSub.size()-1; j ++) {
							fileMap = new HashMap();
							fileMapSub = fileListSub.get(j);
							for(int z = 0; z < 11; z ++) {
								//复制列中信息
								fileMap.put("F_" + (z+1), fileMapSub.get("L_" + z) == null ? "" : fileMapSub.get("L_" + z));
							}
							//状态 逻辑设计依照 SETTLE_PARAMETER_INFO PARAMETER=1000000006配置
							String readStatusId = String.valueOf(fileMap.get("F_8"));
							String statusId = "00".equals(readStatusId) ? "16" : "14";
							fileMap.put("F_13", statusId);
							//FUNC_CODE
							fileMap.put("F_12", "4014");
							
							fileMap.put("F_14", 99);
							
							//存入List
							fileList.add(fileMap);
							
						}
			     }
		}
		
		Map configMap = new HashMap();
		String localfilennew = null;
		if(fileName.indexOf("DF_SHJJSYBL_DG_DZWJ") > -1) {
			localfilennew = SettleConstants.FILE_PATH +accountDate + File.separator + "DF_SHJJSYBL_DG_DZWJ_"+accountDate+".txt";
		}else if(fileName.indexOf("DF_SHJJSYBL_DZWJ") > -1) {
			localfilennew = SettleConstants.FILE_PATH +accountDate + File.separator + "DF_SHJJSYBL_DZWJ_"+accountDate+".txt";
		}else if(fileName.indexOf("DF_SHJJSYBL_DG_THWJ") > -1) {
			localfilennew = SettleConstants.FILE_PATH +accountDate + File.separator + "DF_SHJJSYBL_DG_THWJ_"+accountDate+".txt";
		}else if(fileName.indexOf("DF_SHJJSYBL_THWJ") > -1) {
			localfilennew = SettleConstants.FILE_PATH +accountDate + File.separator + "DF_SHJJSYBL_THWJ_"+accountDate+".txt";
		}
		configMap.put("FILE", localfilennew);

		try {
			txtWriter.WriteTxt(fileList, configMap, SettleConstants.DEDT_SPLIT, "UTF-8");
		} catch(Exception e) {
			logger.error(">>> >>>对账文件写入异常！" + e.getMessage(), e);
			return;
		}
		logger.info("<<< <<< <<< 结束  编辑民生单笔代付对账文件");
	}
    
    public Map<String, String> cmbcMultiFileDown(String invoicedate, String merchantCode, String fileType,String payChannelId) throws Exception {
    	logger.info("下载多渠道网关对账文件 ["+ merchantCode +"]协议 & ["+ fileType +"]交易类型 ————————————START————————————");
    	Map<String, String> rtnMap = new HashMap<String, String>();
		rtnMap.put("errCode", "0000");
		rtnMap.put("errMsg", "成功");
		
		/**
    	 * 判断日中是否正常结束
    	 */
		if (!settlementUtil.cutDayIsSuccess4Account()) {
    		rtnMap.put("errCode", "0001");
    		rtnMap.put("errMsg", "日终没有正常结束！");
    		RkylinMailUtil.sendMailThread("清洁算开始异常","日终没有正常结束，不能开始清洁算操作", "21401233@qq.com");
    		return rtnMap;
    	}
    	String accountDate = "";
    	String accountDate2 = "";
    	try {
	    	if (invoicedate == null || "".equals(invoicedate)) {
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
    	logger.info(">>> >>> 【指定账期下载多渠道网关对账文件】  取得的账期为 :"+ accountDate);
		
    	String localPath = null;
    	FtpDownload ftpDownload = new FtpDownload();
    	boolean flg = false;
    	
    	logger.info(">>> >>> 连接融数金服FTP");
    	try {
    		String addr = ftpProperties.getProperty("CMBC_MULTIFTP_URL");
    		int port = Integer.parseInt(ftpProperties.getProperty("CMBC_MULTIFTP_PORT"));
    		String username = ftpProperties.getProperty("CMBC_MULTIFTP_NAME");
    		String password = ftpProperties.getProperty("CMBC_MULTIFTP_PASS");
    		logger.info("addr="+addr+",port="+port+",username="+username+",password="+password);
    		
    		flg = ftpDownload.connect(addr, port, username, password);
    		if (!flg) {
    			Thread.sleep(3000);
    			flg = ftpDownload.connect(addr, port, username, password);

        		if (!flg) {
	    			logger.error("FTP连接失败" + flg);
	    			rtnMap.put("errCode", "0001");
	    			rtnMap.put("errMsg", "FTP连接失败");
	    			return rtnMap;
        		}
    		}
    	} catch (Exception e) {
    		logger.error("异常【下载多渠道网关对账文件】连接FTP" + e.getMessage());
			rtnMap.put("errCode", "0001");
			rtnMap.put("errMsg", "异常【下载多渠道网关对账文件】");
			e.printStackTrace();
			return rtnMap;
    	}
    	logger.info(">>> >>> 连接融数金服FTP结果"+flg);
    	
    	logger.info(">>> >>> 从FTP上下载文件");
    	List<String> fileNameL = new ArrayList<String>();
    	String fileName = null;
    	if (SettleConstants.ACCOUNT_TYPE_CMBC_AgentPay_DL.equals(fileType)) {//民生银行 -单笔联机代付(代理支付)
        	localPath = SettleConstants.FILE_PATH + accountDate + File.separator+"CMBC_AgentPay_bak" + File.separator;
    		fileName = fileProperties.getProperty("CMBC_AgentPay_DL_"+merchantCode).replace("{YMD}", accountDate);
    		fileNameL.add(fileName);
    	}else if (SettleConstants.ACCOUNT_TYPE_CMBC_AgentPay_FDL.equals(fileType)) {//民生银行 -单笔联机代付(非代理支付)
        	localPath = SettleConstants.FILE_PATH + accountDate + File.separator+"CMBC_AgentPay_bak" + File.separator;
    		fileName = fileProperties.getProperty("CMBC_AgentPay_FDL_"+merchantCode).replace("{YMD}", accountDate);
    		fileNameL.add(fileName);
    	}else if (SettleConstants.ACCOUNT_TYPE_CMBC_Refund_DL.equals(fileType)) {//民生银行 -单笔联机代付退票(代理支付)
        	localPath = SettleConstants.FILE_PATH + accountDate + File.separator+"CMBC_Refund_bak" + File.separator;
    		fileName = fileProperties.getProperty("CMBC_Refund_DL_"+merchantCode).replace("{YMD}", accountDate);
    		fileNameL.add(fileName);
    	}else if (SettleConstants.ACCOUNT_TYPE_CMBC_Refund_FDL.equals(fileType)) {//民生银行 -单笔联机代付退票(非代理支付)
        	localPath = SettleConstants.FILE_PATH + accountDate + File.separator+"CMBC_Refund_bak" + File.separator;
    		fileName = fileProperties.getProperty("CMBC_Refund_FDL_"+merchantCode).replace("{YMD}", accountDate);
    		fileNameL.add(fileName);
    	}else {
    		fileNameL = null;
    		logger.error("异常【下载民生单笔对账文件】fileType不认识" + fileType);
    		rtnMap.put("errCode", "0001");
    		rtnMap.put("errMsg", "fileType不认识");
    		return rtnMap;
    	}
    	try {
    		for (int i=0;i<fileNameL.size();i++) {
    			fileName = fileNameL.get(i);
	    		flg = ftpDownload.download("pay/"+accountDate, fileName, localPath, "");
	    		if (!flg) {
	    			logger.error("FTP文件下载失败" + flg + "|" + fileName);
	    		} else {
	    			logger.error("FTP文件下载成功" + flg + "|" + fileName);
	    		}
    		}
    	} catch (Exception e) {
    		logger.error("异常【下载民生单笔对账文件】FTP文件下载" + e.getMessage());
			rtnMap.put("errCode", "0001");
			rtnMap.put("errMsg", "异常【下载民生单笔对账文件】");
			e.printStackTrace();
			return rtnMap;
    	}
    	logger.info(">>> >>> 从FTP上下载文件完毕");
    	logger.info(">>> >>> 关闭FTP连接");
    	try {
    		flg = ftpDownload.disConnect();
    		if (!flg) {
    			logger.error("关闭FTP连接失败" + flg);
    		}
    	} catch (Exception e) {
    		logger.error("异常【下载民生单笔对账文件】关闭FTP连接" + e.getMessage());
			rtnMap.put("errCode", "0001");
			rtnMap.put("errMsg", "异常【下载民生单笔对账文件】");
			e.printStackTrace();
			return rtnMap;
    	}
    	logger.info(">>> >>> 关闭FTP连接完毕");
    	
    	if (fileNameL != null) {
    		if(SettleConstants.ACCOUNT_TYPE_CMBC_AgentPay_DL.equals(fileType)) {
    			logger.info(">>> >>> 编辑民生单笔代付 对账文件(代理)");
    			editNewCollateFile_CMBC(localPath, fileNameL, accountDate2, accountDate, merchantCode);
    		}else if(SettleConstants.ACCOUNT_TYPE_CMBC_AgentPay_FDL.equals(fileType)) {
    			logger.info(">>> >>> 编辑民生单笔代付 对账文件(非代理)");
    			editNewCollateFile_CMBC(localPath, fileNameL, accountDate2, accountDate, merchantCode);
    		}else if(SettleConstants.ACCOUNT_TYPE_CMBC_Refund_DL.equals(fileType)) {
    			logger.info(">>> >>> 编辑民生单笔代付退票 对账文件(代理)");
    			editNewCollateFile_CMBC(localPath, fileNameL, accountDate2, accountDate, merchantCode);
    		}else if(SettleConstants.ACCOUNT_TYPE_CMBC_Refund_FDL.equals(fileType)) {
    			logger.info(">>> >>> 编辑民生单笔代付 退票对账文件(非代理)");
    			editNewCollateFile_CMBC(localPath, fileNameL, accountDate2, accountDate, merchantCode);
    		}
    	}
		return rtnMap;
    }
    
    /**
     * 编辑易宝代付 原始对账文件
     * @param localPath
     * @param fileNameL
     * @param accountDate2
     * @param accountDate
     * @param merchantCode
     */
    @SuppressWarnings({ "rawtypes", "unchecked", "static-access" })
	public void editNewCollateFile_YB_DF(String localPath,List<String> fileNameL,String accountDate2,String accountDate,String merchantCode) throws Exception {
    	logger.info(">>> >>> >>> 开始  编辑易宝代付 原始对账文件");
    	String fileName = "";
    	File collFile = null;
    	TxtReader txtReader = new TxtReader();
    	TxtWriter txtWriter = new TxtWriter();
    	List<Map> fileList = new ArrayList<Map>();
    	Map fileMap;
    	List<Map> fileListSub;
    	Map fileMapSub;
    	
		for (int i = 0; i < fileNameL.size(); i ++) {
			fileName = fileNameL.get(i);
			fileListSub = new ArrayList<Map>();
			collFile = new File(localPath+fileName);
			
			if (!collFile.exists()) {
				continue;
			}
			
			try {
				txtReader.setEncode("UTF-8");
				fileListSub = txtReader.txtreader(collFile , SettleConstants.DEDT_SPLIT);
			} catch(Exception e) {
				logger.error(">>> >>>对账文件读取异常！" + e.getMessage(), e);
				return;
			}
			
			if (fileListSub == null || fileListSub.size() ==0) {
				continue;
			}
			
			if(fileName.indexOf("Yeepay_AgentPay") > -1) {
			  /*L_0		商户批次号
				L_1		商户订单号
				L_2		创建时间
				L_3		银行名称
				L_4		户名
				L_5		账号
				L_6		打款金额
				L_7		手续费
				L_8		实付金额
				L_9		易宝状态
				L_10	扣账状态
				L_11	银行状态
				*/
				for (int j = 0; j < fileListSub.size(); j ++) {
					fileMap = new HashMap();
					fileMapSub = fileListSub.get(j);
					for(int z = 0; z < 12; z ++) {
						//复制列中信息
						fileMap.put("F_" + (z+1), fileMapSub.get("L_" + z) == null ? "" : fileMapSub.get("L_" + z));
					}
					//状态 逻辑设计依照 SETTLE_PARAMETER_INFO PARAMETER=1000000006配置
					String readStatusId = String.valueOf(fileMap.get("F_12"));
					String statusId = "SUCCESS".equals(readStatusId) ? "16" : "14";
					fileMap.put("F_13", statusId);
					//FUNC_CODE
					fileMap.put("F_14", "4014");
					fileMap.put("F_15", 99);
					
					//存入List
					fileList.add(fileMap);
					
				}
			}
		}
		
		Map configMap = new HashMap();
		String localfilennew = null;
		if(fileName.indexOf("Yeepay_AgentPay") > -1) {
			localfilennew = SettleConstants.FILE_PATH +accountDate2 + File.separator +accountDate+"_Yeepay_AgentPay_10014065388.csv";
		}
		configMap.put("FILE", localfilennew);

		try {
			txtWriter.WriteTxt(fileList, configMap, SettleConstants.DEDT_SPLIT, "UTF-8");
		} catch(Exception e) {
			logger.error(">>> >>>对账文件写入异常！" + e.getMessage(), e);
			return;
		}
		logger.info("<<< <<< <<< 结束  编辑易宝代付对账文件");
	}
    
    /**
     * 编辑融宝代付 原始对账文件
     * @param localPath
     * @param fileNameL
     * @param accountDate2
     * @param accountDate
     * @param merchantCode
     */
    @SuppressWarnings({ "rawtypes", "unchecked", "static-access" })
	public void editNewCollateFile_RB_DF(String localPath,List<String> fileNameL,String accountDate2,String accountDate,String merchantCode) throws Exception {
    	logger.info(">>> >>> >>> 开始  编辑融宝代付 原始对账文件");
    	String fileName = "";
    	File collFile = null;
    	TxtReader txtReader = new TxtReader();
    	TxtWriter txtWriter = new TxtWriter();
    	List<Map> fileList = new ArrayList<Map>();
    	Map fileMap;
    	List<Map> fileListSub;
    	Map fileMapSub;
    	
		for (int i = 0; i < fileNameL.size(); i ++) {
			fileName = fileNameL.get(i);
			fileListSub = new ArrayList<Map>();
			collFile = new File(localPath+fileName);
			
			if (!collFile.exists()) {
				continue;
			}
			
			try {
				txtReader.setEncode("UTF-8");
				fileListSub = txtReader.txtreader(collFile , SettleConstants.DEDT_SPLIT);
			} catch(Exception e) {
				logger.error(">>> >>>对账文件读取异常！" + e.getMessage(), e);
				return;
			}
			
			if (fileListSub == null || fileListSub.size() ==0) {
				continue;
			}
			
			if(fileName.indexOf("Reapal_AgentPay") > -1) {
			  /*L_0		融宝订单流水号
				L_1		批次文件名
				L_2		商户订单号
				L_3		银行账户
				L_4		开户名
				L_5		开户行
				L_6		账号类型
				L_7		订单金额
				L_8		实际付款金额
				L_9		手续费
				L_10	币种
				L_11	创建时间
				L_12	交易完毕时间
				L_13	交易状态
				*/
				for (int j = 1; j < fileListSub.size(); j ++) {
					fileMap = new HashMap();
					fileMapSub = fileListSub.get(j);
					for(int z = 0; z < 14; z ++) {
						//复制列中信息
						fileMap.put("F_" + (z+1), fileMapSub.get("L_" + z) == null ? "" : fileMapSub.get("L_" + z));
					}
					//状态 逻辑设计依照 SETTLE_PARAMETER_INFO PARAMETER=1000000006配置
					String statusId = "16";//文档中说明了只有成功的交易
					fileMap.put("F_15", statusId);
					//FUNC_CODE
					fileMap.put("F_16", "4014");
					fileMap.put("F_17", 99);
					
					//存入List
					fileList.add(fileMap);
				}
			}
		}
		
		Map configMap = new HashMap();
		String localfilennew = null;
		if(fileName.indexOf("Reapal_AgentPay") > -1) {
			localfilennew = SettleConstants.FILE_PATH +accountDate2 + File.separator +accountDate+"_Reapal_AgentPay_100000001300720.txt";
		}
		configMap.put("FILE", localfilennew);

		try {
			txtWriter.WriteTxt(fileList, configMap, SettleConstants.DEDT_SPLIT, "UTF-8");
		} catch(Exception e) {
			logger.error(">>> >>>对账文件写入异常！" + e.getMessage(), e);
			return;
		}
		logger.info("<<< <<< <<< 结束  编辑融宝代付对账文件");
	}
}
