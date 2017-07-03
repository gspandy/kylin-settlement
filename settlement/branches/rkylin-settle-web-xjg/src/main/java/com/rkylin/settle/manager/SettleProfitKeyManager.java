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
	int saveSettleProfitKey(SettleProfitKey settleProfitKey);

	int updateSettleProfitKey(SettleProfitKey settleProfitKey);
	
	SettleProfitKey findSettleProfitKeyById(Long id);
	
	List<SettleProfitKey> queryList(SettleProfitKeyQuery query);
	
	List<SettleProfitKey> selectAllProfitKey(SettleProfitKeyQuery query);
	
	int deleteSettleProfitKeyById(Long id);
	
	void deleteSettleProfitKey(SettleProfitKeyQuery query);
	
	List<SettleProfitKey> queryPage(SettleProfitKeyQuery query);
    
    int countByExample(SettleProfitKeyQuery query);
}
