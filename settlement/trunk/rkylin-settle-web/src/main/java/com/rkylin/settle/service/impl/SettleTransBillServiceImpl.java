package com.rkylin.settle.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rkylin.settle.manager.SettleTransBillManager;
import com.rkylin.settle.pojo.SettleTransBill;
import com.rkylin.settle.pojo.SettleTransBillQuery;
import com.rkylin.settle.service.SettleTransBillService;
import com.rkylin.settle.util.PagerModel;

/***
 * 挂账交易信息业务逻辑
 * @author Yang
 *
 */
@Service("settleTransBillService")
public class SettleTransBillServiceImpl implements SettleTransBillService {
	@Autowired
	private SettleTransBillManager settleTransBillManager;
	/***
	 * 分页条件查询挂账交易信息
	 * @param query
	 * @return
	 */
	@Override
	public PagerModel<SettleTransBill> query(SettleTransBillQuery query) {
		//创建分页Model
		PagerModel<SettleTransBill> pagerModel = new PagerModel<SettleTransBill>();
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
			pagerModel.setList(settleTransBillManager.queryPage(query));
			pagerModel.setTotal(settleTransBillManager.countByExample(query));
			pagerModel.setResult("ok");
		} catch (Exception e) {
			e.printStackTrace();
			pagerModel.setResult("error");
			pagerModel.setMsg("系统异常");
		}
		
		return pagerModel;
	}
	/***
	 * 查询挂账交易byID
	 * @param query
	 * @return
	 */
	@Override
	public SettleTransBill findById(Long id) {
		return settleTransBillManager.findSettleTransBillById(id);
	}
	/***
	 * 修改挂账交易信息
	 * @param query
	 * @return
	 */
	@Override
	public Integer edit(SettleTransBill SettleTransBill) {
		return settleTransBillManager.updateSettleTransBill(SettleTransBill);
	}
}
