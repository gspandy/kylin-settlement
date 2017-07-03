package com.rkylin.settle.service;

import java.util.List;

import com.rkylin.settle.pojo.SettleParameterInfo;
import com.rkylin.settle.pojo.SettleParameterInfoQuery;
import com.rkylin.settle.util.PagerModel;

/**
 * 项目名称：rkylin-settle-web
 * 类名称：SettleParameterInfoService
 * 类描述：系统参数信息业务逻辑
 * 创建人：CLF
 * 创建时间：2015年10月13日 下午3:13:10
 * 修改人：
 * 修改时间：2015年10月13日 下午3:13:10
 * 修改备注：
 * @version
 */
public interface SettleParameterInfoService {
	/***
	 * 查询系统参数信息
	 * @param query
	 * @return
	 */
	public List<SettleParameterInfo> select(SettleParameterInfoQuery query);
	/***
	 * 分页条件查询系统参数信息
	 * @param query
	 * @return
	 */
	public PagerModel<SettleParameterInfo> query(SettleParameterInfoQuery query);
	/***
	 * 查询系统参数信息byID
	 * @param query
	 * @return
	 */
	public SettleParameterInfo findById(Long id);
	/***
	 * 新增或修改系统参数信息
	 * @param query
	 * @return
	 */
	public Integer saveOrEdit(SettleParameterInfo settleParameterInfo);
}
