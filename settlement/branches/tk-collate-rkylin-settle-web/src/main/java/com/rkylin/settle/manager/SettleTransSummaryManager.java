package com.rkylin.settle.manager;

import java.util.List;

import com.rkylin.settle.pojo.SettleTransSummary;
import com.rkylin.settle.pojo.SettleTransSummaryQuery;

public interface SettleTransSummaryManager {
	
	List<SettleTransSummary> queryPage(SettleTransSummaryQuery query);
	
	int countByExample(SettleTransSummaryQuery query);
}
