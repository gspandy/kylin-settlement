package com.rkylin.settle.filedownload;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rkylin.settle.constant.SettleConstants;
import com.rkylin.settle.manager.ParameterInfoManager;
import com.rkylin.settle.util.FtpDownload;
import com.rkylin.settle.util.SettlementUtil;
import com.rkylin.settle.util.TxtWriter;

@Component("lycheeDownload")
public class LycheeDownload extends CheckfileDownload {
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
	
	/**
	 * 
	 * @param invoicedate
	 * @param merchantCode
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> lycheeFileDown(String invoicedate, String merchantCode) throws Exception {
		logger.info(">>> >>> >>> >>> 开始: 下载  快付通对账文件 多渠道FTP ["+ merchantCode +"]协议 , ["+ invoicedate +"]账期");
    	Map<String, String> rtnMap = new HashMap<String, String>();
		rtnMap.put("errCode", "0000");
		rtnMap.put("errMsg", "成功");
		
		/*
		 * 获取账期
		 */
		String accountDate = "";
    	String accountDate2 = "";
    	try {
	    	if (invoicedate == null || "".equals(invoicedate)) {
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
	    	logger.error("异常:下载  快付通对账文件 多渠道FTP", e2);
			rtnMap.put("errCode", "0001");
			rtnMap.put("errMsg", "计算账期异常！");
			return rtnMap;
	    }
    	logger.info(">>> >>> >>> 下载  快付通对账文件 多渠道FTP 取得的账期为 : "+ accountDate);
		
    	/*
    	 * 连接融数金服FTP
    	 */
    	String localPath = null;
    	FtpDownload ftpDownload = new FtpDownload();
    	boolean flg = false;
    	logger.info(">>> >>> >>> 连接融数金服FTP");
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
    		logger.error("异常:下载  快付通对账文件 多渠道FTP", e);
			rtnMap.put("errCode", "0001");
			rtnMap.put("errMsg", "异常:下载  快付通对账文件 多渠道FTP");
			e.printStackTrace();
			return rtnMap;
    	}    	
    	/*
    	 * 从FTP上下载文件
    	 */
    	localPath = SettleConstants.FILE_PATH +accountDate2 + File.separator + "LYCHEE_bak" + File.separator;
    	String fileNameAcp = fileProperties.getProperty(merchantCode + "_LYCHEE_ACP");	//代收
    	String fileNamePay = fileProperties.getProperty(merchantCode + "_LYCHEE_PAY");	//代付
    	logger.info(">>> >>> >>> 从融数金服FTP 下载文件");
    	try {
    		flg = ftpDownload.download(accountDate, fileNameAcp, localPath, "");
        	logger.info(">>> >>> >>> FTP文件下载" + flg + ", 文件:" + fileNameAcp);
    	} catch(Exception e) {
    		logger.error(">>> >>> >>> 异常:FTP文件下载, 文件:" + fileNameAcp, e);
    		e.printStackTrace();
    	}
    	try {
    		flg = ftpDownload.download(accountDate, fileNamePay, localPath, "");
    		logger.info(">>> >>> >>> FTP文件下载" + flg + ", 文件:" + fileNamePay);
    	} catch(Exception e) {
    		logger.error(">>> >>> >>> 异常:FTP文件下载, 文件:" + fileNamePay, e);
    		e.printStackTrace();
    	}
    	/*
    	 * 关闭FTP连接
    	 */
    	logger.info(">>> >>> >>> 关闭FTP连接");
    	try {
    		flg = ftpDownload.disConnect();
    		if (!flg) {
    			logger.error(">>> >>> >>> 关闭FTP连接失败" + flg);
    		}
    	} catch (Exception e) {
    		logger.error(">>> >>> >>> 异常:关闭FTP连接", e);
			rtnMap.put("errCode", "0001");
			rtnMap.put("errMsg", "异常:关闭FTP连接");
			e.printStackTrace();
			return rtnMap;
    	}
    	/*
    	 * 读取快付通对账文件
    	 */
    	List<Map<String, String>> list = null;
    	File fileAcp = new File(localPath + fileNameAcp);
    	File filePay = new File(localPath + fileNamePay);
    	list = this.readJsonFileToList(new File[]{fileAcp, filePay});
    	if(list == null || list.size() < 1)  {
    		logger.error(">>> >>> >>> 读取快付通对账文件0个");
			rtnMap.put("errCode", "0001");
			rtnMap.put("errMsg", "读取快付通对账文件0个");
			return rtnMap;
    	}
    	/*
    	 * 编辑快付通对账文件
    	 */
    	list = this.editLycheelFile(list);
    	/*
    	 * 生成新的对账文件
    	 */
    	this.createFile(accountDate, accountDate2, merchantCode, list);
    	
