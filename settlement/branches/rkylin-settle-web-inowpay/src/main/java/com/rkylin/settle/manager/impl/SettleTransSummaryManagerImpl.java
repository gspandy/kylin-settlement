package com.rkylin.settle.manager.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import com.rkylin.settle.dao.SettleTransSummaryDao;
import com.rkylin.settle.manager.SettleTransSummaryManager;
import com.rkylin.settle.pojo.SettleTransSummary;
import com.rkylin.settle.pojo.SettleTransSummaryQuery;


@Component("settleTransSummaryManager")
public class SettleTransSummaryManagerImpl implements SettleTransSummaryManager{
	@Autowired
	@Qualifier("settleTransSummaryDao")
	private SettleTransSummaryDao settleTransSummaryDao;
	
    @Override
    public List<SettleTransSummary> queryPage(SettleTransSummaryQuery query) {
        return settleTransSummaryDao.selectByPreExample(query);
    }
    
    @Override
    public int countByExample(SettleTransSummaryQuery query) {
        return settleTransSummaryDao.countByExample(query);
    }
}

