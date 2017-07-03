package com.rkylin.settle.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.rkylin.settle.constant.SettleConstants;
import com.rkylin.settle.logic.CollateLogic;
import com.rkylin.settle.manager.SettleParameterInfoManager;
import com.rkylin.settle.manager.SettleProfitKeyManager;
import com.rkylin.settle.manager.SettleProfitRuleManager;
import com.rkylin.settle.pojo.SettleParameterInfo;
import com.rkylin.settle.pojo.SettleParameterInfoQuery;
import com.rkylin.settle.pojo.SettleProfitKey;
import com.rkylin.settle.pojo.SettleProfitKeyQuery;
import com.rkylin.settle.pojo.SettleProfitRule;
import com.rkylin.settle.pojo.SettleProfitRuleQuery;

/***
 * 逻辑常量工具类
 * @author Yang
 */
@Component("logicConstantUtil")
public class LogicConstantUtil {
	//日志对象
	private static Logger logger = LoggerFactory.getLogger(CollateLogic.class);
	/*
	 * 分润规则List, 将分润规则存储在内存中 
	 */
	private static List<SettleProfitKey> settleProfitKeyList;
	/*
	 * 手续费规则List, 将手续费规则存储在内存中 
	 */
	private static List<SettleProfitKey> feeAmoutKeyList;
	/*
	 * 订单号对应关系List, 将分润规则存储在内存中 
	 */
	private static List<SettleParameterInfo> funcCodeAndOrderNoRelation;
	/*
	 * 订单号对应关系List, 将分润规则存储在内存中 
	 */
	private static List<SettleParameterInfo> funcCodeAndAmountRelation;
	/*
	 * deal_product_code(账户)和func_code(清算)对应关系List, 存储在内存中 
	 */
	private static List<SettleParameterInfo> dealProductCodeTofuncCode;	
	/*
	 * 手续费规则List, 将手续费规则存储在内存中
	 */
	private static List<SettleProfitKey> settleFeeAmountKeyList;
	/*
	 * 多渠道ChannelHome和清结算PayChannelId对应关系 
	 */
	private static List<SettleParameterInfo> payChannelIdList;
	
	@Autowired
	@Qualifier("settleProfitKeyManager")
	private SettleProfitKeyManager settleProfitKeyManager;
	@Autowired
	@Qualifier("settleProfitRuleManager")
	private SettleProfitRuleManager settleProfitRuleManager;
	@Autowired
	@Qualifier("settleParameterInfoManager")
	private SettleParameterInfoManager settleParameterInfoManager;		//'清算'属性表Manager
	
