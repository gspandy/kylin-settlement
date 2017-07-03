package com.rkylin.settle.service;

import com.rkylin.settle.pojo.SettleRule;
import com.rkylin.settle.pojo.SettleRuleQuery;
import com.rkylin.settle.util.PagerModel;

/**
 * 项目名称：rkylin-settle-web
 * 类名称：SettleRuleService
 * 类描述：对账规则业务逻辑
 * 创建人：CLF
 * 创建时间：2015年9月10日 下午4:02:21
 * 修改人：
 * 修改时间：2015年9月10日 下午4:02:21
 * 修改备注：
 * @version
 */
public interface SettleRuleService {
	/***
	 * 分页条件查询对账规则
	 * @param query
	 * @return
	 */
	public PagerModel<SettleRule> query(SettleRuleQuery query);
	/***
	 * 查询对账规则byID
	 * @param query
	 * @return
	 */
	public SettleRule findById(Long id);
	/***
	 * 新增或修改对账规则
	 * @param query
	 * @return
	 */
	public Integer saveOrEdit(SettleRule settleRule);
	/***
     * 删除对账规则byID
     * @param query
     * @return
     */
    public Integer delById(Long id);
}
