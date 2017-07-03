package com.rkylin.settle.dao.impl;

import java.util.List;
import org.springframework.stereotype.Repository;
import com.rkylin.database.BaseDao;
import com.rkylin.settle.dao.SettleTransSummaryDao;
import com.rkylin.settle.pojo.SettleTransSummary;
import com.rkylin.settle.pojo.SettleTransSummaryQuery;

@Repository("settleTransSummaryDao")
public class SettleTransSummaryDaoImpl extends BaseDao implements SettleTransSummaryDao {
	  @Override
	    public List<SettleTransSummary> selectByPreExample(SettleTransSummaryQuery example) {
	        return super.getSqlSession().selectList("SettleTransSummaryMapper.selectByPreExample", example);
	    }
	  
		@Override
		public int countByExample(SettleTransSummaryQuery example) {
			return super.getSqlSession().selectOne("SettleTransSummaryMapper.countByExample", example);
		}
}
