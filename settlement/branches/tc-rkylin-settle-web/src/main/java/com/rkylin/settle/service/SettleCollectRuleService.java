package com.rkylin.settle.service;

import com.rkylin.settle.pojo.SettleCollectRule;
import com.rkylin.settle.pojo.SettleCollectRuleQuery;
import com.rkylin.settle.util.PagerModel;

/**
 * 项目名称：rkylin-settle-web
 * 类名称：SettleRuleService
 * 类描述：汇总规则业务逻辑
 * 创建人：Yang
 * @version
 */
public interface SettleCollectRuleService {
	/***
	 * 分页条件查询汇总规则
	 * @param query
	 * @return
	 */
	public PagerModel<SettleCollectRule> query(SettleCollectRuleQuery query);
	/***
	 * 查询汇总规则byID
	 * @param query
	 * @return
	 */
	public SettleCollectRule findById(Long id);
	/***
	 * 新增或修改汇总规则
	 * @param query
	 * @return
	 */
	public Integer saveOrEdit(SettleCollectRule settleCollectRule);
	/***
     * 删除汇总规则byID
     * @param query
     * @return
     */
    public Integer delById(Long id);
}
