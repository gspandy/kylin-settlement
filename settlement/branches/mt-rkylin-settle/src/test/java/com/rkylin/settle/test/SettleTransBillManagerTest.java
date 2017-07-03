/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.settle.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.rkylin.settle.manager.SettleTransBillManager;
import com.rkylin.settle.pojo.SettleTransBill;
import com.rkylin.settle.pojo.SettleTransBillQuery;

public class SettleTransBillManagerTest extends BaseJUnit4Test {
	
	@Autowired
	@Qualifier("settleTransBillManager")
	private SettleTransBillManager settleTransBillManager;


	public void testNewSettleTransBill() {
		SettleTransBill SettleTransBill = new SettleTransBill();
//		SettleTransBill.setAccountingNo("1111111");
		settleTransBillManager.saveSettleTransBill(SettleTransBill);
	}


	public void testUpdateSettleTransBill(){
		SettleTransBill SettleTransBill = new SettleTransBill();
//		SettleTransBill.setId(2l);
//		SettleTransBill.setAccountingNo("222z");
		settleTransBillManager.saveSettleTransBill(SettleTransBill);
	}
	

	public void testDeleteSettleTransBill(){
		settleTransBillManager.deleteSettleTransBillById(99L);
	}
	

	public void testDeleteSettleTransBillByQuery(){
		SettleTransBillQuery query = new SettleTransBillQuery();
//		query.setAccountingNo("1111111");
		settleTransBillManager.deleteSettleTransBill(query);
	}


	public void testFindSettleTransBillById(){
		SettleTransBillQuery query = new SettleTransBillQuery();
//		query.setAccountingNo("1111111");
		int size = settleTransBillManager.queryList(query).size();
		System.out.println(size);
	}
}
