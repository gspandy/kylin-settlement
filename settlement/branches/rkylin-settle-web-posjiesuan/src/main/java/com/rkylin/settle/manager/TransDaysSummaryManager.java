/*
 * Powered By rkylin-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.settle.manager;

import java.util.List;
import java.util.Map;

import com.rkylin.settle.pojo.TransDaysSummary;
import com.rkylin.settle.pojo.TransDaysSummaryQuery;

public interface TransDaysSummaryManager {
	void saveTransDaysSummary(TransDaysSummary transDaysSummary);

	TransDaysSummary findTransDaysSummaryById(String id);
	
	List<TransDaysSummary> queryList(TransDaysSummaryQuery query);
	
	void deleteTransDaysSummaryById(Long id);
	
	void deleteTransDaysSummary(TransDaysSummaryQuery query);

	void updateTransDaysSummary(TransDaysSummary query);
	
	List<Map<String,Object>> findDateAll(Map<String,String> query);
}
