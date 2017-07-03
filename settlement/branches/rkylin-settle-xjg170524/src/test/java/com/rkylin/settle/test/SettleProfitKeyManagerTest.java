/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.settle.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.rkylin.settle.manager.SettleProfitKeyManager;
import com.rkylin.settle.pojo.SettleProfitKey;
import com.rkylin.settle.pojo.SettleProfitKeyQuery;

public class SettleProfitKeyManagerTest extends BaseJUnit4Test {
	
	@Autowired
	@Qualifier("settleProfitKeyManager")
	private SettleProfitKeyManager settleProfitKeyManager;


	public void testNewSettleProfitKey() {
		SettleProfitKey SettleProfitKey = new SettleProfitKey();
//		SettleProfitKey.setAccountingNo("1111111");
		settleProfitKeyManager.saveSettleProfitKey(SettleProfitKey);
	}


	public void testUpdateSettleProfitKey(){
		SettleProfitKey SettleProfitKey = new SettleProfitKey();
//		SettleProfitKey.setId(2l);
//		SettleProfitKey.setAccountingNo("222z");
		settleProfitKeyManager.saveSettleProfitKey(SettleProfitKey);
	}
	

	public void testDeleteSettleProfitKey(){
		settleProfitKeyManager.deleteSettleProfitKeyById(99L);
	}
	

	public void testDeleteSettleProfitKeyByQuery(){
		SettleProfitKeyQuery query = new SettleProfitKeyQuery();
//		query.setAccountingNo("1111111");
		settleProfitKeyManager.deleteSettleProfitKey(query);
	}


	public void testFindSettleProfitKeyById(){
		SettleProfitKeyQuery query = new SettleProfitKeyQuery();
//		query.setAccountingNo("1111111");
		int size = settleProfitKeyManager.queryList(query).size();
		System.out.println(size);
	}
}
