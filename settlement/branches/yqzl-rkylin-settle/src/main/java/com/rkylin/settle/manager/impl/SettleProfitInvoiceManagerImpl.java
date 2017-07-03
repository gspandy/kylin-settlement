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

import com.rkylin.settle.dao.SettleProfitInvoiceDao;
import com.rkylin.settle.manager.SettleProfitInvoiceManager;
import com.rkylin.settle.pojo.SettleProfitInvoice;
import com.rkylin.settle.pojo.SettleProfitInvoiceQuery;

@Component("settleProfitInvoiceManager")
public class SettleProfitInvoiceManagerImpl implements SettleProfitInvoiceManager {
	
	@Autowired
	@Qualifier("settleProfitInvoiceDao")
	private SettleProfitInvoiceDao settleProfitInvoiceDao;
	
	@Override
	public void saveSettleProfitInvoice(SettleProfitInvoice settleProfitInvoice) {
		settleProfitInvoiceDao.insertSelective(settleProfitInvoice);
	}
	
	@Override
	public void updateSettleProfitInvoice(SettleProfitInvoice settleProfitInvoice) {
		settleProfitInvoiceDao.updateByPrimaryKeySelective(settleProfitInvoice);
	}
	
	@Override
	public SettleProfitInvoice findSettleProfitInvoiceById(Long id) {
		return settleProfitInvoiceDao.selectByPrimaryKey(id);
	}
	
	@Override
	public List<SettleProfitInvoice> queryList(SettleProfitInvoiceQuery query) {
		return settleProfitInvoiceDao.selectByExample(query);
	}
	
	@Override
	public void deleteSettleProfitInvoiceById(Long id) {
		settleProfitInvoiceDao.deleteByPrimaryKey(id);
	}
	
	@Override
	public void deleteSettleProfitInvoice(SettleProfitInvoiceQuery query) {
		settleProfitInvoiceDao.deleteByExample(query);
	}

	@Override
	public List<SettleProfitInvoice> selectInvoiceData(SettleProfitInvoiceQuery query) {
		return settleProfitInvoiceDao.selectInvoiceData(query);
	}
}

