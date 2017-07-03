package com.rkylin.settle.service;

import com.rkylin.settle.pojo.SettleTransBill;
import com.rkylin.settle.pojo.SettleTransBillQuery;
import com.rkylin.settle.util.PagerModel;


/**
 * 项目名称：rkylin-settle-web
 * 类名称：SettleTransBillService
 * 类描述：挂账交易信息业务逻辑
 * 创建人：CLF
 * 创建时间：2015年8月27日 下午1:30:12
 * 修改人：
 * 修改时间：2015年8月27日 下午1:30:12
 * 修改备注：
 * @version
 */
public interface SettleTransBillService {
	/***
	 * 分页条件查询交易结果信息
	 * @param query
	 * @return
	 */
	public PagerModel<SettleTransBill> query(SettleTransBillQuery query);
	/***
	 * 查询挂账交易信息byID
	 * @param query
	 * @return
	 */
	public SettleTransBill findById(Long id);
	/***
	 * 修改挂账交易信息
	 * @param query
	 * @return
	 */
	public Integer edit(SettleTransBill settleTransBill);
}
