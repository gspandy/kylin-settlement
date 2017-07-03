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
	int countSettleTransDetail(SettleTransDetailQuery settleTransDetail);
	
	void saveSettleTransDetail(SettleTransDetail settleTransDetail);

	void updateSettleTransDetail(SettleTransDetail settleTransDetail);
	
	SettleTransDetail findSettleTransDetailById(Long id);
	
	List<SettleTransDetail> queryList(SettleTransDetailQuery query);
	
	void deleteSettleTransDetailById(Long id);
	
	void deleteSettleTransDetail(SettleTransDetailQuery query);
	
	List<Map<String,Object>> queryListMap(SettleTransDetailQuery query);
	
	void updateTransStatusId(Map<String, Object> map);
	
	List<Map<String,Object>> selectCollateTransInfo(Map<String, Object> map);
	
	List<Map<String,Object>> selectProfitTransInfo(Map<String, Object> map);
	
	int updateByAccountOrderInfo(SettleTransDetail settleTransDetail);
	
	List<Map<String,Object>> selectBillTransInfo(Map<String, Object> map);
	
	List<Map<String,Object>> selectDsfTransInfo(Map<String, Object> map);
	
	void batchSaveSettleTransDetail(List<SettleTransDetail> settleTransDetailList);
	
	int updateMTAegisCorrectOrder(List<Integer> transDetailIds);
	
	int updateMTAegisWipeOutTheAccount(List<Integer> transDetailIds);
	
	int updateMTAegisSendBack(List<Integer> transDetailIds);
	
	int updatePayChannelIdByoriginalTrans(Map<String, Object> map);
	
	int updatePayChannelIdBy4015Trans();
	
	List<Integer> queryTransDetailIdsByOrderNo(List<String> orderNoList);
	
	void updateTransAmount(Map<String, Object> map);
	
	List<Map<String, Object>> collectAmountByExample(SettleTransDetailQuery query);
	
	List<Map<String, Object>> collectShouldAmountByExample(SettleTransDetailQuery query);
	
	List<Map<String,Object>> queryCollecteListMap(Map<String, Object> map);
	
	List<String> selectRootInstCds(Map<String, Object> map);
	
	List<SettleTransDetail> selectByIdsArr(Map<String, Object> paramMap);
	
	int selectDsfTransCount(Map<String, Object> map);
	
	int updateAccountInfoToDetailInfo(Map<String, Object> map);
	
	List<Map<String, Object>> selectByUnKnowStatus(Map<String, Object> map);
	
	List<Map<String,Object>> summaryFeeAmount(Map<String, Object> map);

	/**
	 * 商户平台分页查询总条数
	 * @param map
	 * @return
	 */
	int countBySHPTPage(Map<String, Object> map);
	
	/**
	 * 商户平台分页查询
	 * @param map
	 * @return
	 */
	List<SettleTransDetail> selectBySHPTPage(Map<String, Object> map);
	
	/**
	 * 商户平台下载全部内容
	 * @param map
	 * @return
	 */
	List<SettleTransDetail> selectAllBySHPT(Map<String, Object> map);
}
