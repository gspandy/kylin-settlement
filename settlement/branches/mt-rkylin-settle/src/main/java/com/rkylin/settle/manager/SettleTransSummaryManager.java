package com.rkylin.settle.manager;

import java.util.List;

import com.rkylin.settle.pojo.SettleTransSummary;
import com.rkylin.settle.pojo.SettleTransSummaryQuery;

public interface SettleTransSummaryManager {
	void saveSettleTransSummary(SettleTransSummary settleTransSummary);

	SettleTransSummary findSettleTransSummaryById(Long id);
	
	List<SettleTransSummary> queryList(SettleTransSummaryQuery query);
	
	void deleteSettleTransSummaryById(Long id);
	
	void deleteSettleTransSummary(SettleTransSummaryQuery query);
	
	void updateSettleTransSummary(SettleTransSummary settleTransSummary);
	
	int batchInsertTransSummery(List<SettleTransSummary> settleTransSummary);
}
