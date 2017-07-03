package com.rkylin.settle.logic;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rkylin.common.RedisIdGenerator;
import com.rkylin.settle.manager.SettleParameterInfoManager;
import com.rkylin.settle.manager.SettleTransBillManager;
import com.rkylin.settle.manager.SettleTransDetailManager;
import com.rkylin.settle.manager.SettleTransProfitManager;
import com.rkylin.settle.manager.TransOrderInfoAccountManager;
import com.rkylin.settle.manager.TransOrderInfoManager;
import com.rkylin.settle.pojo.SettleParameterInfo;
import com.rkylin.settle.pojo.SettleTransDetail;
import com.rkylin.settle.pojo.SettleTransDetailQuery;
import com.rkylin.settle.pojo.TransOrderInfo;
import com.rkylin.settle.pojo.TransOrderInfoAccount;
import com.rkylin.settle.service.TradeService;
import com.rkylin.settle.util.LogicConstantUtil;

@Service("getMTAegisTransOrderInfo")
public class GetMTAegisTransOrderInfo extends BasicLogic {
	@Autowired
	private SettleParameterInfoManager settleParameterInfoManager;

	@Autowired
	private SettleTransDetailManager settleTransDetailManager;
	
	@Autowired
	private SettleTransProfitManager settleTransProfitManager;

	@Autowired
	private SettleTransBillManager settleTransBillManager;

	@Autowired
	private LogicConstantUtil logicConstantUtil;

	@Autowired
	private TransOrderInfoManager transOrderInfoManager;

	@Autowired
	private TransOrderInfoAccountManager transOrderInfoAccountManager;
	
	@Autowired
	private TradeService tradeService;

	@Autowired
	private RedisIdGenerator redisIdGenerator;

	public void getAccountTransOrderInfos(Date beginDate, Date endDate)
			throws Exception {
		logger.info(">>>> >>>> >>>> >>>> 读取【 账户系统  二期】交易并录入清结算库中 start");
		List<SettleTransDetail> settleTransDetailList = new ArrayList<SettleTransDetail>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat dateFormat1 = new SimpleDateFormat(
				"yyyy-MM-dd hh:mm:ss");
		Date beginAccDate = dateFormat1.parse(dateFormat.format(beginDate)
				+ " 00:00:00");

		Long beginAccTime = Long.valueOf(beginAccDate.getTime() - 1296000000L);
		beginAccDate.setTime(beginAccTime.longValue());
		Date endAccDate = dateFormat1.parse(dateFormat.format(endDate)
				+ " 00:00:00");
		Long endAccTime = Long.valueOf(endAccDate.getTime() + 86400000L);
		endAccDate.setTime(endAccTime.longValue());

		Map<String, Object> query = new HashMap<String, Object>();
		query.put("beginAccDate", beginAccDate);
		query.put("endAccDate", endAccDate);
		query.put("beginDate", beginDate);
		query.put("endDate", endDate);

		List<TransOrderInfo> accountOrderInfoList = this.transOrderInfoManager.queryByRequestTime(query);
		
		if (accountOrderInfoList == null || accountOrderInfoList.size() <= 0) {
			logger.info(">>> >>> 账户取得数据为0条 ");
			return;
		}
		for (TransOrderInfo transOrderInfo : accountOrderInfoList) {
			settleTransDetailList.add(this.transOrderInfo2SettleTransDetail(transOrderInfo));
		}
		settleTransDetailList = this.distinctTransDetail(settleTransDetailList);
		super.batchSaveSettleTransDetail(settleTransDetailList);
		logger.info("<<<< <<<< <<<< <<<< 读取【 账户系统  二期】交易并录入清结算库中 end");
	}

	/***
	 * 存储账户系统交易信息
	 */
	private List<SettleTransDetail> distinctTransDetail(
			List<SettleTransDetail> settleTransDetailList) {
		List<SettleTransDetail> distinctSettleTransDetailList = new ArrayList<SettleTransDetail>();
		for(SettleTransDetail settleTransDetail : settleTransDetailList) {
			logger.info(">>> >>> saveAccountTrans start ");
			logger.info(">>> >>> 交易信息读入... ORDER_NO:"
					+ settleTransDetail.getOrderNo() + "; FUNC_CODE:"
					+ settleTransDetail.getFuncCode() + "; MERCHANT_CODE:"
					+ settleTransDetail.getMerchantCode());

			SettleTransDetailQuery query = new SettleTransDetailQuery();
			query.setOrderNo(settleTransDetail.getOrderNo());
			query.setMerchantCode(settleTransDetail.getMerchantCode());
			query.setFuncCode(settleTransDetail.getFuncCode());
			query.setOrderType(0);
			/*
			 * 判断重复数据
			 */
			if (settleTransDetailManager.countSettleTransDetail(query) > 0) {
				logger.info(">>> >>> 不执行 【重复数据】  funcCode:"
						+ settleTransDetail.getFuncCode() + "; orderNo:"
						+ settleTransDetail.getOrderNo() + ";merchantCode:"
						+ settleTransDetail.getMerchantCode());
			} else {
				distinctSettleTransDetailList.add(settleTransDetail);
				logger.info(">>> >>> 准备执行【插入】操作 funcCode:"
						+ settleTransDetail.getFuncCode() + "; orderNo:"
						+ settleTransDetail.getOrderNo() + ";merchantCode:"
						+ settleTransDetail.getMerchantCode());
			}
		}
		return distinctSettleTransDetailList;
	}

