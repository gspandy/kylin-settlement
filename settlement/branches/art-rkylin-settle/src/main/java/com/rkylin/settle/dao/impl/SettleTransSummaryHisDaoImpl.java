package com.rkylin.settle.dao.impl;


import java.util.List;

import org.springframework.stereotype.Repository;

import com.rkylin.database.BaseDao;
import com.rkylin.settle.dao.SettleTransSummaryHisDao;
import com.rkylin.settle.pojo.SettleTransSummaryHis;
import com.rkylin.settle.pojo.SettleTransSummaryHisQuery;


@Repository("settleTransSummaryHisDao")
public class SettleTransSummaryHisDaoImpl extends BaseDao implements SettleTransSummaryHisDao {
	
	@Override
	public int countByExample(SettleTransSummaryHisQuery example) {
		return super.getSqlSession().selectOne("SettleTransSummaryHisMapper.countByExample", example);
	}
	
	@Override
	public int deleteByExample(SettleTransSummaryHisQuery example) {
		return super.getSqlSession().delete("SettleTransSummaryHisMapper.deleteByExample", example);
	}
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return super.getSqlSession().delete("SettleTransSummaryHisMapper.deleteByPrimaryKey", id);
	}
	
	@Override
	public int insert(SettleTransSummaryHis record) {
		return super.getSqlSession().insert("SettleTransSummaryHisMapper.insert", record);
	}
	
	@Override
	public int insertSelective(SettleTransSummaryHis record) {
		return super.getSqlSession().insert("SettleTransSummaryHisMapper.insertSelective", record);
	}
	
	@Override
	public List<SettleTransSummaryHis> selectByExample(SettleTransSummaryHisQuery example) {
		return super.getSqlSession().selectList("SettleTransSummaryHisMapper.selectByExample", example);
	}
	
	@Override
	public SettleTransSummaryHis selectByPrimaryKey(Long id) {
		return super.getSqlSession().selectOne("SettleTransSummaryHisMapper.selectByPrimaryKey", id);
	}
	
	@Override
	public int updateByPrimaryKeySelective(SettleTransSummaryHis record) {
		return super.getSqlSession().update("SettleTransSummaryHisMapper.updateByPrimaryKeySelective", record);
	}
	
	@Override
	public int updateByPrimaryKey(SettleTransSummaryHis record) {
		return super.getSqlSession().update("SettleTransSummaryHisMapper.updateByPrimaryKey", record);
	}
	
}
