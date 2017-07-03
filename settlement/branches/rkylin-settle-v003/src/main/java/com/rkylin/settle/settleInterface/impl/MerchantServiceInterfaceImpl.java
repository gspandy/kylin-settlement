package com.rkylin.settle.settleInterface.impl;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rkylin.settle.pojo.SettleTransDetail;
import com.rkylin.settle.service.CollateFileService;
import com.rkylin.settle.service.SettleTransDetailService;
import com.rkylin.settle.service.impl.SettleTransDetailServiceImpl;
import com.rkylin.settle.settleInterface.MerchantServiceInterface;
import com.rkylin.settle.util.FtpFilesInfo;
import com.rkylin.settle.util.PagerModel;

@Component("merchantServiceInterface")
public class MerchantServiceInterfaceImpl implements MerchantServiceInterface {
	private static Logger logger = LoggerFactory.getLogger(MerchantServiceInterfaceImpl.class);
	/*
	 * 清结算交易逻辑Bean
	 */
	@Autowired
	private SettleTransDetailService settleTransDetailService;
	/*
	 * 对账文件逻辑Bean
	 */
	@Autowired
	private CollateFileService collateFileService;

	@Override
	public FtpFilesInfo uploadMerchantFileToSHPTFTP(Date accountDate,
			String rootInstCd, String readType) throws Exception {
		FtpFilesInfo ftpFilesInfo = null; 
		try {
			ftpFilesInfo = collateFileService.uploadMerchantFileToSHPTFTP(accountDate, rootInstCd, readType);
		} catch (Exception e) {
			logger.error("collateFileService.uploadMerchantFileToSHPTFTP方法异常:", e);
			throw e;
		}
		return ftpFilesInfo;
	}

	@Override
	public PagerModel<SettleTransDetail> selectBySHPTPage(Date beginDate,
			Date endDate, String merchantCode, String payChannelId, Integer pageIndex,
			Integer pageSize) throws Exception {
		PagerModel<SettleTransDetail> pagerModel = null;
		try {
			pagerModel = settleTransDetailService.selectBySHPTPage(beginDate, endDate, merchantCode, payChannelId, pageIndex, pageSize);
		} catch (Exception e) {
			logger.error("settleTransDetailService.selectBySHPTPage方法异常:", e);
			throw e;
		}
		return pagerModel;
	}

	@Override
	public void uploadTransFileToSHPTFTP(Date beginDate, Date endDate,
			String merchantCode, String payChannelId, String ftpFileName) throws Exception {
		try {
			settleTransDetailService.uploadTransFileToSHPTFTP(beginDate, endDate, merchantCode, payChannelId, ftpFileName);
		} catch (Exception e) {
			logger.error("settleTransDetailService.uploadTransFileToSHPTFTP方法异常:", e);
			throw e;
		}
	}
}
