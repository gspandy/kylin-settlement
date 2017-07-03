package com.rkylin.settle.dao.impl;


import java.util.List;

import org.springframework.stereotype.Repository;

import com.rkylin.database.BaseDao;
import com.rkylin.settle.dao.SettleTransSummaryDao;
import com.rkylin.settle.pojo.SettleTransSummary;
import com.rkylin.settle.pojo.SettleTransSummaryQuery;


@Repository("settleTransSummaryDao")
public class SettleTransSummaryDaoImpl extends BaseDao implements SettleTransSummaryDao {
	
	@Override
	public int countByExample(SettleTransSummaryQuery example) {
		return super.getSqlSession().selectOne("SettleTransSummaryMapper.countByExample", example);
	}
	
	@Override
	public int deleteByExample(SettleTransSummaryQuery example) {
		return super.getSqlSession().delete("SettleTransSummaryMapper.deleteByExample", example);
	}
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return super.getSqlSession().delete("SettleTransSummaryMapper.deleteByPrimaryKey", id);
	}
	
	@Override
	public int insert(SettleTransSummary record) {
		return super.getSqlSession().insert("SettleTransSummaryMapper.insert", record);
	}
	
	@Override
	public int insertSelective(SettleTransSummary record) {
		return super.getSqlSession().insert("SettleTransSummaryMapper.insertSelective", record);
	}
	
	@Override
	public List<SettleTransSummary> selectByExample(SettleTransSummaryQuery example) {
		return super.getSqlSession().selectList("SettleTransSummaryMapper.selectByExample", example);
	}
	
	@Override
	public SettleTransSummary selectByPrimaryKey(Long id) {
		return super.getSqlSession().selectOne("SettleTransSummaryMapper.selectByPrimaryKey", id);
	}
	
	@Override
	public int updateByPrimaryKeySelective(SettleTransSummary record) {
		return super.getSqlSession().update("SettleTransSummaryMapper.updateByPrimaryKeySelective", record);
	}
	
	@Override
	public int updateByPrimaryKey(SettleTransSummary record) {
		return super.getSqlSession().update("SettleTransSummaryMapper.updateByPrimaryKey", record);
	}

	@Override
	public int batchInsertTransSummery(List<SettleTransSummary> settleTransSummary) {
		return super.getSqlSession().insert("SettleTransSummaryMapper.batchInsertTransSummery", settleTransSummary);
	}
}
