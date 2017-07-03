/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.settle.manager;

import java.util.List;
import java.util.Map;

import com.rkylin.settle.pojo.SettleFinanaceAccount;
import com.rkylin.settle.pojo.SettleFinanaceAccountQuery;

public interface SettleFinanaceAccountManager {
	void saveFinanaceAccount(SettleFinanaceAccount finanaceAccount);

	SettleFinanaceAccount findFinanaceAccountById(Long id);
	
	List<SettleFinanaceAccount> queryList(SettleFinanaceAccountQuery query);
	
	public List<SettleFinanaceAccount> selectByFinAccountId(String[] finAccountIds);
	
	void deleteFinanaceAccountById(Long id);
	
	void deleteFinanaceAccount(SettleFinanaceAccountQuery query);

	void updateFinanaceAccount(SettleFinanaceAccount finanaceAccount);
	
	public void updateTransStatusId(Map<String, Object> map);
}
