/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.settle.manager;

import java.util.List;
import java.util.Map;

import com.rkylin.settle.pojo.SettleTransProfit;
import com.rkylin.settle.pojo.SettleTransProfitQuery;

public interface SettleTransProfitManager {
	int countByExample(SettleTransProfitQuery query);
	
	void saveSettleTransProfit(SettleTransProfit settleTransProfit);

	void updateSettleTransProfit(SettleTransProfit settleTransProfit);
	
	void updateTransStatusId(Map<String, Object> query);
	
	int updateByIdList(Map<String, Object> query);
	
	SettleTransProfit findSettleTransProfitById(Long id);
	
	List<SettleTransProfit> queryList(SettleTransProfitQuery query);
	
	void deleteSettleTransProfitById(Long id);
	
	void deleteSettleTransProfit(SettleTransProfitQuery query);
	
	List<SettleTransProfit> selectTransProfitWithUnbalance(Map<String, Object> query);
	
	List<SettleTransProfit> selectTransProfitWithDetailId(Map<String, Object> query);
	
	List<SettleTransProfit> selectloanProfitWithUnbalance(Map<String, Object> query);
}
