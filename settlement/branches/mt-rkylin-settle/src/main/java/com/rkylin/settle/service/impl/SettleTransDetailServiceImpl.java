package com.rkylin.settle.service.impl;

import java.io.File;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.allinpay.ets.client.StringUtil;
import com.rkylin.common.RedisIdGenerator;
import com.rkylin.merchant.dto.FileDownloadReqDto;
import com.rkylin.merchant.service.FileDownloadService;
import com.rkylin.settle.constant.SettleConstants;
import com.rkylin.settle.logic.ReadAccWriteDetailLogic;
import com.rkylin.settle.manager.SettleParameterInfoManager;
import com.rkylin.settle.manager.SettleTransBillManager;
import com.rkylin.settle.manager.SettleTransDetailManager;
import com.rkylin.settle.manager.SettleTransProfitManager;
import com.rkylin.settle.pojo.SettleParameterInfo;
import com.rkylin.settle.pojo.SettleParameterInfoQuery;
import com.rkylin.settle.pojo.SettleTransDetail;
import com.rkylin.settle.service.SettleParameterInfoService;
import com.rkylin.settle.service.SettleTransDetailService;
import com.rkylin.settle.util.FtpUpload;
import com.rkylin.settle.util.PagerModel;
import com.rkylin.settle.util.SettlementUtil;
import com.rkylin.settle.util.StringUtils;
import com.rkylin.settle.util.ToExcel2007S;
import com.rongcapital.mtaegis.po.TransOrderInfo;
import com.rongcapital.mtaegis.po.TransOrderInfoBack;
import com.rongcapital.mtaegis.response.CommonResponse;
import com.rongcapital.mtaegis.service.TransactionCommonApi;
import com.rongcapital.mtaegis.service.WriteOffApi;

/***
 * 下游对账交易信息业务逻辑
 * @author Yang
 *
 */
@Service("settleTransDetailService")
public class SettleTransDetailServiceImpl implements SettleTransDetailService {
	private static Logger logger = LoggerFactory.getLogger(SettleTransDetailServiceImpl.class);
	@Autowired
	private SettleTransDetailManager settleTransDetailManager;
	@Autowired
	private SettleParameterInfoManager parameterInfoManager;
	@Autowired
	private RedisIdGenerator redisIdGenerator;
//	@Autowired
//	private PaymentAccountServiceApi payAccSerApi;
//	@Autowired
//	private PaymentInternalOutService payIntOutSer;
	@Autowired
	private WriteOffApi writeOffApi;//冲正/抹账接口
	@Autowired
	private TransactionCommonApi transtdtionCommonApi;//退款交易接口
	@Autowired
    private SettleTransBillManager settleTransBillManager;
	@Autowired
	private SettleTransProfitManager settleTransProfitManager;
	@Autowired
	private SettlementUtil settlementUtil;
	@Autowired
	ReadAccWriteDetailLogic readAccWriteDetailLogic;
	@Autowired
    Properties ftpProperties;
	@Autowired
	FileDownloadService fileDownloadService;
	@Autowired
	SettleParameterInfoService settleParameterInfoService;
	
