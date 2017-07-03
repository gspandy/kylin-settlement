/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2016
 */

package com.rkylin.settle.manager.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.rkylin.settle.dao.OrderInfoDao;
import com.rkylin.settle.manager.OrderInfoManager;
import com.rkylin.settle.pojo.OrderInfo;
import com.rkylin.settle.pojo.OrderInfoQuery;

@Component("orderInfoManager")
public class OrderInfoManagerImpl implements OrderInfoManager {
	
	@Autowired
	@Qualifier("orderInfoDao")
	private OrderInfoDao orderInfoDao;
	
	@Override
	public void saveOrderInfo(OrderInfo orderInfo) {
		orderInfoDao.insertSelective(orderInfo);
	}
	
	@Override
	public void updateOrderInfo(OrderInfo orderInfo) {
		orderInfoDao.updateByPrimaryKeySelective(orderInfo);
	}
	
	@Override
	public OrderInfo findOrderInfoById(Long id) {
		return orderInfoDao.selectByPrimaryKey(id);
	}
	
	@Override
	public List<OrderInfo> queryList(OrderInfoQuery query) {
		return orderInfoDao.selectByExample(query);
	}
	
	@Override
	public void deleteOrderInfoById(Long id) {
		orderInfoDao.deleteByPrimaryKey(id);
	}
	
	@Override
	public void deleteOrderInfo(OrderInfoQuery query) {
		orderInfoDao.deleteByExample(query);
	}
	
	@Override
	public List<OrderInfo> selectByUpdateTime(Map<String,Object> query) {
		return orderInfoDao.selectByUpdateTime(query);
	}
}

