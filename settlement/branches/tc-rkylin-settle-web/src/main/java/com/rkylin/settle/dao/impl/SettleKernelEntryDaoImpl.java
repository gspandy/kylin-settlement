/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2016
 */

package com.rkylin.settle.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.rkylin.database.BaseDao;
import com.rkylin.settle.dao.SettleKernelEntryDao;
import com.rkylin.settle.pojo.SettleKernelEntry;
import com.rkylin.settle.pojo.SettleKernelEntryQuery;

@Repository("settleKernelEntryDao")
public class SettleKernelEntryDaoImpl extends BaseDao implements SettleKernelEntryDao {
	
	@Override
	public int countByExample(SettleKernelEntryQuery example) {
		return super.getSqlSession().selectOne("SettleKernelEntryMapper.countByExample", example);
	}
	
	@Override
	public int deleteByExample(SettleKernelEntryQuery example) {
		return super.getSqlSession().delete("SettleKernelEntryMapper.deleteByExample", example);
	}
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return super.getSqlSession().delete("SettleKernelEntryMapper.deleteByPrimaryKey", id);
	}
	
	@Override
	public int insert(SettleKernelEntry record) {
		return super.getSqlSession().insert("SettleKernelEntryMapper.insert", record);
	}
	
	@Override
	public int insertSelective(SettleKernelEntry record) {
		return super.getSqlSession().insert("SettleKernelEntryMapper.insertSelective", record);
	}
	
	@Override
	public List<SettleKernelEntry> selectByExample(SettleKernelEntryQuery example) {
		return super.getSqlSession().selectList("SettleKernelEntryMapper.selectByExample", example);
	}
	
	@Override
	public List<SettleKernelEntry> selectByPreExample(SettleKernelEntryQuery example) {
		return super.getSqlSession().selectList("SettleKernelEntryMapper.selectByPreExample", example);
	}
	
	@Override
	public SettleKernelEntry selectByPrimaryKey(Long id) {
		return super.getSqlSession().selectOne("SettleKernelEntryMapper.selectByPrimaryKey", id);
	}
	
	@Override
	public int updateByPrimaryKeySelective(SettleKernelEntry record) {
		return super.getSqlSession().update("SettleKernelEntryMapper.updateByPrimaryKeySelective", record);
	}
	
	@Override
	public int updateByPrimaryKey(SettleKernelEntry record) {
		return super.getSqlSession().update("SettleKernelEntryMapper.updateByPrimaryKey", record);
	}
	
}
