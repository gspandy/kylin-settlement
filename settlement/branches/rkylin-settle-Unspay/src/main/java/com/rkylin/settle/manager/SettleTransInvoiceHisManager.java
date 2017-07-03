package com.rkylin.settle.manager;

import java.util.List;
import java.util.Map;

import com.rkylin.settle.pojo.SettleTransInvoiceHis;
import com.rkylin.settle.pojo.SettleTransInvoiceHisQuery;

public interface SettleTransInvoiceHisManager {
	void saveSettleTransInvoiceHis(SettleTransInvoiceHis SettleTransInvoiceHis);

	SettleTransInvoiceHis findSettleTransInvoiceHisById(Long id);
	
	List<SettleTransInvoiceHis> queryList(SettleTransInvoiceHisQuery query);
	
	void deleteSettleTransInvoiceHisById(Long id);
	
	void deleteSettleTransInvoiceHis(SettleTransInvoiceHisQuery query);
	
	void updateSettleTransInvoiceHis(SettleTransInvoiceHis SettleTransInvoiceHis);
	
	List<SettleTransInvoiceHis> selectTransInvoiceList(Map<String, Object> map);
	
	int queryTotalByExample(SettleTransInvoiceHisQuery query);
	
	long sumByExample(Map<String, Object> map);
	
	int removeToHisProc(Map<String, Object> map);
}
