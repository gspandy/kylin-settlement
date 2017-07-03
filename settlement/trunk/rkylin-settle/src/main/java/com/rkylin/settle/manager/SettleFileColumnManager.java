/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.settle.manager;

import java.util.List;

import com.rkylin.settle.pojo.SettleFileColumn;
import com.rkylin.settle.pojo.SettleFileColumnQuery;

public interface SettleFileColumnManager {
	void saveSettleFileColumn(SettleFileColumn settleFileColumn);

	SettleFileColumn findSettleFileColumnById(Long id);
	
	List<SettleFileColumn> queryList(SettleFileColumnQuery query);
	
	void deleteSettleFileColumnById(Long id);
	
	void deleteSettleFileColumn(SettleFileColumnQuery query);
}
