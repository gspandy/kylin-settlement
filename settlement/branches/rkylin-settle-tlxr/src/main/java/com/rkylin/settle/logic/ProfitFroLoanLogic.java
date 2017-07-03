package com.rkylin.settle.logic;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.Rop.api.DefaultRopClient;
import com.Rop.api.request.WheatfieldUserWithdrowRequest;
import com.Rop.api.request.WheatfieldUserWithholdRequest;
import com.Rop.api.response.WheatfieldUserWithdrowResponse;
import com.Rop.api.response.WheatfieldUserWithholdResponse;
import com.rkylin.common.RedisIdGenerator;
import com.rkylin.settle.common.SessionUtils;
import com.rkylin.settle.constant.SettleConstants;
import com.rkylin.settle.constant.TransCodeConst;
import com.rkylin.settle.exception.SettleException;
import com.rkylin.settle.manager.SettleLoanDetailManager;
import com.rkylin.settle.manager.SettleParameterInfoManager;
import com.rkylin.settle.manager.SettleProfitInvoiceManager;
import com.rkylin.settle.manager.SettleTransBillManager;
import com.rkylin.settle.manager.SettleTransProfitManager;
import com.rkylin.settle.manager.TransOrderInfoManager;
import com.rkylin.settle.pojo.SettleBalanceEntry;
import com.rkylin.settle.pojo.SettleProfitInvoice;
import com.rkylin.settle.pojo.SettleProfitInvoiceQuery;
import com.rkylin.settle.pojo.SettleProfitKey;
import com.rkylin.settle.pojo.SettleProfitRule;
import com.rkylin.settle.pojo.SettleTransBill;
import com.rkylin.settle.pojo.SettleTransBillQuery;
import com.rkylin.settle.pojo.SettleTransProfit;
import com.rkylin.settle.pojo.SettleTransProfitQuery;
import com.rkylin.settle.util.LogicConstantUtil;
import com.rkylin.settle.util.StrMathFroProfit;
import com.rkylin.settle.util.StringUtils;
import com.rkylin.wheatfield.api.PaymentAccountServiceApi;
import com.rkylin.wheatfield.api.PaymentInternalOutService;
import com.rkylin.wheatfield.model.CommonResponse;
import com.rkylin.wheatfield.pojo.Balance;
import com.rkylin.wheatfield.pojo.TransOrderInfo;
import com.rkylin.wheatfield.pojo.User;

/***
 * 分润逻辑
 * @author Yang
 *
 */
@Component("profitFroLoanLogic")
public class ProfitFroLoanLogic extends BasicLogic {

//    static ScriptEngine jse = new ScriptEngineManager().getEngineByName("JavaScript");  
	
	@Autowired
	private SettleParameterInfoManager settleParameterInfoManager;		//'清算'属性表Manager
	@Autowired
	private SettleLoanDetailManager settleLoanDetailManager;
	@Autowired
	private SettleTransProfitManager settleTransProfitManager;			//'分润结果信息'Manager
	@Autowired
	private SettleTransBillManager settleTransBillManager;				//'挂账信息'Manager
	@Autowired
	private LogicConstantUtil logicConstantUtil;						//逻辑常量工具类
	@Autowired
    private PaymentInternalOutService paymentInternalOutService;        //'账户系统'分润API
	@Autowired
	private PaymentAccountServiceApi payAccSerApi;
	@Autowired
	private PaymentAccountServiceApi paymentAccountServiceApi;			//'账户系统'余额API
	@Autowired
	private TransOrderInfoManager transOrderInfoManager;				//'账户'交易记录信息Manager
	@Autowired
	private RedisIdGenerator redisIdGenerator;							//'清结算'交易信息Service
	@Autowired
	Properties userProperties;
	@Autowired
	SettleProfitInvoiceManager settleProfitInvoiceManager;
	/***
	 * 分润
	 * @param profitTransDetailList	所有未清分交易信息
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> doProfit(List<Map<String, Object>> profitTransDetailList1) {
		logger.info(">>> >>> >>> >>> 进入   分润   操作");
		//提示信息
		Map<String, Object> resultMap = new HashMap<String, Object>();
		//分润规则List
		List<SettleProfitKey> profitKeyList = null;
		//分润结果List
		List<SettleTransProfit> transProfitList = new ArrayList<SettleTransProfit>();
		//挂账结果List
		List<SettleTransBill> transBillList = new ArrayList<SettleTransBill>();
		//分润结果List
		List<SettleTransProfit> transProfitTmpList = new ArrayList<SettleTransProfit>();
		//挂账结果List
		List<SettleTransBill> transBillTmpList = new ArrayList<SettleTransBill>();
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
		// 需清分交易集合
		List<Map<String, Object>> profitTransDetailList = new ArrayList<Map<String, Object>>();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd 00:00:00");

		if (profitTransDetailList1 == null || profitTransDetailList1.size() == 0) {
			profitTransDetailList = this.getProfitTransDetail(new Integer[]{1});
		} else {
			profitTransDetailList = profitTransDetailList1;
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
			Map<String, Object> loanDetailMap = detailIter.next();
			/**
			 * 待插入: 分润结果的'交易信息数据'
			 */
			Long loanId = Long.parseLong(String.valueOf(loanDetailMap.get("LOAN_ID")));	//交易信息ID
			String rootInstCd = String.valueOf(loanDetailMap.get("ROOT_INST_CD"));		//机构号
			Date account = (Date) loanDetailMap.get("REPAYMENT_DATE");						//还款日期
			String accountdate = null;
			if (account!=null) {
				accountdate = sdf.format(account);
				try {
					account = sdf.parse(accountdate);
				} catch (Exception e) {
					logger.error(">>> " + e.getMessage());
					e.printStackTrace();
				}
			} else {
				Calendar cloDate = Calendar.getInstance();
				cloDate.set(1900, 0, 1);
				account = cloDate.getTime();
			}
			//初始化'交易结果'对象信息
			settleBalanceEntry.setOrderNo(String.valueOf(loanId));
			//交易结果状态位为3表示'分润交易结果'
			settleBalanceEntry.setBalanceType(40);
			//订单号+交易信息ID
			settleBalanceEntry.setObligate1(String.valueOf(loanId));
			settleBalanceEntry.setAccountDate(account);

