package com.rkylin.settle.service;

import java.util.List;

import com.rkylin.settle.pojo.SettleFileColumn;

/**
 * 项目名称：rkylin-settle-web
 * 类名称：SettleFileMatchService
 * 类描述：文件-文件列分配业务逻辑
 * 创建人：CLF
 * 创建时间：2015年9月15日 下午2:36:49
 * 修改人：
 * 修改时间：2015年9月15日 下午2:36:49
 * 修改备注：
 * @version
 */
public interface SettleFileMatchService {
    /***
     * 查询file对应的、未分配的列明细，根据fileSubId
     * 未分配是指：detailId为-1
     * @param String
     * @return
     */
    public List<SettleFileColumn> findByFileSubId(Integer fileSubId);
    
    /***
     * 根据分配页面操作数据，重新分配文件-文件列
     * @param String
     * @return
     */
    public boolean updateByMatch(String matchVal,Integer fileSubId);
}
