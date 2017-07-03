/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.settle.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.rkylin.database.BaseDao;
import com.rkylin.settle.dao.SettleFinanaceAccountDao;
import com.rkylin.settle.pojo.SettleFinanaceAccount;
import com.rkylin.settle.pojo.SettleFinanaceAccountQuery;

@Repository("settleFinanaceAccountDao")
public class SettleFinanaceAccountDaoImpl extends BaseDao implements SettleFinanaceAccountDao {
	
	@Override
	public int countByExample(SettleFinanaceAccountQuery example) {
		return super.getSqlSession().selectOne("SettleFinanaceAccountMapper.countByExample", example);
	}
	
	@Override
	public int deleteByExample(SettleFinanaceAccountQuery example) {
		return super.getSqlSession().delete("SettleFinanaceAccountMapper.deleteByExample", example);
	}
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return super.getSqlSession().delete("SettleFinanaceAccountMapper.deleteByPrimaryKey", id);
	}
	
	@Override
	public int insert(SettleFinanaceAccount record) {
		return super.getSqlSession().insert("SettleFinanaceAccountMapper.insert", record);
	}
	
	@Override
	public int insertSelective(SettleFinanaceAccount record) {
		return super.getSqlSession().insert("SettleFinanaceAccountMapper.insertSelective", record);
	}
	
	@Override
	public List<SettleFinanaceAccount> selectByExample(SettleFinanaceAccountQuery example) {
		return super.getSqlSession().selectList("SettleFinanaceAccountMapper.selectByExample", example);
	}
	
	@Override
	public List<SettleFinanaceAccount> selectByFinAccountId(String[] finAccountIds) {
		return super.getSqlSession().selectList("SettleFinanaceAccountMapper.selectByFinAccountId", finAccountIds);
	}
	
	@Override
	public SettleFinanaceAccount selectByPrimaryKey(Long id) {
		return super.getSqlSession().selectOne("SettleFinanaceAccountMapper.selectByPrimaryKey", id);
	}
	
	@Override
	public int updateByPrimaryKeySelective(SettleFinanaceAccount record) {
		return super.getSqlSession().update("SettleFinanaceAccountMapper.updateByPrimaryKeySelective", record);
	}
	
	@Override
	public int updateByPrimaryKey(SettleFinanaceAccount record) {
		return super.getSqlSession().update("SettleFinanaceAccountMapper.updateByPrimaryKey", record);
	}
	
	//根据机构号和用户id查询其所有产品号
	@Override
	public List<String> selectProductIdList(SettleFinanaceAccountQuery example){
		return super.getSqlSession().selectList("SettleFinanaceAccountMapper.selectProductIdList", example);
	}
	
	@Override
	public int  updateTransStatusId(Map<String, Object> map){
		return super.getSqlSession().update("SettleFinanaceAccountMapper.updateTransStatusId", map);
	}
}
