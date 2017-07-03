package com.rkylin.settle.logic;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rkylin.interest.api.IBorrowRepaymentDubboService;
import com.rkylin.interest.dubboResponse.AttriRespose;
import com.rkylin.interest.dubboVo.InterestBorrowRepayment;
import com.rkylin.interest.dubboVo.InterestBorrowRepaymentQuery;
import com.rkylin.order.domain.M000003ServiceBean;
import com.rkylin.order.service.OrderInfoBaseService;
import com.rkylin.settle.constant.Constants;
import com.rkylin.settle.constant.SettleConstants;
import com.rkylin.settle.constant.TransCodeConst;
import com.rkylin.settle.exception.SettleException;
import com.rkylin.settle.manager.OrderInfoManager;
import com.rkylin.settle.manager.SettleBalanceEntryManager;
import com.rkylin.settle.manager.SettleFinanaceAccountManager;
import com.rkylin.settle.manager.SettleLoanDetailManager;
import com.rkylin.settle.manager.SettleParameterInfoManager;
import com.rkylin.settle.manager.SettlePosDetailManager;
import com.rkylin.settle.manager.SettleRuleManager;
import com.rkylin.settle.manager.SettleTransAccountManager;
import com.rkylin.settle.pojo.OrderInfo;
import com.rkylin.settle.pojo.SettleBalanceEntry;
import com.rkylin.settle.pojo.SettleLoanDetail;
import com.rkylin.settle.pojo.SettleParameterInfo;
import com.rkylin.settle.pojo.SettleParameterInfoQuery;
import com.rkylin.settle.pojo.SettlePosDetail;
import com.rkylin.settle.pojo.SettlePosDetailQuery;
import com.rkylin.settle.pojo.SettleRule;
import com.rkylin.settle.pojo.SettleRuleQuery;
import com.rkylin.settle.pojo.SettleTransAccountQuery;
import com.rkylin.settle.util.RkylinMailUtil;
import com.rkylin.settle.util.SettlementUtil;
import com.rkylin.wheatfield.api.PaymentAccountServiceApi;
import com.rkylin.wheatfield.model.CommonResponse;
import com.rkylin.wheatfield.pojo.Balance;



/***
 * 清分系统通用对账业务逻辑
 * @author Yang
 */
