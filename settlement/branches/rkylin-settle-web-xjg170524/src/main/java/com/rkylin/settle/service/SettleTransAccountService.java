package com.rkylin.settle.service;

import com.rkylin.settle.pojo.SettleTransAccount;
import com.rkylin.settle.pojo.SettleTransAccountQuery;
import com.rkylin.settle.util.PagerModel;

/***
 * 上游对账交易信息业务逻辑
 * @author Yang
 *
 */
public interface SettleTransAccountService {
	/***
	 * 分页条件查询上游对账交易信息
	 * @param query
	 * @return
	 */
	public PagerModel<SettleTransAccount> query(SettleTransAccountQuery query);
	/***
	 * 查询上游对账交易byID
	 * @param query
	 * @return
	 */
	public SettleTransAccount findById(Long id);
	/***
	 * 修改上游对账交易信息
	 * @param query
	 * @return
	 */
	public Integer edit(SettleTransAccount SettleTransAccount);
}
