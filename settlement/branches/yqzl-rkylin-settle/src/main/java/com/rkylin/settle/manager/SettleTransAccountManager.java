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

	void updateSettleTransAccount(SettleTransAccount settleTransAccount);
	
	SettleTransAccount findSettleTransAccountById(Long id);
	
	List<SettleTransAccount> queryList(SettleTransAccountQuery query);
	
	void deleteSettleTransAccountById(Long id);
	
	void deleteSettleTransAccount(SettleTransAccountQuery query);
	
	List<Map<String,Object>> queryListMap(SettleTransAccountQuery query);
	
	void updateTransStatusId(Map<String, Object> map);
	
	void updateByMap(Map<String, Object> map);
	
	void insertSelectivebyMap(Map<String, Object> map);
	
	List<Map<String, Object>> selectByOrderNo(Map<String, Object> paramMap);
	
	List<Map<String, Object>> selectTestAmountSum(Map<String, Object> paramMap);
	
	List<Map<String, Object>> selectTestTrans(Map<String, Object> paramMap);
}
