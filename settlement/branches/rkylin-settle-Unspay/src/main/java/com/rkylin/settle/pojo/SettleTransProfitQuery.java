/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2016
 */

package com.rkylin.settle.pojo;

import java.io.Serializable;

/**
 * SettleTransProfitQuery
 * @author code-generator
 *
 */
public class SettleTransProfitQuery implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private java.lang.Integer transProfitId;
	private java.lang.String orderNo;
	private java.lang.String userId;
	private java.lang.String merchantCode;
	private java.lang.String rootInstCd;
	private java.lang.String productId;
	private Integer profitType;
	private java.lang.Long profitAmount;
	private Integer isMust;
	private java.lang.String settleObject;
	private java.lang.String settleType;
	private java.lang.String settleMain;
	private java.lang.String apiType;
	private java.lang.String roleCode;
	private java.lang.String interUserId;
	private java.lang.String interRoleCode;
	private java.lang.String intoProductId;
	private java.lang.String referUserId;
	private java.lang.String referRoleCode;
	private java.lang.String referProductId;
	private java.lang.String referUserId2;
	private java.lang.String referRoleCode2;
	private java.lang.String referProductId2;
	private java.lang.String userIpAddress;
	private java.lang.String obligate1;
	private java.lang.String obligate2;
	private Integer statusId;
	private java.lang.String remark;
	private java.util.Date accountDate;
	private java.util.Date createdTime;
	private java.util.Date updatedTime;

	/**
	 * 交易分润id
	 * @param transProfitId
	 */
	public void setTransProfitId(java.lang.Integer transProfitId) {
		this.transProfitId = transProfitId;
	}
	
	/**
	 * 交易分润id
	 * @return
	 */
	public java.lang.Integer getTransProfitId() {
		return this.transProfitId;
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
	 * 清分类型:0分润,1代收,2代付等
	 * @param profitType
	 */
	public void setProfitType(Integer profitType) {
		this.profitType = profitType;
	}
	
	/**
	 * 清分类型:0分润,1代收,2代付等
	 * @return
	 */
	public Integer getProfitType() {
		return this.profitType;
	}
	/**
	 * 分润金额
	 * @param profitAmount
	 */
	public void setProfitAmount(java.lang.Long profitAmount) {
		this.profitAmount = profitAmount;
	}
	
	/**
	 * 分润金额
	 * @return
	 */
	public java.lang.Long getProfitAmount() {
		return this.profitAmount;
	}
	/**
	 * 0非必须,1..n必须(优先级)
	 * @param isMust
	 */
	public void setIsMust(Integer isMust) {
		this.isMust = isMust;
	}
	
	/**
	 * 0非必须,1..n必须(优先级)
	 * @return
	 */
	public Integer getIsMust() {
		return this.isMust;
	}
	/**
	 * 结算对象
	 * @param settleObject
	 */
	public void setSettleObject(java.lang.String settleObject) {
		this.settleObject = settleObject;
	}
	
	/**
	 * 结算对象
	 * @return
	 */
	public java.lang.String getSettleObject() {
		return this.settleObject;
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
	 * 结算方式
	 * @param settleMain
	 */
	public void setSettleMain(java.lang.String settleMain) {
		this.settleMain = settleMain;
	}
	
	/**
	 * 结算方式
	 * @return
	 */
	public java.lang.String getSettleMain() {
		return this.settleMain;
	}
	/**
	 * 接口类型1:分润,2,转账
	 * @param apiType
	 */
	public void setApiType(java.lang.String apiType) {
		this.apiType = apiType;
	}
	
	/**
	 * 接口类型1:分润,2,转账
	 * @return
	 */
	public java.lang.String getApiType() {
		return this.apiType;
	}
	/**
	 * 相关方角色编号
	 * @param roleCode
	 */
	public void setRoleCode(java.lang.String roleCode) {
		this.roleCode = roleCode;
	}
	
	/**
	 * 相关方角色编号
	 * @return
	 */
	public java.lang.String getRoleCode() {
		return this.roleCode;
	}
	/**
	 * 转入方
	 * @param interUserId
	 */
	public void setInterUserId(java.lang.String interUserId) {
		this.interUserId = interUserId;
	}
	
	/**
	 * 转入方
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
	 * 转入方产品号
	 * @param intoProductId
	 */
	public void setIntoProductId(java.lang.String intoProductId) {
		this.intoProductId = intoProductId;
	}
	
	/**
	 * 转入方产品号
	 * @return
	 */
	public java.lang.String getIntoProductId() {
		return this.intoProductId;
	}
	/**
	 * 相关方
	 * @param referUserId
	 */
	public void setReferUserId(java.lang.String referUserId) {
		this.referUserId = referUserId;
	}
	
	/**
	 * 相关方
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
	 * 相关方产品号
	 * @param referProductId
	 */
	public void setReferProductId(java.lang.String referProductId) {
		this.referProductId = referProductId;
	}
	
	/**
	 * 相关方产品号
	 * @return
	 */
	public java.lang.String getReferProductId() {
		return this.referProductId;
	}
	/**
	 * 相关方
	 * @param referUserId2
	 */
	public void setReferUserId2(java.lang.String referUserId2) {
		this.referUserId2 = referUserId2;
	}
	
	/**
	 * 相关方
	 * @return
	 */
	public java.lang.String getReferUserId2() {
		return this.referUserId2;
	}
	/**
	 * 相关方角色编号
	 * @param referRoleCode2
	 */
	public void setReferRoleCode2(java.lang.String referRoleCode2) {
		this.referRoleCode2 = referRoleCode2;
	}
	
	/**
	 * 相关方角色编号
	 * @return
	 */
	public java.lang.String getReferRoleCode2() {
		return this.referRoleCode2;
	}
	/**
	 * 相关方产品号
	 * @param referProductId2
	 */
	public void setReferProductId2(java.lang.String referProductId2) {
		this.referProductId2 = referProductId2;
	}
	
	/**
	 * 相关方产品号
	 * @return
	 */
	public java.lang.String getReferProductId2() {
		return this.referProductId2;
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
	 * 预留1:产生此交易结果的交易信息ID
	 * @param obligate1
	 */
	public void setObligate1(java.lang.String obligate1) {
		this.obligate1 = obligate1;
	}
	
	/**
	 * 预留1:产生此交易结果的交易信息ID
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
	 * 状态,0未进行,1已进行,2不处理
	 * @param statusId
	 */
	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}
	
	/**
	 * 状态,0未进行,1已进行,2不处理
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
}