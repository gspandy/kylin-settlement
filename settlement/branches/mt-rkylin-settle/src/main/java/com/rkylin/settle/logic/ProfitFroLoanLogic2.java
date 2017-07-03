package com.rkylin.settle.logic;

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
import com.rkylin.settle.constant.Constants;
import com.rkylin.settle.constant.SettleConstants;
import com.rkylin.settle.constant.TransCodeConst;
import com.rkylin.settle.exception.SettleException;
import com.rkylin.settle.manager.SettleLoanDetailManager;
import com.rkylin.settle.manager.SettleParameterInfoManager;
import com.rkylin.settle.manager.SettleProfitInvoiceManager;
import com.rkylin.settle.manager.SettleTransBillManager;
import com.rkylin.settle.manager.SettleTransProfitManager;
import com.rkylin.settle.manager.TransOrderInfoManager;
import com.rkylin.settle.pojo.SettleProfitInvoice;
import com.rkylin.settle.pojo.SettleTransBill;
import com.rkylin.settle.pojo.SettleTransProfit;
import com.rkylin.settle.util.LogicConstantUtil;
import com.rkylin.wheatfield.api.PaymentAccountServiceApi;
import com.rkylin.wheatfield.api.PaymentInternalOutService;
import com.rongcapital.mtaegis.po.BalanceInfo;
import com.rongcapital.mtaegis.po.MultTransOrderInfo;
import com.rongcapital.mtaegis.po.SubTransOrderInfo;
import com.rongcapital.mtaegis.po.TransOrderInfo;
import com.rongcapital.mtaegis.po.TransOrderQueryInfo;
import com.rongcapital.mtaegis.response.CommonResponse;
import com.rongcapital.mtaegis.response.MultTransOrderInfoResponse;
import com.rongcapital.mtaegis.response.QueryBalanceResponse;
import com.rongcapital.mtaegis.service.BalanceQueryApi;
import com.rongcapital.mtaegis.service.MultTransactionCommonServiceApi;
import com.rongcapital.mtaegis.service.TransactionCommonApi;

/***
 * 分润逻辑
 * @author Yang
 * 
 */
@Component("profitFroLoanLogic2")
public class ProfitFroLoanLogic2 extends BasicLogic2 {
//	private static final Integer ACCOUNT_PROPERTY_INNER = 3;//内部账
	@Autowired
	private SettleParameterInfoManager settleParameterInfoManager; // '清算'属性表Manager
	@Autowired
	private SettleLoanDetailManager settleLoanDetailManager;
	@Autowired
	private SettleTransProfitManager settleTransProfitManager; // '分润结果信息'Manager
	@Autowired
	private SettleTransBillManager settleTransBillManager; // '挂账信息'Manager
	@Autowired
	private LogicConstantUtil logicConstantUtil; // 逻辑常量工具类
	@Autowired
	private PaymentInternalOutService paymentInternalOutService; // '账户系统'分润API
	@Autowired
	private PaymentAccountServiceApi payAccSerApi;
	@Autowired
	private PaymentAccountServiceApi paymentAccountServiceApi; // '账户系统'余额API
	@Autowired
	private TransOrderInfoManager transOrderInfoManager; // '账户'交易记录信息Manager
	@Autowired
	private RedisIdGenerator redisIdGenerator; // '清结算'交易信息Service
	@Autowired
	Properties userProperties;
	@Autowired
	SettleProfitInvoiceManager settleProfitInvoiceManager;
	@Autowired
	MultTransactionCommonServiceApi multTransactionCommonServiceApi;
	@Autowired
	BalanceQueryApi balanceQueryApi;
	@Autowired
	TransactionCommonApi transactionCommonApi;
	/***
	 * 结算分润结果 [调用账户系统接口]
	 * 
	 * @return 提示信息
	 */
	public Map<String, Object> doProfigBalance() throws Exception {
		List<SettleTransProfit> settleTransProfitList = null;
		return this.doProfigBalance(settleTransProfitList);
	}

	/***
	 * 结算分润结果 [调用账户系统接口]
	 * 
	 * @return 提示信息
	 */
	public Map<String, Object> doProfigBalance(String[] ids) throws Exception {
		List<SettleTransProfit> settleTransProfitList = this
				.getTransProfitByDetailIds(ids);
		return this.doProfigBalance(settleTransProfitList);
	}

