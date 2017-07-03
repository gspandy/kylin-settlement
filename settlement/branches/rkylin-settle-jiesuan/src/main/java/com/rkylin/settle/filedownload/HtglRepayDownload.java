package com.rkylin.settle.filedownload;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rkylin.settle.constant.SettleConstants;
import com.rkylin.settle.manager.ParameterInfoManager;
import com.rkylin.settle.util.FtpDownload;
import com.rkylin.settle.util.SettlementUtil;
import com.rkylin.settle.util.TxtReader;
import com.rkylin.settle.util.TxtWriter;

/**
 * 会唐国旅 贷款还款业务对账文件下载
 * @author CaoYang
 */
@Component("htglRepayDownload")
public class HtglRepayDownload extends CheckfileDownload {
	@Autowired
	ParameterInfoManager parameterInfoManager;		//配置表Manager
	@Autowired
	SettlementUtil settlementUtil;					//工具类
	@Autowired
    Properties ftpProperties;						//ftp服务器信息
	@Autowired
    Properties fileProperties;						//文件信息
	/**
	 * 下载会唐国旅 还款对账文件
	 * @param invoicedate	账期yyyy-MM-dd
	 * @param merchantCode	机构号
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> fileDown(String invoicedate, String merchantCode) throws Exception {
		logger.info(">>> >>> >>> >>> 开始: 下载  会唐国旅还款文件 多渠道FTP ["+ merchantCode +"]协议 , ["+ invoicedate +"]账期");
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
	    		accountDate = String.valueOf(settlementUtil.getAccountDate(invoicedate, "yyyyMMdd", -1, "String"));
				accountDate2 = String.valueOf(settlementUtil.getAccountDate(invoicedate, "yyyyMMdd", 0, "String"));
	    	} else {
	    		accountDate = invoicedate.replace("-", "");
	    		accountDate2 = String.valueOf(settlementUtil.getAccountDate(invoicedate, "yyyyMMdd", 1, "String"));
	    	}
	    } catch (Exception e2) {
	    	logger.error("异常:下载  会唐国旅还款文件 多渠道FTP", e2);
			rtnMap.put("errCode", "0001");
			rtnMap.put("errMsg", "计算账期异常！");
			return rtnMap;
	    }
    	logger.info(">>> >>> >>> 下载  会唐国旅还款文件 多渠道FTP 取得的账期为 : "+ accountDate);
    	
    	/*
    	 * 连接融数金服FTP
    	 */
    	String localPath = null;//对账文件备份目录
    	FtpDownload ftpDownload = new FtpDownload();//ftp下载工具类
    	boolean flg = false;//执行结果
    	logger.info(">>> >>> >>> 连接融数金服FTP");
    	try {
    		String addr = ftpProperties.getProperty("MULTIFTP_URL");					//ftp地址
    		int port = Integer.parseInt(ftpProperties.getProperty("MULTIFTP_PORT"));	//端口号
    		String username = ftpProperties.getProperty("MULTIFTP_NAME");				//用户名
    		String password = ftpProperties.getProperty("MULTIFTP_PASS");				//密码
    		flg = ftpDownload.connect(addr, port, username, password);					//链接
    		if (!flg) {//失败重新链接
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
    		logger.error("异常:下载  会唐国旅还款文件 多渠道FTP", e);
			rtnMap.put("errCode", "0001");
			rtnMap.put("errMsg", "异常:下载  会唐国旅还款文件 多渠道FTP");
			e.printStackTrace();
			return rtnMap;
    	}    	
    	/*
    	 * 从FTP上下载文件
    	 */
    	localPath = SettleConstants.FILE_PATH +accountDate2 + File.separator + "HT_REPAYMENT_bak" + File.separator;
    	String fileName = fileProperties.getProperty("HT_REPAYMENT_" + merchantCode).replace("{YMD}", accountDate);
    	logger.info(">>> >>> >>> 从融数金服FTP 下载文件");
    	try {
    		flg = ftpDownload.download(accountDate, fileName, localPath, "");
        	logger.info(">>> >>> >>> FTP文件下载" + flg + ", 文件:" + fileName);
    	} catch(Exception e) {
    		logger.error(">>> >>> >>> 异常:FTP文件下载, 文件:" + fileName, e);
    		e.printStackTrace();
    		rtnMap.put("errCode", "0001");
			rtnMap.put("errMsg", "异常:FTP文件下载");
			return rtnMap;
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
			e.printStackTrace();
			rtnMap.put("errCode", "0001");
			rtnMap.put("errMsg", "异常:关闭FTP连接");
			return rtnMap;
    	}
    	/*
    	 * 编辑会唐国旅还款文件
    	 */
    	List<Map> fileList = null;
    	try {
    		fileList = this.editHtRepayFile(new File(localPath + fileName));
		} catch (Exception e) {
			logger.error("异常:编辑会唐国旅还款文件", e);
			e.printStackTrace();
			rtnMap.put("errCode", "0001");
			rtnMap.put("errMsg", "编辑会唐国旅还款文件");
			return rtnMap;
		}
    	/*
    	 * 上传编辑后的会唐国旅还款文件
    	 */
    	try {
    		this.createFile(accountDate, accountDate2, merchantCode, fileList);
		} catch (Exception e) {
			logger.error("异常:上传编辑后的会唐国旅还款文件", e);
			e.printStackTrace();
			rtnMap.put("errCode", "0001");
			rtnMap.put("errMsg", "上传编辑后的会唐国旅还款文件");
			return rtnMap;
		}
		logger.info("<<< <<< <<< <<< 结束: 下载  会唐国旅还款文件 多渠道FTP ["+ merchantCode +"]协议 , ["+ invoicedate +"]账期");
		return rtnMap;
	}
	
