package com.rkylin.settle.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.rkylin.settle.constant.Constants;
import com.rkylin.settle.constant.SettleConstants;
import com.rkylin.settle.exception.SettleException;
import com.rkylin.settle.logic.BasicLogic;
import com.rkylin.settle.manager.ParameterInfoManager;
import com.rkylin.settle.manager.SettleParameterInfoManager;
import com.rkylin.settle.manager.SettleTransDetailManager;
import com.rkylin.settle.pojo.ParameterInfo;
import com.rkylin.settle.pojo.ParameterInfoQuery;
import com.rkylin.settle.pojo.SettleParameterInfo;
import com.rkylin.settle.pojo.SettleParameterInfoQuery;
import com.rkylin.settle.pojo.SettleTransDetail;
import com.rkylin.settle.pojo.SettleTransDetailQuery;
import com.rkylin.settle.service.TradeService;
import com.rkylin.settle.util.SettlementUtil;
import com.rkylin.utils.RkylinMailUtil;
import com.rop.response.Response;
import com.rop.service.IAPIService;

@Service("tradeService")
public class TradeServiceImpl implements TradeService,
IAPIService{
	private static Logger logger = LoggerFactory.getLogger(TradeServiceImpl.class);
	@Autowired
	SettleTransDetailManager settleTransDetailManager;
	@Autowired
	ParameterInfoManager parameterInfoManager;
	@Autowired
    private SettleParameterInfoManager settleParameterInfoManager;//清算属性表
	@Autowired 
	private BasicLogic basicLogic;
	
	@Override
	public Response doJob(Map<String, String[]> paramMap, String methodName) {
		return null;
	}
	/***
	 * 存储账户系统交易信息
	 */
//	@Override
//	public Map<String,String> saveMTAegisTrans(SettleTransDetail settleTransDetail){
//		logger.info(">>> >>> MTAegisTrans 交易信息读入... ORDER_NO:" + settleTransDetail.getOrderNo() + "; FUNC_CODE:"+ settleTransDetail.getFuncCode() +"; MERCHANT_CODE:" + settleTransDetail.getMerchantCode()); 
//		Map<String,String> rtnMap = new HashMap<String,String>();
//		SettleTransDetailQuery query= new SettleTransDetailQuery();
//		query.setOrderNo(settleTransDetail.getOrderNo());
//		query.setMerchantCode(settleTransDetail.getMerchantCode());
//		query.setFuncCode(settleTransDetail.getFuncCode());
//		query.setOrderType(0);
//		try{
//			/*
//			 * 判断账户交易信息状态，清结算状态对应关系，失败 
//			 */
//		    settleTransDetail.setStatusId(getMerchantCode(settleTransDetail.getStatusId()));
//		    if(settleTransDetailManager.countSettleTransDetail(query) > 0){
//			    logger.info(">>> >>> 重复数据 funcCode:"+settleTransDetail.getFuncCode()+"; orderNo:"+settleTransDetail.getOrderNo()+";merchantCode:"+settleTransDetail.getMerchantCode());
//			}else{
//				settleTransDetailManager.saveSettleTransDetail(settleTransDetail);
//				logger.info(">>> >>> 执行【插入】操作 funcCode:"+settleTransDetail.getFuncCode()+"; orderNo:"+settleTransDetail.getOrderNo()+";merchantCode:"+settleTransDetail.getMerchantCode());
//			}
//		}catch(Exception ex){
//			ex.printStackTrace();
//		    logger.error(">>>>交易信息入库失败，errCode:9999",ex);
//			rtnMap.put("errCode","9999");
//			rtnMap.put("errMsg",ex.getMessage());
//			return rtnMap;
//		}
//		rtnMap.put("errCode","0000");
//		return rtnMap;
//	}
	/***
	 * 存储账户系统交易信息
	 */
	@Override
	@Transactional(rollbackFor = SettleException.class, propagation = Propagation.NESTED)
	public Map<String,String> saveAccountTrans(SettleTransDetail settleTransDetail){
		logger.info(">>> >>> AccountTrans 交易信息读入... ORDER_NO:" + settleTransDetail.getOrderNo() + "; FUNC_CODE:"+ settleTransDetail.getFuncCode() +"; MERCHANT_CODE:" + settleTransDetail.getMerchantCode()); 
		List<SettleTransDetail> settleTransDetailList = new ArrayList<SettleTransDetail>();
		/*
		 * 匹配'订单号'对应关系
		 */
		settleTransDetail = basicLogic.editTransOrderNo(settleTransDetail);
		if ("3001".equals(settleTransDetail.getFuncCode())) {
			if (Constants.FN_ID.equals(settleTransDetail.getMerchantCode())
					|| Constants.HT_ID.equals(settleTransDetail.getMerchantCode())
					|| Constants.JRD_ID.equals(settleTransDetail.getMerchantCode())
					|| Constants.MZ_ID.equals(settleTransDetail.getMerchantCode())) {
			} else {
				settleTransDetail.setRelateOrderNo(settleTransDetail.getRequestNo());
			}
		}
		/*
		 * 匹配'金额'对应关系
		 */
		settleTransDetail = basicLogic.editTransAmount(settleTransDetail);
		Map<String,String> rtnMap = new HashMap<String,String>();
		SettleTransDetailQuery query= new SettleTransDetailQuery();
		query.setOrderNo(settleTransDetail.getOrderNo());
		query.setMerchantCode(settleTransDetail.getMerchantCode());
		query.setFuncCode(settleTransDetail.getFuncCode());
		query.setOrderType(0);
		try{
			/*
			 * 判断账户交易信息状态，清结算状态对应关系，失败 
			 */
		    settleTransDetail.setStatusId(getMerchantCode(settleTransDetail.getStatusId()));
			if(settleTransDetailManager.countSettleTransDetail(query) > 0){
				settleTransDetailManager.updateByAccountOrderInfo(settleTransDetail);
			    logger.info(">>> >>> 执行【更新】操作 funcCode:"+settleTransDetail.getFuncCode()+"; orderNo:"+settleTransDetail.getOrderNo()+";merchantCode:"+settleTransDetail.getMerchantCode());
			}else{
//				settleTransDetailManager.saveSettleTransDetail(settleTransDetail);
				settleTransDetailList.add(settleTransDetail);
				logger.info(">>> >>> 执行【插入】操作 funcCode:"+settleTransDetail.getFuncCode()+"; orderNo:"+settleTransDetail.getOrderNo()+";merchantCode:"+settleTransDetail.getMerchantCode());
			}
			/*
			 * 批量插入清结算交易信息
			 */
			basicLogic.batchSaveSettleTransDetail(settleTransDetailList);
		}catch(Exception ex){
			ex.printStackTrace();
		    logger.error(">>>>交易信息入库失败，errCode:9999",ex);
			rtnMap.put("errCode","9999");
			rtnMap.put("errMsg",ex.getMessage());
			return rtnMap;
		}
		rtnMap.put("errCode","0000");
		return rtnMap;
	}

	@Override
	public Map<String, String> saveUpstreamTransByrop(String rootInstCd,
			String payChannelId, String readType, String fileType,
			String invoicedate, String batch) {
		logger.info("读取HT通联对账文件 ————————————START————————————");
		Map<String,String> rtnMap = new HashMap<String,String>();
		rtnMap.put("errCode", "0000");
		rtnMap.put("errMsg", "成功");

    	ParameterInfoQuery keyList =  new ParameterInfoQuery();
    	keyList.setParameterCode(SettleConstants.DAYEND);
    	List<ParameterInfo> parameterInfo = parameterInfoManager.queryList(keyList);
    	if (!"0".equals(parameterInfo.get(0).getParameterValue())) {
    		rtnMap.put("errCode", "0001");
    		rtnMap.put("errMsg", "日终没有正常结束！");
    		RkylinMailUtil.sendMailThread("清洁算开始异常","日终没有正常结束，不能开始清洁算操作", "21401233@qq.com");
    		return rtnMap;
    	}
    	
    	SettlementUtil settlementUtils = new SettlementUtil();
    	String accountDate = "";
//    	String accountDate2 = "";
    	try {
	    	if (invoicedate == null || "".equals(invoicedate)) {
				try {
					accountDate = (String) settlementUtils.getAccountDate("yyyyMMdd",-1,"");
//					accountDate2 = (String) settlementUtils.getAccountDate("yyyyMMdd",0,"");
				} catch (Exception e2) {
					logger.error("计算账期异常！" + e2.getMessage());
					rtnMap.put("errCode", "0001");
					rtnMap.put("errMsg", "计算账期异常");
					return rtnMap;
				}
	    	} else {
	    		accountDate = invoicedate.replace("-", "");
//	    		accountDate2 = (String)settlementUtils.getAccountDate(invoicedate,"yyyyMMdd",1,"");
	    	}
			logger.info("取得的账期为"+ accountDate);
			
    	} catch (Exception z) {
			logger.error("对账文件生成失败，请联系相关负责人"+z.getMessage());
			rtnMap.put("errCode", "0068");
			rtnMap.put("errMsg", "对账文件生成失败，请联系相关负责人");
			throw new SettleException("对账文件生成失败，请联系相关负责人");
    	}
		logger.info("读取HT通联对账文件 ————————————END————————————");
		return rtnMap;
	}
	public static List<SettleParameterInfo> settleParameterInfo = new ArrayList<SettleParameterInfo>();
	/**
	 * @Description: /获取账户交易信息状态--清结算交易信息状态对应关系
	 * @param statusId 账户交易信息状态
	 * @return
	 * @throws ParseException
	 * @author CLF
	 */
	private Integer getMerchantCode(Integer statusId) throws ParseException{
	    if(statusId==null)return 0;
	    if(settleParameterInfo.isEmpty()){
	        SettleParameterInfoQuery query =  new SettleParameterInfoQuery();
	        //获取账户交易信息状态对应关系
	        query.setParameterType(SettleConstants.PARAMETER_TYPE_ACCOUNT_STATUS);
	        query.setParameterCode("0");
	        settleParameterInfo = settleParameterInfoManager.queryList(query);
	    }
        if(settleParameterInfo.size()>0){
             String parameterValue = settleParameterInfo.get(0).getParameterValue();
             String[] values = parameterValue.split("\\|");
             Integer returnVal = 1;
             for(String val:values){
                 if(statusId == Integer.parseInt(val)){
                     returnVal = 0;
                 }
             }
             return returnVal;
        }else{
            logger.error("取机构号对应关系异常！");
            throw new SettleException("取机构号对应关系异常！"); 
        }
    }
}
