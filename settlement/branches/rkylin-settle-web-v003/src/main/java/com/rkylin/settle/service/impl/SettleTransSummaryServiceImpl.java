package com.rkylin.settle.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rkylin.settle.manager.SettleTransSummaryManager;
import com.rkylin.settle.pojo.SettleTransSummary;
import com.rkylin.settle.pojo.SettleTransSummaryQuery;
import com.rkylin.settle.service.SettleTransSummaryService;
import com.rkylin.settle.util.PagerModel;

/***
 * 汇总表的相关逻辑
 * @author 
 *
 */
@Service("settleTransSummaryService")
public class SettleTransSummaryServiceImpl implements SettleTransSummaryService {
	//private static Logger logger = LoggerFactory.getLogger(SettleTransSummaryServiceImpl.class);
	@Autowired
	private SettleTransSummaryManager settleTransSummaryManager;
	
	/***
	 * 分页条件查询下游对账交易信息
	 * @param query
	 * @return
	 */
	@Override
	public PagerModel<SettleTransSummary> query(SettleTransSummaryQuery query) {
		//创建分页Model
		PagerModel<SettleTransSummary> pagerModel = new PagerModel<SettleTransSummary>();
		try {
			//初始化当前页
			if(query.getPageIndex()==null){
				query.setPageIndex(1);
			}
			//初始化显示条数
			if(query.getPageSize()==null){
				query.setPageSize(10);
			}
			//封装pageModel
			query.setOffset((query.getPageIndex() - 1) * query.getPageSize());
			pagerModel.setList(settleTransSummaryManager.queryPage(query));
			pagerModel.setTotal(settleTransSummaryManager.countByExample(query));
			pagerModel.setResult("ok");
		} catch (Exception e) {
			e.printStackTrace();
			pagerModel.setResult("error");
			pagerModel.setMsg("系统异常");
		}
		
		return pagerModel;
	}
	
}
