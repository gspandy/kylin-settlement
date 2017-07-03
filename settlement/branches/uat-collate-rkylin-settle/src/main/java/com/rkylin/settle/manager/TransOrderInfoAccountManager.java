/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.settle.manager;

import java.util.List;
import java.util.Map;

import com.rkylin.settle.pojo.TransOrderInfo;
import com.rkylin.settle.pojo.TransOrderInfoAccount;
import com.rkylin.settle.pojo.TransOrderInfoAccountQuery;

public interface TransOrderInfoAccountManager {
	void saveTransOrderInfo(TransOrderInfoAccount transOrderInfo);

	TransOrderInfoAccount findTransOrderInfoById(Long id);
	
	List<TransOrderInfoAccount> queryList(TransOrderInfoAccountQuery query);
	
	void deleteTransOrderInfoById(Long id);
	
	void deleteTransOrderInfo(TransOrderInfoAccountQuery query);
	
	List<TransOrderInfoAccount> queryByRequestTime(Map<String,Object> query);
}
