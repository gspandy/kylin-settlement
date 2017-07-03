package com.rkylin.settle.logic;


import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rkylin.settle.constant.Constants;
import com.rkylin.settle.constant.SettleConstants;
import com.rkylin.settle.manager.ParameterInfoManager;
import com.rkylin.settle.manager.SettleCollectRuleManager;
import com.rkylin.settle.manager.SettleKernelEntryManager;
import com.rkylin.settle.manager.SettleParameterInfoManager;
import com.rkylin.settle.manager.SettleTransDetailManager;
import com.rkylin.settle.manager.SettlementManager;
import com.rkylin.settle.pojo.ParameterInfo;
import com.rkylin.settle.pojo.ParameterInfoQuery;
import com.rkylin.settle.pojo.SettleCollectRule;
import com.rkylin.settle.pojo.SettleCollectRuleQuery;
import com.rkylin.settle.pojo.SettleKernelEntry;
import com.rkylin.settle.pojo.SettleParameterInfo;
import com.rkylin.settle.pojo.SettleParameterInfoQuery;
import com.rkylin.settle.pojo.SettleTransDetail;
import com.rkylin.settle.pojo.SettleTransDetailQuery;
import com.rkylin.settle.pojo.TransEntrySa;
import com.rkylin.settle.util.SettlementUtil;
import com.rkylin.settle.util.SignUtils;
import com.rkylin.utils.SendSMS;
import com.rongcapital.activemq.api.po.ReturnObject;
import com.rongcapital.activemq.api.service.ActivemqSenderService;
import com.rongcapital.mtkernel.pojo.ModifyFinAccountVo;
import com.rongcapital.mtkernel.response.CommonResponse;
import com.rongcapital.mtkernel.service.FinanceAccountServiceApi;

/***
 * 清分系统通用汇总业务逻辑
 * @author Yang
 */
@Component("collectLogic")
public class CollectLogic extends BasicLogic {
	private static final String FINACCOUNTID_SUCCESS_CODE = "WF0000";							//账户接口查询finAccountId成功枚举值
	private static final String DEFAULT_CURRENCY_CNY = "CNY";									//默认币种人民币
	private static final String[] FINAL_SUBJECT_NAME_ARR = new String[]{"入金待清算", "出金待清算"};	//常量finAccountId名称
	private static final String CHANNEL_NO_SPLIT = "_";											//多渠道渠道名称分隔符例如TongLian_AgentPay
	private static final String PAY_CHANNEL_ID_SPLIT = ",";										//清结算渠道ID和协议CODE的分隔符
	private static final String COLLECT_MQKEY = "COLLECT_MQKEY";								//ActiveMQ的key(架构组接口)

