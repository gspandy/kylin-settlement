package com.rkylin.settle.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rkylin.settle.manager.SettleRuleManager;
import com.rkylin.settle.pojo.SettleRule;
import com.rkylin.settle.pojo.SettleRuleQuery;
import com.rkylin.settle.service.SettleRuleService;
import com.rkylin.settle.util.PagerModel;

/**
 * 项目名称：rkylin-settle-web
 * 类名称：SettleRuleServiceImpl
 * 类描述：对账规则业务逻辑
 * 创建人：CLF
 * 创建时间：2015年9月10日 下午4:04:01
 * 修改人：
 * 修改时间：2015年9月10日 下午4:04:01
 * 修改备注：
 * @version
 */
@Service("settleRuleService")
public class SettleRuleServiceImpl implements SettleRuleService {
	@Autowired
	private SettleRuleManager settleRuleManager;
	/***
	 * 分页条件查询文件信息
	 * @param query
	 * @return
	 */
	@Override
	public PagerModel<SettleRule> query(SettleRuleQuery query) {
		//创建分页Model
		PagerModel<SettleRule> pagerModel = new PagerModel<SettleRule>();
		try {
			//初始化当前页
			if(query.getPageIndex()==null){
				query.setPageIndex(1);
			}
			//初始化显示条数
			if(query.getPageSize()==null){
				query.setPageSize(10);
			}
			query.setOrderBy("RULE_ID desc");
			//封装pageModel
			query.setOffset((query.getPageIndex() - 1) * query.getPageSize());
			pagerModel.setList(settleRuleManager.queryPage(query));
			pagerModel.setTotal(settleRuleManager.countByExample(query));
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
	public SettleRule findById(Long id) {
		return settleRuleManager.findSettleRuleById(id);
	}
	/***
	 * 新增或修改文件信息
	 * @param query
	 * @return
	 */
	@Override
	public Integer saveOrEdit(SettleRule SettleRule) {
	    if(SettleRule.getRuleId()!=null){
	        return settleRuleManager.updateSettleRule(SettleRule);
	    }else{
	        return settleRuleManager.saveSettleRule(SettleRule);
	    }
	}
    @Override
    public Integer delById(Long id) {
        return settleRuleManager.deleteSettleRuleById(id);
    }
}