	/**
	 * 编辑会唐国旅还款文件
	 * @param list
	 */
	private List<Map> editHtRepayFile(File file) throws Exception {
		logger.info(">>> >>> >>> >>> 开始:编辑会唐国旅还款文件");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmss");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
		List<Map> fileList = null;
		TxtReader txtReader = new TxtReader();
		TxtWriter txtWriter = new TxtWriter();
		List<Map> headList = new ArrayList<Map>();
		List<Map> tailList = new ArrayList<Map>();
		Map fileMap;
		List<Map> fileListSub;
		Map fileMapSub;
		Map<String,Map> keyMap;
		if (!file.exists()) return fileList;
		try {
			txtReader.setEncode("UTF-8");
			fileListSub = txtReader.txtreader(file , SettleConstants.DEDT_SPLIT);
			if (fileListSub == null || fileListSub.size() == 0) {
				logger.error(">>> >>> 对账文件读取异常！fileListSub is Null or size is 0");
				return fileList;
			}
		} catch(Exception e) {
			logger.error(">>> >>>对账文件读取异常！" + e.getMessage());
			return fileList;
		}
		fileList = new ArrayList<Map>();
		fileMapSub = new HashMap();
		keyMap = new HashMap<String,Map>();
		for (int j = 2; j < fileListSub.size(); j ++) {
//			1.交易时间
//			2.交易号
//			3.金额
//			4.我方账号
//			5.我方账户名称
//			6.对方账号
//			7.对方账户名称
//			8.银行流水号
//			9.交易类型
//			10.摘要
//			11.入库时间
			fileMap = new HashMap();
			fileMapSub = fileListSub.get(j);
			fileMap.put("F_1",sdf1.format(sdf.parse(String.valueOf(fileMapSub.get("L_0")))));
			fileMap.put("F_2",fileMapSub.get("L_1"));
			fileMap.put("F_3",fileMapSub.get("L_2"));
			fileMap.put("F_4",fileMapSub.get("L_3"));
			fileMap.put("F_5",fileMapSub.get("L_4"));
			fileMap.put("F_6",fileMapSub.get("L_5"));
			fileMap.put("F_7",fileMapSub.get("L_6"));
			fileMap.put("F_8",fileMapSub.get("L_7"));
			fileMap.put("F_9",fileMapSub.get("L_8"));
			fileMap.put("F_10",fileMapSub.get("L_9"));
			fileMap.put("F_11",sdf2.format(sdf.parse(String.valueOf(fileMapSub.get("L_0")))));
			fileMap.put("F_12",0);			//手续费 文件中无手续费信息
			fileMap.put("F_13","4015");		//交易类型
			fileMap.put("F_14",1);			//交易状态
			fileList.add(fileMap);
		}
		logger.info("<<< <<< <<< <<< 结束:编辑会唐国旅还款文件");
    	return fileList;
	}
	/**
	 * 创建清结算上游对账文件
	 * @param accountDate
	 * @param accountDate2
	 * @param merchantCode
	 */
	private void createFile(String accountDate, String accountDate2, String merchantCode, List<Map> fileList) {
		logger.info(">>> >>> >>> 开始: 创建清结算上游对账文件");
		Map configMap = new HashMap();
		String localfilennew = SettleConstants.FILE_PATH +accountDate2 + File.separator + accountDate +"_POS_HT_" + merchantCode + ".txt";
		configMap.put("FILE", localfilennew);
		try {
			TxtWriter.WriteTxt(fileList, configMap, SettleConstants.DEDT_SPLIT, "UTF-8");
		} catch(Exception e) {
			logger.error(">>> >>> 对账文件写入异常！" + e.getMessage(), e);
			return;
		}
		logger.info("<<< <<< <<< 结束: 创建清结算上游对账文件");
	}
}
