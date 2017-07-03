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
    public List<SettleTransInvoice> queryPage(SettleTransInvoiceQuery query) {
        return settleTransInvoiceDao.selectByPreExample(query);
    }

    @Override
    public int countByExample(SettleTransInvoiceQuery query) {
        return settleTransInvoiceDao.countByExample(query);
    }
    
	@Override
	public SettleTransInvoice findSettleTransInvoiceById(Long id) {
		return settleTransInvoiceDao.selectByPrimaryKey(id);
	}
	
	@Override
	public int updateSettleTransInvoice(SettleTransInvoice settleTransInvoice) {
		return settleTransInvoiceDao.updateByPrimaryKeySelective(settleTransInvoice);
	}
	
	@Override
	public void saveSettleTransInvoice(SettleTransInvoice settleTransInvoice) {
		if (settleTransInvoice.getInvoiceNo() == null) {
			settleTransInvoiceDao.insertSelective(settleTransInvoice);
		}
	}
	
	@Override
	public void updateTransStatusId(Map<String, Object> map) {
		settleTransInvoiceDao.updateTransStatusId(map);
	}
	
	@Override
    public List<SettleTransInvoice> selectByIds(Map<String, Object> example) {
    	return settleTransInvoiceDao.selectByIds(example);
    }
	
    @Override
	public void deleteSettleTransInvoiceByInvoiceNo(Long invoiceNo) {
    	settleTransInvoiceDao.deleteByPrimaryKey(invoiceNo);
	}
    
    @Override
    public long summaryMoneyByExample(SettleTransInvoiceQuery query) {
        return settleTransInvoiceDao.summaryMoneyByExample(query);
    }
}

