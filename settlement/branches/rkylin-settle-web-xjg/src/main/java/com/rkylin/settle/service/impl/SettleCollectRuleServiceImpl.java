package com.rkylin.settle.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rkylin.settle.manager.SettleCollectRuleManager;
import com.rkylin.settle.pojo.SettleCollectRule;
import com.rkylin.settle.pojo.SettleCollectRuleQuery;
import com.rkylin.settle.service.SettleCollectRuleService;
import com.rkylin.settle.util.PagerModel;

/**
 * 项目名称：rkylin-settle-web
 * 类名称：SettleCollectRuleServiceImpl
 * 类描述：汇总规则业务逻辑
 * 创建人：Yang
 * @version
 */
@Service("settleCollectRuleService")
public class SettleCollectRuleServiceImpl implements SettleCollectRuleService {
	@Autowired
	private SettleCollectRuleManager settleCollectRuleManager;
	/***
	 * 分页条件查询文件信息
	 * @param query
	 * @return
	 */
	@Override
	public PagerModel<SettleCollectRule> query(SettleCollectRuleQuery query) {
		//创建分页Model
		PagerModel<SettleCollectRule> pagerModel = new PagerModel<SettleCollectRule>();
		try {
			//初始化当前页
			if(query.getPageIndex()==null){
				query.setPageIndex(1);
			}
			//初始化显示条数
			if(query.getPageSize()==null){
				query.setPageSize(10);
			}
			query.setOrderBy("ID desc");
			//封装pageModel
			query.setOffset((query.getPageIndex() - 1) * query.getPageSize());
			pagerModel.setList(settleCollectRuleManager.queryPage(query));
			pagerModel.setTotal(settleCollectRuleManager.countByExample(query));
			pagerModel.setResult("ok");
		} catch (Exception e) {
			e.printStackTrace();
			pagerModel.setResult("error");
			pagerModel.setMsg("系统异常");
		}
		
		return pagerModel;
	}
	/***
	 * 查询文件信息byID
	 * @param query
	 * @return
	 */
	@Override
	public SettleCollectRule findById(Long id) {
		return settleCollectRuleManager.findSettleCollectRuleById(id);
	}
	/***
	 * 新增或修改文件信息
	 * @param query
	 * @return
	 */
	@Override
	public Integer saveOrEdit(SettleCollectRule settleCollectRule) {
		if(settleCollectRule.getId() != null){
	        return settleCollectRuleManager.updateSettleCollectRule(settleCollectRule);
	    }else{
	        return settleCollectRuleManager.saveSettleCollectRule(settleCollectRule);
	    }
	}
    @Override
    public Integer delById(Long id) {
        return settleCollectRuleManager.deleteSettleCollectRuleById(id);
    }
}
