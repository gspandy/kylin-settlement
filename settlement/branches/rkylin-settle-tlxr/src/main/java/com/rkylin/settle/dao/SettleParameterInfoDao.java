/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.settle.dao;

import java.util.List;

import com.rkylin.settle.pojo.SettleParameterInfo;
import com.rkylin.settle.pojo.SettleParameterInfoQuery;

public interface SettleParameterInfoDao {
	int countByExample(SettleParameterInfoQuery example);
	
	int deleteByExample(SettleParameterInfoQuery example);
	
	int deleteByPrimaryKey(Long id);
	
	int insert(SettleParameterInfo record);
	
	int insertSelective(SettleParameterInfo record);
	
	List<SettleParameterInfo> selectByExample(SettleParameterInfoQuery example);
	
	SettleParameterInfo selectByPrimaryKey(Long id);
	
	int updateByPrimaryKeySelective(SettleParameterInfo record);
	
	int updateByPrimaryKey(SettleParameterInfo record);
}
