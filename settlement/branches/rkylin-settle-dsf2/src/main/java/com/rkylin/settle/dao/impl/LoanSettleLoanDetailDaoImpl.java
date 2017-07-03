/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2016
 */

package com.rkylin.settle.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.rkylin.settle.common.LoanBaseDao;
import com.rkylin.settle.dao.LoanSettleLoanDetailDao;
import com.rkylin.settle.pojo.LoanSettleLoanDetail;
import com.rkylin.settle.pojo.LoanSettleLoanDetailQuery;

@Repository("loanSettleLoanDetailDao")
public class LoanSettleLoanDetailDaoImpl extends LoanBaseDao implements LoanSettleLoanDetailDao {
	
	@Override
	public int countByExample(LoanSettleLoanDetailQuery example) {
		return super.getSqlSession().selectOne("LoanSettleLoanDetailMapper.countByExample", example);
	}
	
	@Override
	public int deleteByExample(LoanSettleLoanDetailQuery example) {
		return super.getSqlSession().delete("LoanSettleLoanDetailMapper.deleteByExample", example);
	}
	
	@Override
	public int deleteByPrimaryKey(Long id) {
		return super.getSqlSession().delete("LoanSettleLoanDetailMapper.deleteByPrimaryKey", id);
	}
	
	@Override
	public int insert(LoanSettleLoanDetail record) {
		return super.getSqlSession().insert("LoanSettleLoanDetailMapper.insert", record);
	}
	
	@Override
	public int insertSelective(LoanSettleLoanDetail record) {
		return super.getSqlSession().insert("LoanSettleLoanDetailMapper.insertSelective", record);
	}
	
	@Override
	public List<LoanSettleLoanDetail> selectByExample(LoanSettleLoanDetailQuery example) {
		return super.getSqlSession().selectList("LoanSettleLoanDetailMapper.selectByExample", example);
	}
	
	@Override
	public LoanSettleLoanDetail selectByPrimaryKey(Long id) {
		return super.getSqlSession().selectOne("LoanSettleLoanDetailMapper.selectByPrimaryKey", id);
	}
	
	@Override
	public int updateByPrimaryKeySelective(LoanSettleLoanDetail record) {
		return super.getSqlSession().update("LoanSettleLoanDetailMapper.updateByPrimaryKeySelective", record);
	}
	
	@Override
	public int updateByPrimaryKey(LoanSettleLoanDetail record) {
		return super.getSqlSession().update("LoanSettleLoanDetailMapper.updateByPrimaryKey", record);
	}

	@Override
	public List<LoanSettleLoanDetail> selectByTime(LoanSettleLoanDetailQuery example) {
		return super.getSqlSession().selectList("LoanSettleLoanDetailMapper.selectByTime", example);
	}
}
