/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.settle.manager.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.rkylin.settle.dao.SettleTransDetailDao;
import com.rkylin.settle.manager.SettleTransDetailManager;
import com.rkylin.settle.pojo.SettleTransDetail;
import com.rkylin.settle.pojo.SettleTransDetailQuery;

@Component("settleTransDetailManager")
public class SettleTransDetailManagerImpl implements SettleTransDetailManager {
	
	@Autowired
	@Qualifier("settleTransDetailDao")
	private SettleTransDetailDao settleTransDetailDao;
	
	@Override
	public void saveSettleTransDetail(SettleTransDetail settleTransDetail) {
		settleTransDetailDao.insertSelective(settleTransDetail);
	}
	
	@Override
	public int updateSettleTransDetail(SettleTransDetail settleTransDetail) {
		return settleTransDetailDao.updateByPrimaryKeySelective(settleTransDetail);
	}
	
	@Override
	public SettleTransDetail findSettleTransDetailById(Long id) {
		return settleTransDetailDao.selectByPrimaryKey(id);
	}
	
	@Override
	public List<SettleTransDetail> queryList(SettleTransDetailQuery query) {
		return settleTransDetailDao.selectByExample(query);
	}
	
	@Override
	public void deleteSettleTransDetailById(Long id) {
		settleTransDetailDao.deleteByPrimaryKey(id);
	}
	
	@Override
	public void deleteSettleTransDetail(SettleTransDetailQuery query) {
		settleTransDetailDao.deleteByExample(query);
	}
	@Override
	public List<Map<String,Object>> queryListMap(SettleTransDetailQuery query){
		return settleTransDetailDao.selectListMapByExample(query);
	}
	
	@Override
	public void updateTransStatusId(Map<String, Object> map) {
		settleTransDetailDao.updateTransStatusId(map);
	}
	
	@Override
	public List<Map<String,Object>> selectCollateTransInfo(Map<String, Object> map) {
		return settleTransDetailDao.selectCollateTransInfo(map);
	}
	
	@Override
	public List<Map<String,Object>> selectProfitTransInfo(Map<String, Object> map) {
		return settleTransDetailDao.selectProfitTransInfo(map);
	}

    @Override
    public List<SettleTransDetail> queryPage(SettleTransDetailQuery query) {
        return settleTransDetailDao.selectByPreExample(query);
    }

    @Override
    public int countByExample(SettleTransDetailQuery query) {
        return settleTransDetailDao.countByExample(query);
    }
    
    @Override
    public List<SettleTransDetail> selectByIds(Map<String, Object> example) {
    	return settleTransDetailDao.selectByIds(example);
    }
}

