/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2016
 */

package com.rkylin.settle.dao;

import java.util.List;
import java.util.Map;

import com.rkylin.settle.pojo.OrderInfo;
import com.rkylin.settle.pojo.OrderInfoQuery;

public interface OrderInfoDao {
	int countByExample(OrderInfoQuery example);
	
	int deleteByExample(OrderInfoQuery example);
	
	int deleteByPrimaryKey(Long id);
	
	int insert(OrderInfo record);
	
	int insertSelective(OrderInfo record);
	
	List<OrderInfo> selectByExample(OrderInfoQuery example);
	
	OrderInfo selectByPrimaryKey(Long id);
	
	int updateByPrimaryKeySelective(OrderInfo record);
	
	int updateByPrimaryKey(OrderInfo record);
	
	List<OrderInfo> selectByUpdateTime(Map<String,Object> query);
}
