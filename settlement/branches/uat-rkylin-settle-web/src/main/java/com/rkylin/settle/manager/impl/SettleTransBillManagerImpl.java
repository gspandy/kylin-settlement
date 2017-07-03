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

import com.rkylin.settle.dao.SettleTransBillDao;
import com.rkylin.settle.manager.SettleTransBillManager;
import com.rkylin.settle.pojo.SettleTransBill;
import com.rkylin.settle.pojo.SettleTransBillQuery;

@Component("settleTransBillManager")
public class SettleTransBillManagerImpl implements SettleTransBillManager {
	
	@Autowired
	@Qualifier("settleTransBillDao")
	private SettleTransBillDao settleTransBillDao;
	
	@Override
	public void saveSettleTransBill(SettleTransBill settleTransBill) {
		settleTransBillDao.insertSelective(settleTransBill);
	}
	
	@Override
	public int updateSettleTransBill(SettleTransBill settleTransBill) {
		return settleTransBillDao.updateByPrimaryKeySelective(settleTransBill);
	}
	
	@Override
	public SettleTransBill findSettleTransBillById(Long id) {
		return settleTransBillDao.selectByPrimaryKey(id);
	}
	
	@Override
	public List<SettleTransBill> queryList(SettleTransBillQuery query) {
		return settleTransBillDao.selectByExample(query);
	}
	
	@Override
	public void deleteSettleTransBillById(Long id) {
		settleTransBillDao.deleteByPrimaryKey(id);
	}
	
	@Override
	public void deleteSettleTransBill(SettleTransBillQuery query) {
		settleTransBillDao.deleteByExample(query);
	}

    @Override
    public List<SettleTransBill> queryPage(SettleTransBillQuery query) {
        return settleTransBillDao.selectByPreExample(query);
    }

    @Override
    public int countByExample(SettleTransBillQuery query) {
        return settleTransBillDao.countByExample(query);
    }
    
    @Override
    public int updateSettleTransBillByOrderNo(SettleTransBill settleTransBill) {
        return settleTransBillDao.updateSettleTransBillByOrderNo(settleTransBill);
    }
}

