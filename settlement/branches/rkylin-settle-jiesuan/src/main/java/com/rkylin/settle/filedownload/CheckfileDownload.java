package com.rkylin.settle.filedownload;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.io.IOUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aipg.acquery.AcQueryReq;
import com.aipg.common.AipgReq;
import com.aipg.common.InfoReq;
import com.aipg.transquery.TransQueryReq;
import com.allinpay.XmlTools;
import com.allinpay.ets.client.SecurityUtil;
import com.allinpay.ets.client.util.Base64;
import com.rkylin.file.txt.TxtReader;
import com.rkylin.file.txt.TxtWriter;
import com.rkylin.settle.constant.SettleConstants;
import com.rkylin.settle.manager.ParameterInfoManager;
import com.rkylin.settle.pojo.ParameterInfo;
import com.rkylin.settle.pojo.ParameterInfoQuery;
import com.rkylin.settle.util.SFTPUtil;
import com.rkylin.settle.util.SettlementUtil;
import com.rkylin.utils.RkylinMailUtil;

@Component("checkfileDownload")
public class CheckfileDownload {
    protected static Logger logger = LoggerFactory.getLogger(CheckfileDownload.class);
    @Autowired
    Properties fileProperties;
    @Autowired
	SettlementUtil settlementUtil;
	@Autowired
	SFTPUtil ftp;
	@Autowired
	ParameterInfoManager parameterInfoManager;
    /**
     * @Title: tlFileDown 
     * @Description: 定时跑批下载通联对账文件（账期默认从DB中获取）
     * @param rootInstId 机构号
     * @param fileType 文件类型：WG：网关支付，ZF：支付系统
     * @return Map<String,String>    返回类型
     * @autor CLF
     * @throws 
     */
    public Map<String, String> tlFileDown(String rootInstId, String fileType) {
        return tlFileDown(rootInstId, fileType, null);
    }
    /**
     * 指定账期下载连连支付对账文件
     * @param invoicedate 上游账期
     * @param rootInstId 机构号
     * @return
     */
    public Map<String, String> lianLianFileDown(String invoicedate, String merchantCode, String fileType) {
    	logger.info("下载连连对账文件 ["+ merchantCode +"]协议 & ["+ fileType +"]交易类型 ————————————START————————————");
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
    	String accountDate = "";
    	String accountDate2 = "";
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
    	logger.info(">>> >>> 【指定账期下载连连支付对账文件】  取得的账期为 :"+ accountDate);
    	/**
    	 * 开始下载上游对账文件
    	 */
    	String thePrefixStr = merchantCode + "_LLKJ_";
    	String filename = null;
    	String p2pfilePath = null;
    	String p2pfileBakPath = null;
    	try {
			//上游渠道对账文件取得方式待定
			filename = "LL_" + accountDate + "_" + merchantCode + "_";
	    	p2pfilePath = SettleConstants.FILE_PATH +accountDate2 + File.separator;
	    	p2pfileBakPath = SettleConstants.FILE_PATH +accountDate2 + File.separator + "ll_bak" + File.separator;
	    	if (!new File(p2pfilePath).exists()) new File(p2pfilePath).mkdirs();
	    	if (!new File(p2pfileBakPath).exists()) new File(p2pfileBakPath).mkdirs();
	    	String host = fileProperties.getProperty(thePrefixStr + "HOST");
	    	String port = fileProperties.getProperty(thePrefixStr + "PORT");
	    	String username = fileProperties.getProperty(thePrefixStr + "USERNAME");
	    	String password = fileProperties.getProperty(thePrefixStr + "PASSWORD");
	    	String directory = fileProperties.getProperty(thePrefixStr + "DIRECTORY");
	    	String srcFileName = null;
	    	
	    	ftp.setHost(host);
			ftp.setPort(Integer.parseInt(port));
			ftp.setUsername(username);
			ftp.setPassword(password);
			ftp.connect();
			
			if(SettleConstants.ACCOUNT_COED_LLKJ.equals(fileType)) {
				srcFileName = fileProperties.getProperty(thePrefixStr + "SRC_FILENAME_JY").replace("{YMD}", accountDate);
			} else if(SettleConstants.ACCOUNT_COED_GENERATE.equals(fileType)) {
				srcFileName = fileProperties.getProperty(thePrefixStr + "SRC_FILENAME_FK").replace("{YMD}", accountDate);
			} else {
				throw new Exception("下载连连对账文件，传入的readType错误！");
			}			
			ftp.download(directory, srcFileName, p2pfileBakPath + filename + fileType + ".txt");

			Thread.sleep(2000);
    	} catch(Exception e) {
			logger.error("获取连连对账文件异常！" + e.getMessage());
    	} finally {
    		/*
    		 * 关闭下载文件资源链接
    		 */
    		ftp.disconnect();
    	}
    	try {
    		boolean isSuccess = this.editNewLianLianCollateFile(p2pfileBakPath, p2pfilePath, filename, fileType);
    		if(!isSuccess) {
    			logger.error(">>> >>> >>> 编辑 连连支付对账文件 "+ fileType +" 执行失败!");
    		}
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 编辑 连连支付对账文件 执行失败!" + e.getMessage());
			e.printStackTrace();
		}
    	logger.info("下载连连对账文件 ————————————END————————————");
    	return rtnMap;
    }
    /**
     * 指定账期下载通联对账文件
     * @param rootInstId 机构号
     * @param fileType 文件类型：WG：网关支付，ZF：支付系统
     * @param accountDate 上游账期
     * @return
     */
    public Map<String, String> tlFileDown(String rootInstId, String fileType, String accountDate) {
        logger.info("通联文件下载 ————————————START————————————rootInstId:["+rootInstId+"]fileType:["+fileType+"]accountDate"+accountDate);
        Map<String, String> rtnMap = new HashMap<String, String>();
        rtnMap.put("errMsg", "通联文件下载成功");
        rtnMap.put("errCode", "0000");
        String serverUrl = "";
        String mchtCd = ""; // 商户号
        String settleDate = ""; // 格式为yyyy-MM-dd
        String settleDateP = ""; // 格式为yyyy-MM-dd
        String settleDateB = ""; // 格式为yyyy-MM-dd
        String md5key = ""; // md5key
        String fileAsString = ""; // 签名信息前的对账文件内容
        String fileSignMsg = ""; // 文件签名信息
        String signMsg = "";
        String cert = "";
        String filename = "";
        String filetype = "";
        String certPublic = "";
        String passwordk = "";
        String username = "";
        String password = "";
        URL url = null;
        TxtReader txtReader = new TxtReader();
        TxtWriter txtWriter = new TxtWriter();
        boolean isVerified = false; // 验证签名结果
        try {
            if (accountDate == null || "".equals(accountDate)) {
                try {
                    settleDate = (String) settlementUtil.getAccountDate("yyyy-MM-dd", 0, "");
                    settleDateP = (String) settlementUtil.getAccountDate(settleDate, "yyyy-MM-dd", -1, "");
                    settleDateB = (String) settlementUtil.getAccountDate(settleDate, "yyyy-MM-dd", -2, "");
                } catch (Exception e2) {
                    logger.error("计算账期异常！" + e2.getMessage());
                    rtnMap.put("errCode", "0001");
                    rtnMap.put("errMsg", "计算账期异常");
                    return rtnMap;
                }
            } else {
                settleDate = (String) settlementUtil.getAccountDate(accountDate, "yyyy-MM-dd", 1, "");
                settleDateP = accountDate;
                settleDateB = (String) settlementUtil.getAccountDate(accountDate, "yyyy-MM-dd", -1, "");
            }
            logger.info("取得的账期为" + settleDate);
            
            if (SettleConstants.ACCOUNT_COED_CHANNEL.equals(fileType) || SettleConstants.ACCOUNT_COED_MOBILE.equals(fileType)) {
                serverUrl = fileProperties.getProperty(rootInstId + fileType + "_URL");
                mchtCd = fileProperties.getProperty(rootInstId + fileType + "_MER_CD");
                md5key = fileProperties.getProperty(rootInstId + fileType + "_KEY");
                cert = fileProperties.getProperty(rootInstId + fileType + "_CERT");
                filename = fileProperties.getProperty(rootInstId + fileType + "_FILE_NAME");
                filetype = fileProperties.getProperty(rootInstId + fileType + "_FILE_TYPE"); 
                filename = filename.replace("{YMD}", settleDateP.replace("-", "")) + filetype;
                // 得到摘要(MD5Encode函数的传入参数为商户号+结算日期+md5key)
                signMsg = SecurityUtil.MD5Encode(mchtCd + settleDate + md5key);
                // 建立连接
                url = new URL(serverUrl + "?mchtCd=" + mchtCd + "&settleDate=" + settleDate + "&signMsg=" + signMsg);
                CheckfileDownload test = new CheckfileDownload();
                HttpURLConnection httpConn = test.getHttpsURLConnection(url);
                httpConn.connect();
                // 读取交易结果
                BufferedReader fileReader = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
                StringBuffer fileBuf = new StringBuffer(); // 签名信息前的字符串
                String lines;
                List<Map<String, String>> excelList = new ArrayList<Map<String, String>>();
                Map<String, String> paraMap = new HashMap<String, String>();
                while ((lines = fileReader.readLine()) != null) {
                    if (lines.length() > 0) {
                        paraMap = new HashMap<String, String>();
                        // 按行读，每行都有换行符
                        fileBuf.append(lines + "\r\n");
                        paraMap.put("F_1", lines.replace("ZF|", "4015|").replace("TH|", "4017|"));
                        
                        
                        
                        excelList.add(paraMap);
                    } else {
                        // 文件中读到空行，则读取下一行为签名信息
                        fileSignMsg = fileReader.readLine();
                    }
                }
                fileReader.close();
                fileAsString = fileBuf.toString();
                if (null == fileAsString || fileAsString.contains("ERRORCODE")) {
                    rtnMap.put("errMsg", "通联文件下载成功，但内容为空:" + fileAsString);
                    rtnMap.put("errCode", "0001");
                    return rtnMap;
                }
                // 验证签名：先对文件内容计算MD5摘要，再将MD5摘要作为明文进行验证签名
                String fileMd5 = SecurityUtil.MD5Encode(fileAsString);
                isVerified = SecurityUtil.verifyByRSA((SettleConstants.TOMCAT_ROOT_PATH + cert).replace("file:", ""), fileMd5.getBytes(), Base64.decode(fileSignMsg));
                if (isVerified) {
                    Map<String, Object> configMap = new HashMap<String, Object>();
                    // 验证签名通过，解析交易明细，开始对账
                    if (excelList.size() > 0) {
                        File file = new File(SettleConstants.FILE_PATH + settleDate.replace("-", "") + File.separator);
                        if (!file.exists()) {
                            file.mkdirs();
                        }
                        configMap.put("FILE", SettleConstants.FILE_PATH + settleDate.replace("-", "") + File.separator + filename);
                        TxtWriter.WriteTxt(excelList, configMap, "", "UTF-8");
                    } else {
                        rtnMap.put("errMsg", "通联文件下载失败，但内容为空");
                        rtnMap.put("errCode", "0001");
                        return rtnMap;
                    }
                } else {
                    // 验证签名不通过，丢弃对账文件
                    rtnMap.put("errMsg", "验证签名不通过，丢弃对账文件。");
                    rtnMap.put("errCode", "0001");
                    return rtnMap;
                    
                }
            } else if (SettleConstants.ACCOUNT_COED_GENERATE.equals(fileType)) {
            	String filenameret = null;
        		List<Map> fileList = new ArrayList<Map>();
        		List<Map> headList = new ArrayList<Map>();
        		Map fileMap = null;
            	File fileF = null;
            	File fileZ = null;
            	File fileJ = null;
            	List<Map> fileFList = null;
            	List<Map> fileZList = null;
            	List<Map> fileJList = null;
                if ("MX00001".equals(rootInstId)) {
                	filenameret= generate_sub(rootInstId,SettleConstants.ACCOUNT_COED_GENERATE_F,settleDate,settleDateP,settleDateB);
                	if (filenameret != null) {
                		fileF = new File(filenameret);
                        if (fileF.exists()) {
                        	try {
                    			txtReader.setEncode("UTF-8");
                    			fileFList = txtReader.txtreader(fileF , SettleConstants.DEDT_SPLIT3);
                    		} catch(Exception e) {
                    			logger.error(">>> >>>对账文件" + fileF.getName() + "读取异常！" + e.getMessage(), e);
                    		}
                        }
                	}
                	filenameret =generate_sub(rootInstId,SettleConstants.ACCOUNT_COED_GENERATE_Z,settleDate,settleDateP,settleDateB);
                	if (filenameret != null) {
                		fileZ = new File(filenameret);
                        if (fileZ.exists()) {
                        	try {
                    			txtReader.setEncode("UTF-8");
                    			fileZList = txtReader.txtreader(fileZ , SettleConstants.DEDT_SPLIT3);
                    		} catch(Exception e) {
                    			logger.error(">>> >>>对账文件" + fileZ.getName() + "读取异常！" + e.getMessage(), e);
                    		}
                        }
                	}
                	filenameret =generate_sub(rootInstId,SettleConstants.ACCOUNT_COED_GENERATE_J,settleDate,settleDateP,settleDateB);
                	if (filenameret != null) {
                		fileJ = new File(filenameret);
                        if (fileJ.exists()) {
                        	try {
                    			txtReader.setEncode("UTF-8");
                    			fileJList = txtReader.txtreader(fileJ , SettleConstants.DEDT_SPLIT3);
                    		} catch(Exception e) {
                    			logger.error(">>> >>>对账文件" + fileJ.getName() + "读取异常！" + e.getMessage(), e);
                    		}
                        }
                	}
                	
                	if (fileFList != null || fileFList.size() > 1) {
                		for (Map subMap : fileFList) {
                			fileMap = new HashMap();
                			if (subMap.size() < 9) {
                				continue;
                			}
                			fileMap.put("F_1", subMap.get("L_0"));
                			fileMap.put("F_2", subMap.get("L_1"));
                			fileMap.put("F_3", subMap.get("L_2"));
                			fileMap.put("F_4", subMap.get("L_3"));
                			fileMap.put("F_5", subMap.get("L_4"));
                			fileMap.put("F_6", subMap.get("L_5"));
                			fileMap.put("F_7", subMap.get("L_6"));
                			fileMap.put("F_8", subMap.get("L_7"));
                			fileMap.put("F_9", subMap.get("L_8"));
                			fileMap.put("F_10", subMap.get("L_9"));
                			fileList.add(fileMap);
                		}
                	}
                	if (fileZList != null || fileZList.size() > 1) {
                		for (Map subMap : fileZList) {
                			fileMap = new HashMap();
                			if (subMap.size() < 9) {
                				continue;
                			}
                			fileMap.put("F_1", subMap.get("L_0"));
                			fileMap.put("F_2", subMap.get("L_1"));
                			fileMap.put("F_3", subMap.get("L_2"));
                			fileMap.put("F_4", subMap.get("L_3"));
                			fileMap.put("F_5", subMap.get("L_4"));
                			fileMap.put("F_6", subMap.get("L_5"));
                			fileMap.put("F_7", subMap.get("L_6"));
                			fileMap.put("F_8", subMap.get("L_7"));
                			fileMap.put("F_9", subMap.get("L_8"));
                			fileMap.put("F_10", subMap.get("L_9"));
                			fileList.add(fileMap);
                		}
                	}
                	if (fileJList != null || fileJList.size() > 1) {
                		for (Map subMap : fileJList) {
                			fileMap = new HashMap();
                			if (subMap.size() < 9) {
                				continue;
                			}
                			fileMap.put("F_1", subMap.get("L_0"));
                			fileMap.put("F_2", subMap.get("L_1"));
                			fileMap.put("F_3", subMap.get("L_2"));
                			fileMap.put("F_4", subMap.get("L_3"));
                			fileMap.put("F_5", subMap.get("L_4"));
                			fileMap.put("F_6", subMap.get("L_5"));
                			fileMap.put("F_7", subMap.get("L_6"));
                			fileMap.put("F_8", subMap.get("L_7"));
                			fileMap.put("F_9", subMap.get("L_8"));
                			fileMap.put("F_10", subMap.get("L_9"));
                			fileList.add(fileMap);
                		}
                	}
                	if (fileList != null || fileList.size() > 0) {
                		Map configMap = new HashMap();
                		String localfilennew = SettleConstants.FILE_PATH + settleDate.replace("-", "") + File.separator + "TL_MX00001_ZF_" + settleDateP.replace("-", "") + ".csv";
                		File pathFile = new File(SettleConstants.FILE_PATH +settleDate.replace("-", "") + File.separator);
                		if (!pathFile.exists()) {
                			pathFile.mkdirs();
                		}
                		configMap.put("FILE", localfilennew);
                		
                		fileMap = new HashMap();
                		fileMap.put("F_1", "START");
                		headList.add(fileMap);
                		configMap.put("REPORT-HEAD", headList);
                		
                		try {
                			txtWriter.WriteTxt(fileList, configMap, SettleConstants.DEDT_SPLIT3, "UTF-8");
                		} catch(Exception e) {
                			logger.error(">>> >>>对账文件写入异常！" + e.getMessage());
                		}
                	}
                } else {
                	generate_sub(rootInstId,fileType,settleDate,settleDateP,settleDateB);
                }
            } else {
                rtnMap.put("errMsg", "输入的文件类型不明！");
                rtnMap.put("errCode", "0001");
                return rtnMap;
            }
        } catch (Exception e) {
            logger.error("通联文件下载异常！", e);
            rtnMap.put("errMsg", "通联文件下载异常！" + e.getMessage());
            rtnMap.put("errCode", "0001");
            return rtnMap;
        }
        logger.info("通联文件下载 ————————————END————————————");
        return rtnMap;
    }
    
