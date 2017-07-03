/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2016
 */

package com.rkylin.settle.dao;

import java.util.List;
import java.util.Map;

import com.rkylin.settle.pojo.SettleLoanDetail;
import com.rkylin.settle.pojo.SettleLoanDetailQuery;

public interface SettleLoanDetailDao {
	int countByExample(SettleLoanDetailQuery example);
	
	int deleteByExample(SettleLoanDetailQuery example);
	
	int deleteByPrimaryKey(Long id);
	
	int insert(SettleLoanDetail record);
	
	int insertSelective(SettleLoanDetail record);
	
	List<SettleLoanDetail> selectByExample(SettleLoanDetailQuery example);
	
	SettleLoanDetail selectByPrimaryKey(Long id);
	
	int updateByPrimaryKeySelective(SettleLoanDetail record);
	
	int updateByPrimaryKey(SettleLoanDetail record);
	
	List<Map<String,Object>> selectProfitTransInfo(Map<String, Object> map);
	
	int updateTransStatusId(Map<String, Object> map);
}
