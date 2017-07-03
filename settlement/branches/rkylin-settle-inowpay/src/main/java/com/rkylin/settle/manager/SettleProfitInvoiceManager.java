/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2016
 */

package com.rkylin.settle.manager;

import java.util.List;

import com.rkylin.settle.pojo.SettleProfitInvoice;
import com.rkylin.settle.pojo.SettleProfitInvoiceQuery;

public interface SettleProfitInvoiceManager {
	void saveSettleProfitInvoice(SettleProfitInvoice settleProfitInvoice);

	void updateSettleProfitInvoice(SettleProfitInvoice settleProfitInvoice);
	
	SettleProfitInvoice findSettleProfitInvoiceById(Long id);
	
	List<SettleProfitInvoice> queryList(SettleProfitInvoiceQuery query);
	
	void deleteSettleProfitInvoiceById(Long id);
	
	void deleteSettleProfitInvoice(SettleProfitInvoiceQuery query);
	
	List<SettleProfitInvoice> selectInvoiceData(SettleProfitInvoiceQuery example);
	
	int countByExample(SettleProfitInvoiceQuery example);
}
