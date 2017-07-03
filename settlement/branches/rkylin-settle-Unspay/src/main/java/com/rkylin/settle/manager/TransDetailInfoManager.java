/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.settle.manager;

import java.util.List;
import java.util.Map;

import com.rkylin.settle.pojo.TransDetailInfo;
import com.rkylin.settle.pojo.TransDetailInfoQuery;

public interface TransDetailInfoManager {
	void saveTransDetailInfo(TransDetailInfo transDetailInfo);

	void updateTransDetailInfo(TransDetailInfo transDetailInfo);
	
	TransDetailInfo findTransDetailInfoById(Long id);
	
	List<TransDetailInfo> queryList(TransDetailInfoQuery query);
	
	List<TransDetailInfo> selectByAccountDate(TransDetailInfoQuery query);
	
	void deleteTransDetailInfoById(Long id);
	
	void deleteTransDetailInfo(TransDetailInfoQuery query);

	List<TransDetailInfo> selectByUpdateTime(Map<String,Object> query);
}
