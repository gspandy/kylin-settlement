package com.rkylin.settle.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rkylin.settle.manager.SettleFileColumnManager;
import com.rkylin.settle.pojo.SettleFileColumn;
import com.rkylin.settle.pojo.SettleFileColumnQuery;
import com.rkylin.settle.service.SettleFileColumnService;
import com.rkylin.settle.util.PagerModel;

/**
 * 项目名称：rkylin-settle-web
 * 类名称：SettleFileColumnServiceImpl
 * 类描述：文件信息业务逻辑
 * 创建人：CLF
 * 创建时间：2015年8月31日 下午2:48:31
 * 修改人：
 * 修改时间：2015年8月31日 下午2:48:31
 * 修改备注：
 * @version
 */
@Service("settleFileColumnService")
public class SettleFileColumnServiceImpl implements SettleFileColumnService {
	@Autowired
	private SettleFileColumnManager settleFileColumnManager;
	/***
	 * 分页条件查询文件信息
	 * @param query
	 * @return
	 */
	@Override
	public PagerModel<SettleFileColumn> query(SettleFileColumnQuery query) {
		//创建分页Model
		PagerModel<SettleFileColumn> pagerModel = new PagerModel<SettleFileColumn>();
		try {
			//初始化当前页
			if(query.getPageIndex()==null){
				query.setPageIndex(1);
			}
			//初始化显示条数
			if(query.getPageSize()==null){
				query.setPageSize(10);
			}
			query.setOrderBy("FILE_COLUMN_ID desc");
			//封装pageModel
			query.setOffset((query.getPageIndex() - 1) * query.getPageSize());
			pagerModel.setList(settleFileColumnManager.queryPage(query));
			pagerModel.setTotal(settleFileColumnManager.countByExample(query));
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
	public SettleFileColumn findById(Long id) {
		return settleFileColumnManager.findSettleFileColumnById(id);
	}
	/***
	 * 新增或修改文件信息
	 * @param query
	 * @return
	 */
	@Override
	public Integer saveOrEdit(SettleFileColumn SettleFileColumn) {
		return settleFileColumnManager.saveSettleFileColumn(SettleFileColumn);
	}
    @Override
    public Integer delById(Long id) {
        return settleFileColumnManager.deleteSettleFileColumnById(id);
    }
}
