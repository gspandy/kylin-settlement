/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2016
 */

package com.rkylin.settle.manager.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.rkylin.settle.dao.LoanSettleLoanDetailDao;
import com.rkylin.settle.manager.LoanSettleLoanDetailManager;
import com.rkylin.settle.pojo.LoanSettleLoanDetail;
import com.rkylin.settle.pojo.LoanSettleLoanDetailQuery;

@Component("loanSettleLoanDetailManager")
public class LoanSettleLoanDetailManagerImpl implements LoanSettleLoanDetailManager {
	
	@Autowired
	@Qualifier("loanSettleLoanDetailDao")
	private LoanSettleLoanDetailDao loanSettleLoanDetailDao;
	
	@Override
	public void saveLoanSettleLoanDetail(LoanSettleLoanDetail loanSettleLoanDetail) {
		loanSettleLoanDetailDao.insertSelective(loanSettleLoanDetail);
	}
	
	@Override
	public void updateLoanSettleLoanDetail(LoanSettleLoanDetail loanSettleLoanDetail) {
		loanSettleLoanDetailDao.updateByPrimaryKeySelective(loanSettleLoanDetail);
	}
	
	@Override
	public LoanSettleLoanDetail findLoanSettleLoanDetailById(Long id) {
		return loanSettleLoanDetailDao.selectByPrimaryKey(id);
	}
	
	@Override
	public List<LoanSettleLoanDetail> queryList(LoanSettleLoanDetailQuery query) {
		return loanSettleLoanDetailDao.selectByExample(query);
	}
	
	@Override
	public void deleteLoanSettleLoanDetailById(Long id) {
		loanSettleLoanDetailDao.deleteByPrimaryKey(id);
	}
	
	@Override
	public void deleteLoanSettleLoanDetail(LoanSettleLoanDetailQuery query) {
		loanSettleLoanDetailDao.deleteByExample(query);
	}

	@Override
	public List<LoanSettleLoanDetail> selectByTime(LoanSettleLoanDetailQuery query) {
		return loanSettleLoanDetailDao.selectByTime(query);
	}
}

