/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.settle.manager;

import java.util.List;
import java.util.Map;

import com.rkylin.settle.pojo.SettleTransDetail;
import com.rkylin.settle.pojo.SettleTransDetailQuery;

public interface SettleTransDetailManager {
	void saveSettleTransDetail(SettleTransDetail settleTransDetail);

	int updateSettleTransDetail(SettleTransDetail settleTransDetail);
	
	SettleTransDetail findSettleTransDetailById(Long id);
	
	List<SettleTransDetail> queryList(SettleTransDetailQuery query);
	
	void deleteSettleTransDetailById(Long id);
	
	void deleteSettleTransDetail(SettleTransDetailQuery query);
	
	List<Map<String,Object>> queryListMap(SettleTransDetailQuery query);
	
	void updateTransStatusId(Map<String, Object> map);
	
	List<Map<String,Object>> selectCollateTransInfo(Map<String, Object> map);
	
	List<Map<String,Object>> selectProfitTransInfo(Map<String, Object> map);
	
	List<SettleTransDetail> queryPage(SettleTransDetailQuery query);
    
    int countByExample(SettleTransDetailQuery query);
    
    List<SettleTransDetail> selectByIds(Map<String, Object> example);
}
