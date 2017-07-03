package com.rkylin.settle.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rkylin.settle.manager.SettleProfitRuleManager;
import com.rkylin.settle.pojo.SettleProfitRule;
import com.rkylin.settle.pojo.SettleProfitRuleQuery;
import com.rkylin.settle.service.SettleProfitRuleService;
import com.rkylin.settle.util.PagerModel;

/**
 * 项目名称：rkylin-settle-web
 * 类名称：SettleProfitRuleServiceImpl
 * 类描述：清分规则明细业务逻辑
 * 创建人：CLF
 * 创建时间：2015年8月31日 下午2:48:31
 * 修改人：
 * 修改时间：2015年8月31日 下午2:48:31
 * 修改备注：
 * @version
 */
@Service("settleProfitRuleService")
public class SettleProfitRuleServiceImpl implements SettleProfitRuleService {
	@Autowired
	private SettleProfitRuleManager settleProfitRuleManager;
	/***
	 * 分页条件查询清分规则明细
	 * @param query
	 * @return
	 */
	@Override
	public PagerModel<SettleProfitRule> query(SettleProfitRuleQuery query) {
		//创建分页Model
		PagerModel<SettleProfitRule> pagerModel = new PagerModel<SettleProfitRule>();
		try {
			//初始化当前页
			if(query.getPageIndex()==null){
				query.setPageIndex(1);
			}
			//初始化显示条数
			if(query.getPageSize()==null){
				query.setPageSize(10);
			}
			query.setOrderBy("PROFIT_DETAIL_ID ASC,SUB_ID ASC");
			//封装pageModel
			query.setOffset((query.getPageIndex() - 1) * query.getPageSize());
			pagerModel.setList(settleProfitRuleManager.queryPage(query));
			pagerModel.setTotal(settleProfitRuleManager.countByExample(query));
			pagerModel.setResult("ok");
		} catch (Exception e) {
			e.printStackTrace();
			pagerModel.setResult("error");
			pagerModel.setMsg("系统异常");
		}
		
		return pagerModel;
	}
	/***
	 * 查询清分规则明细byID
	 * @param query
	 * @return
	 */
	@Override
	public SettleProfitRule findById(SettleProfitRuleQuery query) {
	    List<SettleProfitRule> result = settleProfitRuleManager.queryList(query);
	    if(result!=null && !result.isEmpty()){
	        return result.get(0);
	    }
		return new SettleProfitRule();
	}
	/***
	 * 新增清分规则明细
	 * @param query
	 * @return
	 */
	@Override
	public Integer save(SettleProfitRule settleProfitRule) {
        return settleProfitRuleManager.saveSettleProfitRule(settleProfitRule);
	}
	/***
     * 修改清分规则明细
     * @param query
     * @return
     */
    @Override
    public Integer edit(SettleProfitRule settleProfitRule) {
        return settleProfitRuleManager.updateSettleProfitRule(settleProfitRule);
    }
    @Override
    public Integer delById(SettleProfitRule settleProfitRule) {
        SettleProfitRuleQuery query = new SettleProfitRuleQuery();
        query.setProfitDetailId(settleProfitRule.getProfitDetailId());
        query.setSubId(settleProfitRule.getSubId());
        return settleProfitRuleManager.deleteSettleProfitRule(query);
    }
}
