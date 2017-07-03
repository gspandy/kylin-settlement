/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2016
 */

package com.rkylin.settle.manager;

import java.util.List;
import java.util.Map;

import com.rkylin.settle.pojo.OrderInfo;
import com.rkylin.settle.pojo.OrderInfoQuery;

public interface OrderInfoManager {
	void saveOrderInfo(OrderInfo orderInfo);

	void updateOrderInfo(OrderInfo orderInfo);
	
	OrderInfo findOrderInfoById(Long id);
	
	List<OrderInfo> queryList(OrderInfoQuery query);
	
	void deleteOrderInfoById(Long id);
	
	void deleteOrderInfo(OrderInfoQuery query);
	
	List<OrderInfo> selectByUpdateTime(Map<String,Object> query);
}
