package com.rkylin.settle.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.rkylin.settle.service.SettlePosDetailService;
import com.rkylin.settle.service.SettleTransDetailService;

/***
 * 同步上游和下游的交易信息定时任务
 * @author Yang
 */
public class UpdateAccToDetailTask {
	private static Logger logger = LoggerFactory.getLogger(UpdateAccToDetailTask.class);
	@Autowired
	private SettleTransDetailService settleTransDetailService;
	@Autowired
	private SettlePosDetailService settlePosDetailService;
	
	/**
	 * 批量修改 将SETTLE_TRANS_ACCOUNT表的(手续费)和(机构,协议)回写到SETTLE_TRANS_DETAIL表
	 */
	public void updateAccountInfoToDetailInfo() {
		try {
			settleTransDetailService.updateAccountInfoToDetailInfo();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("异常:批量修改 将SETTLE_TRANS_ACCOUNT表的(手续费)和(机构,协议)回写到SETTLE_TRANS_DETAIL表", e);
		}
	}
	
	/**
	 * 批量修改 将SETTLE_TRANS_ACCOUNT表的(手续费)和(机构,协议)回写到SETTLE_POS_DETAIL表
	 */
	public void updateAccountInfoToPosInfo() {
		try {
			settlePosDetailService.updateAccountInfoToDetailInfo();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("异常:批量修改 将SETTLE_TRANS_ACCOUNT表的(手续费)和(机构,协议)回写到SETTLE_POS_DETAIL表", e);
		}
	}
}
