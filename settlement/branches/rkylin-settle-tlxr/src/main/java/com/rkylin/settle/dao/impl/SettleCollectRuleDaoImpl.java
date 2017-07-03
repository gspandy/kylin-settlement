/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2016
 */

package com.rkylin.settle.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.rkylin.database.BaseDao;
import com.rkylin.settle.dao.SettleCollectRuleDao;
import com.rkylin.settle.pojo.SettleCollectRule;
import com.rkylin.settle.pojo.SettleCollectRuleQuery;

@Repository("settleCollectRuleDao")
public class SettleCollectRuleDaoImpl extends BaseDao implements SettleCollectRuleDao {
	
	@Override
	public int countByExample(SettleCollectRuleQuery example) {
		return super.getSqlSession().selectOne("SettleCollectRuleMapper.countByExample", example);
	}
	
	@Override
	public int deleteByExample(SettleCollectRuleQuery example) {
		return super.getSqlSession().delete("SettleCollectRuleMapper.deleteByExample", example);
	}
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return super.getSqlSession().delete("SettleCollectRuleMapper.deleteByPrimaryKey", id);
	}
	
	@Override
	public int insert(SettleCollectRule record) {
		return super.getSqlSession().insert("SettleCollectRuleMapper.insert", record);
	}
	
	@Override
	public int insertSelective(SettleCollectRule record) {
		return super.getSqlSession().insert("SettleCollectRuleMapper.insertSelective", record);
	}
	
	@Override
	public List<SettleCollectRule> selectByExample(SettleCollectRuleQuery example) {
		return super.getSqlSession().selectList("SettleCollectRuleMapper.selectByExample", example);
	}
	
	@Override
	public SettleCollectRule selectByPrimaryKey(Long id) {
		return super.getSqlSession().selectOne("SettleCollectRuleMapper.selectByPrimaryKey", id);
	}
	
	@Override
	public int updateByPrimaryKeySelective(SettleCollectRule record) {
		return super.getSqlSession().update("SettleCollectRuleMapper.updateByPrimaryKeySelective", record);
	}
	
	@Override
	public int updateByPrimaryKey(SettleCollectRule record) {
		return super.getSqlSession().update("SettleCollectRuleMapper.updateByPrimaryKey", record);
	}
	
}