	/***
	 * 获取分润规则
	 * @return 
	 * @throws Exception
	 */
	private List<SettleProfitKey> getSettleProfitKeyList() throws Exception {
		SettleProfitKeyQuery query = new SettleProfitKeyQuery();
		query.setStatusId(1);
		return settleProfitKeyManager.selectAllProfitKey(query);
	}
	/***
	 * 获取规则明细
	 * @return 
	 * @throws Exception
	 */
	private LinkedList<SettleProfitRule> getSettleProfitRuleList() throws Exception {
		SettleProfitRuleQuery query = new SettleProfitRuleQuery();
		query.setStatusId(1);
		LinkedList<SettleProfitRule> linkedProfitRuleList = new LinkedList<SettleProfitRule>();
		linkedProfitRuleList.addAll(settleProfitRuleManager.selectAllProfitRule(query));
		return linkedProfitRuleList;
	}
	/***
	 * 建立分润规则与规则明细关系
	 * @throws Exception
	 */
	private List<SettleProfitKey> buildProfitInfoAndRelation() throws Exception {
		//创建容器承装分润信息
		List<SettleProfitKey> keylist = new ArrayList<SettleProfitKey>();
		//获取全部分润信息[有效时间,状态可用]
		List<SettleProfitKey> profitKeyList = getSettleProfitKeyList();
		//获取全部分润信息明细[有效时间,状态可用]
		List<SettleProfitRule> profitRuleList = getSettleProfitRuleList();
		//声明迭代器
		Iterator<SettleProfitKey> keyIter = profitKeyList.iterator();
		//遍历分润规则
		while(keyIter.hasNext()) {
			//分润信息item
			SettleProfitKey profitKey = keyIter.next();
			//分润规则中存储分润明细的容器
			List<SettleProfitRule> ruleList = new ArrayList<SettleProfitRule>();
			//声明迭代器
			Iterator<SettleProfitRule> ruleIter = profitRuleList.iterator();
			//遍历规则明细
			while(ruleIter.hasNext()) {
				SettleProfitRule profitRule = ruleIter.next();
				//匹配关联关系
				if(profitKey.getProfitDetailId().equals(profitRule.getProfitDetailId())) {
					ruleList.add(profitRule);
					//ruleIter.remove();
					//profitRuleList.remove(profitRule);
				}
			}
			profitKey.setSettleProfitRuleList(ruleList);
			keylist.add(profitKey);
		}
		return keylist;
	}
	/***
	 * 获取分润规则和规则明细
	 * @return
	 * @throws Exception
	 */
	public List<SettleProfitKey> getsettleProfitKeyList() throws Exception {
		if(settleProfitKeyList == null) {
			settleProfitKeyList = buildProfitInfoAndRelation();
			logger.info(">>> >>> >>> >>> >>> 分润规则 已经 初始化!!!");
		}
		return settleProfitKeyList;
	}
	/***
	 * 刷新分润规则和规则明细
	 * @throws Exception
	 */
	public void refreshProfitKeyList() throws Exception {
		settleProfitKeyList = buildProfitInfoAndRelation();
		try{
			feeAmoutKeyList = buildFeeAmoutInfoAndRelation();
		}catch(Exception e){
			logger.error("刷新手续费规则和规则明细异常！！！！");
		}
		logger.info(">>> >>> >>> >>> >>> 分润规则 已经 更新!!!");
	}
	/***
	 * 查询订单号与存放位置的对应关系存入内存
	 * @return
	 */
	public List<SettleParameterInfo> getFuncCodeAndOrderNoRelation() {
		if(funcCodeAndOrderNoRelation == null) {
			SettleParameterInfoQuery query = new SettleParameterInfoQuery();
			query.setParameterType(SettleConstants.PARAMETER_TYPE_ACCOUNT_ORDER_NO);
			query.setStatusId(1);
			funcCodeAndOrderNoRelation = settleParameterInfoManager.queryList(query);
			logger.info(">>> >>> >>> >>> >>> 订单号与存放位置的对应关系 已经 初始化!!!");
		}
		return funcCodeAndOrderNoRelation;
	}
	/***
	 * 刷新'订单号'与存放位置的对应关系
	 * @return
	 */
	public void refreshFuncCodeAndOrderNoRelation() {
		SettleParameterInfoQuery query = new SettleParameterInfoQuery();
		query.setParameterType(SettleConstants.PARAMETER_TYPE_ACCOUNT_ORDER_NO);
		query.setStatusId(1);
		funcCodeAndOrderNoRelation = settleParameterInfoManager.queryList(query);
		logger.info(">>> >>> >>> >>> >>> 订单号与存放位置的对应关系 已经 更新!!!");
	} 
	/***
	 * 查询'金额'与存放位置的对应关系存入内存
	 * @return
	 */
	public List<SettleParameterInfo> getFuncCodeAndAmountRelation() {
		if(funcCodeAndAmountRelation == null) {
			SettleParameterInfoQuery query = new SettleParameterInfoQuery();
			query.setParameterType(SettleConstants.PARAMETER_TYPE_ACCOUNT_AMOUNT);
			query.setStatusId(1);
			funcCodeAndAmountRelation = settleParameterInfoManager.queryList(query);
			logger.info(">>> >>> >>> >>> >>> 金额已存放位置的对应关系 已经 初始化!!!");
		}
		return funcCodeAndAmountRelation;
	}
	/***
	 * 刷新'金额'与存放位置的对应关系
	 * @return
	 */
	public void refreshFuncCodeAndAmountRelation() {
		SettleParameterInfoQuery query = new SettleParameterInfoQuery();
		query.setParameterType(SettleConstants.PARAMETER_TYPE_ACCOUNT_AMOUNT);
		query.setStatusId(1);
		funcCodeAndAmountRelation = settleParameterInfoManager.queryList(query);
		logger.info(">>> >>> >>> >>> >>> 金额已存放位置的对应关系 已经 更新!!!");
	}
	/***
	 * 查询'func_code'与deal_product_code的对应关系存入内存
	 * @return
	 */
	public List<SettleParameterInfo> getDealProductCodeTofuncCode() {
		if(dealProductCodeTofuncCode == null) {
			SettleParameterInfoQuery query = new SettleParameterInfoQuery();
			query.setParameterType(SettleConstants.PARAMETER_TYPE_FUN_CODE);
			query.setStatusId(1);
			dealProductCodeTofuncCode = settleParameterInfoManager.queryList(query);
			logger.info(">>> >>> >>> >>> >>> deal_product_code和func_code的对应关系 已经 初始化!!!");
		}
		return dealProductCodeTofuncCode;
	}
	/***
	 * 刷新'func_code'与deal_product_code的对应关系
	 * @return
	 */
	public void refreshDealProductCodeTofuncCode() {
		SettleParameterInfoQuery query = new SettleParameterInfoQuery();
		query.setParameterType(SettleConstants.PARAMETER_TYPE_FUN_CODE);
		query.setStatusId(1);
		dealProductCodeTofuncCode = settleParameterInfoManager.queryList(query);
		logger.info(">>> >>> >>> >>> >>> deal_product_code和func_code的对应关系已经更新!!!");
	}
	
