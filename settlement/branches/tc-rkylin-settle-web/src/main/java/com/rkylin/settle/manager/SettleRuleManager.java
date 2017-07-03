/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.settle.manager;

import java.util.List;

import com.rkylin.settle.pojo.SettleRule;
import com.rkylin.settle.pojo.SettleRuleQuery;

public interface SettleRuleManager {
	int saveSettleRule(SettleRule settleRule);

	int updateSettleRule(SettleRule settleRule);
	
	SettleRule findSettleRuleById(Long id);
	
	List<SettleRule> queryList(SettleRuleQuery query);
	
	public List<SettleRule> queryListByStart2EndTime(SettleRuleQuery query);
	
	int deleteSettleRuleById(Long id);
	
	int deleteSettleRule(SettleRuleQuery query);
	
	List<SettleRule> queryPage(SettleRuleQuery query);
    
    int countByExample(SettleRuleQuery query);
}
