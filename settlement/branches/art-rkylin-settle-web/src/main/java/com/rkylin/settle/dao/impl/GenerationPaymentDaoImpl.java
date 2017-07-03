/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2016
 */

package com.rkylin.settle.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.rkylin.settle.common.AccountBaseDao;
import com.rkylin.settle.common.AccountOldBaseDao;
import com.rkylin.settle.dao.GenerationPaymentDao;
import com.rkylin.settle.pojo.GenerationPayment;
import com.rkylin.settle.pojo.GenerationPaymentQuery;

@Repository("generationPaymentDao")
public class GenerationPaymentDaoImpl extends AccountOldBaseDao implements GenerationPaymentDao {
	
	@Override
	public int countByExample(GenerationPaymentQuery example) {
		return super.getSqlSession().selectOne("GenerationPaymentMapper.countByExample", example);
	}
	
	@Override
	public int deleteByExample(GenerationPaymentQuery example) {
		return super.getSqlSession().delete("GenerationPaymentMapper.deleteByExample", example);
	}
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return super.getSqlSession().delete("GenerationPaymentMapper.deleteByPrimaryKey", id);
	}
	
	@Override
	public int insert(GenerationPayment record) {
		return super.getSqlSession().insert("GenerationPaymentMapper.insert", record);
	}
	
	@Override
	public int insertSelective(GenerationPayment record) {
		return super.getSqlSession().insert("GenerationPaymentMapper.insertSelective", record);
	}
	
	@Override
	public List<GenerationPayment> selectByExample(GenerationPaymentQuery example) {
		return super.getSqlSession().selectList("GenerationPaymentMapper.selectByExample", example);
	}
	
	@Override
	public GenerationPayment selectByPrimaryKey(Long id) {
		return super.getSqlSession().selectOne("GenerationPaymentMapper.selectByPrimaryKey", id);
	}
	
	@Override
	public int updateByPrimaryKeySelective(GenerationPayment record) {
		return super.getSqlSession().update("GenerationPaymentMapper.updateByPrimaryKeySelective", record);
	}
	
	@Override
	public int updateByPrimaryKey(GenerationPayment record) {
		return super.getSqlSession().update("GenerationPaymentMapper.updateByPrimaryKey", record);
	}

	@Override
	public List<GenerationPayment> selectByPreExample(GenerationPaymentQuery example) {		
		return super.getSqlSession().selectList("GenerationPaymentMapper.selectByPreExample", example);
	}
}
