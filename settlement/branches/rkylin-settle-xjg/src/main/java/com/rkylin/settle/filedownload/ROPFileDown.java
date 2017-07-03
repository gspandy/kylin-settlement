package com.rkylin.settle.filedownload;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import com.rkylin.settle.util.SettlementUtil;
import com.rkylin.settle.util.TxtReader;
import com.rkylin.settle.util.TxtWriter;


@Component("rOPFileDown")
public class ROPFileDown {
	protected static Logger logger = LoggerFactory.getLogger(ROPFileDown.class);

	@Autowired
	SettlementUtil settlementUtil;
	@Autowired
	Properties userProperties;
	@Autowired
	ParameterInfoManager parameterInfoManager;
	
	public Map<String, String> ROPfileDown(int type,String batch,Date invoicedateD,int flg ,String priOrPubKey,String fileType) {
		logger.info("下载ROP对账文件 ["+ type +"]文件类型 & ["+ batch +"]批次号& ["+ invoicedateD +"]账期 ————————————START————————————");
    	Map<String, String> rtnMap = new HashMap<String, String>();
		rtnMap.put("errCode", "0000");
		rtnMap.put("errMsg", "成功");
		Map<String, String> ropMap = new HashMap<String, String>();
    	ParameterInfoQuery keyList =  new ParameterInfoQuery();
    	SimpleDateFormat ymdformat = new SimpleDateFormat("yyyy-MM-dd");
    	String invoicedate = "";
		String accountDate = "";
    	String accountDate2 = "";
    	try {
    		if (invoicedateD == null || "".equals(invoicedateD)) {
    			
    		} else {
    			invoicedate = ymdformat.format(invoicedateD);
    		}
    		
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
    	logger.info(">>> >>> 【指定账期下载ROP对账文件】  取得的账期为 :"+ accountDate);
		
		String path = null;
		String fileName = null;
		Date dateDta = null;
		try {
		//日期格式对账
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			//获取string形式的账期
			dateDta = sdf.parse(accountDate);
			//拼接过去上传目录 SettleConstants.FILE_UP_PATH:约定目录 + dateStr:账期string +　 File.separator：动态获取系统分隔符
			path = SettleConstants.FILE_PATH + accountDate2 + File.separator;
			fileName = accountDate+"_"+type+batch+"."+fileType;
			//声明实例化目录
			File filePath = new File(path);
			if (!filePath.exists()) {//此目录不存在
				//创建目录
				filePath.mkdirs();
			}
		} catch (Exception e) {
			logger.error("生成/查找下载目录失败！"+e.getMessage());
			e.getStackTrace();
			rtnMap.put("errCode", "0001");
			rtnMap.put("errMsg", "生成/查找下载目录失败！");
			return rtnMap;
		}

		String priOrPubKeyArr = null;
		try {
			priOrPubKeyArr = userProperties.getProperty(priOrPubKey);
		} catch (Exception e) {
			logger.error("查找解密密钥失败！{"+priOrPubKey+"}"+e.getMessage());
			e.getStackTrace();
			rtnMap.put("errCode", "0001");
			rtnMap.put("errMsg", "查找解密密钥失败！{"+priOrPubKey+"}");
			return rtnMap;
		}

		try {
		ropMap = settlementUtil.getFileFromROP(type,
				batch, 
				dateDta, 
				path+fileName, 
				priOrPubKeyArr, 
				flg, 
				userProperties.getProperty("FSAPP_KEY"), 
				userProperties.getProperty("FSDAPP_SECRET"), 
				userProperties.getProperty("FSROP_URL"),
				fileType);
		} catch (Exception e) {
			logger.error("ROP执行失败！"+e.getMessage());
			e.getStackTrace();
			rtnMap.put("errCode", "0001");
			rtnMap.put("errMsg", "ROP执行失败！");
			return rtnMap;
		}
		
		if (!ropMap.containsKey("file")) {
			logger.error("ROP执行下载失败！");
			rtnMap.put("errCode", "0001");
			rtnMap.put("errMsg", "ROP执行下载失败！");
			return rtnMap;
		}
		
		if (type == 78) {// 银联商务
	    	logger.info(">>> >>> 编辑银联商务对账文件");
    		editNewCollateFile(ropMap.get("file"),accountDate2,accountDate,"M666666");
		}
		
		logger.info("下载ROP对账文件 ["+ type +"]文件类型 & ["+ batch +"]批次号& ["+ accountDate +"]账期 ————————————END————————————");
		return rtnMap;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked", "static-access" })
	public void editNewCollateFile(String filePath,String accountDate2,String accountDate,String merchantCode) {
		String fileName = "";
		File collFile = null;
    	TxtReader txtReader = new TxtReader();
    	TxtWriter txtWriter = new TxtWriter();
    	List<Map> fileList = new ArrayList<Map>();
    	List<Map> headList = new ArrayList<Map>();
    	List<Map> tailList = new ArrayList<Map>();
    	Map fileMap;
    	List<Map> fileListSub = new ArrayList<Map>();
    	Map fileMapSub;
    	int jj = 0;
    	int zz = 1;

		collFile = new File(filePath);
		if (!collFile.exists()) {
			return;
		}
		try {
			txtReader.setEncode("UTF-8");
			fileListSub = txtReader.txtreader(collFile , SettleConstants.DEDT_SPLIT);
		} catch(Exception e) {
			logger.error(">>> >>>对账文件读取异常！" + e.getMessage());
			return;
		}
		
		if (fileListSub == null || fileListSub.size() ==0) {
			return;
		}
		fileMapSub = new HashMap();
		for (int j=1;j<=fileListSub.size();j++) {
			fileMap = new HashMap();
			fileMapSub = fileListSub.get(j-1);
			if (fileMapSub.size() <= 4) {//标题行
				continue;
			}
			fileMap.put("F_1", fileMapSub.get("L_0").toString() + fileMapSub.get("L_1").toString());//POS请求日期
			fileMap.put("F_2", fileMapSub.get("L_1"));//POS请求时间
			fileMap.put("F_3", fileMapSub.get("L_2"));//卡号
			fileMap.put("F_4", fileMapSub.get("L_3"));//金额
			fileMap.put("F_5", fileMapSub.get("L_4"));//终端流水号
			fileMap.put("F_6", fileMapSub.get("L_5"));//终端号
			fileMap.put("F_7", fileMapSub.get("L_6"));//商户号
			fileMap.put("F_8", fileMapSub.get("L_7"));//批次号
			fileMap.put("F_9", fileMapSub.get("L_8"));//清算日期
			fileMap.put("F_10", fileMapSub.get("L_9"));//检索参考号
//			fileMap.put("F_11", fileMapSub.get("L_10"));//分账标志(0-未分账 1-以分账)
			if (fileMapSub.get("L_10") == null || "".equals(fileMapSub.get("L_10").toString())) {
				fileMap.put("F_11", "0");//卡类型标示(1:借记卡2:贷记卡3:准贷记卡4:储蓄卡0:暂无说明)
			} else {
				fileMap.put("F_11", fileMapSub.get("L_10"));//卡类型标示(1:借记卡2:贷记卡3:准贷记卡4:储蓄卡0:暂无说明)
			}
//			fileMap.put("F_13", fileMapSub.get("L_12"));//退货标志(0-未退货 1-以退货)
			if (fileMapSub.get("L_11") == null || "".equals(fileMapSub.get("L_11").toString())) {
				fileMap.put("F_12", "0.00");//消费手续费
			} else {
				fileMap.put("F_12", fileMapSub.get("L_11"));//消费手续费
			}
			fileMap.put("F_13", "16");//状态
//			if ("1".equals(fileMapSub.get("L_12"))) {
//				fileMap.put("F_14", "4017");//交易类型
//			} else {
				fileMap.put("F_14", "4015");//交易类型
//			}
			fileMap.put("F_15", fileMapSub.get("L_12"));//钱包流水号
			fileList.add(fileMap);
		}
		
		Map configMap = new HashMap();
		String localfilennew = SettleConstants.FILE_PATH +accountDate2 + File.separator + accountDate +"_POS_"+merchantCode+".txt";
		configMap.put("FILE", localfilennew);
		
		fileMap = new HashMap();
		fileMap.put("F_1", "START");
		headList.add(fileMap);
		configMap.put("REPORT-HEAD", headList);
		
//		fileMap = new HashMap();
//		fileMap.put("F_1", "END");
//		tailList.add(fileMap);
//		configMap.put("REPORT-TAIL", tailList);
		
		try {
			txtWriter.WriteTxt(fileList, configMap, SettleConstants.DEDT_SPLIT, "UTF-8");
		} catch(Exception e) {
			logger.error(">>> >>>对账文件写入异常！" + e.getMessage());
			return;
		}
		collFile.delete();
	}
	
}
