package com.rkylin.settle.task;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.rkylin.settle.constant.SettleConstants;
import com.rkylin.settle.logic.CollectForAccLogic;
import com.rkylin.settle.service.CancelAfterVerificationService;
import com.rkylin.settle.util.SettlementUtil;

/***
 * 通联对账相关定时任务
 * @author Yang
 */
public class ArtHTask {
	private static Logger logger = LoggerFactory.getLogger(ArtHTask.class);
	@Autowired
	private CollectForAccLogic collectForAccLogic;
	@Autowired
	private CancelAfterVerificationService cancelAfterVerificationService;
	@Autowired
	private SettlementUtil settlementUtil;
	/***
	 * 悦艺术家 线上充值40153汇总
	 * 定时任务, 内部对账之后
	 */
	public void collectForAcc40153() {
		logger.info(">>> >>> >>> 开始: 悦艺术家 线上充值40153汇总");
		try {
			collectForAccLogic.doCollect(
					SettleConstants.COLLATE_STU_3, 
					(Date) settlementUtil.getAccountDate("", -1, "Date"));
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 悦艺术家 线上充值40153汇总", e);
			e.printStackTrace();
		}
		logger.info("<<< <<< <<< 结束: 悦艺术家 线上充值40153汇总");
	}
	/***
	 * 悦艺术家 提现4014汇总
	 * 定时任务, 外部对账之后
	 */
	public void collectForAcc4014() {
		logger.info(">>> >>> >>> 开始: 悦艺术家 提现4014汇总");
		try {
			collectForAccLogic.doCollect(
					SettleConstants.COLLATE_STU_4, 
					(Date) settlementUtil.getAccountDate("", -1, "Date"));
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 悦艺术家 提现4014汇总", e);
			e.printStackTrace();
		}
		logger.info("<<< <<< <<< 结束: 悦艺术家 提现4014汇总");
	}
	/***
	 * 悦艺术家 核销
	 * 定时任务, 凌晨1点
	 */
	public void cancelAfterVerification() {
		logger.info(">>> >>> >>> 开始: 悦艺术家 核销");
		try {
			cancelAfterVerificationService.doCancelAfterVerification();
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 悦艺术家 核销", e);
			e.printStackTrace();
		}
		logger.info("<<< <<< <<< 结束: 悦艺术家 核销");
	}
}
