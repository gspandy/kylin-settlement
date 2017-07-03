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

import com.rkylin.settle.dao.SettleProfitRuleDao;
import com.rkylin.settle.manager.SettleProfitRuleManager;
import com.rkylin.settle.pojo.SettleProfitRule;
import com.rkylin.settle.pojo.SettleProfitRuleQuery;

@Component("settleProfitRuleManager")
public class SettleProfitRuleManagerImpl implements SettleProfitRuleManager {
	
	@Autowired
	@Qualifier("settleProfitRuleDao")
	private SettleProfitRuleDao settleProfitRuleDao;
	
	@Override
	public int saveSettleProfitRule(SettleProfitRule settleProfitRule) {
		return settleProfitRuleDao.insertSelective(settleProfitRule);
	}
	
	@Override
	public int updateSettleProfitRule(SettleProfitRule settleProfitRule) {
		return settleProfitRuleDao.updateByUnionKeySelective(settleProfitRule);
	}
	
	@Override
	public SettleProfitRule findSettleProfitRuleById(Long id) {
		return settleProfitRuleDao.selectByPrimaryKey(id);
	}
	
	@Override
	public List<SettleProfitRule> queryList(SettleProfitRuleQuery query) {
		return settleProfitRuleDao.selectByExample(query);
	}
	
	@Override
	public List<SettleProfitRule> selectAllProfitRule(SettleProfitRuleQuery query) {
		return settleProfitRuleDao.selectAllProfitRule(query);
	}
	
	@Override
	public int deleteSettleProfitRule(SettleProfitRuleQuery query) {
		return settleProfitRuleDao.deleteByExample(query);
	}

    @Override
    public List<SettleProfitRule> queryPage(SettleProfitRuleQuery query) {
        return settleProfitRuleDao.selectByPreExample(query);
    }

    @Override
    public int countByExample(SettleProfitRuleQuery query) {
        return settleProfitRuleDao.countByExample(query);
    }

    @Override
    public List<SettleProfitRule> findByprofitDetailId(String profitDetailId) {
        return settleProfitRuleDao.findByprofitDetailId(profitDetailId);
    }

    @Override
    public int updateProfitDetailIdByMatch(SettleProfitRuleQuery query) {
        return settleProfitRuleDao.updateProfitDetailIdByMatch(query);
    }
}

