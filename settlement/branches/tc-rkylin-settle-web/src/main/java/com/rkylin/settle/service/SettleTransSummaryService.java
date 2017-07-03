package com.rkylin.settle.service;
import com.rkylin.settle.pojo.SettleTransSummary;
import com.rkylin.settle.pojo.SettleTransSummaryQuery;
import com.rkylin.settle.util.PagerModel;

/***
 * 下游交易信息业务逻辑
 * @author Yang
 *
 */
public interface SettleTransSummaryService {
	/***
	 * 分页条件查询下游交易信息
	 * @param query
	 * @return
	 */
	public PagerModel<SettleTransSummary> query(SettleTransSummaryQuery query);
}
