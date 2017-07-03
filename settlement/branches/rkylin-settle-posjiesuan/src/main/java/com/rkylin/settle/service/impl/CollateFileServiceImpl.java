package com.rkylin.settle.service.impl;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.allinpay.ets.client.StringUtil;
import com.rkylin.settle.constant.SettleConstants;
import com.rkylin.settle.manager.SettleFileManager;
import com.rkylin.settle.pojo.SettleFile;
import com.rkylin.settle.pojo.SettleFileQuery;
import com.rkylin.settle.service.CollateFileService;
import com.rkylin.settle.util.FtpFilesInfo;
import com.rkylin.settle.util.FtpUpload;

@Service("collateFileService")
public class CollateFileServiceImpl implements CollateFileService {
	private Logger logger = LoggerFactory.getLogger(CollateFileServiceImpl.class);
	
	@Autowired
	private SettleFileManager settleFileManager;
	
	@Autowired
    Properties ftpProperties;
	
	@Override
	public FtpFilesInfo uploadMerchantFileToSHPTFTP(Date accountDate, String rootInstCd, String readType) throws Exception {
		logger.info(">>> >>> >>> >>> 开始:向商户平台FTP上传对账文件");
		//返回值
		FtpFilesInfo ftpFilesInfo = null;
		//清结算文件beanList
		List<SettleFile> settleFileList = null;
		//账期格式
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		//文件位置
		String filePath = SettleConstants.FILE_UP_PATH + "beifen" + File.separator;
		//File对象数组
		List<File> collectFileList = null;
		//FileName数组
		List<String> collectFileNameList = null;
		//Ftp上传工具类
		FtpUpload ftpUpload = null;
		//对账文件文件名称
		String collectFileName = null;
		//上传状态
		boolean flg = false;
		//文件对象
		File collectFile = null;
		
		/*
		 * 入参验证
		 */
		if(accountDate == null) {
			logger.error(">>> >>> >>> 异常:向商户平台FTP上传对账文件, accountDate不能为空, accountDate isNull Exception!");
			throw new Exception("异常:向商户平台FTP上传对账文件, accountDate不能为空, accountDate isNull Exception!");
		}
		if(StringUtil.isEmpty(rootInstCd)) {
			logger.error(">>> >>> >>> 异常:向商户平台FTP上传对账文件, rootInstCd不能为空, rootInstCd isEmpty Exception!");
			throw new Exception("异常:向商户平台FTP上传对账文件, rootInstCd不能为空, rootInstCd isEmpty Exception!");
		}
		if(StringUtil.isEmpty(readType)) {
			readType = null;
		}
		logger.info(">>> >>> >>> 向商户平台FTP上传对账文件入参:accountDate="+sdf.format(accountDate)+", rootInstCd="+rootInstCd+", readType="+readType);
		
		/*
		 * 查询文件信息
		 */
		SettleFileQuery query = new SettleFileQuery();
		query.setFileActive(1);//下游对文件
		query.setFileType("body");//文件体(去掉冗余信息)
		query.setStatusId(1);//状态为可用
		query.setRootInstCd(rootInstCd);
		query.setReadType(readType);//01充值,02非充值,03代收付
		settleFileList = settleFileManager.queryList(query);
		if(settleFileList == null || settleFileList.size() < 1) {
			logger.error(">>> >>> >>> 异常:向商户平台FTP上传对账文件, 获取下游对账文件信息为0条!");
			throw new Exception("异常:向商户平台FTP上传对账文件, 获取下游对账文件信息为0条!");
		}
		
		/*
		 * 获取文件对象 
		 */
		collectFileList = new ArrayList<File>();
		collectFileNameList = new ArrayList<String>();
		for(SettleFile settleFile : settleFileList) {
			//前缀 + "_" + 账期(yyyyMMdd) + "." + 后缀
			collectFileName = settleFile.getFilePrefix() + "_" + sdf.format(accountDate) + "." + settleFile.getFilePostfix();
			collectFile = new File(filePath, collectFileName);
			//文件不存在跳过此次循环
			if(!collectFile.exists()) continue;
			//添加文件对账
			collectFileList.add(collectFile);
			//添加文件名称
			collectFileNameList.add(collectFileName);
			logger.info(">>> >>> >>> 获取文件对象  " + filePath + collectFileName);
		}
		
		/*
		 * 链接FTP服务器
		 */
		logger.info(">>> >>> >>> 连接 商户平台FTP");
    	try {
    		ftpUpload = new FtpUpload();
    		String path = ftpProperties.getProperty("SHPT_FTP_DIRECTORY");//目录
    		String addr = ftpProperties.getProperty("SHPT_FTP_URL");//url地址
    		int port = Integer.parseInt(ftpProperties.getProperty("SHPT_FTP_PORT"));//端口
    		String username = ftpProperties.getProperty("SHPT_FTP_NAME");//账号
    		String password = ftpProperties.getProperty("SHPT_FTP_PASS");//密码
    		flg = ftpUpload.connect(path, addr, port, username, password);//链接
    		if (!flg) {//链接失败
    			logger.error(">>> >>> FTP连接失败, 3s后重连 " + flg);
    			Thread.sleep(3000);
    			flg = ftpUpload.connect(path, addr, port, username, password);//重新链接
        		if (!flg) {//重新连接失败
        			logger.error(">>> >>> FTP连接失败 " + flg);
        			throw new Exception("FTP连接失败 " + flg);
        		}
    		}
    	} catch (Exception e) {
    		logger.error("异常:向商户平台FTP上传对账文件, 商户平台FTP", e);
			e.printStackTrace();
			throw e;
    	}
    	
    	/*
    	 * 向FTP上传文件
    	 */
		logger.info(">>> >>> >>> 向FTP上传文件");
    	try {
        	flg = ftpUpload.upload(collectFileList);//上传
        	if (!flg) {//上传失败
    			logger.error(">>> >>> 向FTP上传文件失败 " + flg);
    			throw new Exception("向FTP上传文件失败 " + flg);
    		}
		} catch (Exception e) {
			logger.error(">>> >>> 向FTP上传文件失败!", e);
			e.printStackTrace();
			throw e;
		}
		logger.info("<<< <<< <<< <<< 结束:向商户平台FTP上传对账文件");
		
		ftpFilesInfo = new FtpFilesInfo();
		ftpFilesInfo.setFtpDirectory(ftpProperties.getProperty("SHPT_FTP_DIRECTORY"));//FTP清结算文件目录
		ftpFilesInfo.setFileNameList(collectFileNameList);//对账文件名称List<String>
		return ftpFilesInfo;
	}
}
