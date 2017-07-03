package com.rkylin.settle.manager.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.rkylin.settle.dao.SettleTransInvoiceHisDao;
import com.rkylin.settle.manager.SettleTransInvoiceHisManager;
import com.rkylin.settle.pojo.SettleTransInvoiceHis;
import com.rkylin.settle.pojo.SettleTransInvoiceHisQuery;


@Component("settleTransInvoiceHisManager")
public class SettleTransInvoiceHisManagerImpl implements SettleTransInvoiceHisManager {
	
	@Autowired
	@Qualifier("settleTransInvoiceHisDao")
	private SettleTransInvoiceHisDao SettleTransInvoiceHisDao;
	
	@Override
	public void saveSettleTransInvoiceHis(SettleTransInvoiceHis SettleTransInvoiceHis) {
		if (SettleTransInvoiceHis.getInvoiceNo() == null) {
			SettleTransInvoiceHisDao.insertSelective(SettleTransInvoiceHis);
		} else {
			SettleTransInvoiceHisDao.updateByPrimaryKeySelective(SettleTransInvoiceHis);
		}
	}
	
	@Override
	public SettleTransInvoiceHis findSettleTransInvoiceHisById(Long id) {
		return SettleTransInvoiceHisDao.selectByPrimaryKey(id);
	}
	
	@Override
	public List<SettleTransInvoiceHis> queryList(SettleTransInvoiceHisQuery query) {
		return SettleTransInvoiceHisDao.selectByExample(query);
	}
	
	@Override
	public void deleteSettleTransInvoiceHisById(Long id) {
		SettleTransInvoiceHisDao.deleteByPrimaryKey(id);
	}
	
	@Override
	public void deleteSettleTransInvoiceHis(SettleTransInvoiceHisQuery query) {
		SettleTransInvoiceHisDao.deleteByExample(query);
	}

	@Override
	public void updateSettleTransInvoiceHis(SettleTransInvoiceHis SettleTransInvoiceHis) {
		SettleTransInvoiceHisDao.updateByPrimaryKey(SettleTransInvoiceHis);
		
	}

	@Override
	public List<SettleTransInvoiceHis> selectTransInvoiceList(Map<String, Object> map) {
		return SettleTransInvoiceHisDao.selectTransInvoiceList(map);
	}

	@Override
	public int queryTotalByExample(SettleTransInvoiceHisQuery query) {
		return SettleTransInvoiceHisDao.queryTotalByExample(query);
	}

	@Override
	public long sumByExample(Map<String, Object> map) {
		return SettleTransInvoiceHisDao.sumByExample(map);
	}
	
	@Override
	public int removeToHisProc(Map<String, Object> map) {
		return SettleTransInvoiceHisDao.removeToHisProc(map);
	}
}

