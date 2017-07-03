/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.settle.dao;

import java.util.List;
import java.util.Map;

import com.rkylin.settle.pojo.SettleTransDetail;
import com.rkylin.settle.pojo.SettleTransDetailQuery;

public interface SettleTransDetailDao {
	int countByExample(SettleTransDetailQuery example);
	
	int deleteByExample(SettleTransDetailQuery example);
	
	int deleteByPrimaryKey(Long id);
	
	int insert(SettleTransDetail record);
	
	int insertSelective(SettleTransDetail record);
	
	List<SettleTransDetail> selectByExample(SettleTransDetailQuery example);
	
	SettleTransDetail selectByPrimaryKey(Long id);
	
	int updateByPrimaryKeySelective(SettleTransDetail record);
	
	int updateByPrimaryKey(SettleTransDetail record);
	
	List<Map<String,Object>> selectListMapByExample(SettleTransDetailQuery example);
	
	int updateTransStatusId(Map<String, Object> map);
	
	List<Map<String,Object>> selectCollateTransInfo(Map<String, Object> map);
	
	List<Map<String,Object>> selectProfitTransInfo(Map<String, Object> map);
	
	List<SettleTransDetail> selectByPreExample(SettleTransDetailQuery example);
	
	List<SettleTransDetail> selectByIds(Map<String, Object> example);
}
