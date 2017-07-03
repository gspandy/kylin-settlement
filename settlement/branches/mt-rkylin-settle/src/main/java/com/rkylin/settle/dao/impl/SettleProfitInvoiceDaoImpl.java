/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2016
 */

package com.rkylin.settle.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.rkylin.settle.dao.SettleProfitInvoiceDao;
import com.rkylin.settle.pojo.SettleProfitInvoice;
import com.rkylin.settle.pojo.SettleProfitInvoiceQuery;
import com.rkylin.database.BaseDao;

@Repository("settleProfitInvoiceDao")
public class SettleProfitInvoiceDaoImpl extends BaseDao implements SettleProfitInvoiceDao {
	
	@Override
	public int countByExample(SettleProfitInvoiceQuery example) {
		return super.getSqlSession().selectOne("SettleProfitInvoiceMapper.countByExample", example);
	}
	
	@Override
	public int deleteByExample(SettleProfitInvoiceQuery example) {
		return super.getSqlSession().delete("SettleProfitInvoiceMapper.deleteByExample", example);
	}
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return super.getSqlSession().delete("SettleProfitInvoiceMapper.deleteByPrimaryKey", id);
	}
	
	@Override
	public int insert(SettleProfitInvoice record) {
		return super.getSqlSession().insert("SettleProfitInvoiceMapper.insert", record);
	}
	
	@Override
	public int insertSelective(SettleProfitInvoice record) {
		return super.getSqlSession().insert("SettleProfitInvoiceMapper.insertSelective", record);
	}
	
	@Override
	public List<SettleProfitInvoice> selectByExample(SettleProfitInvoiceQuery example) {
		return super.getSqlSession().selectList("SettleProfitInvoiceMapper.selectByExample", example);
	}
	
	@Override
	public SettleProfitInvoice selectByPrimaryKey(Long id) {
		return super.getSqlSession().selectOne("SettleProfitInvoiceMapper.selectByPrimaryKey", id);
	}
	
	@Override
	public int updateByPrimaryKeySelective(SettleProfitInvoice record) {
		return super.getSqlSession().update("SettleProfitInvoiceMapper.updateByPrimaryKeySelective", record);
	}
	
	@Override
	public int updateByPrimaryKey(SettleProfitInvoice record) {
		return super.getSqlSession().update("SettleProfitInvoiceMapper.updateByPrimaryKey", record);
	}
	@Override
	public List<SettleProfitInvoice> selectInvoiceData(SettleProfitInvoiceQuery example) {
		return super.getSqlSession().selectList("SettleProfitInvoiceMapper.selectInvoiceData", example);
	}
	@Override
	public List<SettleProfitInvoice> selectInvoiceDataByMap(Map<String, Object> example) {
		return super.getSqlSession().selectList("SettleProfitInvoiceMapper.selectInvoiceDataByMap", example);
	}
}