	/**
	 * 将账户交易信息转换为清算交易信息
	 * @param transOrderInfo
	 * @return
	 */
	private SettleTransDetail transOrderInfo2SettleTransDetail(
			TransOrderInfo transOrderInfo) {
		SettleTransDetail settleTransDetail = new SettleTransDetail();
		settleTransDetail.setRequestId(transOrderInfo.getId().intValue());
		settleTransDetail.setRequestNo(transOrderInfo.getRequestNo());
		settleTransDetail.setRequestTime(transOrderInfo.getRequestTime());
		settleTransDetail.setTransFlowNo(transOrderInfo.getTradeFlowNo());
		settleTransDetail.setOrderPackageNo(transOrderInfo.getOrderPackageNo());
		settleTransDetail.setRelateOrderNo(transOrderInfo.getRelateOrderNo());
		settleTransDetail.setOrderNo(transOrderInfo.getOrderNo());
		settleTransDetail.setOrderDate(transOrderInfo.getOrderDate());
		settleTransDetail.setOrderAmount(transOrderInfo.getOrderAmount());
		settleTransDetail.setOrderCount(transOrderInfo.getOrderCount());
		settleTransDetail.setTransType(transOrderInfo.getTransType());
		settleTransDetail.setFuncCode(transOrderInfo.getFuncCode());
		settleTransDetail.setDealProductCode(transOrderInfo
				.getDealProductCode());
		settleTransDetail.setMerchantCode(transOrderInfo.getRootInstCd());
		settleTransDetail.setIntoInstCode(transOrderInfo.getIntoRootInstCd());
		settleTransDetail.setUserId(transOrderInfo.getUserId());
		settleTransDetail.setRoleCode(transOrderInfo.getRoleCode());
		settleTransDetail.setInterMerchantCode(transOrderInfo.getInterUserId());
		settleTransDetail.setInterRoleCode(transOrderInfo.getInterRoleCode());
		settleTransDetail.setReferRoleCode(transOrderInfo.getReferRoleCode());
		settleTransDetail.setReferProductId(transOrderInfo.getReferProductId());
		settleTransDetail.setProductId(transOrderInfo.getProductId());
		settleTransDetail.setProductWid(transOrderInfo.getIntoProductId());
		settleTransDetail.setAmount(transOrderInfo.getAmount());
		settleTransDetail.setOtherAmount(transOrderInfo.getOtherAmount());
		settleTransDetail.setUserFee(transOrderInfo.getUserFee());
		settleTransDetail.setUserFeeWay(transOrderInfo.getUserFeeWay());
		settleTransDetail.setProfit(transOrderInfo.getProfit());
		settleTransDetail.setInterest(transOrderInfo.getInterest());
		settleTransDetail.setBusinessType(transOrderInfo.getBusiTypeId());
		settleTransDetail.setPayChannelId(transOrderInfo.getPayChannelId());
		settleTransDetail.setPayWay(transOrderInfo.getPayWay());
		settleTransDetail.setUserIpAddress(transOrderInfo.getUserIpAddress());
		settleTransDetail.setProcessStatus(transOrderInfo.getProcessStatus());
		settleTransDetail.setOriginalOrderPackageNo(transOrderInfo
				.getOriginalOrderPackageNo());
		settleTransDetail.setOriginalOrderId(transOrderInfo
				.getOriginalOrderId());
		settleTransDetail.setErrorCode(transOrderInfo.getErrorCode());
		settleTransDetail.setErrorMsg(transOrderInfo.getErrorMsg());
		settleTransDetail.setAccountDate(transOrderInfo.getAccountDate());
		settleTransDetail.setCurrency(transOrderInfo.getCurrency());
		settleTransDetail.setReverseFlag(transOrderInfo.getReverseFlag());
		settleTransDetail.setCancelTag(transOrderInfo.getCancelTag());
		settleTransDetail.setAcceptInstCode(transOrderInfo.getAcceptInstCode());
		settleTransDetail.setAcceptInstId(transOrderInfo.getAcceptInstId());
		settleTransDetail.setTargetMerchantCode(transOrderInfo
				.getTargetMerchantCode());
		settleTransDetail.setTargetTerminalCode(transOrderInfo
				.getTargetTerminalCode());
		settleTransDetail.setChannelReturnCode(transOrderInfo
				.getChannelReturnCode());
		settleTransDetail.setReturnMeg(transOrderInfo.getReturnMeg());
		settleTransDetail.setReceiptSerialNo(transOrderInfo
				.getReceiptSerialNo());
		settleTransDetail.setMcc(transOrderInfo.getMcc());
		settleTransDetail.setAuthCode(transOrderInfo.getAuthCode());
		settleTransDetail.setChannelInfo(transOrderInfo.getChannelInfo());
		settleTransDetail.setBankCode(transOrderInfo.getBankCode());
		settleTransDetail.setTradeEsbNo(transOrderInfo.getTradeEsbNo());
		settleTransDetail.setReserve1(transOrderInfo.getReserve1());
		settleTransDetail.setReserve2(transOrderInfo.getReserve2());
		settleTransDetail.setReserve3(transOrderInfo.getReserve3());
		settleTransDetail.setRemark(transOrderInfo.getRemark());
		settleTransDetail.setReadStatusId(transOrderInfo.getStatusId());
		settleTransDetail.setReadCreatedTime(transOrderInfo.getCreatedTime());

		settleTransDetail
				.setProfit(Long.valueOf(transOrderInfo.getProfit() == null ? 0L
						: transOrderInfo.getProfit().longValue()));
		settleTransDetail.setFcAmount(Long.valueOf(transOrderInfo.getAmount()
				.longValue()
				+ (transOrderInfo.getProfit() == null ? 0L : transOrderInfo
						.getProfit().longValue())));
		settleTransDetail.setOrderType(0);
		settleTransDetail.setCancelInd(0);
		settleTransDetail.setDataFrom(4);
		settleTransDetail.setStatusId(transOrderInfo.getStatusId());
		
		//根据账户的DEAL_PRODUCT_CODE映射出清结算的FUNC_CODE
		List<SettleParameterInfo> parameterList = logicConstantUtil.getDealProductCodeTofuncCode();
		
		SettleParameterInfo parameterInfo =null;
		String funcCode = null;
		String dealProductCodes = null;
		//遍历对应关系
		Iterator<SettleParameterInfo> parameterIter = parameterList.iterator();
		while(parameterIter.hasNext()) {
			parameterInfo = parameterIter.next();//当前对应关系记录
			funcCode = parameterInfo.getParameterCode();	//功能编码
			dealProductCodes = parameterInfo.getParameterValue(); //deal_product_code
			if((","+dealProductCodes+",").indexOf(","+settleTransDetail.getDealProductCode()+",") !=-1){
				settleTransDetail.setFuncCode(funcCode);
				break;
			}
		}
		this.updateDflag(settleTransDetail);
		return settleTransDetail;
	}
	
