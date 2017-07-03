/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.settle.dao;

import java.util.List;
import java.util.Map;

import com.rkylin.settle.pojo.SettleBalanceEntry;
import com.rkylin.settle.pojo.SettleBalanceEntryQuery;

public interface SettleBalanceEntryDao {
	int countByExample(SettleBalanceEntryQuery example);
	
	int deleteByExample(SettleBalanceEntryQuery example);
	
	int deleteByPrimaryKey(Long id);
	
	int insert(SettleBalanceEntry record);
	
	int insertSelective(SettleBalanceEntry record);
	
	List<SettleBalanceEntry> selectByExample(SettleBalanceEntryQuery example);
	
	SettleBalanceEntry selectByPrimaryKey(Long id);
	
	int updateByPrimaryKeySelective(SettleBalanceEntry record);
	
	int updateByPrimaryKey(SettleBalanceEntry record);
	
	int updateByAccIdSelective(Map<String,Object> record);
}
