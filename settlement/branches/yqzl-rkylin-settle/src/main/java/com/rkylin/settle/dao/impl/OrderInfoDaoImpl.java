/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2016
 */

package com.rkylin.settle.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.rkylin.settle.common.OrderBaseDao;
import com.rkylin.settle.dao.OrderInfoDao;
import com.rkylin.settle.pojo.OrderInfo;
import com.rkylin.settle.pojo.OrderInfoQuery;

@Repository("orderInfoDao")
public class OrderInfoDaoImpl extends OrderBaseDao implements OrderInfoDao {
	
	@Override
	public int countByExample(OrderInfoQuery example) {
		return super.getSqlSession().selectOne("OrderInfoMapper.countByExample", example);
	}
	
	@Override
	public int deleteByExample(OrderInfoQuery example) {
		return super.getSqlSession().delete("OrderInfoMapper.deleteByExample", example);
	}
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return super.getSqlSession().delete("OrderInfoMapper.deleteByPrimaryKey", id);
	}
	
	@Override
	public int insert(OrderInfo record) {
		return super.getSqlSession().insert("OrderInfoMapper.insert", record);
	}
	
	@Override
	public int insertSelective(OrderInfo record) {
		return super.getSqlSession().insert("OrderInfoMapper.insertSelective", record);
	}
	
	@Override
	public List<OrderInfo> selectByExample(OrderInfoQuery example) {
		return super.getSqlSession().selectList("OrderInfoMapper.selectByExample", example);
	}
	
	@Override
	public OrderInfo selectByPrimaryKey(Long id) {
		return super.getSqlSession().selectOne("OrderInfoMapper.selectByPrimaryKey", id);
	}
	
	@Override
	public int updateByPrimaryKeySelective(OrderInfo record) {
		return super.getSqlSession().update("OrderInfoMapper.updateByPrimaryKeySelective", record);
	}
	
	@Override
	public int updateByPrimaryKey(OrderInfo record) {
		return super.getSqlSession().update("OrderInfoMapper.updateByPrimaryKey", record);
	}

	@Override
	public List<OrderInfo> selectByUpdateTime(Map<String,Object> query) {
		return super.getSqlSession().selectList("OrderInfoMapper.selectByUpdateTime", query);
	}
}
