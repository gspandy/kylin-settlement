package com.rkylin.settle.logic;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rkylin.settle.manager.SettleTransDetailManager;
import com.rkylin.settle.manager.SettleTransInvoiceManager;
import com.rkylin.settle.manager.SettleTransSummaryManager;
import com.rkylin.settle.pojo.SettleTransDetail;
import com.rkylin.settle.pojo.SettleTransInvoice;
import com.rkylin.settle.pojo.SettleTransInvoiceQuery;
import com.rkylin.settle.pojo.SettleTransSummary;

/***
 * 代收付数据库层逻辑
 * @author youyu
 *
 */
@Component("dsfLogic")
public class DsfLogic extends BasicLogic {
	@Autowired
	private SettleTransDetailManager settleTransDetailManager;			//交易信息Manager
	
	@Autowired
	private SettleTransSummaryManager settleTransSummaryManager;			//汇总表Manager
	
	@Autowired
	private SettleTransInvoiceManager settleTransInvoiceManager;			//结算表Manager
	
	/***
	 * 获取代收付交易
	 * @param dFlags 代收付标记位的Integer数组
	 * @return
	 * @throws Exception 
	 */
	public List<Map<String,Object>> getDsfTransDetail(Map<String, Object> map) throws Exception {
		List<Map<String,Object>> dsfDetailList = null;
		try{
			dsfDetailList = settleTransDetailManager.selectDsfTransInfo(map);
		}catch(Exception e){
			logger.error(">>>>>getDsfTransDetail方法异常！！！！！！！！！！");
			throw new Exception(e.getMessage());
		}
		return dsfDetailList;
	}
	
	/***
	 * 获取代收付交易count
	 * @param dFlags 代收付标记位的Integer数组
	 * @return
	 * @throws Exception 
	 */
	public int getDsfTransDetailCount(Map<String, Object> map) throws Exception {
		int result = 0;
		try{
			result = settleTransDetailManager.selectDsfTransCount(map);
		}catch(Exception e){
			logger.error(">>>>>getDsfTransDetailCount方法异常！！！！！！！！！！");
		}
		return result;
	}
	
	/**
	 * 插入或更新汇总表中的数据
	 * @throws Exception 
	 */
	public void insertTransSummery(List<SettleTransSummary> transSummeryList) throws Exception {
		logger.info(">>> >>> >>> 向汇总表中插入数据 ... ...");
		try{
			if (transSummeryList !=null && transSummeryList.size() > 0) {
				for (SettleTransSummary subbean : transSummeryList) {
					settleTransSummaryManager.saveSettleTransSummary(subbean);
				}
			}
		}catch(Exception e){
			logger.error(">>>>>insertTransSummery方法发生异常!!!!!!!!"+e.getMessage());
			throw new Exception(e.getMessage());
		}
	
	}
	
	/**
	 * 往结算表中插入数据
	 * @throws Exception 
	 */
	public void insertTransInvoice(List<SettleTransInvoice> transInvoiceList) throws Exception {
		try{
			if (transInvoiceList !=null && transInvoiceList.size() > 0) {
				logger.info(">>> >>> >>> 向结算表中插入信息 ... ...");
				for (SettleTransInvoice subBean : transInvoiceList) {
					settleTransInvoiceManager.saveSettleTransInvoice(subBean);
				}
			}else{
				logger.info(">>> >>> >>> transInvoiceList为空，不用向结算表中插入信息.....");
			}
		}catch(Exception e){
			logger.error(">>>>>>insertTransInvoice方法发生异常！！！！！"+e.getMessage());
			throw new Exception(e.getMessage());
		}
	
	}
	/**
	 * 更新交易表的DFLAG的值
	 * @param transDetailList
	 * @throws Exception 
	 */
	public void updateSettleTransDetail(List<SettleTransDetail> transDetailList) throws Exception  {
		logger.info(">>> >>> >>> 更新 交易表信息 ... ...");
		try{
			if (transDetailList !=null && transDetailList.size() > 0) {
				for (SettleTransDetail subbean : transDetailList) {
					settleTransDetailManager.updateSettleTransDetail(subbean);
				}
			}
		}catch(Exception e){
			logger.error(">>>>>>>updateSettleTransDetail方法异常！！！！！"+e.getMessage());
			throw new Exception(e.getMessage());
		}
		
	}
	
