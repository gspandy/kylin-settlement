/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.settle.manager;

import java.util.List;

import com.rkylin.settle.pojo.SettleBatchManage;
import com.rkylin.settle.pojo.SettleBatchManageQuery;

public interface SettleBatchManageManager {
	void saveSettleBatchManage(SettleBatchManage settleBatchManage);

	SettleBatchManage findSettleBatchManageById(Long id);
	
	List<SettleBatchManage> queryList(SettleBatchManageQuery query);
	
	void deleteSettleBatchManageById(Long id);
	
	void deleteSettleBatchManage(SettleBatchManageQuery query);
}
