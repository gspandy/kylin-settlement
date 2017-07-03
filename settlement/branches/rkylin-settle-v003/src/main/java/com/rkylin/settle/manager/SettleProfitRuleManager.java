/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.settle.manager;

import java.util.List;

import com.rkylin.settle.pojo.SettleProfitRule;
import com.rkylin.settle.pojo.SettleProfitRuleQuery;

public interface SettleProfitRuleManager {
	void saveSettleProfitRule(SettleProfitRule settleProfitRule);

	void updateSettleProfitRule(SettleProfitRule settleProfitRule);
	
	SettleProfitRule findSettleProfitRuleById(Long id);
	
	List<SettleProfitRule> queryList(SettleProfitRuleQuery query);
	
	List<SettleProfitRule> selectAllProfitRule(SettleProfitRuleQuery query);
	
	void deleteSettleProfitRuleById(Long id);
	
	void deleteSettleProfitRule(SettleProfitRuleQuery query);
}
