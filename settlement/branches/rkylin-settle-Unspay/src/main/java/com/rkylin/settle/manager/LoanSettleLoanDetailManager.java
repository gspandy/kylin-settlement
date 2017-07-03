/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2016
 */

package com.rkylin.settle.manager;

import java.util.List;

import com.rkylin.settle.pojo.LoanSettleLoanDetail;
import com.rkylin.settle.pojo.LoanSettleLoanDetailQuery;

public interface LoanSettleLoanDetailManager {
	void saveLoanSettleLoanDetail(LoanSettleLoanDetail loanSettleLoanDetail);

	void updateLoanSettleLoanDetail(LoanSettleLoanDetail loanSettleLoanDetail);
	
	LoanSettleLoanDetail findLoanSettleLoanDetailById(Long id);
	
	List<LoanSettleLoanDetail> queryList(LoanSettleLoanDetailQuery query);
	
	void deleteLoanSettleLoanDetailById(Long id);
	
	void deleteLoanSettleLoanDetail(LoanSettleLoanDetailQuery query);
	
	List<LoanSettleLoanDetail> selectByTime(LoanSettleLoanDetailQuery query);
}
