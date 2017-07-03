package com.rkylin.settle.logic;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rkylin.common.RedisIdGenerator;
import com.rkylin.settle.constant.SettleConstants;
import com.rkylin.settle.exception.SettleException;
import com.rkylin.settle.manager.SettleParameterInfoManager;
import com.rkylin.settle.manager.SettleTransBillManager;
import com.rkylin.settle.manager.SettleTransDetailManager;
import com.rkylin.settle.manager.SettleTransProfitManager;
import com.rkylin.settle.manager.TransOrderInfoManager;
import com.rkylin.settle.pojo.SettleBalanceEntry;
import com.rkylin.settle.pojo.SettleParameterInfo;
import com.rkylin.settle.pojo.SettleParameterInfoQuery;
import com.rkylin.settle.pojo.SettleProfitKey;
import com.rkylin.settle.pojo.SettleProfitRule;
import com.rkylin.settle.pojo.SettleTransBill;
import com.rkylin.settle.pojo.SettleTransBillQuery;
import com.rkylin.settle.pojo.SettleTransDetail;
import com.rkylin.settle.pojo.SettleTransProfit;
import com.rkylin.settle.pojo.SettleTransProfitQuery;
import com.rkylin.settle.service.TradeService;
import com.rkylin.settle.util.LogicConstantUtil;
import com.rkylin.wheatfield.pojo.FinanaceEntry;
import com.rongcapital.mtaegis.po.BalanceInfo;
import com.rongcapital.mtaegis.po.TransOrderInfo;
import com.rongcapital.mtaegis.po.TransOrderQueryInfo;
import com.rongcapital.mtaegis.response.CommonResponse;
import com.rongcapital.mtaegis.response.QueryBalanceResponse;
import com.rongcapital.mtaegis.service.BalanceQueryApi;
import com.rongcapital.mtaegis.service.TransactionCommonApi;

/***
 * 分润逻辑
 * @author Yang
 *
 */
