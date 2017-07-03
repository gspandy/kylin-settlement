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

import com.rkylin.settle.dao.TransDetailInfoDao;
import com.rkylin.settle.manager.TransDetailInfoManager;
import com.rkylin.settle.pojo.TransDetailInfo;
import com.rkylin.settle.pojo.TransDetailInfoQuery;

@Component("transDetailInfoManager")
public class TransDetailInfoManagerImpl implements TransDetailInfoManager {
	
	@Autowired
	@Qualifier("transDetailInfoDao")
	private TransDetailInfoDao transDetailInfoDao;
	
	@Override
	public void saveTransDetailInfo(TransDetailInfo transDetailInfo) {
		transDetailInfoDao.insertSelective(transDetailInfo);
	}
	
	@Override
	public void updateTransDetailInfo(TransDetailInfo transDetailInfo) {
		transDetailInfoDao.updateByPrimaryKeySelective(transDetailInfo);
	}
	
	@Override
	public TransDetailInfo findTransDetailInfoById(Long id) {
		return transDetailInfoDao.selectByPrimaryKey(id);
	}
	
	@Override
	public List<TransDetailInfo> queryList(TransDetailInfoQuery query) {
		return transDetailInfoDao.selectByExample(query);
	}
	
	@Override
	public void deleteTransDetailInfoById(Long id) {
		transDetailInfoDao.deleteByPrimaryKey(id);
	}
	
	@Override
	public void deleteTransDetailInfo(TransDetailInfoQuery query) {
		transDetailInfoDao.deleteByExample(query);
	}
}

