/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2016
 */

package com.rkylin.settle.dao;

import java.util.List;

import com.rkylin.settle.pojo.LoanSettleLoanDetail;
import com.rkylin.settle.pojo.LoanSettleLoanDetailQuery;

public interface LoanSettleLoanDetailDao {
	int countByExample(LoanSettleLoanDetailQuery example);
	
	int deleteByExample(LoanSettleLoanDetailQuery example);
	
	int deleteByPrimaryKey(Long id);
	
	int insert(LoanSettleLoanDetail record);
	
	int insertSelective(LoanSettleLoanDetail record);
	
	List<LoanSettleLoanDetail> selectByExample(LoanSettleLoanDetailQuery example);
	
	LoanSettleLoanDetail selectByPrimaryKey(Long id);
	
	int updateByPrimaryKeySelective(LoanSettleLoanDetail record);
	
	int updateByPrimaryKey(LoanSettleLoanDetail record);
	
	List<LoanSettleLoanDetail> selectByTime(LoanSettleLoanDetailQuery example);
}
