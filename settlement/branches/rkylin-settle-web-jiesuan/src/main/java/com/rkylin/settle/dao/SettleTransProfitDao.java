/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.settle.dao;

import java.util.List;

import com.rkylin.settle.pojo.SettleTransProfit;
import com.rkylin.settle.pojo.SettleTransProfitQuery;

public interface SettleTransProfitDao {
	int countByExample(SettleTransProfitQuery example);
	
	int deleteByExample(SettleTransProfitQuery example);
	
	int deleteByPrimaryKey(Long id);
	
	int insert(SettleTransProfit record);
	
	int insertSelective(SettleTransProfit record);
	
	List<SettleTransProfit> selectByExample(SettleTransProfitQuery example);
	
	SettleTransProfit selectByPrimaryKey(Long id);
	
	int updateByPrimaryKeySelective(SettleTransProfit record);
	
	int updateByPrimaryKey(SettleTransProfit record);
	
	List<SettleTransProfit> selectByPreExample(SettleTransProfitQuery example);

    int updateStatusByOrderNo(SettleTransProfit record);
}
