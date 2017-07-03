package com.rkylin.settle.service;

import com.rkylin.settle.pojo.TransOrderInfo;
import com.rkylin.settle.pojo.TransOrderInfoQuery;
import com.rkylin.settle.util.PagerModel;

public interface AccTransOrderInfoService {
	/***
	 * 分页条件查询上游对账交易信息
	 * @param query
	 * @return
	 */
	public PagerModel<TransOrderInfo> query(TransOrderInfoQuery query);
}
