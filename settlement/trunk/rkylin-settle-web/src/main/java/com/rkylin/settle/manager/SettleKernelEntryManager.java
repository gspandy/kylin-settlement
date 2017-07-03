/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2016
 */

package com.rkylin.settle.manager;

import java.util.List;

import com.rkylin.settle.pojo.SettleKernelEntry;
import com.rkylin.settle.pojo.SettleKernelEntryQuery;

public interface SettleKernelEntryManager {
	int countByExample(SettleKernelEntryQuery example);
	
	int saveSettleKernelEntry(SettleKernelEntry settleKernelEntry);

	int updateSettleKernelEntry(SettleKernelEntry settleKernelEntry);
	
	SettleKernelEntry findSettleKernelEntryById(Long id);
	
	List<SettleKernelEntry> queryList(SettleKernelEntryQuery query);
	
	List<SettleKernelEntry> queryPage(SettleKernelEntryQuery query);
	
	void deleteSettleKernelEntryById(Long id);
	
	void deleteSettleKernelEntry(SettleKernelEntryQuery query);
}
