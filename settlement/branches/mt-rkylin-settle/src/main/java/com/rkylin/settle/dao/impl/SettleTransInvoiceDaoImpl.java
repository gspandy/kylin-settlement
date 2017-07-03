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
	public int countByExample(SettleTransInvoiceQuery example) {
		return super.getSqlSession().selectOne("SettleTransInvoiceMapper.countByExample", example);
	}
	
	@Override
	public int deleteByExample(SettleTransInvoiceQuery example) {
		return super.getSqlSession().delete("SettleTransInvoiceMapper.deleteByExample", example);
	}
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return super.getSqlSession().delete("SettleTransInvoiceMapper.deleteByPrimaryKey", id);
	}
	
	@Override
	public int insert(SettleTransInvoice record) {
		return super.getSqlSession().insert("SettleTransInvoiceMapper.insert", record);
	}
	
	@Override
	public int insertSelective(SettleTransInvoice record) {
		return super.getSqlSession().insert("SettleTransInvoiceMapper.insertSelective", record);
	}
	
	@Override
	public List<SettleTransInvoice> selectByExample(SettleTransInvoiceQuery example) {
		return super.getSqlSession().selectList("SettleTransInvoiceMapper.selectByExample", example);
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
	public int updateByPrimaryKey(SettleTransInvoice record) {
		try{
			super.getSqlSession().update("SettleTransInvoiceMapper.updateByPrimaryKey", record);
		}catch(Exception e){
			logger.error(e);
		}
		return 0;
	}

	@Override
	public List<SettleTransInvoice> selectTransInvoiceList(
			Map<String, Object> map) {
		return super.getSqlSession().selectList("SettleTransInvoiceMapper.selectTransInvoiceList", map);
	}

	@Override
	public int queryTotalByExample(SettleTransInvoiceQuery example) {
		return super.getSqlSession().selectOne("SettleTransInvoiceMapper.countByExample", example);
	}
	
	@Override
	public long sumByExample(Map<String, Object> map) {
		return super.getSqlSession().selectOne("SettleTransInvoiceMapper.sumByExample", map);
	}
	
	
	@Override
	public long countByDfExample(Map<String, Object> map) {
		return super.getSqlSession().selectOne("SettleTransInvoiceMapper.countByDfExample", map);
	}
	
	@Override
	public int removeToHisProc(Map<String, Object> map) {
		try{
			super.getSqlSession().selectOne("SettleTransInvoiceMapper.setgeneration", map);
		}catch(Exception e){
			logger.error("代收付业务移入历史的存储过程发生异常，异常信息："+e);
		}
		return 0;
		
	}
	
	@Override
	public int batchInsertTransInvoice(List<SettleTransInvoice> example) {
		return super.getSqlSession().insert("SettleTransInvoiceMapper.batchInsertTransInvoice", example);
	}
}
