package com.rkylin.settle.pojo;

import java.io.Serializable;

/**
 * SettleTransSummaryQuery
 * @author code-generator
 *
 */
public class SettleTransSummaryQuery implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private java.lang.Integer transSumId;
	private java.lang.String rootInstCd;
	private java.lang.String userId;
	private java.lang.String funcCode;
	private java.util.Date accountDate;
	private java.lang.String orderNo;
	private java.lang.String batchNo;
	private java.lang.Long amount;
	private Integer statusId;
	private java.util.Date createdTime;
	private java.util.Date updatedTime;
	private java.lang.String businessType;
	private java.lang.String obligate1;
	private java.lang.String obligate2;
	private java.lang.String obligate3;
	private java.lang.String remark;
	private Integer[] statusIds;
	
	public java.lang.String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(java.lang.String businessType) {
		this.businessType = businessType;
	}

	public java.lang.String getObligate1() {
		return obligate1;
	}

	public void setObligate1(java.lang.String obligate1) {
		this.obligate1 = obligate1;
	}

	public java.lang.String getObligate2() {
		return obligate2;
	}

	public void setObligate2(java.lang.String obligate2) {
		this.obligate2 = obligate2;
	}

	public java.lang.String getObligate3() {
		return obligate3;
	}

	public void setObligate3(java.lang.String obligate3) {
		this.obligate3 = obligate3;
	}

	public java.lang.String getRemark() {
		return remark;
	}

	public void setRemark(java.lang.String remark) {
		this.remark = remark;
	}

	public Integer[] getStatusIds() {
		return statusIds;
	}

	public void setStatusIds(Integer[] statusIds) {
		this.statusIds = statusIds;
	}

	/**
	 * 汇总交易ID
	 * @param transSumId
	 */
	public void setTransSumId(java.lang.Integer transSumId) {
		this.transSumId = transSumId;
	}
	
	/**
	 * 汇总交易ID
	 * @return
	 */
	public java.lang.Integer getTransSumId() {
		return this.transSumId;
	}
	/**
	 * 管理机构号
	 * @param rootInstCd
	 */
	public void setRootInstCd(java.lang.String rootInstCd) {
		this.rootInstCd = rootInstCd;
	}
	
	/**
	 * 管理机构号
	 * @return
	 */
	public java.lang.String getRootInstCd() {
		return this.rootInstCd;
	}
	/**
	 * 用户号
	 * @param userId
	 */
	public void setUserId(java.lang.String userId) {
		this.userId = userId;
	}
	
	/**
	 * 用户号
	 * @return
	 */
	public java.lang.String getUserId() {
		return this.userId;
	}
	
	public java.lang.String getFuncCode() {
		return funcCode;
	}

	public void setFuncCode(java.lang.String funcCode) {
		this.funcCode = funcCode;
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
	 * 批次号（跟结算单表的BATCH_NO关联）
	 * @param batchNo
	 */
	public void setBatchNo(java.lang.String batchNo) {
		this.batchNo = batchNo;
	}
	
	/**
	 * 批次号（跟结算单表的BATCH_NO关联）
	 * @return
	 */
	public java.lang.String getBatchNo() {
		return this.batchNo;
	}
	/**
	 * 金额(分)
	 * @param amount
	 */
	public void setAmount(java.lang.Long amount) {
		this.amount = amount;
	}
	
	/**
	 * 金额(分)
	 * @return
	 */
	public java.lang.Long getAmount() {
		return this.amount;
	}
	/**
	 * 状态: 0未处理,1处理中,2成功，3失败
	 * @param statusId
	 */
	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}
	
	/**
	 * 状态: 0未处理,1处理中,2成功，3失败
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