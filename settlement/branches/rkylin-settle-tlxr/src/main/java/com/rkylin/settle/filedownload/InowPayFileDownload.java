package com.rkylin.settle.filedownload;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.espertech.esper.client.ConfigurationEngineDefaults.Logging;
import com.rkylin.settle.constant.SettleConstants;
import com.rkylin.settle.manager.ParameterInfoManager;
import com.rkylin.settle.util.ArithUtil;
import com.rkylin.settle.util.FtpDownload;
import com.rkylin.settle.util.SettlementUtil;
import com.rkylin.settle.util.TxtReader;
import com.rkylin.settle.util.TxtWriter;

@Component("inowPayDownload")
public class InowPayFileDownload extends CheckfileDownload {
	@Autowired
	ParameterInfoManager parameterInfoManager;		//配置表Manager
	@Autowired
	SettlementUtil settlementUtil;					//工具类
	@Autowired
    Properties ftpProperties;						//ftp服务器信息
	@Autowired
    Properties fileProperties;						//文件信息
	
	public Map<String, String> fileDown(String invoicedate, String merchantCode) throws Exception {
		logger.info(">>> >>> >>> >>> 开始: 下载  现在支付文件 多渠道FTP ["+ merchantCode +"]协议 , ["+ invoicedate +"]账期");
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
	    	logger.error("异常:下载  现在支付文件 多渠道FTP", e2);
			rtnMap.put("errCode", "0001");
			rtnMap.put("errMsg", "计算账期异常！");
			return rtnMap;
	    }
    	logger.info(">>> >>> >>> 下载  现在支付文件 多渠道FTP 取得的账期为 : "+ accountDate);
    	
    	
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
    		logger.error("异常:下载  现在支付文件 多渠道FTP", e);
			rtnMap.put("errCode", "0001");
			rtnMap.put("errMsg", "异常:下载  现在支付文件 多渠道FTP");
			e.printStackTrace();
			return rtnMap;
    	}    	
    	
    	//从FTP上下载文件
    	 
    	localPath = SettleConstants.FILE_PATH +accountDate2 + File.separator + "INOWPAY_bak" + File.separator;
    	String fileName = fileProperties.getProperty("INOWPAY_DF_" + merchantCode).replace("{YMD}", accountDate);
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
    	 * 编辑现在支付文件
    	 */
    	List<Map> fileList = null;
    	try {
    		fileList = this.editInowPayFile(new File(localPath + fileName),accountDate,merchantCode);
		} catch (Exception e) {
			logger.error("异常:编辑现在支付文件", e);
			e.printStackTrace();
			rtnMap.put("errCode", "0001");
			rtnMap.put("errMsg", "编辑现在支付文件");
			return rtnMap;
		}
    	/*
    	 * 上传编辑后的现在支付文件
    	 */
    	try {
    		this.createFile(accountDate, accountDate2, merchantCode, fileList);
		} catch (Exception e) {
			logger.error("异常:上传编辑后的现在支付文件", e);
			e.printStackTrace();
			rtnMap.put("errCode", "0001");
			rtnMap.put("errMsg", "上传编辑后的现在支付文件");
			return rtnMap;
		}
		logger.info("<<< <<< <<< <<< 结束: 下载  现在支付文件 多渠道FTP ["+ merchantCode +"]协议 , ["+ invoicedate +"]账期");
		return rtnMap;
	}
	
	/**
	 * 编辑现在支付文件
	 * @param list
	 */
	private List<Map> editInowPayFile(File file,String invoicedate,String merchantCode) throws Exception {
		
		logger.info(">>> >>> >>> >>> 开始:编辑现在支付文件"+file.getPath());
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
			File tempFile = createTempFile(file);
			txtReader.setEncode("UTF-8");
			fileListSub = txtReader.txtreader(tempFile , SettleConstants.DEDT_SPLIT2);
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
			
			fileMap = new HashMap();
			fileMapSub = fileListSub.get(j);
			//1.交易时间	REQUEST_TIME 交易请求时间
			fileMap.put("F_1",sdf1.format(sdf.parse(String.valueOf(fileMapSub.get("L_0")))));
			//2.订单号	ORDER_NO 订单号
			fileMap.put("F_2",fileMapSub.get("L_1").toString().trim());
			//3.支付流水号	TRANS_FLOW_NO 交易流水号
			fileMap.put("F_3",fileMapSub.get("L_2").toString().trim());
			//4.交易类型	TRANS_TYPE 交易类型
			fileMap.put("F_4","4014");
			//5.交易渠道	PAY_CHANNEL_ID 支付渠道ID
			fileMap.put("F_5","S05");
			//6.交易状态	READ_STATUS_ID  16成功  14失败
			String readStatusId = "成功".equals(fileMapSub.get("L_8").toString().trim())?"16":"14";
			fileMap.put("F_6",readStatusId);
			//7.交易金额（元）	TRANS_AMOUNT 交易金额
			ArithUtil au = new ArithUtil();
			fileMap.put("F_7",super.getAmountStrF(fileMapSub.get("L_12") == null ? "0" : fileMapSub.get("L_12").toString().trim()));
			//8.手续费（元）	FEE_AMOUNT 手续费金额
			fileMap.put("F_8",super.getAmountStrF(fileMapSub.get("L_13") == null ? "0" : fileMapSub.get("L_13").toString().trim()));
			//9.实际金额（元）	SETTLE_AMOUNT 清算金额
			fileMap.put("F_9",super.getAmountStrF(fileMapSub.get("L_14") == null ? "0" : fileMapSub.get("L_14").toString().trim()));
			//10.对账类型	READ_TYPE
			fileMap.put("F_10","02");
			//11.账期		SETTLE_TIME
			fileMap.put("F_11",invoicedate);
			//12.机构号		MERCHANT_CODE
			fileMap.put("F_12",merchantCode);
			//13.交易		IS_INVOICE
			fileMap.put("F_13","0");
			fileList.add(fileMap);
		}
		logger.info("<<< <<< <<< <<< 结束:编辑现在支付文件");
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
		String localfilennew = SettleConstants.FILE_PATH +accountDate2 + File.separator + accountDate +"_INOWPAY_" + merchantCode + ".txt";
		configMap.put("FILE", localfilennew);
		try {
			TxtWriter.WriteTxt(fileList, configMap, SettleConstants.DEDT_SPLIT, "UTF-8");
		} catch(Exception e) {
			logger.error(">>> >>> 对账文件写入异常！" + e.getMessage(), e);
			return;
		}
		logger.info("<<< <<< <<< 结束: 创建清结算上游对账文件");
	}
	
	/**
	 * 创建临时文件，截取csv文件数据
	 * @param file
	 * @return
	 */
	public  File createTempFile(File file){
		logger.info(">>> >>> >>> 开始: 创建csv临时文件createTempFile");
		File tempFile = null;
		OutputStreamWriter out = null;
		BufferedReader reader = null;
		try{
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "GBK"));// 换成你的文件名
			String line = null;
			StringBuffer buffer  =new StringBuffer();
			boolean isWirte = false;//是否开始读取
			while ((line = reader.readLine()) != null) {
				if(line.indexOf("现在支付交易明细表")>-1){
					isWirte = true;
				}
				if(isWirte){
					buffer.append(line+"\n");
				}
			}
			tempFile = new File(file.getPath().replaceAll(file.getName(), "")+"temp-"+file.getName());
			if(!tempFile.exists()){
				tempFile.createNewFile();
			}
			logger.info(buffer.toString());
			out = new OutputStreamWriter(new FileOutputStream(tempFile),"UTF-8");
			out.write(buffer.toString());
		}catch(Exception e){
			logger.error(">>> >>> csv临时文件写入异常！" + e.getMessage(), e);
		}finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
				logger.error("BufferedReader关闭异常");
			}
			try {
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				logger.error("FileWriter关闭异常");
			}
		}
		logger.info("<<< <<< <<< 结束: 创建csv临时文件createTempFile");
		return tempFile;
	}
}
