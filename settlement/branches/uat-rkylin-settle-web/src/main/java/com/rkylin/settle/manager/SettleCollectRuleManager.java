/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2016
 */

package com.rkylin.settle.manager;

import java.util.List;

import com.rkylin.settle.pojo.SettleCollectRule;
import com.rkylin.settle.pojo.SettleCollectRuleQuery;

public interface SettleCollectRuleManager {
	Integer countByExample(SettleCollectRuleQuery example);
	
	Integer saveSettleCollectRule(SettleCollectRule settleCollectRule);
	
	Integer updateSettleCollectRule(SettleCollectRule settleCollectRule);
	
	SettleCollectRule findSettleCollectRuleById(Long id);
	
	List<SettleCollectRule> queryList(SettleCollectRuleQuery query);
	
	Integer deleteSettleCollectRuleById(Long id);
	
	Integer deleteSettleCollectRule(SettleCollectRuleQuery query);
	
	List<SettleCollectRule> queryPage(SettleCollectRuleQuery query);
}
