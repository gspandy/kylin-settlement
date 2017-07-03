/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.settle.manager;

import java.util.List;

import com.rkylin.settle.pojo.SettleTransProfit;
import com.rkylin.settle.pojo.SettleTransProfitQuery;

public interface SettleTransProfitManager {
	void saveSettleTransProfit(SettleTransProfit settleTransProfit);

	int updateSettleTransProfit(SettleTransProfit settleTransProfit);
	
	SettleTransProfit findSettleTransProfitById(Long id);
	
	List<SettleTransProfit> queryList(SettleTransProfitQuery query);
	
	void deleteSettleTransProfitById(Long id);
	
	void deleteSettleTransProfit(SettleTransProfitQuery query);
	
	List<SettleTransProfit> queryPage(SettleTransProfitQuery query);
    
    int countByExample(SettleTransProfitQuery query);
    
    int updateStatusByOrderNo(SettleTransProfit settleTransProfit);
}
