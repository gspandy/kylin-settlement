package com.rkylin.settle.dao;

import java.util.List;

import com.rkylin.settle.pojo.SettleTransSummary;
import com.rkylin.settle.pojo.SettleTransSummaryQuery;


public interface SettleTransSummaryDao {
	
	List<SettleTransSummary> selectByPreExample(SettleTransSummaryQuery example);
	
	int countByExample(SettleTransSummaryQuery example);
}
