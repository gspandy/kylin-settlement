package com.rkylin.settle.service.impl;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.activemq.command.ActiveMQQueue;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.rkylin.crps.dto.BatchTransDto;
import com.rkylin.crps.dto.BatchTransRespDto;
import com.rkylin.crps.dto.SingleTransRespDto;
import com.rkylin.crps.dto.TransDetailDto;
import com.rkylin.crps.dto.TransDetailRespDto;
import com.rkylin.crps.dto.TransQueryDto;
import com.rkylin.crps.pojo.OrderDetail;
import com.rkylin.crps.pojo.OrderDetails;
import com.rkylin.crps.service.CrpsAgentPayService;
import com.rkylin.crps.service.NotifyService;
import com.rkylin.order.mixservice.SettlementToOrderService;
import com.rkylin.order.pojo.OrderAccountInfo;
import com.rkylin.order.pojo.OrderPayment;
import com.rkylin.order.service.OrderAccountInfoService;
import com.rkylin.settle.constant.Constants;
import com.rkylin.settle.constant.SettleConstants;
import com.rkylin.settle.filedownload.CheckfileDownload;
import com.rkylin.settle.filedownload.ROPFileDown;
import com.rkylin.settle.listen.DsfMessageListener;
import com.rkylin.settle.logic.DsfLogic;
import com.rkylin.settle.manager.ParameterInfoManager;
import com.rkylin.settle.manager.SettleParameterInfoManager;
import com.rkylin.settle.manager.SettleTransDetailManager;
import com.rkylin.settle.manager.SettleTransInvoiceHisManager;
import com.rkylin.settle.manager.SettleTransInvoiceManager;
import com.rkylin.settle.manager.SettleTransSummaryHisManager;
import com.rkylin.settle.manager.SettleTransSummaryManager;
import com.rkylin.settle.pojo.SettleParameterInfo;
import com.rkylin.settle.pojo.SettleParameterInfoQuery;
import com.rkylin.settle.pojo.SettleTransDetail;
import com.rkylin.settle.pojo.SettleTransDetailQuery;
import com.rkylin.settle.pojo.SettleTransInvoice;
import com.rkylin.settle.pojo.SettleTransInvoiceHis;
import com.rkylin.settle.pojo.SettleTransInvoiceHisQuery;
import com.rkylin.settle.pojo.SettleTransInvoiceQuery;
import com.rkylin.settle.pojo.SettleTransSummary;
import com.rkylin.settle.pojo.SettleTransSummaryHis;
import com.rkylin.settle.pojo.SettleTransSummaryHisQuery;
import com.rkylin.settle.pojo.SettleTransSummaryQuery;
import com.rkylin.settle.service.DsfService;
import com.rkylin.settle.util.LogicConstantUtil;
import com.rkylin.settle.util.RegexUtil;
import com.rkylin.settle.util.SettlementUtil;
import com.rkylin.utils.SendSMS;
import com.rkylin.wheatfield.api.AccountManagementService;
import com.rkylin.wheatfield.api.TransOrderDubboService;
import com.rkylin.wheatfield.pojo.AccountInfo;
import com.rkylin.wheatfield.pojo.AccountInfoQuery;
import com.rongcapital.corpbanking.pojo.payment.BatchCorpBankPay;
import com.rongcapital.corpbanking.pojo.payment.BatchCorpBankPayResp;
import com.rongcapital.corpbanking.pojo.payment.CorpBankPay;
import com.rongcapital.corpbanking.pojo.payment.CorpBankPayQuery;
import com.rongcapital.corpbanking.pojo.payment.CorpBankPayResp;
import com.rongcapital.corpbanking.service.CorpBankPayService;
import com.rongcapital.corpbanking.service.CorpBankingNotifyService;
import com.rongcapital.mtaegis.po.AccountInfoExt;
import com.rongcapital.mtaegis.po.CorporatAccountInfoExt;
import com.rongcapital.mtaegis.po.MultTransOrderInfoReturn;
//import com.rongcapital.mtaegis.po.TransOrderCashPayInfo;
import com.rongcapital.mtaegis.po.TransOrderInfo;
import com.rongcapital.mtaegis.po.TransOrderInfoResult;
import com.rongcapital.mtaegis.po.User;
import com.rongcapital.mtaegis.response.AccountInfoRes;
import com.rongcapital.mtaegis.response.CommonResponse;
import com.rongcapital.mtaegis.response.CorAccountInfoRes;
import com.rongcapital.mtaegis.response.MultBaseResultReturnResponse;
import com.rongcapital.mtaegis.response.UpdateOrderRes;
import com.rongcapital.mtaegis.service.AccountInfoExtApi;
import com.rongcapital.mtaegis.service.CorporatAccountInfoApi;
import com.rongcapital.mtaegis.service.MultCollectionResultReturnApi;
import com.rongcapital.mtaegis.service.MultWithholdAndWithdrowResultReturnApi;
import com.rongcapital.mtaegis.service.RefundApi;
import com.rongcapital.mtaegis.service.TransOrderInfoApi;

