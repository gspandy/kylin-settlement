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

import com.rkylin.settle.dao.TransOrderInfoDao;
import com.rkylin.settle.manager.TransOrderInfoManager;
import com.rkylin.settle.pojo.TransOrderInfo;
import com.rkylin.settle.pojo.TransOrderInfoQuery;


@Component("transOrderInfoManager")
public class TransOrderInfoManagerImpl implements TransOrderInfoManager {
	
	@Autowired
	@Qualifier("transOrderInfoDao")
	private TransOrderInfoDao transOrderInfoDao;
	
	@Override
	public void saveTransOrderInfo(TransOrderInfo transOrderInfo) {
		if (transOrderInfo.getId() == null) {
			transOrderInfoDao.insertSelective(transOrderInfo);
		} else {
			transOrderInfoDao.updateByPrimaryKeySelective(transOrderInfo);
		}
	}
	
	@Override
	public TransOrderInfo findTransOrderInfoById(Long id) {
		return transOrderInfoDao.selectByPrimaryKey(id);
	}
	
	@Override
	public List<TransOrderInfo> queryList(TransOrderInfoQuery query) {
		return transOrderInfoDao.selectByExample(query);
	}
	
	@Override
	public void deleteTransOrderInfoById(Long id) {
		transOrderInfoDao.deleteByPrimaryKey(id);
	}
	
	@Override
	public void deleteTransOrderInfo(TransOrderInfoQuery query) {
		transOrderInfoDao.deleteByExample(query);
	}
	
	@Override
    public List<TransOrderInfo> queryByRequestTime(Map<String, Object> query) {
        return transOrderInfoDao.selectByRequestTime(query);
    }
}

