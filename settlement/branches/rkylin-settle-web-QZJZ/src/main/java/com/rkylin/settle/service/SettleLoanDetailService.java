package com.rkylin.settle.service;

import java.util.List;
import java.util.Map;

import com.rkylin.settle.pojo.SettleLoanDetail;
import com.rkylin.settle.pojo.SettleLoanDetailQuery;
import com.rkylin.settle.pojo.SettleTransDetail;
import com.rkylin.settle.pojo.SettleTransDetailQuery;
import com.rkylin.settle.util.PagerModel;

/***
 * 下游交易信息业务逻辑
 * @author Yang
 *
 */
public interface SettleLoanDetailService {
	/***
	 * 分页条件查询下游交易信息
	 * @param query
	 * @return
	 */
	public PagerModel<SettleLoanDetail> query(SettleLoanDetailQuery query);
	/***
	 * 查询下游交易byID
	 * @param query
	 * @return
	 */
	public SettleLoanDetail findById(Long id);
	/***
	 * 修改下游交易信息
	 * @param query
	 * @return
	 */
	public Integer edit(SettleLoanDetail settleLoanDetail);
	/***
	 * 批量查询交易信息by主键数组
	 * @param ids
	 * @return
	 */
	public List<SettleTransDetail> queryByIds(Integer[] ids);
	/***
	 * 批量查询交易信息by主键数组
	 * @param queryMap
	 * @return
	 */
	public List<SettleTransDetail> queryByIds(Map<String, Object> queryMap);
	/***
	 * 获取分润funcCodes
	 */
	public List<String> queryProfitFuncCodes() throws Exception;
	/***
	 * 调用账户系统接口冲正
	 * @return
	 */
	public Map<String, Object> doCorrectByAccount(List<SettleTransDetail> settleTransDetailList) throws Exception;
	/***
	 * 调用账户系统接口抹账或退款
	 * @return
	 */
	public Map<String, Object> doAccOfOrRefundByAccount(List<SettleTransDetail> settleTransDetailList) throws Exception;
	/***
     * 挂账
     * @return
     */
    public Map<String, Object> doBill(Integer[] ids) throws Exception;
    /***
     * 取消挂账
     * @return
     */
    public Map<String, Object> doCancelBill(Integer[] ids) throws Exception;
}
