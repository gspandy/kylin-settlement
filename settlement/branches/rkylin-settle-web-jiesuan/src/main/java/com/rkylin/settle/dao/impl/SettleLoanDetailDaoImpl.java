/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2016
 */

package com.rkylin.settle.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.rkylin.settle.dao.SettleLoanDetailDao;
import com.rkylin.settle.pojo.SettleLoanDetail;
import com.rkylin.settle.pojo.SettleLoanDetailQuery;
import com.rkylin.settle.pojo.SettleTransDetail;
import com.rkylin.settle.pojo.SettleTransDetailQuery;
import com.rkylin.database.BaseDao;

@Repository("settleLoanDetailDao")
public class SettleLoanDetailDaoImpl extends BaseDao implements SettleLoanDetailDao {
	
	@Override
	public int countByExample(SettleLoanDetailQuery example) {
		return super.getSqlSession().selectOne("SettleLoanDetailMapper.countByExample", example);
	}
	
	@Override
	public int deleteByExample(SettleLoanDetailQuery example) {
		return super.getSqlSession().delete("SettleLoanDetailMapper.deleteByExample", example);
	}
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return super.getSqlSession().delete("SettleLoanDetailMapper.deleteByPrimaryKey", id);
	}
	
	@Override
	public int insert(SettleLoanDetail record) {
		return super.getSqlSession().insert("SettleLoanDetailMapper.insert", record);
	}
	
	@Override
	public int insertSelective(SettleLoanDetail record) {
		return super.getSqlSession().insert("SettleLoanDetailMapper.insertSelective", record);
	}
	
	@Override
	public List<SettleLoanDetail> selectByExample(SettleLoanDetailQuery example) {
		return super.getSqlSession().selectList("SettleLoanDetailMapper.selectByExample", example);
	}
	
	@Override
	public SettleLoanDetail selectByPrimaryKey(Long id) {
		return super.getSqlSession().selectOne("SettleLoanDetailMapper.selectByPrimaryKey", id);
	}
	
	@Override
	public int updateByPrimaryKeySelective(SettleLoanDetail record) {
		return super.getSqlSession().update("SettleLoanDetailMapper.updateByPrimaryKeySelective", record);
	}
	
	@Override
	public int updateByPrimaryKey(SettleLoanDetail record) {
		return super.getSqlSession().update("SettleLoanDetailMapper.updateByPrimaryKey", record);
	}
	
	@Override
	public List<Map<String,Object>> selectProfitTransInfo(Map<String, Object> map) {
		return super.getSqlSession().selectList("SettleLoanDetailMapper.selectProfitTransInfo", map);
	}
	
	@Override
	public int updateTransStatusId(Map<String, Object> map) {
		return super.getSqlSession().update("SettleLoanDetailMapper.updateTransStatusId", map);
	}
	
    @Override
    public List<SettleLoanDetail> selectByPreExample(SettleLoanDetailQuery example) {
        return super.getSqlSession().selectList("SettleLoanDetailMapper.selectByPreExample", example);
    }
    
}
