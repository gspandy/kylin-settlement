package com.rkylin.settle.logic;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rkylin.settle.constant.SettleConstants;
import com.rkylin.settle.exception.SettleException;
import com.rkylin.settle.manager.SettleBalanceEntryManager;
import com.rkylin.settle.manager.SettleParameterInfoManager;
import com.rkylin.settle.manager.SettleTransBillManager;
import com.rkylin.settle.manager.SettleTransDetailManager;
import com.rkylin.settle.manager.SettleTransProfitManager;
import com.rkylin.settle.pojo.SettleBalanceEntry;
import com.rkylin.settle.pojo.SettleBalanceEntryQuery;
import com.rkylin.settle.pojo.SettleParameterInfo;
import com.rkylin.settle.pojo.SettleParameterInfoQuery;
import com.rkylin.settle.pojo.SettleTransBill;
import com.rkylin.settle.pojo.SettleTransBillQuery;
import com.rkylin.settle.pojo.SettleTransDetail;
import com.rkylin.settle.pojo.SettleTransDetailQuery;
import com.rkylin.settle.pojo.SettleTransProfit;
import com.rkylin.settle.pojo.SettleTransProfitQuery;
import com.rkylin.settle.util.LogicConstantUtil;
import com.rkylin.wheatfield.api.SemiAutomatizationServiceApi;
import com.rkylin.wheatfield.bean.TransOrderInf;
import com.rkylin.wheatfield.bean.User;
import com.rkylin.wheatfield.model.CommonResponse;


/***
 * 清分共同业务逻辑
 * @author Yang
 */
@Component("basicLogic")
public class BasicLogic {
	//日志对象
	protected static Logger logger = LoggerFactory.getLogger(BasicLogic.class);
	//调单边账DEAL_PRODUCT_ID
//	private static final String CHECK_THE_BALANCE_DPI = "PROD_00_RS_0066";
	@Autowired
	private SettleParameterInfoManager settleParameterInfoManager;		//'清算'属性表Manager
	@Autowired
	private SettleBalanceEntryManager settleBalanceEntryManager;	//对账结果Manager
	@Autowired
	private SettleTransDetailManager settleTransDetailManager;		//'清算'交易信息Manager
	@Autowired
	private SettleTransProfitManager settleTransProfitManager;		//'清算'分润结果交易信息Manager
	@Autowired
	private SettleTransBillManager settleTransBillManager;			//'清算'挂账交易信息Manager
	@Autowired
	private LogicConstantUtil logicConstantUtil;					//逻辑常量工具类
	@Autowired
	private SemiAutomatizationServiceApi semiAutomatizationService;
//	@Autowired
//	private OperationAmountByUserApi operationAmountByUserService;
	
