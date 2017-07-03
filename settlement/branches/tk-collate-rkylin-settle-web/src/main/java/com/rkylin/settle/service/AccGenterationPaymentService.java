package com.rkylin.settle.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.rkylin.settle.pojo.GenerationPayment;
import com.rkylin.settle.pojo.GenerationPaymentQuery;
import com.rkylin.settle.util.PagerModel;

public interface AccGenterationPaymentService {
	/***
	 * 分页条件查询上游对账交易信息
	 * @param query
	 * @return
	 */
	public PagerModel<GenerationPayment> query(GenerationPaymentQuery query);
	/***
	 * 查询上游对账交易信息
	 * @param query
	 * @return
	 */
	public List<GenerationPayment> select(GenerationPaymentQuery query);
	/***
	 * 全部到处excl
	 * @param query
	 * @throws Exception
	 */
	public boolean queryAndOutputExcl(HttpServletRequest request, GenerationPaymentQuery query) throws Exception;
}
