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

import com.rkylin.settle.dao.SettleBalanceEntryDao;
import com.rkylin.settle.manager.SettleBalanceEntryManager;
import com.rkylin.settle.pojo.SettleBalanceEntry;
import com.rkylin.settle.pojo.SettleBalanceEntryQuery;

@Component("settleBalanceEntryManager")
public class SettleBalanceEntryManagerImpl implements SettleBalanceEntryManager {
	@Autowired
	@Qualifier("settleBalanceEntryDao")
	private SettleBalanceEntryDao settleBalanceEntryDao;
	
	@Override
	public int countByExample(SettleBalanceEntryQuery query) {
		return settleBalanceEntryDao.countByExample(query);
	}
	
	@Override
	public void saveSettleBalanceEntry(SettleBalanceEntry settleBalanceEntry) {
		settleBalanceEntryDao.insertSelective(settleBalanceEntry);
	}
	
	@Override
	public int updateSettleBalanceEntry(SettleBalanceEntry settleBalanceEntry) {
		return settleBalanceEntryDao.updateByPrimaryKeySelective(settleBalanceEntry);
	}
	
	@Override
	public SettleBalanceEntry findSettleBalanceEntryById(Long id) {
		return settleBalanceEntryDao.selectByPrimaryKey(id);
	}
	
	@Override
	public List<SettleBalanceEntry> queryList(SettleBalanceEntryQuery query) {
		return settleBalanceEntryDao.selectByExample(query);
	}
	
	@Override
	public List<SettleBalanceEntry> queryPage(SettleBalanceEntryQuery query) {
		return settleBalanceEntryDao.selectByPreExample(query);
	}
	
	@Override
	public void deleteSettleBalanceEntryById(Long id) {
		settleBalanceEntryDao.deleteByPrimaryKey(id);
	}
	
	@Override
	public void deleteSettleBalanceEntry(SettleBalanceEntryQuery query) {
		settleBalanceEntryDao.deleteByExample(query);
	}

	@Override
	public List<SettleBalanceEntry> queryByIds(Long[] ids) {
		return settleBalanceEntryDao.queryByIds(ids);
	}
}

