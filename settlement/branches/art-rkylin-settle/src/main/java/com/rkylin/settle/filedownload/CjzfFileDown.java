package com.rkylin.settle.filedownload;

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipException;

import javax.servlet.http.HttpServletRequest;

import net.lingala.zip4j.core.ZipFile;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rkylin.settle.util.TxtReader;
import com.rkylin.settle.util.TxtWriter;
import com.rkylin.settle.constant.Constants;
import com.rkylin.settle.constant.SettleConstants;
import com.rkylin.settle.manager.ParameterInfoManager;
import com.rkylin.settle.manager.SettleBalanceEntryManager;
import com.rkylin.settle.manager.SettleBatchManageManager;
import com.rkylin.settle.manager.SettlementManager;
import com.rkylin.settle.pojo.ParameterInfo;
import com.rkylin.settle.pojo.ParameterInfoQuery;
import com.rkylin.settle.util.PKCS7Util;
import com.rkylin.settle.util.SettlementUtil;
import com.rkylin.utils.RkylinMailUtil;

@Component("cjzfFileDown")
public class CjzfFileDown extends CheckfileDownload {
	@Autowired
	HttpServletRequest request;
	@Autowired
	ParameterInfoManager parameterInfoManager;
	@Autowired
	SettlementManager settlementManager;
	@Autowired
	SettlementUtil settlementUtil;
	@Autowired
	SettleBatchManageManager settleBatchManageManager;
	@Autowired
	SettleBalanceEntryManager settleBalanceEntryManager;
	/***
	 * 下载畅捷支付对账文件
	 * @param 当前账期
	 * @param 融数机构号
	 * @param 代收付
	 * @return
	 */
	public Map<String, String> cjFileDown() throws Exception {
		return this.cjFileDown(SettleConstants.ACCOUNT_COED_GENERATE);
	}
	/***
	 * 下载畅捷支付对账文件
	 * @param 当前账期
	 * @param 融数机构号
	 * @param fileType
	 * @return
	 */
	public Map<String, String> cjFileDown(String fileType) throws Exception {
		return this.cjFileDown(Constants.RS_ID, fileType);
	}
	/***
	 * 下载畅捷支付对账文件
	 * @param 当前账期
	 * @param merchantCode
	 * @param fileType
	 * @return
	 */
	public Map<String, String> cjFileDown(String merchantCode, String fileType) throws Exception {
		return this.cjFileDown(null, merchantCode, fileType);
	}
	/***
	 * 下载畅捷支付对账文件
	 * @param invoicedate
	 * @param merchantCode
	 * @param fileType
	 * @return
	 */
	public Map<String, String> cjFileDown(String invoicedate, String merchantCode, String fileType) throws Exception {
		logger.info("下载 【畅捷支付】 ["+ merchantCode +"]协议 & ["+ fileType +"]交易类型 对账文件 ————————————START————————————");
		Map<String, String> rtnMap = new HashMap<String, String>();
		rtnMap.put("errCode", "0000");
		rtnMap.put("errMsg", "成功");
		/**
    	 * 判断日中是否正常结束
    	 */
//    	ParameterInfoQuery keyList =  new ParameterInfoQuery();
//    	keyList.setParameterCode(SettleConstants.DAYEND);
//    	List<ParameterInfo> parameterInfo = parameterInfoManager.queryList(keyList);
		if (!settlementUtil.cutDayIsSuccess4Account()) {
    		rtnMap.put("errCode", "0001");
    		rtnMap.put("errMsg", "日终没有正常结束！");
    		RkylinMailUtil.sendMailThread("清洁算开始异常","日终没有正常结束，不能开始清洁算操作", "21401233@qq.com");
    		return rtnMap;
    	}
    	/**
    	 * 获取账期
    	 */
    	String accountDate = "";					//T-1日账期
    	String accountDate2 = "";					//T日账期
    	try {
	    	if (invoicedate == null || "".equals(invoicedate)) {//如果未传入账期看
	    		//从账户DB中过去当前
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
    	logger.info(">>> >>> 【指定账期下载畅捷支付支付对账文件】  取得的账期为 :"+ accountDate);
    	/**
    	 * 下载文件
    	 */
    	//zip文件名称
    	String zipFileName = "CJZF_" + accountDate+"_" + merchantCode + ".zip";
    	//对账文件名称
    	String fileName = "CJZF_" + accountDate+"_" + merchantCode + ".txt";
    	//原始文件存储位置
    	String bakPath = SettleConstants.FILE_PATH +accountDate2 + File.separator + "cj_bak" + File.separator + merchantCode + File.separator;
    	//编辑后的文件存储位置
    	String newPath = SettleConstants.FILE_PATH +accountDate2 + File.separator;
    	//目录对象
    	File file = new File(bakPath);
    	if (!file.exists()) {
    		file.mkdirs();
    	}
		try {
			boolean fileflg = downloadFile(merchantCode, fileType, bakPath + zipFileName, accountDate);
			if(!fileflg) {
				logger.error("失败【下载畅捷支付支付对账文件】");
				rtnMap.put("errCode", "0001");
				rtnMap.put("errMsg", "失败【下载畅捷支付支付对账文件】");
				return rtnMap;
			}
		} catch (Exception e1) {
			logger.error("异常【下载畅捷支付支付对账文件】" + e1.getMessage());
			rtnMap.put("errCode", "0001");
			rtnMap.put("errMsg", "异常【下载畅捷支付支付对账文件】");
			e1.printStackTrace();
			return rtnMap;
		}
		/**
		 * 解压文件
		 */
		try {
			unZipFile(bakPath, zipFileName);
		} catch (Exception e) {
			logger.error("解压文件异常【下载畅捷支付支付对账文件zip文件解压失败】" + e.getMessage());
			rtnMap.put("errCode", "0001");
			rtnMap.put("errMsg", "解压文件异常【下载畅捷支付支付对账文件zip文件解压失败】");
			e.printStackTrace();
			return rtnMap;
		}
		try {
			/**
			 * 获取原始对账文件对象
			 */
			File foremostFile = filterTheFile(bakPath);
			editNewCollateFile(foremostFile, newPath, fileName);
			/**
			 * 对账文件
			 */
			//File theCollcateFile = new File(newPath, fileName);
			/**
			 * 解析&编辑原始对账文件
			 */
			//FileUtils.copyFile(foremostFile, theCollcateFile);
		} catch (Exception e) {
			logger.error("异常:复制结业后的文件到指定目录失败" + e.getMessage());
			rtnMap.put("errCode", "0001");
			rtnMap.put("errMsg", "异常:复制结业后的文件到指定目录失败");
			e.printStackTrace();
			return rtnMap;
		}
    	return rtnMap;
	}
	/***
	 * 配置下载文件所需的报文等信息并调用下载
	 * @param merchantCode
	 * @param fileType
	 * @param filePath
	 * @param dateStr
	 * @return
	 * @throws Exception
	 */
	private boolean downloadFile(String merchantCode, String fileType, String filePath ,String dateStr) throws Exception {
		logger.info(">>> >>> >>> 进入 downloadFile 方法");
    	String thePrefixStr = merchantCode + "_" + SettleConstants.PAY_CHANNEL_ID_CHANPAY + "_" + fileType + "_";
		SimpleDateFormat formatter3 = new SimpleDateFormat("yyyyMMddHHmmss");
    	String nwdate = formatter3.format(new Date());
    	StringBuffer xmlStrBuff1 = new StringBuffer();
    	StringBuffer xmlStrBuff2 = new StringBuffer();
    	String merchantId = fileProperties.getProperty(thePrefixStr + "MERCHANT_ID");
    	String pfx = fileProperties.getProperty(thePrefixStr + "PFX");
    	String psw = fileProperties.getProperty(thePrefixStr + "PSW");
    	String url = fileProperties.getProperty(thePrefixStr + "URL");
    	/*
    	 * 第一部分XML文件拼接
    	 * */
    	xmlStrBuff1.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><MESSAGE><INFO>");
    	xmlStrBuff1.append("<TRX_CODE>G40003</TRX_CODE>");
    	xmlStrBuff1.append("<VERSION>01</VERSION>");
    	xmlStrBuff1.append("<MERCHANT_ID>"+ merchantId +"</MERCHANT_ID>");
    	xmlStrBuff1.append("<REQ_SN>"+ merchantId + nwdate.substring(2) + "00001</REQ_SN>");
    	xmlStrBuff1.append("<TIMESTAMP>" + nwdate + "</TIMESTAMP>");
    	xmlStrBuff1.append("<SIGNED_MSG>");
    	/*
    	 * 第二部分XML文件拼接
    	 * */
    	xmlStrBuff2.append("</SIGNED_MSG></INFO>");
    	xmlStrBuff2.append("<BODY>");
    	xmlStrBuff2.append("<BILL_TYPE>00</BILL_TYPE>");
    	xmlStrBuff2.append("<BILL_DAY>"+ dateStr +"</BILL_DAY>");
    	xmlStrBuff2.append("</BODY>");
    	xmlStrBuff2.append("</MESSAGE>");
    	/*
    	 * 获取两部分XML的String
    	 */
    	String xml_1 = xmlStrBuff1.toString();
    	String xml_2 = xmlStrBuff2.toString();
		PKCS7Util pkcs7util = new PKCS7Util();
		String certpath = SettleConstants.TOMCAT_ROOT_PATH;
		String signeddata = pkcs7util.getSignedData(
				(xml_1 + xml_2).getBytes(), 
				certpath + pfx, 
				psw, 
				""
		);
		//拼接两部分XML
		String body = xml_1 +  signeddata + xml_2;
		httpRequest(url, "UTF-8", body, filePath);
		return true;
	}
	/**
	 * 编辑畅捷支付上游对账文件
	 * @param foremostFile
	 * @param newPath
	 * @param fileName
	 */
	public void editNewCollateFile(File foremostFile,String newPath,String fileName) {
		TxtReader txtReader = new TxtReader();
		TxtWriter txtWriter = new TxtWriter();
		List<Map> fileList = new ArrayList<Map>();
		List<Map> headList = new ArrayList<Map>();
		List<Map> tailList = new ArrayList<Map>();
		List<Map> fileListSub;
		Map fileMapSub;
		Map fileMap;
		if (!foremostFile.exists()) {
			return;
		}
		try {
			txtReader.setEncode("UTF-8");
			fileListSub = txtReader.txtreader(foremostFile , SettleConstants.DEDT_SPLIT);
		} catch(Exception e) {
			logger.error(">>> >>>对账文件读取异常！" + e.getMessage());
			return;
		}
		if (fileListSub == null || fileListSub.size() ==0) {
			return;
		}
		fileMapSub = new HashMap();
		for (int j=0;j<fileListSub.size();j++) {
			fileMap = new HashMap();
			fileMapSub = fileListSub.get(j);
			for (int i = 1;i <= fileMapSub.size();i++) {
				fileMap.put("F_"+i,fileMapSub.get("L_"+(i-1))==null?" ":fileMapSub.get("L_"+(i-1)));
			}
			if (j == 0) {
				headList.add(fileMap);
			} else {
				for (int i = 0;i < fileMapSub.size();i++) {
					fileMap.put("F_"+i,fileMapSub.get("L_"+(i-1)));
				}
				if ("100011".equals(fileMapSub.get("L_8")) || "100014".equals(fileMapSub.get("L_8"))) {
					fileMap.put("F_2",fileMapSub.get("L_1"));
					fileMap.put("F_3",fileMapSub.get("L_2"));
				} else {
					fileMap.put("F_2",fileMapSub.get("L_2"));
					fileMap.put("F_3",fileMapSub.get("L_1"));
				}
				if ("1".equals(fileMapSub.get("L_7"))) {
					fileMap.put("F_8","4013");
				} else {
					fileMap.put("F_8","4014");
				}
				
				fileList.add(fileMap);
			}
		}
		Map configMap = new HashMap();
		String localfilennew = newPath + fileName;
		File pathFile = new File(newPath);
		if (!pathFile.exists()) {
			pathFile.mkdirs();
		}
		configMap.put("FILE", localfilennew);	
		configMap.put("REPORT-HEAD", headList);
		try {
			txtWriter.WriteTxt(fileList, configMap, SettleConstants.DEDT_SPLIT, "UTF-8");
		} catch(Exception e) {
			logger.error(">>> >>>对账文件写入异常！" + e.getMessage());
			return;
		}	
	}
	/***
	 * httpRequest下载文件的方法
	 * @param url
	 * @param charset
	 * @param body
	 * @param filePath
	 * @return
	 * @throws Exception
	 */
	private static String httpRequest(String url, String charset, String body, String filePath) throws Exception {
		logger.info(">>> >>> >>> 进入 httpRequest 方法");
		HttpClient client = new HttpClient();
		client.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, charset);
		PostMethod postMethod = null;
		try {
			postMethod = new PostMethod(url);
			RequestEntity requestEntity = new StringRequestEntity(body, "text/xml", "UTF-8");
			postMethod.setRequestEntity(requestEntity);
			int statusCode = client.executeMethod(postMethod);
			if (statusCode != HttpStatus.SC_OK) {
				String msg = "访问失败！！HTTP_STATUS=" + statusCode;
				throw new HttpException(msg);
			}
			Header[] headerResponse = postMethod.getResponseHeaders();
			String headerStr = headerResponse[3].getValue();
			logger.info(">>> >>> 下载文件信息返回: " + java.net.URLDecoder.decode(headerStr));
			InputStream inputStream = null;
            OutputStream zipS = new FileOutputStream(new File(filePath));
			try{
				// 下载文件
				inputStream = postMethod.getResponseBodyAsStream();
				byte[] buffer = new byte[1024];
				int i = -1;
				while ((i = inputStream.read(buffer)) != -1) {
					zipS.write(buffer, 0, i);
				}
			}catch(Exception e){
				System.out.println(e.getMessage());
			}finally{
				try{
					zipS.flush();
					 if(zipS != null){
						 zipS.close();
					 }
					 if(inputStream != null){
						inputStream.close();
					 }
					 zipS = null;
				  }catch(Exception e2){
					;
				  }
			}
			String context = postMethod.getResponseBodyAsString();
			return context;
		} finally {
			if (postMethod != null)
				postMethod.releaseConnection();
		}
	}
	/***
	 * 解压ZIP文件
	 * @return
	 */
	private boolean unZipFile(String srcFilePath, String srcFileName) throws Exception {
		logger.info(">>> >>> >>> >>> >>> >>> 解压 ZIP 文件");
		//源文件位置
		File srcFile = new File(srcFilePath, srcFileName);
		ZipFile zipFile = null;
		//判断是否存在
		if(!srcFile.exists()) {
			logger.error(">>> >>>" + srcFilePath + srcFileName + "此文件不存在!");
			throw new Exception(srcFilePath + srcFileName + "此文件不存在!");
		}
		//创建ZIP对象
		zipFile = new ZipFile(srcFile);
		//设置UTF-8编码
		zipFile.setFileNameCharset("UTF-8");       				// 设置文件名编码，在UTF-8系统中需要设置    
        //验证文件未被损坏
		if(!zipFile.isValidZipFile()) {   						// 验证.zip文件是否合法，包括文件是否存在、是否为zip文件、是否被损坏等    
            throw new ZipException("压缩文件不合法,可能被损坏.");    
        }
        //解压
        zipFile.extractAll(srcFilePath);      					// 将文件抽出到解压目录(解压)    
		return true;
	}
	/***
	 * 获取解压后的对账文件
	 * @return
	 */
	private File filterTheFile(String srcFilePath) {
		logger.info(">>> >>> >>> >>> >>> >>> 获取解压后的对账文件");
		File folder = new File(srcFilePath);
		File[] fileArr = folder.listFiles(new CjFileFilter());
		/**
		 * 解压后的文件应该是唯一的
		 */
		if(fileArr.length < 1) {//解压后没有文件
			logger.error(">>> >>> 解压【畅捷支付】下载的zip后, 没有发现文件!");
			return null;
		} else if(fileArr.length > 1) {//目录下发现多个文件
			logger.error(">>> >>> 解压【畅捷支付】下载的zip后, 目录下发现多个文件!");
			return fileArr[fileArr.length - 1];
		}
		return fileArr[0];
	}
	/***
	 * 筛选文件的Filter
	 * 或取.txt文件
	 * @author CaoYang
	 */
	private class CjFileFilter implements FileFilter {
		public boolean accept(File pathname) {
			return pathname.getName().endsWith(".txt");
		}
	}
}
