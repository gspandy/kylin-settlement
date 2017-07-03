/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2016
 */

package com.rkylin.settle.manager;

import java.util.List;

import com.rkylin.settle.pojo.TransOrderInfo;
import com.rkylin.settle.pojo.TransOrderInfoQuery;

public interface TransOrderInfoManager {
	void saveTransOrderInfo(TransOrderInfo transOrderInfo);

	TransOrderInfo findTransOrderInfoById(Long id);
	
	List<TransOrderInfo> queryList(TransOrderInfoQuery query);
	
	void deleteTransOrderInfoById(Long id);
	
	void deleteTransOrderInfo(TransOrderInfoQuery query);
	
	List<TransOrderInfo> queryPage(TransOrderInfoQuery query);
	
	Integer countByExample(TransOrderInfoQuery query);
	
}
