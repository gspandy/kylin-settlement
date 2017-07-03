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

import com.rkylin.settle.dao.SettleTransAccountDao;
import com.rkylin.settle.manager.SettleTransAccountManager;
import com.rkylin.settle.pojo.SettleTransAccount;
import com.rkylin.settle.pojo.SettleTransAccountQuery;

@Component("settleTransAccountManager")
public class SettleTransAccountManagerImpl implements SettleTransAccountManager {
	
	@Autowired
	@Qualifier("settleTransAccountDao")
	private SettleTransAccountDao settleTransAccountDao;
	
	@Override
	public void saveSettleTransAccount(SettleTransAccount settleTransAccount) {
		settleTransAccountDao.insertSelective(settleTransAccount);
	}
	
	@Override
	public void updateSettleTransAccount(SettleTransAccount settleTransAccount) {
		settleTransAccountDao.updateByPrimaryKeySelective(settleTransAccount);
	}
	
	@Override
	public SettleTransAccount findSettleTransAccountById(Long id) {
		return settleTransAccountDao.selectByPrimaryKey(id);
	}
	
	@Override
	public List<SettleTransAccount> queryList(SettleTransAccountQuery query) {
		return settleTransAccountDao.selectByExample(query);
	}
	
	@Override
	public void deleteSettleTransAccountById(Long id) {
		settleTransAccountDao.deleteByPrimaryKey(id);
	}
	
	@Override
	public void deleteSettleTransAccount(SettleTransAccountQuery query) {
		settleTransAccountDao.deleteByExample(query);
	}
	
	@Override
	public List<Map<String,Object>> queryListMap(SettleTransAccountQuery query){
		return settleTransAccountDao.selectListMapByExample(query);
	}
	
	@Override
	public void updateTransStatusId(Map<String, Object> map) {
		settleTransAccountDao.updateTransStatusId(map);
	}
	
	@Override
	public void updateByMap(Map<String, Object> map) {
		settleTransAccountDao.updateByMap(map);
	}
	
	@Override
	public void insertSelectivebyMap(Map<String, Object> map) {
		settleTransAccountDao.insertSelectivebyMap(map);
	}

	@Override
	public List<Map<String, Object>> selectByOrderNo(Map<String, Object> paramMap) {
		return settleTransAccountDao.selectByOrderNo(paramMap);
	}
	
	@Override
	public List<Map<String, Object>> selectTestAmountSum(Map<String, Object> paramMap) {
		return settleTransAccountDao.selectTestAmountSum(paramMap);
	}
	
	@Override
	public List<Map<String, Object>> selectTestTrans(Map<String, Object> paramMap) {
		return settleTransAccountDao.selectTestTrans(paramMap);
	}
}

