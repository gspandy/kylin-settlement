package com.rkylin.settle.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rkylin.settle.manager.SettleProfitKeyManager;
import com.rkylin.settle.pojo.SettleProfitKey;
import com.rkylin.settle.pojo.SettleProfitKeyQuery;
import com.rkylin.settle.service.SettleProfitKeyService;
import com.rkylin.settle.util.PagerModel;

/**
 * 项目名称：rkylin-settle-web
 * 类名称：SettleProfitKeyServiceImpl
 * 类描述：清分规则业务逻辑
 * 创建人：CLF
 * 创建时间：2015年8月31日 下午2:48:31
 * 修改人：
 * 修改时间：2015年8月31日 下午2:48:31
 * 修改备注：
 * @version
 */
@Service("settleProfitKeyService")
public class SettleProfitKeyServiceImpl implements SettleProfitKeyService {
	@Autowired
	private SettleProfitKeyManager settleProfitKeyManager;
	/***
	 * 分页条件查询清分规则
	 * @param query
	 * @return
	 */
	@Override
	public PagerModel<SettleProfitKey> query(SettleProfitKeyQuery query) {
		//创建分页Model
		PagerModel<SettleProfitKey> pagerModel = new PagerModel<SettleProfitKey>();
		try {
			//初始化当前页
			if(query.getPageIndex()==null){
				query.setPageIndex(1);
			}
			//初始化显示条数
			if(query.getPageSize()==null){
				query.setPageSize(10);
			}
			query.setOrderBy("PROFIT_KEY_ID DESC");
			//封装pageModel
			query.setOffset((query.getPageIndex() - 1) * query.getPageSize());
			pagerModel.setList(settleProfitKeyManager.queryPage(query));
			pagerModel.setTotal(settleProfitKeyManager.countByExample(query));
			pagerModel.setResult("ok");
		} catch (Exception e) {
			e.printStackTrace();
			pagerModel.setResult("error");
			pagerModel.setMsg("系统异常");
		}
		
		return pagerModel;
	}
	/***
	 * 查询清分规则byID
	 * @param query
	 * @return
	 */
	@Override
	public SettleProfitKey findById(Long id) {
		return settleProfitKeyManager.findSettleProfitKeyById(id);
	}
	/***
	 * 新增或修改清分规则
	 * @param query
	 * @return
	 */
	@Override
	public Integer saveOrEdit(SettleProfitKey settleProfitKey) {
	    if(settleProfitKey.getProfitKeyId()!=null){
	        SettleProfitKey record = settleProfitKeyManager.findSettleProfitKeyById(Long.parseLong(settleProfitKey.getProfitKeyId().toString()));
	        settleProfitKey.setCreatedTime(record.getCreatedTime());
	        settleProfitKey.setUpdatedTime(new Date());
	        return settleProfitKeyManager.updateSettleProfitKey(settleProfitKey);
	    }else{
	        return settleProfitKeyManager.saveSettleProfitKey(settleProfitKey);
	    }
	}
    @Override
    public Integer delById(Long id) {
        return settleProfitKeyManager.deleteSettleProfitKeyById(id);
    }
}
