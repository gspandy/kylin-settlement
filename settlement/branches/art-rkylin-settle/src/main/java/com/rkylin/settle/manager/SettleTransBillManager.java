/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.settle.manager;

import java.util.List;
import java.util.Map;

import com.rkylin.settle.pojo.SettleTransBill;
import com.rkylin.settle.pojo.SettleTransBillQuery;

public interface SettleTransBillManager {
	int countByExample(SettleTransBillQuery example);
	
	void saveSettleTransBill(SettleTransBill settleTransBill);

	void updateSettleTransBill(SettleTransBill settleTransBill);
	
	SettleTransBill findSettleTransBillById(Long id);
	
	List<SettleTransBill> queryList(SettleTransBillQuery query);
	
	List<SettleTransBill> queryOrderByCreatedTime(SettleTransBillQuery query);
	
	SettleTransBill selectUniqueBill(SettleTransBillQuery query);
	
	void deleteSettleTransBillById(Long id);
	
	void deleteSettleTransBill(SettleTransBillQuery query);
	
	List<SettleTransBill> queryloanByCreatedTime(Map<String, Object> example);
}
