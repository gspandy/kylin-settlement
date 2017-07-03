/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2016
 */

package com.rkylin.settle.manager.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.rkylin.settle.dao.TransOrderInfoAccountDao;
import com.rkylin.settle.manager.TransOrderInfoAccountManager;
import com.rkylin.settle.pojo.TransOrderInfoAccount;
import com.rkylin.settle.pojo.TransOrderInfoAccountQuery;


@Component("transOrderInfoAccountManager")
public class TransOrderInfoAccountManagerImpl implements TransOrderInfoAccountManager {
	@Autowired
	@Qualifier("transOrderInfoAccountDao")
	private TransOrderInfoAccountDao transOrderInfoAccountDao;
	
	@Override
	public void saveTransOrderInfo(TransOrderInfoAccount transOrderInfo) {
		if (transOrderInfo.getRequestId() == null) {
			transOrderInfoAccountDao.insertSelective(transOrderInfo);
		} else {
			transOrderInfoAccountDao.updateByPrimaryKeySelective(transOrderInfo);
		}
	}
	
	@Override
	public TransOrderInfoAccount findTransOrderInfoById(Long id) {
		return transOrderInfoAccountDao.selectByPrimaryKey(id);
	}
	
	@Override
	public List<TransOrderInfoAccount> queryList(TransOrderInfoAccountQuery query) {
		return transOrderInfoAccountDao.selectByExample(query);
	}
	
	@Override
	public void deleteTransOrderInfoById(Long id) {
		transOrderInfoAccountDao.deleteByPrimaryKey(id);
	}
	
	@Override
	public void deleteTransOrderInfo(TransOrderInfoAccountQuery query) {
		transOrderInfoAccountDao.deleteByExample(query);
	}
	
	@Override
    public List<TransOrderInfoAccount> queryByRequestTime(Map<String, Object> query) {
        return transOrderInfoAccountDao.selectByRequestTime(query);
    }
}

