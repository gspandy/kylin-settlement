package com.rkylin.settle.logic;


import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rkylin.settle.constant.SettleConstants;
import com.rkylin.settle.manager.SettleCollectRuleManager;
import com.rkylin.settle.manager.SettleKernelEntryManager;
import com.rkylin.settle.manager.SettleTransDetailManager;
import com.rkylin.settle.manager.SettlementManager;
import com.rkylin.settle.pojo.SettleCollectRule;
import com.rkylin.settle.pojo.SettleCollectRuleQuery;
import com.rkylin.settle.pojo.SettleKernelEntry;
import com.rkylin.settle.pojo.SettleTransDetailQuery;
import com.rkylin.settle.util.SettlementUtil;

/***
 * 清结算账户一期汇总业务逻辑
 * @author Yang
 */
@Component("collectForAccLogic")
public class CollectForAccLogic extends BasicLogic {
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
	
	/**
	 * 汇总账户一期交易并记账
	 * @param collectType
	 */
	public void doCollect(Integer collectType, Date accountDate) throws Exception {
		logger.info(">>> >>> >>> >>> 开始:汇总账户一期交易并记账 [汇总类型:'"+ collectType +"']");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		//汇总交易MAP
		Map<String, Map<String, Object>> collectMap = null;
		//汇总Key对应的Value
		Map<String, Object> collectValue = null;
		//汇总信息List 用于持久化
		List<SettleKernelEntry> skeList = new ArrayList<SettleKernelEntry>();
		//汇总规则
		List<SettleCollectRule> settleCollectRuleList = null;
		//汇总规则实体
		SettleCollectRule srcItem = null;
		//汇总信息实体
		SettleKernelEntry ske = null;
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
		//汇总规则迭代器
		Iterator<SettleCollectRule> settleCollectRuleIter = null;
		/*
		 * 获取汇总规则
		 */
		SettleCollectRuleQuery query = new SettleCollectRuleQuery();
		query.setCollectType(collectType);
		query.setStatusId(1);
		settleCollectRuleList = settleCollectRuleManager.queryList(query);
		if(settleCollectRuleList == null || settleCollectRuleList.size() < 1) {
			logger.info("<<< <<< <<< <<< 结束:汇总账户一期交易并记账 [汇总类型:'"+ collectType +"'] settleCollectRuleList is Null or settleCollectRuleList.size < 1");
			return;
		}
		settleCollectRuleIter = settleCollectRuleList.iterator();
		//汇总信息集合
		List<Map<String, Object>> relationList = new ArrayList<Map<String, Object>>();
		Map<String, Object> relationMap = null;
		//判断日中是否正常结束
//		if(!settlementUtil.cutDayIsSuccess4Account()) {//
//			//获取紧急联系人手机号
//			String mobile = settlementUtil.getEmergencyContactMobile();
//			//短信模板: xx系统xx环境，编号××存在问题，请及时解决!
//			//编号3会计系统,系统日切失败
//			String content = "清算系统测试环境，编号"+Constants.SMS_NUMBER_3+"存在问题，请及时解决！";
//			//给相关人员发送短信
//			SendSMS.sendSMS(mobile, content);
//			throw new Exception("判断日中是否正常结束异常");
//		}
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
				collectMap = this.getCollectAmount(accountDate, srcItem);
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
						srcItem.getAccountName1(),  
						sumAmount,
						referEntryId, 
						accountDate, 
						String.valueOf(collectType), 
						srcItem.getRootInstCd());
						//汇总金额小于等于 0, 不发送ActiveMQ
						ske.setStatusId(-1);//汇总金额为 0, 不发送ActiveMQ
						ske.setRemark2("【汇总金额为 0】, 不发送ActiveMQ");
						logger.info(">>> >>> 【汇总金额为 0】, 不发送ActiveMQ ");
				ske.setObligate1(srcItem.getProfitRuleName());
				ske.setMerchantNo(srcItem.getAccountName2());
				/*
				 * 插入到 汇总信息List 并持久化 
				 */
				skeList.add(ske);
				logger.info("<<< <<< << 汇总结束:" + "【机构--" + srcItem.getRootInstCd() + "】，" + "【渠道--" + srcItem.getPayChannelId() + "】，" + "【funcCode--" + srcItem.getFuncCode() + "】");	
			} else {
				String remark = "";
				String settleDay = null;
				for(String collectKey : collectMap.keySet()) {
					collectValue = collectMap.get(collectKey);
					//全局流水号
					transId = this.genTimestampAndRandom();
					referEntryId = settleNo = "QJ" + transId;
					//计算汇总金额
					sumAmount = Long.parseLong(String.valueOf(collectValue.get("AMOUNT")));
					//交易账期
					settleDay = String.valueOf(collectValue.get("ACCOUNT_DATE"));
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
							srcItem.getFinAccountId1(), 
							srcItem.getAccountName1(), 
							sumAmount,
							referEntryId, 
							sdf.parse(settleDay), 
							String.valueOf(collectType), 
							srcItem.getRootInstCd());
					ske.setObligate1(srcItem.getProfitRuleName());
					ske.setMerchantNo(srcItem.getAccountName2());
					if(sumAmount > 0L) {//汇总金额大于 0, 调用账户调账
						/*
						 * 发送账户调账
						 */
						ske = this.changeAccount(ske);
					} else {//汇总金额小于等于 0, 不发送ActiveMQ
						remark = "";
						if(sumAmount <= 0) remark = "【汇总金额为 0】";
						remark += ", 不发送账户调账";
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
		logger.info("<<< <<< <<< <<< 结束:汇总账户一期交易并记账  [账期:'"+ new SimpleDateFormat("yyyy-MM-dd").format(accountDate) +"'],[汇总类型:'"+ collectType +"']");
	}
	/**
	 * 发送汇总交易记账
	 * @param ske
	 * @throws Exception
	 */
	public Map<String, String> doCollectBySettleKernelEntry(Long id) throws Exception {
		//返回值
		Map<String, String> resultMap = new HashMap<String, String>();
		//获取汇总信息通过ID
		SettleKernelEntry ske = settleKernelEntryManager.findSettleKernelEntryById(id);
		//汇总信息List 用于持久化
		List<SettleKernelEntry> skeList = null;
		//汇总结果 状态 -1 金额 0不发送, 0失败, 1成功
		Integer statusId = ske.getStatusId();
		//汇总金额
		Long sumAmount = ske.getPaymentAmount1();
		
		//已经汇总并发送成功的交易不能重复发送
		if(statusId == 1) {
			logger.info(">>> >>> 已经汇总并发送成功的交易不能重复发送!");
			resultMap.put("code", "-1");
			resultMap.put("msg", "已经汇总并发送成功的交易不能重复发送!");
			return resultMap;
		}
		
		//汇总金额 <= 0
		if(sumAmount <= 0L) {
			logger.info(">>> >>> 汇总金额 <= 0");
			resultMap.put("code", "-1");
			resultMap.put("msg", "汇总金额 <= 0");
			return resultMap;
		}
		
		//汇总金额大于 0 , 调用账户记账
		ske = this.changeAccount(ske);
		
		//修改SettleKernelEntry汇总结果
		skeList = new ArrayList<SettleKernelEntry>();
		skeList.add(ske);
		try {
			this.insertAndUpdateSettleKernelEntry(skeList);
		} catch (Exception e) {
			logger.info(">>> >>> 异常:修改SettleKernelEntry汇总结果", e);
			resultMap.put("code", "-1");
			resultMap.put("msg", "修改SettleKernelEntry汇总结果异常");
			return resultMap;
		}
		
		resultMap.put("code", "1");
		resultMap.put("msg", "成功!");
		return resultMap;
	}
	/**
	 * 获取‘汇总金额’
	 * @param accountDate
	 * @param rootInstCd
	 * @param payChannelId
	 * @param funcCode
	 * @return
	 * @throws Exception
	 */
	private Map<String, Map<String, Object>> getCollectAmount(Date accountDate, SettleCollectRule settleCollectRule) throws Exception {
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
		//规则类型
		Integer collectType = settleCollectRule.getCollectType();
		//交易类型
		String funcCode = settleCollectRule.getFuncCode();
		//机构号
		String merchantCode = settleCollectRule.getRootInstCd();
		if(SettleConstants.COLLECT_TYPE_3 == collectType) {//账户一期记账 账户一期数据
			query.setMerchantCode(merchantCode);
			query.setAccountDate(accountDate);
			query.setDealProductCode(funcCode);
			query.setOrderType(0);
			query.setDataFrom(0);
			query.setStatusId(21);
			query.setTransType(collectType);
			/*
			 * 查询待清算交易 
			 */
			list = settleTransDetailManager.collectAmountByExample(query);
		} else if(SettleConstants.COLLECT_TYPE_4 == collectType) {//账户一期记账 多渠道数据
			query.setMerchantCode(merchantCode);
			query.setAccountDate(accountDate);
			query.setFuncCode(funcCode);
			query.setOrderType(1);
			query.setDataFrom(3);
			query.setStatusId(21);
			query.setTransType(collectType);
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
		//交易金额ID和账期的映射
		Map<String, Object> mapItem = null;
		//计算金额
		BigDecimal addamt = null;
		BigDecimal settleamt = null;
		//记录交易主键
		String transId = null;
		//遍历 查询到的全部汇总交易
		for(int i = 0; i < list.size(); i++) {
			//当前交易映射
			mapItem = list.get(i);
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
				collectValue.put("COLLECT_TYPE", collectType);
				collectValue.put("TRANS_DETAIL_IDS", transId);
				collectValue.put("USER_ID", settleCollectRule.getFinAccountId1());
				collectValue.put("PRODUCT_ID", settleCollectRule.getAccountName1());
				collectValue.put("KERNEL_FUNC_CODE", settleCollectRule.getKernelFuncCode());
				collectValue.put("COLLECT_ID", settleCollectRule.getId());
				collectValue.put("COLLECT_NAME", settleCollectRule.getProfitRuleName());
				logger.debug(">>> >>> 【创建信息】 collectKey:" + collectKey);
			}
			collectMap.put(collectKey, collectValue);
		}
		logger.info("<<< <<< <<< 结束  获取‘汇总金额’");
		return collectMap;
	}
	/**
	 * 调用账户一期调单边账接口记账
	 * @param sumTransMap
	 */
	private SettleKernelEntry changeAccount(SettleKernelEntry ske) {
		logger.info(">>> >>> >>> 开始:调用账户一期调单边账接口记账");
		SimpleDateFormat ymdIdformat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		Map<String, Object> changeAccountResultMap = null;
		String message = null;
		
		try {
			changeAccountResultMap = this.changeAccount(
					ske.getMerchantNo(),
					ske.getFinAccountId1(),
					ske.getFinAccountId2(),
					ske.getPaymentAmount1(),
					ske.getFuncCode(),//4410+ or 4411-
					"Settle" + ske.getRemark1() + "_" + ymdIdformat.format(new Date()),
					ske.getObligate1());
		} catch (Exception e) {
			ske.setStatusId(0);
			ske.setRsMsg(changeAccountResultMap != null 
					? String.valueOf(changeAccountResultMap.get("msg"))
					: "changeAccountResultMap is Null");
			ske.setRemark2("调账到账户【异常】");
			logger.error("调账到账户【异常】:" + message, e);
			return ske;
		}
		if(changeAccountResultMap == null || !"1".equals(changeAccountResultMap.get("code").toString())) {//失败
			ske.setStatusId(0);
			ske.setRsMsg(changeAccountResultMap != null 
					? String.valueOf(changeAccountResultMap.get("msg")) 
					: "changeAccountResultMap is Null");
			ske.setRemark2("调账到账户【失败】");
			logger.error("调账到账户【失败】:" + message);
		} else if("1".equals(changeAccountResultMap.get("code").toString())) {//成功
			ske.setStatusId(1);
			ske.setRsMsg(String.valueOf(changeAccountResultMap.get("msg")));
			ske.setRemark2("调账到账户【成功】");
			logger.error("调账到账户【成功】:" + message);
		}
		logger.info("<<< <<< <<< 结束:调用账户一期调单边账接口记账");
		return ske;
	}
}
