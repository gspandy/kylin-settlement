/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.settle.manager.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.rkylin.settle.dao.SettleRuleDao;
import com.rkylin.settle.manager.SettleRuleManager;
import com.rkylin.settle.pojo.SettleRule;
import com.rkylin.settle.pojo.SettleRuleQuery;

@Component("settleRuleManager")
public class SettleRuleManagerImpl implements SettleRuleManager {
	
	@Autowired
	@Qualifier("settleRuleDao")
	private SettleRuleDao settleRuleDao;
	
	@Override
	public void saveSettleRule(SettleRule settleRule) {
		settleRuleDao.insertSelective(settleRule);
	}
	
	@Override
	public void updateSettleRule(SettleRule settleRule) {
		settleRuleDao.updateByPrimaryKeySelective(settleRule);
	}
	
	@Override
	public SettleRule findSettleRuleById(Long id) {
		return settleRuleDao.selectByPrimaryKey(id);
	}
	
	@Override
	public List<SettleRule> queryList(SettleRuleQuery query) {
		return settleRuleDao.selectByExample(query);
	}
	
	@Override
	public List<SettleRule> queryListByStart2EndTime(SettleRuleQuery query) {
		return settleRuleDao.selectByStart2EndTime(query);
	}
	
	@Override
	public void deleteSettleRuleById(Long id) {
		settleRuleDao.deleteByPrimaryKey(id);
	}
	
	@Override
	public void deleteSettleRule(SettleRuleQuery query) {
		settleRuleDao.deleteByExample(query);
	}
}

