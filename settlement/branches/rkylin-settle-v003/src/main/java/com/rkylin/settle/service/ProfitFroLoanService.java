package com.rkylin.settle.service;

public interface ProfitFroLoanService {
	/**
	 * 贷款款换清分后结算[兼容账户一二期]
	 */
	void doProfigBalance() throws Exception;
	/**
	 * 调用账户系统接口生成结算交易[兼容账户一二期]
	 */
	void doInvoiceSettle() throws Exception;
}
