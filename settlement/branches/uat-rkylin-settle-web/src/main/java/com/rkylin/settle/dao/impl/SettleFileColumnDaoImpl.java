/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.settle.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.rkylin.database.BaseDao;
import com.rkylin.settle.dao.SettleFileColumnDao;
import com.rkylin.settle.pojo.SettleFileColumn;
import com.rkylin.settle.pojo.SettleFileColumnQuery;

@Repository("settleFileColumnDao")
public class SettleFileColumnDaoImpl extends BaseDao implements SettleFileColumnDao {
	
	@Override
	public int countByExample(SettleFileColumnQuery example) {
		return super.getSqlSession().selectOne("SettleFileColumnMapper.countByExample", example);
	}
	
	@Override
	public int deleteByExample(SettleFileColumnQuery example) {
		return super.getSqlSession().delete("SettleFileColumnMapper.deleteByExample", example);
	}
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return super.getSqlSession().delete("SettleFileColumnMapper.deleteByPrimaryKey", id);
	}
	
	@Override
	public int insert(SettleFileColumn record) {
		return super.getSqlSession().insert("SettleFileColumnMapper.insert", record);
	}
	
	@Override
	public int insertSelective(SettleFileColumn record) {
		return super.getSqlSession().insert("SettleFileColumnMapper.insertSelective", record);
	}
	
	@Override
	public List<SettleFileColumn> selectByExample(SettleFileColumnQuery example) {
		return super.getSqlSession().selectList("SettleFileColumnMapper.selectByExample", example);
	}
	
	@Override
	public SettleFileColumn selectByPrimaryKey(Long id) {
		return super.getSqlSession().selectOne("SettleFileColumnMapper.selectByPrimaryKey", id);
	}
	
	@Override
	public int updateByPrimaryKeySelective(SettleFileColumn record) {
		return super.getSqlSession().update("SettleFileColumnMapper.updateByPrimaryKeySelective", record);
	}
	
	@Override
	public int updateByPrimaryKey(SettleFileColumn record) {
		return super.getSqlSession().update("SettleFileColumnMapper.updateByPrimaryKey", record);
	}

    @Override
    public List<SettleFileColumn> selectByPreExample(SettleFileColumnQuery example) {
        return super.getSqlSession().selectList("SettleFileColumnMapper.selectByPreExample", example);
    }

    @Override
    public List<SettleFileColumn> queryDefaultAndByFileSubId(SettleFileColumnQuery query) {
        return super.getSqlSession().selectList("SettleFileColumnMapper.queryDefaultAndByFileSubId", query);
    }
	
}
