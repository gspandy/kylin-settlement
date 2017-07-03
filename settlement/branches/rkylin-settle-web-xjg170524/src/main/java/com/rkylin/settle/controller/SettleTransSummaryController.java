package com.rkylin.settle.controller;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rkylin.common.RedisIdGenerator;
import com.rkylin.settle.pojo.SettleTransSummary;
import com.rkylin.settle.pojo.SettleTransSummaryQuery;
import com.rkylin.settle.service.SettleTransSummaryService;
import com.rkylin.settle.settleInterface.SettleWebInterface;
import com.rkylin.settle.util.PagerModel;

/**
 * @Description: 代收付结算表管理
 * @author youyu
 * @Create Time: 
 * @version V1.00
 */
@Controller
@RequestMapping("/summary")
@Scope("prototype")
public class SettleTransSummaryController extends BaseController {
	@Autowired
	private SettleTransSummaryService settleTransSummaryService;
	@Autowired
	private SettleWebInterface settleWebInterface;
	@Autowired
	private RedisIdGenerator redisIdGenerator;
	
	/***
	 * 分页查询
	 * @param query
	 * @throws Exception
	 */
	@RequestMapping("/query_ajax")
	public void queryAjax(SettleTransSummaryQuery query) throws Exception {
		try {
			PagerModel<SettleTransSummary> pagerModel = settleTransSummaryService.query(query);
			returnJsonList(pagerModel);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/***
	 * 打开查询页面
	 * @param query
	 * @throws Exception
	 */
	@RequestMapping("/summary_manager")
	public String openQuery() {
		return "/settlement/trans/summary/query";
	}
}
