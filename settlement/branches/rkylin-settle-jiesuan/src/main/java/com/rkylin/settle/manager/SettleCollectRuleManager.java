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
	void saveSettleCollectRule(SettleCollectRule settleCollectRule);

	SettleCollectRule findSettleCollectRuleById(Long id);
	
	List<SettleCollectRule> queryList(SettleCollectRuleQuery query);
	
	void deleteSettleCollectRuleById(Long id);
	
	void deleteSettleCollectRule(SettleCollectRuleQuery query);
}
