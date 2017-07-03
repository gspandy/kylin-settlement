package com.rkylin.settle.dao;

import java.util.List;

import com.rkylin.settle.pojo.SettleTransSummary;
import com.rkylin.settle.pojo.SettleTransSummaryQuery;

public interface SettleTransSummaryDao {
	int countByExample(SettleTransSummaryQuery example);
	
	int deleteByExample(SettleTransSummaryQuery example);
	
	int deleteByPrimaryKey(Long id);
	
	int insert(SettleTransSummary record);
	
	int insertSelective(SettleTransSummary record);
	
	List<SettleTransSummary> selectByExample(SettleTransSummaryQuery example);
	
	SettleTransSummary selectByPrimaryKey(Long id);
	
	int updateByPrimaryKeySelective(SettleTransSummary record);
	
	int updateByPrimaryKey(SettleTransSummary record);
	
	int batchInsertTransSummery(List<SettleTransSummary> settleTransSummary);
}
