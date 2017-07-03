package com.rkylin.settle.logic;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rkylin.settle.constant.SettleConstants;
import com.rkylin.settle.constant.TransCodeConst;
import com.rkylin.settle.exception.SettleException;
import com.rkylin.settle.manager.SettleBalanceEntryManager;
import com.rkylin.settle.manager.SettleParameterInfoManager;
import com.rkylin.settle.manager.SettleRuleManager;
import com.rkylin.settle.manager.SettleTransAccountManager;
import com.rkylin.settle.manager.SettleTransDetailManager;
import com.rkylin.settle.manager.TransDetailInfoManager;
import com.rkylin.settle.pojo.SettleBalanceEntry;
import com.rkylin.settle.pojo.SettleParameterInfo;
import com.rkylin.settle.pojo.SettleParameterInfoQuery;
import com.rkylin.settle.pojo.SettleProfitKey;
import com.rkylin.settle.pojo.SettleProfitRule;
import com.rkylin.settle.pojo.SettleRule;
import com.rkylin.settle.pojo.SettleRuleQuery;
import com.rkylin.settle.pojo.SettleTransAccountQuery;
import com.rkylin.settle.pojo.SettleTransDetail;
import com.rkylin.settle.pojo.SettleTransDetailQuery;
import com.rkylin.settle.pojo.TransDetailInfo;
import com.rkylin.settle.util.LogicConstantUtil;
import com.rkylin.settle.util.SettlementUtil;



/***
 * 清分系统通用对账业务逻辑
 * @author Yang
 */
