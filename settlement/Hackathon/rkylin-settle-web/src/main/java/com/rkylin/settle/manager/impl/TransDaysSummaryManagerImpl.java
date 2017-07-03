/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.settle.manager.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.rkylin.settle.dao.TransDaysSummaryDao;
import com.rkylin.settle.manager.TransDaysSummaryManager;
import com.rkylin.settle.pojo.TransDaysSummary;
import com.rkylin.settle.pojo.TransDaysSummaryQuery;

@Component("transDaysSummaryManager")
public class TransDaysSummaryManagerImpl implements TransDaysSummaryManager {
	
	@Autowired
	@Qualifier("transDaysSummaryDao")
	private TransDaysSummaryDao transDaysSummaryDao;
	
	@Override
	public void saveTransDaysSummary(TransDaysSummary transDaysSummary) {
		transDaysSummaryDao.insertSelective(transDaysSummary);
	}
	
	@Override
	public TransDaysSummary findTransDaysSummaryById(String id) {
		return transDaysSummaryDao.selectByPrimaryKey(id);
	}
	
	@Override
	public List<TransDaysSummary> queryList(TransDaysSummaryQuery query) {
		return transDaysSummaryDao.selectByExample(query);
	}
	
	@Override
	public void deleteTransDaysSummaryById(Long id) {
		transDaysSummaryDao.deleteByPrimaryKey(id);
	}
	
	@Override
	public void deleteTransDaysSummary(TransDaysSummaryQuery query) {
		transDaysSummaryDao.deleteByExample(query);
	}

	@Override
	public void updateTransDaysSummary(TransDaysSummary query) {
		transDaysSummaryDao.updateByPrimaryKeySelective(query);
	}

	@Override
	public List<Map<String,Object>> findDateAll(Map<String,String> query) {
		return transDaysSummaryDao.findDateAll(query);
	}
}

