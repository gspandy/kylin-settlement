package com.rkylin.settle.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rkylin.settle.manager.SettleKernelEntryManager;
import com.rkylin.settle.pojo.SettleKernelEntry;
import com.rkylin.settle.pojo.SettleKernelEntryQuery;
import com.rkylin.settle.service.SettleKernelEntryService;
import com.rkylin.settle.util.PagerModel;

/**
 * 项目名称：rkylin-settle-web
 * 类名称：SettleKernelEntryServiceImpl
 * 类描述：汇总规则业务逻辑
 * 创建人：Yang
 * @version
 */
@Service("settleKernelEntryService")
public class SettleKernelEntryServiceImpl implements SettleKernelEntryService {
	@Autowired
	private SettleKernelEntryManager settleKernelEntryManager;
	/***
	 * 分页条件查询文件信息
	 * @param query
	 * @return
	 */
	@Override
	public PagerModel<SettleKernelEntry> query(SettleKernelEntryQuery query) {
		//创建分页Model
		PagerModel<SettleKernelEntry> pagerModel = new PagerModel<SettleKernelEntry>();
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
			pagerModel.setList(settleKernelEntryManager.queryPage(query));
			pagerModel.setTotal(settleKernelEntryManager.countByExample(query));
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
	public SettleKernelEntry findById(Long id) {
		return settleKernelEntryManager.findSettleKernelEntryById(id);
	}
	/***
	 * 新增或修改文件信息
	 * @param query
	 * @return
	 */
	@Override
	public Integer saveOrEdit(SettleKernelEntry settleKernelEntry) {
		if(settleKernelEntry.getId() != null){
	        return settleKernelEntryManager.updateSettleKernelEntry(settleKernelEntry);
	    }else{
	        return settleKernelEntryManager.saveSettleKernelEntry(settleKernelEntry);
	    }
	}
	@Override
	public Integer delById(Long id) {
		return null;
	}
}
