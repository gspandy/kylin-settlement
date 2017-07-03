package com.rkylin.settle.settleInterface;

import java.util.Date;

import com.rkylin.settle.pojo.SettleTransDetail;
import com.rkylin.settle.util.FtpFilesInfo;
import com.rkylin.settle.util.PagerModel;

public interface MerchantServiceInterface {
	/**
	 * 上传商户对账文件到商户平台FTP
	 * @param accountDate 	账期
	 * @param rootInstCd	商户编码: 例如M000001丰年, M000005君融贷
	 * @param readType		交易类型: 01充值, 02非充值, 03代收付
	 * @return	FtpFilesInfo 返回值: ftpDirectory FTP目录, fileNameList 文件名List 
	 * @throws Exception
	 */
	FtpFilesInfo uploadMerchantFileToSHPTFTP(Date accountDate, String rootInstCd, String readType) throws Exception;
	/**
	 * 商户平台查SETTLE_TRANS_DETAIL表分页信息
	 * @param beginDate 非null
	 * @param endDate
	 * @param payChannelId
	 * @param pageIndex 默认1
	 * @param pageSize 默认20
	 * @return
	 * @throws Exception
	 */
	PagerModel<SettleTransDetail> selectBySHPTPage(Date beginDate, Date endDate, String merchantCode, String payChannelId, Integer pageIndex, Integer pageSize) throws Exception;
	/**
	 * 商户平台查SETTLE_TRANS_DETAIL下载全部
	 * @param beginDate 非null
	 * @param endDate
	 * @param payChannelId
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	void uploadTransFileToSHPTFTP(Date beginDate, Date endDate, String merchantCode, String payChannelId, String ftpFileName) throws Exception;
}