@Component("profitLogic")
public class ProfitLogic extends BasicLogic {
	@Autowired
	private SettleParameterInfoManager settleParameterInfoManager;		//'清算'属性表Manager
	@Autowired
	private SettleTransDetailManager settleTransDetailManager;			//'清算'交易信息Manager
	@Autowired
	private SettleTransProfitManager settleTransProfitManager;			//'分润结果信息'Manager
	@Autowired
	private SettleTransBillManager settleTransBillManager;				//'挂账信息'Manager
	@Autowired
	private LogicConstantUtil logicConstantUtil;						//逻辑常量工具类
//	@Autowired
//    private PaymentInternalOutService paymentInternalOutService;        //'账户系统'分润API
//	@Autowired
//	private PaymentAccountServiceApi paymentAccountServiceApi;			//'账户系统'余额API
	@Autowired
	private TransOrderInfoManager transOrderInfoManager;				//'账户'交易记录信息Manager
	@Autowired
	private TradeService tradeService;									//'清结算'交易信息Service
	@Autowired
	private RedisIdGenerator redisIdGenerator;							//生成批次号的工具类
	@Autowired
	private TransactionCommonApi transactionCommonApi;					//mtaegis 分润接口
	@Autowired
	private BalanceQueryApi balanceQueryApi;							//mtaegis 余额查询接口
	/***
	 * 分润
	 * @param profitTransDetailList	所有未清分交易信息
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> doProfit(List<Map<String, Object>> profitTransDetailList) throws Exception {
		logger.info(">>> >>> >>> >>> 进入   分润   操作");
		//提示信息
		Map<String, Object> resultMap = new HashMap<String, Object>();
		//需分润的交易类型
		List<String> profitTypeByKeyList = null;
		//分润规则List
		List<SettleProfitKey> profitKeyList = null;
		//分润结果List
		List<SettleTransProfit> transProfitList = new ArrayList<SettleTransProfit>();
		//不需分润的交易信息IDList
		List<Long> unProTrDetIdList = new ArrayList<Long>();
		//分润'成功'的交易信息IDList
		List<Long> successProTrDetIdList = new ArrayList<Long>();
		//分润'失败'的交易信息IDList
		List<Long> failProTrDetIdList = new ArrayList<Long>();
		//待更新交易信息ID Map<String, List>
		Map<String, List<Long>> updateTrDetIdMap = new HashMap<String, List<Long>>();
		//交易结果表
		List<SettleBalanceEntry> settleBalanceEntryList = new ArrayList<SettleBalanceEntry>();
		//提示信息
		String message = "";
		try {
			//获取需分润的交易类型
			profitTypeByKeyList = this.getProfitTypeByKey(SettleConstants.PROFIT_FUNC_CODES);
			if(profitTypeByKeyList == null || profitTypeByKeyList.size() < 1) {
				message = "获取0条 需分润的交易类型";
				logger.info(">>>　" + message);
				return this.editResultMap(resultMap, "0", message);
			}
		} catch (Exception e) {
			message = "异常:获取需分润的交易类型";
			logger.error(">>> " + message);
			e.printStackTrace();
			return this.editResultMap(resultMap, "-1", message);
		}
		try {
			//获取全表分润规则
			profitKeyList = this.getAllProfitKey();
			if(profitKeyList == null || profitKeyList.size() < 1) {
				message = "获取0条全表分润规则";
				logger.info(">>> " + message);
				return this.editResultMap(resultMap, "0", message);
			}
		} catch (Exception e) {
			message = "异常:全表分润规则";
			logger.error(">>> " + message);
			e.printStackTrace();
			return this.editResultMap(resultMap, "-1", message);
		}
		/**
		 * 遍历交易信息,匹配相应分润规则并进行分润
		 */
		logger.info(">>> >>> >>> 分润开始 ... ...");
		Iterator<Map<String, Object>> detailIter = profitTransDetailList.iterator();
		detailIter: while(detailIter.hasNext()) {
			//交易结果对象
			SettleBalanceEntry settleBalanceEntry = new SettleBalanceEntry();
			//交易信息Map
			Map<String, Object> transDetailMap = detailIter.next();
			/**
			 * 待插入: 分润结果的'交易信息数据'
			 */
			Long transDetailId = Long.parseLong(String.valueOf(transDetailMap.get("TRANS_DETAIL_ID")));	//交易信息ID
			String orderNo = String.valueOf(transDetailMap.get("ORDER_NO"));				//订单号	
			String rootInstCd = String.valueOf(transDetailMap.get("MERCHANT_CODE"));		//机构号
			Date account = (Date) transDetailMap.get("ACCOUNT_DATE");						//账期
			Integer dataFrom = Integer.parseInt(String.valueOf(transDetailMap.get("DATA_FROM")));//数据源
			String roleCode = String.valueOf(transDetailMap.get("ROLE_CODE"));//转出方角色编号
			String userIpAddress = String.valueOf(transDetailMap.get("USER_IP_ADDRESS"));//消费者IP地址
			
			//初始化'交易结果'对象信息
			settleBalanceEntry.setOrderNo(orderNo);
			//交易结果状态位为3表示'分润交易结果'
			settleBalanceEntry.setBalanceType(50);
			//订单号+交易信息ID
			settleBalanceEntry.setObligate1(orderNo+","+transDetailId);
			settleBalanceEntry.setAccountDate(account);
			//判断是否是需要分润的交易类型
			if(!profitTypeByKeyList.contains(String.valueOf(transDetailMap.get("FUNC_CODE")))) {
				message = "不参加分润: 此条交易[ID:"+transDetailId+", ORDER_NO:"+orderNo+"], 信息无需分润";
				logger.info(">>> " + message);
				//无需分润直接更新状态的ID
				unProTrDetIdList.add(transDetailId);
				//初始化交易结果表信息
				continue detailIter;
			}
			//分润规则信息对象 - 获取到唯一的分润信息
			SettleProfitKey profitKey = null;
			//分润规则详细信息对象
			List<SettleProfitRule> profitRuleList = null;
			try {
				//匹配唯一分润规则
				profitKey = this.getUniqueProfitKey4TransDetail(transDetailMap, profitKeyList);
				if(profitKey == null) {
					message = "分润失败: 未查询到此条交易 [ID:"+transDetailId+", ORDER_NO:"+orderNo+"] 相应的到分润规则!";
					logger.info(">>> " + message);
					failProTrDetIdList.add(transDetailId);
					this.editResultMap(resultMap, "0", message);
					//初始化交易结果表信息
					settleBalanceEntry.setStatusId(SettleConstants.STATUS_PROFIT_FALID);
					settleBalanceEntry.setRemark(message);
					settleBalanceEntryList.add(settleBalanceEntry);
					continue detailIter;
				}
			} catch (Exception e) {
				message = "异常:查询到此条交易 [ID:"+transDetailId+", ORDER_NO:"+orderNo+"] 分润规则异常!";
				logger.error(">>> " + message);
				e.printStackTrace();
				failProTrDetIdList.add(transDetailId);
				this.editResultMap(resultMap, "-1", message);
				//初始化交易结果表信息
				settleBalanceEntry.setStatusId(SettleConstants.STATUS_PROFIT_FALID);
				settleBalanceEntry.setRemark(message);
				settleBalanceEntryList.add(settleBalanceEntry);
				continue detailIter;
			}
			try {
				//获取分润规则
				profitRuleList = profitKey.getSettleProfitRuleList();
				if(profitRuleList == null || profitRuleList.size() < 1) {
					message = "未查询到此条交易 [ID:"+transDetailId+", ORDER_NO:"+orderNo+"] 相应的到分润规则明细!";
					logger.info(">>> " + message);
					failProTrDetIdList.add(transDetailId);
					this.editResultMap(resultMap, "0", message);
					//初始化交易结果表信息
					settleBalanceEntry.setStatusId(SettleConstants.STATUS_PROFIT_FALID);
					settleBalanceEntry.setRemark(message);
					settleBalanceEntryList.add(settleBalanceEntry);
					continue detailIter;
				}
			} catch (Exception e) {
				message = "异常:查询到此条交易 [ID:"+transDetailId+", ORDER_NO:"+orderNo+"] 分润规则明细异常!";
				logger.error(">>> " + message);
				e.printStackTrace();
				failProTrDetIdList.add(transDetailId);
				this.editResultMap(resultMap, "-1", message);
				//初始化交易结果表信息
				settleBalanceEntry.setStatusId(SettleConstants.STATUS_PROFIT_FALID);
				settleBalanceEntry.setRemark(message);
				settleBalanceEntryList.add(settleBalanceEntry);
				continue detailIter;
			}
			/**
			 * 待处理 : 分润规则将要处理的交易信息
			 */
			String amountStr = String.valueOf(transDetailMap.get("AMOUNT"));				//待金额String
			BigDecimal amount = new BigDecimal(amountStr);									//分润金额
			/**
			 *遍历'分润详细规则'进行分润操作和分润结果更新
			 */
			Iterator<SettleProfitRule> ruleIter = profitRuleList.iterator();
			ruleIter: while(ruleIter.hasNext()) {
				//对账规则明细对象
				SettleProfitRule rule = ruleIter.next();
				//分润结果信息对象
				SettleTransProfit transProfit = new SettleTransProfit();
				//分润对象形态1固定,2可变
				Integer profitObject = rule.getProfitObject();
				//分润模式:0为不启用1为按比例计费2为固定金额
				Integer profitMd = rule.getProfitMd();
				//分润比例 or 分润金额String
				String profitFeeStr = rule.getProfitFee();
				//分润比例 or 分润金额
				BigDecimal profitFee = null;
				//分润后的金额 初始化0
				BigDecimal profitAmount = new BigDecimal("0");
				//产品ID
				String productId = rule.getProductId();
				//实际用户ID
				String userId = null;
				//选择 分润对象形态1固定,2可变
				switch(profitObject) {
					case 1 ://1固定
						userId = rule.getUserId();
						break;
					case 2 ://2可变
						userId = String.valueOf(transDetailMap.get(rule.getUserId()));
						break;
					default :
						message = "异常: 此条交易 [ID:"+transDetailId+", ORDER_NO:"+orderNo+"]的 分润规则明细  分润对象形态 数据有误 数据必须在[1,2]之中";
						logger.error(">>> " + message);
						this.editResultMap(resultMap, "-1", message);
						//初始化交易结果表信息
						settleBalanceEntry.setStatusId(SettleConstants.STATUS_PROFIT_FALID);
						settleBalanceEntry.setRemark(message);
						settleBalanceEntryList.add(settleBalanceEntry);
						continue detailIter;
				}
				//选择 分润模式:0为不启用1为按比例计费2为固定金额3算式分配
				switch(profitMd) {
					case 0 :
						logger.info(">>> 此分润规则明细[ID:"+rule.getProductId()+","+rule.getSubId()+"] 未启用!");
						continue ruleIter;
					case 1 :
						profitFee = new BigDecimal(profitFeeStr);
						profitAmount = amount.multiply(profitFee);//金额 * 百分比
						profitAmount = profitAmount.setScale(0, BigDecimal.ROUND_HALF_UP);//取整 - 四舍五入
						break;
					case 2 :
						profitFee = new BigDecimal(profitFeeStr);
						profitAmount = profitAmount.add(profitFee);
						break;
					case 3 :
						/*
						 * 采用减法算式进行分润金额的计算
						 * 例如:1-0.7-0.2的含义是:应分金额 = 交易金额 * 1 - 交易金额 * 0.7 - 交易金额 * 0.2
						 */
						//解析算式
						String[] profitFeeStrArr = profitFeeStr.split("-");
						//判断算式格式是否正确
						Integer len = profitFeeStrArr.length;
						if(len > 1) {//正确
							profitAmount = amount;
							for(int i = 1; i < len; i ++) {
								String amountItemStr = profitFeeStrArr[i];
								BigDecimal amountItem = new BigDecimal(amountItemStr);
								amountItem = amount.multiply(amountItem);
								profitAmount = profitAmount.subtract(amountItem);
							}
						} else {//错误
							message = "此条交易 [ID:"+transDetailId+", ORDER_NO:"+orderNo+"]的 分润规则明细  分润模式为采用减法算式进行分润金额的计算 算式'解析'失败!";
							logger.error(">>> " + message);
							failProTrDetIdList.add(transDetailId);
							this.editResultMap(resultMap, "-1", message);
							//初始化交易结果表信息
							settleBalanceEntry.setStatusId(SettleConstants.STATUS_PROFIT_FALID);
							settleBalanceEntry.setRemark(message);
							settleBalanceEntryList.add(settleBalanceEntry);
							continue detailIter;
						}
						break;
					default : 
						message = "此条交易 [ID:"+transDetailId+", ORDER_NO:"+orderNo+"]的 分润规则明细  分润模式  数据有误 数据必须在[0,1,2]之中";
						logger.error(">>> " + message);
						failProTrDetIdList.add(transDetailId);
						this.editResultMap(resultMap, "-1", message);
						//初始化交易结果表信息
						settleBalanceEntry.setStatusId(SettleConstants.STATUS_PROFIT_FALID);
						settleBalanceEntry.setRemark(message);
						settleBalanceEntryList.add(settleBalanceEntry);
						continue detailIter;
				}
				//比较金额的'封顶值' & '封底值'
				Long longProfitAmount = profitAmount.longValue();						//金额
				Long feilvUp = rule.getFeilvUp();
				Long feilvBelow = rule.getFeilvBelow();
				//金额应 < 封顶值
				if(feilvUp != null && feilvUp != 0) {
					longProfitAmount = Math.min(longProfitAmount, feilvUp);
				}
				//金额应 > 封底值
				if(feilvBelow != null && feilvBelow != 0) {
					longProfitAmount = Math.max(longProfitAmount, feilvBelow);
				}
				/**
				 * 待插入 分润结果的'分润规则信息数据'
				 */
				Integer profitType = rule.getProfitType();								//清分类型				
				Integer isMust = rule.getIsMust();										//0非必须,1..n必须(优先级)
				Integer statusId = 0;													//状态,0未进行结算处理,1已进行结算处理,2不处理
				String remark = "已经执行清分, 未进行结算处理";									//备注信息 待编辑
				if (profitType == 2) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put(userId, longProfitAmount);
					map.put("transDetailId", transDetailId);
					settleTransDetailManager.updateTransAmount(map);
				} else {// 1:分润情况
					//初始化'分润结果信息'对象
					transProfit.setOrderNo(orderNo);
					transProfit.setUserId(userId);
					transProfit.setRootInstCd(rootInstCd);
					transProfit.setProductId(productId);
					transProfit.setProfitType(profitType);
					transProfit.setProfitAmount(longProfitAmount);
					transProfit.setIsMust(isMust);
					transProfit.setRoleCode(roleCode);
					transProfit.setUserIpAddress(userIpAddress);
					transProfit.setStatusId(statusId);
					transProfit.setRemark(remark);
					transProfit.setAccountDate(account);
					transProfit.setObligate1(String.valueOf(transDetailId));
					//存入对账结果List
					transProfitList.add(transProfit);
				}
			}
			message = "成功: 此条交易 [ID:"+transDetailId+", ORDER_NO:"+orderNo+"] 分润成功 !";
			logger.info(">>> " + message);
			//存入分润'成功'的交易信息IDList
			successProTrDetIdList.add(transDetailId);
			//初始化交易结果表信息
			settleBalanceEntry.setStatusId(SettleConstants.STATUS_PROFIT_SUCCESS);
			settleBalanceEntry.setRemark(message);
			settleBalanceEntryList.add(settleBalanceEntry);
		}
		/**
		 * 待更新的交易信息IDList,如果有数据就存入Map 
		 */
		if(unProTrDetIdList.size() > 0) updateTrDetIdMap.put("unProTrDetIdList", unProTrDetIdList);
		if(successProTrDetIdList.size() > 0) updateTrDetIdMap.put("successProTrDetIdList", successProTrDetIdList);
		if(failProTrDetIdList.size() > 0) updateTrDetIdMap.put("failProTrDetIdList", failProTrDetIdList);
		/**
		 * 更新DB中分润相关 '交易信息' SETTLE_TRANS_DETAIL
		 */
		this.updateTransDetailProfitStatus(updateTrDetIdMap);
		/**
		 * 更新DB中分润相关 '分润结果' SETTLE_TRANS_PROFIT
		 */
		this.insertAndUpdateSettleTransProfit(transProfitList);
		/**
		 * 更新DB中分润相关 '交易结果' SETTLE_BALANCE_ENTRY
		 */
		super.insertAndUpdateSettleBalanceEntry(settleBalanceEntryList);
		if(resultMap.size() < 1) {
			message = "本次分润全都成功!";
			logger.info(">>> >>> >>> " + message);
			this.editResultMap(resultMap, "1", message);
		} else {
			message = "本次分润完成,但没有全都成功!失败交易请查看交易结果表!";
			logger.info(">>> >>> >>> " + message);
			this.editResultMap(resultMap, "0", message);
		}
		return resultMap;
	}
	
	/***
	 * 结算分润结果 [调用账户系统接口]
	 * @return 提示信息
	 */
	public Map<String, Object> doProfigBalance() throws Exception {
		List<SettleTransProfit> settleTransProfitList = null;
		return this.doProfigBalance(settleTransProfitList);
	}
	/***
	 * 结算分润结果 [调用账户系统接口]
	 * @return 提示信息
	 */
	public Map<String, Object> doProfigBalance(String[] ids) throws Exception {
		List<SettleTransProfit> settleTransProfitList = this.getTransProfitByDetailIds(ids);
		return this.doProfigBalance(settleTransProfitList);
	}
	/***
	 * 结算分润结果 [调用账户系统接口]
	 * @return 提示信息
	 */
	public Map<String, Object> doProfigBalance(List<SettleTransProfit> settleTransProfitList) throws Exception {
		logger.info(">>> >>> >>> >>> 进入结算[分润结果]");
		//提示信息
		Map<String, Object> resultMap = new HashMap<String, Object>();
		//挂账[交易后退款]交易List
		List<SettleTransBill> billList = null;
		//分润结果结算成功的IdList
		List<Integer> idList = new ArrayList<Integer>();
		//分润结果结算成功的IdList
		List<Integer> failIdList = new ArrayList<Integer>();
		//机构和企业主账户对应关系
		Map<String, String> enterpriseAccountUserIdMap = null;
		//mtaegis 交易结构体
		TransOrderInfo transOrderInfo = null;
		//mtaegis 返回值接口结构体
		CommonResponse commonResponse = null;
		/*
		 * 获取分润结果
		 */
		try {
			if(settleTransProfitList == null) {
				settleTransProfitList = this.getTransProfitWithUnbalance();
			}
			if(settleTransProfitList.size() < 1) {
				String msg = "获取分润结果信息 0条";
				logger.error(">>> " + msg);
				this.editResultMap(resultMap, "0", msg);
				return resultMap;
			}
		} catch (Exception e) {
			String msg = "获取分润结果信息, 异常!";
			logger.error(">>> " + msg);
			this.editResultMap(resultMap, "-1", msg);
			e.printStackTrace();
			return resultMap;
		}
		/*
		 * 获取所有未处理挂账单
		 */
		try {
			billList = getTransBillByUntreated();
			if(billList == null || billList.size() < 1) {
				logger.info(">>> >>> >>> 获取所有未处理挂账单 0 条!");
			}
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 获取所有未处理挂账单");
			e.printStackTrace();
		}
		/*
		 * 获取所有机构号对应的企业主账户USER_ID
		 */
		try {
			enterpriseAccountUserIdMap = this.getEnterpriseAccountUserId();
			if(enterpriseAccountUserIdMap == null || enterpriseAccountUserIdMap.size() < 1) {
				logger.info(">>> >>> >>>  获取所有机构号对应的企业主账户USER_ID 0 条!");
			}
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 获取所有机构号对应的企业主账户USER_ID");
			e.printStackTrace();
		}
		/*
		 * 遍历分润结果信息
		 */
		Iterator<SettleTransProfit> iter = settleTransProfitList.iterator();
		while(iter.hasNext()) {
			//当前分润结果信息
			SettleTransProfit item = iter.next();
			//分润金额
			Long paymentamount = item.getProfitAmount();
			//用户ID
			String userId = item.getUserId();
			//机构编码
			String merchantId = item.getRootInstCd();
			//产品号
			String productId = item.getProductId();
			//角色编码
			String roleCode = item.getRoleCode();
			//用户IP地址
			String userIpAddress = item.getUserIpAddress();
			//交易产品号
			String dealProdCode = SettleConstants.PROFIT_BALANCE_DPC;
			//记账凭证号 保证账户系统此字段唯一性故加清算前缀'S'
			String referId = "S" + (new Date()).getTime();
			//String referUserId = null;
			FinanaceEntry finanaceEntry = new FinanaceEntry();
			finanaceEntry.setPaymentAmount(paymentamount);
			finanaceEntry.setReferId(referId);
			
			//调用账号dubbo接口
			try {
				//调用账户分润接口
				//String resultMessage = paymentInternalOutService.shareBenefit(finanaceEntry, userId, merchantId, productId, referUserId);
				transOrderInfo = new TransOrderInfo();
				transOrderInfo.setUserId(userId);
//				transOrderInfo.setProductId(productId);
				transOrderInfo.setRootInstCd(merchantId);
				transOrderInfo.setOrderAmount(paymentamount);
				transOrderInfo.setOrderCount(1);
				transOrderInfo.setOrderDate(new Date());
				transOrderInfo.setOrderNo(referId);
				transOrderInfo.setRoleCode(roleCode);
				transOrderInfo.setUserIpAddress(userIpAddress);
				transOrderInfo.setDealProductCode(dealProdCode);
				
				commonResponse = transactionCommonApi.execute(transOrderInfo);
				String code = commonResponse.getCode();
				String msg = commonResponse.getMsg();
				logger.info("调用[mtaegis分润接口]返回值: {code:"+ code +", msg:"+ msg +"}");
				boolean isSuccess = "1".equals(code);
				String message = "";
				//判断结算是否成功
				if(isSuccess) {
					message = ">>> 成功: 订单信息[USER_ID:"+userId+", ID:"+referId+", ORDER_NO:"+item.getOrderNo()+"] 分润结算成功!";
					logger.info(message);
					//将结算成功的分润结果存入待更新IdList
					idList.add(item.getTransProfitId());
				} else {//失败
					message = "返回失败: 订单信息[USER_ID:"+userId+", ID:"+referId+", ORDER_NO:"+item.getOrderNo()+"] 结算'账户系统'失败!; ";
					message = resultMap.get("msg") == null ? message : resultMap.get("msg") + message;
					logger.error(">>> " + message);
					this.editResultMap(resultMap, "-1", message);
					failIdList.add(item.getTransProfitId());
				}
				/*
				 * 交易后退款 处理
				 */
				if(billList != null && billList.size() > 0) this.doBillBalance(item, billList, enterpriseAccountUserIdMap);
			} catch (Exception e) {
				String msg = "异常:　订单信息[USER_ID:"+userId+", ID:"+referId+", ORDER_NO:"+item.getOrderNo()+"] 结算'账户系统'失败!; ";
				msg = resultMap.get("msg") == null ? msg : resultMap.get("msg") + msg;
				logger.error(">>> " + msg);
				this.editResultMap(resultMap, "-1", msg);
				e.printStackTrace();
			}
		}
		if(idList.size() > 0) this.updateTransProfitStatus(idList);
		if(failIdList.size() > 0) this.updateFailTransProfitStatus(failIdList);
		if(resultMap.size() < 1) this.editResultMap(resultMap, "1", "调用'账户系统'结算全都成功!!");
		return resultMap;
	}
	/***
	 * 结算[消费后退款]挂账金额
	 * @param userId 转账用户ID
	 */
	private void doBillBalance(SettleTransProfit profit, List<SettleTransBill> billList, Map<String, String> enterpriseAccountUserIdMap) throws SettleException {
		logger.info(">>> >>> >>> >>> 进入 结算[消费后退款]挂账金额");
		//产生此条分润结果的交易信息ID
		Integer transDetailId = Integer.parseInt(profit.getObligate1());
		//获取此交易信息
		SettleTransDetail detail = null;
		//mtaegis交易信息结构体
		TransOrderInfo transOrderInfo = null;
		//mtaegis交易信息结构体
		TransOrderQueryInfo transOrderQueryInfo = null;
		//mtaegis挂账交易信息返回值结构体
		CommonResponse commonResponse = null;
		//mtaegis余额信息返回值结构体
		QueryBalanceResponse queryBalanceResponse = null;
		//mtaegis余额信息结构体
		BalanceInfo balance = null;
		//mtaegis 返回值 编码
		String code = "";
		//mtaegis 返回值 信息
		String msg = "";
		//'转账'功能编码
//		String funCode = "3001";
		
		//需要添加的新挂账信息
		List<SettleTransBill> newBillList = new ArrayList<SettleTransBill>();
		try {
			detail = settleTransDetailManager.findSettleTransDetailById(transDetailId.longValue());
			if(detail == null) {
				logger.error(">>> 异常: 获取对应交易信息失败! 返回结果为null");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		String userId = profit.getUserId();
		String rootInstCd = profit.getRootInstCd();
		String productId = profit.getProductId();
		String roleCode = profit.getRoleCode();
		String userIpAddress = profit.getUserIpAddress();
		String dealProductCode = SettleConstants.BILL_PAY_DPC;

		/*
		 *	1.调用 账户系统 - 查询账户余额接口
		 */
		transOrderQueryInfo = new TransOrderQueryInfo();
		transOrderQueryInfo.setUserId(userId);
		transOrderQueryInfo.setProductId(productId);
		transOrderQueryInfo.setRootInstCd(rootInstCd);
		transOrderQueryInfo.setRoleCode(roleCode);
		transOrderQueryInfo.setDealProductCode(dealProductCode);
		Iterator<SettleTransBill> transBillIter = billList.iterator();
		while(transBillIter.hasNext()) {
			//调用接口返回Balance实体
			//Balance balance = null;
			try {
				//balance = paymentAccountServiceApi.getBalance(user, paramString);
				queryBalanceResponse = balanceQueryApi.execute(transOrderQueryInfo);
				code = queryBalanceResponse.getCode();
				msg = queryBalanceResponse.getMsg();
				balance = queryBalanceResponse.getBalance();
				logger.info("调用[mtaegis余额查询接口]返回值: {code:"+ code +", msg:"+ msg +"}");
				if((!"1".equals(code)) || balance == null) {
					logger.error("调用账户接口获取余额失败! [USER_ID:"+ userId +", PRODUCT_ID:"+ productId +", CONST_ID:"+ rootInstCd +"]");
					return;
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("调用账户接口获取余额失败!查询余额方法异常 [USER_ID:"+ userId +", PRODUCT_ID:"+ productId +", CONST_ID:"+ rootInstCd +"]", e);
				return;
			}
			//账户提现余额
			Long amountBalance = balance.getBalanceSettle();
			//判断余额是否为0
			if(amountBalance <= 0) {
				logger.error("此用户余额为0, 不进行转账操作! [USER_ID:"+ userId +", PRODUCT_ID:"+ productId +", CONST_ID:"+ rootInstCd +"]");
				return;
			}
			logger.info(">>> >>> 调用账户接口获取余额:" + amountBalance);
			//交易后退款实体Bean
			SettleTransBill bill = transBillIter.next();
			//商户userId(出钱方)
			String billUserId = bill.getUserId();
			//平台企业账户USER_ID(入钱方)
			String interMerChantCode = enterpriseAccountUserIdMap.get(bill.getRootInstCd() + "_userId");
			String intoProductId = enterpriseAccountUserIdMap.get(bill.getRootInstCd() + "_productId");
			String interRoleCode = enterpriseAccountUserIdMap.get(bill.getRootInstCd() + "_roleCode");
			if(!billUserId.equals(userId)) {//判断此结算交易与退款交易操作的是不是同一个商户,如果不是跳出退款操作
				logger.error(">>> 失败：此结算交易与退款交易操作的不是同一个商户, 跳出退款操作 ");
				continue;
			}
			if(interMerChantCode == null || interMerChantCode.trim().isEmpty()) {
				logger.error(">>> 失败： 交易后退款信息[TRANS_BILL_ID:"+bill.getTransBillId()+"] 消费后退款 匹配机构企业主账户失败, SETTLE_PARAMENTER_INFO中初始数据错误!");
				continue;
			}
			if(billUserId.equals(interMerChantCode)) {//判断出钱方是否是平台企业主账户(入钱方是平台企业主账户,所以不能把钱自己转给自己),如果是跳出退款操作
				logger.error(">>> 失败： 出钱方是否是平台企业主账户(入钱方是平台企业主账户,所以不能把钱自己转给自己), 操作失败!");
				continue;
			}
			/*
			 * 处理转账金额逻辑
			 */
			Map<String, Object> resultMap = this.getTransferAmount(amountBalance, bill);//转账金额
			//新退款信息
			SettleTransBill newBill = (SettleTransBill) resultMap.get("newBill");
			//老退款信息
			SettleTransBill oldBill = (SettleTransBill) resultMap.get("oldBill");
			//退款金额(转账金额)
			Long transferAmount = (Long) resultMap.get("transferAmount");
			//机构userId(入钱方,消费者退款,机构垫付)
			String billMerchantCode = bill.getRootInstCd();
			//订单数量
			Integer orderCount = 1;
			//当前日期
			Date newDate = new Date();
			//自动生成订单号
			String newOrderNo = "Q" + redisIdGenerator.createRequestNo();
			//被退款交易订单号
//			String oldOrderNo = bill.getOrderNo();
			//备注
//			String remark = "qjs_tuikuan";
//			String requestNo = "";
			//交易状态
//			Integer status = 1;
			//用户手续费
//			Long userFee = Long.parseLong("0");
			/*
			 * 2.调用 账户系统 - 转账
			 */
			transOrderInfo.setUserId(billUserId);//出钱方
			transOrderInfo.setOrderAmount(transferAmount);//转账金额
			transOrderInfo.setInterUserId(interMerChantCode);//入钱方
			transOrderInfo.setRootInstCd(billMerchantCode);
			transOrderInfo.setOrderCount(orderCount);
			transOrderInfo.setOrderDate(newDate);
			transOrderInfo.setOrderNo(newOrderNo);
			transOrderInfo.setRoleCode(roleCode);
			transOrderInfo.setUserIpAddress(userIpAddress);
			transOrderInfo.setDealProductCode(dealProductCode);
//			transOrderInfo.setIntoProductId(intoProductId);
			transOrderInfo.setInterRoleCode(interRoleCode);
//			transOrderInfo.setOrderPackageNo(oldOrderNo);
//			transOrderInfo.setRemark(remark);
//			transOrderInfo.setRequestNo(requestNo);
//			transOrderInfo.setRequestTime(newDate);
//			transOrderInfo.setStatus(String.valueOf(status));
//			transOrderInfo.setUserFee(userFee);
			try {
				/*
				 * 执行转账操作
				 */
				logger.info(">>> 执行交易后退款的转账操作!");
				//errorResponse = paymentAccountServiceApi.transferForDubbo(transOrderInfo, productId);
				commonResponse = transactionCommonApi.execute(transOrderInfo);
				code = commonResponse.getCode();
				msg = commonResponse.getMsg();
				logger.info("调用[mtaegis 挂账后支付 接口]返回值: {code:"+ code +", msg:"+ msg +"}");
				if (!"1".equals(code)) {
					logger.info("失败: 分润结束，退款挂账 账户余额：["+amountBalance+"]需要退款：["+ transferAmount +"]");
					continue;
				}
			} catch (Exception e) {
				logger.info("失败: 分润结束，退款挂账 账户余额：["+amountBalance+"]需要退款：["+ transferAmount +"], 账户操作失败！调用账户系统 转账方法异常!");
				e.printStackTrace();
				continue;
			}
			try {
				//更新老的退款信息
				if("1".equals(code) && oldBill != null) {
					/*
					 * 调用'账户转账接口'成功
					 */
					logger.info(">>> >>> >>> 调用'账户转账接口'成功!");
					settleTransBillManager.updateSettleTransBill(oldBill);	
				} else {
					/*
					 * 调用'账户转账接口'失败
					 */
					logger.error(">>> >>> >>> 调用'账户转账接口'失败!");
					oldBill.setStatusId(0);
					oldBill.setRemark("交易后退款时,调用账户转账接口返回'失败'");
					settleTransBillManager.updateSettleTransBill(oldBill);	
				}	
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("失败: 账户转账交易成功，更新老的退款信息 [交易后退款] 异常!");
			}
			try {
				//存储新的退款信息
				if("1".equals(code) && newBill != null) {
					settleTransBillManager.saveSettleTransBill(newBill);
					logger.info(">>> 【挂账处理完成】新挂账单信息：主键" + newBill.getTransBillId() + "; 金额" + newBill.getBillAmount() + "; 用户ID" + newBill.getUserId()); 
					newBillList.add(newBill);
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("失败: 账户转账交易成功，存储新的退款信息 [交易后退款] 异常!");
			}
			transBillIter.remove();
			billList.remove(bill);
			logger.info("成功: 分润结束，退款挂账 账户余额：["+amountBalance+"]需要退款：["+ transferAmount +"]");
		}
		billList.addAll(newBillList);
	}
	/***
	 * 获取转账金额 并 更新挂账信息
	 * @param amountBalance 用户余额
	 * @param bill
	 * @return 转账金额
	 */
	private Map<String, Object> getTransferAmount(Long amountBalance, SettleTransBill bill) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		logger.info(">>> >>> >>> >>> 进入 获取转账金额 并 更新挂账[交易后退款]信息");
		//新生成的挂账Bean
		SettleTransBill newBill = null;
		//挂账金额
		Long billAmount = bill.getBillAmount();
		//最终金额
		Long transferAmount = 0l;
		//老挂账备注
		String oldRemark = "";
		//老挂账状态 [已处理]
		Integer oldStatusId = 1;
		if(amountBalance >= billAmount) {//如果余额 >= 挂账金额[退款金额],转账金额 为 挂账金额[退款金额],此挂账结清
			logger.info(">>> 余额 >= 挂账金额[退款金额],转账金额 为 挂账金额[退款金额],此挂账结清");
			oldRemark = "至此结清!";
			transferAmount = billAmount;
		} else {//如果余额 < 挂账金额[退款金额],转账金额 为 所有余额, 此挂账未结清
			logger.info(">>> 余额 < 挂账金额[退款金额],转账金额 为 所有余额, 此挂账未结清");
			billAmount = Math.abs(billAmount - amountBalance);
			oldRemark = "尚未结清,已处理!";
			transferAmount = amountBalance;
			newBill = this.editChildBill(bill, billAmount);
		}		
		bill.setRemark(oldRemark);
		bill.setStatusId(oldStatusId);
		bill.setUpdatedTime(null);
		
		resultMap.put("transferAmount", transferAmount);
		resultMap.put("newBill", newBill);
		resultMap.put("oldBill", bill);
		return resultMap;
	}
	/***
	 * 获取所有未清分交易信息
	 * @param 默认状态1为未进行实时清分处理
	 * @return
	 */
	public List<Map<String, Object>> getProfitTransDetail() {
		return this.getProfitTransDetail(new Integer[]{1});
	}
	/***
	 * 获取所有未清分交易信息
	 * @param statusIds 交易类型状态的Integer数组
	 * @return
	 */
	public List<Map<String, Object>> getProfitTransDetail(Integer[] statusIds) {
		logger.info(">>> >>> >>> 获取所有未清分交易信息 ... ...");
		Map<String, Object> map = new HashMap<String, Object>();
		Date beginAccDate = new Date();
		Date endAccDate = new Date();
		beginAccDate.setTime(beginAccDate.getTime() - 3 * 24 * 60 * 60 * 1000L);
		endAccDate.setTime(endAccDate.getTime() + 24 * 60 * 60 * 1000L);
		map.put("beginAccDate", beginAccDate);
		map.put("endAccDate", endAccDate);
		//订单类型0交易,1结算单, 分润就分账户的交易
		map.put("isBilled", 1);
		map.put("orderType", 0);
		map.put("statusIds", statusIds);
		//查询清分交易信息
		List<Map<String, Object>> profitTransDetailList = settleTransDetailManager.selectProfitTransInfo(map);
		return profitTransDetailList;
	}
	/***
	 * 更新DB中分润相关'交易结果' SETTLE_BALANCE_ENTRY
	 * SETTLE_BALANCE_ENTRY 	交易结果
	 * @param transProfitList
	 */
	private void insertAndUpdateSettleTransProfit(List<SettleTransProfit> transProfitList) {
		logger.info(">>> >>> >>> 更新 分润结果信息 ... ...");
		//分润信息查询query对象
		SettleTransProfitQuery query = null;
		//分润信息结果
		List<SettleTransProfit> resList = null;
		if (transProfitList.size() > 0) {
			for (SettleTransProfit subbean : transProfitList) {
				query = new SettleTransProfitQuery();
				query.setOrderNo(subbean.getOrderNo());
				query.setMerchantCode(subbean.getMerchantCode());
				query.setRootInstCd(subbean.getRootInstCd());
				query.setUserId(subbean.getUserId());
				query.setProductId(subbean.getProductId());
				query.setStatusId(0);
				//通过对账key和订单号查询对账结构
				resList = settleTransProfitManager.queryList(query);
				if (resList != null && resList.size() > 0) {//有此结果
					//修改原分润结果
					subbean.setTransProfitId(resList.get(0).getTransProfitId());
					settleTransProfitManager.updateSettleTransProfit(subbean);
				} else {//无此结果
					//添加新分润结果
					settleTransProfitManager.saveSettleTransProfit(subbean);
				}
			}
		}
	}
	/***
	 * 更新DB中分润相关'清算'交易信息
	 * SETTLE_TRANS_DETAIL 		交易信息明细
	 * @param updateTrDetIdMap
	 */
	private void updateTransDetailProfitStatus(Map<String, List<Long>> updateTrDetIdMap) throws Exception {
		logger.info(">>> >>> >>> 更新 交易信息明细 ... ...");
		Map<String, Object> map = new HashMap<String, Object>();
		//不需分润的交易信息IDList
		List<Long> unProTrDetIdList = updateTrDetIdMap.get("unProTrDetIdList");
		//分润'成功'的交易信息IDList
		List<Long> successProTrDetIdList = updateTrDetIdMap.get("successProTrDetIdList");
		//分润'失败'的交易信息IDList
		List<Long> failProTrDetIdList = updateTrDetIdMap.get("failProTrDetIdList");
		if(unProTrDetIdList != null) {//无需分润, 交易状态:分润成功
			map.put("statusId", SettleConstants.STATUS_PROFIT_SUCCESS);
			map.put("idList", unProTrDetIdList);
			settleTransDetailManager.updateTransStatusId(map);
		}
		if(successProTrDetIdList != null) {//分润成功, 交易状态:分润成功
			map.put("statusId", SettleConstants.STATUS_PROFIT_SUCCESS);
			map.put("idList", successProTrDetIdList);
			settleTransDetailManager.updateTransStatusId(map);
		}
		if(failProTrDetIdList != null) {//分润失败, 交易状态:分润失败
			map.put("statusId", SettleConstants.STATUS_PROFIT_FALID);
			map.put("idList", failProTrDetIdList);
			settleTransDetailManager.updateTransStatusId(map);
		}
	}
	/***
	 * 获取需分润的交易类型
	 * @param profitStuKey	SETTLE_PARAMETER_INFO中存储交易类型的Key
	 * @return
	 */
	private List<String> getProfitTypeByKey(String profitTypeKey) {
		return super.getFuncCodeFromParamInfo(profitTypeKey);
	}
	/***
	 * 获取全表分润规则
	 */
	private List<SettleProfitKey> getAllProfitKey() throws Exception {
		logger.info(">>> >>> >>> 获取全本分润规则 ... ...");
		//调用逻辑常量,获取内存中的分润规则		
		return logicConstantUtil.getsettleProfitKeyList();
	}
	/***
	 * 匹配交易所对应的唯一分润规则
	 * @param transDetailMap
	 * @param profitKeyList
	 * @return
	 */
	private SettleProfitKey getUniqueProfitKey4TransDetail(Map<String, Object> transDetailMap, List<SettleProfitKey> profitKeyList) {
		logger.info(">>> >>> >>> 匹配交易所对应的唯一分润规则 ... ...");
		//全部分润规则迭代器
		Iterator<SettleProfitKey> iter = profitKeyList.iterator();
		//声明分润规则对账
		SettleProfitKey theProfitKey = null;
		//遍历
		while(iter.hasNext()) {
			//从迭代器中获取分润规则对象
			SettleProfitKey item = iter.next();
			//匹配规则的key - 规则信息
			String rulekey = "";
			//匹配规则的key - 交易信息
			String transKey = "";
			//默认key "机构号,功能码"
			rulekey = item.getRootInstCd() + "," + item.getFuncCode();
			transKey = String.valueOf(transDetailMap.get("MERCHANT_CODE")) + "," + String.valueOf(transDetailMap.get("FUNC_CODE"));
			/**
			 * 匹配分润规则
			 * 常规情况下使用"机构号&功能码"就可以匹配到此条交易的唯一分润规则
			 * 考虑特殊需求预留了3组扩展字段作为动态key设置
			 * 以下逻辑为动态key匹配
			 */
			if(item.getKeyName1()!=null && !item.getKeyName1().isEmpty()) {
				rulekey += "," + item.getKeyValue1();
				transKey += "," + transDetailMap.get(item.getKeyName1());
				if(item.getKeyName2()!=null && !item.getKeyName2().isEmpty()) {
					rulekey += "," + item.getKeyValue2();
					transKey += "," + transDetailMap.get(item.getKeyName2());
					if(item.getKeyName3()!=null && !item.getKeyName3().isEmpty()) {
						rulekey += "," + item.getKeyValue3();
						transKey += "," + transDetailMap.get(item.getKeyName3());
						if(item.getKeyName4()!=null && !item.getKeyName4().isEmpty()) {
							rulekey += "," + item.getKeyValue4();
							transKey += "," + transDetailMap.get(item.getKeyName4());
							if(item.getKeyName5()!=null && !item.getKeyName5().isEmpty()) {
								rulekey += "," + item.getKeyValue5();
								transKey += "," + transDetailMap.get(item.getKeyName5());
								if(item.getKeyName6()!=null && !item.getKeyName6().isEmpty()) {
									rulekey += "," + item.getKeyValue6();
									transKey += "," + transDetailMap.get(item.getKeyName6());
									if(item.getKeyName7()!=null && !item.getKeyName7().isEmpty()) {
										rulekey += "," + item.getKeyValue7();
										transKey += "," + transDetailMap.get(item.getKeyName7());
										if(item.getKeyName8()!=null && !item.getKeyName8().isEmpty()) {
											rulekey += "," + item.getKeyValue8();
											transKey += "," + transDetailMap.get(item.getKeyName8());
										}
									}
								}
							}
						}
					}
				}
			}
			if(rulekey.equals(transKey)) {
				theProfitKey = item;
				break;
			}
		}
		if(theProfitKey == null) {
			logger.error("未匹配到此条交易[ID:"+String.valueOf(transDetailMap.get("TRANS_DETAIL_ID"))+", ORDER_NO:"+String.valueOf(transDetailMap.get("ORDER_NO"))+"]的'分润规则'!");
			throw new SettleException("未匹配到此条交易[ID:"+String.valueOf(transDetailMap.get("TRANS_DETAIL_ID"))+", ORDER_NO:"+String.valueOf(transDetailMap.get("ORDER_NO"))+"]的'分润规则'!");
		}
		return theProfitKey;
	}
	/***
	 * 查询全部未结算的清分交易
	 * @return
	 */
	private List<SettleTransProfit> getTransProfitWithUnbalance() {
		logger.info(">>> >>> >>> >>> 查询全部未结算的清分交易!");
		List<Integer> detailStatusIdList = new ArrayList<Integer>();
		detailStatusIdList.add(21);//对账成功的交易,交易进来分润后状态为11[实时分润],如果参与对账状态为21[参与对账的交易]
		detailStatusIdList.add(11);//分润成功
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("proStatusId", 0);
		queryMap.put("detailStatusIdList", detailStatusIdList);
		return settleTransProfitManager.selectTransProfitWithUnbalance(queryMap);
	}
	/***
	 * 通过交易信息表的ID 查询对应的分润规则 
	 * @param ids
	 * @return
	 */
	private List<SettleTransProfit> getTransProfitByDetailIds(String[] ids) {
		logger.info(">>> >>> >>> >>> 查询全部未结算的清分交易,根据交易ID!");
		List<Integer> detailStatusIdList = new ArrayList<Integer>();
		detailStatusIdList.add(21);//对账成功的交易,交易进来分润后状态为11[实时分润],如果参与对账状态为21[参与对账的交易]
		detailStatusIdList.add(11);//分润成功
		List<SettleTransProfit> settleTransProfitList = null;
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("ids", ids);
		queryMap.put("proStatusId", 0);
		queryMap.put("detailStatusIdList", detailStatusIdList);
		settleTransProfitList = settleTransProfitManager.selectTransProfitWithDetailId(queryMap);
		return settleTransProfitList;
	}
	/***
	 * 更新 分润结果表状态 为'已结算'
	 * @param idList
	 */
	private void updateTransProfitStatus(List<Integer> idList) {
		logger.info(">>> >>> >>> >>> 更新 分润结果表状态 为'已结算'");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("statusId", 1);//已结算
		map.put("remark", "分润结果, 结算成功!");//已结算
		map.put("idList", idList);
		settleTransProfitManager.updateTransStatusId(map);
	}
	/***
	 * 更新 失败分润结果
	 * @param idList
	 */
	private void updateFailTransProfitStatus(List<Integer> idList) {
		logger.info(">>> >>> >>> >>> 更新 分润结果表备注 为'结算失败'");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("statusId", 0);//已结算
		map.put("remark", "结算失败, 调用账户结算接口返回结果失败!");//未结算
		map.put("idList", idList);
		settleTransProfitManager.updateTransStatusId(map);
	}
	/***
	 * 获取所有未处理的挂账[交易后退款]信息
	 * @return
	 * @throws SettleException
	 */
	private List<SettleTransBill> getTransBillByUntreated() throws SettleException {
		SettleTransBillQuery query = new SettleTransBillQuery();
		query.setBillType(1);//交易后退款挂账
		query.setStatusId(0);//未处理挂账
		return settleTransBillManager.queryOrderByCreatedTime(query);
	}
	
	/***
	 * 编辑新的挂账信息
	 * @param oldBill	衍生新挂账的老挂账信息
	 * @param newAmount 挂账结算后的剩余金额
	 * @return
	 */
	private SettleTransBill editChildBill(SettleTransBill oldBill, Long newAmount) {
		SettleTransBill newBill = new SettleTransBill();
		String orderNo = oldBill.getOrderNo();
		String batchNo = oldBill.getBatchNo();
		String billNo = oldBill.getBillNo();
		String rootInstCd = oldBill.getRootInstCd();
		String productId = oldBill.getProductId();
		String userId = oldBill.getUserId();
		String referUserId = oldBill.getReferUserId();
		Integer billType = oldBill.getBillType();
		String obligate1 = oldBill.getObligate1();
		String obligate2 = oldBill.getObligate2();
		Integer newStatusId = 0;
		String newRemark = "尚未结清,未处理!";
		Date accountDate = oldBill.getAccountDate();
		newBill.setOrderNo(orderNo);
		newBill.setBatchNo(batchNo);
		newBill.setBillNo(billNo);
		newBill.setRootInstCd(rootInstCd);
		newBill.setProductId(productId);
		newBill.setUserId(userId);
		newBill.setReferUserId(referUserId);
		newBill.setBillType(billType);
		newBill.setObligate1(obligate1);
		newBill.setObligate2(obligate2);
		newBill.setAccountDate(accountDate);
		newBill.setBillAmount(newAmount);
		newBill.setRemark(newRemark);
		newBill.setStatusId(newStatusId);
		return newBill;
	}
	/***
	 * 获取所有机构号和企业主账户USER_ID的数据结构
	 * @return
	 */
	private Map<String, String> getEnterpriseAccountUserId() throws Exception {
		//存储企业主账户的数据结构
		Map<String, String> enterpriseAccountUserIdMap = new HashMap<String, String>();	
		//创建属性表的查询对象query
		SettleParameterInfoQuery query = new SettleParameterInfoQuery(); 
		//ROOT_ENTERPRISE_ACCOUNT_USER_ID(1000000012)表示 机构对应的企业主账户
		query.setParameterType(SettleConstants.ROOT_ENTERPRISE_ACCOUNT_USER_ID);
		//查询企业主账户
		List<SettleParameterInfo> enterpriseUserIdList = settleParameterInfoManager.queryList(query);
		//遍历所有信息重构数据
		Iterator<SettleParameterInfo> enterpriseUserIdIter = enterpriseUserIdList.iterator();
		while(enterpriseUserIdIter.hasNext()) {
			SettleParameterInfo settleParameterInfo = enterpriseUserIdIter.next();
			String rootInstCd = settleParameterInfo.getParameterCode();
			String enterpriseUserId = settleParameterInfo.getParameterValue();
			String enterpriseProductId = settleParameterInfo.getProductId();
			String enterpriseProductIdObligate1 = settleParameterInfo.getObligate1();
			enterpriseAccountUserIdMap.put(rootInstCd + "_userId", enterpriseUserId);
			enterpriseAccountUserIdMap.put(rootInstCd + "_productId", enterpriseProductId);
			enterpriseAccountUserIdMap.put(rootInstCd + "_roleCode", enterpriseProductIdObligate1);
		}
		return enterpriseAccountUserIdMap;
	}
	/**
	 * 获取 需进行实时清分的交易状态
	 * @return
	 */
	public Integer[] getProfitIds() throws Exception {
		//ID字符串数组
		String[] idsStrArr = null;
		//ID整形数组
		Integer[] doProfitIds = null;
		//ID字符串
		String idsStr = "";
		//索引
		Integer index = 0;
		//获取 未进行实时清分的交易状态SQl_Query
		SettleParameterInfoQuery query = new SettleParameterInfoQuery();
		query.setParameterType(SettleConstants.PARAMETER_TYPE_STATUS_IDS);
		query.setParameterCode(SettleConstants.PROFIT_STATUS_IDS);
		query.setStatusId(1);
		//获取 未进行实时清分的交易状态
		List<SettleParameterInfo> settleParameterInfoList = settleParameterInfoManager.queryList(query);
		if(settleParameterInfoList == null) throw new Exception("获取 需进行实时清分的交易状态时 返回值为null!");
		if(settleParameterInfoList.size() < 1) throw new Exception("获取 需进行实时清分的交易状态信息 0条!");
		//拆分ID字符串
		Iterator<SettleParameterInfo> settleParameterInfoIter = settleParameterInfoList.iterator();
		while(settleParameterInfoIter.hasNext()) {
			SettleParameterInfo item = settleParameterInfoIter.next();
			idsStr += "," + item.getParameterValue();
		}
		idsStr = idsStr.substring(1);
		//ID字符串数组 => ID整形数组
		idsStrArr = idsStr.split(",");
		doProfitIds = new Integer[idsStrArr.length];
		for(String idStr : idsStrArr) {
			doProfitIds[index ++] = Integer.parseInt(idStr);
		}
		return doProfitIds;
	}
}