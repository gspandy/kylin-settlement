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
	public int saveSettleRule(SettleRule settleRule) {
		return settleRuleDao.insertSelective(settleRule);
	}
	
	@Override
	public int updateSettleRule(SettleRule settleRule) {
		return settleRuleDao.updateByPrimaryKeySelective(settleRule);
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
	public int deleteSettleRuleById(Long id) {
		return settleRuleDao.deleteByPrimaryKey(id);
	}
	
	@Override
	public int deleteSettleRule(SettleRuleQuery query) {
		return settleRuleDao.deleteByExample(query);
	}

    @Override
    public List<SettleRule> queryPage(SettleRuleQuery query) {
        return settleRuleDao.selectByPreExample(query);
    }

    @Override
    public int countByExample(SettleRuleQuery query) {
        return settleRuleDao.countByExample(query);
    }
}

