package com.rkylin.settle.logic;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.Rop.api.domain.FileUrl;
import com.rkylin.settle.common.UploadAndDownLoadUtils;
import com.rkylin.settle.constant.SettleConstants;
import com.rkylin.settle.manager.ParameterInfoManager;
import com.rkylin.settle.manager.SettleBatchManageManager;
import com.rkylin.settle.manager.SettlementManager;
import com.rkylin.settle.pojo.ParameterInfo;
import com.rkylin.settle.pojo.ParameterInfoQuery;
import com.rkylin.settle.pojo.SettleBatchManage;
import com.rkylin.settle.util.ArithUtil;
import com.rkylin.settle.util.SettlementUtil;
@Component("settlementLogic")
public class SettlementLogic {
	private static Logger logger = LoggerFactory.getLogger(SettlementLogic.class);

	@Autowired
	HttpServletRequest request;
	@Autowired
	ParameterInfoManager parameterInfoManager;
	@Autowired
	SettlementManager settlementManager;
	@Autowired
	SettlementUtil settlementUtils;
	@Autowired
	SettleBatchManageManager settleBatchManageManager;
	
	/**
	 * 文件下载
	 * 
	 * @param type 文件类型
	 * @param batch 批次号
	 * @param accountDate 账期 yyyyMMdd
	 * @param filepath 生成文件路径+文件名
	 * @param priOrPubKey 公钥或者私钥
	 * @param flg 私钥时为1，公钥为其他
	 * @param appKey ropKey
	 * @param appSecret ropSecret
	 * @param ropUrl ropUrl
	 * 
	 * @return
	 */
	public Map<String,String> getFileFromROP(int type,String batch,Date accountDate,String filepath,String priOrPubKey,int flg
			,String appKey,String appSecret,String ropUrl){

		Map<String,String> rtnMap = new HashMap<String,String>();
		rtnMap.put("errMsg", "ok");
		rtnMap.put("errCode", "0000");
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
    	SettlementUtil settlementUtils = new SettlementUtil();
    	try {
			String urlKeyStr = "";
			List<FileUrl> urlKey = UploadAndDownLoadUtils.getUrlKeys(type, accountDate, batch, SettleConstants.FILE_XML,appKey,appSecret,ropUrl);
			if (urlKey == null || urlKey.size() == 0) {
				logger.error("urlkey不存在！");
				rtnMap.put("errMsg", "urlkey不存在！");
				rtnMap.put("errCode", "0001");
				return rtnMap;
			}
			String dfilename;
			String dfilepath;
			for (FileUrl fileurl:urlKey) {
				urlKeyStr = fileurl.getUrl_key();
				dfilename = type+fileurl.getInvoice_date()+"_"+fileurl.getBatch()+"."+SettleConstants.FILE_CSV;
				dfilepath = UploadAndDownLoadUtils.downloadFile(formatter.format(accountDate),SettleConstants.FILE_XML, urlKeyStr,dfilename,request,type,priOrPubKey,flg,appKey,appSecret,ropUrl);
				settlementUtils.copyFile(dfilepath+File.separator+dfilename, filepath);
				break;
			}
    	} catch(Exception e) {
    		rtnMap.put("errMsg", "文件下载失败！"+e.getMessage());
			rtnMap.put("errCode", "0001");
			return rtnMap;
    	} 	
		File file = new File(filepath);
		if (!file.exists()) {
			rtnMap.put("errMsg", "文件不存在！");
			rtnMap.put("errCode", "0001");
		}
    	
		return rtnMap;
	}
	
	/**
	 * 文件上传
	 * 
	 * @param type 文件类型
	 * @param batch 批次号
	 * @param accountDate 账期 yyyyMMdd
	 * @param filepath 生成文件路径+文件名
	 * @param priOrPubKey 公钥或者私钥
	 * @param flg 私钥时为1，公钥为其他
	 * @param appKey ropKey
	 * @param appSecret ropSecret
	 * @param ropUrl ropUrl
	 * 
	 * @return
	 */
	public Map<String,String> setFileFromROP(int type,String batch,Date accountDate,String filepath,String priOrPubKey,int flg
			,String appKey,String appSecret,String ropUrl){

		Map<String,String> rtnMap = new HashMap<String,String>();
		rtnMap.put("errMsg", "ok");
		rtnMap.put("errCode", "0000");

		File file = new File(filepath);
		if (!file.exists()) {
			rtnMap.put("errMsg", "文件不存在！");
			rtnMap.put("errCode", "0001");
		}
		
		String rtnurlKey = "";
    	try {
    		rtnurlKey = UploadAndDownLoadUtils.uploadFile(filepath, type, accountDate, batch, SettleConstants.FILE_XML,priOrPubKey,flg,appKey,appSecret,ropUrl);
			if (rtnurlKey == null || "".equals(rtnurlKey)) {
				logger.error("文件上传失败！");
				rtnMap.put("errMsg", "文件上传失败！");
				rtnMap.put("errCode", "0001");
				return rtnMap;
			}
			rtnMap.put("urlKey",rtnurlKey);
    	} catch(Exception e) {
    		rtnMap.put("errMsg", "文件上传失败！"+e.getMessage());
			rtnMap.put("errCode", "0001");
			return rtnMap;
    	}
		return rtnMap;
	}
	