    private String generate_sub(String rootInstId, String fileType, String settleDate, String settleDateP, String settleDateB) throws Exception {
        String serverUrl = "";
        String mchtCd = ""; // 商户号
    	String cert = "";
        String filename = "";
        String filetype = "";
        String certPublic = "";
        String passwordk = "";
        String username = "";
        String password = "";
    	serverUrl = fileProperties.getProperty(rootInstId + fileType + "_URL");
        mchtCd = fileProperties.getProperty(rootInstId + fileType + "_MER_CD");
        cert = (SettleConstants.TOMCAT_ROOT_PATH + fileProperties.getProperty(rootInstId + fileType + "_CERT")).replace("file:", "");
        certPublic = (SettleConstants.TOMCAT_ROOT_PATH + fileProperties.getProperty(rootInstId + fileType + "_CERT_PUBLIC")).replace("file:", "");
        passwordk = fileProperties.getProperty(rootInstId + fileType + "_PASSWORDK");
        username = fileProperties.getProperty(rootInstId + fileType + "_USERNAME");
        password = fileProperties.getProperty(rootInstId + fileType + "_PASSWORD");
        filename = fileProperties.getProperty(rootInstId + fileType + "_FILE_NAME");
        filetype = fileProperties.getProperty(rootInstId + fileType + "_FILE_TYPE");
        filename = filename.replace("{YMD}", settleDateP.replace("-", "")) + filetype;
        
        String xml = "";
        File resFile = null;
        for (int i = 0; i < 10; i ++) {
            AipgReq aipgReq = new AipgReq();
            InfoReq info = makeReq("200002", mchtCd, username, password);
            TransQueryReq dqr = new TransQueryReq();
            aipgReq.setINFO(info);
            aipgReq.addTrx(dqr);
            dqr.setSTATUS(2);
            dqr.setMERCHANT_ID(mchtCd);
            dqr.setTYPE(1);
            dqr.setSTART_DAY(settleDateB.replace("-", "") + "235000");
            dqr.setEND_DAY(settleDateP.replace("-", "") + "235000");
            dqr.setCONTFEE("1");
            
            xml = XmlTools.buildXml(aipgReq, true);
            String resp = sendToTlt(xml, false, serverUrl, certPublic, cert, passwordk);
            File file = new File(SettleConstants.FILE_PATH + settleDate.replace("-", "") + File.separator);
            if (!file.exists()) {
                file.mkdirs();
            }
            try {
            	 writeBill(resp, SettleConstants.FILE_PATH + settleDate.replace("-", "") + File.separator + filename, SettleConstants.FILE_PATH + settleDate.replace("-", "")
	                        + File.separator);
			} catch (Exception e) {
				logger.error("通联对账文件下载异常:", e);
				/*
				 * 通联对账文件下载, 经常出现下载异常的情况，通常是多个环境并发下载发生线程冲突所导致的。
				 * 解决方案：
				 * 		捕获下载异常，打印异常信息但不做return、throws等结束方法的操作。
				 * 		在随机等待时间后重复下载 ， 重复次数10次等待时间随机
				 */
			}
            resFile = new File(SettleConstants.FILE_PATH + settleDate.replace("-", "") + File.separator + filename);
            if (resFile.exists()) {
            	return SettleConstants.FILE_PATH + settleDate.replace("-", "") + File.separator + filename;
            } else {
            	 logger.info("通联文件下载异常,尝试次数：[" + i + "]");
            	 Thread.sleep(getRandomTime(3000L));
            }
        }
        return null;
    }
    
