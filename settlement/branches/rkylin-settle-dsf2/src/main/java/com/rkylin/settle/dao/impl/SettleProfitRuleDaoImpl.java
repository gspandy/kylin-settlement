/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.settle.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.rkylin.settle.dao.SettleProfitRuleDao;
import com.rkylin.settle.pojo.SettleProfitRule;
import com.rkylin.settle.pojo.SettleProfitRuleQuery;
import com.rkylin.database.BaseDao;

@Repository("settleProfitRuleDao")
public class SettleProfitRuleDaoImpl extends BaseDao implements SettleProfitRuleDao {
	
	@Override
	public int countByExample(SettleProfitRuleQuery example) {
		return super.getSqlSession().selectOne("SettleProfitRuleMapper.countByExample", example);
	}
	
	@Override
	public int deleteByExample(SettleProfitRuleQuery example) {
		return super.getSqlSession().delete("SettleProfitRuleMapper.deleteByExample", example);
	}
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return super.getSqlSession().delete("SettleProfitRuleMapper.deleteByPrimaryKey", id);
	}
	
	@Override
	public int insert(SettleProfitRule record) {
		return super.getSqlSession().insert("SettleProfitRuleMapper.insert", record);
	}
	
	@Override
	public int insertSelective(SettleProfitRule record) {
		return super.getSqlSession().insert("SettleProfitRuleMapper.insertSelective", record);
	}
	
	@Override
	public List<SettleProfitRule> selectByExample(SettleProfitRuleQuery example) {
		return super.getSqlSession().selectList("SettleProfitRuleMapper.selectByExample", example);
	}
	
	@Override
	public List<SettleProfitRule> selectAllProfitRule(SettleProfitRuleQuery example) {
		return super.getSqlSession().selectList("SettleProfitRuleMapper.selectAllProfitRule", example);
	}
	
	@Override
	public SettleProfitRule selectByPrimaryKey(Long id) {
		return super.getSqlSession().selectOne("SettleProfitRuleMapper.selectByPrimaryKey", id);
	}
	
	@Override
	public int updateByPrimaryKeySelective(SettleProfitRule record) {
		return super.getSqlSession().update("SettleProfitRuleMapper.updateByPrimaryKeySelective", record);
	}
	
	@Override
	public int updateByPrimaryKey(SettleProfitRule record) {
		return super.getSqlSession().update("SettleProfitRuleMapper.updateByPrimaryKey", record);
	}
	
}
