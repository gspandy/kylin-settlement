/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.settle.dao;

import java.util.List;
import java.util.Map;

import com.rkylin.settle.pojo.SettleFinanaceAccount;
import com.rkylin.settle.pojo.SettleFinanaceAccountQuery;

public interface SettleFinanaceAccountDao {
	int countByExample(SettleFinanaceAccountQuery example);
	
	int deleteByExample(SettleFinanaceAccountQuery example);
	
	int deleteByPrimaryKey(Long id);
	
	int insert(SettleFinanaceAccount record);
	
	int insertSelective(SettleFinanaceAccount record);
	
	List<SettleFinanaceAccount> selectByExample(SettleFinanaceAccountQuery example);
	
	public List<SettleFinanaceAccount> selectByFinAccountId(String[] finAccountIds);
	
	SettleFinanaceAccount selectByPrimaryKey(Long id);
	
	int updateByPrimaryKeySelective(SettleFinanaceAccount record);
	
	int updateByPrimaryKey(SettleFinanaceAccount record);
	
	//根据机构号和用户id查询其所有产品号
	List<String> selectProductIdList(SettleFinanaceAccountQuery example);
	
	public int  updateTransStatusId(Map<String, Object> map);
}