    /**
     * @param resp
     * @throws Exception
     */
    private void writeBill(String resp, String filename, String gzname) throws Exception {
    	int iStart = resp.indexOf("<CONTENT>");
        if (iStart == -1)
            throw new Exception("XML报文中不存在<CONTENT>");
        int end = resp.indexOf("</CONTENT>");
        if (end == -1)
            throw new Exception("XML报文中不存在</CONTENT>");
        String billContext = resp.substring(iStart + 9, end);
        // 写文件
        FileOutputStream sos = null;
        sos = new FileOutputStream(new File(gzname + "bill.gz"));
        Base64InputStream b64is = new Base64InputStream(IOUtils.toInputStream(billContext), false);
        IOUtils.copy(b64is, sos);
        IOUtils.closeQuietly(b64is);
        // 解压
        ZipInputStream zin = new ZipInputStream(new FileInputStream(new File(gzname + "bill.gz")));
        ZipEntry zipEntry = null;
        while ((zipEntry = zin.getNextEntry()) != null) {
            String entryName = zipEntry.getName().toLowerCase();
            logger.info(">>>>>>>>>读取压缩包内对账文件，文件名：" + entryName);
            FileOutputStream os = new FileOutputStream(filename);
            // Transfer bytes from the ZIP file to the output file
            byte[] buf = new byte[1024];
            int len;
            while ((len = zin.read(buf)) > 0) {
                os.write(buf, 0, len);
            }
            os.close();
            zin.closeEntry();
        }
        sos.close();
        zin.close();
    }
    /*
     * 查询通联账户余额
     * @param rootInstId 机构号
     * @param accountNo 帐户号
     * @return 金额（分）
     */
	public String merAcctBalance(String rootInstId,String accountNo) {
        logger.info("通账余额取得 ————————————START————————————");
		String xml="";
		String balance = "失败";
		AipgReq aipg=new AipgReq();
		String serverUrl = fileProperties.getProperty(rootInstId + "ZF_URL");
		String mchtCd = fileProperties.getProperty(rootInstId + "ZF_MER_CD");
		String cert = (SettleConstants.TOMCAT_ROOT_PATH + fileProperties.getProperty(rootInstId + "ZF_CERT")).replace("file:", "");
		String certPublic = (SettleConstants.TOMCAT_ROOT_PATH + fileProperties.getProperty(rootInstId + "ZF_CERT_PUBLIC")).replace("file:", "");
		String passwordk = fileProperties.getProperty(rootInstId + "ZF_PASSWORDK");
		String username = fileProperties.getProperty(rootInstId + "ZF_USERNAME");
		String password = fileProperties.getProperty(rootInstId + "ZF_PASSWORD");
		if (null == accountNo || "".equals(accountNo)) {
			accountNo = fileProperties.getProperty(rootInstId + "ZF_ACCOUNTNO");
		}
		if  (null == accountNo || "".equals(accountNo)) {
			return "0";
		}
		InfoReq info=makeReq("300000", mchtCd, username, password);//交易码
		aipg.setINFO(info);
		AcQueryReq ahquery=new AcQueryReq();
		ahquery.setACCTNO(accountNo);//商户在通联的虚拟账号 会唐：200222000008786000;君融贷： 200222000008705000;融数：200222000009005000;丰年：200222000008708000
//		ahquery.setSTARTDAY(startDate);//查询开始日期
//		ahquery.setENDDAY(endDate);//查询结束日期
		aipg.addTrx(ahquery);
		xml=XmlTools.buildXml(aipg,true); 
		String resp = sendToTlt(xml,false,serverUrl, certPublic, cert, passwordk);
		Document document;
		try {
			resp = resp.substring(0, resp.indexOf("</AIPG>")+7);
			document = DocumentHelper.parseText(resp);
	        Element rootElement = document.getRootElement();
	        balance = rootElement.element("ACQUERYREP").element("ACNODE").element("BALANCE").getText();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("通账余额取得异常:", e);
		}
        logger.info("通账余额取得 ————————————END————————————");
        return balance;
	}
    /**
     * 组装报文头部
     * @param trxcod
     * @return 日期：Sep 9, 2012
     */
    private InfoReq makeReq(String trxcod, String merchantId, String userName, String password) {   
        InfoReq info = new InfoReq();
        info.setTRX_CODE(trxcod);
        info.setREQ_SN(merchantId + "-" + String.valueOf(System.currentTimeMillis()));
        info.setUSER_NAME(userName);
        info.setUSER_PASS(password);
        info.setLEVEL("5");
        info.setDATA_TYPE("2");
        info.setVERSION("03");
        if ("300000".equals(trxcod) || "300001".equals(trxcod) || "300003".equals(trxcod) || "220001".equals(trxcod) || "220003".equals(trxcod)) {
            info.setMERCHANT_ID(merchantId);
        }
        return info;
    }
    
