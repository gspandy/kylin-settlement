/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.settle.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.rkylin.settle.dao.SettleProfitKeyDao;
import com.rkylin.settle.pojo.SettleProfitKey;
import com.rkylin.settle.pojo.SettleProfitKeyQuery;
import com.rkylin.database.BaseDao;

@Repository("settleProfitKeyDao")
public class SettleProfitKeyDaoImpl extends BaseDao implements SettleProfitKeyDao {
	
	@Override
	public int countByExample(SettleProfitKeyQuery example) {
		return super.getSqlSession().selectOne("SettleProfitKeyMapper.countByExample", example);
	}
	
	@Override
	public int deleteByExample(SettleProfitKeyQuery example) {
		return super.getSqlSession().delete("SettleProfitKeyMapper.deleteByExample", example);
	}
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return super.getSqlSession().delete("SettleProfitKeyMapper.deleteByPrimaryKey", id);
	}
	
	@Override
	public int insert(SettleProfitKey record) {
		return super.getSqlSession().insert("SettleProfitKeyMapper.insert", record);
	}
	
	@Override
	public int insertSelective(SettleProfitKey record) {
		return super.getSqlSession().insert("SettleProfitKeyMapper.insertSelective", record);
	}
	
	@Override
	public List<SettleProfitKey> selectByExample(SettleProfitKeyQuery example) {
		return super.getSqlSession().selectList("SettleProfitKeyMapper.selectByExample", example);
	}
	
	@Override
	public List<SettleProfitKey> selectAllProfitKey(SettleProfitKeyQuery example) {
		return super.getSqlSession().selectList("SettleProfitKeyMapper.selectAllProfitKey", example);
	}
	
	@Override
	public SettleProfitKey selectByPrimaryKey(Long id) {
		return super.getSqlSession().selectOne("SettleProfitKeyMapper.selectByPrimaryKey", id);
	}
	
	@Override
	public int updateByPrimaryKeySelective(SettleProfitKey record) {
		return super.getSqlSession().update("SettleProfitKeyMapper.updateByPrimaryKeySelective", record);
	}
	
	@Override
	public int updateByPrimaryKey(SettleProfitKey record) {
		return super.getSqlSession().update("SettleProfitKeyMapper.updateByPrimaryKey", record);
	}

    @Override
    public List<SettleProfitKey> selectByPreExample(SettleProfitKeyQuery example) {
        return super.getSqlSession().selectList("SettleProfitKeyMapper.selectByPreExample", example);
    }
	
}
