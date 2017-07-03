/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.settle.dao;

import java.util.List;
import java.util.Map;

import com.rkylin.settle.pojo.SettleTransBill;
import com.rkylin.settle.pojo.SettleTransBillQuery;

public interface SettleTransBillDao {
	int countByExample(SettleTransBillQuery example);
	
	int deleteByExample(SettleTransBillQuery example);
	
	int deleteByPrimaryKey(Long id);
	
	int insert(SettleTransBill record);
	
	int insertSelective(SettleTransBill record);
	
	List<SettleTransBill> selectByExample(SettleTransBillQuery example);
	
	List<SettleTransBill> selectOrderByCreatedTime(SettleTransBillQuery example);
	
	SettleTransBill selectByPrimaryKey(Long id);
	
	SettleTransBill selectUniqueBill(SettleTransBillQuery example);
	
	int updateByPrimaryKeySelective(SettleTransBill record);
	
	int updateByPrimaryKey(SettleTransBill record);
	
	List<SettleTransBill> queryloanByCreatedTime(Map<String, Object> example);
}
