package com.rkylin.settle.service;

import com.rkylin.settle.pojo.SettleKernelEntry;
import com.rkylin.settle.pojo.SettleKernelEntryQuery;
import com.rkylin.settle.util.PagerModel;

/**
 * 项目名称：rkylin-settle-web
 * 类名称：SettleRuleService
 * 类描述：汇总规则业务逻辑
 * 创建人：Yang
 * @version
 */
public interface SettleKernelEntryService {
	/***
	 * 分页条件查询汇总规则
	 * @param query
	 * @return
	 */
	public PagerModel<SettleKernelEntry> query(SettleKernelEntryQuery query);
	/***
	 * 查询汇总规则byID
	 * @param query
	 * @return
	 */
	public SettleKernelEntry findById(Long id);
	/***
	 * 新增或修改汇总规则
	 * @param query
	 * @return
	 */
	public Integer saveOrEdit(SettleKernelEntry settleKernelEntry);
	/***
     * 删除汇总规则byID
     * @param query
     * @return
     */
    public Integer delById(Long id);
}
