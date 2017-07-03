/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2017
 */

package com.rkylin.settle.manager;

import java.util.List;

import com.rkylin.settle.pojo.SettlewebAccountsumCompare;
import com.rkylin.settle.pojo.SettlewebAccountsumCompareQuery;

public interface SettlewebAccountsumCompareManager {
	void saveSettlewebAccountsumCompare(SettlewebAccountsumCompare settlewebAccountsumCompare);

	SettlewebAccountsumCompare findSettlewebAccountsumCompareById(Long id);
	
	List<SettlewebAccountsumCompare> queryList(SettlewebAccountsumCompareQuery query);
	
	void deleteSettlewebAccountsumCompareById(Long id);
	
	void deleteSettlewebAccountsumCompare(SettlewebAccountsumCompareQuery query);
	
	int countByExample(SettlewebAccountsumCompareQuery example);
	
	List<SettlewebAccountsumCompare> queryPage(SettlewebAccountsumCompareQuery query);
}
