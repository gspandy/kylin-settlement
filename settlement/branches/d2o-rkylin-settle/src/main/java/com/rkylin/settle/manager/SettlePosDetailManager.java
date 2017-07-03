/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2016
 */

package com.rkylin.settle.manager;

import java.util.List;
import java.util.Map;

import com.rkylin.settle.pojo.SettlePosDetail;
import com.rkylin.settle.pojo.SettlePosDetailQuery;

public interface SettlePosDetailManager {
	
	int countSettlePosDetail(SettlePosDetailQuery settlePosDetail);
	
	void saveSettlePosDetail(SettlePosDetail settlePosDetail);

	void updateSettlePosDetail(SettlePosDetail settlePosDetail);
	
	SettlePosDetail findSettlePosDetailById(Long id);
	
	List<SettlePosDetail> queryList(SettlePosDetailQuery query);
	
	void deleteSettlePosDetailById(Long id);
	
	void deleteSettlePosDetail(SettlePosDetailQuery query);
	
	List<Map<String,Object>> queryListMap(SettlePosDetailQuery query);
	
	void updateTransStatusId(Map<String, Object> map);
	
	void batchSaveSettlePosDetail(List<SettlePosDetail> settlePosDetailList);
	
	List<Map<String,Object>> queryPosForProfitList(SettlePosDetailQuery query);
	
	int updateAccountInfoToDetailInfo(Map<String, Object> map);
	
	List<SettlePosDetail> selectRePayTransFee(SettlePosDetailQuery query);
}
