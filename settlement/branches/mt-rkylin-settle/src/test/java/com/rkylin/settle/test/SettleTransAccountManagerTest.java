/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.settle.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.rkylin.settle.manager.SettleTransAccountManager;
import com.rkylin.settle.pojo.SettleTransAccount;
import com.rkylin.settle.pojo.SettleTransAccountQuery;

public class SettleTransAccountManagerTest extends BaseJUnit4Test {
	
	@Autowired
	@Qualifier("settleTransAccountManager")
	private SettleTransAccountManager settleTransAccountManager;


	public void testNewSettleTransAccount() {
		SettleTransAccount SettleTransAccount = new SettleTransAccount();
//		SettleTransAccount.setAccountingNo("1111111");
		settleTransAccountManager.saveSettleTransAccount(SettleTransAccount);
	}


	public void testUpdateSettleTransAccount(){
		SettleTransAccount SettleTransAccount = new SettleTransAccount();
//		SettleTransAccount.setId(2l);
//		SettleTransAccount.setAccountingNo("222z");
		settleTransAccountManager.saveSettleTransAccount(SettleTransAccount);
	}
	

	public void testDeleteSettleTransAccount(){
		settleTransAccountManager.deleteSettleTransAccountById(99L);
	}
	

	public void testDeleteSettleTransAccountByQuery(){
		SettleTransAccountQuery query = new SettleTransAccountQuery();
//		query.setAccountingNo("1111111");
		settleTransAccountManager.deleteSettleTransAccount(query);
	}


	public void testFindSettleTransAccountById(){
		SettleTransAccountQuery query = new SettleTransAccountQuery();
//		query.setAccountingNo("1111111");
		int size = settleTransAccountManager.queryList(query).size();
		System.out.println(size);
	}
}
