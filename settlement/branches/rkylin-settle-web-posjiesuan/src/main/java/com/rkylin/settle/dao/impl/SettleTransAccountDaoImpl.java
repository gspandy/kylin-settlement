/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.settle.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.rkylin.database.BaseDao;
import com.rkylin.settle.dao.SettleTransAccountDao;
import com.rkylin.settle.pojo.SettleTransAccount;
import com.rkylin.settle.pojo.SettleTransAccountQuery;

@Repository("settleTransAccountDao")
public class SettleTransAccountDaoImpl extends BaseDao implements SettleTransAccountDao {
	
	@Override
	public int countByExample(SettleTransAccountQuery example) {
		return super.getSqlSession().selectOne("SettleTransAccountMapper.countByExample", example);
	}
	
	@Override
	public int deleteByExample(SettleTransAccountQuery example) {
		return super.getSqlSession().delete("SettleTransAccountMapper.deleteByExample", example);
	}
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return super.getSqlSession().delete("SettleTransAccountMapper.deleteByPrimaryKey", id);
	}
	
	@Override
	public int insert(SettleTransAccount record) {
		return super.getSqlSession().insert("SettleTransAccountMapper.insert", record);
	}
	
	@Override
	public int insertSelective(SettleTransAccount record) {
		return super.getSqlSession().insert("SettleTransAccountMapper.insertSelective", record);
	}
	
	@Override
	public List<SettleTransAccount> selectByExample(SettleTransAccountQuery example) {
		return super.getSqlSession().selectList("SettleTransAccountMapper.selectByExample", example);
	}
	
	@Override
	public SettleTransAccount selectByPrimaryKey(Long id) {
		return super.getSqlSession().selectOne("SettleTransAccountMapper.selectByPrimaryKey", id);
	}
	
	@Override
	public int updateByPrimaryKeySelective(SettleTransAccount record) {
		return super.getSqlSession().update("SettleTransAccountMapper.updateByPrimaryKeySelective", record);
	}
	
	@Override
	public int updateByPrimaryKey(SettleTransAccount record) {
		return super.getSqlSession().update("SettleTransAccountMapper.updateByPrimaryKey", record);
	}
	
	@Override
	public List<Map<String,Object>> selectListMapByExample(SettleTransAccountQuery example){
		return super.getSqlSession().selectList("SettleTransAccountMapper.selectListMapByExample", example);
	}
	
	@Override
	public int updateTransStatusId(Map<String, Object> map) {
		return super.getSqlSession().update("SettleTransAccountMapper.updateTransStatusId", map);
	}
	
	@Override
	public int insertSelectivebyMap(Map<String,Object> record) {
		return super.getSqlSession().insert("SettleTransAccountMapper.insertSelectivebyMap", record);
	}

    @Override
    public List<SettleTransAccount> selectByPreExample(SettleTransAccountQuery example) {
        return super.getSqlSession().selectList("SettleTransAccountMapper.selectByPreExample", example);
    }
	@Override
	public List<SettleTransAccount> summaryByExample(SettleTransAccountQuery example) {
		return super.getSqlSession().selectList("SettleTransAccountMapper.summaryByExample", example);
	}
}
