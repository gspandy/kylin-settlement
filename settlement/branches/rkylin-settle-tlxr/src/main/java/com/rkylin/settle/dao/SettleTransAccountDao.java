/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.settle.dao;

import java.util.List;
import java.util.Map;

import com.rkylin.settle.pojo.SettleTransAccount;
import com.rkylin.settle.pojo.SettleTransAccountQuery;

public interface SettleTransAccountDao {
	int countByExample(SettleTransAccountQuery example);
	
	int deleteByExample(SettleTransAccountQuery example);
	
	int deleteByPrimaryKey(Long id);
	
	int insert(SettleTransAccount record);
	
	int insertSelective(SettleTransAccount record);
	
	List<SettleTransAccount> selectByExample(SettleTransAccountQuery example);
	
	SettleTransAccount selectByPrimaryKey(Long id);
	
	int updateByPrimaryKeySelective(SettleTransAccount record);
	
	int updateByPrimaryKey(SettleTransAccount record);
	
	List<Map<String,Object>> selectListMapByExample(SettleTransAccountQuery example);
	
	int updateTransStatusId(Map<String, Object> map);
	
	int updateByMap(Map<String, Object> map);
	
	int insertSelectivebyMap(Map<String,Object> record);
	
	List<Map<String, Object>> selectByOrderNo(Map<String, Object> paramMap);
	
	List<Map<String, Object>> selectTestAmountSum(Map<String, Object> paramMap);
	
	List<Map<String, Object>> selectTestTrans(Map<String, Object> paramMap);
}
