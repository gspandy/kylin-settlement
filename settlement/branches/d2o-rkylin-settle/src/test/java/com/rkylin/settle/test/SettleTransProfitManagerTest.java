/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.settle.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.rkylin.settle.manager.SettleTransProfitManager;
import com.rkylin.settle.pojo.SettleTransProfit;
import com.rkylin.settle.pojo.SettleTransProfitQuery;

public class SettleTransProfitManagerTest extends BaseJUnit4Test {
	
	@Autowired
	@Qualifier("settleTransProfitManager")
	private SettleTransProfitManager settleTransProfitManager;


	public void testNewSettleTransProfit() {
		SettleTransProfit SettleTransProfit = new SettleTransProfit();
//		SettleTransProfit.setAccountingNo("1111111");
		settleTransProfitManager.saveSettleTransProfit(SettleTransProfit);
	}


	public void testUpdateSettleTransProfit(){
		SettleTransProfit SettleTransProfit = new SettleTransProfit();
//		SettleTransProfit.setId(2l);
//		SettleTransProfit.setAccountingNo("222z");
		settleTransProfitManager.saveSettleTransProfit(SettleTransProfit);
	}
	

	public void testDeleteSettleTransProfit(){
		settleTransProfitManager.deleteSettleTransProfitById(99L);
	}
	

	public void testDeleteSettleTransProfitByQuery(){
		SettleTransProfitQuery query = new SettleTransProfitQuery();
//		query.setAccountingNo("1111111");
		settleTransProfitManager.deleteSettleTransProfit(query);
	}


	public void testFindSettleTransProfitById(){
		SettleTransProfitQuery query = new SettleTransProfitQuery();
//		query.setAccountingNo("1111111");
		int size = settleTransProfitManager.queryList(query).size();
		System.out.println(size);
	}
}
