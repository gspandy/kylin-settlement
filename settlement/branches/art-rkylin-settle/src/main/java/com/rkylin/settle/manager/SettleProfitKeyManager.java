/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.settle.manager;

import java.util.List;

import com.rkylin.settle.pojo.SettleProfitKey;
import com.rkylin.settle.pojo.SettleProfitKeyQuery;

public interface SettleProfitKeyManager {
	void saveSettleProfitKey(SettleProfitKey settleProfitKey);

	void updateSettleProfitKey(SettleProfitKey settleProfitKey);
	
	SettleProfitKey findSettleProfitKeyById(Long id);
	
	List<SettleProfitKey> queryList(SettleProfitKeyQuery query);
	
	List<SettleProfitKey> selectAllProfitKey(SettleProfitKeyQuery query);
	
	void deleteSettleProfitKeyById(Long id);
	
	void deleteSettleProfitKey(SettleProfitKeyQuery query);
}
