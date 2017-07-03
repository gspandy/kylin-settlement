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

import com.rkylin.settle.dao.SettleFileColumnDao;
import com.rkylin.settle.manager.SettleFileColumnManager;
import com.rkylin.settle.pojo.SettleFileColumn;
import com.rkylin.settle.pojo.SettleFileColumnQuery;

@Component("settleFileColumnManager")
public class SettleFileColumnManagerImpl implements SettleFileColumnManager {
	
	@Autowired
	@Qualifier("settleFileColumnDao")
	private SettleFileColumnDao settleFileColumnDao;
	
	@Override
	public void saveSettleFileColumn(SettleFileColumn settleFileColumn) {
		if (settleFileColumn.getFileColumnId() == null) {
			settleFileColumnDao.insertSelective(settleFileColumn);
		} else {
			settleFileColumnDao.updateByPrimaryKeySelective(settleFileColumn);
		}
	}
	
	@Override
	public SettleFileColumn findSettleFileColumnById(Long id) {
		return settleFileColumnDao.selectByPrimaryKey(id);
	}
	
	@Override
	public List<SettleFileColumn> queryList(SettleFileColumnQuery query) {
		return settleFileColumnDao.selectByExample(query);
	}
	
	@Override
	public void deleteSettleFileColumnById(Long id) {
		settleFileColumnDao.deleteByPrimaryKey(id);
	}
	
	@Override
	public void deleteSettleFileColumn(SettleFileColumnQuery query) {
		settleFileColumnDao.deleteByExample(query);
	}
}