	//挂账交易状态
//	private Map<String,String> billOrderStatusMap = new HashMap<String, String>();
	/***
	 * 获取分润funcCodes
	 * @throws Exception 
	 */
	public List<String> queryProfitFuncCodes() throws Exception {
		SettleParameterInfoQuery query = new SettleParameterInfoQuery();
		query.setParameterType(SettleConstants.PARAMETER_TYPE_FUNC);
		query.setParameterCode(SettleConstants.PROFIT_FUNC_CODES);
		List<SettleParameterInfo> paramList = parameterInfoManager.queryList(query);
		List<String> funcCodeList = new ArrayList<String>();
		SettleParameterInfo param = null;
		
		if(paramList != null && paramList.size() > 0) {
			param = paramList.get(0);
		} else {
			String message = "获取分润FuncCodes异常!";
			logger.error(message);
			throw new Exception(message);
		}
		
		if(param != null) {
			String[] funcCodeArr = param.getParameterValue().split(",");
			for(String funcCode : funcCodeArr) {
				funcCodeList.add(funcCode);
			}
		} else {
			String message = "获取分润FuncCodes异常!";
			logger.error(message);
			throw new Exception(message);
		}
		return funcCodeList;
	}
	/***
	 * 调用账户系统接口冲正
	 * @return
	 */
	public Map<String, Object> doCorrectByAccount(List<SettleTransDetail> settleTransDetailList) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Iterator<SettleTransDetail> settleTransDetailIter = settleTransDetailList.iterator();
		TransOrderInfoBack transOrderInfo = null;
		CommonResponse commonResponse = null;
		String code = "";
		String msg = "";
		while(settleTransDetailIter.hasNext()) {
			/**
		     * @Description : TODO(商户扣款冲正dubbo提供接口)
		     * @Params : funcCode :交易编码 商户冲正 10011
		     *           orderNo : 新订单号
		     *           userIpAddress : 用户IP地址
		     *           orderPackageNo : 订单包号 ----原定单号
		     *           rootInstCd : 机构码
		     * @Return : 
		     * @CreateTime : 2015年9月15日 下午1:51:34
		     * @Updator : 
		     * @UpdateTime :
		     */
			SettleTransDetail settleTransDetail = settleTransDetailIter.next();
			String orderNo = settleTransDetail.getOrderNo(); 
			String newOrderNo = "Q" + redisIdGenerator.createRequestNo();
			String userIpAddress = settleTransDetail.getUserIpAddress();
//			Long orderAmount = settleTransDetail.getAmount();
			String roleCode = settleTransDetail.getRoleCode();
			String merchantCode = settleTransDetail.getMerchantCode();
//			String productId = settleTransDetail.getProductId();
			Date nowDate = new Date();
//			String userId = settleTransDetail.getUserId();
			try {
//				ErrorResponse errorResponse = payAccSerApi.antideductForDubbo(funcCode, newOrderNo, userIpAddress, orderNo, rootInstCd);
//				boolean isSuccess = errorResponse.isIs_success();
				transOrderInfo = new TransOrderInfoBack();
				transOrderInfo.setOrderNo(newOrderNo);
				transOrderInfo.setOriginalOrderId(orderNo);
//				transOrderInfo.setsetOrderAmount(orderAmount);
				transOrderInfo.setRootInstCd(merchantCode);
				transOrderInfo.setRoleCode(roleCode);
				transOrderInfo.setDealProductCode(SettleConstants.ACC_OF_DPC);
				transOrderInfo.setUserIpAddress(userIpAddress);
				transOrderInfo.setOrderCount(1);
//				transOrderInfo.setProductId(productId);
				transOrderInfo.setOrderDate(nowDate);
//				transOrderInfo.setUserId(userId);
				commonResponse = writeOffApi.execute(transOrderInfo);
				code = commonResponse.getCode();
				msg = commonResponse.getMsg();
				logger.info("调用[mtaegis抹帐接口]返回值: {code:"+ code +", msg:"+ msg +"}");
				boolean isSuccess = "1".equals(code);
				if(!isSuccess) {
					resultMap.put("msg", "冲正操作完成,但未全部成功!");
				}
			} catch (Exception e) {
				e.printStackTrace();
				resultMap.put("msg", "冲正操作完成,但未全部成功!");
			}
			
		}
		if(resultMap.get("msg") == null) resultMap.put("msg", "冲正操作完成, 全部成功!");
		return resultMap;
	}
	/***
	 * 调用账户系统接口抹账或退款
	 * @return
	 */
	public Map<String, Object> doAccOfOrRefundByAccount(List<SettleTransDetail> settleTransDetailList) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Iterator<SettleTransDetail> settleTransDetailIter = settleTransDetailList.iterator();
		List<String> queryProfitFuncCodes = this.queryProfitFuncCodes();
		CommonResponse commonResponse = null;//mtaegis返回值
		TransOrderInfo transOrderInfo = null;//mtaegis交易结构体
		TransOrderInfoBack transOrderInfoBack = null;
		String code = null;
		String msg = null;
		
		while(settleTransDetailIter.hasNext()) {
			/*
			 * '账户系统'交易信息结构体
			 */
			transOrderInfo = null;
			/*
			 * '清算系统'交易信息结构体
			 */
			SettleTransDetail settleTransDetail = settleTransDetailIter.next();
			String userId = settleTransDetail.getUserId();
			String productId = settleTransDetail.getProductId();
			String merchantCode = settleTransDetail.getMerchantCode();
			Long orderAmount = settleTransDetail.getAmount();
			Integer orderCount = 1;
			Date orderDate = new Date();
			String funcCode = settleTransDetail.getFuncCode();
			String orderNo = settleTransDetail.getOrderNo(); 
			String newOrderNo = "Q"+redisIdGenerator.createRequestNo();
			String roleCode = settleTransDetail.getRoleCode();
			String userIpAddress = settleTransDetail.getUserIpAddress();
			
			if(queryProfitFuncCodes.contains(funcCode)) {
				settleTransDetail.setOrderNo(newOrderNo);
				settleTransDetail.setOrderPackageNo(orderNo);
				settleTransDetail.setProductId(productId);
				settleTransDetail.setFuncCode("4020");
				//根据'清算'交易结构体获得'账户'交易结构体
				//transOrderInfo = getTransOrderInfoBySettleTransDetail(settleTransDetail);
				/**
			     * 
			     * @Description : TODO(消费后退款)
			     * @Param : 
			     * @Return : 
			     * @CreateTime : 2015年9月15日 下午3:36:13
			     * @Updator : 
			     * @UpdateTime :
			     */
				try {
					//ErrorResponse errorResponse = payAccSerApi.afterSpendingRefundForDubbo(transOrderInfo, productId, referUserId);				
					//boolean isSuccess = errorResponse.isIs_success();
					transOrderInfo = new TransOrderInfo();
					transOrderInfo.setUserId(userId);
//					transOrderInfo.setProductId(productId);
					transOrderInfo.setRootInstCd(merchantCode);
					transOrderInfo.setOrderAmount(orderAmount);
					transOrderInfo.setOrderCount(orderCount);
					transOrderInfo.setOrderDate(orderDate);
					transOrderInfo.setOrderNo(newOrderNo);
//					transOrderInfo.setOriginalOrderId(orderNo);
					transOrderInfo.setRoleCode(roleCode);
					transOrderInfo.setUserIpAddress(userIpAddress);
					transOrderInfo.setDealProductCode(SettleConstants.REFUND_DPC);
					commonResponse = transtdtionCommonApi.execute(transOrderInfo);
					code = commonResponse.getCode();
					msg = commonResponse.getMsg();
					logger.info("调用[mtaegis消费后退款接口]返回值: {code:"+ code +", msg:"+ msg +"}");
					boolean isSuccess = "1".equals(code);			
					if(!isSuccess) {
						resultMap.put("msg", "操作完成,但未全部成功!");
					}
				} catch (Exception e) {
					e.printStackTrace();
					resultMap.put("msg", "操作完成,但未全部成功!");
				}
			} else {
				/**
				 * 
				 * @Description : TODO(抹帐 dubbo提供接口)
				 * @Param : 
				 * @Return : 
				 * @Creator : liuhuan
				 * @CreateTime : 2015年9月15日 下午4:46:10
				 * @Updator : 
				 * @UpdateTime :
				 */
				try {
					//ErrorResponse errorResponse = payIntOutSer.wipeAccountForDubbo(newOrderNo, orderNo, merchantCode);
					//boolean isSuccess = errorResponse.isIs_success();
					transOrderInfoBack = new TransOrderInfoBack();
					transOrderInfoBack.setOrderNo(newOrderNo);
					transOrderInfoBack.setOriginalOrderId(orderNo);
//					transOrderInfoBack.setOrderAmount(orderAmount);
					transOrderInfoBack.setRootInstCd(merchantCode);
					transOrderInfoBack.setRoleCode(roleCode);
					transOrderInfoBack.setDealProductCode(SettleConstants.ACC_OF_DPC);
					transOrderInfoBack.setUserIpAddress(userIpAddress);
					transOrderInfoBack.setOrderCount(1);
//					transOrderInfoBack.setProductId(productId);
					transOrderInfoBack.setOrderDate(orderDate);
//					transOrderInfoBack.setUserId(userId);
					commonResponse = writeOffApi.execute(transOrderInfoBack);
					code = commonResponse.getCode();
					msg = commonResponse.getMsg();
					logger.info("调用[mtaegis抹帐接口]返回值: {code:"+ code +", msg:"+ msg +"}");
					boolean isSuccess = "1".equals(code);
					if(!isSuccess) {
						resultMap.put("msg", "操作完成,但未全部成功!");
					}
				} catch (Exception e) {
					e.printStackTrace();
					resultMap.put("msg", "操作完成,但未全部成功!");
				}
			}
		}
		if(resultMap.get("msg") == null) resultMap.put("msg", "操作完成, 全部成功!");
		return resultMap;
	}

	/***
	 * 根据'清算'交易结构体获得'账户'交易结构体
	 * @param settleTransDetail
	 * @return
	 */
