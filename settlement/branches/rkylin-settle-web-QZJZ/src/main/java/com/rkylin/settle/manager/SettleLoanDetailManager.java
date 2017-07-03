/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2016
 */

package com.rkylin.settle.manager;

import java.util.List;
import java.util.Map;

import com.rkylin.settle.pojo.SettleLoanDetail;
import com.rkylin.settle.pojo.SettleLoanDetailQuery;

public interface SettleLoanDetailManager {
	void saveSettleLoanDetail(SettleLoanDetail settleLoanDetail);

	int updateSettleLoanDetail(SettleLoanDetail settleLoanDetail);
	
	SettleLoanDetail findSettleLoanDetailById(Long id);
	
	List<SettleLoanDetail> queryList(SettleLoanDetailQuery query);
	
	void deleteSettleLoanDetailById(Long id);
	
	void deleteSettleLoanDetail(SettleLoanDetailQuery query);
	
	List<Map<String,Object>> selectProfitTransInfo(Map<String, Object> map);
	
	void updateTransStatusId(Map<String, Object> map);
	
	public List<SettleLoanDetail> queryPage(SettleLoanDetailQuery query);
	
	public int countByExample(SettleLoanDetailQuery query);
}
