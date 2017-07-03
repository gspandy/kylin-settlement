/*
 * Powered By rongcapital-code-generator
 * Web Site: http://www.rongcapital.cn
 * Since 2014 - 2016
 */

package com.rongcapital.mtkernel.pojo;

import java.io.Serializable;

/**
 * TransEntrySa
 * @author code-generator
 *
 */
public class TransEntrySa implements Serializable{
	private static final long serialVersionUID = 1L;
	
	
	private java.lang.Long id;
	
	private java.lang.String transEntrySaId;
	
	private java.util.Date transDate;
	
	private java.lang.String requestIdFrom;
	
	private java.lang.Long transId;
	
	private Integer transNumber;
	
	private Integer transNo;
	
	private java.lang.String funcCode;
	
	private java.lang.String finAccountId1;
	
	private java.lang.Long paymentAmout1;
	
	private java.lang.String currency1;
	
	private java.lang.String finAccountId2;
	
	private java.lang.Long paymentAmout2;
	
	private java.lang.String currency2;
	
	private Integer accountingStatus;
	
	private java.lang.String reverseNumber;
	
	private java.lang.String referEntryId;
	
	private java.util.Date accountDate;
	
	private Integer statusId;
	
	private java.lang.String remark;
	
	private java.util.Date createdTime;
	
	private java.util.Date updatedTime;

	/**
	 * @param id
	 */
	public void setId(Long id) {
		this.id = id;
	}
	
	/**
	 * @return
	 */
	public Long getId() {
		return this.id;
	}
	/**
	 * 会计交易流水ID
	 * @param transEntrySaId
	 */
	public void setTransEntrySaId(java.lang.String transEntrySaId) {
		this.transEntrySaId = transEntrySaId;
	}
	
	/**
	 * 会计交易流水ID
	 * @return
	 */
	public java.lang.String getTransEntrySaId() {
		return this.transEntrySaId;
	}
	/**
	 * 交易日期
	 * @param transDate
	 */
	public void setTransDate(java.util.Date transDate) {
		this.transDate = transDate;
	}
	
	/**
	 * 交易日期
	 * @return
	 */
	public java.util.Date getTransDate() {
		return this.transDate;
	}
	/**
	 * 交易记录来源
	 * @param requestIdFrom
	 */
	public void setRequestIdFrom(java.lang.String requestIdFrom) {
		this.requestIdFrom = requestIdFrom;
	}
	
	/**
	 * 交易记录来源
	 * @return
	 */
	public java.lang.String getRequestIdFrom() {
		return this.requestIdFrom;
	}
	/**
	 * 交易流水ID
	 * @param transId
	 */
	public void setTransId(java.lang.Long transId) {
		this.transId = transId;
	}
	
	/**
	 * 交易流水ID
	 * @return
	 */
	public java.lang.Long getTransId() {
		return this.transId;
	}
	/**
	 * 交易流水条数
	 * @param transNumber
	 */
	public void setTransNumber(Integer transNumber) {
		this.transNumber = transNumber;
	}
	
	/**
	 * 交易流水条数
	 * @return
	 */
	public Integer getTransNumber() {
		return this.transNumber;
	}
	/**
	 * 流水序号
	 * @param transNo
	 */
	public void setTransNo(Integer transNo) {
		this.transNo = transNo;
	}
	
	/**
	 * 流水序号
	 * @return
	 */
	public Integer getTransNo() {
		return this.transNo;
	}
	/**
	 * 基础功能码
	 * @param funcCode
	 */
	public void setFuncCode(java.lang.String funcCode) {
		this.funcCode = funcCode;
	}
	
	/**
	 * 基础功能码
	 * @return
	 */
	public java.lang.String getFuncCode() {
		return this.funcCode;
	}
	/**
	 * 账户ID1
	 * @param finAccountId1
	 */
	public void setFinAccountId1(java.lang.String finAccountId1) {
		this.finAccountId1 = finAccountId1;
	}
	
	/**
	 * 账户ID1
	 * @return
	 */
	public java.lang.String getFinAccountId1() {
		return this.finAccountId1;
	}
	/**
	 * 发生额1
	 * @param paymentAmout1
	 */
	public void setPaymentAmout1(java.lang.Long paymentAmout1) {
		this.paymentAmout1 = paymentAmout1;
	}
	
	/**
	 * 发生额1
	 * @return
	 */
	public java.lang.Long getPaymentAmout1() {
		return this.paymentAmout1;
	}
	/**
	 * 币种1
	 * @param currency1
	 */
	public void setCurrency1(java.lang.String currency1) {
		this.currency1 = currency1;
	}
	
	/**
	 * 币种1
	 * @return
	 */
	public java.lang.String getCurrency1() {
		return this.currency1;
	}
	/**
	 * 账户ID2
	 * @param finAccountId2
	 */
	public void setFinAccountId2(java.lang.String finAccountId2) {
		this.finAccountId2 = finAccountId2;
	}
	
	/**
	 * 账户ID2
	 * @return
	 */
	public java.lang.String getFinAccountId2() {
		return this.finAccountId2;
	}
	/**
	 * 发生额2
	 * @param paymentAmout2
	 */
	public void setPaymentAmout2(java.lang.Long paymentAmout2) {
		this.paymentAmout2 = paymentAmout2;
	}
	
	/**
	 * 发生额2
	 * @return
	 */
	public java.lang.Long getPaymentAmout2() {
		return this.paymentAmout2;
	}
	/**
	 * 币种2
	 * @param currency2
	 */
	public void setCurrency2(java.lang.String currency2) {
		this.currency2 = currency2;
	}
	
	/**
	 * 币种2
	 * @return
	 */
	public java.lang.String getCurrency2() {
		return this.currency2;
	}
	/**
	 * 分录状态
	 * @param accountingStatus
	 */
	public void setAccountingStatus(Integer accountingStatus) {
		this.accountingStatus = accountingStatus;
	}
	
	/**
	 * 分录状态
	 * @return
	 */
	public Integer getAccountingStatus() {
		return this.accountingStatus;
	}
	/**
	 * 冲正流水号
	 * @param reverseNumber
	 */
	public void setReverseNumber(java.lang.String reverseNumber) {
		this.reverseNumber = reverseNumber;
	}
	
	/**
	 * 冲正流水号
	 * @return
	 */
	public java.lang.String getReverseNumber() {
		return this.reverseNumber;
	}
	/**
	 * 套录号
	 * @param referEntryId
	 */
	public void setReferEntryId(java.lang.String referEntryId) {
		this.referEntryId = referEntryId;
	}
	
	/**
	 * 套录号
	 * @return
	 */
	public java.lang.String getReferEntryId() {
		return this.referEntryId;
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
	 * 状态ID
	 * @param statusId
	 */
	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}
	
	/**
	 * 状态ID
	 * @return
	 */
	public Integer getStatusId() {
		return this.statusId;
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

    @Override
    public String toString() {
        return "TransEntrySa [transId=" + transId + ", transEntrySaId=" + transEntrySaId + ", transDate=" + transDate
                + ", transId=" + transId + ", transNumber=" + transNumber
                + ", transNo=" + transNo + ", funcCode=" + funcCode + ", accountingStatus="
                + accountingStatus + ", reverseNumber=" + reverseNumber + ", referEntryId=" + referEntryId
                + ", accountDate=" + accountDate + ", remark=" + remark + ", createdTime="
                + createdTime + ", updatedTime=" + updatedTime + "]";
    }
	
	
	
}