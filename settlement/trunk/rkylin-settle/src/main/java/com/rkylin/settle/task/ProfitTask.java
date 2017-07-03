package com.rkylin.settle.task;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.rkylin.settle.constant.Constants;
import com.rkylin.settle.constant.SettleConstants;
import com.rkylin.settle.filedownload.HtglRepayDownload;
import com.rkylin.settle.filedownload.ROPFileDown;
import com.rkylin.settle.logic.CollateForPosLogic;
import com.rkylin.settle.logic.FromLoanLogic;
import com.rkylin.settle.logic.ProfitFroLoanLogic;
import com.rkylin.settle.service.AccountService;
import com.rkylin.settle.service.CollateService;
import com.rkylin.settle.service.PositionService;
import com.rkylin.settle.service.ProfitService;

@Repository("profitTask")
public class ProfitTask {
	private static Logger logger = LoggerFactory.getLogger(ProfitTask.class);
	@Autowired
	private ProfitService profitService;
	@Autowired
	private ProfitFroLoanLogic profitFroLoanLogic;
	@Autowired
	private FromLoanLogic fromLoanLogic;
	@Autowired
	private ROPFileDown rOPFileDown;
	@Autowired
	private CollateForPosLogic collateForPosLogic; // 对账相关逻辑
	@Autowired
	AccountService accountService;
	@Autowired
	CollateService collateService;
	@Autowired
	HtglRepayDownload htglRepayDownload;
	@Autowired
	PositionService positionService;
	
	/***
	 * 读取账户系统 二期 交易信息 调用存储过程
	 * 执行1次/天
	 */
	public void readAccDateBySP() {
		logger.info(">>> >>> 读取账户系统 二期 交易信息 调用存储过程 开始 ... ...");
		try {
			profitService.readAccDateBySP();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(">>> >>> 异常: 读取账户系统 二期 交易信息 调用存储过程 定时任务, 执行1次/天", e);
		}
		logger.info(">>> >>>  ... ... 读取账户系统 二期 交易信息 调用存储过程 结束");
	}
	/***
	 * 读取账户系统 一期 交易信息 调用存储过程
	 * 执行1次/天
	 */
	public void readAccOldDateBySP() {
		logger.info(">>> >>> 读取账户系统交易 一期 信息 调用存储过程 开始 ... ...");
		try {
			profitService.readAccOldDateBySP();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(">>> >>> 异常: 读取账户系统 一期 交易信息 调用存储过程 定时任务, 执行1次/天", e);
		}
		logger.info(">>> >>>  ... ... 读取账户系统 一期 交易信息 调用存储过程 结束");
	}
	/***
	 * 读取账一期系统交易信息
	 * 执行1次/2分钟
	 */
	public void getAccountOldTransOrderInfos() {
		logger.info(">>> >>> 读取【账户系统 一期】交易信息 开始 ... ...");
		try {
			profitService.getAccountOldTransOrderInfos();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(">>> >>> 异常: 读取账户系统交易信息 定时任务, 执行1次/2分钟", e);
		}
		logger.info("<<< <<< 读取【账户系统 一期】交易信息 结束 ... ...");
	}
	/***
	 * 读取账户系统二期交易信息
	 * 执行1次/2分钟
	 */
	public void getAccountTransOrderInfos() {
		logger.info(">>> >>> 读取【账户系统 二期】交易信息 开始 ... ...");
		try {
			profitService.getAccountTransOrderInfos();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(">>> >>> 异常: 读取账户系统交易信息 定时任务, 执行1次/2分钟", e);
		}
		logger.info("<<< <<< 读取【账户系统 二期】交易信息 结束 ... ...");
	}
	/***
	 * 清分定时任务
	 * 执行1次/2分钟
	 */
	public void profitTask() {
		profitService.doProfit();
	}
	/***
	 * 清分结算定时任务
	 * 执行1次/1天
	 */
	public void profitBalanceTask() {
		profitService.doProfitBalance();
	}
	/***
	 * 刷新分润规则
	 * 定时任务, 执行1次/1天
	 * 画面使用
	 */
	public void refreshProfitKeyTask() {
		profitService.refreshProfitKey();
	}
	/***
	 * 刷新'订单号'与存放位置的对应关系
	 * 定时任务, 待定
	 * 画面使用
	 */
	public void refreshFuncCodeAndOrderNoRelationTask() {
		profitService.refreshFuncCodeAndOrderNoRelation();
	}
	/***
	 * 刷新'金额'与存放位置的对应关系
	 * 定时任务, 待定
	 * 画面使用
	 */
	public void refreshFuncCodeAndAmountRelationTask() {
		profitService.refreshFuncCodeAndAmountRelation();
	}
	/***
	 * 读取贷款系统交易信息
	 * 执行1次/2分钟
	 */
	public void getLoanTrans() {
		logger.info(">>> >>> 读取贷款系统交易信息 开始 ... ...");
		try {
			fromLoanLogic.getLoanDetailFromLoan();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(">>> >>> 异常: 读取贷款系统交易信息 定时任务");
		}
		logger.info(">>> >>>  ... ... 读取贷款系统交易信息 结束");
	}
	/***
	 * 清分定时任务
	 * 执行1次/2分钟
	 */
	public void profitForLoanTask() {
		profitFroLoanLogic.doProfit(null);
	}
	/***
	 * 清分结算定时任务
	 * 执行1次/1天
	 */
	public void profitBalanceForLaonTask() {
		try {
			profitFroLoanLogic.doProfigBalance();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(">>> >>> 异常: 还款结算");
		}
	}
	/**
	 * 向待结算表SETTLE_PROFIT_INVOICE插入数据 10:30
	 */
	public void settleInvoiceTask() {
		/*
		 * 结算[生成代付数据]
		 */
		try {
			profitFroLoanLogic.doSettleInvoice(null);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(">>> >>> 异常: 结算[生成代付数据]", e);
		}
		/*
		 * 生成头寸信息
		 */
		try {
			positionService.createPositionInfo();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(">>> >>> 异常: 生成头寸信息", e);
		}
	}
	/***
	 * 查询待结算表SETTLE_PROFIT_INVOICE生成结算[代收付等]交易
	 */
	public void invoiceSettleTask() {
		profitFroLoanLogic.doInvoiceSettle(null);
	}
	/***
	 * 清分定时任务
	 * 执行1次/2分钟
	 */
	public void invoiceSettleForCashTask() {
		profitFroLoanLogic.doInvoiceSettleForCash(null);
	}
	

