/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.settle.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.rkylin.settle.common.MtkernelBaseDao;
import com.rkylin.settle.dao.ParameterInfoDao;
import com.rkylin.settle.pojo.ParameterInfo;
import com.rkylin.settle.pojo.ParameterInfoQuery;

@Repository("parameterInfoDao")
public class ParameterInfoDaoImpl extends MtkernelBaseDao implements ParameterInfoDao {
	
	@Override
	public int countByExample(ParameterInfoQuery example) {
		return super.getSqlSession().selectOne("ParameterInfoMapper.countByExample", example);
	}
	
	@Override
	public int deleteByExample(ParameterInfoQuery example) {
		return super.getSqlSession().delete("ParameterInfoMapper.deleteByExample", example);
	}
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return super.getSqlSession().delete("ParameterInfoMapper.deleteByPrimaryKey", id);
	}
	
	@Override
	public int insert(ParameterInfo record) {
		return super.getSqlSession().insert("ParameterInfoMapper.insert", record);
	}
	
	@Override
	public int insertSelective(ParameterInfo record) {
		return super.getSqlSession().insert("ParameterInfoMapper.insertSelective", record);
	}
	
	@Override
	public List<ParameterInfo> selectByExample(ParameterInfoQuery example) {
		return super.getSqlSession().selectList("ParameterInfoMapper.selectByExample", example);
	}
	
	@Override
	public ParameterInfo selectByPrimaryKey(Long id) {
		return super.getSqlSession().selectOne("ParameterInfoMapper.selectByPrimaryKey", id);
	}
	
	@Override
	public int updateByPrimaryKeySelective(ParameterInfo record) {
		return super.getSqlSession().update("ParameterInfoMapper.updateByPrimaryKeySelective", record);
	}
	
	@Override
	public int updateByPrimaryKey(ParameterInfo record) {
		return super.getSqlSession().update("ParameterInfoMapper.updateByPrimaryKey", record);
	}
	
}
