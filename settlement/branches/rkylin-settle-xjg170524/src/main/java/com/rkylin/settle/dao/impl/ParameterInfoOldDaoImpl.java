/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.settle.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.rkylin.settle.common.AccountOldBaseDao;
import com.rkylin.settle.dao.ParameterInfoOldDao;
import com.rkylin.settle.pojo.ParameterInfo;
import com.rkylin.settle.pojo.ParameterInfoOld;
import com.rkylin.settle.pojo.ParameterInfoOldQuery;

@Repository("parameterInfoOldDao")
public class ParameterInfoOldDaoImpl extends AccountOldBaseDao implements ParameterInfoOldDao {
	
	@Override
	public int countByExample(ParameterInfoOldQuery example) {
		return super.getSqlSession().selectOne("ParameterInfoOldMapper.countByExample", example);
	}
	
	@Override
	public int deleteByExample(ParameterInfoOldQuery example) {
		return super.getSqlSession().delete("ParameterInfoOldMapper.deleteByExample", example);
	}
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return super.getSqlSession().delete("ParameterInfoOldMapper.deleteByPrimaryKey", id);
	}
	
	@Override
	public int insert(ParameterInfoOld record) {
		return super.getSqlSession().insert("ParameterInfoOldMapper.insert", record);
	}
	
	@Override
	public int insertSelective(ParameterInfoOld record) {
		return super.getSqlSession().insert("ParameterInfoOldMapper.insertSelective", record);
	}
	
	@Override
	public List<ParameterInfoOld> selectByExample(ParameterInfoOldQuery example) {
		return super.getSqlSession().selectList("ParameterInfoOldMapper.selectByExample", example);
	}
	
	@Override
	public ParameterInfoOld selectByPrimaryKey(Long id) {
		return super.getSqlSession().selectOne("ParameterInfoOldMapper.selectByPrimaryKey", id);
	}
	
	@Override
	public int updateByPrimaryKeySelective(ParameterInfoOld record) {
		return super.getSqlSession().update("ParameterInfoOldMapper.updateByPrimaryKeySelective", record);
	}
	
	@Override
	public int updateByPrimaryKey(ParameterInfoOld record) {
		return super.getSqlSession().update("ParameterInfoOldMapper.updateByPrimaryKey", record);
	}
	
}
