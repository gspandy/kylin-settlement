package com.rkylin.settle.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rkylin.settle.manager.SettleParameterInfoManager;
import com.rkylin.settle.pojo.SettleParameterInfo;
import com.rkylin.settle.pojo.SettleParameterInfoQuery;
import com.rkylin.settle.service.SettleParameterInfoService;
import com.rkylin.settle.util.PagerModel;

/**
 * 项目名称：rkylin-settle-web
 * 类名称：SettleParameterInfoServiceImpl
 * 类描述：系统参数信息业务逻辑
 * 创建人：CLF
 * 创建时间：2015年10月13日 下午3:15:52
 * 修改人：
 * 修改时间：2015年10月13日 下午3:15:52
 * 修改备注：
 * @version
 */
@Service("settleParameterInfo")
public class SettleParameterInfoServiceImpl implements SettleParameterInfoService {
	@Autowired
	private SettleParameterInfoManager settleParameterInfoManager;
	/***
	 * 分页条件查询系统参数信息
	 * @param query
	 * @return
	 */
	@Override
	public PagerModel<SettleParameterInfo> query(SettleParameterInfoQuery query) {
		//创建分页Model
		PagerModel<SettleParameterInfo> pagerModel = new PagerModel<SettleParameterInfo>();
		try {
			//初始化当前页
			if(query.getPageIndex()==null){
				query.setPageIndex(1);
			}
			//初始化显示条数
			if(query.getPageSize()==null){
				query.setPageSize(10);
			}
			query.setOrderBy("PARAMETER_ID ASC");
			//封装pageModel
			query.setOffset((query.getPageIndex() - 1) * query.getPageSize());
			pagerModel.setList(settleParameterInfoManager.queryPage(query));
			pagerModel.setTotal(settleParameterInfoManager.countByExample(query));
			pagerModel.setResult("ok");
		} catch (Exception e) {
			e.printStackTrace();
			pagerModel.setResult("error");
			pagerModel.setMsg("系统异常");
		}
		
		return pagerModel;
	}
	/***
	 * 查询系统参数信息byID
	 * @param query
	 * @return
	 */
	@Override
	public SettleParameterInfo findById(Long id) {
		return settleParameterInfoManager.findSettleParameterInfoById(id);
	}
	/***
	 * 新增或修改系统参数信息
	 * @param query
	 * @return
	 */
	@Override
	public Integer saveOrEdit(SettleParameterInfo SettleParameterInfo) {
	    if(SettleParameterInfo.getParameterId()!=null){
	        return settleParameterInfoManager.updateSettleParameterInfo(SettleParameterInfo);
	    }else{
	        return settleParameterInfoManager.saveSettleParameterInfo(SettleParameterInfo);
	    }
	}
	@Override
	public List<SettleParameterInfo> select(SettleParameterInfoQuery query) {
		return settleParameterInfoManager.queryList(query);
	}
}
