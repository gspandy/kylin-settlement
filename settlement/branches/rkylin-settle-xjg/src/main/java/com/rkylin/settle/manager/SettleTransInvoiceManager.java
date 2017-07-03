package com.rkylin.settle.manager;

import java.util.List;
import java.util.Map;

import com.rkylin.settle.pojo.SettleTransInvoice;
import com.rkylin.settle.pojo.SettleTransInvoiceQuery;

public interface SettleTransInvoiceManager {
	void saveSettleTransInvoice(SettleTransInvoice settleTransInvoice);

	SettleTransInvoice findSettleTransInvoiceById(Long id);
	
	List<SettleTransInvoice> queryList(SettleTransInvoiceQuery query);
	
	void deleteSettleTransInvoiceById(Long id);
	
	void deleteSettleTransInvoice(SettleTransInvoiceQuery query);
	
	void updateSettleTransInvoice(SettleTransInvoice settleTransInvoice);
	
	List<SettleTransInvoice> selectTransInvoiceList(Map<String, Object> map);
	
	int queryTotalByExample(SettleTransInvoiceQuery query);
	
	long sumByExample(Map<String, Object> map);
	
	long countByDfExample(Map<String, Object> map);
	
	int removeToHisProc(Map<String, Object> map);
}
