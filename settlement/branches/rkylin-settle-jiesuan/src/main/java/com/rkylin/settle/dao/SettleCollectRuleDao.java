/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2016
 */

package com.rkylin.settle.dao;

import java.util.List;

import com.rkylin.settle.pojo.SettleCollectRule;
import com.rkylin.settle.pojo.SettleCollectRuleQuery;

public interface SettleCollectRuleDao {
	int countByExample(SettleCollectRuleQuery example);
	
	int deleteByExample(SettleCollectRuleQuery example);
	
	int deleteByPrimaryKey(Long id);
	
	int insert(SettleCollectRule record);
	
	int insertSelective(SettleCollectRule record);
	
	List<SettleCollectRule> selectByExample(SettleCollectRuleQuery example);
	
	SettleCollectRule selectByPrimaryKey(Long id);
	
	int updateByPrimaryKeySelective(SettleCollectRule record);
	
	int updateByPrimaryKey(SettleCollectRule record);
}