	/**
	 * 取得一个机构号
	 * 
	 * @param parameterCode P2P机构号
	 * @param parameterType 类型
	 * 
	 * @return
	 */
	public String getParameterInfoByCode(String parameterCode,String parameterType) {
		// TODO Auto-generated method stub
		ParameterInfoQuery query = new ParameterInfoQuery();
		query.setParameterType(parameterType);
		query.setParameterCode(parameterCode);
		query.setStatusId(1);
		List<ParameterInfo> parameterInfos=parameterInfoManager.queryList(query);
		if( parameterInfos!=null && parameterInfos.size() >0){
			return parameterInfos.get(0).getParameterValue();
		}
		return null;
	}
	
	/**
	 * 取得全部机构号
	 * 
	 * @param parameterType 类型
	 * @param flg key设置1：VALUE为MapKey，其他：CODE为MapKey
	 * 
	 * @return
	 */
	public Map<String,String> getParameterCodeByType(String parameterType,int flg) {
		// TODO Auto-generated method stub
		ParameterInfoQuery query = new ParameterInfoQuery();
		query.setParameterType(parameterType);
		query.setStatusId(1);
		HashMap<String,String> parameterCode = new HashMap<String,String>();
		List<ParameterInfo> parameterInfos=parameterInfoManager.queryList(query);
		if( parameterInfos!=null && parameterInfos.size() >0){
			for(int i=0;i<parameterInfos.size();i++){
				if (flg==1) {
					parameterCode.put(parameterInfos.get(i).getParameterValue(), parameterInfos.get(i).getParameterCode());
				}else {
					parameterCode.put(parameterInfos.get(i).getParameterCode(), parameterInfos.get(i).getParameterValue());
				}
			}
			return parameterCode;
		}
		return null;
	}
	
	/**
	 * 取得对应批次号
	 * 
	 * @param accountDate 账期
	 * @param batchType 对应不同业务的不同类型ID
	 * 
	 * @return
	 */
	public String getBatchNo(Date accountDate,String batchType,String rootInstCd) throws Exception {
		List<SettleBatchManage> rtnList = null;
		SettleBatchManage batchManageQuery = new SettleBatchManage();
		SettleBatchManage batchManage = new SettleBatchManage();
		String rtnSrc = "";
		ArithUtil arithUtil = new ArithUtil();
		
		batchManageQuery.setRootInstCd(rootInstCd);
		batchManageQuery.setAccountDate(accountDate);
		batchManageQuery.setBatchType(batchType);
		
		rtnList = settlementManager.selectbatchno(batchManageQuery);
		
		if (rtnList.size() == 0) {
			rtnSrc = "";
		} else if (rtnList.size() == 1) {
			batchManage = rtnList.get(0);
			try {
				rtnSrc = batchManage.getBatchCons() + settlementUtils.pad(batchManage.getBatchNum(),batchManage.getBatchNumLen(),1);
			} catch (Exception e) {
				rtnSrc = "";
			}
			batchManage.setBatchId(null);
			batchManage.setCreatedTime(null);
			batchManage.setUpdatedTime(null);
			batchManage.setBatchSubNo(arithUtil.doubleAdd(batchManage.getBatchSubNo(), "1"));
			batchManage.setAccountDate(accountDate);
			batchManage.setBatchNum(arithUtil.doubleAdd(batchManage.getBatchNum(), "1"));
			settleBatchManageManager.saveSettleBatchManage(batchManage);
		} else {
			batchManage = rtnList.get(1);
			try {
				rtnSrc = batchManage.getBatchCons() + settlementUtils.pad(batchManage.getBatchNum(),batchManage.getBatchNumLen(),1);
			} catch (Exception e) {
				rtnSrc = "";
			}
			batchManage.setBatchNum(arithUtil.doubleAdd(batchManage.getBatchNum(), "1"));
			settleBatchManageManager.saveSettleBatchManage(batchManage);
		}
		if ("".equals(rtnSrc)) {
//			logger.error("未查询到相应'批次号信息'!!!");
//			logger.error("可能原因1:传入参数有误, 通过 RootInstCd 和 BatchType 条件查不到数据");
//			logger.error("可能原因2:批次号管理表[SETTLE_BATCH_MANAGE表] 中缺少初始数据!");
//			throw new SettleException("未查询到相应 '批次号信息'");
		}
		return rtnSrc;
	}
	
}
