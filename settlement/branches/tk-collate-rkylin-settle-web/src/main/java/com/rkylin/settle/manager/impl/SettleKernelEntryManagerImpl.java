/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2016
 */

package com.rkylin.settle.manager.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.rkylin.settle.dao.SettleKernelEntryDao;
import com.rkylin.settle.manager.SettleKernelEntryManager;
import com.rkylin.settle.pojo.SettleKernelEntry;
import com.rkylin.settle.pojo.SettleKernelEntryQuery;

@Component("settleKernelEntryManager")
public class SettleKernelEntryManagerImpl implements SettleKernelEntryManager {
	
	@Autowired
	@Qualifier("settleKernelEntryDao")
	private SettleKernelEntryDao settleKernelEntryDao;
	
	@Override
	public int saveSettleKernelEntry(SettleKernelEntry settleKernelEntry) {
		return	settleKernelEntryDao.insertSelective(settleKernelEntry);
	}
	
	@Override
	public int updateSettleKernelEntry(SettleKernelEntry settleKernelEntry) {
		return settleKernelEntryDao.updateByPrimaryKeySelective(settleKernelEntry);
	}
	
	@Override
	public SettleKernelEntry findSettleKernelEntryById(Long id) {
		return settleKernelEntryDao.selectByPrimaryKey(id);
	}
	
	@Override
	public List<SettleKernelEntry> queryList(SettleKernelEntryQuery query) {
		return settleKernelEntryDao.selectByExample(query);
	}
	
	@Override
	public List<SettleKernelEntry> queryPage(SettleKernelEntryQuery query) {
		return settleKernelEntryDao.selectByPreExample(query);
	}
	
	@Override
	public void deleteSettleKernelEntryById(Long id) {
		settleKernelEntryDao.deleteByPrimaryKey(id);
	}
	
	@Override
	public void deleteSettleKernelEntry(SettleKernelEntryQuery query) {
		settleKernelEntryDao.deleteByExample(query);
	}
	
	@Override
	public int countByExample(SettleKernelEntryQuery example) {
		return settleKernelEntryDao.countByExample(example);
	}
}

