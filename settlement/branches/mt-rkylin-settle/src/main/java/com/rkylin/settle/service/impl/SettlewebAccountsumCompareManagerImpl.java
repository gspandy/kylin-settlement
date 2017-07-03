package com.rkylin.settle.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.rkylin.settle.dao.SettlewebAccountsumCompareDao;
import com.rkylin.settle.manager.SettlewebAccountsumCompareManager;
import com.rkylin.settle.pojo.SettleTransDetailQuery;
import com.rkylin.settle.pojo.SettlewebAccountsumCompare;
import com.rkylin.settle.pojo.SettlewebAccountsumCompareQuery;

@Component("settlewebAccountsumCompareManager")
public class SettlewebAccountsumCompareManagerImpl implements SettlewebAccountsumCompareManager {
	
	@Autowired
	@Qualifier("settlewebAccountsumCompareDao")
	private SettlewebAccountsumCompareDao settlewebAccountsumCompareDao;
	
	@Override
	public void saveSettlewebAccountsumCompare(SettlewebAccountsumCompare settlewebAccountsumCompare) {
		if (settlewebAccountsumCompare.getId() == null) {
			settlewebAccountsumCompareDao.insertSelective(settlewebAccountsumCompare);
		} else {
			settlewebAccountsumCompareDao.updateByPrimaryKeySelective(settlewebAccountsumCompare);
		}
	}
	
	@Override
	public SettlewebAccountsumCompare findSettlewebAccountsumCompareById(Long id) {
		return settlewebAccountsumCompareDao.selectByPrimaryKey(id);
	}
	
	@Override
	public List<SettlewebAccountsumCompare> queryList(SettlewebAccountsumCompareQuery query) {
		return settlewebAccountsumCompareDao.selectByExample(query);
	}
	
	@Override
	public void deleteSettlewebAccountsumCompareById(Long id) {
		settlewebAccountsumCompareDao.deleteByPrimaryKey(id);
	}
	
	@Override
	public void deleteSettlewebAccountsumCompare(SettlewebAccountsumCompareQuery query) {
		settlewebAccountsumCompareDao.deleteByExample(query);
	}
	
	@Override
	public int countByExample(SettlewebAccountsumCompareQuery example) {
		return settlewebAccountsumCompareDao.countByExample(example);
	}
	
	@Override
	public List<SettlewebAccountsumCompare> queryPage(SettlewebAccountsumCompareQuery query) {
		return settlewebAccountsumCompareDao.selectByPreExample(query);
	}
}

