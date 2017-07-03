/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2016
 */

package com.rkylin.settle.dao;

import java.util.List;
import java.util.Map;

import com.rkylin.settle.pojo.SettlePosDetail;
import com.rkylin.settle.pojo.SettlePosDetailQuery;

public interface SettlePosDetailDao {
	int countByExample(SettlePosDetailQuery example);
	
	int deleteByExample(SettlePosDetailQuery example);
	
	int deleteByPrimaryKey(Long id);
	
	int insert(SettlePosDetail record);
	
	int insertSelective(SettlePosDetail record);
	
	List<SettlePosDetail> selectByExample(SettlePosDetailQuery example);
	
	SettlePosDetail selectByPrimaryKey(Long id);
	
	int updateByPrimaryKeySelective(SettlePosDetail record);
	
	int updateByPrimaryKey(SettlePosDetail record);
	
	List<Map<String,Object>> selectListMapByExample(SettlePosDetailQuery example);
	
	int updateTransStatusId(Map<String, Object> map);
	
	int batchInsertSelective(List<SettlePosDetail> settlePosDetailList);
	
	List<Map<String,Object>> queryPosForProfitList(SettlePosDetailQuery query);
	
	int updateAccountInfoToDetailInfo(Map<String, Object> map);
	
	List<SettlePosDetail> selectRePayTransFee(SettlePosDetailQuery example);
}
