/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2016
 */

package com.rkylin.settle.pojo;

import java.io.Serializable;

/**
 * SettleCollectRule
 * @author code-generator
 *
 */
public class SettleCollectRule implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private java.lang.Long id;
	private java.lang.String profitRuleName;
	private java.lang.String rootInstCd;
	private java.lang.String payChannelId;
	private java.lang.String funcCode;
	private java.lang.String kernelFuncCode;
	private java.lang.String accountName1;
	private java.lang.String finAccountId1;
	private java.lang.String accountName2;
	private java.lang.String finAccountId2;
	private java.lang.String obligate1;
	private java.lang.String obligate2;
	private java.lang.String obligate3;
	private Integer collectType;
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
	 * 汇总规则名称
	 * @param profitRuleName
	 */
	public void setProfitRuleName(java.lang.String profitRuleName) {
		this.profitRuleName = profitRuleName;
	}
	
	/**
	 * 汇总规则名称
	 * @return
	 */
	public java.lang.String getProfitRuleName() {
		return this.profitRuleName;
	}
	/**
	 * 机构代码
	 * @param rootInstCd
	 */
	public void setRootInstCd(java.lang.String rootInstCd) {
		this.rootInstCd = rootInstCd;
	}
	
	/**
	 * 机构代码
	 * @return
	 */
	public java.lang.String getRootInstCd() {
		return this.rootInstCd;
	}
	/**
	 * 渠道代码
	 * @param payChannelId
	 */
	public void setPayChannelId(java.lang.String payChannelId) {
		this.payChannelId = payChannelId;
	}
	
	/**
	 * 渠道代码
	 * @return
	 */
	public java.lang.String getPayChannelId() {
		return this.payChannelId;
	}
	/**
	 * 功能码
	 * @param funcCode
	 */
	public void setFuncCode(java.lang.String funcCode) {
		this.funcCode = funcCode;
	}
	
	/**
	 * 功能码
	 * @return
	 */
	public java.lang.String getFuncCode() {
		return this.funcCode;
	}
	/**
	 * 会计功能码
	 * @param kernelFuncCode
	 */
	public void setKernelFuncCode(java.lang.String kernelFuncCode) {
		this.kernelFuncCode = kernelFuncCode;
	}
	
	/**
	 * 会计功能码
	 * @return
	 */
	public java.lang.String getKernelFuncCode() {
		return this.kernelFuncCode;
	}
	/**
	 * 科目名称1
	 * @param accountName1
	 */
	public void setAccountName1(java.lang.String accountName1) {
		this.accountName1 = accountName1;
	}
	
	/**
	 * 科目名称1
	 * @return
	 */
	public java.lang.String getAccountName1() {
		return this.accountName1;
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
	 * 科目名称2
	 * @param accountName2
	 */
	public void setAccountName2(java.lang.String accountName2) {
		this.accountName2 = accountName2;
	}
	
	/**
	 * 科目名称2
	 * @return
	 */
	public java.lang.String getAccountName2() {
		return this.accountName2;
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
	 * 汇总类型 1:日切汇总, 2:对账汇总
	 * @param collectType
	 */
	public void setCollectType(Integer collectType) {
		this.collectType = collectType;
	}
	
	/**
	 * 汇总类型 1:日切汇总, 2:对账汇总
	 * @return
	 */
	public Integer getCollectType() {
		return this.collectType;
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