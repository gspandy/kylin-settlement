package com.rkylin.settle.manager.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.rkylin.settle.dao.SettleTransSummaryHisDao;
import com.rkylin.settle.manager.SettleTransSummaryHisManager;
import com.rkylin.settle.pojo.SettleTransSummaryHis;
import com.rkylin.settle.pojo.SettleTransSummaryHisQuery;


@Component("settleTransSummaryHisManager")
public class SettleTransSummaryHisManagerImpl implements SettleTransSummaryHisManager {
	
	@Autowired
	@Qualifier("settleTransSummaryHisDao")
	private SettleTransSummaryHisDao SettleTransSummaryHisDao;
	
	@Override
	public void saveSettleTransSummaryHis(SettleTransSummaryHis SettleTransSummaryHis) {
		if (SettleTransSummaryHis.getTransSumId() == null) {
			SettleTransSummaryHisDao.insertSelective(SettleTransSummaryHis);
		} else {
			SettleTransSummaryHisDao.updateByPrimaryKeySelective(SettleTransSummaryHis);
		}
	}
	
	@Override
	public SettleTransSummaryHis findSettleTransSummaryHisById(Long id) {
		return SettleTransSummaryHisDao.selectByPrimaryKey(id);
	}
	
	@Override
	public List<SettleTransSummaryHis> queryList(SettleTransSummaryHisQuery query) {
		return SettleTransSummaryHisDao.selectByExample(query);
	}
	
	@Override
	public void deleteSettleTransSummaryHisById(Long id) {
		SettleTransSummaryHisDao.deleteByPrimaryKey(id);
	}
	
	@Override
	public void deleteSettleTransSummaryHis(SettleTransSummaryHisQuery query) {
		SettleTransSummaryHisDao.deleteByExample(query);
	}
	
	@Override
	public void updateSettleTransSummaryHis(SettleTransSummaryHis SettleTransSummaryHis) {
		SettleTransSummaryHisDao.updateByPrimaryKeySelective(SettleTransSummaryHis);
	}
}

