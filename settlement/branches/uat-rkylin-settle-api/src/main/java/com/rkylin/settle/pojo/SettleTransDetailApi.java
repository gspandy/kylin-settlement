/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.settle.pojo;

import java.io.Serializable;

/**
 * SettleTransDetail
 * @author code-generator
 *
 */
public class SettleTransDetailApi implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private java.lang.Integer transDetailId;
	private java.lang.String requestNo;
	private java.util.Date requestTime;
	private java.lang.String transFlowNo;
	private java.lang.String orderNo;
	private java.lang.String orderPackageNo;
	private java.util.Date orderDate;
	private java.lang.Long orderAmount;
	private java.lang.Integer orderCount;
	private Integer orderType;
	private java.lang.String funcCode;
	private java.lang.String userId;
	private java.lang.String interMerchantCode;
	private java.lang.String merchantCode;
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
	private java.lang.String remark;
	private java.util.Date accountDate;
	private java.util.Date createdTime;
	private java.util.Date updatedTime;

	/**
	 * 交易id
	 * @param transDetailId
	 */
	public void setTransDetailId(java.lang.Integer transDetailId) {
		this.transDetailId = transDetailId;
	}
	
	/**
	 * 交易id
	 * @return
	 */
	public java.lang.Integer getTransDetailId() {
		return this.transDetailId;
	}
	/**
	 * 支付订单号
	 * @param requestNo
	 */
	public void setRequestNo(java.lang.String requestNo) {
		this.requestNo = requestNo;
	}
	
	/**
	 * 支付订单号
	 * @return
	 */
	public java.lang.String getRequestNo() {
		return this.requestNo;
	}
	/**
	 * 交易请求时间
	 * @param requestTime
	 */
	public void setRequestTime(java.util.Date requestTime) {
		this.requestTime = requestTime;
	}
	
	/**
	 * 交易请求时间
	 * @return
	 */
	public java.util.Date getRequestTime() {
		return this.requestTime;
	}
	/**
	 * 统一交易流水号
	 * @param transFlowNo
	 */
	public void setTransFlowNo(java.lang.String transFlowNo) {
		this.transFlowNo = transFlowNo;
	}
	
	/**
	 * 统一交易流水号
	 * @return
	 */
	public java.lang.String getTransFlowNo() {
		return this.transFlowNo;
	}
	/**
	 * 订单系统订单号
	 * @param orderNo
	 */
	public void setOrderNo(java.lang.String orderNo) {
		this.orderNo = orderNo;
	}
	
	/**
	 * 订单系统订单号
	 * @return
	 */
	public java.lang.String getOrderNo() {
		return this.orderNo;
	}
	/**
	 * 下游订单号
	 * @param orderPackageNo
	 */
	public void setOrderPackageNo(java.lang.String orderPackageNo) {
		this.orderPackageNo = orderPackageNo;
	}
	
	/**
	 * 下游订单号
	 * @return
	 */
	public java.lang.String getOrderPackageNo() {
		return this.orderPackageNo;
	}
	/**
	 * 订单日期
	 * @param orderDate
	 */
	public void setOrderDate(java.util.Date orderDate) {
		this.orderDate = orderDate;
	}
	
	/**
	 * 订单日期
	 * @return
	 */
	public java.util.Date getOrderDate() {
		return this.orderDate;
	}
	/**
	 * 订单金额
	 * @param orderAmount
	 */
	public void setOrderAmount(java.lang.Long orderAmount) {
		this.orderAmount = orderAmount;
	}
	
	/**
	 * 订单金额
	 * @return
	 */
	public java.lang.Long getOrderAmount() {
		return this.orderAmount;
	}
	/**
	 * 订单数量
	 * @param orderCount
	 */
	public void setOrderCount(java.lang.Integer orderCount) {
		this.orderCount = orderCount;
	}
	
	/**
	 * 订单数量
	 * @return
	 */
	public java.lang.Integer getOrderCount() {
		return this.orderCount;
	}
	/**
	 * 订单类型0交易,1结算单
	 * @param orderType
	 */
	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}
	
	/**
	 * 订单类型0交易,1结算单
	 * @return
	 */
	public Integer getOrderType() {
		return this.orderType;
	}
	/**
	 * 功能编码
	 * @param funcCode
	 */
	public void setFuncCode(java.lang.String funcCode) {
		this.funcCode = funcCode;
	}
	
	/**
	 * 功能编码
	 * @return
	 */
	public java.lang.String getFuncCode() {
		return this.funcCode;
	}
	/**
	 * 用户ID
	 * @param userId
	 */
	public void setUserId(java.lang.String userId) {
		this.userId = userId;
	}
	
	/**
	 * 用户ID
	 * @return
	 */
	public java.lang.String getUserId() {
		return this.userId;
	}
	/**
	 * 中间商户编码
	 * @param interMerchantCode
	 */
	public void setInterMerchantCode(java.lang.String interMerchantCode) {
		this.interMerchantCode = interMerchantCode;
	}
	
	/**
	 * 中间商户编码
	 * @return
	 */
	public java.lang.String getInterMerchantCode() {
		return this.interMerchantCode;
	}
	/**
	 * 商户编码
	 * @param merchantCode
	 */
	public void setMerchantCode(java.lang.String merchantCode) {
		this.merchantCode = merchantCode;
	}
	
	/**
	 * 商户编码
	 * @return
	 */
	public java.lang.String getMerchantCode() {
		return this.merchantCode;
	}
	/**
	 * 入账金额
	 * @param amount
	 */
	public void setAmount(java.lang.Long amount) {
		this.amount = amount;
	}
	
	/**
	 * 入账金额
	 * @return
	 */
	public java.lang.Long getAmount() {
		return this.amount;
	}
	/**
	 * 手续费金额
	 * @param feeAmount
	 */
	public void setFeeAmount(java.lang.Long feeAmount) {
		this.feeAmount = feeAmount;
	}
	
	/**
	 * 手续费金额
	 * @return
	 */
	public java.lang.Long getFeeAmount() {
		return this.feeAmount;
	}
	/**
	 * 手续金额备用1
	 * @param feeAmount1
	 */
	public void setFeeAmount1(java.lang.Long feeAmount1) {
		this.feeAmount1 = feeAmount1;
	}
	
	/**
	 * 手续金额备用1
	 * @return
	 */
	public java.lang.Long getFeeAmount1() {
		return this.feeAmount1;
	}
	/**
	 * 手续金额备用2
	 * @param feeAmount2
	 */
	public void setFeeAmount2(java.lang.Long feeAmount2) {
		this.feeAmount2 = feeAmount2;
	}
	
	/**
	 * 手续金额备用2
	 * @return
	 */
	public java.lang.Long getFeeAmount2() {
		return this.feeAmount2;
	}
	/**
	 * 手续金额备用3
	 * @param feeAmount3
	 */
	public void setFeeAmount3(java.lang.Long feeAmount3) {
		this.feeAmount3 = feeAmount3;
	}
	
	/**
	 * 手续金额备用3
	 * @return
	 */
	public java.lang.Long getFeeAmount3() {
		return this.feeAmount3;
	}
	/**
	 * 手续金额备用4
	 * @param feeAmount4
	 */
	public void setFeeAmount4(java.lang.Long feeAmount4) {
		this.feeAmount4 = feeAmount4;
	}
	
	/**
	 * 手续金额备用4
	 * @return
	 */
	public java.lang.Long getFeeAmount4() {
		return this.feeAmount4;
	}
	/**
	 * 用户手续费
	 * @param userFee
	 */
	public void setUserFee(java.lang.Long userFee) {
		this.userFee = userFee;
	}
	
	/**
	 * 用户手续费
	 * @return
	 */
	public java.lang.Long getUserFee() {
		return this.userFee;
	}
	/**
	 * 利润、收益
	 * @param profit
	 */
	public void setProfit(java.lang.Long profit) {
		this.profit = profit;
	}
	
	/**
	 * 利润、收益
	 * @return
	 */
	public java.lang.Long getProfit() {
		return this.profit;
	}
	/**
	 * 最终的对账金额
	 * @param fcAmount
	 */
	public void setFcAmount(java.lang.Long fcAmount) {
		this.fcAmount = fcAmount;
	}
	
	/**
	 * 最终的对账金额
	 * @return
	 */
	public java.lang.Long getFcAmount() {
		return this.fcAmount;
	}
	/**
	 * 业务类型
	 * @param businessType
	 */
	public void setBusinessType(java.lang.String businessType) {
		this.businessType = businessType;
	}
	
	/**
	 * 业务类型
	 * @return
	 */
	public java.lang.String getBusinessType() {
		return this.businessType;
	}
	/**
	 * 支付渠道ID
	 * @param payChannelId
	 */
	public void setPayChannelId(java.lang.String payChannelId) {
		this.payChannelId = payChannelId;
	}
	
	/**
	 * 支付渠道ID
	 * @return
	 */
	public java.lang.String getPayChannelId() {
		return this.payChannelId;
	}
	/**
	 * 银行联行编码
	 * @param bankCode
	 */
	public void setBankCode(java.lang.String bankCode) {
		this.bankCode = bankCode;
	}
	
	/**
	 * 银行联行编码
	 * @return
	 */
	public java.lang.String getBankCode() {
		return this.bankCode;
	}
	/**
	 * 消费者IP地址
	 * @param userIpAddress
	 */
	public void setUserIpAddress(java.lang.String userIpAddress) {
		this.userIpAddress = userIpAddress;
	}
	
	/**
	 * 消费者IP地址
	 * @return
	 */
	public java.lang.String getUserIpAddress() {
		return this.userIpAddress;
	}
	/**
	 * 错误编码
	 * @param errorCode
	 */
	public void setErrorCode(java.lang.String errorCode) {
		this.errorCode = errorCode;
	}
	
	/**
	 * 错误编码
	 * @return
	 */
	public java.lang.String getErrorCode() {
		return this.errorCode;
	}
	/**
	 * 错误信息
	 * @param errorMsg
	 */
	public void setErrorMsg(java.lang.String errorMsg) {
		this.errorMsg = errorMsg;
	}
	
	/**
	 * 错误信息
	 * @return
	 */
	public java.lang.String getErrorMsg() {
		return this.errorMsg;
	}
	/**
	 * 产品号
	 * @param productId
	 */
	public void setProductId(java.lang.String productId) {
		this.productId = productId;
	}
	
	/**
	 * 产品号
	 * @return
	 */
	public java.lang.String getProductId() {
		return this.productId;
	}
	/**
	 * 被付方产品号（代付时必须）
	 * @param productWid
	 */
	public void setProductWid(java.lang.String productWid) {
		this.productWid = productWid;
	}
	
	/**
	 * 被付方产品号（代付时必须）
	 * @return
	 */
	public java.lang.String getProductWid() {
		return this.productWid;
	}
	/**
	 * 冲正/撤销标记0正常1冲正2撤销
	 * @param cancelInd
	 */
	public void setCancelInd(Integer cancelInd) {
		this.cancelInd = cancelInd;
	}
	
	/**
	 * 冲正/撤销标记0正常1冲正2撤销
	 * @return
	 */
	public Integer getCancelInd() {
		return this.cancelInd;
	}
	/**
	 * 数据来源0账户1收银台2代收付3多渠道
	 * @param dataFrom
	 */
	public void setDataFrom(Integer dataFrom) {
		this.dataFrom = dataFrom;
	}
	
	/**
	 * 数据来源0账户1收银台2代收付3多渠道
	 * @return
	 */
	public Integer getDataFrom() {
		return this.dataFrom;
	}
	/**
	 * 发送状态
	 * @param deliverStatusId
	 */
	public void setDeliverStatusId(Integer deliverStatusId) {
		this.deliverStatusId = deliverStatusId;
	}
	
	/**
	 * 发送状态
	 * @return
	 */
	public Integer getDeliverStatusId() {
		return this.deliverStatusId;
	}
	/**
	 * 结算订单号
	 * @param invoiceNo
	 */
	public void setInvoiceNo(java.lang.String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	
	/**
	 * 结算订单号
	 * @return
	 */
	public java.lang.String getInvoiceNo() {
		return this.invoiceNo;
	}
	/**
	 * 预留1:'多渠道'交易状态 | 已经生成退款挂账的退款交易
	 * @param obligate1
	 */
	public void setObligate1(java.lang.String obligate1) {
		this.obligate1 = obligate1;
	}
	
	/**
	 * 预留1:'多渠道'交易状态 | 已经生成退款挂账的退款交易
	 * @return
	 */
	public java.lang.String getObligate1() {
		return this.obligate1;
	}
	/**
	 * 预留2
	 * @param obligate2
	 */
	public void setObligate2(java.lang.String obligate2) {
		this.obligate2 = obligate2;
	}
	
	/**
	 * 预留2
	 * @return
	 */
	public java.lang.String getObligate2() {
		return this.obligate2;
	}
	/**
	 * 预留3
	 * @param obligate3
	 */
	public void setObligate3(java.lang.String obligate3) {
		this.obligate3 = obligate3;
	}
	
	/**
	 * 预留3
	 * @return
	 */
	public java.lang.String getObligate3() {
		return this.obligate3;
	}
	/**
	 * 状态
	 * @param statusId
	 */
	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}
	
	/**
	 * 状态
	 * @return
	 */
	public Integer getStatusId() {
		return this.statusId;
	}
	/**
	 * 读取交易状态
	 * @param readStatusId
	 */
	public void setReadStatusId(Integer readStatusId) {
		this.readStatusId = readStatusId;
	}
	
	/**
	 * 读取交易状态
	 * @return
	 */
	public Integer getReadStatusId() {
		return this.readStatusId;
	}
	/**
	 * 备注
	 * @param remark
	 */
	public void setRemark(java.lang.String remark) {
		this.remark = remark;
	}
	
	/**
	 * 备注
	 * @return
	 */
	public java.lang.String getRemark() {
		return this.remark;
	}
	/**
	 * 记账日期
	 * @param accountDate
	 */
	public void setAccountDate(java.util.Date accountDate) {
		this.accountDate = accountDate;
	}
	
	/**
	 * 记账日期
	 * @return
	 */
	public java.util.Date getAccountDate() {
		return this.accountDate;
	}
	/**
	 * 记录创建时间
	 * @param createdTime
	 */
	public void setCreatedTime(java.util.Date createdTime) {
		this.createdTime = createdTime;
	}
	
	/**
	 * 记录创建时间
	 * @return
	 */
	public java.util.Date getCreatedTime() {
		return this.createdTime;
	}
	/**
	 * 记录更新时间
	 * @param updatedTime
	 */
	public void setUpdatedTime(java.util.Date updatedTime) {
		this.updatedTime = updatedTime;
	}
	
	/**
	 * 记录更新时间
	 * @return
	 */
	public java.util.Date getUpdatedTime() {
		return this.updatedTime;
	}
}