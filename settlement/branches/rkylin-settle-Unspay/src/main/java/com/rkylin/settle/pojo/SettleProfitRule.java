/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2016
 */

package com.rkylin.settle.pojo;

import java.io.Serializable;

/**
 * SettleProfitRule
 * @author code-generator
 *
 */
public class SettleProfitRule implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private java.lang.String profitDetailId;
	private java.lang.String subId;
	private Integer profitObject;
	private Integer interProfitObject;
	private Integer referProfitObject;
	private Integer refer2ProfitObject;
	private java.lang.String rootInstCd;
	private java.lang.String productId;
	private java.lang.String userId;
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
	private Integer profitType;
	private Integer isMust;
	private Integer profitMd;
	private java.lang.String profitFee;
	private java.lang.Long feilvUp;
	private java.lang.Long feilvBelow;
	private java.util.Date startTime;
	private java.util.Date endTime;
	private java.lang.String settleObject;
	private java.lang.String settleMain;
	private java.lang.String settleType;
	private java.lang.String apiType;
	private java.lang.String insertTable;
	private java.lang.String obligate1;
	private java.lang.String obligate2;
	private Integer statusId;
	private java.lang.String remark;
	private java.util.Date createdTime;
	private java.util.Date updatedTime;

	/**
	 * 规则明细ID
	 * @param profitDetailId
	 */
	public void setProfitDetailId(java.lang.String profitDetailId) {
		this.profitDetailId = profitDetailId;
	}
	
	/**
	 * 规则明细ID
	 * @return
	 */
	public java.lang.String getProfitDetailId() {
		return this.profitDetailId;
	}
	/**
	 * 子ID
	 * @param subId
	 */
	public void setSubId(java.lang.String subId) {
		this.subId = subId;
	}
	
	/**
	 * 子ID
	 * @return
	 */
	public java.lang.String getSubId() {
		return this.subId;
	}
	/**
	 * 转出方形态1固定,2可变
	 * @param profitObject
	 */
	public void setProfitObject(Integer profitObject) {
		this.profitObject = profitObject;
	}
	
	/**
	 * 转出方形态1固定,2可变
	 * @return
	 */
	public Integer getProfitObject() {
		return this.profitObject;
	}
	/**
	 * 转入方形态1固定,2可变
	 * @param interProfitObject
	 */
	public void setInterProfitObject(Integer interProfitObject) {
		this.interProfitObject = interProfitObject;
	}
	
	/**
	 * 转入方形态1固定,2可变
	 * @return
	 */
	public Integer getInterProfitObject() {
		return this.interProfitObject;
	}
	/**
	 * 相关方形态1固定,2可变
	 * @param referProfitObject
	 */
	public void setReferProfitObject(Integer referProfitObject) {
		this.referProfitObject = referProfitObject;
	}
	
	/**
	 * 相关方形态1固定,2可变
	 * @return
	 */
	public Integer getReferProfitObject() {
		return this.referProfitObject;
	}
	/**
	 * 相关方形态1固定,2可变
	 * @param refer2ProfitObject
	 */
	public void setRefer2ProfitObject(Integer refer2ProfitObject) {
		this.refer2ProfitObject = refer2ProfitObject;
	}
	
	/**
	 * 相关方形态1固定,2可变
	 * @return
	 */
	public Integer getRefer2ProfitObject() {
		return this.refer2ProfitObject;
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
	 * 转出方
	 * @param userId
	 */
	public void setUserId(java.lang.String userId) {
		this.userId = userId;
	}
	
	/**
	 * 转出方
	 * @return
	 */
	public java.lang.String getUserId() {
		return this.userId;
	}
	/**
	 * 角色编号
	 * @param roleCode
	 */
	public void setRoleCode(java.lang.String roleCode) {
		this.roleCode = roleCode;
	}
	
	/**
	 * 角色编号
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
	 * 清分类型:0分润，1代收，2代付等
	 * @param profitType
	 */
	public void setProfitType(Integer profitType) {
		this.profitType = profitType;
	}
	
	/**
	 * 清分类型:0分润，1代收，2代付等
	 * @return
	 */
	public Integer getProfitType() {
		return this.profitType;
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
	 * 分润模式:0为不启用1为按比例计费2为固定金额
	 * @param profitMd
	 */
	public void setProfitMd(Integer profitMd) {
		this.profitMd = profitMd;
	}
	
	/**
	 * 分润模式:0为不启用1为按比例计费2为固定金额
	 * @return
	 */
	public Integer getProfitMd() {
		return this.profitMd;
	}
	/**
	 * 根据分润模式填写内容
	 * @param profitFee
	 */
	public void setProfitFee(java.lang.String profitFee) {
		this.profitFee = profitFee;
	}
	
	/**
	 * 根据分润模式填写内容
	 * @return
	 */
	public java.lang.String getProfitFee() {
		return this.profitFee;
	}
	/**
	 * 封顶值
	 * @param feilvUp
	 */
	public void setFeilvUp(java.lang.Long feilvUp) {
		this.feilvUp = feilvUp;
	}
	
	/**
	 * 封顶值
	 * @return
	 */
	public java.lang.Long getFeilvUp() {
		return this.feilvUp;
	}
	/**
	 * 封底值
	 * @param feilvBelow
	 */
	public void setFeilvBelow(java.lang.Long feilvBelow) {
		this.feilvBelow = feilvBelow;
	}
	
	/**
	 * 封底值
	 * @return
	 */
	public java.lang.Long getFeilvBelow() {
		return this.feilvBelow;
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
	 * 数据方向1:分润表,2:挂账表
	 * @param insertTable
	 */
	public void setInsertTable(java.lang.String insertTable) {
		this.insertTable = insertTable;
	}
	
	/**
	 * 数据方向1:分润表,2:挂账表
	 * @return
	 */
	public java.lang.String getInsertTable() {
		return this.insertTable;
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