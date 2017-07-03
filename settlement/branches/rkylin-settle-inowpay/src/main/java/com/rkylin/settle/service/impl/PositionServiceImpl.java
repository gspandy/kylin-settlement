package com.rkylin.settle.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rkylin.settle.manager.SettleParameterInfoManager;
import com.rkylin.settle.manager.SettleProfitInvoiceManager;
import com.rkylin.settle.manager.SettleProfitKeyManager;
import com.rkylin.settle.pojo.SettleProfitInvoice;
import com.rkylin.settle.pojo.SettleProfitInvoiceQuery;
import com.rkylin.settle.pojo.SettleProfitKey;
import com.rkylin.settle.pojo.SettleProfitKeyQuery;
import com.rkylin.settle.service.PositionService;
import com.rkylin.settle.util.SettlementUtil;
import com.rkylin.settle.util.StringUtils;
import com.rkylin.wheatfield.api.FinanaceAccountService;
import com.rkylin.wheatfield.bean.FinAccountInfo;
import com.rkylin.wheatfield.bean.FinAccountInfoVo;
import com.rkylin.wheatfield.model.FinAccountInfoResponse;

@Service("positionService")
public class PositionServiceImpl implements PositionService {
	//日志对象
	private static Logger logger = LoggerFactory.getLogger(PositionServiceImpl.class);	
	private static final String POSITION_AMOUNT_ACCOUNT1 = "ACCOUNT1";
	private static final String POSITION_SUCCESS_CODE = "1";
	
	@Autowired
	private SettleProfitInvoiceManager settleProfitInvoiceManager;		//分润待结算表Manager
	@Autowired
	private SettleParameterInfoManager settleParameterInfoManager;		//字典表Manager
	@Autowired
	private FinanaceAccountService finanaceAccountService;				//查询账户信息,期初余额
	@Autowired
	private SettlementUtil settlementUtil;										//清结算工具类
	@Autowired
	private SettleProfitKeyManager settleProfitKeyManager;		//分润待结算表Manager
	
	/**
	 * 生成头寸信息
	 */
	@Override
	public String createPositionInfo() throws Exception {
		return this.createPositionInfo(null, null, null, null);
	}
	