@Component("collateLogic")
public class CollateLogic extends BasicLogic {
	@Autowired
	private SettleRuleManager settleRuleManager;					//对账规则Manager
	@Autowired
	private SettleBalanceEntryManager settleBalanceEntryManager;	//对账结果Manager
	@Autowired
	private SettleTransDetailManager settleTransDetailManager;		//'清算'交易信息Manager
	@Autowired
	private SettleTransAccountManager settleTransAccountManager;	//'上游渠道'交易信息Manager
	@Autowired
	private SettlementUtil settlementUtil;							//清算工具类
	@Autowired
	private TransDetailInfoManager transDetailInfoManager;			//'多渠道'交易明细Manager
	@Autowired
	private SettleParameterInfoManager settleParameterInfoManager;	//常量表
	@Autowired
	private LogicConstantUtil logicConstantUtil;					//逻辑常量工具类
	/***
	 * 对账
	 * @param tAccountList	上游渠道交易信息
	 * @param tDetailList	清算交易信息
	 * @param payChannelId	上游渠道ID	例如:通联01, 支付宝02
	 * @param collateType	上游交易类型 例如:代收付1, 网关支付2
	 * @return 提示信息 {code: 结果编号, msg: 运行结果信息, failOrderNo: 未平账目订单号, failOrderMsg: 未平账目信息}
	 */
	public synchronized Map<String, Object> doCollate(List<Map<String, Object>> tAccountList, List<Map<String, Object>> tDetailList, String payChannelId, String readType) throws Exception {
		logger.info(">>> >>> >>> >>> 进入'上游渠道'和'清算'交易信息对账 ... ...");
		//提示信息
		Map<String, Object> resultMap = new HashMap<String, Object>();
		//对账结果List, 暂存对账结果信息
		List<SettleBalanceEntry> settleBalanceEntryList = new ArrayList<SettleBalanceEntry>();
		//错账订单号
		List<String> failOrderNo = new ArrayList<String>();
		//错账订单提示信息 key为订单号
		Map<String, String> failOrderMsg = new HashMap<String, String>();
		//对账规则 对账key[keys] & 对账项[params]
		Map<String, List<String>> keysAndParams = null;
		//对账状态列表, 用于 update ‘上游渠道’&‘清算’交易信息对账状态 [备注 {detId:清算交易ID, accId:上游交易ID, statusId:对账状态}]
		List<Map<String, Object>> colStatInfoList = new ArrayList<Map<String, Object>>();
		//声明对账结果对象
		SettleBalanceEntry settleBalanceEntry = null;
		try {
			//获取对账规则, 通过上游渠道ID获取对账规则
			keysAndParams = this.getRule(payChannelId, readType);
			if(keysAndParams == null || keysAndParams.size() <= 0) {
				logger.info(">>> 获取对账规则  0条记录");
				return this.editResultMap(resultMap, "0", "获取 对账规则  0条记录");
			}
		} catch (Exception e) {
			logger.error(">>> 获取 对账规则  异常");
			logger.error(">>> 异常信息: " + e.getMessage());
			e.printStackTrace();
			return this.editResultMap(resultMap, "-1", "获取 对账规则 异常");
		}
		//上游渠道对账key
		List<String> accKeys = keysAndParams.get("accKeys");
		//清算方对账key
		List<String> detKeys = keysAndParams.get("detKeys");
		//上游渠道对账项param
		List<String> accParams = keysAndParams.get("accParams");
		//清算方对账项param
		List<String> detParams = keysAndParams.get("detParams");
		
		//上游渠道交易信息-对账结构
		Map<String, Map<String, Object>> accountMapStru = null;
		try {
			//把入参tAccountList[上游渠道交易信息]编辑成适合对账业务的'对账结构'
			accountMapStru = this.editCollateStructure(tAccountList, accKeys);//上游渠道对账结构 type=1
			if(accountMapStru == null || accountMapStru.size() <= 0) {
				logger.info(">>> 获取 上游渠道交易信息-对账结构 0条记录");
//				return this.editResultMap(resultMap, "0", "获取 上游渠道交易信息-对账结构 0条记录");
			}
		} catch (Exception e) {
			logger.error(">>> 获取 上游渠道交易信息-对账结构 异常");
			logger.error(">>> 异常信息: " + e.getMessage());
			e.printStackTrace();
			return this.editResultMap(resultMap, "-1", "获取 上游渠道交易信息-对账结构 异常");
		}
		//清算交易信息-对账结构
		Map<String, Map<String, Object>> detailMapStru = null;
		try {
			//把入参tDetailList[清算交易信息]编辑成适合对账业务的'对账结构'
			detailMapStru = this.editCollateStructure(tDetailList, detKeys);//清算对账结构 type=2
			if(detailMapStru == null || detailMapStru.size() <= 0) {
				logger.info(">>> 获取 清算交易信息-对账结构 0条记录");
//				return this.editResultMap(resultMap, "0", "获取 清算交易信息-对账结构 0条记录");
			}
		} catch (Exception e) {
			logger.error(">>> 获取 清算交易信息-对账结构 异常");
			logger.error(">>> 异常信息: ", e);
			e.printStackTrace();
			return this.editResultMap(resultMap, "-1", "获取 清算交易信息-对账结构 异常");
		}
		
		logger.info(">>> >>> >>> 对账开始 ... ...");
		for(String ruleKey : detailMapStru.keySet()) {//遍历'清算'方对账结构体
			//实例化对账结果对象
			settleBalanceEntry = new SettleBalanceEntry();
			//对账结果存储detail表和account表的主键, 便于画面逆向查询
			String detIdAndAccId;
			//获取账目
			Map<String, Object> detailMap = detailMapStru.get(ruleKey);
			//获取订单号[备注:大多数情况下,订单号是账目的唯一标示,所以我们要进行记录]
			String orderNo = String.valueOf(detailMap.get("ORDER_NO"));
			//外部对账1, 内部对账账户&多渠道2
			settleBalanceEntry.setBalanceType(1);
			//判断'上游渠道'所有的对账key中,是否包含'清算'的对账key
			if(accountMapStru.containsKey(ruleKey)) {//[我有, 他也有]
				logger.info(">>> >>> 双方都有[orderNo:"+ ruleKey +"]此订单");
				Map<String, Object> accountMap = accountMapStru.get(ruleKey);
				detIdAndAccId = detailMap.get("TRANS_DETAIL_ID") + "#" + accountMap.get("TRANS_ACCOUNT_ID");
				settleBalanceEntry.setOrderNo(orderNo);
				settleBalanceEntry.setObligate1(ruleKey);
				settleBalanceEntry.setObligate2(detIdAndAccId);
				/**
				 * 判断我方交易状态,如果是成功的交易就正常对账.
				 * 否则标记为特殊长款,即是:上游包含清算失败的交易,视为'长款'!
				 */
				//当交易状态为: 清分成功, 对账失败 异常, 对账错误 错账, 上游渠道没有数据 咱们有  短款这几个状态是 继续对账
				boolean isCollater = String.valueOf(SettleConstants.STATUS_PROFIT_SUCCESS).equals(String.valueOf(detailMap.get("STATUS_ID")))
							|| String.valueOf(SettleConstants.STATUS_COLLATE_ERROR).equals(String.valueOf(detailMap.get("STATUS_ID")))
							|| String.valueOf(SettleConstants.STATUS_COLLATE_FIALD).equals(String.valueOf(detailMap.get("STATUS_ID")))
							|| String.valueOf(SettleConstants.STATUS_COLLATE_LOSS).equals(String.valueOf(detailMap.get("STATUS_ID")));
				if(isCollater) {//我方成功交易处理
					logger.info(">>> >>> 生效[orderNo:"+ ruleKey +"]");
					//平账标记, 所有对账项都对平即是true,有某一项未平,表示错账即是false
					boolean allRight = true;
					//核对账目时的错误项
					String failParams = "";
					//遍历对账项进行核对... ...
					for(int i = 0; i< accParams.size(); i++) {
						if(!String.valueOf(detailMap.get(detParams.get(i))).equals(String.valueOf(accountMap.get(accParams.get(i))))) {
							allRight = false;
							failParams += "," + "{"+ detParams.get(i) +" vs "+ accParams.get(i) +"}";
						}
					}
					
					if(allRight) {//平账
						logger.info(">>>  平账: [orderNo:"+ ruleKey +"]");
						settleBalanceEntry.setStatusId(SettleConstants.COLLATE_STU_1);
						settleBalanceEntry.setRemark("平账");
						
						//用于 update ‘上游’&‘清算’交易表
						Map<String, Object> colStatInfoMap = new HashMap<String, Object>();
						colStatInfoMap.put("detId", detailMap.get("TRANS_DETAIL_ID"));
						colStatInfoMap.put("accId", accountMap.get("TRANS_ACCOUNT_ID"));
						colStatInfoMap.put("statusId", SettleConstants.STATUS_COLLATE_SUCCESS);
						colStatInfoList.add(colStatInfoMap);
					} else {//错账
						failParams = failParams.substring(1);//干掉拼串
						logger.info(">>>  错账: [orderNo:"+ ruleKey +"]");
						failOrderNo.add(ruleKey);
						failOrderMsg.put(ruleKey,"错账项目:" + "错账, 未平项目:" + failParams);
						settleBalanceEntry.setStatusId(SettleConstants.COLLATE_STU_0);
						settleBalanceEntry.setRemark("错账: 未平项目:" + failParams);
						
						//用于 update ‘上游’&‘清算’交易表
						Map<String, Object> colStatInfoMap = new HashMap<String, Object>();
						colStatInfoMap.put("detId", detailMap.get("TRANS_DETAIL_ID"));
						colStatInfoMap.put("accId", accountMap.get("TRANS_ACCOUNT_ID"));
						colStatInfoMap.put("statusId", SettleConstants.STATUS_COLLATE_FIALD);
						colStatInfoList.add(colStatInfoMap);
					}
				} else {//我方失败交易处理
					logger.info(">>> >>> 无效[orderNo:"+ ruleKey +"]");
					logger.info(">>>  长款(特殊): [orderNo:"+ ruleKey +"] 因'清算'方的交易状态为'失败'导致对账结果为'长款'");
					failOrderNo.add(ruleKey);
					failOrderMsg.put(ruleKey,"因清算方的交易状态为'失败'导致对账结果为'长款'");
					settleBalanceEntry.setStatusId(SettleConstants.COLLATE_STU_2);
					settleBalanceEntry.setRemark("长款(特殊): 因'清算'方的交易状态为'失败'导致对账结果为'长款'");
					
					//用于 update ‘上游’&‘清算’交易表
					Map<String, Object> colStatInfoMap = new HashMap<String, Object>();
					colStatInfoMap.put("detId", detailMap.get("TRANS_DETAIL_ID"));
					colStatInfoMap.put("accId", accountMap.get("TRANS_ACCOUNT_ID"));
					colStatInfoMap.put("statusId", SettleConstants.STATUS_COLLATE_GAIN);
					colStatInfoList.add(colStatInfoMap);
				}
				//记录'清算'账期
				settleBalanceEntry.setAccountDate((Date) detailMap.get("ACCOUNT_DATE"));
				//填入对账结果List
				settleBalanceEntryList.add(settleBalanceEntry);
			} else {//[我有, 他没有] 需要比较双方交易时间判断是否是 短款
				/**
				 * '上游渠道'不包含'清算'的交易 ,原因有二
				 * 1.程序或网络原因导致数据不对称, 对账结果标记为'长款'
				 * 2.'清算'的交易发生在'上游渠道'对账文件产生之后.本次对账不予考虑,此账目会在下次对账触发时进行核对
				 * 所以程序需要判断双方交易交易的时间
				 */
				Long detailOrderDateTimes = ((Date) detailMap.get("REQUEST_TIME")).getTime();
				Long maxtAccountTimeTimes = this.getMaxtAccountTimeStr(tAccountList);

				if(detailOrderDateTimes <= maxtAccountTimeTimes) {//如果'清算交易时间'早于'上游渠道交易时间'
					logger.info(">>> >>> 清算'有', 上游渠道'无'[orderNo:"+ ruleKey +"]此订单");
					logger.info(">>>  短款: [orderNo:"+ ruleKey +"] 因'上游渠道'对账信息不包含'清算'对账信息");
					detIdAndAccId = detailMap.get("TRANS_DETAIL_ID") + "#";
					failOrderNo.add(ruleKey);
					failOrderMsg.put(ruleKey,"因清算方的交易状态为'失败'导致对账结果为'长款'");
					settleBalanceEntry.setOrderNo(orderNo);
					settleBalanceEntry.setObligate1(ruleKey);
					settleBalanceEntry.setObligate2(detIdAndAccId);
					settleBalanceEntry.setStatusId(SettleConstants.COLLATE_STU_3);
					settleBalanceEntry.setRemark("短款: 因'上游渠道'对账信息不包含'清算'对账信息");
					//记录'清算'账期
					settleBalanceEntry.setAccountDate((Date) detailMap.get("ACCOUNT_DATE"));
					settleBalanceEntryList.add(settleBalanceEntry);
					
					//用于 update ‘上游’&‘清算’交易表
					Map<String, Object> colStatInfoMap = new HashMap<String, Object>();
					colStatInfoMap.put("detId", detailMap.get("TRANS_DETAIL_ID"));
					colStatInfoMap.put("statusId", SettleConstants.STATUS_COLLATE_LOSS);
					colStatInfoList.add(colStatInfoMap);
				} else {
					logger.info(">>> >>> 未对账  此单交易[orderNo:"+ orderNo +"] 因'清算'交易时间  > '上游渠道'交易, 所以不参与此次校对");
					logger.info(">>> 因此单'清算'交易发生在'上游渠道'生成对账信息之后, 故不参与此次对帐!");
				}
			}
		}
		logger.info(">>> >>> >>> 对账结束 ... ...");
		
		//'清算'交易信息中不包含的'上游渠道'交易信息  [他有, 我没有]
		Set<String> accountKeySet = accountMapStru.keySet();		//'清算'对账key集合
		Set<String> detailKeySet = detailMapStru.keySet();			//'上游渠道'对账key集合
		accountKeySet.removeAll(detailKeySet);						//'清算'交易信息中不包含的'上游渠道'交易信息  [他有, 我没有] 对账key集合
		for(String ruleKey : accountKeySet) {
			Map<String, Object> map = accountMapStru.get(ruleKey);
			String detIdAndAccId = "#" + map.get("TRANS_ACCOUNT_ID");
			settleBalanceEntry = new SettleBalanceEntry();
			logger.info(">>> >>> >>> '清算'交易信息中 不包含的 '上游渠道'交易信息 ['上游'有, '清算'没有的交易] [orderNo:"+ ruleKey +"]");
			failOrderNo.add(ruleKey);
			failOrderMsg.put(ruleKey,"长款: '清算'交易信息中 不包含的 '上游渠道'交易信息 ['上游'有, '清算'没有的交易]");
			settleBalanceEntry.setBalanceType(1);
			settleBalanceEntry.setOrderNo(String.valueOf(map.get("ORDER_NO")));
			settleBalanceEntry.setObligate1(ruleKey);
			settleBalanceEntry.setObligate2(detIdAndAccId);
			settleBalanceEntry.setStatusId(SettleConstants.COLLATE_STU_2);
			settleBalanceEntry.setRemark("长款: '清算'交易信息中 不包含的 '上游渠道'交易信息 ['上游'有, '清算'没有的交易]");
			//记录'上游渠道'账期
			settleBalanceEntry.setAccountDate((Date) map.get("SETTLE_TIME"));
			settleBalanceEntryList.add(settleBalanceEntry);
			
			//用于 update ‘上游’&‘清算’交易表
			Map<String, Object> colStatInfoMap = new HashMap<String, Object>();
			colStatInfoMap.put("accId", map.get("TRANS_ACCOUNT_ID"));
			colStatInfoMap.put("statusId", SettleConstants.STATUS_COLLATE_GAIN);
			colStatInfoList.add(colStatInfoMap);
		}
		
		/**
		 * 将'对账结果'插入或更新至'对账结果表'
		 * 因为,支持同一批交易被多次核对[例如:手动对账]
		 * 对账结果信息对于账目来说是唯一的.
		 * 所以,做如下处理.
		 */
		super.insertAndUpdateSettleBalanceEntry(settleBalanceEntryList);
		/**
		 * 更新'清算'交易信息状态
		 * 更新'上游渠道'交易信息状态
		 */
		this.updateAccAndDetTransStatus(colStatInfoList);
		
		//封装返回值对象
		if(failOrderNo.size() > 0) {//有未对平的订单被记录
			this.editResultMap(resultMap, "2", "对账执行结束, 有["+failOrderNo.size()+"]条账目未对平", failOrderNo, failOrderMsg);
			logger.info(">>> >>> 对账执行结束, 有["+failOrderNo.size()+"]条账目未对平, 详情请看本方法的返回结果或查看db对账结果中REMARK信息");
		} else {//爽歪歪, 全都对平!
			this.editResultMap(resultMap, "1", "对账结束, 全都对平!");
			logger.info(">>> >>> 对账结束, 全都对平!");
		}
		
		logger.info(">>> >>> >>> >>> 执行结束  '上游渠道'和'清算'交易信息对账 ... ...");
		return resultMap;
	}
	/**
	 * 从多渠道读取交易信息并存入'清算'DB
	 * @return 提示信息
	 */
	public Map<String, Object> getTransDetailFromMultiGate() throws Exception {
		return this.getTransDetailFromMultiGate(null);
	}
	/**
	 * 从多渠道读取交易信息并存入'清算'DB
	 * @param accountDate
	 * @return 提示信息
	 */
	public Map<String, Object> getTransDetailFromMultiGate(Date accountDate) throws Exception {
		logger.info(">>> >>> >>> >>> 从多渠道读取交易信息并存入'清算'DB START <<< <<< <<< <<<");
		Map<String, Object> resultMap = new HashMap<String, Object>();							//提示信息
		List<SettleTransDetail> settleTransDetailList = new ArrayList<SettleTransDetail>();		//'清算'交易信息
		List<TransDetailInfo> transDetailInfoList = null;										//'多渠道'交易信息
		List<SettleParameterInfo> funcCodeList = null; 											//'功能编码'对应关系
		List<SettleParameterInfo> readTypeList = null;											//'交易类型'对应关系
		List<SettleParameterInfo> payChannelIdList = null;										//'上游渠道'对应关系
		/*
		 * 不参与对账的字符串
		 * 10:受理
		 * 11:发送异常
		 */
		final String unCollateStatusIdStr = "10,11";
		
		//验证账期
		if(accountDate == null) {//如果账期为null, 即是未传入账期
			//从DB中过去账期信息, 账期在DB中每日更新,获取T-1日账期  	
			accountDate = (Date) settlementUtil.getAccountDate("yyyyMMdd", -1, "Date");	
		}
		//创建'多渠道'交易查询query对象
		Map<String,Object> whereMap = new HashMap<String,Object>();
		Date beginAccDate = new Date();
		Date endAccDate = new Date();
		beginAccDate.setTime(beginAccDate.getTime() - 10 * 24 * 60 * 60 * 1000L);
		endAccDate.setTime(endAccDate.getTime() + 24 * 60 * 60 * 1000L);
		whereMap.put("createdTime_f",beginAccDate);
		whereMap.put("createdTime_t",endAccDate);
		whereMap.put("updatedTime",accountDate);
		//查询'多渠道'交易信息
		try {
			transDetailInfoList = transDetailInfoManager.selectByUpdateTime(whereMap);
			if(transDetailInfoList == null || transDetailInfoList.size() < 1) {
				String msg = "查询'多渠道'交易信息  0条";
				logger.error(">>> " + msg);
				return super.editResultMap(resultMap, "0", msg);
			}
		} catch (Exception e) {
			String msg = "异常:查询'多渠道'交易信息  异常";
			logger.error(">>> " + msg);
			e.printStackTrace();
			return super.editResultMap(resultMap, "-1", msg);
		}	
		//创建字典表query对象
		SettleParameterInfoQuery paramQuery = new SettleParameterInfoQuery();
		//查询funcCode[业务编码]和transCode[交易代码]的对应关系
		paramQuery.setParameterType(SettleConstants.FUNC_CODE_TRANS_TYPE);
		paramQuery.setStatusId(1);
		try {
			funcCodeList = settleParameterInfoManager.queryList(paramQuery);
			if(funcCodeList == null || funcCodeList.size() < 1) {
				String msg = "查询funcCode[业务编码]和channelNo[交易代码]的对应关系  0条";
				logger.error(">>> " + msg);
				return super.editResultMap(resultMap, "0", msg);
			}
		} catch (Exception e) {
			String msg = "异常:查询funcCode[业务编码]和channelNo[交易代码]的对应关系  异常";
			logger.error(">>> " + msg);
			e.printStackTrace();
			return super.editResultMap(resultMap, "-1", msg);
		}
		//查询readType[网关支付/代收付]和transCode[交易代码]的对应关系
		paramQuery.setParameterType(SettleConstants.READ_TYPE_TRANS_TYPE);
		paramQuery.setStatusId(1);
		try {
			readTypeList = settleParameterInfoManager.queryList(paramQuery);
			if(readTypeList == null || readTypeList.size() < 1) {
				String msg = "查询readType[网关支付/代收付]和channelNo[交易代码]的对应关系  0条";
				logger.error(">>> " + msg);
				return super.editResultMap(resultMap, "0", msg);
			}
		} catch (Exception e) {
			String msg = "异常:查询readType[网关支付/代收付]和channelNo[交易代码]的对应关系  异常";
			logger.error(">>> " + msg);
			e.printStackTrace();
			return super.editResultMap(resultMap, "-1", msg);
		}
		//查询'多渠道渠道商'和'清算渠道编码'的对应关系
		paramQuery.setParameterType(SettleConstants.PARAMETER_TYPE_PAY_CHANNEL);
		try {
			payChannelIdList = settleParameterInfoManager.queryList(paramQuery);
			if(payChannelIdList == null || payChannelIdList.size() < 1) {
				String msg = "查询'多渠道渠道商'和'清算渠道编码'的对应关系  0条";
				logger.error(">>> " + msg);
				return super.editResultMap(resultMap, "0", msg);
			}
		} catch (Exception e) {
			String msg = "异常:查询'多渠道渠道商'和'清算渠道编码'的对应关系  异常";
			logger.error(">>> " + msg);
			e.printStackTrace();
			return super.editResultMap(resultMap, "-1", msg);
		}
		logger.info(">>> >>> >>> 把'多渠道'交易信息封装成'清算'交易信息");
		//遍历上游交易信息
		Iterator<TransDetailInfo> iter = transDetailInfoList.iterator();
		while(iter.hasNext()) {
			//当前'多渠道'交易信息
			TransDetailInfo multiDetail = iter.next();
			//创建'清算'交易信息
			SettleTransDetail settleDetail = new SettleTransDetail();
			String requestNo = multiDetail.getRequestNo();						//请求编号	
			Date requestTime = multiDetail.getCreatedTime();					//支付时间
			String transFlowNo = multiDetail.getOrderNo();						//流水号
			String channelNo = multiDetail.getChannelNo();						//渠道业务号
			Integer payerAccountType = multiDetail.getPayerAccountType();		//帐户类型
			Date orderDate = multiDetail.getCreatedTime();						//交易创建时间
			Integer orderType = 1;												//订单类型'结算单'
			String merchantCode = multiDetail.getOrgNo();						//机构号
			String payWay = multiDetail.getOrgCode();
			Long amount = multiDetail.getPayAmount();							//金额
			Integer dataFrom = 3;												//数据来源'多渠道'
			String invoiceNo = String.valueOf(multiDetail.getTransDetailId());	//'多渠道'交易信息主键ID
			String receiverCity = multiDetail.getReceiverCity();				//银行所在城市
			String receiverBankName = multiDetail.getReceiverBankName();
			String level = "1";//默认 1:普通  多渠道系统未开发此字段, 待多渠道开发完成再做定夺.
			String channelHome = multiDetail.getChannelHome();					//渠道名称
			/*
			 * 如果不对账的状态包含此条交易的状态值
			 */
			Boolean isUnCollate = unCollateStatusIdStr.contains(String.valueOf(multiDetail.getStatusId()));
			Integer statusId = isUnCollate ? 10 : 11;							//'清算'默认状态位[未进行对账]
			//17状态为'退票',上游渠道对账文件中'退票'按'成功'处理,所以多渠道的17我们存为16同样做'成功'处理
			//String obligate1 = String.valueOf(multiDetail.getStatusId()==17 ? 16 : multiDetail.getStatusId());	//'多渠道'交易状态
			String obligate1 = null;
			String obligate2 = null;
			String remark = multiDetail.getRemark();							//备注
			if(multiDetail.getStatusId() == 17) {//交易结果为【退票】
				obligate1 = "16";												//对账文件中只发送退票前处理成功的交易
				remark = "【退票】" + remark;
			} else {
				obligate1 = String.valueOf(multiDetail.getStatusId());
			}
			//封装'清算交易信息'结构体
			settleDetail.setRequestNo(requestNo);
			settleDetail.setRequestTime(requestTime);
			settleDetail.setTransFlowNo(transFlowNo);
			settleDetail.setOrderDate(orderDate);
			settleDetail.setOrderType(orderType);
			settleDetail.setMerchantCode(merchantCode);
			settleDetail.setPayWay(payWay);
			settleDetail.setAmount(amount);
			settleDetail.setDataFrom(dataFrom);
			settleDetail.setInvoiceNo(invoiceNo);
			settleDetail.setStatusId(statusId);
			settleDetail.setReadStatusId(multiDetail.getStatusId());//存储多渠道交易状态
			settleDetail.setAccountDate(accountDate);
			settleDetail.setObligate1(obligate1);
			settleDetail.setBusinessType(channelNo);
			settleDetail.setDeliverStatusId(payerAccountType);
			settleDetail.setRemark(remark);
			settleDetail.setBankCode(receiverBankName);
			settleDetail.setObligate2(level);
			settleDetail.setObligate3(receiverCity);
			settleDetail.setChannelInfo(channelHome); 				//渠道名称
			/*
			 * 匹配funcCode
			 */
			String funcCode = "";
			Iterator<SettleParameterInfo> funcCodeParamIter = funcCodeList.iterator();
			while(funcCodeParamIter.hasNext()) {
				SettleParameterInfo param = funcCodeParamIter.next();
				String transCodes = param.getParameterValue();
				if(transCodes.contains(channelNo)) {
					funcCode = param.getParameterCode();
					break;
				}
			}
			if(funcCode == null || funcCode.equals("")) {
				String resultMsg = resultMap.get("msg")==null?"":String.valueOf(resultMap.get("msg"));
				String msg = resultMsg + "; 异常:'多渠道'交易 主键ID:" + invoiceNo + "匹配funcCode 失败!";
				logger.error(">>> 异常:'多渠道'交易 主键ID:" + invoiceNo + "匹配funcCode 失败!");
				super.editResultMap(resultMap, "-1", msg);
			} else {
				settleDetail.setFuncCode(funcCode);
			}
			/*
			 * 匹配payChannelId
			 */
			String payChannelId = null;
			Iterator<SettleParameterInfo> payChannelIter = payChannelIdList.iterator();
			while(payChannelIter.hasNext()) {
				SettleParameterInfo param = payChannelIter.next();
				if(param.getParameterValue().trim().equals(channelHome)) {
					payChannelId = param.getParameterCode();
					break;
				}
			}
			if(payChannelId == null || payChannelId.equals("")) {
				String resultMsg = resultMap.get("msg")==null?"":String.valueOf(resultMap.get("msg"));
				String msg = resultMsg + "; 异常:'多渠道'交易 主键ID:"+invoiceNo +", 匹配payChannelId 失败!";
				logger.error(">>> 异常:'多渠道'交易 主键ID:"+invoiceNo +", 匹配payChannelId 失败!");
				super.editResultMap(resultMap, "-1", msg);
			}
			settleDetail.setPayChannelId(payChannelId);
			/*
			 * 匹配readType
			 */
			String readType = "";
			String orderNo = "";
			Iterator<SettleParameterInfo> readTypeParamIter = readTypeList.iterator();
			while(readTypeParamIter.hasNext()) {
				SettleParameterInfo param = readTypeParamIter.next();
				String channelNoes = param.getParameterValue();
				if(channelNoes.contains(channelNo)) {
					readType = param.getParameterCode();
					break;
				}
			}
			if(SettleConstants.ACCOUNT_TYPE_CHANNEL.equals(readType)) {//网关支付
				if ("4017".equals(funcCode)) {
					orderNo = this.getOrderNoFor4017(beginAccDate,endAccDate,multiDetail.getOtransNo());
				} else {
					orderNo = multiDetail.getGatewayTransNo();
				}
			} else if(SettleConstants.ACCOUNT_TYPE_MOBILE.equals(readType)) {//移动支付
				if ("4017".equals(funcCode)) {
					orderNo = this.getOrderNoFor4017(beginAccDate,endAccDate,multiDetail.getOtransNo());
				} else {
					orderNo = multiDetail.getGatewayTransNo();
				}
			} else if(SettleConstants.ACCOUNT_TYPE_LLKJ.equals(readType)) {//连连快捷支付
				if ("4017".equals(funcCode)) {
					orderNo = this.getOrderNoFor4017(beginAccDate,endAccDate,multiDetail.getOtransNo());
				} else {
					orderNo = multiDetail.getGatewayTransNo();
				}
			} else if(SettleConstants.ACCOUNT_TYPE_GENERATE.equals(readType)) {//代收付
				/*
				 * 读取 多渠道 ZF[代收付]数据时
				 * 批量代收 和 【单笔代收】 的业务处理
				 * 批量代收时 orderNo存储多渠道GatewayBatchNo[批次号]
				 * 单笔代收时 orderNo存储多渠道GatewayTransNo[流水号]
				 */
				orderNo = "40131".equals(funcCode) ? multiDetail.getGatewayTransNo() : multiDetail.getGatewayBatchNo();
				/*
				 * 读取 多渠道 ZF[代收付]数据时
				 * 批量代收 和【单笔代付】 的业务处理
				 * 批量代收时 orderNo存储多渠道GatewayBatchNo[批次号]
				 * 单笔代收时 orderNo存储多渠道GatewayTransNo[流水号]
				 */
				if (SettleConstants.PAY_CHANNEL_ID_TL.equals(payChannelId)) {
					if("11012".equals(multiDetail.getTransCode()) || "11022".equals(multiDetail.getTransCode())) {
						orderNo = multiDetail.getGatewayBatchNo();
					} else {
						orderNo = multiDetail.getGatewayTransNo();
					}
				} else {
					orderNo = multiDetail.getGatewayTransNo();
				}
//			} else if(SettleConstants.ACCOUNT_TYPE_PAB.equals(readType)) {//平安银企直联
//				if ("4017".equals(funcCode)) {
//					orderNo = this.getOrderNoFor4017(beginAccDate,endAccDate,multiDetail.getOtransNo());
//				} else {
//					orderNo = multiDetail.getGatewayTransNo();
//				}
//			} else if(SettleConstants.ACCOUNT_TYPE_WXMOBILE.equals(readType)) {//通联微信支付
//				if ("4017".equals(funcCode)) {
//					orderNo = this.getOrderNoFor4017(beginAccDate,endAccDate,multiDetail.getOtransNo());
//				} else {
//					orderNo = multiDetail.getGatewayTransNo();
//				}
//			} else {
//				String resultMsg = resultMap.get("msg")==null?"":String.valueOf(resultMap.get("msg"));
//				String msg = resultMsg + "; 异常:'多渠道'交易 主键ID:"+invoiceNo +", 未匹配到对应的'交易类型!'[网关或代收付]";
//				logger.error(">>> 异常:'多渠道'交易 主键ID:"+invoiceNo +", 未匹配到对应的'交易类型!'[网关或代收付]");
//				super.editResultMap(resultMap, "-1", msg);
//			}
			}  else if(SettleConstants.ACCOUNT_TYPE_PAB.equals(readType)) {//平安银企直联
					orderNo = multiDetail.getBankFlowNo();
			} else if(SettleConstants.ACCOUNT_TYPE_CMBC.equals(readType)) {//民生银企直联
				orderNo = multiDetail.getOrderNo();
			} else if(SettleConstants.ACCOUNT_TYPE_WXMOBILE.equals(readType)) {//通联微信支付
				orderNo = multiDetail.getGatewayTransNo();
			} else {
				/*
				 * 默认使用ORDER_NO存储多渠道GATEWAY_TRANS_NO
				 */
				orderNo = multiDetail.getGatewayTransNo();
				String resultMsg = resultMap.get("msg")==null?"":String.valueOf(resultMap.get("msg"));
				String msg = resultMsg + "; 异常:'多渠道'交易 主键ID:"+invoiceNo +", 未匹配到对应的'交易类型!'[网关或代收付]";
				logger.error(">>> 异常:'多渠道'交易 主键ID:"+invoiceNo +", 未匹配到对应的'交易类型!'[网关或代收付]");
				super.editResultMap(resultMap, "-1", msg);
			}
			settleDetail.setOrderNo(orderNo);
			//'清算'交易信息List添加交易信息对象
			settleTransDetailList.add(settleDetail);
		}
		//计算手续费
		this.doProfitFeeAmount(settleTransDetailList);
		/*
		 * 添加或修改'清算'方交易信息
		 */
		super.insertAndUpdateSettleTransDetail(settleTransDetailList);
		if(resultMap.size() <= 0) {
			super.editResultMap(resultMap, "1", "成功!");
		}
		logger.info("<<< <<< <<< <<<从多渠道读取交易信息并存入'清算'DB END >>> >>> >>> >>> ");
		return resultMap;
	}
	/***
	 * 更新'上游渠道'和'清算'的交易状态
	 * @param colStatInfoList 
	 * 对账状态列表, 用于 update ‘上游渠道’&‘清算’交易信息对账状态 [备注 {detId:清算交易ID, accId:上游交易ID, statusId:对账状态}]
	 */
	private void updateAccAndDetTransStatus(List<Map<String, Object>> colStatInfoList) {
		Map<String, Object> map = null;
		List<Integer> succAccIdList = new ArrayList<Integer>();
		List<Integer> succDetIdList = new ArrayList<Integer>();
		List<Integer> succMultiIdList = new ArrayList<Integer>();
		List<Integer> errAccIdList = new ArrayList<Integer>();
		List<Integer> errDetIdList = new ArrayList<Integer>();
		List<Integer> gainAccIdList = new ArrayList<Integer>();
		List<Integer> lossDetIdList = new ArrayList<Integer>();
		
		Iterator<Map<String, Object>> iter = colStatInfoList.iterator();
		while(iter.hasNext()) {
			Map<String, Object> item = iter.next();
			String statusId = String.valueOf(item.get("statusId"));

			if(SettleConstants.STATUS_COLLATE_SUCCESS == Integer.parseInt(statusId)) {
				if(item.get("accId") !=null){
					succAccIdList.add(Integer.parseInt(String.valueOf(item.get("accId"))));
				}
				if(item.get("multiId") !=null){
					succMultiIdList.add(Integer.parseInt(String.valueOf(item.get("multiId"))));
				}
				if(item.get("detId") !=null){
					succDetIdList.add(Integer.parseInt(String.valueOf(item.get("detId"))));
				}
			} else if(SettleConstants.STATUS_COLLATE_FIALD == Integer.parseInt(statusId)) {
				if(item.get("accId") !=null){
					errAccIdList.add(Integer.parseInt(String.valueOf(item.get("accId"))));
				}
				if(item.get("detId") !=null){
					errDetIdList.add(Integer.parseInt(String.valueOf(item.get("detId"))));
				}
			} else if(SettleConstants.STATUS_COLLATE_GAIN == Integer.parseInt(statusId)) {
				if(item.get("accId")!=null) gainAccIdList.add(Integer.parseInt(String.valueOf(item.get("accId"))));
			} else if(SettleConstants.STATUS_COLLATE_LOSS == Integer.parseInt(statusId)) {
				lossDetIdList.add(Integer.parseInt(String.valueOf(item.get("detId"))));
			}
		}
		
		/**
		 * 修改 '清算'交易信息
		 */
		if(succDetIdList.size() > 0) {
			map = new HashMap<String, Object>();
			map.put("statusId", SettleConstants.STATUS_COLLATE_SUCCESS);
			map.put("idList", succDetIdList);
			settleTransDetailManager.updateTransStatusId(map);
		}
		if(succMultiIdList.size() > 0) {//内部对账多渠道对平的交易将reserve1的值改成21
			map = new HashMap<String, Object>();
			map.put("reserve1", SettleConstants.STATUS_COLLATE_SUCCESS);
			map.put("idList", succMultiIdList);
			settleTransDetailManager.updateTransStatusId(map);
		}
		if(errDetIdList.size() > 0) {
			map = new HashMap<String, Object>();
			map.put("statusId", SettleConstants.STATUS_COLLATE_FIALD);
			map.put("idList", errDetIdList);
			settleTransDetailManager.updateTransStatusId(map);
		}
		if(lossDetIdList.size() > 0) {
			map = new HashMap<String, Object>();
			map.put("statusId", SettleConstants.STATUS_COLLATE_LOSS);
			map.put("idList", lossDetIdList);
			settleTransDetailManager.updateTransStatusId(map);
		}
		/**
		 * 修改 '上游渠道'交易信息
		 */
		if(succAccIdList.size() > 0) {
			map = new HashMap<String, Object>();
			map.put("statusId", SettleConstants.STATUS_COLLATE_SUCCESS);
			map.put("idList", succAccIdList);
			settleTransAccountManager.updateTransStatusId(map);
		}
		if(errAccIdList.size() > 0) {
			map = new HashMap<String, Object>();
			map.put("statusId", SettleConstants.STATUS_COLLATE_FIALD);
			map.put("idList", errAccIdList);
			settleTransAccountManager.updateTransStatusId(map);
		}
		if(gainAccIdList.size() > 0) {
			map = new HashMap<String, Object>();
			map.put("statusId", SettleConstants.STATUS_COLLATE_GAIN);
			map.put("idList", gainAccIdList);
			settleTransAccountManager.updateTransStatusId(map);
		}
	}
	
