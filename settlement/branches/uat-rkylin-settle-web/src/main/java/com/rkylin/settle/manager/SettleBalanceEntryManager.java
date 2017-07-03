/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.settle.manager;

import java.util.List;

import com.rkylin.settle.pojo.SettleBalanceEntry;
import com.rkylin.settle.pojo.SettleBalanceEntryQuery;

public interface SettleBalanceEntryManager {
	int countByExample(SettleBalanceEntryQuery query);
	
	void saveSettleBalanceEntry(SettleBalanceEntry settleBalanceEntry);

	int updateSettleBalanceEntry(SettleBalanceEntry settleBalanceEntry);
	
	SettleBalanceEntry findSettleBalanceEntryById(Long id);
	
	List<SettleBalanceEntry> queryList(SettleBalanceEntryQuery query);
	
	List<SettleBalanceEntry> queryByIds(Long[] ids);
	
	List<SettleBalanceEntry> queryPage(SettleBalanceEntryQuery query);
	
	void deleteSettleBalanceEntryById(Long id);
	
	void deleteSettleBalanceEntry(SettleBalanceEntryQuery query);
}
