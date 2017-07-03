/*
 * Powered By rongcapital-code-generator
 * Web Site: http://www.rongcapital.cn
 * Since 2014 - 2016
 */

package com.rongcapital.mtkernel.pojo;

import java.io.Serializable;

/**
 * RegisterFalut
 * @author code-generator
 *
 */
public class RegisterFalut implements Serializable{
	private static final long serialVersionUID = 1L;
	
	
	private java.lang.Long id;
	
	private java.lang.String rootInstCd;
	
	private java.lang.String busiTypeCode;
	
	private java.lang.String merchantNo;
	
	private java.lang.String accountNo;
	
	private java.lang.String settleType;
	
	private java.lang.String payType;
	
	private java.lang.String settleCycle;
	
	private java.lang.String tn;
	
	private java.util.Date settleDay;
	
	private java.util.Date actualSettleDay;
	
	private java.lang.String settleNo;
	
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
	 * 机构号
	 * @param rootInstCd
	 */
	public void setRootInstCd(java.lang.String rootInstCd) {
		this.rootInstCd = rootInstCd;
	}
	
	/**
	 * 机构号
	 * @return
	 */
	public java.lang.String getRootInstCd() {
		return this.rootInstCd;
	}
	/**
	 * 业务种类编码
	 * @param busiTypeCode
	 */
	public void setBusiTypeCode(java.lang.String busiTypeCode) {
		this.busiTypeCode = busiTypeCode;
	}
	
	/**
	 * 业务种类编码
	 * @return
	 */
	public java.lang.String getBusiTypeCode() {
		return this.busiTypeCode;
	}
	/**
	 * 商户号
	 * @param merchantNo
	 */
	public void setMerchantNo(java.lang.String merchantNo) {
		this.merchantNo = merchantNo;
	}
	
	/**
	 * 商户号
	 * @return
	 */
	public java.lang.String getMerchantNo() {
		return this.merchantNo;
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
	 * 结算类型
	 * @param settleType
	 */
	public void setSettleType(java.lang.String settleType) {
		this.settleType = settleType;
	}
	
	/**
	 * 结算类型
	 * @return
	 */
	public java.lang.String getSettleType() {
		return this.settleType;
	}
	/**
	 * 付款类型
	 * @param payType
	 */
	public void setPayType(java.lang.String payType) {
		this.payType = payType;
	}
	
	/**
	 * 付款类型
	 * @return
	 */
	public java.lang.String getPayType() {
		return this.payType;
	}
	/**
	 * 结算周期
	 * @param settleCycle
	 */
	public void setSettleCycle(java.lang.String settleCycle) {
		this.settleCycle = settleCycle;
	}
	
	/**
	 * 结算周期
	 * @return
	 */
	public java.lang.String getSettleCycle() {
		return this.settleCycle;
	}
	/**
	 * T+n
	 * @param tn
	 */
	public void setTn(java.lang.String tn) {
		this.tn = tn;
	}
	
	/**
	 * T+n
	 * @return
	 */
	public java.lang.String getTn() {
		return this.tn;
	}
	/**
	 * 结算日
	 * @param settleDay
	 */
	public void setSettleDay(java.util.Date settleDay) {
		this.settleDay = settleDay;
	}
	
	/**
	 * 结算日
	 * @return
	 */
	public java.util.Date getSettleDay() {
		return this.settleDay;
	}
	/**
	 * 实际结算日
	 * @param actualSettleDay
	 */
	public void setActualSettleDay(java.util.Date actualSettleDay) {
		this.actualSettleDay = actualSettleDay;
	}
	
	/**
	 * 实际结算日
	 * @return
	 */
	public java.util.Date getActualSettleDay() {
		return this.actualSettleDay;
	}
	/**
	 * 结算单号
	 * @param settleNo
	 */
	public void setSettleNo(java.lang.String settleNo) {
		this.settleNo = settleNo;
	}
	
	/**
	 * 结算单号
	 * @return
	 */
	public java.lang.String getSettleNo() {
		return this.settleNo;
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