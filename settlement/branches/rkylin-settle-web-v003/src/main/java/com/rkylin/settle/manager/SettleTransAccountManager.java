/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.settle.manager;

import java.util.List;
import java.util.Map;

import com.rkylin.settle.pojo.SettleTransAccount;
import com.rkylin.settle.pojo.SettleTransAccountQuery;

public interface SettleTransAccountManager {
	void saveSettleTransAccount(SettleTransAccount settleTransAccount);

	int updateSettleTransAccount(SettleTransAccount settleTransAccount);
	
	SettleTransAccount findSettleTransAccountById(Long id);
	
	List<SettleTransAccount> queryList(SettleTransAccountQuery query);
	
	void deleteSettleTransAccountById(Long id);
	
	void deleteSettleTransAccount(SettleTransAccountQuery query);
	
	List<Map<String,Object>> queryListMap(SettleTransAccountQuery query);
	
	void updateTransStatusId(Map<String, Object> map);
	
	void insertSelectivebyMap(Map<String, Object> map);
	
	List<SettleTransAccount> queryPage(SettleTransAccountQuery query);
	
	int countByExample(SettleTransAccountQuery query);
	
	List<SettleTransAccount> summaryList(SettleTransAccountQuery query);
}
