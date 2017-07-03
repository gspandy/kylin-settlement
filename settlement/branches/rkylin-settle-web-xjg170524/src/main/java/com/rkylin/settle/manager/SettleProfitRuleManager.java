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
	int saveSettleProfitRule(SettleProfitRule settleProfitRule);

	int updateSettleProfitRule(SettleProfitRule settleProfitRule);
	
	SettleProfitRule findSettleProfitRuleById(Long id);
	
	List<SettleProfitRule> queryList(SettleProfitRuleQuery query);
	
	List<SettleProfitRule> selectAllProfitRule(SettleProfitRuleQuery query);
	
	int deleteSettleProfitRule(SettleProfitRuleQuery query);
	
	List<SettleProfitRule> queryPage(SettleProfitRuleQuery query);
    
    int countByExample(SettleProfitRuleQuery query);
    
    List<SettleProfitRule> findByprofitDetailId(String profitDetailId);
    
    int updateProfitDetailIdByMatch(SettleProfitRuleQuery query);
}
