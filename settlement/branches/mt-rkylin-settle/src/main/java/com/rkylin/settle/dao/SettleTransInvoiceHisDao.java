package com.rkylin.settle.dao;

import java.util.List;
import java.util.Map;

import com.rkylin.settle.pojo.SettleTransInvoiceHis;
import com.rkylin.settle.pojo.SettleTransInvoiceHisQuery;


public interface SettleTransInvoiceHisDao {
	int countByExample(SettleTransInvoiceHisQuery example);
	
	int deleteByExample(SettleTransInvoiceHisQuery example);
	
	int deleteByPrimaryKey(Long id);
	
	int insert(SettleTransInvoiceHis record);
	
	int insertSelective(SettleTransInvoiceHis record);
	
	List<SettleTransInvoiceHis> selectByExample(SettleTransInvoiceHisQuery example);
	
	SettleTransInvoiceHis selectByPrimaryKey(Long id);
	
	int updateByPrimaryKeySelective(SettleTransInvoiceHis record);
	
	int updateByPrimaryKey(SettleTransInvoiceHis record);
	
	List<SettleTransInvoiceHis> selectTransInvoiceList(Map<String, Object> map);
	
	int queryTotalByExample(SettleTransInvoiceHisQuery example);
	
	long sumByExample(Map<String, Object> map);
	
	int removeToHisProc(Map<String, Object> map);
}
