/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2016
 */

package com.rkylin.settle.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.rkylin.settle.common.AccountBaseDao;
import com.rkylin.settle.dao.TransOrderInfoDao;
import com.rkylin.settle.pojo.TransOrderInfo;
import com.rkylin.settle.pojo.TransOrderInfoQuery;

@Repository("transOrderInfoDao")
public class TransOrderInfoDaoImpl extends AccountBaseDao implements TransOrderInfoDao {
	
	@Override
	public int countByExample(TransOrderInfoQuery example) {
		return super.getSqlSession().selectOne("TransOrderInfoMapper.countByExample", example);
	}
	
	@Override
	public int deleteByExample(TransOrderInfoQuery example) {
		return super.getSqlSession().delete("TransOrderInfoMapper.deleteByExample", example);
	}
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return super.getSqlSession().delete("TransOrderInfoMapper.deleteByPrimaryKey", id);
	}
	
	@Override
	public int insert(TransOrderInfo record) {
		return super.getSqlSession().insert("TransOrderInfoMapper.insert", record);
	}
	
	@Override
	public int insertSelective(TransOrderInfo record) {
		return super.getSqlSession().insert("TransOrderInfoMapper.insertSelective", record);
	}
	
	@Override
	public List<TransOrderInfo> selectByExample(TransOrderInfoQuery example) {
		return super.getSqlSession().selectList("TransOrderInfoMapper.selectByExample", example);
	}
	
	@Override
	public TransOrderInfo selectByPrimaryKey(Long id) {
		return super.getSqlSession().selectOne("TransOrderInfoMapper.selectByPrimaryKey", id);
	}
	
	@Override
	public int updateByPrimaryKeySelective(TransOrderInfo record) {
		return super.getSqlSession().update("TransOrderInfoMapper.updateByPrimaryKeySelective", record);
	}
	
	@Override
	public int updateByPrimaryKey(TransOrderInfo record) {
		return super.getSqlSession().update("TransOrderInfoMapper.updateByPrimaryKey", record);
	}
	
	@Override
	public List<TransOrderInfo> selectByPreExample(TransOrderInfoQuery example) {		
		return super.getSqlSession().selectList("TransOrderInfoMapper.selectByPreExample", example);
	}
}
