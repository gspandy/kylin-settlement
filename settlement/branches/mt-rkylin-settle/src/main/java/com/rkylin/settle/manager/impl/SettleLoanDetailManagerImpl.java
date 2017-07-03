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

import com.rkylin.settle.dao.SettleLoanDetailDao;
import com.rkylin.settle.manager.SettleLoanDetailManager;
import com.rkylin.settle.pojo.SettleLoanDetail;
import com.rkylin.settle.pojo.SettleLoanDetailQuery;

@Component("settleLoanDetailManager")
public class SettleLoanDetailManagerImpl implements SettleLoanDetailManager {
	
	@Autowired
	@Qualifier("settleLoanDetailDao")
	private SettleLoanDetailDao settleLoanDetailDao;
	
	@Override
	public void saveSettleLoanDetail(SettleLoanDetail settleLoanDetail) {
		settleLoanDetailDao.insertSelective(settleLoanDetail);
	}
	
	@Override
	public void updateSettleLoanDetail(SettleLoanDetail settleLoanDetail) {
		settleLoanDetailDao.updateByPrimaryKeySelective(settleLoanDetail);
	}
	
	@Override
	public SettleLoanDetail findSettleLoanDetailById(Long id) {
		return settleLoanDetailDao.selectByPrimaryKey(id);
	}
	
	@Override
	public List<SettleLoanDetail> queryList(SettleLoanDetailQuery query) {
		return settleLoanDetailDao.selectByExample(query);
	}
	
	@Override
	public void deleteSettleLoanDetailById(Long id) {
		settleLoanDetailDao.deleteByPrimaryKey(id);
	}
	
	@Override
	public void deleteSettleLoanDetail(SettleLoanDetailQuery query) {
		settleLoanDetailDao.deleteByExample(query);
	}
	
	@Override
	public List<Map<String,Object>> selectProfitTransInfo(Map<String, Object> map) {
		return settleLoanDetailDao.selectProfitTransInfo(map);
	}
	
	@Override
	public void updateTransStatusId(Map<String, Object> map) {
		settleLoanDetailDao.updateTransStatusId(map);
	}
}

