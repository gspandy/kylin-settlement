/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.settle.manager;

import java.util.List;
import java.util.Map;

import com.rkylin.settle.pojo.SettleBalanceEntry;
import com.rkylin.settle.pojo.SettleBalanceEntryQuery;

public interface SettleBalanceEntryManager {
	void saveSettleBalanceEntry(SettleBalanceEntry settleBalanceEntry);

	void updateSettleBalanceEntry(SettleBalanceEntry settleBalanceEntry);
	
	SettleBalanceEntry findSettleBalanceEntryById(Long id);
	
	List<SettleBalanceEntry> queryList(SettleBalanceEntryQuery query);
	
	void deleteSettleBalanceEntryById(Long id);
	
	void deleteSettleBalanceEntry(SettleBalanceEntryQuery query);
	
	void updateByAccIdSelective(Map<String, Object> record);
}