    public String sendToTlt(String xml, boolean flag, String url, String cert, String cert_K, String pass) {
        try {
            if (!flag) {
                xml = XmlTools.signMsg(xml, cert_K, pass, false);
            } else {
                xml = xml.replaceAll("<SIGNED_MSG></SIGNED_MSG>", "");
            }
            return sendXml(xml, url, flag, cert);
        } catch (Exception e) {
            e.printStackTrace();
            if (e.getCause() instanceof ConnectException || e instanceof ConnectException) {
                logger.info("请求链接中断，请做交易结果查询，以确认上一笔交易是否已被通联受理，避免重复交易");
            }
        }
        return "请求链接中断，请做交易结果查询，以确认上一笔交易是否已被通联受理，避免重复交易";
    }
    
    public String sendXml(String xml, String url, boolean isFront, String cert) throws Exception, Exception {
        logger.info("======================发送报文======================：\n" + xml);
        String resp = CheckfileDownload.send(url, xml);
        logger.info(resp + "\n======================响应内容======================");
        boolean flag = XmlTools.verifySign(resp, cert, false, isFront);
        if (flag) {
            logger.info("响应内容验证通过");
        } else {
            logger.info("响应内容验证不通过");
        }
        return resp;
    }
    
    public static String send(String url, String xml) throws Exception {
        OutputStream reqStream = null;
        InputStream resStream = null;
        URLConnection request = null;
        String respText = null;
        byte[] postData;
        try {
            postData = xml.getBytes("GBK");
            request = createRequest(url, "POST");
            
            request.setRequestProperty("Content-type", "application/tlt-notify");
            request.setRequestProperty("Content-length", String.valueOf(postData.length));
            request.setRequestProperty("Keep-alive", "false");
            
            reqStream = request.getOutputStream();
            reqStream.write(postData);
            reqStream.close();
            
            ByteArrayOutputStream ms = null;
            resStream = request.getInputStream();
            ms = new ByteArrayOutputStream();
            byte[] buf = new byte[4096];
            int count;
            while ((count = resStream.read(buf, 0, buf.length)) > 0) {
                ms.write(buf, 0, count);
            }
            resStream.close();
            respText = new String(ms.toByteArray(), "GBK");
        } catch (Exception ex) {
            throw ex;
        } finally {
            close(reqStream);
            close(resStream);
        }
        return respText;
    }
    
