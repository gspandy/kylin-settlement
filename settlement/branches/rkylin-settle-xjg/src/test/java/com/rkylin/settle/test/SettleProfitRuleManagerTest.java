/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.settle.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.rkylin.settle.manager.SettleProfitRuleManager;
import com.rkylin.settle.pojo.SettleProfitRule;
import com.rkylin.settle.pojo.SettleProfitRuleQuery;

public class SettleProfitRuleManagerTest extends BaseJUnit4Test {
	
	@Autowired
	@Qualifier("settleProfitRuleManager")
	private SettleProfitRuleManager settleProfitRuleManager;


	public void testNewSettleProfitRule() {
		SettleProfitRule SettleProfitRule = new SettleProfitRule();
//		SettleProfitRule.setAccountingNo("1111111");
		settleProfitRuleManager.saveSettleProfitRule(SettleProfitRule);
	}


	public void testUpdateSettleProfitRule(){
		SettleProfitRule SettleProfitRule = new SettleProfitRule();
//		SettleProfitRule.setId(2l);
//		SettleProfitRule.setAccountingNo("222z");
		settleProfitRuleManager.saveSettleProfitRule(SettleProfitRule);
	}
	

	public void testDeleteSettleProfitRule(){
		settleProfitRuleManager.deleteSettleProfitRuleById(99L);
	}
	

	public void testDeleteSettleProfitRuleByQuery(){
		SettleProfitRuleQuery query = new SettleProfitRuleQuery();
//		query.setAccountingNo("1111111");
		settleProfitRuleManager.deleteSettleProfitRule(query);
	}


	public void testFindSettleProfitRuleById(){
		SettleProfitRuleQuery query = new SettleProfitRuleQuery();
//		query.setAccountingNo("1111111");
		int size = settleProfitRuleManager.queryList(query).size();
		System.out.println(size);
	}
}
