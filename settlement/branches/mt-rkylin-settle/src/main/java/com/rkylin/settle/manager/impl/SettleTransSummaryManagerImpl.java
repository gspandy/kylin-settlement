package com.rkylin.settle.manager.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.rkylin.settle.dao.SettleTransSummaryDao;
import com.rkylin.settle.manager.SettleTransSummaryManager;
import com.rkylin.settle.pojo.SettleTransSummary;
import com.rkylin.settle.pojo.SettleTransSummaryQuery;


@Component("settleTransSummaryManager")
public class SettleTransSummaryManagerImpl implements SettleTransSummaryManager {
	
	@Autowired
	@Qualifier("settleTransSummaryDao")
	private SettleTransSummaryDao settleTransSummaryDao;
	
	@Override
	public void saveSettleTransSummary(SettleTransSummary settleTransSummary) {
		if (settleTransSummary.getTransSumId() == null) {
			settleTransSummaryDao.insertSelective(settleTransSummary);
		} else {
			settleTransSummaryDao.updateByPrimaryKeySelective(settleTransSummary);
		}
	}
	
	@Override
	public SettleTransSummary findSettleTransSummaryById(Long id) {
		return settleTransSummaryDao.selectByPrimaryKey(id);
	}
	
	@Override
	public List<SettleTransSummary> queryList(SettleTransSummaryQuery query) {
		return settleTransSummaryDao.selectByExample(query);
	}
	
	@Override
	public void deleteSettleTransSummaryById(Long id) {
		settleTransSummaryDao.deleteByPrimaryKey(id);
	}
	
	@Override
	public void deleteSettleTransSummary(SettleTransSummaryQuery query) {
		settleTransSummaryDao.deleteByExample(query);
	}
	
	@Override
	public void updateSettleTransSummary(SettleTransSummary settleTransSummary) {
		settleTransSummaryDao.updateByPrimaryKeySelective(settleTransSummary);
	}

	@Override
	public int batchInsertTransSummery(List<SettleTransSummary> settleTransSummary) {
		return settleTransSummaryDao.batchInsertTransSummery(settleTransSummary);
	}
}

