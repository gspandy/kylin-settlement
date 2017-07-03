package com.rkylin.settle.filedownload;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jcraft.jsch.SftpException;
import com.rkylin.file.txt.TxtWriter;
import com.rkylin.settle.constant.Constants;
import com.rkylin.settle.constant.SettleConstants;
import com.rkylin.settle.manager.ParameterInfoManager;
import com.rkylin.settle.util.SFTPUtil;
import com.rkylin.settle.util.SettlementUtil;
import com.rkylin.utils.RkylinMailUtil;

@Component("pABB2BDownload")
public class PABB2BDownload extends CheckfileDownload {
	protected static Logger logger = LoggerFactory.getLogger(PABB2BDownload.class);
	@Autowired
	private SettlementUtil settlementUtil;
	@Autowired
	private Properties ftpProperties;
	@Autowired
	private Properties fileProperties;
	@Autowired
	private SFTPUtil sftp;
	@Autowired
	private ParameterInfoManager parameterInfoManager;
	
	/**
     * 指定账期下载PABB2B支付对账文件
     * @param invoicedate 上游账期
     * @param rootInstId 机构号
     * @return
     */
    public Map<String, String> pABB2BFileDown(String invoicedate, String merchantCode) {
    	logger.info(">>> >>> >>> >>> 下载 PABB2B对账文件 ["+ invoicedate +"]账期 & ["+ merchantCode +"]协议 ————————————START————————————");
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
    	/**
    	 * 获取账期
    	 */
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
    	logger.info(">>> >>> 【指定账期下载PABB2B支付对账文件】  取得的账期为 :"+ accountDate);
    	/**
    	 * 开始下载上游对账文件
    	 */
    	String filename = null;
    	String p2pfilePath = null;
    	String p2pfileBakPath = null;
    	
		//上游渠道对账文件取得方式待定
		filename = accountDate + "_PAB_BankPayment_B2B_" + merchantCode + ".txt";
    	p2pfilePath = SettleConstants.FILE_PATH +accountDate2 + File.separator;
    	p2pfileBakPath = SettleConstants.FILE_PATH +accountDate2 + File.separator + "PAB_B2B_bak" + File.separator;
    	if (!new File(p2pfilePath).exists()) new File(p2pfilePath).mkdirs();
    	if (!new File(p2pfileBakPath).exists()) new File(p2pfileBakPath).mkdirs();
    	String srcFileName = null;
    	String[] srcFileNameArr = new String[3];
    	srcFileNameArr[0] = fileProperties.getProperty(merchantCode + "_CRJ").replace("{YMD}", accountDate);//出入金
    	srcFileNameArr[1] = fileProperties.getProperty(merchantCode + "_TRN").replace("{YMD}", accountDate);//会员交易
    	srcFileNameArr[2] = fileProperties.getProperty(merchantCode + "_RCG").replace("{YMD}", accountDate);//在途重置文件
    	
		for(int i = 0; i < srcFileNameArr.length; i ++) {
			srcFileName = srcFileNameArr[i];
			try {
				this.fileDownFromSFtp(srcFileName, p2pfileBakPath + srcFileName);
			} catch(Exception e) {
				logger.error("下载PABB2B["+ srcFileName +"]对账文件异常！" + e.getMessage());
	    	}
		}
    	
    	try {
    		boolean isSuccess = this.editNewPABB2BCollateFile(p2pfileBakPath, p2pfilePath, srcFileNameArr, filename);
    		if(!isSuccess) {
    			logger.error(">>> >>> >>> 编辑 PABB2B支付对账文件  执行失败!");
    		}
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 编辑 PABB2B支付对账文件 执行失败!" + e.getMessage());
			e.printStackTrace();
		}
    	logger.info("<<< <<< <<< <<< 下载PABB2B对账文件 ————————————END————————————");
    	return rtnMap;
    }
    /**
     * 
     * @param p2pfileBakPath
     * @param p2pfilePath
     * @param filename
     * @param fileType
     * @return
     */
    private boolean editNewPABB2BCollateFile(String srcDirectory, String tarDirectory, String[] srcFileArr, String tarFile) {
    	//写入文件是时需要的配置映射结构体
    	Map<String, Object> configMap = new HashMap<String, Object>();
    	//新对账文件名称
    	String newFilename;
    	List<Map<String, String>> allList = new ArrayList<Map<String, String>>();
    	//出入金
    	String crjFileName = srcDirectory + srcFileArr[0];
    	//会员交易
    	String trnFileName = srcDirectory + srcFileArr[1];
    	//在途充值
    	String rcgFileName = srcDirectory + srcFileArr[2];
    	//读取文件并获取文件信息结构体
    	List<Map<String, String>> crjList = analyzeOriCollateFile(crjFileName, 1, 0, SettleConstants.DEDT_SPLIT6, Constants.CHARSET_GBK);
    	List<Map<String, String>> trnList = analyzeOriCollateFile(trnFileName, 1, 0, SettleConstants.DEDT_SPLIT6, Constants.CHARSET_GBK);
    	List<Map<String, String>> rcgList = analyzeOriCollateFile(rcgFileName, 1, 0, SettleConstants.DEDT_SPLIT6, Constants.CHARSET_GBK);
    	//编辑文件信息结构体
    	crjList = editCrjFile(crjList);
    	trnList = editTrnFile(trnList);
    	rcgList = editRcgFile(rcgList);
    	allList.addAll(crjList);
    	allList.addAll(trnList);
    	allList.addAll(rcgList);
    	
    	//获取新对账文件名称 并 存入结构体
		newFilename = tarDirectory + tarFile;
		configMap.put("FILE", newFilename); 
    	try {
			TxtWriter.WriteTxt(allList, configMap, SettleConstants.DEDT_SPLIT6, Constants.CHARSET_GBK);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(">>> >>> >>> 异常:", e);
			return false;
		}
    	return true;
    }
    /**
     * 编辑出入金对账文件
     * @param crjFile
     * @return
     */
    private List<Map<String, String>> editCrjFile(List<Map<String, String>> crjList) {
    	List<Map<String, String>> newList = new ArrayList<Map<String, String>>();
    	Map<String, String> newRowMap;
    	String funcCodeT;
    	for(Map<String, String> rowMap : crjList) {
    		newRowMap = new HashMap<String, String>();
    		newRowMap.put("F_1", rowMap.get("L_11") == null ? "" : rowMap.get("L_11"));
    		newRowMap.put("F_2", rowMap.get("L_10") == null ? "" : rowMap.get("L_10"));
    		newRowMap.put("F_3", rowMap.get("L_8") + rowMap.get("L_9"));
    		newRowMap.put("F_4", super.getAmountStrF(rowMap.get("L_4") == null ? "0" : rowMap.get("L_4")));
    		newRowMap.put("F_5", super.getAmountStrF(rowMap.get("L_5") == null ? "0" : rowMap.get("L_5")));
    		funcCodeT = rowMap.get("L_0");
    		if("1".equals(funcCodeT)) {
    			funcCodeT = "4014";
    		} else if("2".equals(funcCodeT)) {
    			funcCodeT = "4013";
    		} else if("3".equals(funcCodeT)) {
    			funcCodeT = "bill";
    		}
    		newRowMap.put("F_6", funcCodeT);
    		newRowMap.put("F_7", "出入金CRJ");
    		newRowMap.put("F_8", "16");
    		newList.add(newRowMap);
    	}
    	return newList;
    }
    /**
     * 编辑会员交易对账文件
     * @param trnFile
     * @return
     */
    private List<Map<String, String>> editTrnFile(List<Map<String, String>> trnList) {
    	List<Map<String, String>> newList = new ArrayList<Map<String, String>>();
    	Map<String, String> newRowMap;
    	String funcCodeT = "4014";
    	for(Map<String, String> rowMap : trnList) {
    		newRowMap = new HashMap<String, String>();
    		newRowMap.put("F_1", rowMap.get("L_0") == null ? "" : rowMap.get("L_0"));
    		newRowMap.put("F_2", rowMap.get("L_1") == null ? "" : rowMap.get("L_1"));
    		newRowMap.put("F_3", rowMap.get("L_10") + rowMap.get("L_11"));
    		newRowMap.put("F_4", super.getAmountStrF(rowMap.get("L_3") == null ? "0" : rowMap.get("L_3")));
    		newRowMap.put("F_5", "0");
    		newRowMap.put("F_6", funcCodeT);
    		newRowMap.put("F_7", "会员交易TRN");
    		newRowMap.put("F_8", "16");
    		newList.add(newRowMap);
    	}
    	return newList;
    }
    /**
     * 编辑在途充值对账文件
     * @param rcgFile
     * @return
     */
    private List<Map<String, String>> editRcgFile(List<Map<String, String>> rcgList) {
    	List<Map<String, String>> newList = new ArrayList<Map<String, String>>();
    	Map<String, String> newRowMap;
    	String funcCodeT = "4014";
    	for(Map<String, String> rowMap : rcgList) {
    		newRowMap = new HashMap<String, String>();
    		newRowMap.put("F_1", rowMap.get("L_8") == null ? "" : rowMap.get("L_8"));
    		newRowMap.put("F_2", rowMap.get("L_7") == null ? "" : rowMap.get("L_7"));
    		newRowMap.put("F_3", rowMap.get("L_5") + rowMap.get("L_6"));
    		newRowMap.put("F_4", super.getAmountStrF(rowMap.get("L_3") == null ? "0" : rowMap.get("L_3")));
    		newRowMap.put("F_5", "0");
    		newRowMap.put("F_6", funcCodeT);
    		newRowMap.put("F_7", "在途充值RCG");
    		newRowMap.put("F_8", "16");
    		newList.add(newRowMap);
    	}
    	return newList;
    }
    /**
     * 从融数SFTP服务器下载对账文件
     * @param srcFileName
     * @param tarFileName
     * @throws Exception
     */
    private void fileDownFromSFtp(String srcFileName, String tarFileName) throws Exception {
    	logger.info(">>> >>> >>> >>> 开始 从融数SFTP服务器下载对账文件");
    	String thePrefixStr = "MULTI_SFTP_";
    	String host = ftpProperties.getProperty(thePrefixStr + "HOST");
    	String port = ftpProperties.getProperty(thePrefixStr + "PORT");
    	String username = ftpProperties.getProperty(thePrefixStr + "NAME");
    	String password = ftpProperties.getProperty(thePrefixStr + "PASS");
    	String directory = ftpProperties.getProperty(thePrefixStr + "DIRECTORY");
    	sftp.setHost(host);
		sftp.setPort(Integer.parseInt(port));
		sftp.setUsername(username);
		sftp.setPassword(password);
		sftp.connect();
		try {
			sftp.download(directory, srcFileName, tarFileName);
			Thread.sleep(2000);
		} catch (FileNotFoundException e) {
			logger.error("异常: FileNotFoundException", e);
			e.printStackTrace();
		} catch (SftpException e) {
			logger.error("异常: SftpException", e);
			e.printStackTrace();
		} finally {
			/*
    		 * 关闭下载文件资源链接
    		 */
    		sftp.disconnect();
		}
		logger.info("<<< <<< <<< <<< 结束 从融数SFTP服务器下载对账文件");
    }
}
