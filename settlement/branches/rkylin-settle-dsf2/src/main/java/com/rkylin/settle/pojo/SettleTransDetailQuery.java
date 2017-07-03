/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2016
 */

package com.rkylin.settle.pojo;

import java.io.Serializable;

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
	private java.util.Date minAccountDate;//大于某个账期
	// 分页
	private java.lang.String orderBy;
	private Integer offset;
	private java.lang.String limit;
	private Integer pageIndex;
	private Integer pageSize;
	//账期开始
	private java.lang.String accountDateStrBegin;
	//账期结束
	private java.lang.String accountDateStrEnd;
	
	/*
	 * 分页
	 */
	public java.lang.String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(java.lang.String orderBy) {
		this.orderBy = orderBy;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public java.lang.String getLimit() {
		return limit;
	}

	public void setLimit(java.lang.String limit) {
		this.limit = limit;
	}

	public Integer getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(Integer pageIndex) {
		this.pageIndex = pageIndex;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	
	/*
	 * 开始账期
	 */
	public java.lang.String getAccountDateStrBegin() {
		return accountDateStrBegin;
	}

	public void setAccountDateStrBegin(java.lang.String accountDateStrBegin) {
		this.accountDateStrBegin = accountDateStrBegin;
	}
	
	/*
	 * 结束账期
	 */
	public java.lang.String getAccountDateStrEnd() {
		return accountDateStrEnd;
	}

	public void setAccountDateStrEnd(java.lang.String accountDateStrEnd) {
		this.accountDateStrEnd = accountDateStrEnd;
	}

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
	 * 相关订单号(商户订单号)
	 * @param relateOrderNo
	 */
	public void setRelateOrderNo(java.lang.String relateOrderNo) {
		this.relateOrderNo = relateOrderNo;
	}
	
	/**
	 * 相关订单号(商户订单号)
	 * @return
	 */
	public java.lang.String getRelateOrderNo() {
		return this.relateOrderNo;
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
	 * 
	 * @param intoInstCode
	 */
	public void setIntoInstCode(java.lang.String intoInstCode) {
		this.intoInstCode = intoInstCode;
	}
	
	/**
	 * 
	 * @return
	 */
	public java.lang.String getIntoInstCode() {
		return this.intoInstCode;
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
	 * 代收付交易单标记：0未处理,1处理中,2成功，3失败，null非代收付交易单
	 * @param dflag
	 */
	public void setDflag(Integer dflag) {
		this.dflag = dflag;
	}
	
	/**
	 * 代收付交易单标记：0未处理,1处理中,2成功，3失败，null非代收付交易单
	 * @return
	 */
	public Integer getDflag() {
		return this.dflag;
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
	/**
	 * 账户交易ID
	 * @param requestId
	 */
	public void setRequestId(java.lang.Integer requestId) {
		this.requestId = requestId;
	}
	
	/**
	 * 账户交易ID
	 * @return
	 */
	public java.lang.Integer getRequestId() {
		return this.requestId;
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
	 * 撤销标记,0正常，1抹账交易
	 * @param cancelTag
	 */
	public void setCancelTag(Integer cancelTag) {
		this.cancelTag = cancelTag;
	}
	
	/**
	 * 撤销标记,0正常，1抹账交易
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
	 * 账户侧创建时间
	 * @param readCreatedTime
	 */
	public void setReadCreatedTime(java.util.Date readCreatedTime) {
		this.readCreatedTime = readCreatedTime;
	}
	
	/**
	 * 账户侧创建时间
	 * @return
	 */
	public java.util.Date getReadCreatedTime() {
		return this.readCreatedTime;
	}

	public java.util.Date getMinAccountDate() {
		return minAccountDate;
	}

	public void setMinAccountDate(java.util.Date minAccountDate) {
		this.minAccountDate = minAccountDate;
	}
	
}