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

import com.rkylin.settle.dao.ParameterInfoOldDao;
import com.rkylin.settle.manager.ParameterInfoOldManager;
import com.rkylin.settle.pojo.ParameterInfo;
import com.rkylin.settle.pojo.ParameterInfoOld;
import com.rkylin.settle.pojo.ParameterInfoOldQuery;

@Component("parameterInfoOldManager")
public class ParameterInfoOldManagerImpl implements ParameterInfoOldManager {
	
	@Autowired
	@Qualifier("parameterInfoOldDao")
	private ParameterInfoOldDao parameterInfoOldDao;
	
	@Override
	public void saveParameterInfo(ParameterInfoOld parameterInfo) {
		if (parameterInfo.getParameterId() == null) {
			parameterInfoOldDao.insertSelective(parameterInfo);
		} else {
			parameterInfoOldDao.updateByPrimaryKeySelective(parameterInfo);
		}
	}
	
	@Override
	public ParameterInfoOld findParameterInfoById(Long id) {
		return parameterInfoOldDao.selectByPrimaryKey(id);
	}
	
	@Override
	public List<ParameterInfoOld> queryList(ParameterInfoOldQuery query) {
		return parameterInfoOldDao.selectByExample(query);
	}
	
	@Override
	public void deleteParameterInfoById(Long id) {
		parameterInfoOldDao.deleteByPrimaryKey(id);
	}
	
	@Override
	public void deleteParameterInfo(ParameterInfoOldQuery query) {
		parameterInfoOldDao.deleteByExample(query);
	}
}

