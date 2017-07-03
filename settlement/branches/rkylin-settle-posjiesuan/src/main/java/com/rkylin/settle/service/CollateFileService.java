package com.rkylin.settle.service;

import java.util.Date;

import com.rkylin.settle.util.FtpFilesInfo;

public interface CollateFileService {
	/**
	 * 上传商户对账文件到商户平台FTP
	 * @param accountDate 	账期
	 * @param rootInstCd	商户编码: 例如M000001丰年, M000005君融贷
	 * @param readType		交易类型: 01充值, 02非充值, 03代收付
	 * @return	FtpFilesInfo 返回值: ftpDirectory FTP目录, fileNameList 文件名List 
	 * @throws Exception
	 */
	FtpFilesInfo uploadMerchantFileToSHPTFTP(Date accountDate, String rootInstCd, String readType) throws Exception;
}
