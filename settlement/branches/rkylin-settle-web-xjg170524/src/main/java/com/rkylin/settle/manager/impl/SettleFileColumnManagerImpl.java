/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.settle.manager.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.rkylin.settle.dao.SettleFileColumnDao;
import com.rkylin.settle.manager.SettleFileColumnManager;
import com.rkylin.settle.pojo.SettleFileColumn;
import com.rkylin.settle.pojo.SettleFileColumnQuery;

@Component("settleFileColumnManager")
public class SettleFileColumnManagerImpl implements SettleFileColumnManager {
	
	@Autowired
	@Qualifier("settleFileColumnDao")
	private SettleFileColumnDao settleFileColumnDao;
	
	@Override
	public int saveSettleFileColumn(SettleFileColumn settleFileColumn) {
		if (settleFileColumn.getFileColumnId() == null) {
		    settleFileColumn.setCreatedTime(new Date());
			return settleFileColumnDao.insertSelective(settleFileColumn);
		} else {
		    settleFileColumn.setUpdatedTime(new Date());
		    return settleFileColumnDao.updateByPrimaryKeySelective(settleFileColumn);
		}
	}
	
	@Override
	public SettleFileColumn findSettleFileColumnById(Long id) {
		return settleFileColumnDao.selectByPrimaryKey(id);
	}
	
	@Override
	public List<SettleFileColumn> queryList(SettleFileColumnQuery query) {
		return settleFileColumnDao.selectByExample(query);
	}
	
	@Override
	public Integer deleteSettleFileColumnById(Long id) {
		return settleFileColumnDao.deleteByPrimaryKey(id);
	}
	
	@Override
	public void deleteSettleFileColumn(SettleFileColumnQuery query) {
		settleFileColumnDao.deleteByExample(query);
	}

    @Override
    public List<SettleFileColumn> queryPage(SettleFileColumnQuery query) {
        return settleFileColumnDao.selectByPreExample(query);
    }

    @Override
    public int countByExample(SettleFileColumnQuery query) {
        return settleFileColumnDao.countByExample(query);
    }

    @Override
    public List<SettleFileColumn> queryDefaultAndByFileSubId(SettleFileColumnQuery query) {
        return settleFileColumnDao.queryDefaultAndByFileSubId(query);
    }

    @Override
    public int updateFileSubIdByMatch(SettleFileColumn record) {
        return settleFileColumnDao.updateByPrimaryKeySelective(record);
    }
}

