/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.settle.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.rkylin.settle.dao.SettleParameterInfoDao;
import com.rkylin.settle.pojo.SettleParameterInfo;
import com.rkylin.settle.pojo.SettleParameterInfoQuery;
import com.rkylin.database.BaseDao;

@Repository("settleParameterInfoDao")
public class SettleParameterInfoDaoImpl extends BaseDao implements SettleParameterInfoDao {
	
	@Override
	public int countByExample(SettleParameterInfoQuery example) {
		return super.getSqlSession().selectOne("SettleParameterInfoMapper.countByExample", example);
	}
	
	@Override
	public int deleteByExample(SettleParameterInfoQuery example) {
		return super.getSqlSession().delete("SettleParameterInfoMapper.deleteByExample", example);
	}
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return super.getSqlSession().delete("SettleParameterInfoMapper.deleteByPrimaryKey", id);
	}
	
	@Override
	public int insert(SettleParameterInfo record) {
		return super.getSqlSession().insert("SettleParameterInfoMapper.insert", record);
	}
	
	@Override
	public int insertSelective(SettleParameterInfo record) {
		return super.getSqlSession().insert("SettleParameterInfoMapper.insertSelective", record);
	}
	
	@Override
	public List<SettleParameterInfo> selectByExample(SettleParameterInfoQuery example) {
		return super.getSqlSession().selectList("SettleParameterInfoMapper.selectByExample", example);
	}
	
	@Override
	public SettleParameterInfo selectByPrimaryKey(Long id) {
		return super.getSqlSession().selectOne("SettleParameterInfoMapper.selectByPrimaryKey", id);
	}
	
	@Override
	public int updateByPrimaryKeySelective(SettleParameterInfo record) {
		return super.getSqlSession().update("SettleParameterInfoMapper.updateByPrimaryKeySelective", record);
	}
	
	@Override
	public int updateByPrimaryKey(SettleParameterInfo record) {
		return super.getSqlSession().update("SettleParameterInfoMapper.updateByPrimaryKey", record);
	}

    @Override
    public List<SettleParameterInfo> selectByPreExample(SettleParameterInfoQuery example) {
        return super.getSqlSession().selectList("SettleParameterInfoMapper.selectByPreExample", example);
    }

}
