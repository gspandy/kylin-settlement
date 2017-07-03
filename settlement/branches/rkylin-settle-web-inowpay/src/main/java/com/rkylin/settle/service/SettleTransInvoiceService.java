package com.rkylin.settle.service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import com.rkylin.settle.pojo.SettleTransDetail;
import com.rkylin.settle.pojo.SettleTransInvoice;
import com.rkylin.settle.pojo.SettleTransInvoiceQuery;
import com.rkylin.settle.util.PagerModel;

/***
 * 交易信息业务逻辑
 * @author YouYu
 *
 */
public interface SettleTransInvoiceService {
	/***
	 * 分页条件查询下游交易信息
	 * @param query
	 * @return
	 */
	public PagerModel<SettleTransInvoice> query(SettleTransInvoiceQuery query);
	/***
	 * 查询下游交易byID
	 * @param query
	 * @return
	 */
	public SettleTransInvoice findById(Long id);
	/***
	 * 修改下游交易信息
	 * @param query
	 * @return
	 */
	public Integer edit(SettleTransInvoice SettleTransInvoice);
	
	/**
	 * 导入结算表信息
	 * @param excelFile
	 * @param fileName
	 * @param request
	 * @return
	 */
	public String importInvoice(InputStream excelFile) ;
	public Map<String, Object> doBill(Integer[] ids) throws Exception;
	public Map<String, Object> doCancelBill(Integer[] ids) throws Exception;
	
	
	
	/***
	 * 批量查询交易信息by主键数组
	 * @param ids
	 * @return
	 */
	public List<SettleTransInvoice> queryByIds(Integer[] ids);
	
	/***
	 * 查询金额和条数
	 */
	public Map<String,Long> queryAmountAndTotalNum(String dataSource,String[] inRootInstCds,Integer[] orderTypes);
}
