package com.rkylin.settle.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rkylin.settle.manager.SettleTransProfitManager;
import com.rkylin.settle.pojo.SettleTransProfit;
import com.rkylin.settle.pojo.SettleTransProfitQuery;
import com.rkylin.settle.service.SettleTransProfitService;
import com.rkylin.settle.util.PagerModel;

/***
 * 清分记录信息业务逻辑
 * @author Yang
 *
 */
@Service("settleTransProfitService")
public class SettleTransProfitServiceImpl implements SettleTransProfitService {
	@Autowired
	private SettleTransProfitManager settleTransProfitManager;
	/***
	 * 分页条件查询清分记录信息
	 * @param query
	 * @return
	 */
	@Override
	public PagerModel<SettleTransProfit> query(SettleTransProfitQuery query) {
		//创建分页Model
		PagerModel<SettleTransProfit> pagerModel = new PagerModel<SettleTransProfit>();
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
			pagerModel.setList(settleTransProfitManager.queryPage(query));
			pagerModel.setTotal(settleTransProfitManager.countByExample(query));
			pagerModel.setResult("ok");
		} catch (Exception e) {
			e.printStackTrace();
			pagerModel.setResult("error");
			pagerModel.setMsg("系统异常");
		}
		
		return pagerModel;
	}
	/***
	 * 查询清分记录byID
	 * @param query
	 * @return
	 */
	@Override
	public SettleTransProfit findById(Long id) {
		return settleTransProfitManager.findSettleTransProfitById(id);
	}
	/***
	 * 修改清分记录信息
	 * @param query
	 * @return
	 */
	@Override
	public Integer edit(SettleTransProfit SettleTransProfit) {
		return settleTransProfitManager.updateSettleTransProfit(SettleTransProfit);
	}
}
