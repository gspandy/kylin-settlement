package com.rkylin.settle.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.rkylin.database.BaseDao;
import com.rkylin.settle.dao.SettleTransInvoiceHisDao;
import com.rkylin.settle.pojo.SettleTransInvoiceHis;
import com.rkylin.settle.pojo.SettleTransInvoiceHisQuery;

@Repository("settleTransInvoiceHisDao")
public class SettleTransInvoiceHisDaoImpl extends BaseDao implements SettleTransInvoiceHisDao {
	
	@Override
	public int countByExample(SettleTransInvoiceHisQuery example) {
		return super.getSqlSession().selectOne("SettleTransInvoiceHisMapper.countByExample", example);
	}
	
	@Override
	public int deleteByExample(SettleTransInvoiceHisQuery example) {
		return super.getSqlSession().delete("SettleTransInvoiceHisMapper.deleteByExample", example);
	}
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return super.getSqlSession().delete("SettleTransInvoiceHisMapper.deleteByPrimaryKey", id);
	}
	
	@Override
	public int insert(SettleTransInvoiceHis record) {
		return super.getSqlSession().insert("SettleTransInvoiceHisMapper.insert", record);
	}
	
	@Override
	public int insertSelective(SettleTransInvoiceHis record) {
		return super.getSqlSession().insert("SettleTransInvoiceHisMapper.insertSelective", record);
	}
	
	@Override
	public List<SettleTransInvoiceHis> selectByExample(SettleTransInvoiceHisQuery example) {
		return super.getSqlSession().selectList("SettleTransInvoiceHisMapper.selectByExample", example);
	}
	
	@Override
	public SettleTransInvoiceHis selectByPrimaryKey(Long id) {
		return super.getSqlSession().selectOne("SettleTransInvoiceHisMapper.selectByPrimaryKey", id);
	}
	
	@Override
	public int updateByPrimaryKeySelective(SettleTransInvoiceHis record) {
		return super.getSqlSession().update("SettleTransInvoiceHisMapper.updateByPrimaryKeySelective", record);
	}
	
	@Override
	public int updateByPrimaryKey(SettleTransInvoiceHis record) {
		return super.getSqlSession().update("SettleTransInvoiceHisMapper.updateByPrimaryKey", record);
	}

	@Override
	public List<SettleTransInvoiceHis> selectTransInvoiceList(
			Map<String, Object> map) {
		return super.getSqlSession().selectList("SettleTransInvoiceHisMapper.selectTransInvoiceList", map);
	}

	@Override
	public int queryTotalByExample(SettleTransInvoiceHisQuery example) {
		return super.getSqlSession().selectOne("SettleTransInvoiceHisMapper.countByExample", example);
	}
	
	@Override
	public long sumByExample(Map<String, Object> map) {
		return super.getSqlSession().selectOne("SettleTransInvoiceHisMapper.sumByExample", map);
	}
	
	@Override
	public int removeToHisProc(Map<String, Object> map) {
		return super.getSqlSession().selectOne("SettleTransInvoiceHisMapper.setgeneration", map);
	}
}
