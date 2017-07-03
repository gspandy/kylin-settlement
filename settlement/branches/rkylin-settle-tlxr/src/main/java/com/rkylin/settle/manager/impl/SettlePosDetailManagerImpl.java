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

import com.rkylin.settle.dao.SettlePosDetailDao;
import com.rkylin.settle.manager.SettlePosDetailManager;
import com.rkylin.settle.pojo.SettlePosDetail;
import com.rkylin.settle.pojo.SettlePosDetailQuery;
import com.rkylin.settle.pojo.SettleTransDetail;
import com.rkylin.settle.pojo.SettleTransDetailQuery;

@Component("settlePosDetailManager")
public class SettlePosDetailManagerImpl implements SettlePosDetailManager {
	
	@Autowired
	@Qualifier("settlePosDetailDao")
	private SettlePosDetailDao settlePosDetailDao;
	
	@Override
	public int countSettlePosDetail(SettlePosDetailQuery settlePosDetail) {
		return settlePosDetailDao.countByExample(settlePosDetail);
	}
	
	@Override
	public void saveSettlePosDetail(SettlePosDetail settlePosDetail) {
		settlePosDetailDao.insertSelective(settlePosDetail);
	}
	
	@Override
	public void updateSettlePosDetail(SettlePosDetail settlePosDetail) {
		settlePosDetailDao.updateByPrimaryKeySelective(settlePosDetail);
	}
	
	@Override
	public SettlePosDetail findSettlePosDetailById(Long id) {
		return settlePosDetailDao.selectByPrimaryKey(id);
	}
	
	@Override
	public List<SettlePosDetail> queryList(SettlePosDetailQuery query) {
		return settlePosDetailDao.selectByExample(query);
	}
	
	@Override
	public void deleteSettlePosDetailById(Long id) {
		settlePosDetailDao.deleteByPrimaryKey(id);
	}
	
	@Override
	public void deleteSettlePosDetail(SettlePosDetailQuery query) {
		settlePosDetailDao.deleteByExample(query);
	}
	
	@Override
	public List<Map<String,Object>> queryListMap(SettlePosDetailQuery query){
		return settlePosDetailDao.selectListMapByExample(query);
	}
	
	@Override
	public void updateTransStatusId(Map<String, Object> map) {
		settlePosDetailDao.updateTransStatusId(map);
	}

    @Override
    public void batchSaveSettlePosDetail(List<SettlePosDetail> settlePosDetailList) {
    	settlePosDetailDao.batchInsertSelective(settlePosDetailList);
    }

    @Override
    public List<Map<String,Object>> queryPosForProfitList(SettlePosDetailQuery query) {
    	return settlePosDetailDao.queryPosForProfitList(query);
    }
    
    @Override
	public int updateAccountInfoToDetailInfo(Map<String, Object> map) {
		return settlePosDetailDao.updateAccountInfoToDetailInfo(map);
	}
    
    @Override
    public List<SettlePosDetail> selectRePayTransFee(SettlePosDetailQuery query) {
    	return settlePosDetailDao.selectRePayTransFee(query);
    }
}

