/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.settle.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.rkylin.settle.dao.SettleRuleDao;
import com.rkylin.settle.pojo.SettleRule;
import com.rkylin.settle.pojo.SettleRuleQuery;
import com.rkylin.database.BaseDao;

@Repository("settleRuleDao")
public class SettleRuleDaoImpl extends BaseDao implements SettleRuleDao {
	
	@Override
	public int countByExample(SettleRuleQuery example) {
		return super.getSqlSession().selectOne("SettleRuleMapper.countByExample", example);
	}
	
	@Override
	public int deleteByExample(SettleRuleQuery example) {
		return super.getSqlSession().delete("SettleRuleMapper.deleteByExample", example);
	}
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return super.getSqlSession().delete("SettleRuleMapper.deleteByPrimaryKey", id);
	}
	
	@Override
	public int insert(SettleRule record) {
		return super.getSqlSession().insert("SettleRuleMapper.insert", record);
	}
	
	@Override
	public int insertSelective(SettleRule record) {
		return super.getSqlSession().insert("SettleRuleMapper.insertSelective", record);
	}
	
	@Override
	public List<SettleRule> selectByExample(SettleRuleQuery example) {
		return super.getSqlSession().selectList("SettleRuleMapper.selectByExample", example);
	}
	
	@Override
	public SettleRule selectByPrimaryKey(Long id) {
		return super.getSqlSession().selectOne("SettleRuleMapper.selectByPrimaryKey", id);
	}
	
	@Override
	public int updateByPrimaryKeySelective(SettleRule record) {
		return super.getSqlSession().update("SettleRuleMapper.updateByPrimaryKeySelective", record);
	}
	
	@Override
	public int updateByPrimaryKey(SettleRule record) {
		return super.getSqlSession().update("SettleRuleMapper.updateByPrimaryKey", record);
	}
	
	@Override
	public List<SettleRule> selectByStart2EndTime(SettleRuleQuery example) {
		return super.getSqlSession().selectList("SettleRuleMapper.selectByStart2EndTime", example);
	}
}
