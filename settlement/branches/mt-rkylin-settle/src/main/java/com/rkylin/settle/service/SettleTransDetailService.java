package com.rkylin.settle.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.rkylin.settle.pojo.SettleTransDetail;
import com.rkylin.settle.util.PagerModel;

/***
 * 下游交易信息业务逻辑
 * @author Yang
 *
 */
public interface SettleTransDetailService {
	/***
	 * 获取分润funcCodes
	 */
	public List<String> queryProfitFuncCodes() throws Exception;
	/***
	 * 调用账户系统接口冲正
	 * @return
	 */
	public Map<String, Object> doCorrectByAccount(List<SettleTransDetail> settleTransDetailList) throws Exception;
	/***
	 * 调用账户系统接口抹账或退款
	 * @return
	 */
	public Map<String, Object> doAccOfOrRefundByAccount(List<SettleTransDetail> settleTransDetailList) throws Exception;
	/***
	 * 批量修改 将SETTLE_TRANS_ACCOUNT表的(手续费)和(机构,协议)回写到SETTLE_TRANS_DETAIL表
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> updateAccountInfoToDetailInfo() throws Exception;
	/***
	 * 批量修改 将SETTLE_TRANS_ACCOUNT表的(手续费)和(机构,协议)回写到SETTLE_TRANS_DETAIL表
	 * @param accountDate 账期日期
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> updateAccountInfoToDetailInfo(Date accountDate) throws Exception;
	/***
	 * 批量修改 将SETTLE_TRANS_ACCOUNT表的(手续费)和(机构,协议)回写到SETTLE_TRANS_DETAIL表
	 * @param beginDate 账期开始日期
	 * @param endDate 	账期结束日期	
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> updateAccountInfoToDetailInfo(Date beginDate, Date endDate) throws Exception;
	/***
	 * 将accountList中的readStatusId写入detailList
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> doWriteToSettleTransDetail() throws Exception;
	/***
	 * 将accountList中的readStatusId写入detailList
	 * @param accountDate
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> doWriteToSettleTransDetail(Date accountDate) throws Exception;
	/***
	 * 将accountList中的readStatusId写入detailList
	 * @param beginDate
	 * @param endDate
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> doWriteToSettleTransDetail(Date beginDate, Date endDate) throws Exception;
	/**
	 * 商户平台查SETTLE_TRANS_DETAIL表分页信息
	 * @param beginDate 非null
	 * @param endDate
	 * @param merchantCode 商户号
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
	 * @param merchantCode 商户号
	 * @param payChannelId
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	void uploadTransFileToSHPTFTP(Date beginDate, Date endDate, String merchantCode, String payChannelId, String ftpFileName) throws Exception;
}
