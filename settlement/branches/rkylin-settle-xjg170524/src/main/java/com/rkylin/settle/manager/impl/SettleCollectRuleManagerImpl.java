/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2016
 */

package com.rkylin.settle.manager.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.rkylin.settle.dao.SettleCollectRuleDao;
import com.rkylin.settle.manager.SettleCollectRuleManager;
import com.rkylin.settle.pojo.SettleCollectRule;
import com.rkylin.settle.pojo.SettleCollectRuleQuery;

@Component("settleCollectRuleManager")
public class SettleCollectRuleManagerImpl implements SettleCollectRuleManager {
	
	@Autowired
	@Qualifier("settleCollectRuleDao")
	private SettleCollectRuleDao settleCollectRuleDao;
	
	@Override
	public void saveSettleCollectRule(SettleCollectRule settleCollectRule) {
		if (settleCollectRule.getId() == null) {
			settleCollectRuleDao.insertSelective(settleCollectRule);
		} else {
			settleCollectRuleDao.updateByPrimaryKeySelective(settleCollectRule);
		}
	}
	
	@Override
	public SettleCollectRule findSettleCollectRuleById(Long id) {
		return settleCollectRuleDao.selectByPrimaryKey(id);
	}
	
	@Override
	public List<SettleCollectRule> queryList(SettleCollectRuleQuery query) {
		return settleCollectRuleDao.selectByExample(query);
	}
	
	@Override
	public void deleteSettleCollectRuleById(Long id) {
		settleCollectRuleDao.deleteByPrimaryKey(id);
	}
	
	@Override
	public void deleteSettleCollectRule(SettleCollectRuleQuery query) {
		settleCollectRuleDao.deleteByExample(query);
	}
}

