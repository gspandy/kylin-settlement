package com.rkylin.settle.service;

import com.rkylin.settle.pojo.SettleTransProfit;
import com.rkylin.settle.pojo.SettleTransProfitQuery;
import com.rkylin.settle.util.PagerModel;


/**
 * 项目名称：rkylin-settle-web
 * 类名称：SettleTransBillService
 * 类描述：清分记录信息业务逻辑
 * 创建人：CLF
 * 创建时间：2015年8月27日 下午1:30:12
 * 修改人：
 * 修改时间：2015年8月27日 下午1:30:12
 * 修改备注：
 * @version
 */
public interface SettleTransProfitService {
	/***
	 * 分页条件查询
	 * @param query
	 * @return
	 */
	public PagerModel<SettleTransProfit> query(SettleTransProfitQuery query);
	/***
	 * 查询清分记录信息byID
	 * @param query
	 * @return
	 */
	public SettleTransProfit findById(Long id);
	/***
	 * 修改清分记录信息
	 * @param query
	 * @return
	 */
	public Integer edit(SettleTransProfit settleTransProfit);
}
