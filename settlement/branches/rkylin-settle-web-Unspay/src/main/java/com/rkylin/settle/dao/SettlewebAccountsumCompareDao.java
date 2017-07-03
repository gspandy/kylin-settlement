/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2017
 */

package com.rkylin.settle.dao;

import java.util.List;

import com.rkylin.settle.pojo.SettlewebAccountsumCompare;
import com.rkylin.settle.pojo.SettlewebAccountsumCompareQuery;
import com.rkylin.settle.util.PagerModel;

public interface SettlewebAccountsumCompareDao {
	int countByExample(SettlewebAccountsumCompareQuery example);
	
	int deleteByExample(SettlewebAccountsumCompareQuery example);
	
	int deleteByPrimaryKey(Long id);
	
	int insert(SettlewebAccountsumCompare record);
	
	int insertSelective(SettlewebAccountsumCompare record);
	
	List<SettlewebAccountsumCompare> selectByExample(SettlewebAccountsumCompareQuery example);
	
	SettlewebAccountsumCompare selectByPrimaryKey(Long id);
	
	int updateByPrimaryKeySelective(SettlewebAccountsumCompare record);
	
	int updateByPrimaryKey(SettlewebAccountsumCompare record);
	
	List<SettlewebAccountsumCompare> selectByPreExample(SettlewebAccountsumCompareQuery example);
}