	/**
	 * 查询结算表List
	 * @throws Exception 
	 */
	public List<SettleTransInvoice> queryTransInvoiceList(Map<String,Object> paramMap) throws Exception {
		logger.info(">>> >>> >>> 查询结算表中的信息 ... ...");
		SettleTransInvoiceQuery query = null;
		List<SettleTransInvoice> invoiceList = null;
		try{
			query = new SettleTransInvoiceQuery();
			if(paramMap.get("dataSource") != null){
				query.setDataSource((Integer)paramMap.get("dataSource"));
			}
			if(paramMap.get("statusId") !=null){
				query.setStatusId((Integer)paramMap.get("statusId"));
			}
	    	if(paramMap.get("requestNo") !=null){
	    		query.setRequestNo((String)paramMap.get("requestNo"));
	    	}
	    	if(paramMap.get("dataSources") != null){
				query.setDataSources((Integer[])paramMap.get("dataSources"));
			}
	    	if(paramMap.get("orderTypes") != null){
				query.setOrderTypes((Integer[])paramMap.get("orderTypes"));
			}
	    	if(paramMap.get("rootInstCd") != null){
				query.setRootInstCd((String)paramMap.get("rootInstCd"));
			}
	    	if(paramMap.get("rootInstCds") != null){
				query.setRootInstCds((String [])paramMap.get("rootInstCds"));
			}
	    	if(paramMap.get("ids") != null){
				query.setIds((Integer [])paramMap.get("ids"));
			}
	    	invoiceList = settleTransInvoiceManager.queryList(query);
		}catch(Exception e){
			logger.error(">>>>>queryTransInvoiceList方法异常！！！！");
			throw new Exception(e.getMessage());
		}
		return invoiceList;
	}
	
	/**
	 * 查询结算表
	 * @throws Exception 
	 */
	public List<SettleTransInvoice> queryTransInvoices(Map<String,Object> paramMap) throws Exception {
		logger.info(">>> >>> >>> 查询结算表中的信息 ... ...");
		SettleTransInvoiceQuery query = new SettleTransInvoiceQuery();
		try{
			if(paramMap !=null && paramMap.size()>0){
				if(paramMap.get("requestNo") !=null){
					query.setRequestNo((String)paramMap.get("requestNo"));
				}
		    	if(paramMap.get("limit") != null){
		    		query.setLimit((Integer)paramMap.get("limit"));
		    	}
		    	if(paramMap.get("funcCodes") !=null){
		    		query.setFuncCodes((String[]) paramMap.get("funcCodes"));
				}
		     	if(paramMap.get("dataSource") !=null){
		     		query.setDataSource((Integer)paramMap.get("dataSource"));
		    	}
		     	if(paramMap.get("StatusId") !=null ){
		     		query.setStatusId((Integer) paramMap.get("StatusId"));
		     	}
		    	if(paramMap.get("rootInstCd") !=null){
		    		query.setRootInstCd((String)paramMap.get("rootInstCd"));
		    	}
			}
		}catch(Exception e){
			logger.error(">>>>>>>>queryTransInvoices异常！！！！！！！！");
			throw new Exception(e.getMessage());
		}
		return settleTransInvoiceManager.queryList(query);
	}
	
	/**
	 * 根据条件查询结算表记录数
	 * @throws Exception 
	 */
	public int queryTotalInvoiceByExample(SettleTransInvoiceQuery query) throws Exception {
		logger.info(">>> >>> >>> 查询结算表中的信息 ... ...");
		int total = 0;
		try{
			total =  settleTransInvoiceManager.queryTotalByExample(query);
		}catch(Exception e){
			logger.error(">>>>>>queryTotalInvoiceByExample方法异常！！！！！");
			throw new Exception(e.getMessage());
		}
		return total;
	}
}