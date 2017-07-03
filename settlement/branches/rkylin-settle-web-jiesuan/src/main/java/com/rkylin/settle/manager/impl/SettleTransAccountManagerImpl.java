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
	public int updateSettleTransAccount(SettleTransAccount settleTransAccount) {
		return settleTransAccountDao.updateByPrimaryKeySelective(settleTransAccount);
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
	public void insertSelectivebyMap(Map<String, Object> map) {
		settleTransAccountDao.insertSelectivebyMap(map);
	}

    @Override
    public List<SettleTransAccount> queryPage(SettleTransAccountQuery query) {
        return settleTransAccountDao.selectByPreExample(query);
    }

    @Override
    public int countByExample(SettleTransAccountQuery query) {
        return settleTransAccountDao.countByExample(query);
    }
	
	@Override
	public List<SettleTransAccount> summaryList(SettleTransAccountQuery query) {
		return settleTransAccountDao.summaryByExample(query);
	}
}

