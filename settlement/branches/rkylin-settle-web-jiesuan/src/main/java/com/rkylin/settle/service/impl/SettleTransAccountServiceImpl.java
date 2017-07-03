package com.rkylin.settle.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rkylin.settle.manager.SettleTransAccountManager;
import com.rkylin.settle.pojo.SettleTransAccount;
import com.rkylin.settle.pojo.SettleTransAccountQuery;
import com.rkylin.settle.service.SettleTransAccountService;
import com.rkylin.settle.util.PagerModel;

/***
 * 上游对账交易信息业务逻辑
 * @author Yang
 *
 */
@Service("settleTransAccountService")
public class SettleTransAccountServiceImpl implements SettleTransAccountService {
	@Autowired
	private SettleTransAccountManager settleTransAccountManager;
	/***
	 * 分页条件上游对账交易信息
	 * @param query
	 * @return
	 */
	@Override
	public PagerModel<SettleTransAccount> query(SettleTransAccountQuery query) {
		//创建分页Model
		PagerModel<SettleTransAccount> pagerModel = new PagerModel<SettleTransAccount>();
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
			pagerModel.setList(settleTransAccountManager.queryPage(query));
			pagerModel.setTotal(settleTransAccountManager.countByExample(query));
			pagerModel.setResult("ok");
		} catch (Exception e) {
			e.printStackTrace();
			pagerModel.setResult("error");
			pagerModel.setMsg("系统异常");
		}
		
		return pagerModel;
	}
	/***
	 * 查询上游对账交易byID
	 * @param query
	 * @return
	 */
	@Override
	public SettleTransAccount findById(Long id) {
		return settleTransAccountManager.findSettleTransAccountById(id);
	}
	/***
	 * 修改上游对账交易信息
	 * @param query
	 * @return
	 */
	@Override
	public Integer edit(SettleTransAccount SettleTransAccount) {
		return settleTransAccountManager.updateSettleTransAccount(SettleTransAccount);
	}
}
