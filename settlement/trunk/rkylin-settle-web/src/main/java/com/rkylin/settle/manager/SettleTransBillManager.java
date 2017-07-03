/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.settle.manager;

import java.util.List;

import com.rkylin.settle.pojo.SettleTransBill;
import com.rkylin.settle.pojo.SettleTransBillQuery;

public interface SettleTransBillManager {
	void saveSettleTransBill(SettleTransBill settleTransBill);

	int updateSettleTransBill(SettleTransBill settleTransBill);
	
	SettleTransBill findSettleTransBillById(Long id);
	
	List<SettleTransBill> queryList(SettleTransBillQuery query);
	
	void deleteSettleTransBillById(Long id);
	
	void deleteSettleTransBill(SettleTransBillQuery query);
	
	List<SettleTransBill> queryPage(SettleTransBillQuery query);
    
    int countByExample(SettleTransBillQuery query);
    
    int updateSettleTransBillByOrderNo(SettleTransBill settleTransBill);
}
