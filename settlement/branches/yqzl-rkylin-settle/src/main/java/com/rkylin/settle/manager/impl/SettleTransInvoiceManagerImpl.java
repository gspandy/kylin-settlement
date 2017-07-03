package com.rkylin.settle.manager.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.rkylin.settle.dao.SettleTransInvoiceDao;
import com.rkylin.settle.manager.SettleTransInvoiceManager;
import com.rkylin.settle.pojo.SettleTransInvoice;
import com.rkylin.settle.pojo.SettleTransInvoiceQuery;


@Component("settleTransInvoiceManager")
public class SettleTransInvoiceManagerImpl implements SettleTransInvoiceManager {
	
	@Autowired
	@Qualifier("settleTransInvoiceDao")
	private SettleTransInvoiceDao settleTransInvoiceDao;
	
	@Override
	public void saveSettleTransInvoice(SettleTransInvoice settleTransInvoice) {
		if (settleTransInvoice.getInvoiceNo() == null) {
			settleTransInvoiceDao.insertSelective(settleTransInvoice);
		} else {
			settleTransInvoiceDao.updateByPrimaryKeySelective(settleTransInvoice);
		}
	}
	
	@Override
	public SettleTransInvoice findSettleTransInvoiceById(Long id) {
		return settleTransInvoiceDao.selectByPrimaryKey(id);
	}
	
	@Override
	public List<SettleTransInvoice> queryList(SettleTransInvoiceQuery query) {
		return settleTransInvoiceDao.selectByExample(query);
	}
	
	@Override
	public void deleteSettleTransInvoiceById(Long id) {
		settleTransInvoiceDao.deleteByPrimaryKey(id);
	}
	
	@Override
	public void deleteSettleTransInvoice(SettleTransInvoiceQuery query) {
		settleTransInvoiceDao.deleteByExample(query);
	}

	@Override
	public void updateSettleTransInvoice(SettleTransInvoice settleTransInvoice) {
		settleTransInvoiceDao.updateByPrimaryKey(settleTransInvoice);
		
	}

	@Override
	public List<SettleTransInvoice> selectTransInvoiceList(Map<String, Object> map) {
		return settleTransInvoiceDao.selectTransInvoiceList(map);
	}

	@Override
	public int queryTotalByExample(SettleTransInvoiceQuery query) {
		return settleTransInvoiceDao.queryTotalByExample(query);
	}

	@Override
	public long sumByExample(Map<String, Object> map) {
		return settleTransInvoiceDao.sumByExample(map);
	}
	
	@Override
	public long countByDfExample(Map<String, Object> map) {
		return settleTransInvoiceDao.countByDfExample(map);
	}
	
	@Override
	public int removeToHisProc(Map<String, Object> map) {
		return settleTransInvoiceDao.removeToHisProc(map);
	}
	
	@Override
	public int batchInsertTransInvoice(List<SettleTransInvoice> example) {
		return settleTransInvoiceDao.batchInsertTransInvoice(example);
	}
}

