/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2016
 */

package com.rkylin.settle.pojo;

import java.io.Serializable;

/**
 * TransOrderInfoQuery
 * @author code-generator
 *
 */
public class TransOrderInfoQuery implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private java.lang.String requestNo;
	private java.util.Date requestTime;
	private java.lang.String tradeFlowNo;
	private java.lang.String orderPackageNo;
	private java.lang.String relateOrderNo;
	private java.lang.String orderNo;
	private java.util.Date orderDate;
	private java.lang.Long orderAmount;
	private java.lang.Integer orderCount;
	private Integer transType;
	private java.lang.String funcCode;
	private java.lang.String dealProductCode;
	private java.lang.String rootInstCd;
	private java.lang.String intoRootInstCd;
	private java.lang.String userId;
	private java.lang.String roleCode;
	private java.lang.String interUserId;
	private java.lang.String interRoleCode;
	private java.lang.String referUserId;
	private java.lang.String referRoleCode;
	private java.lang.String referProductId;
	private java.lang.String productId;
	private java.lang.String intoProductId;
	private java.lang.Long amount;
	private java.lang.Long otherAmount;
	private java.lang.Long userFee;
	private Integer userFeeWay;
	private java.lang.Long profit;
	private java.lang.Long interest;
	private java.lang.String busiTypeId;
	private java.lang.String payChannelId;
	private java.lang.String payWay;
	private java.lang.String userIpAddress;
	private Integer processStatus;
	private java.lang.String originalOrderPackageNo;
	private java.lang.String originalOrderId;
	private java.lang.String errorCode;
	private java.lang.String errorMsg;
	private java.util.Date accountDate;
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
	private java.lang.String bankCode;
	private java.lang.String tradeEsbNo;
	private java.lang.String reserve1;
	private java.lang.String reserve2;
	private java.lang.String reserve3;
	private java.lang.String remark;
	private Integer statusId;
	private java.util.Date createdTime;
	private java.util.Date updatedTime;

	/**
	 * 交易请求号
	 * @param requestNo
	 */
	public void setRequestNo(java.lang.String requestNo) {
		this.requestNo = requestNo;
	}
	
	/**
	 * 交易请求号
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
	 * @param tradeFlowNo
	 */
	public void setTradeFlowNo(java.lang.String tradeFlowNo) {
		this.tradeFlowNo = tradeFlowNo;
	}
	
	/**
	 * 统一交易流水号
	 * @return
	 */
	public java.lang.String getTradeFlowNo() {
		return this.tradeFlowNo;
	}
	/**
	 * 订单包号
	 * @param orderPackageNo
	 */
	public void setOrderPackageNo(java.lang.String orderPackageNo) {
		this.orderPackageNo = orderPackageNo;
	}
	
	/**
	 * 订单包号
	 * @return
	 */
	public java.lang.String getOrderPackageNo() {
		return this.orderPackageNo;
	}
	/**
	 * 相关订单号
	 * @param relateOrderNo
	 */
	public void setRelateOrderNo(java.lang.String relateOrderNo) {
		this.relateOrderNo = relateOrderNo;
	}
	
	/**
	 * 相关订单号
	 * @return
	 */
	public java.lang.String getRelateOrderNo() {
		return this.relateOrderNo;
	}
	/**
	 * 订单号
	 * @param orderNo
	 */
	public void setOrderNo(java.lang.String orderNo) {
		this.orderNo = orderNo;
	}
	
	/**
	 * 订单号
	 * @return
	 */
	public java.lang.String getOrderNo() {
		return this.orderNo;
	}
	/**
	 * 订单日期(商户订单日期)
	 * @param orderDate
	 */
	public void setOrderDate(java.util.Date orderDate) {
		this.orderDate = orderDate;
	}
	
	/**
	 * 订单日期(商户订单日期)
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
	 * 订单类型
	 * @param transType
	 */
	public void setTransType(Integer transType) {
		this.transType = transType;
	}
	
	/**
	 * 订单类型
	 * @return
	 */
	public Integer getTransType() {
		return this.transType;
	}
	/**
	 * 功能编码(兼容旧交易)
	 * @param funcCode
	 */
	public void setFuncCode(java.lang.String funcCode) {
		this.funcCode = funcCode;
	}
	
	/**
	 * 功能编码(兼容旧交易)
	 * @return
	 */
	public java.lang.String getFuncCode() {
		return this.funcCode;
	}
	/**
	 * 交易产品编码
	 * @param dealProductCode
	 */
	public void setDealProductCode(java.lang.String dealProductCode) {
		this.dealProductCode = dealProductCode;
	}
	
	/**
	 * 交易产品编码
	 * @return
	 */
	public java.lang.String getDealProductCode() {
		return this.dealProductCode;
	}
	/**
	 * 机构编码
	 * @param rootInstCd
	 */
	public void setRootInstCd(java.lang.String rootInstCd) {
		this.rootInstCd = rootInstCd;
	}
	
	/**
	 * 机构编码
	 * @return
	 */
	public java.lang.String getRootInstCd() {
		return this.rootInstCd;
	}
	/**
	 * 机构编码2
	 * @param intoRootInstCd
	 */
	public void setIntoRootInstCd(java.lang.String intoRootInstCd) {
		this.intoRootInstCd = intoRootInstCd;
	}
	
	/**
	 * 机构编码2
	 * @return
	 */
	public java.lang.String getIntoRootInstCd() {
		return this.intoRootInstCd;
	}
	/**
	 * 转出方USERID
	 * @param userId
	 */
	public void setUserId(java.lang.String userId) {
		this.userId = userId;
	}
	
	/**
	 * 转出方USERID
	 * @return
	 */
	public java.lang.String getUserId() {
		return this.userId;
	}
	/**
	 * 转出方角色编号
	 * @param roleCode
	 */
	public void setRoleCode(java.lang.String roleCode) {
		this.roleCode = roleCode;
	}
	
	/**
	 * 转出方角色编号
	 * @return
	 */
	public java.lang.String getRoleCode() {
		return this.roleCode;
	}
	/**
	 * 转入方USERID
	 * @param interUserId
	 */
	public void setInterUserId(java.lang.String interUserId) {
		this.interUserId = interUserId;
	}
	
	/**
	 * 转入方USERID
	 * @return
	 */
	public java.lang.String getInterUserId() {
		return this.interUserId;
	}
	/**
	 * 转入方角色编号
	 * @param interRoleCode
	 */
	public void setInterRoleCode(java.lang.String interRoleCode) {
		this.interRoleCode = interRoleCode;
	}
	
	/**
	 * 转入方角色编号
	 * @return
	 */
	public java.lang.String getInterRoleCode() {
		return this.interRoleCode;
	}
	/**
	 * 相关方ID
	 * @param referUserId
	 */
	public void setReferUserId(java.lang.String referUserId) {
		this.referUserId = referUserId;
	}
	
	/**
	 * 相关方ID
	 * @return
	 */
	public java.lang.String getReferUserId() {
		return this.referUserId;
	}
	/**
	 * 相关方角色编号
	 * @param referRoleCode
	 */
	public void setReferRoleCode(java.lang.String referRoleCode) {
		this.referRoleCode = referRoleCode;
	}
	
	/**
	 * 相关方角色编号
	 * @return
	 */
	public java.lang.String getReferRoleCode() {
		return this.referRoleCode;
	}
	/**
	 * 相关方账户产品码
	 * @param referProductId
	 */
	public void setReferProductId(java.lang.String referProductId) {
		this.referProductId = referProductId;
	}
	
	/**
	 * 相关方账户产品码
	 * @return
	 */
	public java.lang.String getReferProductId() {
		return this.referProductId;
	}
	/**
	 * 转出方账户产品码
	 * @param productId
	 */
	public void setProductId(java.lang.String productId) {
		this.productId = productId;
	}
	
	/**
	 * 转出方账户产品码
	 * @return
	 */
	public java.lang.String getProductId() {
		return this.productId;
	}
	/**
	 * 转入方账户产品码
	 * @param intoProductId
	 */
	public void setIntoProductId(java.lang.String intoProductId) {
		this.intoProductId = intoProductId;
	}
	
	/**
	 * 转入方账户产品码
	 * @return
	 */
	public java.lang.String getIntoProductId() {
		return this.intoProductId;
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
	 * 其他金额
	 * @param otherAmount
	 */
	public void setOtherAmount(java.lang.Long otherAmount) {
		this.otherAmount = otherAmount;
	}
	
	/**
	 * 其他金额
	 * @return
	 */
	public java.lang.Long getOtherAmount() {
		return this.otherAmount;
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
	 * 手续费方式(内扣/外扣)
	 * @param userFeeWay
	 */
	public void setUserFeeWay(Integer userFeeWay) {
		this.userFeeWay = userFeeWay;
	}
	
	/**
	 * 手续费方式(内扣/外扣)
	 * @return
	 */
	public Integer getUserFeeWay() {
		return this.userFeeWay;
	}
	/**
	 * 利润
	 * @param profit
	 */
	public void setProfit(java.lang.Long profit) {
		this.profit = profit;
	}
	
	/**
	 * 利润
	 * @return
	 */
	public java.lang.Long getProfit() {
		return this.profit;
	}
	/**
	 * 利息
	 * @param interest
	 */
	public void setInterest(java.lang.Long interest) {
		this.interest = interest;
	}
	
	/**
	 * 利息
	 * @return
	 */
	public java.lang.Long getInterest() {
		return this.interest;
	}
	/**
	 * 业务类型ID
	 * @param busiTypeId
	 */
	public void setBusiTypeId(java.lang.String busiTypeId) {
		this.busiTypeId = busiTypeId;
	}
	
	/**
	 * 业务类型ID
	 * @return
	 */
	public java.lang.String getBusiTypeId() {
		return this.busiTypeId;
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
	 * 支付方式
	 * @param payWay
	 */
	public void setPayWay(java.lang.String payWay) {
		this.payWay = payWay;
	}
	
	/**
	 * 支付方式
	 * @return
	 */
	public java.lang.String getPayWay() {
		return this.payWay;
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
	 * 处理状态(代收付结果状态)
	 * @param processStatus
	 */
	public void setProcessStatus(Integer processStatus) {
		this.processStatus = processStatus;
	}
	
	/**
	 * 处理状态(代收付结果状态)
	 * @return
	 */
	public Integer getProcessStatus() {
		return this.processStatus;
	}
	/**
	 * 原订单包号
	 * @param originalOrderPackageNo
	 */
	public void setOriginalOrderPackageNo(java.lang.String originalOrderPackageNo) {
		this.originalOrderPackageNo = originalOrderPackageNo;
	}
	
	/**
	 * 原订单包号
	 * @return
	 */
	public java.lang.String getOriginalOrderPackageNo() {
		return this.originalOrderPackageNo;
	}
	/**
	 * 原定单号
	 * @param originalOrderId
	 */
	public void setOriginalOrderId(java.lang.String originalOrderId) {
		this.originalOrderId = originalOrderId;
	}
	
	/**
	 * 原定单号
	 * @return
	 */
	public java.lang.String getOriginalOrderId() {
		return this.originalOrderId;
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
	 * 账期
	 * @param accountDate
	 */
	public void setAccountDate(java.util.Date accountDate) {
		this.accountDate = accountDate;
	}
	
	/**
	 * 账期
	 * @return
	 */
	public java.util.Date getAccountDate() {
		return this.accountDate;
	}
	/**
	 * 币种
	 * @param currency
	 */
	public void setCurrency(java.lang.String currency) {
		this.currency = currency;
	}
	
	/**
	 * 币种
	 * @return
	 */
	public java.lang.String getCurrency() {
		return this.currency;
	}
	/**
	 * 冲正标志,0正常交易，1冲正交易
	 * @param reverseFlag
	 */
	public void setReverseFlag(Integer reverseFlag) {
		this.reverseFlag = reverseFlag;
	}
	
	/**
	 * 冲正标志,0正常交易，1冲正交易
	 * @return
	 */
	public Integer getReverseFlag() {
		return this.reverseFlag;
	}
	/**
	 * 撤销标记,0正常，1冲正交易
	 * @param cancelTag
	 */
	public void setCancelTag(Integer cancelTag) {
		this.cancelTag = cancelTag;
	}
	
	/**
	 * 撤销标记,0正常，1冲正交易
	 * @return
	 */
	public Integer getCancelTag() {
		return this.cancelTag;
	}
	/**
	 * 受理机构代码
	 * @param acceptInstCode
	 */
	public void setAcceptInstCode(java.lang.String acceptInstCode) {
		this.acceptInstCode = acceptInstCode;
	}
	
	/**
	 * 受理机构代码
	 * @return
	 */
	public java.lang.String getAcceptInstCode() {
		return this.acceptInstCode;
	}
	/**
	 * 接受机构标识码
	 * @param acceptInstId
	 */
	public void setAcceptInstId(java.lang.String acceptInstId) {
		this.acceptInstId = acceptInstId;
	}
	
	/**
	 * 接受机构标识码
	 * @return
	 */
	public java.lang.String getAcceptInstId() {
		return this.acceptInstId;
	}
	/**
	 * 目标商户代码
	 * @param targetMerchantCode
	 */
	public void setTargetMerchantCode(java.lang.String targetMerchantCode) {
		this.targetMerchantCode = targetMerchantCode;
	}
	
	/**
	 * 目标商户代码
	 * @return
	 */
	public java.lang.String getTargetMerchantCode() {
		return this.targetMerchantCode;
	}
	/**
	 * 目标终端码
	 * @param targetTerminalCode
	 */
	public void setTargetTerminalCode(java.lang.String targetTerminalCode) {
		this.targetTerminalCode = targetTerminalCode;
	}
	
	/**
	 * 目标终端码
	 * @return
	 */
	public java.lang.String getTargetTerminalCode() {
		return this.targetTerminalCode;
	}
	/**
	 * 渠道返回码
	 * @param channelReturnCode
	 */
	public void setChannelReturnCode(java.lang.String channelReturnCode) {
		this.channelReturnCode = channelReturnCode;
	}
	
	/**
	 * 渠道返回码
	 * @return
	 */
	public java.lang.String getChannelReturnCode() {
		return this.channelReturnCode;
	}
	/**
	 * 返回信息
	 * @param returnMeg
	 */
	public void setReturnMeg(java.lang.String returnMeg) {
		this.returnMeg = returnMeg;
	}
	
	/**
	 * 返回信息
	 * @return
	 */
	public java.lang.String getReturnMeg() {
		return this.returnMeg;
	}
	/**
	 * 小票流水号
	 * @param receiptSerialNo
	 */
	public void setReceiptSerialNo(java.lang.String receiptSerialNo) {
		this.receiptSerialNo = receiptSerialNo;
	}
	
	/**
	 * 小票流水号
	 * @return
	 */
	public java.lang.String getReceiptSerialNo() {
		return this.receiptSerialNo;
	}
	/**
	 * MCC
	 * @param mcc
	 */
	public void setMcc(java.lang.String mcc) {
		this.mcc = mcc;
	}
	
	/**
	 * MCC
	 * @return
	 */
	public java.lang.String getMcc() {
		return this.mcc;
	}
	/**
	 * 授权码
	 * @param authCode
	 */
	public void setAuthCode(java.lang.String authCode) {
		this.authCode = authCode;
	}
	
	/**
	 * 授权码
	 * @return
	 */
	public java.lang.String getAuthCode() {
		return this.authCode;
	}
	/**
	 * 渠道信息
	 * @param channelInfo
	 */
	public void setChannelInfo(java.lang.String channelInfo) {
		this.channelInfo = channelInfo;
	}
	
	/**
	 * 渠道信息
	 * @return
	 */
	public java.lang.String getChannelInfo() {
		return this.channelInfo;
	}
	/**
	 * 银行编号
	 * @param bankCode
	 */
	public void setBankCode(java.lang.String bankCode) {
		this.bankCode = bankCode;
	}
	
	/**
	 * 银行编号
	 * @return
	 */
	public java.lang.String getBankCode() {
		return this.bankCode;
	}
	/**
	 * 交易平台标识
	 * @param tradeEsbNo
	 */
	public void setTradeEsbNo(java.lang.String tradeEsbNo) {
		this.tradeEsbNo = tradeEsbNo;
	}
	
	/**
	 * 交易平台标识
	 * @return
	 */
	public java.lang.String getTradeEsbNo() {
		return this.tradeEsbNo;
	}
	/**
	 * 预留1
	 * @param reserve1
	 */
	public void setReserve1(java.lang.String reserve1) {
		this.reserve1 = reserve1;
	}
	
	/**
	 * 预留1
	 * @return
	 */
	public java.lang.String getReserve1() {
		return this.reserve1;
	}
	/**
	 * 预留2
	 * @param reserve2
	 */
	public void setReserve2(java.lang.String reserve2) {
		this.reserve2 = reserve2;
	}
	
	/**
	 * 预留2
	 * @return
	 */
	public java.lang.String getReserve2() {
		return this.reserve2;
	}
	/**
	 * 预留3
	 * @param reserve3
	 */
	public void setReserve3(java.lang.String reserve3) {
		this.reserve3 = reserve3;
	}
	
	/**
	 * 预留3
	 * @return
	 */
	public java.lang.String getReserve3() {
		return this.reserve3;
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
	 * 交易状态(交易落单状态)
	 * @param statusId
	 */
	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}
	
	/**
	 * 交易状态(交易落单状态)
	 * @return
	 */
	public Integer getStatusId() {
		return this.statusId;
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