package com.rkylin.settle.dao;

import java.util.List;
import java.util.Map;

import com.rkylin.settle.pojo.SettleTransInvoice;
import com.rkylin.settle.pojo.SettleTransInvoiceQuery;


public interface SettleTransInvoiceDao {
	
	List<SettleTransInvoice> selectByPreExample(SettleTransInvoiceQuery example);
	
	int countByExample(SettleTransInvoiceQuery example);
	
	SettleTransInvoice selectByPrimaryKey(Long id);
	
	int updateByPrimaryKeySelective(SettleTransInvoice record);
	
	int insertSelective(SettleTransInvoice record);
	
	int updateTransStatusId(Map<String, Object> map);
	
	List<SettleTransInvoice> selectByIds(Map<String, Object> example);
	
	int deleteByPrimaryKey(Long invoiceNo);
	
	long summaryMoneyByExample(SettleTransInvoiceQuery example) ;
}
