/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.settle.pojo;

import java.io.Serializable;
import java.util.Date;

import com.alibaba.dubbo.common.utils.StringUtils;

/**
 * SettleTransDetailQuery
 * @author code-generator
 *
 */
public class SettleTransDetailQuery implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private java.lang.Integer transDetailId;
	private java.lang.String requestNo;
	private java.util.Date requestTime;
	private java.lang.String transFlowNo;
	private java.lang.String orderNo;
	private java.lang.String relateOrderNo;
	private java.lang.String orderPackageNo;
	private java.util.Date orderDate;
	private java.lang.Long orderAmount;
	private java.lang.Integer orderCount;
	private Integer orderType;
	private Integer transType;
	private java.lang.String funcCode;
	private java.lang.String dealProductCode;
	private java.lang.String userId;
	private java.lang.String interMerchantCode;
	private java.lang.String merchantCode;
	private java.lang.String intoInstCode;
	private java.lang.Long amount;
	private java.lang.Long feeAmount;
	private java.lang.Long feeAmount1;
	private java.lang.Long feeAmount2;
	private java.lang.Long feeAmount3;
	private java.lang.Long feeAmount4;
	private java.lang.Long userFee;
	private java.lang.Long profit;
	private java.lang.Long fcAmount;
	private java.lang.String businessType;
	private java.lang.String payChannelId;
	private java.lang.String bankCode;
	private java.lang.String userIpAddress;
	private java.lang.String errorCode;
	private java.lang.String errorMsg;
	private java.lang.String productId;
	private java.lang.String productWid;
	private Integer cancelInd;
	private Integer dataFrom;
	private Integer deliverStatusId;
	private java.lang.String invoiceNo;
	private java.lang.String obligate1;
	private java.lang.String obligate2;
	private java.lang.String obligate3;
	private Integer statusId;
	private Integer readStatusId;
	private Integer dflag;
	private java.lang.String remark;
	private java.util.Date accountDate;
	private java.util.Date createdTime;
	private java.util.Date updatedTime;
	private java.lang.Integer requestId;
	private java.lang.String roleCode;
	private java.lang.String interRoleCode;
	private java.lang.String referUserId;
	private java.lang.String referRoleCode;
	private java.lang.String referProductId;
	private java.lang.Long otherAmount;
	private Integer userFeeWay;
	private java.lang.Long interest;
	private java.lang.String payWay;
	private Integer processStatus;
	private java.lang.String originalOrderPackageNo;
	private java.lang.String originalOrderId;
	private java.lang.String currency;
	private Integer reverseFlag;
	private Integer cancelTag;
	private java.lang.String acceptInstCode;
	private java.lang.String acceptInstId;
	private java.lang.String targetMerchantCode;
	private java.lang.String targetTerminalCode;
	private java.lang.String channelReturnCode;
	private java.lang.String returnMeg;
	private java.lang.String receiptSerialNo;
	private java.lang.String mcc;
	private java.lang.String authCode;
	private java.lang.String channelInfo;
	private java.lang.String tradeEsbNo;
	private java.lang.String reserve1;
	private java.lang.String reserve2;
	private java.lang.String reserve3;
	private java.util.Date readCreatedTime;
	// 分页
	private java.lang.String orderBy;
	private Integer offset;
	private java.lang.String limit;
	private Integer pageIndex;
	private Integer pageSize;

	//交易请求时间开始
	private java.lang.String requestDateStrBegin;
	//交易请求时间结束
	private java.lang.String requestDateStrEnd;
	
	private Integer notStatusId;
	
	private String[] funcCodes;
	
	public String[] getFuncCodes() {
		return funcCodes;
	}

	public void setFuncCodes(String[] funcCodes) {
		this.funcCodes = funcCodes;
	}

	public Integer getNotStatusId() {
		return notStatusId;
	}

	public void setNotStatusId(Integer notStatusId) {
		this.notStatusId = notStatusId;
	}

	public void setTransDetailId(Integer transDetailId)
  {
    this.transDetailId = transDetailId;
  }

  public Integer getTransDetailId()
  {
    return this.transDetailId;
  }

  public void setRequestNo(String requestNo)
  {
	this.requestNo = (StringUtils.isEmpty(requestNo) ? null : requestNo);
  }

  public String getRequestNo()
  {
    return this.requestNo;
  }

  public void setRequestTime(Date requestTime)
  {
    this.requestTime = requestTime;
  }

  public Date getRequestTime()
  {
    return this.requestTime;
  }

  public void setTransFlowNo(String transFlowNo)
  {
    this.transFlowNo = transFlowNo;
  }

  public String getTransFlowNo()
  {
    return this.transFlowNo;
  }

  public void setOrderNo(String orderNo)
  {
    this.orderNo = (StringUtils.isEmpty(orderNo) ? null : orderNo);
  }

  public String getOrderNo()
  {
    return this.orderNo;
  }

  public void setOrderPackageNo(String orderPackageNo)
  {
    this.orderPackageNo = orderPackageNo;
  }

  public String getOrderPackageNo()
  {
    return this.orderPackageNo;
  }

  public void setOrderDate(Date orderDate)
  {
    this.orderDate = orderDate;
  }

  public Date getOrderDate()
  {
    return this.orderDate;
  }

  public void setOrderAmount(Long orderAmount)
  {
    this.orderAmount = orderAmount;
  }

  public Long getOrderAmount()
  {
    return this.orderAmount;
  }

  public void setOrderCount(Integer orderCount)
  {
    this.orderCount = orderCount;
  }

  public Integer getOrderCount()
  {
    return this.orderCount;
  }

  public void setOrderType(Integer orderType)
  {
    this.orderType = (orderType.intValue() == -1 ? null : orderType);
  }

  public Integer getOrderType()
  {
    return this.orderType;
  }

  public void setFuncCode(String funcCode)
  {
    this.funcCode = (StringUtils.isEmpty(funcCode) ? null : funcCode);
  }

  public String getFuncCode()
  {
    return this.funcCode;
  }

  public void setUserId(String userId)
  {
    this.userId = (StringUtils.isEmpty(userId) ? null : userId);
  }

  public String getUserId()
  {
    return this.userId;
  }

  public void setInterMerchantCode(String interMerchantCode)
  {
    this.interMerchantCode = interMerchantCode;
  }

  public String getInterMerchantCode()
  {
    return this.interMerchantCode;
  }

  public void setMerchantCode(String merchantCode)
  {
    this.merchantCode = (StringUtils.isEmpty(merchantCode) ? null : merchantCode);
  }

  public String getMerchantCode()
  {
    return this.merchantCode;
  }

  public void setAmount(Long amount)
  {
    this.amount = amount;
  }

  public Long getAmount()
  {
    return this.amount;
  }

  public void setFeeAmount(Long feeAmount)
  {
    this.feeAmount = feeAmount;
  }

  public Long getFeeAmount()
  {
    return this.feeAmount;
  }

  public void setFeeAmount1(Long feeAmount1)
  {
    this.feeAmount1 = feeAmount1;
  }

  public Long getFeeAmount1()
  {
    return this.feeAmount1;
  }

  public void setFeeAmount2(Long feeAmount2)
  {
    this.feeAmount2 = feeAmount2;
  }

  public Long getFeeAmount2()
  {
    return this.feeAmount2;
  }

  public void setFeeAmount3(Long feeAmount3)
  {
    this.feeAmount3 = feeAmount3;
  }

  public Long getFeeAmount3()
  {
    return this.feeAmount3;
  }

  public void setFeeAmount4(Long feeAmount4)
  {
    this.feeAmount4 = feeAmount4;
  }

  public Long getFeeAmount4()
  {
    return this.feeAmount4;
  }

  public void setUserFee(Long userFee)
  {
    this.userFee = userFee;
  }

  public Long getUserFee()
  {
    return this.userFee;
  }
  public Long getProfit() {
    return this.profit;
  }
  public void setProfit(Long profit) {
    this.profit = profit;
  }
  public Long getFcAmount() {
    return this.fcAmount;
  }

  public void setFcAmount(Long fcAmount) {
    this.fcAmount = fcAmount;
  }

  public void setBusinessType(String businessType)
  {
    this.businessType = businessType;
  }

  public String getBusinessType()
  {
    return this.businessType;
  }

  public void setPayChannelId(String payChannelId)
  {
    this.payChannelId = ((StringUtils.isEmpty(payChannelId)) || ("-1".equals(payChannelId)) ? null : payChannelId);
  }

  public String getPayChannelId()
  {
    return this.payChannelId;
  }

  public void setBankCode(String bankCode)
  {
    this.bankCode = bankCode;
  }

  public String getBankCode()
  {
    return this.bankCode;
  }

  public void setUserIpAddress(String userIpAddress)
  {
    this.userIpAddress = userIpAddress;
  }

  public String getUserIpAddress()
  {
    return this.userIpAddress;
  }

  public void setErrorCode(String errorCode)
  {
    this.errorCode = errorCode;
  }

  public String getErrorCode()
  {
    return this.errorCode;
  }

  public void setErrorMsg(String errorMsg)
  {
    this.errorMsg = errorMsg;
  }

  public String getErrorMsg()
  {
    return this.errorMsg;
  }

  public void setProductId(String productId)
  {
    this.productId = productId;
  }

  public String getProductId()
  {
    return this.productId;
  }

  public void setProductWid(String productWid)
  {
    this.productWid = productWid;
  }

  public String getProductWid()
  {
    return this.productWid;
  }

  public void setCancelInd(Integer cancelInd)
  {
    this.cancelInd = (-1 == cancelInd.intValue() ? null : cancelInd);
  }

  public Integer getCancelInd()
  {
    return this.cancelInd;
  }

  public void setDataFrom(Integer dataFrom)
  {
    this.dataFrom = (-1 == dataFrom.intValue() ? null : dataFrom);
  }

  public Integer getDataFrom()
  {
    return this.dataFrom;
  }

  public void setDeliverStatusId(Integer deliverStatusId)
  {
    this.deliverStatusId = deliverStatusId;
  }

  public Integer getDeliverStatusId()
  {
    return this.deliverStatusId;
  }

  public void setInvoiceNo(String invoiceNo)
  {
    this.invoiceNo = invoiceNo;
  }

  public String getInvoiceNo()
  {
    return this.invoiceNo;
  }

  public void setObligate1(String obligate1)
  {
    this.obligate1 = obligate1;
  }

  public String getObligate1()
  {
    return this.obligate1;
  }

  public void setObligate2(String obligate2)
  {
    this.obligate2 = obligate2;
  }

  public String getObligate2()
  {
    return this.obligate2;
  }

  public void setObligate3(String obligate3)
  {
    this.obligate3 = obligate3;
  }

  public String getObligate3()
  {
    return this.obligate3;
  }

  public void setStatusId(Integer statusId)
  {
    this.statusId = (-1 == statusId.intValue() ? null : statusId);
  }

  public Integer getStatusId()
  {
    return this.statusId;
  }

  public void setReadStatusId(Integer readStatusId)
  {
    this.readStatusId = readStatusId;
  }

  public Integer getReadStatusId()
  {
    return this.readStatusId;
  }

  public void setRemark(String remark)
  {
    this.remark = remark;
  }

  public String getRemark()
  {
    return this.remark;
  }

  public void setAccountDate(Date accountDate)
  {
    this.accountDate = accountDate;
  }

  public Date getAccountDate()
  {
    return this.accountDate;
  }

  public void setCreatedTime(Date createdTime)
  {
    this.createdTime = createdTime;
  }

  public Date getCreatedTime()
  {
    return this.createdTime;
  }

  public void setUpdatedTime(Date updatedTime)
  {
    this.updatedTime = updatedTime;
  }

  public Date getUpdatedTime() {
    return this.updatedTime;
  }

  public String getOrderBy() {
    return this.orderBy;
  }

  public void setOrderBy(String orderBy) {
    this.orderBy = orderBy;
  }

  public Integer getOffset() {
    return this.offset;
  }

  public void setOffset(Integer offset) {
    this.offset = offset;
  }

  public String getLimit() {
    return this.limit;
  }

  public void setLimit(String limit) {
    this.limit = limit;
  }

  public Integer getPageIndex() {
    return this.pageIndex;
  }

  public void setPageIndex(Integer pageIndex) {
    this.pageIndex = pageIndex;
  }

  public Integer getPageSize() {
    return this.pageSize;
  }

  public void setPageSize(Integer pageSize) {
    this.pageSize = pageSize;
  }

  public String getRequestDateStrBegin() {
    return this.requestDateStrBegin;
  }

  public void setRequestDateStrBegin(String requestDateStrBegin) {
	if(requestDateStrBegin==null || requestDateStrBegin.isEmpty()) requestDateStrBegin = null;
    this.requestDateStrBegin = requestDateStrBegin;
  }

  public String getRequestDateStrEnd() {
    return this.requestDateStrEnd;
  }

  public void setRequestDateStrEnd(String requestDateStrEnd) {
	  if(requestDateStrEnd==null || requestDateStrEnd.isEmpty()) requestDateStrEnd = null;
    this.requestDateStrEnd = requestDateStrEnd;
  }
  
	public void setDflag(Integer dflag) {
		this.dflag = dflag;
	}
	
	public Integer getDflag() {
		return this.dflag;
	}

	public java.lang.String getRelateOrderNo() {
		return relateOrderNo;
	}

	public void setRelateOrderNo(java.lang.String relateOrderNo) {
		this.relateOrderNo = relateOrderNo;
	}

	public Integer getTransType() {
		return transType;
	}

	public void setTransType(Integer transType) {
		this.transType = transType;
	}

	public java.lang.String getDealProductCode() {
		return dealProductCode;
	}

	public void setDealProductCode(java.lang.String dealProductCode) {
		this.dealProductCode = dealProductCode;
	}

	public java.lang.String getIntoInstCode() {
		return intoInstCode;
	}

	public void setIntoInstCode(java.lang.String intoInstCode) {
		this.intoInstCode = intoInstCode;
	}

	public java.lang.Integer getRequestId() {
		return requestId;
	}

	public void setRequestId(java.lang.Integer requestId) {
		this.requestId = requestId;
	}

	public java.lang.String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(java.lang.String roleCode) {
		this.roleCode = roleCode;
	}

	public java.lang.String getInterRoleCode() {
		return interRoleCode;
	}

	public void setInterRoleCode(java.lang.String interRoleCode) {
		this.interRoleCode = interRoleCode;
	}

	public java.lang.String getReferUserId() {
		return referUserId;
	}

	public void setReferUserId(java.lang.String referUserId) {
		this.referUserId = referUserId;
	}

	public java.lang.String getReferRoleCode() {
		return referRoleCode;
	}

	public void setReferRoleCode(java.lang.String referRoleCode) {
		this.referRoleCode = referRoleCode;
	}

	public java.lang.String getReferProductId() {
		return referProductId;
	}

	public void setReferProductId(java.lang.String referProductId) {
		this.referProductId = referProductId;
	}

	public java.lang.Long getOtherAmount() {
		return otherAmount;
	}

	public void setOtherAmount(java.lang.Long otherAmount) {
		this.otherAmount = otherAmount;
	}

	public Integer getUserFeeWay() {
		return userFeeWay;
	}

	public void setUserFeeWay(Integer userFeeWay) {
		this.userFeeWay = userFeeWay;
	}

	public java.lang.Long getInterest() {
		return interest;
	}

	public void setInterest(java.lang.Long interest) {
		this.interest = interest;
	}

	public java.lang.String getPayWay() {
		return payWay;
	}

	public void setPayWay(java.lang.String payWay) {
		this.payWay = payWay;
	}

	public Integer getProcessStatus() {
		return processStatus;
	}

	public void setProcessStatus(Integer processStatus) {
		this.processStatus = processStatus;
	}

	public java.lang.String getOriginalOrderPackageNo() {
		return originalOrderPackageNo;
	}

	public void setOriginalOrderPackageNo(java.lang.String originalOrderPackageNo) {
		this.originalOrderPackageNo = originalOrderPackageNo;
	}

	public java.lang.String getOriginalOrderId() {
		return originalOrderId;
	}

	public void setOriginalOrderId(java.lang.String originalOrderId) {
		this.originalOrderId = originalOrderId;
	}

	public java.lang.String getCurrency() {
		return currency;
	}

	public void setCurrency(java.lang.String currency) {
		this.currency = currency;
	}

	public Integer getReverseFlag() {
		return reverseFlag;
	}

	public void setReverseFlag(Integer reverseFlag) {
		this.reverseFlag = reverseFlag;
	}

	public Integer getCancelTag() {
		return cancelTag;
	}

	public void setCancelTag(Integer cancelTag) {
		this.cancelTag = cancelTag;
	}

	public java.lang.String getAcceptInstCode() {
		return acceptInstCode;
	}

	public void setAcceptInstCode(java.lang.String acceptInstCode) {
		this.acceptInstCode = acceptInstCode;
	}

	public java.lang.String getAcceptInstId() {
		return acceptInstId;
	}

	public void setAcceptInstId(java.lang.String acceptInstId) {
		this.acceptInstId = acceptInstId;
	}

	public java.lang.String getTargetMerchantCode() {
		return targetMerchantCode;
	}

	public void setTargetMerchantCode(java.lang.String targetMerchantCode) {
		this.targetMerchantCode = targetMerchantCode;
	}

	public java.lang.String getTargetTerminalCode() {
		return targetTerminalCode;
	}

	public void setTargetTerminalCode(java.lang.String targetTerminalCode) {
		this.targetTerminalCode = targetTerminalCode;
	}

	public java.lang.String getChannelReturnCode() {
		return channelReturnCode;
	}

	public void setChannelReturnCode(java.lang.String channelReturnCode) {
		this.channelReturnCode = channelReturnCode;
	}

	public java.lang.String getReturnMeg() {
		return returnMeg;
	}

	public void setReturnMeg(java.lang.String returnMeg) {
		this.returnMeg = returnMeg;
	}

	public java.lang.String getReceiptSerialNo() {
		return receiptSerialNo;
	}

	public void setReceiptSerialNo(java.lang.String receiptSerialNo) {
		this.receiptSerialNo = receiptSerialNo;
	}

	public java.lang.String getMcc() {
		return mcc;
	}

	public void setMcc(java.lang.String mcc) {
		this.mcc = mcc;
	}

	public java.lang.String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(java.lang.String authCode) {
		this.authCode = authCode;
	}

	public java.lang.String getChannelInfo() {
		return channelInfo;
	}

	public void setChannelInfo(java.lang.String channelInfo) {
		this.channelInfo = channelInfo;
	}

	public java.lang.String getTradeEsbNo() {
		return tradeEsbNo;
	}

	public void setTradeEsbNo(java.lang.String tradeEsbNo) {
		this.tradeEsbNo = tradeEsbNo;
	}

	public java.lang.String getReserve1() {
		return reserve1;
	}

	public void setReserve1(java.lang.String reserve1) {
		this.reserve1 = reserve1;
	}

	public java.lang.String getReserve2() {
		return reserve2;
	}

	public void setReserve2(java.lang.String reserve2) {
		this.reserve2 = reserve2;
	}

	public java.lang.String getReserve3() {
		return reserve3;
	}

	public void setReserve3(java.lang.String reserve3) {
		this.reserve3 = reserve3;
	}

	public java.util.Date getReadCreatedTime() {
		return readCreatedTime;
	}

	public void setReadCreatedTime(java.util.Date readCreatedTime) {
		this.readCreatedTime = readCreatedTime;
	}
}