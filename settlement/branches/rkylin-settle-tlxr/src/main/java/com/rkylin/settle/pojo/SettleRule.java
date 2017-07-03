/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.settle.pojo;

import java.io.Serializable;

/**
 * SettleRule
 * @author code-generator
 *
 */
public class SettleRule implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private java.lang.Integer ruleId;
	private java.lang.String ruleName;
	private java.lang.String ruleType;
	private java.lang.String readType;
	private java.lang.String detKeyCode;
	private java.lang.String accKeyCode;
	private java.lang.String settleKeyName;
	private java.lang.String ropBatchNo;
	private Integer keyType;
	private java.util.Date startTime;
	private java.util.Date endTime;
	private Integer statusId;
	private java.lang.String remark;
	private java.util.Date createdTime;
	private java.util.Date updatedTime;

	/**
	 * 规则ID
	 * @param ruleId
	 */
	public void setRuleId(java.lang.Integer ruleId) {
		this.ruleId = ruleId;
	}
	
	/**
	 * 规则ID
	 * @return
	 */
	public java.lang.Integer getRuleId() {
		return this.ruleId;
	}
	/**
	 * 对账规则名称
	 * @param ruleName
	 */
	public void setRuleName(java.lang.String ruleName) {
		this.ruleName = ruleName;
	}
	
	/**
	 * 对账规则名称
	 * @return
	 */
	public java.lang.String getRuleName() {
		return this.ruleName;
	}
	/**
	 * 规则种类（渠道，机构）
	 * @param ruleType
	 */
	public void setRuleType(java.lang.String ruleType) {
		this.ruleType = ruleType;
	}
	
	/**
	 * 规则种类（渠道，机构）
	 * @return
	 */
	public java.lang.String getRuleType() {
		return this.ruleType;
	}
	/**
	 * 上游交易类型 网关支付:01, 代收付:02
	 * @param readType
	 */
	public void setReadType(java.lang.String readType) {
		this.readType = readType;
	}
	
	/**
	 * 上游交易类型 网关支付:01, 代收付:02
	 * @return
	 */
	public java.lang.String getReadType() {
		return this.readType;
	}
	/**
	 * 清算方对账项目
	 * @param detKeyCode
	 */
	public void setDetKeyCode(java.lang.String detKeyCode) {
		this.detKeyCode = detKeyCode;
	}
	
	/**
	 * 清算方对账项目
	 * @return
	 */
	public java.lang.String getDetKeyCode() {
		return this.detKeyCode;
	}
	/**
	 * 上游渠道对账项目
	 * @param accKeyCode
	 */
	public void setAccKeyCode(java.lang.String accKeyCode) {
		this.accKeyCode = accKeyCode;
	}
	
	/**
	 * 上游渠道对账项目
	 * @return
	 */
	public java.lang.String getAccKeyCode() {
		return this.accKeyCode;
	}
	/**
	 * 对账名
	 * @param settleKeyName
	 */
	public void setSettleKeyName(java.lang.String settleKeyName) {
		this.settleKeyName = settleKeyName;
	}
	
	/**
	 * 对账名
	 * @return
	 */
	public java.lang.String getSettleKeyName() {
		return this.settleKeyName;
	}
	/**
	 * ROP文件批次号
	 * @param ropBatchNo
	 */
	public void setRopBatchNo(java.lang.String ropBatchNo) {
		this.ropBatchNo = ropBatchNo;
	}
	
	/**
	 * ROP文件批次号
	 * @return
	 */
	public java.lang.String getRopBatchNo() {
		return this.ropBatchNo;
	}
	/**
	 * 对账类型0:对账key,1:对账项目
	 * @param keyType
	 */
	public void setKeyType(Integer keyType) {
		this.keyType = keyType;
	}
	
	/**
	 * 对账类型0:对账key,1:对账项目
	 * @return
	 */
	public Integer getKeyType() {
		return this.keyType;
	}
	/**
	 * 生效时间
	 * @param startTime
	 */
	public void setStartTime(java.util.Date startTime) {
		this.startTime = startTime;
	}
	
	/**
	 * 生效时间
	 * @return
	 */
	public java.util.Date getStartTime() {
		return this.startTime;
	}
	/**
	 * 失效时间
	 * @param endTime
	 */
	public void setEndTime(java.util.Date endTime) {
		this.endTime = endTime;
	}
	
	/**
	 * 失效时间
	 * @return
	 */
	public java.util.Date getEndTime() {
		return this.endTime;
	}
	/**
	 * 状态,1:生效,0:失效
	 * @param statusId
	 */
	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}
	
	/**
	 * 状态,1:生效,0:失效
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