//	private TransOrderInfo getTransOrderInfoBySettleTransDetail(SettleTransDetail settleTransDetail) {
//		TransOrderInfo transOrderInfo = new TransOrderInfo();
//		transOrderInfo.setRequestNo(settleTransDetail.getRequestNo());
//		transOrderInfo.setRequestTime(settleTransDetail.getRequestTime());
//		transOrderInfo.setTradeFlowNo(settleTransDetail.getTransFlowNo());
//		transOrderInfo.setOrderNo(settleTransDetail.getOrderNo());
//		transOrderInfo.setOrderPackageNo(settleTransDetail.getOrderPackageNo());
//		transOrderInfo.setOrderDate(settleTransDetail.getOrderDate());
//		transOrderInfo.setOrderAmount(settleTransDetail.getOrderAmount());
//		transOrderInfo.setOrderCount(settleTransDetail.getOrderCount());
//		transOrderInfo.setFuncCode(settleTransDetail.getFuncCode());
//		/**
//		 * 出钱方
//		 */
//		transOrderInfo.setUserId(settleTransDetail.getInterMerchantCode());
//		/**
//		 * 入钱方
//		 */
//		transOrderInfo.setInterUserId(settleTransDetail.getUserId());
//		transOrderInfo.setRootInstCd(settleTransDetail.getMerchantCode());
//		transOrderInfo.setOrderAmount(settleTransDetail.getAmount());
//		transOrderInfo.setUserFee(settleTransDetail.getFeeAmount());
//		transOrderInfo.setBusiTypeId(settleTransDetail.getBusinessType());
//		transOrderInfo.setPayChannelId(settleTransDetail.getPayChannelId());
//		transOrderInfo.setBankCode(settleTransDetail.getBankCode());
//		transOrderInfo.setUserIpAddress(settleTransDetail.getUserIpAddress());
//		transOrderInfo.setUserFee(settleTransDetail.getUserFee());
////		transOrderInfo.set(settleTransDetail.getErrorCode());
////		transOrderInfo.setErrorMsg(settleTransDetail.getErrorMsg());
////		if(settleTransDetail.getCancelInd() == 1) transOrderInfo.setStatus("6");
////		transOrderInfo.setStatus(String.valueOf(settleTransDetail.getReadStatusId()));
//		transOrderInfo.setRemark(settleTransDetail.getRemark());
//		transOrderInfo.setOrderDate(settleTransDetail.getAccountDate());
//		return transOrderInfo;
//	}
	@Override
	public Map<String, Object> updateAccountInfoToDetailInfo() throws Exception {
		//默认系统账期
		return this.updateAccountInfoToDetailInfo((Date) settlementUtil.getAccountDate("yyyy-MM-dd", -1, "Date"));
	}
	@Override
	public Map<String, Object> updateAccountInfoToDetailInfo(Date accountDate) throws Exception {
		//批量修改accountDate当天交易信息
		return this.updateAccountInfoToDetailInfo(accountDate, null);
	}
	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.NESTED)
	public Map<String, Object> updateAccountInfoToDetailInfo(Date beginDate, Date endDate) throws Exception {
		logger.info(">>> >>> >>> >>> 开始: 批量修改 将SETTLE_TRANS_ACCOUNT表的(手续费)和(机构,协议)回写到SETTLE_TRANS_DETAIL表");
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> queryMap = new HashMap<String, Object>();
		String code = "";
		String msg = "";
		Integer rows = -1;
		queryMap.put("beginDate", beginDate);
		queryMap.put("endDate", endDate);
		rows = settleTransDetailManager.updateAccountInfoToDetailInfo(queryMap);
		if(rows > -1) {
			code = "1";
			msg = "success";
		} else {
			code = "0";
			msg = "fail";
			logger.error(">>> >>> >>> 失败: 批量修改 将SETTLE_TRANS_ACCOUNT表的(手续费)和(机构,协议)回写到SETTLE_TRANS_DETAIL表");
		}
		resultMap.put("code", code);
		resultMap.put("msg", msg);
		resultMap.put("rows", rows);
		logger.info("<<< <<< <<< <<< 结束: 修改数据行数【"+ rows +"】 批量修改 将SETTLE_TRANS_ACCOUNT表的(手续费)和(机构,协议)回写到SETTLE_TRANS_DETAIL表");
		return resultMap;
	}
	@Override
	public Map<String, Object> doWriteToSettleTransDetail() throws Exception {
		return doWriteToSettleTransDetail((Date) settlementUtil.getAccountDate("yyyy-MM-dd", -1, "Date"));
	}
	@Override
	public Map<String, Object> doWriteToSettleTransDetail(Date accountDate) throws Exception {
		return doWriteToSettleTransDetail(accountDate, null);
	}
	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.NESTED)
	public Map<String, Object> doWriteToSettleTransDetail(Date beginDate, Date endDate) throws Exception {
		Map<String, Object> resultMap = new HashMap<String,Object>();
		List<Map<String, Object>> detailList = null;
		List<Map<String, Object>> accountList = null;
		String[] orderNoArr = null;
		detailList = readAccWriteDetailLogic.querySettleTransDetail(beginDate, endDate);
		if(detailList == null || detailList.size() < 1) {
			String msg = "查询交易状态为'未知'的detail交易 0 条";
			logger.error(">>> >>> " + msg);
			resultMap.put("code", -1);
			resultMap.put("msg", msg);
			return resultMap;
		}
		orderNoArr = new String[detailList.size()];
		for(int i = 0; i < orderNoArr.length; i ++) {
			orderNoArr[i] = String.valueOf(detailList.get(i).get("ORDER_NO"));
		}
		accountList = readAccWriteDetailLogic.querySettleTransAccount(orderNoArr);
		if(accountList == null || accountList.size() < 1) {
			String msg = "交易状态为未知的交易对应的account交易 0 条";
			logger.error(">>> >>> " + msg);
			resultMap.put("errorCode", -1);
			resultMap.put("msg", msg);
			return resultMap;
		}
		resultMap = readAccWriteDetailLogic.doWriteToSettleTransDetail(detailList, accountList);
		return resultMap;
	}
	/**
	 * 商户平台查SETTLE_TRANS_DETAIL表分页信息
	 * @param beginDate 非null
	 * @param endDate
	 * @param payChannelId
	 * @param pageIndex 默认1
	 * @param pageSize 默认20
	 * @return
	 * @throws Exception
	 */
	@Override
	public PagerModel<SettleTransDetail> selectBySHPTPage(Date beginDate, Date endDate, String merchantCode, String payChannelId, Integer pageIndex, Integer pageSize) throws Exception {
		logger.info(">>> >>> >>> >>> 开始:商户平台查SETTLE_TRANS_DETAIL表分页信息");
		//创建分页Model
		PagerModel<SettleTransDetail> pagerModel = new PagerModel<SettleTransDetail>();
		//查询条件Map
		Map<String, Object> queryMap = new HashMap<String, Object>();
		//Limit索引
		Integer offset = null;
		//入参验证
		if(beginDate == null) {
			logger.error(">>> >>> >>> 入参beginDate为null, beginDate isNull Error!");
			throw new Exception("入参beginDate为null, beginDate isNull Error!");
		}
		if(endDate != null && endDate.getTime() < beginDate.getTime()) {
			logger.error(">>> >>> >>> 入参endDate小于beginDate, endDate < beginDate Error!");
			throw new Exception("入参endDate小于beginDate, endDate < beginDate Error!");
		}
		if(StringUtil.isEmpty(merchantCode)) {
			logger.error(">>> >>> >>> 异常:merchantCode不能为空, merchantCode isEmpty Exception!");
			throw new Exception("异常:merchantCode不能为空, merchantCode isEmpty Exception!");
		}
		if(StringUtil.isEmpty(payChannelId)) {
			payChannelId = null;
		}
		//初始化当前页
		if(pageIndex == null) pageIndex = 1;
		//初始化显示条数
		if(pageSize == null) pageSize = 20;
		offset = ((pageIndex - 1) * pageSize);
		//封装queryMap
		queryMap.put("beginDate", beginDate);
		queryMap.put("endDate", endDate);
		queryMap.put("merchantCode", merchantCode);
		queryMap.put("payChannelId", payChannelId);
		queryMap.put("offset", offset);
		queryMap.put("pageSize", pageSize);
		List<SettleTransDetail> list = settleTransDetailManager.selectBySHPTPage(queryMap);
		//开始匹配payChannelId和merchant的名称
		list = editSettleTrasnDetailInfo(list);
		try {	
			pagerModel.setList(list);
			pagerModel.setTotal(settleTransDetailManager.countBySHPTPage(queryMap));
		} catch (Exception e) {
			logger.error(">>> >>> >>> 异常:商户平台查SETTLE_TRANS_DETAIL表分页信息", e);
			e.printStackTrace();
			pagerModel.setResult("exception");
			pagerModel.setMsg("系统异常:" + e.getMessage());
			return pagerModel;
		}
		pagerModel.setResult("success");
		logger.info("<<< <<< <<< <<< 结束:商户平台查SETTLE_TRANS_DETAIL表分页信息");
		return pagerModel;
	}
	
	/**
	 * 商户平台查SETTLE_TRANS_DETAIL下载全部
	 * @param beginDate 非null
	 * @param endDate
	 * @param payChannelId
	 * @param pageIndex 默认1
	 * @param pageSize 默认20
	 * @return
	 * @throws Exception
	 */
	@Override
	public void uploadTransFileToSHPTFTP(Date beginDate, Date endDate, String merchantCode, String payChannelId, String ftpFileName) throws Exception {
		logger.info(">>> >>> >>> >>> 开始:商户平台查SETTLE_TRANS_DETAIL下载全部");
		//文件对象
		File ftpFile = null;
		//查询条件Map
		Map<String, Object> queryMap = new HashMap<String, Object>();
		//文件内容
		List<SettleTransDetail> list = null;
		//入参验证
		if(beginDate == null) {
			logger.error(">>> >>> >>> 入参beginDate为null, beginDate isNull Error!");
			throw new Exception("入参beginDate为null, beginDate isNull Error!");
		}
		if(endDate != null && endDate.getTime() < beginDate.getTime()) {
			logger.error(">>> >>> >>> 入参endDate小于beginDate, endDate < beginDate Error!");
			throw new Exception("入参endDate小于beginDate, endDate < beginDate Error!");
		}
		if(StringUtil.isEmpty(merchantCode)) {
			logger.error(">>> >>> >>> 异常:merchantCode不能为空, merchantCode isEmpty Exception!");
			throw new Exception("异常:merchantCode不能为空, merchantCode isEmpty Exception!");
		}
		if(StringUtil.isEmpty(ftpFileName)) {
			logger.error(">>> >>> >>> 异常:ftpFileName不能为空, ftpFileName isEmpty Exception!");
			throw new Exception("异常:ftpFileName不能为空, ftpFileName isEmpty Exception!");
		}
		if(StringUtil.isEmpty(payChannelId)) {
			payChannelId = null;
		}
		//文件名添加后缀
		String ftpAllFileName = ftpFileName + ".xlsx";
		//封装queryMap
		queryMap.put("beginDate", beginDate);
		queryMap.put("endDate", endDate);
		queryMap.put("merchantCode", merchantCode);
		queryMap.put("payChannelId", payChannelId);
		list = settleTransDetailManager.selectAllBySHPT(queryMap);
		/*
		 * 创建Excel文件并返回文件信息
		 */
		ftpFile = this.createExcel(list, ftpAllFileName);
		/*
		 * 上传至FTP服务器
		 */
		boolean flg = this.uploadFileToSHPTFTP(ftpFile);
		if(!flg) {
			logger.error(">>> >>> >>> 异常:" + ftpAllFileName + "上传至FTP服务器失败 , uploadFileToFTP false Exception!");
			throw new Exception("异常:" + ftpAllFileName + "上传至FTP服务器失败, uploadFileToFTP false Exception!");
		}
		/*
		 * 调用MerchantService接口通知文件上传FTP结果
		 */
		try{
			String path = ftpProperties.getProperty("SHPT_FTP_DIRECTORY");//目录
    		String addr = ftpProperties.getProperty("SHPT_FTP_URL");//url地址
			FileDownloadReqDto dto = new FileDownloadReqDto();
			dto.setFileName(ftpFileName);
			dto.setFileUrl(addr + path + ftpAllFileName);
			dto.setState("成功");
			fileDownloadService.notifyFromSettle(dto);
		}catch(Exception e){
			logger.error(">>> >>> >>> 异常:调用MerchantService接口通知文件上传FTP结果", e);
			throw e;
		}
		
		logger.info("<<< <<< <<< <<< 结束:商户平台查SETTLE_TRANS_DETAIL下载全部");
	}
	
	/**
	 * 创建Excel文件
	 * @param list
	 * @return
	 */
	private File createExcel(List<SettleTransDetail> list, String fileName) throws Exception {
		logger.info(">>> >>> >>> 开始:创建Excel文件");
		//文件目录
		String drectory = SettleConstants.SHPT_PATH;
		//金额格式
		DecimalFormat amountFormat = new DecimalFormat();
		//日期格式yyyyMMdd
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		//日期格式yyyy-MM-dd HH:mm:s
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//excel文件对象
		File file = null;
		//数据实例(对象)
		SettleTransDetail std;
		//excl工具类入参
		Map configMap = new HashMap();
		//文件头信息
		List<Map<String, Object>> headList = new ArrayList<Map<String, Object>>();
		//文件内容信息
		List<Map<String, String>> fileList = new ArrayList<Map<String, String>>();
		//数据实例(行)
		Map<String, Object> itemMap;
		//数据实例(行)
		Map<String, String> detailMap;
		//编辑文件头信息(表头) 【ROW&COL的索引从0开始】
		itemMap = new HashMap<String, Object>();
		itemMap.put("ROW", "0");
		itemMap.put("COL", "0");
		itemMap.put("VALUE", "ID");
		headList.add(itemMap);
		itemMap = new HashMap<String, Object>();
		itemMap.put("ROW", "0");
		itemMap.put("COL", "1");
		itemMap.put("VALUE", "交易请求时间");
		headList.add(itemMap);
		itemMap = new HashMap<String, Object>();
		itemMap.put("ROW", "0");
		itemMap.put("COL", "2");
		itemMap.put("VALUE", "订单号");
		headList.add(itemMap);
		itemMap = new HashMap<String, Object>();
		itemMap.put("ROW", "0");
		itemMap.put("COL", "3");
		itemMap.put("VALUE", "订单金额");
		headList.add(itemMap);
		itemMap = new HashMap<String, Object>();
		itemMap.put("ROW", "0");
		itemMap.put("COL", "4");
		itemMap.put("VALUE", "订单类型");
		headList.add(itemMap);
		itemMap = new HashMap<String, Object>();
		itemMap.put("ROW", "0");
		itemMap.put("COL", "5");
		itemMap.put("VALUE", "功能编码");
		headList.add(itemMap);
		itemMap = new HashMap<String, Object>();
		itemMap.put("ROW", "0");
		itemMap.put("COL", "6");
		itemMap.put("VALUE", "用户ID");
		headList.add(itemMap);
		itemMap = new HashMap<String, Object>();
		itemMap.put("ROW", "0");
		itemMap.put("COL", "7");
		itemMap.put("VALUE", "商户编码");
		headList.add(itemMap);
		itemMap = new HashMap<String, Object>();
		itemMap.put("ROW", "0");
		itemMap.put("COL", "8");
		itemMap.put("VALUE", "入账金额");
		headList.add(itemMap);
		itemMap = new HashMap<String, Object>();
		itemMap.put("ROW", "0");
		itemMap.put("COL", "9");
		itemMap.put("VALUE", "手续费金额");
		headList.add(itemMap);
		itemMap = new HashMap<String, Object>();
		itemMap.put("ROW", "0");
		itemMap.put("COL", "10");
		itemMap.put("VALUE", "支付渠道");
		headList.add(itemMap);
		itemMap = new HashMap<String, Object>();
		itemMap.put("ROW", "0");
		itemMap.put("COL", "11");
		itemMap.put("VALUE", "数据来源");
		headList.add(itemMap);
		itemMap = new HashMap<String, Object>();
		itemMap.put("ROW", "0");
		itemMap.put("COL", "12");
		itemMap.put("VALUE", "状态");
		headList.add(itemMap);
		//编辑excl工具类入参
		//模板文件位置
		configMap.put("MODEL_FILE", drectory + SettleConstants.SHPT_EXCEL_MODEL);
		//生成文件位置
		configMap.put("FILE", drectory + fileName);
		//excl的sheet页名称 非空串 & 唯一
		configMap.put("SHEET", "账户结算查询");
		//内容开始行
		configMap.put("firstDetailRow", "1");
		//内容样式行
		configMap.put("firstStyleRow", "1");
		//sheet页数
		configMap.put("multiSheet", "1");
		//sheet开始索引
		configMap.put("multiSheet_index", "1");
		//定义文件头信息
		configMap.put("REPORT-HEAD", headList);
		
		//匹配payChannelId和merchant的名称
		list = editSettleTrasnDetailInfo(list);
		
		//编辑文件内容
		for(int i = 0; i < list.size(); i ++) {
			//数据对象
			std = list.get(i);
			//行
			detailMap = new HashMap<String, String>();
			detailMap.put("F_1",String.valueOf(std.getTransDetailId()));
			detailMap.put("F_2",std.getRequestTime()==null?"":sdf1.format(std.getRequestTime()));
			detailMap.put("F_3",StringUtils.isEmpty(std.getOrderNo())?"":std.getOrderNo());
			detailMap.put("F_4",std.getOrderAmount()==null?"":amountFormat.format((new BigDecimal(std.getOrderAmount())).divide(new BigDecimal(100)).doubleValue()));
			
			String orderType = null;
			switch(std.getOrderType()) {
				case 0: orderType = "账户交易"; break;
				case 1: orderType = "多渠道交易"; break;
				default: orderType = String.valueOf(std.getOrderType());
			}
			detailMap.put("F_5",orderType);
			
			String funcCode = String.valueOf(std.getFuncCode());
			if("4013".equals(funcCode)) {
				funcCode = "代收";
			} else if("4014".equals(funcCode)) {
				funcCode = "代付";
			} else if("4015".equals(funcCode)) {
				funcCode = "充值";
			} else if("4016".equals(funcCode)) {
				funcCode = "提现";
			} else if("4017".equals(funcCode)) {
				funcCode = "退款";
			} else if("5024".equals(funcCode)) {
				funcCode = "退票";
			} else {
			}
			detailMap.put("F_6",funcCode);
			detailMap.put("F_7",String.valueOf(std.getUserId()));
			detailMap.put("F_8",String.valueOf(std.getMerchantCode()));
			detailMap.put("F_9",std.getAmount()==null?"":amountFormat.format((new BigDecimal(std.getAmount())).divide(new BigDecimal(100)).doubleValue()));
			detailMap.put("F_10",std.getUserFee()==null?"":amountFormat.format((new BigDecimal(std.getUserFee())).divide(new BigDecimal(100)).doubleValue()));
			
			String payChannelId = String.valueOf(std.getPayChannelId());
			if(payChannelId.endsWith("01")) {
				if(payChannelId.endsWith("S01")) {
					payChannelId = "畅捷支付";
				} else if(payChannelId.endsWith("Y01")) {
					payChannelId = "平安银行银企直联";
				} else {
					payChannelId = "通联";
				}
			} else if(payChannelId.endsWith("04")) {
				payChannelId = "连连支付";
			} else if(payChannelId.endsWith("05")) {
				payChannelId = "联动优势";
			} else if(payChannelId.endsWith("Y02")) {
				payChannelId = "民生银行银企直联";
			} else if(payChannelId.endsWith("S02")) {
				payChannelId = "融宝";
			} else {
			}
			
			detailMap.put("F_11",payChannelId);
			
			String dataFromStr = "";
			switch(std.getDataFrom()) {
				case 0: dataFromStr = "账户"; break;
				case 3: dataFromStr = "多渠道"; break;
				default: dataFromStr = String.valueOf(std.getDataFrom());
			}
			detailMap.put("F_12", dataFromStr);
			
			String statusId = null;
			switch(std.getStatusId()) {
				case 1: statusId = "未清分"; break;
				case 11: statusId = "清分"; break;
				case 22: statusId = "错账"; break;
				case 21: statusId = "平账(结算)"; break;
				case 24: statusId = "短款"; break;
				default: statusId = String.valueOf(std.getStatusId());
			}
			detailMap.put("F_13",statusId);
			fileList.add(detailMap);
		}
		//excl工具类创建文件并编辑
		ToExcel2007S.WriteDetailSheet(fileList ,configMap ,null);
		//获取excl工具类生成的文件对象
		file = new File(drectory, fileName);
		logger.info("<<< <<< <<< 结束 :创建Excel文件");
		return file;
	}
	
	/**
	 * 向商户平台FTP上传文件
	 * @param file 源文件
	 * @return
	 */
	private boolean uploadFileToSHPTFTP(File file) throws Exception {
		logger.info(">>> >>> >>> 开始:向商户平台FTP上传文件");
		//Ftp上传工具类
		FtpUpload ftpUpload = null;
		//上传状态
		boolean flg = false;
		/*
		 * 链接FTP服务器
		 */
		logger.info(">>> >>> >>> 连接 商户平台FTP");
    	try {
    		ftpUpload = new FtpUpload();
    		String path = ftpProperties.getProperty("SHPT_FTP_DIRECTORY");//目录
    		String addr = ftpProperties.getProperty("SHPT_FTP_URL");//url地址
    		int port = Integer.parseInt(ftpProperties.getProperty("SHPT_FTP_PORT"));//端口
    		String username = ftpProperties.getProperty("SHPT_FTP_NAME");//账号
    		String password = ftpProperties.getProperty("SHPT_FTP_PASS");//密码
    		flg = ftpUpload.connect(path, addr, port, username, password);//链接
    		if (!flg) {//链接失败
    			logger.error(">>> >>> FTP连接失败, 3s后重连 " + flg);
    			Thread.sleep(3000);
    			flg = ftpUpload.connect(path, addr, port, username, password);//重新链接
        		if (!flg) {//重新连接失败
        			logger.error(">>> >>> FTP连接失败 " + flg);
        			throw new Exception("FTP连接失败 " + flg);
        		}
    		}
    	} catch (Exception e) {
    		logger.error("异常:向商户平台FTP上传对账文件, 商户平台FTP", e);
			e.printStackTrace();
			throw e;
    	}
    	
    	/*
    	 * 向FTP上传文件
    	 */
		logger.info(">>> >>> >>> 向FTP上传文件");
    	try {
        	flg = ftpUpload.upload(file);//上传
        	if (!flg) {//上传失败
    			logger.error(">>> >>> 向FTP上传文件失败 " + flg);
    			throw new Exception("向FTP上传文件失败 " + flg);
    		}
		} catch (Exception e) {
			logger.error(">>> >>> 向FTP上传文件失败!", e);
			e.printStackTrace();
			throw e;
		}
		logger.info("<<< <<< <<< 结束:向商户平台FTP上传文件");
		return flg;
	}
	
	/**
	 * 匹配payChannelId和merchant的名称
	 * @return
	 */
	private List<SettleTransDetail> editSettleTrasnDetailInfo(List<SettleTransDetail> list) {
		logger.info(">>> >>> >>> 开始匹配payChannelId和merchant的名称");
		String pCId = null;
		SettleTransDetail std = null;
		String[] pCStrArr = null;
		String mStr = null;
		String pStr = null;
		Map<String, String> payChannelIdMap = settleParameterInfoService.selectPayChannelIdInfo();
		Map<String, String> merchantCdIdMap = settleParameterInfoService.selectMerchantCdIdInfo();

		for(int i = 0; i < list.size(); i ++) {
			std = list.get(i);
			pCId = std.getPayChannelId();
			if(StringUtils.isEmpty(pCId)) {
				std.setPayChannelId("未知渠道");
			} else {
				pCStrArr = pCId.split(",");
				if(pCStrArr.length == 2) {
					mStr = merchantCdIdMap==null ? pCStrArr[0] : merchantCdIdMap.get(pCStrArr[0]);
					pStr = payChannelIdMap==null ? pCStrArr[1] : payChannelIdMap.get(pCStrArr[1]);
					mStr = StringUtils.isEmpty(mStr) ? pCStrArr[0] : mStr;
					pStr = StringUtils.isEmpty(pStr) ? pCStrArr[1] : pStr;
					pCId = mStr + "," + pStr;
				} else if(pCStrArr.length == 1) {
					pStr = payChannelIdMap==null ? pCStrArr[0] : payChannelIdMap.get(pCStrArr[0]);
					pCId = StringUtils.isEmpty(pStr) ? pCStrArr[0] : pStr;
				}
				std.setPayChannelId(pCId);
			}
			list.set(i, std);
		}
		logger.info("<<< <<< <<< 开始匹配payChannelId和merchant的名称");
		return list;
	}
}