@Service("dsfService")
public class DsfServiceImpl implements DsfService, NotifyService,
		CorpBankingNotifyService {
	// 日志对象
	protected static Logger logger = LoggerFactory
			.getLogger(DsfServiceImpl.class);
//	private static final String CHANNEL_NO_SPLIT = "_"; // 多渠道渠道名称分隔符例如TongLian_AgentPay
//	private static final String PAY_CHANNEL_ID_SPLIT = ","; // 清结算渠道ID和协议CODE的分隔符
//	private static final String AGREEMENT_CODE = "agreementCode"; // 协议号key
//	private static final String CHANNEL_CODE = "channelCode"; // 渠道名称key
	private static final String CMBC_BANK_CODE = "305"; // 民生银行bankCode
	private static final String CORPBANKPAY_SYS_NO = "qjs001"; // 银企直联系统code
	private static final String CORPBANKPAY_SYS_KEY = "qjs10001"; // 银企直联接口MD5Key
	private static final String CORPBANKPAY_PAY = "16011"; // 银企直联支付
	private static final String CORPBANKPAY_BATCH_PAY = "16012"; // 银企直联批量支付
	private static final String CORPBANKPAY_QUERY = "10001";	//
	@Autowired
	private DsfLogic dsfLogic;

	@Autowired
	private SettlementUtil settlementUtil;

	@Autowired
	private OrderAccountInfoService orderAccountInfoService;// 订单系统的服务

	@Autowired
	private CrpsAgentPayService crpsAgentPayService;// 代收付的服务

	@Autowired
	private CorpBankPayService corpBankPayService;// 银企直联服务

	@Autowired
	private SettleTransInvoiceManager settleTransInvoiceManager;// 结算表Manager

	@Autowired
	private SettleTransInvoiceHisManager settleTransInvoiceHisManager;// 结算表Manager

	@Autowired
	private SettleTransDetailManager settleTransDetailManager;// 交易信息Manager

	@Autowired
	private SettleTransSummaryManager settleTransSummaryManager;// 汇总表Manager

	@Autowired
	private SettleTransSummaryHisManager settleTransSummaryHisManager;// 汇总表Manager

	@Autowired
	private ParameterInfoManager parameterInfoManager; // 参数表manager

	@Autowired
	private RefundApi refundService;// 退票的服务

	@Autowired
	private CheckfileDownload checkfileDownload; // 查询备付金余额

	@Autowired
	private JmsTemplate jmsTemplate;

	@Autowired
	private ActiveMQQueue queueDest;

	@Autowired
	private SettleParameterInfoManager settleParameterInfoManager; // 清算参数表manager

	@Autowired
	private SettlementToOrderService settlementToOrderService;// 返回代收付结果给订单系统

	@Autowired
	private AccountInfoExtApi accountInfoExtApi;// 账户系统银行卡信息查询服务

	@Autowired
	private CorporatAccountInfoApi corporatAccountInfoApi;// 账户系统一分钱代付银行卡信息查询的服务

	@Autowired
	private AccountManagementService accountManagementService;// 账户一期查询银行卡信息

	@Autowired
	private TransOrderInfoApi transOrderInfoApi;// 返回一分钱代收付结果给账户系统

	@Autowired
	private TransOrderDubboService transOrderDubboService;// 返回代收付结果给账户系统

	@Autowired
	private LogicConstantUtil logicConstantUtil;

	@Autowired
	private MultWithholdAndWithdrowResultReturnApi multWithholdAndWithdrowResultReturnApi;// 返回代付结果给账户系统

	@Autowired
	private MultCollectionResultReturnApi multCollectionResultReturnApi;// 返回代收结果给账户系统

	@Autowired
	private DsfMessageListener dsfMessageListener;

	@Autowired
	private ROPFileDown rOPFileDown;

	// String mobile = "13581558215,18701022977,18601062528,15001304318";
	String mobile = "13581558215,18701022977";// 暂时用自己手机号测试
	// 短信模板: xx系统xx环境，编号××存在问题，请及时解决!
	String content = "清算系统测试环境，编号" + Constants.SMS_NUMBER_0 + "存在问题，请及时解决！";// 编号0指备付金余额不足
	String content1 = "清算系统测试环境，编号" + Constants.SMS_NUMBER_1 + "存在问题，请及时解决！";// 编号1指退票发送账户失败或异常

	/**
	 * 汇总 发送代收付
	 */
	public void dealDsf(Integer[] ids, String flag, String[] rootInstCds,
			Integer inStep, String[] funcCodes, String[] businessTypes) {
		logger.info(">>> >>> >>> >>> >>>开始执行'代收付汇总方法dealDsf' ... ...");
		/*
		 * 判断手工操作
		 */
		Boolean manualFlag = false;// 手工处理的标记，true手工处理，false系统处理
		if (ids != null) manualFlag = true;// 手工汇总
		/*
		 * 组织查询交易表的条件
		 */
		Map<String, Object> detailwhereMap = null;
		try {
			detailwhereMap = this.queryParams(ids, flag,
					rootInstCds, inStep, funcCodes, businessTypes, manualFlag);// 组织代收付交易的查询条件
			if (detailwhereMap == null || detailwhereMap.size() <= 0) {
				logger.error(">>> >>> >>> 异常 组织查询交易表的条件返回值为null ！ detailwhereMap is Null or detailwhereMap.size() <= 0");
				return;
			}
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常 组织查询交易表的条件 ！" + e.getMessage(), e);
			throw new RuntimeException(e.getMessage());
		}
		
		List<SettleParameterInfo> paramList = this
				.getMerchantConvertRelation();// 获取机构号映射关系
		Map<String, Object> amountMap = null;// 用于存放汇总后的金额(根据机构号+用户ID+交易类型汇总)
		Map<String, Object> detailBeanMap = null;// 存放SettleDetail的实体对象,机构号+用户ID+交易类型都相同的只存一个
		Set<String> merChantAndFuncSet = null;// 存放机构号(机构号的数量应该很少)
		Map<String, String> merAndFuncResNoMap = null;// 给每个"机构号+func_code"生成对应的交易批次号,并放入map中
		Boolean dsfOnOff = false;// 是否汇总,初始值为false
		List<SettleTransInvoice> transInvoiceList = null;// list存结算表bean
		List<SettleTransSummary> transSummeryList = null;// list存汇总表bean
		List<SettleTransDetail> transDetailList = null;// list存交易表bean
		Map<String, Object> theDetailMap = null;
		Map<String, Object> tmpDetailMap = null;
		String requestNoKey = null;
		BigDecimal amount = new BigDecimal("0");
		String merchantCode = null;
		List<String> rootInstCdsList = settleTransDetailManager
				.selectRootInstCds(detailwhereMap);// 查询满足条件的机构号
		Iterator<String> rootInstCdsIter = rootInstCdsList.iterator();
		while (rootInstCdsIter.hasNext()) {
			amountMap = new HashMap<String, Object>();
			detailBeanMap = new HashMap<String, Object>();
			merChantAndFuncSet = new HashSet<String>();
			transInvoiceList = new ArrayList<SettleTransInvoice>();
			transSummeryList = new ArrayList<SettleTransSummary>();
			transDetailList = new ArrayList<SettleTransDetail>();
			merchantCode = rootInstCdsIter.next();
			detailwhereMap.put("merchantCode", merchantCode);
			List<Map<String, Object>> dsfDetailList = null;
			/*
			 * 查询未处理的代收付交易
			 */
			try {
				dsfDetailList = dsfLogic.getDsfTransDetail(detailwhereMap);// 查询未处理的代收付交易
			} catch(Exception e) {
				logger.error(">>> >>> >>> 异常:查询未处理的代收付交易", e);
				throw new RuntimeException(e.getMessage());
			}
			/*
			 * 系统发起的汇总判断汇总开关是否打开
			 */
			try {
				if (manualFlag == false) dsfOnOff = this.getDsfOnOff(merchantCode);// 系统发起的汇总判断汇总开关是否打开
			} catch (Exception e) {
				logger.error(">>> >>> >>> 异常:系统发起的汇总判断汇总开关是否打开", e);
				throw new RuntimeException(e.getMessage());
			}
			/*
			 * 汇总detail表的数据
			 */
			try {
				this.collectDetail(dsfOnOff, dsfDetailList, amountMap, detailBeanMap, merChantAndFuncSet);// 汇总detail表的数据
			} catch (Exception e) {
				logger.error(">>> >>> >>> 异常:汇总detail表的数据", e);
				throw new RuntimeException(e.getMessage());
			}
			/*
			 * 给每个机构号生成对应的交易批次号,并放入map中
			 */
			try {
				merAndFuncResNoMap = this.createMerchantRequestNo(merChantAndFuncSet);
			} catch (Exception e) {
				logger.error(">>> >>> >>> 异常:给每个机构号生成对应的交易批次号,并放入map中", e);
				throw new RuntimeException(e.getMessage());
			}
			Iterator<Map<String, Object>> dsfDetailIter = dsfDetailList.iterator();
			/**
			 * 暂存4013交易的list, 4013 交易永远不汇总
			 */
			List<Map<String, Object>> dsfDetal4013List = null;
			if (dsfOnOff) {// 汇总开关打开 //改! 代收 4013 交易永远不汇总
				logger.info(">>>汇总开关打开，给机构号生成批次号");
				// 实例化 暂存4013交易的list
				dsfDetal4013List = new ArrayList<Map<String, Object>>();
				Set<String> detailKeySet = detailBeanMap.keySet();// 过滤掉了
																	// 4013交易
				Map<String, String> batchNoMap = this
						.createBatchNoMap(detailKeySet);
				logger.info(">>>汇总开关打开,进行汇总的第一段逻辑,处理交易表和汇总表");
				while (dsfDetailIter.hasNext()) {
					theDetailMap = dsfDetailIter.next();
					if ("4013".equals(String.valueOf(theDetailMap
							.get("FUNC_CODE")))) {
						logger.info(">>> 4013 交易永远不汇总 dsfDetal4013List临时存储4013交易");
						dsfDetal4013List.add(theDetailMap);
						continue;
					}
					/*
					 * 向代收付相关表中插入或更新数据
					 */
					try {
						this.organizeDetailAndSummaryToList(batchNoMap,theDetailMap, transDetailList, transSummeryList);
					} catch (Exception e) {
						logger.error(">>> >>> >>> 异常:向代收付相关表中插入或更新数据", e);
						throw new RuntimeException(e.getMessage());
					}
				}
				logger.info(">>>汇总开关打开,开始进行第二段逻辑,汇总汇总表数据到结算表");
				String tmpKey = null;
				Iterator<String> ite = detailKeySet.iterator();
				while (ite.hasNext()) {
					tmpKey = ite.next();// tmpKey = 机构号+用户ID+func_code+账期
					tmpDetailMap = this.putInvoiceParam(tmpKey, amountMap,
							merAndFuncResNoMap, batchNoMap, detailBeanMap);
					/*
					 * 向代收付相关表中插入或更新数据
					 */
					try {
						this.organizeInvoiceToList(tmpDetailMap, transDetailList, transSummeryList, transInvoiceList, dsfOnOff, manualFlag, paramList);
					} catch (Exception e) {
						logger.error(">>> >>> >>> 异常:向代收付相关表中插入或更新数据", e);
						throw new RuntimeException(e.getMessage());
					}
				}
			} else {// 汇总开关为false
				logger.info(">>>汇总开关关闭,逐条往代收付先关表写入数据");
				while (dsfDetailIter.hasNext()) {
					theDetailMap = dsfDetailIter.next();
					requestNoKey = String.valueOf(theDetailMap
							.get("MERCHANT_CODE"))
							+ String.valueOf(theDetailMap.get("FUNC_CODE"))
							+ String.valueOf(theDetailMap
									.get("PAY_CHANNEL_ID")); // 获取交易批次号的KEY
					amount = new BigDecimal(String.valueOf(theDetailMap
							.get("AMOUNT")));// 金额
					/*
					 * map中压入批次号、交易批次号、单条金额
					 */
					try {
						
						theDetailMap = this.updateTheDetailMap(theDetailMap,
								merAndFuncResNoMap, requestNoKey, amount);
					} catch (Exception e) {
						logger.error(">>> >>> >>> 异常:map中压入批次号、交易批次号、单条金额", e);
						throw new RuntimeException(e.getMessage());
					}
					/*
					 * 向代收付相关表中插入或更新数据
					 */
					try {
						this.organizeBeanInfoToList(theDetailMap,
								transDetailList, transSummeryList,
								transInvoiceList, dsfOnOff, manualFlag,
								paramList);
					} catch (Exception e) {
						logger.error(">>> >>> >>> 异常:向代收付相关表中插入或更新数据", e);
						throw new RuntimeException(e.getMessage());
					}
				}
			}
			/**
			 * 4013 交易永远不汇总 调用汇总开关为false 逻辑
			 */
			if (dsfDetal4013List != null && dsfDetal4013List.size() > 0) {
				logger.info(">>> 4013 交易永远不汇总 调用汇总开关为false 逻辑开始");
				boolean tempDsfOnOff = dsfOnOff;
				dsfOnOff = false;
				dsfDetailIter = dsfDetal4013List.iterator();
				while (dsfDetailIter.hasNext()) {
					theDetailMap = dsfDetailIter.next();
					requestNoKey = String.valueOf(theDetailMap
							.get("MERCHANT_CODE"))
							+ String.valueOf(theDetailMap.get("FUNC_CODE")
									+ String.valueOf(theDetailMap
											.get("PAY_CHANNEL_ID"))); // 获取交易批次号的KEY
					amount = new BigDecimal(String.valueOf(theDetailMap
							.get("AMOUNT")));// 金额
					
					/*
					 * map中压入批次号、交易批次号、单条金额
					 */
					try {
						theDetailMap = this.updateTheDetailMap(theDetailMap,
								merAndFuncResNoMap, requestNoKey, amount);
					} catch (Exception e) {
						logger.error(">>> >>> >>> 异常:map中压入批次号、交易批次号、单条金额", e);
						throw new RuntimeException(e.getMessage());
					}
					/*
					 * 向代收付相关表中插入或更新数据
					 */
					try {
						this.organizeBeanInfoToList(theDetailMap,
								transDetailList, transSummeryList,
								transInvoiceList, dsfOnOff, manualFlag,
								paramList);
					} catch (Exception e) {
						logger.error(">>> >>> >>> 异常:向代收付相关表中插入或更新数据", e);
						throw new RuntimeException(e.getMessage());
					}
				}
				dsfOnOff = tempDsfOnOff;
				logger.info("<<< 4013 交易永远不汇总 调用汇总开关为false 逻辑结束");
			}
			/*
			 * 修改清结算代收付汇总数据
			 */
			try {
				this.dealDsfUpdateTrans(transInvoiceList, transSummeryList, transDetailList);
			} catch (Exception e) {
				logger.error(">>> >>> >>> 异常:修改清结算代收付汇总数据", e);
				throw new RuntimeException(e.getMessage());
			}
		}
		logger.info(">>> >>> >>> >>> >>>结束执行　DsfService'代收付汇总' ... ...");
	}

	/**
	 * 组织结算表的交易批次号，金额，批次号到Map中
	 * 
	 * @param tmpKey
	 * @param amountMap
	 * @param merAndFuncResNoMap
	 * @param batchNoMap
	 * @param tmpDetailMap
	 * @param detailBeanMap
	 */
	public Map<String, Object> putInvoiceParam(String tmpKey,
			Map<String, Object> amountMap,
			Map<String, String> merAndFuncResNoMap,
			Map<String, String> batchNoMap, Map<String, Object> detailBeanMap) {
		String reqNo = null;
		String tmpBatchNo = null;
		String requestNoKey = null;
		Map<String, Object> tmpDetailMap = null;
		try {
			BigDecimal amount = new BigDecimal("0");
			tmpDetailMap = (Map<String, Object>) detailBeanMap.get(tmpKey);// 得到交易表的实体beanMap
			requestNoKey = String.valueOf(tmpDetailMap.get("MERCHANT_CODE"))
					+ String.valueOf(tmpDetailMap.get("FUNC_CODE")
							+ String.valueOf(tmpDetailMap.get("PAY_CHANNEL_ID")));// 用来取交易批次号的key
			amount = new BigDecimal(String.valueOf(amountMap.get("M" + tmpKey)));// 汇总金额
			tmpBatchNo = batchNoMap.get(tmpKey);
			reqNo = merAndFuncResNoMap.get(requestNoKey); // 交易批次号
			tmpDetailMap.put("reqNo", reqNo);
			tmpDetailMap.put("totalAmount", amount);// 单条金额或汇总金额
			tmpDetailMap.put("batchNo", tmpBatchNo);
		} catch (Exception e) {
			logger.error(">>> >>>组织结算表的交易批次号，金额，批次号到Map中发生异常！！！！！！errorMsg:"
					+ e.getMessage());
		}
		return tmpDetailMap;
	}

	/**
	 * 组织查询交易表的条件
	 * 
	 * @param ids
	 *            交易表的主键ID构成的数组 null:定时任务触发, 非null:页面手动触发时此参数为页面选中交易的主键ID
	 * @param flag
	 *            标记值, 0:代付和提现,1:代收和一分钱代付,2:课栈T0提现,其他值：代收、代付、提现、一分钱代付、课栈T0提现,5:
	 *            杏仁现金贷
	 * @param rootInstCds
	 *            机构号 null:全机构
	 * @param inStep
	 *            步长,用于计算账期 【逻辑中可能会对此字段进行重新复制】
	 * @param funcCodes
	 *            功能编码 ,4013:代收,4014：代付，4014_1一分钱代付，4016：提现 【逻辑中可能会对此字段进行重新复制】
	 * @param businessTypes
	 *            业务类型：值为1是课栈T0提现，其他值(含null)则是其它业务
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> queryParams(Integer[] ids, String flag,
			String[] rootInstCds, Integer inStep, String[] funcCodes,
			String[] businessTypes, Boolean manualSendFlag) throws Exception {
		Map<String, Object> detailwhereMap = new HashMap<String, Object>();
		detailwhereMap.put("readStatusId", 1);
		detailwhereMap.put("notStatusId", 99);
		detailwhereMap.put("orderType", 0);
		detailwhereMap.put("dFlags", new Integer[] { 0, 99 });
		if (rootInstCds != null && rootInstCds.length > 0) detailwhereMap.put("rootInstCds", rootInstCds);
		if (manualSendFlag) {// 手工汇总
			detailwhereMap.put("ids", ids);
		} else {// 系统触发,只查询未处理的代收付交易
			logger.info("###########funcCodes="
					+ detailwhereMap.get("funcCodes"));
			logger.info("###########flag=" + flag);
			String settleDate = String.valueOf(settlementUtil.getAccountDate(
					"yyyy-MM-dd", 0, ""));
			String settleDateP = null;
			int step = -1;
			if (inStep != null)
				step = inStep.intValue();

			if ("0".equals(flag)) {//T-1 的代付和提现
				if (funcCodes != null) {
					detailwhereMap.put("funcCodes", funcCodes);
				} else {
					detailwhereMap.put("funcCodes", new String[] { "4014","4016" });// 4014代付、4016提现
				}
			} else if ("2".equals(flag)) {//课栈T0提现
				if (funcCodes != null) {
					detailwhereMap.put("funcCodes", funcCodes);
				} else {
					detailwhereMap.put("funcCodes", new String[] { "4016" });// 4016提现
				}
				step = 0;// 课栈T0提现
			} else if ("1".equals(flag)) {//代收 和 有T0标记的代付(一分钱代付)
				if (funcCodes != null) {
					detailwhereMap.put("funcCodes", funcCodes);
				} else {
					logger.error(">>>> >>> 代收或一分钱代付汇总时 funcCodes is Null");
				}
			} else if ("5".equals(flag)) {//杏仁现金贷T0
				step = 0;
			} else {
				if (funcCodes != null) {
					detailwhereMap.put("funcCodes", funcCodes);
				} else {
					detailwhereMap.put("funcCodes", new String[] { "4014","4016", "4013", "4014_1" });
				}
			}
			settleDateP = String.valueOf(settlementUtil.getAccountDate(settleDate, "yyyy-MM-dd", step, ""));
			detailwhereMap.put("accountDate", settleDateP);// 账期
			if (businessTypes != null) detailwhereMap.put("businessTypes", businessTypes);
		}
		logger.info("ErrorMsg=" + detailwhereMap.get("ErrorMsg"));

		return detailwhereMap;
	}

	/**
	 * 获得机构号的转换关系,例如：有些机构可能需要转换成融数的机构号
	 * 
	 * @return
	 */
	public List<SettleParameterInfo> getMerchantConvertRelation() {
		List<SettleParameterInfo> paramList = null;
		try {
			SettleParameterInfoQuery paramQuery = new SettleParameterInfoQuery();
			paramQuery
					.setParameterType(SettleConstants.PARAMETER_TYPE_DSF_MERCHANT);
			paramList = settleParameterInfoManager.queryList(paramQuery);// 获取机构号映射关系
		} catch (Exception e) {
			logger.error("获得机构号的转换关系方法发生异常!!!!!,errorMsg:" + e.getMessage()
					+ e.getCause());
		}
		return paramList;
	}

	/**
	 * 生成批次号,注意不是交易批次号
	 * 
	 * @param 生成批次号的key集合
	 * @return
	 */
	public Map<String, String> createBatchNoMap(Set<String> detailKeySet) {
		logger.info(">>>给'机构号+用户ID+funcCode+账期'生成对应的批次号");
		String batchNo = null;
		String tmpBeanKey = null;
		Map<String, String> batchNoMap = null;
		try {
			batchNoMap = new HashMap<String, String>();
			Iterator<String> tmpIterator = detailKeySet.iterator();
			while (tmpIterator.hasNext()) {
				tmpBeanKey = tmpIterator.next();
				batchNo = settlementUtil.createBatchNo("BatchNo");// 生成批次号
				batchNoMap.put(tmpBeanKey, batchNo);
			}
		} catch (Exception e) {
			logger.error("机构号+用户ID+funcCode+账期相同的交易生成批次号发生异常！！！！"
					+ e.getMessage() + e.getCause());
		}
		return batchNoMap;
	}

	/**
	 * 处理交易表和汇总表
	 * 
	 * @param theDetailMap
	 * @param transDetailList
	 * @param transSummeryList
	 * @param transInvoiceList
	 * @param dsfOnOff
	 * @throws Exception
	 */
	public void organizeDetailAndSummaryToList(Map<String, String> batchNoMap,
			Map<String, Object> theDetailMap,
			List<SettleTransDetail> transDetailList,
			List<SettleTransSummary> transSummeryList) throws Exception {
		logger.info(">>>>>>插入汇总表并更新交易表,trans_detail_id="
				+ theDetailMap.get("TRANS_DETAIL_ID"));
		try {
			Map<String, Object> resultMap = this
					.publicQueryCardInfo(theDetailMap);
			Boolean isCardInfoNull = (Boolean) resultMap.get("isCardInfoNull");
			if (isCardInfoNull) {
				// this.updateOneDetailDflag(theDetailMap);
				logger.info("更新单条交易表的绑卡信息为空,orderNo="
						+ theDetailMap.get("ORDER_NO"));
				SettleTransDetail detailBean = new SettleTransDetail();
				detailBean.setTransDetailId(Integer.parseInt(String
						.valueOf(theDetailMap.get("TRANS_DETAIL_ID"))));
				detailBean.setDflag(99);
				detailBean.setObligate1("绑卡信息为空");
				transDetailList.add(detailBean);
			} else {
				// 给汇总表SETTLE_TRANS_SUMMARY组织信息，并放入list
				SettleTransSummary summaryBean = this.createSettleTransSummary(
						theDetailMap, batchNoMap);
				transSummeryList.add(summaryBean);
				// 将待修改dflag值的交易信息放入交易表list
				SettleTransDetail detailBean = new SettleTransDetail();
				detailBean.setTransDetailId(Integer.parseInt(String
						.valueOf(theDetailMap.get("TRANS_DETAIL_ID"))));
				detailBean.setDflag(SettleConstants.STATUSID_1);// 处理中
				detailBean.setObligate1("");
				transDetailList.add(detailBean);
			}
		} catch (Exception e) {
			logger.error(">>>>>>插入汇总表并更新交易表,报错！！！！！！！！,trans_detail_id="
					+ theDetailMap.get("TRANS_DETAIL_ID"));
			throw new Exception(e.getMessage());
		}
		logger.info(">>>>>>结束方法:插入汇总表并更新交易表,trans_detail_id="
				+ theDetailMap.get("TRANS_DETAIL_ID"));
	}

	/**
	 * 查询银行卡信息
	 */
	public Map<String, Object> publicQueryCardInfo(
			Map<String, Object> theDetailMap) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			Boolean isCardInfoNull = false;// 标记银行卡信息，默认不为空
			OrderAccountInfo orderAccountInfo = null;// 订单系统的卡信息对象
			CorporatAccountInfoExt corporatAccountInfo = null;// 账户系统二期的卡信息对象(一分钱代付)
			AccountInfoExt accountInfoExt = null;// 账户系统二期的卡信息对象
			AccountInfo accountInfo = null; // 账户一期绑卡信息
			String merchantCode = String.valueOf(theDetailMap
					.get("MERCHANT_CODE"));// 机构号
			String userId = String.valueOf(theDetailMap.get("USER_ID"));// 用户ID
			String businessType = String.valueOf(theDetailMap
					.get("BUSINESS_TYPE"));// 业务类型
			String funcCode = String.valueOf(theDetailMap.get("FUNC_CODE"));// 功能编码
			String roleCode = String.valueOf(theDetailMap.get("ROLE_CODE"));// 角色编码
			String accProductCode = String.valueOf(theDetailMap
					.get("PRODUCT_ID"));// 产品号
			String dataFrom = String.valueOf(theDetailMap.get("DATA_FROM"));// 产品号
			String dealProductCode = String.valueOf(theDetailMap
					.get("DEAL_PRODUCT_CODE"));// 产品号
			String reserve1 = String.valueOf(theDetailMap.get("RESERVE_1"));// 产品号
			String interMerchantCode = String.valueOf(theDetailMap
					.get("INTER_MERCHANT_CODE"));//

			// 查询绑卡信息 ，1. 对于提现业务，先从订单系统查询绑卡信息，没查到，再从账户系统查询，2.对于其它业务直接从账户系统查询
			if (SettleConstants.BATCH_TX_FUN_CODE.equals(funcCode)) {// 提现
				String orderNo = String.valueOf(theDetailMap.get("ORDER_NO"));
				try {
					// 先从订单系统查询绑卡信息
					logger.info(">>>>>>>开始从订单系统查询银行卡信息,订单号:" + orderNo);
					orderAccountInfo = orderAccountInfoService
							.findAccountInfoByOrderId(orderNo, "1");
					logger.info(">>>>>>>结束从订单系统查询银行卡信息,订单号：" + orderNo);
				} catch (Exception e) {
					logger.error("根据订单号:" + orderNo + "在订单系统中没有找到相应的提现绑卡信息！");
				}

				if (orderAccountInfo == null) {
					if ("0".equals(dataFrom)) {
						if (StringUtils.isNotBlank(interMerchantCode)) {
							accountInfo = this.get1AccountCardInfo(
									merchantCode, interMerchantCode,
									accProductCode, funcCode);
						} else {
							accountInfo = this.get1AccountCardInfo(
									merchantCode, userId, accProductCode,
									funcCode);
						}

						if (accountInfo == null) {
							isCardInfoNull = true;
						}
					} else if ("4".equals(dataFrom)) {
						if (StringUtils.isNotBlank(interMerchantCode)) {
							accountInfoExt = this.getCardInfor(merchantCode,
									roleCode, accProductCode,
									interMerchantCode, funcCode, businessType);
						} else {
							accountInfoExt = this.getCardInfor(merchantCode,
									roleCode, accProductCode, userId, funcCode,
									businessType);
						}
						if (accountInfoExt == null) {
							logger.info(">>>>机构号:" + merchantCode + ",用户ID:"
									+ userId + "的绑卡信息为空");
							isCardInfoNull = true;
						}
					}
				}
			} else {
				if ("0".equals(dataFrom)) {// 一期
					if (SettleConstants.BATCH_DF_FUN_CODE.equals(funcCode)
							&& "YFYZ".equals(businessType)) {// 如果是1分钱代付卡信息
						accountInfo = this.get1AccountCardInfo(merchantCode,
								interMerchantCode, "YFQDF", funcCode);
					} else if (StringUtils.isNotBlank(interMerchantCode)) {
						accountInfo = this.get1AccountCardInfo(merchantCode,
								interMerchantCode, accProductCode, funcCode);
					} else {
						accountInfo = this.get1AccountCardInfo(merchantCode,
								userId, accProductCode, funcCode);
					}
					if (accountInfo == null) {
						isCardInfoNull = true;
					}
				} else if ("4".equals(dataFrom)) {// 账户二期数据
					// 从账户系统查询绑卡信息,一分钱代付并且PROD_00_RS_0064才从CORPORAT_ACCOUNT_INFO表查询卡信息，其他的一分钱或代收付从ACCOUNT_INFO表查卡信息
					if (SettleConstants.BATCH_DF_YFQ_FUN_CODE.equals(funcCode)
							&& "PROD_00_RS_0064".equals(dealProductCode)) {// 如果是1分钱代付卡信息
						corporatAccountInfo = this.getCorCardInfor(
								merchantCode, reserve1);
					} else if (StringUtils.isNotBlank(interMerchantCode)) {
						accountInfoExt = this.getCardInfor(merchantCode,
								roleCode, accProductCode, interMerchantCode,
								funcCode, businessType);
					} else {
						accountInfoExt = this.getCardInfor(merchantCode,
								roleCode, accProductCode, userId, funcCode,
								businessType);
					}
					if (corporatAccountInfo == null && accountInfoExt == null) {
						logger.info(">>>>机构号:" + merchantCode + ",用户ID:"
								+ userId + "的绑卡信息为空");
						isCardInfoNull = true;
					}
				} else {
					logger.info("userId=" + userId + "dataFrom不是0或4");
				}
			}
			resultMap.put("isCardInfoNull", isCardInfoNull);
			resultMap.put("orderAccountInfo", orderAccountInfo);
			resultMap.put("accountInfoExt", accountInfoExt);
			resultMap.put("corporatAccountInfo", corporatAccountInfo);
			resultMap.put("accountInfo", accountInfo);
		} catch (Exception e) {
			logger.info("查询银行卡信息的公共方法异常！！！！,errorMsg:" + e.getMessage()
					+ e.getCause());
		}
		return resultMap;
	}

	/**
	 * 插入并更新代收付涉及的相关表
	 * 
	 * @param theDetailMap
	 * @param transDetailList
	 * @param transSummeryList
	 * @param transInvoiceList
	 * @param dsfOnOff
	 * @throws Exception
	 */
	public void organizeBeanInfoToList(Map<String, Object> theDetailMap,
			List<SettleTransDetail> transDetailList,
			List<SettleTransSummary> transSummeryList,
			List<SettleTransInvoice> transInvoiceList, Boolean dsfOnOff,
			Boolean manualSendFlag, List<SettleParameterInfo> paramList)
			throws Exception {
		logger.info(">>>>>>开始方法:插入并更新代收付涉及的相关表+trans_detail_id="
				+ theDetailMap.get("TRANS_DETAIL_ID"));
		try {
			Map<String, Object> resultMap = this
					.publicQueryCardInfo(theDetailMap);
			Boolean isCardInfoNull = (Boolean) resultMap.get("isCardInfoNull");

			if (isCardInfoNull) {// 绑卡信息为空，只更新交易表，不再走后续流程(不用再向汇总表和结算表写入数据)
				if (dsfOnOff) {// 汇总开关开着需要更新一批数据(机构号+用户ID+交易类型+账期相同)
					List<SettleTransDetail> batchDetailList = this
							.updateBatchDetailDflag(theDetailMap);
					if (batchDetailList != null && batchDetailList.size() > 0) {
						transDetailList.addAll(batchDetailList);
					}
				} else {// 汇总开关关闭,只需更新一条
				// this.updateOneDetailDflag(theDetailMap);
					logger.info("更新单条交易表的绑卡信息为空,orderNo="
							+ theDetailMap.get("ORDER_NO"));
					SettleTransDetail detailBean = new SettleTransDetail();
					detailBean.setTransDetailId(Integer.parseInt(String
							.valueOf(theDetailMap.get("TRANS_DETAIL_ID"))));
					detailBean.setDflag(99);
					detailBean.setObligate1("绑卡信息为空");
					transDetailList.add(detailBean);
				}
			} else {
				// 给汇总表SETTLE_TRANS_SUMMARY组织信息，并放入list
				SettleTransSummary summaryBean = this
						.createSettleTransSummaryOne(theDetailMap);
				transSummeryList.add(summaryBean);
				// 给结算表组织信息，并放入list
				SettleTransInvoice invoiceBean = this.createSettleTransInvoice(
						theDetailMap, resultMap, manualSendFlag, paramList);
				transInvoiceList.add(invoiceBean);
				// 将待修改dflag值的交易信息放入交易表list
				SettleTransDetail detailBean = new SettleTransDetail();
				detailBean.setTransDetailId(Integer.parseInt(String
						.valueOf(theDetailMap.get("TRANS_DETAIL_ID"))));
				detailBean.setDflag(SettleConstants.STATUSID_1);// 处理中
				detailBean.setObligate1("");
				transDetailList.add(detailBean);
			}
		} catch (Exception e) {
			logger.error(">>>>>>插入并更新代收付涉及的相关表,报错！！！！！！！！");
			throw new Exception(e.getMessage());
		}
		logger.info(">>>>>>结束方法:插入并更新代收付涉及的相关表,+trans_detail_id="
				+ theDetailMap.get("TRANS_DETAIL_ID"));
	}

	/**
	 * 插入并更新代收付涉及的结算表111
	 * 
	 * @param theDetailMap
	 * @param transDetailList
	 * @param transSummeryList
	 * @param transInvoiceList
	 * @param dsfOnOff
	 * @throws Exception
	 */
	public void organizeInvoiceToList(Map<String, Object> theDetailMap,
			List<SettleTransDetail> transDetailList,
			List<SettleTransSummary> transSummeryList,
			List<SettleTransInvoice> transInvoiceList, Boolean dsfOnOff,
			Boolean manualSendFlag, List<SettleParameterInfo> paramList)
			throws Exception {
		logger.info(">>>>>>开始方法:插入并更新代收付涉及的相关表,orderNo="
				+ theDetailMap.get("ORDER_NO"));
		try {
			Map<String, Object> resultMap = new HashMap<String, Object>();
			Boolean isCardInfoNull = false;// 标记银行卡信息，默认不为空
			OrderAccountInfo orderAccountInfo = null;// 订单系统的卡信息对象
			CorporatAccountInfoExt corporatAccountInfo = null;// 账户二期系统的卡信息对象(一分钱代付)
			AccountInfoExt accountInfoExt = null;// 账户二期系统的卡信息对象
			AccountInfo accountInfo = null;// 账户一期系统的卡信息对象
			String merchantCode = String.valueOf(theDetailMap
					.get("MERCHANT_CODE"));// 机构号
			String userId = String.valueOf(theDetailMap.get("USER_ID"));// 用户ID
			String businessType = String.valueOf(theDetailMap
					.get("BUSINESS_TYPE"));// 业务类型
			String funcCode = String.valueOf(theDetailMap.get("FUNC_CODE"));// 功能编码
			String roleCode = String.valueOf(theDetailMap.get("ROLE_CODE"));// 角色编码
			String accProductCode = String.valueOf(theDetailMap
					.get("PRODUCT_ID"));// 产品号
			String dataFrom = String.valueOf(theDetailMap.get("DATA_FROM"));// 数据来源
																			// 0账户一期，4账户二期
			String dealProductCode = String.valueOf(theDetailMap
					.get("DEAL_PRODUCT_CODE"));// 产品号
			String reserve1 = String.valueOf(theDetailMap.get("RESERVE_1"));// 产品号
			String interMerchantCode = String.valueOf(theDetailMap
					.get("INTER_MERCHANT_CODE"));// 用户ID

			// 查询绑卡信息 ，1. 对于提现业务，先从订单系统查询绑卡信息，没查到，再从账户系统查询，2.对于其它业务直接从账户系统查询
			if (SettleConstants.BATCH_TX_FUN_CODE.equals(funcCode)) {// 提现
				String orderNo = String.valueOf(theDetailMap.get("ORDER_NO"));
				try {
					// 先从订单系统查询绑卡信息
					logger.info(">>>>>>>开始从订单系统查询银行卡信息,订单号:" + orderNo);
					orderAccountInfo = orderAccountInfoService
							.findAccountInfoByOrderId(orderNo, "1");
					logger.info(">>>>>>>结束从订单系统查询银行卡信息,订单号：" + orderNo);
				} catch (Exception e) {
					logger.error("根据订单号:" + orderNo + "在订单系统中没有找到相应的提现绑卡信息！");
				}
				if (orderAccountInfo == null) {
					if ("0".equals(dataFrom)) {
						if (StringUtils.isNotBlank(interMerchantCode)) {
							accountInfo = this.get1AccountCardInfo(
									merchantCode, interMerchantCode,
									accProductCode, funcCode);
						} else {
							accountInfo = this.get1AccountCardInfo(
									merchantCode, userId, accProductCode,
									funcCode);
						}

						if (accountInfo == null) {
							isCardInfoNull = true;
						}
					} else if ("4".equals(dataFrom)) {
						if (StringUtils.isNotBlank(interMerchantCode)) {
							accountInfoExt = this.getCardInfor(merchantCode,
									roleCode, accProductCode,
									interMerchantCode, funcCode, businessType);
						} else {
							accountInfoExt = this.getCardInfor(merchantCode,
									roleCode, accProductCode, userId, funcCode,
									businessType);
						}

						if (orderAccountInfo == null
								&& (accountInfoExt == null && "4"
										.equals(dataFrom))) {
							logger.info(">>>>机构号:" + merchantCode + ",用户ID:"
									+ userId + "的绑卡信息为空");
							isCardInfoNull = true;
						}
					}
				}
			} else {// 非提现业务
				if ("0".equals(dataFrom)) {// 一期
					if (SettleConstants.BATCH_DF_FUN_CODE.equals(funcCode)
							&& "YFYZ".equals(businessType)) {// 如果是1分钱代付卡信息
						accountInfo = this.get1AccountCardInfo(merchantCode,
								interMerchantCode, "YFQDF", funcCode);
					} else if (StringUtils.isNotBlank(interMerchantCode)) {
						accountInfo = this.get1AccountCardInfo(merchantCode,
								interMerchantCode, accProductCode, funcCode);
					} else {
						accountInfo = this.get1AccountCardInfo(merchantCode,
								userId, accProductCode, funcCode);
					}
					if (accountInfo == null) {
						isCardInfoNull = true;
					}
				} else if ("4".equals(dataFrom)) {// 二期
					// 从账户系统查询绑卡信息
					if (SettleConstants.BATCH_DF_YFQ_FUN_CODE.equals(funcCode)
							&& "PROD_00_RS_0064".equals(dealProductCode)) {// 如果是1分钱代付卡信息
						corporatAccountInfo = this.getCorCardInfor(
								merchantCode, reserve1);
					} else if (StringUtils.isNotBlank(interMerchantCode)) {
						accountInfoExt = this.getCardInfor(merchantCode,
								roleCode, accProductCode, interMerchantCode,
								funcCode, businessType);
					} else {
						accountInfoExt = this.getCardInfor(merchantCode,
								roleCode, accProductCode, userId, funcCode,
								businessType);
					}
					if (corporatAccountInfo == null && accountInfoExt == null) {
						logger.info(">>>>机构号:" + merchantCode + ",用户ID:"
								+ userId + "的绑卡信息为空");
						isCardInfoNull = true;
					}
				} else {
					logger.info("userId=" + userId + "dataFrom不是0或4");
				}
			}
			resultMap.put("isCardInfoNull", isCardInfoNull);
			resultMap.put("orderAccountInfo", orderAccountInfo);
			resultMap.put("accountInfoExt", accountInfoExt);
			resultMap.put("corporatAccountInfo", corporatAccountInfo);
			resultMap.put("accountInfo", accountInfo);

			if (!isCardInfoNull) {// 绑卡信息不为空才组织结算表信息
				// 给结算表组织信息，并放入list
				SettleTransInvoice invoiceBean = this.createSettleTransInvoice(
						theDetailMap, resultMap, manualSendFlag, paramList);
				transInvoiceList.add(invoiceBean);
			}
		} catch (Exception e) {
			logger.error(">>>>>>插入并更新代收付涉及的相关表,报错！！！！！！！！orderNo="
					+ theDetailMap.get("ORDER_NO"));
			throw new Exception(e.getMessage());
		}
		logger.info(">>>>>>结束方法:插入并更新代收付涉及的相关表,orderNo="
				+ theDetailMap.get("ORDER_NO"));
	}

	/**
	 * 1分钱代付卡信息查询
	 * 
	 * @param merchantCode
	 *            机构号
	 * @param userId
	 *            用户ID
	 * @return 卡信息对象
	 * @throws Exception
	 */
	public CorporatAccountInfoExt getCorCardInfor(String merchantCode,
			String reserve1) {
		logger.info(">>>>开始getCorCardInfor方法,merchantCode=" + merchantCode
				+ ",reserve1=" + reserve1);
		CorporatAccountInfoExt corporatAccountInfoExt = null;
		try {
			User user = new User();
			user.setRootInstCd(merchantCode);
			user.setAccountNumber(reserve1);
			user.setStatuses(new Integer[] { 3 });
			List<User> userList = new ArrayList<User>();
			userList.add(user);
			logger.info(">>>>>开始从账户系统查询绑卡信息(一分钱代付),RootInstCd=" + merchantCode
					+ ",AccountNumber=" + reserve1 + ",Statuses:" + "[3]");
			CorAccountInfoRes corCardResponse = corporatAccountInfoApi
					.batchQueryCorCards(userList);
			logger.info(">>>>>结束从账户系统查询绑卡信息(一分钱代付)");
			if (corCardResponse != null) {
				if ("1".equals(corCardResponse.getCode())) {
					logger.info("从账户系统查询绑卡信息(一分钱代付)返回成功,code=1");
					List<CorporatAccountInfoExt> corporatAccountInfoList = corCardResponse
							.getCorAccountInfoList();
					if (corporatAccountInfoList != null
							&& corporatAccountInfoList.size() > 0) {
						corporatAccountInfoExt = corporatAccountInfoList.get(0);
					} else {
						logger.info("机构号:" + merchantCode + ",reserve1:"
								+ reserve1 + "没有从账户系统查询到一分钱代付卡信息");
					}
				} else {
					logger.info("查询对一分钱代付卡信息,账户系统返回的msg="
							+ corCardResponse.getMsg() + ",code="
							+ corCardResponse.getCode());
				}
			} else {
				logger.info("查询对一分钱代付卡信息,账户系统返回的CorAccountInfoRes对象为空!");
			}
			logger.info(">>>>结束getCorCardInfor方法");
		} catch (Exception e) {
			logger.error(">>>>getCorCardInfor方法发生异常！！！！");
		}
		return corporatAccountInfoExt;
	}

	/**
	 * 从账户系统查询绑卡信息
	 * 
	 * @param merchantCode
	 *            机构号
	 * @param userId
	 *            用户ID
	 * @param productId
	 *            产品号
	 * @return
	 * @throws Exception
	 */
	public AccountInfoExt getCardInfor(String merchantCode, String roleCode,
			String accProductCode, String userId, String funcCode,
			String businessType) {
		logger.info(">>>>开始getCardInfor方法,merchantCode=" + merchantCode
				+ ",userId=" + userId + ",funcCode=" + funcCode
				+ ",businessType=" + businessType);
		AccountInfoExt accountInfo = null;
		try {
			User user = new User();
			user.setRootInstCd(merchantCode);
			if (StringUtils.isNotBlank(roleCode)) {
				user.setRoleCodes(new String[] { roleCode });
			}
			user.setUserId(userId);
			user.setTypes(new String[] { "1", "3", "4" });
			user.setStatuses(new Integer[] { 1, 3 });
			List<User> userList = new ArrayList<User>();
			userList.add(user);
			logger.info(">>>>>开始从账户系统查询绑卡信息,RootInstCd=" + merchantCode
					+ ",RoleCode=" + roleCode + ",UserId=" + userId
					+ ",types:['1','3','4']" + ",Statuses:" + "[1,3]");
			AccountInfoRes accountInfoResponse = accountInfoExtApi
					.batchQueryCards(userList);
			logger.info(">>>>>结束从账户系统查询绑卡信息");
			if (accountInfoResponse != null) {
				if ("1".equals(accountInfoResponse.getCode())) {
					logger.info("从账户系统查询绑卡信息返回的code=1,成功");
					Map<String, List<AccountInfoExt>> accountInfoListMap = accountInfoResponse
							.getAccountInfoListMap();
					String reusltKey = null;
					if (StringUtils.isNotBlank(roleCode)) {
						reusltKey = merchantCode + "." + userId + "."
								+ roleCode;
					} else {
						reusltKey = merchantCode + "." + userId + ".";
					}
					logger.info("机构号.userid.角色号,拼接的字符串=" + reusltKey);
					List<AccountInfoExt> accountInfoList = accountInfoListMap
							.get(reusltKey);// 键为机构号.userid.角色号,拼接的字符串
					if (accountInfoList != null && accountInfoList.size() > 0) {
						logger.info("账户系统返回的accountInfoList的size="
								+ accountInfoList.size());
					} else {
						logger.info("账户系统返回的accountInfoList的size=0或accountInfoList=null");
					}

					if (SettleConstants.BATCH_TX_FUN_CODE.equals(funcCode)) {// 提现
						accountInfo = this.get2CardBeanByPurpose(
								accountInfoList, accountInfo, new String[] {
										"3", "4", "1" });
					} else {
						accountInfo = this.get2CardBeanByPurpose(
								accountInfoList, accountInfo, new String[] {
										"1", "4", "3" });
					}
				} else {
					logger.info("查询对卡信息,账户系统返回失败,msg="
							+ accountInfoResponse.getMsg() + ",code="
							+ accountInfoResponse.getCode());
				}
			} else {
				logger.info("查询对卡信息,账户系统返回AccountInfoRes为空!");
			}
			logger.info(">>>>结束getCardInfor方法");
		} catch (Exception e) {
			logger.error(">>>>getCardInfor方法发生异常！！！！" + e);
		}
		return accountInfo;
	}

	/**
	 * 根据要求卡目的得到卡对象(账户二期)
	 * 
	 * @param accountInfoList
	 * @param accountInfo
	 * @param inPurpose
	 */
	public AccountInfoExt get2CardBeanByPurpose(
			List<AccountInfoExt> accountInfoList, AccountInfoExt accountInfo,
			String[] inPurpose) {
		if (inPurpose == null || inPurpose.length == 0) {
			return null;
		}
		String purpose = null;
		Boolean purposeIsCorrect = false;
		for (AccountInfoExt bean : accountInfoList) {
			accountInfo = bean;
			purpose = bean.getAccountPurpose();
			if (inPurpose[0].equals(purpose)) {
				logger.info("user_id=" + accountInfo.getUserId() + "的卡目的="
						+ purpose);
				purposeIsCorrect = true;
				break;
			}
		}
		if (!purposeIsCorrect) {
			for (AccountInfoExt bean : accountInfoList) {
				accountInfo = bean;
				purpose = bean.getAccountPurpose();
				if (inPurpose[1].equals(purpose)) {
					logger.info("user_id=" + accountInfo.getUserId() + "的卡目的="
							+ purpose);
					purposeIsCorrect = true;
					break;
				}
			}
		}
		if (!purposeIsCorrect) {
			for (AccountInfoExt bean : accountInfoList) {
				accountInfo = bean;
				purpose = bean.getAccountPurpose();
				if (inPurpose[2].equals(purpose)) {
					logger.info("user_id=" + accountInfo.getUserId() + "的卡目的="
							+ purpose);
					purposeIsCorrect = true;
					break;
				}
			}
		}
		String merchantCode = null;
		String accountNumber = null;
		if (accountInfo != null) {
			merchantCode = accountInfo.getRootInstCd();
			accountNumber = accountInfo.getAccountNumber();
		}
		if (!purposeIsCorrect) {
			logger.info("机构号：" + merchantCode + ",accountNumber:"
					+ accountNumber + "的卡目的错误,purpose:" + purpose);
			return null;
		}
		return accountInfo;
	}

	/**
	 * 根据要求卡目的得到卡对象(账户一期)
	 * 
	 * @param accountInfoList
	 * @param accountInfo
	 * @param inPurpose
	 */
	public AccountInfo get1CardBeanByPurpose(List<AccountInfo> accountInfoList,
			String[] inPurpose, String yfqFlag) {
		AccountInfo accountInfo = null;
		if (inPurpose == null || inPurpose.length == 0) {
			return null;
		} else {
			logger.info("进入账户一期匹配卡目的的方法");
			if (inPurpose != null && inPurpose[0] != null) {
				logger.info("inPurpose[0]=" + inPurpose[0]);
			}
		}
		String purpose = null;
		Boolean purposeIsCorrect = false;
		for (AccountInfo bean : accountInfoList) {
			accountInfo = bean;
			purpose = bean.getAccountPurpose();
			logger.info("账户一期接口返回的卡目的=" + purpose + ";status="
					+ bean.getStatus());
			if (bean.getStatus() == 1 && inPurpose[0].equals(purpose)) {
				purposeIsCorrect = true;
				break;
			} else if (bean.getStatus() == 3 && "YFQDF".equals(yfqFlag)
					&& inPurpose[0].equals(purpose)) {
				purposeIsCorrect = true;
				break;
			} else {
				continue;
			}
		}
		if (!purposeIsCorrect) {
			for (AccountInfo bean : accountInfoList) {
				accountInfo = bean;
				purpose = bean.getAccountPurpose();
				logger.info("账户一期接口返回的卡目的=" + purpose + ";status="
						+ bean.getStatus());
				if (bean.getStatus() == 1 && inPurpose[1].equals(purpose)) {
					purposeIsCorrect = true;
					break;
				} else if (bean.getStatus() == 3 && "YFQDF".equals(yfqFlag)
						&& inPurpose[1].equals(purpose)) {
					purposeIsCorrect = true;
					break;
				} else {
					continue;
				}
			}
		}
		if (!purposeIsCorrect) {
			for (AccountInfo bean : accountInfoList) {
				accountInfo = bean;
				purpose = bean.getAccountPurpose();
				logger.info("账户一期接口返回的卡目的=" + purpose + ";status="
						+ bean.getStatus());
				if (bean.getStatus() == 1 && inPurpose[2].equals(purpose)) {
					purposeIsCorrect = true;
					break;
				} else if (bean.getStatus() == 3 && "YFQDF".equals(yfqFlag)
						&& inPurpose[2].equals(purpose)) {
					purposeIsCorrect = true;
					break;
				} else {
					continue;
				}
			}
		}
		String merchantCode = null;
		String accountNumber = null;
		if (accountInfo != null) {
			merchantCode = accountInfo.getRootInstCd();
			accountNumber = accountInfo.getAccountNumber();
		}
		if (!purposeIsCorrect) {
			logger.info("机构号：" + merchantCode + ",accountNumber:"
					+ accountNumber + "的卡目的错误,purpose:" + purpose);
			return null;
		}
		return accountInfo;
	}

	@Override
	public void sendDsf(String dataSource, String[] inRootInstCds,
			Integer[] orderTypes, Integer[] ids) {
		logger.info(">>>>>开始sendDsf方法...........");
		try {
			/*
			 * String rootInstCd = null; long sumMoney = 0; String
			 * merAcctBalance = "0"; BigDecimal blance = null;//备付金余额 BigDecimal
			 * dfAmount = null;//代付金额 List<SettleTransInvoice> tmpList = null;
			 * Set<String> rootInstCdSet = new HashSet<String>();//机构号的集合
			 */
			Set<String> requestNoSet = new HashSet<String>();// 交易批次号集合
			this.sendDsfAgain(ids);// 失败重发的走这段逻辑
			Map<String, Object> paramMap = this.getNotDealInvoiceExpress(dataSource, inRootInstCds, orderTypes, ids);
			List<SettleTransInvoice> transInvoiceList = dsfLogic.queryTransInvoiceList(paramMap);
			for (SettleTransInvoice bean : transInvoiceList) {
				requestNoSet.add(bean.getRequestNo());
				// rootInstCdSet.add(bean.getRootInstCd());
			}

			if ((transInvoiceList == null || transInvoiceList.size() == 0) && orderTypes[0] == 8) {
				dsfLogic.createXrXjdExcel(null);
			}
			// 校验备付金是否足够
			/*
			 * Iterator<String> rootInstCdSetIte = rootInstCdSet.iterator();
			 * while(rootInstCdSetIte.hasNext()){ rootInstCd =
			 * rootInstCdSetIte.next(); sumMoney =
			 * this.sumMoneyOneMerchant(rootInstCd, paramMap); try{
			 * merAcctBalance = checkfileDownload.merAcctBalance(rootInstCd,
			 * null);//查询备付金余额(单位:分)
			 * logger.info("查询到的备付金余额是="+merAcctBalance+";机构号："+rootInstCd);
			 * blance = new BigDecimal(merAcctBalance); dfAmount = new
			 * BigDecimal(String.valueOf(sumMoney));
			 * this.compareDsfAmountAndBlance(blance, dfAmount, paramMap);
			 * }catch(Exception e){
			 * logger.error("查询备付金异常！！rootInstCd="+rootInstCd+",异常信息："+e); } }
			 */
			String requestNo = null;
			SettleTransInvoiceQuery invoiceQuery = new SettleTransInvoiceQuery();
			List<SettleTransInvoice> tmpInvoiceList = null;
			List<SettleTransInvoice> sendInvoiceList = null;
			List<SettleTransInvoice> fileInvoiceList = null;
			String resquestNoNew = null;
			int total = 0;
			Iterator<String> requesNoIterator = requestNoSet.iterator();
			while (requesNoIterator.hasNext()) {
				requestNo = requesNoIterator.next();// 得到交易批次号
				invoiceQuery.setRequestNo(requestNo);
				invoiceQuery.setStatusId(SettleConstants.DSF_NOT_DEAL);
				total = dsfLogic.queryTotalInvoiceByExample(invoiceQuery);
				try {
					if (total > 200) {
						Boolean zeroFlag = false;
						fileInvoiceList = new ArrayList<SettleTransInvoice>();
						while (!zeroFlag) {
							paramMap.put("requestNo", requestNo);
							paramMap.put("limit", 200);
							tmpInvoiceList = dsfLogic.queryTransInvoices(paramMap);
							sendInvoiceList = new ArrayList<SettleTransInvoice>();
							resquestNoNew = settlementUtil.createBatchNo("DSF");
							for (SettleTransInvoice settleTransInvoice : tmpInvoiceList) {
								settleTransInvoice.setUpdatedTime(new Date());
								settleTransInvoice.setRequestNo(resquestNoNew);
								settleTransInvoice
										.setStatusId(SettleConstants.DSF_HAS_SEND);// 更新状态为已发送
								settleTransInvoiceManager
										.updateSettleTransInvoice(settleTransInvoice);
								sendInvoiceList.add(settleTransInvoice);
							}
							if (",4013,4014,4014_1,4016,".indexOf(","
									+ sendInvoiceList.get(0).getFuncCode()
									+ ",") != -1) {
								this.sendToPaySys(sendInvoiceList,
										resquestNoNew);// 调用发送代收付接口的方法
							} else if (",40147,".indexOf(","
									+ sendInvoiceList.get(0).getFuncCode()
									+ ",") != -1) {
								fileInvoiceList.addAll(sendInvoiceList);// 文件发送
							} else if (",40143,40144,".indexOf(","
									+ sendInvoiceList.get(0).getFuncCode()
									+ ",") != -1) {
								this.sendToCorpBankPay(sendInvoiceList);// 银企直联系统
							}
							total = dsfLogic
									.queryTotalInvoiceByExample(invoiceQuery);
							if (total == 0)
								zeroFlag = true;
						}
						if (fileInvoiceList != null
								&& fileInvoiceList.size() > 0) {
							final Integer totalZ = fileInvoiceList.size();// 总数量
							final Integer size = 2000;// 每批数量
							final Integer totalIndex = totalZ % size == 0 ? totalZ
									/ size
									: totalZ / size + 1;// 总批数
							Integer batchIndex = 1;// 当前批数
							Integer fromIndex = null;// 开始索引 包含
							Integer toIndex = null;// 结束索引 不包含
							while (totalIndex >= batchIndex) {
								fromIndex = (batchIndex - 1) * size;
								toIndex = totalIndex == batchIndex ? totalZ
										: batchIndex * size;
								dsfLogic.createXrXjdExcel(fileInvoiceList
										.subList(fromIndex, toIndex));
								batchIndex++;
							}
						}
					} else if (total > 0) {
						paramMap.put("requestNo", requestNo);
						sendInvoiceList = dsfLogic.queryTransInvoices(paramMap);
						for (SettleTransInvoice tmpBean : sendInvoiceList) {
							tmpBean.setUpdatedTime(new Date());
							tmpBean.setStatusId(SettleConstants.DSF_HAS_SEND);// 更新状态为已发送
							settleTransInvoiceManager
									.updateSettleTransInvoice(tmpBean);
						}
						if (",4013,4014,4014_1,4016,".indexOf(","
								+ sendInvoiceList.get(0).getFuncCode() + ",") != -1) {
							this.sendToPaySys(sendInvoiceList, requestNo);// 调用发送代收付接口的方法
						} else if (",40147,".indexOf(","
								+ sendInvoiceList.get(0).getFuncCode() + ",") != -1) {
							dsfLogic.createXrXjdExcel(sendInvoiceList);
						} else if (",40143,40144,".indexOf(","
								+ sendInvoiceList.get(0).getFuncCode() + ",") != -1) {
							this.sendToCorpBankPay(sendInvoiceList);
						}
					} else {
						logger.info("根据交易批次号requestNo=" + requestNo
								+ ",没有查询到未处理的结算表信息！");
					}
				} catch (Exception e) {
					logger.error(e.getMessage());
					try {
						for (SettleTransInvoice sendBean : sendInvoiceList) {
							sendBean.setStatusId(SettleConstants.EXCEPTION);
							settleTransInvoiceManager
									.updateSettleTransInvoice(sendBean);// 更新结算表
						}
					} catch (Exception e1) {
						logger.error(">>>交易批次号=" + requestNo
								+ ",发送代收付系统异常>>>>>>>>>>" + e1.getMessage());
						throw new Exception(e1.getMessage());
					}
					throw new Exception(e.getMessage());
				}
			}
		} catch (Exception e) {
			logger.error(">>>>sendDsf方法发生异常！！！！" + e.getMessage());
		}
		logger.info(">>>>>结束sendDsf方法...........");
	}

	/**
	 * 比较代付和提现总金额跟备付金余额
	 * 
	 * @param blance
	 * @param dfAmount
	 * @param paramMap
	 */
	public void compareDsfAmountAndBlance(BigDecimal blance,
			BigDecimal dfAmount, Map<String, Object> paramMap) {
		if (blance.compareTo(dfAmount) < 0) {// 备付金余额小于代付和提现总金额
			List<SettleTransInvoice> tmpList;
			try {
				tmpList = dsfLogic.queryTransInvoices(paramMap);
				for (SettleTransInvoice bean : tmpList) {
					bean.setStatusId(SettleConstants.EXCEPTION);
					bean.setRemark("备付金余额不足");
					bean.setRequestNo(bean.getRequestNo() + "99");
					settleTransInvoiceManager.updateSettleTransInvoice(bean);
				}
				SendSMS.sendSMS(mobile, content);// 给相关人员发送短信
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 根据条件查询某个机构号在结算表中提现和代付的总金额
	 * 
	 * @param rootInstCd
	 *            机构号
	 * @param paramMap
	 *            参数(存放的是部分限制条件)
	 * @return 提现和代付的总金额
	 */
	public long sumMoneyOneMerchant(String rootInstCd,
			Map<String, Object> paramMap) {
		long sumMoney = 0;
		try {
			paramMap.put("rootInstCd", rootInstCd);
			paramMap.put("funcCodes", new String[] { "4014", "4016" });
			long count = settleTransInvoiceManager.countByDfExample(paramMap);// 某个机构代付和提现的总金额
			if (count > 0) {
				sumMoney = settleTransInvoiceManager.sumByExample(paramMap);// 某个机构代付和提现的总金额
			}
			paramMap.put("rootInstCd", null);
			paramMap.put("funcCodes", null);
		} catch (Exception e) {
			logger.error("查询机构机构号:" + rootInstCd + "的代付和提现的总金额发生异常！！！！error:"
					+ e);
		}
		return sumMoney;
	}

	/**
	 * 得到结算表中状态是未发送的交易
	 * 
	 * @param dataSource
	 *            数据来源
	 * @param inRootInstCds
	 *            机构号
	 * @param orderTypes
	 *            订单类型,跟代收付接口文档里的含义一致
	 * @param ids
	 *            结算表的ID号
	 */
	public Map<String, Object> getNotDealInvoiceExpress(String dataSource,
			String[] inRootInstCds, Integer[] orderTypes, Integer[] ids) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		try {
			if (StringUtils.isNotBlank(dataSource)) {
				paramMap.put("dataSources",
						new Integer[] { Integer.parseInt(dataSource) });
			} else {
				paramMap.put("dataSources", new Integer[] {
						SettleConstants.DSF_DATA_SOURCE_SYSTEM,
						SettleConstants.DSF_DATA_SOURCE_FILE,
						SettleConstants.DSF_DATA_SOURCE_MANUAL });
			}
			paramMap.put("statusId", SettleConstants.DSF_NOT_DEAL);
			if (inRootInstCds != null && inRootInstCds.length > 0)
				paramMap.put("rootInstCds", inRootInstCds);
			if (orderTypes != null && orderTypes.length > 0)
				paramMap.put("orderTypes", orderTypes);
			if (ids != null && ids.length > 0)
				paramMap.put("ids", ids);
		} catch (Exception e) {
			logger.error("得到结算表中状态是未发送的交易发生异常！！！！errorMsg:" + e.getMessage()
					+ e.getCause());
		}
		return paramMap;
	}

	/**
	 * 将代收付交易提交到代收付系统
	 * 
	 * @param sendInvoiceList
	 *            结算表的list
	 * @param requestNo
	 *            交易批次号
	 * @throws Exception
	 */
	public void sendToPaySys(List<SettleTransInvoice> sendInvoiceList,
			String requestNo) throws Exception {
		if (sendInvoiceList == null || sendInvoiceList.size() == 0) {
			logger.info("发送代收付系统的list为空！");
			return;
		}
		BatchTransRespDto batchTransRespDto = null;
		try {
			// 某一批发送到代收付的订单数据,OrderDetail是代收付系统的对象
			List<TransDetailDto> transDetailDtoList = new ArrayList<TransDetailDto>();
			TransDetailDto transDetailDto = new TransDetailDto();
			BatchTransDto batchTransDto = new BatchTransDto();
			String busiCode = null;
			// String transCode = null;
			String orgNo = null;
			// String sysNo = null;
			String batchNo = null;
			String signMsg = null;
			String payChannelId = null;
			Date settleDate = null;
			BigDecimal totalAmount = new BigDecimal("0");
			BigDecimal singelAmount = new BigDecimal("0");
			for (SettleTransInvoice settleTransInvoice : sendInvoiceList) {
				transDetailDto = getOrderDeByTransOrder(settleTransInvoice,
						requestNo);
				busiCode = settleTransInvoice.getBussinessCode();
				orgNo = settleTransInvoice.getRootInstCd();
				batchNo = settleTransInvoice.getRequestNo();
				settleDate = settleTransInvoice.getAccountDate();
				payChannelId = settleTransInvoice.getMerchantCode();
				singelAmount = new BigDecimal(transDetailDto.getAmount()
						.toString());
				totalAmount = totalAmount.add(singelAmount);
				transDetailDtoList.add(transDetailDto);
			}
			batchTransDto.setBusiCode(busiCode);
			if ("09100".equals(busiCode)) { // 代收
				batchTransDto.setTransCode("11022");
			} else { // 代付
				batchTransDto.setTransCode("11012");
			}
			batchTransDto.setOrgNo(orgNo);
			batchTransDto.setBatchNo(batchNo);
			batchTransDto.setSignType(1); // 固定1
			batchTransDto.setSettleFlag(1);// 0：实时 ；1：非实时
			batchTransDto.setTotalAcount(transDetailDtoList.size());
			batchTransDto.setTotalAmount(totalAmount.longValue());
			batchTransDto.setSubmitTime(new Date());
			batchTransDto.setSettleDate(settleDate);

			signMsg = batchTransDto.sign("000000");

			batchTransDto.setSysNo("sys1001");
			batchTransDto.setSignMsg(signMsg);

			if (payChannelId != null && !"null".equalsIgnoreCase(payChannelId)) {
				batchTransDto.setOrgCode(payChannelId);
			}

			batchTransDto.setTransDetailDtoList(transDetailDtoList);

			try {
				logger.info(">>>>>>开始调用代收付系统批量接口发送交易,交易批次号是:" + requestNo
						+ ",时间:" + new Date() + ">>>>>>>>,发送了"
						+ batchTransDto.getTotalAcount() + "条数据");
				batchTransRespDto = crpsAgentPayService
						.batchAgentPay(batchTransDto);// 调用代收付系统接口
				logger.info(">>>>>>结束调用代收付系统批量接口发送交易,交易批次号是:" + requestNo
						+ ",时间:" + new Date() + ">>>>>>>>,代收付返回的结果,retCode="
						+ batchTransRespDto.getReturnCode() + ",errorMsg="
						+ batchTransRespDto.getReturnMsg());
			} catch (Exception e) {
				logger.error(">>>>>调用代收付系统批量接口发送交易失败！批次号=" + requestNo + ","
						+ e.getMessage());
			}
			if (batchTransRespDto == null) {
				logger.info("调用批量代收付接口代收付系统返回的batchTransRespDto对象为空!!!!!!!!!！");
				return;
			}
			String retCode = batchTransRespDto.getReturnCode();
			String errorMsg = batchTransRespDto.getReturnMsg();
			if (!"100000".equals(retCode)) {// 不等于100000的即为失败
				for (SettleTransInvoice bean : sendInvoiceList) {
					bean.setStatusId(SettleConstants.DSF_SEND_FAIL);
					bean.setRemark(errorMsg);
					settleTransInvoiceManager.updateSettleTransInvoice(bean);// 更新发送失败的状态
					logger.info(">>>>代收付返回失败,更新结算表,invoiceNo="
							+ bean.getInvoiceNo() + ",retCode" + retCode
							+ ",errorMsg=" + errorMsg);
				}
			}
		} catch (Exception e) {
			logger.error(">>>>>sendToPaySys发送代收付系统发生异常！！！！！！！" + e.getMessage());
			throw new Exception(e.getMessage());
		}
	}

	/**
	 * 根据传入的汇总数据，封装成要传入代收付系统的数据OrderDetail
	 * 
	 * @param settleTransInvoice
	 * @param requestNo
	 * @return
	 * @throws Exception
	 */
	private TransDetailDto getOrderDeByTransOrder(
			SettleTransInvoice settleTransInvoice, String requestNo)
			throws Exception {
		logger.info(">>>>>>开始getOrderDeByTransOrder方法,requestNo=" + requestNo);
		TransDetailDto orderDetail = new TransDetailDto();
		try {
			orderDetail.setTransNo(settleTransInvoice.getOrderNo());
			orderDetail.setCurrency(settleTransInvoice.getCurrency());
			orderDetail.setAmount(settleTransInvoice.getAmount());
			;
			orderDetail.setUserNo(settleTransInvoice.getUserId());
			orderDetail.setUserName(settleTransInvoice.getAccountName());
			orderDetail.setIdType(settleTransInvoice.getCertificateType());
			orderDetail.setIdCode(settleTransInvoice.getCertificateNumber());
			orderDetail.setAccountNo(settleTransInvoice.getAccountNo());
			orderDetail.setAccountName(settleTransInvoice.getAccountName());
			String accountProp = "10"; // //对私
			if ("1".equals(settleTransInvoice.getAccountProperty())) {
				accountProp = "20";// 对公
			}
			orderDetail.setAccountType(accountProp);
			orderDetail.setExpand("1");

			orderDetail.setBankCode(settleTransInvoice.getBankCode());
			orderDetail.setBankName(settleTransInvoice.getOpenBankName());
			orderDetail.setPayBankCode(settleTransInvoice.getPayBankCode());
			orderDetail.setPayBankName(settleTransInvoice.getOpenBankName());
			orderDetail.setProvince(settleTransInvoice.getProvince());
			orderDetail.setCity(settleTransInvoice.getCity());
			orderDetail.setRemark(settleTransInvoice.getRemark());

		} catch (Exception e) {
			logger.error("getOrderDeByTransOrder发成异常！！！！！！requestNo="
					+ requestNo + ",异常信息" + e);
			throw new Exception(e.getMessage());
		}
		logger.info(">>>>>>结束getOrderDeByTransOrder方法,requestNo=" + requestNo);
		return orderDetail;
	}

	/**
	 * 组织汇总表的信息
	 * 
	 * @param theDetailMap
	 * @return
	 * @throws Exception
	 */
	public SettleTransSummary createSettleTransSummary(
			Map<String, Object> theDetailMap, Map<String, String> batchNoMap)
			throws Exception {
		logger.info(">>>>>>>开始组织汇总表信息,orderNo=" + theDetailMap.get("ORDER_NO"));
		SettleTransSummary summaryBean = new SettleTransSummary();
		String userIdOrInterManchantCode = null;
		try {
			if (StringUtils.isNotBlank((String) theDetailMap
					.get("INTER_MERCHANT_CODE"))) {
				userIdOrInterManchantCode = (String) theDetailMap
						.get("INTER_MERCHANT_CODE");
			} else {
				userIdOrInterManchantCode = String.valueOf(theDetailMap
						.get("USER_ID"));
			}
			String tmpKey = String.valueOf(theDetailMap.get("MERCHANT_CODE"))
					+ userIdOrInterManchantCode
					+ String.valueOf(theDetailMap.get("FUNC_CODE"))
					+ String.valueOf(theDetailMap.get("ACCOUNT_DATE"))
					+ String.valueOf(theDetailMap.get("DATA_FROM"));
			String batchNo = batchNoMap.get(tmpKey);

			BigDecimal amount = (BigDecimal) theDetailMap.get("AMOUNT");
			summaryBean.setAccountDate((Date) theDetailMap.get("ACCOUNT_DATE"));// 账期
			summaryBean.setAmount(new Long(amount.longValue()));// 金额(分)
			summaryBean.setBatchNo(batchNo);// 批次号(跟结算单表的BATCH_NO关联)
			summaryBean
					.setOrderNo(String.valueOf(theDetailMap.get("ORDER_NO")));// 订单号
			summaryBean.setFuncCode(String.valueOf(theDetailMap
					.get("FUNC_CODE")));// 功能编码
			summaryBean.setRootInstCd(String.valueOf(theDetailMap
					.get("MERCHANT_CODE")));// 管理机构代码
			summaryBean.setBusinessType(String.valueOf(theDetailMap
					.get("BUSINESS_TYPE")));
			summaryBean.setStatusId(SettleConstants.STATUSID_1);// 状态,
																// 0未处理,1处理中,2成功，3失败
			summaryBean.setUserId(String.valueOf(theDetailMap.get("USER_ID")));// 用户号
		} catch (Exception e) {
			logger.error(">>>>>组织汇总表信息发生异常！！！！！！！,orderNo="
					+ theDetailMap.get("ORDER_NO") + e);
			throw new Exception(e.getMessage());
		}
		logger.info(">>>>>>>结束组织汇总表信息,orderNo=" + theDetailMap.get("ORDER_NO"));
		return summaryBean;
	}

	/**
	 * 组织汇总表的信息(单条)
	 * 
	 * @param theDetailMap
	 * @return
	 * @throws Exception
	 */
	public SettleTransSummary createSettleTransSummaryOne(
			Map<String, Object> theDetailMap) throws Exception {
		logger.info(">>>>>>>开始组织汇总表信息,orderNo=" + theDetailMap.get("ORDER_NO"));
		SettleTransSummary summaryBean = new SettleTransSummary();
		try {
			BigDecimal amount = (BigDecimal) theDetailMap.get("AMOUNT");
			summaryBean.setAccountDate((Date) theDetailMap.get("ACCOUNT_DATE"));// 账期
			summaryBean.setAmount(new Long(amount.longValue()));// 金额(分)
			summaryBean.setBatchNo(String.valueOf(theDetailMap.get("batchNo")));// 批次号(跟结算单表的BATCH_NO关联)
			summaryBean
					.setOrderNo(String.valueOf(theDetailMap.get("ORDER_NO")));// 订单号
			summaryBean.setFuncCode(String.valueOf(theDetailMap
					.get("FUNC_CODE")));// 功能编码
			summaryBean.setRootInstCd(String.valueOf(theDetailMap
					.get("MERCHANT_CODE")));// 管理机构代码
			summaryBean.setBusinessType(String.valueOf(theDetailMap
					.get("BUSINESS_TYPE")));
			summaryBean.setStatusId(SettleConstants.STATUSID_1);// 状态,
																// 0未处理,1处理中,2成功，3失败
			summaryBean.setUserId(String.valueOf(theDetailMap.get("USER_ID")));// 用户号
		} catch (Exception e) {
			logger.error(">>>>>组织汇总表信息发生异常！！！！！！！,orderNo="
					+ theDetailMap.get("ORDER_NO") + e);
			throw new Exception(e.getMessage());
		}
		logger.info(">>>>>>>结束组织汇总表信息,orderNo=" + theDetailMap.get("ORDER_NO"));
		return summaryBean;
	}

	/**
	 * 更新汇总表的批次号
	 * 
	 * @param theDetailMap
	 * @throws Exception
	 */
	public void updateSummaryBatchNo(Map<String, Object> theDetailMap)
			throws Exception {
		logger.info(">>>>>>>开始更新汇总表批次号,orderNo=" + theDetailMap.get("ORDER_NO")
				+ ",RootInstCd=" + theDetailMap.get("MERCHANT_CODE")
				+ ",userId=" + theDetailMap.get("USER_ID") + ",accounteDate="
				+ theDetailMap.get("ACCOUNT_DATE") + ",batchNo="
				+ theDetailMap.get("batchNo"));
		SettleTransSummaryQuery summaryQuery = new SettleTransSummaryQuery();
		try {
			summaryQuery
					.setOrderNo(String.valueOf(theDetailMap.get("ORDER_NO")));
			summaryQuery.setRootInstCd(String.valueOf(theDetailMap
					.get("MERCHANT_CODE")));
			summaryQuery
					.setAccountDate((Date) theDetailMap.get("ACCOUNT_DATE"));
			summaryQuery.setUserId(String.valueOf(theDetailMap.get("USER_ID")));

			List<SettleTransSummary> settleTransSummaryList = settleTransSummaryManager
					.queryList(summaryQuery);
			for (SettleTransSummary summaryBean : settleTransSummaryList) {
				summaryBean.setBatchNo(String.valueOf(theDetailMap
						.get("batchNo")));
				settleTransSummaryManager.updateSettleTransSummary(summaryBean);
			}
		} catch (Exception e) {
			logger.error(">>>>>>>发送异常！！！更新汇总表批次号,orderNo="
					+ theDetailMap.get("ORDER_NO") + ",RootInstCd="
					+ theDetailMap.get("MERCHANT_CODE") + ",userId="
					+ theDetailMap.get("USER_ID") + ",accounteDate="
					+ theDetailMap.get("ACCOUNT_DATE") + ",batchNo="
					+ theDetailMap.get("batchNo") + e);
			throw new Exception(e.getMessage());
		}
		logger.info(">>>>>>>结束更新汇总表批次号,orderNo=" + theDetailMap.get("ORDER_NO")
				+ ",RootInstCd=" + theDetailMap.get("MERCHANT_CODE")
				+ ",userId=" + theDetailMap.get("USER_ID") + ",accounteDate="
				+ theDetailMap.get("ACCOUNT_DATE") + ",batchNo="
				+ theDetailMap.get("batchNo"));
	}

	/**
	 * 组织结算表的信息
	 * 
	 * @param theDetailMap
	 * @param orderAccountInfo
	 * @param corporatAccountInfo
	 * @param accountInfo
	 * @param manualSendFlag
	 * @return
	 * @throws Exception
	 */
	public SettleTransInvoice createSettleTransInvoice(
			Map<String, Object> theDetailMap, Map<String, Object> resultMap,
			Boolean manualSendFlag, List<SettleParameterInfo> paramList)
			throws Exception {
		logger.info(">>>>>开始组织结算表信息:requestNo:" + theDetailMap.get("reqNo"));
		SettleTransInvoice invoiceBean = new SettleTransInvoice();
		try {
			OrderAccountInfo orderAccountInfo = (OrderAccountInfo) resultMap
					.get("orderAccountInfo");// 订单系统的卡信息对象
			CorporatAccountInfoExt corporatAccountInfo = (CorporatAccountInfoExt) resultMap
					.get("corporatAccountInfo");// 账户系统的卡信息对象(一分钱代付)
			AccountInfoExt accountInfoExt = (AccountInfoExt) resultMap
					.get("accountInfoExt");
			AccountInfo accountInfo = (AccountInfo) resultMap
					.get("accountInfo");

			invoiceBean.setRequestNo((String) theDetailMap.get("reqNo"));// 交易批次号(代收付系统)

			String rootInstCd = (String) theDetailMap.get("MERCHANT_CODE");// 机构号
			String businessType = (String) theDetailMap.get("BUSINESS_TYPE");// 业务编码
			// invoiceBean.setMerchantCode(rootInstCd);

			Boolean isFlag = true;
			for (SettleParameterInfo bean : paramList) {
				if (StringUtils.isNotBlank(bean.getObligate2())) {
					if (StringUtils.isNotBlank(rootInstCd)
							&& bean.getParameterValue().contains(rootInstCd)
							&& StringUtils.isNotBlank(businessType)
							&& bean.getObligate2().contains(businessType)) {
						invoiceBean.setRootInstCd(bean.getParameterCode());// 机构号转换
						isFlag = false;
						break;
					}
				}
			}
			if (isFlag) {
				for (SettleParameterInfo bean : paramList) {
					if (StringUtils.isNotBlank(rootInstCd)
							&& bean.getParameterValue().contains(rootInstCd)) {
						invoiceBean.setRootInstCd(bean.getParameterCode());// 机构号转换
						break;//
					}
				}
			}

			if (StringUtils.isBlank(invoiceBean.getRootInstCd())) {
				invoiceBean.setRootInstCd(rootInstCd);// 机构号
			}
			String funcCode = (String) theDetailMap.get("FUNC_CODE");
			String businessCode = null;
			if (",4014,4014_1,4016,40143,40144,40147,".indexOf("," + funcCode
					+ ",") != -1) {// 代付
				businessCode = "11101";
			} else {
				businessCode = "09100";
			}
			invoiceBean.setBussinessCode(businessCode);// 业务代码
			Integer orderType = 5;// 初始值5代收
			if (SettleConstants.BATCH_DF_FUN_CODE.equals(funcCode)
					|| SettleConstants.BATCH_DF_YFQ_FUN_CODE.equals(funcCode)
					|| SettleConstants.BATCH_DF3_FUN_CODE.equals(funcCode)
					|| SettleConstants.BATCH_DF4_FUN_CODE.equals(funcCode)) {// 代付
				orderType = 6;
			} else if (SettleConstants.BATCH_TX_FUN_CODE.equals(funcCode)) {// 提现
				orderType = 2;
			} else if (SettleConstants.BATCH_DF7_FUN_CODE.equals(funcCode)) {// 提现
				orderType = 8;
			}
			invoiceBean.setOrderType(orderType);// 订单类型,代收、代付、提现等
			invoiceBean.setBatchNo((String) theDetailMap.get("batchNo"));// 批次号
			invoiceBean.setUserId((String) theDetailMap.get("USER_ID"));// 用户ID
			invoiceBean.setOrderNo((String) theDetailMap.get("ORDER_NO"));// 订单号
			invoiceBean.setFuncCode((String) theDetailMap.get("FUNC_CODE"));// 功能编码
			invoiceBean.setMerchantCode((String) theDetailMap
					.get("PAY_CHANNEL_ID"));// 渠道信息
			logger.info(">>>>组织结算表信息,开始加入绑卡信息,orderNo="
					+ invoiceBean.getOrderNo());

			if (orderAccountInfo != null) {// 取订单系统的值
				this.modifyInvoiceByOrderAccountInfo(invoiceBean,
						orderAccountInfo);
			} else {
				if ("4".equals(String.valueOf(theDetailMap.get("DATA_FROM")))) {
					// 取账户系统的值
					if (corporatAccountInfo != null) {// 一分钱代付
						this.modifyInvoiceByCorporatAccountInfo(invoiceBean,
								corporatAccountInfo);
					} else {
						this.modifyInvoiceByAccountInfoExt(invoiceBean,
								accountInfoExt);
					}
				}
				if ("0".equals(String.valueOf(theDetailMap.get("DATA_FROM")))) {
					this.updateInvoiceByAccountInfo(invoiceBean, accountInfo);
				}

				BigDecimal amount = (BigDecimal) theDetailMap
						.get("totalAmount");
				if (amount != null) {
					invoiceBean.setAmount(new Long(amount.longValue()));// 金额
				} else {
					logger.info(">>>>orderNo=" + theDetailMap.get("ORDER_NO")
							+ "的金额Amount为空!");
				}
			}
			logger.info(">>>>组织结算表信息,结束加入绑卡信息,orderNo="
					+ invoiceBean.getOrderNo());
			if (manualSendFlag) {// 手工重发
				invoiceBean
						.setDataSource(SettleConstants.DSF_DATA_SOURCE_SYSTEM);// 数据来源：0是文件导入，1系统推送，2手工重付
			} else {// 系统推送
				invoiceBean
						.setDataSource(SettleConstants.DSF_DATA_SOURCE_SYSTEM);// 数据来源：0是文件导入，1系统推送，2手工重付
			}
			invoiceBean.setSendType(SettleConstants.DSF_SEND_TYPE_0);// 发送类型,0正常,1代扣失败,2代扣延迟
			invoiceBean.setStatusId(SettleConstants.DSF_NOT_DEAL);// 状态
			invoiceBean.setAccountDate((Date) theDetailMap.get("ACCOUNT_DATE"));// 记账日期
			invoiceBean.setRealTimeFlag(0);// 实时标识 0否，1是
		} catch (Exception e) {
			logger.error(">>>>>>>>>>组织结算表信息发生异常！！！！！！！！！！！异常信息"
					+ e.getMessage());
			throw new Exception(e.getMessage());
		}
		logger.info(">>>>>结束组织结算表信息,orderNo=" + invoiceBean.getOrderNo());
		return invoiceBean;
	}

	/**
	 * 从数据库查询代收付交易是否进行汇总的开关
	 * 
	 * @return true汇总开关打开,false汇总开关关闭
	 * @throws Exception
	 */
	public Boolean getDsfOnOff(String merchantCode) throws Exception {
		logger.info(">>>>>开始查询代收付交易是否汇总的开关");
		Boolean dsfOnOff = false;
		try {
			SettleParameterInfoQuery query = new SettleParameterInfoQuery();
			query.setParameterCode("dsfOnOff-" + merchantCode);
			List<SettleParameterInfo> settleParameterInfoList = settleParameterInfoManager
					.queryList(query);
			for (SettleParameterInfo bean : settleParameterInfoList) {
				String tmpVal = bean.getParameterValue();
				if ("true".equals(tmpVal)) {
					dsfOnOff = true;
					break;
				}
			}
		} catch (Exception e) {
			logger.error(">>>>>getDsfOnOff发生异常！！！！" + e.getMessage());
			throw new Exception(e.getMessage());
		}
		logger.info(">>>>>结束查询代收付交易是否汇总的开关,dsfOnOff=" + dsfOnOff);
		return dsfOnOff;
	}

	@Override
	public String dubboNotify(OrderDetail orderDetail) {
		if (orderDetail == null) {
			logger.info(">>>>代收付推送回来的参数orderDetail为空！！！！！");
			return "returnCode=100001&returnMsg=交易失败";// 失败
		}
		logger.info(">>>>>>开始接收代收付推送的结果");
		try {// 退票
			logger.info("-------代收付推送回退票信息,orderNo=" + orderDetail.getOrderNo()
					+ ",statusId=" + orderDetail.getStatusId() + " errMsg="
					+ orderDetail.getErrMsg());
			int orderType = orderDetail.getOrderType();// 订单类型
			if (orderType != 2) {// 只有代付、提现可以走退票,如果不是直接返回失败
				logger.info("代收付推送回退票信息错误！！！,orderType不是2,只有代付和提现(orderType=2)才可以走退票接口");
				return "0";// 给代收付返回失败
			}
			if (orderDetail.getStatusId() == 16) { // 16 表示退票
				this.callTuiPiao(orderDetail);
			} else {
				logger.info(">>>>>>代收付返回的不是退票业务,orderNo="
						+ orderDetail.getOrderNo()
						+ ",退票业务的statusId应该是16,而实际传回的是statusId="
						+ orderDetail.getStatusId());
			}
		} catch (Exception e) {
			logger.error(">>>>receiveDsfResult发生异常！！！！！！！" + e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
		logger.info(">>>>>>结束receiveDsfResult,orderDetail=" + orderDetail);
		return "returnCode=100000&returnMsg=交易成功";
	}

	@Override
	public String dubboNotify(OrderDetails orderDetails) {
		if (orderDetails == null) {
			logger.info(">>>>代收付推送回来的参数orderDetails为空！！！！！");
			return "returnCode=100001&returnMsg=交易失败";// 失败
		}
		logger.info(">>>>>>开始接收代收付推送的结果");
		try {
			// 批量接口返回的结果
			logger.info("-------代收付系统推送回批量接口信息");
			List<OrderDetail> orderDetailList = null;
			List<OrderDetail> mqList = new ArrayList<OrderDetail>();
			try {
				logger.info("orderDetails.getOrderDetails()==="
						+ orderDetails.getOrderDetails());
				orderDetailList = orderDetails.getOrderDetails();
				if (orderDetailList != null && orderDetailList.size() > 0) {
					int orderType = 0;
					for (OrderDetail orderDetailBean : orderDetailList) {
						orderType = orderDetailBean.getOrderType() == null ? 0
								: orderDetailBean.getOrderType();// 订单类型
						logger.info("orderType=" + orderType);
						if (orderDetailBean.getStatusId() == 16
								&& orderType == 2) {// 只有代付、提现可以走退票,如果不是直接返回失败
							logger.info("即将调用账户退票接口！！");
							this.callTuiPiao(orderDetailBean);
						} else {
							logger.info("即将代收付通知结果写入MQ");
							mqList.add(orderDetailBean);
						}
					}
					logger.info("-------代收付系统推送回批量接口信息,共"
							+ orderDetailList.size() + "条");
					this.writeToMQ(mqList);
				} else {
					logger.info("orderDetailList.size==0");
				}
			} catch (Exception e) {
				logger.error("错误信息：" + e);
			}
		} catch (Exception e) {
			logger.error(">>>>receiveDsfResult发生异常！！！！！！！" + e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
		logger.info(">>>>>>结束receiveDsfResult,orderDetails=" + orderDetails);
		return "returnCode=100000&returnMsg=交易成功";
	}

	/**
	 * 组织调用账户退票接口的数据
	 * 
	 * @param orderDetail
	 */
	public void callTuiPiao(OrderDetail orderDetail) throws Exception {
		String dsfOrderNo = orderDetail.getOrderNo();// 代收付系统返回的订单号

		SettleTransInvoiceQuery invoiceQuery = new SettleTransInvoiceQuery();
		invoiceQuery.setOrderNo(dsfOrderNo);

		List<SettleTransInvoice> invoiceList = settleTransInvoiceManager
				.queryList(invoiceQuery);

		if (invoiceList != null && invoiceList.size() > 0) {
			SettleTransInvoice invoice = invoiceList.get(0);
			SettleTransSummaryQuery summaryQuery = new SettleTransSummaryQuery();
			summaryQuery.setBatchNo(invoice.getBatchNo());
			List<SettleTransSummary> summaryList = settleTransSummaryManager
					.queryList(summaryQuery);
			SettleTransDetailQuery detailQuery = new SettleTransDetailQuery();
			for (SettleTransSummary summaryBean : summaryList) {
				detailQuery.setOrderNo(summaryBean.getOrderNo());
				this.tuiPiaoSendAccount(detailQuery);
			}
		} else {
			SettleTransInvoiceHisQuery invoiceHisQuery = new SettleTransInvoiceHisQuery();
			invoiceHisQuery.setOrderNo(dsfOrderNo);

			List<SettleTransInvoiceHis> invoiceHisList = settleTransInvoiceHisManager
					.queryList(invoiceHisQuery);

			if (invoiceHisList != null && invoiceHisList.size() > 0) {
				SettleTransInvoiceHis invoiceHis = invoiceHisList.get(0);
				SettleTransSummaryHisQuery summaryHisQuery = new SettleTransSummaryHisQuery();
				summaryHisQuery.setBatchNo(invoiceHis.getBatchNo());
				List<SettleTransSummaryHis> summaryList = settleTransSummaryHisManager
						.queryList(summaryHisQuery);
				SettleTransDetailQuery detailQuery = new SettleTransDetailQuery();
				for (SettleTransSummaryHis summaryBean : summaryList) {
					detailQuery.setOrderNo(summaryBean.getOrderNo());
					this.tuiPiaoSendAccount(detailQuery);
				}
			} else {
				logger.info("在结算表和结算历史表中都不存在该条记录，orderNo=" + dsfOrderNo);
			}
		}
	}

	/**
	 * 将代收付结果写入MQ
	 * 
	 * @throws Exception
	 */
	public void writeToMQ(List<OrderDetail> orderDetailList) throws Exception {
		if (orderDetailList == null || orderDetailList.size() == 0) {
			logger.info("-------代收付调用清结算代收付结果接收接口,结束<<<<<<<<<<<<<<参数为空<<<<<<<<<<<<<<<<");
			return;
		} else {
			logger.info(">>>>>>进入writeToMQ方法");
		}
		try {
			Destination destination = (Destination) queueDest;
			for (final OrderDetail orderDetail : orderDetailList) {
				logger.info("代收付推送回批量接口信息,每条分别是orderNo="
						+ orderDetail.getOrderNo() + ",statusId="
						+ orderDetail.getStatusId() + ",errMsg="
						+ orderDetail.getErrMsg());
				jmsTemplate.send(destination, new MessageCreator() {
					public Message createMessage(Session session)
							throws JMSException {
						MapMessage message = session.createMapMessage();
						logger.info(">>>创建MQ的message对象成功");
						try {
							if (orderDetail != null) {
								message.setString("requestNo",
										orderDetail.getRequestNo());
								message.setString("orderNo",
										orderDetail.getOrderNo());
								message.setString("orgCode",
										orderDetail.getOrgCode());// 上游的入网商户号
								if (orderDetail.getStatusId() != null) {
									message.setInt("statusId", orderDetail
											.getStatusId().intValue());
								}
								String errMsg = orderDetail.getErrMsg();
								if (StringUtils.isNotBlank(errMsg)) {// 如果代收付返回的errMsg是用GBK编码，则对它进行转码成UTF-8
									String gbkStr = new String(errMsg
											.getBytes("GBK"));
									if (gbkStr.length() < errMsg.length()) {
										errMsg = new String(errMsg
												.getBytes("GBK"), "UTF-8");
									}
								}
								message.setString("errorMsg", errMsg);
							}
						} catch (Exception e) {
							logger.error("给MQ的对象message组织信息的时候异常！！！！requestNo="
									+ orderDetail.getRequestNo() + ",orderNo="
									+ orderDetail.getOrderNo() + ",statusId="
									+ orderDetail.getStatusId() + ",errorMsg="
									+ orderDetail.getErrMsg() + e.getMessage());
						}
						logger.info(">>>组织message信息完成即将return,requestNo="
								+ orderDetail.getRequestNo() + ",orderNo="
								+ orderDetail.getOrderNo() + ",statusId="
								+ orderDetail.getStatusId() + ",errorMsg="
								+ orderDetail.getErrMsg());
						return message;
					}
				});
			}
		} catch (Exception e) {
			logger.error(">>>writeToMQ发生异常！！！！！！！！" + e.getMessage());
			throw new Exception(e.getMessage());
		}
	}

	/**
	 * 银企直联结果写入MQ
	 * 
	 * @throws Exception
	 */
	public void writeToMQByCorpBankPayResp(
			List<CorpBankPayResp> corpBankPayRespList) throws Exception {
		logger.info(">>> >>> 开始:银企直联结果写入MQ");
		if (corpBankPayRespList == null || corpBankPayRespList.size() == 0) {
			logger.info(">>> >>> 开始:银企直联结果写入MQ corpBankPayRespList is Null");
			return;
		}
		try {
			Destination destination = (Destination) queueDest;
			for (final CorpBankPayResp corpBankPayResp : corpBankPayRespList) {
				logger.info(">>> >>> 银企直联结果写入MQ,每条分别是orderNo="
						+ corpBankPayResp.getTransNo() + ",statusId="
						+ corpBankPayResp.getStatusId() + ",errMsg="
						+ corpBankPayResp.getReturnMsg());
				jmsTemplate.send(destination, new MessageCreator() {
					public Message createMessage(Session session)
							throws JMSException {
						MapMessage message = session.createMapMessage();
						logger.info(">>> 创建MQ的message对象成功");
						try {
							if (corpBankPayResp != null) {
								message.setString("requestNo",
										corpBankPayResp.getRequestNo());
								message.setString("orderNo",
										corpBankPayResp.getTransNo());
								message.setString("orgCode",
										corpBankPayResp.getTransCode());// 上游的入网商户
								message.setInt("statusId",
										corpBankPayResp.getStatusId());
								message.setString("errorMsg",
										corpBankPayResp.getReturnMsg());
							}
						} catch (Exception e) {
							logger.error(">>> >>> 异常:银企直联结果写入MQ orderNo="
									+ corpBankPayResp.getTransNo(), e);
						}
						return message;
					}
				});
			}
		} catch (Exception e) {
			logger.error(">>> >>> 异常:银企直联结果写入MQ" + e.getMessage(), e);
			throw e;
		}
		logger.info("<<< <<< 结束:银企直联结果写入MQ");
	}

	/**
	 * 返回代收付结果给订单系统
	 * 
	 * @throws Exception
	 */
	@Override
	public void sendOrder(Integer[] ids, Integer[] dflags) {
		logger.info(">>>>>开始进入返回代收付结果给订单系统的方法(人工触发的,不是MQ监听类里的方法)");
		try {
			Map<String, Object> detailwhereMap = new HashMap<String, Object>();
			if (ids != null && ids.length > 0) {
				detailwhereMap.put("ids", ids);
			}
			if (dflags != null && dflags.length > 0) {
				detailwhereMap.put("dflags", dflags);
			}
			logger.info(">>>>入参ids=" + ids + "dflags=" + dflags);
			List<Map<String, Object>> dsfDetailList = dsfLogic
					.getDsfTransDetail(detailwhereMap);// 获取交易List
			if (dsfDetailList != null) {
				logger.info(">>>>入参ids=" + ids + "dflags=" + dflags
						+ ",从交易表中供查询到" + dsfDetailList.size() + "条信息");
			}

			Iterator<Map<String, Object>> dsfTransDetailIter = dsfDetailList
					.iterator();
			Map<String, Object> theDetailMap = null;
			Integer transDetailId = 0;
			String dflag = null;
			SettleTransDetail detail = null;
			List<SettleTransDetail> sendToOrderList = new ArrayList<SettleTransDetail>();
			while (dsfTransDetailIter.hasNext()) {
				theDetailMap = dsfTransDetailIter.next();// 当前交易信息
				transDetailId = Integer.parseInt(String.valueOf(theDetailMap
						.get("TRANS_DETAIL_ID")));
				dflag = String.valueOf(theDetailMap.get("DFLAG"));
				detail = new SettleTransDetail();
				detail.setTransDetailId(transDetailId);
				detail.setDflag(Integer.parseInt(dflag));
				detail.setOrderNo(String.valueOf(theDetailMap.get("ORDER_NO")));
				detail.setRemark(String.valueOf(theDetailMap.get("REMARK")));
				sendToOrderList.add(detail);
			}

			this.returnToOrder(sendToOrderList);
		} catch (Exception e) {
			logger.error(">>>>>返回代收付结果给账户系统的方法(人工触发的,不是MQ监听类里的方法)异常！！！"
					+ e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
		logger.info(">>>>>结束sendOrder方法");
	}

	@Override
	public SettleTransDetail queryDsfStatus(String orderNo, String funcCode)
			throws Exception {
		SettleTransDetail detail = null;
		try {
			SettleTransDetailQuery query = new SettleTransDetailQuery();
			query.setOrderNo(orderNo);
			query.setFuncCode(funcCode);
			logger.info(">>>>>根据orderNo=" + orderNo + ",funcCode=" + funcCode
					+ "查询交易表");
			List<SettleTransDetail> detailList = settleTransDetailManager
					.queryList(query);
			if (detailList != null && detailList.size() > 0) {
				detail = detailList.get(0);
				logger.info(">>>>>根据orderNo=" + orderNo + ",funcCode="
						+ funcCode + "查询交易表查到的结果不为空");
			}
		} catch (Exception e) {
			logger.error(">>>>>queryDsfStatus方法发生异常！！！！！！,orderNo=" + orderNo
					+ ",funcCode=" + funcCode + ",异常信息" + e.getMessage());
			throw new Exception(e.getMessage());
		}
		return detail;
	}

	@Override
	public void sendDsfAgain(Integer[] ids) {
		try {
			if (ids == null || ids.length == 0) {
				return;
			}
			logger.info(">>>>>进入手工发送代收付方法sendDsfAgain");
			// 查询结算表中代收付返回失败，且发送次数<=4次的记录
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("processResult", "13");
			map.put("nullResult", 1);
			map.put("sendTimes", 4);
			if (ids != null && ids.length > 0)
				map.put("ids", ids);
			List<SettleTransInvoice> tmpInvoiceList = settleTransInvoiceManager
					.selectTransInvoiceList(map);
			if (tmpInvoiceList != null) {
				logger.info(">>>>手工发送代收付方法中根据条件processResult=13,sendTimes<=4从结算表中查询到"
						+ tmpInvoiceList.size() + "条信息");
			} else {
				logger.info(">>>>手工发送代收付方法中根据条件processResult=13,sendTimes<=4从结算表中查询到0条信息");
			}
			Integer sendTimes = 0;
			String orderNo = null;
			String requestNo = null;
			List<SettleTransInvoice> sendInvoiceList = null;
			String tmpOrderNo = null;
			String tmpRequestNo = null;
			// 修改发送次数、订单号和交易批次号
			for (SettleTransInvoice bean : tmpInvoiceList) {
				logger.info(">>>>结算表的原发送次数、订单号、交易批次号为:sendTimes="
						+ bean.getSendTimes() + ",orderNo=" + bean.getOrderNo()
						+ ",requestNo=" + bean.getRequestNo());
				if (bean.getSendTimes() != null) {
					sendTimes = bean.getSendTimes().intValue() + 1;// 每发送一次发送次数加1
																	// 4+1最多5次
				} else {
					sendTimes = 1;// 发送次数加1
				}
				bean.setSendTimes(sendTimes);

				if (sendTimes == 1) {
					orderNo = sendTimes + "cf9" + bean.getOrderNo();// 每发送一次订单号前加次数+cf9
					requestNo = sendTimes + "cf9" + bean.getRequestNo();// 每发送一次批次号前加次数+cf9
				} else {
					tmpOrderNo = bean.getOrderNo();
					tmpRequestNo = bean.getRequestNo();
					if (StringUtils.isNotBlank(tmpOrderNo)) {
						tmpOrderNo = tmpOrderNo.substring(1,
								tmpOrderNo.length());
					}
					if (StringUtils.isNotBlank(tmpRequestNo)) {
						tmpRequestNo = tmpRequestNo.substring(1,
								tmpRequestNo.length());
					}
					orderNo = sendTimes + tmpOrderNo;// 每发送一次订单号前加次数
					requestNo = sendTimes + tmpRequestNo;// 每发送一次批次号前加次数
				}

				bean.setOrderNo(orderNo);
				bean.setRequestNo(requestNo);
				bean.setStatusId(1);
				bean.setProcessResult("");

				settleTransInvoiceManager.updateSettleTransInvoice(bean);
				logger.info(">>>>更新结算表的发送次数、订单号、交易批次号为:sendTimes=" + sendTimes
						+ ",orderNo=" + orderNo + ",requestNo=" + requestNo);
				// 调用代收付系统接口进行重发
				sendInvoiceList = new ArrayList<SettleTransInvoice>();
				sendInvoiceList.add(bean);
				if (",4013,4014,4014_1,4016,".indexOf("," + bean.getFuncCode()
						+ ",") != -1) {
					this.sendToPaySys(sendInvoiceList, requestNo);
				}
			}
		} catch (Exception e) {
			logger.error(">>>>>>sendDsfAgain方法发生异常！！！！！！！" + e);
		}
	}

	/**
	 * 给每个机构号生成对应的交易批次号,并放入map中
	 * 
	 * @param merChantSet
	 *            机构号的set集合
	 * @return "机构号+交易码"<-对应的->交易批次号的MAP
	 * @throws Exception
	 */
	public Map<String, String> createMerchantRequestNo(
			Set<String> merChantAndFuncSet) throws Exception {
		logger.info(">>>>>开始获得机构号和交易批次号的对应关系");
		Map<String, String> mercharMap = new HashMap<String, String>();
		try {
			Iterator<String> mechantAndFuncIterator = merChantAndFuncSet
					.iterator();
			while (mechantAndFuncIterator.hasNext()) {// 遍历存放机构号的set集合
				String merchantAndFuncKey = String
						.valueOf(mechantAndFuncIterator.next());// 拿到机构号+交易码
				String requestNo = settlementUtil.createBatchNo("DSF");// 生成交易批次号
				mercharMap.put(merchantAndFuncKey, requestNo);
				logger.info(">>>>给机构号和func_code:" + merchantAndFuncKey
						+ "生成的交易批次号是 " + requestNo);
			}
		} catch (Exception e) {
			logger.error(">>>>获得机构号和交易批次号的对应关系发生异常！!!!!!" + e.getMessage());
			throw new Exception(e.getMessage());
		}
		logger.info(">>>>>结束获得机构号和交易批次号的对应关系");
		return mercharMap;
	}

	/**
	 * 汇总detail表的数据
	 * 
	 * @param dsfOnOff
	 *            汇总开关
	 * @param dsfDetailList
	 *            detail表的list
	 * @param amountMap
	 *            存放汇总金额的map
	 * @param detailBeanMap
	 *            存放机构号+用户ID+交易类型+账期相同
	 * @param merChantSet
	 * @throws Exception
	 */
	public void collectDetail(Boolean dsfOnOff,
			List<Map<String, Object>> dsfDetailList,
			Map<String, Object> amountMap, Map<String, Object> detailBeanMap,
			Set<String> merChantAndFuncSet) throws Exception {
		logger.info(">>>>开始汇总detail表的数据");
		try {
			String key = "";
			String userIdOrInterManchantCode = null;
			Iterator<Map<String, Object>> dsfTransDetailIter = dsfDetailList
					.iterator();
			while (dsfTransDetailIter.hasNext()) {
				Map<String, Object> theDetailMap = dsfTransDetailIter.next();
				if (StringUtils.isNotBlank((String) theDetailMap
						.get("INTER_MERCHANT_CODE"))) {
					userIdOrInterManchantCode = (String) theDetailMap
							.get("INTER_MERCHANT_CODE");
				} else {
					userIdOrInterManchantCode = String.valueOf(theDetailMap
							.get("USER_ID"));
				}

				if (dsfOnOff
						&& !"4013".equals(String.valueOf(theDetailMap
								.get("FUNC_CODE")))) {// 开关为true,进行汇总 //改! 代收
														// 4013 交易永远不汇总
					// key=机构号+用户ID+交易类型+账期
					key = String.valueOf(theDetailMap.get("MERCHANT_CODE"))
							+ userIdOrInterManchantCode
							+ String.valueOf(theDetailMap.get("FUNC_CODE"))
							+ String.valueOf(theDetailMap.get("ACCOUNT_DATE"))
							+ String.valueOf(theDetailMap.get("DATA_FROM"))
							+ String.valueOf(theDetailMap.get("PAY_CHANNEL_ID"));
					BigDecimal dsfAmount = new BigDecimal(
							String.valueOf(theDetailMap.get("AMOUNT")));
					if (amountMap.containsKey("M" + key)) {
						// 将交易金额放入amountMap,注意加M
						amountMap.put(
								"M" + key,
								new BigDecimal(String.valueOf((amountMap
										.get("M" + key)))).add(dsfAmount));
					} else {
						detailBeanMap.put(key, theDetailMap);// 机构号+用户ID+交易类型+账期相同的，只记录一个bean,不用每个都存
						amountMap.put("M" + key, dsfAmount);// 将交易金额放入amountMap,注意加M
					}
				} else {// 开关为false，不进行汇总
					if ("4013".equals(String.valueOf(theDetailMap
							.get("FUNC_CODE")))) {
						logger.info(">>>> dsfOnOff:" + dsfOnOff
								+ ", 代收 (4013) 交易永远不汇总! >>>>>>>>>>>>>>>>>");
					} else {
						logger.info(">>>> 代收付汇总开关是关闭的 >>>>>>>>>>>>>>>>>");
					}
				}
				merChantAndFuncSet.add(String.valueOf(theDetailMap
						.get("MERCHANT_CODE"))
						+ String.valueOf(theDetailMap.get("FUNC_CODE"))
						+ String.valueOf(theDetailMap.get("PAY_CHANNEL_ID")));// set存放的都是去重后的(机构号+func_code)
			}
		} catch (Exception e) {
			logger.error(">>>>汇总detail表的数据发生异常！!!!!!" + e.getMessage());
			throw new Exception(e.getMessage());
		}
		logger.info(">>>>结束汇总detail表的数据");
	}

	/**
	 * 修改detailMap,放入交易批次号reqNo,金额amount,批次号batchNo
	 * 
	 * @param theDetailMap
	 * @param mercharMap
	 * @param rootInstCd
	 * @param amount
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> updateTheDetailMap(
			Map<String, Object> theDetailMap,
			Map<String, String> merAndFuncResNoMap, String requestNoKey,
			BigDecimal amount) throws Exception {
		logger.info(">>>>>开始修改detailMap,放入交易批次号reqNo,金额amount,批次号batchNo");
		try {
			String reqNo = merAndFuncResNoMap.get(requestNoKey); // 交易批次号
			String batchNo = settlementUtil.createBatchNo("BatchNo");// 生成批次号,注意不是交易批次号
			theDetailMap.put("reqNo", reqNo);
			theDetailMap.put("totalAmount", amount);// 单条金额或汇总金额
			theDetailMap.put("batchNo", batchNo);
		} catch (Exception e) {
			logger.error(">>>>发生异常！！！！！修改detailMap,放入交易批次号reqNo,金额amount,批次号batchNo,TRANS_DETAIL_ID="
					+ theDetailMap.get("TRANS_DETAIL_ID") + e.getMessage());
			throw new Exception(e.getMessage());
		}
		logger.info(">>>>>结束修改detailMap,放入交易批次号reqNo,金额amount,批次号batchNo");
		return theDetailMap;
	}

	/**
	 * 批量更新detail交易表的dflag字段
	 * 
	 * @param theDetailMap
	 * @throws Exception
	 */
	public List<SettleTransDetail> updateBatchDetailDflag(
			Map<String, Object> theDetailMap) throws Exception {
		List<SettleTransDetail> transDetailList = null;
		try {
			transDetailList = new ArrayList<SettleTransDetail>();
			String funcCode = String.valueOf(theDetailMap.get("FUNC_CODE"));
			String accountDate = String.valueOf(theDetailMap
					.get("ACCOUNT_DATE"));
			String merchantCode = String.valueOf(theDetailMap
					.get("MERCHANT_CODE"));// 机构号
			String userId = String.valueOf(theDetailMap.get("USER_ID"));// 用户ID
			logger.info(">>>>批量更新交易表的dflag字段,根据条件查出来的全部更新funcCode=" + funcCode
					+ ",accountDate=" + accountDate + ",merchantCode="
					+ merchantCode + ",userId=" + userId);
			Map<String, Object> detailwhereMap = new HashMap<String, Object>();
			detailwhereMap.put("dFlags", new Integer[] { 0 });
			detailwhereMap.put("merchantCode", merchantCode);
			detailwhereMap.put("userId", userId);
			detailwhereMap.put("funcCode", funcCode);
			detailwhereMap.put("accountDate", accountDate);
			List<Map<String, Object>> dsfDetailList = dsfLogic
					.getDsfTransDetail(detailwhereMap);// 获取未处理的代收付交易List
			Iterator<Map<String, Object>> dsfTransDetailIter = dsfDetailList
					.iterator();
			while (dsfTransDetailIter.hasNext()) {
				Map<String, Object> theDetailBeanMap = dsfTransDetailIter
						.next();
				SettleTransDetail detailBean = new SettleTransDetail();
				detailBean.setTransDetailId(Integer.parseInt(String
						.valueOf(theDetailBeanMap.get("TRANS_DETAIL_ID"))));
				detailBean.setDflag(99);
				detailBean.setObligate1("绑卡信息为空");
				// settleTransDetailManager.updateSettleTransDetail(detailBean);
				transDetailList.add(detailBean);
			}
		} catch (Exception e) {
			logger.error(">>>>>>批量更新detail交易表的dflag字段报错！！！！！！！TRANS_DETAIL_ID = "
					+ theDetailMap.get("TRANS_DETAIL_ID") + e.getMessage());
		}
		return transDetailList;
	}

	/**
	 * 单条更新detail交易表的dflag字段
	 * 
	 * @param theDetailMap
	 * @throws Exception
	 */
	// public void updateOneDetailDflag(Map<String,Object> theDetailMap) throws
	// Exception{
	// try{
	// SettleTransDetail detailBean = new SettleTransDetail();
	// detailBean.setTransDetailId(Integer.parseInt(String.valueOf(theDetailMap.get("TRANS_DETAIL_ID"))));
	// detailBean.setDflag(99);
	// detailBean.setObligate1("绑卡信息为空");
	// logger.info("更新单条交易表的绑卡信息为空,orderNo="+theDetailMap.get("ORDER_NO"));
	// settleTransDetailManager.updateSettleTransDetail(detailBean);
	// }catch(Exception e){
	// logger.error(">>>>>>单条更新detail交易表的dflag字段报错！！！！！！！TRANS_DETAIL_ID = "+theDetailMap.get("TRANS_DETAIL_ID")+e.getMessage());
	// }
	// }

	/**
	 * 给结算表的Bean SettleTransInvoice 新增从订单系统查到的卡信息
	 * 
	 * @param invoiceBean
	 * @param orderAccountInfo
	 * @throws Exception
	 */
	public void modifyInvoiceByOrderAccountInfo(SettleTransInvoice invoiceBean,
			OrderAccountInfo orderAccountInfo) throws Exception {
		try {
			logger.info("开始给结算表的orderNo=" + invoiceBean.getOrderNo()
					+ "新增从订单系统查到的绑卡信息");
			invoiceBean.setBankCode(orderAccountInfo.getBankHead());// 银行代码**********
			invoiceBean.setAccountType(orderAccountInfo.getAccountTypeId());// 账户类型
			invoiceBean.setAccountNo(orderAccountInfo.getAccountNumber());// 账号
			invoiceBean.setAccountName(orderAccountInfo.getAccountName());// 账号名
			invoiceBean.setAccountProperty(orderAccountInfo
					.getAccountProperty());// 账户属性
			invoiceBean.setProvince(orderAccountInfo.getBankProvince());// 开户行所在省
			invoiceBean.setCity(orderAccountInfo.getBankCity());// 开户行所在市
			invoiceBean.setOpenBankName(orderAccountInfo.getBankBranchName());// 开户行名称
			invoiceBean.setPayBankCode(orderAccountInfo.getBankBranch());// 支付行号
			invoiceBean.setCurrency(orderAccountInfo.getCurrency());// 币种
			invoiceBean
					.setCertificateType(RegexUtil.IsIntNumber(orderAccountInfo
							.getCertificateType()) ? orderAccountInfo
							.getCertificateType() : "99");// 开户证件类型
			invoiceBean.setCertificateNumber(orderAccountInfo
					.getCertificateNumber());// 证件号
		} catch (Exception e) {
			logger.error(">>>>>>>>modifyInvoiceByOrderAccountInfo给结算表Bean新增信息异常！！！！！！！！！orderNo="
					+ invoiceBean.getOrderNo() + ",异常信息:" + e.getMessage());
			throw new Exception(e.getMessage());
		}
		logger.info("结束给结算表的orderNo=" + invoiceBean.getOrderNo()
				+ "新增从订单系统查到的绑卡信息");
	}

	/**
	 * 给结算表的Bean SettleTransInvoice 新增从账户系统查到的一分钱代付卡信息
	 * 
	 * @param invoiceBean
	 * @param corporatAccountInfo
	 * @throws Exception
	 */
	public void modifyInvoiceByCorporatAccountInfo(
			SettleTransInvoice invoiceBean,
			CorporatAccountInfoExt corporatAccountInfo) throws Exception {
		try {
			logger.info("开始给结算表的orderNo=" + invoiceBean.getOrderNo()
					+ "新增从账户系统查到的一分钱代付的绑卡信息");
			invoiceBean.setBankCode(corporatAccountInfo.getBankHead());// 银行代码
			invoiceBean.setAccountType("00");// 账户类型 00：银行卡，01：存折，02：信用卡
			invoiceBean.setAccountNo(corporatAccountInfo.getAccountNumber());// 账号
			invoiceBean
					.setAccountName(corporatAccountInfo.getAccountRealName());// 账号名
			invoiceBean.setAccountProperty("1");// 账户属性: 对公 1, 对私2
			invoiceBean.setProvince(corporatAccountInfo.getBankProvince());// 开户行所在省
			invoiceBean.setCity(corporatAccountInfo.getBankCity());// 开户行所在市
			invoiceBean
					.setOpenBankName(corporatAccountInfo.getBankBranchName());// 开户行名称
			invoiceBean.setPayBankCode(corporatAccountInfo.getBankBranch());// 支付行号
			invoiceBean.setCurrency(corporatAccountInfo.getCurrency());// 币种
			invoiceBean
					.setCertificateType(RegexUtil
							.IsIntNumber(corporatAccountInfo
									.getCertificateType()) ? corporatAccountInfo
							.getCertificateType() : "99");// 开户证件类型
			invoiceBean.setCertificateNumber(corporatAccountInfo
					.getCertificateNumber());// 证件号
		} catch (Exception e) {
			logger.error(">>>>>>>>modifyInvoiceByCorporatAccountInfo给结算表Bean新增信息异常！！！！！！！！！orderNo="
					+ invoiceBean.getOrderNo() + ",异常信息:" + e.getMessage());
			throw new Exception(e.getMessage());
		}
		logger.info("结束给结算表的orderNo=" + invoiceBean.getOrderNo()
				+ "新增从账户系统查到的一分钱代付的绑卡信息");
	}

	/**
	 * 给结算表的Bean SettleTransInvoice 新增从账户系统查到卡信息
	 * 
	 * @param invoiceBean
	 * @param accountInfo
	 * @throws Exception
	 */
	public void modifyInvoiceByAccountInfoExt(SettleTransInvoice invoiceBean,
			AccountInfoExt accountInfoExt) throws Exception {
		try {
			logger.info("结束给结算表的orderNo=" + invoiceBean.getOrderNo()
					+ "新增从账户系统查到的绑卡信息");
			invoiceBean.setBankCode(accountInfoExt.getBankHead());// 银行代码
			invoiceBean.setAccountType(accountInfoExt.getAccountTypeId());// 账户类型
			invoiceBean.setAccountNo(accountInfoExt.getAccountNumber());// 账号
			invoiceBean.setAccountName(accountInfoExt.getAccountRealName());// 账号名
			invoiceBean.setAccountProperty(accountInfoExt.getAccountProperty());// 账户属性
			invoiceBean.setProvince(accountInfoExt.getBankProvince());// 开户行所在省
			invoiceBean.setCity(accountInfoExt.getBankCity());// 开户行所在市
			invoiceBean.setOpenBankName(accountInfoExt.getBankBranchName());// 开户行名称
			invoiceBean.setPayBankCode(accountInfoExt.getBankBranch());// 支付行号
			invoiceBean.setCurrency(accountInfoExt.getCurrency());// 币种
			invoiceBean.setCertificateType(RegexUtil.IsIntNumber(accountInfoExt
					.getCertificateType()) ? accountInfoExt
					.getCertificateType() : "99");// 开户证件类型
			invoiceBean.setCertificateNumber(accountInfoExt
					.getCertificateNumber());// 证件号
		} catch (Exception e) {
			logger.error(">>>>>>>>modifyInvoiceByAccountInfo给结算表Bean新增信息异常！！！！！！！！！invoiceNo="
					+ invoiceBean.getInvoiceNo());
			throw new Exception(e.getMessage());
		}
		logger.info("结束给结算表的orderNo=" + invoiceBean.getOrderNo()
				+ "新增从账户系统查到的绑卡信息");
	}

	/**
	 * 给结算表的Bean SettleTransInvoice 新增账户一期的绑卡数据信息
	 * 
	 * @param invoiceBean
	 * @param accountInfo
	 * @throws Exception
	 */
	public void updateInvoiceByAccountInfo(SettleTransInvoice invoiceBean,
			AccountInfo accountInfo) throws Exception {
		try {
			logger.info("333accountInfo===" + accountInfo + ","
					+ accountInfo.getAccountPurpose());
			logger.info("结束给结算表的orderNo=" + invoiceBean.getOrderNo()
					+ "新增从账户系统查到的绑卡信息");
			if (invoiceBean.getOrderType() == 8) {
				invoiceBean.setBankCode(accountInfo.getBankHeadName());// 银行名称
			} else {
				invoiceBean.setBankCode(accountInfo.getBankHead());// 银行代码
			}
			invoiceBean.setAccountType(accountInfo.getAccountTypeId());// 账户类型
			invoiceBean.setAccountNo(accountInfo.getAccountNumber());// 账号
			invoiceBean.setAccountName(accountInfo.getAccountRealName());// 账号名
			invoiceBean.setAccountProperty(accountInfo.getAccountProperty());// 账户属性
			invoiceBean.setProvince(accountInfo.getBankProvince());// 开户行所在省
			invoiceBean.setCity(accountInfo.getBankCity());// 开户行所在市
			invoiceBean.setOpenBankName(accountInfo.getBankBranchName());// 开户行名称
			invoiceBean.setPayBankCode(accountInfo.getBankBranch());// 支付行号
			invoiceBean.setCurrency(accountInfo.getCurrency());// 币种
			invoiceBean.setCertificateType(RegexUtil.IsIntNumber(accountInfo
					.getCertificateType()) ? accountInfo.getCertificateType()
					: "99");
			invoiceBean
					.setCertificateNumber(accountInfo.getCertificateNumber());// 证件号
		} catch (Exception e) {
			logger.error(">>>>>>>>organizeInvoiceToList给结算表Bean新增信息异常！！！！！！！！！invoiceNo="
					+ invoiceBean.getInvoiceNo());
			throw new Exception(e.getMessage());
		}
		logger.info("结束给结算表的orderNo=" + invoiceBean.getOrderNo()
				+ "新增从账户系统查到的绑卡信息");
	}

	@Override
	public void sendOrderAgain(Integer[] ids, Integer[] statusIds) {
		try {
			// 查询结算表中代收付返回失败D的记录
			Map<String, Object> map = new HashMap<String, Object>();
			if (ids != null && ids.length > 0) {
				map.put("ids", ids);
			}
			List<SettleTransInvoice> TmpInvoiceList = settleTransInvoiceManager
					.selectTransInvoiceList(map);
			String orgNumAndChannelHome = null;

			for (SettleTransInvoice invoiceBean : TmpInvoiceList) {
				orgNumAndChannelHome = invoiceBean.getGeneSeq();
				// 去重
				if (SettleConstants.DSF_HAS_RETURN != invoiceBean.getStatusId()) {
					this.updateSummeryStatusId(invoiceBean.getBatchNo(),
							invoiceBean.getProcessResult(),
							invoiceBean.getRemark(), false,
							orgNumAndChannelHome);
				}
			}
		} catch (Exception e) {
			logger.error(">>>>>>sendAccountAgain方法发生异常！！！！！！！");
		}
	}

	/**
	 * 将代收付结果更新到汇总表
	 * 
	 * @param batchNo
	 * @param returnStatusId
	 */
	public Boolean updateSummeryStatusId(String batchNo, String processResult,
			String remark, Boolean sendOrderFlag, String orgNumAndChannelHome) {
		Boolean summeryResult = true;
		try {
			SettleTransSummaryQuery summeryQuery = new SettleTransSummaryQuery();
			summeryQuery.setBatchNo(batchNo);
			summeryQuery.setStatusIds(new Integer[] {
					SettleConstants.EXCEPTION_96, SettleConstants.STATUSID_1 });
			List<SettleTransSummary> summaryList = settleTransSummaryManager
					.queryList(summeryQuery);

			int dflag = 0;
			if ("13".equals(processResult)) {
				dflag = 4;
			} else if ("15".equals(processResult)) {
				dflag = 6;
			} else {
				dflag = 99;
			}
			List<SettleTransDetail> sendToOrderList = new ArrayList<SettleTransDetail>();
			SettleTransDetail detailBean = null;
			for (SettleTransSummary summaryBean : summaryList) {
				summaryBean.setStatusId(Integer.parseInt(processResult));
				try {
					detailBean = ((DsfServiceImpl) AopContext.currentProxy())
							.updateSummaryAndDetailOne(summaryBean, dflag,
									remark, orgNumAndChannelHome);
				} catch (Exception e) {
					logger.error("错误信息:" + e.getMessage());
				}

				sendToOrderList.add(detailBean);
			}
			if (sendOrderFlag == true)
				this.returnToOrder(sendToOrderList);
			// this.returnAccount(sendToOrderList);
		} catch (Exception e) {
			logger.error(">>>>>updateSummeryStatusId发生异常!!!!!!batchNo="
					+ batchNo);
			summeryResult = false;
		}
		return summeryResult;
	}

	/**
	 * 根据账户系统返回的结果更新交易表的dflag
	 * 
	 * @param detailBean
	 *            交易表的bean
	 * @param commonResponse
	 *            账户系统的返回结果对象
	 */
	public void updateDetailByAccountReturn(Map<String, String> orderMap,
			UpdateOrderRes updateOrderRes) {
		if (updateOrderRes == null) {
			logger.info(">>>根据账户系统返回的结果更新交易表的commonResponse方法的入参为空!");
		}
		try {
			String merchantCode = null;
			String orderNo = null;
			String roleCode = null;
			String result = null;
			String[] resultArray = null;
			Map<String, String> failOrderMap = new HashMap<String, String>();

			if (updateOrderRes.getFailOrderSet() != null
					&& updateOrderRes.getFailOrderSet().size() > 0) {
				logger.info("一分钱代付结果推送给账户，账户返回有部分失败，失败的条数："
						+ updateOrderRes.getFailOrderSet().size());
				Set<String> failSet = updateOrderRes.getFailOrderSet();
				Iterator<String> iterator = failSet.iterator();
				while (iterator.hasNext()) {
					result = iterator.next();// 机构号_订单号_角色号
					resultArray = result.split(",");
					if (resultArray != null && resultArray.length > 0) {
						if (StringUtils.isNotBlank(resultArray[1])) {
							orderNo = resultArray[1];
							failOrderMap.put(orderNo, orderNo);
						}
					}
					SettleTransDetailQuery query = new SettleTransDetailQuery();
					query.setOrderNo(orderNo);
					query.setMerchantCode(merchantCode);
					query.setRoleCode(roleCode);
					List<SettleTransDetail> detailList = settleTransDetailManager
							.queryList(query);
					for (SettleTransDetail detail : detailList) {
						if (detail.getDflag() == 6) {
							detail.setDflag(61);
						} else if (detail.getDflag() == 4) {
							detail.setDflag(41);
						} else {
							continue;
						}
						settleTransDetailManager
								.updateSettleTransDetail(detail);
					}
				}

			}
			SettleTransDetail detailBean = new SettleTransDetail();
			for (String key : orderMap.keySet()) {
				if (!failOrderMap.containsKey(key)) {
					String transDetailIdAndDflag = orderMap.get(key);// 得到transDetailId
					if (StringUtils.isNotBlank(transDetailIdAndDflag)) {
						String[] detailIdAndDflagArray = transDetailIdAndDflag
								.split(":");
						if (detailIdAndDflagArray != null
								&& detailIdAndDflagArray.length > 0
								&& detailIdAndDflagArray[0] != null
								&& detailIdAndDflagArray[1] != null) {
							detailBean.setTransDetailId(Integer
									.parseInt(detailIdAndDflagArray[0]));
							if (",6,61,".indexOf(","
									+ String.valueOf(detailIdAndDflagArray[1])
									+ ",") != -1) {
								detailBean.setDflag(60);
							} else if (",4,41,".indexOf(","
									+ String.valueOf(detailIdAndDflagArray[1])
									+ ",") != -1) {
								detailBean.setDflag(40);
							} else {
								continue;
							}
							settleTransDetailManager
									.updateSettleTransDetail(detailBean);
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("账户系统返回的结果更新交易表的dflag发生异常,errorMsg:" + e.getMessage());
		}
	}

	/**
	 * 将代收付结果返回给订单系统并更新交易表dflag状态
	 * 
	 * @throws Exception
	 */
	public void returnToOrder(List<SettleTransDetail> sendToOrderList)
			throws Exception {
		try {
			List<OrderPayment> orderPaymentList = new ArrayList<OrderPayment>();
			if (sendToOrderList != null && sendToOrderList.size() > 0) {
				String orderStatusId = null;
				OrderPayment orderPayment = null;
				for (SettleTransDetail detailBean : sendToOrderList) {
					orderPayment = new OrderPayment();
					orderPayment.setPaymentId(detailBean.getOrderNo());
					if (detailBean.getDflag() == 6) {
						orderStatusId = "0";// 成功
					} else if (detailBean.getDflag() == 4) {
						orderStatusId = "1";// 失败
					}
					orderPayment.setStatusId(orderStatusId);
					orderPayment.setRetMsg(detailBean.getRemark());
					orderPaymentList.add(orderPayment);

					// if(detailBean.getDflag() == 6){
					// detailBean.setDflag(60);
					// }else if(detailBean.getDflag() == 4){
					// detailBean.setDflag(40);
					// }else{
					// continue;
					// }
					// settleTransDetailManager.updateSettleTransDetail(detailBean);
				}
			}

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("paymentList", orderPaymentList);
			// 订单系统只返回处理失败的list
			Map<String, Object> resultmap = settlementToOrderService
					.updateBatchPaymentStatus(map);
			if (resultmap != null && !resultmap.isEmpty()) {
				List<OrderPayment> resultList = (List<OrderPayment>) resultmap.get("paymentList");
//				String orderNo = null;
				if (resultList != null && resultList.size() > 0) {
					// for(OrderPayment orderBean : resultList){
					// orderNo = orderBean.getPaymentId();
					// SettleTransDetailQuery detailQuery = new
					// SettleTransDetailQuery();
					// detailQuery.setOrderNo(orderNo);
					// detailQuery.setOrderType(0);
					// List<SettleTransDetail> detailBeanList =
					// settleTransDetailManager.queryList(detailQuery);
					// if(detailBeanList !=null && detailBeanList.size()>0){
					// SettleTransDetail detailBean = detailBeanList.get(0);
					// if(detailBean.getDflag() == 6){
					// detailBean.setDflag(61);
					// }else if(detailBean.getDflag() == 4){
					// detailBean.setDflag(41);
					// }else{
					// continue;
					// }
					// settleTransDetailManager.updateSettleTransDetail(detailBean);
					// }
					// }
				}
			}
		} catch (Exception e) {
			logger.error("将代收付结果返回给订单系统并更新交易表dflag状态发生异常！！！！！");
			throw new Exception(e.getMessage());
		}
	}

	// 更新汇总表和交易表(单条)
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public SettleTransDetail updateSummaryAndDetailOne(
			SettleTransSummary summaryBean, int dflag, String errorMsg,
			String orgNumAndChannelHome) throws Exception {
		SettleTransDetail detailBean = null;
		try {
			settleTransSummaryManager.updateSettleTransSummary(summaryBean);// 更新汇总表
			logger.info(">>>>更新汇总表的单条记录成功,orderNo=" + summaryBean.getOrderNo());

			SettleTransDetailQuery detailQuery = new SettleTransDetailQuery();
			detailQuery.setOrderNo(summaryBean.getOrderNo());
			detailQuery.setDflag(1);
			List<SettleTransDetail> detailList = settleTransDetailManager
					.queryList(detailQuery);
			if (detailList != null && detailList.size() > 0) {
				detailBean = detailList.get(0);
				detailBean.setDflag(dflag);
				detailBean.setErrorMsg(errorMsg);
				// 获取多渠道ChannelHome 和 清结算 PayChannelId 对应关系
				List<SettleParameterInfo> payChannelIdList = logicConstantUtil
						.getMultiGateChannelHome2PayChannelId();

				// 将orgNumAndChannelHome中的ChannelHome改成payChannelID
				if (orgNumAndChannelHome != null
						&& orgNumAndChannelHome.split(",").length > 1) {
					String[] orgNumAndChannelHomeArr = orgNumAndChannelHome
							.split(",");
					// 协议号
					String orgNum = orgNumAndChannelHomeArr[0];
					// 渠道名称
					String ChannelHome = orgNumAndChannelHomeArr[1];
					// 渠道ID
					String payChannelId = null;
					// 遍历对应关系进行匹配
					for (SettleParameterInfo paraInfo : payChannelIdList) {
						if (ChannelHome.equals(paraInfo.getParameterValue())) {// 匹配成功
							payChannelId = paraInfo.getParameterCode();
							break;
						}
					}
					if (payChannelId == null) {// 匹配失败,未匹配到对应的payChannelID
						payChannelId = ChannelHome;
						logger.error(">>> >>> 异常: " + orgNumAndChannelHome
								+ "中的 ChannelHome 未找到对应的 payChannelID");
					}
					orgNumAndChannelHome = orgNum + "," + payChannelId;
				} else {
					logger.error(">>> >>> 异常: 代收付的MQ监听类中更新单条汇总表和交易表发生异常! 入参orgNumAndChannelHome:"
							+ orgNumAndChannelHome + "有误");
				}
				detailBean.setPayChannelId(orgNumAndChannelHome);
				settleTransDetailManager.updateSettleTransDetail(detailBean);
				logger.info(">>>>更新交易表的单条记录成功,orderNo="
						+ summaryBean.getOrderNo());
			}
		} catch (Exception e) {
			detailBean = null;
			logger.error("代收付更新单条汇总表和交易表发生异常!!!!!!orderNo="
					+ summaryBean.getOrderNo() + e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
		return detailBean;
	}

	/**
	 * 退票的或一分钱代付推送账户失败的手工重发
	 * 
	 * @throws Exception
	 */
	@Override
	public void sendAccountTP(Integer[] ids, Integer[] deliverStatusIds) {
		logger.info("开始将退票失败的或异常的手工重发给账户系统！");
		try {
			// 退票逻辑 start
			Map<String, Object> detailwhereMap = new HashMap<String, Object>();
			if (ids != null && ids.length > 0) {
				detailwhereMap.put("ids", ids);
			}
			if (deliverStatusIds != null && deliverStatusIds.length > 0) {
				detailwhereMap.put("deliverStatusIds", deliverStatusIds);
			} else {
				detailwhereMap.put("deliverStatusIds", new Integer[] { 99 });
			}
			List<Map<String, Object>> dsfDetailList = dsfLogic
					.getDsfTransDetail(detailwhereMap);// 获取交易List
			Iterator<Map<String, Object>> dsfTransDetailIter = dsfDetailList
					.iterator();
			Map<String, Object> theDetailMap = null;
//			String originalOrderId = null;
//			String merchantCode = null;
//			String roleCode = null;
//			String dealProductCode = null;
//			String orderNo = null;
//			String userIpAddress = null;
//			String dateFrom = null;
//			Boolean isSuccess = false;
//			Integer deliverStatusId = 0;
//			CommonResponse commonResponse = null;
			Map<String, List<SettleTransDetail>> detailList = new HashMap<String, List<SettleTransDetail>>();
			List<SettleTransDetail> detailSubList = new ArrayList<SettleTransDetail>();
			SettleTransDetail detailSub = new SettleTransDetail();
			while (dsfTransDetailIter.hasNext()) {
				theDetailMap = dsfTransDetailIter.next();// 当前交易信息
				detailSub.setOrderNo(String.valueOf(theDetailMap
						.get("ORDER_NO")));
				detailSub.setStatusId(Integer.parseInt(String
						.valueOf(theDetailMap.get("READ_STATUS_ID"))));
				detailSub.setErrorMsg(String.valueOf(theDetailMap
						.get("ERROR_MSG")));

				detailSubList.add(detailSub);
			}

			detailList.put("1", detailSubList);
			dsfMessageListener.returnMq(detailList);

			// while(dsfTransDetailIter.hasNext()){
			// theDetailMap = dsfTransDetailIter.next();//当前交易信息
			// originalOrderId =
			// String.valueOf(theDetailMap.get("ORIGINAL_ORDER_ID"));
			// merchantCode = String.valueOf(theDetailMap.get("MERCHANT_CODE"));
			// roleCode = String.valueOf(theDetailMap.get("ROLE_CODE"));
			// dealProductCode =
			// String.valueOf(theDetailMap.get("DEAL_PRODUCT_CODE"));
			// orderNo = String.valueOf(theDetailMap.get("ORDER_NO"));
			// userIpAddress =
			// String.valueOf(theDetailMap.get("USER_IP_ADDRESS"));
			// dateFrom = String.valueOf(theDetailMap.get("DATA_FROM"));
			// if ("4".equals(dateFrom)) {
			// try{
			// TransOrderCashPayInfo transOrderInfo = new
			// TransOrderCashPayInfo();
			// transOrderInfo.setOriginalOrderId(orderNo);
			// transOrderInfo.setRootInstCd(merchantCode);
			// transOrderInfo.setRoleCode(roleCode);
			// transOrderInfo.setDealProductCode(SettleConstants.REFUND_DEAL_PRODUCT_CODE);
			// transOrderInfo.setOrderNo("ST"+new
			// Random().nextInt(100)+orderNo);
			// transOrderInfo.setUserIpAddress(userIpAddress);
			// transOrderInfo.setOrderCount(1);
			// transOrderInfo.setOrderDate(settlementUtil.getCurrentTime());
			// logger.info(">>>>>>开始调用账户系统的退票接口发起退票......originalOrderId="+transOrderInfo.getOriginalOrderId()+",rootInstCd="+transOrderInfo.getRootInstCd()+",roleCode="+transOrderInfo.getRoleCode()+",DealProductCode="+transOrderInfo.getDealProductCode()+",OrderNo="+transOrderInfo.getOrderNo()+",UserIpAddress"+transOrderInfo.getUserIpAddress()+",orderCounnt="+transOrderInfo.getOrderCount());
			// commonResponse = refundService.execute(transOrderInfo);
			// if(commonResponse !=null){
			// if("1".equals(commonResponse.getCode())){
			// isSuccess = true;
			// logger.info(">>>>>>调用账户系统的退票接口成功,orderNo="+orderNo+",账户系统返回code="+commonResponse.getCode()+",msg="+commonResponse.getMsg());
			// }else{
			// deliverStatusId = 1;
			// logger.info(">>>>>>调用账户系统的退票接口失败,orderNo="+orderNo+",账户系统返回code="+commonResponse.getCode()+",msg="+commonResponse.getMsg());
			// }
			// }else{
			// deliverStatusId = 1;
			// logger.info(">>>>>>调用账户系统的退票接口失败,orderNo="+orderNo+",账户系统返回的commonResponse="+commonResponse);
			// }
			// }catch(Exception e){
			// deliverStatusId = 99;
			// if(commonResponse !=null){
			// logger.error(">>>>调用账户系统的退票接口发起退票发生异常！！！！！！！！orderNo="+orderNo+",merchantCode="+merchantCode+",异常信息:code="+commonResponse.getCode()+",msg="+commonResponse.getMsg()+","+e.getMessage());
			// }
			// logger.error(">>>>调用账户系统的退票接口发起退票发生异常！！！！！！！！orderNo="+orderNo+",merchantCode="+merchantCode+",异常信息:"+e.getMessage());
			//
			// }
			// try{
			// SettleTransDetail detailBean = new SettleTransDetail();
			// detailBean.setTransDetailId((Integer)theDetailMap.get("TRANS_DETAIL_ID"));
			// detailBean.setDeliverStatusId(deliverStatusId);
			// settleTransDetailManager.updateSettleTransDetail(detailBean);
			// }catch(Exception e){
			// logger.info("更新交易表的退票接口状态失败(调用账户的退票接口)");
			// }
			// }
			// if ("0".equals(dateFrom)) {
			// com.rkylin.wheatfield.model.CommonResponse rtnResponse = null;
			// List<com.rkylin.wheatfield.bean.TransOrderInfo> paramList = new
			// ArrayList<com.rkylin.wheatfield.bean.TransOrderInfo>();//代收付1期
			// com.rkylin.wheatfield.bean.TransOrderInfo transOrderInfoOne = new
			// com.rkylin.wheatfield.bean.TransOrderInfo();
			// SettleTransDetail detailSubBean = new SettleTransDetail();
			// transOrderInfoOne.setMerchantCode(merchantCode);
			// transOrderInfoOne.setOrderNo(orderNo);
			// transOrderInfoOne.setAmount((Long)theDetailMap.get("AMOUNT"));
			// transOrderInfoOne.setErrorMsg("银行退票");
			// // transOrderInfoOne.setErrorCode(detailBean.getErrorCode());
			// transOrderInfoOne.setStatus(9);//9退票
			// paramList.add(transOrderInfoOne);
			//
			// try{
			// if(paramList !=null && paramList.size()>0){
			// rtnResponse =
			// transOrderDubboService.notifyTransOrderResults(paramList);//代付结果推送给账户
			// }
			// if (rtnResponse!=null && !"".equals(rtnResponse)) {
			// if ("1".equals(rtnResponse.getCode())) {
			// isSuccess = true;
			// logger.info(">>>>>>调用账户系统的退票接口成功,orderNo="+orderNo+",账户系统返回code="+commonResponse.getCode()+",msg="+commonResponse.getMsg());
			// }else{
			// deliverStatusId = 1;
			// logger.info(">>>>>>调用账户系统的退票接口失败,orderNo="+orderNo+",账户系统返回code="+commonResponse.getCode()+",msg="+commonResponse.getMsg());
			// }
			// }else{
			// deliverStatusId = 1;
			// logger.info(">>>>>>调用账户系统的退票接口失败,orderNo="+orderNo+",账户系统返回的commonResponse="+commonResponse);
			// }
			// }catch(Exception e){
			// deliverStatusId = 99;
			// if(commonResponse !=null){
			// logger.error(">>>>调用账户系统的退票接口发起退票发生异常！！！！！！！！orderNo="+orderNo+",merchantCode="+merchantCode+",异常信息:code="+commonResponse.getCode()+",msg="+commonResponse.getMsg()+","+e.getMessage());
			// }
			// logger.error(">>>>调用账户系统的退票接口发起退票发生异常！！！！！！！！orderNo="+orderNo+",merchantCode="+merchantCode+",异常信息:"+e.getMessage());
			// }
			// try{
			// SettleTransDetail detailBean = new SettleTransDetail();
			// detailBean.setTransDetailId((Integer)theDetailMap.get("TRANS_DETAIL_ID"));
			// detailBean.setDeliverStatusId(deliverStatusId);
			// settleTransDetailManager.updateSettleTransDetail(detailBean);
			// }catch(Exception e){
			// logger.info("更新交易表的退票接口(1期)状态失败(调用账户的退票接口)");
			// }
			// }
			// if(!isSuccess){//失败或异常
			// SendSMS.sendSMS(mobile, content1);//给相关人员发送短信
			// }
			// }
			// //退票逻辑 end
			// this.yfqDfResultSendToAccount(ids);//一分钱代付结果推送给账户
		} catch (Exception e) {
			logger.error(">>>>将退票失败的或异常的手工重发给账户系统发生异常！！！！");
		}
	}

	/**
	 * 手工将代收付结果推送给账户系统 注意：之前只有一分钱推给账户，后来需求变更成了所有代收付业务都推给账户，方法名沿用了以前的
	 * 
	 * @param ids
	 */
	public void yfqDfResultSendToAccount(Integer[] ids) {
		try {
			// 一分钱代付逻辑 start
			Map<String, Object> yfqWhereMap = new HashMap<String, Object>();
			if (ids != null && ids.length > 0) {
				yfqWhereMap.put("ids", ids);
			}
			yfqWhereMap.put("funcCodes", new String[] { "4014_1", "4014",
					"4013", "4016", "40143", "40144", "40147" });
			yfqWhereMap.put("dFlags", new Integer[] { 4, 6, 40, 60 });

//			List<TransOrderInfoExt> paramList = new ArrayList<TransOrderInfoExt>();// 一分钱代付
			List<Map<String, Object>> yfqDetailList = dsfLogic
					.getDsfTransDetail(yfqWhereMap);
			Iterator<Map<String, Object>> iterator = yfqDetailList.iterator();
			String torderNo = null;
			Integer transDetailId = null;
			Map<String, String> orderMap = new HashMap<String, String>();
			Map<String, List<SettleTransDetail>> accountMap = new HashMap<String, List<SettleTransDetail>>();
			List<SettleTransDetail> sendToAccount1List = new ArrayList<SettleTransDetail>();
			List<SettleTransDetail> sendToAccount2List = new ArrayList<SettleTransDetail>();
			SettleTransDetail detailBean = new SettleTransDetail();
			while (iterator.hasNext()) {
				Map<String, Object> detailMap = iterator.next();
				torderNo = String.valueOf(detailMap.get("ORDER_NO"));
				transDetailId = Integer.parseInt(stringToNum(detailMap
						.get("TRANS_DETAIL_ID")));
				orderMap.put(torderNo,
						transDetailId + ":" + detailMap.get("DFLAG"));

				detailBean = transDetailMapToList(detailMap);

				if ("0".equals(String.valueOf(detailMap.get("DATA_FROM")))) {// 账户1期
					if (accountMap.containsKey("1")) {
						sendToAccount1List = accountMap.get("1");
						sendToAccount1List.add(detailBean);
					} else {
						sendToAccount1List.add(detailBean);
						accountMap.put("1", sendToAccount1List);
					}
				} else if ("4"
						.equals(String.valueOf(detailMap.get("DATA_FROM")))) {// 账户2期
					if (accountMap.containsKey("2")) {
						sendToAccount2List = accountMap.get("2");
						sendToAccount2List.add(detailBean);
					} else {
						sendToAccount2List.add(detailBean);
						accountMap.put("2", sendToAccount2List);
					}
				} else {
					logger.info("dataFrom不满足=0或4的条件！！！！！");
				}
			}
			dsfMessageListener.returnAccount(accountMap);
			dsfMessageListener.returnMq(accountMap);
			// 代收付逻辑 end
		} catch (Exception e) {
			logger.error(
					"代收付结果推送给账户异常！！！！" + e.getLocalizedMessage() + ","
							+ e.getMessage(),
					e.getCause() + e.getLocalizedMessage() + e.getStackTrace()
							+ e.getSuppressed());
		}
	}

	/**
	 * 将结算中的代收付结果回写到汇总表和交易表
	 */
	@Override
	public void WriteResult(Integer[] ids, Integer[] statusIds) {
		try {
			// 查询结算表中代收付返回失败D的记录
			Map<String, Object> map = new HashMap<String, Object>();
			if (ids != null && ids.length > 0) {
				map.put("ids", ids);
			}
			List<SettleTransInvoice> TmpInvoiceList = settleTransInvoiceManager
					.selectTransInvoiceList(map);
			String orgNumAndChannelHome = null;
			for (SettleTransInvoice invoiceBean : TmpInvoiceList) {
				orgNumAndChannelHome = invoiceBean.getGeneSeq();
				// 去重
				if (SettleConstants.DSF_HAS_RETURN == invoiceBean.getStatusId()
						&& (",13,15,".indexOf(","
								+ invoiceBean.getProcessResult() + ",") != -1)) {
					this.updateSummeryStatusId(invoiceBean.getBatchNo(),
							invoiceBean.getProcessResult(),
							invoiceBean.getRemark(), false,
							orgNumAndChannelHome);
					invoiceBean.setStatusId(SettleConstants.DSF_HAS_WRITE);
					settleTransInvoiceManager
							.updateSettleTransInvoice(invoiceBean);
				}
			}
			this.removeSuccessToHis();// 数据移入历史
		} catch (Exception e) {
			logger.error(">>>>>>sendAccountAgain方法发生异常！！！！！！！");
		}
	}

	/**
	 * 更新汇总表和交易表(代收付结果回写)
	 * 
	 * @param batchNo
	 *            批次号
	 * @param processResult
	 *            代收付结果
	 */
	/*
	 * public void updateSummeryAndDetail(String batchNo,String
	 * processResult,String remark){ try{ SettleTransSummaryQuery summeryQuery =
	 * new SettleTransSummaryQuery(); summeryQuery.setBatchNo(batchNo);
	 * summeryQuery.setStatusIds(new
	 * Integer[]{SettleConstants.EXCEPTION_96,SettleConstants.STATUSID_1});
	 * List<SettleTransSummary> summaryList =
	 * settleTransSummaryManager.queryList(summeryQuery);
	 * 
	 * int dflag = 0; if("13".equals(processResult)){ dflag = 4; }else
	 * if("15".equals(processResult)){ dflag = 6; }else{ dflag = 99; }
	 * for(SettleTransSummary summaryBean : summaryList){
	 * summaryBean.setStatusId(Integer.parseInt(processResult)); try{
	 * ((DsfServiceImpl)
	 * AopContext.currentProxy()).updateSummaryAndDetailOne(summaryBean, dflag,
	 * remark,orgNumAndChannelHome); }catch(Exception e){
	 * logger.error("错误信息:"+e.getMessage()); } } }catch(Exception e){
	 * logger.error(">>>>>updateSummeryStatusId发生异常!!!!!!batchNo="+batchNo); } }
	 */

	/**
	 * 退票发送账户的方法
	 * 
	 * @param detailQuery
	 *            交易表的查询对象
	 */
	public void tuiPiaoSendAccount(SettleTransDetailQuery detailQuery) {
		Boolean isFail = false;
		Integer deliverStatusId = 0;
		CommonResponse commonResponse = null;
		List<SettleTransDetail> detailBeanList = settleTransDetailManager
				.queryList(detailQuery);
		for (SettleTransDetail detailBean : detailBeanList) {
			if (detailBean.getDataFrom() == 4) { // 账户2期
				try {
					// TransOrderCashPayInfo transOrderInfo = new
					// TransOrderCashPayInfo();
					// transOrderInfo.setOriginalOrderId(detailBean.getOrderNo());
					// transOrderInfo.setRootInstCd(detailBean.getMerchantCode());
					// transOrderInfo.setRoleCode(detailBean.getRoleCode());
					// transOrderInfo.setDealProductCode(SettleConstants.REFUND_DEAL_PRODUCT_CODE);
					// transOrderInfo.setOrderNo("ST"+new
					// Random().nextInt(100)+detailBean.getOrderNo());
					// transOrderInfo.setUserIpAddress(detailBean.getUserIpAddress());
					// transOrderInfo.setOrderCount(1);
					// transOrderInfo.setOrderDate(settlementUtil.getCurrentTime());
					// logger.info(">>>>>>开始调用账户系统的退票接口发起退票......originalOrderId="+transOrderInfo.getOrderNo()+",rootInstCd="+transOrderInfo.getRootInstCd()+",roleCode="+transOrderInfo.getRoleCode()+",DealProductCode="+transOrderInfo.getDealProductCode()+",OrderNo="+"ST"+transOrderInfo.getOrderNo()+",UserIpAddress"+transOrderInfo.getUserIpAddress()+",orderCount="+transOrderInfo.getOrderCount());
					// commonResponse = refundService.execute(transOrderInfo);
					// if(commonResponse !=null){
					// if("1".equals(commonResponse.getCode())){
					// logger.info(">>>>>>调用账户系统的退票接口成功,orderNo="+detailBean.getOrderNo()+",账户系统返回code="+commonResponse.getCode()+",msg="+commonResponse.getMsg());
					// }else{
					// deliverStatusId = 1;
					// logger.info(">>>>>>调用账户系统的退票接口失败,orderNo="+detailBean.getOrderNo()+",账户系统返回code="+commonResponse.getCode()+",msg="+commonResponse.getMsg());
					// }
					// }else{
					// deliverStatusId = 1;
					// logger.info(">>>>>>调用账户系统的退票接口失败,orderNo="+detailBean.getOrderNo()+",账户系统返回的commonResponse="+commonResponse);
					// }
				} catch (Exception e) {
					isFail = true;
					deliverStatusId = 99;
					if (commonResponse != null) {
						logger.error(">>>>调用账户系统的退票接口发起退票发生异常！！！！！！！！orderNo="
								+ detailBean.getOrderNo() + ",merchantCode="
								+ detailBean.getMerchantCode() + ",异常信息:code="
								+ commonResponse.getCode() + ",msg="
								+ commonResponse.getMsg() + ","
								+ e.getMessage());
					}
					logger.error(">>>>调用账户系统的退票接口发起退票发生异常！！！！！！！！orderNo="
							+ detailBean.getOrderNo() + ",merchantCode="
							+ detailBean.getMerchantCode() + ",异常信息:"
							+ e.getMessage());
				}
				try {
					detailBean.setDeliverStatusId(deliverStatusId);
					settleTransDetailManager
							.updateSettleTransDetail(detailBean);
				} catch (Exception e) {
					logger.info("更新交易表的退票接口状态失败(调用账户的退票接口)");
				}
			}
			if (detailBean.getDataFrom() == 0) {// 账户1期
				com.rkylin.wheatfield.model.CommonResponse rtnResponse = null;
				List<com.rkylin.wheatfield.bean.TransOrderInfo> paramList = new ArrayList<com.rkylin.wheatfield.bean.TransOrderInfo>();// 代收付1期
				com.rkylin.wheatfield.bean.TransOrderInfo transOrderInfoOne = new com.rkylin.wheatfield.bean.TransOrderInfo();
				transOrderInfoOne.setMerchantCode(detailBean.getMerchantCode());
				transOrderInfoOne.setOrderNo(detailBean.getOrderNo());
				transOrderInfoOne.setAmount(detailBean.getAmount());
				transOrderInfoOne.setErrorMsg("银行退票");
				// transOrderInfoOne.setErrorCode(detailBean.getErrorCode());
				transOrderInfoOne.setStatus(9);// 9退票
				transOrderInfoOne.setRemark(String.valueOf(detailBean
						.getTransDetailId()));
				logger.info("账户一期退票参数：orderNo=" + detailBean.getOrderNo()
						+ ",amount=" + detailBean.getAmount()
						+ ",merchantCode=" + detailBean.getMerchantCode());
				paramList.add(transOrderInfoOne);

				try {
					if (paramList != null && paramList.size() > 0) {
						logger.info("开始调用账户一期退票接口："
								+ System.currentTimeMillis());
						rtnResponse = transOrderDubboService
								.notifyTransOrderResults(paramList);// 代付结果推送给账户
						logger.info("结束调用账户一期退票接口："
								+ System.currentTimeMillis());
					}
					if (rtnResponse != null && !"".equals(rtnResponse)) {
						if ("1".equals(rtnResponse.getCode())) {
							logger.info(">>>>>>调用账户系统的退票接口成功,orderNo="
									+ detailBean.getOrderNo() + ",账户系统返回code="
									+ rtnResponse.getCode() + ",msg="
									+ rtnResponse.getMsg());
						} else {
							deliverStatusId = 1;
							logger.info(">>>>>>调用账户系统的退票接口失败,orderNo="
									+ detailBean.getOrderNo() + ",账户系统返回code="
									+ rtnResponse.getCode() + ",msg="
									+ rtnResponse.getMsg());
						}
					} else {
						deliverStatusId = 1;
						logger.info(">>>>>>调用账户系统的退票接口失败,orderNo="
								+ detailBean.getOrderNo()
								+ ",账户系统返回的commonResponse=" + rtnResponse);
					}
				} catch (Exception e) {
					isFail = true;
					deliverStatusId = 99;
					if (rtnResponse != null) {
						logger.error(">>>>调用账户系统的退票接口发起退票发生异常！！！！！！！！orderNo="
								+ detailBean.getOrderNo() + ",merchantCode="
								+ detailBean.getMerchantCode() + ",异常信息:code="
								+ rtnResponse.getCode() + ",msg="
								+ rtnResponse.getMsg() + "," + e.getMessage());
					}
					logger.error(">>>>调用账户系统的退票接口发起退票发生异常！！！！！！！！orderNo="
							+ detailBean.getOrderNo() + ",merchantCode="
							+ detailBean.getMerchantCode() + ",异常信息:"
							+ e.getMessage());
				}
				try {
					detailBean.setDeliverStatusId(deliverStatusId);
					settleTransDetailManager
							.updateSettleTransDetail(detailBean);
				} catch (Exception e) {
					logger.info("更新交易表的退票接口(1期)状态失败(调用账户的退票接口)");
				}
			}
		}

		if (isFail) {// 失败或异常
			SendSMS.sendSMS(mobile, content1);// 给相关人员发送短信
		}

	}

	/**
	 * 将成功的代收付移到历史表
	 */
	public void removeSuccessToHis() {
		try {
			// 通过结算单号将指定的数据移入历史表
			Map<String, Object> map = new HashMap<String, Object>();
			Calendar c = Calendar.getInstance();
			int hour = c.get(Calendar.HOUR_OF_DAY);
			int minute = c.get(Calendar.MINUTE);
			if ((hour == 18 && minute >= 30) || hour > 18) {// 18:30之后成功失败的都立即发给订单或者18:30之前的成功的立即发给订单
				map.put("iv_status_id", "13");
				settleTransInvoiceManager.removeToHisProc(map);
				logger.info("18:30之后,将失败单移入历史表完成");
				map.put("iv_status_id", "15");
				settleTransInvoiceManager.removeToHisProc(map);
				logger.info("18:30之后,将成功单移入历史表完成");
			} else {
				map.put("iv_status_id", "15");
				settleTransInvoiceManager.removeToHisProc(map);
				logger.info("18:30之前,将成功单移入历史表完成");
			}
		} catch (Exception e) {
			logger.error(">>>>>removeSuccessToHis将成功的代收付移到历史表异常！！！！！！"
					+ e.getMessage());
		}
	}

	public SettleTransDetail transDetailMapToList(Map<String, Object> detailMap) {
		SettleTransDetail settleTransDetail = new SettleTransDetail();

		settleTransDetail.setTransDetailId(Integer
				.parseInt(stringToNum(detailMap.get("TRANS_DETAIL_ID"))));
		settleTransDetail.setRequestNo(String.valueOf(detailMap
				.get("REQUEST_NO")));
		settleTransDetail.setRequestTime((java.util.Date) detailMap
				.get("REQUEST_TIME"));
		settleTransDetail.setTransFlowNo(String.valueOf(detailMap
				.get("TRANS_FLOW_NO")));
		settleTransDetail.setOrderNo(String.valueOf(detailMap.get("ORDER_NO")));
		settleTransDetail.setRelateOrderNo(String.valueOf(detailMap
				.get("RELATE_ORDER_NO")));
		settleTransDetail.setOrderPackageNo(String.valueOf(detailMap
				.get("ORDER_PACKAGE_NO")));
		settleTransDetail.setOrderDate((java.util.Date) detailMap
				.get("ORDER_DATE"));
		settleTransDetail.setOrderAmount(Long.parseLong(stringToNum(detailMap
				.get("ORDER_AMOUNT"))));
		settleTransDetail.setOrderCount(Integer.parseInt(stringToNum(detailMap
				.get("ORDER_COUNT"))));
		settleTransDetail.setOrderType(Integer.parseInt(stringToNum(detailMap
				.get("ORDER_TYPE"))));
		settleTransDetail.setTransType(Integer.parseInt(stringToNum(detailMap
				.get("TRANS_TYPE"))));
		settleTransDetail
				.setFuncCode(String.valueOf(detailMap.get("FUNC_CODE")));
		settleTransDetail.setDealProductCode(String.valueOf(detailMap
				.get("DEAL_PRODUCT_CODE")));
		settleTransDetail.setUserId(String.valueOf(detailMap.get("USER_ID")));
		settleTransDetail.setInterMerchantCode(String.valueOf(detailMap
				.get("INTER_MERCHANT_CODE")));
		settleTransDetail.setMerchantCode(String.valueOf(detailMap
				.get("MERCHANT_CODE")));
		settleTransDetail.setIntoInstCode(String.valueOf(detailMap
				.get("INTO_INST_CODE")));
		settleTransDetail.setAmount(Long.parseLong(stringToNum(detailMap
				.get("AMOUNT"))));
		settleTransDetail.setFeeAmount(Long.parseLong(stringToNum(detailMap
				.get("FEE_AMOUNT"))));
		settleTransDetail.setFeeAmount1(Long.parseLong(stringToNum(detailMap
				.get("FEE_AMOUNT1"))));
		settleTransDetail.setFeeAmount2(Long.parseLong(stringToNum(detailMap
				.get("FEE_AMOUNT2"))));
		settleTransDetail.setFeeAmount3(Long.parseLong(stringToNum(detailMap
				.get("FEE_AMOUNT3"))));
		settleTransDetail.setFeeAmount4(Long.parseLong(stringToNum(detailMap
				.get("FEE_AMOUNT4"))));
		settleTransDetail.setUserFee(Long.parseLong(stringToNum(detailMap
				.get("USER_FEE"))));
		settleTransDetail.setProfit(Long.parseLong(stringToNum(detailMap
				.get("PROFIT"))));
		settleTransDetail.setFcAmount(Long.parseLong(stringToNum(detailMap
				.get("FC_AMOUNT"))));
		settleTransDetail.setBusinessType(String.valueOf(detailMap
				.get("BUSINESS_TYPE")));
		settleTransDetail.setPayChannelId(String.valueOf(detailMap
				.get("PAY_CHANNEL_ID")));
		settleTransDetail
				.setBankCode(String.valueOf(detailMap.get("BANK_CODE")));
		settleTransDetail.setUserIpAddress(String.valueOf(detailMap
				.get("USER_IP_ADDRESS")));
		settleTransDetail.setErrorCode(String.valueOf(detailMap
				.get("ERROR_CODE")));
		settleTransDetail
				.setErrorMsg(String.valueOf(detailMap.get("ERROR_MSG")));
		settleTransDetail.setProductId(String.valueOf(detailMap
				.get("PRODUCT_ID")));
		settleTransDetail.setProductWid(String.valueOf(detailMap
				.get("PRODUCT_W_ID")));
		settleTransDetail.setCancelInd(Integer.parseInt(stringToNum(detailMap
				.get("CANCEL_IND"))));
		settleTransDetail.setDataFrom(Integer.parseInt(stringToNum(detailMap
				.get("DATA_FROM"))));
		settleTransDetail.setDeliverStatusId(Integer
				.parseInt(stringToNum(detailMap.get("DELIVER_STATUS_ID"))));
		settleTransDetail.setInvoiceNo(String.valueOf(detailMap
				.get("INVOICE_NO")));
		settleTransDetail.setObligate1(String.valueOf(detailMap
				.get("OBLIGATE1")));
		settleTransDetail.setObligate2(String.valueOf(detailMap
				.get("OBLIGATE2")));
		settleTransDetail.setObligate3(String.valueOf(detailMap
				.get("OBLIGATE3")));
		settleTransDetail.setStatusId(Integer.parseInt(stringToNum(detailMap
				.get("STATUS_ID"))));
		settleTransDetail.setReadStatusId(Integer
				.parseInt(stringToNum(detailMap.get("READ_STATUS_ID"))));
		settleTransDetail.setDflag(Integer.parseInt(stringToNum(detailMap
				.get("DFLAG"))));
		settleTransDetail.setRemark(String.valueOf(detailMap.get("REMARK")));
		settleTransDetail.setAccountDate((java.util.Date) detailMap
				.get("ACCOUNT_DATE"));
		settleTransDetail.setCreatedTime((java.util.Date) detailMap
				.get("CREATED_TIME"));
		settleTransDetail.setUpdatedTime((java.util.Date) detailMap
				.get("UPDATED_TIME"));
		settleTransDetail.setRequestId(Integer.parseInt(stringToNum(detailMap
				.get("REQUEST_ID"))));
		settleTransDetail
				.setRoleCode(String.valueOf(detailMap.get("ROLE_CODE")));
		settleTransDetail.setInterRoleCode(String.valueOf(detailMap
				.get("INTER_ROLE_CODE")));
		settleTransDetail.setReferUserId(String.valueOf(detailMap
				.get("REFER_USER_ID")));
		settleTransDetail.setReferRoleCode(String.valueOf(detailMap
				.get("REFER_ROLE_CODE")));
		settleTransDetail.setReferProductId(String.valueOf(detailMap
				.get("REFER_PRODUCT_ID")));
		settleTransDetail.setOtherAmount(Long.parseLong(stringToNum(detailMap
				.get("OTHER_AMOUNT"))));
		settleTransDetail.setUserFeeWay(Integer.parseInt(stringToNum(detailMap
				.get("USER_FEE_WAY"))));
		settleTransDetail.setInterest(Long.parseLong(stringToNum(detailMap
				.get("INTEREST"))));
		settleTransDetail.setPayWay(String.valueOf(detailMap.get("PAY_WAY")));
		settleTransDetail.setProcessStatus(Integer
				.parseInt(stringToNum(detailMap.get("PROCESS_STATUS"))));
		settleTransDetail.setOriginalOrderPackageNo(String.valueOf(detailMap
				.get("ORIGINAL_ORDER_PACKAGE_NO")));
		settleTransDetail.setOriginalOrderId(String.valueOf(detailMap
				.get("ORIGINAL_ORDER_ID")));
		settleTransDetail
				.setCurrency(String.valueOf(detailMap.get("CURRENCY")));
		settleTransDetail.setReverseFlag(Integer.parseInt(stringToNum(detailMap
				.get("REVERSE_FLAG"))));
		settleTransDetail.setCancelTag(Integer.parseInt(stringToNum(detailMap
				.get("CANCEL_TAG"))));
		settleTransDetail.setAcceptInstCode(String.valueOf(detailMap
				.get("ACCEPT_INST_CODE")));
		settleTransDetail.setAcceptInstId(String.valueOf(detailMap
				.get("ACCEPT_INST_ID")));
		settleTransDetail.setTargetMerchantCode(String.valueOf(detailMap
				.get("TARGET_MERCHANT_CODE")));
		settleTransDetail.setTargetTerminalCode(String.valueOf(detailMap
				.get("TARGET_TERMINAL_CODE")));
		settleTransDetail.setChannelReturnCode(String.valueOf(detailMap
				.get("CHANNEL_RETURN_CODE")));
		settleTransDetail.setReturnMeg(String.valueOf(detailMap
				.get("RETURN_MEG")));
		settleTransDetail.setReceiptSerialNo(String.valueOf(detailMap
				.get("RECEIPT_SERIAL_NO")));
		settleTransDetail.setMcc(String.valueOf(detailMap.get("MCC")));
		settleTransDetail
				.setAuthCode(String.valueOf(detailMap.get("AUTH_CODE")));
		settleTransDetail.setChannelInfo(String.valueOf(detailMap
				.get("CHANNEL_INFO")));
		settleTransDetail.setTradeEsbNo(String.valueOf(detailMap
				.get("TRADE_ESB_NO")));
		settleTransDetail
				.setReserve1(String.valueOf(detailMap.get("RESERVE_1")));
		settleTransDetail
				.setReserve2(String.valueOf(detailMap.get("RESERVE_2")));
		settleTransDetail
				.setReserve3(String.valueOf(detailMap.get("RESERVE_3")));
		settleTransDetail.setReadCreatedTime((java.util.Date) detailMap
				.get("READ_CREATED_TIME"));

		return settleTransDetail;
	}

	/**
	 * 获取账户一期的绑卡信息
	 */
	public AccountInfo get1AccountCardInfo(String merchantCode, String userId,
			String yfqFlag, String funcCode) {
		AccountInfoQuery accountInfoQuery = new AccountInfoQuery();
		accountInfoQuery.setRootInstCd(merchantCode);
		accountInfoQuery.setAccountName(userId);
		List<AccountInfo> accountInfoList = null;
		AccountInfo accountInfo = null;
		try {
			logger.info("开始调用账户一期的绑卡信息查询接口,RootInstCd=" + merchantCode + ",AccountName=" + userId);
			accountInfoList = accountManagementService.selectAccountListForJsp(accountInfoQuery);
			logger.info("结束调用账户一期的绑卡信息查询接口,RootInstCd=" + merchantCode + ",AccountName=" + userId);
			if (accountInfoList == null) {
				logger.info("调用账户一期绑卡信息查询接口，账户系统返回null");
			} else {
				logger.info("调用账户一期的绑卡信息查询接口，账户返回的accountInfoList的size=" + accountInfoList.size());
			}
		} catch (Exception e) {
			logger.error("开始调用账户一期的绑卡信息查询接口发生异常,e" + e);
		}

		if (SettleConstants.BATCH_TX_FUN_CODE.equals(funcCode)) {// 提现
			accountInfo = this.get1CardBeanByPurpose(accountInfoList,
					new String[] { "3", "4", "1" }, null);
		} else {
			accountInfo = this.get1CardBeanByPurpose(accountInfoList,
					new String[] { "1", "4", "3" }, yfqFlag);
		}
		return accountInfo;
	}

	/**
	 * 从代收付系统读取结果
	 */
	@Override
	public String getResultFromDSF(String rootInstCd, String businessCode,
			String requestNo, String orderNos, String accountDate) {
		try {
			if ("M0000029".equals(rootInstCd) || "cmbFile".equals(businessCode)) {
				File resFile = new File(requestNo);
				dsfLogic.readResFile(resFile);
				return "success";
			}
			if ("".equals(rootInstCd))
				rootInstCd = null;
			if ("".equals(requestNo))
				requestNo = null;
			if ("".equals(orderNos))
				orderNos = null;
			if ("".equals(accountDate))
				accountDate = null;
			if ("".equals(businessCode))
				businessCode = null;
			String[] orderNoArr = null;
//			Set<String> batchNoSet = new HashSet<String>();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

			SettleTransInvoiceQuery query = new SettleTransInvoiceQuery();
			query.setRootInstCd(rootInstCd);
			if (StringUtils.isNotBlank(accountDate)) {
				query.setAccountDate(format.parse(accountDate));
			}
			query.setBussinessCode(businessCode);
			query.setOrderNo(orderNos);

			query.setRequestNo(requestNo);
			List<SettleTransInvoice> invoiceList = settleTransInvoiceManager
					.queryList(query);

			List<SettleTransInvoiceHis> invoiceHisList = null;
			if (invoiceList == null || invoiceList.size() < 1) {
				// 当前表没查到，再查历史表
				SettleTransInvoiceHisQuery hisQuery = new SettleTransInvoiceHisQuery();
				hisQuery.setRootInstCd(rootInstCd);
				if (StringUtils.isNotBlank(accountDate)) {
					hisQuery.setAccountDate(format.parse(accountDate));
				}
				hisQuery.setBussinessCode(businessCode);
				hisQuery.setOrderNo(orderNos);

				hisQuery.setRequestNo(requestNo);
				invoiceHisList = settleTransInvoiceHisManager
						.queryList(hisQuery);
			}

			Map<String, TransQueryDto> batchNoMap = new HashMap<String, TransQueryDto>();
			TransQueryDto transQueryDto = new TransQueryDto();
			if (invoiceList != null && invoiceList.size() > 1) {
				for (SettleTransInvoice invoice : invoiceList) {
					if (!batchNoMap.containsKey(invoice.getRequestNo())) {
						transQueryDto.setBusiCode(invoice.getBussinessCode());
						if ("09100".equals(transQueryDto.getBusiCode())) {// 代收
							transQueryDto.setTransCode("11022");
						} else {// 代付
							transQueryDto.setTransCode("11012");
						}
						transQueryDto.setOrgNo(invoice.getRootInstCd());
						transQueryDto.setSignType(1);
						transQueryDto.setSysNo("sys1001");
						// transQueryDto.setOrgCode(invoice.getMerchantCode());//orgCode是商户编号，怎么传渠道号？？？
						transQueryDto.setTransNo(invoice.getOrderNo());
						transQueryDto.setBatchNo(invoice.getRequestNo());
						batchNoMap.put(invoice.getRequestNo(), transQueryDto);
					}
				}
			} else if (invoiceList != null && invoiceList.size() == 1) {
				transQueryDto
						.setBusiCode(invoiceList.get(0).getBussinessCode());
				if ("09100".equals(transQueryDto.getBusiCode())) { // 代收
					transQueryDto.setTransCode("11021");
				} else { // 代付
					transQueryDto.setTransCode("11011");
				}
				transQueryDto.setOrgNo(invoiceList.get(0).getRootInstCd());
				transQueryDto.setSignType(1);
				transQueryDto.setSysNo("sys1001");
				// transQueryDto.setOrgCode(invoiceList.get(0).getMerchantCode());
				transQueryDto.setTransNo(invoiceList.get(0).getOrderNo());
				orderNoArr = new String[] { invoiceList.get(0).getOrderNo() };
			} else if (invoiceHisList != null && invoiceHisList.size() > 1) {
				for (SettleTransInvoiceHis invoiceHis : invoiceHisList) {
					if (!batchNoMap.containsKey(invoiceHis.getRequestNo())) {
						transQueryDto
								.setBusiCode(invoiceHis.getBussinessCode());
						if ("09100".equals(transQueryDto.getBusiCode())) { // 代收
							transQueryDto.setTransCode("11022");
						} else { // 代付
							transQueryDto.setTransCode("11012");
						}
						transQueryDto.setOrgNo(invoiceHis.getRootInstCd());
						transQueryDto.setSignType(1);
						transQueryDto.setSysNo("sys1001");
						// transQueryDto.setOrgCode(invoiceHis.getMerchantCode());
						transQueryDto.setTransNo(invoiceHis.getOrderNo());
						transQueryDto.setBatchNo(invoiceHis.getRequestNo());
						batchNoMap
								.put(invoiceHis.getRequestNo(), transQueryDto);
					}
				}
			} else if (invoiceHisList != null && invoiceHisList.size() == 1) {
				transQueryDto.setBusiCode(invoiceHisList.get(0)
						.getBussinessCode());
				if ("09100".equals(transQueryDto.getBusiCode())) { // 代收
					transQueryDto.setTransCode("11021");
				} else { // 代付
					transQueryDto.setTransCode("11011");
				}
				transQueryDto.setOrgNo(invoiceHisList.get(0).getRootInstCd());
				transQueryDto.setSignType(1);
				transQueryDto.setSysNo("sys1001");
				// transQueryDto.setOrgCode(invoiceHisList.get(0).getMerchantCode());
				transQueryDto.setTransNo(invoiceHisList.get(0).getOrderNo());
				orderNoArr = new String[] { invoiceHisList.get(0).getOrderNo() };
			} else {
				logger.info("根据查询条件rootInstCd=" + rootInstCd + ",businessCode="
						+ businessCode + ",requestNo=" + requestNo
						+ ",orderNos=" + orderNos + ",accountDate="
						+ accountDate + "在结算表共查询到0条数据");
				return "success";
			}

			transQueryDto.setSignMsg(transQueryDto.sign("000000"));
			OrderDetails orderDetails = null;
			OrderDetail orderDetail = null;
			List<OrderDetail> orderDetailList = new ArrayList<OrderDetail>();
			List<TransDetailRespDto> transDetailRespDtoList = new ArrayList<TransDetailRespDto>();
			if (orderNoArr != null && orderNoArr.length == 1) {
				orderDetails = new OrderDetails();
				logger.info("根据订单号：" + orderNoArr[0] + ",businessCode="
						+ businessCode + "调用代收付系统的结果查询接口，开始");
				SingleTransRespDto singleTransRespDto = null;
				// 最后一个参数 1是走批量查询接口,只有在代收付系统是终态了才能查回结果
				singleTransRespDto = crpsAgentPayService
						.singleTransQuery(transQueryDto);
				logger.info("singleTransRespDto===" + singleTransRespDto);
				logger.info("根据订单号：" + orderNoArr[0] + ",businessCode="
						+ businessCode + "调用代收付系统的结果查询接口，结束");
				orderDetail = new OrderDetail();
				orderDetail.setOrderNo(singleTransRespDto.getTransNo());
				orderDetail.setStatusId(singleTransRespDto.getStatusId());
				orderDetail.setErrMsg(singleTransRespDto.getReturnMsg());
				orderDetailList.add(orderDetail);
				orderDetails.setOrderDetails(orderDetailList);
				this.dubboNotify(orderDetails);
			} else {
				orderDetails = new OrderDetails();
				Iterator<Map.Entry<String, TransQueryDto>> batchIter = batchNoMap
						.entrySet().iterator();
				transQueryDto = new TransQueryDto();
				while (batchIter.hasNext()) {
					Map.Entry<String, TransQueryDto> entry = batchIter.next();
					transQueryDto = (TransQueryDto) entry.getValue();
					BatchTransRespDto batchTransRespDto = null;
					try {
						logger.info("根据批次号：" + transQueryDto.getBatchNo()
								+ ",businessCode=" + businessCode
								+ "调用代收付系统的结果查询接口，开始");
						batchTransRespDto = crpsAgentPayService
								.batchTransQuery(transQueryDto);
						logger.info("根据批次号：" + transQueryDto.getBatchNo()
								+ ",businessCode=" + businessCode
								+ "调用代收付系统的结果查询接口，结束");
					} catch (Exception e) {
						logger.error("调用代收付批量结果查询接口发生异常，error_msg="
								+ e.getMessage());
						continue;
					}

					if (batchTransRespDto.getTotalAcount() == null
							|| batchTransRespDto.getTotalAcount() == 0) {
						logger.info("根据批次号：" + transQueryDto.getBatchNo()
								+ ",businessCode=" + businessCode
								+ "调用代收付系统的结果查询接口，查到0条数据");
						continue;
					}

					transDetailRespDtoList = batchTransRespDto
							.getTransDetailRespDtoList();
					for (TransDetailRespDto subBean : transDetailRespDtoList) {
						orderDetail = new OrderDetail();
						orderDetail.setOrderNo(subBean.getTransNo());
						orderDetail.setStatusId(subBean.getStatusId());
						orderDetail.setErrMsg(subBean.getReturnMsg());
						orderDetail.setOrgCode(subBean.getOrgNo());
						orderDetailList.add(orderDetail);
					}
					orderDetails.setOrderDetails(orderDetailList);
					this.dubboNotify(orderDetails);
				}
			}
		} catch (Exception e) {
			logger.error(">>>>>>getResultFromDSF方法发生异常！！！！！！！" + e);
		}
		return "success";
	}

	/**
	 * 返回代付结果给账户系统
	 * 
	 * @param detailBean
	 *            交易表的bean
	 */
	public void returnAccount(Integer[] ids, Integer[] dFlags) {
		try {
			Map<String, Object> detailwhereMap = new HashMap<String, Object>();
			if (ids != null && ids.length > 0) {
				detailwhereMap.put("ids", ids);
			}
			if (dFlags != null && dFlags.length > 0) {
				detailwhereMap.put("dFlags", dFlags);
			}
			logger.info(">>>>入参ids=" + ids + "dFlags=" + dFlags);
			List<Map<String, Object>> dsfDetailList = dsfLogic
					.getDsfTransDetail(detailwhereMap);
			List<com.rkylin.wheatfield.bean.TransOrderInfo> param1List = new ArrayList<com.rkylin.wheatfield.bean.TransOrderInfo>();// 代收付1期
			List<TransOrderInfoResult> param2DfList = new ArrayList<TransOrderInfoResult>();//代付2期代付、提现、一分钱代付
			List<TransOrderInfoResult> param2DsList = new ArrayList<TransOrderInfoResult>();//代收2期代收
			this.splitAccountData(dsfDetailList,param1List,param2DfList,param2DsList);//拆分交易表的数据，决定具体调用账户的哪个接口进行代收付结果的推送
			this.dealDsfResultTo1Account(param1List); //一期 
			this.dealDsResultTo2Account(param2DsList);//二期代收 
			this.dealDfResultTo2Account(param2DfList);//二期代付、提现、一分钱代付

			Iterator<Map<String, Object>> dsfTransDetailIter = dsfDetailList
					.iterator();
			Map<String, List<SettleTransDetail>> detailList = new HashMap<String, List<SettleTransDetail>>();
			List<SettleTransDetail> detailSubList = new ArrayList<SettleTransDetail>();
			Map<String, Object> theDetailMap = null;
			SettleTransDetail detailSub = new SettleTransDetail();
			while (dsfTransDetailIter.hasNext()) {
				theDetailMap = dsfTransDetailIter.next();// 当前交易信息
				detailSub.setOrderNo(String.valueOf(theDetailMap
						.get("ORDER_NO")));
				detailSub.setStatusId(Integer.parseInt(String
						.valueOf(theDetailMap.get("READ_STATUS_ID"))));
				detailSub.setErrorMsg(String.valueOf(theDetailMap
						.get("ERROR_MSG")));

				detailSubList.add(detailSub);
			}

			detailList.put("1", detailSubList);
			dsfMessageListener.returnMq(detailList);
		} catch (Exception e) {
			logger.error("错误信息：" + e);
		}
	}

	/**
	 * 拆分交易表的数据，决定具体调用账户的哪个接口进行代收付结果的推送
	 * @param detailwhereMap
	 */
	public void splitAccountData(List<Map<String, Object>> dsfDetailList,List<com.rkylin.wheatfield.bean.TransOrderInfo> param1List,List<TransOrderInfoResult> param2DfList,List<TransOrderInfoResult> param2DsList){
		try {
			if(dsfDetailList ==null || dsfDetailList.size()<=0){
				logger.info(">>>>dsfDetailList的大小为0,直接返回");
				return;
			}
			String dataFrom = null;
		    String merchantCode = null;
		    String orderNo = null;
		    String amount = null;
		    String dflag = null;
		    String errorMsg = null;
		    String errorCode = null;
		    String transDetailId = null;
		    String roleCode = null;
		    String userIdAddress = null;
			Map<String, Object> theDetailMap = null;
			TransOrderInfo transOrderInfo = null;
			TransOrderInfoResult transOrderInfoResult = null;
			com.rkylin.wheatfield.bean.TransOrderInfo transOrderInfoOne = null;
		    //循环对数据做拆分，便于后续决定调用账户的哪个接口
			Iterator<Map<String, Object>> dsfTransDetailIter = dsfDetailList.iterator();		
			while(dsfTransDetailIter.hasNext()){
				theDetailMap = dsfTransDetailIter.next();
			    dataFrom = String.valueOf(theDetailMap.get("DATA_FROM"));
			    merchantCode = (String)theDetailMap.get("MERCHANT_CODE");
			    orderNo = (String)theDetailMap.get("ORDER_NO");
			    amount = String.valueOf(theDetailMap.get("AMOUNT"));
			    dflag = String.valueOf(theDetailMap.get("DFLAG"));
			    errorMsg = (String)theDetailMap.get("ERROR_MSG");
			    errorCode = (String)theDetailMap.get("ERROR_CODE");
			    transDetailId = String.valueOf(theDetailMap.get("TRANS_DETAIL_ID"));
			    roleCode = (String)theDetailMap.get("ROLE_CODE");
			    userIdAddress = (String)theDetailMap.get("USER_IP_ADDRESS");
			
				if("0".equals(dataFrom)){//一期
					transOrderInfoOne = new com.rkylin.wheatfield.bean.TransOrderInfo();
					transOrderInfoOne.setMerchantCode(merchantCode);
					transOrderInfoOne.setOrderNo(orderNo);
					if(StringUtils.isNotBlank(amount) && !amount.equals("null")){
						transOrderInfoOne.setAmount(Long.parseLong(amount));
					}
					if("4".equals(dflag)||"40".equals(dflag)){
						transOrderInfoOne.setErrorMsg(errorMsg);
						transOrderInfoOne.setErrorCode(errorCode);
						transOrderInfoOne.setStatus(5);//5失败
					}else if("6".equals(dflag) || "60".equals(dflag)){
						transOrderInfoOne.setStatus(4);//4成功
					}else{
						logger.info(theDetailMap.get("TRANS_DETAIL_ID") + "一期代付结果状态异常！");
						continue;
					}
					transOrderInfoOne.setRemark(transDetailId);
					logger.info("账户一期接口的请求内部参数：订单号="+transOrderInfoOne.getOrderNo()+",金额="+transOrderInfoOne.getAmount()+";");
					param1List.add(transOrderInfoOne);
				}else if("4".equals(dataFrom)){//二期
					transOrderInfoResult = new TransOrderInfoResult();
					transOrderInfoResult.setOrderDate(settlementUtil.getCurrentTime());
					transOrderInfoResult.setOrderNo(settlementUtil.createBatchNo("ST"));
					transOrderInfoResult.setOriginalOrderId(orderNo);
					transOrderInfoResult.setRootInstCd(merchantCode);
					if(StringUtils.isNotBlank(roleCode)){
						transOrderInfoResult.setRoleCode(roleCode);
					}
					if("4".equals(dflag) || "40".equals(dflag)){
						transOrderInfoResult.setErrorMsg(errorMsg);
						transOrderInfoResult.setErrorCode(errorCode);
						transOrderInfoResult.setStatusId(5);//5失败
					}else if("6".equals(dflag) || "60".equals(dflag)){
						transOrderInfoResult.setStatusId(4);//4成功
					}else{
						logger.info(transDetailId + "状态异常！");
						continue;
					}
					transOrderInfo.setUserIpAddress(userIdAddress);
					transOrderInfo.setRemark(transDetailId);
				
					if(SettleConstants.BATCH_DS_FUN_CODE.equals(String.valueOf(theDetailMap.get("FUNC_CODE")))){
						transOrderInfoResult.setDealProductCode("MPROD_00_RS_002");
						param2DsList.add(transOrderInfoResult);
					}
					
					if (SettleConstants.BATCH_DF_FUN_CODE.equals(String.valueOf(theDetailMap.get("FUNC_CODE"))) 
							|| SettleConstants.BATCH_TX_FUN_CODE.equals(String.valueOf(theDetailMap.get("FUNC_CODE")))
							|| SettleConstants.BATCH_DF_YFQ_FUN_CODE.equals(String.valueOf(theDetailMap.get("FUNC_CODE")))) {
						transOrderInfoResult.setDealProductCode("MPROD_00_RS_001");
						param2DfList.add(transOrderInfoResult);
				    }
			    }
			}
		} catch (Exception e) {
			logger.error("拆分交易表的数据，决定具体调用账户的哪个接口进行代收付结果的推送发生异常,error="+e);
		}
	}

	/**
	 * 处理账户一期的代收付结果，推送给账户
	 * @param paramList
	 */
	public void dealDsfResultTo1Account(List<com.rkylin.wheatfield.bean.TransOrderInfo> param1List){
		if(param1List !=null && param1List.size()>0){
			try{
				com.rkylin.wheatfield.model.CommonResponse  rtnResponse = null;
				List<com.rkylin.wheatfield.bean.TransOrderInfo> paramTmpList = new ArrayList<com.rkylin.wheatfield.bean.TransOrderInfo>();//代付2期
				int countList = 0;
				countList = param1List.size();
				Integer size = 150;//每批数量
				Integer totalIndex = countList / size + 1;//总批数
				Integer batchIndex = 1;//当前批数
				Integer fromIndex = null;//开始索引 包含
				Integer toIndex = null;//结束索引 不包含
				while(totalIndex >= batchIndex) {
					try{
						fromIndex = (batchIndex - 1) * size;
						toIndex = totalIndex == batchIndex ? countList : batchIndex * size;
						paramTmpList = param1List.subList(fromIndex, toIndex);
						logger.info("一期数据调用账户系统代收付结果推送接口，共推送"+paramTmpList.size()+"条，开始");
						rtnResponse = transOrderDubboService.notifyTransOrderResults(paramTmpList);//代付结果推送给账户
						logger.info("一期数据调用账户系统代收付结果推送接口，共推送"+paramTmpList.size()+"条，结束");
						if(rtnResponse!=null && !"".equals(rtnResponse)){
							logger.info("账户一期代收付结果推送给账户，账户系统返回的code="+rtnResponse.getCode());
							if("1".equals(rtnResponse.getCode())){
								logger.info("一期数据调用账户系统代收付结果推送接口，共推送"+paramTmpList.size()+"条，账户系统返回成功！");
								this.updateSend1AccountSuccess(paramTmpList);
							}else{
								logger.info("一期数据调用账户系统代收付结果推送接口，共推送"+paramTmpList.size()+"条，账户系统返回失败！");
								this.updateSend1AccountFail(paramTmpList);
							}
						}else{
							logger.info("一期数据调用账户系统代收付结果推送接口，共推送"+paramTmpList.size()+"条，账户系统返回的对象为空！！！！！");
						}
					}catch(Exception e){
						logger.error("代付结果推送账户发生异常,error_msg:"+e.getMessage());
						this.updateSend1AccountFail(paramTmpList);
					}
					batchIndex ++;
				}
			}catch(Exception e){
				logger.error("组织账户一期代收付结果即将发往账户的过程中发生异常"+e);
			}
		}
	}
	
	/**
	 * 将账户二期的代付、一分钱代付、提现的代收付结果推送给账户
	 */
	public void dealDfResultTo2Account(List<TransOrderInfoResult> paramDfList){
		MultBaseResultReturnResponse rtnPResponse = null;
		List<MultTransOrderInfoReturn> rtnPList = null;
		List<TransOrderInfoResult> paramTmpList = new ArrayList<TransOrderInfoResult>();//代付2期
		int countList = 0;
		try{
			if(paramDfList !=null && paramDfList.size()>0){
				countList = paramDfList.size();
				Integer size = 150;//每批数量
				Integer totalIndex = countList / size + 1;//总批数
				Integer batchIndex = 1;//当前批数
				Integer fromIndex = null;//开始索引 包含
				Integer toIndex = null;//结束索引 不包含
				while(totalIndex >= batchIndex) {
					try{
						String orderPackageNo = settlementUtil.createBatchNo("SP");
						fromIndex = (batchIndex - 1) * size;
						toIndex = totalIndex == batchIndex ? countList : batchIndex * size;
						paramTmpList = paramDfList.subList(fromIndex, toIndex);
						Iterator<TransOrderInfoResult> ite = paramTmpList.iterator();
						while(ite.hasNext()){
							TransOrderInfoResult tmpTransOrderInfo = ite.next();
							tmpTransOrderInfo.setOrderCount(paramTmpList.size());
							tmpTransOrderInfo.setOrderPackageNo(orderPackageNo);
							logger.info("二期，订单号="+tmpTransOrderInfo.getOrderNo()+",原始订单号："+tmpTransOrderInfo.getOriginalOrderId()+"订单包号："+tmpTransOrderInfo.getOrderPackageNo());
						}
						logger.info("二期代付结果推送给账户系统，开始》》》》》》,paramPList.size="+paramTmpList.size());
						rtnPResponse = multWithholdAndWithdrowResultReturnApi.execute(paramTmpList);//代付结果推送给账户
						
						logger.info("二期代付结果推送给账户系统，结束》》》》》》,paramPList.size="+paramTmpList.size());
						if (rtnPResponse!=null && !"".equals(rtnPResponse)) {
							logger.info("二期代付结果推送给账户系统,账户系统返回的code="+rtnPResponse.getCode());
							if (!"1".equals(rtnPResponse.getCode())) {
								rtnPList = rtnPResponse.getErrList();
								if(rtnPList !=null && rtnPList.size()>0){
									this.updateDflagByAccountFail(paramTmpList, rtnPList);//部分失败
									logger.info("二期代付结果推送给账户系统，账户系统有部分处理失败，共"+rtnPList.size());
								}else{
									this.updateDflagByAccountFail(paramTmpList, null);//全部失败
									logger.info("二期代付结果推送给账户系统，账户系统有部分处理失败，但是返回的失败信息的列表为空或0条");
								}
							}else{
								logger.info("二期代付结果推送给账户系统成功");
								this.updateDflagByAccountSuccess(paramTmpList);//全部成功
							}
						}else{
							logger.info("二期代付结果推送给账户系统，账户系统返回的对象为空");
							this.updateDflagByAccountFail(paramTmpList, null);//全部失败
						}
					}catch(Exception e){
						logger.error("代付结果推送账户发生异常,error_msg:"+e.getMessage());
						this.updateDflagByAccountFail(paramTmpList, null);
					}
					batchIndex ++;
				}
			}
		}catch(Exception e){
			logger.error("组织账户二期代收付结果即将发往账户的过程中发生异常"+e);
		}
	}
	
	/**
	 * 将账户二期的代收的代收付结果推送给账户
	 */
	public void dealDsResultTo2Account(List<TransOrderInfoResult> param2DsList){
		try{
			int countList = 0;
			MultBaseResultReturnResponse rtnRResponse = null;
			List<MultTransOrderInfoReturn> rtnRList = null;
			List<TransOrderInfoResult> paramTmpList = new ArrayList<TransOrderInfoResult>();//代付2期
			if(param2DsList !=null && param2DsList.size()>0){
				countList = param2DsList.size();
				Integer size = 150;//每批数量
				Integer totalIndex = countList / size + 1;//总批数
				Integer batchIndex = 1;//当前批数
				Integer fromIndex = null;//开始索引 包含
				Integer toIndex = null;//结束索引 不包含
				while(totalIndex >= batchIndex){
					try{
						String orderPackageNo = settlementUtil.createBatchNo("SP");
						fromIndex = (batchIndex - 1) * size;
						toIndex = totalIndex == batchIndex ? countList : batchIndex * size;
						paramTmpList = param2DsList.subList(fromIndex, toIndex);
						Iterator<TransOrderInfoResult> ite = paramTmpList.iterator();
						while(ite.hasNext()){
							TransOrderInfoResult tmpTransOrderInfo = ite.next();
							tmpTransOrderInfo.setOrderCount(paramTmpList.size());
							tmpTransOrderInfo.setOrderPackageNo(orderPackageNo);
							logger.info("二期，订单号="+tmpTransOrderInfo.getOrderNo()+",原始订单号："+tmpTransOrderInfo.getOriginalOrderId()+"订单包号："+tmpTransOrderInfo.getOrderPackageNo());
						}
						logger.info("二期代收结果推送给账户系统，开始》》》》》》,paramRList.size="+paramTmpList.size());
						rtnRResponse = multCollectionResultReturnApi.execute(paramTmpList);//代收结果推送给账户
						logger.info("二期代收结果推送给账户系统，结束》》》》》》,paramRList.size="+paramTmpList.size());
						if (rtnRResponse!=null && !"".equals(rtnRResponse)) {
							logger.info("二期代付结果推送给账户系统,账户系统返回的code="+rtnRResponse.getCode());
							if (!"1".equals(rtnRResponse.getCode())) {
								rtnRList = rtnRResponse.getErrList();
								if(rtnRList !=null && rtnRList.size()>0){
									this.updateDflagByAccountFail(paramTmpList, rtnRList);//部分失败
									logger.info("二期代收结果推送给账户系统，账户系统有部分处理失败，共"+rtnRList.size());
								}else{
									this.updateDflagByAccountFail(paramTmpList, null);//全部失败
									logger.info("二期代收结果推送给账户系统，账户系统有部分处理失败，但是返回的失败信息的列表为空或0条");
								}
							}else{
								this.updateDflagByAccountSuccess(paramTmpList);//全部成功
								logger.info("二期代收结果推送给账户系统成功");
							}
						}else{
							this.updateDflagByAccountFail(paramTmpList, null);//全部失败
							logger.info("二期代收结果推送给账户系统，账户系统返回的对象为空");
						}
					}catch(Exception e){
						logger.error("代收结果推送账户发生异常,error_msg:"+e.getMessage());
						this.updateDflagByAccountFail(paramTmpList, null);
					}
					batchIndex ++;
				}
			}
		}catch(Exception e){
			logger.error("组织账户二期代收结果即将发往账户的过程中发生异常"+e);
		}
	}
	
	/**
	 * 更新账户二期代收付结果推送给账户成功的数据
	 * @param paramList
	 */
	public void updateDflagByAccountSuccess(List<TransOrderInfoResult> paramList){
		try{
			SettleTransDetail detail = new SettleTransDetail();
			for(TransOrderInfoResult transBean :paramList){
				detail.setTransDetailId(Integer.parseInt(transBean.getRemark()));
				if(transBean.getStatusId() == 5){
					detail.setDflag(41);
				}else if(transBean.getStatusId() == 4){
					detail.setDflag(61);
				}else{
					continue;
				}
				settleTransDetailManager.updateSettleTransDetail(detail);
			}
			detail = null;
		}catch(Exception e){
			logger.error("更新账户二期代收付结果推送给账户成功的数据发生异常，error="+e);
		}
	}

	/**
	 * 更新账户二期代收付结果推送给账户失败的数据
	 * @param paramList
	 */
	public void updateDflagByAccountFail(List<TransOrderInfoResult> paramList,List<MultTransOrderInfoReturn> rtnList){
		try{
			SettleTransDetail detail = new SettleTransDetail();
			for(TransOrderInfoResult transBean :paramList){
				detail.setTransDetailId(Integer.parseInt(transBean.getRemark()));
				if (rtnList != null && rtnList.size()!=0){//部分失败
					for(MultTransOrderInfoReturn bean : rtnList){
						logger.info("orderNo="+bean.getOriginalOrderId()+",二期账户系统返回失败");
						if (transBean.getOriginalOrderId().compareTo(bean.getOriginalOrderId()) == 0) {
							if(detail.getDflag() == 61 || detail.getDflag() == 6){
								detail.setDflag(60);
							}else if(detail.getDflag() == 41 || detail.getDflag() == 4){
								detail.setDflag(40);
							}
							settleTransDetailManager.updateSettleTransDetail(detail);
							break;
						}
					}
				}else{//全部失败
					if(transBean.getStatusId() == 5){
						detail.setDflag(40);
					}else if(transBean.getStatusId() == 4){
						detail.setDflag(60);
					}else{
						continue;
					}
					settleTransDetailManager.updateSettleTransDetail(detail);
				}
			}
		}catch(Exception e){
			logger.error("更新账户二期代收付结果推送给账户失败的数据发生异常，error="+e);
		}
	}

	/**
	 * 更新账户一期代收付结果推送给账户失败的数据
	 */
	public void updateSend1AccountFail(
			List<com.rkylin.wheatfield.bean.TransOrderInfo> paramTmpList) {
		try {
			SettleTransDetail detailSubBean = new SettleTransDetail();
			for (com.rkylin.wheatfield.bean.TransOrderInfo transBean : paramTmpList) {
				detailSubBean.setTransDetailId(Integer.parseInt(transBean
						.getRemark()));
				if (transBean.getStatus() == 5) {
					detailSubBean.setDflag(40);
				} else if (transBean.getStatus() == 4) {
					detailSubBean.setDflag(60);
				} else {
					continue;
				}
				settleTransDetailManager.updateSettleTransDetail(detailSubBean);
			}
		} catch (Exception e) {
			logger.error("更新账户一期代收付结果推送给账户失败的数据发生异常=" + e);
		}

	}

	/**
	 * 更新账户一期代收付结果推送给账户成功的数据 注意：数据量较大的情况下可以考虑批量更新
	 */
	public void updateSend1AccountSuccess(
			List<com.rkylin.wheatfield.bean.TransOrderInfo> paramTmpList) {
		try {
			SettleTransDetail detailSubBean = new SettleTransDetail();
			for (com.rkylin.wheatfield.bean.TransOrderInfo transBean : paramTmpList) {
				detailSubBean.setTransDetailId(Integer.parseInt(transBean
						.getRemark()));
				if (transBean.getStatus() == 5) {
					detailSubBean.setDflag(41);
				} else if (transBean.getStatus() == 4) {
					detailSubBean.setDflag(61);
				} else {
					continue;
				}
				settleTransDetailManager.updateSettleTransDetail(detailSubBean);
			}
		} catch (Exception e) {
			logger.error("更新账户一期代收付结果推送给账户成功的数据发生异常，error=" + e);
		}
	}

	private String stringToNum(Object obj) {
		String str = String.valueOf(obj);
		if (obj == null || "".equals(obj)) {
			return "0";
		}
		return str;
	}

	/**
	 * 支付的回盘文件
	 * 
	 * @param fileType
	 *            文件类型
	 * @param batchNo
	 *            文件批次号
	 * @param accountDate
	 *            账期
	 */
	public Map<String, String> returnRopFile(Integer fileType, String batchNo,
			Date accountDate) {
		logger.info(">>> >>> >>> 开始:支付的回盘文件");
		Map<String, String> rtnMap = new HashMap<String, String>();
		int flg = 0;
		String priOrPubKey = null;
		String fileHType = "txt";
		if (fileType == 87) {
			flg = 1;
			priOrPubKey = "XR_PRIVATE_KEY";
			fileHType = "csv";
		} else {
			rtnMap.put("errCode", "0001");
			rtnMap.put("errMsg", "不支持的文件类型！");
			return rtnMap;
		}
		rtnMap = rOPFileDown.ROPfileDown(fileType, batchNo, accountDate, flg,
				priOrPubKey, fileHType);

		if ("0001".equals(rtnMap.get("errCode"))) {
			rtnMap.put("errCode", "0001");
			rtnMap.put("errMsg", "文件操作失败！");
		}
		logger.info("<<< <<< <<< 结束:支付的回盘文件");
		return rtnMap;
	}

	/**
	 * 修改清结算代收付汇总数据
	 * 
	 * @param transInvoiceList
	 * @param transSummeryList
	 * @param transDetailList
	 * @throws Exception
	 */
	@Transactional("settle_db")
	private void dealDsfUpdateTrans(List<SettleTransInvoice> transInvoiceList,
			List<SettleTransSummary> transSummeryList,
			List<SettleTransDetail> transDetailList) throws Exception {
		dsfLogic.updateSettleTransDetail(transDetailList);// 更新交易表中dflag
		dsfLogic.batchInsertTransSummery(transSummeryList);
		dsfLogic.batchInsertTransInvoice(transInvoiceList);
		// dsfLogic.insertTransSummery(transSummeryList);//插入汇总表
		// dsfLogic.insertTransInvoice(transInvoiceList);//插入结算表
	}

	// private Map<String, String> editPayChannelId(String payChannelId) throws
	// Exception {
	// Map<String, String> resultMap = new HashMap<String, String>();
	// String agreementCode; //协议
	// String channelCode; //渠道名称
	// String[] payChannelIdArr; //
	// /*
	// * 对 例如:"M000001,TongLian_Payment"格式payChannelId进行处理
	// */
	// payChannelIdArr = payChannelId.split(PAY_CHANNEL_ID_SPLIT);
	// if(payChannelIdArr != null && payChannelIdArr.length > 0) {
	// agreementCode = payChannelIdArr[0];
	// channelCode = payChannelIdArr[1];
	// } else {
	// Exception e = new Exception("payChannelId formart Exception!");
	// logger.error(">>> >>> 账户二期交易payChannelId格式异常", e);
	// throw e;
	// }
	// //截取渠道名 例:TongLian_AgentPay截取TongLian
	// if(channelCode.indexOf(CHANNEL_NO_SPLIT) > -1) {
	// channelCode = channelCode.substring(0,
	// channelCode.indexOf(CHANNEL_NO_SPLIT));
	// }
	// resultMap.put(AGREEMENT_CODE, agreementCode);
	// resultMap.put(CHANNEL_CODE, channelCode);
	// return resultMap;
	// }
	/**
	 * 发送交易至银企直联系统
	 * 
	 * @param sendInvoiceList
	 *            未发送的银企直联交易
	 */
	public void sendToCorpBankPay(List<SettleTransInvoice> sendInvoiceList)
			throws Exception {
		logger.info(">>> >>> >>> 开始:发送交易至银企直联系统");
		/*
		 * 入参校验
		 */
		if (sendInvoiceList == null || sendInvoiceList.size() < 1) {
			logger.info("<<< <<< <<< 结束:发送交易至银企直联系统 sendInvoiceList == null || sendInvoiceList.size() < 1");
			return;
		}
		/*
		 * 编辑入参
		 */
		BatchCorpBankPay batchCorpBankPay = new BatchCorpBankPay();
		List<CorpBankPay> corpbankpayList = new ArrayList<CorpBankPay>();
		batchCorpBankPay.setSysNo(CORPBANKPAY_SYS_NO);// 银企直联 的 清结算系统编号
		batchCorpBankPay.setTransCode(CORPBANKPAY_BATCH_PAY);
		batchCorpBankPay.setOrgNo(sendInvoiceList.get(0).getRootInstCd());
		batchCorpBankPay.setBusiCode(CORPBANKPAY_PAY);
		batchCorpBankPay.setSignType(1);// 签名类型 固定值1 即MD5
		batchCorpBankPay.setBatchNo(sendInvoiceList.get(0).getRequestNo());
		batchCorpBankPay.setTotalAcount(sendInvoiceList.size());
		batchCorpBankPay.setCurrency(sendInvoiceList.get(0).getCurrency());
		batchCorpBankPay.setAccountType(20);// 对公
		batchCorpBankPay.setSettleFlag(1);// 普通
		BigDecimal amountBD = null;
		BigDecimal sumAmountBD = new BigDecimal("0");
		for (SettleTransInvoice settleTransInvoice : sendInvoiceList) {
			corpbankpayList.add(getCorpBankPayByTransInvoice(settleTransInvoice));
			amountBD = new BigDecimal(String.valueOf(settleTransInvoice.getAmount()));
			sumAmountBD = sumAmountBD.add(amountBD);
		}
		batchCorpBankPay.setSignMsg(batchCorpBankPay.sign(CORPBANKPAY_SYS_KEY));
		batchCorpBankPay.setTotalAmount(sumAmountBD.longValue());
		batchCorpBankPay.setCorpbankpayList(corpbankpayList);
		batchCorpBankPay.setRemark("1");//dubbo通知
		BatchCorpBankPayResp batchCorpBankPayResp  = corpBankPayService.batchPaymnet(batchCorpBankPay);
		if(batchCorpBankPayResp != null) {
			logger.info(">>> >>> >>> 发送交易至银企直联系统信息");
			if("100000".equals(batchCorpBankPayResp.getReturnCode())) {
				logger.info(">>> >>> >>> 成功:发送交易至银企直联系统 returnCode:" + batchCorpBankPayResp.getReturnCode());
			} else {
				logger.info(">>> >>> >>> 失败:发送交易至银企直联系统 returnCode:" + batchCorpBankPayResp.getReturnCode());
			}
			logger.info(">>> >>> >>> returnMsg:" + batchCorpBankPayResp.getReturnMsg());
		} else {
			logger.info(">>> >>> >>> 失败:发送交易至银企直联系统 batchCorpBankPayResp is Null");
		}
		logger.info(">>> >>> >>> 发送交易至银企直联系统");
		logger.info("<<< <<< <<< 结束:发送交易至银企直联系统");
	}

	/**
	 * 将settleTransInvoice转化成CorpBankPay
	 * 
	 * @param settleTransInvoice
	 * @return
	 * @throws Exception
	 */
	private CorpBankPay getCorpBankPayByTransInvoice(
			SettleTransInvoice settleTransInvoice) throws Exception {
		logger.info(">>> >>> 开始:getCorpBankPayByTransInvoice方法");
		CorpBankPay corpBankPay = new CorpBankPay();
		corpBankPay.setTransCode(CORPBANKPAY_BATCH_PAY);// 企业银行批量支付
		corpBankPay.setSysNo(CORPBANKPAY_SYS_NO);// 清结算系统sysNo
		corpBankPay.setOrgNo(settleTransInvoice.getRootInstCd());// 协议
		corpBankPay.setBusiCode(CORPBANKPAY_PAY);
		corpBankPay.setSignType(1);// 签名类型 固定值1 即MD5
		corpBankPay.setSignMsg(corpBankPay.sign(CORPBANKPAY_SYS_KEY));
		corpBankPay.setTransNo(settleTransInvoice.getOrderNo());
		corpBankPay.setPayAmount(settleTransInvoice.getAmount());
		corpBankPay.setCurrency(settleTransInvoice.getCurrency());
		// corpBankPay.setPayerAccountNo("");
		// corpBankPay.setPayerAccountName("");
		// corpBankPay.setPayerBankNo("");
		// corpBankPay.setPayerDistrictCode("");
//		corpBankPay.setReceiverAccountType(10); //10:对私账户,11:信用卡:,12:活期一本通,13:定期一本通,20:对公账户
//		corpBankPay.setReceiverAccountNo("11014584071006");
//		corpBankPay.setReceiverAccountName("平安测试六零零零四六三六七五八五");
//		corpBankPay.setReceiverBankNo("");
//		corpBankPay.setReceiverBankName("平安银行");

		corpBankPay.setReceiverAccountNo(settleTransInvoice.getAccountNo());
		corpBankPay.setReceiverAccountName(settleTransInvoice.getAccountName());
		if ("1".equals(settleTransInvoice.getAccountProperty())) {// 对公/对私判断
			corpBankPay.setReceiverAccountType(20);// 对公
		} else {
			corpBankPay.setReceiverAccountType(10);// 对私
		}
		corpBankPay.setReceiverBankNo(settleTransInvoice.getPayBankCode());
		corpBankPay.setReceiverBankName(settleTransInvoice.getOpenBankName());
		corpBankPay.setReceiverProvince(settleTransInvoice.getProvince());
		corpBankPay.setReceiverCity(settleTransInvoice.getCity());
		// corpBankPay.setReceiverBankAddr("");
		if (CMBC_BANK_CODE.equals(settleTransInvoice.getBankCode())) {//判断行内行外
			corpBankPay.setBankFlag(1);//行内
		} else {
			corpBankPay.setBankFlag(2);//行外
		}
		if (settleTransInvoice.getAmount() <= 5000000) {//5w以上填大额,5w以下填小额
			corpBankPay.setRemitWay(3);
        } else {
        	corpBankPay.setRemitWay(4);
        }
		corpBankPay.setOrgNo(settleTransInvoice.getRootInstCd());
		corpBankPay.setSettleFlag(1);//1普通，2实时，3 特急
		corpBankPay.setPurpose("放款");
		corpBankPay.setRemark("1");//dubbo通知
		logger.info("<<< <<< 结束:getCorpBankPayByTransInvoice方法");
		return corpBankPay;
	}

	/**
	 * 从 银企直联 系统读取结果
	 */
	@Override
	public String getResultFromCorpBankPay(String rootInstCd, String requestNo,
			String orderNos, String accountDate, String[] funcCodes)
			throws Exception {
		logger.info(">>> >>> >>> 开始:从 银企直联 系统读取结果 rootInstCd=" + rootInstCd
				+ ",requestNo=" + requestNo + ",orderNos=" + orderNos
				+ ",accountDate=" + accountDate + ",funcCodes=" + funcCodes);
		List<SettleTransInvoice> invoiceList = null;
		List<SettleTransInvoiceHis> invoiceHisList = null;
		BatchCorpBankPayResp batchCorpBankPayResp = null;
		/*
		 * 入参校验
		 */
		if (StringUtils.isEmpty(rootInstCd))
			rootInstCd = null;
		if (StringUtils.isEmpty(requestNo))
			requestNo = null;
		if (StringUtils.isEmpty(orderNos))
			orderNos = null;
		if (StringUtils.isEmpty(accountDate))
			accountDate = null;
		if (funcCodes != null && funcCodes.length < 1)
			funcCodes = null;
		/*
		 * 汇总并去重查询条件 批次号
		 */
		invoiceList = this.listSettleTransInvoice(rootInstCd, requestNo, orderNos, accountDate, funcCodes);
		if (invoiceList != null && invoiceList.size() > 0) {
			
		} else {
			logger.info(">>> >>> 根据查询条件rootInstCd=" + rootInstCd
					+ ",requestNo=" + requestNo + ",orderNos=" + orderNos
					+ ",accountDate=" + accountDate
					+ "在 SettleTransInvoice 共查询到0条数据");
			invoiceHisList = this.listSettleTransInvoiceHis(rootInstCd,
					requestNo, orderNos, accountDate, funcCodes);
			if (invoiceHisList != null && invoiceHisList.size() > 0) {
				invoiceList = new ArrayList<SettleTransInvoice>();
				for(int i = 0; i < invoiceHisList.size(); i ++) {
					/*
					 * 将settleTransInvoiceHis的值 存入 settleTransInvoice
					 */
					invoiceList.add(this.settleTransInvoiceHisToSettleTransInvoice(invoiceHisList.get(i)));
				}
			} else {
				logger.info(">>> >>> 根据查询条件rootInstCd=" + rootInstCd
						+ ",requestNo=" + requestNo + ",orderNos="
						+ orderNos + ",accountDate=" + accountDate
						+ "在 SettleTransInvoiceHis 共查询到0条数据");
				return "success";
			}
		}
		/*
		 * 遍历批次号Set进行查询
		 */
		for (SettleTransInvoice settleTransInvoice : invoiceList) {
			batchCorpBankPayResp = this.corpBankPaybatchQuery(settleTransInvoice);
			if(batchCorpBankPayResp != null) {
				logger.info(">>> >>> >>> 发送交易至银企直联系统信息");
				if("100000".equals(batchCorpBankPayResp.getReturnCode())) {
					logger.info(">>> >>> >>> 成功:[requestNo:"+ settleTransInvoice.getRequestNo() +"]从 银企直联 系统读取结果 returnCode:" + batchCorpBankPayResp.getReturnCode());
					this.corpBankingNotify(batchCorpBankPayResp);
				} else {
					logger.info(">>> >>> >>> 失败:[requestNo:"+ settleTransInvoice.getRequestNo() +"]从 银企直联 系统读取结果 returnCode:" + batchCorpBankPayResp.getReturnCode());
				}
				logger.info(">>> >>> >>> returnMsg:" + batchCorpBankPayResp.getReturnMsg());
			} else {
				logger.info(">>> >>> >>> 失败:[requestNo:"+ settleTransInvoice.getRequestNo() +"]从 银企直联 系统读取结果 batchCorpBankPayResp is Null");
			}
		}
		logger.info("<<< <<< <<< 结束:从 银企直联 系统读取结果 rootInstCd=" + rootInstCd
				+ ",requestNo=" + rootInstCd + ",orderNos=" + orderNos
				+ ",accountDate=" + accountDate + ",funcCodes=" + funcCodes);
		return "success";
	}
	/**
	 * 将settleTransInvoiceHis的值 存入 settleTransInvoice
	 * @param settleTransInvoiceHis
	 * @return
	 */
	private SettleTransInvoice settleTransInvoiceHisToSettleTransInvoice(SettleTransInvoiceHis settleTransInvoiceHis) {
		SettleTransInvoice settleTransInvoice = new SettleTransInvoice();
		/*
		 * 将settleTransInvoiceHis 存入 settleTransInvoice
		 */
		BeanUtils.copyProperties(settleTransInvoiceHis, settleTransInvoice);
		return settleTransInvoice;
	}
	/**
	 * 查询SettleTransInvoice中的批次号
	 * 
	 * @param rootInstCd
	 * @param requestNo
	 * @param orderNos
	 * @param accountDate
	 * @param funcCodes
	 * @return
	 * @throws Exception
	 */
	private List<SettleTransInvoice> listSettleTransInvoice(String rootInstCd,
			String requestNo, String orderNos, String accountDate,
			String[] funcCodes) throws Exception {
		logger.info(">>> >>> 查询SettleTransInvoice中的批次号");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		SettleTransInvoiceQuery query = new SettleTransInvoiceQuery();
		if (accountDate != null)
			query.setAccountDate(format.parse(accountDate));
		query.setOrderNo(orderNos);
		query.setRootInstCd(rootInstCd);
		query.setFuncCodes(funcCodes);
		query.setRequestNo(requestNo);
		logger.info("<<< <<< 查询SettleTransInvoice中的批次号");
		return settleTransInvoiceManager.queryList(query);
	}

	/**
	 * 查询SettleTransInvoiceHis中的批次号
	 * 
	 * @param rootInstCd
	 * @param requestNo
	 * @param orderNos
	 * @param accountDate
	 * @param funcCodes
	 * @return
	 * @throws Exception
	 */
	private List<SettleTransInvoiceHis> listSettleTransInvoiceHis(
			String rootInstCd, String requestNo, String orderNos,
			String accountDate, String[] funcCodes) throws Exception {
		logger.info(">>> >>> 查询SettleTransInvoiceHis中的批次号");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		SettleTransInvoiceHisQuery query = new SettleTransInvoiceHisQuery();
		if (accountDate != null)
			query.setAccountDate(format.parse(accountDate));
		query.setOrderNo(orderNos);
		query.setRootInstCd(rootInstCd);
		query.setFuncCodes(funcCodes);
		logger.info("<<< <<< 查询SettleTransInvoiceHis中的批次号");
		return settleTransInvoiceHisManager.queryList(query);
	}

	/**
	 * 银企直联 批量转账查询接口
	 * 
	 * @param requestNo
	 * @return
	 */
	private BatchCorpBankPayResp corpBankPaybatchQuery(SettleTransInvoice settleTransInvoice) {
		logger.info(">>> >>> 银企直联 批量转账查询接口 requestNo:" + settleTransInvoice.getRequestNo() 
				+ ", batchNo:" + settleTransInvoice.getBatchNo());
		BatchCorpBankPayResp batchCorpBankPayResp = null;
		CorpBankPayQuery corpBankPayQuery = new CorpBankPayQuery();
		corpBankPayQuery.setSignType(1);
		corpBankPayQuery.setBatchNo(settleTransInvoice.getRequestNo());
		corpBankPayQuery.setBusiCode(settleTransInvoice.getBussinessCode());
		corpBankPayQuery.setOrgNo(settleTransInvoice.getRootInstCd());
		corpBankPayQuery.setTransCode(CORPBANKPAY_QUERY);
		corpBankPayQuery.setOtransCode(CORPBANKPAY_BATCH_PAY);
		corpBankPayQuery.setSysNo(CORPBANKPAY_SYS_NO);
		corpBankPayQuery.setSignMsg(corpBankPayQuery.sign(CORPBANKPAY_SYS_KEY));
		
		batchCorpBankPayResp = corpBankPayService.batchQuery(corpBankPayQuery);
		logger.info("<<< <<< 银企直联 批量转账查询接口 requestNo:" + settleTransInvoice.getRequestNo() 
				+ ", batchNo:" + settleTransInvoice.getBatchNo());
		return batchCorpBankPayResp;
	}

	/**
	 * 单条接收银企直联代收付结果
	 */
	@Override
	public String corpBankingNotify(CorpBankPayResp corpBankPayResp) {
		logger.info(">>> >>> >>> 开始:单条接收银企直联代收付结果 corpBankingNotify");
		List<CorpBankPayResp> mqList = null;
		/*
		 * 验证入参
		 */
		if (corpBankPayResp == null) {
			logger.info(">>> >>> >>> 异常:单条接收银企直联代收付结果 corpBankPayResp is Null");
			return "returnCode=100001&returnMsg=交易失败";// 失败
		}
		/*
		 * 银企直联结果写入MQ
		 */
		mqList = new ArrayList<CorpBankPayResp>();
		mqList.add(corpBankPayResp);
		try {
			this.writeToMQByCorpBankPayResp(mqList);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(
					">>> >>> >>> 异常:单条接收银企直联代收付结果 writeToMQByCorpBankPayResp方法异常",
					e);
		}
		logger.info("<<< <<< <<< 结束:单条接收银企直联代收付结果 corpBankingNotify");
		return "returnCode=100000&returnMsg=交易成功";
	}

	/**
	 * 批量接收银企直联代收付结果
	 */
	@Override
	public String corpBankingNotify(BatchCorpBankPayResp batchCorpBankPayResp) {
		logger.info(">>> >>> >>> 开始:批量接收银企直联代收付结果 corpBankingNotify");
		List<CorpBankPayResp> corpBankPayRespList = null;
		List<CorpBankPayResp> mqList = null;
		/*
		 * 验证批量结果信息
		 */
		if (batchCorpBankPayResp == null) {
			logger.info(">>> >>> >>> 异常:批量接收银企直联代收付结果 batchCorpBankPayResp is Null");
			return "returnCode=100001&returnMsg=交易失败";// 失败
		}
		/*
		 * 验证批量结果明细信息
		 */
		corpBankPayRespList = batchCorpBankPayResp.getCorpbankpayRespList();
		if (corpBankPayRespList == null || corpBankPayRespList.size() < 1) {
			logger.info(">>> >>> >>> 异常:批量接收银企直联代收付结果 corpbankpayRespList is Null or corpBankPayRespList.size() < 1");
			return "returnCode=100001&returnMsg=交易失败";// 失败
		}
		logger.info(">>> >>> >>> 批量接收银企直联代收付结果 corpBankPayRespList.size() ="
				+ corpBankPayRespList.size());
		mqList = new ArrayList<CorpBankPayResp>();
		for (CorpBankPayResp corpBankPayResp : corpBankPayRespList) {
			logger.info("即将代收付通知结果写入MQ");
			mqList.add(corpBankPayResp);
		}
		/*
		 * 银企直联结果写入MQ
		 */
		try {
			this.writeToMQByCorpBankPayResp(mqList);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(
					">>> >>> >>> 异常:批量接收银企直联代收付结果 writeToMQByCorpBankPayResp方法异常",
					e);
		}
		logger.info("<<< <<< <<< 结束:批量接收银企直联代收付结果 corpBankingNotify");
		return "returnCode=100000&returnMsg=交易成功";
	}
}
