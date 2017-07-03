package com.rkylin.settle.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rkylin.settle.manager.SettleParameterInfoManager;
import com.rkylin.settle.manager.SettleTransAccountManager;
import com.rkylin.settle.manager.SettlewebAccountsumCompareManager;
import com.rkylin.settle.pojo.SettleParameterInfo;
import com.rkylin.settle.pojo.SettleParameterInfoQuery;
import com.rkylin.settle.pojo.SettleTransAccount;
import com.rkylin.settle.pojo.SettleTransAccountQuery;
import com.rkylin.settle.pojo.SettlewebAccountsumCompare;
import com.rkylin.settle.pojo.SettlewebAccountsumCompareQuery;
import com.rkylin.settle.service.SettleWebAccountSumCompareService;
import com.rkylin.settle.util.PagerModel;

/**
 * @Description: 充值结算对账功能
 * @author youyu
 * @Create Time: 
 * @version V1.00
 */
@Controller
@RequestMapping("/compare")
@Scope("prototype")
public class SettleWebAccountSumCompareController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(SettleTransInvoiceController.class);
	@Autowired
	private SettleParameterInfoManager settleParameterInfoManager;
	
	@Autowired
	private SettleTransAccountManager settleTransAccountManager;
	
	@Autowired
	private SettlewebAccountsumCompareManager settlewebAccountsumCompareManager;
	
	@Autowired
	private SettleWebAccountSumCompareService settleWebAccountSumCompareService;
	
	/***
	 * 打开查询页面
	 * @param query
	 * @throws Exception
	 */
	@RequestMapping("/openQuery")
	public String openQuery() {
		return "/account/compare/query_accountSum_compare";
	}
	/***
	 * 打开查询详情页面
	 * @param query
	 * @throws Exception
	 */
	@RequestMapping("/openQueryPage")
	public String openQueryPage() {
		return "/account/compare/query_page";
	}
	
	/***
	 * 查询
	 * @param query
	 * @throws Exception
	 */
	@RequestMapping("/query_page_ajax")
	public void queryPageAjax(SettlewebAccountsumCompareQuery query) {
		try {
			PagerModel<SettlewebAccountsumCompare> pagerModel = settleWebAccountSumCompareService.queryPage(query);
			returnJsonList(pagerModel);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/***
	 * 全部到处excl
	 * @param query
	 * @throws Exception
	 */
	@RequestMapping("/query_and_output_excl")
	public void queryAndOutputExcl(SettlewebAccountsumCompareQuery query) throws Exception {
		logger.info(">>> >>> >>> >>> 开始 到处全部信息excl文件");
		boolean isSuccess = false;
		try {
			isSuccess = settleWebAccountSumCompareService.queryAndOutputExcl(request, query);
		} catch (Exception e) {
			isSuccess = false;
			e.printStackTrace();
			logger.error("全部到处excl异常:", e);
		}
		if(isSuccess) {
			writeText("导出成功! 请点击【我的下载】下载导出文件！");
		} else {
			writeText("导出失败!");
		}
		logger.info("<<< <<< <<< <<< 结束 到处全部信息excl文件 执行结果:" + isSuccess);
	}
	/***
	 * 查询
	 * @param query
	 * @throws Exception
	 */
	@RequestMapping("/query_ajax")
	public void queryAjax(String accountDate,String merchantCode,String payChannelId ) throws Exception {
		try {
			//根据条件从SETTLE_TRANS_ACCOUNT汇总数据并写入SETTLEWEB_ACCOUNTSUM_COMPARE表
			SettleTransAccountQuery query = new SettleTransAccountQuery();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			if(StringUtils.isNotBlank(accountDate)){
				query.setSettleTime(format.parse(accountDate));
			}
	        if(StringUtils.isNotBlank(merchantCode)){
	        	query.setMerchantCode(merchantCode);
	        }
	        if(StringUtils.isNotBlank(payChannelId)){
	        	query.setPayChannelId(payChannelId);
	        }
	        List<SettleTransAccount>  accountList  =settleTransAccountManager.summaryList(query);
	        SettlewebAccountsumCompare compare = null;
	        if(accountList !=null && accountList.size()>0){
	        	for(SettleTransAccount account : accountList){
	        		if("4015".equals(account.getTransType())){//充值
		        		compare = new SettlewebAccountsumCompare();
		        		compare.setPayChannelId(account.getPayChannelId());
		        		compare.setMerchantCode(account.getMerchantCode());
		        		compare.setReadType(account.getReadType());
		        		compare.setAccountType("0");
	        			compare.setCount2(String.valueOf(account.getTotal()));
	        			compare.setAmount2(String.valueOf(account.getTransAmount()));
		        		compare.setFeeAmount(String.valueOf(account.getFeeAmount()));
		        		compare.setStatusId(1);
		        		if(StringUtils.isNotBlank(accountDate)){
		        			compare.setAccountDate(format.parse(accountDate));
		    			}
		        		//根据PAY_CHANNEL_ID,MERCHANT_CODE,SETTLE_TIME,TRANS_TYPE,READ_TYPE从后台查询
		        		List<SettlewebAccountsumCompare> comList = this.queryCompare(compare, account);
		        		if(comList !=null && comList.size()>0){
		        			for(SettlewebAccountsumCompare cBean : comList){
		        				cBean.setAmount2(compare.getAmount2());
		        				cBean.setCount2(compare.getCount2());
		        				cBean.setFeeAmount(compare.getFeeAmount());
		        				settlewebAccountsumCompareManager.saveSettlewebAccountsumCompare(cBean);
			        		}
		        		}else{
		        			settlewebAccountsumCompareManager.saveSettlewebAccountsumCompare(compare);
		        		}
	        		}
	        	}
	        	
	        	for(SettleTransAccount account : accountList){
	        		if("4017".equals(account.getTransType())){//退款
		        		compare = new SettlewebAccountsumCompare();
		        		compare.setPayChannelId(account.getPayChannelId());
		        		compare.setMerchantCode(account.getMerchantCode());
		        		compare.setReadType(account.getReadType());
		        		compare.setAccountType("0");
	        			compare.setCount1(String.valueOf(account.getTotal()));
	        			compare.setAmount1(String.valueOf(account.getTransAmount()));
		        		compare.setFeeAmount(String.valueOf(account.getFeeAmount()));
		        		compare.setStatusId(1);
		        		if(StringUtils.isNotBlank(accountDate)){
		        			compare.setAccountDate(format.parse(accountDate));
		    			}
		        		
		        		//根据PAY_CHANNEL_ID,MERCHANT_CODE,SETTLE_TIME,TRANS_TYPE,READ_TYPE从后台查询
		        		List<SettlewebAccountsumCompare> comList = this.queryCompare(compare, account);
		        		if(comList !=null && comList.size()>0){
		        			for(SettlewebAccountsumCompare cBean : comList){
		        				cBean.setCount1(String.valueOf(account.getTotal()));
		        				cBean.setAmount1(String.valueOf(account.getTransAmount()));
		        				if(StringUtils.isNotBlank(cBean.getFeeAmount()) && StringUtils.isNotBlank(compare.getFeeAmount())){
		        					cBean.setFeeAmount((Long.parseLong(cBean.getFeeAmount())+Long.parseLong(compare.getFeeAmount()))+"");
		        				}else{
		        					cBean.setFeeAmount(compare.getFeeAmount());
		        				}
		        				
		        				settlewebAccountsumCompareManager.saveSettlewebAccountsumCompare(cBean);
			        		}
		        		}else{
		        			settlewebAccountsumCompareManager.saveSettlewebAccountsumCompare(compare);
		        		}
	        		}
	        	}

	        	for(SettleTransAccount account : accountList){
	        		if("4013".equals(account.getTransType())){//代收
		        		compare = new SettlewebAccountsumCompare();
		        		compare.setPayChannelId(account.getPayChannelId());
		        		compare.setMerchantCode(account.getMerchantCode());
		        		compare.setReadType(account.getReadType());
		        		compare.setAccountType("0");
	        			compare.setCount2(String.valueOf(account.getTotal()));
	        			compare.setAmount2(String.valueOf(account.getTransAmount()));
		        		compare.setFeeAmount(String.valueOf(account.getFeeAmount()));
		        		compare.setStatusId(1);
		        		if(StringUtils.isNotBlank(accountDate)){
		        			compare.setAccountDate(format.parse(accountDate));
		    			}
		        		//根据PAY_CHANNEL_ID,MERCHANT_CODE,SETTLE_TIME,TRANS_TYPE,READ_TYPE从后台查询
		        		List<SettlewebAccountsumCompare> comList = this.queryCompare(compare, account);
		        		if(comList !=null && comList.size()>0){
		        			for(SettlewebAccountsumCompare cBean : comList){
		        				cBean.setAmount2(compare.getAmount2());
		        				cBean.setCount2(compare.getCount2());
		        				cBean.setFeeAmount(compare.getFeeAmount());
		        				settlewebAccountsumCompareManager.saveSettlewebAccountsumCompare(cBean);
			        		}
		        		}else{
		        			settlewebAccountsumCompareManager.saveSettlewebAccountsumCompare(compare);
		        		}
	        		}
	        	}
	        }
	       
	        SettlewebAccountsumCompareQuery qr = new SettlewebAccountsumCompareQuery();
	        if(StringUtils.isNotBlank(accountDate)){
	        	qr.setAccountDate(format.parse(accountDate));
			}
	        if(StringUtils.isNotBlank(merchantCode)){
	        	qr.setMerchantCode(merchantCode);
	        }
	        if(StringUtils.isNotBlank(payChannelId)){
	        	qr.setPayChannelId(payChannelId);
	        }
	        qr.setStatusId(1);
	        qr.setAccountType("0");
	        List<SettlewebAccountsumCompare> compareList = new ArrayList<SettlewebAccountsumCompare>();
	        compareList = settlewebAccountsumCompareManager.queryList(qr);
	        
	        
	        //查询支付方式
	        SettleParameterInfoQuery queryZ = new SettleParameterInfoQuery();
	        queryZ.setParameterType("1000000005");
	        List<SettleParameterInfo> infoZList = settleParameterInfoManager.queryList(queryZ);
	        
	        //查询渠道
	        SettleParameterInfoQuery queryQ = new SettleParameterInfoQuery();
	        queryQ.setParameterType("1000000018");
	        List<SettleParameterInfo> infoQList = settleParameterInfoManager.queryList(queryQ);
	        
	        //查询机构(或协议)
	        SettleParameterInfoQuery queryM = new SettleParameterInfoQuery();
	        queryM.setParameterType("1000000013");
	        List<SettleParameterInfo> infoMList = settleParameterInfoManager.queryList(queryM);
	        
	        
	        Iterator<SettlewebAccountsumCompare> ite = compareList.iterator();
	        while(ite.hasNext()){
	        	SettlewebAccountsumCompare bean = ite.next();
	        	for(SettleParameterInfo infoZ :infoZList){
	        		if((bean.getReadType()).equals(infoZ.getParameterCode())){
	        			bean.setReadType(infoZ.getObligate3());
	        		}
	        	}
	        	for(SettleParameterInfo infoQ :infoQList){
	        		if((bean.getPayChannelId()).equals(infoQ.getParameterCode())){
	        			bean.setPayChannelId(infoQ.getObligate3());
	        		}
	        	}
	        	for(SettleParameterInfo infoM :infoMList){
	        		if((bean.getMerchantCode()).equals(infoM.getParameterCode())){
	        			bean.setMerchantCode(infoM.getObligate3());
	        		}
	        	}
	        }
	        
	        //把数据显示到页面上
	        PagerModel<SettlewebAccountsumCompare> pagerModel = new PagerModel<SettlewebAccountsumCompare>();
			pagerModel.setList(compareList);
			try {
				returnJsonList(pagerModel);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * 从SETTLEWEB_ACCOUNTSUM_COMPARE查数据
	 */
	public List<SettlewebAccountsumCompare>  queryCompare(SettlewebAccountsumCompare compare,SettleTransAccount account) throws Exception{
		if(compare ==null || account==null){
			return null;
		}
		SettlewebAccountsumCompareQuery queryC = new SettlewebAccountsumCompareQuery();
		queryC.setPayChannelId(compare.getPayChannelId());
		queryC.setMerchantCode(compare.getMerchantCode());
		queryC.setAccountDate(compare.getAccountDate());
		queryC.setReadType(compare.getReadType());
		queryC.setAccountType("0");
		queryC.setStatusId(1);
		List<SettlewebAccountsumCompare> comList = settlewebAccountsumCompareManager.queryList(queryC);
		return comList;
	}
	
	/**
	 * 保存并更新数据
	 * @param updateData
	 * @throws Exception
	 */
	@RequestMapping("/save_ajax")
	@ResponseBody
	public String saveAjax(String  updateData){
		String result ="success";
		try{
			 JSONArray arr = JSONArray.fromObject(updateData);
		     List<SettlewebAccountsumCompare> list = (List<SettlewebAccountsumCompare>) JSONArray.toCollection(arr, SettlewebAccountsumCompare.class);
			 for(SettlewebAccountsumCompare bean : list){
				 settlewebAccountsumCompareManager.saveSettlewebAccountsumCompare(bean);
			 }
		}catch(Exception e){
			result = "fail";
			logger.error("保存并更新充值对账表出错，e="+e);
		}
	    return result;
	}
	
	/**
	 * 获取渠道号和名称
	 * @return
	 */
	@RequestMapping("/getPayChannelIdAndName")
	public void getPayChannelIdAndName(){
		SettleParameterInfoQuery query = new SettleParameterInfoQuery();
		query.setParameterType("1000000018");
		List<SettleParameterInfo> list  =  settleParameterInfoManager.queryList(query); 
		
		PagerModel<SettleParameterInfo> pagerModel = new PagerModel<SettleParameterInfo>();
		pagerModel.setList(list);
		try {
			returnJsonList(pagerModel);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取机构号和名称
	 * @return
	 */
	@RequestMapping("/getProtocol")
	public void getProtocol(){
		SettleParameterInfoQuery query = new SettleParameterInfoQuery();
		query.setParameterType("1000000013");
		List<SettleParameterInfo> list  =  settleParameterInfoManager.queryList(query); 
		
		PagerModel<SettleParameterInfo> pagerModel = new PagerModel<SettleParameterInfo>();
		pagerModel.setList(list);
		try {
			returnJsonList(pagerModel);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