	/**
	 * @Description: 读取账户系统交易并录入清结算库中
	 * @param beginDate    开始时间yyyy-MM-dd HH:mm:ss
	 * @param endDate  结束时间yyyy-MM-dd HH:mm:ss
	 * @author CLF
	 */
	public void getAccountOldTransOrderInfos(Date beginDate, Date endDate) throws Exception {
	    logger.info(">>>> >>>> >>>> >>>> 读取【 账户系统  一期】交易并录入清结算库中 start");
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	    Date beginAccDate = dateFormat1.parse(dateFormat.format(beginDate) + " 00:00:00");
	    /***
	     * 账期的起始时间点,退前至15日前
	     */
	    Long beginAccTime = beginAccDate.getTime() - 15 * 24 * 60 * 60 * 1000; 
	    beginAccDate.setTime(beginAccTime);
	    Date endAccDate = dateFormat1.parse(dateFormat.format(endDate) + " 00:00:00");
	    Long endAccTime = endAccDate.getTime() + 24 * 60 * 60 * 1000;
	    endAccDate.setTime(endAccTime);
        Map<String,Object> query = new HashMap<String, Object>();
        query.put("beginAccDate", beginAccDate);
        query.put("endAccDate", endAccDate);
        query.put("beginDate", beginDate);
        query.put("endDate", endDate);
        //'账户'交易信息列表
        List<TransOrderInfoAccount> accountOrderInfoList = transOrderInfoAccountManager.queryByRequestTime(query);
        
        if (accountOrderInfoList==null || accountOrderInfoList.size() <=0) {
			logger.info(">>> >>> 账户取得数据为0条 ");
			return;
		}
        
        //'清结算'交易信息转换封装入库
        logger.info(">>> >>> >>> 账户交易信息 ==> 清算交易信息 ... ...");
        for(TransOrderInfoAccount transOrderInfo : accountOrderInfoList){
        	logger.info(">>> >>> >>> ORDER_NO:" + transOrderInfo.getOrderNo() + "; FUNC_CODE:" + transOrderInfo.getFuncCode() + "; MERCHANT_CODE:" + transOrderInfo.getMerchantCode());
        	SettleTransDetail settleTransDetail = new SettleTransDetail();
            settleTransDetail.setRequestNo(transOrderInfo.getRequestNo());
            settleTransDetail.setRequestTime(transOrderInfo.getRequestTime());
            settleTransDetail.setTransFlowNo(transOrderInfo.getTradeFlowNo());
            settleTransDetail.setOrderNo(transOrderInfo.getOrderNo());
            settleTransDetail.setOrderPackageNo(transOrderInfo.getOrderPackageNo());
            settleTransDetail.setOrderDate(transOrderInfo.getOrderDate());
            settleTransDetail.setOrderAmount(transOrderInfo.getOrderAmount());
            settleTransDetail.setOrderCount(transOrderInfo.getOrderCount());
            //0:分润交易1：对账交易
            settleTransDetail.setOrderType(0);
            settleTransDetail.setFuncCode(transOrderInfo.getFuncCode());
            settleTransDetail.setUserId(transOrderInfo.getUserId());
            settleTransDetail.setMerchantCode(transOrderInfo.getMerchantCode());
            settleTransDetail.setInterMerchantCode(transOrderInfo.getInterMerchantCode());
            settleTransDetail.setAmount(transOrderInfo.getAmount());
            settleTransDetail.setFeeAmount(transOrderInfo.getFeeAmount());
            settleTransDetail.setBusinessType(transOrderInfo.getBusiTypeId());
            settleTransDetail.setPayChannelId(transOrderInfo.getPayChannelId());
            settleTransDetail.setBankCode(transOrderInfo.getBankCode());
            settleTransDetail.setUserIpAddress(transOrderInfo.getUserIpAddress());
            settleTransDetail.setUserFee(transOrderInfo.getUserFee());
            /*
             *交易后退款时, 账户转账接口需要存储product_id
             *在账户系统bean中, 消费交易的 product_id是存储在error_code中的
             */
            settleTransDetail.setProductId(transOrderInfo.getErrorCode());
            settleTransDetail.setErrorCode(transOrderInfo.getErrorCode());
            settleTransDetail.setErrorMsg(transOrderInfo.getErrorMsg());
            settleTransDetail.setDataFrom(0);
            settleTransDetail.setCancelInd(0);
            //冲正/抹帐标记都是1
            if(transOrderInfo.getStatus().equals(6)) settleTransDetail.setCancelInd(1);
            settleTransDetail.setStatusId(transOrderInfo.getStatus());
            settleTransDetail.setReadStatusId(transOrderInfo.getStatus());//账户系统交易信息状态位
            settleTransDetail.setRemark(transOrderInfo.getRemark());
            settleTransDetail.setAccountDate(transOrderInfo.getAccountDate());
            /*
             *二期兼容一期  存原订单号的字段 有一期的ORDER_PAKAGE_NO 改为 ORIGINAL_ORDER_ID
             */
            settleTransDetail.setOriginalOrderId(transOrderInfo.getOrderPackageNo());
            /*
             * 计算最终的对账金额FC_AMOUNT => final collate amount
             * FC_AMOUNT = AMOUNT + PROFIT
             * */
            settleTransDetail.setProfit(transOrderInfo.getProfit() == null ? 0 : transOrderInfo.getProfit());
    		settleTransDetail.setFcAmount(transOrderInfo.getAmount() + (transOrderInfo.getProfit() == null ? 0 : transOrderInfo.getProfit()));
    		this.updateDflag(settleTransDetail);
    		/***
    		 * M000004 客栈退款 remark = 'qjs_tuikuan' 此时 func_code 存 4015_qjs_tuikuan 不参与对账
    		 */
//    		if("qjs_tuikuan".equals(settleTransDetail.getRemark()) && "4015".equals(settleTransDetail.getFuncCode())) {
//    			settleTransDetail.setFuncCode("4015_qjs_tuikuan");
//    		}
    		
    		//插入交易信息
            tradeService.saveAccountTrans(settleTransDetail);
            logger.info("<<<< <<<< <<<< <<<< 读取【 账户系统  一期】交易并录入清结算库中 end");
        }
	}
	
	/**
	 * 给交易明细表更新代收付业务的标记位DFLAG,如果是代收付业务默认为0
	 * @param settleTransDetail
	 * author:youyu
	 */
	public void updateDflag(SettleTransDetail settleTransDetail){
		if(settleTransDetail ==null){
			logger.info("updateDflag方法的入参settleTransDetail为空！！！！");
			return;
		}
		if (",4013,4014,4014_1,4016,".indexOf("," + settleTransDetail.getFuncCode()+ ",") != -1){
			 settleTransDetail.setDflag(Integer.valueOf(0));
		}
	} 
}