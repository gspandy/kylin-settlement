package com.rkylin.settle.dao;

import java.util.List;
import java.util.Map;

import com.rkylin.settle.pojo.SettleTransInvoice;
import com.rkylin.settle.pojo.SettleTransInvoiceQuery;


public interface SettleTransInvoiceDao {
	int countByExample(SettleTransInvoiceQuery example);
	
	int deleteByExample(SettleTransInvoiceQuery example);
	
	int deleteByPrimaryKey(Long id);
	
	int insert(SettleTransInvoice record);
	
	int insertSelective(SettleTransInvoice record);
	
	List<SettleTransInvoice> selectByExample(SettleTransInvoiceQuery example);
	
	SettleTransInvoice selectByPrimaryKey(Long id);
	
	int updateByPrimaryKeySelective(SettleTransInvoice record);
	
	int updateByPrimaryKey(SettleTransInvoice record);
	
	List<SettleTransInvoice> selectTransInvoiceList(Map<String, Object> map);
	
	int queryTotalByExample(SettleTransInvoiceQuery example);
	
	long sumByExample(Map<String, Object> map);
	
	long countByDfExample(Map<String, Object> map);
	
	int removeToHisProc(Map<String, Object> map);
}