	/***
	 * 获取手续费规则和规则明细
	 * @return
	 * @throws Exception
	 */
	public List<SettleProfitKey> getFeeAmountKeyList() throws Exception {
		if(feeAmoutKeyList == null) {
			feeAmoutKeyList = buildFeeAmoutInfoAndRelation();
			logger.info(">>> >>> >>> >>> >>> 分润规则 已经 初始化!!!");
		}
		return feeAmoutKeyList;
	}
	
	/***
	 * 建立手续费规则与规则明细关系(rule表信息可复用，以此减少该表的配置)
	 * @author youyu
	 * @throws Exception
	 */
	public List<SettleProfitKey> buildFeeAmoutInfoAndRelation() throws Exception {
		//创建容器承装分润信息
		List<SettleProfitKey> keylist = new ArrayList<SettleProfitKey>();
		//获取全部手续费信息[有效时间,状态可用]
		List<SettleProfitKey> profitKeyList = getFeeAmoutKeyList("计算手续费");
		if(profitKeyList == null){
			profitKeyList = getSettleProfitKeyList();
		}
		//手续费明细的容器
		List<SettleProfitRule> ruleList = new ArrayList<SettleProfitRule>();
		SettleProfitRuleQuery ruleQuery = new SettleProfitRuleQuery();//手续费明细的查询条件对象
		ruleQuery.setStatusId(1);
		//声明迭代器
		Iterator<SettleProfitKey> keyIter = profitKeyList.iterator();
		//遍历手续费规则
		while(keyIter.hasNext()) {
			//手续费规则信息item
			SettleProfitKey profitKey = keyIter.next();
			ruleQuery.setProfitDetailId(String.valueOf(profitKey.getProfitDetailId()));
			ruleList = settleProfitRuleManager.queryList(ruleQuery);
			profitKey.setSettleProfitRuleList(ruleList);
			keylist.add(profitKey);
		}
		return keylist;
	}
	/***
	 * 获取手续费规则
	 * @return 
	 * @throws Exception
	 */
	private List<SettleProfitKey> getFeeAmoutKeyList(String remark) throws Exception {
		List<SettleProfitKey> list = null;
		try{
			SettleProfitKeyQuery query = new SettleProfitKeyQuery();
			query.setStatusId(1);
			if(remark !=null && !"".equals(remark)){
				query.setRemark(remark);
			}
			list =  settleProfitKeyManager.queryList(query);
		}catch(Exception e){
			logger.info("===="+e.getMessage());
		}
		return list;
	}
	/***
	 * 获取多渠道ChannelHome 和 清结算 PayChannelId 对应关系
	 * @return
	 */
	public List<SettleParameterInfo> getMultiGateChannelHome2PayChannelId() {
		//payChannelIdList不为null 直接返回payChannelIdList
		if(payChannelIdList != null) return payChannelIdList;
		
		//查询'多渠道渠道商'和'清算渠道编码'的对应关系
		SettleParameterInfoQuery paramQuery = new SettleParameterInfoQuery();
		paramQuery.setParameterType(SettleConstants.PARAMETER_TYPE_PAY_CHANNEL);
		try {
			payChannelIdList = settleParameterInfoManager.queryList(paramQuery);
			if(payChannelIdList == null || payChannelIdList.size() < 1) {
				String msg = "查询'多渠道渠道商'和'清算渠道编码'的对应关系  0条";
				logger.error(">>> " + msg);
			}
		} catch (Exception e) {
			String msg = "异常:查询'多渠道渠道商'和'清算渠道编码'的对应关系  异常";
			logger.error(">>> " + msg, e);
			e.printStackTrace();
		}
		
		return payChannelIdList;
	}
	/***
	 * 刷新 多渠道ChannelHome 和 清结算 PayChannelId 对应关系
	 * @return
	 */
	public void refreshMultiGateChannelHome2PayChannelId() {
		//查询'多渠道渠道商'和'清算渠道编码'的对应关系
		SettleParameterInfoQuery paramQuery = new SettleParameterInfoQuery();
		paramQuery.setParameterType(SettleConstants.PARAMETER_TYPE_PAY_CHANNEL);
		try {
			payChannelIdList = settleParameterInfoManager.queryList(paramQuery);
			if(payChannelIdList == null || payChannelIdList.size() < 1) {
				String msg = "查询'多渠道渠道商'和'清算渠道编码'的对应关系  0条";
				logger.error(">>> " + msg);
			}
		} catch (Exception e) {
			String msg = "异常:查询'多渠道渠道商'和'清算渠道编码'的对应关系  异常";
			logger.error(">>> " + msg, e);
			e.printStackTrace();
		}
		logger.info(">>> >>> >>> >>> >>> 多渠道ChannelHome 和 清结算 PayChannelId 对应关系 已经更新!!!");
	}
}
