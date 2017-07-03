/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.settle.pojo;

import java.io.Serializable;

/**
 * SettleBalanceEntry
 * @author code-generator
 *
 */
public class SettleBalanceEntry implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private java.lang.Integer balanceEntryId;
	private java.lang.String orderNo;
	private Integer balanceType;
	private java.lang.String obligate1;
	private java.lang.String obligate2;
	private Integer statusId;
	private java.lang.String remark;
	private java.util.Date accountDate;
	private java.util.Date createdTime;
	private java.util.Date updatedTime;

	/**
	 * 对账id
	 * @param balanceEntryId
	 */
	public void setBalanceEntryId(java.lang.Integer balanceEntryId) {
		this.balanceEntryId = balanceEntryId;
	}
	
	/**
	 * 对账id
	 * @return
	 */
	public java.lang.Integer getBalanceEntryId() {
		return this.balanceEntryId;
	}
	/**
	 * 交易订单号
	 * @param orderNo
	 */
	public void setOrderNo(java.lang.String orderNo) {
		this.orderNo = orderNo;
	}
	
	/**
	 * 交易订单号
	 * @return
	 */
	public java.lang.String getOrderNo() {
		return this.orderNo;
	}
	/**
	 * 对账类型0账户,1收银台,2代收付
	 * @param balanceType
	 */
	public void setBalanceType(Integer balanceType) {
		this.balanceType = balanceType;
	}
	
	/**
	 * 对账类型0账户,1收银台,2代收付
	 * @return
	 */
	public Integer getBalanceType() {
		return this.balanceType;
	}
	/**
	 * 预留1
	 * @param obligate1
	 */
	public void setObligate1(java.lang.String obligate1) {
		this.obligate1 = obligate1;
	}
	
	/**
	 * 预留1
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

	@Override
	public String toString() {
		return "[balanceEntryId=" + balanceEntryId
				+ ", orderNo=" + orderNo + ", balanceType=" + balanceType
				+ ", obligate1=" + obligate1 + ", obligate2=" + obligate2
				+ ", statusId=" + statusId + ", remark=" + remark
				+ ", accountDate=" + accountDate + ", createdTime="
				+ createdTime + ", updatedTime=" + updatedTime + "]";
	}
}