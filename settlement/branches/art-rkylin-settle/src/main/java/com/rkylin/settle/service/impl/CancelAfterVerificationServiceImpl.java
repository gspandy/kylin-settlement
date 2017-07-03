package com.rkylin.settle.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rkylin.settle.manager.SettleProfitKeyManager;
import com.rkylin.settle.pojo.SettleProfitKey;
import com.rkylin.settle.pojo.SettleProfitKeyQuery;
import com.rkylin.settle.service.CancelAfterVerificationService;
import com.rkylin.wheatfield.api.PaymentAccountServiceApi;
import com.rkylin.wheatfield.api.SemiAutomatizationServiceApi;
import com.rkylin.wheatfield.model.CommonResponse;
import com.rkylin.wheatfield.pojo.Balance;
import com.rkylin.wheatfield.pojo.TransOrderInfo;

@Service("cancelAfterVerificationService")
public class CancelAfterVerificationServiceImpl implements CancelAfterVerificationService {
	//日志对象
	protected static Logger logger = LoggerFactory.getLogger(CancelAfterVerificationServiceImpl.class);
	@Autowired
	private SettleProfitKeyManager settleProfitKeyManager;
	@Autowired
	private SemiAutomatizationServiceApi semiAutomatizationServiceApi;
	@Autowired
	private PaymentAccountServiceApi paymentAccountServiceApi;			//'账户系统'余额API
	/**
	 * 核销
	 */
	public void doCancelAfterVerification() {
		logger.info(">>> >>> >>> 开始: 核销  全规则");
		List<SettleProfitKey> list = this.getCAVProfitKey();
		if(list == null || list.size() < 1) {
			logger.error(">>> >>> >>> 获取核销规则getCAVProfitKey list is Null or list.size < 1");
			return;
		}
		Map<String, String> resultMap = null;
		for(SettleProfitKey settleProfitKey : list) {
			resultMap = this.doCancelAfterVerification(settleProfitKey);
			logger.info(">>> >>> "
						+ settleProfitKey.getProfitRuleName()
						+ " 核销执行结果 code:"+ resultMap.get("code") + ", msg:" + resultMap.get("msg"));
		}
		logger.info("<<< <<< <<< 结束: 核销  全规则");
	}
	/**
	 * 核销
	 */
	public Map<String, String> doCancelAfterVerification(SettleProfitKey settleProfitKey) {
		logger.info(">>> >>> >>> 开始: 核销 [" + settleProfitKey.getProfitRuleName() + "]");
		Map<String, String> resultMap = new HashMap<String, String>();
		String userId1 = settleProfitKey.getKeyValue1();
		String productCode1 = settleProfitKey.getKeyValue2();
		String userId2 = settleProfitKey.getKeyValue3();
		String productCode2 = settleProfitKey.getKeyValue4();
		String merchantCode = settleProfitKey.getRootInstCd();
		
		Long amount = null;
		Long amount1 = null;
		Long amount2 = null;
		
		String resultStr = null;
		/*
		 * 查询账户1余额
		 */
		amount1 = this.getBalance(userId1, productCode1, merchantCode);
		if(amount1 == null) {
			resultMap.put("code", "0");
			resultMap.put("msg", "查询账户1余额失败");
			return resultMap;
		}
		/*
		 * 查询账户2余额
		 */
		amount2 = this.getBalance(userId2, productCode2, merchantCode);
		if(amount2 == null) {
			resultMap.put("code", "0");
			resultMap.put("msg", "查询账户2余额失败");
			return resultMap;
		}
		/*
		 * 查询账户取最小值余额
		 */
		amount = Math.min(amount1, amount2);
		logger.info(">>> >>> 核销金额:【" + amount + "】");
		/*
		 * 调账（两个用户减)
		 */
		resultStr = this.accountAdjustmentReduceAll(settleProfitKey, amount);
		if(!"1".equals(resultStr)) {
			resultMap.put("code", "0");
			resultMap.put("msg", "调账（两个用户减) 失败");
			return resultMap;
		}
		resultMap.put("code", "1");
		resultMap.put("msg", "成功");
		logger.info(">>> >>> >>> 结束: 核销 [" + settleProfitKey.getProfitRuleName() + "]");
		return resultMap;
	}
	/**
	 * 获取头寸规则
	 * @return
	 */
	private List<SettleProfitKey> getCAVProfitKey() {
		SettleProfitKeyQuery query = new SettleProfitKeyQuery();
		query.setRemark("核销规则");
		query.setStatusId(1);
		return settleProfitKeyManager.queryList(query);
	}
	/**
	 * 调账（两个用户减）
	 */
//	amount	必须	Long	出账金额
//	userid	必须	String	用户号（转出A用户ID）
//	funccode	必须	String	功能编码 （该字段存交易类型 3003）
//	intermerchantcode	必须	String	(转出B用户ID)
//	merchantcode	必须	String	商户编码/机构号
//	ordercount	必须	int	订单数量
//	orderdate	必须	datetime	订单日期 格式 YYYY-MM-DD HH:MM:SS
//	orderno	必须	String	订单号
//	orderpackageno	可选	String	订单包号
//	remark	可选	String	备注
//	requestno	可选	String	交易请求号
//	requesttime	必须	datetime	交易请求时间 YYYY-MM-DD HH:MM:SS
//	tradeflowno	可选	String	统一交易流水号
//	productId	必须	String	产品号（转出A产品号）
//	productIdd	必须	String	产品号（转出B产品号）
//	useripaddress	必须	String	用户IP地址 格式 127.0.0.1
	private String accountAdjustmentReduceAll(SettleProfitKey settleProfitKey,  Long amount) {
		logger.info(">>> >>> >>> 开始 调账（两个用户减）");
		Date date = new Date();
		CommonResponse commonResponse = null;
		String resultCode = null;
		TransOrderInfo transOrderInfo = new TransOrderInfo();
		transOrderInfo.setAmount(amount);
		transOrderInfo.setUserId(settleProfitKey.getKeyValue1());
		transOrderInfo.setFuncCode(settleProfitKey.getFuncCode());
		transOrderInfo.setInterMerchantCode(settleProfitKey.getKeyValue3());
		transOrderInfo.setMerchantCode(settleProfitKey.getRootInstCd());
		transOrderInfo.setOrderCount(1);
		transOrderInfo.setOrderDate(date);
		transOrderInfo.setOrderNo("CAV" + date.getTime());
		transOrderInfo.setOrderPackageNo(null);
		transOrderInfo.setRemark("清结算【核销】操作");
		transOrderInfo.setRequestNo(null);
		transOrderInfo.setRequestTime(date);
		transOrderInfo.setTradeFlowNo(null);
		transOrderInfo.setProductId(settleProfitKey.getKeyValue2());
		transOrderInfo.setProductIdd(settleProfitKey.getKeyValue4());
		transOrderInfo.setUserIpAddress("127.0.0.1");
		try {
			commonResponse = semiAutomatizationServiceApi.accountAdjustmentReduceAll(transOrderInfo);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 调账（两个用户减）异常["+ settleProfitKey.getProfitRuleName() +"]", e);
			return resultCode;
		}
		if(commonResponse == null) {//失败
			logger.error(">>> >>> >>> 调账（两个用户减）失败["+ settleProfitKey.getProfitRuleName() +"]");
			logger.error(">>> >>> >>> commonResponse is Null");
		} else if(!"1".equals(commonResponse.getCode())) {//失败
			resultCode = commonResponse.getCode();
			logger.error(">>> >>> >>> 调账（两个用户减）失败["+ settleProfitKey.getProfitRuleName() +"]");
			logger.error(">>> >>> >>> commonResponse.code:" + commonResponse.getCode());
			logger.error(">>> >>> >>> commonResponse.msg:" + commonResponse.getMsg());
		} else if("1".equals(commonResponse.getCode())) {//成功
			resultCode = commonResponse.getCode();
			logger.info(">>> >>> >>> 调账（两个用户减）成功["+ settleProfitKey.getProfitRuleName() +"]");
			logger.error(">>> >>> >>> commonResponse.code:" + commonResponse.getCode());
			logger.error(">>> >>> >>> commonResponse.msg:" + commonResponse.getMsg());
		}
		logger.info(">>> >>> >>> 结束 调账（两个用户减）");
		return resultCode;
	}
	/**
	 * 查询账户余额
	 */
	private Long getBalance(String userId, String productId, String constId) {
		logger.info(">>> >>> >>> 开始: 查询账户余额");
		String argMsg = "[USER_ID:"+ userId +""
					+ ", PRODUCT_ID:"+ productId +""
					+ ", CONST_ID:"+ constId + "]";
		logger.info("查询账户余额入参:" + argMsg);
		/*
		 * 实时余额对象
		 */
		Balance balance = null;
		/*
		 * 实时余额金额
		 */
		Long amountBalance = null;
		com.rkylin.wheatfield.pojo.User user = new com.rkylin.wheatfield.pojo.User();
		user.userId = String.valueOf(userId);
		user.productId = String.valueOf(productId);
		user.constId = String.valueOf(constId);
		try {
			balance = paymentAccountServiceApi.getBalance(user, "");
		} catch (Exception e) {
			logger.error("调用账户接口获取余额异常! " + argMsg, e);
		}
		if(balance == null) {
			logger.error("调用账户接口获取余额失败!返回结果为null " + argMsg);
		} else {
			//账户提现余额
			amountBalance = balance.getBalanceSettle();
		}
		logger.info("<<< <<< <<< 结束: 查询账户余额 " + amountBalance);
		return amountBalance;
	}
}