    private static URLConnection createRequest(String strUrl, String strMethod) throws Exception {
        URL url = new URL(strUrl);
        URLConnection conn = url.openConnection();
        conn.setDoInput(true);
        conn.setDoOutput(true);
        if (conn instanceof HttpsURLConnection) {
            HttpsURLConnection httpsConn = (HttpsURLConnection) conn;
            httpsConn.setRequestMethod(strMethod);
            httpsConn.setSSLSocketFactory(XmlTools.getSSLSF());
            httpsConn.setHostnameVerifier(XmlTools.getVerifier());
        } else if (conn instanceof HttpURLConnection) {
            HttpURLConnection httpConn = (HttpURLConnection) conn;
            httpConn.setRequestMethod(strMethod);
        }
        return conn;
    }
    
    private static void close(InputStream c) {
        try {
            if (c != null)
                c.close();
        } catch (Exception ex) {
            
        }
    }
    
    private static void close(OutputStream c) {
        try {
            if (c != null)
                c.close();
        } catch (Exception ex) {
            
        }
    }
    
    /*
     * 得到HttpURLConnection对象 @author: Robin @param : URL
     * @return:HttpURLConnection @date : 2011-03-10
     */
    public HttpURLConnection getHttpsURLConnection(URL url) {
        try {
            HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
            
            if ("https".equals(url.getProtocol())) // 如果是https协议
            {
                HttpsURLConnection httpsConn = (HttpsURLConnection) httpConnection;
                TrustManager[] managers = { (TrustManager) new MyX509TrustManager() };// 证书过滤
                SSLContext sslContext;
                sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, managers, new SecureRandom());
                SSLSocketFactory ssf = sslContext.getSocketFactory();
                httpsConn.setSSLSocketFactory(ssf);
                httpsConn.setHostnameVerifier((HostnameVerifier) new MyHostnameVerifier());// 主机名过滤
                return httpsConn;
                
            }
            return httpConnection;
            
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return null;
    }
    
