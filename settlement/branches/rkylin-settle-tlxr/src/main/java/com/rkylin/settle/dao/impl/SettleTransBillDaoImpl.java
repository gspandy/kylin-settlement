/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.settle.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.rkylin.settle.dao.SettleTransBillDao;
import com.rkylin.settle.pojo.SettleTransBill;
import com.rkylin.settle.pojo.SettleTransBillQuery;
import com.rkylin.database.BaseDao;

@Repository("settleTransBillDao")
public class SettleTransBillDaoImpl extends BaseDao implements SettleTransBillDao {
	
	@Override
	public int countByExample(SettleTransBillQuery example) {
		return super.getSqlSession().selectOne("SettleTransBillMapper.countByExample", example);
	}
	
	@Override
	public int deleteByExample(SettleTransBillQuery example) {
		return super.getSqlSession().delete("SettleTransBillMapper.deleteByExample", example);
	}
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return super.getSqlSession().delete("SettleTransBillMapper.deleteByPrimaryKey", id);
	}
	
	@Override
	public int insert(SettleTransBill record) {
		return super.getSqlSession().insert("SettleTransBillMapper.insert", record);
	}
	
	@Override
	public int insertSelective(SettleTransBill record) {
		return super.getSqlSession().insert("SettleTransBillMapper.insertSelective", record);
	}
	
	@Override
	public List<SettleTransBill> selectByExample(SettleTransBillQuery example) {
		return super.getSqlSession().selectList("SettleTransBillMapper.selectByExample", example);
	}
	
	@Override
	public List<SettleTransBill> selectOrderByCreatedTime(SettleTransBillQuery example) {
		return super.getSqlSession().selectList("SettleTransBillMapper.selectOrderByCreatedTime", example);
	} 
	
	@Override
	public SettleTransBill selectByPrimaryKey(Long id) {
		return super.getSqlSession().selectOne("SettleTransBillMapper.selectByPrimaryKey", id);
	}
	
	@Override
	public SettleTransBill selectUniqueBill(SettleTransBillQuery example) {
		return super.getSqlSession().selectOne("SettleTransBillMapper.selectUniqueBill", example);
	}
	
	@Override
	public int updateByPrimaryKeySelective(SettleTransBill record) {
		return super.getSqlSession().update("SettleTransBillMapper.updateByPrimaryKeySelective", record);
	}
	
	@Override
	public int updateByPrimaryKey(SettleTransBill record) {
		return super.getSqlSession().update("SettleTransBillMapper.updateByPrimaryKey", record);
	}

	@Override
	public List<SettleTransBill> queryloanByCreatedTime(Map<String, Object> example) {
		return super.getSqlSession().selectList("SettleTransBillMapper.queryloanByCreatedTime", example);
	}
}
