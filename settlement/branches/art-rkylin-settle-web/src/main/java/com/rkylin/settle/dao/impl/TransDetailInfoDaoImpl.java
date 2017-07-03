/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.settle.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.rkylin.settle.common.MultiBaseDao;
import com.rkylin.settle.dao.TransDetailInfoDao;
import com.rkylin.settle.pojo.TransDetailInfo;
import com.rkylin.settle.pojo.TransDetailInfoQuery;

@Repository("transDetailInfoDao")
public class TransDetailInfoDaoImpl extends MultiBaseDao implements TransDetailInfoDao {
	
	@Override
	public int countByExample(TransDetailInfoQuery example) {
		return super.getSqlSession().selectOne("TransDetailInfoMapper.countByExample", example);
	}
	
	@Override
	public int deleteByExample(TransDetailInfoQuery example) {
		return super.getSqlSession().delete("TransDetailInfoMapper.deleteByExample", example);
	}
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return super.getSqlSession().delete("TransDetailInfoMapper.deleteByPrimaryKey", id);
	}
	
	@Override
	public int insert(TransDetailInfo record) {
		return super.getSqlSession().insert("TransDetailInfoMapper.insert", record);
	}
	
	@Override
	public int insertSelective(TransDetailInfo record) {
		return super.getSqlSession().insert("TransDetailInfoMapper.insertSelective", record);
	}
	
	@Override
	public List<TransDetailInfo> selectByExample(TransDetailInfoQuery example) {
		return super.getSqlSession().selectList("TransDetailInfoMapper.selectByExample", example);
	}
	
	@Override
	public TransDetailInfo selectByPrimaryKey(Long id) {
		return super.getSqlSession().selectOne("TransDetailInfoMapper.selectByPrimaryKey", id);
	}
	
	@Override
	public int updateByPrimaryKeySelective(TransDetailInfo record) {
		return super.getSqlSession().update("TransDetailInfoMapper.updateByPrimaryKeySelective", record);
	}
	
	@Override
	public int updateByPrimaryKey(TransDetailInfo record) {
		return super.getSqlSession().update("TransDetailInfoMapper.updateByPrimaryKey", record);
	}
	
}
