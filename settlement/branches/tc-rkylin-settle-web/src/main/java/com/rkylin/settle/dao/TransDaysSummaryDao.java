/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.settle.dao;

import java.util.List;
import java.util.Map;

import com.rkylin.settle.pojo.TransDaysSummary;
import com.rkylin.settle.pojo.TransDaysSummaryQuery;

public interface TransDaysSummaryDao {
	int countByExample(TransDaysSummaryQuery example);
	
	int deleteByExample(TransDaysSummaryQuery example);
	
	int deleteByPrimaryKey(Long id);
	
	int insert(TransDaysSummary record);
	
	int insertSelective(TransDaysSummary record);
	
	List<TransDaysSummary> selectByExample(TransDaysSummaryQuery example);
	
	TransDaysSummary selectByPrimaryKey(String id);
	
	int updateByPrimaryKeySelective(TransDaysSummary record);
	
	int updateByPrimaryKey(TransDaysSummary record);
	
	List<Map<String,Object>> findDateAll(Map<String,String> query);
}
