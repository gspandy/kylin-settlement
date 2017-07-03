/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2017
 */

package com.rkylin.settle.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.rkylin.settle.dao.SettlewebAccountsumCompareDao;
import com.rkylin.settle.pojo.SettlewebAccountsumCompare;
import com.rkylin.settle.pojo.SettlewebAccountsumCompareQuery;
import com.rkylin.settle.util.PagerModel;
import com.rkylin.database.BaseDao;

@Repository("settlewebAccountsumCompareDao")
public class SettlewebAccountsumCompareDaoImpl extends BaseDao implements SettlewebAccountsumCompareDao {
	
	@Override
	public int countByExample(SettlewebAccountsumCompareQuery example) {
		return super.getSqlSession().selectOne("SettlewebAccountsumCompareMapper.countByExample", example);
	}
	
	@Override
	public int deleteByExample(SettlewebAccountsumCompareQuery example) {
		return super.getSqlSession().delete("SettlewebAccountsumCompareMapper.deleteByExample", example);
	}
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return super.getSqlSession().delete("SettlewebAccountsumCompareMapper.deleteByPrimaryKey", id);
	}
	
	@Override
	public int insert(SettlewebAccountsumCompare record) {
		return super.getSqlSession().insert("SettlewebAccountsumCompareMapper.insert", record);
	}
	
	@Override
	public int insertSelective(SettlewebAccountsumCompare record) {
		return super.getSqlSession().insert("SettlewebAccountsumCompareMapper.insertSelective", record);
	}
	
	@Override
	public List<SettlewebAccountsumCompare> selectByExample(SettlewebAccountsumCompareQuery example) {
		return super.getSqlSession().selectList("SettlewebAccountsumCompareMapper.selectByExample", example);
	}
	
	@Override
	public SettlewebAccountsumCompare selectByPrimaryKey(Long id) {
		return super.getSqlSession().selectOne("SettlewebAccountsumCompareMapper.selectByPrimaryKey", id);
	}
	
	@Override
	public int updateByPrimaryKeySelective(SettlewebAccountsumCompare record) {
		return super.getSqlSession().update("SettlewebAccountsumCompareMapper.updateByPrimaryKeySelective", record);
	}
	
	@Override
	public int updateByPrimaryKey(SettlewebAccountsumCompare record) {
		return super.getSqlSession().update("SettlewebAccountsumCompareMapper.updateByPrimaryKey", record);
	}
	
	@Override
	public List<SettlewebAccountsumCompare> selectByPreExample(SettlewebAccountsumCompareQuery example) {
		return super.getSqlSession().selectList("SettlewebAccountsumCompareMapper.selectByPreExample", example);
	}
}
