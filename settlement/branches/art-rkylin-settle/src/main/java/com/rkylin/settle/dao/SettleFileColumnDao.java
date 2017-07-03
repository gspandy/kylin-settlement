/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.settle.dao;

import java.util.List;

import com.rkylin.settle.pojo.SettleFileColumn;
import com.rkylin.settle.pojo.SettleFileColumnQuery;

public interface SettleFileColumnDao {
	int countByExample(SettleFileColumnQuery example);
	
	int deleteByExample(SettleFileColumnQuery example);
	
	int deleteByPrimaryKey(Long id);
	
	int insert(SettleFileColumn record);
	
	int insertSelective(SettleFileColumn record);
	
	List<SettleFileColumn> selectByExample(SettleFileColumnQuery example);
	
	SettleFileColumn selectByPrimaryKey(Long id);
	
	int updateByPrimaryKeySelective(SettleFileColumn record);
	
	int updateByPrimaryKey(SettleFileColumn record);
}
