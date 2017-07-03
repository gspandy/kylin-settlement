/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.settle.dao;

import java.util.List;

import com.rkylin.settle.pojo.SettleTransBill;
import com.rkylin.settle.pojo.SettleTransBillQuery;

public interface SettleTransBillDao {
	int countByExample(SettleTransBillQuery example);
	
	int deleteByExample(SettleTransBillQuery example);
	
	int deleteByPrimaryKey(Long id);
	
	int insert(SettleTransBill record);
	
	int insertSelective(SettleTransBill record);
	
	List<SettleTransBill> selectByExample(SettleTransBillQuery example);
	
	SettleTransBill selectByPrimaryKey(Long id);
	
	int updateByPrimaryKeySelective(SettleTransBill record);
	
	int updateByPrimaryKey(SettleTransBill record);
	
	List<SettleTransBill> selectByPreExample(SettleTransBillQuery example);

    int updateSettleTransBillByOrderNo(SettleTransBill record);
}