	/***
	 * 获取对账规则
	 * @param payChannelId	上游渠道ID	例如:通联01 支付宝02
	 * @param collateType	上游交易类型 例如:代收付1, 网关支付2
	 * @return keys:对账key, params:对账项
	 * @throws Exception
	 */
	private Map<String, List<String>> getRule(String payChannelId, String readType) throws Exception {
		Map<String, List<String>> ruleMap = new HashMap<String, List<String>>();
		//上游对账key
		List<String> accKeys = new ArrayList<String>();
		//上游对账项param
		List<String> accParams = new ArrayList<String>();
		//下游对账key
		List<String> detKeys = new ArrayList<String>();
		//下游对账项param
		List<String> detParams = new ArrayList<String>();
		//对账规则query
		SettleRuleQuery query = new SettleRuleQuery();
		//通过上游渠道ID,查询对账规则
		query.setRuleType(payChannelId);
		query.setReadType(readType);
		query.setStatusId(1);//状态1为可用规则, 未定义相应常量
		List<SettleRule> ruleList = settleRuleManager.queryListByStart2EndTime(query);
		
		//获取对账key&对账项
		Iterator<SettleRule> iter = ruleList.iterator();
		while(iter.hasNext()) {
			SettleRule r = iter.next();
			if(r.getKeyType() == 0) {
				accKeys.add(r.getAccKeyCode());
				detKeys.add(r.getDetKeyCode());
			} else if(r.getKeyType() == 1) {
				accParams.add(r.getAccKeyCode());
				detParams.add(r.getDetKeyCode());
			}
		}
		
		if(accKeys==null || accKeys.size() < 1) throw new SettleException("上游对账key 查询0条记录!");
		if(detKeys==null || detKeys.size() < 1) throw new SettleException("'清算'游对账key 查询0条记录!");
		if(accParams==null || accParams.size() < 1) throw new SettleException("上游对账param 查询0条记录!");
		if(detParams==null || detParams.size() < 1) throw new SettleException("'清算'对账param 查询0条记录!");
		
		ruleMap.put("accKeys", accKeys);
		ruleMap.put("detKeys", detKeys);
		ruleMap.put("accParams", accParams);
		ruleMap.put("detParams", detParams);
		return ruleMap;
	}
	/***
	 * 获取上游交易中最晚的时间点
	 * @param tAccountList 上游交易信息
	 * @return
	 */
	private Long getMaxtAccountTimeStr(List<Map<String, Object>> tAccountList) throws Exception {
		Date tmpdate = null;
		Date maxtradetime = null;
		
		if(tAccountList == null || tAccountList.size() < 1) {
			logger.info(">>> >>> >>> >>> 因为获取上游对账交易0条，所以上游交易中最晚的时间点为“系统当前时间”!");
			return new Date().getTime();			
		}
		
		for (Map<String, Object> map : tAccountList) {
			if (map.get("REQUEST_TIME") == null) {
				continue;
			}
			tmpdate = (Date) map.get("REQUEST_TIME");
			if(maxtradetime == null) maxtradetime = tmpdate;
			if (maxtradetime.getTime() < tmpdate.getTime()) {
				maxtradetime = tmpdate;
			}
		}
		return maxtradetime.getTime();
	}
	/***
	 * 交易表的数据取得n
	 * 亮哥
	 * @param accountDate
	 * @param payChannelId
	 * @param accountType
	 * @param merchantCode
	 * @return
	 */
	public List<Map<String, Object>> querySettleTransDetail(Date accountDate ,String payChannelId, String accountType, String merchantCode, String bussType) throws Exception {
		/*
		 * 获取对账KEY
		 */
		List<String> keyList = null;
		try {
			keyList = this.getRule(payChannelId, accountType).get("detKeys");
			if(keyList == null || keyList.size() < 1) {
				logger.error(">>> 获取清算对账交易信息, 对账key信息0条! return null! 方法结束!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(">>>异常: 获取清算对账交易信息, 对账key信息 异常! return null! 方法结束!");
		}
		/*
		 * 拼接对账KEY进行SQL注入
		 */
		String theKeyColumnEquation = "";
		for(String key : keyList) {
			theKeyColumnEquation += ",',',ti." + key;
		}
		theKeyColumnEquation = theKeyColumnEquation.substring(5);
		theKeyColumnEquation = " CONCAT(" + theKeyColumnEquation + ") ";
		/*
		 * 查询清算交易Query对象
		 */
		SettleTransDetailQuery query = new SettleTransDetailQuery();
		query.setObligate1(theKeyColumnEquation);
		query.setRequestTime(accountDate);
		query.setMerchantCode(merchantCode);
		query.setOrderType(1);	//订单类型0交易,1结算单 对账就对结算单
		//与上游渠道对账
		if(SettleConstants.ACCOUNT_TYPE_CHANNEL.equals(accountType)) {
			/*
			 * 对账的交易类型: 网关支付=充值+订单退款
			 */
			query.setFuncCode("'" + TransCodeConst.CHARGE +"','" + TransCodeConst.FROZON +"'");
			/*
			 * readStatusId != 14  失败交易
			 */
//			query.setObligate2("14");
			query.setReadStatusId(16);
		}else if(SettleConstants.ACCOUNT_TYPE_GENERATE.equals(accountType)){
			/*
			 * 对账的交易类型: 代收付=代收+代付+提现
			 */
			query.setFuncCode("'" + TransCodeConst.PAYMENT_COLLECTION_SINGLE +"','" + TransCodeConst.PAYMENT_COLLECTION +"','" + TransCodeConst.PAYMENT_WITHHOLD +"','" 
		                          + TransCodeConst.WITHDROW +"'");
		}else if(SettleConstants.ACCOUNT_TYPE_MOBILE.equals(accountType)){
			/*
			 * 对账的交易类型: 移动支付=充值+退款
			 */
			query.setFuncCode("'" + TransCodeConst.CHARGE +"','" + TransCodeConst.FROZON +"'");
			/*
			 * readStatusId != 14  失败交易
			 */
//			query.setObligate2("14");
			query.setReadStatusId(16);
		}else if(SettleConstants.ACCOUNT_TYPE_LLKJ.equals(accountType)) {
			/*
			 * 对账的交易类型: 连连支付-移动快捷支付 = 充值 + 退款
			 */
			query.setFuncCode("'" + TransCodeConst.CHARGE +"','" + TransCodeConst.FROZON +"'");
			/*
			 * readStatusId != 14  失败交易
			 */
//			query.setObligate2("14");
			query.setReadStatusId(16);
		}else if(SettleConstants.ACCOUNT_TYPE_PAB.equals(accountType) || SettleConstants.ACCOUNT_TYPE_CMBC.equals(accountType)) {
			query.setFuncCode("'" + TransCodeConst.PAYMENT_WITHHOLD +"'");
			query.setReadStatusId(16);
		} else if(SettleConstants.ACCOUNT_TYPE_WXMOBILE.equals(accountType)) {
			query.setFuncCode("'" + TransCodeConst.CHARGE +"','" + TransCodeConst.FROZON +"'");
			/*
			 * readStatusId != 14  失败交易
			 */
//			query.setObligate2("14");
			query.setReadStatusId(16);
		} else if(SettleConstants.ACCOUNT_TYPE_TLSDK.equals(accountType)) {
			query.setFuncCode("'" + TransCodeConst.CHARGE +"','" + TransCodeConst.FROZON +"'");
			/*
			 * readStatusId != 14  失败交易
			 */
//			query.setObligate2("14");
			query.setReadStatusId(16);
		} else if(SettleConstants.ACCOUNT_TYPE_WXAPP.equals(accountType)) {
			query.setFuncCode("'" + TransCodeConst.CHARGE +"','" + TransCodeConst.FROZON +"'");
			/*
			 * readStatusId != 14  失败交易
			 */
//			query.setObligate2("14");
			query.setReadStatusId(16);
		} else if(SettleConstants.ACCOUNT_TYPE_PABKHKF.endsWith(accountType)) {
			/*
			 * 对账的交易类型: 代收付=代付
			 */
			query.setFuncCode("'" + TransCodeConst.PAYMENT_WITHHOLD +"','" 
		                          + TransCodeConst.WITHDROW +"'");
		} else if(SettleConstants.ACCOUNT_TYPE_PABB2B.endsWith(accountType)) {
			/*
			 * 16-12-19
			 * 对账的交易类型:还没有最终确定 对账文件里的交易 都例如为4014|4013 从多渠道获取的数据也录入成4014|4013外部对账顺畅, 未和账户系统核实 func_code 
			 */
			query.setFuncCode("'" + TransCodeConst.PAYMENT_WITHHOLD +"','" 
		                          + TransCodeConst.PAYMENT_COLLECTION +"'");
			query.setReadStatusId(16);
		} else if(SettleConstants.ACCOUNT_TYPE_YB_PAYMENT.endsWith(accountType)) {
			query.setFuncCode("'" + TransCodeConst.PAYMENT_COLLECTION +"','"
					 +TransCodeConst.CHARGE +"'");
			query.setReadStatusId(16);
		}else if(SettleConstants.ACCOUNT_TYPE_YB_WITHDRAW.endsWith(accountType)) {
			query.setFuncCode("'" + TransCodeConst.PAYMENT_WITHHOLD +"','"
					 +TransCodeConst.WITHDROW +"','"
					 +TransCodeConst.PAYMENT_YFQ+"'");
			query.setReadStatusId(16);
		}else if(SettleConstants.ACCOUNT_TYPE_YB_REFUND.endsWith(accountType)) {
			query.setFuncCode("'" + TransCodeConst.FROZON +"'");
			query.setReadStatusId(16);
		}else if(SettleConstants.ACCOUNT_TYPE_CMBC_AgentPay.endsWith(accountType)) {
			query.setFuncCode("'" + TransCodeConst.PAYMENT_WITHHOLD +"','"
					 +TransCodeConst.WITHDROW +"','"
					 +TransCodeConst.PAYMENT_YFQ+"'");
			query.setReadStatusId(16);
		}else if(SettleConstants.ACCOUNT_TYPE_CMBC_Refund.endsWith(accountType)) {
			query.setFuncCode("'" + TransCodeConst.PAYMENT_WITHHOLD +"','"
					 +TransCodeConst.WITHDROW +"','"
					 +TransCodeConst.PAYMENT_YFQ+"'");
			query.setReadStatusId(16);
		}else if(SettleConstants.ACCOUNT_TYPE_YB_DF.endsWith(accountType)) {
			query.setFuncCode("'" + TransCodeConst.PAYMENT_WITHHOLD +"','"
					 +TransCodeConst.WITHDROW +"','"
					 +TransCodeConst.PAYMENT_YFQ+"'");
			query.setReadStatusId(16);
		}else if(SettleConstants.ACCOUNT_TYPE_RB_DF.endsWith(accountType)) {
			query.setFuncCode("'" + TransCodeConst.PAYMENT_WITHHOLD +"','"
					 +TransCodeConst.WITHDROW +"','"
					 +TransCodeConst.PAYMENT_YFQ+"'");
			query.setReadStatusId(16);
		}else {
			String msg = "对账逻辑 取SETTLE_TRANS_DETAIL交易 querySettleTransDetail, 未配置"+ accountType +"获取FUNC_CODE的分支!";
			logger.error(">>> >>> >>> >>> " + msg);
			throw new SettleException(msg);
		}
		query.setPayChannelId(payChannelId);
		return settleTransDetailManager.queryListMap(query);
	}
	/***
	 * 上游渠道交易表的数据取得
	 * 亮哥
	 * @param settleTime
	 * @param payChannelId
	 * @param acountMerchantCode
	 * @param readType
	 * @return
	 */
	public List<Map<String, Object>> querySettleTransAccount(Date settleTime ,String payChannelId, String accountMerchantCode, String readType) throws Exception {
		return this.querySettleTransAccount(settleTime, payChannelId, accountMerchantCode, new String[]{readType});
	}
	/***
	 * 上游渠道交易表的数据取得
	 * 亮哥
	 * @param settleTime
	 * @param payChannelId
	 * @param acountMerchantCode
	 * @param readType
	 * @return
	 */
	public List<Map<String, Object>> querySettleTransAccount(Date settleTime ,String payChannelId, String accountMerchantCode, String[] readTypeArr) throws Exception {
		SettleTransAccountQuery query = new SettleTransAccountQuery();
		query.setSettleTime(settleTime);
		query.setPayChannelId(payChannelId);
		query.setMerchantCode(accountMerchantCode);
		if(readTypeArr.length == 1) {
			query.setReadType(readTypeArr[0]);
		} else if(readTypeArr.length > 1) {
			query.setReadTypeArr(readTypeArr);
		} else {
			throw new SettleException("异常:上游渠道交易表的数据取得, 错误的readType或readTypeArr");
		}
		/*
		 * 这个9的具体用法, 详见 sqlMapper
		 */
		query.setStatusId(9);
		return settleTransAccountManager.queryListMap(query);
	}
	
	/***
	 * 计算手续费
	 * @param profitTransDetailList	交易信息
	 * @return
	 * @throws Exception
	 */
	public void doProfitFeeAmount(List<SettleTransDetail> profitTransDetailList ) throws Exception {
		logger.info(">>> >>> >>> >>> 进入计算手续费操作");
		List<SettleProfitKey> profitKeyList = null;//规则List
		String message = "";//提示信息
		try {
			profitKeyList = logicConstantUtil.buildFeeAmoutInfoAndRelation();//获取手续费规则
		} catch (Exception e) {
			message = "异常:获取手续费规则";
			logger.error(">>> " + message);
		}
		
		/**
		 * 遍历交易信息,匹配相应手续费
		 */
		logger.info(">>> >>> >>> 计算手续费开始 ... ...");
		Iterator<SettleTransDetail> detailIter = profitTransDetailList.iterator();
		detailIter: while(detailIter.hasNext()) {
			SettleTransDetail transDetail = detailIter.next();//交易信息Map
			String invoiceNo = transDetail.getInvoiceNo();//多渠道交易信息ID
			String orderNo = transDetail.getOrderNo();//订单号	
			
			//手续费规则信息对象 - 获取到唯一的手续费信息
			SettleProfitKey profitKey = null;
			//手续费规则明细对象
			List<SettleProfitRule> profitRuleList = null;
			try {
				//匹配唯一手续费规则规则
				profitKey = this.getUniqueProfitKey4TransDetail(transDetail, profitKeyList);
				if(profitKey == null) {
					continue detailIter;
				}
			} catch (Exception e) {
				continue detailIter;
			}
			try {
				//获取手续费规则明细
				profitRuleList = profitKey.getSettleProfitRuleList();
				if(profitRuleList == null || profitRuleList.size() < 1) {
					message = "未查询到此条交易 [invoiceNo:"+invoiceNo+", ORDER_NO:"+orderNo+"] 相应的到手续费规则明细!";
					logger.info(">>> " + message);
					continue detailIter;
				}
			} catch (Exception e) {
				message = "异常:查询到此条交易 [invoiceNo:"+invoiceNo+", ORDER_NO:"+orderNo+"] 手续费规则明细异常!";
				logger.error(">>> " + message);
				e.printStackTrace();
				continue detailIter;
			}
			/**
			 * 待处理 : 手续费规则将要处理的交易信息
			 */
			String amountStr = String.valueOf(transDetail.getAmount());//待金额String
			BigDecimal amount = new BigDecimal(amountStr);//待计算手续费的金额
			/**
			 *遍历'手续费规则明细'进行手续费的更新和计算
			 */
			Iterator<SettleProfitRule> ruleIter = profitRuleList.iterator();
			ruleIter: while(ruleIter.hasNext()) {
				//手续费规则明细对象
				SettleProfitRule rule = ruleIter.next();
				//手续费模式:0为不启用1为按比例计费2为固定金额
				Integer profitMd = rule.getProfitMd();
				//比例 or 金额String
				String profitFeeStr = rule.getProfitFee();
				//比例 or 金额
				BigDecimal profitFee = null;
				//手续费金额 初始化0
				BigDecimal profitAmount = new BigDecimal("0");
				//选择 模式:0为不启用1为按比例计费2为固定金额3算式分配
				switch(profitMd) {
					case 0 :
						logger.info(">>> 此手续费规则明细[ID:"+rule.getProductId()+","+rule.getSubId()+"] 未启用!");
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
						 * 采用减法算式进行手续费的计算
						 * 例如:1-0.7-0.2的含义是:手续费= 交易金额 * 1 - 交易金额 * 0.7 - 交易金额 * 0.2
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
							message = "此条交易 [invoiceNo:"+invoiceNo+", ORDER_NO:"+orderNo+"]的 手续费规则明细  模式为采用减法算式进行手续费计算 算式'解析'失败!";
							logger.error(">>> " + message);
							continue detailIter;
						}
						break;
					default : 
						message = "此条交易 [invoiceNo:"+invoiceNo+", ORDER_NO:"+orderNo+"]的 手续费规则明细  的模式  数据有误 数据必须在[0,1,2]之中";
						logger.error(">>> " + message);
						continue detailIter;
				}
				//比较金额的'封顶值' & '封底值'
				Long longProfitAmount = profitAmount.longValue();//金额
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
				
				Integer profitType = rule.getProfitType();//类型				
				if (profitType == 2) {
					transDetail.setFeeAmount(longProfitAmount);
				}
			}
			message = "成功: 此条交易 [invoiceNo:"+invoiceNo+", ORDER_NO:"+orderNo+"] 计算手续费并更新到交易表成功 !";
			logger.info(">>> " + message);
		}
	}
	
	/***
	 * 匹配交易所对应的唯一手续费规则
	 * @param transDetailMap
	 * @param profitKeyList
	 * @return
	 */
	private SettleProfitKey getUniqueProfitKey4TransDetail(SettleTransDetail transDetail, List<SettleProfitKey> profitKeyList) {
		logger.info(">>> >>> >>> 匹配交易所对应的唯一手续费规则 ... ...");
		//全部分润规则迭代器
		Iterator<SettleProfitKey> iter = profitKeyList.iterator();
		//声明手续费规则
		SettleProfitKey theProfitKey = null;
		
		//遍历
	    while(iter.hasNext()) {
			try{
				//从迭代器中获取手续费规则对象
				SettleProfitKey item = iter.next();
				//匹配规则的key - 规则信息
				String rulekey = "";
				//匹配规则的key - 交易信息
				String transKey = "";
				//默认key "机构号,功能码"
				rulekey = item.getRootInstCd();
				transKey = String.valueOf(transDetail.getPayWay());
		
				/**                                                                                                                                                                                               
				 * 匹配手续费规则                                                                                                                                                                                 
				 * 常规情况下使用"机构号&功能码"就可以匹配到此条交易的唯一分润规则                                                                                                                                
				 * 考虑特殊需求预留了3组扩展字段作为动态key设置                                                                                                                                                   
				 * 以下逻辑为动态key匹配                                                                                                                                                                          
				 */                                                                                                                                                                                               
				if(item.getKeyName1()!=null && !item.getKeyName1().isEmpty()) {                                                                                                                                   
					rulekey += "," + item.getKeyValue1();                                                                                                                                                           
					transKey += "," + transDetail.getBusinessType();                                                                                                                                                
				}                                                                                                                                                                                                 
				if(item.getKeyName2()!=null && !item.getKeyName2().isEmpty()) {                                                                                                                                   
					rulekey += "," + item.getKeyValue2();                                                                                                                                                           
					transKey += "," + transDetail.getDeliverStatusId();                                                                                                                                             
				}                                                                                                                                                                                                 
				if(item.getKeyName3()!=null && !item.getKeyName3().isEmpty()) {                                                                                                                                   
					rulekey += "," + item.getKeyValue3();                                                                                                                                                           
					transKey += "," + transDetail.getObligate1();                                                                                                                                                   
				}                                                                                                                                                                                                 
				if(item.getKeyName4()!=null && !item.getKeyName4().isEmpty()) {                                                                                                                                   
					rulekey += "," + item.getKeyValue4();                                                                                                                                                           
					transKey += "," + transDetail.getObligate2();                                                                                                                                                   
				}                                                                                                                                                                                                 
				if(item.getKeyName5()!=null && !item.getKeyName5().isEmpty()) {                                                                                                                                   
					rulekey += "," + item.getKeyValue5();                                                                                                                                                           
					Long amout = transDetail.getAmount();                                                                                                                                                           
					String keyValue5 = item.getKeyValue5();                                                                                                                                                         
					String[] keyValue5ToArray = keyValue5.split("~");                                                                                                                                               
					Long moneyAreaLower = null;                                                                                                                                                                     
					Long moneyAreaUpper = null;                                                                                                                                                                     
					Boolean tempFlag = false;                                                                                                                                                                       
					if(keyValue5ToArray !=null && keyValue5ToArray.length>0){                                                                                                                                       
						if(StringUtils.isNotBlank(keyValue5ToArray[0])){                                                                                                                                              
							moneyAreaLower = Long.parseLong(keyValue5ToArray[0]);                                                                                                                                       
						}                                                                                                                                                                                             
						if(keyValue5ToArray.length>1 && StringUtils.isNotBlank(keyValue5ToArray[1])){                                                                                                                 
							moneyAreaUpper = Long.parseLong(keyValue5ToArray[1]);                                                                                                                                       
						}                                                                                                                                                                                             
					}                                                                                                                                                                                               
					if(amout !=null){                                                                                                                                                                               
						if(moneyAreaLower !=null && amout.longValue()>=moneyAreaLower.longValue()){                                                                                                                   
							tempFlag = true;                                                                                                                                                                            
						}                                                                                                                                                                                             
						if(moneyAreaUpper !=null){  
							if(amout.longValue()<=moneyAreaUpper.longValue()){
								tempFlag = true;        
							}else{
								tempFlag = false;        
							}
						}                                                                                                                                                                                               
					}                                                                                                                                                                                               
					if(tempFlag){                                                                                                                                                                                   
						transKey += "," + item.getKeyValue5();                                                                                                                                                        
					}else{                                                                                                                                                                                          
						transKey += ",";                                                                                                                                                                              
					}                                                                                                                                                                                               
				}                                                                                                                                                                                                 
				if(item.getKeyName6()!=null && !item.getKeyName6().isEmpty()) {                                                                                                                                   
					rulekey += "," + item.getKeyValue6();                                                                                                                                                           
					if(StringUtils.isNotBlank(transDetail.getObligate3())){                                                                                                                                         
						if("北京市".equals(transDetail.getObligate3())){                                                                                                                                                
							transKey += "," + "N";                                                                                                                                                                      
						}else{                                                                                                                                                                                        
							transKey += "," + "Y";                                                                                                                                                                      
						}                                                                                                                                                                                             
					}else{                                                                                                                                                                                          
						transKey += ",";                                                                                                                                                                              
					}                                                                                                                                                                                               
				}                                                                                                                                                                                                 
				if(item.getKeyName7()!=null && !item.getKeyName7().isEmpty()) {                                                                                                                                   
					rulekey += "," + item.getKeyValue7();                                                                                                                                                           
					if(StringUtils.isNotBlank(transDetail.getBankCode())){                                                                                                                                          
						if(transDetail.getBankCode().indexOf("平安银行") != -1){//判断银行名称中是否包含"平安银行"                                                                                                                                           
							transKey += "," + "N";                                                                                                                                                                      
						}else{                                                                                                                                                                                        
							transKey += "," + "Y";                                                                                                                                                                      
						}                                                                                                                                                                                             
					}else{                                                                                                                                                                                          
						transKey += ",";                                                                                                                                                                              
					}                                                                                                                                                                                               
				}  
				
				if(rulekey.equals(transKey)) {
					theProfitKey = item;
					break;
				}
			}catch(Exception e){
				logger.error("此条交易[ID:"+String.valueOf(transDetail.getTransDetailId())+", ORDER_NO:"+String.valueOf(transDetail.getOrderNo())+",匹配手续费唯一规则发生异常,error_msg:"+e.getMessage());
			}
	     }
		
		if(theProfitKey == null){
			logger.error("未匹配到此条交易[ID:"+String.valueOf(transDetail.getTransDetailId())+", ORDER_NO:"+String.valueOf(transDetail.getOrderNo())+"]的'手续费规则'!");
			throw new SettleException("未匹配到此条交易[ID:"+String.valueOf(transDetail.getTransDetailId())+", ORDER_NO:"+String.valueOf(transDetail.getOrderNo())+"]的'手续费规则'!");
		}else{
			logger.info("此条交易[ID:"+String.valueOf(transDetail.getTransDetailId())+", ORDER_NO:"+String.valueOf(transDetail.getOrderNo())+"]的手续费规则明细ID="+theProfitKey.getProfitDetailId());
		}
		return theProfitKey;
	}
	
	/***
	 * 对账(账户和多渠道)
	 * @param tAccountList	账户交易信息
	 * @param tMultiList	多渠道交易信息
	 * @param payChannelId	
	 * @param collateType	
	 * @return 提示信息 {code: 结果编号, msg: 运行结果信息, failOrderNo: 未平账目订单号, failOrderMsg: 未平账目信息}
	 */
	public synchronized Map<String, Object> doCollateAccountAndMuti(List<Map<String, Object>> tAccountList, List<Map<String, Object>> tMultiList, String payChannelId, String readType,Date accountDateBefore3) throws Exception {
		logger.info(">>> >>> >>> >>> 进入'账户'和'多渠道'交易信息对账 ... ...");
		//提示信息
		Map<String, Object> resultMap = new HashMap<String, Object>();
		//对账结果List, 暂存对账结果信息
		List<SettleBalanceEntry> settleBalanceEntryList = new ArrayList<SettleBalanceEntry>();
		//错账订单号
		List<String> failOrderNo = new ArrayList<String>();
		//错账订单提示信息 key为订单号
		Map<String, String> failOrderMsg = new HashMap<String, String>();
		//对账规则 对账key[keys] & 对账项[params]
		Map<String, List<String>> keysAndParams = null;
		//对账状态列表, 用于 update ‘账户‘交易信息对账状态 [备注 {accId:账户交易ID, statusId:对账状态}]
		List<Map<String, Object>> colStatInfoList = new ArrayList<Map<String, Object>>();
		
		try {
			//获取对账规则, 通过上游渠道ID获取对账规则
			keysAndParams = this.getRule(payChannelId, readType);//沿用老方法
			if(keysAndParams == null || keysAndParams.size() <= 0) {
				logger.info(">>> 获取对账规则  0条记录");
				return this.editResultMap(resultMap, "0", "获取 对账规则  0条记录");
			}
		} catch (Exception e) {
			logger.error(">>> 获取 对账规则  异常");
			logger.error(">>> 异常信息: " + e.getMessage());
			e.printStackTrace();
			return this.editResultMap(resultMap, "-1", "获取 对账规则 异常");
		}
		//账户方对账key
		List<String> accKeys = keysAndParams.get("accKeys");
		//多渠道方对账key
		List<String> multiKeys = keysAndParams.get("detKeys");
		//账户方对账项param
		List<String> accParams = keysAndParams.get("accParams");
		//多渠道方对账项param
		List<String> multiParams = keysAndParams.get("detParams");
		//回写账户交易【协议,渠道】, id和【协议,渠道】的对应关系
		Map<String, Object> idAndPayChannelMap;
		//回写账户交易【协议,渠道】, id和【协议,渠道】的对应关系List
		List<Map<String, Object>> idAndPayChannelMapList = new ArrayList<Map<String, Object>>();
		//账户交易信息-对账结构
		Map<String, Map<String, Object>> accountMapStru = null;
		try {
			//把入参tAccountList[账户交易信息]编辑成适合对账业务的'对账结构'
			accountMapStru = this.editCollateStructure(tAccountList, accKeys);//账户对账结构 type=1,沿用老方法
//			if(accountMapStru == null || accountMapStru.size() <= 0) {
//				logger.info(">>> 获取账户交易信息-对账结构 0条记录");
//				return this.editResultMap(resultMap, "0", "获取账户交易信息-对账结构 0条记录");
//			}
		} catch (Exception e) {
			logger.error(">>> 获取 账户交易信息-对账结构 异常", e);
			logger.error(">>> 异常信息: " + e.getMessage());
			e.printStackTrace();
			return this.editResultMap(resultMap, "-1", "获取 账户交易信息-对账结构 异常");
		}
		//多渠道交易信息-对账结构
		Map<String, Map<String, Object>> multiMapStru = null;
		try {
			//把入参tDetailList[多渠道交易信息]编辑成适合对账业务的'对账结构'
			multiMapStru = this.editCollateStructure(tMultiList, multiKeys);//多渠道对账结构 type=2
//			if(multiMapStru == null || multiMapStru.size() <= 0) {
//				logger.info(">>> 获取 多渠道交易信息-对账结构 0条记录");
//				return this.editResultMap(resultMap, "0", "获取 多渠道交易信息-对账结构 0条记录");
//			}
		} catch (Exception e) {
			logger.error(">>> 获取 多渠道交易信息-对账结构 异常");
			logger.error(">>> 异常信息: ", e);
			e.printStackTrace();
			return this.editResultMap(resultMap, "-1", "获取多渠道交易信息-对账结构 异常");
		}
		
		logger.info(">>> >>> >>> 对账开始 ... ...");
		for(String ruleKey : accountMapStru.keySet()) {//遍历'账户'方对账结构体
			//实例化对账结果对象
			SettleBalanceEntry settleBalanceEntry = new SettleBalanceEntry();
			//对账结果存储账户和多渠道的主键, 便于画面逆向查询
			String accIdAndMultiId;
			//获取账目
			Map<String, Object> accountMap = accountMapStru.get(ruleKey);
			//获取订单号[备注:大多数情况下,支付订单号是账目的唯一标示,所以我们要进行记录]
			String requestNo = String.valueOf(accountMap.get("REQUEST_NO"));
			settleBalanceEntry.setBalanceType(2);
			//判断'上游渠道'所有的对账key中,是否包含'清算'的对账key
			if(multiMapStru.containsKey(ruleKey)) {//[我有, 他也有]
				logger.info(">>> >>> 双方都有[orderNo:"+ ruleKey +"]此订单");
				Map<String, Object> multiMap = multiMapStru.get(ruleKey);
				accIdAndMultiId = accountMap.get("TRANS_DETAIL_ID")+ "#" + multiMap.get("TRANS_DETAIL_ID");
				settleBalanceEntry.setOrderNo(requestNo);
				settleBalanceEntry.setObligate1(ruleKey);
				settleBalanceEntry.setObligate2(accIdAndMultiId);
				
				logger.info(">>> >>> 生效[orderNo:"+ ruleKey +"]");
				//平账标记, 所有对账项都对平即是true,有某一项未平,表示错账即是false
				boolean allRight = true;
				//核对账目时的错误项
				String failParams = "";
				//遍历对账项进行核对... ...
				for(int i = 0; i< accParams.size(); i++) {
					if(!accountMap.get(accParams.get(i)).equals(multiMap.get(multiParams.get(i)))) {
						allRight = false;
						failParams += "," + "{"+ accParams.get(i) +" vs "+ multiParams.get(i) +"}";
					}
				}
				if(allRight) {//平账
					logger.info(">>>  平账: [orderNo:"+ ruleKey +"]");
					settleBalanceEntry.setStatusId(SettleConstants.COLLATE_STU_1);
					settleBalanceEntry.setRemark("平账");
					
					//用于 update ‘上游’&‘清算’交易表
					Map<String, Object> colStatInfoMap = new HashMap<String, Object>();
					colStatInfoMap.put("detId", accountMap.get("TRANS_DETAIL_ID"));
					colStatInfoMap.put("multiId",  multiMap.get("TRANS_DETAIL_ID"));//存放内部对账(账户跟多渠道)对平的交易
					colStatInfoMap.put("statusId", SettleConstants.STATUS_COLLATE_SUCCESS);
					colStatInfoList.add(colStatInfoMap);
					
				} else {//错账
					failParams = failParams.substring(1);//干掉拼串
					logger.info(">>>  错账: [orderNo:"+ ruleKey +"]");
					failOrderNo.add(ruleKey);
					failOrderMsg.put(ruleKey,"错账项目:" + "错账, 未平项目:" + failParams);
					settleBalanceEntry.setStatusId(SettleConstants.COLLATE_STU_0);
					settleBalanceEntry.setRemark("错账: 未平项目:" + failParams);
					
					//用于 update ‘上游’&‘清算’交易表
					Map<String, Object> colStatInfoMap = new HashMap<String, Object>();
					colStatInfoMap.put("detId", accountMap.get("TRANS_DETAIL_ID"));
					colStatInfoMap.put("statusId", SettleConstants.STATUS_COLLATE_FIALD);
					colStatInfoList.add(colStatInfoMap);
				}
				//记录'清算'账期
				settleBalanceEntry.setAccountDate((Date) accountMap.get("ACCOUNT_DATE"));
				//填入对账结果List
				settleBalanceEntryList.add(settleBalanceEntry);
				//账户交易回写协议和渠道
				String theAccpayChannelID = String.valueOf(multiMap.get("MERCHANT_CODE")) + "," + String.valueOf(multiMap.get("PAY_CHANNEL_ID"));
				idAndPayChannelMap = new HashMap<String, Object>();
				idAndPayChannelMap.put("transDetailId", accountMap.get("TRANS_DETAIL_ID"));
				idAndPayChannelMap.put("payChannelId", theAccpayChannelID);
				idAndPayChannelMapList.add(idAndPayChannelMap);
			} else {//[我有, 他没有]
				logger.info(">>> >>> 账户'有', 多渠道'无'[orderNo:"+ ruleKey +"]此订单");
				logger.info(">>>  短款: [orderNo:"+ ruleKey +"]" +"账户'有', 多渠道'无'");
				accIdAndMultiId = accountMap.get("TRANS_DETAIL_ID") + "#";
				failOrderNo.add(ruleKey);
				failOrderMsg.put(ruleKey,"短款");
				settleBalanceEntry.setOrderNo(requestNo);
				settleBalanceEntry.setObligate1(ruleKey);
				settleBalanceEntry.setObligate2(accIdAndMultiId);
				settleBalanceEntry.setStatusId(SettleConstants.COLLATE_STU_3);
				settleBalanceEntry.setRemark("短款: 因'多渠道'对账信息不包含'账户'对账信息");
				//记录'清算'账期
				settleBalanceEntry.setAccountDate((Date) accountMap.get("ACCOUNT_DATE"));
				settleBalanceEntryList.add(settleBalanceEntry);
				
				//用于 update ‘上游’&‘清算’交易表
				Map<String, Object> colStatInfoMap = new HashMap<String, Object>();
				colStatInfoMap.put("detId", accountMap.get("TRANS_DETAIL_ID"));
				colStatInfoMap.put("statusId", SettleConstants.STATUS_COLLATE_LOSS);
				colStatInfoList.add(colStatInfoMap);
			}
		}
		logger.info(">>> >>> >>> 对账结束 ... ...");
		
		//长款：多渠道有,账户没有  [他有, 我没有]
		Set<String> multiKeySet = multiMapStru.keySet();				//'多渠道'对账key集合
		Set<String> accountKeySet = accountMapStru.keySet();			//'账户'对账key集合
		multiKeySet.removeAll(accountKeySet);							//他有, 我没有
		for(String ruleKey : multiKeySet) {
			Map<String, Object> map = multiMapStru.get(ruleKey);
			String multiOrderNo = String.valueOf(map.get("ORDER_NO"));
		/*	Boolean result = this.isPingZhang(multiOrderNo,accountDateBefore3);
			if(result) continue;*/
			String accIdAndMultiId= "#" + map.get("TRANS_DETAIL_ID");
			SettleBalanceEntry settleBalanceEntry = new SettleBalanceEntry();
			logger.info(">>> >>> >>> '长款：多渠道有,账户没有 [orderNo:"+ ruleKey +"]");
			failOrderNo.add(ruleKey);
			failOrderMsg.put(ruleKey,"长款：多渠道有,账户没有");
			settleBalanceEntry.setOrderNo(multiOrderNo);
			settleBalanceEntry.setObligate1(ruleKey);
			settleBalanceEntry.setObligate2(accIdAndMultiId);
			settleBalanceEntry.setStatusId(SettleConstants.COLLATE_STU_2);
			settleBalanceEntry.setRemark("长款：多渠道有,账户没有");
			settleBalanceEntry.setBalanceType(2);
			//记录'上游渠道'账期
			settleBalanceEntry.setAccountDate((Date) map.get("ACCOUNT_DATE"));
			settleBalanceEntryList.add(settleBalanceEntry);
			
			//用于 update ‘上游’&‘清算’交易表
			/*Map<String, Object> colStatInfoMap = new HashMap<String, Object>();
			colStatInfoMap.put("detId", map.get("TRANS_DETAIL_ID"));
			colStatInfoMap.put("statusId", SettleConstants.STATUS_COLLATE_GAIN);
			colStatInfoList.add(colStatInfoMap);*/
		}
		/**
		 * 将'对账结果'插入或更新至'对账结果表'
		 * 因为,支持同一批交易被多次核对[例如:手动对账]
		 * 对账结果信息对于账目来说是唯一的.
		 * 所以,做如下处理.
		 */
		super.insertAndUpdateSettleBalanceEntry(settleBalanceEntryList);
		/**
		 * 更新'清算'交易信息状态
		 * 更新'上游渠道'交易信息状态
		 */
		this.updateAccAndDetTransStatus(colStatInfoList);
		/**
		 * 回写协议,渠道到账户系统
		 */
		this.updateAccPayChannelById(idAndPayChannelMapList);
		/**
		 * 封装返回值对象
		 */
		if(failOrderNo.size() > 0) {//有未对平的订单被记录
			this.editResultMap(resultMap, "2", "对账执行结束, 有["+failOrderNo.size()+"]条账目未对平", failOrderNo, failOrderMsg);
			logger.info(">>> >>> 对账执行结束, 有["+failOrderNo.size()+"]条账目未对平, 详情请看本方法的返回结果或查看db对账结果中REMARK信息");
		} else {//爽歪歪, 全都对平!
			this.editResultMap(resultMap, "1", "对账结束, 全都对平!");
			logger.info(">>> >>> 对账结束, 全都对平!");
		}
		logger.info(">>> >>> >>> >>> 执行结束  '上游渠道'和'清算'交易信息对账 ... ...");
		return resultMap;
	}
	
	/***
	 * 取得账户的数据
	 * @param settleTime
	 * @param payChannelId
	 * @param acountMerchantCode
	 * @param readType
	 * @return
	 */
	public List<Map<String, Object>> queryDetailAccount(Date minAccountDate ,String[] funcCodes,String inRootInstCd) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("minAccountDate", minAccountDate);
		map.put("notStatusId", 21);
		map.put("funcCodes", funcCodes);
		map.put("orderType", 0);
		map.put("merchantCode", inRootInstCd);
		return settleTransDetailManager.queryCollecteListMap(map);
	}
	
