/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.settle.dao;

import java.util.List;
import java.util.Map;

import com.rkylin.settle.pojo.SettleTransDetail;
import com.rkylin.settle.pojo.SettleTransDetailQuery;

public interface SettleTransDetailOtherEnvironDao {
	int countByExample(SettleTransDetailQuery example);
	
	int deleteByExample(SettleTransDetailQuery example);
	
	int deleteByPrimaryKey(Long id);
	
	int insert(SettleTransDetail record);
	
	int insertSelective(SettleTransDetail record);
	
	List<SettleTransDetail> selectByExample(SettleTransDetailQuery example);
	
	SettleTransDetail selectByPrimaryKey(Long id);
	
	int updateByPrimaryKeySelective(SettleTransDetail record);
	
	int updateByPrimaryKey(SettleTransDetail record);
	
	List<Map<String,Object>> selectListMapByExample(SettleTransDetailQuery example);
	
	List<Map<String,Object>> selectListMapOEByExample(SettleTransDetailQuery example);
	
	int updateTransStatusId(Map<String, Object> map);
	
	List<Map<String,Object>> selectCollateTransInfo(Map<String, Object> map);
	
	List<Map<String,Object>> selectProfitTransInfo(Map<String, Object> map);
	
	List<Map<String,Object>> selectBillTransInfo(Map<String, Object> map);
	
	int updateByAccountOrderInfo(SettleTransDetail record);
	
	List<Map<String,Object>> selectDsfTransInfo(Map<String, Object> map);
	
	int batchInsertSelective(List<SettleTransDetail> settleTransDetailList);
	
	int updateMTAegisCorrectOrder(List<Integer> transDetailIds);
	
	int updateMTAegisWipeOutTheAccount(List<Integer> transDetailIds);
	
	int updateMTAegisSendBack(List<Integer> transDetailIds);
	
	int updatePayChannelIdBy4015Trans();
	
	int updatePayChannelIdByoriginalTrans(Map<String, Object> map);
	
	public List<Integer> queryTransDetailIdsByOrderNo(List<String> orderNoList);
	
	int updateTransAmount(Map<String, Object> map);
	
	List<Map<String,Object>> collectAmountByExample(SettleTransDetailQuery query);
	
	List<Map<String,Object>> collectShouldAmountByExample(SettleTransDetailQuery query);
	
	List<Map<String,Object>> collecteListMap(Map<String, Object> map);
	
	List<String> selectRootInstCds(Map<String, Object> map);
	
	List<SettleTransDetail> selectByIdsArr(Map<String, Object> paramMap);

	int selectDsfTransCount(Map<String, Object> paramMap);
	
	int updateAccountInfoToDetailInfo(Map<String, Object> map);
	
	List<Map<String, Object>> selectByUnKnowStatus(Map<String, Object> map);
	
	List<Map<String, Object>> summaryFeeAmount(Map<String, Object> map);

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
	 * 商户平台下载全部内容查询
	 * @param map
	 * @return
	 */
	List<SettleTransDetail> selectAllBySHPT(Map<String, Object> map);
	
	/**
	 * 批量修改 代收付交易dflag
	 * @param map
	 * @return
	 */
	int batchUpdateSettleTransDetailDflag(Map<String, Object> map);
}
