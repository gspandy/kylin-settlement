package com.rkylin.settle.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rkylin.settle.manager.TransOrderInfoManager;
import com.rkylin.settle.pojo.TransOrderInfo;
import com.rkylin.settle.pojo.TransOrderInfoQuery;
import com.rkylin.settle.service.AccTransOrderInfoService;
import com.rkylin.settle.util.PagerModel;

@Service("accTransOrderInfoService")
public class AccTransOrderInfoServiceImpl implements  AccTransOrderInfoService {
	@Autowired
	private TransOrderInfoManager transOrderInfoManager;
	
	public PagerModel<TransOrderInfo> query(TransOrderInfoQuery query) {
		//创建分页Model
		PagerModel<TransOrderInfo> pagerModel = new PagerModel<TransOrderInfo>();
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
			pagerModel.setList(transOrderInfoManager.queryPage(query));
			pagerModel.setTotal(transOrderInfoManager.countByExample(query));
			pagerModel.setResult("ok");
		} catch (Exception e) {
			e.printStackTrace();
			pagerModel.setResult("error");
			pagerModel.setMsg("系统异常");
		}
		
		return pagerModel;
	}
}
