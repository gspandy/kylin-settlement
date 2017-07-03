package com.rkylin.settle.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rkylin.settle.logic.ProfitFroLoanLogic;
import com.rkylin.settle.logic.ProfitFroLoanLogic2;
import com.rkylin.settle.service.ProfitFroLoanService;

@Service("profitFroLoanService")
public class ProfitFroLoanServiceImpl implements ProfitFroLoanService {
	//日志对象
	protected static Logger logger = LoggerFactory.getLogger(ProfitFroLoanServiceImpl.class);
	@Autowired
	private ProfitFroLoanLogic profitFroLoanLogic;	//账户一期结算逻辑类
	@Autowired
	private ProfitFroLoanLogic2 profitFroLoanLogic2;	//账户二期结算逻辑类
	
	@Override
	public void doProfigBalance() throws Exception {
		logger.info(">>> >>> >>> >>> 开始:贷款款换清分后结算[兼容账户一,二期]");
		try {
			profitFroLoanLogic.doProfigBalance();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("异常:结算分润结果 [调用账户一期系统接口]", e);
		}
		try {
			profitFroLoanLogic2.doProfigBalance();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("异常:结算分润结果 [调用账户二期系统接口]", e);
		}
		logger.info("<<< <<< <<< <<< 结束:贷款款换清分后结算[兼容账户一,二期]");
	}
	
	/**
	 * 调用账户系统接口生成结算交易[兼容账户一二期]
	 */
	@Override
	public void doInvoiceSettle() throws Exception {
		logger.info(">>> >>> >>> >>> 开始:调用账户系统接口生成结算交易[兼容账户一二期]");
		try {
			profitFroLoanLogic.doInvoiceSettle(null);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("异常:调用账户系统接口生成结算交易 [调用账户一期系统接口]", e);
		}
		try {
			profitFroLoanLogic2.doInvoiceSettle(null);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("异常:调用账户系统接口生成结算交易 [调用账户二期系统接口]", e);
		}
		logger.info("<<< <<< <<< <<< 结束:调用账户系统接口生成结算交易[兼容账户一二期]");
	}
}
