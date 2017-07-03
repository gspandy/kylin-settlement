/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2016
 */

package com.rkylin.settle.dao;

import java.util.List;

import com.rkylin.settle.pojo.SettleProfitInvoice;
import com.rkylin.settle.pojo.SettleProfitInvoiceQuery;

public interface SettleProfitInvoiceDao {
	int countByExample(SettleProfitInvoiceQuery example);
	
	int deleteByExample(SettleProfitInvoiceQuery example);
	
	int deleteByPrimaryKey(Long id);
	
	int insert(SettleProfitInvoice record);
	
	int insertSelective(SettleProfitInvoice record);
	
	List<SettleProfitInvoice> selectByExample(SettleProfitInvoiceQuery example);
	
	SettleProfitInvoice selectByPrimaryKey(Long id);
	
	int updateByPrimaryKeySelective(SettleProfitInvoice record);
	
	int updateByPrimaryKey(SettleProfitInvoice record);
	
	List<SettleProfitInvoice> selectInvoiceData(SettleProfitInvoiceQuery example);
}
