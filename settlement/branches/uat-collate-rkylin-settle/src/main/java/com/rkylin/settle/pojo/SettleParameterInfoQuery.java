/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.settle.pojo;

import java.io.Serializable;

/**
 * SettleParameterInfoQuery
 * @author code-generator
 *
 */
public class SettleParameterInfoQuery implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private java.lang.Integer parameterId;
	private java.lang.String parameterType;
	private java.lang.String productId;
	private java.lang.String parameterCode;
	private java.lang.String parameterValue;
	private java.lang.String obligate1;
	private java.lang.String obligate2;
	private java.lang.String obligate3;
	private Integer statusId;
	private java.lang.String remark;
	private java.util.Date createdTime;
	private java.util.Date updatedTime;

	/**
	 * 参数ID
	 * @param parameterId
	 */
	public void setParameterId(java.lang.Integer parameterId) {
		this.parameterId = parameterId;
	}
	
	/**
	 * 参数ID
	 * @return
	 */
	public java.lang.Integer getParameterId() {
		return this.parameterId;
	}
	/**
	 * 参数类型
	 * @param parameterType
	 */
	public void setParameterType(java.lang.String parameterType) {
		this.parameterType = parameterType;
	}
	
	/**
	 * 参数类型
	 * @return
	 */
	public java.lang.String getParameterType() {
		return this.parameterType;
	}
	/**
	 * 参数适用产品ID
	 * @param productId
	 */
	public void setProductId(java.lang.String productId) {
		this.productId = productId;
	}
	
	/**
	 * 参数适用产品ID
	 * @return
	 */
	public java.lang.String getProductId() {
		return this.productId;
	}
	/**
	 * 参数编码
	 * @param parameterCode
	 */
	public void setParameterCode(java.lang.String parameterCode) {
		this.parameterCode = parameterCode;
	}
	
	/**
	 * 参数编码
	 * @return
	 */
	public java.lang.String getParameterCode() {
		return this.parameterCode;
	}
	/**
	 * 参数值
	 * @param parameterValue
	 */
	public void setParameterValue(java.lang.String parameterValue) {
		this.parameterValue = parameterValue;
	}
	
	/**
	 * 参数值
	 * @return
	 */
	public java.lang.String getParameterValue() {
		return this.parameterValue;
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
	 * 状态,1生效;0失效
	 * @param statusId
	 */
	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}
	
	/**
	 * 状态,1生效;0失效
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