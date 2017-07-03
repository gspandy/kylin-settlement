package com.rkylin.settle.service;

import com.rkylin.settle.pojo.SettleFile;
import com.rkylin.settle.pojo.SettleFileQuery;
import com.rkylin.settle.util.PagerModel;

/**
 * 项目名称：rkylin-settle-web
 * 类名称：SettleFileService
 * 类描述：文件信息业务逻辑
 * 创建人：CLF
 * 创建时间：2015年8月31日 下午2:49:53
 * 修改人：
 * 修改时间：2015年8月31日 下午2:49:53
 * 修改备注：
 * @version
 */
public interface SettleFileService {
	/***
	 * 分页条件查询文件信息
	 * @param query
	 * @return
	 */
	public PagerModel<SettleFile> query(SettleFileQuery query);
	/***
	 * 查询文件信息byID
	 * @param query
	 * @return
	 */
	public SettleFile findById(Long id);
	/***
	 * 新增或修改文件信息
	 * @param query
	 * @return
	 */
	public Integer saveOrEdit(SettleFile settleFile);
	/***
     * 删除文件信息byID
     * @param query
     * @return
     */
    public Integer delById(Long id);
}
