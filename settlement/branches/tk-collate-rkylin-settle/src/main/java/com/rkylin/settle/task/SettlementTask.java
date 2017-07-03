package com.rkylin.settle.task;

import org.springframework.beans.factory.annotation.Autowired;

import com.rkylin.settle.service.SettlePosDetailService;
import com.rkylin.settle.service.SettleTransAccountService;

/***
 * 同步上游和下游的交易信息定时任务
 * @author Yang
 */
public class SettlementTask {
	@Autowired
	SettleTransAccountService settleTransAccountService;
	@Autowired
	SettlePosDetailService settlePosDetailService;
	
	/**
	 * 查询银企直联测试交易并汇总金额 selectTestTransAndSumAmountYQZL
	 */
	public void selectTestTransAndSumAmountYQZL() {
		settleTransAccountService.selectTestTransAndSumAmountYQZL();
	}
	
	/**
	 * 查询银企直联测试交易并汇总金额 selectTestTransAndSumAmountYQZL
	 */
	public void selectPosTransFeeAndUpdateOrder() {
		settlePosDetailService.selectPosTransFeeAndUpdateOrder();
	}
	
}