	@Autowired
	private SettleCollectRuleManager settleCollectRuleManager;
	@Autowired
	private SettleTransDetailManager settleTransDetailManager;
	@Autowired
	private SettleKernelEntryManager settleKernelEntryManager;
	@Autowired
	private SettlementUtil settlementUtil;
	@Autowired
	private SettlementManager settlementManager;
	@Autowired
	private SettleParameterInfoManager settleParameterInfoManager;
	@Autowired
	private ActivemqSenderService activemqSenderService;				//发送activeMQ接口(架构组)
	@Autowired
	private ParameterInfoManager parameterInfoManager;					//查询账户二期属性表
	@Autowired
	private FinanceAccountServiceApi financeAccountService;				//账户接口查询finAccountId
	/***
	 * 从交易画面发起汇总
	 * @param settleTransDetail
	 */
	public void doCollect(Integer collectType, SettleTransDetail settleTransDetail) throws Exception {
		logger.info(">>> >>> >>> >>> 开始执行汇总任务 按交易信息汇总 [汇总类型:'"+ collectType +"'],[交易信息ID:'"+ settleTransDetail.getTransDetailId() +"']");
		String merchantCode = settleTransDetail.getMerchantCode();
		String funcCode = settleTransDetail.getFuncCode();
		Date accountDate = settleTransDetail.getAccountDate();
		//汇总信息List 用于持久化
		List<SettleKernelEntry> skeList = new ArrayList<SettleKernelEntry>();
		//汇总规则
		List<SettleCollectRule> settleCollectRuleList = null;
		SettleCollectRuleQuery query = new SettleCollectRuleQuery();
		query.setCollectType(collectType);
		query.setFuncCode(funcCode);
		query.setStatusId(1);
//		query.setRootInstCd(merchantCode);
//		query.setPayChannelId(payChannelId);
//		query.setObligate1(protocol);
		settleCollectRuleList = settleCollectRuleManager.queryList(query);
		//规则获取校验
		if(settleCollectRuleList == null || settleCollectRuleList.size() < 1) {
			throw new Exception("异常:获取到0条汇总规则!");
		}
		//汇总规则迭代器
		Iterator<SettleCollectRule> settleCollectRuleIter = settleCollectRuleList.iterator();
		//汇总规则实体
		SettleCollectRule srcItem = null;
		//汇总信息实体
		SettleKernelEntry ske = null;
		//交易流水 用于会计系统记账
		TransEntrySa tes = null;
		//交易流水List 用于会计系统记账 
		List<TransEntrySa> tesList = null;
		//交易流水 默认为1
		Integer transNo = 1;
		//交易流水 唯一标示
		Long transId = null;
		//登记薄错误记录表 唯一标示
		String settleNo = null;
		//全局的套录号
		String referEntryId = null;
		//汇总结果 状态 -1 金额0不发送, 0失败, 1成功
		Integer statusId = null;
		//错误信息
		String errorMessage = null;
		String retMessage = null;
		//汇总金额
		Long sumAmount = null;
		//汇总交易主键数组
		Long[] detailIdArr = null;
		//汇总信息集合
		List<Map<String, Object>> relationList = new ArrayList<Map<String, Object>>();
		Map<String, Object> relationMap = null;
		//判断日中是否正常结束
		if(!settlementUtil.cutDayIsSuccess4Account()) {
			//获取紧急联系人手机号
			String mobile = settlementUtil.getEmergencyContactMobile();
			//短信模板: xx系统xx环境，编号××存在问题，请及时解决!
			//编号3会计系统,系统日切失败
			String content = "清算系统测试环境，编号"+Constants.SMS_NUMBER_3+"存在问题，请及时解决！";
			//给相关人员发送短信
			SendSMS.sendSMS(mobile, content);
			throw new Exception("判断日中是否正常结束异常");
		}
		logger.info(">>> >>> >>> 账期:"+ new SimpleDateFormat("yyyy-MM-dd").format(accountDate));
		String finAccountId1 = null;
		String finAccountId2 = null;
		Map<String, String> finAccountIdMap = null;
		Boolean isCompleteAccountIds = false;
		//渠道
		String payChannelId = null;
		//渠道名称
		String channelCode = null;
		//协议
		String payWay = null;
		//渠道
		String[] payChannelIdArr = null;
		//查询渠道名称和编码的对应关系
		Map<String, String> payChannelIdTitleMap = null;
		//备注
		String remark = "";
		try {
			payChannelIdTitleMap = this.getPayChannelIdTitleMap();
		} catch (Exception e) {
			logger.error(">>> >>> 异常:查询渠道名称和编码的对应关系" + e);
		}
		while(settleCollectRuleIter.hasNext()) {
			srcItem = settleCollectRuleIter.next();
			logger.info(">>> >>> >>> 开始汇总:" + "【机构--" + srcItem.getRootInstCd() + "】，" + "【渠道--" + srcItem.getPayChannelId() + "】，" + "【funcCode--" + srcItem.getFuncCode() + "】");
			//全局流水号
			transId = this.genTimestampAndRandom();
			referEntryId = settleNo = "QJ" + transId;
			//计算汇总金额
			sumAmount = settleTransDetail.getAmount();
			if(SettleConstants.COLLECT_TYPE_1 == collectType) {
				channelCode = settleTransDetail.getChannelInfo();
				payWay = settleTransDetail.getPayWay();
			} else if(SettleConstants.COLLECT_TYPE_2 == collectType) {
				/*
				 * 对 例如:"M000001,01"格式payChannelId进行处理
				 */
				payChannelIdArr = payChannelId.split(PAY_CHANNEL_ID_SPLIT);
				if(payChannelIdArr != null && payChannelIdArr.length > 0) {
					payWay = payChannelIdArr[0];
					payChannelId = payChannelIdArr[1];
					channelCode = payChannelIdTitleMap.get(payChannelId);
				} else {
					logger.error(">>> >>> 账户二期交易payChannelId格式异常", new Exception("payChannelId formart Exception!"));
					continue;
				}
			}
			//汇总交易主键ID
			detailIdArr =  new Long[]{settleTransDetail.getTransDetailId().longValue()};
			//记录汇总信息和交易信息的对应关系
			relationMap = new HashMap<String, Object>();
			relationMap.put("transId", transId);
			relationMap.put("detailIdArr", detailIdArr);
			relationMap.put("collectType", collectType);
			relationMap.put("accountDate", accountDate);
			relationList.add(relationMap);
			try {
				finAccountIdMap = this.getFinAccountIdByCollectRule(merchantCode, channelCode, payWay, srcItem);
				finAccountId1 = finAccountIdMap.get(srcItem.getFinAccountId1());
				finAccountId2 = finAccountIdMap.get(srcItem.getFinAccountId2());
				isCompleteAccountIds = !StringUtils.isEmpty(finAccountId1) && !StringUtils.isEmpty(finAccountId2);
			} catch (Exception e) {
				logger.error(">>> >>> 异常:调用会计并根据汇总信息二次编辑汇总规则", e);
			}
			//获取汇总信息结构体
			ske = this.createSettleKernelEntry(
					transId,
					settleNo,
					transNo, 
					srcItem.getKernelFuncCode(), 
					srcItem.getFinAccountId1(), 
					srcItem.getFinAccountId2(), 
					sumAmount,
					referEntryId, 
					accountDate, 
					String.valueOf(collectType), 
					srcItem.getRootInstCd());
			if(sumAmount > 0L && isCompleteAccountIds) {//汇总金额大于 0并且账户ID信息完整 , 发送ActiveMQ
				tes = this.createTransEntrySa(transId, ske);
				tesList = new ArrayList<TransEntrySa>();
				tesList.add(tes);
				statusId = 0;	//未发送或者失败
				errorMessage = "";
				retMessage = "'交易'画面触发交易金额汇总,未发送会计系统ActiveMQ";
				ske.setStatusId(statusId);
				ske.setRemark2(errorMessage);
				ske.setRsMsg(retMessage);
				logger.info(">>> >>> '交易'画面触发交易金额汇总,未发送会计系统ActiveMQ");
			} else {//汇总金额小于等于 0, 不发送ActiveMQ
				remark = "";
				if(sumAmount <= 0) remark = "【汇总金额为 0】";
				if(!isCompleteAccountIds) remark += "【账户ID信息不完整】";
				remark += ", 不发送ActiveMQ";
				ske.setStatusId(-1);//汇总金额为 0, 不发送ActiveMQ
				ske.setRemark2(remark);
				logger.info(">>> >>> " + remark);
			}
			/*
			 * 插入到 汇总信息List 并持久化 
			 */
			skeList.add(ske);
			logger.info("<<< <<< << 汇总结束:" + "【机构--" + srcItem.getRootInstCd() + "】，" + "【渠道--" + srcItem.getPayChannelId() + "】，" + "【funcCode--" + srcItem.getFuncCode() + "】");
		}
		this.insertAndUpdateSettleKernelEntry(skeList);
		logger.info(">>> >>> >>> 开始: 批量插入 kernel_entry和detail_info 对应关系");
		if(relationList.size() > 0) settlementManager.batInsKerDetRel(relationList);
		logger.info("<<< <<< <<< 结束: 批量插入 kernel_entry和detail_info 对应关系");
		logger.info("<<< <<< <<< <<< 结束执行汇总任务 按交易信息汇总 [汇总类型:'"+ collectType +"'],[trans_detail_id:'"+ settleTransDetail.getTransDetailId() +"']");
	}
	