    public class MyX509TrustManager implements X509TrustManager {
        /**
         * 该方法体为空时信任所有客户端证书
         * @param chain
         * @param authType
         */
        public void checkClientTrusted(X509Certificate[] chain, String authType) {
        }
        
        /**
         * 该方法体为空时信任所有服务器证书
         * @param chain
         * @param authType
         */
        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) {
        }
        
        /**
         * 返回信任的证书
         * @return
         */
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
        
    }
    /*
     * 编写主机名过滤器 @author: Robin @date : 2011-03-10
     */
    private class MyHostnameVerifier implements HostnameVerifier {
        /**
         * 返回true时为通过认证 当方法体为空时，信任所有的主机名
         * @param hostname
         * @param session
         * @return
         */
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }
    /***
     * 编辑新连连对账文件
     * @param path		原始文件目录
     * @param filename	原始文件名称
     * @return 执行结果	true成功; false失败
     */
    public boolean editNewLianLianCollateFile(String oldPath, String newPath, String filename, String fileType) throws Exception {    	
    	logger.info(">>> >>> >>> 读取 并 编辑连连对账文件");
    	List<Map<String, String>> analyzedList = new ArrayList<Map<String, String>>();
		//连连原始对账文件目录+名称
    	String targetFile = null;
    	//写入文件是时需要的配置映射结构体
    	Map<String, Object> configMap = new HashMap<String, Object>();
    	//新对账文件名称
    	String newFilename;
    	/*
    	 * 读取并解析连连对账文件 return List<Map<String, String>>
    	 */
    	try {
    		targetFile = oldPath + filename + fileType + ".txt";
        	analyzedList = this.analyzeOriCollateFile(targetFile);
    		if (null == analyzedList || 1 >= analyzedList.size()) {
    			logger.error(">>> >>> >>> 对账文件里面的内容为空！");
    			return false;
    		}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(">>> >>> >>>异常: 读取并解析连连原始对账文件异常！");
			return false;
		}
		/*
		 * 编辑上游对账文件数据结构
		 */
    	String amountInputCd = null;
    	String amountOutputCd = null;
    	String reverse = null;
    	
    	if(SettleConstants.ACCOUNT_COED_GENERATE.equals(fileType)) {
    		amountInputCd = "4013";
    		amountOutputCd = "4014";
    		reverse = "5024";
    	} else if(SettleConstants.ACCOUNT_COED_LLKJ.equals(fileType)) {
    		amountInputCd = "4015";
    		amountOutputCd = "4017";
    		reverse = "4017";
    	} else {
    		throw new Exception("编辑连连对账文件("+ fileType +"), 匹配商户收款标志(0收款 1付款) 异常");
    	}
    	//第一行:标题行
		//明细行:
//		0:商户订单号(商户退款单号)
//		1:商户号
//		2:商户订单时间(yyyymmdd hh24:mm:ss)(商户退款时 间)
//		3:商户业务编号
//		4:银通支付单号(退款时返回商户原订单号)
//		5:银通账务日期
//		6:订单金额(元 保留2位小数)
//		7:商户收款标志(0收款 1付款)
//		8:交易状态(0-成功 5-已退款)
//		9:更新时间(yyyymmdd hh24:mm:ss)
//		10:手续费(元 保留2位小数)
//		11:支付产品(WEB支付网关|手机应用支 付网关|API渠道| WAP支付网关| IVR支付网关)
//		12:支付方式 (余额支付|储蓄卡网银支付|信用 卡网银支付|储蓄卡快捷支付|信用卡快捷支付|线下网点支付|充值卡支付|企业网银支付)
//		13:订单信息
		List<Map<String,String>> newFileMap = new ArrayList<Map<String,String>>();
		for(int i = 0; i < analyzedList.size(); i ++) {
			Map<String, String> bean = analyzedList.get(i);
			if(i == 0) {//表头不做编辑
				Map<String, String> newBean = this.editNewCollateFileRow(bean);
				newFileMap.add(newBean); 
				continue;
			}
			//判断交易结果转换业务编码
			if ("0".equals(bean.get("L_7").toString())) {
				bean.put("L_3",amountInputCd);
			} else if ("1".equals(bean.get("L_7").toString())) {
				bean.put("L_3",amountOutputCd);
			}
			//判断交易结果转换业务编码
			if ("0".equals(bean.get("L_8").toString())) {
				bean.put("L_8","16");
			} else if ("5".equals(bean.get("L_8").toString())) {
				bean.put("L_3",reverse);
				bean.put("L_0",bean.get("L_4"));
				bean.put("L_6",bean.get("L_6").toString().replace("-", ""));
				bean.put("L_10",bean.get("L_10")==null?"0":bean.get("L_10").toString().replace("-", ""));
				bean.put("L_8","16");
			} else {
				bean.put("L_8","14");
			}
			/*
			 * 编辑新连连文件的结构体
			 */
			Map<String, String> newBean = this.editNewCollateFileRow(bean);
			newFileMap.add(newBean);
		}		
		//获取新对账文件名称 并 存入结构体
		newFilename = newPath +  filename + fileType + ".txt";
		configMap.put("FILE", newFilename); 
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
     * 编辑支付对账文件 '行' 结构体
     * @param oldRowMap	原始结构 映射Key L_*
     * @return	newRowMap 新结构	映射Key F_*
     */
    protected Map<String, String> editNewCollateFileRow(Map<String, String> oldRowMap) {
    	//行Map结构体的entrySet对象
    	Set<Entry<String, String>> entrySet = oldRowMap.entrySet();
    	//新的行Map结构体
		Map<String, String> newRowMap = new HashMap<String, String>();
		//遍历行Map结构体
		for(Entry<String, String> entry : entrySet) {
			String oldKey = entry.getKey();
			String rowNum = String.valueOf(Integer.parseInt(oldKey.substring(oldKey.indexOf("_") + 1)) + 1);
			String newKey = "F_" + rowNum;	//KEY中的L替换成F, 详见TxtWriter和TxtReader类源码
			String newValue = entry.getValue().equals("null") ? "": entry.getValue();	//value值不变
			newRowMap.put(newKey, newValue);
		}
		return newRowMap;
    }
    /***
     * 解析TXT对账文件
     * @param targetFile 	目标文件位置
     * @return 新对账文件结构体
     */
    protected List<Map<String, String>> analyzeOriCollateFile(String targetFile) {
    	return this.analyzeOriCollateFile(targetFile, 0);
    }
    /***
     * 解析TXT对账文件
     * @param targetFile 	目标文件位置
     * @return 新对账文件结构体
     */
    protected List<Map<String, String>> analyzeOriCollateFile(String targetFile, Integer startRow) {
    	return this.analyzeOriCollateFile(targetFile, startRow, 0);
    }
    /***
     * 解析TXT对账文件
     * @param targetFile 	目标文件位置
     * @return 新对账文件结构体
     */
    protected List<Map<String, String>> analyzeOriCollateFile(String targetFile, Integer startRow, Integer tailRow) {
    	return this.analyzeOriCollateFile(targetFile, startRow, tailRow, null, null);
    }
    /***
     * 解析TXT对账文件
     * @param targetFile	目标文件位置
     * @param startRow		解析文件行的起点 【从第startRow行 开始写解析文件】
     * @return 新对账文件结构体
     */
    protected List<Map<String, String>> analyzeOriCollateFile(String targetFile, Integer startRow, Integer tailRows, String split, String encoding) {
    	//读取文件信息工具类
    	TxtReader txtReader;
    	//读取文件信息的结构体
    	List<Map> fileListSub;
    	//临时Map结构体
    	Map<String, String> tempMap;
    	//解析完成的List
    	List<Map<String, String>> analyzedList;
    	//原始对账文件File对象
    	File file = new File(targetFile);
		if (!file.exists()) {
			logger.error(">>> >>> 异常: 对账文件不存在！");
			return null;
		}
		File collatefile = file;
		txtReader = new TxtReader();
		fileListSub = new ArrayList<Map>();
		analyzedList = new ArrayList<Map<String, String>>();
		tempMap = new HashMap<String, String>();
		
		if(split == null) split = SettleConstants.DEDT_SPLIT2;
		if(encoding == null) encoding = "UTF-8";
		
		try {
			txtReader.setEncode(encoding);
			fileListSub = txtReader.txtreader(collatefile , split);
		} catch(Exception e) {
			logger.error(">>> >>>对账文件" + collatefile.getName() + "读取异常！" + e.getMessage(), e);
			return null;
		}
		if (fileListSub.size() > 1) {
			for (int i = startRow ; i < fileListSub.size() - tailRows; i ++) {
				tempMap = fileListSub.get(i);
				analyzedList.add(tempMap);
			}
		}
    	return analyzedList;
    }
    /**
     * 金额单位为【元】 更改为【分】
     * @param amountStrY 金额 单位为【元】
     * @return String 金额 单位为【分】
     */
    protected String getAmountStrF(String amountStrY) {
    	String amountStrF = null;
		BigDecimal amountBigDecimal = new BigDecimal(amountStrY);
		BigDecimal multiplicand = new BigDecimal(100);//乘数
		amountStrF = String.valueOf(amountBigDecimal.multiply(multiplicand).intValue());
		return amountStrF;
    }
    /**
     * 随机毫秒
     * @param startMs
     * @return
     */
    private Long getRandomTime(Long startMs) {
    	Long time = (Double.valueOf(Math.random() * 10000).longValue()) + startMs;
    	logger.info(">>> >>> >>> 随机获得的毫秒:" + time);
    	return time;
    }
}
