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

import com.rkylin.settle.dao.SettleFinanaceAccountDao;
import com.rkylin.settle.manager.SettleFinanaceAccountManager;
import com.rkylin.settle.pojo.SettleFinanaceAccount;
import com.rkylin.settle.pojo.SettleFinanaceAccountQuery;

@Component("settleFinanaceAccountManager")
public class SettleFinanaceAccountManagerImpl implements SettleFinanaceAccountManager {
	
	@Autowired
	@Qualifier("settleFinanaceAccountDao")
	private SettleFinanaceAccountDao settlefinanaceAccountDao;
	
	@Override
	public void saveFinanaceAccount(SettleFinanaceAccount finanaceAccount) {
//		if (finanaceAccount.getFinAccountId() == null) {
			settlefinanaceAccountDao.insertSelective(finanaceAccount);
//		} else {
//			finanaceAccountDao.updateByPrimaryKeySelective(finanaceAccount);
//		}
	}
	
	@Override
	public SettleFinanaceAccount findFinanaceAccountById(Long id) {
		return settlefinanaceAccountDao.selectByPrimaryKey(id);
	}
	
	@Override
	public List<SettleFinanaceAccount> queryList(SettleFinanaceAccountQuery query) {
		return settlefinanaceAccountDao.selectByExample(query);
	}
	
	@Override
	public List<SettleFinanaceAccount> selectByFinAccountId(String[] finAccountIds) {
		return settlefinanaceAccountDao.selectByFinAccountId(finAccountIds);
	}
	
	@Override
	public void deleteFinanaceAccountById(Long id) {
		settlefinanaceAccountDao.deleteByPrimaryKey(id);
	}
	
	@Override
	public void deleteFinanaceAccount(SettleFinanaceAccountQuery query) {
		settlefinanaceAccountDao.deleteByExample(query);
	}
	@Override
	public void updateFinanaceAccount(SettleFinanaceAccount finanaceAccount) {
//		if (finanaceAccount.getFinAccountId() == null) {
//			finanaceAccountDao.insertSelective(finanaceAccount);
//		} else {
		settlefinanaceAccountDao.updateByPrimaryKeySelective(finanaceAccount);
//		}
	}
	
	@Override
	public void updateTransStatusId(Map<String, Object> map) {
//		if (finanaceAccount.getFinAccountId() == null) {
//			finanaceAccountDao.insertSelective(finanaceAccount);
//		} else {
		settlefinanaceAccountDao.updateTransStatusId(map);
//		}
	}
}

