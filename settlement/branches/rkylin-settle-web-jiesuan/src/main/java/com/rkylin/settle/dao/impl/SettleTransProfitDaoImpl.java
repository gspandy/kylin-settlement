/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.settle.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.rkylin.settle.dao.SettleTransProfitDao;
import com.rkylin.settle.pojo.SettleTransProfit;
import com.rkylin.settle.pojo.SettleTransProfitQuery;
import com.rkylin.database.BaseDao;

@Repository("settleTransProfitDao")
public class SettleTransProfitDaoImpl extends BaseDao implements SettleTransProfitDao {
	
	@Override
	public int countByExample(SettleTransProfitQuery example) {
		return super.getSqlSession().selectOne("SettleTransProfitMapper.countByExample", example);
	}
	
	@Override
	public int deleteByExample(SettleTransProfitQuery example) {
		return super.getSqlSession().delete("SettleTransProfitMapper.deleteByExample", example);
	}
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return super.getSqlSession().delete("SettleTransProfitMapper.deleteByPrimaryKey", id);
	}
	
	@Override
	public int insert(SettleTransProfit record) {
		return super.getSqlSession().insert("SettleTransProfitMapper.insert", record);
	}
	
	@Override
	public int insertSelective(SettleTransProfit record) {
		return super.getSqlSession().insert("SettleTransProfitMapper.insertSelective", record);
	}
	
	@Override
	public List<SettleTransProfit> selectByExample(SettleTransProfitQuery example) {
		return super.getSqlSession().selectList("SettleTransProfitMapper.selectByExample", example);
	}
	
	@Override
	public SettleTransProfit selectByPrimaryKey(Long id) {
		return super.getSqlSession().selectOne("SettleTransProfitMapper.selectByPrimaryKey", id);
	}
	
	@Override
	public int updateByPrimaryKeySelective(SettleTransProfit record) {
		return super.getSqlSession().update("SettleTransProfitMapper.updateByPrimaryKeySelective", record);
	}
	
	@Override
	public int updateByPrimaryKey(SettleTransProfit record) {
		return super.getSqlSession().update("SettleTransProfitMapper.updateByPrimaryKey", record);
	}

    @Override
    public List<SettleTransProfit> selectByPreExample(SettleTransProfitQuery example) {
        return super.getSqlSession().selectList("SettleTransProfitMapper.selectByPreExample", example);
    }

    @Override
    public int updateStatusByOrderNo(SettleTransProfit record) {
        return super.getSqlSession().update("SettleTransProfitMapper.updateStatusByOrderNo", record);
    }
	
}