	/**
	 * 汇总并发送
	 * @param collectType	//汇总类型
	 * @return
	 * @throws Exception
	 */
	public void doCollect(Integer collectType) throws Exception {
		Date accountDate = null;
		this.doCollect(collectType, accountDate);
	}
	/**
	 * 汇总并发送
	 * @param accountDate	//账期
	 * @param collectType	//汇总类型
	 * @return
	 * @throws Exception
	 */
	public void doCollect(Integer collectType, Date accountDate) throws Exception {
		this.doCollect(collectType, accountDate, null, null, null);
	}
	/**
	 * 汇总并发送
	 * @param collectType	//汇总类型
	 * @param funcCode		//交易类型
	 * @return
	 * @throws Exception
	 */
	public void doCollect(Integer collectType, String funcCode) throws Exception {
		this.doCollect(collectType, null, null, null, funcCode);
	}
	/**
	 * 汇总并发送
	 * @param collectType	汇总类型
	 * @param accountDate	账期
	 * @param merchantCode	机构号
	 * @param payChannelId	渠道号
	 * @param funcCode		交易类型个
	 * @throws Exception
	 */
	public void doCollect(Integer collectType, Date accountDate, String merchantCode, String payChannelId, String funcCode) throws Exception {
		logger.info(">>> >>> >>> >>> 开始执行汇总任务 [汇总类型:'"+ collectType +"'],[机构:'"+ merchantCode +"'],[渠道:'"+ payChannelId +"'],[funcCode:'"+ funcCode +"']");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		//汇总交易MAP
		Map<String, Map<String, Object>> collectMap = null;
		//汇总Key对应的Value
		Map<String, Object> collectValue = null;
		//汇总信息List 用于持久化
		List<SettleKernelEntry> skeList = new ArrayList<SettleKernelEntry>();
		//汇总规则
		List<SettleCollectRule> settleCollectRuleList = null;
		SettleCollectRuleQuery query = new SettleCollectRuleQuery();
		query.setCollectType(collectType);
		query.setRootInstCd(merchantCode);
		query.setFuncCode(funcCode);
		query.setPayChannelId(payChannelId);
		query.setStatusId(1);
		settleCollectRuleList = settleCollectRuleManager.queryList(query);
		//汇总规则迭代器
		Iterator<SettleCollectRule> settleCollectRuleIter = settleCollectRuleList.iterator();
		//汇总规则实体
		SettleCollectRule srcItem = null;
		//汇总信息实体
		SettleKernelEntry ske = null;
		//交易流水 用于会计系统记账
		TransEntrySa tes = null;
		//交易流水List 用于会计系统记账 
		List<TransEntrySa> tesList = null;
		//交易流水 默认为1
		Integer transNo = 1;
		//交易流水 唯一标示
		Long transId = null;
		//登记薄错误记录表 唯一标示
		String settleNo = null;
		//全局的套录号
		String referEntryId = null;
		//汇总金额
		Long sumAmount = null;
		//汇总交易主键数组
		Long[] detailIdArr = null;
		//MD5加密签名
		String verifySign = null;
		//mqKey 消息队列名称
		String mqKey = null;
		try {
			mqKey = this.getCollectMqKey(COLLECT_MQKEY);
		} catch (Exception e) {
			logger.error(">>> >>> 异常:获取消息队列名称", e);
		}
		//汇总信息集合
		List<Map<String, Object>> relationList = new ArrayList<Map<String, Object>>();
		Map<String, Object> relationMap = null;
		//判断日中是否正常结束
		if(!settlementUtil.cutDayIsSuccess4Account()) {//
			//获取紧急联系人手机号
			String mobile = settlementUtil.getEmergencyContactMobile();
			//短信模板: xx系统xx环境，编号××存在问题，请及时解决!
			//编号3会计系统,系统日切失败
			String content = "清算系统测试环境，编号"+Constants.SMS_NUMBER_3+"存在问题，请及时解决！";
			//给相关人员发送短信
			SendSMS.sendSMS(mobile, content);
			throw new Exception("判断日中是否正常结束异常");
		}
		//判断账期是否传入
		if(accountDate == null) {//如果账期为null, 即是未传入账期
			//从DB中过去账期信息, 账期在DB中每日更新,获取T-1日账期  	
			accountDate = (Date) settlementUtil.getAccountDate("yyyyMMdd", -1, "Date");	
		}
		logger.info(">>> >>> >>> 账期:"+ new SimpleDateFormat("yyyy-MM-dd").format(accountDate));
		
		while(settleCollectRuleIter.hasNext()) {
			srcItem = settleCollectRuleIter.next();
			logger.info(">>> >>> >>> 开始汇总:" + "【机构--" + srcItem.getRootInstCd() + "】，" + "【渠道--" + srcItem.getPayChannelId() + "】，" + "【funcCode--" + srcItem.getFuncCode() + "】");	
			try {		
				collectMap = this.getCollectAmount(accountDate, srcItem.getFuncCode(), collectType);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(">>> >>> 汇总金额异常:", e);
				continue;
			}		
			if(collectMap == null || collectMap.size() < 1) {
				//计算汇总金额
				sumAmount = 0L;	//汇总交易主键ID
				detailIdArr = null;
				//全局流水号
				transId = this.genTimestampAndRandom();
				referEntryId = settleNo = "QJ" + transId;
				//获取汇总信息结构体
				ske = this.createSettleKernelEntry(
						transId,
						settleNo,
						transNo, 
						srcItem.getKernelFuncCode(), 
						srcItem.getFinAccountId1(), 
						srcItem.getFinAccountId2(), 
						sumAmount,
						referEntryId, 
						accountDate, 
						String.valueOf(collectType), 
						srcItem.getRootInstCd());
						//汇总金额小于等于 0, 不发送ActiveMQ
						ske.setStatusId(-1);//汇总金额为 0, 不发送ActiveMQ
						ske.setRemark2("【汇总金额为 0】, 不发送ActiveMQ");
						logger.info(">>> >>> 【汇总金额为 0】, 不发送ActiveMQ ");
				/*
				 * 插入到 汇总信息List 并持久化 
				 */
				skeList.add(ske);
				logger.info("<<< <<< << 汇总结束:" + "【机构--" + srcItem.getRootInstCd() + "】，" + "【渠道--" + srcItem.getPayChannelId() + "】，" + "【funcCode--" + srcItem.getFuncCode() + "】");	
			} else {
				String rootInstCd = null;
				String channelCode = null;
				String agreementCode = null;
				String finAccountId1 = null;
				String finAccountId2 = null;
				String remark = "";
				String settleDay = null;
				Map<String, String> finAccountIdMap = null;
				Boolean isCompleteAccountIds = false;
				for(String collectKey : collectMap.keySet()) {
					collectValue = collectMap.get(collectKey);
					//全局流水号
					transId = this.genTimestampAndRandom();
					referEntryId = settleNo = "QJ" + transId;
					//计算汇总金额
					sumAmount = Long.parseLong(String.valueOf(collectValue.get("AMOUNT")));
					//机构
					rootInstCd = String.valueOf(collectValue.get("MERCHANT_CODE"));
					//渠道名称
					channelCode = String.valueOf(collectValue.get("CHANNEL_INFO"));
					//协议
					agreementCode = String.valueOf(collectValue.get("PAY_WAY"));
					//交易账期
					settleDay = String.valueOf(collectValue.get("ACCOUNT_DATE"));
					//调用会计并根据汇总信息二次编辑汇总规则
					try {
						finAccountIdMap = this.getFinAccountIdByCollectRule(rootInstCd, channelCode, agreementCode, srcItem);
						finAccountId1 = finAccountIdMap.get(srcItem.getFinAccountId1());
						finAccountId2 = finAccountIdMap.get(srcItem.getFinAccountId2());
						isCompleteAccountIds = !StringUtils.isEmpty(finAccountId1) && !StringUtils.isEmpty(finAccountId2);
					} catch (Exception e) {
						logger.error(">>> >>> 异常:调用会计并根据汇总信息二次编辑汇总规则", e);
					}
					//获取MD5加密签名
					verifySign = this.getVerifySign(referEntryId);
					//汇总交易主键ID
					detailIdArr =  this.editTransDetailIdArr(String.valueOf(collectValue.get("TRANS_DETAIL_IDS")));
					//记录汇总信息和交易信息的对应关系
					relationMap = new HashMap<String, Object>();
					relationMap.put("transId", transId);
					relationMap.put("detailIdArr", detailIdArr);
					relationMap.put("collectType", collectType);
					relationMap.put("accountDate", settleDay);
					relationList.add(relationMap);
					//获取汇总信息结构体
					ske = this.createSettleKernelEntry(
							transId,
							settleNo,
							transNo, 
							srcItem.getKernelFuncCode(), 
							finAccountId1,
							finAccountId2,
							sumAmount,
							referEntryId, 
							sdf.parse(settleDay), 
							String.valueOf(collectType), 
							String.valueOf(rootInstCd));
					
					if(sumAmount > 0L && isCompleteAccountIds) {//汇总金额大于 0并且账户ID信息完整 , 发送ActiveMQ
						tes = this.createTransEntrySa(transId, ske);
						tesList = new ArrayList<TransEntrySa>();
						tesList.add(tes);
						/*
						 * 发送activeMQ
						 */
						ske = this.activemqSender(settleNo, tesList, ske, verifySign, mqKey);
						logger.info(">>> >>> '汇总'画面触发交易金额汇总,未发送会计系统ActiveMQ");
					} else {//汇总金额小于等于 0, 不发送ActiveMQ
						remark = "";
						if(sumAmount <= 0) remark = "【汇总金额为 0】";
						if(!isCompleteAccountIds) remark += "【账户ID信息不完整】";
						remark += ", 不发送ActiveMQ";
						ske.setStatusId(-1);//汇总金额为 0, 不发送ActiveMQ
						ske.setRemark2(remark);
						logger.info(">>> >>> " + remark);
					}
					/*
					 * 插入到 汇总信息List 并持久化 
					 */
					skeList.add(ske);
					logger.info("<<< <<< << 汇总结束:" + "【机构--" + srcItem.getRootInstCd() + "】，" + "【渠道--" + srcItem.getPayChannelId() + "】，" + "【funcCode--" + srcItem.getFuncCode() + "】");		
				}
			}
		}
		this.insertAndUpdateSettleKernelEntry(skeList);
		logger.info(">>> >>> >>> 开始: 批量插入 kernel_entry和detail_info 对应关系");
		if(relationList.size() > 0) settlementManager.batInsKerDetRel(relationList);
		logger.info("<<< <<< <<< 结束: 批量插入 kernel_entry和detail_info 对应关系");
		logger.info("<<< <<< <<< <<< 执行汇总任务完成 [账期:'"+ new SimpleDateFormat("yyyy-MM-dd").format(accountDate) +"'],[汇总类型:'"+ collectType +"'],[机构:'"+ merchantCode +"'],[渠道:'"+ payChannelId +"'],[funcCode:'"+ funcCode +"']");
	}
	/**
	 * 汇总信息发送MQ
	 * @param ske
	 * @throws Exception
	 */
	public void doCollectBySettleKernelEntry(Long id) throws Exception {
		//获取汇总信息通过ID
		SettleKernelEntry ske = settleKernelEntryManager.findSettleKernelEntryById(id);
		//汇总信息List 用于持久化
		List<SettleKernelEntry> skeList = null;
		//交易流水 用于会计系统记账
		TransEntrySa tes = null;
		//交易流水List 用于会计系统记账
		List<TransEntrySa> tesList = null;
		//登记薄错误记录表 用于会计系统记账
//		RegisterFalut rf = null;
		//
		String verifySign = null;
		//交易流水 唯一标示
		Long transId = ske.getTransId();
		//登记薄错误记录表 唯一标示
//		String settleNo = ske.getSettleNo();
		//mq消息体
		String kernelEntryJSONStr = null;
		//mq发送返回值
		ReturnObject<String> returnObject = null;
		//mq是否执行成功
		Boolean isSucc = null;
		//汇总结果 状态 -1 金额 0不发送, 0失败, 1成功
		Integer statusId = ske.getStatusId();
		//错误信息
		String errorMessage = null;
		String retMessage = null;
		//mqKey 消息队列名称
		String mqKey = null;
		try {
			mqKey = this.getCollectMqKey(COLLECT_MQKEY);
		} catch (Exception e) {
			logger.error(">>> >>> 异常:获取消息队列名称", e);
		}
		//汇总金额
		Long sumAmount = ske.getPaymentAmount1();
		//已经汇总并发送成功的交易不能重复发送
		if(statusId == 1) {
			logger.info(">>> >>> 已经汇总并发送成功的交易不能重复发送!");
			return;
		}
		//汇总金额大于 0 , 发送ActiveMQ
		if(sumAmount > 0L) {
			tes = this.createTransEntrySa(transId, ske);
			tesList = new ArrayList<TransEntrySa>();
			tesList.add(tes);
//			rf = this.createRegisterFalut(settleNo, ske);
			verifySign = this.getVerifySign(ske.getReferEntryId());
			kernelEntryJSONStr = this.map4MQ2JsonObj(tesList, verifySign);
			try {
				returnObject = activemqSenderService.sendQueueSingle(mqKey, kernelEntryJSONStr);
				isSucc = returnObject.isSuccFlag();
				statusId = isSucc ? 1 : 0;
				errorMessage = returnObject.getErrorMessage();
				retMessage = returnObject.getRetMessage();
				ske.setStatusId(statusId);
				ske.setRemark2(errorMessage);
				ske.setRsMsg(retMessage);
				skeList = new ArrayList<SettleKernelEntry>();
				skeList.add(ske);
				this.insertAndUpdateSettleKernelEntry(skeList);
				logger.info(">>> >>> 发送ActiveMQ[mqKey:"+ mqKey +"], 返回值 [isSucc:'"+isSucc+"', errorMessage:'"+errorMessage+"', retMessage:'"+retMessage+"']");
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(">>> >>> 发送ActiveMQ异常:", e);
			}
		} else {//汇总金额小于等于 0, 不发送ActiveMQ
			ske.setStatusId(-1);	//汇总金额为 0, 不发送ActiveMQ
			ske.setRemark2("【汇总金额为 0】, 不发送ActiveMQ");
			logger.info(">>> >>> 【汇总金额为 0】, 不发送ActiveMQ ");
		}
	}
	/**
	 * 获取延签信息并使用MD5加密返回加密后的string
	 * @return
	 */
	private String getVerifySign(String referEntryId) throws Exception {
		SignUtils signUtils = new SignUtils();
		String verifySign = null;
		ParameterInfoQuery query = new ParameterInfoQuery();
		query.setParameterType("1");
		query.setParameterCode("signature");
		query.setStatusId(1);
		List<ParameterInfo> list = parameterInfoManager.queryList(query);
		verifySign = list.size() > 0 ? referEntryId + list.get(0).getParameterValue() : null;
		logger.info(">>> >>> >>> 获取 验签的数据 字符串[未加密] :" + verifySign);
		verifySign = signUtils.getMD5Code(verifySign);
		logger.info(">>> >>> >>> 获取 验签的数据 字符串[已加密] :" + verifySign);
		return verifySign;
	}
	/**
	 * 将MQ消息结构体及转换程JSON
	 * @param tesList
	 * @param rf
	 * @return
	 */
//	private String map4MQ2JsonObj(List<TransEntrySa> tesList, RegisterFalut rf, String verifySign) {
//		Map<String, Object> map4MQ = new HashMap<String, Object>();
//		map4MQ.put("size", tesList.size());
//		map4MQ.put("objs", tesList);
//		map4MQ.put("register", rf);
//		map4MQ.put("verifySign", verifySign);
//		
//		JSONObject jsonArray = JSONObject.fromObject(map4MQ);
//		logger.info(">>> >>> >>> 清结算 汇总信息 封装成 MQ并转为JSON:" + jsonArray.toString());
//		return jsonArray.toString();
//	}
	/**
	 * 将MQ消息结构体及转换程JSON 不传Register
	 * @param tesList
	 * @param rf
	 * @return
	 */
	private String map4MQ2JsonObj(List<TransEntrySa> tesList, String verifySign) {
		Map<String, Object> map4MQ = new HashMap<String, Object>();
		map4MQ.put("size", tesList.size());
		map4MQ.put("objs", tesList);
		map4MQ.put("verifySign", verifySign);
		JSONObject jsonArray = JSONObject.fromObject(map4MQ);
		logger.info(">>> >>> >>> 清结算 汇总信息 封装成 MQ并转为JSON:" + jsonArray.toString());
		return jsonArray.toString();
	}
	/**
	 * 获取汇总规则
	 * @param collectType
	 * @return
	 */
//	private List<SettleCollectRule> getCollectRule(Integer collectType) {
//		SettleCollectRuleQuery query = new SettleCollectRuleQuery();
//		query.setCollectType(collectType);
//		query.setStatusId(1);
//		return settleCollectRuleManager.queryList(query);
//	}
	/**
	 * 获取‘汇总金额’
	 * @param accountDate
	 * @param rootInstCd
	 * @param payChannelId
	 * @param funcCode
	 * @return
	 * @throws Exception
	 */
//	private Map<String, Object> getCollectAmount(Date accountDate, String rootInstCd, String payChannelId, String funcCode, Integer collectType) throws Exception {
//		logger.info(">>> >>> 开始 获取‘汇总金额’");
//		//返回值结构体春初 汇总金额和交易信息ID数组
//		Map<String, Object> map = new HashMap<String, Object>();
//		//查询条件query对象
//		SettleTransDetailQuery query = new SettleTransDetailQuery();
//		//总金额
//		Long sumAmount = 0L;
//		//交易信息ID数组
//		Long[] detailIdArr = null;
//		query.setAccountDate(accountDate);	//账期
//		query.setMerchantCode(rootInstCd);	//机构号
//		query.setPayChannelId(payChannelId);//渠道号
//		query.setFuncCode(funcCode);		//交易类型
//		query.setOrderType(0);				
//		query.setTransType(collectType); 	//汇总类型 判断此交易是否被汇总过
//		switch(collectType) {				//判断汇总类型
//			case 1:							//结算后汇总
//				query.setReadStatusId(0);	
//				break;
//			case 2:							//对账后汇总
//				query.setStatusId(SettleConstants.STATUS_COLLATE_SUCCESS);
//				break;
//		}
//		
//		//查询汇总交易的金额和ID
//		List<Map<String, Object>> list = settleTransDetailManager.collectAmountByExample(query);
//		//非空校验和处理
//		if(list == null || list.size() == 0) {
//			logger.info(">>> >>> 结束 获取‘汇总金额’ [无交易] 汇总金额 为 0");
//			return null;
//		}
//		//初始化交易信息ID数组
//		detailIdArr = new Long[list.size()];
//		//遍历list计算汇总金额并将交易ID存入数组
//		for(int i = 0; i < detailIdArr.length; i++) {
//			sumAmount += Long.parseLong(String.valueOf(list.get(i).get("AMOUNT")));
//			detailIdArr[i] = Long.parseLong(String.valueOf(list.get(i).get("TRANS_DETAIL_ID")));
//		}
//		//封装返回值结构体
//		map.put("SUM_AMOUNT", sumAmount);
//		map.put("TRANS_DETAIL_ID_ARR", detailIdArr);
//		logger.info(">>> >>> 结束 获取‘汇总金额’ 【汇总条数:" + detailIdArr.length + "】; " + "【汇总金额:" + sumAmount + "】");
//		return map;
//	}
	/**
	 * 获取‘汇总金额’
	 * @param accountDate
	 * @param rootInstCd
	 * @param payChannelId
	 * @param funcCode
	 * @return
	 * @throws Exception
	 */
	private Map<String, Map<String, Object>> getCollectAmount(Date accountDate, String funcCode, Integer collectType) throws Exception {
		logger.info(">>> >>> >>> 开始 获取‘汇总金额’");
		//汇总Map
		Map<String, Map<String, Object>> collectMap = new HashMap<String, Map<String, Object>>();
		//汇总Key
		String collectKey = null;
		//collectKey对应的Value
		Map<String, Object> collectValue = null;
		//汇总Key
		StringBuffer collectKeyBuffer = null;
		//查询条件query对象
		SettleTransDetailQuery query = new SettleTransDetailQuery();
		//查询汇总交易的金额和ID
		List<Map<String, Object>> list = null;
		query.setAccountDate(accountDate);		//账期
		query.setFuncCode(funcCode);			//交易类型
		query.setTransType(collectType); 		//汇总类型 判断此交易是否被汇总过
		if(SettleConstants.COLLECT_TYPE_1 == collectType) {//使用【多渠道】汇总记账
			query.setOrderType(1);
			query.setDataFrom(3);
			query.setStatusId(21);
			if("40131".equals(funcCode)) {/**实时代收**/
				/*
				 * 暂无处理
				 */
			} else if("4013".equals(funcCode)) {/**还款代收**/
				/*
				 * 暂无处理
				 */
			} else {
				throw new Exception("获取'汇总金额'异常:未知的funcCode:" + funcCode);
			}
			/*
			 * 查询待清算交易 
			 */
			list = settleTransDetailManager.collectAmountByExample(query);
		} else if(SettleConstants.COLLECT_TYPE_2 == collectType) {//使用【账户二期】汇总记账
			query.setOrderType(0);
			query.setDataFrom(4);
			if("4015".equals(funcCode)) {/**充值**/
				query.setStatusId(21);
			}  else if("4014".equals(funcCode)) {/**代付**/
				query.setObligate1("6,60,61");
			} else if("4016".equals(funcCode)) {/**提现**/
				query.setObligate1("6,60,61");
			}  else if("5024".equals(funcCode)) {/**退票**/
				query.setStatusId(21);
			} else {
				throw new Exception("获取'汇总金额'异常:未知的funcCode:" + funcCode);
			}
			/*
			 * 查询待清算交易 
			 */
			list = settleTransDetailManager.collectAmountByExample(query);
		} else {
			throw new Exception("获取'汇总金额'异常:未知的collectType:" + collectType);
		}
		//非空校验和处理
		if(list == null || list.size() == 0) {
			logger.info(">>> >>> 结束 获取‘汇总金额’ [无交易] 汇总金额 为 0");
			return null;
		}
		//查询渠道名称和编码的对应关系
		Map<String, String> payChannelIdTitleMap = null;
		try {
			payChannelIdTitleMap = this.getPayChannelIdTitleMap();
		} catch (Exception e) {
			logger.error(">>> >>> 异常:查询渠道名称和编码的对应关系" + e);
		}
		//交易金额ID和账期的映射
		Map<String, Object> mapItem = null;
		//计算金额
		BigDecimal addamt = null;
		BigDecimal settleamt = null;
		//记录交易主键
		String transId = null;
		//渠道
		String payChannelId = null;
		//渠道名称
		String channelCode = null;
		//协议
		String payWay = null;
		//渠道
		String[] payChannelIdArr = null;
		//遍历 查询到的全部汇总交易
		for(int i = 0; i < list.size(); i++) {
			//当前交易映射
			mapItem = list.get(i);
			if(SettleConstants.COLLECT_TYPE_1 == collectType) {
				channelCode = String.valueOf(mapItem.get("CHANNEL_INFO"));
				payChannelId = String.valueOf(mapItem.get("PAY_CHANNEL_ID"));
				payWay = String.valueOf(mapItem.get("PAY_WAY"));
			} else if(SettleConstants.COLLECT_TYPE_2 == collectType) {
				/*
				 * 对 例如:"M000001,01"格式payChannelId进行处理
				 */
				payChannelIdArr = String.valueOf(mapItem.get("PAY_CHANNEL_ID")).split(PAY_CHANNEL_ID_SPLIT);
				if(payChannelIdArr != null && payChannelIdArr.length > 0) {
					payWay = payChannelIdArr[0];
					payChannelId = payChannelIdArr[1];
					channelCode = payChannelIdTitleMap.get(payChannelId);
				} else {
					logger.error(">>> >>> 账户二期交易payChannelId格式异常", new Exception("payChannelId formart Exception!"));
					continue;
				}
			}
			//截取渠道名 例:TongLian_AgentPay截取TongLian
			if(channelCode.indexOf(CHANNEL_NO_SPLIT) > -1) {
				channelCode = channelCode.substring(0, channelCode.indexOf(CHANNEL_NO_SPLIT));
			}
			//当前交易主键
			transId = String.valueOf(mapItem.get("TRANS_DETAIL_ID"));
			//汇总Key
			collectKeyBuffer = new StringBuffer();
			collectKeyBuffer.append(String.valueOf(mapItem.get("ACCOUNT_DATE")));
			collectKeyBuffer.append("_");
			collectKeyBuffer.append(String.valueOf(mapItem.get("FUNC_CODE")));
			collectKeyBuffer.append("_");
			collectKeyBuffer.append(String.valueOf(mapItem.get("MERCHANT_CODE")));
			collectKeyBuffer.append("_");
			collectKeyBuffer.append(String.valueOf(payChannelId));
			collectKeyBuffer.append("_");
			collectKeyBuffer.append(String.valueOf(payWay));
			collectKeyBuffer.append("_");
			collectKeyBuffer.append(collectType);
			collectKey = collectKeyBuffer.toString();
			//汇总判断
			if(collectMap.containsKey(collectKey)) {
				collectValue = collectMap.get(collectKey);
				settleamt = new BigDecimal(String.valueOf(collectValue.get("AMOUNT")));
				addamt = new BigDecimal(String.valueOf(mapItem.get("AMOUNT")));
				settleamt = settleamt.add(addamt);
				collectValue.put("AMOUNT", settleamt.longValue());
				collectValue.put("TRANS_DETAIL_IDS", String.valueOf(collectValue.get("TRANS_DETAIL_IDS")) + "," + transId);
				logger.debug(">>> >>> 【汇总金额】 collectKey:" + collectKey);
			} else {
				collectValue = new HashMap<String, Object>();
				collectValue.put("ACCOUNT_DATE", String.valueOf(mapItem.get("ACCOUNT_DATE")));
				collectValue.put("FUNC_CODE", mapItem.get("FUNC_CODE"));
				collectValue.put("MERCHANT_CODE", mapItem.get("MERCHANT_CODE"));
				collectValue.put("AMOUNT", mapItem.get("AMOUNT"));
				collectValue.put("PAY_CHANNEL_ID", payChannelId);
				collectValue.put("PAY_WAY", payWay);
				collectValue.put("COLLECT_TYPE", collectType);
				collectValue.put("TRANS_DETAIL_IDS", transId);
				collectValue.put("CHANNEL_INFO", channelCode);
				
				logger.debug(">>> >>> 【创建信息】 collectKey:" + collectKey);
			}
			collectMap.put(collectKey, collectValue);
		}
		logger.info("<<< <<< <<< 结束  获取‘汇总金额’");
		return collectMap;
	}
	/**
	 * 编辑SettleCollectRule查询finAccountId
	 * @param settleCollectRule
	 * @return
	 */
	private Map<String, String> getFinAccountIdByCollectRule(String rootInstCd, String channelCode, String agreementCode, SettleCollectRule settleCollectRule) throws Exception {
		logger.info(">>> >>> >>> 开始:编辑SettleCollectRule查询finAccountId");
		Map<String, String> finAccountIdMap = new HashMap<String, String>();
		String accountName1 = null;		//借记科目
		String accountName2 = null;		//贷记科目
		String finAccountId1 = null;	//借记科目finAccountId
		String finAccountId2 = null;	//贷记科目finAccountId
		String subjectCode1 = null;		//借记科目号
		String subjectCode2 = null;		//贷记科目号
		finAccountId1 = subjectCode1 = settleCollectRule.getFinAccountId1();
		finAccountId2 = subjectCode2 = settleCollectRule.getFinAccountId2();
		accountName1 = settleCollectRule.getAccountName1();
		accountName2 = settleCollectRule.getAccountName2();
		
		if(!Arrays.asList(FINAL_SUBJECT_NAME_ARR).contains(accountName1)) {
			finAccountId1 = this.getFinAccountId(subjectCode1, rootInstCd, DEFAULT_CURRENCY_CNY, channelCode, agreementCode);
			logger.info(">>> >>>　调会计接口查询finAccountId1:" + finAccountId1);
			if(StringUtils.isEmpty(finAccountId1)) throw new Exception("finAccountId1 is Empty Exception");
		} else {
			logger.info(">>> >>>　采用规则中的finAccountId1:" + finAccountId1);
		}
		if(!Arrays.asList(FINAL_SUBJECT_NAME_ARR).contains(accountName2)) {
			finAccountId2 = this.getFinAccountId(subjectCode2, rootInstCd, DEFAULT_CURRENCY_CNY, channelCode, agreementCode);
			logger.info(">>> >>>　调会计接口查询finAccountId2:" + finAccountId2);
			if(StringUtils.isEmpty(finAccountId2)) throw new Exception("finAccountId2 is Empty Exception");
		} else {
			logger.info(">>> >>>　采用规则中的finAccountId2:" + finAccountId2);
		}
		finAccountIdMap.put(subjectCode1, finAccountId1);
		finAccountIdMap.put(subjectCode2, finAccountId2);
		
		logger.info("<<< <<< <<< 结束:编辑SettleCollectRule查询finAccountId");
		return finAccountIdMap;
	}
	/**
	 * 会计接口插叙AccountId
	 * @param subjectCode 	科目号
	 * @param rootInstCd 	机构号
	 * @param currency 		币种
	 * @param channelCode 	渠道
	 * @param agreementCode 协议 
	 */
	private String getFinAccountId(String subjectCode, String rootInstCd, String currency, String channelCode, String agreementCode) throws Exception {
		logger.info(">>> >>> >>> 开始:调用会计接口financeAccountService查询FinAccountId,封装前的参数subjectCode="+subjectCode+",rootInstCd="+rootInstCd+",channelCode="+channelCode+",currency="+currency+",agreementCode="+agreementCode);
		String code  = null;	//结果编码
		String msg  = null;		//结果信息
		CommonResponse commonRes = null;
		ModifyFinAccountVo params = new ModifyFinAccountVo();
		params.setSubjectCode(subjectCode);
		params.setRootInstCd(rootInstCd);
		params.setChannelCode(channelCode);
		params.setCurrency(currency);
		params.setAgreementCode(agreementCode);
		try{
			commonRes = financeAccountService.getFinAccountId(params);
		}catch(Exception e){
			logger.error(">>> >>> getFinAccountId方法发生异常!", e);
			throw e;
		}
		if(commonRes == null) {
			logger.info(">>> >>> 失败, 会计返回的CommonResponse为null");
			return null;
		}
		code = commonRes.getCode();
		msg = commonRes.getMsg();
		if(FINACCOUNTID_SUCCESS_CODE.equals(code)){//成功
			logger.info("<<< <<< <<< 结束:调用会计接口financeAccountService查询FinAccountId 成功, 会计返回code="+code+",msg="+msg);
			return msg;
		}else{
			logger.info("<<< <<< <<< 结束:调用会计接口financeAccountService查询FinAccountId 失败, 会计返回code="+code+",msg="+msg);
			return null;
		}
	}
	/**
	 * 发送ActiveMQ
	 * @param settleNo
	 * @param tesList
	 * @param ske
	 * @param verifySign
	 * @param mqKey
	 * @return
	 */
	private SettleKernelEntry activemqSender(String settleNo, List<TransEntrySa> tesList, SettleKernelEntry ske, String verifySign, String mqKey) {
		//mq发送返回值
		ReturnObject<String> returnObject = null;
		//mq是否执行成功
		Boolean isSucc = null;
		//错误信息
		String errorMessage = null;
		//异常信息
		String excptionMessage = null;
		String retMessage = null;
		//汇总结果 状态 -1 金额 0不发送, 0失败, 1成功
		Integer statusId = ske.getStatusId();
//		RegisterFalut rf = this.createRegisterFalut(settleNo, ske);
		String kernelEntryJSONStr = this.map4MQ2JsonObj(tesList, verifySign);

		try {
			returnObject = activemqSenderService.sendQueueSingle(mqKey, kernelEntryJSONStr);
			isSucc = returnObject.isSuccFlag();
			statusId = isSucc ? 1 : 0;
			errorMessage = returnObject.getErrorMessage();
			retMessage = returnObject.getRetMessage();
			ske.setStatusId(statusId);
			ske.setRemark2(errorMessage);
			ske.setRsMsg(retMessage);
			logger.info(">>> >>> 发送ActiveMQ[mqKey:"+ mqKey +"], 返回值 [isSucc:'"+isSucc+"', errorMessage:'"+errorMessage+"', retMessage:'"+retMessage+"']");
		} catch (Exception e) {
			e.printStackTrace();
			ske.setStatusId(0);
			excptionMessage = e.getMessage().length() > 64 ? e.getMessage().substring(0, 63) + "..." : e.getMessage();
			ske.setRemark2(excptionMessage);
			logger.error(">>> >>> 发送ActiveMQ异常:", e);
		}
		return ske;
	}
	/**
	 * 查询渠道名称和编码的对应关系
	 * @return
	 */
	private Map<String, String> getPayChannelIdTitleMap() throws Exception {
		logger.info(">>> >>> >>> 开始:查询渠道名称和编码的对应关系");
		Map<String, String> payChannelIdTitleMap = new HashMap<String, String>();
		SettleParameterInfoQuery query = new SettleParameterInfoQuery();
		query.setParameterType(SettleConstants.PARAMETER_TYPE_PAY_CHANNEL);
		List<SettleParameterInfo> parameterInfoList = null;
		try {
			parameterInfoList = settleParameterInfoManager.queryList(query);
		} catch (Exception e) {
			logger.error(">>> >>> 异常:查询渠道名称和编码的对应关系", e);
		}
		if(parameterInfoList == null || parameterInfoList.size() < 1) {
			logger.error(">>> >>> 失败:查询渠道名称和编码的对应关系为0");
			throw new Exception("parameterInfoList is null || parameterInfoList.size() < 1 Exception");
		}
		for(SettleParameterInfo spi : parameterInfoList) {
			payChannelIdTitleMap.put(spi.getParameterCode(), spi.getParameterValue());
		}
		logger.info("<<< <<< <<< 结束:查询渠道名称和编码的对应关系");
		return payChannelIdTitleMap;
	}
	/**
	 * 开始查询 activeMQ的key
	 * @param parameterCode
	 * @return
	 * @throws Exception
	 */
	private String getCollectMqKey(String parameterCode) throws Exception {
		logger.info(">>> >>> >>> 开始:查询 activeMQ的key");
		String mqKey = null;
		SettleParameterInfoQuery query = new SettleParameterInfoQuery();
		query.setParameterType(SettleConstants.PARAMETER_TYPE_COLLECT_MQKEY);
		query.setParameterCode(parameterCode);
		List<SettleParameterInfo> settleParameterInfoList = null;
		try {
			settleParameterInfoList = settleParameterInfoManager.queryList(query);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("异常:查询 activeMQ的key", e);
			throw e;
		}
		if(settleParameterInfoList == null || settleParameterInfoList.size() < 1) {
			logger.error("异常:查询 activeMQ的key, settleParameterInfoList is null or settleParameterInfoList.size() < 1");
			throw new Exception("settleParameterInfoList is null or settleParameterInfoList.size() < 1");
		}
		mqKey = settleParameterInfoList.get(0).getParameterValue();
		logger.info("<<< <<< <<< 结束:查询 activeMQ的key为" + mqKey);
		return mqKey;
	}
}
