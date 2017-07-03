package com.rkylin.settle.pojo;

import java.io.Serializable;

/**
 * SettleTransInvoice
 * @author code-generator
 *
 */
public class SettleTransInvoice implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private java.lang.Integer invoiceNo;
	private java.lang.String requestNo;
	private java.lang.String bussinessCode;
	private java.lang.String rootInstCd;
	private java.lang.String merchantCode;
	private java.lang.String funcCode;
	private Integer orderType;
	private java.lang.String orderNo;
	private java.lang.String batchNo;
	private java.lang.String geneSeq;
	private java.lang.String userId;
	private java.lang.String bankCode;
	private java.lang.String accountType;
	private java.lang.String accountNo;
	private java.lang.String accountName;
	private java.lang.String accountProperty;
	private java.lang.String province;
	private java.lang.String city;
	private java.lang.String openBankName;
	private java.lang.String payBankCode;
	private java.lang.Long amount;
	private java.lang.String currency;
	private java.lang.String certificateType;
	private java.lang.String certificateNumber;
	private java.lang.Integer dataSource;
	private java.lang.String processResult;
	private Integer sendType;
	private java.lang.String errorCode;
	private Integer sendTimes;
	private Integer statusId;
	private java.util.Date accountDate;
	private Integer realTimeFlag;
	private java.lang.String remark;
	private java.util.Date createdTime;
	private java.util.Date updatedTime;
	
	public java.lang.String getMerchantCode() {
		return merchantCode;
	}

	public void setMerchantCode(java.lang.String merchantCode) {
		this.merchantCode = merchantCode;
	}

	/**
	 * 订单号
	 * @param invoiceNo
	 */
	public void setOrderNo(java.lang.String orderNo) {
		this.orderNo = orderNo;
	}
	
	/**
	 * 订单号
	 * @return
	 */
	public java.lang.String getOrderNo() {
		return orderNo;
	}

	/**
	 * 结算单号
	 * @param invoiceNo
	 */
	public void setInvoiceNo(java.lang.Integer invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	
	/**
	 * 结算单号
	 * @return
	 */
	public java.lang.Integer getInvoiceNo() {
		return this.invoiceNo;
	}
	/**
	 * 交易批次号(上游)
	 * @param requestNo
	 */
	public void setRequestNo(java.lang.String requestNo) {
		this.requestNo = requestNo;
	}
	
	/**
	 * 交易批次号(上游)
	 * @return
	 */
	public java.lang.String getRequestNo() {
		return this.requestNo;
	}
	/**
	 * 业务代码
	 * @param bussinessCode
	 */
	public void setBussinessCode(java.lang.String bussinessCode) {
		this.bussinessCode = bussinessCode;
	}
	
	/**
	 * 业务代码
	 * @return
	 */
	public java.lang.String getBussinessCode() {
		return this.bussinessCode;
	}
	/**
	 * 管理机构代码
	 * @param rootInstCd
	 */
	public void setRootInstCd(java.lang.String rootInstCd) {
		this.rootInstCd = rootInstCd;
	}
	
	/**
	 * 管理机构代码
	 * @return
	 */
	public java.lang.String getRootInstCd() {
		return this.rootInstCd;
	}
	/**
	 * 订单类型,代收、代付、提现等
	 * @param orderType
	 */
	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}
	
	/**
	 * 订单类型,代收、代付、提现等
	 * @return
	 */
	public Integer getOrderType() {
		return this.orderType;
	}
	/**
	 * BATCH_NO
	 * @param batchNo
	 */
	public void setBatchNo(java.lang.String batchNo) {
		this.batchNo = batchNo;
	}
	
	/**
	 * BATCH_NO
	 * @return
	 */
	public java.lang.String getBatchNo() {
		return this.batchNo;
	}
	/**
	 * 记录序号
	 * @param geneSeq
	 */
	public void setGeneSeq(java.lang.String geneSeq) {
		this.geneSeq = geneSeq;
	}
	
	/**
	 * 记录序号
	 * @return
	 */
	public java.lang.String getGeneSeq() {
		return this.geneSeq;
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
	 * 银行代码
	 * @param bankCode
	 */
	public void setBankCode(java.lang.String bankCode) {
		this.bankCode = bankCode;
	}
	
	/**
	 * 银行代码
	 * @return
	 */
	public java.lang.String getBankCode() {
		return this.bankCode;
	}
	/**
	 * 账户类型
	 * @param accountType
	 */
	public void setAccountType(java.lang.String accountType) {
		this.accountType = accountType;
	}
	
	/**
	 * 账户类型
	 * @return
	 */
	public java.lang.String getAccountType() {
		return this.accountType;
	}
	/**
	 * 账号
	 * @param accountNo
	 */
	public void setAccountNo(java.lang.String accountNo) {
		this.accountNo = accountNo;
	}
	
	/**
	 * 账号
	 * @return
	 */
	public java.lang.String getAccountNo() {
		return this.accountNo;
	}
	/**
	 * 账号名
	 * @param accountName
	 */
	public void setAccountName(java.lang.String accountName) {
		this.accountName = accountName;
	}
	
	/**
	 * 账号名
	 * @return
	 */
	public java.lang.String getAccountName() {
		return this.accountName;
	}
	/**
	 * 账号属性
	 * @param accountProperty
	 */
	public void setAccountProperty(java.lang.String accountProperty) {
		this.accountProperty = accountProperty;
	}
	
	/**
	 * 账号属性
	 * @return
	 */
	public java.lang.String getAccountProperty() {
		return this.accountProperty;
	}
	/**
	 * 开户行所在省
	 * @param province
	 */
	public void setProvince(java.lang.String province) {
		this.province = province;
	}
	
	/**
	 * 开户行所在省
	 * @return
	 */
	public java.lang.String getProvince() {
		return this.province;
	}
	/**
	 * 开户行所在市
	 * @param city
	 */
	public void setCity(java.lang.String city) {
		this.city = city;
	}
	
	/**
	 * 开户行所在市
	 * @return
	 */
	public java.lang.String getCity() {
		return this.city;
	}
	/**
	 * 开户行名称
	 * @param openBankName
	 */
	public void setOpenBankName(java.lang.String openBankName) {
		this.openBankName = openBankName;
	}
	
	/**
	 * 开户行名称
	 * @return
	 */
	public java.lang.String getOpenBankName() {
		return this.openBankName;
	}
	/**
	 * 支付行号
	 * @param payBankCode
	 */
	public void setPayBankCode(java.lang.String payBankCode) {
		this.payBankCode = payBankCode;
	}
	
	/**
	 * 支付行号
	 * @return
	 */
	public java.lang.String getPayBankCode() {
		return this.payBankCode;
	}
	/**
	 * 金额
	 * @param amount
	 */
	public void setAmount(java.lang.Long amount) {
		this.amount = amount;
	}
	
	/**
	 * 金额
	 * @return
	 */
	public java.lang.Long getAmount() {
		return this.amount;
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
	 * 开户证件类型
	 * @param certificateType
	 */
	public void setCertificateType(java.lang.String certificateType) {
		this.certificateType = certificateType;
	}
	
	/**
	 * 开户证件类型
	 * @return
	 */
	public java.lang.String getCertificateType() {
		return this.certificateType;
	}
	/**
	 * 证件号
	 * @param certificateNumber
	 */
	public void setCertificateNumber(java.lang.String certificateNumber) {
		this.certificateNumber = certificateNumber;
	}
	
	/**
	 * 证件号
	 * @return
	 */
	public java.lang.String getCertificateNumber() {
		return this.certificateNumber;
	}
	/**
	 * 数据来源：0是文件导入，1系统推送，2手工重付
	 * @param dataSource
	 */
	public void setDataSource(java.lang.Integer dataSource) {
		this.dataSource = dataSource;
	}
	
	/**
	 * 数据来源：0是文件导入，1系统推送，2手工重付
	 * @return
	 */
	public java.lang.Integer getDataSource() {
		return this.dataSource;
	}
	/**
	 * 处理结果
	 * @param processResult
	 */
	public void setProcessResult(java.lang.String processResult) {
		this.processResult = processResult;
	}
	
	/**
	 * 处理结果
	 * @return
	 */
	public java.lang.String getProcessResult() {
		return this.processResult;
	}
	/**
	 * 发送类型,0正常,1代扣失败,2代扣延迟
	 * @param sendType
	 */
	public void setSendType(Integer sendType) {
		this.sendType = sendType;
	}
	
	/**
	 * 发送类型,0正常,1代扣失败,2代扣延迟
	 * @return
	 */
	public Integer getSendType() {
		return this.sendType;
	}
	/**
	 * 错误码
	 * @param errorCode
	 */
	public void setErrorCode(java.lang.String errorCode) {
		this.errorCode = errorCode;
	}
	
	/**
	 * 错误码
	 * @return
	 */
	public java.lang.String getErrorCode() {
		return this.errorCode;
	}
	/**
	 * 错误码
	 * @param sendTimes
	 */
	public void setSendTimes(Integer sendTimes) {
		this.sendTimes = sendTimes;
	}
	
	/**
	 * 错误码
	 * @return
	 */
	public Integer getSendTimes() {
		return this.sendTimes;
	}
	/**
	 * 状态： 0未处理,1处理中,2成功，3失败
	 * @param statusId
	 */
	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}
	
	/**
	 * 状态： 0未处理,1处理中,2成功，3失败
	 * @return
	 */
	public Integer getStatusId() {
		return this.statusId;
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
	 * 实时标识 ：0否，1是
	 * @param realTimeFlag
	 */
	public void setRealTimeFlag(Integer realTimeFlag) {
		this.realTimeFlag = realTimeFlag;
	}
	
	/**
	 * 实时标识 ：0否，1是
	 * @return
	 */
	public Integer getRealTimeFlag() {
		return this.realTimeFlag;
	}
	/**
	 * 用户备注
	 * @param remark
	 */
	public void setRemark(java.lang.String remark) {
		this.remark = remark;
	}
	
	/**
	 * 用户备注
	 * @return
	 */
	public java.lang.String getRemark() {
		return this.remark;
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

	public java.lang.String getFuncCode() {
		return funcCode;
	}

	public void setFuncCode(java.lang.String funcCode) {
		this.funcCode = funcCode;
	}
	
}