	/***
	 * 更新&插入对账结果表
	 * @param settleBalanceEntryList
	 * 对账结果List, 暂存对账结果信息
	 */
	protected void insertAndUpdateSettleBalanceEntry(List<SettleBalanceEntry> settleBalanceEntryList) {
		logger.info(">>> >>> >>> 更新'交易结果'文件 ... ...");
		//对账信息查询query对象
		SettleBalanceEntryQuery settleBalanceEntryQuery = new SettleBalanceEntryQuery();
		//对账信息结果
		List<SettleBalanceEntry> resList = new ArrayList<SettleBalanceEntry>();
		if (settleBalanceEntryList.size() > 0) {
			for (SettleBalanceEntry subbean : settleBalanceEntryList) {
				settleBalanceEntryQuery = new SettleBalanceEntryQuery();
				settleBalanceEntryQuery.setObligate1(subbean.getObligate1());
				settleBalanceEntryQuery.setAccountDate(subbean.getAccountDate());
				//通过对账key和订单号查询对账结构
				resList = settleBalanceEntryManager.queryList(settleBalanceEntryQuery);
				if (resList.size() > 0) {//有此结果
					//修改原对账结果
					subbean.setBalanceEntryId(resList.get(0).getBalanceEntryId());
					settleBalanceEntryManager.updateSettleBalanceEntry(subbean);
				} else {//无此结果
					//添加新对账结果
					settleBalanceEntryManager.saveSettleBalanceEntry(subbean);
				}
			}
		}
	}
	/***
	 * 编辑对账返回值
	 * @param resultMap		返回值Map
	 * @param code			信息编码	1:成功, 0:失败, -1:异常, 其他 ... ...
	 * @param msg			信息内容
	 * @return
	 */
	protected Map<String, Object> editResultMap(Map<String, Object> resultMap, String code, String msg) {
		resultMap.put("code", code);
		resultMap.put("msg", msg);
		return resultMap;
	}
	/***
	 * 编辑对账返回值
	 * @param resultMap		返回值Map
	 * @param code			信息编码
	 * @param msg			信息内容
	 * @param failOrderNo	未对平订单号列表
	 * @param failOrderMsg	错误信息映射(key:orderNo[订单号], value:string[错误信息])
	 * @return
	 */
	protected Map<String, Object> editResultMap(Map<String, Object> resultMap, String code, String msg, List<String> failOrderNo, Map<String, String> failOrderMsg) {
		resultMap.put("code", code);
		resultMap.put("msg", msg);
		resultMap.put("failOrderNo", failOrderNo);
		resultMap.put("failOrderMsg", failOrderMsg);
		return resultMap;
	}
	/**
	 * 添加或修改'清算'方交易信息
	 * @param settleTransDetailList
	 */
	protected void insertAndUpdateSettleTransDetail(List<SettleTransDetail> settleTransDetailList) throws Exception {
		logger.info(">>> >>> >>> >>> 添加或修改'清算'方交易信息");
		//遍历'清算'方交易信息
		Iterator<SettleTransDetail> iter  = settleTransDetailList.iterator();
		SettleTransDetail updateItem = null;//修改的交易
		List<SettleTransDetail> addList = new ArrayList<SettleTransDetail>();//新增的交易
		while(iter.hasNext()) {
			//当前'清算'交易信息
			SettleTransDetail item = iter.next();
			//查询此信息在DB中是否存在
			SettleTransDetailQuery query = new SettleTransDetailQuery();
//			query.setAccountDate(item.getAccountDate());
			query.setOrderNo(item.getOrderNo());
			query.setInvoiceNo(item.getInvoiceNo());
			Integer count = settleTransDetailManager.countSettleTransDetail(query);
			if(count > 0) {//存在
				updateItem = new SettleTransDetail();
				updateItem.setReadStatusId(item.getReadStatusId());
				updateItem.setAccountDate(item.getAccountDate());
				updateItem.setOrderNo(item.getOrderNo());
				updateItem.setInvoiceNo(item.getInvoiceNo());
				updateItem.setFeeAmount(item.getFeeAmount());
				settleTransDetailManager.updateSettleTransDetail(updateItem);
			} else {//不存在
				addList.add(item);
			}
		}
		if(addList.size() > 0) this.batchSaveSettleTransDetail(addList);	
	}
	/***
	 * 插入OR更新挂账信息
	 * @param settleTransBillList	挂账信息List
	 * @throws SettleException
	 */
	protected void insertAndUpdateTransBill(List<SettleTransBill> settleTransBillList) throws SettleException {
		logger.info(">>> >>> >>> >>> 插入OR更新挂账信息");
		//遍历挂账信息List
		Iterator<SettleTransBill> iter = settleTransBillList.iterator();
		while(iter.hasNext()) {
			SettleTransBill bill = iter.next();
			SettleTransBillQuery query = new SettleTransBillQuery();
			query.setOrderNo(bill.getOrderNo());
			query.setRootInstCd(bill.getRootInstCd());
			query.setUserId(bill.getUserId());
			//通过'订单号'和'机构号'查询此挂账信息是否存在
			Integer count = settleTransBillManager.countByExample(query);
			if(count > 0) {//存在
				settleTransBillManager.updateSettleTransBill(bill);	//修改
			} else {//不存在
				settleTransBillManager.saveSettleTransBill(bill);	//插入
			}
		}
	}
	/***
	 * 插入OR更新挂账信息
	 * @param settleTransBillList	挂账信息List
	 * @throws SettleException
	 */
//	private void insertAndUpdateTransProfit(List<SettleTransProfit> settleTransProfitList) throws SettleException {
//		logger.info(">>> >>> >>> >>> 插入OR更新分润结果信息");
//		//遍历挂账信息List
//		Iterator<SettleTransProfit> iter = settleTransProfitList.iterator();
//		while(iter.hasNext()) {
//			SettleTransProfit profit = iter.next();
//			SettleTransProfitQuery query = new SettleTransProfitQuery();
//			query.setOrderNo(profit.getOrderNo());
//			query.setRootInstCd(profit.getRootInstCd());
//			//通过'订单号'和'机构号'查询此挂账信息是否存在
//			Integer count = settleTransProfitManager.countByExample(query);
//			if(count > 0) {//存在
//				settleTransProfitManager.updateSettleTransProfit(profit);	//修改
//			} else {//不存在
//				settleTransProfitManager.saveSettleTransProfit(profit);	//插入
//			}
//		}
//	}
	/***
	 * 通过PARAM_CODE查询对应的FUNC_CODE
	 * @param paramCode
	 * @return
	 */
	public List<String> getFuncCodeFromParamInfo(String paramCode) {
		List<String> profitStuList = new ArrayList<String>();
		SettleParameterInfoQuery query = new SettleParameterInfoQuery();
		//参与分润的功能编码
		String parameterType = SettleConstants.PARAMETER_TYPE_FUNC;
		//查询parameter表的query对像
		query.setParameterType(parameterType);
		query.setParameterCode(paramCode);
		//查询 分润的交易类型
		List<SettleParameterInfo> parameterInfoList = settleParameterInfoManager.queryList(query);
		
		if(parameterInfoList == null || parameterInfoList.size()==0){
			return profitStuList;
		}
		//获取 需分润的交易类型用','隔开的string形式
		String stuListString = parameterInfoList.get(0).getParameterValue();
		//转换成数组
		String[] stuListArr = stuListString.split(",");
		//去掉空格,去掉单引号
		for(String statusId : stuListArr) {
			statusId = statusId.trim();
			statusId = statusId.replace("'", "");
			profitStuList.add(statusId);
		}
		/*竖表存储形式 备用
		Iterator<SettleParameterInfo> iter = parameterInfoList.iterator();
		while(iter.hasNext()) {
			SettleParameterInfo parameterInfo = iter.next();
			String statusId = parameterInfo.getParameterValue();
			profitStuList.add(statusId);
		}*/
		return profitStuList;
	}
	/***
	 * 根据交易信息获取分润结果
	 * 因为要进行退款的交易已经分润
	 * 所以需查询分润结果表并按照记录插入|修改挂账信息表
	 * @param settleTransDetail 退款交易信息
	 * @return
	 */
	protected List<SettleTransProfit> getProfitListByTheDetail(Map<String, Object> settleTransDetail) throws SettleException {
		logger.info(">>> >>> >>> >>> 进入  查询退款分润结果");
		//创建查询对象
		SettleTransProfitQuery query = new SettleTransProfitQuery();
		//订单号
		query.setOrderNo(String.valueOf(settleTransDetail.get("ORIGINAL_ORDER_ID")));
		//机构号
		query.setRootInstCd(String.valueOf(settleTransDetail.get("MERCHANT_CODE")));
		//获取分润结果
		return settleTransProfitManager.queryList(query);
	}
	/*** 我想分出来!!!!
	 * 根据对应关系, 编辑交易信息的订单号
	 * 因从其他系统所得交易信息,其业务不同订单号存放字段各异
	 * 为进入清算系统后位置统一,故创建此方法!
	 * @param settleTransDetail
	 * @return
	 */
	public synchronized SettleTransDetail editTransOrderNo(SettleTransDetail settleTransDetail) {
		//查询订单号与存放位置的对应关系
		List<SettleParameterInfo> parameterList = logicConstantUtil.getFuncCodeAndOrderNoRelation();
		//默认存放位置的对应关系
		List<SettleParameterInfo> defaultParameterList = new ArrayList<SettleParameterInfo>();
		String funcCode = settleTransDetail.getFuncCode();					//'交易信息'功能编码
		String requestNo = settleTransDetail.getRequestNo();				//原始交易信息 requestNo
		String orderPackageNo = settleTransDetail.getOrderPackageNo();		//原始交易信息 orderPackageNo
		String orderNo = settleTransDetail.getOrderNo();					//原始交易信息 orderNo
		Map<String, String> orderNoMap = new HashMap<String, String>();		//编辑订单号的结构体
		orderNoMap.put("requestNo", requestNo);
		orderNoMap.put("orderPackageNo", orderPackageNo);
		orderNoMap.put("orderNo", orderNo);
		//校验传入的订单号对应关系记录
		if(parameterList == null || parameterList.size() < 1) {
			String message = "传入的List<SettleParameterInfo>记录   0条!";
			logger.info(">>> " + message);
			throw new SettleException(message);
		}
		//遍历对应关系
		Iterator<SettleParameterInfo> parameterIter = parameterList.iterator();
		Boolean isMatched = false;
		while(parameterIter.hasNext()) {
			SettleParameterInfo parameterInfo = parameterIter.next();		//当前对应关系记录
			String code = parameterInfo.getParameterCode();					//功能编码
			String value = parameterInfo.getParameterValue();				//订单号名称
			String theColumn = parameterInfo.getObligate1();				//订单号位置
			String theNo = orderNoMap.get(theColumn);						//订单号
			Boolean isTheDetail = code.equals(funcCode);					//匹配功能编码
			if(isTheDetail) {//匹配'功能编码'成功
				logger.info(">>> >>> 使用'匹配'订单规则");
				isMatched = true;
				if(value.equals("requestNo")) {//支付订单(订单系统产生)
					settleTransDetail.setRequestNo(theNo);
				} else if(value.equals("relateOrderNo")) {//下游商户订单号:用于下游对账文件
					settleTransDetail.setRelateOrderNo(theNo);
				} else if(value.equals("orderNo")) {//订单系统订单号(唯一)
					settleTransDetail.setOrderNo(theNo);
				} else {//匹配'订单号名称'错误
					logger.error(">>> 此funcCode:"+ funcCode +"匹配的订单号对应关系在DB中配置有误, 请查明并修改!");
				}
			} else if(code.equals("default")) {
				defaultParameterList.add(parameterInfo);
			}
		}
		/*
		 * 使用默认规则
		 */
		if(!isMatched) {
			logger.info(">>> >>> 使用'默认'订单规则");
			Iterator<SettleParameterInfo> defaultParameterIter = defaultParameterList.iterator();
			while(defaultParameterIter.hasNext()) {
				SettleParameterInfo parameterInfo = defaultParameterIter.next();		//当前对应关系记录
				String value = parameterInfo.getParameterValue();						//订单号名称
				String theColumn = parameterInfo.getObligate1();						//订单号位置
				String theNo = orderNoMap.get(theColumn);								//订单号
				if(value.equals("requestNo")) {//支付订单(订单系统产生)
					settleTransDetail.setRequestNo(theNo);
				} else if(value.equals("relateOrderNo")) {//下游商户订单号:用于下游对账文件
					settleTransDetail.setRelateOrderNo(theNo);
				} else if(value.equals("orderNo")) {//订单系统订单号(唯一)
					settleTransDetail.setOrderNo(theNo);
				} else {//匹配'订单号名称'错误
					logger.error(">>> 此funcCode:"+ funcCode +"匹配的订单号对应关系在DB中配置有误, 请查明并修改!");
				}
			}
		}
		return settleTransDetail;
	}
	/***  我想分出来!!!!
	 * 根据对应关系, 编辑交易信息的'金额'
	 * 因从其他系统所得交易信息,其业务不同'金额'存放字段各异
	 * 为进入清算系统后位置统一,故创建此方法!
	 * @param settleTransDetail
	 * @return
	 */
	public synchronized SettleTransDetail editTransAmount(SettleTransDetail settleTransDetail) {
		//查询金额与存放位置的对应关系
		List<SettleParameterInfo> parameterList = logicConstantUtil.getFuncCodeAndAmountRelation();
		//默认存放位置的对应关系
		List<SettleParameterInfo> defaultParameterList = new ArrayList<SettleParameterInfo>();
		String funcCode = settleTransDetail.getFuncCode();					//'交易信息'功能编码
		Long amount = 														//交易金额
				settleTransDetail.getAmount() == null 
				?
				0l
				:
				settleTransDetail.getAmount();						
		Long userFee = 														//手续费
				settleTransDetail.getUserFee() == null
				? 
				0l 
				: 
				settleTransDetail.getUserFee();								
		Long orderAmount = 
				settleTransDetail.getOrderAmount() == null 
				|| 
				settleTransDetail.getOrderAmount() == 0l 
				? 
				(amount - userFee) 
				: 
				settleTransDetail.getOrderAmount();							//订单金额
		Map<String, Long> amountMap = new HashMap<String, Long>();			//编辑订单号的结构体
		amountMap.put("orderAmount", orderAmount);
		amountMap.put("amount", amount);
		amountMap.put("userFee", userFee);
		//校验传入的订单号对应关系记录
		if(parameterList == null || parameterList.size() < 1) {
			String message = "传入的List<SettleParameterInfo>记录   0条!";
			logger.info(">>> " + message);
			throw new SettleException(message);
		}
		//遍历对应关系
		Iterator<SettleParameterInfo> parameterIter = parameterList.iterator();
		Boolean isMatched = false;
		while(parameterIter.hasNext()) {
			SettleParameterInfo parameterInfo = parameterIter.next();		//当前对应关系记录
			String code = parameterInfo.getParameterCode();					//功能编码
			String value = parameterInfo.getParameterValue();				//金额名称
			String theColumn = parameterInfo.getObligate1();				//金额位置
			Long theAmount = amountMap.get(theColumn);						//金额
			Boolean isTheDetail = code.equals(funcCode);					//匹配功能编码
			if(isTheDetail) {//匹配'功能编码'成功
				logger.info(">>> >>> 使用'匹配'金额规则");
				isMatched = true;
				if(value.equals("orderAmount")) {//订单金额
					settleTransDetail.setOrderAmount(theAmount);
				} else if(value.equals("amount")) {//交易金额
					settleTransDetail.setAmount(theAmount);
				} else if(value.equals("userFee")) {//手续费
					settleTransDetail.setUserFee(theAmount);
				} else {//匹配'订单号名称'错误
					logger.error(">>> 此funcCode:"+ funcCode +"匹配的金额对应关系在DB中配置有误, 请查明并修改!");
				}
			} else if(code.equals("default")) {
				defaultParameterList.add(parameterInfo);
			}
		}
		/*
		 * 使用默认规则
		 */
		if(!isMatched) {
			logger.info(">>> >>> 使用'默认'金额规则");
			Iterator<SettleParameterInfo> defaultParameterIter = defaultParameterList.iterator();
			while(defaultParameterIter.hasNext()) {
				SettleParameterInfo parameterInfo = defaultParameterIter.next();		//当前对应关系记录
				String value = parameterInfo.getParameterValue();						//金额名称
				String theColumn = parameterInfo.getObligate1();						//金额位置
				Long theAmount = amountMap.get(theColumn);								//金额
				if(value.equals("orderAmount")) {//订单金额
					settleTransDetail.setOrderAmount(theAmount);
				} else if(value.equals("amount")) {//交易金额
					settleTransDetail.setAmount(theAmount);
				} else if(value.equals("userFee")) {//手续费
					settleTransDetail.setUserFee(theAmount);
				} else {//匹配'订单号名称'错误
					logger.error(">>> 此funcCode:"+ funcCode +"匹配的'金额'对应关系在DB中配置有误, 请查明并修改!");
				}
			}
		}
		return settleTransDetail;
	}
	
