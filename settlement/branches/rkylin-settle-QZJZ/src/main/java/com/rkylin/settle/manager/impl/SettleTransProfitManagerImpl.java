/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.settle.manager.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.rkylin.settle.dao.SettleTransProfitDao;
import com.rkylin.settle.manager.SettleTransProfitManager;
import com.rkylin.settle.pojo.SettleTransProfit;
import com.rkylin.settle.pojo.SettleTransProfitQuery;

@Component("settleTransProfitManager")
public class SettleTransProfitManagerImpl implements SettleTransProfitManager {
	
	@Autowired
	@Qualifier("settleTransProfitDao")
	private SettleTransProfitDao settleTransProfitDao;
	
	@Override
	public int countByExample(SettleTransProfitQuery query) {
		return settleTransProfitDao.countByExample(query);
	}
	
	@Override
	public void saveSettleTransProfit(SettleTransProfit settleTransProfit) {
		settleTransProfitDao.insertSelective(settleTransProfit);
	}
	
	@Override
	public void updateTransStatusId(Map<String, Object> query) {
		settleTransProfitDao.updateTransStatusId(query);
	}
	
	@Override
	public void updateSettleTransProfit(SettleTransProfit settleTransProfit) {
		settleTransProfitDao.updateByPrimaryKeySelective(settleTransProfit);
	}
	
	@Override
	public int updateByIdList(Map<String, Object> query) {
		return settleTransProfitDao.updateByIdList(query);
	}
	
	@Override
	public SettleTransProfit findSettleTransProfitById(Long id) {
		return settleTransProfitDao.selectByPrimaryKey(id);
	}
	
	@Override
	public List<SettleTransProfit> queryList(SettleTransProfitQuery query) {
		return settleTransProfitDao.selectByExample(query);
	}
	
	@Override
	public void deleteSettleTransProfitById(Long id) {
		settleTransProfitDao.deleteByPrimaryKey(id);
	}
	
	@Override
	public void deleteSettleTransProfit(SettleTransProfitQuery query) {
		settleTransProfitDao.deleteByExample(query);
	}
	
	@Override
	public List<SettleTransProfit> selectTransProfitWithUnbalance(Map<String, Object> query) {
		return settleTransProfitDao.selectTransProfitWithUnbalance(query);
	}
	
	@Override
	public List<SettleTransProfit> selectTransProfitWithDetailId(Map<String, Object> query) {
		return settleTransProfitDao.selectTransProfitWithDetailId(query);
	}

	@Override
	public List<SettleTransProfit> selectloanProfitWithUnbalance(Map<String, Object> query) {
		return settleTransProfitDao.selectloanProfitWithUnbalance(query);
	}
}

