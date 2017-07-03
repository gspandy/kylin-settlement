package com.rkylin.settle.manager;

import java.util.List;
import java.util.Map;

import com.rkylin.settle.pojo.SettleTransInvoice;
import com.rkylin.settle.pojo.SettleTransInvoiceQuery;

public interface SettleTransInvoiceManager {
	
	List<SettleTransInvoice> queryPage(SettleTransInvoiceQuery query);
    
    int countByExample(SettleTransInvoiceQuery query);
    
    SettleTransInvoice findSettleTransInvoiceById(Long id);
    
	int updateSettleTransInvoice(SettleTransInvoice settleTransInvoice);
	
	void saveSettleTransInvoice(SettleTransInvoice settleTransInvoice);
	
	void updateTransStatusId(Map<String, Object> map);
	
	List<SettleTransInvoice> selectByIds(Map<String, Object> example);
	
	void deleteSettleTransInvoiceByInvoiceNo(Long invoiceNo);
	
	long summaryMoneyByExample(SettleTransInvoiceQuery query);
}
