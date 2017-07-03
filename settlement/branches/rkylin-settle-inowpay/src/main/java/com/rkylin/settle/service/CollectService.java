package com.rkylin.settle.service;

import java.util.Date;

/***
 * 清分系统通用汇总业务service
 * @author Yang
 */
public interface CollectService {
	/**
	 * 汇总
	 * @param collectType	//汇总类型
	 * @return
	 * @throws Exception
	 */
	public void doCollect(Integer collectType) throws Exception;/**
	 * 汇总
	 * @param collectType	//汇总类型
	 * @param funcCode		//类型
	 * @return
	 * @throws Exception
	 */
	public void doCollect(Integer collectType, String funcCode) throws Exception;
	/**
	 * 汇总
	 * @param accountDate
	 * @param collectType	//汇总类型
	 * @return
	 * @throws Exception
	 */
	public void doCollect(Integer collectType, Date accountDate) throws Exception;
	/**
	 * 从交易画面发起汇总
	 * @param accountDate	//账期
	 * @param collectType	//汇总类型
	 * @return
	 * @throws Exception
	 */
	public void doCollect(Integer collectType, Integer[] settleTransDetailIdArr) throws Exception;
	/**
	 * 从汇总画面发起汇总
	 * @param accountDate	//账期
	 * @param collectType	//汇总类型
	 * @return
	 * @throws Exception
	 */
	public void doCollect(Integer collectType, Date accountDate, String merchantCode, String payChannelId, String funcCode) throws Exception;
	/**
	 * 汇总信息发送MQ
	 * @param ske
	 * @throws Exception
	 */
	public void doCollectBySettleKernelEntry(Long id) throws Exception;
	/**
	 * 汇总前回写10014机构号渠道号
	 */
	public void updatePayChannelIdBy4015Trans() throws Exception;
	/**
	 * 代收付结果返回后汇总
	 * @param funcCode	交易类型
	 * @return
	 * @throws Exception
	 */
//	void doCollectAfterDsfReturn(String funcCode) throws Exception;
}