	/**
	 * 批量插入清结算交易信息
	 * 
	 * @param settleTransDetailList
	 */
	public void batchSaveSettleTransDetail(List<SettleTransDetail> settleTransDetailList) {
		logger.info(">>> >>> 开始执行 批量插入清结算交易信息");
		final Integer total = settleTransDetailList.size();//总数量
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
			settleTransDetailManager.batchSaveSettleTransDetail(settleTransDetailList.subList(fromIndex, toIndex));
			batchIndex ++;
		}
		logger.info("<<< <<< 结束执行 批量插入清结算交易信息");
	}
	/***
	 * 获取传入机构号[merchantCode]在渠道[payChannelId]的协议中包含哪些机构的业务
	 * @param merchantCode
	 * @return
	 * @throws ParseException
	 */
	protected String getMerchantCode(String merchantCode, String payChannelId) throws ParseException{
    	SettleParameterInfoQuery keyList1 =  new SettleParameterInfoQuery();
    	keyList1.setParameterType(SettleConstants.PARAMETER_TYPE_MERCHANT);
    	keyList1.setParameterCode(merchantCode);
    	keyList1.setObligate1(payChannelId);
    	List<SettleParameterInfo> settleParameterInfo = settleParameterInfoManager.queryList(keyList1);
    	if(settleParameterInfo.size()>0){
    		return settleParameterInfo.get(0).getParameterValue();
    	}else{
    		logger.error("取机构号对应关系异常！");
    		throw new SettleException("取机构号对应关系异常！"); 
    	}
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
				} else {
					rulekey += "," + item.get(key);
				}
			}
			rulekey = rulekey.substring(1);
			transMap.put(rulekey, item);
		}
		
		return transMap;
	}
	/**
	 *
	 * 调用账户调账接口账户一期
	 * @param instCode
	 * @param userId
	 * @param productId
	 * @param amount
	 * @param status 加钱1、 减钱0
	 * @return
	 * @throws Exception
	 */
	protected Map<String, Object> changeAccount(String instCode,String userId,String productId,long amount,int status) throws Exception {
		Map<String, Object> rtnMap = new HashMap<String, Object>();
		CommonResponse commonResponse = null;
		User user = new User();
		user.setInstCode(instCode);
		user.setUserId(userId);
		user.setProductId(productId);
		user.setAmount(amount);
		user.setStatus(status);
		user.setFinAccountId("pos收单调账");
		try {
			/*
			 * 执行转账操作
			 */
			logger.info(">>> 执行账户操作!");
			commonResponse = semiAutomatizationService.operateFinAccount(user);
			if (commonResponse == null || !"1".equals(commonResponse.code)) {
				logger.info("失败: 调账结束，：["+user.getUserId()+"|" + user.getProductId() + "]金额["+amount+", 账户操作失败！返回结果为["+commonResponse.msg);
			} else if ("1".equals(commonResponse.code)) {
				logger.info("成功: 调账结束，：["+user.getUserId()+"|" + user.getProductId() + "]金额["+amount+", 账户操作成功！返回结果为["+commonResponse.msg);
			}
		} catch (Exception e) {
			logger.info("异常: 调账结束，：["+user.getUserId()+"|" + user.getProductId() + "]金额["+amount+", 账户操作异常！");
			e.printStackTrace();
		}
		rtnMap.put("code", commonResponse.code);
		rtnMap.put("msg", commonResponse.msg);
		return rtnMap;
	}
	/**
	 *
	 * 调用账户调账接口 账户一期[改]
	 * @param instCode
	 * @param userId
	 * @param productId
	 * @param amount
	 * @param status 加钱1、 减钱0
	 * @return
	 * @throws Exception
	 */
	protected Map<String, Object> changeAccount(String instCode, String userId, String productId, long amount, String funcCode, String orderNo, String remark) throws Exception {
		Map<String, Object> rtnMap = new HashMap<String, Object>();
		CommonResponse commonResponse = null;
		TransOrderInf transOrderInf = new TransOrderInf();
		transOrderInf.setMerchantCode(instCode);
		transOrderInf.setUserId(userId);
		transOrderInf.setProductId(productId);
		transOrderInf.setAmount(amount);
		transOrderInf.setFuncCode(funcCode);
		transOrderInf.setOrderNo(orderNo);
		transOrderInf.setRemark(remark);
		
		try {
			/*
			 * 执行转账操作
			 */
			logger.info(">>> 执行账户操作!");
			commonResponse = semiAutomatizationService.operateFinanaceAccount(transOrderInf);
			if (commonResponse == null || !"1".equals(commonResponse.code)) {
				logger.info("失败: 调账结束，：["+transOrderInf.getUserId()+"|" + transOrderInf.getProductId() + "]金额["+amount+", 账户操作失败！返回结果为["+commonResponse.msg);
			} else if ("1".equals(commonResponse.code)) {
				logger.info("成功: 调账结束，：["+transOrderInf.getUserId()+"|" + transOrderInf.getProductId() + "]金额["+amount+", 账户操作成功！返回结果为["+commonResponse.msg);
			}
		} catch (Exception e) {
			logger.info("异常: 调账结束，：["+transOrderInf.getUserId()+"|" + transOrderInf.getProductId() + "]金额["+amount+", 账户操作异常！");
			e.printStackTrace();
		}
		rtnMap.put("code", commonResponse.code);
		rtnMap.put("msg", commonResponse.msg);
		return rtnMap;
	}
	/**
	 * 调用账户调账接口 账户二期
	 * @param instCode
	 * @param userId
	 * @param productId
	 * @param kernelFuncCode
	 *  F30281转出(单边-)(实时)
		F30282转出(单边-)(延迟)
		F30283转出(单边-)(实时，不参加平衡检查)
		F30261转入(单边+)(实时)
		F30262转入(单边+)(延迟)
		F30263转入(单边+)(实时，不参加平衡检查)
	 * @param amount
	 * @param orderNo
	 * @param remark
	 * @return
	 * @throws Exception
	 */
	protected Map<String, Object> changeAccount2P(String instCode, String userId, String productId, String kernelFuncCode, long amount, String orderNo, String remark) throws Exception {
		Map<String, Object> rtnMap = new HashMap<String, Object>();
//		com.rongcapital.mtaegis.response.CommonResponse commonResponse = null;
//		TransOrderInfo transOrderInfo = new TransOrderInfo();
//		//动态参数
//		transOrderInfo.setRootInstCd(instCode);
//		transOrderInfo.setUserId(userId);
//		transOrderInfo.setProductId(productId);
//		transOrderInfo.setOrderAmount(amount);
//		transOrderInfo.setAmount(amount);
//		transOrderInfo.setOrderNo(orderNo);
//		transOrderInfo.setRemark(remark);
//		transOrderInfo.setReserve2(kernelFuncCode);
//		//静态参数
//		transOrderInfo.setDealProductCode(CHECK_THE_BALANCE_DPI);
//		transOrderInfo.setOrderCount(1);
//		transOrderInfo.setUserIpAddress(SettleConstants.LOCALHOST_URL);
//		
//		try {
//			/*
//			 * 执行转账操作
//			 */
//			logger.info(">>> 执行账户操作!");
//			commonResponse = operationAmountByUserService.execute(transOrderInfo);
//			if (commonResponse == null || !"1".equals(commonResponse.code)) {
//				logger.info("失败: 调账结束，：["+transOrderInfo.getUserId()+"|" + transOrderInfo.getProductId() + "]金额["+amount+", 账户操作失败！返回结果为["+commonResponse.msg);
//			} else if ("1".equals(commonResponse.code)) {
//				logger.info("成功: 调账结束，：["+transOrderInfo.getUserId()+"|" + transOrderInfo.getProductId() + "]金额["+amount+", 账户操作成功！返回结果为["+commonResponse.msg);
//			}
//		} catch (Exception e) {
//			logger.info("异常: 调账结束，：["+transOrderInfo.getUserId()+"|" + transOrderInfo.getProductId() + "]金额["+amount+", 账户操作异常！");
//			e.printStackTrace();
//		}
//		rtnMap.put("code", commonResponse.code);
//		rtnMap.put("msg", commonResponse.msg);
		return rtnMap;
	}
}
