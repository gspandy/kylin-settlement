/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2016
 */

package com.rkylin.settle.pojo;

import java.io.Serializable;

import com.rop.utils.StringUtils;

/**
 * GenerationPaymentQuery
 * @author code-generator
 *
 */
public class GenerationPaymentQuery implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private java.lang.Long geneId;
	private java.lang.String requestNo;
	private java.lang.String bussinessCode;
	private java.lang.String rootInstCd;
	private java.lang.String orderNo;
	private Integer orderType;
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
	private java.lang.String processResult;
	private Integer sendType;
	private java.lang.String errorCode;
	private Integer statusId;
	private java.util.Date accountDate;
	private java.lang.String remark;
	private java.util.Date createdTime;
	private java.util.Date updatedTime;
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
	/**
	 * 记录号
	 * @param geneId
	 */
	public void setGeneId(java.lang.Long geneId) {
		this.geneId = geneId;
	}
	
	/**
	 * 记录号
	 * @return
	 */
	public java.lang.Long getGeneId() {
		return this.geneId;
	}
	/**
	 * 交易批次号
	 * @param requestNo
	 */
	public void setRequestNo(java.lang.String requestNo) {
		this.requestNo = StringUtils.isEmpty(requestNo)||"-1".equals(requestNo)?null:requestNo;
	}
	
	/**
	 * 交易批次号
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
		this.bussinessCode = StringUtils.isEmpty(bussinessCode)||"-1".equals(bussinessCode)?null:bussinessCode;
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
		this.rootInstCd = StringUtils.isEmpty(rootInstCd)||"-1".equals(rootInstCd)?null:rootInstCd;
	}
	
	/**
	 * 管理机构代码
	 * @return
	 */
	public java.lang.String getRootInstCd() {
		return this.rootInstCd;
	}
	/**
	 * 订单号
	 * @param orderNo
	 */
	public void setOrderNo(java.lang.String orderNo) {
		this.orderNo =  StringUtils.isEmpty(orderNo)||"-1".equals(orderNo)?null:orderNo;
	}
	
	/**
	 * 订单号
	 * @return
	 */
	public java.lang.String getOrderNo() {
		return this.orderNo;
	}
	/**
	 * 订单类型,1债券包,2提现,3还款
	 * @param orderType
	 */
	public void setOrderType(Integer orderType) {
		this.orderType = -1 == orderType ? null : orderType;
	}
	
	/**
	 * 订单类型,1债券包,2提现,3还款
	 * @return
	 */
	public Integer getOrderType() {
		return this.orderType;
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
	 * 
	 * @param userId
	 */
	public void setUserId(java.lang.String userId) {
		this.userId = StringUtils.isEmpty(userId)||"-1".equals(userId)?null:userId;
	}
	
	/**
	 * 
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
		this.bankCode = StringUtils.isEmpty(bankCode)||"-1".equals(bankCode)?null:bankCode;
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
		this.accountNo = StringUtils.isEmpty(accountNo)||"-1".equals(accountNo)?null:accountNo;
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
		this.accountName = StringUtils.isEmpty(accountName)||"-1".equals(accountName)?null:accountName;
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
		this.accountProperty = StringUtils.isEmpty(accountProperty)||"-1".equals(accountProperty)?null:accountProperty;
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
		this.openBankName = StringUtils.isEmpty(openBankName)||"-1".equals(openBankName)?null:openBankName;
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
		this.amount = amount == -1 ? null:amount;
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
	 * 
	 * @param processResult
	 */
	public void setProcessResult(java.lang.String processResult) {
		this.processResult = processResult;
	}
	
	/**
	 * 
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
		this.sendType = sendType == -1 ? null : sendType;
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
	 * 状态,0失效,1生效
	 * @param statusId
	 */
	public void setStatusId(Integer statusId) {
		this.statusId = statusId == -1 ? null : statusId;
	}
	
	/**
	 * 状态,0失效,1生效
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

	public java.lang.String getRequestDateStrBegin() {
		return requestDateStrBegin;
	}

	public void setRequestDateStrBegin(java.lang.String requestDateStrBegin) {
		this.requestDateStrBegin = requestDateStrBegin;
	}

	public java.lang.String getRequestDateStrEnd() {
		return requestDateStrEnd;
	}

	public void setRequestDateStrEnd(java.lang.String requestDateStrEnd) {
		this.requestDateStrEnd = requestDateStrEnd;
	}
	
}