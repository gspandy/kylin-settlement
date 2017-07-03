package com.rkylin.settle.service;

import java.util.List;

import com.rkylin.settle.pojo.SettleProfitRule;

/**
 * 项目名称：rkylin-settle-web
 * 类名称：SettleProfitMatchService
 * 类描述：清分规则-明细分配业务逻辑
 * 创建人：CLF
 * 创建时间：2015年9月14日 下午2:40:08
 * 修改人：
 * 修改时间：2015年9月14日 下午2:40:08
 * 修改备注：
 * @version
 */
public interface SettleProfitMatchService {
    /***
     * 查询key对应的、未分配的规则明细，根据keyId
     * 未分配是指：detailId为-1
     * @param String
     * @return
     */
    public List<SettleProfitRule> findByprofitDetailId(String profitdetailId);
    
    /***
     * 根据分配页面操作数据，重新分配规则-明细
     * @param String
     * @return
     */
    public boolean updateByMatch(String matchVal,String profitDetailId);
}
