/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.settle.dao;

import java.util.List;
import java.util.Map;

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
	
	//分润结束,更新分润结果表的状态
	int updateTransStatusId(Map<String, Object> example);
	
	//批量更新
	int updateByIdList(Map<String, Object> example);
	
	//关联查询 trans_detail 查询待结算的分润结果
	List<SettleTransProfit> selectTransProfitWithUnbalance(Map<String, Object> example);
	
	//通过交易信息表的ID 查询对应的分润规则 
	List<SettleTransProfit> selectTransProfitWithDetailId(Map<String, Object> example);
	
	List<SettleTransProfit> selectloanProfitWithUnbalance(Map<String, Object> example);
}
