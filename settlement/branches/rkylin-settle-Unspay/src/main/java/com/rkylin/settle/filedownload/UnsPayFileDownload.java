package com.rkylin.settle.filedownload;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rkylin.settle.constant.SettleConstants;
import com.rkylin.settle.manager.ParameterInfoManager;
import com.rkylin.settle.util.ArithUtil;
import com.rkylin.settle.util.FtpDownload;
import com.rkylin.settle.util.ReadExcelUtils;
import com.rkylin.settle.util.SettlementUtil;
import com.rkylin.settle.util.TxtWriter;

@Component("unsPayDownload")
public class UnsPayFileDownload extends CheckfileDownload {
	@Autowired
	ParameterInfoManager parameterInfoManager;		//配置表Manager
	@Autowired
	SettlementUtil settlementUtil;					//工具类
	@Autowired
    Properties ftpProperties;						//ftp服务器信息
	@Autowired
    Properties fileProperties;						//文件信息
	
	public Map<String, String> fileDown(String invoicedate, String merchantCode) throws Exception {
		logger.info(">>> >>> >>> >>> 开始: 下载  银生宝文件 多渠道FTP ["+ merchantCode +"]协议 , ["+ invoicedate +"]账期");
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
	    	logger.error("异常:下载  银生宝文件 多渠道FTP", e2);
			rtnMap.put("errCode", "0001");
			rtnMap.put("errMsg", "计算账期异常！");
			return rtnMap;
	    }
    	logger.info(">>> >>> >>> 下载  银生宝文件 多渠道FTP 取得的账期为 : "+ accountDate);
    	
    	
    	//连接融数金服FTP
    	 
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
    		logger.error("异常:下载  银生宝文件 多渠道FTP", e);
			rtnMap.put("errCode", "0001");
			rtnMap.put("errMsg", "异常:下载  银生宝文件 多渠道FTP");
			e.printStackTrace();
			return rtnMap;
    	}    	
    	
    	//从FTP上下载文件
    	 
    	localPath = SettleConstants.FILE_PATH +accountDate2 + File.separator + "UnsPay_bak" + File.separator;
    	String fileName = fileProperties.getProperty("UNSPAY_DS_" + merchantCode).replace("{YMD}", accountDate);
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
    	
    	//关闭FTP连接
    	 
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
    	 * 编辑银生宝文件
    	 */
    	List<Map> fileList = null;
    	try {
    		fileList = this.editUnsPayFile(new File(localPath + fileName),accountDate,merchantCode);
    		System.out.println();
		} catch (Exception e) {
			logger.error("异常:编辑银生宝文件", e);
			e.printStackTrace();
			rtnMap.put("errCode", "0001");
			rtnMap.put("errMsg", "编辑银生宝文件");
			return rtnMap;
		}
    	/*
    	 * 上传编辑后的银生宝文件
    	 */
    	try {
    		this.createFile(accountDate, accountDate2, merchantCode, fileList);
		} catch (Exception e) {
			logger.error("异常:上传编辑后的银生宝文件", e);
			e.printStackTrace();
			rtnMap.put("errCode", "0001");
			rtnMap.put("errMsg", "上传编辑后的银生宝文件");
			return rtnMap;
		}
		logger.info("<<< <<< <<< <<< 结束: 下载  银生宝文件 多渠道FTP ["+ merchantCode +"]协议 , ["+ invoicedate +"]账期");
		return rtnMap;
	}
	
	/**
	 * 编辑银生宝文件
	 * @param list
	 */
	private List<Map> editUnsPayFile(File file,String accountDate, String merchantCode) throws Exception {
		logger.info(">>> >>> >>> >>> 开始:编辑银生宝文件"+file.getPath());
		List<Map> fileList = null;
		Map fileMap = null;
		Map<Integer, Map<Integer, Object>> contentMap = null;
		ReadExcelUtils excelReader = new ReadExcelUtils(file.getAbsolutePath());
		if (!file.exists()) return fileList;
		try {
			logger.info("读取文件.....");
			contentMap = excelReader.readExcelContent();
			if (contentMap == null || contentMap.size() == 0) {
				logger.error(">>> >>> 对账文件读取异常！fileMap is Null or size is 0");
				return fileList;
			}
			
			fileList = new ArrayList<Map>();
			logger.info("已获得Excel表格内容，接下来转换...");
			//从1开始
			for (int i = 1; i <= contentMap.size(); i++) {
				fileMap = new HashMap();
				//1.交易时间	REQUEST_TIME 交易请求时间
				fileMap.put("F_1",contentMap.get(i).get(0));
				//2.订单号	ORDER_NO 订单号
				fileMap.put("F_2",contentMap.get(i).get(1));
				//3.支付流水号	TRANS_FLOW_NO 交易流水号
				fileMap.put("F_3",contentMap.get(i).get(2));
				//4.交易类型	TRANS_TYPE 交易类型
				fileMap.put("F_4","4013");
				//5.交易渠道	PAY_CHANNEL_ID 支付渠道ID
				fileMap.put("F_5","S06");
				//6.交易状态	READ_STATUS_ID  16成功  14失败
				fileMap.put("F_6","16");
				//7.交易金额（元）	TRANS_AMOUNT 交易金额
				ArithUtil au = new ArithUtil();
				fileMap.put("F_7",super.getAmountStrF(contentMap.get(i).get(4) == null ? "0" : contentMap.get(i).get(4).toString().trim()));
				//8.手续费（元）	FEE_AMOUNT 手续费金额
				fileMap.put("F_8",super.getAmountStrF(contentMap.get(i).get(5) == null ? "0" : contentMap.get(i).get(5).toString().trim()));
				//9.实际金额（元）	SETTLE_AMOUNT 清算金额
				fileMap.put("F_9","0");
				//10.对账类型	READ_TYPE
				fileMap.put("F_10","02");
				//11.账期		SETTLE_TIME
				fileMap.put("F_11",accountDate);
				//12.机构号		MERCHANT_CODE
				fileMap.put("F_12",merchantCode);
				//13.交易		IS_INVOICE
				fileMap.put("F_13","0");
				fileList.add(fileMap);
			}
		} catch(Exception e) {
			logger.error(">>> >>>对账文件读取异常！" + e.getMessage());
			return fileList;
		}
		logger.info("<<< <<< <<< <<< 结束:编辑银生宝文件");
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
		String localfilennew = SettleConstants.FILE_PATH +accountDate2 + File.separator + accountDate +"_UNSPAY_" + merchantCode + ".txt";
		logger.info(localfilennew);
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
