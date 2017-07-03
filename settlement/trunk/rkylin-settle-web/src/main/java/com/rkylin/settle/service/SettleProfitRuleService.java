package com.rkylin.settle.service;

import com.rkylin.settle.pojo.SettleProfitRule;
import com.rkylin.settle.pojo.SettleProfitRuleQuery;
import com.rkylin.settle.util.PagerModel;

/**
 * 项目名称：rkylin-settle-web
 * 类名称：SettleProfitRuleService
 * 类描述：清分规则明细业务逻辑
 * 创建人：CLF
 * 创建时间：2015年9月7日 下午3:05:24
 * 修改人：
 * 修改时间：2015年9月7日 下午3:05:24
 * 修改备注：
 * @version
 */
public interface SettleProfitRuleService {
	/***
	 * 分页条件查询清分规则明细
	 * @param query
	 * @return
	 */
	public PagerModel<SettleProfitRule> query(SettleProfitRuleQuery query);
	/***
	 * 查询清分规则明细byID
	 * @param query
	 * @return
	 */
	public SettleProfitRule findById(SettleProfitRuleQuery query);
	/***
	 * 新增清分规则明细
	 * @param query
	 * @return
	 */
	public Integer save(SettleProfitRule settleProfitRule);
	/***
     * 修改清分规则明细
     * @param query
     * @return
     */
	public Integer edit(SettleProfitRule settleProfitRule);
	/***
     * 删除清分规则明细byID
     * @param query
     * @return
     */
    public Integer delById(SettleProfitRule settleProfitRule);
}
