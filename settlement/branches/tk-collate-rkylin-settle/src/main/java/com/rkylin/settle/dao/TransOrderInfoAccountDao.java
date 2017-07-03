/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.settle.dao;

import java.util.List;
import java.util.Map;

import com.rkylin.settle.pojo.TransOrderInfo;
import com.rkylin.settle.pojo.TransOrderInfoAccount;
import com.rkylin.settle.pojo.TransOrderInfoAccountQuery;

public interface TransOrderInfoAccountDao {
	int countByExample(TransOrderInfoAccountQuery example);
	
	int deleteByExample(TransOrderInfoAccountQuery example);
	
	int deleteByPrimaryKey(Long id);
	
	int insert(TransOrderInfoAccount  record);
	
	int insertSelective(TransOrderInfoAccount  record);
	
	List<TransOrderInfoAccount> selectByExample(TransOrderInfoAccountQuery example);
	
	TransOrderInfoAccount selectByPrimaryKey(Long id);
	
	int updateByPrimaryKeySelective(TransOrderInfoAccount  record);
	
	int updateByPrimaryKey(TransOrderInfoAccount  record);
	
	List<TransOrderInfoAccount> selectByRequestTime(Map<String,Object> example);
}
