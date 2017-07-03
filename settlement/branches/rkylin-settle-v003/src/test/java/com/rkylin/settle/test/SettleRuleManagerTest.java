/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.settle.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.rkylin.settle.manager.SettleRuleManager;
import com.rkylin.settle.pojo.SettleRule;
import com.rkylin.settle.pojo.SettleRuleQuery;

public class SettleRuleManagerTest extends BaseJUnit4Test {
	
	@Autowired
	@Qualifier("settleRuleManager")
	private SettleRuleManager settleRuleManager;


	public void testNewSettleRule() {
		SettleRule SettleRule = new SettleRule();
//		SettleRule.setAccountingNo("1111111");
		settleRuleManager.saveSettleRule(SettleRule);
	}


	public void testUpdateSettleRule(){
		SettleRule SettleRule = new SettleRule();
		SettleRule.setRuleId(2);
//		SettleRule.setAccountingNo("222z");
		settleRuleManager.saveSettleRule(SettleRule);
	}
	

	public void testDeleteSettleRule(){
		settleRuleManager.deleteSettleRuleById(99L);
	}
	

	public void testDeleteSettleRuleByQuery(){
		SettleRuleQuery query = new SettleRuleQuery();
//		query.setAccountingNo("1111111");
		settleRuleManager.deleteSettleRule(query);
	}


	public void testFindSettleRuleById(){
		SettleRuleQuery query = new SettleRuleQuery();
//		query.setAccountingNo("1111111");
		int size = settleRuleManager.queryList(query).size();
		System.out.println(size);
	}
}
