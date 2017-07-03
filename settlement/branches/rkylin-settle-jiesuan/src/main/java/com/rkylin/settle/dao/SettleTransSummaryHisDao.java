package com.rkylin.settle.dao;

import java.util.List;

import com.rkylin.settle.pojo.SettleTransSummaryHis;
import com.rkylin.settle.pojo.SettleTransSummaryHisQuery;

public interface SettleTransSummaryHisDao {
	int countByExample(SettleTransSummaryHisQuery example);
	
	int deleteByExample(SettleTransSummaryHisQuery example);
	
	int deleteByPrimaryKey(Long id);
	
	int insert(SettleTransSummaryHis record);
	
	int insertSelective(SettleTransSummaryHis record);
	
	List<SettleTransSummaryHis> selectByExample(SettleTransSummaryHisQuery example);
	
	SettleTransSummaryHis selectByPrimaryKey(Long id);
	
	int updateByPrimaryKeySelective(SettleTransSummaryHis record);
	
	int updateByPrimaryKey(SettleTransSummaryHis record);
}
