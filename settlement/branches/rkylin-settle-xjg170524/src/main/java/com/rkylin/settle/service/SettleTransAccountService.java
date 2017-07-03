package com.rkylin.settle.service;

import java.util.Date;


/***
 * 上游交易信息业务逻辑
 * @author Yang
 *
 */
public interface SettleTransAccountService {
	/**
	 * 查询银企直联测试交易并汇总金额 selectTestTransAndSumAmountYQZL
	 */
	void selectTestTransAndSumAmountYQZL();
	/**
	 * 查询银企直联测试交易并汇总金额 selectTestTransAndSumAmountYQZL
	 * @param date
	 */
	void selectTestTransAndSumAmountYQZL(Date date);
}
