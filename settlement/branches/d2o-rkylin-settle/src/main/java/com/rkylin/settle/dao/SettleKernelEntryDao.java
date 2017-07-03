/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2016
 */

package com.rkylin.settle.dao;

import java.util.List;

import com.rkylin.settle.pojo.SettleKernelEntry;
import com.rkylin.settle.pojo.SettleKernelEntryQuery;

public interface SettleKernelEntryDao {
	int countByExample(SettleKernelEntryQuery example);
	
	int deleteByExample(SettleKernelEntryQuery example);
	
	int deleteByPrimaryKey(Long id);
	
	int insert(SettleKernelEntry record);
	
	int insertSelective(SettleKernelEntry record);
	
	List<SettleKernelEntry> selectByExample(SettleKernelEntryQuery example);
	
	SettleKernelEntry selectByPrimaryKey(Long id);
	
	int updateByPrimaryKeySelective(SettleKernelEntry record);
	
	int updateByPrimaryKey(SettleKernelEntry record);
}
