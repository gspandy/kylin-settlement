/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.settle.dao;

import java.util.List;

import com.rkylin.settle.pojo.SettleRule;
import com.rkylin.settle.pojo.SettleRuleQuery;

public interface SettleRuleDao {
	int countByExample(SettleRuleQuery example);
	
	int deleteByExample(SettleRuleQuery example);
	
	int deleteByPrimaryKey(Long id);
	
	int insert(SettleRule record);
	
	int insertSelective(SettleRule record);
	
	List<SettleRule> selectByExample(SettleRuleQuery example);
	
	List<SettleRule> selectByStart2EndTime(SettleRuleQuery example);
	
	SettleRule selectByPrimaryKey(Long id);
	
	int updateByPrimaryKeySelective(SettleRule record);
	
	int updateByPrimaryKey(SettleRule record);
}