	/**
	 * 从订单系统读取交易信息并存入'清算'DB
	 * @param accountDate
	 * @return 提示信息
	 */
	public void getPosDetailFromOrdertask() {
		try {
			collateForPosLogic.getTransDetailFromOrder();
		} catch (Exception e) {
			logger.error("异常:pos交易 从订单系统读取交易信息并存入'清算'DB", e);
			e.printStackTrace();
		}
	}
	/***
	 * 还款对账文件下载
	 * @return 提示信息
	 */
	public void rOPFileDownTask(){
		try {
			rOPFileDown.ROPfileDown(78, "", null, 1, "YLSW_PRIVATE_KEY", "txt");
		} catch (Exception e) {
			logger.error("异常:万众还款对账文件下载", e);
			e.printStackTrace();
		}
		try {
			htglRepayDownload.fileDown(null, "M666666");
		} catch (Exception e) {
			logger.error("异常:会唐国旅还款对账文件下载", e);
			e.printStackTrace();
		}
//		try {
//			htglRepayDownload.fileDown(null, "M000033");
//		} catch (Exception e) {
//			logger.error("异常:会唐国旅还款对账文件下载", e);
//			e.printStackTrace();
//		}
	}
	/***
	 * 读取上游对账文件
	 */
	public void readCollateFileTask() {
		try {
			collateService.readCollateFile(Constants.RSQB_ID, SettleConstants.ACCOUNT_TYPE_POS, null, null, SettleConstants.PAY_CHANNEL_ID_REPAY);
		} catch (Exception e) {
			logger.error("异常:pos交易 读取上游对账文件", e);
			e.printStackTrace();
		}
		try {
			collateService.readCollateFile(Constants.RSQB_ID, SettleConstants.ACCOUNT_TYPE_POS_HT, null, null, SettleConstants.PAY_CHANNEL_ID_REPAY);
		} catch (Exception e) {
			logger.error("异常:会唐国旅  读取上游对账文件", e);
			e.printStackTrace();
		}
//		try {
//			collateService.readCollateFile(Constants.RDPT_ID, SettleConstants.ACCOUNT_TYPE_POS_HT, null, null, SettleConstants.PAY_CHANNEL_ID_REPAY);
//		} catch (Exception e) {
//			logger.error("异常:融单平台  读取上游对账文件", e);
//			e.printStackTrace();
//		}
	}
	/***
	 * 对账
	 */
	public void collageForPosTask() {
		try {
			accountService.collageForPos(
					SettleConstants.PAY_CHANNEL_ID_REPAY
					, new String[]{SettleConstants.ACCOUNT_TYPE_POS, SettleConstants.ACCOUNT_TYPE_POS_HT}
					, Constants.RSQB_ID
					, null);
		} catch (Exception e) {
			logger.error("异常:pos交易 对账", e);
			e.printStackTrace();
		}
//		try {
//			accountService.collageForPos(
//					SettleConstants.PAY_CHANNEL_ID_REPAY
//					, new String[]{SettleConstants.ACCOUNT_TYPE_POS_HT}
//					, Constants.RDPT_ID
//					, null);
//		} catch (Exception e) {
//			logger.error("异常:融单交易 对账", e);
//			e.printStackTrace();
//		}
	}
	/***
	 * pos交易结算
	 */
	public void settleForPosTask() {
		try {
			collateForPosLogic.getPosToSettle(new Date());
			
		} catch (Exception e) {
			logger.error("异常:pos交易 结算", e);
			e.printStackTrace();
		}
		profitFroLoanLogic.doProfit(null);
		try {
			profitFroLoanLogic.doProfigBalance();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(">>> >>> 异常: 还款结算");
		}
	}
	
}