		logger.info("<<< <<< <<< <<< 结束: 下载  快付通对账文件 多渠道FTP ["+ merchantCode +"]协议 , ["+ invoicedate +"]账期");
		return rtnMap;
	}
	
	/**
	 * 读取JSON文件
	 * @param jsonFile
	 * @return
	 */
	private List<Map<String, String>> readJsonFileToList(File[] jsonFileArr) throws Exception {
		logger.info(">>> >>> >>> 开始:读取JSON文件");
		List<Map<String, String>> list = null;
		FileReader fr = null;
		BufferedReader br = null;
		String json = null;
		Map<String, String> mapItem = null;
		JSONObject jsonObject = null;
		if(jsonFileArr == null || jsonFileArr.length < 1) {
			logger.error(">>> >>> >>> 异常:jsonFileArr is null or jsonFileArr.length < 1");
			throw new Exception("jsonFileArr is null or jsonFileArr.length < 1");
		}
		
		list = new ArrayList<Map<String, String>>();
		for(File jsonFile : jsonFileArr) {
			if(!jsonFile.exists()) {
				logger.info(">>> >>> >>> " + jsonFile.getName() + ", 不存在!");
				continue;
			}
			logger.info(">>> >>> >>> " + jsonFile.getName() + ", 读取中 ... ...");
			fr = new FileReader(jsonFile);
			br = new BufferedReader(fr);
			while((json = br.readLine()) != null) {
				mapItem = new HashMap<String, String>();
				jsonObject = JSONObject.fromObject(json);
				mapItem.put("amount", jsonObject.getString("amount"));
            	mapItem.put("checkDate", jsonObject.getString("checkDate"));
            	mapItem.put("currency", jsonObject.getString("currency"));
            	mapItem.put("errorCode", jsonObject.getString("errorCode"));
            	mapItem.put("failureDetails", jsonObject.getString("failureDetails"));
            	mapItem.put("kftTradeTime", jsonObject.getString("kftTradeTime"));
            	mapItem.put("orderNo", jsonObject.getString("orderNo"));
            	mapItem.put("payeeBankAccountNo", jsonObject.getString("payeeBankAccountNo"));
            	mapItem.put("payerBankAccountNo", jsonObject.getString("payerBankAccountNo"));
            	mapItem.put("productNo", jsonObject.getString("productNo"));
            	mapItem.put("status", jsonObject.getString("status"));
            	mapItem.put("fileName", jsonFile.getName());
            	list.add(mapItem);
            }
			if(br != null) br.close();
			if(fr != null) fr.close();
		}
		
		logger.info("<<< <<< <<< 结束:读取JSON文件");
		return list;
	}
	/**
	 * 编辑快付宝对账文件
	 * @param list
	 */
	private List<Map<String, String>> editLycheelFile(List<Map<String, String>> list) {
		logger.info(">>> >>> >>> >>> 开始:编辑快付宝对账文件");
		List<Map<String, String>> newList = new ArrayList<Map<String, String>>();
    	Map<String, String> newRowMap;
    	String funcCodeT;
    	String statusId;
    	
    	for(Map<String, String> rowMap : list) {
    		if(rowMap.get("fileName").endsWith("_Lychee_acp.json")) {
    			funcCodeT = "4013";
    		} else if(rowMap.get("fileName").endsWith("_Lychee_pay.json")) {
    			funcCodeT = "4014";
    		} else {
    			funcCodeT = "";
    			logger.error(">>> >>> >>> 未知的对账文件:" + funcCodeT);
    		}
    		if("1".equals(rowMap.get("status"))) {
    			statusId = "16";
    		} else if("2".equals(rowMap.get("status"))) {
    			statusId = "14";
    		} else if("3".equals(rowMap.get("status"))) {
    			statusId = "12";
    		} else {
    			statusId = rowMap.get("status");
    		}
    		newRowMap = new HashMap<String, String>();
    		newRowMap.put("F_1", rowMap.get("orderNo") == null ? "" : rowMap.get("orderNo"));
    		newRowMap.put("F_2", rowMap.get("productNo") == null ? "" : rowMap.get("productNo"));
    		newRowMap.put("F_3", rowMap.get("kftTradeTime"));//例子:2017-02-20 15:08:19
    		newRowMap.put("F_4", rowMap.get("amount") == null ? "0" : rowMap.get("amount").replace(",", ""));
    		newRowMap.put("F_5", "");
    		newRowMap.put("F_6", funcCodeT);
    		newRowMap.put("F_7", rowMap.get("errorCode"));
    		newRowMap.put("F_8", statusId);
    		newList.add(newRowMap);
    	}
    	logger.info("<<< <<< <<< <<< 结束:编辑快付宝对账文件");
    	return newList;
	}
	/**
	 * 创建清结算上游对账文件
	 * @param accountDate
	 * @param accountDate2
	 * @param merchantCode
	 */
	private void createFile(String accountDate, String accountDate2, String merchantCode, List<Map<String, String>> fileList) {
		logger.info(">>> >>> >>> 开始: 创建清结算上游对账文件");
		Map configMap = new HashMap();

		String localfilennew = SettleConstants.FILE_PATH +accountDate2 + File.separator + accountDate +"_Lychee_ZF_"+merchantCode+".txt";
		configMap.put("FILE", localfilennew);
		
		try {
			TxtWriter.WriteTxt(fileList, configMap, SettleConstants.DEDT_SPLIT2, "UTF-8");
		} catch(Exception e) {
			logger.error(">>> >>> 对账文件写入异常！" + e.getMessage(), e);
			return;
		}
		logger.info("<<< <<< <<< 结束: 创建清结算上游对账文件");
	}
}