@Component("collateforposLogic")
public class CollateForPosLogic extends BasicLogic {
	@Autowired
	private SettleRuleManager settleRuleManager;					//对账规则Manager
	@Autowired
	private SettleBalanceEntryManager settleBalanceEntryManager;	//对账结果Manager
	@Autowired
	private SettlePosDetailManager settlePosDetailManager;			//'清算'交易信息Manager
	@Autowired
	private SettleTransAccountManager settleTransAccountManager;	//'上游渠道'交易信息Manager
	@Autowired
	private SettlementUtil settlementUtil;							//清算工具类
	@Autowired
	private OrderInfoManager orderInfoManager;						//'订单'交易明细Manager
	@Autowired
	private SettleParameterInfoManager settleParameterInfoManager;	//常量表
	@Autowired
	private SettleFinanaceAccountManager settleFinanaceAccountManager;	//账户侧对账账户余额
	@Autowired
	private IBorrowRepaymentDubboService iBorrowRepaymentDubboService;
	@Autowired
	private SettleLoanDetailManager settleLoanDetailManager;
	@Autowired
	private OrderInfoBaseService orderInfoBaseService;
	@Autowired
	private PaymentAccountServiceApi paymentAccountServiceApi;			//'账户系统'余额API
	

//	private String[] mailTo = new String[]{"caoyang@rkylin.com.cn","zhangqi4@rkylin.com.cn","liqiuying@rongcapital.cn"};
//	private String[] mailCc = new String[]{"sunruibin@rongcapital.cn"};
//	private String[] mailTo = new String[]{"caoyang@rkylin.com.cn","sunruibin@rongcapital.cn"};
//	private String[] mailCc = new String[]{};
//	private static final String USER_ID_HTGL = "14623543519624388";			//会堂国旅
	private static final String MAIN_ACCOUNT_PRODUCT_ID = "P000221";		//主账户管理分组
	private static final String ORDER_TYPE_REPAY = "W1003";					//订单系统还款单
	private static final String ORDER_TYPE_SETTLE = "W1001";				//订单系统结算单
	private static final String SA_PRODUCT_ID = "P000283";					//商单云待清算账户管理分组
	private static final String[] CONFIRMED_MERCHANT_CODE = new String[]{Constants.RSQB_ID};
	private static final String SPLIT_STR = "&";
	private static final String ATTACHMENT_NAME = "POS收单信息.htm";
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
			logger.error(">>> 异常信息: " + e.getMessage());
			e.printStackTrace();
			return this.editResultMap(resultMap, "-1", "获取 清算交易信息-对账结构 异常");
		}
		
		logger.info(">>> >>> >>> 对账开始 ... ...");
		for(String ruleKey : detailMapStru.keySet()) {//遍历'清算'方对账结构体
			//实例化对账结果对象
			SettleBalanceEntry settleBalanceEntry = new SettleBalanceEntry();
			//对账结果存储detail表和account表的主键, 便于画面逆向查询
			String detIdAndAccId;
			//获取账目
			Map<String, Object> detailMap = detailMapStru.get(ruleKey);
			//获取订单号[备注:大多数情况下,订单号是账目的唯一标示,所以我们要进行记录]
			String orderNo = String.valueOf(detailMap.get("ORDER_NO"));
			Integer dataFrom = Integer.parseInt(String.valueOf(detailMap.get("DATA_FROM")));
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
						settleBalanceEntry.setRemark("收单侧平账");
						
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
						settleBalanceEntry.setRemark("收单侧错账: 未平项目:" + failParams);
						
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
					settleBalanceEntry.setRemark("收单侧长款(特殊): 因'清算'方的交易状态为'失败'导致对账结果为'长款'");
					
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
					settleBalanceEntry.setRemark("收单侧短款: 因'上游渠道'对账信息不包含'清算'对账信息");
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
			SettleBalanceEntry settleBalanceEntry = new SettleBalanceEntry();
			logger.info(">>> >>> >>> '清算'交易信息中 不包含的 '上游渠道'交易信息 ['上游'有, '清算'没有的交易] [orderNo:"+ ruleKey +"]");
			failOrderNo.add(ruleKey);
			failOrderMsg.put(ruleKey,"长款: '清算'交易信息中 不包含的 '上游渠道'交易信息 ['上游'有, '清算'没有的交易]");
			settleBalanceEntry.setOrderNo(String.valueOf(map.get("ORDER_NO")));
			settleBalanceEntry.setObligate1(ruleKey);
			settleBalanceEntry.setObligate2(detIdAndAccId);
			settleBalanceEntry.setStatusId(SettleConstants.COLLATE_STU_2);
			settleBalanceEntry.setRemark("收单侧长款: '清算'交易信息中 不包含的 '上游渠道'交易信息 ['上游'有, '清算'没有的交易]");
			settleBalanceEntry.setBalanceType(1);
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
	/***
	 * 更新'上游渠道'和'清算'的交易状态
	 * @param colStatInfoList 
	 * 对账状态列表, 用于 update ‘上游渠道’&‘清算’交易信息对账状态 [备注 {detId:清算交易ID, accId:上游交易ID, statusId:对账状态}]
	 */
	private void updateAccAndDetTransStatus(List<Map<String, Object>> colStatInfoList) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Integer> succAccIdList = new ArrayList<Integer>();
		List<Integer> succDetIdList = new ArrayList<Integer>();
		List<Integer> errAccIdList = new ArrayList<Integer>();
		List<Integer> errDetIdList = new ArrayList<Integer>();
		List<Integer> gainAccIdList = new ArrayList<Integer>();
		List<Integer> lossDetIdList = new ArrayList<Integer>();
		
		Iterator<Map<String, Object>> iter = colStatInfoList.iterator();
		while(iter.hasNext()) {
			Map<String, Object> item = iter.next();
			String statusId = String.valueOf(item.get("statusId"));

			if(SettleConstants.STATUS_COLLATE_SUCCESS == Integer.parseInt(statusId)) {
				succAccIdList.add(Integer.parseInt(String.valueOf(item.get("accId"))));
				succDetIdList.add(Integer.parseInt(String.valueOf(item.get("detId"))));
			} else if(SettleConstants.STATUS_COLLATE_FIALD == Integer.parseInt(statusId)) {
				errAccIdList.add(Integer.parseInt(String.valueOf(item.get("accId"))));
				errDetIdList.add(Integer.parseInt(String.valueOf(item.get("detId"))));
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
			map.put("statusId", SettleConstants.STATUS_COLLATE_SUCCESS);
			map.put("idList", succDetIdList);
			settlePosDetailManager.updateTransStatusId(map);
		}
		if(errDetIdList.size() > 0) {
			map.put("statusId", SettleConstants.STATUS_COLLATE_FIALD);
			map.put("idList", errDetIdList);
			settlePosDetailManager.updateTransStatusId(map);
		}
		if(lossDetIdList.size() > 0) {
			map.put("statusId", SettleConstants.STATUS_COLLATE_LOSS);
			map.put("idList", lossDetIdList);
			settlePosDetailManager.updateTransStatusId(map);
		}
		/**
		 * 修改 '上游渠道'交易信息
		 */
		if(succAccIdList.size() > 0) {
			map.put("statusId", SettleConstants.STATUS_COLLATE_SUCCESS);
			map.put("idList", succAccIdList);
			settleTransAccountManager.updateTransStatusId(map);
		}
		if(errAccIdList.size() > 0) {
			map.put("statusId", SettleConstants.STATUS_COLLATE_FIALD);
			map.put("idList", errAccIdList);
			settleTransAccountManager.updateTransStatusId(map);
		}
		if(gainAccIdList.size() > 0) {
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
	 * 编辑对账信息结构
	 * @param tList	对账信息:上游or我方的对账信息
	 * @param keys	对账规则:keys
	 * @return Map<String, Map<String, String>>	String:对账key, Map<String, String>:对账信息
	 */
	protected Map<String, Map<String, Object>> editCollateStructure(List<Map<String, Object>> tList, List<String> keys) throws Exception {
		//用于对账业务的结构
		Map<String, Map<String, Object>> transMap = new HashMap<String, Map<String, Object>>();
		//迭代交易信息
		Iterator<Map<String, Object>> iter = tList.iterator();
		while(iter.hasNext()) {
			Map<String, Object> item = iter.next();
			String rulekey = "";
			for(String key : keys) {
				if(item.get(key) == null) {
					logger.error(">>> >>> >>>　'上游or清算的对账信息'的Map中,key'"+ key +"'所对应的value为'null'");
//					throw new SettleException("'上游or清算的对账信息'的Map中,key'"+ key +"'所对应的value为'null'");
					rulekey += "," + key + "_null";
				}
				rulekey += "," + item.get(key);
			}
			rulekey = rulekey.substring(1);
			transMap.put(rulekey, item);
		}
		
		return transMap;
	}
	/***
	 * 获取上游交易中最晚的时间点
	 * @param tAccountList 上游交易信息
	 * @return
	 */
	private Long getMaxtAccountTimeStr(List<Map<String, Object>> tAccountList) throws Exception {
		Date tmpdate = null;
		Date maxtradetime = null;
		
		if (tAccountList == null || tAccountList.size()<=0) {
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
	public List<Map<String, Object>> querySettlePosDetail(Date accountDate ,String payChannelId, String accountType, String merchantCode, String bussType) throws Exception {
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
		SettlePosDetailQuery query = new SettlePosDetailQuery();
		query.setObligate1(theKeyColumnEquation);
		query.setRequestTime(accountDate);
		query.setMerchantCode(merchantCode);
		query.setOrderType(2);	//订单类型0交易,1结算单 对账就对结算单
		//与上游渠道对账
		if(SettleConstants.ACCOUNT_TYPE_POS.equals(accountType) || SettleConstants.ACCOUNT_TYPE_POS_HT.equals(accountType)) {
			/**
			 * 临时解决方案:
			 * 目前充值对账的交易都是从'账户系统获取的',但是orderType字段限制了账户系统的交易不参加对账
			 * 所以按照当前需求,在获取充值交易进行对账时orderType=0
			 */
			/*
			 * 对账的交易类型: 网关支付=充值+订单退款
			 */
			query.setFuncCode("'" + TransCodeConst.CHARGE +"','" + TransCodeConst.FROZON +"'");
		}
		query.setPayChannelId(payChannelId);
		return settlePosDetailManager.queryListMap(query);
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
		SettleTransAccountQuery query = new SettleTransAccountQuery();
		query.setSettleTime(settleTime);
		query.setPayChannelId(payChannelId);
		query.setMerchantCode(accountMerchantCode);
		query.setReadType(readType);
		/*
		 * 这个9的具体用法, 详见 sqlMapper
		 */
		query.setStatusId(9);
		return settleTransAccountManager.queryListMap(query);
	}


	/***
	 * pos交易接受接口
	 * @param accountDate
	 * @param payChannelId
	 * @param accountType
	 * @param merchantCode
	 * @return
	 */
	public Map<String, Object> insertPosData(List<SettlePosDetail> settlePosDetail) throws Exception {
		Map<String, Object> rtnMap = new HashMap<String, Object>();
		rtnMap.put("code", "0000");
		rtnMap.put("msg", "成功");

		if (settlePosDetail.size() ==0) {
			rtnMap.put("code", "0001");
			rtnMap.put("msg", "参数结构体为空！");
			return rtnMap;
		}
		if (settlePosDetail.size() > 100) {
			rtnMap.put("code", "0001");
			rtnMap.put("msg", "参数结构体条数不能大于100！");
			return rtnMap;
		}
		Date accountDate = (Date) settlementUtil.getAccountDate("yyyyMMdd", 0, "Date");
		List<SettlePosDetail> rtnList = new ArrayList<SettlePosDetail>();
		SettlePosDetailQuery settlePosDetailQuery = new SettlePosDetailQuery();
		List<SettlePosDetail> rtnSettlePosDetail = new ArrayList<SettlePosDetail>();
		for (SettlePosDetail tmpBean : settlePosDetail) {
			settlePosDetailQuery = new SettlePosDetailQuery();
			settlePosDetailQuery.setAccountDate(accountDate);
			settlePosDetailQuery.setRelateOrderNo(tmpBean.getRelateOrderNo());
			
			rtnSettlePosDetail = settlePosDetailManager.queryList(settlePosDetailQuery);
			if (rtnSettlePosDetail.size() >0) {
				rtnMap.put("code", "0002");
				rtnMap.put("msg", "存在失败交易");
				tmpBean.setErrorMsg("存在相同商户订单号！请确认！");
				rtnList.add(tmpBean);
				logger.info("交易：" + tmpBean.getOrderNo() + "Key:["+ tmpBean.getRelateOrderNo() +"].[" +accountDate+ "]存在相同交易");
			} else {
				tmpBean.setAccountDate(accountDate);
				tmpBean.setStatusId(1);
				settlePosDetailManager.saveSettlePosDetail(tmpBean);
			}
		}
		if (rtnList.size() > 0) {
			rtnMap.put("detail", rtnList);
		}
		return rtnMap;
	}
	/***
	 * 调用账户转账接口
	 * @return
	 */
	public Map<String, Object> transferInCommon(long amount,String userId,String interUserId,String rootInstCd,String productId,String intoProductId) throws Exception {
		Map<String, Object> rtnMap = new HashMap<String, Object>();

		SimpleDateFormat formatDate = new SimpleDateFormat("yyyyMMddhhmmssSSS");
		SimpleDateFormat formatPara = new SimpleDateFormat("YYYY-MM-DD HH:MM:SS");
		Date newDate = new Date();
		CommonResponse commonResponse = null;
		Map<String,String[]> paraMap = new HashMap<String,String[]>();
	    paraMap.put("amount", new String[]{String.valueOf(amount)});//转账金额
	    paraMap.put("userid", new String[]{userId});//出钱方
	    paraMap.put("funccode", new String[]{"3001"});//'转账'功能编码
	    paraMap.put("intermerchantcode", new String[]{interUserId});//入钱方
	    paraMap.put("merchantcode", new String[]{rootInstCd});//入钱方
	    paraMap.put("ordercount", new String[]{"1"});
	    paraMap.put("orderdate", new String[]{formatPara.format(newDate)});
	    paraMap.put("orderno", new String[]{"PS" + formatDate.format(newDate)});
	    paraMap.put("status", new String[]{"1"});
	    paraMap.put("userfee", new String[]{"0"});
	    paraMap.put("productid", new String[]{productId});
	    paraMap.put("intoproductid", new String[]{intoProductId});
	    paraMap.put("useripaddress", new String[]{"127.0.0.1"});
	    paraMap.put("requesttime", new String[]{formatPara.format(newDate)});
	    paraMap.put("remark", new String[]{"清算POS还款转账"});
	    
		try {
			/*
			 * 执行转账操作
			 */
			logger.info(">>> 执行账户操作!");
			commonResponse = paymentAccountServiceApi.transferInCommon(paraMap);
			if (commonResponse == null) {
				logger.info("失败: 转账结束，：["+userId+"|" + productId + "]TO["+ interUserId +"|" + intoProductId + "]金额["+amount+"], 账户操作失败！返回结果为[commonResponse is Null]");
				rtnMap.put("code", "false");
				rtnMap.put("msg", "commonResponse == null");
			} else if (!"1".equals(commonResponse.code)) {
				logger.info("失败: 转账结束，：["+userId+"|" + productId + "]TO["+ interUserId +"|" + intoProductId + "]金额["+amount+"], 账户操作失败！返回结果为[commonResponse is Null]");
				rtnMap.put("code", commonResponse.code);
				rtnMap.put("msg", commonResponse.msg);
			} else if ("1".equals(commonResponse.code)) {
				logger.info("成功: 转账结束，：["+userId+"|" + productId + "]TO["+ interUserId +"|" + intoProductId + "]金额["+amount+"], 账户操作成功！");
				rtnMap.put("code", commonResponse.code);
				rtnMap.put("msg", commonResponse.msg);
			}
		} catch (Exception e) {
			logger.info("异常: 调账结束，：["+userId+"|" + productId + "]TO["+ interUserId +"|" + intoProductId + "]金额["+amount+"], 账户操作异常！");
			e.printStackTrace();
			rtnMap.put("code", "false");
			rtnMap.put("msg", e.getMessage());
		}
		return rtnMap;
	}
	
	/***
	 * 调用计息更新状态接口
	 * @return
	 */
	public Map<String, Object> saveServiceOrder(M000003ServiceBean query) throws Exception {
		Map<String, Object> rtnMap = new HashMap<String, Object>();

		SimpleDateFormat ymdformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Map<String, String> rtnOrderMap = new HashMap<String, String>();
		
		try {
			/*
			 * 执行转账操作
			 */
			logger.info(">>> 执行计息还款操作!");
			rtnOrderMap = orderInfoBaseService.saveServiceOrder(query);
			if (rtnOrderMap == null || !"true".equals(rtnOrderMap.get("issuccess"))) {
				logger.info("失败: 订单落单，：["+query.getOrderTypeId()+"|" + query.getUserOrderId() + "]"+ rtnOrderMap.get("retmsg"));
			} else if ("true".equals(rtnOrderMap.get("issuccess"))) {
				logger.info("成功: 订单落单，：["+query.getOrderTypeId()+"|" + query.getUserOrderId() + "]订单返回订单号：["+rtnOrderMap.get("orderid")+"]");
			}
		} catch (Exception e) {
			logger.info("异常: 订单落单，：["+query.getOrderTypeId()+"|" + query.getUserOrderId() + "]");
			e.printStackTrace();
		}
		
		rtnMap.put("code", rtnOrderMap.get("issuccess"));
		rtnMap.put("msg", rtnOrderMap.get("retmsg"));
		rtnMap.put("orderid", rtnOrderMap.get("orderid"));
		
		return rtnMap;
	}
	
	/***
	 * 调用计息应还总金额接口
	 * @return
	 */
	public Map<String, Object> queryRepaymentSumAmount(String rootInstCd,String userId,Date shouldRepaymentDate) throws Exception {
		Map<String, Object> rtnMap = new HashMap<String, Object>();
		SimpleDateFormat ymdformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		AttriRespose attriResponse = null;
		InterestBorrowRepaymentQuery query = new InterestBorrowRepaymentQuery();
		query.setRootInstCd(rootInstCd);
		query.setUserId(userId);
		query.setUserShouldRepaymentDate(shouldRepaymentDate);
		
		try {
			/*
			 * 执行转账操作
			 */
			logger.info(">>> 执行计息查询操作!");
			attriResponse = iBorrowRepaymentDubboService.queryRepaymentSumAmount(query);
			if (attriResponse == null || attriResponse.isSuccess() !=true) {
				logger.info("失败: 查询应还款金额结束，：["+userId+"|" + rootInstCd + "]应还款日["+ymdformat.format(shouldRepaymentDate)+", 计息操作失败！返回结果为["+attriResponse.getMessage());
			} else if (attriResponse.isSuccess() ==true) {
				logger.info("成功: 查询应还款金额结束，：["+userId+"|" + rootInstCd + "]应还款日["+ymdformat.format(shouldRepaymentDate)+", 计息操作成功!");
			}
		} catch (Exception e) {
			logger.info("异常: 查询应还款金额结束，：["+userId+"|" + rootInstCd + "]应还款日["+ymdformat.format(shouldRepaymentDate)+", 计息操作异常！");
			e.printStackTrace();
		}
		rtnMap.put("code", attriResponse.isSuccess());
		rtnMap.put("msg", attriResponse.getMessage());
		rtnMap.put("amount", attriResponse.getAmount());
		return rtnMap;
	}
	
	/***
	 * 调用计息更新状态接口
	 * @return
	 */
	public Map<String, Object> updateStatusByRecord(String rootInstCd,String userId,Date shouldRepaymentDate,Long totalAmount) throws Exception {
		Map<String, Object> rtnMap = new HashMap<String, Object>();

		SimpleDateFormat ymdformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		com.rkylin.interest.dubboResponse.BaseResponse baseResponse = null;
		InterestBorrowRepayment query = new InterestBorrowRepayment();
		query.setRootInstCd(rootInstCd);
		query.setUserId(userId);
		query.setUserShouldRepaymentDate(shouldRepaymentDate);
		query.setUserRepaidRepaymentDate(new Date());
		query.setUserShouldTotalAmount(totalAmount);
		
		try {
			/*
			 * 执行转账操作
			 */
			logger.info(">>> 执行计息还款操作!");
			baseResponse = iBorrowRepaymentDubboService.updateStatusByRecord(query);
			if (baseResponse == null || baseResponse.isSuccess() !=true) {
				logger.info("失败: 查询应还款金额结束，：["+userId+"|" + rootInstCd + "]应还款日["+ymdformat.format(shouldRepaymentDate)+", 计息操作失败！返回结果为["+baseResponse.getMessage());
			} else if (baseResponse.isSuccess() ==true) {
				logger.info("成功: 查询应还款金额结束，：["+userId+"|" + rootInstCd + "]应还款日["+ymdformat.format(shouldRepaymentDate)+", 计息操作成功!");
			}
		} catch (Exception e) {
			logger.info("异常: 查询应还款金额结束，：["+userId+"|" + rootInstCd + "]应还款日["+ymdformat.format(shouldRepaymentDate)+", 计息操作异常！");
			e.printStackTrace();
		}
		
		rtnMap.put("code", baseResponse.isSuccess());
		rtnMap.put("msg", baseResponse.getMessage());
		
		return rtnMap;
	}
	
	/**
	 * 从订单读取交易信息并存入'清算'DB
	 * @return 提示信息
	 */
	public Map<String, Object> getTransDetailFromOrder() throws Exception {
		return this.getTransDetailFromOrder(null);
	}
	/**
	 * 从订单读取交易信息并存入'清算'DB
	 * @param accountDate
	 * @return 提示信息
	 */
	public Map<String, Object> getTransDetailFromOrder(Date accountDate) throws Exception {
		logger.info(">>> >>> >>> >>> 从订单读取交易信息并存入'清算'DB START <<< <<< <<< <<<");
		Map<String, Object> resultMap = new HashMap<String, Object>();							//提示信息
		List<SettlePosDetail> settlePosDetailList = new ArrayList<SettlePosDetail>();		//'清算'交易信息
		List<OrderInfo> orderInfoList = null;										//'订单'交易信息
		
		//验证账期
		if(accountDate == null) {//如果账期为null, 即是未传入账期
			//从DB中过去账期信息, 账期在DB中每日更新,获取T-1日账期  	
			accountDate = (Date) settlementUtil.getAccountDate("yyyyMMdd", -1, "Date");	
		}
		//创建'订单'交易查询query对象
		Map<String,Object> whereMap = new HashMap<String,Object>();
		String[] typesArr = {"W1002"};
		whereMap.put("typesArr",typesArr);
		String[] rootsArr = {Constants.RSQB_ID,Constants.RDPT_ID};
		whereMap.put("rootsArr",rootsArr);
		whereMap.put("updatedTime",accountDate);
		//查询'订单'交易信息
		try {
			orderInfoList = orderInfoManager.selectByUpdateTime(whereMap);
			if(orderInfoList == null || orderInfoList.size() < 1) {
				String msg = "查询'订单'交易信息  0条";
				logger.error(">>> " + msg);
				return super.editResultMap(resultMap, "0", msg);
			}
		} catch (Exception e) {
			String msg = "异常:查询'订单'交易信息  异常";
			logger.error(">>> " + msg);
			e.printStackTrace();
			return super.editResultMap(resultMap, "-1", msg);
		}	
		
		logger.info(">>> >>> >>> 把'订单'交易信息封装成'清算'交易信息");
		//遍历上游交易信息
		Iterator<OrderInfo> iter = orderInfoList.iterator();
		String jsonBusiType = null;
		String jsonChannel = null;
		String jsonFeeAmount = null;
		String jsonFeeAmountWay = null;
		String jsonSettleDate = null;
		String jsonDealTime = null;
		String jsonSettleFlg = null;
		String jsonFactorUserId = null;		//保理USER_ID
		String jsonFactorProductId = null;	//保理主账户PRODUCT_ID
		String jsonFactorTitle = null;		//保理公司名称
		String jsonBusinessOrderNo = null;	//商单号
		
		SimpleDateFormat ymdformat = new SimpleDateFormat("yyyyMMddHHmmss");
		SimpleDateFormat ymd1format = new SimpleDateFormat("yyyyMMdd");
		try {
			while(iter.hasNext()) {
				//当前'订单'交易信息
				OrderInfo orderDetail = iter.next();
				//创建'清算'交易信息
				SettlePosDetail settleDetail = new SettlePosDetail();
				
				// josn解析
				String jsonStr = orderDetail.getGoodsDetail();
				
				// 以employee为例解析，map类似  
	//	        JSONObject jb = JSONObject.fromObject(jsonStr);
	//	        JSONArray ja = jb.getJSONArray("json");
				
				if (jsonStr == null || "".equals(jsonStr)) {
					logger.info("订单OrderId：["+orderDetail.getOrderId()+"]的关键字段[GoodsDetail]没有值");
					continue;
				} else {
			        JSONObject jb = JSONObject.fromObject(jsonStr);
					jsonBusiType = jb.get("busiType").toString();
					jsonChannel = jb.get("channel").toString();
					jsonFeeAmount = jb.get("feeAmount").toString();
					jsonFeeAmountWay = jb.get("feeAmountWay").toString();
					jsonSettleDate = jb.get("settleDate").toString();
					jsonDealTime = jb.get("dealTime").toString();
					jsonSettleFlg = jb.get("settleFlg").toString();
					if (jb.get("factorUserId") == null) {
						jsonFactorUserId = null;
					} else {
						jsonFactorUserId = jb.get("factorUserId").toString();
					}
					if (jb.get("factorProductId") == null) {
						jsonFactorProductId = null;
					} else {
						jsonFactorProductId = jb.get("factorProductId").toString();
					}
					if (jb.get("factorTitle") == null) {
						jsonFactorTitle = null;
					} else {
						jsonFactorTitle = jb.get("factorTitle").toString();
					}
					if (jb.get("businessOrderNo") == null) {
						jsonBusinessOrderNo = null;
					} else {
						jsonBusinessOrderNo = jb.get("businessOrderNo").toString();
					}
				}
				
				String userId = orderDetail.getUserId();
				String rootCd = orderDetail.getRootInstCd();
				String ordertypeid = orderDetail.getOrderTypeId();
				String productid = orderDetail.getProductId();
				String goodsName = orderDetail.getGoodsName();
				// String opertype = orderDetail.getOrderType();
				String goodsname = orderDetail.getGoodsName();
				Long unitprice = orderDetail.getUnitPrice();
				if (unitprice == null) {
					unitprice = 0l;
				}
				Date orderdate = orderDetail.getOrderDate(); 
				String userorderid = orderDetail.getUserOrderId();
				String userrelateid = orderDetail.getUserRelateId();
				Long amount = orderDetail.getAmount();
				
				
				//封装'清算交易信息'结构体
				settleDetail.setUserId(userId);
				if (jsonDealTime == null || "".equals(jsonDealTime)) {
					settleDetail.setRequestTime(orderdate);
				} else {
					settleDetail.setRequestTime(ymdformat.parse(jsonDealTime));
				}
				//settleDetail.setTransFlowNo(transFlowNo);
				settleDetail.setOrderDate(orderdate);
				settleDetail.setRelateOrderNo(userorderid);
				//settleDetail.setOrderType(orderType);
				settleDetail.setMerchantCode(rootCd);
				settleDetail.setOrderNo(orderDetail.getOrderId());
				settleDetail.setProductId(productid);
				settleDetail.setDealProductCode(ordertypeid);
				if ("W1002".equals(ordertypeid)) {
					settleDetail.setFuncCode("4015");
				} else {
					continue;//目前只操作pos支付交易
				}
				settleDetail.setInterMerchantCode(userrelateid);
				settleDetail.setBusinessType(jsonBusiType);//结算类型:00 当日 01次日
				if ("00".equals(jsonChannel)) {
					settleDetail.setPayChannelId("P01");//渠道号:00 银商
				} else {
					settleDetail.setPayChannelId(jsonChannel);//渠道号:00 银商
				}
				settleDetail.setPayWay(jsonSettleFlg);//对公/对私:对公:0,对私:1
				settleDetail.setOrderAmount(amount);
	
				if ("0".equals(jsonFeeAmountWay)) {//手续费方式:内扣/外扣 0无手续费，1为内扣，2为外扣
					settleDetail.setUserFeeWay(Integer.parseInt(jsonFeeAmountWay));
					settleDetail.setUserFee(0l);
					settleDetail.setAmount(amount);
				} else if ("1".equals(jsonFeeAmountWay)) {
					settleDetail.setUserFeeWay(Integer.parseInt(jsonFeeAmountWay));
					settleDetail.setUserFee(unitprice);
					settleDetail.setAmount(amount-unitprice);
				} else if ("2".equals(jsonFeeAmountWay)) {
					settleDetail.setUserFeeWay(Integer.parseInt(jsonFeeAmountWay));
					settleDetail.setUserFee(unitprice);
					settleDetail.setAmount(amount);
				}
				
				// 必须配置的字段
				if ("YQZL".equals(goodsName)) {
					settleDetail.setDataFrom(1);
				} else {
					settleDetail.setDataFrom(0);
				}
				settleDetail.setOrderType(2);// 订单系统读入
				settleDetail.setStatusId(11);// 无需清分，所以状态设成11
				settleDetail.setReadCreatedTime(orderDetail.getCreatedTime());
				settleDetail.setReadStatusId(8);
				settleDetail.setDeliverStatusId(1);// 处理状态初始化

				if (jsonSettleDate == null || "".equals(jsonSettleDate)) {
					settleDetail.setRequestTime(accountDate);
				} else {
					settleDetail.setAccountDate(ymd1format.parse(jsonSettleDate));
				}
				
				settleDetail.setInterMerchantCode(jsonFactorUserId);
				settleDetail.setIntoInstCode(jsonFactorProductId);
				settleDetail.setObligate1(jsonBusinessOrderNo);
				settleDetail.setObligate2(jsonFactorTitle);
				
				//'清算'交易信息List添加交易信息对象
				settlePosDetailList.add(settleDetail);
			}
		} catch (Exception e) {
			String msg = "异常:编辑'订单'交易信息  异常";
			logger.error(">>> " + msg);
			e.printStackTrace();
			return super.editResultMap(resultMap, "-1", msg);
		}	
		/*
		 * 添加或修改'清算'方交易信息
		 */
		this.insertAndUpdateSettlePosDetail(settlePosDetailList);
		if(resultMap.size() <= 0) {
			super.editResultMap(resultMap, "1", "成功!");
		}
		logger.info("<<< <<< <<< <<<从订单读取交易信息并存入'清算'DB END >>> >>> >>> >>> ");
		return resultMap;
	}

	/**
	 * 添加或修改'清算'方交易信息
	 * @param settleTransDetailList
	 */
	protected void insertAndUpdateSettlePosDetail(List<SettlePosDetail> settlePosDetailList) throws Exception {
		logger.info(">>> >>> >>> >>> 添加或修改'清算'方交易信息");
		//遍历'清算'方交易信息
		Iterator<SettlePosDetail> iter  = settlePosDetailList.iterator();
		SettlePosDetail updateItem = null;//修改的交易
		List<SettlePosDetail> addList = new ArrayList<SettlePosDetail>();//新增的交易
		while(iter.hasNext()) {
			//当前'清算'交易信息
			SettlePosDetail item = iter.next();
			//查询此信息在DB中是否存在
			SettlePosDetailQuery query = new SettlePosDetailQuery();
			query.setAccountDate(item.getAccountDate());
			query.setOrderNo(item.getOrderNo());
			Integer count = settlePosDetailManager.countSettlePosDetail(query);
			if(count > 0) {//存在，不做更新
//				if () {
//					updateItem = new SettlePosDetail();
//					updateItem.setReadStatusId(item.getReadStatusId());
//					updateItem.setAccountDate(item.getAccountDate());
//					updateItem.setOrderNo(item.getOrderNo());
//					updateItem.setInvoiceNo(item.getInvoiceNo());
//					settlePosDetailManager.updateSettlePosDetail(updateItem);
//				}
			} else {//不存在
				addList.add(item);
			}
		}
		if(addList.size() > 0) this.batchSaveSettlePosDetail(addList);	
	}
	
	/**
	 * 批量插入清结算交易信息
	 * 
	 * @param settleTransDetailList
	 */
	public void batchSaveSettlePosDetail(List<SettlePosDetail> settlePosDetailList) {
		logger.info(">>> >>> 开始执行 批量插入清结算交易信息");
		final Integer total = settlePosDetailList.size();//总数量
		if(total < 1) {
			logger.info(">>> >>> 批量插入清结算交易信息条数为0条!");
			return;
		}
		final Integer size = 500;//每批数量
		final Integer totalIndex = total / size + 1;//总批数
		Integer batchIndex = 1;//当前批数
		Integer fromIndex = null;//开始索引 包含
		Integer toIndex = null;//结束索引 不包含
		while(totalIndex >= batchIndex) {
			fromIndex = (batchIndex - 1) * size;
			toIndex = totalIndex == batchIndex ? total : batchIndex * size;
			settlePosDetailManager.batchSaveSettlePosDetail(settlePosDetailList.subList(fromIndex, toIndex));
			batchIndex ++;
		}
		logger.info("<<< <<< 结束执行 批量插入清结算交易信息");
	}
	/**
	 * 
	 * @param invoiceDate
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getPosToSettle(Date invoiceDate) throws Exception {
		logger.info(">>> >>> >>> >>> 根据收单交易调用计息&账户接口  START <<< <<< <<< <<<");
		//提示信息Map
		Map<String, Object> resultMap = new HashMap<String, Object>();//提示信息
		Map<String, Map<String, String>> accountInfoMap = null;
		//邮件内容StringBuffer
		StringBuffer mailContext = new StringBuffer();
		mailContext.append("根据收单交易调用计息&账户接口");
		mailContext.append("<BR>");
		
		List<Map<String,Object>> settlePosDetailList = new ArrayList<Map<String,Object>>();
		Calendar colDate = Calendar.getInstance();
		colDate.setTime(invoiceDate);
		colDate.set(Calendar.HOUR_OF_DAY, 0);
		colDate.set(Calendar.MINUTE, 0);
		colDate.set(Calendar.SECOND, 0);
		Date accountDate = colDate.getTime();
		SimpleDateFormat ymdformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat ymdIdformat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		SimpleDateFormat ymdForYMDformat = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat ymdForHMSformat = new SimpleDateFormat("HHmmss");
		String mailDate = ymdformat.format(accountDate);
		colDate.add(Calendar.DATE, -1);
		Date accountDateForP = colDate.getTime();
		try {
			//获取未处理的还款交易
			settlePosDetailList = this.getPosDetailForProfit(accountDate);
			logger.info(">>> 获取未处理收单交易信息"+ settlePosDetailList.size() +"条");
			mailContext.append("<BR>&nbsp;1.获取未处理收单交易信息"+ settlePosDetailList.size() +"条");
			mailContext.append("<BR>");
		} catch (Exception e) {
			String msg = "获取未处理收单交易信息, 异常!";
			logger.error(">>> " + msg);
			this.editResultMap(resultMap, "-1", msg);
			e.printStackTrace();
			this.sendMail("POS收单异常-"+mailDate, e.getMessage(), "POSRES", null);
			return resultMap;
		}
		try {
			//查询POS交易中USER_ID(供应商)对应的账户信息并更新到数据库中
			queryAccountInfoByPosDetail(settlePosDetailList);
		} catch (Exception e) {
			logger.error(">>> >>> >>> 查询POS交易中USER_ID(供应商)对应的账户信息并更新到数据库中", e);
		}
		try {
			//获取贷款还款账户信息
			accountInfoMap = getAccountInfoMap();
		} catch (Exception e) {
			String msg = "获取贷款还款账户信息, 异常!";
			logger.error(">>> >>> >>> " + msg, e);
			e.printStackTrace();
			this.editResultMap(resultMap, "-1", msg);
			this.sendMail("POS收单异常-"+mailDate, e.getMessage(), "POSRES", null);
			return resultMap;
		}
		//遍历还款交易进行汇总
		Iterator<Map<String,Object>> iter = settlePosDetailList.iterator();
		//keyStr汇总【机构+用户】
		Map<String,Map<String,Object>> sumAmtMap = new HashMap<String,Map<String,Object>>();
		//keyStr1汇总【机构+用户+对公/私】
		Map<String,SettleLoanDetail> keyMap = new HashMap<String,SettleLoanDetail>();
		Map<String,Object> sumAmtSubMap = new HashMap<String,Object>();
		String keyStr = "";
		String keyStr1 = "";
		//还款结算单
		SettleLoanDetail tmpBean = new SettleLoanDetail();
		BigDecimal addamt = null;
		BigDecimal addamt1 = null;
		BigDecimal settleamt = null;
		Map<String, String> transIdMap = new HashMap<String, String>();
		Map<String, String> transIdMapByUpdate = new HashMap<String, String>();
		String transIdStr = "";
		/*
		 * 查询UserId对应的待清算账户
		 */
		Map<String, String> intoUserIdMap = accountInfoMap.get("sAUserIdMap");
		/*
		 * 查询ProductId对应的待清算账户
		 */
		Map<String, String> intoProductIdMap = accountInfoMap.get("sAProductIdMap");
		/*
		 * 保理公司UserId
		 */
		Map<String, String> factorUserIdMap = accountInfoMap.get("factorUserIdMap");
		/*
		 * 保理公司管理分组
		 */
		Map<String, String> factorProductIdMap = accountInfoMap.get("factorProductIdMap");
		/*
		 * 保理公司名称
		 */
		Map<String, String> factorTitleMap = accountInfoMap.get("factorTitleMap");
		/*
		 * 机构Id
		 */
		Map<String, String> merchantMap = accountInfoMap.get("merchantMap");
		/*
		 * 主账户UserId
		 */
		Map<String, String> userIdMap = accountInfoMap.get("userIdMap");
		/*
		 * 主账户ProductId
		 */
		Map<String, String> productIdMap = accountInfoMap.get("productIdMap");
		
		try {
			while(iter.hasNext()) {
				//当前分润结果信息
				Map<String,Object> item = iter.next();
				// 机构+用户
				keyStr = item.get("MERCHANT_CODE").toString() + item.get("USER_ID").toString();
				if (item.get("PAY_WAY") == null || "".equals(item.get("PAY_WAY").toString())) {
					item.put("PAY_WAY", "0");
				}
				keyStr1 = item.get("MERCHANT_CODE").toString()+item.get("USER_ID").toString()+item.get("PAY_WAY").toString();
				sumAmtSubMap = new HashMap<String,Object>();
				tmpBean = new SettleLoanDetail();
				
				if (keyMap.containsKey(keyStr1)) {
					tmpBean = keyMap.get(keyStr1);
					addamt = new BigDecimal(item.get("AMOUNT").toString());
					addamt1 = new BigDecimal(item.get("USER_FEE").toString());
					settleamt = addamt.subtract(addamt1); // 内扣手续费
					addamt = new BigDecimal(tmpBean.getAmount());
					settleamt = settleamt.add(addamt);
					tmpBean.setAmount(settleamt.longValue());
					transIdStr = transIdMap.get(keyStr1);
					transIdStr = transIdStr + "," + String.valueOf(item.get("TRANS_DETAIL_ID"));
					transIdMap.put(keyStr1, transIdStr);
					tmpBean = new SettleLoanDetail();
				} else {
					addamt = new BigDecimal(item.get("AMOUNT").toString());
					addamt1 = new BigDecimal(item.get("USER_FEE").toString());
					settleamt = addamt.subtract(addamt1); // 内扣手续费
					tmpBean.setAmount(settleamt.longValue());
					tmpBean.setProductId(item.get("PRODUCT_ID").toString());
					tmpBean.setRootInstCd(item.get("MERCHANT_CODE").toString());
					tmpBean.setUserId(item.get("USER_ID").toString());
					tmpBean.setLoanOrderNo(item.get("RELATE_ORDER_NO").toString());
					tmpBean.setStatusId(1);
					tmpBean.setSettleFlg(item.get("PAY_WAY").toString());
					tmpBean.setObligate1(intoUserIdMap.get(keyStr));
					tmpBean.setObligate2(intoProductIdMap.get(keyStr));
					tmpBean.setObligate3(factorUserIdMap.get(keyStr));
					tmpBean.setObligate4(factorProductIdMap.get(keyStr));
//					if(Arrays.asList(CONFIRMED_MERCHANT_CODE).contains(tmpBean.getRootInstCd())) {
//						tmpBean.setProductId(this.genPosOrderNo("POS", item.get("USER_ID").toString(), item.get("PAY_WAY").toString()));
//					}
					keyMap.put(keyStr1,tmpBean);
					transIdMap.put(keyStr1, String.valueOf(item.get("TRANS_DETAIL_ID")));
				}
	
				if (sumAmtMap.containsKey(keyStr)) {
					sumAmtSubMap = sumAmtMap.get(keyStr);
					addamt = new BigDecimal(item.get("AMOUNT").toString());
					addamt1 = new BigDecimal(item.get("USER_FEE").toString());
					settleamt = addamt.subtract(addamt1); // 内扣手续费
					addamt = new BigDecimal(sumAmtSubMap.get("AMOUNT").toString());
					settleamt = settleamt.add(addamt);
					sumAmtSubMap.put("AMOUNT", settleamt.longValue());
					transIdStr = transIdMapByUpdate.get(keyStr);
					transIdStr = transIdStr + "," + String.valueOf(item.get("TRANS_DETAIL_ID"));
					transIdMapByUpdate.put(keyStr, transIdStr);
				} else {
					addamt = new BigDecimal(item.get("AMOUNT").toString());
					addamt1 = new BigDecimal(item.get("USER_FEE").toString());
					settleamt = addamt.subtract(addamt1); // 内扣手续费
					sumAmtSubMap.put("AMOUNT", settleamt.longValue());
					sumAmtSubMap.put("MERCHANT_CODE", item.get("MERCHANT_CODE"));
					sumAmtSubMap.put("USER_ID", item.get("USER_ID"));
					sumAmtSubMap.put("PRODUCT_ID", item.get("PRODUCT_ID"));
					sumAmtSubMap.put("INTO_USER_ID", intoUserIdMap.get(keyStr));
					sumAmtMap.put(keyStr,sumAmtSubMap);
					transIdMapByUpdate.put(keyStr, String.valueOf(item.get("TRANS_DETAIL_ID")));
				}
			}
			for(String key : userIdMap.keySet()) {
				if(!sumAmtMap.containsKey(key)) {
					keyStr1 = key + "0";
					sumAmtSubMap = new HashMap<String,Object>();
					sumAmtSubMap.put("AMOUNT", 0l);
					sumAmtSubMap.put("MERCHANT_CODE", merchantMap.get(key));
					sumAmtSubMap.put("USER_ID", userIdMap.get(key));
					sumAmtSubMap.put("PRODUCT_ID", productIdMap.get(key));
					sumAmtSubMap.put("INTO_USER_ID", intoUserIdMap.get(key));
					sumAmtMap.put(key,sumAmtSubMap);
					tmpBean = new SettleLoanDetail();
					tmpBean.setAmount(0l);
					tmpBean.setProductId(productIdMap.get(key));
//						if(Arrays.asList(CONFIRMED_MERCHANT_CODE).contains(tmpBean.getRootInstCd())) {
//							tmpBean.setProductId(this.genPosOrderNo("POS", userIdMap.get(key), "0"));
//						}
					tmpBean.setRootInstCd(merchantMap.get(key));
					tmpBean.setUserId(userIdMap.get(key));
					tmpBean.setLoanOrderNo("NoPosDetail");
					tmpBean.setStatusId(1);
					tmpBean.setSettleFlg("0");
					tmpBean.setObligate1(factorUserIdMap.get(keyStr));
					tmpBean.setObligate2(factorProductIdMap.get(keyStr));
					keyMap.put(keyStr1,tmpBean);
				}
			}
			String keyDelete = "";
			String keyDelete1 = "";
			for(String key : sumAmtMap.keySet()) {
				if(!userIdMap.containsKey(key)) {
					keyDelete = keyDelete + key + ",";
					if (keyMap.containsKey(key+"0")) {
						keyDelete1 = keyDelete1+ key+"0" + ",";
					}
					if (keyMap.containsKey(key+"1")) {
						keyDelete1 = keyDelete1+ key+"1" + ",";
					}
				}
			}

			if (!"".equals(keyDelete)) {
				String[] keyDeleteArr = keyDelete.split(",");
				for (int ii=0 ; ii<keyDeleteArr.length; ii++) {
					sumAmtMap.remove(keyDeleteArr[ii]);
				}
			}
			if (!"".equals(keyDelete1)) {
				String[] keyDelete1Arr = keyDelete1.split(",");
				for (int ii=0 ; ii<keyDelete1Arr.length; ii++) {
					keyMap.remove(keyDelete1Arr[ii]);
				}
			}
		} catch (Exception e) {
			String msg = "编辑数据, 异常!";
			logger.error(">>> " + msg);
			this.editResultMap(resultMap, "-1", msg);
			e.printStackTrace();
			this.sendMail("POS收单异常-"+mailDate, e.getMessage(), "POSRES", null);
			return resultMap;
		}
		
		mailContext.append("<BR>&nbsp;2.编辑完共有"+ sumAmtMap.size() +"人，需要分润判断！");
		mailContext.append("<BR>");
		/*
		 * 待清算账户调账 & 标记交易已清分 & 查询待清算账户余额
		 */
		Long settleAmt = null;
		Map<String, Object> changeAccountResultMap = null;
		String message = null;
		String keyG = null;
		String keyS = null;
		SettleLoanDetail sldG = null;
		SettleLoanDetail sldS = null;
		for(String key : sumAmtMap.keySet()) {
			sumAmtSubMap = sumAmtMap.get(key);
			settleAmt = (Long) sumAmtSubMap.get("AMOUNT");
			/*
			 * 待清算账户调账
			 */
			mailContext.append("&nbsp;&nbsp;2-1.待清算账户调账, 入账结算金额为[" + settleAmt + "]");
			mailContext.append("<BR>");
			if(settleAmt > 0L) {
				try {
					message = "参数：[" + sumAmtSubMap.get("INTO_USER_ID") 
								+ "|" + intoProductIdMap.get(key) 
								+ "|" + settleAmt+"|1]";
					changeAccountResultMap = this.changeAccount(
							sumAmtSubMap.get("MERCHANT_CODE").toString(), 
							sumAmtSubMap.get("INTO_USER_ID").toString(), 
							intoProductIdMap.get(key), 
							settleAmt,
							TransCodeConst.CHECK_THE_BALANCE_PLUS,
							"Settle" + ymdIdformat.format(new Date()),
							"POS收单调账");
				} catch (Exception e) {
					logger.error("调账到待清算账户【异常】:" + message);
			    	mailContext.append("&nbsp;&nbsp;调账到待清算账户【异常】:" + message);
			    	e.printStackTrace();
				}
				if(changeAccountResultMap == null || !"1".equals(changeAccountResultMap.get("code").toString())) {//失败
					logger.error("调账到待清算账户【失败】:" + message);
					mailContext.append("&nbsp;&nbsp;调账到待清算账户【失败】:" + message);
				} else if("1".equals(changeAccountResultMap.get("code").toString())) {//成功
					logger.error("调账到待清算账户【成功】:" + message);
					mailContext.append("&nbsp;&nbsp;调账到待清算账户【成功】:" + message);
				}
				mailContext.append("<BR>");
			}
			/*
			 * 修改收单数据状态
			 */
			mailContext.append("&nbsp;&nbsp;2-2.修改收单数据状态");
			mailContext.append("<BR>");
			String proId = null;
			String[] proIdArr = null;
			List<Integer> idList = null;
			if (transIdMapByUpdate.containsKey(key)) {
				proId = transIdMapByUpdate.get(key);
			    proIdArr = proId.split(",");
				idList = new ArrayList<Integer>();
				for (String id : proIdArr) {
					idList.add(Integer.parseInt(id));
				}
				if(idList.size() > 0) {
					try {
						this.updatePosStatus(idList, "已生成分润交易！", 21, 2, "");
					} catch (Exception e) {
						logger.error("修改收单数据状态【异常】", e);
				    	mailContext.append("&nbsp;&nbsp;修改收单数据状态【异常】:" + e.getMessage());
						e.printStackTrace();
						this.editResultMap(resultMap, "-1", "修改收单数据状态【异常】:" + e.getMessage());
						this.sendMail("POS收单异常-"+mailDate, e.getMessage(), "POSRES", null);
						return resultMap;
					}
					logger.info("修改收单数据状态【成功】");
			    	mailContext.append("&nbsp;&nbsp;修改收单数据状态【成功】");
			    	mailContext.append("<BR>");
				} else {
					logger.info("修改收单数据状态【 0 条】");
			    	mailContext.append("&nbsp;&nbsp;修改收单数据状态【 0 条】");
			    	mailContext.append("<BR>");
				}
			}
			/*
			 * 查询待清算账户余额
			 */
			mailContext.append("&nbsp;&nbsp;2-3.查询待清算账户余额");
			mailContext.append("<BR>");
			String paramString = "";
			String argMsg = null;
			Balance balance = null;
			Long amountBalance = null;
			try {
				argMsg = "[USER_ID:"+sumAmtSubMap.get("INTO_USER_ID").toString()+""
						+ ", PRODUCT_ID:"+String.valueOf(intoProductIdMap.get(key))+""
						+ ", CONST_ID:"+String.valueOf(sumAmtSubMap.get("MERCHANT_CODE")) + "]";
				balance = this.getBalance(String.valueOf(sumAmtSubMap.get("INTO_USER_ID"))
							,String.valueOf(intoProductIdMap.get(key))
							,String.valueOf(sumAmtSubMap.get("MERCHANT_CODE"))
							,paramString);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("调用账户接口获取余额失败!查询余额方法异常  " + argMsg);
				mailContext.append("&nbsp;&nbsp;调用账户接口获取余额失败!查询余额方法异常  " + argMsg);
				mailContext.append("<BR>");
				amountBalance = 0l;
			}
			if(balance == null) {
				logger.info("调用账户接口获取余额失败!返回结果为null " + argMsg);
				mailContext.append("&nbsp;&nbsp;调用账户接口获取余额失败!返回结果为null" + argMsg);
				mailContext.append("<BR>");
				amountBalance = 0l;
			} else {
				//账户提现余额
				amountBalance = balance.getBalanceSettle();
				mailContext.append("&nbsp;&nbsp;调用账户接口获取余额成功!"+ argMsg +"，账户余额为：["+amountBalance+"]");
				mailContext.append("<BR>");
			}
			/*
			 * 结算金额替换为
			 */
			keyG = key + "0";
			keyS = key + "1";
			sldG = keyMap.get(keyG);
			sldS = keyMap.get(keyS);
			sumAmtSubMap.put("AMOUNT", amountBalance);
			sumAmtMap.put(key, sumAmtSubMap);
			if(sldG != null) {
				sldG.setAmount(0L);
				keyMap.put(keyG, sldG);
			}
			if(sldS != null) {
				sldS.setAmount(amountBalance);
				keyMap.put(keyS, sldS);
			} else {
				sldG.setAmount(amountBalance);
				keyMap.put(keyG, sldG);
			}
			mailContext.append("--------------------------------------------------------<BR>");
		}
		
		Map<String , Object> subRtnMap = new HashMap<String , Object>();
		Map<String , Object> subARtnMap = new HashMap<String , Object>();
		Iterator<Map.Entry<String,Map<String,Object>>> entries = sumAmtMap.entrySet().iterator();
		List<String> amtList = new ArrayList<String>();
		mailContext.append("<BR>&nbsp;3.查询应还款金额");
		mailContext.append("<BR>");
		Long shAmt = 0l;			//应还金额
		Long amt = 0l;				//实收金额
		Long accAmt = 0l;			//账户余额转出金额
		Long amountBalance = 0l;	//账户余额
		Map<String, Long> totAmtMap = new HashMap<String, Long>();
		Map<String, Long> accAmtMap = new HashMap<String, Long>();
		try {
			//遍历sumAmtMap【机构+用户】
			sumAmtMapWhile:
			while (entries.hasNext()) {
				Map.Entry<String,Map<String,Object>> entry = entries.next();
			    keyStr = entry.getKey();
			    sumAmtSubMap = entry.getValue();
			    //调计息查询还款总金额 机构+用户ID+账期
			    subRtnMap = this.queryRepaymentSumAmount(sumAmtSubMap.get("MERCHANT_CODE").toString(), sumAmtSubMap.get("USER_ID").toString(), accountDate);
			    if (subRtnMap == null || "false".equals(subRtnMap.get("code").toString())) {
			    	logger.info("失败: 查询应还款金额结束，：["+sumAmtSubMap.get("MERCHANT_CODE").toString()+"|" + sumAmtSubMap.get("USER_ID").toString() + "]应还款日["+ymdformat.format(accountDate)+", 计息操作失败！返回结果为["+subRtnMap.get("code").toString()+"]");
			    	mailContext.append("&nbsp;&nbsp;失败: 查询应还款金额结束，：["+sumAmtSubMap.get("MERCHANT_CODE").toString()+"|" + sumAmtSubMap.get("USER_ID").toString() + "]应还款日["+ymdformat.format(accountDate)+", 计息操作失败！返回结果为["+subRtnMap.get("code").toString()+"]");
			    	mailContext.append("<BR>--------------------------------------------------------");
			    	mailContext.append("<BR>");
					continue sumAmtMapWhile;
			    } else if ("true".equals(subRtnMap.get("code").toString())) {
			    	logger.info("成功: 查询应还款金额结束，：["+sumAmtSubMap.get("MERCHANT_CODE").toString()+"|" + sumAmtSubMap.get("USER_ID").toString() + "]应还款日["+ymdformat.format(accountDate)+", 计息操作成功！");
			    	mailContext.append("&nbsp;&nbsp;成功: 查询应还款金额结束，：["+sumAmtSubMap.get("MERCHANT_CODE").toString()+"|" + sumAmtSubMap.get("USER_ID").toString() + "]应还款日["+ymdformat.format(accountDate)+", 计息操作成功！");
					mailContext.append("<BR>");
			    }
			    if (subRtnMap.get("amount") == null) {
			    	subRtnMap.put("amount", 0L);
			    }
			    //应还金额
			    shAmt = (Long) subRtnMap.get("amount");
			    /*
			     * 应还款金额为0时, 保利云转账到主账户. 原还款机构万众和会堂资金留在待清算账户
			     */
			    if(shAmt == 0L) {//应还为0
			    	for(String confirmedMerchantCodeItem : CONFIRMED_MERCHANT_CODE) {//判断是否是原还款机构的还款信息
			    		if(confirmedMerchantCodeItem.equals(String.valueOf(sumAmtSubMap.get("MERCHANT_CODE")))) {
			    			String msg = "&nbsp;&nbsp;查询应还款金额为0 : " + "["+sumAmtSubMap.get("MERCHANT_CODE").toString()+"|" + sumAmtSubMap.get("USER_ID").toString() + "]";
							logger.error(">>> >>> " + msg);
							mailContext.append(msg);
							mailContext.append("<BR>--------------------------------------------------------");
					    	mailContext.append("<BR>");
					    	continue sumAmtMapWhile;
				    	}
				    }
			    }
			    //待清算余额
			    amt = (Long)sumAmtSubMap.get("AMOUNT");
			    logger.info("应还款金额为["+shAmt+"]~待清算余额为["+amt+"]");
		    	mailContext.append("&nbsp;&nbsp;应还款金额为["+shAmt+"]~待清算余额为["+amt+"]");
				mailContext.append("<BR>");
			    if (shAmt > amt) {
			    	logger.info("应还款金额大于待清算余额：["+sumAmtSubMap.get("MERCHANT_CODE").toString()+"|" + sumAmtSubMap.get("USER_ID").toString() + "]应还款日["+ymdformat.format(accountDate));
			    	mailContext.append("&nbsp;&nbsp;应还款金额大于待清算余额：["+sumAmtSubMap.get("USER_ID").toString() + "]的应还款金额["+shAmt + "],待清算还款金额["+amt+"]");
					mailContext.append("<BR>");
					mailContext.append("&nbsp;3-1.查询账户余额");
					mailContext.append("<BR>");
					String paramString = "";
					String argMsg = null;
					Balance balance = null;
					try {
						argMsg = "[USER_ID:"+String.valueOf(sumAmtSubMap.get("USER_ID"))
								+ ", PRODUCT_ID:"+String.valueOf(sumAmtSubMap.get("PRODUCT_ID"))
								+ ", CONST_ID:"+String.valueOf(sumAmtSubMap.get("MERCHANT_CODE"))+"]";
						balance = this.getBalance(String.valueOf(sumAmtSubMap.get("USER_ID"))
								, String.valueOf(sumAmtSubMap.get("PRODUCT_ID"))
								, String.valueOf(sumAmtSubMap.get("MERCHANT_CODE"))
								, paramString);
						if(balance == null) {
							logger.info("调用账户接口获取余额失败!返回结果为null " + argMsg);
							mailContext.append("&nbsp;&nbsp;调用账户接口获取余额失败!返回结果为null " + argMsg);
							mailContext.append("<BR>");
							amountBalance = 0l;
						} else {
							//账户提现余额
							amountBalance = balance.getBalanceSettle();
							mailContext.append("&nbsp;&nbsp;调用账户接口获取余额成功!返回结果为null "+ argMsg +"，账户余额为：["+amountBalance+"]");
							mailContext.append("<BR>");
						}
					} catch (Exception e) {
						e.printStackTrace();
						logger.error("调用账户接口获取余额失败!查询余额方法异常  " + argMsg);
						mailContext.append("&nbsp;&nbsp;调用账户接口获取余额失败!返回结果为null " + argMsg);
						mailContext.append("<BR>");
						amountBalance = 0l;
					}
					if (shAmt > amt + amountBalance) {
						mailContext.append("&nbsp;&nbsp;金额不满足条件：["+sumAmtSubMap.get("USER_ID").toString() + "]的应还款金额["+shAmt + "],还款金额["+(amt+amountBalance)+"],差额["+(shAmt-(amt+amountBalance))+"]");
						mailContext.append("<BR>--------------------------------------------------------");
						mailContext.append("<BR>");
						continue;
//						if(this.isPartialPay(String.valueOf(sumAmtSubMap.get("MERCHANT_CODE")) + String.valueOf(sumAmtSubMap.get("USER_ID")))) {//部分还款
//							mailContext.append("&nbsp;&nbsp;金额不满足条件， 但支持部分还款：["+sumAmtSubMap.get("USER_ID").toString() + "]的应还款金额["+shAmt + "],还款金额["+(amt+amountBalance)+"]");
//							mailContext.append("<BR>");
//							shAmt = amt + amountBalance;
//							accAmt = amountBalance;
//						} else {//不支持部分还款
//							mailContext.append("&nbsp;&nbsp;金额不满足条件：["+sumAmtSubMap.get("USER_ID").toString() + "]的应还款金额["+shAmt + "],还款金额["+(amt+amountBalance)+"],差额["+(shAmt-(amt+amountBalance))+"]");
//							mailContext.append("<BR>");
//							continue;
//						}
					} else {
						accAmt = shAmt - amt;
						mailContext.append("&nbsp;&nbsp;金额满足条件：["+sumAmtSubMap.get("USER_ID").toString() + "],但还应从账户支出：["+accAmt+"]");
						mailContext.append("<BR>");
					}
			    }
			    mailContext.append("&nbsp;&nbsp;金额满足条件：["+sumAmtSubMap.get("USER_ID").toString() + "]的应还款金额["+shAmt + "],还款金额["+amt+"]");
			    mailContext.append("<BR>--------------------------------------------------------");
			    mailContext.append("<BR>");
				totAmtMap.put(keyStr, shAmt);
				accAmtMap.put(keyStr, accAmt);
				amtList.add(keyStr);
			}
		} catch (Exception e) {
			String msg = "查询应还款金额, 异常!";
			logger.error(">>> " + msg, e);
			this.editResultMap(resultMap, "-1", msg);
			e.printStackTrace();
			mailContext.append(e.getMessage());
			this.sendMail("POS收单异常-"+mailDate, e.getMessage(), "POSRES", null);
			return resultMap;
		}
		mailContext.append("<BR>&nbsp;4.调用线下还款接口&账户接口");
		mailContext.append("<BR>");
		logger.info(">>> 满足还款条件的："+ amtList.size() +"条");
		mailContext.append("&nbsp;&nbsp;满足还款条件的："+ amtList.size() +"条");
		mailContext.append("<BR>");
		String listKey = null;
		
		try {
			for (int i = 0; i < amtList.size(); i++) {
				listKey = amtList.get(i);
				sumAmtSubMap = sumAmtMap.get(listKey);
				amt = totAmtMap.get(listKey);
				accAmt = accAmtMap.get(listKey);
				String productId = productIdMap.get(listKey);//主账户productId
				String intoProductId = intoProductIdMap.get(listKey);//待清算productId
				/*
				 * 调用计息更新状态接口
				 */
				try {
					if (amt != 0) {
						subRtnMap = this.updateStatusByRecord(sumAmtSubMap.get("MERCHANT_CODE").toString(), sumAmtSubMap.get("USER_ID").toString(), accountDate, amt);
					}
				} catch (Exception e) {
					logger.error(">>> " + "更新计息接口调用异常！");
					mailContext.append("&nbsp;&nbsp;计息异常: ["+sumAmtSubMap.get("MERCHANT_CODE").toString()+"|" + sumAmtSubMap.get("USER_ID").toString() + "]调用计息和账户都失败"+"计息参数：["+ymdformat.format(accountDate)+"|"+amt+"]账户参数：["+ sumAmtSubMap.get("INTO_USER_ID") +"|"+ productId +"|"+sumAmtSubMap.get("AMOUNT").toString()+"|1]");
					mailContext.append("<BR>");
					e.printStackTrace();
				}
				if ((subRtnMap == null || "false".equals(subRtnMap.get("code").toString()))) {
			    	logger.info("计息失败: ["+sumAmtSubMap.get("MERCHANT_CODE").toString()+"|" + sumAmtSubMap.get("USER_ID").toString() + "]调用计息失败; 计息参数：["+ymdformat.format(accountDate)+"|"+amt+"]");
			    	mailContext.append("&nbsp;&nbsp;["+sumAmtSubMap.get("MERCHANT_CODE").toString()+"|" + sumAmtSubMap.get("USER_ID").toString() + "]调用计息失败; 计息参数：["+ymdformat.format(accountDate)+"|"+amt+"]");
			    } else if ("true".equals(subRtnMap.get("code").toString())) {
			    	logger.info("计息成功: ["+sumAmtSubMap.get("MERCHANT_CODE").toString()+"|" + sumAmtSubMap.get("USER_ID").toString() + "]调用计息成功; 计息参数：["+ymdformat.format(accountDate)+"|"+amt+"]");
			    	mailContext.append("&nbsp;&nbsp;["+sumAmtSubMap.get("MERCHANT_CODE").toString()+"|" + sumAmtSubMap.get("USER_ID").toString() + "]调用计息成功; 计息参数：["+ymdformat.format(accountDate)+"|"+amt+"]");
			    }
				mailContext.append("<BR>");
				/*
				 * 账户转账
				 */
				if (accAmt > 0) {
					mailContext.append("&nbsp;&nbsp;账户转账: ["+accAmt+"]>0所以进入转账流程");
					mailContext.append("<BR>");
					try {
						subARtnMap = this.transferInCommon(accAmt, sumAmtSubMap.get("USER_ID").toString(), sumAmtSubMap.get("INTO_USER_ID").toString(),  sumAmtSubMap.get("MERCHANT_CODE").toString(), productId, intoProductId);
						if((subARtnMap == null || "0".equals(subARtnMap.get("code").toString()))) {
							mailContext.append("&nbsp;&nbsp;账户转账: ["+sumAmtSubMap.get("MERCHANT_CODE").toString()+"|" + sumAmtSubMap.get("USER_ID").toString()+"|"+  sumAmtSubMap.get("INTO_USER_ID").toString()  +"|" + accAmt+"|"+ productId +"|"+ intoProductId +"],["+ subARtnMap.get("code") +"|"+ subARtnMap.get("msg") +"]调用失败");
						} else {
							mailContext.append("&nbsp;&nbsp;账户转账: ["+sumAmtSubMap.get("MERCHANT_CODE").toString()+"|" + sumAmtSubMap.get("USER_ID").toString()+"|"+  sumAmtSubMap.get("INTO_USER_ID").toString()  +"|" + accAmt+"|"+ productId +"|"+ intoProductId +"],["+ subARtnMap.get("code") +"|"+ subARtnMap.get("msg") +"]调用完成!");
						}
					} catch (Exception e) {
						logger.error(">>> " + "更新账户接口调用异常！");
						mailContext.append("&nbsp;&nbsp;账户异常: ["+sumAmtSubMap.get("MERCHANT_CODE").toString()+"|" + sumAmtSubMap.get("USER_ID").toString()+"|"+ sumAmtSubMap.get("INTO_USER_ID").toString() +"|" + accAmt+"|"+ productId +"|"+ intoProductId +"]调用异常");
					    e.printStackTrace();
					}
				} else {
					mailContext.append("&nbsp;&nbsp;账户转账: ["+accAmt+"]<=0所以不进行主账户转账");
				}
				mailContext.append("<BR>--------------------------------------------------------");
				mailContext.append("<BR>");
			}
		} catch (Exception e) {
			String msg = "调用账户，计息, 异常!";
			logger.error(">>> " + msg);
			this.editResultMap(resultMap, "-1", msg);
			e.printStackTrace();
			mailContext.append("&nbsp;&nbsp;"+e.getMessage());
			this.sendMail("POS收单异常-"+mailDate, mailContext.toString(), "POSRES", null);
			return resultMap;
		}
		
		mailContext.append("<BR>&nbsp;5.编辑还款金额");
		mailContext.append("<BR>");
		List<String> loanList = new ArrayList<String>();
		keyG = "";
		keyS = "";
		SettleLoanDetail tmpBeanG = null;
		SettleLoanDetail tmpBeanS = null;
		BigDecimal settleamt1 = null;// 用于结算的金额
		try {
			for (int i = 0; i < amtList.size(); i++) {
				tmpBeanG = new SettleLoanDetail();
				tmpBeanS = new SettleLoanDetail();
				listKey = amtList.get(i);
				//对公对私判断
				keyG = listKey + 0;//对公
				keyS = listKey + 1;//对私
				sumAmtSubMap = sumAmtMap.get(listKey);
				amt = totAmtMap.get(listKey);
				accAmt = accAmtMap.get(listKey);
				if (keyMap.containsKey(keyS)) {
					tmpBeanS = keyMap.get(keyS);
					tmpBeanS.setAmount(tmpBeanS.getAmount()+accAmt);
					addamt = new BigDecimal(tmpBeanS.getAmount());//对私金额
					if (keyMap.containsKey(keyG)) {
						tmpBeanG = keyMap.get(keyG);
						settleamt = new BigDecimal(tmpBeanG.getAmount());//对公金额
					} else {
						settleamt = new BigDecimal(0l);//对公金额
					}
				} else {
					addamt = new BigDecimal(0l);//对私金额
					tmpBeanS.setAmount(0l);		//如果没有对私交易 对私结算金额为0
					if (keyMap.containsKey(keyG)) {
						tmpBeanG = keyMap.get(keyG);
						tmpBeanG.setAmount(tmpBeanG.getAmount()+accAmt);
						settleamt = new BigDecimal(tmpBeanG.getAmount());//对公金额
					} else {
						settleamt = new BigDecimal(0l);//对公金额
					}
				}
				settleamt = settleamt.add(addamt);
				addamt1 = new BigDecimal(amt);//应还金额
				settleamt1 = settleamt.subtract(addamt1);//总还款金额-应还款金额
				if (settleamt1.longValue() == 0) {
					tmpBeanS.setShouldCapital(tmpBeanS.getAmount());
					tmpBeanG.setShouldCapital(tmpBeanG.getAmount());
					loanList.add(keyG);
					loanList.add(keyS);
				} else if (amt >= tmpBeanS.getAmount()) {
					/**
					 * 如果包含对私交易 然后生成对私还款信息
					 */
					if(keyMap.get(keyS) != null) {
						tmpBeanS.setShouldCapital(tmpBeanS.getAmount());
						loanList.add(keyS);
					}
					tmpBeanG.setShouldCapital(amt-tmpBeanS.getAmount());
					loanList.add(keyG);
				} else if (amt < tmpBeanS.getAmount()) {
					tmpBeanS.setShouldCapital(amt);
					tmpBeanG.setShouldCapital(0l);
					loanList.add(keyG);
					loanList.add(keyS);
				}
			}
		} catch (Exception e) {
			String msg = "编辑应还本金， 异常!";
			logger.error(">>> " + msg);
			this.editResultMap(resultMap, "-1", msg);
			e.printStackTrace();
			this.sendMail("POS收单异常-"+mailDate, e.getMessage(), "POSRES", null);
			return resultMap;
		}

		mailContext.append("<BR>&nbsp;6.生成还款数据");
		mailContext.append("<BR>");
		tmpBean = new SettleLoanDetail();
		try {
			for (int i = 0; i < loanList.size(); i++) {
				listKey = loanList.get(i);
				if (keyMap.containsKey(listKey)) {
					tmpBean = keyMap.get(listKey);
					if (tmpBean.getAmount() == 0) {
						continue;
					}
					tmpBean.setLoanId(Long.parseLong(String.valueOf(System.currentTimeMillis()).toString().substring(3,13)));
					tmpBean.setRepaymentDate(new Date());
					settleLoanDetailManager.saveSettleLoanDetail(tmpBean);
				}
			}
		} catch (Exception e) {
			String msg = "编辑应还本金， 异常!";
			logger.error(">>> " + msg);
			this.editResultMap(resultMap, "-1", msg);
			e.printStackTrace();
			this.sendMail("POS收单异常-"+mailDate, e.getMessage(), "POSRES", null);
			return resultMap;
		}		
		/*
		 * 调订单落还款单
		 */
		mailContext.append("<BR>&nbsp;7.生成还款订单");
		mailContext.append("<BR>");
		M000003ServiceBean parm = new M000003ServiceBean();
		Map<String, Object> rtnOrderMap = null;
		try {
			for (int i = 0; i < amtList.size(); i++) {
				listKey = amtList.get(i);
				sumAmtSubMap = sumAmtMap.get(listKey);
				parm = new M000003ServiceBean();
				Long shAmt1 = totAmtMap.get(listKey);
				if (shAmt1 == 0 ) {
					continue;
				}
				String userOrderId = "POS"+ymdIdformat.format(new Date());
				Date nwDate = new Date();
				parm.setUserId(sumAmtSubMap.get("USER_ID").toString());
				parm.setAmount(String.valueOf(shAmt1));
				parm.setConstid(sumAmtSubMap.get("MERCHANT_CODE").toString());
				parm.setOrderTypeId(ORDER_TYPE_REPAY);
				parm.setProductId(sumAmtSubMap.get("PRODUCT_ID").toString());
				parm.setOpertype("1");
				parm.setOrderDate(ymdformat.format(nwDate));
				parm.setOrderTime(ymdformat.format(nwDate));
				parm.setUserOrderId(userOrderId);
				
				Map<String, String> map1 = new HashMap<String, String>();  
		        map1.put("CUSTOMER_NO", sumAmtSubMap.get("USER_ID").toString());
		        map1.put("REPAYMENT_SERIAL_NO", userOrderId);
		        map1.put("REPAYMENT_AMOUNT", String.valueOf(shAmt1));
		        map1.put("REPAYMENT_DATE", ymdForYMDformat.format(nwDate));
		        map1.put("REPAYMENT_TIME", ymdForHMSformat.format(nwDate));
		        map1.put("UNDERLIE_USE_CREDIT", "0");
		        map1.put("UNDERLIE_ID", "");
		        map1.put("FINANCIAL_MERCHANT_ID", factorUserIdMap.get(listKey));//放款机构USER_ID
		        map1.put("FINANCIAL_MERCHANT_NAME", factorTitleMap.get(listKey));//放款机构名称
				JSONArray jmap = new JSONArray();
		        jmap.add(map1);  
		        String jsonStr = jmap.toString();
		        jsonStr = jsonStr.replace("[{", "{").replace("}]", "}");
		        parm.setRemark(jsonStr);
				
				rtnOrderMap = this.saveServiceOrder(parm);
				if (rtnOrderMap == null || !"true".equals(rtnOrderMap.get("code"))) {
					logger.info("失败: 订单落还款单，：["+parm.getUserId() + "|" + parm.getAmount() + "|" + parm.getConstid() + "|" + parm.getOrderTypeId() + "|" + parm.getOpertype() + "|" + parm.getUserOrderId() + "|" + parm.getOrderDate() +"|" + parm.getRemark()  + "]");
					mailContext.append("&nbsp;&nbsp;失败: 订单落还款单，：["+parm.getUserId() + "|" + parm.getAmount() + "|" + parm.getConstid() + "|" + parm.getOrderTypeId() + "|" + parm.getOpertype() + "|" + parm.getUserOrderId() + "|" + parm.getOrderDate() +"|" + parm.getRemark()  + "|" +parm.getProductId() + "]"+rtnOrderMap.get("msg"));
					mailContext.append("<BR>");
				} else if ("true".equals(rtnOrderMap.get("code"))) {
					logger.info("成功: 订单落还款单，：["+ parm.getUserId() + "|" + parm.getAmount() + "|" + parm.getConstid() + "|" + parm.getOrderTypeId() + "|" + parm.getOpertype() + "|" + parm.getUserOrderId() + "|" + parm.getOrderDate() + "]订单返回订单号：["+rtnOrderMap.get("orderid")+"]");
					mailContext.append("&nbsp;&nbsp;成功: 订单落还款单，：["+ parm.getUserId() + "|" + parm.getAmount() + "|" + parm.getConstid() + "|" + parm.getOrderTypeId() + "|" + parm.getOpertype() + "|" + parm.getUserOrderId() + "|" + parm.getOrderDate() + "]订单返回订单号：["+rtnOrderMap.get("orderid")+"]");
					mailContext.append("<BR>");
				}
			}
		} catch (Exception e) {
			String msg = "生成还款订单， 异常!";
			logger.error(">>> " + msg);
			this.editResultMap(resultMap, "-1", msg);
			e.printStackTrace();
			this.sendMail("POS收单异常-"+mailDate, e.getMessage(), "POSRES", null);
			return resultMap;
		}
		//调订单落结算单
		try {
			for (int i = 0; i < amtList.size(); i++) {
				listKey = amtList.get(i);
				sumAmtSubMap = sumAmtMap.get(listKey);
				parm = new M000003ServiceBean();
				Long shAmt1 = totAmtMap.get(listKey);
				Long Amt1 = (Long)sumAmtSubMap.get("AMOUNT");
				
				parm.setUserId(sumAmtSubMap.get("USER_ID").toString());
				if (Amt1 - shAmt1 <= 0 ) {
					continue;
				}
				String userOrderId = "POS"+ymdIdformat.format(new Date());
				Date nwDate = new Date();
				Long shAmt2 = Amt1 - shAmt1;
				parm.setAmount(String.valueOf(shAmt2));
				parm.setConstid(sumAmtSubMap.get("MERCHANT_CODE").toString());
				parm.setOrderTypeId(ORDER_TYPE_SETTLE);
				parm.setProductId(sumAmtSubMap.get("PRODUCT_ID").toString());
				parm.setOpertype("1");
				parm.setOrderDate(ymdformat.format(nwDate));
				parm.setOrderTime(ymdformat.format(nwDate));
				parm.setUserOrderId(userOrderId);
				parm.setGoodsDetail(tmpBean.getSettleFlg());
				Map<String, String> map1 = new HashMap<String, String>();  
		        map1.put("CUSTOMER_NO", sumAmtSubMap.get("USER_ID").toString());
		        map1.put("SETTLE_SERIAL_NO", userOrderId);
		        map1.put("SETTLE_AMOUNT", String.valueOf(shAmt2));
		        map1.put("SETTLE_FEE", "0");
		        map1.put("SETTLE_DATE", ymdForYMDformat.format(nwDate));
		        map1.put("SETTLE_TIME", ymdForHMSformat.format(nwDate));
		        map1.put("SERIAL_NO", "");
				JSONArray jmap = new JSONArray();  
		        jmap.add(map1);  
		        String jsonStr = jmap.toString();
		        jsonStr = jsonStr.replace("[{", "{").replace("}]", "}");
		        parm.setRemark(jsonStr);
				
				rtnOrderMap = this.saveServiceOrder(parm);
				if (rtnOrderMap == null || !"true".equals(rtnOrderMap.get("code"))) {
					logger.info("失败: 订单落结算单，：["+parm.getUserId() + "|" + parm.getAmount() + "|" + parm.getConstid() + "|" + parm.getOrderTypeId() + "|" + parm.getOpertype() + "|" + parm.getUserOrderId() + "|" + parm.getOrderDate() +"|" + parm.getRemark() + "]");
					mailContext.append("&nbsp;&nbsp;失败: 订单落结算单，：["+parm.getUserId() + "|" + parm.getAmount() + "|" + parm.getConstid() + "|" + parm.getOrderTypeId() + "|" + parm.getOpertype() + "|" + parm.getUserOrderId() + "|" + parm.getOrderDate() +"|" + parm.getRemark()  + "|" +parm.getProductId() + "]"+rtnOrderMap.get("msg"));
					mailContext.append("<BR>");
				} else if ("true".equals(rtnOrderMap.get("code"))) {
					logger.info("成功: 订单落结算单，：["+ parm.getUserId() + "|" + parm.getAmount() + "|" + parm.getConstid() + "|" + parm.getOrderTypeId() + "|" + parm.getOpertype() + "|" + parm.getUserOrderId() + "|" + parm.getOrderDate() + "]订单返回订单号：["+rtnOrderMap.get("orderid")+"]");
					mailContext.append("&nbsp;&nbsp;成功: 订单落结算单，：["+ parm.getUserId() + "|" + parm.getAmount() + "|" + parm.getConstid() + "|" + parm.getOrderTypeId() + "|" + parm.getOpertype() + "|" + parm.getUserOrderId() + "|" + parm.getOrderDate() + "]订单返回订单号：["+rtnOrderMap.get("orderid")+"]");
					mailContext.append("<BR>");
				}
			}
		} catch (Exception e) {
			String msg = "生成结算订单， 异常!";
			logger.error(">>> " + msg);
			this.editResultMap(resultMap, "-1", msg);
			e.printStackTrace();
			this.sendMail("POS收单异常-"+mailDate, e.getMessage(), "POSRES", null);
			return resultMap;
		}
		
//		RkylinMailUtil.sendMail("POS收单结束-"+mailDate, mailContext.toString(), mailTo, mailCc);
		File attachment = createAttachmentFile(mailContext.toString());
		this.sendMail("POS收单结束-"+mailDate, "POS收单处理交易笔数: " + settlePosDetailList.size(), "POSRES", attachment);
		return resultMap;
	}
	/***
	 * 更新 分润结果表状态 为'已结算'
	 * @param idList
	 */
	private void updatePosStatus(List<Integer> idList , String remark,int statusId,int deliverStatusId,String transFlowNo) {
		logger.info(">>> >>> >>> >>> 更新 收单表状态 为'以生成还款数据'");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("transFlowNo", transFlowNo);//已结算
		map.put("deliverStatusId", deliverStatusId);//已结算
		map.put("remark", remark);//已结算
		map.put("idList", idList);
		settlePosDetailManager.updateTransStatusId(map);
	}
	/***
	 * 查询全部处理过的清分交易
	 * @return
	 */
	public List<Map<String,Object>> getPosDetailForProfit(Date settleTime) {
		logger.info(">>> >>> >>> >>> 查询全部未处理的收单交易!");
		SettlePosDetailQuery query = new SettlePosDetailQuery();
		query.setDeliverStatusId(1);//没有操作过的数据
		query.setAccountDate(settleTime);
		query.setStatusId(21);//平账
		return settlePosDetailManager.queryPosForProfitList(query);
	}
	/**
	 * 判断此UserId的还款交易是否支持部分还款
	 * @param userId
	 * @return
	 */
//	private Boolean isPartialPay(String key) {
//		//return (Constants.RSQB_ID + USER_ID_HTGL).equals(key);
//		/*
//		 * 暂无 部分还款业务
//		 */
//		return false;
//	}
	/**
	 * AccountInfo对应关系
	 * @param userId
	 * @return
	 */
	private Map<String, Map<String, String>> getAccountInfoMap() throws Exception {
		Map<String, Map<String, String>> accountInfoMap = new HashMap<String, Map<String, String>>();
		SettleParameterInfoQuery query = new SettleParameterInfoQuery();
		query.setParameterType(SettleConstants.PARAMETER_TYPE_ACCOUNT_INFO);
		query.setStatusId(1);
		List<SettleParameterInfo> list = settleParameterInfoManager.queryList(query);
		if(list == null || list.size() < 1) throw new Exception("查询贷款还款账户信息0条");
		Map<String, String> sAProductIdMap = new HashMap<String, String>();
		Map<String, String> sAUserIdMap = new HashMap<String, String>();
		Map<String, String> factorUserIdMap = new HashMap<String, String>();
		Map<String, String> factorTitleMap = new HashMap<String, String>();
		Map<String, String> factorProductIdMap = new HashMap<String, String>();
		Map<String, String> userIdMap = new HashMap<String, String>();
		Map<String, String> merchantMap = new HashMap<String, String>();
		Map<String, String> productIdMap = new HashMap<String, String>();
		
		String merchantCode = null;
		String userId = null;
		String productId = null;
		String sAProductId = null;
		String sAUserId = null;
		String factorUserId = null;
		String factorTitle = null;
		String factorProductId = null;
		for(SettleParameterInfo parameterInfo : list) {
			merchantCode = parameterInfo.getProductId();
			userId = parameterInfo.getParameterCode();
			sAUserId = parameterInfo.getParameterValue();
			productId = parameterInfo.getObligate1();
			sAProductId = parameterInfo.getObligate2();
			factorUserId = parameterInfo.getObligate3();
			factorTitle = parameterInfo.getRemark();
			factorProductId = MAIN_ACCOUNT_PRODUCT_ID;
			if(factorTitle.indexOf(SPLIT_STR) > -1) {
				factorProductId = factorTitle.split(SPLIT_STR)[1];
				factorTitle = factorTitle.split(SPLIT_STR)[0];
			}
			sAProductIdMap.put(merchantCode + userId, sAProductId);
			sAUserIdMap.put(merchantCode + userId, sAUserId);
			factorUserIdMap.put(merchantCode + userId, factorUserId);
			factorProductIdMap.put(merchantCode + userId, factorProductId);
			factorTitleMap.put(merchantCode + userId, factorTitle);
			merchantMap.put(merchantCode + userId, merchantCode);
			userIdMap.put(merchantCode + userId, userId);
			productIdMap.put(merchantCode + userId, productId);
		}		
		accountInfoMap.put("sAProductIdMap", sAProductIdMap);
		accountInfoMap.put("sAUserIdMap", sAUserIdMap);
		accountInfoMap.put("factorUserIdMap", factorUserIdMap);
		accountInfoMap.put("factorProductIdMap", factorProductIdMap);
		accountInfoMap.put("factorTitleMap", factorTitleMap);
		accountInfoMap.put("merchantMap", merchantMap);
		accountInfoMap.put("userIdMap", userIdMap);
		accountInfoMap.put("productIdMap", productIdMap);
		return accountInfoMap;
	}
	/**
	 * POS + UserId.substring(12) + 对公/私]
	 * 生成POS订单号 取订单号12索引之前
	 * @param userId
	 * @return
	 */
	private String genPosOrderNo(String prefix, String userId, String payWay) {
		userId = userId.length() < 13  ? userId : userId.substring(0, 12);
		return prefix + userId + payWay;
	}
	/**
	 * 查询账户余额
	 */
	private Balance getBalance(String userId, String productId, String constId, String paramString) throws Exception {
		com.rkylin.wheatfield.pojo.User user = new com.rkylin.wheatfield.pojo.User();
		user.userId = String.valueOf(userId);
		user.productId = String.valueOf(productId);
		user.constId = String.valueOf(constId);
		return paymentAccountServiceApi.getBalance(user, paramString);
	}
	/**
	 * 查询POS交易中USER_ID(供应商)对应的账户信息并更新到数据库中
	 */
	private void queryAccountInfoByPosDetail(List<Map<String,Object>> settlePosDetailList) throws Exception {
		logger.info(">>> >>> >>> 开始:查询POS交易中USER_ID(供应商)对应的账户信息并更新到数据库中");
		if(settlePosDetailList == null || settlePosDetailList.size() < 1) {
			logger.info("<<< <<< <<< 结束:查询POS交易中USER_ID(供应商)对应的账户信息并更新到数据库中 settlePosDetailList is Null or settlePosDetailList.size() < 1");
			return; 
		}
		/*
		 * 1.从当日还款交易中汇总账户信息
		 */
		String rootInstCd = null;				//机构号
		String userId = null;					//供应商UserId
		String productId = null;				//供应商主账户管理分组
		String sAUserId = null;					//供应商待清算UserId
		String sAProductId = SA_PRODUCT_ID;		//供应商待清算管理分组
		String factorUserId = null;				//保理公司UserId
		String factorProductId = null;			//保理公司管理分组
		String factorTitle = null;				//保理公司名称
		Map<String, Map<String, String>> accountInfoMap = new HashMap<String, Map<String, String>>();	//交易中包含的账户信息结构体
		Map<String, String> itemMap = null;		//交易中包含的账户信息结构体单例
		String keyStr = null;					//rootInstCd + userId作为交易中包含的账户信息结构体的key
		settlePosDetailListForEach:
		for(Map<String,Object> settlePosDetailMap : settlePosDetailList) {
			/*
			 * 因:保理云之前的还款机构使用约定的保理账户,不从交易中获取.
			 * 故:配置了非保理云的机构号数组常量CONFIRMED_MERCHANT_CODE以此逻辑判断
			 */
			rootInstCd = String.valueOf(settlePosDetailMap.get("MERCHANT_CODE"));
			for(String confirmedMerchantCode : CONFIRMED_MERCHANT_CODE) {
				if(confirmedMerchantCode.equals(rootInstCd)) continue settlePosDetailListForEach;
			}
			userId = sAUserId = String.valueOf(settlePosDetailMap.get("USER_ID"));
			productId = String.valueOf(settlePosDetailMap.get("PRODUCT_ID"));
			factorUserId = String.valueOf(settlePosDetailMap.get("INTER_MERCHANT_CODE"));
			factorProductId = String.valueOf(settlePosDetailMap.get("INTO_INST_CODE"));
			factorTitle = String.valueOf(settlePosDetailMap.get("OBLIGATE2"));
			itemMap = new HashMap<String, String>();
			itemMap.put("rootInstCd", rootInstCd);
			itemMap.put("userId", userId);
			itemMap.put("productId", productId);
			itemMap.put("sAUserId", sAUserId);
			itemMap.put("sAProductId", sAProductId);
			itemMap.put("factorUserId", factorUserId);
			itemMap.put("factorProductId", factorProductId);
			itemMap.put("factorTitle", factorTitle);
			keyStr = rootInstCd + userId;
			if(accountInfoMap.containsKey(keyStr)) continue settlePosDetailListForEach;
			accountInfoMap.put(keyStr, itemMap);
		}
		/*
		 * accountInfoMap为空表示交易中没有保理云的供应商的还款交易
		 */
		if(accountInfoMap.size() < 1) {
			logger.info("<<< <<< <<< 结束:交易中保理云的供应商的还款交易为0条");
			return;
		}
		/*
		 * 2.查询数据库已配置的账户信息并更新
		 */
		SettleParameterInfoQuery query = new SettleParameterInfoQuery();
		query.setParameterType(SettleConstants.PARAMETER_TYPE_ACCOUNT_INFO);
		// query.setStatusId(1);
		List<SettleParameterInfo> settleParameterInfoList = settleParameterInfoManager.queryList(query);
		SettleParameterInfo settleParameterInfo = null;
		if(settleParameterInfoList == null || settleParameterInfoList.size() < 1) {
			logger.info(">>> >>> >>> settleParameterInfo中已存在的还款账户信息 is Null or 还款账户信息0条");
			for(String key : accountInfoMap.keySet()) {
				itemMap = accountInfoMap.get(key);
				settleParameterInfo = new SettleParameterInfo();
				settleParameterInfo.setParameterType(SettleConstants.PARAMETER_TYPE_ACCOUNT_INFO);
				settleParameterInfo.setProductId(itemMap.get("rootInstCd"));
				settleParameterInfo.setParameterCode(itemMap.get("userId"));
				settleParameterInfo.setParameterValue(itemMap.get("sAUserId"));
				settleParameterInfo.setObligate1(itemMap.get("productId"));
				settleParameterInfo.setObligate2(itemMap.get("sAProductId"));
				settleParameterInfo.setObligate3(itemMap.get("factorUserId"));
				settleParameterInfo.setRemark(itemMap.get("factorTitle") + SPLIT_STR + itemMap.get("factorProductId"));
				settleParameterInfo.setStatusId(1);
				settleParameterInfoManager.saveSettleParameterInfo(settleParameterInfo);
			}
		} else {
			accountInfoMapkeySetForEach : 
			for(String key : accountInfoMap.keySet()) {
				itemMap = accountInfoMap.get(key);
				for(SettleParameterInfo itemParam : settleParameterInfoList) {
					keyStr = itemParam.getProductId() + itemParam.getParameterCode();
					if(key.equals(keyStr)) {
						itemParam.setParameterType(SettleConstants.PARAMETER_TYPE_ACCOUNT_INFO);
						itemParam.setProductId(itemMap.get("rootInstCd"));
						itemParam.setParameterCode(itemMap.get("userId"));
						itemParam.setParameterValue(itemMap.get("sAUserId"));
						itemParam.setObligate1(itemMap.get("productId"));
						itemParam.setObligate2(itemMap.get("sAProductId"));
						itemParam.setObligate3(itemMap.get("factorUserId"));
						itemParam.setRemark(itemMap.get("factorTitle") + SPLIT_STR + itemMap.get("factorProductId"));
						settleParameterInfoManager.updateSettleParameterInfo(itemParam);
						continue accountInfoMapkeySetForEach;
					}
				}
				settleParameterInfo = new SettleParameterInfo();
				settleParameterInfo.setParameterType(SettleConstants.PARAMETER_TYPE_ACCOUNT_INFO);
				settleParameterInfo.setProductId(itemMap.get("rootInstCd"));
				settleParameterInfo.setParameterCode(itemMap.get("userId"));
				settleParameterInfo.setParameterValue(itemMap.get("sAUserId"));
				settleParameterInfo.setObligate1(itemMap.get("productId"));
				settleParameterInfo.setObligate2(itemMap.get("sAProductId"));
				settleParameterInfo.setObligate3(itemMap.get("factorUserId"));
				settleParameterInfo.setRemark(itemMap.get("factorTitle") + SPLIT_STR + itemMap.get("factorProductId"));
				settleParameterInfo.setStatusId(1);
				settleParameterInfoManager.saveSettleParameterInfo(settleParameterInfo);
			}
		}
		logger.info("<<< <<< <<< 结束:查询POS交易中USER_ID(供应商)对应的账户信息并更新到数据库中");
	}
	/**编辑邮件附件, 显示还款结算信息
	 * @param content	邮件副本内容
	 * @return
	 * @throws Exception
	 */
	private File createAttachmentFile(String content) throws Exception {
		BufferedWriter bw = null;		//输出流
    	File folderFile = null;			//文件目录对象
    	File targetFile = null;			//文件对象
    	SimpleDateFormat sdf = null;	//日期格式yyyyMMddHHmmss
    	String folderName = null;		//文件目录名称
    	String targetFileName = null;	//文件名称
    	StringBuffer head = null;		//文件内容头
    	StringBuffer tail = null;		//文件内容尾
    	
		sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		folderName = SettleConstants.MAIL_FILE_PATH + File.separator + sdf.format(new Date());
		targetFileName = ATTACHMENT_NAME;//附件为htm文件
		folderFile = new File(folderName);
		
		head = new StringBuffer();
		tail = new StringBuffer();
		head.append("<!DOCTYPE HTML>");
		head.append("<html>");
		head.append("<head>");
		head.append("<meta charset=\"utf-8\">");
		head.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />");
		head.append("</head>");
		head.append("<body>");
		tail.append("</body>");
		tail.append("</html>");
		
		if(!folderFile.exists()) folderFile.mkdirs();
		targetFile = new File(folderFile, targetFileName);
		if(!targetFile.exists()) targetFile.createNewFile();
		try {
			bw = new BufferedWriter(new FileWriter(targetFile));
    		bw.write(head.toString() + content + tail.toString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(bw != null) {
				bw.flush();
				bw.close();
			}
		}
    	
		return targetFile;
	}
}
