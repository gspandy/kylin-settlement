package com.rkylin.settle.service;

import com.rkylin.settle.pojo.SettleProfitKey;
import com.rkylin.settle.pojo.SettleProfitKeyQuery;
import com.rkylin.settle.util.PagerModel;

/**
 * 项目名称：rkylin-settle-web
 * 类名称：SettleProfitKeyService
 * 类描述：清分规则业务逻辑
 * 创建人：CLF
 * 创建时间：2015年9月7日 下午3:05:24
 * 修改人：
 * 修改时间：2015年9月7日 下午3:05:24
 * 修改备注：
 * @version
 */
public interface SettleProfitKeyService {
	/***
	 * 分页条件查询清分规则
	 * @param query
	 * @return
	 */
	public PagerModel<SettleProfitKey> query(SettleProfitKeyQuery query);
	/***
	 * 查询清分规则byID
	 * @param query
	 * @return
	 */
	public SettleProfitKey findById(Long id);
	/***
	 * 新增或修改清分规则
	 * @param query
	 * @return
	 */
	public Integer saveOrEdit(SettleProfitKey settleProfitKey);
	/***
     * 删除清分规则byID
     * @param query
     * @return
     */
    public Integer delById(Long id);
}
