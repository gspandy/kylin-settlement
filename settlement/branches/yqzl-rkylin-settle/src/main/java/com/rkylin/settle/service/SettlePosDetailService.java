package com.rkylin.settle.service;

import java.util.Date;
import java.util.Map;

/***
 * POS交易信息业务逻辑
 * @author Yang
 *
 */
public interface SettlePosDetailService {
	/***
	 * 批量修改 将SETTLE_TRANS_ACCOUNT表的(手续费)和(机构,协议)回写到SETTLE_POS_DETAIL表
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> updateAccountInfoToDetailInfo() throws Exception;
	/***
	 * 批量修改 将SETTLE_TRANS_ACCOUNT表的(手续费)和(机构,协议)回写到SETTLE_POS_DETAIL表
	 * @param accountDate 账期日期
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> updateAccountInfoToDetailInfo(Date accountDate) throws Exception;
	/***
	 * 批量修改 将SETTLE_TRANS_ACCOUNT表的(手续费)和(机构,协议)回写到SETTLE_POS_DETAIL表
	 * @param beginDate 账期开始日期
	 * @param endDate 	账期结束日期	
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> updateAccountInfoToDetailInfo(Date beginDate, Date endDate) throws Exception;
	/**
	 * 查询还款交易手续费并更新订单系统
	 * @param date
	 */
	void selectPosTransFeeAndUpdateOrder();
	/**
	 * 查询还款交易手续费并更新订单系统
	 * @param date
	 */
	void selectPosTransFeeAndUpdateOrder(Date date);
}
