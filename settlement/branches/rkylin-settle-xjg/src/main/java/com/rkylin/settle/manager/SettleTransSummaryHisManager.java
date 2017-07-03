package com.rkylin.settle.manager;

import java.util.List;

import com.rkylin.settle.pojo.SettleTransSummaryHis;
import com.rkylin.settle.pojo.SettleTransSummaryHisQuery;

public interface SettleTransSummaryHisManager {
	void saveSettleTransSummaryHis(SettleTransSummaryHis SettleTransSummaryHis);

	SettleTransSummaryHis findSettleTransSummaryHisById(Long id);
	
	List<SettleTransSummaryHis> queryList(SettleTransSummaryHisQuery query);
	
	void deleteSettleTransSummaryHisById(Long id);
	
	void deleteSettleTransSummaryHis(SettleTransSummaryHisQuery query);
	
	void updateSettleTransSummaryHis(SettleTransSummaryHis SettleTransSummaryHis);
}
