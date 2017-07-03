/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.settle.dao;

import java.util.List;

import com.rkylin.settle.pojo.TransDetailInfo;
import com.rkylin.settle.pojo.TransDetailInfoQuery;

public interface TransDetailInfoDao {
	int countByExample(TransDetailInfoQuery example);
	
	int deleteByExample(TransDetailInfoQuery example);
	
	int deleteByPrimaryKey(Long id);
	
	int insert(TransDetailInfo record);
	
	int insertSelective(TransDetailInfo record);
	
	List<TransDetailInfo> selectByExample(TransDetailInfoQuery example);
	
	TransDetailInfo selectByPrimaryKey(Long id);
	
	int updateByPrimaryKeySelective(TransDetailInfo record);
	
	int updateByPrimaryKey(TransDetailInfo record);
}
