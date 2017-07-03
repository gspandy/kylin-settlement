/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.settle.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.rkylin.settle.common.AccountOldBaseDao;
import com.rkylin.settle.dao.TransDaysSummaryDao;
import com.rkylin.settle.pojo.TransDaysSummary;
import com.rkylin.settle.pojo.TransDaysSummaryQuery;

@Repository("transDaysSummaryDao")
public class TransDaysSummaryDaoImpl extends AccountOldBaseDao implements TransDaysSummaryDao {
	
	@Override
	public int countByExample(TransDaysSummaryQuery example) {
		return super.getSqlSession().selectOne("TransDaysSummaryMapper.countByExample", example);
	}
	
	@Override
	public int deleteByExample(TransDaysSummaryQuery example) {
		return super.getSqlSession().delete("TransDaysSummaryMapper.deleteByExample", example);
	}
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return super.getSqlSession().delete("TransDaysSummaryMapper.deleteByPrimaryKey", id);
	}
	
	@Override
	public int insert(TransDaysSummary record) {
		return super.getSqlSession().insert("TransDaysSummaryMapper.insert", record);
	}
	
	@Override
	public int insertSelective(TransDaysSummary record) {
		return super.getSqlSession().insert("TransDaysSummaryMapper.insertSelective", record);
	}
	
	@Override
	public List<TransDaysSummary> selectByExample(TransDaysSummaryQuery example) {
		return super.getSqlSession().selectList("TransDaysSummaryMapper.selectByExample", example);
	}
	
	@Override
	public TransDaysSummary selectByPrimaryKey(String id) {
		return super.getSqlSession().selectOne("TransDaysSummaryMapper.selectByPrimaryKey", id);
	}
	
	@Override
	public int updateByPrimaryKeySelective(TransDaysSummary record) {
		return super.getSqlSession().update("TransDaysSummaryMapper.updateByPrimaryKeySelective", record);
	}
	
	@Override
	public int updateByPrimaryKey(TransDaysSummary record) {
		return super.getSqlSession().update("TransDaysSummaryMapper.updateByPrimaryKey", record);
	}

	@Override
	public List<Map<String,Object>> findDateAll(Map<String,String> query) {
		return super.getSqlSession().selectList("TransDaysSummaryMapper.findDateAll", query);
	}
}
