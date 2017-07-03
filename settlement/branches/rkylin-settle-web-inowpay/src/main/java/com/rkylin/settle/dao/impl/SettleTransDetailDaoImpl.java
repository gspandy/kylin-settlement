/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.settle.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.rkylin.settle.dao.SettleTransDetailDao;
import com.rkylin.settle.pojo.SettleTransDetail;
import com.rkylin.settle.pojo.SettleTransDetailQuery;
import com.rkylin.database.BaseDao;

@Repository("settleTransDetailDao")
public class SettleTransDetailDaoImpl extends BaseDao implements SettleTransDetailDao {
	
	@Override
	public int countByExample(SettleTransDetailQuery example) {
		return super.getSqlSession().selectOne("SettleTransDetailMapper.countByExample", example);
	}
	
	@Override
	public int deleteByExample(SettleTransDetailQuery example) {
		return super.getSqlSession().delete("SettleTransDetailMapper.deleteByExample", example);
	}
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return super.getSqlSession().delete("SettleTransDetailMapper.deleteByPrimaryKey", id);
	}
	
	@Override
	public int insert(SettleTransDetail record) {
		return super.getSqlSession().insert("SettleTransDetailMapper.insert", record);
	}
	
	@Override
	public int insertSelective(SettleTransDetail record) {
		return super.getSqlSession().insert("SettleTransDetailMapper.insertSelective", record);
	}
	
	@Override
	public List<SettleTransDetail> selectByExample(SettleTransDetailQuery example) {
		return super.getSqlSession().selectList("SettleTransDetailMapper.selectByExample", example);
	}
	
	@Override
	public SettleTransDetail selectByPrimaryKey(Long id) {
		return super.getSqlSession().selectOne("SettleTransDetailMapper.selectByPrimaryKey", id);
	}
	
	@Override
	public int updateByPrimaryKeySelective(SettleTransDetail record) {
		return super.getSqlSession().update("SettleTransDetailMapper.updateByPrimaryKeySelective", record);
	}
	
	@Override
	public int updateByPrimaryKey(SettleTransDetail record) {
		return super.getSqlSession().update("SettleTransDetailMapper.updateByPrimaryKey", record);
	}
	
	@Override
	public List<Map<String,Object>> selectListMapByExample(SettleTransDetailQuery example){
		return super.getSqlSession().selectList("SettleTransDetailMapper.selectListMapByExample", example);
	}
	
	@Override
	public int updateTransStatusId(Map<String, Object> map) {
		return super.getSqlSession().update("SettleTransDetailMapper.updateTransStatusId", map);
	}
	
	@Override
	public List<Map<String,Object>> selectCollateTransInfo(Map<String, Object> map) {
		return super.getSqlSession().selectList("SettleTransDetailMapper.selectCollateTransInfo", map);
	}
	
	@Override
	public List<Map<String,Object>> selectProfitTransInfo(Map<String, Object> map) {
		return super.getSqlSession().selectList("SettleTransDetailMapper.selectProfitTransInfo", map);
	}

    @Override
    public List<SettleTransDetail> selectByPreExample(SettleTransDetailQuery example) {
        return super.getSqlSession().selectList("SettleTransDetailMapper.selectByPreExample", example);
    }
    
    @Override
    public List<SettleTransDetail> selectByIds(Map<String, Object> example) {
    	return super.getSqlSession().selectList("SettleTransDetailMapper.selectByIds", example);
    }
}
