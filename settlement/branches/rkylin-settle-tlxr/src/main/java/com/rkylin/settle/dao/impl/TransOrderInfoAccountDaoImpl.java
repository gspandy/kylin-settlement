/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2016
 */

package com.rkylin.settle.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.rkylin.settle.common.AccountOldBaseDao;
import com.rkylin.settle.dao.TransOrderInfoAccountDao;
import com.rkylin.settle.pojo.TransOrderInfoAccount;
import com.rkylin.settle.pojo.TransOrderInfoAccountQuery;

@Repository("transOrderInfoAccountDao")
public class TransOrderInfoAccountDaoImpl extends AccountOldBaseDao implements TransOrderInfoAccountDao {
	
	@Override
	public int countByExample(TransOrderInfoAccountQuery example) {
		return super.getSqlSession().selectOne("TransOrderInfoAccountMapper.countByExample", example);
	}
	
	@Override
	public int deleteByExample(TransOrderInfoAccountQuery example) {
		return super.getSqlSession().delete("TransOrderInfoAccountMapper.deleteByExample", example);
	}
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return super.getSqlSession().delete("TransOrderInfoAccountMapper.deleteByPrimaryKey", id);
	}
	
	@Override
	public int insert(TransOrderInfoAccount record) {
		return super.getSqlSession().insert("TransOrderInfoAccountMapper.insert", record);
	}
	
	@Override
	public int insertSelective(TransOrderInfoAccount record) {
		return super.getSqlSession().insert("TransOrderInfoAccountMapper.insertSelective", record);
	}
	
	@Override
	public List<TransOrderInfoAccount> selectByExample(TransOrderInfoAccountQuery example) {
		return super.getSqlSession().selectList("TransOrderInfoAccountMapper.selectByExample", example);
	}
	
	@Override
	public TransOrderInfoAccount selectByPrimaryKey(Long id) {
		return super.getSqlSession().selectOne("TransOrderInfoAccountMapper.selectByPrimaryKey", id);
	}
	
	@Override
	public int updateByPrimaryKeySelective(TransOrderInfoAccount record) {
		return super.getSqlSession().update("TransOrderInfoAccountMapper.updateByPrimaryKeySelective", record);
	}
	
	@Override
	public int updateByPrimaryKey(TransOrderInfoAccount record) {
		return super.getSqlSession().update("TransOrderInfoAccountMapper.updateByPrimaryKey", record);
	}
	
	@Override
    public List<TransOrderInfoAccount> selectByRequestTime(Map<String, Object> example) {
        return super.getSqlSession().selectList("TransOrderInfoAccountMapper.selectByRequestTime", example);
    }
}
