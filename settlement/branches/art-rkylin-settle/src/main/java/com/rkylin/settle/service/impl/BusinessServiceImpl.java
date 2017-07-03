package com.rkylin.settle.service.impl;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rkylin.merchant.dto.FileDownloadReqDto;
import com.rkylin.merchant.service.FileDownloadService;
import com.rkylin.settle.manager.SettleTransDetailManager;
import com.rkylin.settle.service.BusinessService;
import com.rkylin.settle.service.SettleParameterInfoService;
import com.rkylin.settle.util.ExcelUtil;
import com.rkylin.settle.util.FtpUpload;
import com.rkylin.settle.util.PagerModel;
@Service("businessService")
public class BusinessServiceImpl implements BusinessService{
	//日志对象
	protected static Logger logger = LoggerFactory.getLogger(BasicServiceImpl.class);
	
	@Autowired
	private SettleTransDetailManager settleTransDetailManager;
	
	@Autowired
    Properties ftpProperties;
	
	@Autowired
	FileDownloadService fileDownloadService;
	
	@Autowired
	SettleParameterInfoService settleParameterInfoService;
	
	@Override
	public PagerModel<Map<String,Object>> feeAmountSummary(
			String accountDate, String merchantCode, String payChannelId,
			String dealType, Integer pageIndex, Integer pageSize)throws Exception {
		List<Map<String,Object>> list = null;
		List<Map<String,Object>> allList = null;
		PagerModel<Map<String,Object>> pagerModel = new PagerModel<Map<String,Object>>();
		try {
			if(StringUtils.isBlank(accountDate)){
				logger.info("商户平台接口汇总手续费分页查询的入参-账期为空！！！");
				pagerModel.setMsg("账期不能为空！");
				return pagerModel;
			}
			list = this.getfeeAmountSummaryList(accountDate, merchantCode, payChannelId, dealType, pageIndex, pageSize,true);
			allList = this.getfeeAmountSummaryList(accountDate, merchantCode, payChannelId, dealType, null, null,false);
			pagerModel.setList(list);
			if(allList !=null && allList.size()>0){
				pagerModel.setTotal(allList.size());
			}else{
				pagerModel.setTotal(0);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return pagerModel;
	}
	
	@Override
	public String downloadAllfeeAmountSummary(
			String accountDate, String merchantCode, String payChannelId,
			String dealType,String fileName) throws Exception {
		List<Map<String,Object>> list = null;
		List<String[]> dataList = null;
		fileName += ".xlsx";
		try{
			if(StringUtils.isBlank(accountDate)){
				logger.info("商户平台接口汇总手续费全部下载excel的入参-账期为空！！！");
				return "账期不能为空！！！";
			}
			list = this.getfeeAmountSummaryList(accountDate, merchantCode, payChannelId, dealType, null, null,false);
			dataList = new ArrayList<String[]>();
			//生成EXCEL，并上传到FTP
			String[] excelHead = new String[]{"账期","笔数","金额(元)","手续费(元)","渠道","交易类型","商户"};//表头
			String[] contentRow = null;
			for(Map<String,Object> map :list){
				contentRow = new String[]{
					map.get("ACCOUNT_DATE")==null?"":(String)map.get("ACCOUNT_DATE"),map.get("TOTAL")==null?"0":(Long)map.get("TOTAL")+"",
					 map.get("AMOUNT")==null?"0":(((BigDecimal) map.get("AMOUNT")).divide(new BigDecimal(100))).toString(),map.get("FEE_AMOUNT")==null?"0":(((BigDecimal) map.get("FEE_AMOUNT"))).divide(new BigDecimal(100)).toString(),
					(String)map.get("PAY_CHANNEL_ID"),(String)map.get("FUNC_CODE"),
					(String)map.get("MERCHANT_CODE"),""
				};
				dataList.add(contentRow);
			}
			ExcelUtil excelUtil = new ExcelUtil();
			File file = excelUtil.createExcelFile(excelHead,dataList,fileName,5000);
			this.uploadFileToSHPTFTP(file);
			file.delete();
			
			//将结果通知商户平台
			try{
				String path = ftpProperties.getProperty("SHPT_FTP_DIRECTORY");//目录
	    		String addr = ftpProperties.getProperty("SHPT_FTP_URL");//url地址
				FileDownloadReqDto dto = new FileDownloadReqDto();
				dto.setFileName(fileName);
				dto.setFileUrl(addr+path+fileName);
				dto.setState("成功");
				fileDownloadService.notifyFromSettle(dto);
			}catch(Exception e){
				logger.error("通知商户平台报错："+e.getMessage());
			}
			
		}catch(Exception e){
			logger.error("错误啦：downloadAllfeeAmountSummary："+e.getMessage());
			return "操作失败！";
		}
		return "操作成功,excel数据已经生成并上传到FTP上！";
	}
	
	
	public List<Map<String,Object>> getfeeAmountSummaryList(
			String accountDate, String merchantCode, String payChannelId,
			String dealType, Integer pageIndex, Integer pageSize,Boolean paging)throws Exception {
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		try {
			logger.info("开始进入手续费汇总的方法");
			logger.info("手续费汇总的入参：accountDate="+accountDate+",merchantCode="+merchantCode+",payChannelId="+payChannelId+",dealType="+dealType+",pageIndex="+pageIndex+",pageSize="+pageSize+",paging="+paging);
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("accountDateStr", accountDate);
			map.put("merchantCode", merchantCode);
			if(!StringUtils.isEmpty(payChannelId)) map.put("payChannelId", payChannelId);
			String[] funcCodes = null;
			Integer dataFrom = null;
			if("0".equals(dealType)){
				funcCodes = new String[]{"4015","4017"};
				dataFrom = 0;//充值或退款交易用账户的数据汇总
			}else if("1".equals(dealType)){
				funcCodes =  new String[]{"4013","4014"};
				dataFrom = 3;//代收付用多渠道的数据汇总
			}else{
				logger.info("调用手续费汇总分页查询的入参dealType为null");
			}
		
			if(paging){
				if(pageIndex ==null){
					map.put("pageIndex", 1);
				}else{
					map.put("pageIndex", pageIndex);
				}
				if(pageSize ==null){
					map.put("pageSize", 10);
				}else{
					map.put("pageSize", pageSize);
				}
				map.put("offset", ((Integer)map.get("pageIndex") - 1) * (Integer)map.get("pageSize"));
			}
			
			if(funcCodes !=null){
				map.put("funcCodes", funcCodes);
				map.put("dataFrom", dataFrom);
				map.put("flag", "single");
				list = settleTransDetailManager.summaryFeeAmount(map);
			}else{
				map.put("flag", "double");
				list = settleTransDetailManager.summaryFeeAmount(map);
			}
			list = editSettleTrasnDetailInfo(list);
		} catch (Exception e) {
			logger.error("手续费汇总的方法发生错误，错误信息："+e.getMessage());
		}
		logger.info("结束进入手续费汇总的方法");
		return list;
	}
	
	/**
	 * 向商户平台FTP上传文件
	 * @param file 源文件
	 * @return
	 */
	private boolean uploadFileToSHPTFTP(File file) throws Exception {
		logger.info(">>> >>> >>> 开始:向商户平台FTP上传文件");
		//Ftp上传工具类
		FtpUpload ftpUpload = null;
		//上传状态
		boolean flg = false;
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
        	flg = ftpUpload.upload(file);//上传
        	if (!flg) {//上传失败
    			logger.error(">>> >>> 向FTP上传文件失败 " + flg);
    			throw new Exception("向FTP上传文件失败 " + flg);
    		}
		} catch (Exception e) {
			logger.error(">>> >>> 向FTP上传文件失败!", e);
			e.printStackTrace();
			throw e;
		}
		logger.info("<<< <<< <<< 结束:向商户平台FTP上传文件");
		return flg;
	}
	/**
	 * 匹配payChannelId和merchant的名称
	 * @return
	 */
	private List<Map<String, Object>> editSettleTrasnDetailInfo(List<Map<String, Object>> list) {
		logger.info(">>> >>> >>> 开始匹配payChannelId和merchant的名称");
		String pCId = null;
		Map<String, Object> std = null;
		String[] pCStrArr = null;
		String mStr = null;
		String pStr = null;
		Map<String, String> payChannelIdMap = settleParameterInfoService.selectPayChannelIdInfo();
		Map<String, String> merchantCdIdMap = settleParameterInfoService.selectMerchantCdIdInfo();

		for(int i = 0; i < list.size(); i ++) {
			std = list.get(i);
			pCId = String.valueOf(std.get("PAY_CHANNEL_ID"));
			if(StringUtils.isEmpty(pCId)) {
				std.put("PAY_CHANNEL_ID", "未知渠道");
			} else {
				pCStrArr = pCId.split(",");
				if(pCStrArr.length == 2) {
					mStr = merchantCdIdMap==null ? pCStrArr[0] : merchantCdIdMap.get(pCStrArr[0]);
					pStr = payChannelIdMap==null ? pCStrArr[1] : payChannelIdMap.get(pCStrArr[1]);
					mStr = StringUtils.isEmpty(mStr) ? pCStrArr[0] : mStr;
					pStr = StringUtils.isEmpty(pStr) ? pCStrArr[1] : pStr;
					pCId = mStr + "," + pStr;
				} else if(pCStrArr.length == 1) {
					pStr = payChannelIdMap==null ? pCStrArr[0] : payChannelIdMap.get(pCStrArr[0]);
					pCId = StringUtils.isEmpty(pStr) ? pCStrArr[0] : pStr;
				}
				std.put("PAY_CHANNEL_ID", pCId);
			}
			list.set(i, std);
		}
		
		logger.info("<<< <<< <<< 开始匹配payChannelId和merchant的名称");
		return list;
	}
}