	@Override
	/**
	 * 生成头寸信息
	 */
	public String createPositionInfo(String pstRootInstCd, String pstFuncCode, String pstUserId, String pstProductId) throws Exception {
		logger.info(">>> >>> >>> 开始:生成头寸信息");
		String rootInstCd = null; 				//机构号
		String funcCode = null; 				//交易类型
		String userId = null; 					//用户Id
		String groupManage = null; 				//管理分组productId
		String amountType = null; 				//金额查询方法
		Long amount = null; 					//头寸金额
		String payChannelId = null;				//多渠道ORG_CD
		FinAccountInfo finAccountInfo = null;	//账户一期期初余额接口返回值
		List<SettleProfitKey> settleProfitKeyList = null;
		String args = null;
		
		/*
		 * 查询头寸规则
		 */
		settleProfitKeyList = this.getPositionProfitKey(pstRootInstCd, pstFuncCode, pstUserId, pstProductId);
		if(settleProfitKeyList == null || settleProfitKeyList.size() < 1) {
			logger.info("<<< <<< <<< 结束:生成头寸信息 查询头寸规则 is Null or 0条");
			return "查询头寸规则 0条";
		}
		/*
		 * 生成头寸信息
		 */
		for(SettleProfitKey settleProfitKey : settleProfitKeyList) {
			/*
			 * 初始化数据
			 */
			rootInstCd = null;
			funcCode = null;
			userId = null;
			groupManage = null;
			amount = null;
			payChannelId = null;
			args = null;
			/*
			 * 机构号和交易码
			 */
			rootInstCd = settleProfitKey.getRootInstCd();
			funcCode = settleProfitKey.getFuncCode();
			/*
			 * 获取账户信息
			 */
			userId = settleProfitKey.getKeyValue1();
			groupManage = settleProfitKey.getKeyValue2();
			/*
			 * 金额的获取类型
			 */
			amountType = settleProfitKey.getKeyValue3();
			/*
			 * 获取账户pay_channel_id配置的org_cd
			 */
			payChannelId = settleProfitKey.getKeyValue4();
			/*
			 * 查询头寸金额
			 */
			args = rootInstCd + "_" + funcCode + "_" + groupManage + "_" + userId;
			if(POSITION_AMOUNT_ACCOUNT1.equals(amountType)) {
				finAccountInfo = this.getFinAccountsInfo(rootInstCd, groupManage, userId);
				if(finAccountInfo == null) {
					logger.error(">>> >>> >>> 失败:查询头寸金额["+ args +"] finAccountInfo is Null");
				} else {
					amount = finAccountInfo.getBalanceUseble();
					logger.info(">>> >>> >>> 成功:查询头寸金额["+ args +"] amount = " + amount);
				}
			} else {
				logger.error(">>> >>> >>> 失败:查询头寸金额["+ args +"] 未定义  amountType:" + amountType);
			}
			/*
			 * 生成头寸信息
			 */
			try {
				this.createSettleProfitInvoice(rootInstCd, funcCode, userId, groupManage, amount, payChannelId);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(">>> >>> >>> 异常:生成头寸信息 ["+ args +"]", e);
			}
			logger.info(">>> >>> >>> 成功:生成头寸信息["+ args +"]");
		}
		/*
		 * 插入待结算表SETTLE_PROFIT_INVOICE
		 */
		logger.info("<<< <<< <<< 结束:生成头寸信息");
		return POSITION_SUCCESS_CODE;
	}
	/**
	 * 生成头寸交易信息
	 * @param rootInstCd
	 * @param funcCode
	 * @param userId
	 * @param groupManage
	 * @param amount
	 * @param payChannelId
	 */
	private void createSettleProfitInvoice(String rootInstCd, String funcCode, String userId, String groupManage, Long amount, String payChannelId) throws Exception {
		logger.info(">>> >>> >>> 开始:生成头寸交易信息");
		/*
		 * 获取T-1日账期
		 */
		Date accountDate = null;
		try {
			accountDate = (Date) settlementUtil.getAccountDate("yyyy-MM-dd", -1, "Date");
	    } catch (Exception e) {
	    	logger.error("获取T-1日账期 异常:生成头寸交易信息", e);
	    	throw e;
	    }
		/*
		 * 查询此头寸交易是否重复
		 */
		Integer count = null;
		SettleProfitInvoiceQuery query = new SettleProfitInvoiceQuery();
		query.setMerchantCode(rootInstCd);
		query.setFuncCode(funcCode);
		query.setUserId(userId);
		query.setProductId(groupManage);
		query.setPayChannelId(payChannelId);
		query.setOrderDate(accountDate);
		count = settleProfitInvoiceManager.countByExample(query);
		if(count > 0) {
			logger.info("<<< <<< <<< 结束:生成头寸交易信息 已存在 {"
					+ "rootInstCd:"+rootInstCd
					+ ",funcCode:"+funcCode
					+ ",userId:"+userId
					+ ",groupManage:"+groupManage
					+ ",payChannelId:"+payChannelId+"}");
			return;
		}
		SettleProfitInvoice settleProfitInvoice = new SettleProfitInvoice();
		/*
		 * 生成订单号
		 */
		SimpleDateFormat formatDate = new SimpleDateFormat("yyyyMMddhhmmssSSS");
		String orderNo = "PT" + formatDate.format(new Date());
		/*
		 * 传入参数
		 */
		settleProfitInvoice.setMerchantCode(rootInstCd);
		settleProfitInvoice.setFuncCode(funcCode);
		settleProfitInvoice.setUserId(userId);
		settleProfitInvoice.setInterMerchantCode(userId);
		settleProfitInvoice.setProductId(groupManage);
		settleProfitInvoice.setAmount(amount);
		settleProfitInvoice.setOrderAmount(amount);
		settleProfitInvoice.setPayChannelId(payChannelId);
		/*
		 * 约定参数
		 */
		settleProfitInvoice.setUserFee(0l);
    	settleProfitInvoice.setUserIpAddress("127.0.0.1");
    	settleProfitInvoice.setReturnMsg("尚未头寸");
		settleProfitInvoice.setRequestTime(new Date());
		settleProfitInvoice.setOrderNo(orderNo);
		settleProfitInvoice.setOrderCount(1);
		settleProfitInvoice.setStatus(0);
		settleProfitInvoice.setCashStatus(1);
		settleProfitInvoice.setSettleType("T-1");
		settleProfitInvoice.setOrderDate(accountDate);
		settleProfitInvoiceManager.saveSettleProfitInvoice(settleProfitInvoice);
		logger.info("<<< <<< <<< 开始:生成头寸交易信息");
	}
	/**
	 * 查询账户期初余额
	 * @param rootInstCd
	 * @param groupManage
	 * @param userId
	 * @return
	 */
	private FinAccountInfo getFinAccountsInfo(String rootInstCd, String groupManage, String userId) throws Exception {
		List<FinAccountInfo> finAccountInfoList = null;
		List<String> userIdList = null;
		/*
		 * userId校验
		 */
		if(StringUtils.isEmpty(userId)) throw new Exception(">>> >>> >>> 失败:查询账户期初余额 userId is Empty");
		/*
		 * 封装入参
		 */
		userIdList = new ArrayList<String>();
		userIdList.add(userId);
		/*
		 * 查询
		 */
		finAccountInfoList = this.getFinAccountsInfo(rootInstCd, groupManage, userIdList);
		if(finAccountInfoList == null || finAccountInfoList.size() < 1) {
			throw new Exception(">>> >>> >>> 失败:查询账户期初余额 finAccountInfoList is Null or finAccountInfoList.size < 1");
		} else {
			return finAccountInfoList.get(0);
		}	
	}
	/**
	 * 查询账户期初余额
	 * @param rootInstCd
	 * @param groupManage
	 * @param List
	 * @return
	 */
	private List<FinAccountInfo> getFinAccountsInfo(String rootInstCd, String groupManage, List<String> userIdList) throws Exception {
		logger.info(">>> >>> >>> 查询账户期初余额");
		FinAccountInfoResponse finAccountInfoResponse = null;
		String code = null;
		String msg = null;
		List<FinAccountInfo> finAccountInfoList = null;
		FinAccountInfoVo finAccountInfoVo = null;
		/*
		 * 入参校验
		 */
		if(StringUtils.isEmpty(rootInstCd)) throw new Exception("rootInstCd is Empty Exception");
		if(StringUtils.isEmpty(groupManage)) throw new Exception("groupManage is Empty Exception");
		if(userIdList == null || userIdList.size() < 1) throw new Exception("userIdList is Null or userIdList.size < 1");
		logger.info(">>> >>> >>> 入参:rootInstCd:" + rootInstCd + ", groupManage:" + groupManage + ", userIdList.size:" + userIdList.size());
		/*
		 * 封装入参
		 */
		finAccountInfoVo = new FinAccountInfoVo();
		finAccountInfoVo.setRootInstCd(rootInstCd);
		finAccountInfoVo.setGroupManage(groupManage);
		finAccountInfoVo.setUserIdList(userIdList);
		/*
		 * 调用账户一期查询期初余额接口
		 */
		try {
			finAccountInfoResponse = finanaceAccountService.getFinAccountsInfoByCondition(finAccountInfoVo);
			code = finAccountInfoResponse.getCode();
			msg = finAccountInfoResponse.getMsg();
			if("1".equals(code)) {
				logger.info(">>> >>> 成功:调用账户一期查询期初余额接口 code:"+ code +", msg:" + msg);
				finAccountInfoList = finAccountInfoResponse.getFinAccountInfoList();
			} else  {
				logger.info(">>> >>> 失败:调用账户一期查询期初余额接口 code:"+ code +", msg:" + msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(">>> >>> >>> 异常:查询账户期初余额", e);
		}
		logger.info("<<< <<< <<< 查询账户期初余额");
		return finAccountInfoList;
	}
	/**
	 * 获取头寸规则
	 * @return
	 */
	private List<SettleProfitKey> getPositionProfitKey(String rootInstCd, String funcCode, String userId, String productId) {
		SettleProfitKeyQuery query = new SettleProfitKeyQuery();
		query.setRootInstCd(rootInstCd);
		query.setFuncCode(funcCode);
		query.setKeyValue1(userId);
		query.setKeyValue2(productId);
		query.setRemark("头寸规则");
		query.setStatusId(1);
		return settleProfitKeyManager.queryList(query);
	}
}