	/***
	 * 取得多渠道的数据
	 * @param settleTime
	 * @param payChannelId
	 * @param acountMerchantCode
	 * @param readType
	 * @return
	 */
	public List<Map<String, Object>> queryDetailMulti(Date accountDate ,String[] funcCodes,String inRootInstCd) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("minAccountDate", accountDate);
		map.put("statusId", 21);
		map.put("funcCodes", funcCodes);
		map.put("orderType", 1);
		map.put("merchantCode", inRootInstCd);
		map.put("notReserve1", 21);//过滤掉内部对账(账户跟多渠道)已经对平的交易
		return settleTransDetailManager.queryCollecteListMap(map);
	}

	/***
	 * 取得多渠道的退款对应的订单号
	 * @param beginAccDate
	 * @param endAccDate
	 * @param OrderNoOld
	 * @return
	 */
	public String getOrderNoFor4017(Date beginAccDate,Date endAccDate,String OrderNoOld) throws Exception {
		List<TransDetailInfo> transDetailInfoList = null;	
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("createdTime_f",beginAccDate);
		map.put("createdTime_t",endAccDate);
		map.put("ordernoold", OrderNoOld);
		transDetailInfoList = transDetailInfoManager.selectByUpdateTime(map);
		if (transDetailInfoList.size() == 0) {
			logger.info(">>> >>> >>> >>> 退款交易对应订单号未取得，使用退款订单号 ... ..." + OrderNoOld);
			return OrderNoOld;
		}
		return transDetailInfoList.get(0).getGatewayTransNo();
	}
	
	/**
	 * 查询这笔订单是否已经对成平账了
	 * @param multiOrderNo 多渠道的订单号
	 * @return
	 * @throws Exception
	 */
	public Boolean isPingZhang(String multiOrderNo,Date accountDateBefore3) throws Exception {
		Boolean result = false;
		SettleTransDetailQuery  query = new SettleTransDetailQuery();
		query.setRequestNo(multiOrderNo);
		query.setStatusId(21);
		query.setMinAccountDate(accountDateBefore3);
		int count = settleTransDetailManager.countSettleTransDetail(query);
	    if(count>0){
	    	result = true;
	    }
		return result;
	}
	
	/**
	 * 根据多渠道交易信息回写账户交易的渠道号和机构号
	 * 入参:idAndPayChannelMapList : 交易主键和所对应的协议渠道号[{transDetailId:交易主键ID},{payChannelId:"协议号,渠道号"},...]
	 */
	private void updateAccPayChannelById(List<Map<String, Object>> idAndPayChannelMapList) {
		if(idAndPayChannelMapList == null || idAndPayChannelMapList.size() < 1) {
			logger.info(">>> >>> 根据多渠道交易信息回写账户交易的渠道号和机构号！ idAndPayChannelMapList 为 null 或 size 小于1");
			return;
		}
		/*
		 * 遍历对应关系执行修改语句
		 */
		Iterator<Map<String, Object>> idAndPayChannelInter = idAndPayChannelMapList.iterator();
		Map<String, Object> idAndPayChannelMap;
		SettleTransDetail settleTransDetail;
		while(idAndPayChannelInter.hasNext()) {
			idAndPayChannelMap = idAndPayChannelInter.next();
			settleTransDetail = new SettleTransDetail();
			settleTransDetail.setTransDetailId(Integer.parseInt(String.valueOf(idAndPayChannelMap.get("transDetailId"))));
			settleTransDetail.setPayChannelId(String.valueOf(idAndPayChannelMap.get("payChannelId")));
			settleTransDetailManager.updateSettleTransDetail(settleTransDetail);
		}
	}
}
