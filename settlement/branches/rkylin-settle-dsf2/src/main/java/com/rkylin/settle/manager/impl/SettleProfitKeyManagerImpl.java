/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.settle.manager.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.rkylin.settle.dao.SettleProfitKeyDao;
import com.rkylin.settle.manager.SettleProfitKeyManager;
import com.rkylin.settle.pojo.SettleProfitKey;
import com.rkylin.settle.pojo.SettleProfitKeyQuery;

@Component("settleProfitKeyManager")
public class SettleProfitKeyManagerImpl implements SettleProfitKeyManager {
	
	@Autowired
	@Qualifier("settleProfitKeyDao")
	private SettleProfitKeyDao settleProfitKeyDao;
	
	@Override
	public void saveSettleProfitKey(SettleProfitKey settleProfitKey) {
		settleProfitKeyDao.insertSelective(settleProfitKey);
	}
	
	@Override
	public void updateSettleProfitKey(SettleProfitKey settleProfitKey) {
		settleProfitKeyDao.updateByPrimaryKeySelective(settleProfitKey);
	}
	
	@Override
	public SettleProfitKey findSettleProfitKeyById(Long id) {
		return settleProfitKeyDao.selectByPrimaryKey(id);
	}
	
	@Override
	public List<SettleProfitKey> queryList(SettleProfitKeyQuery query) {
		return settleProfitKeyDao.selectByExample(query);
	}
	
	@Override
	public List<SettleProfitKey> selectAllProfitKey(SettleProfitKeyQuery query) {
		return settleProfitKeyDao.selectAllProfitKey(query);
	}
	
	@Override
	public void deleteSettleProfitKeyById(Long id) {
		settleProfitKeyDao.deleteByPrimaryKey(id);
	}
	
	@Override
	public void deleteSettleProfitKey(SettleProfitKeyQuery query) {
		settleProfitKeyDao.deleteByExample(query);
	}
}

