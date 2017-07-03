package com.rkylin.settle.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rkylin.common.RedisIdGenerator;
import com.rkylin.settle.constant.SettleConstants;
import com.rkylin.settle.manager.SettleParameterInfoManager;
import com.rkylin.settle.manager.SettleTransBillManager;
import com.rkylin.settle.manager.SettleTransDetailManager;
import com.rkylin.settle.manager.SettleTransProfitManager;
import com.rkylin.settle.pojo.SettleParameterInfo;
import com.rkylin.settle.pojo.SettleParameterInfoQuery;
import com.rkylin.settle.pojo.SettleTransBill;
import com.rkylin.settle.pojo.SettleTransBillQuery;
import com.rkylin.settle.pojo.SettleTransDetail;
import com.rkylin.settle.pojo.SettleTransDetailQuery;
import com.rkylin.settle.pojo.SettleTransProfit;
import com.rkylin.settle.service.SettleTransDetailService;
import com.rkylin.settle.util.PagerModel;
//import com.rkylin.wheatfield.api.PaymentAccountServiceApi;
//import com.rkylin.wheatfield.api.PaymentInternalOutService;
//import com.rkylin.wheatfield.response.ErrorResponse;
import com.rongcapital.mtaegis.po.TransOrderInfo;
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
	private TransactionCommonApi transactionCommonApi;//退款交易接口
	@Autowired
    private SettleTransBillManager settleTransBillManager;
	@Autowired
	private SettleTransProfitManager settleTransProfitManager;
	//挂账交易状态
	private Map<String,String> billOrderStatusMap = new HashMap<String, String>();
	/***
	 * 分页条件查询下游对账交易信息
	 * @param query
	 * @return
	 */
	@Override
	public PagerModel<SettleTransDetail> query(SettleTransDetailQuery query) {
		//创建分页Model
		PagerModel<SettleTransDetail> pagerModel = new PagerModel<SettleTransDetail>();
		try {
			//初始化当前页
			if(query.getPageIndex()==null){
				query.setPageIndex(1);
			}
			//初始化显示条数
			if(query.getPageSize()==null){
				query.setPageSize(10);
			}
			//封装pageModel
			query.setOffset((query.getPageIndex() - 1) * query.getPageSize());
			pagerModel.setList(settleTransDetailManager.queryPage(query));
			pagerModel.setTotal(settleTransDetailManager.countByExample(query));
			pagerModel.setResult("ok");
		} catch (Exception e) {
			e.printStackTrace();
			pagerModel.setResult("error");
			pagerModel.setMsg("系统异常");
		}
		return pagerModel;
	}
	/***
	 * 查询下游对账交易byID
	 * @param query
	 * @return
	 */
	@Override
	public SettleTransDetail findById(Long id) {
		return settleTransDetailManager.findSettleTransDetailById(id);
	}
	/***
	 * 修改下游对账交易信息
	 * @param query
	 * @return
	 */
	@Override
	public Integer edit(SettleTransDetail SettleTransDetail) {
		return settleTransDetailManager.updateSettleTransDetail(SettleTransDetail);
	}
	/***
	 * 批量查询交易信息by主键数组
	 * @param queryMap
	 * @return
	 */
	@Override
	public List<SettleTransDetail> queryByIds(Integer[] ids) {
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("ids", ids);
		return settleTransDetailManager.selectByIds(queryMap);
	}
	/***
	 * 批量查询交易信息by主键数组
	 * @param queryMap
	 * @return
	 */
	@Override
	public List<SettleTransDetail> queryByIds(Map<String, Object> queryMap) {
		return settleTransDetailManager.selectByIds(queryMap);
	}
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
		TransOrderInfo transOrderInfo = null;
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
			Long orderAmount = settleTransDetail.getAmount();
			String roleCode = settleTransDetail.getRoleCode();
			String merchantCode = settleTransDetail.getMerchantCode();
			String productId = settleTransDetail.getProductId();
			Date nowDate = new Date();
			String userId = settleTransDetail.getUserId();
			try {
//				ErrorResponse errorResponse = payAccSerApi.antideductForDubbo(funcCode, newOrderNo, userIpAddress, orderNo, rootInstCd);
//				boolean isSuccess = errorResponse.isIs_success();
				transOrderInfo = new TransOrderInfo();
				transOrderInfo.setOrderNo(newOrderNo);
				transOrderInfo.setOriginalOrderId(orderNo);
				transOrderInfo.setOrderAmount(orderAmount);
				transOrderInfo.setRootInstCd(merchantCode);
				transOrderInfo.setRoleCode(roleCode);
				transOrderInfo.setDealProductCode(SettleConstants.ACC_OF_DPC);
				transOrderInfo.setUserIpAddress(userIpAddress);
				transOrderInfo.setOrderCount(1);
				transOrderInfo.setProductId(productId);
				transOrderInfo.setOrderDate(nowDate);
				transOrderInfo.setUserId(userId);
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
					transOrderInfo.setProductId(productId);
					transOrderInfo.setRootInstCd(merchantCode);
					transOrderInfo.setOrderAmount(orderAmount);
					transOrderInfo.setOrderCount(orderCount);
					transOrderInfo.setOrderDate(orderDate);
					transOrderInfo.setOrderNo(newOrderNo);
					transOrderInfo.setOriginalOrderId(orderNo);
					transOrderInfo.setRoleCode(roleCode);
					transOrderInfo.setUserIpAddress(userIpAddress);
					transOrderInfo.setDealProductCode(SettleConstants.REFUND_DPC);
					commonResponse = transactionCommonApi.execute(transOrderInfo);
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
					transOrderInfo = new TransOrderInfo();
					transOrderInfo.setOrderNo(newOrderNo);
					transOrderInfo.setOriginalOrderId(orderNo);
					transOrderInfo.setOrderAmount(orderAmount);
					transOrderInfo.setRootInstCd(merchantCode);
					transOrderInfo.setRoleCode(roleCode);
					transOrderInfo.setDealProductCode(SettleConstants.ACC_OF_DPC);
					transOrderInfo.setUserIpAddress(userIpAddress);
					transOrderInfo.setOrderCount(1);
					transOrderInfo.setProductId(productId);
					transOrderInfo.setOrderDate(orderDate);
					transOrderInfo.setUserId(userId);
					commonResponse = writeOffApi.execute(transOrderInfo);
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
	private TransOrderInfo getTransOrderInfoBySettleTransDetail(SettleTransDetail settleTransDetail) {
		TransOrderInfo transOrderInfo = new TransOrderInfo();
		transOrderInfo.setRequestNo(settleTransDetail.getRequestNo());
		transOrderInfo.setRequestTime(settleTransDetail.getRequestTime());
		transOrderInfo.setTradeFlowNo(settleTransDetail.getTransFlowNo());
		transOrderInfo.setOrderNo(settleTransDetail.getOrderNo());
		transOrderInfo.setOrderPackageNo(settleTransDetail.getOrderPackageNo());
		transOrderInfo.setOrderDate(settleTransDetail.getOrderDate());
		transOrderInfo.setOrderAmount(settleTransDetail.getOrderAmount());
		transOrderInfo.setOrderCount(settleTransDetail.getOrderCount());
		transOrderInfo.setFuncCode(settleTransDetail.getFuncCode());
		/**
		 * 出钱方
		 */
		transOrderInfo.setUserId(settleTransDetail.getInterMerchantCode());
		/**
		 * 入钱方
		 */
		transOrderInfo.setInterUserId(settleTransDetail.getUserId());
		transOrderInfo.setRootInstCd(settleTransDetail.getMerchantCode());
		transOrderInfo.setAmount(settleTransDetail.getAmount());
		transOrderInfo.setUserFee(settleTransDetail.getFeeAmount());
		transOrderInfo.setBusiTypeId(settleTransDetail.getBusinessType());
		transOrderInfo.setPayChannelId(settleTransDetail.getPayChannelId());
		transOrderInfo.setBankCode(settleTransDetail.getBankCode());
		transOrderInfo.setUserIpAddress(settleTransDetail.getUserIpAddress());
		transOrderInfo.setUserFee(settleTransDetail.getUserFee());
//		transOrderInfo.set(settleTransDetail.getErrorCode());
//		transOrderInfo.setErrorMsg(settleTransDetail.getErrorMsg());
		if(settleTransDetail.getCancelInd() == 1) transOrderInfo.setStatus("6");
		transOrderInfo.setStatus(String.valueOf(settleTransDetail.getReadStatusId()));
		transOrderInfo.setRemark(settleTransDetail.getRemark());
		transOrderInfo.setOrderDate(settleTransDetail.getAccountDate());
		return transOrderInfo;
	}

	/***
     * 挂账
     * 1、更新detail表状态为99，插入挂账表信息状态为3
     * 2、清分成功交易，需要更新清分表信息为2不处理
     * 3、清算成功交易，不做挂账处理，将ID返回前端
     * @return
     */
    @Override
    public Map<String, Object> doBill(Integer[] ids) throws Exception {
        Map<String, Object> returnMap = new HashMap<String, Object>();
        //清算成功的交易
        StringBuffer settleSb = new StringBuffer();
        //detail表中需要更新的交易
        List<Integer> updateIdList = new ArrayList<Integer>();
        
        Map<String, Object> queryMap = new HashMap<String, Object>();
        queryMap.put("ids", ids);
        List<SettleTransDetail> settleTransDetailList = settleTransDetailManager.selectByIds(queryMap);
        
        for(SettleTransDetail settleTransDetail:settleTransDetailList){
        	boolean dontBill = 
        			//清算成功交易，不做挂账
        			queryBillOrderStatus("NO_BILL").equals(settleTransDetail.getStatusId().toString()) 
        			|| 
        			//交易已经汇总，不做挂账
        			(settleTransDetail.getDflag() != null || settleTransDetail.getDflag() != 0);
            if(dontBill){
                //记录交易信息，返回前端
                settleSb.append(settleTransDetail.getTransDetailId()).append(",");
                logger.debug("清算成功交易，不做挂账,ID为"+settleTransDetail.getTransDetailId());
                continue;
            }else if(SettleConstants.STATUS_PROFIT_SUCCESS==settleTransDetail.getStatusId()){
                //清分成功交易，需要更新清分表信息状态
                SettleTransProfit settleTransProfit = new SettleTransProfit();
                //状态,0未进行,1已进行,2不处理
                settleTransProfit.setStatusId(2);
                settleTransProfit.setOrderNo(settleTransDetail.getOrderNo());
                settleTransProfitManager.updateStatusByOrderNo(settleTransProfit);
            }
            SettleTransBill settleTransBill = new SettleTransBill();
            SettleTransBillQuery query = new SettleTransBillQuery();
            query.setOrderNo(settleTransDetail.getOrderNo());
            List<SettleTransBill> billList = settleTransBillManager.queryList(query);
            if(!billList.isEmpty()){
                settleTransBill = billList.get(0);
            }
            //订单号=订单系统订单号
            settleTransBill.setOrderNo(settleTransDetail.getOrderNo());
            //批次号
            //settleTransBill.setBatchNo(batchNo);
            //挂账条目=订单系统订单号+0001
            settleTransBill.setBillNo(settleTransDetail.getOrderNo()+"0001");
            //管理机构代码=商户编码
            settleTransBill.setRootInstCd(settleTransDetail.getMerchantCode());
            //产品号
            settleTransBill.setProductId(settleTransDetail.getProductId());
            //用户ID
            settleTransBill.setUserId(settleTransDetail.getUserId());
            //第三方账户ID
            //settleTransBill.setReferUserId(settleTransDetail.getInterMerchantCode());
            //挂账金额=入账金额
            settleTransBill.setBillAmount(settleTransDetail.getAmount());
            //挂账类型0差错处理,1退款
            settleTransBill.setBillType(0);
            //状态,0未进行,1已进行,2不处理,3挂账中,4取消挂账
            settleTransBill.setStatusId(3);
            //记账日期
            settleTransBill.setAccountDate(settleTransDetail.getAccountDate());
            settleTransBill.setRemark("挂账中");
            //如果挂账记录不为空，则更新挂账表信息
            if(!billList.isEmpty()){
                settleTransBillManager.updateSettleTransBill(settleTransBill);
            }else{
                settleTransBillManager.saveSettleTransBill(settleTransBill);
            }
            
            //detail表中需要更新的ID
            updateIdList.add(settleTransDetail.getTransDetailId());
        }
        if(!updateIdList.isEmpty()){
            //批量更新交易信息状态
            queryMap.clear();
            queryMap.put("idList", updateIdList);
            queryMap.put("statusId", "99");
            settleTransDetailManager.updateTransStatusId(queryMap);
        }
        //封装返回值
        if(settleSb.length()!=0){
            returnMap.put("error", "error");
            settleSb.insert(0, "ID为");
            settleSb.insert(settleSb.length(), "交易状态为清算成功，不允许挂账操作，操作失败！");
            returnMap.put("msg", settleSb.toString());
        }
        return returnMap;
    }
    /***
     * 取消挂账
     * @return
     */
    @Override
    public Map<String, Object> doCancelBill(Integer[] ids) throws Exception {
        Map<String, Object> returnMap = new HashMap<String, Object>();
        StringBuffer returnSb = new StringBuffer();
        for(Integer id:ids){
            //查询交易信息
            SettleTransDetail settleTransDetail = settleTransDetailManager.findSettleTransDetailById(Long.valueOf(id));
            //交易不是挂账状态，不处理
            if(99!=settleTransDetail.getStatusId()){
                returnSb.append(id).append(",");
                logger.debug("交易不是挂账状态，不处理,ID为"+settleTransDetail.getTransDetailId());
                continue;
            }
            //更新交易信息状态
            SettleTransDetail updateDetail = new SettleTransDetail();
            updateDetail.setTransDetailId(id);
            updateDetail.setStatusId(1);
            settleTransDetailManager.updateSettleTransDetail(updateDetail);
            
            
            //更新挂账表信息状态
            SettleTransBill settleTransBill = new SettleTransBill();
            settleTransBill.setOrderNo(settleTransDetail.getOrderNo());
            //取消挂账后的状态
            settleTransBill.setStatusId(4);
            settleTransBill.setRemark("挂账取消");
            settleTransBillManager.updateSettleTransBillByOrderNo(settleTransBill);
        }
        //封装返回值
        if(returnSb.length()!=0){
            returnMap.put("error", "error");
            returnSb.insert(0, "本操作仅限取消挂账，ID为");
            returnSb.insert(returnSb.length(), "交易状态不是挂账中，操作失败！");
            returnMap.put("msg", returnSb.toString());
        }
        return returnMap;
    }
    
    /**
     * @Description: 获取挂账交易状态，
     * 结算成功（状态为41）的交易不允许挂账
     * @param parameterCode NO_BILL不允许挂账
     * @return
     * @author CLF
     */
    private String queryBillOrderStatus(String parameterCode){
        if(!billOrderStatusMap.containsKey(parameterCode)){
            SettleParameterInfoQuery parameterInfoQuery = new SettleParameterInfoQuery ();
            //挂账交易状态常量-不允许挂账交易状态
            parameterInfoQuery.setParameterType(SettleConstants.PARAMETER_TYPE_BILL_NO);
            List<SettleParameterInfo> paramInfoList = parameterInfoManager.queryList(parameterInfoQuery);
            for(SettleParameterInfo info:paramInfoList){
               billOrderStatusMap.put(info.getParameterCode(), info.getParameterValue());
            }
        }
        return billOrderStatusMap.get(parameterCode);
    }
}
