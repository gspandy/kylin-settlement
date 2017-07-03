/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2016
 */

package com.rkylin.settle.pojo;

import java.io.Serializable;

/**
 * ParameterInfo
 * @author code-generator
 *
 */
public class ParameterInfo implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private java.lang.Long id;
	private java.lang.String parameterType;
	private java.lang.String parameterCode;
	private java.lang.String parameterValue;
	private java.util.Date startTime;
	private java.util.Date endTime;
	private java.lang.String reserve1;
	private java.lang.String reserve2;
	private java.lang.String reserve3;
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
	 * 预留1
	 * @param reserve1
	 */
	public void setReserve1(java.lang.String reserve1) {
		this.reserve1 = reserve1;
	}
	
	/**
	 * 预留1
	 * @return
	 */
	public java.lang.String getReserve1() {
		return this.reserve1;
	}
	/**
	 * 预留2
	 * @param reserve2
	 */
	public void setReserve2(java.lang.String reserve2) {
		this.reserve2 = reserve2;
	}
	
	/**
	 * 预留2
	 * @return
	 */
	public java.lang.String getReserve2() {
		return this.reserve2;
	}
	/**
	 * 预留3
	 * @param reserve3
	 */
	public void setReserve3(java.lang.String reserve3) {
		this.reserve3 = reserve3;
	}
	
	/**
	 * 预留3
	 * @return
	 */
	public java.lang.String getReserve3() {
		return this.reserve3;
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