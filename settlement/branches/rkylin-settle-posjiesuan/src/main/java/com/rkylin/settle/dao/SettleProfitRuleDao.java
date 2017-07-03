/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.settle.dao;

import java.util.List;

import com.rkylin.settle.pojo.SettleProfitRule;
import com.rkylin.settle.pojo.SettleProfitRuleQuery;

public interface SettleProfitRuleDao {
	int countByExample(SettleProfitRuleQuery example);
	
	int deleteByExample(SettleProfitRuleQuery example);
	
	int deleteByPrimaryKey(Long id);
	
	int insert(SettleProfitRule record);
	
	int insertSelective(SettleProfitRule record);
	
	List<SettleProfitRule> selectByExample(SettleProfitRuleQuery example);
	
	List<SettleProfitRule> selectAllProfitRule(SettleProfitRuleQuery example);
	
	SettleProfitRule selectByPrimaryKey(Long id);
	
	int updateByPrimaryKeySelective(SettleProfitRule record);
	
	int updateByPrimaryKey(SettleProfitRule record);
}
