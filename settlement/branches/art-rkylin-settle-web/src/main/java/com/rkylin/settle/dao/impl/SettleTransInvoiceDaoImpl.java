package com.rkylin.settle.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.rkylin.database.BaseDao;
import com.rkylin.settle.dao.SettleTransInvoiceDao;
import com.rkylin.settle.pojo.SettleTransInvoice;
import com.rkylin.settle.pojo.SettleTransInvoiceQuery;

@Repository("settleTransInvoiceDao")
public class SettleTransInvoiceDaoImpl extends BaseDao implements SettleTransInvoiceDao {
	  @Override
	    public List<SettleTransInvoice> selectByPreExample(SettleTransInvoiceQuery example) {
	        return super.getSqlSession().selectList("SettleTransInvoiceMapper.selectByPreExample", example);
	    }
	
		@Override
		public int countByExample(SettleTransInvoiceQuery example) {
			return super.getSqlSession().selectOne("SettleTransInvoiceMapper.countByExample", example);
		}
		
		@Override
		public SettleTransInvoice selectByPrimaryKey(Long id) {
			return super.getSqlSession().selectOne("SettleTransInvoiceMapper.selectByPrimaryKey", id);
		}
		
		@Override
		public int updateByPrimaryKeySelective(SettleTransInvoice record) {
			return super.getSqlSession().update("SettleTransInvoiceMapper.updateByPrimaryKeySelective", record);
		}
		
		@Override
		public int insertSelective(SettleTransInvoice record) {
			return super.getSqlSession().insert("SettleTransInvoiceMapper.insertSelective", record);
		}
		
		@Override
		public int updateTransStatusId(Map<String, Object> map) {
			return super.getSqlSession().update("SettleTransInvoiceMapper.updateTransStatusId", map);
		}
		
		@Override
	    public List<SettleTransInvoice> selectByIds(Map<String, Object> example) {
	    	return super.getSqlSession().selectList("SettleTransInvoiceMapper.selectByIds", example);
	    }
		
		@Override
		public int deleteByPrimaryKey(Long invoiceNo) {
			return super.getSqlSession().delete("SettleTransInvoiceMapper.deleteByPrimaryKey", invoiceNo);
		}
		
		@Override
		public long summaryMoneyByExample(SettleTransInvoiceQuery example) {
			return super.getSqlSession().selectOne("SettleTransInvoiceMapper.sumMoneyByExample", example);
		}
		
}
