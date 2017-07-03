/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2016
 */

package com.rkylin.settle.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.rkylin.settle.dao.SettlePosDetailDao;
import com.rkylin.settle.pojo.SettlePosDetail;
import com.rkylin.settle.pojo.SettlePosDetailQuery;
import com.rkylin.settle.pojo.SettleTransDetail;
import com.rkylin.settle.pojo.SettleTransDetailQuery;
import com.rkylin.database.BaseDao;

@Repository("settlePosDetailDao")
public class SettlePosDetailDaoImpl extends BaseDao implements SettlePosDetailDao {
	
	@Override
	public int countByExample(SettlePosDetailQuery example) {
		return super.getSqlSession().selectOne("SettlePosDetailMapper.countByExample", example);
	}
	
	@Override
	public int deleteByExample(SettlePosDetailQuery example) {
		return super.getSqlSession().delete("SettlePosDetailMapper.deleteByExample", example);
	}
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return super.getSqlSession().delete("SettlePosDetailMapper.deleteByPrimaryKey", id);
	}
	
	@Override
	public int insert(SettlePosDetail record) {
		return super.getSqlSession().insert("SettlePosDetailMapper.insert", record);
	}
	
	@Override
	public int insertSelective(SettlePosDetail record) {
		return super.getSqlSession().insert("SettlePosDetailMapper.insertSelective", record);
	}
	
	@Override
	public List<SettlePosDetail> selectByExample(SettlePosDetailQuery example) {
		return super.getSqlSession().selectList("SettlePosDetailMapper.selectByExample", example);
	}
	
	@Override
	public SettlePosDetail selectByPrimaryKey(Long id) {
		return super.getSqlSession().selectOne("SettlePosDetailMapper.selectByPrimaryKey", id);
	}
	
	@Override
	public int updateByPrimaryKeySelective(SettlePosDetail record) {
		return super.getSqlSession().update("SettlePosDetailMapper.updateByPrimaryKeySelective", record);
	}
	
	@Override
	public int updateByPrimaryKey(SettlePosDetail record) {
		return super.getSqlSession().update("SettlePosDetailMapper.updateByPrimaryKey", record);
	}
	
	@Override
	public List<Map<String,Object>> selectListMapByExample(SettlePosDetailQuery example){
		return super.getSqlSession().selectList("SettlePosDetailMapper.selectListMapByExample", example);
	}
	
	@Override
	public int updateTransStatusId(Map<String, Object> map) {
		return super.getSqlSession().update("SettlePosDetailMapper.updateTransStatusId", map);
	}
	
    @Override
    public int batchInsertSelective(List<SettlePosDetail> settlePosDetailList) {
    	return super.getSqlSession().insert("SettlePosDetailMapper.batchInsertSelective", settlePosDetailList);
    }
    
    @Override
    public List<Map<String,Object>> queryPosForProfitList(SettlePosDetailQuery query) {
    	return super.getSqlSession().selectList("SettlePosDetailMapper.queryPosForProfitList", query);
    }
    
    @Override
	public int updateAccountInfoToDetailInfo(Map<String, Object> map) {
		return super.getSqlSession().update("SettlePosDetailMapper.updateAccountInfoToDetailInfo", map);
	}
    
    @Override
    public List<SettlePosDetail> selectRePayTransFee(SettlePosDetailQuery example) {
    	return super.getSqlSession().selectList("SettlePosDetailMapper.selectRePayTransFee", example);
    }

	@Override
	public List<Map<String, Object>> queryPosForProfitListByMap(Map<String, Object> query) {
		return super.getSqlSession().selectList("SettlePosDetailMapper.queryPosForProfitListByMap", query);
	}
}