			//分润规则信息对象 - 获取到唯一的分润信息
			SettleProfitKey profitKey = null;
			//分润规则详细信息对象
			List<SettleProfitRule> profitRuleList = null;
			try {
				//匹配唯一分润规则
				profitKey = this.getUniqueProfitKey4TransDetail(loanDetailMap, profitKeyList);
				if(profitKey == null) {
					message = "分润失败: 未查询到此条交易 [ID:"+loanId+"] 相应的到分润规则!";
					logger.info(">>> " + message);
					failProTrDetIdList.add(loanId);
					this.editResultMap(resultMap, "0", message);
					//初始化交易结果表信息
					settleBalanceEntry.setStatusId(SettleConstants.STATUS_PROFIT_FALID);
					settleBalanceEntry.setRemark(message);
					settleBalanceEntryList.add(settleBalanceEntry);
					continue detailIter;
				}
			} catch (Exception e) {
				message = "异常:查询到此条交易 [ID:"+loanId+"] 分润规则异常!";
				logger.error(">>> " + message);
				e.printStackTrace();
				failProTrDetIdList.add(loanId);
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
					message = "未查询到此条交易 [ID:"+loanId+"] 相应的到分润规则明细!";
					logger.info(">>> " + message);
					failProTrDetIdList.add(loanId);
					this.editResultMap(resultMap, "0", message);
					//初始化交易结果表信息
					settleBalanceEntry.setStatusId(SettleConstants.STATUS_PROFIT_FALID);
					settleBalanceEntry.setRemark(message);
					settleBalanceEntryList.add(settleBalanceEntry);
					continue detailIter;
				}
			} catch (Exception e) {
				message = "异常:查询到此条交易 [ID:"+loanId+"] 分润规则明细异常!";
				logger.error(">>> " + message);
				e.printStackTrace();
				failProTrDetIdList.add(loanId);
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
			String amountStr = loanDetailMap.get("AMOUNT")==null?"0":String.valueOf(loanDetailMap.get("AMOUNT"));
			String capitalStr =  loanDetailMap.get("SHOULD_CAPITAL")==null?"0":String.valueOf(loanDetailMap.get("SHOULD_CAPITAL"));
			String shinterestStr = loanDetailMap.get("SHOULD_INTEREST")==null?"0":String.valueOf(loanDetailMap.get("SHOULD_INTEREST"));
			String fineStr = loanDetailMap.get("OVERDUE_FINE")==null?"0":String.valueOf(loanDetailMap.get("OVERDUE_FINE"));
			String ovinterestStr = loanDetailMap.get("OVERDUE_INTEREST")==null?"0":String.valueOf(loanDetailMap.get("OVERDUE_INTEREST"));
			String specialStr = loanDetailMap.get("SPECIAL_FLG3")==null?"0":String.valueOf(loanDetailMap.get("SPECIAL_FLG3"));//保理服务费
			String ob1Str = loanDetailMap.get("OBLIGATE1")==null?"0":String.valueOf(loanDetailMap.get("OBLIGATE1"));//融资比例
			String ob2Str = loanDetailMap.get("OBLIGATE2")==null?"0":String.valueOf(loanDetailMap.get("OBLIGATE2"));//已还利息
			String ob3Str = loanDetailMap.get("OBLIGATE3")==null?"0":String.valueOf(loanDetailMap.get("OBLIGATE3"));//已还本金
			String ob4Str = loanDetailMap.get("OBLIGATE4")==null?"0":String.valueOf(loanDetailMap.get("OBLIGATE4"));//未还利息
			String ob5Str = loanDetailMap.get("OBLIGATE5")==null?"0":String.valueOf(loanDetailMap.get("OBLIGATE5"));//未还本金
			String ob6Str = loanDetailMap.get("OBLIGATE6")==null?"0":String.valueOf(loanDetailMap.get("OBLIGATE6"));//未还本金
			BigDecimal amount = new BigDecimal("0");									//分润金额
			
			transProfitTmpList = new ArrayList<SettleTransProfit>();
			transBillTmpList = new ArrayList<SettleTransBill>();
			/**
			 *遍历'分润详细规则'进行分润操作和分润结果更新
			 */
			Iterator<SettleProfitRule> ruleIter = profitRuleList.iterator();
			ruleIter: while(ruleIter.hasNext()) {
				//对账规则明细对象
				SettleProfitRule rule = ruleIter.next();
				//分润结果信息对象
				SettleTransProfit transProfit = new SettleTransProfit();
				//挂账结果信息对象
				SettleTransBill transBill = new SettleTransBill();
				//转出方形态1固定,2可变
				Integer profitObject = rule.getProfitObject();
				//转入方形态1固定,2可变
				Integer interProfitObject = rule.getInterProfitObject();
				//相关方形态1固定,2可变
				Integer referProfitObject = rule.getReferProfitObject();
				//相关方形态1固定,2可变
				Integer refer2ProfitObject = rule.getRefer2ProfitObject();
				//分润模式:0为不启用1为按比例计费2为固定金额
				Integer profitMd = rule.getProfitMd();
				//分润比例 or 分润金额String
				String profitFeeStr = rule.getProfitFee();
				//分润比例 or 分润金额
				BigDecimal profitFee = null;
				//分润后的金额 初始化0
				BigDecimal profitAmount = new BigDecimal("0");
				//数据方向
				String insertTable = rule.getInsertTable();
				//产品ID
				String productId = rule.getProductId();
				//实际用户ID
				String userId = null;
				String interUserId = null;
				String intoProductId = null;
				String referUserId = null;
				String referUserId2 = null;
				//选择 分润对象形态1固定,2可变
				switch(profitObject) {
					case 1 ://1固定
						userId = rule.getUserId();
						break;
					case 2 ://2可变
						userId = String.valueOf(loanDetailMap.get(rule.getUserId()));
						break;
					default :
						message = "异常: 此条交易 [ID:"+loanId+"]的 分润规则明细  分润对象形态 数据有误 数据必须在[1,2]之中";
						logger.error(">>> " + message);
						this.editResultMap(resultMap, "-1", message);
						//初始化交易结果表信息
						settleBalanceEntry.setStatusId(SettleConstants.STATUS_PROFIT_FALID);
						settleBalanceEntry.setRemark(message);
						settleBalanceEntryList.add(settleBalanceEntry);
						continue detailIter;
				}
				switch(interProfitObject) {
					case 1 ://1:interUserId固定
						interUserId = rule.getInterUserId();
						intoProductId = rule.getIntoProductId();
						break;
					case 2 ://2:interUserId可变
						interUserId = String.valueOf(loanDetailMap.get(rule.getInterUserId()));
						intoProductId = rule.getIntoProductId();
						break;
					case 3 ://3:interUserId可变并且IntoProductId可变
						interUserId = String.valueOf(loanDetailMap.get(rule.getInterUserId()));
						intoProductId = String.valueOf(loanDetailMap.get(rule.getIntoProductId()));
						break;
					default :
						message = "异常: 此条交易 [ID:"+loanId+"]的 分润规则明细  分润对象形态 数据有误 数据必须在[1,2]之中";
						logger.error(">>> " + message);
						this.editResultMap(resultMap, "-1", message);
						//初始化交易结果表信息
						settleBalanceEntry.setStatusId(SettleConstants.STATUS_PROFIT_FALID);
						settleBalanceEntry.setRemark(message);
						settleBalanceEntryList.add(settleBalanceEntry);
						continue detailIter;
				}
				if (rule.getReferUserId2()!=null && !"".equals(rule.getReferUserId2())) {
					switch(refer2ProfitObject) {
						case 1 ://1固定
							referUserId2 = rule.getReferUserId2();
							break;
						case 2 ://2可变
							referUserId2 = String.valueOf(loanDetailMap.get(rule.getReferUserId2()));
							break;
						default :
							message = "异常: 此条交易 [ID:"+loanId+"]的 分润规则明细  分润对象形态 数据有误 数据必须在[1,2]之中";
							logger.error(">>> " + message);
							this.editResultMap(resultMap, "-1", message);
							//初始化交易结果表信息
							settleBalanceEntry.setStatusId(SettleConstants.STATUS_PROFIT_FALID);
							settleBalanceEntry.setRemark(message);
							settleBalanceEntryList.add(settleBalanceEntry);
							continue detailIter;
					}
				}
				if (rule.getReferUserId()!=null && !"".equals(rule.getReferUserId())) {
					switch(referProfitObject) {
						case 1 ://1固定
							referUserId = rule.getReferUserId();
							break;
						case 2 ://2可变
							referUserId = String.valueOf(loanDetailMap.get(rule.getReferUserId()));
							break;
						default :
							message = "异常: 此条交易 [ID:"+loanId+"]的 分润规则明细  分润对象形态 数据有误 数据必须在[1,2]之中";
							logger.error(">>> " + message);
							this.editResultMap(resultMap, "-1", message);
							//初始化交易结果表信息
							settleBalanceEntry.setStatusId(SettleConstants.STATUS_PROFIT_FALID);
							settleBalanceEntry.setRemark(message);
							settleBalanceEntryList.add(settleBalanceEntry);
							continue detailIter;
					}
				}
				//选择 分润模式:0为不启用1为按比例计费2为固定金额3算式分配
				switch(profitMd) {
					case 0 :
						logger.info(">>> 此分润规则明细[ID:"+rule.getProductId()+","+rule.getSubId()+"] 未启用!");
						continue ruleIter;
//					case 1 :
//						profitFee = new BigDecimal(profitFeeStr);
//						profitAmount = amount.multiply(profitFee);//金额 * 百分比
//						profitAmount = profitAmount.setScale(0, BigDecimal.ROUND_HALF_UP);//取整 - 四舍五入
//						break;
//					case 2 :
//						profitFee = new BigDecimal(profitFeeStr);
//						profitAmount = profitAmount.add(profitFee);
//						break;
//					case 3 :
//						/*
//						 * 采用减法算式进行分润金额的计算
//						 * 例如:1-0.7-0.2的含义是:应分金额 = 交易金额 * 1 - 交易金额 * 0.7 - 交易金额 * 0.2
//						 */
//						//解析算式
//						String[] profitFeeStrArr = profitFeeStr.split("-");
//						//判断算式格式是否正确
//						Integer len = profitFeeStrArr.length;
//						if(len > 1) {//正确
//							profitAmount = amount;
//							for(int i = 1; i < len; i ++) {
//								String amountItemStr = profitFeeStrArr[i];
//								BigDecimal amountItem = new BigDecimal(amountItemStr);
//								amountItem = amount.multiply(amountItem);
//								profitAmount = profitAmount.subtract(amountItem);
//							}
//						} else {//错误
//							message = "此条交易 [ID:"+loanId+"]的 分润规则明细  分润模式为采用减法算式进行分润金额的计算 算式'解析'失败!";
//							logger.error(">>> " + message);
//							failProTrDetIdList.add(loanId);
//							this.editResultMap(resultMap, "-1", message);
//							//初始化交易结果表信息
//							settleBalanceEntry.setStatusId(SettleConstants.STATUS_PROFIT_FALID);
//							settleBalanceEntry.setRemark(message);
//							settleBalanceEntryList.add(settleBalanceEntry);
//							continue detailIter;
//						}
//						break;
//					case 4 :
//						/*
//						 * 采用减法算式进行分润金额的计算
//						 * 例如:1-12000-300的含义是:应分金额 = 交易金额 * 1 - 12000 - 300
//						 */
//						//解析算式
//						String[] profitFeeStrArr2 = profitFeeStr.split("-");
//						//判断算式格式是否正确
//						Integer len2 = profitFeeStrArr2.length;
//						if(len2 > 1) {//正确
//							profitAmount = amount;
//							for(int i = 1; i < len2; i ++) {
//								String amountItemStr = profitFeeStrArr2[i];
//								BigDecimal amountItem = new BigDecimal(amountItemStr);
//								profitAmount = profitAmount.subtract(amountItem);
//							}
//							if (profitAmount.longValue() < 0) {
//								message = "此条交易 [ID:"+loanId+"]的 分润规则明细  分润模式为采用减法算式进行分润金额的计算 算式结果出现负值!";
//								logger.error(">>> " + message);
//								failProTrDetIdList.add(loanId);
//								this.editResultMap(resultMap, "-1", message);
//								//初始化交易结果表信息
//								settleBalanceEntry.setStatusId(SettleConstants.STATUS_PROFIT_FALID);
//								settleBalanceEntry.setRemark(message);
//								settleBalanceEntryList.add(settleBalanceEntry);
//								continue detailIter;
//							}
//						} else {//错误
//							message = "此条交易 [ID:"+loanId+"]的 分润规则明细  分润模式为采用减法算式进行分润金额的计算 算式'解析'失败!";
//							logger.error(">>> " + message);
//							failProTrDetIdList.add(loanId);
//							this.editResultMap(resultMap, "-1", message);
//							//初始化交易结果表信息
//							settleBalanceEntry.setStatusId(SettleConstants.STATUS_PROFIT_FALID);
//							settleBalanceEntry.setRemark(message);
//							settleBalanceEntryList.add(settleBalanceEntry);
//							continue detailIter;
//						}
//						break;
					default : 
						try {
							if (profitFeeStr.contains("{AMOUNT}")) {
								profitFeeStr = profitFeeStr.replace("{AMOUNT}", amountStr);
							}
							if (profitFeeStr.contains("{SHOULD_CAPITAL}")) {
								profitFeeStr = profitFeeStr.replace("{SHOULD_CAPITAL}", capitalStr);
							}
							if (profitFeeStr.contains("{SHOULD_INTEREST}")) {
								profitFeeStr = profitFeeStr.replace("{SHOULD_INTEREST}", shinterestStr);
							}
							if (profitFeeStr.contains("{OVERDUE_FINE}")) {
								profitFeeStr = profitFeeStr.replace("{OVERDUE_FINE}", fineStr);
							}
							if (profitFeeStr.contains("{OI}")) {
								profitFeeStr = profitFeeStr.replace("{OI}", ovinterestStr);
							}
							if (profitFeeStr.contains("{SP3}")) {
								profitFeeStr = profitFeeStr.replace("{SP3}", specialStr);
							}
							if (profitFeeStr.contains("{OB1}")) {
								profitFeeStr = profitFeeStr.replace("{OB1}", ob1Str);
							}
							if (profitFeeStr.contains("{OB2}")) {
								profitFeeStr = profitFeeStr.replace("{OB2}", ob2Str);
							}
							if (profitFeeStr.contains("{OB3}")) {
								profitFeeStr = profitFeeStr.replace("{OB3}", ob3Str);
							}
							if (profitFeeStr.contains("{OB4}")) {
								profitFeeStr = profitFeeStr.replace("{OB4}", ob4Str);
							}
							if (profitFeeStr.contains("{OB5}")) {
								profitFeeStr = profitFeeStr.replace("{OB5}", ob5Str);
							}
							if (profitFeeStr.contains("{OB6}")) {
								profitFeeStr = profitFeeStr.replace("{OB6}", ob6Str);
							}
						
							amount = new BigDecimal(StrMathFroProfit.cacComplex(profitFeeStr));
							profitAmount = amount.setScale(0, BigDecimal.ROUND_HALF_UP);
						} catch (Exception e) {
							message = "此条交易 [ID:"+loanId+"]的 分润规则明细  分润公式有问题！";
							logger.error(">>> " + message+e.getMessage());
							e.printStackTrace();
							failProTrDetIdList.add(loanId);
							this.editResultMap(resultMap, "-1", message);
							//初始化交易结果表信息
							settleBalanceEntry.setStatusId(SettleConstants.STATUS_PROFIT_FALID);
							settleBalanceEntry.setRemark(message);
							settleBalanceEntryList.add(settleBalanceEntry);
							continue detailIter;
						}
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
				// 结算日期计算
				Date settleTime = new Date();
				Calendar cloDate = Calendar.getInstance();
				Calendar cloBDate = Calendar.getInstance();

				if ("T-1".equals(rule.getSettleType())) {
					settleTime = account;
				} else if (rule.getSettleType().startsWith("M")) {
					int dint = Integer.parseInt(rule.getSettleType().substring(1));
					int d1int = dint - 1;
					// 前月20日
					cloBDate.add(Calendar.MONTH, -1);
					cloBDate.set(Calendar.DATE, dint);
					cloBDate.set(Calendar.HOUR_OF_DAY, 0);
					cloBDate.set(Calendar.MINUTE,0);
					cloBDate.set(Calendar.SECOND, 0);
					// 当月20日
					cloDate.set(Calendar.DATE, dint);
					cloDate.set(Calendar.HOUR_OF_DAY, 0);
					cloDate.set(Calendar.MINUTE,0);
					cloDate.set(Calendar.SECOND, 0);
					if (account.before(cloBDate.getTime())) {
						settleTime = account;
					} else if (account.before(cloDate.getTime())) {
						cloDate.add(Calendar.DATE, -1);
						cloDate.set(Calendar.HOUR_OF_DAY, 0);
						cloDate.set(Calendar.MINUTE,0);
						cloDate.set(Calendar.SECOND, 0);
						settleTime = cloDate.getTime();
					} else {
						cloDate.add(Calendar.MONTH, 1);
						cloDate.add(Calendar.DATE, -1);
						cloDate.set(Calendar.HOUR_OF_DAY, 0);
						cloDate.set(Calendar.MINUTE,0);
						cloDate.set(Calendar.SECOND, 0);
						settleTime = cloDate.getTime();
					}
				} else {
					settleTime = account;
				}
				
				if (longProfitAmount < 0) {
					transProfitTmpList = new ArrayList<SettleTransProfit>();
					transBillTmpList = new ArrayList<SettleTransBill>();
					message = "失败: 此条交易 [ID:"+loanId+"] 分润中存在负值 !";
					logger.info(">>> " + message);
					//存入分润'成功'的交易信息IDList
					failProTrDetIdList.add(loanId);
					//初始化交易结果表信息
					settleBalanceEntry.setStatusId(SettleConstants.STATUS_PROFIT_FALID);
					settleBalanceEntry.setRemark(message);
					settleBalanceEntryList.add(settleBalanceEntry);
					continue detailIter;
				} else if (longProfitAmount == 0) {
					// nothing
				} else if ("1".equals(rule.getInsertTable())) {
					Integer profitType = rule.getProfitType();								//清分类型:0分润，1代收，2代付等					
					Integer isMust = rule.getIsMust();										//0非必须,1..n必须(优先级)
					Integer statusId = 0;													//状态,0未进行结算处理,1已进行结算处理,2不处理
					String remark = "已经执行清分, 未进行结算处理";									//备注信息 待编辑
					//初始化'分润结果信息'对象
					transProfit.setOrderNo(String.valueOf(loanId));
					transProfit.setUserId(userId);
					transProfit.setRootInstCd(rootInstCd);
					transProfit.setProductId(productId);
					transProfit.setProfitType(profitType);
					transProfit.setProfitAmount(longProfitAmount);
					transProfit.setIsMust(isMust);
					transProfit.setSettleObject(rule.getSettleObject());
					transProfit.setSettleType(rule.getSettleType());
					transProfit.setSettleMain(rule.getSettleMain());
					transProfit.setApiType(rule.getApiType());
					transProfit.setRoleCode(rule.getRoleCode());
					transProfit.setInterUserId(interUserId);
					transProfit.setInterRoleCode(rule.getInterRoleCode());
					transProfit.setIntoProductId(StringUtils.isEmpty(intoProductId) ? rule.getIntoProductId() : intoProductId);
					transProfit.setReferUserId(referUserId2);
					transProfit.setReferRoleCode(rule.getReferRoleCode());
					transProfit.setReferProductId(rule.getReferProductId());
					transProfit.setReferUserId2(referUserId2);
					transProfit.setReferRoleCode2(rule.getReferRoleCode2());
					transProfit.setReferProductId2(rule.getReferProductId2());
					transProfit.setUserIpAddress("168.0.0.1");
					transProfit.setStatusId(statusId);
					transProfit.setRemark(remark);
					transProfit.setAccountDate(settleTime);
					transProfit.setObligate1(rule.getProfitDetailId()+","+rule.getSubId());
					//存入对账结果List
					transProfitTmpList.add(transProfit);
				} else if ("2".equals(rule.getInsertTable())) {
					transBill.setOrderNo(String.valueOf(loanId));
					transBill.setBillNo(loanId+"0001");
					transBill.setRootInstCd(rootInstCd);
					transBill.setProductId(productId);
					transBill.setUserId(userId);
					transBill.setRoleCode(rule.getRoleCode());
					transBill.setInterUserId(interUserId);
					transBill.setInterRoleCode(rule.getInterRoleCode());
					transBill.setIntoProductId(StringUtils.isEmpty(intoProductId) ? rule.getIntoProductId() : intoProductId);
					transBill.setReferUserId(referUserId2);
					transBill.setReferRoleCode(rule.getReferRoleCode());
					transBill.setReferProductId(rule.getReferProductId());
					transBill.setReferUserId2(referUserId2);
					transBill.setReferRoleCode2(rule.getReferRoleCode2());
					transBill.setReferProductId2(rule.getReferProductId2());
					transBill.setSettleObject(rule.getSettleObject());
					transBill.setSettleType(rule.getSettleType());
					transBill.setBillAmount(longProfitAmount);
					transBill.setBillType(3);
					transBill.setStatusId(0);
					transBill.setRemark("尚未结清,未处理!");
					transBill.setAccountDate(settleTime);
					transBill.setObligate1(rule.getProfitDetailId()+","+rule.getSubId());
					
					transBillTmpList.add(transBill);
				}
			}
			
			transProfitList.addAll(transProfitTmpList);
			transBillList.addAll(transBillTmpList);
			message = "成功: 此条交易 [ID:"+loanId+"] 分润成功 !";
			logger.info(">>> " + message);
			//存入分润'成功'的交易信息IDList
			successProTrDetIdList.add(loanId);
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
		 * 更新DB中分润相关 '分润结果' SETTLE_TRANS_PROFIT
		 */
		this.insertAndUpdateSettleTransBill(transBillList);
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
	@SuppressWarnings("rawtypes")
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
		SimpleDateFormat formatDate = new SimpleDateFormat("yyyyMMddhhmmssSSS");
		SimpleDateFormat formatPara = new SimpleDateFormat("YYYY-MM-DD HH:MM:SS");
		/*
		 * 获取分润结果
		 */
		Calendar colDate = Calendar.getInstance();
		colDate.set(Calendar.HOUR_OF_DAY, 0);
		colDate.set(Calendar.MINUTE, 0);
		colDate.set(Calendar.SECOND, 0);
		try {
			if(settleTransProfitList == null) {
				settleTransProfitList = this.getTransProfitWithUnbalance(colDate.getTime());
			}
			if(settleTransProfitList.size() < 1) {
				String msg = "获取分润结果信息 0条";
				logger.error(">>> " + msg);
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
			billList = getTransBillByUntreated(colDate.getTime());
			if(billList == null || billList.size() < 1) {
				logger.info(">>> >>> >>> 获取所有未处理挂账单 0 条!");
			}
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 获取所有未处理挂账单");
			e.printStackTrace();
		}

		/*
		 * 遍历分润结果信息
		 */
		Map<String,SettleTransProfit> editMapBean = new HashMap<String,SettleTransProfit>();
		Map<String,SettleTransProfit> editMapAmount = new HashMap<String,SettleTransProfit>();
		Map<String,String> editMapBill = new HashMap<String,String>();
		Map<String,String> editMapId = new HashMap<String,String>();
		Map<String,String> editMapAmtId = new HashMap<String,String>();
		Iterator<SettleTransProfit> iter = settleTransProfitList.iterator();
		SettleTransProfit editBean = new SettleTransProfit();
		BigDecimal addAmount = null;
		BigDecimal priAmount = null;
		String profitId = "";
		while(iter.hasNext()) {
			//当前分润结果信息
			SettleTransProfit item = iter.next();
			//分润金额
			Long paymentamount = item.getProfitAmount();
			//转出方ID
			String userId = item.getUserId();
			//转入方ID
			String interUserId = item.getInterUserId();
			//机构编码
			String merchantId = item.getRootInstCd();
			//转出方产品号
			String productId = item.getProductId();
			//转入方产品号
			String intoProductId = item.getIntoProductId();
			//调用方法
			String api_info = item.getApiType();
			
			String editKey = merchantId + "|" + userId + "|" + productId 
					+ "|" + interUserId + "|" + intoProductId + "|" + item.getSettleMain();
			
			if (editMapBean.containsKey(editKey)) {
				editBean = editMapBean.get(editKey);
				priAmount = new BigDecimal(editBean.getProfitAmount());
				addAmount = new BigDecimal(paymentamount);
				priAmount = priAmount.add(addAmount);
				editBean.setProfitAmount(priAmount.longValue());
				profitId = editMapId.get(editKey);
				profitId = profitId + "," + String.valueOf(item.getTransProfitId());
				editMapId.put(editKey, profitId);
				editBean = new SettleTransProfit();
			} else {
				editMapId.put(editKey, String.valueOf(item.getTransProfitId()));
				editMapBean.put(editKey, item);
			}
		}
		
		/*
		 * 2.调用 账户系统 - 转账
		 */
		//转账交易实体Bean(类名重复)
		Map<String,String[]> paraMap = new HashMap<String,String[]>();
		Iterator entries = editMapBean.entrySet().iterator();
		CommonResponse commonResponse = null;
		String proId = "";
		String[] proIdArr = null;
		while (entries.hasNext()) {  
		  
		    Map.Entry entry = (Map.Entry) entries.next();
		    String key = (String)entry.getKey();
		    SettleTransProfit value = editMapBean.get(key);
		    Date newDate = new Date();
		    
		    paraMap.put("amount", new String[]{String.valueOf(value.getProfitAmount())});//转账金额
		    paraMap.put("userid", new String[]{value.getUserId()});//出钱方
		    paraMap.put("funccode", new String[]{"3001"});//'转账'功能编码
		    paraMap.put("intermerchantcode", new String[]{value.getInterUserId()});//入钱方
		    paraMap.put("merchantcode", new String[]{value.getRootInstCd()});//入钱方
		    paraMap.put("ordercount", new String[]{"1"});
		    paraMap.put("orderdate", new String[]{formatPara.format(newDate)});
		    paraMap.put("orderno", new String[]{"S" + formatDate.format(newDate)});
		    paraMap.put("status", new String[]{"1"});
		    paraMap.put("userfee", new String[]{"0"});
		    paraMap.put("productid", new String[]{value.getProductId()});
		    paraMap.put("intoproductid", new String[]{value.getIntoProductId()});
		    paraMap.put("useripaddress", new String[]{value.getUserIpAddress()});
		    paraMap.put("requesttime", new String[]{formatPara.format(newDate)});
		    paraMap.put("remark", new String[]{"清算分润"});

		    proId = editMapId.get(key);
		    proIdArr = proId.split(",");
		    
			try {
				/*
				 * 执行转账操作
				 */
				logger.info(">>> 执行分润的转账操作!");
				commonResponse = paymentAccountServiceApi.transferInCommon(paraMap);
				if (commonResponse == null || !"1".equals(commonResponse.code)) {
					logger.info("失败: 转账结束，：["+value.getUserId()+"|" + value.getProductId() + "]TO["+ value.getInterUserId() +"|" + value.getIntoProductId() + "]金额["+value.getProfitAmount()+", 账户操作失败！返回结果为["+commonResponse.msg);
					failIdList = new ArrayList<Integer>();
					for (String id : proIdArr) {
						failIdList.add(Integer.parseInt(id));
					}
					String msg = commonResponse.msg;
					if (commonResponse.msg.length() > 250) {
						msg = commonResponse.msg.substring(0, 250);
					}
					if(failIdList.size() > 0) this.updateFailTransProfitStatus(failIdList,msg);
				} else if ("1".equals(commonResponse.code)) {
					logger.info("成功: 转账结束，：["+value.getUserId()+"|" + value.getProductId() + "]TO["+ value.getInterUserId() +"|" + value.getIntoProductId() + "]金额["+value.getProfitAmount()+", 账户操作成功！返回结果为["+commonResponse.msg);
//					if (value.getSettleObject() != null || "".equals(value.getSettleObject())) {
//						String keys = "";
//						if ("USER_ID".equals(value.getSettleObject())) {
//							keys = value.getRootInstCd()+"|" + value.getUserId()+"|" + value.getProductId() + "|" + value.getSettleMain();
//						} else if ("INTER_USER_ID".equals(value.getSettleObject())) {
//							keys = value.getRootInstCd()+"|" + value.getInterUserId() +"|" + value.getIntoProductId() + "|" + value.getSettleMain();
//						}
//						if (!"".equals(keys)) {
//							if (editMapAmount.containsKey(keys)) {
//								editBean = editMapAmount.get(keys);
//								priAmount = new BigDecimal(editBean.getProfitAmount());
//								addAmount = new BigDecimal(value.getProfitAmount());
//								priAmount = priAmount.add(addAmount);
//								editBean.setProfitAmount(priAmount.longValue());
//								profitId = editMapAmtId.get(keys);
//								profitId = profitId + "," + String.valueOf(value.getTransProfitId());
//								editMapId.put(keys, profitId);
//								editBean = new SettleTransProfit();
//							} else {
//								editMapAmount.put(keys, value);
//								editMapAmtId.put(keys, String.valueOf(value.getTransProfitId()));
//							}
//						}
//					}
					idList = new ArrayList<Integer>();
					for (String id : proIdArr) {
						idList.add(Integer.parseInt(id));
					}
					if(idList.size() > 0) this.updateTransProfitStatus(idList,"分润结果, 结算成功!",1,null);
				}
			} catch (Exception e) {
				logger.info("异常: 转账结束，：["+value.getUserId()+"|" + value.getProductId() + "]TO["+ value.getInterUserId() +"|" + value.getIntoProductId() + "]金额["+value.getProfitAmount()+", 账户操作异常！");
				e.printStackTrace();
				for (String id : proIdArr) {
					failIdList.add(Integer.parseInt(id));
				}
				if(failIdList.size() > 0) this.updateFailTransProfitStatus(failIdList,commonResponse.msg.substring(0,250));
				continue;
			}
		}
		
		if(billList != null && billList.size() > 0) this.doBillBalance(billList);

		
		if(resultMap.size() < 1) this.editResultMap(resultMap, "1", "调用'账户系统'结算全都成功!!");
		return resultMap;
	}
	/***
	 * 结算[生成代付数据]
	 * @param userId 转账用户ID
	 */
	public Map<String, Object> doSettleInvoice(List<SettleTransProfit> settleTransProfitList) {
		logger.info(">>> >>> >>> >>> 进入 结算[生成代付数据]");

		//提示信息
		Map<String, Object> resultMap = new HashMap<String, Object>();
		SimpleDateFormat formatDate = new SimpleDateFormat("yyyyMMddhhmmssSSS");

		//挂账[交易后退款]交易List
		List<SettleTransBill> billList = null;
		Calendar colDate = Calendar.getInstance();
		colDate.set(Calendar.HOUR_OF_DAY, 0);
		colDate.set(Calendar.MINUTE, 0);
		colDate.set(Calendar.SECOND, 0);
		try {
			if(settleTransProfitList == null) {
				settleTransProfitList = this.getTransProfitWithUnsettle(colDate.getTime());
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
			billList = getTransBillByUnsettle(colDate.getTime());
			if(billList == null || billList.size() < 1) {
				logger.info(">>> >>> >>> 获取所有未处理挂账单 0 条!");
			}
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 获取所有未处理挂账单");
			e.printStackTrace();
		}
		Iterator<SettleTransProfit> iter = settleTransProfitList.iterator();
		SettleTransProfit editBean = new SettleTransProfit();
		BigDecimal addAmount = null;
		BigDecimal priAmount = null;
		BigDecimal midAmount = null;
		BigDecimal subAmount = null;
		String profitId = "";
		Map<String,SettleTransProfit> editMapAmount = new HashMap<String,SettleTransProfit>(); 
		Map<String,String> editMapAmtId = new HashMap<String,String>(); 
		while(iter.hasNext()) {
			//当前分润结果信息
			SettleTransProfit item = iter.next();
			
			if (null !=item.getSettleObject() && !"".equals(item.getSettleObject())) {
				String keys = "";
				if ("USER_ID".equals(item.getSettleObject())) {
					keys = item.getRootInstCd()+"|" + item.getUserId()+"|" + item.getProductId() + "|" + item.getSettleMain();
				} else if ("INTER_USER_ID".equals(item.getSettleObject())) {
					keys = item.getRootInstCd()+"|" + item.getInterUserId() +"|" + item.getIntoProductId() + "|" + item.getSettleMain();
				}
				if (!"".equals(keys)) {
					if (editMapAmount.containsKey(keys)) {
						editBean = editMapAmount.get(keys);
						priAmount = new BigDecimal(editBean.getProfitAmount());
						addAmount = new BigDecimal(item.getProfitAmount());
						priAmount = priAmount.add(addAmount);
						editBean.setProfitAmount(priAmount.longValue());
						profitId = editMapAmtId.get(keys);
						profitId = profitId + "," + String.valueOf(item.getTransProfitId());
						editMapAmtId.put(keys, profitId);
						editBean = new SettleTransProfit();
					} else {
						editMapAmount.put(keys, item);
						editMapAmtId.put(keys, String.valueOf(item.getTransProfitId()));
					}
				}
			}
		}

		Iterator<SettleTransBill> iterBill = billList.iterator();
		SettleTransProfit settleTransProfit = null;
		Map<String,List<Integer>> billDoList = new HashMap<String,List<Integer>>();
		List<Integer> tmpList = new ArrayList<Integer>();
		while(iterBill.hasNext()) {
			//当前分润结果信息
			SettleTransBill bill = iterBill.next();
			String billKey1 = bill.getRootInstCd() + "|" + bill.getUserId() + "|" + bill.getProductId();
			String billKey2 = bill.getRootInstCd() + "|" + bill.getInterUserId() + "|" + bill.getIntoProductId();
			Iterator entries = editMapAmount.entrySet().iterator();
			Long logicAmonut = Long.parseLong(bill.getObligate2());
			while (entries.hasNext()) {  
			    settleTransProfit = new SettleTransProfit();
			    Map.Entry entry = (Map.Entry) entries.next();
			    tmpList = new ArrayList<Integer>();
			    String key = (String)entry.getKey();
			    settleTransProfit = editMapAmount.get(key);
			    String[] kayArr = key.split("\\|");
			    String key2 = kayArr[0] + "|" + kayArr[1] + "|" +kayArr[2];
			    if (key2.compareTo(billKey2) == 0) {
			    	priAmount = new BigDecimal(settleTransProfit.getProfitAmount());
			    	subAmount = new BigDecimal(logicAmonut);
			    	midAmount = priAmount.add(subAmount);
			    	settleTransProfit.setProfitAmount(midAmount.longValue());
			    	if (billDoList.containsKey(key)) {
			    		tmpList = billDoList.get(key);
			    		tmpList.add(bill.getTransBillId());
			    	} else {
			    		tmpList.add(bill.getTransBillId());
			    		billDoList.put(key, tmpList);
			    	}
			    	break;
			    }
			    if (key2.compareTo(billKey1) == 0) {
			    	priAmount = new BigDecimal(settleTransProfit.getProfitAmount());
			    	subAmount = new BigDecimal(logicAmonut);
			    	midAmount = priAmount.subtract(subAmount);
			    	if (midAmount.intValue() ==0) {
			    		settleTransProfit.setProfitAmount(0l);
				    	if (billDoList.containsKey(key)) {
				    		tmpList = billDoList.get(key);
				    		tmpList.add(bill.getTransBillId());
				    	} else {
				    		tmpList.add(bill.getTransBillId());
				    		billDoList.put(key, tmpList);
				    	}
			    		break;
			    	} else if (midAmount.intValue() < 0) {
			    		settleTransProfit.setProfitAmount(0l);
			    		logicAmonut = subAmount.subtract(priAmount).longValue();
			    	} else {
			    		settleTransProfit.setProfitAmount(midAmount.longValue());
				    	if (billDoList.containsKey(key)) {
				    		tmpList = billDoList.get(key);
				    		tmpList.add(bill.getTransBillId());
				    	} else {
				    		tmpList.add(bill.getTransBillId());
				    		billDoList.put(key, tmpList);
				    	}
			    		break;
			    	}
			    	if (billDoList.containsKey(key)) {
			    		tmpList = billDoList.get(key);
			    		tmpList.add(bill.getTransBillId());
			    	} else {
			    		tmpList.add(bill.getTransBillId());
			    		billDoList.put(key, tmpList);
			    	}
			    }
			    settleTransProfit = new SettleTransProfit();
			}
		}
		
		
		Iterator entries = editMapAmount.entrySet().iterator();
		SettleProfitInvoice settleProfitInvoice = new SettleProfitInvoice();
		SettleProfitInvoiceQuery query = new SettleProfitInvoiceQuery();
		List<SettleProfitInvoice> resultList = null;
		List<Integer> idList = null;
		String proId = null;
		String[] proIdArr = null;
		colDate.add(Calendar.DATE, -1);
		while (entries.hasNext()) {  
		    Map.Entry entry = (Map.Entry) entries.next();
		    String key = (String)entry.getKey();
		    settleTransProfit = editMapAmount.get(key);
		    String orderNo = "SD" + formatDate.format(new Date());
		    
		    if (settleTransProfit.getSettleObject() != null || !"".equals(settleTransProfit.getSettleObject())) {
		    	if (settleTransProfit.getProfitAmount() > 0) {
//			    	settleProfitInvoice.setRequestNo();
			    	settleProfitInvoice.setAmount(settleTransProfit.getProfitAmount());
					if ("USER_ID".equals(settleTransProfit.getSettleObject())) {
				    	settleProfitInvoice.setUserId(settleTransProfit.getUserId());
				    	settleProfitInvoice.setInterMerchantCode(settleTransProfit.getUserId());
				    	settleProfitInvoice.setProductId(settleTransProfit.getProductId());
					} else if ("INTER_USER_ID".equals(settleTransProfit.getSettleObject())) {
				    	settleProfitInvoice.setUserId(settleTransProfit.getInterUserId());
				    	settleProfitInvoice.setInterMerchantCode(settleTransProfit.getInterUserId());
				    	settleProfitInvoice.setProductId(settleTransProfit.getIntoProductId());
					}
			    	settleProfitInvoice.setFuncCode(settleTransProfit.getSettleMain());
			    	settleProfitInvoice.setMerchantCode(settleTransProfit.getRootInstCd());
			    	settleProfitInvoice.setOrderAmount(settleTransProfit.getProfitAmount());
			    	settleProfitInvoice.setOrderCount(1);
			    	settleProfitInvoice.setOrderDate(colDate.getTime());
			    	settleProfitInvoice.setOrderNo(orderNo);
			    	settleProfitInvoice.setRequestTime(new Date());
			    	settleProfitInvoice.setStatus(0);
			    	settleProfitInvoice.setCashStatus(0);
			    	settleProfitInvoice.setSettleType(settleTransProfit.getSettleType());;
			    	settleProfitInvoice.setUserFee(0l);
			    	settleProfitInvoice.setUserIpAddress("168.0.0.1");
			    	settleProfitInvoice.setReturnMsg("尚未结算");
			    	
			    	settleProfitInvoiceManager.saveSettleProfitInvoice(settleProfitInvoice);

			    }
		    	proId = editMapAmtId.get(key);
			    proIdArr = proId.split(",");
				idList = new ArrayList<Integer>();
				for (String id : proIdArr) {
					idList.add(Integer.parseInt(id));
				}
				if(idList.size() > 0) this.updateTransProfitStatus(idList,"已生成支付交易！",3,orderNo);

				if (billDoList.size()>0) {
					tmpList = new ArrayList<Integer>();
					Iterator entries2 = billDoList.entrySet().iterator();
					while (entries2.hasNext()) {  
						SettleTransBill bill = new SettleTransBill();
					    Map.Entry entry2 = (Map.Entry) entries2.next();
					    String key2 = (String)entry2.getKey();
					    if (key.compareTo(key2) == 0) { 
					    	tmpList = billDoList.get(key2);
					    	for (Integer tmpId : tmpList) {
							    bill.setTransBillId(tmpId);
							    bill.setStatusId(3);
							    bill.setRemark("已生成支付交易！");
							    bill.setBatchNo(orderNo);
								settleTransBillManager.updateSettleTransBill(bill);
					    	}
					    }
					}
				}
		    }
		}
		if(resultMap.size() < 1) this.editResultMap(resultMap, "1", "生成代付交易数据全都成功!!");
		return resultMap;
	}

	/***
	 * 结算[生成代付交易]
	 * @param userId 转账用户ID
	 */
	public Map<String, Object>  doInvoiceSettle(List<SettleProfitInvoice> settleProfitInvoiceList) {
		logger.info(">>> >>> >>> >>> 进入 结算[生成代付交易]");
		//提示信息
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Calendar colDate = Calendar.getInstance();
		colDate.set(Calendar.HOUR_OF_DAY, 0);
		colDate.set(Calendar.MINUTE, 0);
		colDate.set(Calendar.SECOND, 0);
		try {
			if(settleProfitInvoiceList == null) {
				settleProfitInvoiceList = this.getTransProfitInvoice(colDate.getTime());
			}
			if(settleProfitInvoiceList.size() < 1) {
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
		
		Iterator<SettleProfitInvoice> iter = settleProfitInvoiceList.iterator();

		Map<String,String> resMap = null;
		Map<String,Object> resMap2 = null;
		while(iter.hasNext()) {
			//当前分润结果信息
			SettleProfitInvoice item = iter.next();
			TransOrderInfo transorderinfo = new TransOrderInfo();
			transorderinfo.setAmount(item.getAmount());
			transorderinfo.setUserId(item.getUserId());
			transorderinfo.setFuncCode(item.getFuncCode());
			transorderinfo.setInterMerchantCode(item.getInterMerchantCode());
			transorderinfo.setMerchantCode(item.getMerchantCode());
			transorderinfo.setOrderAmount(item.getOrderAmount());
			transorderinfo.setOrderCount(item.getOrderCount());
			transorderinfo.setOrderDate(item.getOrderDate());
			transorderinfo.setOrderNo(item.getOrderNo());
			transorderinfo.setErrorCode(item.getProductId());
			transorderinfo.setRequestTime(item.getRequestTime());
			transorderinfo.setStatus(1);
			transorderinfo.setUserFee(item.getUserFee());
			transorderinfo.setUserIpAddress(item.getUserIpAddress());
			transorderinfo.setRemark("分润结算");
			transorderinfo.setPayChannelId(item.getPayChannelId());
			
			resMap = new HashMap<String,String>();
			if ("4016".equals(item.getFuncCode())) {//调用账户提现接口
				resMap = this.toAccWithdrow(transorderinfo);
			} else if ("4014".equals(item.getFuncCode()) || "40144".equals(item.getFuncCode())) {//调用账户代付接口
				resMap = this.toAccWithhold(transorderinfo);
			} else if (TransCodeConst.CHECK_THE_BALANCE_SUBTRACT.equals(item.getFuncCode())) {//调用账户调账接口
				try {
					//调用账户调账接口
					resMap2 = super.changeAccount(
							transorderinfo.getMerchantCode()
							, transorderinfo.getUserId()
							, transorderinfo.getErrorCode()
							, transorderinfo.getAmount()
							, transorderinfo.getFuncCode()
							, transorderinfo.getOrderNo()
							, "POS收单调账");//调账减
				} catch (Exception e) {
					logger.error(">>> >>> >>> 异常:调用账户调账接口", e);
					e.printStackTrace();
					resMap.put("code", "false");
					resMap.put("msg", "异常:调用账户调账接口" + e.getMessage());
				}
				if("1".equals(String.valueOf(resMap2.get("code")))) {
					resMap.put("code", "true");
					resMap.put("msg", "调用账户调账成功");
				} else {
					resMap.put("code", "false");
					resMap.put("msg", String.valueOf(resMap2.get("msg")));
				}
			} else {
				resMap.put("code", "false");
				resMap.put("msg", "未知FuncCode");
			}

			if ("true".equals(resMap.get("code"))) {
				item.setStatus(1);
				item.setReturnMsg("已经生成支付交易");
				settleProfitInvoiceManager.updateSettleProfitInvoice(item);
			} else {
				item.setStatus(0);
				item.setReturnMsg(resMap.get("msg"));
				settleProfitInvoiceManager.updateSettleProfitInvoice(item);
			}
		}
		if(resultMap.size() < 1) this.editResultMap(resultMap, "1", "生成代付交易全都成功!!");
		return resultMap;
	}
	/***
	 * 结算[头寸代付交易]
	 * @param userId 转账用户ID
	 */
	public Map<String, Object>  doInvoiceSettleForCash(List<SettleProfitInvoice> settleProfitInvoiceList) {
		logger.info(">>> >>> >>> >>> 进入 结算[头寸]代付金额");
		//提示信息
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Calendar colDate = Calendar.getInstance();
		colDate.set(Calendar.HOUR_OF_DAY, 0);
		colDate.set(Calendar.MINUTE, 0);
		colDate.set(Calendar.SECOND, 0);
		try {
			if(settleProfitInvoiceList == null) {
				settleProfitInvoiceList = this.getTransProfitInvoiceForCash(colDate.getTime());
			}
			if(settleProfitInvoiceList.size() < 1) {
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
		
		Iterator<SettleProfitInvoice> iter = settleProfitInvoiceList.iterator();

		Map<String,String> resMap = null;
		Map<String,String> cashMap = new HashMap<String,String>();
		Map<String,String> cashIdMap = new HashMap<String,String>();
		String cashKey = "";
		String invoiceId = "";
		String[] invoiceIdArr = null;
		BigDecimal priAmount = null;
		BigDecimal addAmount = null;
		logger.info(">>> 按机构+交易类型汇总，头寸交易");
		while(iter.hasNext()) {
			//当前分润结果信息
			SettleProfitInvoice item = iter.next();
			if (item.getStatus() == 0 || item.getStatus() == 1) {
				cashKey = item.getMerchantCode() + item.getFuncCode();
				if (cashMap.containsKey(cashKey)) {
					priAmount = new BigDecimal(cashMap.get(cashKey));
					addAmount = new BigDecimal(item.getAmount());
					priAmount = priAmount.add(addAmount);
					cashMap.put(cashKey, priAmount.toString());
					invoiceId = cashIdMap.get(cashKey);
					invoiceId = invoiceId + "," + String.valueOf(item.getProfitInvoiceId());
					cashIdMap.put(cashKey, invoiceId);
				} else {
					cashMap.put(cashKey, String.valueOf(item.getAmount()));
					cashIdMap.put(cashKey, String.valueOf(item.getProfitInvoiceId()));
				}
			}
		}
		logger.info(">>> 根据汇总结果汇总，发起头寸交易");
		Iterator entries = cashMap.entrySet().iterator();
		SimpleDateFormat formatDate = new SimpleDateFormat("yyyyMMddhhmmssSSS");
		Date newDate = new Date();
		SettleProfitInvoice updata = new SettleProfitInvoice();
		while (entries.hasNext()) {  
		    Map.Entry entry = (Map.Entry) entries.next();
		    String key = (String)entry.getKey();
		    /*
		     * 课栈和旅游分期有头寸操作
		     * 既是  课栈和旅游分期的生成的代付交易 在生成代付交易之后, 还要做头寸操作.
		     * 出钱方: 通联丰年协议
		     * 入钱方: 民生
		     */
		    if (key.compareTo("M00000440144") == 0 || key.compareTo("M00002740144") == 0) {
		    	TransOrderInfo transorderinfo = new TransOrderInfo();
				transorderinfo.setAmount(Long.parseLong(cashMap.get(key)));
				transorderinfo.setUserId("141223100000001");
				transorderinfo.setFuncCode("4014");
				transorderinfo.setInterMerchantCode("14623543518818651");
				transorderinfo.setMerchantCode("M000001");
				transorderinfo.setOrderAmount(Long.parseLong(cashMap.get(key)));
				transorderinfo.setOrderCount(1);
				transorderinfo.setOrderDate(newDate);
				transorderinfo.setOrderNo("SC" + formatDate.format(newDate));
				transorderinfo.setErrorCode("P000002");
				transorderinfo.setRequestTime(newDate);
				transorderinfo.setStatus(1);
				transorderinfo.setUserFee(0l);
				transorderinfo.setUserIpAddress("168.0.0.1");
				transorderinfo.setRemark("清算头寸");
				
				resMap = this.toAccWithhold(transorderinfo);
				
				if ("true".equals(resMap.get("code"))) {
					invoiceId = cashIdMap.get(key);
					invoiceIdArr = invoiceId.split(",");
					for (String id : invoiceIdArr) {
						updata.setProfitInvoiceId(Long.parseLong(id));
						updata.setCashStatus(1);
						updata.setReturnMsg("已经生成头寸交易");
						settleProfitInvoiceManager.updateSettleProfitInvoice(updata);
					}
				} else {
					invoiceId = cashIdMap.get(key);
					invoiceIdArr = invoiceId.split(",");
					for (String id : invoiceIdArr) {
						updata.setProfitInvoiceId(Long.parseLong(id));
						updata.setCashStatus(0);
						updata.setReturnMsg("生成头寸交易失败");
						settleProfitInvoiceManager.updateSettleProfitInvoice(updata);
					}
				}
		    }
		}
		
		if(resultMap.size() < 1) this.editResultMap(resultMap, "1", "生成代付交易全都成功!!");
		return resultMap;
	}
	/***
	 * 结算[消费后退款]挂账金额
	 * @param userId 转账用户ID
	 */
	private void doBillBalance(List<SettleTransBill> billList) {
		logger.info(">>> >>> >>> >>> 进入 结算[消费后退款]挂账金额");
		//需要添加的新挂账信息
		List<SettleTransBill> newBillList = new ArrayList<SettleTransBill>();
		SimpleDateFormat formatDate = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		SimpleDateFormat formatPara = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		/*
		 *	1.调用 账户系统 - 查询账户余额接口
		 */
		User user = new User();
		String paramString = "";

		Map<String,String[]> paraMap = new HashMap<String,String[]>();
		CommonResponse commonResponse = null;
		Iterator<SettleTransBill> transBillIter = billList.iterator();
		BigDecimal midAmount = null;
		BigDecimal subAmount = null;
		BigDecimal priAmount = null;
		SettleTransProfit settleTransProfit = null;
		while(transBillIter.hasNext()) {
			//交易后退款实体Bean
			SettleTransBill bill = transBillIter.next();
			//调用接口返回Balance实体
			Balance balance = null;
			try {
				user.userId = bill.getUserId();
				user.productId = bill.getProductId();
				user.constId = bill.getRootInstCd();
				balance = payAccSerApi.getBalance(user, paramString);
				if(balance == null) {
					logger.error("调用账户接口获取余额失败!返回结果为null [USER_ID:"+ bill.getUserId() +", PRODUCT_ID:"+ bill.getProductId() +", CONST_ID:"+ bill.getRootInstCd() +"]");
					bill.setStatusId(0);
					bill.setRemark("调用账户接口获取余额失败!");
					settleTransBillManager.updateSettleTransBill(bill);
					continue;
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("调用账户接口获取余额失败!查询余额方法异常 [USER_ID:"+ bill.getUserId() +", PRODUCT_ID:"+ bill.getProductId() +", CONST_ID:"+ bill.getRootInstCd() +"]");
				bill.setStatusId(0);
				bill.setRemark("调用账户接口获取余额异常!");
				settleTransBillManager.updateSettleTransBill(bill);
				continue;
			}
			//账户提现余额
			Long amountBalance = balance.getBalanceSettle();
			//判断余额是否为0
			if(amountBalance <= 0) {
				logger.error("此用户余额为0, 不进行转账操作! [USER_ID:"+  bill.getUserId() +", PRODUCT_ID:"+ bill.getProductId() +", CONST_ID:"+ bill.getRootInstCd() +"]");
				bill.setStatusId(0);
				bill.setRemark("此用户余额为0, 不进行转账操作!");
				settleTransBillManager.updateSettleTransBill(bill);
				continue;
			}
			logger.info(">>> >>> 调用账户接口获取余额:" + amountBalance);
			
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
			//当前日期
			Date newDate = new Date();
			//备注
//String remark = "清算发起 的[交易后退款]转账";
			String remark = "分润清算";
			/*
			 * 2.调用 账户系统 - 转账
			 */
			paraMap.put("amount", new String[]{String.valueOf(transferAmount)});//转账金额
		    paraMap.put("userid", new String[]{bill.getUserId()});//出钱方
		    paraMap.put("funccode", new String[]{"3001"});//'转账'功能编码
		    paraMap.put("intermerchantcode", new String[]{bill.getInterUserId()});//入钱方
		    paraMap.put("merchantcode", new String[]{bill.getRootInstCd()});//入钱方
		    paraMap.put("ordercount", new String[]{"1"});
		    paraMap.put("orderdate", new String[]{formatPara.format(newDate)});
		    paraMap.put("orderno", new String[]{"SBI" + formatDate.format(newDate)});
		    paraMap.put("status", new String[]{"1"});
		    paraMap.put("userfee", new String[]{"0"});
		    paraMap.put("productid", new String[]{bill.getProductId()});
		    paraMap.put("intoproductid", new String[]{bill.getIntoProductId()});
		    paraMap.put("useripaddress", new String[]{"168.0.0.1"});
		    paraMap.put("requesttime", new String[]{formatPara.format(newDate)});
		    paraMap.put("remark", new String[]{remark});
		    
			try {
				/*
				 * 执行转账操作
				 */
				logger.info(">>> 执行交易后退款的转账操作!");
				commonResponse = paymentAccountServiceApi.transferInCommon(paraMap);
				if (commonResponse == null || !"1".equals(commonResponse.code)) {
					logger.info("失败: 转账结束，：["+bill.getUserId()+"|" + bill.getProductId() + "]TO["+ bill.getInterUserId() +"|" + bill.getIntoProductId() + "]金额["+transferAmount+", 账户操作失败！返回结果为["+commonResponse.msg);
				} else if ("1".equals(commonResponse.code)) {
					logger.info("成功: 转账结束，：["+bill.getUserId()+"|" + bill.getProductId() + "]TO["+ bill.getInterUserId() +"|" + bill.getIntoProductId() + "]金额["+transferAmount+", 账户操作成功！返回结果为["+commonResponse.msg);
				}
			} catch (Exception e) {
				logger.info("异常: 转账结束，：["+bill.getUserId()+"|" + bill.getProductId() + "]TO["+ bill.getInterUserId() +"|" + bill.getIntoProductId() + "]金额["+transferAmount+", 账户操作异常！");
				e.printStackTrace();
			}
			try {
				//更新老的退款信息
				if("1".equals(commonResponse.code) && oldBill != null) {
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
				if("1".equals(commonResponse.code) && newBill != null) {
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
		bill.setObligate2(transferAmount.toString());
		
		resultMap.put("transferAmount", transferAmount);
		resultMap.put("newBill", newBill);
		resultMap.put("oldBill", bill);
		return resultMap;
	}
	/***
	 * 获取所有未清分交易信息
	 * @param statusIds 交易类型状态的Integer数组
	 * @return
	 */
	public List<Map<String, Object>> getProfitTransDetail(Integer[] statusIds) {
		logger.info(">>> >>> >>> 获取所有未清分交易信息 ... ...");
		Map<String, Object> map = new HashMap<String, Object>();
		//订单类型0交易,1结算单, 分润就分账户的交易
	    Date beginAccDate = new Date();
	    Date endAccDate = new Date();
	    beginAccDate.setTime(beginAccDate.getTime() - 20 * 24 * 60 * 60 * 1000L);
	    endAccDate.setTime(endAccDate.getTime() + 24 * 60 * 60 * 1000L);
	    map.put("beginAccDate", beginAccDate);
	    map.put("endAccDate", endAccDate);
	    map.put("statusIds", statusIds);
		//查询清分交易信息
	    List<Map<String, Object>> profitTransDetailList = this.settleLoanDetailManager.selectProfitTransInfo(map);
	    return profitTransDetailList;
	}
	/***
	 * 更新DB中分润相关'交易结果' SETTLE_BALANCE_ENTRY
	 * SETTLE_BALANCE_ENTRY 	交易结果
	 * @param transProfitList
	 */
	private void insertAndUpdateSettleTransProfit(List<SettleTransProfit> transProfitList) {
		logger.info(">>> >>> >>> 更新 分润结果信息 ... ..."+transProfitList.size());
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
				query.setObligate1(subbean.getObligate1());
				//通过对账key和订单号查询对账结构
				resList = settleTransProfitManager.queryList(query);
				if (resList != null && resList.size() > 0) {//有此结果
					if (resList.get(0).getStatusId() == 0) {
						//修改原分润结果
						subbean.setTransProfitId(resList.get(0).getTransProfitId());
						settleTransProfitManager.updateSettleTransProfit(subbean);
					}
				} else {//无此结果
					//添加新分润结果
					settleTransProfitManager.saveSettleTransProfit(subbean);
				}
			}
		}
	}
	/***
	 * 更新DB中分润相关'交易结果' SETTLE_BALANCE_ENTRY
	 * SETTLE_BALANCE_ENTRY 	交易结果
	 * @param transProfitList
	 */
	private void insertAndUpdateSettleTransBill(List<SettleTransBill> transBillList) {
		logger.info(">>> >>> >>> 更新 挂账结果信息 ... ..."+transBillList.size());
		//分润信息查询query对象
		SettleTransBillQuery query = null;
		//分润信息结果
		List<SettleTransBill> resList = null;
		if (transBillList.size() > 0) {
			for (SettleTransBill subbean : transBillList) {
				query = new SettleTransBillQuery();
				query.setOrderNo(subbean.getOrderNo());
				query.setRootInstCd(subbean.getRootInstCd());
				query.setUserId(subbean.getUserId());
				query.setProductId(subbean.getProductId());
				query.setObligate1(subbean.getObligate1());
				//通过对账key和订单号查询对账结构
				resList = settleTransBillManager.queryList(query);
				if (resList != null && resList.size() > 0) {//有此结果
					if (resList.get(0).getStatusId() == 0) {
						//修改原挂账结果
						subbean.setTransBillId(resList.get(0).getTransBillId());
						settleTransBillManager.updateSettleTransBill(subbean);
					}
				} else {//无此结果
					//添加新分润结果
					settleTransBillManager.saveSettleTransBill(subbean);
				}
			}
		}
	}
	/***
	 * 更新DB中分润相关'清算'交易信息
	 * SETTLE_TRANS_DETAIL 		交易信息明细
	 * @param updateTrDetIdMap
	 */
	private void updateTransDetailProfitStatus(Map<String, List<Long>> updateTrDetIdMap) {
		logger.info(">>> >>> >>> 更新 交易信息明细 ... ..."+updateTrDetIdMap.size());
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
			settleLoanDetailManager.updateTransStatusId(map);
		}
		if(successProTrDetIdList != null) {//分润成功, 交易状态:分润成功
			map.put("statusId", SettleConstants.STATUS_PROFIT_SUCCESS);
			map.put("idList", successProTrDetIdList);
			settleLoanDetailManager.updateTransStatusId(map);
		}
		if(failProTrDetIdList != null) {//分润失败, 交易状态:分润失败
			map.put("statusId", SettleConstants.STATUS_PROFIT_FALID);
			map.put("idList", failProTrDetIdList);
			settleLoanDetailManager.updateTransStatusId(map);
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
			transKey = String.valueOf(transDetailMap.get("ROOT_INST_CD")) + "," + String.valueOf(transDetailMap.get("PRODUCT_ID"));
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
			logger.error("未匹配到此条交易[ID:"+String.valueOf(transDetailMap.get("LOAN_ID"))+"]的'分润规则'!");
			throw new SettleException("未匹配到此条交易[ID:"+String.valueOf(transDetailMap.get("LOAN_ID"))+"]的'分润规则'!");
		}
		return theProfitKey;
	}
	/***
	 * 查询全部未结算的清分交易
	 * @return
	 */
	private List<SettleTransProfit> getTransProfitWithUnbalance(Date settleTime) {
		logger.info(">>> >>> >>> >>> 查询全部未结算的清分交易!");
		List<Integer> detailStatusIdList = new ArrayList<Integer>();
		detailStatusIdList.add(11);//分润成功
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("proStatusId", 0);
		queryMap.put("detailStatusIdList", detailStatusIdList);
		queryMap.put("settleTime", settleTime);
		return settleTransProfitManager.selectloanProfitWithUnbalance(queryMap);
	}
	/***
	 * 查询全部未结算的清分交易
	 * @return
	 */
	private List<SettleTransProfit> getTransProfitWithUnsettle(Date settleTime) {
		logger.info(">>> >>> >>> >>> 查询全部未生成代付的清分交易!");
		List<Integer> detailStatusIdList = new ArrayList<Integer>();
		detailStatusIdList.add(11);//分润成功
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("proStatusId", 1);
		queryMap.put("detailStatusIdList", detailStatusIdList);
		queryMap.put("settleTime", settleTime);
		queryMap.put("settleObjectFlg", "0");
		return settleTransProfitManager.selectloanProfitWithUnbalance(queryMap);
	}
	/***
	 * 查询全部未结算的清分交易
	 * @return
	 */
	private List<SettleProfitInvoice> getTransProfitInvoice(Date settleTime) {
		logger.info(">>> >>> >>> >>> 查询全部未生成代付交易的清分交易!");
		SettleProfitInvoiceQuery example = new SettleProfitInvoiceQuery();
		example.setStatus(0);
		example.setOrderDate(settleTime);
		return settleProfitInvoiceManager.selectInvoiceData(example);
	}
	/***
	 * 查询全部未头寸的清分交易
	 * @return
	 */
	private List<SettleProfitInvoice> getTransProfitInvoiceForCash(Date settleTime) {
		logger.info(">>> >>> >>> >>> 查询全部未生成代付交易的清分交易!");
		SettleProfitInvoiceQuery example = new SettleProfitInvoiceQuery();
		example.setCashStatus(0);
		example.setOrderDate(settleTime);
		return settleProfitInvoiceManager.selectInvoiceData(example);
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
	private void updateTransProfitStatus(List<Integer> idList , String remark,int statusId,String orderNo) {
		logger.info(">>> >>> >>> >>> 更新 分润结果表状态 为'已结算'");
		Map<String, Object> map = new HashMap<String, Object>();
		if (orderNo!=null&&!"".equals(orderNo)) {
			map.put("orderNo", orderNo);//结算单好好
		}
		map.put("statusId", statusId);//已结算
		map.put("remark", remark);//已结算
		map.put("idList", idList);
		settleTransProfitManager.updateTransStatusId(map);
	}
	/***
	 * 更新 失败分润结果
	 * @param idList
	 */
	private void updateFailTransProfitStatus(List<Integer> idList , String remark) {
		logger.info(">>> >>> >>> >>> 更新 分润结果表备注 为'结算失败'");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("statusId", 0);//已结算
		map.put("remark", remark);//未结算
		map.put("idList", idList);
		settleTransProfitManager.updateTransStatusId(map);
	}
	/***
	 * 获取所有未处理的挂账[交易后退款]信息
	 * @return
	 * @throws SettleException
	 */
	private List<SettleTransBill> getTransBillByUntreated(Date settleTime) throws SettleException {
		logger.info(">>> >>> >>> >>> 查询全部未结算的挂账交易!");
		List<Integer> detailStatusIdList = new ArrayList<Integer>();
		detailStatusIdList.add(11);//分润成功
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("proStatusId", 0);
		queryMap.put("detailStatusIdList", detailStatusIdList);
		queryMap.put("settleTime", settleTime);
		queryMap.put("proBillType", "3");
		return settleTransBillManager.queryloanByCreatedTime(queryMap);
	}
	/***
	 * 获取所有未处理的挂账[交易后退款]信息
	 * @return
	 * @throws SettleException
	 */
	private List<SettleTransBill> getTransBillByUnsettle(Date settleTime) throws SettleException {
		logger.info(">>> >>> >>> >>> 查询全部未结算的挂账交易!");
		List<Integer> detailStatusIdList = new ArrayList<Integer>();
		detailStatusIdList.add(11);//分润成功
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("proStatusId", 1);
		queryMap.put("detailStatusIdList", detailStatusIdList);
		queryMap.put("settleTime", settleTime);
		queryMap.put("proBillType", "3");
		return settleTransBillManager.queryloanByCreatedTime(queryMap);
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
		String roleCode = oldBill.getRoleCode();
		String interUserId = oldBill.getInterUserId();
		String interRoleCode = oldBill.getInterRoleCode();
		String intoProductId = oldBill.getIntoProductId();
		String referUserId = oldBill.getReferUserId();
		String referRoleCode = oldBill.getReferRoleCode();
		String referProductId = oldBill.getReferProductId();
		String referUserId2 = oldBill.getReferUserId2();
		String referRoleCode2 = oldBill.getReferRoleCode2();
		String referProductId2 = oldBill.getReferProductId2();
		String settleObject = oldBill.getSettleObject();
		String settleType = oldBill.getSettleType();
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
		newBill.setRoleCode(roleCode);
		newBill.setInterUserId(interUserId);
		newBill.setInterRoleCode(interRoleCode);
		newBill.setIntoProductId(intoProductId);
		newBill.setReferUserId(referUserId);
		newBill.setReferRoleCode(referRoleCode);
		newBill.setReferProductId(referProductId);
		newBill.setReferUserId2(referUserId2);
		newBill.setReferRoleCode2(referRoleCode2);
		newBill.setReferProductId2(referProductId2);
		newBill.setSettleObject(settleObject);
		newBill.setSettleType(settleType);
		newBill.setBillType(billType);
		newBill.setObligate1(obligate1);
		newBill.setObligate2(obligate2);
		newBill.setAccountDate(accountDate);
		newBill.setBillAmount(newAmount);
		newBill.setRemark(newRemark);
		newBill.setStatusId(newStatusId);
		return newBill;
	}
	/**
	 * 调用账户接口 生成代付交易
	 * @param transorderinfo
	 * @return
	 */
	private Map<String,String> toAccWithhold(TransOrderInfo transorderinfo) {
		Map<String,String> rtnMap = new HashMap<String,String>();
		String appKey_AC=userProperties.getProperty("ACAPP_KEY");
		String appSecret_AC =userProperties.getProperty("ACAPP_SECRET");
		String url_AC=userProperties.getProperty("ACAPP_URL");
		DefaultRopClient ropClient = new DefaultRopClient(url_AC, appKey_AC,
				appSecret_AC, SettleConstants.FILE_XML);
		WheatfieldUserWithholdRequest acRequest=new WheatfieldUserWithholdRequest();
		acRequest.setRequestno(transorderinfo.getRequestNo());
		acRequest.setRequesttime(transorderinfo.getRequestTime());
		acRequest.setTradeflowno(transorderinfo.getTradeFlowNo());
		acRequest.setOrderpackageno("");
		acRequest.setOrderno(transorderinfo.getOrderNo());
		acRequest.setOrderdate(transorderinfo.getOrderDate());
		acRequest.setOrderamount(transorderinfo.getOrderAmount());
		acRequest.setOrdercount(transorderinfo.getOrderCount());
		acRequest.setFunccode(transorderinfo.getFuncCode());
		acRequest.setIntermerchantcode(transorderinfo.getInterMerchantCode());
		acRequest.setMerchantcode(transorderinfo.getMerchantCode());
		acRequest.setUserid(transorderinfo.getUserId());
		acRequest.setAmount(transorderinfo.getAmount());
		acRequest.setFeeamount(transorderinfo.getFeeAmount());
		acRequest.setUserfee(transorderinfo.getUserFee());
		acRequest.setProfit(transorderinfo.getProfit());
		acRequest.setBusitypeid(transorderinfo.getBusiTypeId());
		acRequest.setPaychannelid(transorderinfo.getPayChannelId());
		acRequest.setBankcode(transorderinfo.getBankCode());
		acRequest.setUseripaddress(transorderinfo.getUserIpAddress());
		acRequest.setStatus(1);
		acRequest.setErrorcode(transorderinfo.getErrorCode());
		acRequest.setErrormsg(transorderinfo.getErrorMsg());
		acRequest.setRemark(transorderinfo.getRemark());
		
		acRequest.setProductid(transorderinfo.getErrorCode());

		try {
			String session = SessionUtils.sessionGet(url_AC, appKey_AC,appSecret_AC);
			logger.info("取得url_AC:"+url_AC);
			logger.info("取得appKey_AC:"+appKey_AC);
			logger.info("取得appSecret_AC:"+appSecret_AC);
			logger.info("取得session:"+session);
			WheatfieldUserWithholdResponse acResponse=ropClient.execute(acRequest, session);
			if("true".equals(acResponse.getIs_success())) {
				rtnMap.put("code", "true");
				rtnMap.put("msg", "成功");
			} else {
				logger.error("帐户侧返回消息：" + acResponse.getMsg());
				rtnMap.put("code", "false");
				rtnMap.put("msg", acResponse.getMsg());
			}
		} catch (Exception e) {
			logger.error("调用帐户异常帐户：" + e.getMessage());
			e.printStackTrace();
			rtnMap.put("code", "false");
			rtnMap.put("msg", e.getMessage());
		}
		return rtnMap;
	}
	/**
	 * 调用账户接口 生成提现交易
	 * @param transorderinfo
	 * @return
	 */
	private Map<String,String> toAccWithdrow(TransOrderInfo transorderinfo) {
		Map<String,String> rtnMap = new HashMap<String,String>();
		String appKey_AC=userProperties.getProperty("ACAPP_KEY");
		String appSecret_AC =userProperties.getProperty("ACAPP_SECRET");
		String url_AC=userProperties.getProperty("ACAPP_URL");
		DefaultRopClient ropClient = new DefaultRopClient(url_AC, appKey_AC,
				appSecret_AC, SettleConstants.FILE_XML);
		WheatfieldUserWithdrowRequest acRequest=new WheatfieldUserWithdrowRequest();
		acRequest.setRequestno(transorderinfo.getRequestNo());
		acRequest.setRequesttime(transorderinfo.getRequestTime());
		acRequest.setTradeflowno(transorderinfo.getTradeFlowNo());
		acRequest.setOrderpackageno("");
		acRequest.setOrderno(transorderinfo.getOrderNo());
		acRequest.setOrderdate(transorderinfo.getOrderDate());
		acRequest.setOrderamount(transorderinfo.getOrderAmount());
		acRequest.setOrdercount(transorderinfo.getOrderCount());
		acRequest.setFunccode(transorderinfo.getFuncCode());
		acRequest.setIntermerchantcode(transorderinfo.getInterMerchantCode());
		acRequest.setMerchantcode(transorderinfo.getMerchantCode());
		acRequest.setUserid(transorderinfo.getUserId());
		acRequest.setAmount(transorderinfo.getAmount());
//		acRequest.setFeeamount(transorderinfo.getFeeAmount());
		acRequest.setUserfee(transorderinfo.getUserFee());
		acRequest.setProfit(transorderinfo.getProfit());
		acRequest.setBusitypeid(transorderinfo.getBusiTypeId());
		acRequest.setPaychannelid(transorderinfo.getPayChannelId());
		acRequest.setBankcode(transorderinfo.getBankCode());
		acRequest.setUseripaddress(transorderinfo.getUserIpAddress());
		acRequest.setStatus(1);
		acRequest.setErrorcode(transorderinfo.getErrorCode());
		acRequest.setErrormsg(transorderinfo.getErrorMsg());
		acRequest.setRemark(transorderinfo.getRemark());
		
		acRequest.setProductid(transorderinfo.getErrorCode());

		try {
			String session = SessionUtils.sessionGet(url_AC, appKey_AC,appSecret_AC);
			logger.info("取得url_AC:"+url_AC);
			logger.info("取得appKey_AC:"+appKey_AC);
			logger.info("取得appSecret_AC:"+appSecret_AC);
			logger.info("取得session:"+session);
			WheatfieldUserWithdrowResponse acResponse=ropClient.execute(acRequest, session);
			if("true".equals(acResponse.getIs_success())) {
				rtnMap.put("code", "true");
				rtnMap.put("msg", "成功");
			} else {
				logger.error("帐户侧返回消息：" + acResponse.getMsg());
				rtnMap.put("code", "false");
				rtnMap.put("msg", acResponse.getMsg());
			}
		} catch (Exception e) {
			logger.error("调用帐户异常帐户：" + e.getMessage());
			e.printStackTrace();
			rtnMap.put("code", "false");
			rtnMap.put("msg", e.getMessage());
		}
		return rtnMap;
	}
	
}