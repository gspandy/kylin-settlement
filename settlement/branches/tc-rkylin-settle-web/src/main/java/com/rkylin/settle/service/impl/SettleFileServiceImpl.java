package com.rkylin.settle.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rkylin.settle.manager.SettleFileManager;
import com.rkylin.settle.pojo.SettleFile;
import com.rkylin.settle.pojo.SettleFileQuery;
import com.rkylin.settle.service.SettleFileService;
import com.rkylin.settle.util.PagerModel;

/**
 * 项目名称：rkylin-settle-web
 * 类名称：SettleFileServiceImpl
 * 类描述：文件信息业务逻辑
 * 创建人：CLF
 * 创建时间：2015年8月31日 下午2:48:31
 * 修改人：
 * 修改时间：2015年8月31日 下午2:48:31
 * 修改备注：
 * @version
 */
@Service("settleFileService")
public class SettleFileServiceImpl implements SettleFileService {
	@Autowired
	private SettleFileManager settleFileManager;
	/***
	 * 分页条件查询文件信息
	 * @param query
	 * @return
	 */
	@Override
	public PagerModel<SettleFile> query(SettleFileQuery query) {
		//创建分页Model
		PagerModel<SettleFile> pagerModel = new PagerModel<SettleFile>();
		try {
			//初始化当前页
			if(query.getPageIndex()==null){
				query.setPageIndex(1);
			}
			//初始化显示条数
			if(query.getPageSize()==null){
				query.setPageSize(10);
			}
			query.setOrderBy("FILE_ID desc");
			//封装pageModel
			query.setOffset((query.getPageIndex() - 1) * query.getPageSize());
			pagerModel.setList(settleFileManager.queryPage(query));
			pagerModel.setTotal(settleFileManager.countByExample(query));
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
	public SettleFile findById(Long id) {
		return settleFileManager.findSettleFileById(id);
	}
	/***
	 * 新增或修改文件信息
	 * @param query
	 * @return
	 */
	@Override
	public Integer saveOrEdit(SettleFile SettleFile) {
		return settleFileManager.saveSettleFile(SettleFile);
	}
    @Override
    public Integer delById(Long id) {
        return settleFileManager.deleteSettleFileById(id);
    }
}
