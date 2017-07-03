/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2016
 */

package com.rkylin.settle.dao;

import java.util.List;
import java.util.Map;

import com.rkylin.settle.pojo.TransOrderInfo;
import com.rkylin.settle.pojo.TransOrderInfoQuery;

public interface TransOrderInfoDao {
	int countByExample(TransOrderInfoQuery example);
	
	int deleteByExample(TransOrderInfoQuery example);
	
	int deleteByPrimaryKey(Long id);
	
	int insert(TransOrderInfo record);
	
	int insertSelective(TransOrderInfo record);
	
	List<TransOrderInfo> selectByExample(TransOrderInfoQuery example);
	
	TransOrderInfo selectByPrimaryKey(Long id);
	
	int updateByPrimaryKeySelective(TransOrderInfo record);
	
	int updateByPrimaryKey(TransOrderInfo record);
	
	List<TransOrderInfo> selectByRequestTime(Map<String,Object> example);
}
