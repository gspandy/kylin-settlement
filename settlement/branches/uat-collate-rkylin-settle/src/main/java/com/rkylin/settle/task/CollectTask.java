package com.rkylin.settle.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.rkylin.settle.constant.SettleConstants;
import com.rkylin.settle.service.CollectService;

/***
 * 通联对账相关定时任务
 * @author Yang
 * 
 */
public class CollectTask {
	private static Logger logger = LoggerFactory.getLogger(CollectTask.class);
	@Autowired
	private CollectService collectService;
	/**
	 * 应收记会计账 23:20
	 */
	public void collectBalance() {
		try {
			collectService.doCollect(SettleConstants.COLLECT_TYPE_2, "4014");
		} catch(Exception e) {
			e.printStackTrace();
			logger.info(">>> >>> >>> 异常 【代付】计会计账 ： ", e);
		}
		try {
			collectService.doCollect(SettleConstants.COLLECT_TYPE_2, "4016");
		} catch(Exception e) {
			e.printStackTrace();
			logger.info(">>> >>> >>> 异常 【提现】计会计账 ： ", e);
		}
	}
	/**
	 * 回写10014交易的pay_channel_id
	 */
	public void updatePayChannelIdBy4015Trans() {
//		try {
//			collectService.updatePayChannelIdBy4015Trans();
//		} catch (Exception e) {
//			e.printStackTrace();
//			logger.info(">>> >>> >>> 异常 回写10014交易的pay_channel_id ： ", e);
//		}
	}
	/**
	 * 实收记会计账 11:30
	 */
	public void collectCollate() {
		try {
			collectService.doCollect(SettleConstants.COLLECT_TYPE_2, "4015");
		} catch(Exception e) {
			e.printStackTrace();
			logger.info(">>> >>> >>> 异常 【充值】计会计账 ： ", e);
		}
		try {
			collectService.doCollect(SettleConstants.COLLECT_TYPE_1, "40131");
		} catch(Exception e) {
			e.printStackTrace();
			logger.info(">>> >>> >>> 异常 【实时代收】计会计账 ： ", e);
		}
		try {
			collectService.doCollect(SettleConstants.COLLECT_TYPE_1, "4013");
		} catch(Exception e) {
			e.printStackTrace();
			logger.info(">>> >>> >>> 异常 【还款代收】计会计账 ： ", e);
		}
	}
}
