/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.settle.dao;

import java.util.List;

import com.rkylin.settle.pojo.ParameterInfo;
import com.rkylin.settle.pojo.ParameterInfoOld;
import com.rkylin.settle.pojo.ParameterInfoOldQuery;

public interface ParameterInfoOldDao {
	int countByExample(ParameterInfoOldQuery example);
	
	int deleteByExample(ParameterInfoOldQuery example);
	
	int deleteByPrimaryKey(Long id);
	
	int insert(ParameterInfoOld record);
	
	int insertSelective(ParameterInfoOld record);
	
	List<ParameterInfoOld> selectByExample(ParameterInfoOldQuery example);
	
	ParameterInfoOld selectByPrimaryKey(Long id);
	
	int updateByPrimaryKeySelective(ParameterInfoOld record);
	
	int updateByPrimaryKey(ParameterInfoOld record);
}
