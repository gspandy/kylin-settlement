/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2016
 */

package com.rkylin.settle.dao;

import java.util.List;

import com.rkylin.settle.pojo.SettleFile;
import com.rkylin.settle.pojo.SettleFileQuery;

public interface SettleFileDao {
	int countByExample(SettleFileQuery example);
	
	int deleteByExample(SettleFileQuery example);
	
	int deleteByPrimaryKey(Long id);
	
	int insert(SettleFile record);
	
	int insertSelective(SettleFile record);
	
	List<SettleFile> selectByExample(SettleFileQuery example);
	
	SettleFile selectByPrimaryKey(Long id);
	
	int updateByPrimaryKeySelective(SettleFile record);
	
	int updateByPrimaryKey(SettleFile record);
}