	/***
	 * 结算分润结果 [调用账户系统接口]
	 * 
	 * @return 提示信息
	 */
	public Map<String, Object> doProfigBalance(List<SettleTransProfit> settleTransProfitList) throws Exception {
		logger.info(">>> >>> >>> >>> 进入结算[分润结果]");
		// 提示信息
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 挂账[交易后退款]交易List
		List<SettleTransBill> billList = null;
		// 分润结果结算成功的IdList
		List<Integer> idList = new ArrayList<Integer>();
		// 分润结果结算成功的IdList
		List<Integer> failIdList = new ArrayList<Integer>();
//		SimpleDateFormat formatDate = new SimpleDateFormat("yyyyMMddhhmmssSSS");
//		SimpleDateFormat formatPara = new SimpleDateFormat("YYYY-MM-DD HH:MM:SS");
		/*
		 * 查询账户二期机构集合
		 */
		//机构号集合
		String[] rootInstCdArr = null;
		/*
		 * 查询账户二期机构号集合
		 */
		try {
			rootInstCdArr = super.queryRootInstCdArr(Constants.ACCOUNT_SYS_CD_MTAEGIS);
		} catch (Exception e) {
			String msg = "查询账户二期机构号集合, 异常!";
			logger.error(">>> " + msg, e);
			this.editResultMap(resultMap, "-1", msg);
			e.printStackTrace();
			return resultMap;
		}
		/*
		 * 获取分润结果
		 */
		Calendar colDate = Calendar.getInstance();
		colDate.set(Calendar.HOUR_OF_DAY, 0);
		colDate.set(Calendar.MINUTE, 0);
		colDate.set(Calendar.SECOND, 0);
		try {
			if (settleTransProfitList == null) {
				settleTransProfitList = super.getTransProfitWithUnbalance(colDate.getTime(), rootInstCdArr);
			}
			if (settleTransProfitList.size() < 1) {
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
			if (billList == null || billList.size() < 1) {
				logger.info(">>> >>> >>> 获取所有未处理挂账单 0 条!");
			}
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常: 获取所有未处理挂账单");
			e.printStackTrace();
		}

		/*
		 * 1.遍历分润结果信息
		 */
//		Map<String, SettleTransProfit> editMapBean = new HashMap<String, SettleTransProfit>();
//		Map<String, SettleTransProfit> editMapAmount = new HashMap<String, SettleTransProfit>();
//		Map<String, String> editMapBill = new HashMap<String, String>();
//		Map<String, String> editMapId = new HashMap<String, String>();
//		Map<String, String> editMapAmtId = new HashMap<String, String>();
		Iterator<SettleTransProfit> iter = settleTransProfitList.iterator();
//		SettleTransProfit editBean = new SettleTransProfit();
//		BigDecimal addAmount = null;
//		BigDecimal priAmount = null;
//		String profitId = "";

		Map<String, List<TransOrderInfo>> mapTransOrderInfoList = new HashMap<String, List<TransOrderInfo>>();
		List<TransOrderInfo> transOrderInfoList = null;
		TransOrderInfo transOrderInfo = null;
		SettleTransProfit value = null;
		String loadDetailId = null;
		String[] proIdArr = null;
		while (iter.hasNext()) {
			value = (SettleTransProfit) iter.next();
			loadDetailId = value.getOrderNo();
			transOrderInfo = createTransOrderInfo(value);
			transOrderInfo.setRemark(loadDetailId + "#" + value.getTransProfitId());
			if (mapTransOrderInfoList.containsKey(loadDetailId)) {
				transOrderInfoList = mapTransOrderInfoList.get(loadDetailId);
			} else {
				transOrderInfoList = new ArrayList<TransOrderInfo>();
			}
			transOrderInfoList.add(transOrderInfo);
			mapTransOrderInfoList.put(loadDetailId, transOrderInfoList);
		}
		/*
		 * 2.调用 账户系统 - 转账
		 */
		Map<String, MultTransOrderInfo> mapMultTransOrderInfo = this.createMapMultTransOrderInfo(mapTransOrderInfoList);
		MultTransOrderInfo multTransOrderInfo = null;
		String remark = null;
		String msg = null;
		List<SubTransOrderInfo> subTransOrderInfoList = null;
		MultTransOrderInfoResponse commonResponse = null;
		for (String loadDetailIdKey : mapMultTransOrderInfo.keySet()) {
			multTransOrderInfo = mapMultTransOrderInfo.get(loadDetailIdKey);
			subTransOrderInfoList = multTransOrderInfo.getSubTransOrderInfoList();
			proIdArr = new String[transOrderInfoList.size()];
			for (int i = 0; i < subTransOrderInfoList.size(); i++) {
				remark = subTransOrderInfoList.get(i).getRemark();
				proIdArr[i] = remark.substring(remark.indexOf("#") + 1);
			}
			try {
				commonResponse = this.multTransactionCommonServiceApi.execute(multTransOrderInfo);
				if (commonResponse == null || !"1".equals(commonResponse.code)) {
					logger.info("失败:调用账务记账接口, msg:" + commonResponse.msg);
					failIdList = new ArrayList<Integer>();
					for (String id : proIdArr) {
						failIdList.add(Integer.parseInt(id));
					}
					if (failIdList.size() > 0) {
						msg = commonResponse.msg;
						msg = msg == null ? "commonResponse.msg is Null" : msg.length() > 250 ? msg.substring(0, 250) : msg;
						updateFailTransProfitStatus(failIdList, msg);
					}
				} else if ("1".equals(commonResponse.code)) {
					logger.info("成功:调用账务记账接口, msg:" + commonResponse.msg);
					idList = new ArrayList<Integer>();
					for (String id : proIdArr) {
						idList.add(Integer.parseInt(id));
					}
					if (idList.size() > 0) updateTransProfitStatus(idList, "分润结果, 结算成功!", 1, null);
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("异常:调用账务记账接口:", e);
				failIdList = new ArrayList<Integer>();
				for (String id : proIdArr) {
					failIdList.add(Integer.parseInt(id));
				}
				if (failIdList.size() > 0) {
					msg = commonResponse == null 
							? "commonResponse is Null" 
							: commonResponse.msg.length() > 250 
							? commonResponse.msg.substring(0, 250) 
							: commonResponse.msg;
					updateFailTransProfitStatus(failIdList, msg);
				}
			}
		}
		if (billList != null && billList.size() > 0) this.doBillBalance(billList);
		if (resultMap.size() < 1) this.editResultMap(resultMap, "1", "调用'账户系统'结算全都成功!!");
		return resultMap;
	}
	/***
	 * 结算[生成代付交易]
	 * 
	 * @param userId
	 *            转账用户ID
	 */
	public Map<String, Object> doInvoiceSettle(List<SettleProfitInvoice> settleProfitInvoiceList) {
		logger.info(">>> >>> >>> >>> 进入 结算[生成代付交易]");
		// 提示信息
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Calendar colDate = Calendar.getInstance();
		colDate.set(Calendar.HOUR_OF_DAY, 0);
		colDate.set(Calendar.MINUTE, 0);
		colDate.set(Calendar.SECOND, 0);
		//机构号集合
		String[] rootInstCdArr = null;
		/*
		 * 查询账户机构号集合
		 */
		try {
			rootInstCdArr = super.queryRootInstCdArr(Constants.ACCOUNT_SYS_CD_MTAEGIS);
		} catch (Exception e) {
			String msg = "查询账户机构号集合, 异常!";
			logger.error(">>> " + msg, e);
			this.editResultMap(resultMap, "-1", msg);
			e.printStackTrace();
			return resultMap;
		}
		try {
			if (settleProfitInvoiceList == null) {
				settleProfitInvoiceList = this.getTransProfitInvoice(colDate.getTime(), rootInstCdArr);
			}
			if (settleProfitInvoiceList.size() < 1) {
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

		Map<String, String> resMap = null;
		Map<String,Object> resMap2 = null;
		while (iter.hasNext()) {
			// 当前分润结果信息
			SettleProfitInvoice item = iter.next();
			com.rkylin.wheatfield.pojo.TransOrderInfo transorderinfo = new com.rkylin.wheatfield.pojo.TransOrderInfo();
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

			resMap = new HashMap<String,String>();
			if ("4016".equals(item.getFuncCode())) {//调用账户提现接口
				resMap = this.toAccWithdrow(transorderinfo);
			} else if ("4014".equals(item.getFuncCode()) || "40144".equals(item.getFuncCode())) {//调用账户代付接口
				resMap = this.toAccWithhold(transorderinfo);
			} else if (TransCodeConst.CHECK_THE_BALANCE_SUBTRACT.equals(item.getFuncCode())) {//调用账户调账接口
				try {
					//调用账户调账接口
					resMap2 = super.changeAccount2P(
							transorderinfo.getMerchantCode()
							, transorderinfo.getUserId()
							, transorderinfo.getErrorCode()
							, "F30281"//转出(单边-)(实时)
							, transorderinfo.getAmount()
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
		if (resultMap.size() < 1)
			this.editResultMap(resultMap, "1", "生成代付交易全都成功!!");
		return resultMap;
	}

	/***
	 * 结算[头寸代付交易]
	 * 
	 * @param userId
	 *            转账用户ID
	 */
//	public Map<String, Object> doInvoiceSettleForCash(
//			List<SettleProfitInvoice> settleProfitInvoiceList) {
//		logger.info(">>> >>> >>> >>> 进入 结算[头寸]代付金额");
//		// 提示信息
//		Map<String, Object> resultMap = new HashMap<String, Object>();
//		Calendar colDate = Calendar.getInstance();
//		colDate.set(Calendar.HOUR_OF_DAY, 0);
//		colDate.set(Calendar.MINUTE, 0);
//		colDate.set(Calendar.SECOND, 0);
//		try {
//			if (settleProfitInvoiceList == null) {
//				settleProfitInvoiceList = this.getTransProfitInvoiceForCash(colDate.getTime());
//			}
//			if (settleProfitInvoiceList.size() < 1) {
//				String msg = "获取分润结果信息 0条";
//				logger.error(">>> " + msg);
//				this.editResultMap(resultMap, "0", msg);
//				return resultMap;
//			}
//		} catch (Exception e) {
//			String msg = "获取分润结果信息, 异常!";
//			logger.error(">>> " + msg);
//			this.editResultMap(resultMap, "-1", msg);
//			e.printStackTrace();
//			return resultMap;
//		}
//
//		Iterator<SettleProfitInvoice> iter = settleProfitInvoiceList.iterator();
//
//		Map<String, String> resMap = null;
//		Map<String, String> cashMap = new HashMap<String, String>();
//		Map<String, String> cashIdMap = new HashMap<String, String>();
//		String cashKey = "";
//		String invoiceId = "";
//		String[] invoiceIdArr = null;
//		BigDecimal priAmount = null;
//		BigDecimal addAmount = null;
//		logger.info(">>> 按机构+交易类型汇总，头寸交易");
//		while (iter.hasNext()) {
//			// 当前分润结果信息
//			SettleProfitInvoice item = iter.next();
//			if (item.getStatus() == 0 || item.getStatus() == 1) {
//				cashKey = item.getMerchantCode() + item.getFuncCode();
//				if (cashMap.containsKey(cashKey)) {
//					priAmount = new BigDecimal(cashMap.get(cashKey));
//					addAmount = new BigDecimal(item.getAmount());
//					priAmount = priAmount.add(addAmount);
//					cashMap.put(cashKey, priAmount.toString());
//					invoiceId = cashIdMap.get(cashKey);
//					invoiceId = invoiceId + "," + String.valueOf(item.getProfitInvoiceId());
//					cashIdMap.put(cashKey, invoiceId);
//				} else {
//					cashMap.put(cashKey, String.valueOf(item.getAmount()));
//					cashIdMap.put(cashKey, String.valueOf(item.getProfitInvoiceId()));
//				}
//			}
//		}
//		logger.info(">>> 根据汇总结果汇总，发起头寸交易");
//		Iterator entries = cashMap.entrySet().iterator();
//		SimpleDateFormat formatDate = new SimpleDateFormat("yyyyMMddhhmmssSSS");
//		Date newDate = new Date();
//		SettleProfitInvoice updata = new SettleProfitInvoice();
//		while (entries.hasNext()) {
//			Map.Entry entry = (Map.Entry) entries.next();
//			String key = (String) entry.getKey();
//			if (key.compareTo("M00000440144") == 0) {
//				TransOrderInfo transorderinfo = new TransOrderInfo();
//				transorderinfo.setAmount(Long.parseLong(cashMap.get(key)));
//				transorderinfo.setUserId("141223100000001");
//				transorderinfo.setFuncCode("4014");
//				// transorderinfo.setInterMerchantCode("14623543518818651");
//				// transorderinfo.setMerchantCode("M000001");
//				transorderinfo.setOrderAmount(Long.parseLong(cashMap.get(key)));
//				transorderinfo.setOrderCount(1);
//				transorderinfo.setOrderDate(newDate);
//				transorderinfo.setOrderNo("SC" + formatDate.format(newDate));
//				transorderinfo.setErrorCode("P000002");
//				transorderinfo.setRequestTime(newDate);
//				// transorderinfo.setStatus(1);
//				transorderinfo.setUserFee(0l);
//				transorderinfo.setUserIpAddress("168.0.0.1");
//				transorderinfo.setRemark("清算头寸");
//
//				resMap = this.toAccWithhold(transorderinfo);
//
//				if ("true".equals(resMap.get("code"))) {
//					invoiceId = cashIdMap.get(key);
//					invoiceIdArr = invoiceId.split(",");
//					for (String id : invoiceIdArr) {
//						updata.setProfitInvoiceId(Long.parseLong(id));
//						updata.setCashStatus(1);
//						updata.setReturnMsg("已经生成头寸交易");
//						settleProfitInvoiceManager
//								.updateSettleProfitInvoice(updata);
//					}
//				} else {
//					invoiceId = cashIdMap.get(key);
//					invoiceIdArr = invoiceId.split(",");
//					for (String id : invoiceIdArr) {
//						updata.setProfitInvoiceId(Long.parseLong(id));
//						updata.setCashStatus(0);
//						updata.setReturnMsg("生成头寸交易失败");
//						settleProfitInvoiceManager
//								.updateSettleProfitInvoice(updata);
//					}
//				}
//			}
//		}
//
//		if (resultMap.size() < 1) this.editResultMap(resultMap, "1", "生成代付交易全都成功!!");
//		return resultMap;
//	}
	/***
	 * 结算[消费后退款]挂账金额
	 * 
	 * @param userId
	 *            转账用户ID
	 */
	private void doBillBalance(List<SettleTransBill> billList) {
		logger.info(">>> >>> >>> >>> 进入 结算[消费后退款]挂账金额");
		// 需要添加的新挂账信息
		List<SettleTransBill> newBillList = new ArrayList<SettleTransBill>();
//		SimpleDateFormat formatDate = new SimpleDateFormat("yyyyMMddhhmmssSSS");
//		SimpleDateFormat formatPara = new SimpleDateFormat("YYYY-MM-DD HH:MM:SS");
		TransOrderInfo transOrderInfo;
		TransOrderQueryInfo transOrderQueryInfo;
		/*
		 * 1.调用 账户系统 - 查询账户余额接口
		 */
//		User user = new User();
//		String paramString = "";

//		Map<String, String[]> paraMap = new HashMap<String, String[]>();
		CommonResponse commonResponse = null;
		Iterator<SettleTransBill> transBillIter = billList.iterator();
//		BigDecimal midAmount = null;
//		BigDecimal subAmount = null;
//		BigDecimal priAmount = null;
//		SettleTransProfit settleTransProfit = null;
		BalanceInfo balance = null;
		QueryBalanceResponse qbr = null;
		Long amountBalance = null;
		while (transBillIter.hasNext()) {
			// 交易后退款实体Bean
			SettleTransBill bill = transBillIter.next();
			// 调用接口返回Balance实体
			transOrderInfo = new TransOrderInfo();
			transOrderInfo.setDealProductCode(SettleConstants.BALANCE_QUERY_DPI);
			transOrderInfo.setRootInstCd(bill.getRootInstCd());
//			transOrderInfo.setProductId(bill.getProductId());
			transOrderInfo.setRoleCode(bill.getRoleCode());
			transOrderInfo.setUserId(bill.getUserId());
//			transOrderInfo.setUserIpAddress(SettleConstants.LOCALHOST_URL);
			
			transOrderQueryInfo = new TransOrderQueryInfo();
			transOrderQueryInfo.setDealProductCode(SettleConstants.BALANCE_QUERY_DPI);
			transOrderQueryInfo.setRootInstCd(bill.getRootInstCd());
//			transOrderQueryInfo.setProductId(bill.getProductId());
			transOrderQueryInfo.setRoleCode(bill.getRoleCode());
			transOrderQueryInfo.setUserId(bill.getUserId());
//			transOrderQueryInfo.setUserIpAddress(SettleConstants.LOCALHOST_URL);
			try {
				qbr = this.balanceQueryApi.execute(transOrderQueryInfo);
			} catch (Exception e) {
				logger.error(">>> >>> 异常: balanceQueryApi 查询余额失败", e);
			}
			if(qbr != null) {
				if("1".equals(qbr.code)) {
					balance = qbr.getBalance();
				} else {
					logger.error(">>> >>> balanceQueryApi 查询余额失败 code:"+ qbr.code +", msg:" + qbr.msg);
				}
			} else {
				logger.error(">>> >>> balanceQueryApi 查询余额失败 qbr is Null");
			}
			// 账户提现余额
			amountBalance = balance==null ? 0L : balance.getBalanceSettle();
			// 判断余额是否为0
			if (amountBalance <= 0) {
				logger.error("此用户余额为0, 不进行转账操作! [USER_ID:" + bill.getUserId()
						+ ", PRODUCT_ID:" + bill.getProductId() + ", CONST_ID:"
						+ bill.getRootInstCd() + "]");
				bill.setStatusId(0);
				bill.setRemark("此用户余额为0, 不进行转账操作!");
				settleTransBillManager.updateSettleTransBill(bill);
				continue;
			}
			logger.info(">>> >>> 调用账户接口获取余额:" + amountBalance);
			/*
			 * 处理转账金额逻辑
			 */
			Map<String, Object> resultMap = this.getTransferAmount(amountBalance, bill);// 转账金额
			// 新退款信息
			SettleTransBill newBill = (SettleTransBill) resultMap.get("newBill");
			// 老退款信息
			SettleTransBill oldBill = (SettleTransBill) resultMap.get("oldBill");
			// 退款金额(转账金额)
			Long transferAmount = (Long) resultMap.get("transferAmount");
			// 当前日期
//			Date newDate = new Date();
			// 备注
//			String remark = "分润清算";
			/*
			 * 2.调用 账户系统 - 转账
			 */
			try {
				transOrderInfo = createTransOrderInfo(bill, transferAmount);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(">>> >>> >>> 异常:SettleTransBill封装账务接口入参TransOrderInfo", e);
				continue;
			}
			try {
				logger.info(">>> 执行交易后退款的转账操作!");
				commonResponse = transactionCommonApi.execute(transOrderInfo);
				if ((commonResponse == null) || (!"1".equals(commonResponse.code))) {
					logger.info("失败: 转账结束，：[" + bill.getUserId() + "|"
							+ bill.getProductId() + "]TO["
							+ bill.getInterUserId() + "|"
							+ bill.getIntoProductId() + "]金额["
							+ transferAmount + ", 账户操作失败！返回结果为["
							+ commonResponse.msg);
				} else if ("1".equals(commonResponse.code)) {
					logger.info("成功: 转账结束，：[" + bill.getUserId() + "|"
							+ bill.getProductId() + "]TO["
							+ bill.getInterUserId() + "|"
							+ bill.getIntoProductId() + "]金额["
							+ transferAmount + ", 账户操作成功！返回结果为["
							+ commonResponse.msg);
				}
			} catch (Exception e) {
				logger.info("异常: 转账结束，：[" + bill.getUserId() + "|"
						+ bill.getProductId() + "]TO["
						+ bill.getInterUserId() + "|"
						+ bill.getIntoProductId() + "]金额[" + transferAmount
						+ ", 账户操作异常！");
				e.printStackTrace();
			}
			try {
				if (("1".equals(commonResponse.code)) && (oldBill != null)) {
					logger.info(">>> >>> >>> 调用'账户转账接口'成功!");
					this.settleTransBillManager
							.updateSettleTransBill(oldBill);
				} else {
					logger.error(">>> >>> >>> 调用'账户转账接口'失败!");
					oldBill.setStatusId(Integer.valueOf(0));
					oldBill.setRemark("交易后退款时,调用账户转账接口返回'失败'");
					this.settleTransBillManager
							.updateSettleTransBill(oldBill);
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("失败: 账户转账交易成功，更新老的退款信息 [交易后退款] 异常!");
			}
			try {
				if (("1".equals(commonResponse.code)) && (newBill != null)) {
					this.settleTransBillManager
							.saveSettleTransBill(newBill);
					logger.info(">>> 【挂账处理完成】新挂账单信息：主键"
							+ newBill.getTransBillId() + "; 金额"
							+ newBill.getBillAmount() + "; 用户ID"
							+ newBill.getUserId());
					newBillList.add(newBill);
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("失败: 账户转账交易成功，存储新的退款信息 [交易后退款] 异常!");
			}
			transBillIter.remove();
			billList.remove(bill);
			logger.info("成功: 分润结束，退款挂账 账户余额：[" + amountBalance + "]需要退款：["
					+ transferAmount + "]");
		}
		billList.addAll(newBillList);
	}

	/***
	 * 获取转账金额 并 更新挂账信息
	 * 
	 * @param amountBalance
	 *            用户余额
	 * @param bill
	 * @return 转账金额
	 */
	private Map<String, Object> getTransferAmount(Long amountBalance,
			SettleTransBill bill) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		logger.info(">>> >>> >>> >>> 进入 获取转账金额 并 更新挂账[交易后退款]信息");
		// 新生成的挂账Bean
		SettleTransBill newBill = null;
		// 挂账金额
		Long billAmount = bill.getBillAmount();
		// 最终金额
		Long transferAmount = 0l;
		// 老挂账备注
		String oldRemark = "";
		// 老挂账状态 [已处理]
		Integer oldStatusId = 1;
		if (amountBalance >= billAmount) {// 如果余额 >= 挂账金额[退款金额],转账金额 为 挂账金额[退款金额],此挂账结清
			logger.info(">>> 余额 >= 挂账金额[退款金额],转账金额 为 挂账金额[退款金额],此挂账结清");
			oldRemark = "至此结清!";
			transferAmount = billAmount;
		} else {// 如果余额 < 挂账金额[退款金额],转账金额 为 所有余额, 此挂账未结清
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
	 * 
	 * @param statusIds
	 *            交易类型状态的Integer数组
	 * @return
	 */
	public List<Map<String, Object>> getProfitTransDetail(Integer[] statusIds) {
		logger.info(">>> >>> >>> 获取所有未清分交易信息 ... ...");
		Map<String, Object> map = new HashMap<String, Object>();
		// 订单类型0交易,1结算单, 分润就分账户的交易
		Date beginAccDate = new Date();
		Date endAccDate = new Date();
		beginAccDate
				.setTime(beginAccDate.getTime() - 20 * 24 * 60 * 60 * 1000L);
		endAccDate.setTime(endAccDate.getTime() + 24 * 60 * 60 * 1000L);
		map.put("beginAccDate", beginAccDate);
		map.put("endAccDate", endAccDate);
		map.put("statusIds", statusIds);
		// 查询清分交易信息
		List<Map<String, Object>> profitTransDetailList = this.settleLoanDetailManager
				.selectProfitTransInfo(map);
		return profitTransDetailList;
	}

	/***
	 * 更新DB中分润相关'交易结果' SETTLE_BALANCE_ENTRY SETTLE_BALANCE_ENTRY 交易结果
	 * 
	 * @param transProfitList
	 */
	/*private void insertAndUpdateSettleTransProfit(
			List<SettleTransProfit> transProfitList) {
		logger.info(">>> >>> >>> 更新 分润结果信息 ... ..." + transProfitList.size());
		// 分润信息查询query对象
		SettleTransProfitQuery query = null;
		// 分润信息结果
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
				// 通过对账key和订单号查询对账结构
				resList = settleTransProfitManager.queryList(query);
				if (resList != null && resList.size() > 0) {// 有此结果
					if (resList.get(0).getStatusId() == 0) {
						// 修改原分润结果
						subbean.setTransProfitId(resList.get(0)
								.getTransProfitId());
						settleTransProfitManager
								.updateSettleTransProfit(subbean);
					}
				} else {// 无此结果
					// 添加新分润结果
					settleTransProfitManager.saveSettleTransProfit(subbean);
				}
			}
		}
	}*/

	/***
	 * 更新DB中分润相关'交易结果' SETTLE_BALANCE_ENTRY SETTLE_BALANCE_ENTRY 交易结果
	 * 
	 * @param transProfitList
	 */
	/*private void insertAndUpdateSettleTransBill(
			List<SettleTransBill> transBillList) {
		logger.info(">>> >>> >>> 更新 挂账结果信息 ... ..." + transBillList.size());
		// 分润信息查询query对象
		SettleTransBillQuery query = null;
		// 分润信息结果
		List<SettleTransBill> resList = null;
		if (transBillList.size() > 0) {
			for (SettleTransBill subbean : transBillList) {
				query = new SettleTransBillQuery();
				query.setOrderNo(subbean.getOrderNo());
				query.setRootInstCd(subbean.getRootInstCd());
				query.setUserId(subbean.getUserId());
				query.setProductId(subbean.getProductId());
				query.setObligate1(subbean.getObligate1());
				// 通过对账key和订单号查询对账结构
				resList = settleTransBillManager.queryList(query);
				if (resList != null && resList.size() > 0) {// 有此结果
					if (resList.get(0).getStatusId() == 0) {
						// 修改原挂账结果
						subbean.setTransBillId(resList.get(0).getTransBillId());
						settleTransBillManager.updateSettleTransBill(subbean);
					}
				} else {// 无此结果
					// 添加新分润结果
					settleTransBillManager.saveSettleTransBill(subbean);
				}
			}
		}
	}*/

	/***
	 * 更新DB中分润相关'清算'交易信息 SETTLE_TRANS_DETAIL 交易信息明细
	 * 
	 * @param updateTrDetIdMap
	 */
	/*private void updateTransDetailProfitStatus(
			Map<String, List<Long>> updateTrDetIdMap) {
		logger.info(">>> >>> >>> 更新 交易信息明细 ... ..." + updateTrDetIdMap.size());
		Map<String, Object> map = new HashMap<String, Object>();
		// 不需分润的交易信息IDList
		List<Long> unProTrDetIdList = updateTrDetIdMap.get("unProTrDetIdList");
		// 分润'成功'的交易信息IDList
		List<Long> successProTrDetIdList = updateTrDetIdMap
				.get("successProTrDetIdList");
		// 分润'失败'的交易信息IDList
		List<Long> failProTrDetIdList = updateTrDetIdMap
				.get("failProTrDetIdList");
		if (unProTrDetIdList != null) {// 无需分润, 交易状态:分润成功
			map.put("statusId", SettleConstants.STATUS_PROFIT_SUCCESS);
			map.put("idList", unProTrDetIdList);
			settleLoanDetailManager.updateTransStatusId(map);
		}
		if (successProTrDetIdList != null) {// 分润成功, 交易状态:分润成功
			map.put("statusId", SettleConstants.STATUS_PROFIT_SUCCESS);
			map.put("idList", successProTrDetIdList);
			settleLoanDetailManager.updateTransStatusId(map);
		}
		if (failProTrDetIdList != null) {// 分润失败, 交易状态:分润失败
			map.put("statusId", SettleConstants.STATUS_PROFIT_FALID);
			map.put("idList", failProTrDetIdList);
			settleLoanDetailManager.updateTransStatusId(map);
		}
	}*/

	/***
	 * 获取需分润的交易类型
	 * 
	 * @param profitStuKey
	 *            SETTLE_PARAMETER_INFO中存储交易类型的Key
	 * @return
	 */
	/*private List<String> getProfitTypeByKey(String profitTypeKey) {
		return super.getFuncCodeFromParamInfo(profitTypeKey);
	}*/

	/***
	 * 获取全表分润规则
	 */
	/*private List<SettleProfitKey> getAllProfitKey() throws Exception {
		logger.info(">>> >>> >>> 获取全本分润规则 ... ...");
		// 调用逻辑常量,获取内存中的分润规则
		return logicConstantUtil.getsettleProfitKeyList();
	}*/

	/***
	 * 匹配交易所对应的唯一分润规则
	 * 
	 * @param transDetailMap
	 * @param profitKeyList
	 * @return
	 */
	/*private SettleProfitKey getUniqueProfitKey4TransDetail(
			Map<String, Object> transDetailMap,
			List<SettleProfitKey> profitKeyList) {
		logger.info(">>> >>> >>> 匹配交易所对应的唯一分润规则 ... ...");
		// 全部分润规则迭代器
		Iterator<SettleProfitKey> iter = profitKeyList.iterator();
		// 声明分润规则对账
		SettleProfitKey theProfitKey = null;
		// 遍历
		while (iter.hasNext()) {
			// 从迭代器中获取分润规则对象
			SettleProfitKey item = iter.next();
			// 匹配规则的key - 规则信息
			String rulekey = "";
			// 匹配规则的key - 交易信息
			String transKey = "";
			// 默认key "机构号,功能码"
			rulekey = item.getRootInstCd() + "," + item.getFuncCode();
			transKey = String.valueOf(transDetailMap.get("ROOT_INST_CD")) + ","
					+ String.valueOf(transDetailMap.get("PRODUCT_ID"));
			*//**
			 * 匹配分润规则 常规情况下使用"机构号&功能码"就可以匹配到此条交易的唯一分润规则 考虑特殊需求预留了3组扩展字段作为动态key设置
			 * 以下逻辑为动态key匹配
			 *//*
			if (item.getKeyName1() != null && !item.getKeyName1().isEmpty()) {
				rulekey += "," + item.getKeyValue1();
				transKey += "," + transDetailMap.get(item.getKeyName1());
				if (item.getKeyName2() != null && !item.getKeyName2().isEmpty()) {
					rulekey += "," + item.getKeyValue2();
					transKey += "," + transDetailMap.get(item.getKeyName2());
					if (item.getKeyName3() != null
							&& !item.getKeyName3().isEmpty()) {
						rulekey += "," + item.getKeyValue3();
						transKey += ","
								+ transDetailMap.get(item.getKeyName3());
						if (item.getKeyName4() != null
								&& !item.getKeyName4().isEmpty()) {
							rulekey += "," + item.getKeyValue4();
							transKey += ","
									+ transDetailMap.get(item.getKeyName4());
							if (item.getKeyName5() != null
									&& !item.getKeyName5().isEmpty()) {
								rulekey += "," + item.getKeyValue5();
								transKey += ","
										+ transDetailMap
												.get(item.getKeyName5());
								if (item.getKeyName6() != null
										&& !item.getKeyName6().isEmpty()) {
									rulekey += "," + item.getKeyValue6();
									transKey += ","
											+ transDetailMap.get(item
													.getKeyName6());
									if (item.getKeyName7() != null
											&& !item.getKeyName7().isEmpty()) {
										rulekey += "," + item.getKeyValue7();
										transKey += ","
												+ transDetailMap.get(item
														.getKeyName7());
										if (item.getKeyName8() != null
												&& !item.getKeyName8()
														.isEmpty()) {
											rulekey += ","
													+ item.getKeyValue8();
											transKey += ","
													+ transDetailMap.get(item
															.getKeyName8());
										}
									}
								}
							}
						}
					}
				}
			}
			if (rulekey.equals(transKey)) {
				theProfitKey = item;
				break;
			}
		}
		if (theProfitKey == null) {
			logger.error("未匹配到此条交易[ID:"
					+ String.valueOf(transDetailMap.get("LOAN_ID"))
					+ "]的'分润规则'!");
			throw new SettleException("未匹配到此条交易[ID:"
					+ String.valueOf(transDetailMap.get("LOAN_ID"))
					+ "]的'分润规则'!");
		}
		return theProfitKey;
	}*/
	/***
	 * 查询全部未结算的清分交易
	 * 
	 * @return
	 */
	/*private List<SettleTransProfit> getTransProfitWithUnsettle(Date settleTime) {
		logger.info(">>> >>> >>> >>> 查询全部未生成代付的清分交易!");
		List<Integer> detailStatusIdList = new ArrayList<Integer>();
		detailStatusIdList.add(11);// 分润成功
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("proStatusId", 1);
		queryMap.put("detailStatusIdList", detailStatusIdList);
		queryMap.put("settleTime", settleTime);
		queryMap.put("settleObjectFlg", "0");
		return settleTransProfitManager.selectloanProfitWithUnbalance(queryMap);
	}*/

	/***
	 * 查询全部未结算的清分交易
	 * 
	 * @return
	 */
//	private List<SettleProfitInvoice> getTransProfitInvoice(Date settleTime) {
//		logger.info(">>> >>> >>> >>> 查询全部未生成代付交易的清分交易!");
//		SettleProfitInvoiceQuery example = new SettleProfitInvoiceQuery();
//		example.setStatus(0);
//		example.setOrderDate(settleTime);
//		return settleProfitInvoiceManager.selectInvoiceData(example);
//	}
	/***
	 * 查询全部未结算的清分交易
	 * 
	 * @return
	 */
	private List<SettleProfitInvoice> getTransProfitInvoice(Date settleTime, String[] rootInstCdArr) {
		logger.info(">>> >>> >>> >>> 查询全部未生成代付交易的清分交易!");
		Map<String, Object> example = new HashMap<String, Object>();
		example.put("status", 0);
		example.put("orderDate", settleTime);
		example.put("rootInstCdArr", rootInstCdArr);
		return settleProfitInvoiceManager.selectInvoiceDataByMap(example);
	}

	/***
	 * 查询全部未头寸的清分交易
	 * 
	 * @return
	 */
//	private List<SettleProfitInvoice> getTransProfitInvoiceForCash(
//			Date settleTime) {
//		logger.info(">>> >>> >>> >>> 查询全部未生成代付交易的清分交易!");
//		SettleProfitInvoiceQuery example = new SettleProfitInvoiceQuery();
//		example.setCashStatus(0);
//		example.setOrderDate(settleTime);
//		return settleProfitInvoiceManager.selectInvoiceData(example);
//	}
	/***
	 * 通过交易信息表的ID 查询对应的分润规则
	 * 
	 * @param ids
	 * @return
	 */
	private List<SettleTransProfit> getTransProfitByDetailIds(String[] ids) {
		logger.info(">>> >>> >>> >>> 查询全部未结算的清分交易,根据交易ID!");
		List<Integer> detailStatusIdList = new ArrayList<Integer>();
		detailStatusIdList.add(21);// 对账成功的交易,交易进来分润后状态为11[实时分润],如果参与对账状态为21[参与对账的交易]
		detailStatusIdList.add(11);// 分润成功
		List<SettleTransProfit> settleTransProfitList = null;
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("ids", ids);
		queryMap.put("proStatusId", 0);
		queryMap.put("detailStatusIdList", detailStatusIdList);
		settleTransProfitList = settleTransProfitManager
				.selectTransProfitWithDetailId(queryMap);
		return settleTransProfitList;
	}

	/***
	 * 更新 分润结果表状态 为'已结算'
	 * 
	 * @param idList
	 */
	private void updateTransProfitStatus(List<Integer> idList, String remark,
			int statusId, String orderNo) {
		logger.info(">>> >>> >>> >>> 更新 分润结果表状态 为'已结算'");
		Map<String, Object> map = new HashMap<String, Object>();
		if (orderNo != null && !"".equals(orderNo)) {
			map.put("orderNo", orderNo);// 结算单好好
		}
		map.put("statusId", statusId);// 已结算
		map.put("remark", remark);// 已结算
		map.put("idList", idList);
		settleTransProfitManager.updateTransStatusId(map);
	}

	/***
	 * 更新 失败分润结果
	 * 
	 * @param idList
	 */
	private void updateFailTransProfitStatus(List<Integer> idList, String remark) {
		logger.info(">>> >>> >>> >>> 更新 分润结果表备注 为'结算失败'");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("statusId", 0);// 已结算
		map.put("remark", remark);// 未结算
		map.put("idList", idList);
		settleTransProfitManager.updateTransStatusId(map);
	}

	/***
	 * 获取所有未处理的挂账[交易后退款]信息
	 * 
	 * @return
	 * @throws SettleException
	 */
	private List<SettleTransBill> getTransBillByUntreated(Date settleTime)
			throws SettleException {
		logger.info(">>> >>> >>> >>> 查询全部未结算的挂账交易!");
		List<Integer> detailStatusIdList = new ArrayList<Integer>();
		detailStatusIdList.add(11);// 分润成功
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("proStatusId", 0);
		queryMap.put("detailStatusIdList", detailStatusIdList);
		queryMap.put("settleTime", settleTime);
		queryMap.put("proBillType", "3");
		return settleTransBillManager.queryloanByCreatedTime(queryMap);
	}

	/***
	 * 获取所有未处理的挂账[交易后退款]信息
	 * 
	 * @return
	 * @throws SettleException
	 */
	/*private List<SettleTransBill> getTransBillByUnsettle(Date settleTime)
			throws SettleException {
		logger.info(">>> >>> >>> >>> 查询全部未结算的挂账交易!");
		List<Integer> detailStatusIdList = new ArrayList<Integer>();
		detailStatusIdList.add(11);// 分润成功
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("proStatusId", 1);
		queryMap.put("detailStatusIdList", detailStatusIdList);
		queryMap.put("settleTime", settleTime);
		queryMap.put("proBillType", "3");
		return settleTransBillManager.queryloanByCreatedTime(queryMap);
	}*/

	/***
	 * 编辑新的挂账信息
	 * 
	 * @param oldBill
	 *            衍生新挂账的老挂账信息
	 * @param newAmount
	 *            挂账结算后的剩余金额
	 * @return
	 */
	private SettleTransBill editChildBill(SettleTransBill oldBill,
			Long newAmount) {
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
	 * 调用账户rop接口 生成代付交易
	 * @param transorderinfo
	 * @return
	 */
	private Map<String, String> toAccWithhold(com.rkylin.wheatfield.pojo.TransOrderInfo transorderinfo) {
		Map<String, String> rtnMap = new HashMap<String, String>();
		String appKey_AC = userProperties.getProperty("ACAPP_KEY");
		String appSecret_AC = userProperties.getProperty("ACAPP_SECRET");
		String url_AC = userProperties.getProperty("ACAPP_URL");
		DefaultRopClient ropClient = new DefaultRopClient(url_AC, appKey_AC,
				appSecret_AC, SettleConstants.FILE_XML);
		WheatfieldUserWithholdRequest acRequest = new WheatfieldUserWithholdRequest();
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
			String session = SessionUtils.sessionGet(url_AC, appKey_AC,
					appSecret_AC);
			logger.info("取得url_AC:" + url_AC);
			logger.info("取得appKey_AC:" + appKey_AC);
			logger.info("取得appSecret_AC:" + appSecret_AC);
			logger.info("取得session:" + session);
			WheatfieldUserWithholdResponse acResponse = ropClient.execute(
					acRequest, session);
			if ("true".equals(acResponse.getIs_success())) {
				rtnMap.put("code", "true");
				rtnMap.put("msg", "成功");
			} else {
				logger.error("账户侧返回消息：" + acResponse.getMsg());
				rtnMap.put("code", "false");
				rtnMap.put("msg", acResponse.getMsg());
			}
		} catch (Exception e) {
			logger.error("调用账户异常账户：" + e.getMessage());
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
	private Map<String, String> toAccWithdrow(com.rkylin.wheatfield.pojo.TransOrderInfo transorderinfo) {
		Map<String, String> rtnMap = new HashMap<String, String>();
		String appKey_AC = userProperties.getProperty("ACAPP_KEY");
		String appSecret_AC = userProperties.getProperty("ACAPP_SECRET");
		String url_AC = userProperties.getProperty("ACAPP_URL");
		DefaultRopClient ropClient = new DefaultRopClient(url_AC, appKey_AC,
				appSecret_AC, SettleConstants.FILE_XML);
		WheatfieldUserWithdrowRequest acRequest = new WheatfieldUserWithdrowRequest();
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
		acRequest.setFeeanount(transorderinfo.getUserFee());
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
			String session = SessionUtils.sessionGet(url_AC, appKey_AC,
					appSecret_AC);
			logger.info("取得url_AC:" + url_AC);
			logger.info("取得appKey_AC:" + appKey_AC);
			logger.info("取得appSecret_AC:" + appSecret_AC);
			logger.info("取得session:" + session);
			WheatfieldUserWithdrowResponse acResponse = ropClient.execute(
					acRequest, session);
			if ("true".equals(acResponse.getIs_success())) {
				rtnMap.put("code", "true");
				rtnMap.put("msg", "成功");
			} else {
				logger.error("账户侧返回消息：" + acResponse.getMsg());
				rtnMap.put("code", "false");
				rtnMap.put("msg", acResponse.getMsg());
			}
		} catch (Exception e) {
			logger.error("调用账户异常账户：" + e.getMessage());
			e.printStackTrace();
			rtnMap.put("code", "false");
			rtnMap.put("msg", e.getMessage());
		}
		return rtnMap;
	}
	/**
	 * SettleTransProfit封装账务接口入参TransOrderInfo
	 * @param value
	 * @return
	 * @throws Exception
	 */
	private TransOrderInfo createTransOrderInfo(SettleTransProfit value)
			throws Exception {
		logger.info(">>> >>> >>> 开始: SettleTransProfit封装账务接口入参TransOrderInfo");
		TransOrderInfo transOrderInfo = new TransOrderInfo();
		SimpleDateFormat formatDate = new SimpleDateFormat("yyyyMMddhhmmssSSS");
		Date newDate = new Date();
		transOrderInfo.setDealProductCode(value.getObligate2());
//		transOrderInfo.setAmount(value.getProfitAmount());
		transOrderInfo.setUserId(value.getUserId());
		transOrderInfo.setFuncCode("3001");
		transOrderInfo.setInterUserId(value.getInterUserId());
		transOrderInfo.setRootInstCd(value.getRootInstCd());
		transOrderInfo.setOrderCount(Integer.valueOf(1));
		transOrderInfo.setOrderDate(newDate);
		transOrderInfo.setOrderNo("S" + formatDate.format(newDate));
//		transOrderInfo.setStatusId(Integer.valueOf(1));
		transOrderInfo.setUserFee(Long.valueOf(0L));
//		transOrderInfo.setProductId(value.getProductId());
//		transOrderInfo.setIntoProductId(value.getIntoProductId());
		transOrderInfo.setUserIpAddress(value.getUserIpAddress());
		transOrderInfo.setRequestTime(newDate);
		logger.info("<<< <<< <<< 结束: SettleTransProfit封装账务接口入参TransOrderInfo");
		return transOrderInfo;
	}
	/**
	 * 
	 * @param bill
	 * @param transferAmount
	 * @return
	 * @throws Exception
	 */
	private TransOrderInfo createTransOrderInfo(SettleTransBill bill,
			Long transferAmount) throws Exception {
		logger.info(">>> >>> >>> 开始: SettleTransBill封装账务接口入参TransOrderInfo");
		TransOrderInfo transOrderInfo = new TransOrderInfo();
		SimpleDateFormat formatDate = new SimpleDateFormat("yyyyMMddhhmmssSSS");
		Date newDate = new Date();
		String remark = "挂账单结算";
		transOrderInfo.setDealProductCode(bill.getObligate2());
		transOrderInfo.setOrderAmount(transferAmount);
		transOrderInfo.setUserId(bill.getUserId());
		transOrderInfo.setInterUserId(bill.getInterUserId());
		transOrderInfo.setRootInstCd(bill.getRootInstCd());
		transOrderInfo.setOrderCount(1);
		transOrderInfo.setOrderDate(newDate);
		transOrderInfo.setOrderNo("Settle" + formatDate.format(newDate));
//		transOrderInfo.setStatusId(1);
//		transOrderInfo.setUserFee(0L);
//		transOrderInfo.setProductId(bill.getProductId());
//		transOrderInfo.setIntoProductId(bill.getIntoProductId());
		transOrderInfo.setUserIpAddress(SettleConstants.LOCALHOST_URL);
		transOrderInfo.setRequestTime(newDate);
		transOrderInfo.setRemark(remark);
		logger.info("<<< <<< <<< 结束: SettleTransBill封装账务接口入参TransOrderInfo");
		return transOrderInfo;
	}
	/**
	 * 封装账务记账接口
	 * @param mapTransOrderInfoList
	 * @return
	 */
	private Map<String, MultTransOrderInfo> createMapMultTransOrderInfo(Map<String, List<TransOrderInfo>> mapTransOrderInfoList) {
		logger.info(">>> >>> >>> 开始: 创建MultTransOrderInfo对象Map结构体");
		Map<String, MultTransOrderInfo> mapMultTransOrderInfo = new HashMap<String, MultTransOrderInfo>();
		List<TransOrderInfo> transOrderInfoList = null;
		MultTransOrderInfo multTransOrderInfo = null;
		TransOrderInfo transOrderInfo = null;
		List<SubTransOrderInfo> subTransOrderInfoList = null;
		SubTransOrderInfo subTransOrderInfo = null;
		Integer executeSequence = 1;
		
		for (String key : mapTransOrderInfoList.keySet()) {
			transOrderInfoList = mapTransOrderInfoList.get(key);
			transOrderInfo = transOrderInfoList.get(0);
			multTransOrderInfo = new MultTransOrderInfo();
			subTransOrderInfoList = new ArrayList<SubTransOrderInfo>();
			for (TransOrderInfo t : transOrderInfoList) {
				subTransOrderInfo = new SubTransOrderInfo();
//				subTransOrderInfo.setIntoRootInstCd(t.getIntoRootInstCd());
//				subTransOrderInfo.setInterUserId(t.getInterUserId());
//				subTransOrderInfo.setIntoProductId(t.getIntoProductId());
//				subTransOrderInfo.setInterRoleCode(t.getInterRoleCode());
//				subTransOrderInfo.setRemark(t.getRemark());
//				subTransOrderInfo.setAccountProperty(ACCOUNT_PROPERTY_INNER);
//				subTransOrderInfo.setProductId(t.getProductId());
				subTransOrderInfo.setRootInstCd(t.getRootInstCd());
				subTransOrderInfo.setUserId(t.getUserId());
				subTransOrderInfo.setRoleCode(t.getRoleCode());
				subTransOrderInfo.setOrderAmount(t.getOrderAmount());
				subTransOrderInfo.setOrderNo(t.getOrderNo());
				subTransOrderInfo.setExecuteSequence(executeSequence ++);
				subTransOrderInfoList.add(subTransOrderInfo);
			}
			multTransOrderInfo.setDealProductCode(transOrderInfo.getDealProductCode());
			multTransOrderInfo.setOrderCount(subTransOrderInfoList.size());
			multTransOrderInfo.setOrderDate(transOrderInfo.getOrderDate());
			multTransOrderInfo.setOrderPackageNo(transOrderInfo.getOrderPackageNo());
			multTransOrderInfo.setRootInstCd(transOrderInfo.getRootInstCd());
			multTransOrderInfo.setSubTransOrderInfoList(subTransOrderInfoList);
			multTransOrderInfo.setUserIpAddress(transOrderInfo.getUserIpAddress());
			multTransOrderInfo.setRoleCode(transOrderInfo.getRoleCode());
			mapMultTransOrderInfo.put(key, multTransOrderInfo);
		}
		logger.info("<<< <<< <<< 结束: 创建MultTransOrderInfo对象Map结构体");
		return mapMultTransOrderInfo;
	}
}