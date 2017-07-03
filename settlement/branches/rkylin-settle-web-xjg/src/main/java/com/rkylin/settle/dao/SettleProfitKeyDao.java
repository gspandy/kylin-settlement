/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.settle.dao;

import java.util.List;

import com.rkylin.settle.pojo.SettleProfitKey;
import com.rkylin.settle.pojo.SettleProfitKeyQuery;

public interface SettleProfitKeyDao {
	int countByExample(SettleProfitKeyQuery example);
	
	int deleteByExample(SettleProfitKeyQuery example);
	
	int deleteByPrimaryKey(Long id);
	
	int insert(SettleProfitKey record);
	
	int insertSelective(SettleProfitKey record);
	
	List<SettleProfitKey> selectByExample(SettleProfitKeyQuery example);
	
	List<SettleProfitKey> selectAllProfitKey(SettleProfitKeyQuery example);
	
	SettleProfitKey selectByPrimaryKey(Long id);
	
	int updateByPrimaryKeySelective(SettleProfitKey record);
	
	int updateByPrimaryKey(SettleProfitKey record);
	
	List<SettleProfitKey> selectByPreExample(SettleProfitKeyQuery example);
}
