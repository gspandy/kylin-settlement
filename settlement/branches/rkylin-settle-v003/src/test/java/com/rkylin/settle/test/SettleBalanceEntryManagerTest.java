/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.settle.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.rkylin.settle.manager.SettleBalanceEntryManager;
import com.rkylin.settle.pojo.SettleBalanceEntry;
import com.rkylin.settle.pojo.SettleBalanceEntryQuery;

public class SettleBalanceEntryManagerTest extends BaseJUnit4Test {
	
	@Autowired
	@Qualifier("settleBalanceEntryManager")
	private SettleBalanceEntryManager settleBalanceEntryManager;


	public void testNewSettleBalanceEntry() {
		SettleBalanceEntry SettleBalanceEntry = new SettleBalanceEntry();
//		SettleBalanceEntry.setAccountingNo("1111111");
		settleBalanceEntryManager.saveSettleBalanceEntry(SettleBalanceEntry);
	}


	public void testUpdateSettleBalanceEntry(){
		SettleBalanceEntry SettleBalanceEntry = new SettleBalanceEntry();
		//SettleBalanceEntry.setId(2l);
//		SettleBalanceEntry.setAccountingNo("222z");
		settleBalanceEntryManager.saveSettleBalanceEntry(SettleBalanceEntry);
	}
	

	public void testDeleteSettleBalanceEntry(){
		settleBalanceEntryManager.deleteSettleBalanceEntryById(99L);
	}
	

	public void testDeleteSettleBalanceEntryByQuery(){
		SettleBalanceEntryQuery query = new SettleBalanceEntryQuery();
//		query.setAccountingNo("1111111");
		settleBalanceEntryManager.deleteSettleBalanceEntry(query);
	}


	public void testFindSettleBalanceEntryById(){
		SettleBalanceEntryQuery query = new SettleBalanceEntryQuery();
//		query.setAccountingNo("1111111");
		int size = settleBalanceEntryManager.queryList(query).size();
		System.out.println(size);
	}
}
