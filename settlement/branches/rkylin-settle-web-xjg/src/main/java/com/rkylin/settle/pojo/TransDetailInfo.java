/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.settle.pojo;

import java.io.Serializable;

/**
 * TransDetailInfo
 * @author code-generator
 *
 */
public class TransDetailInfo implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private java.lang.Integer transDetailId;
	private java.lang.String parentId;
	private java.lang.String requestNo;
	private java.lang.String sysNo;
	private java.lang.String transCode;
	private java.lang.String busiCode;
	private java.lang.String orgNo;
	private java.lang.String orgCode;
	private java.lang.String channelNo;
	private java.lang.String channelHome;
	private java.lang.String batchNo;
	private java.lang.String transNo;
	private java.lang.String gatewayBatchNo;
	private java.lang.String gatewayTransNo;
	private java.lang.Long payAmount;
	private java.lang.String currency;
	private java.lang.String payerAccountNo;
	private Integer payerAccountType;
	private java.lang.String payerAccountName;
	private java.lang.String payerBankNo;
	private java.lang.String payerBankName;
	private java.lang.String payerDistrictCode;
	private java.lang.String receiverAccountNo;
	private Integer receiverAccountType;
	private java.lang.String receiverAccountName;
	private java.lang.String receiverBankNo;
	private java.lang.String receiverBankName;
	private java.lang.String receiverDistrictCode;
	private java.lang.String receiverProvince;
	private java.lang.String receiverCity;
	private java.lang.String protocolNo;
	private java.lang.String protocolUser;
	private java.util.Date payTime;
	private java.lang.String summary;
	private java.lang.String purpose;
	private java.lang.String orderNo;
	private Integer sysFlag;
	private Integer splitInfo;
	private java.lang.String bankAcceptNo;
	private java.lang.String bankFlowNo;
	private java.lang.String bankOrderNo;
	private java.lang.String returnCode;
	private java.lang.String returnMsg;
	private java.util.Date returnTime;
	private Integer statusId;
	private java.lang.String remark;
	private java.lang.String expand1;
	private java.lang.String expand2;
	private java.lang.String expand3;
	private java.util.Date createdTime;
	private java.util.Date updatedTime;

	/**
	 * 明细ID
	 * @param transDetailId
	 */
	public void setTransDetailId(java.lang.Integer transDetailId) {
		this.transDetailId = transDetailId;
	}
	
	/**
	 * 明细ID
	 * @return
	 */
	public java.lang.Integer getTransDetailId() {
		return this.transDetailId;
	}
	/**
	 * 父明细ID
	 * @param parentId
	 */
	public void setParentId(java.lang.String parentId) {
		this.parentId = parentId;
	}
	
	/**
	 * 父明细ID
	 * @return
	 */
	public java.lang.String getParentId() {
		return this.parentId;
	}
	/**
	 * 请求编号
	 * @param requestNo
	 */
	public void setRequestNo(java.lang.String requestNo) {
		this.requestNo = requestNo;
	}
	
	/**
	 * 请求编号
	 * @return
	 */
	public java.lang.String getRequestNo() {
		return this.requestNo;
	}
	/**
	 * 系统编号
	 * @param sysNo
	 */
	public void setSysNo(java.lang.String sysNo) {
		this.sysNo = sysNo;
	}
	
	/**
	 * 系统编号
	 * @return
	 */
	public java.lang.String getSysNo() {
		return this.sysNo;
	}
	/**
	 * 交易代码
	 * @param transCode
	 */
	public void setTransCode(java.lang.String transCode) {
		this.transCode = transCode;
	}
	
	/**
	 * 交易代码
	 * @return
	 */
	public java.lang.String getTransCode() {
		return this.transCode;
	}
	/**
	 * 业务代码
	 * @param busiCode
	 */
	public void setBusiCode(java.lang.String busiCode) {
		this.busiCode = busiCode;
	}
	
	/**
	 * 业务代码
	 * @return
	 */
	public java.lang.String getBusiCode() {
		return this.busiCode;
	}
	/**
	 * 机构号
	 * @param orgNo
	 */
	public void setOrgNo(java.lang.String orgNo) {
		this.orgNo = orgNo;
	}
	
	/**
	 * 机构号
	 * @return
	 */
	public java.lang.String getOrgNo() {
		return this.orgNo;
	}
	/**
	 * 机构代码
	 * @param orgCode
	 */
	public void setOrgCode(java.lang.String orgCode) {
		this.orgCode = orgCode;
	}
	
	/**
	 * 机构代码
	 * @return
	 */
	public java.lang.String getOrgCode() {
		return this.orgCode;
	}
	/**
	 * 渠道编号
	 * @param channelNo
	 */
	public void setChannelNo(java.lang.String channelNo) {
		this.channelNo = channelNo;
	}
	
	/**
	 * 渠道编号
	 * @return
	 */
	public java.lang.String getChannelNo() {
		return this.channelNo;
	}
	/**
	 * 渠道商
	 * @param channelHome
	 */
	public void setChannelHome(java.lang.String channelHome) {
		this.channelHome = channelHome;
	}
	
	/**
	 * 渠道商
	 * @return
	 */
	public java.lang.String getChannelHome() {
		return this.channelHome;
	}
	/**
	 * 批次号
	 * @param batchNo
	 */
	public void setBatchNo(java.lang.String batchNo) {
		this.batchNo = batchNo;
	}
	
	/**
	 * 批次号
	 * @return
	 */
	public java.lang.String getBatchNo() {
		return this.batchNo;
	}
	/**
	 * 流水号
	 * @param transNo
	 */
	public void setTransNo(java.lang.String transNo) {
		this.transNo = transNo;
	}
	
	/**
	 * 流水号
	 * @return
	 */
	public java.lang.String getTransNo() {
		return this.transNo;
	}
	/**
	 * 支付网关批次号
	 * @param gatewayBatchNo
	 */
	public void setGatewayBatchNo(java.lang.String gatewayBatchNo) {
		this.gatewayBatchNo = gatewayBatchNo;
	}
	
	/**
	 * 支付网关批次号
	 * @return
	 */
	public java.lang.String getGatewayBatchNo() {
		return this.gatewayBatchNo;
	}
	/**
	 * 支付网关流水号
	 * @param gatewayTransNo
	 */
	public void setGatewayTransNo(java.lang.String gatewayTransNo) {
		this.gatewayTransNo = gatewayTransNo;
	}
	
	/**
	 * 支付网关流水号
	 * @return
	 */
	public java.lang.String getGatewayTransNo() {
		return this.gatewayTransNo;
	}
	/**
	 * 付款金额
	 * @param payAmount
	 */
	public void setPayAmount(java.lang.Long payAmount) {
		this.payAmount = payAmount;
	}
	
	/**
	 * 付款金额
	 * @return
	 */
	public java.lang.Long getPayAmount() {
		return this.payAmount;
	}
	/**
	 * 币种
	 * @param currency
	 */
	public void setCurrency(java.lang.String currency) {
		this.currency = currency;
	}
	
	/**
	 * 币种
	 * @return
	 */
	public java.lang.String getCurrency() {
		return this.currency;
	}
	/**
	 * 付款账号
	 * @param payerAccountNo
	 */
	public void setPayerAccountNo(java.lang.String payerAccountNo) {
		this.payerAccountNo = payerAccountNo;
	}
	
	/**
	 * 付款账号
	 * @return
	 */
	public java.lang.String getPayerAccountNo() {
		return this.payerAccountNo;
	}
	/**
	 * 付款账号类型：1个人账号，2企业账号
	 * @param payerAccountType
	 */
	public void setPayerAccountType(Integer payerAccountType) {
		this.payerAccountType = payerAccountType;
	}
	
	/**
	 * 付款账号类型：1个人账号，2企业账号
	 * @return
	 */
	public Integer getPayerAccountType() {
		return this.payerAccountType;
	}
	/**
	 * 付款账户名称
	 * @param payerAccountName
	 */
	public void setPayerAccountName(java.lang.String payerAccountName) {
		this.payerAccountName = payerAccountName;
	}
	
	/**
	 * 付款账户名称
	 * @return
	 */
	public java.lang.String getPayerAccountName() {
		return this.payerAccountName;
	}
	/**
	 * 付款开户行号，联行号
	 * @param payerBankNo
	 */
	public void setPayerBankNo(java.lang.String payerBankNo) {
		this.payerBankNo = payerBankNo;
	}
	
	/**
	 * 付款开户行号，联行号
	 * @return
	 */
	public java.lang.String getPayerBankNo() {
		return this.payerBankNo;
	}
	/**
	 * 付款开户行名称
	 * @param payerBankName
	 */
	public void setPayerBankName(java.lang.String payerBankName) {
		this.payerBankName = payerBankName;
	}
	
	/**
	 * 付款开户行名称
	 * @return
	 */
	public java.lang.String getPayerBankName() {
		return this.payerBankName;
	}
	/**
	 * 付款地区代码
	 * @param payerDistrictCode
	 */
	public void setPayerDistrictCode(java.lang.String payerDistrictCode) {
		this.payerDistrictCode = payerDistrictCode;
	}
	
	/**
	 * 付款地区代码
	 * @return
	 */
	public java.lang.String getPayerDistrictCode() {
		return this.payerDistrictCode;
	}
	/**
	 * 收款账号
	 * @param receiverAccountNo
	 */
	public void setReceiverAccountNo(java.lang.String receiverAccountNo) {
		this.receiverAccountNo = receiverAccountNo;
	}
	
	/**
	 * 收款账号
	 * @return
	 */
	public java.lang.String getReceiverAccountNo() {
		return this.receiverAccountNo;
	}
	/**
	 * 收款账号类型：1个人账号，2企业账号
	 * @param receiverAccountType
	 */
	public void setReceiverAccountType(Integer receiverAccountType) {
		this.receiverAccountType = receiverAccountType;
	}
	
	/**
	 * 收款账号类型：1个人账号，2企业账号
	 * @return
	 */
	public Integer getReceiverAccountType() {
		return this.receiverAccountType;
	}
	/**
	 * 收款账户名称
	 * @param receiverAccountName
	 */
	public void setReceiverAccountName(java.lang.String receiverAccountName) {
		this.receiverAccountName = receiverAccountName;
	}
	
	/**
	 * 收款账户名称
	 * @return
	 */
	public java.lang.String getReceiverAccountName() {
		return this.receiverAccountName;
	}
	/**
	 * 收款开户行号，联行号
	 * @param receiverBankNo
	 */
	public void setReceiverBankNo(java.lang.String receiverBankNo) {
		this.receiverBankNo = receiverBankNo;
	}
	
	/**
	 * 收款开户行号，联行号
	 * @return
	 */
	public java.lang.String getReceiverBankNo() {
		return this.receiverBankNo;
	}
	/**
	 * 收款开户行名称
	 * @param receiverBankName
	 */
	public void setReceiverBankName(java.lang.String receiverBankName) {
		this.receiverBankName = receiverBankName;
	}
	
	/**
	 * 收款开户行名称
	 * @return
	 */
	public java.lang.String getReceiverBankName() {
		return this.receiverBankName;
	}
	/**
	 * 收款地区代码
	 * @param receiverDistrictCode
	 */
	public void setReceiverDistrictCode(java.lang.String receiverDistrictCode) {
		this.receiverDistrictCode = receiverDistrictCode;
	}
	
	/**
	 * 收款地区代码
	 * @return
	 */
	public java.lang.String getReceiverDistrictCode() {
		return this.receiverDistrictCode;
	}
	/**
	 * 收款省份
	 * @param receiverProvince
	 */
	public void setReceiverProvince(java.lang.String receiverProvince) {
		this.receiverProvince = receiverProvince;
	}
	
	/**
	 * 收款省份
	 * @return
	 */
	public java.lang.String getReceiverProvince() {
		return this.receiverProvince;
	}
	/**
	 * 收款城市
	 * @param receiverCity
	 */
	public void setReceiverCity(java.lang.String receiverCity) {
		this.receiverCity = receiverCity;
	}
	
	/**
	 * 收款城市
	 * @return
	 */
	public java.lang.String getReceiverCity() {
		return this.receiverCity;
	}
	/**
	 * 协议号
	 * @param protocolNo
	 */
	public void setProtocolNo(java.lang.String protocolNo) {
		this.protocolNo = protocolNo;
	}
	
	/**
	 * 协议号
	 * @return
	 */
	public java.lang.String getProtocolNo() {
		return this.protocolNo;
	}
	/**
	 * 协议用户
	 * @param protocolUser
	 */
	public void setProtocolUser(java.lang.String protocolUser) {
		this.protocolUser = protocolUser;
	}
	
	/**
	 * 协议用户
	 * @return
	 */
	public java.lang.String getProtocolUser() {
		return this.protocolUser;
	}
	/**
	 * 付款时间
	 * @param payTime
	 */
	public void setPayTime(java.util.Date payTime) {
		this.payTime = payTime;
	}
	
	/**
	 * 付款时间
	 * @return
	 */
	public java.util.Date getPayTime() {
		return this.payTime;
	}
	/**
	 * 摘要
	 * @param summary
	 */
	public void setSummary(java.lang.String summary) {
		this.summary = summary;
	}
	
	/**
	 * 摘要
	 * @return
	 */
	public java.lang.String getSummary() {
		return this.summary;
	}
	/**
	 * 用途
	 * @param purpose
	 */
	public void setPurpose(java.lang.String purpose) {
		this.purpose = purpose;
	}
	
	/**
	 * 用途
	 * @return
	 */
	public java.lang.String getPurpose() {
		return this.purpose;
	}
	/**
	 * 明细序号
	 * @param orderNo
	 */
	public void setOrderNo(java.lang.String orderNo) {
		this.orderNo = orderNo;
	}
	
	/**
	 * 明细序号
	 * @return
	 */
	public java.lang.String getOrderNo() {
		return this.orderNo;
	}
	/**
	 * 系统内外标志:0系统内,1系统外
	 * @param sysFlag
	 */
	public void setSysFlag(Integer sysFlag) {
		this.sysFlag = sysFlag;
	}
	
	/**
	 * 系统内外标志:0系统内,1系统外
	 * @return
	 */
	public Integer getSysFlag() {
		return this.sysFlag;
	}
	/**
	 * 拆分标志:0未拆分,1拆分明细,n主明细
	 * @param splitInfo
	 */
	public void setSplitInfo(Integer splitInfo) {
		this.splitInfo = splitInfo;
	}
	
	/**
	 * 拆分标志:0未拆分,1拆分明细,n主明细
	 * @return
	 */
	public Integer getSplitInfo() {
		return this.splitInfo;
	}
	/**
	 * 银行受理号(子批次受理号)
	 * @param bankAcceptNo
	 */
	public void setBankAcceptNo(java.lang.String bankAcceptNo) {
		this.bankAcceptNo = bankAcceptNo;
	}
	
	/**
	 * 银行受理号(子批次受理号)
	 * @return
	 */
	public java.lang.String getBankAcceptNo() {
		return this.bankAcceptNo;
	}
	/**
	 * 银行交易流水号
	 * @param bankFlowNo
	 */
	public void setBankFlowNo(java.lang.String bankFlowNo) {
		this.bankFlowNo = bankFlowNo;
	}
	
	/**
	 * 银行交易流水号
	 * @return
	 */
	public java.lang.String getBankFlowNo() {
		return this.bankFlowNo;
	}
	/**
	 * 银行返回序号
	 * @param bankOrderNo
	 */
	public void setBankOrderNo(java.lang.String bankOrderNo) {
		this.bankOrderNo = bankOrderNo;
	}
	
	/**
	 * 银行返回序号
	 * @return
	 */
	public java.lang.String getBankOrderNo() {
		return this.bankOrderNo;
	}
	/**
	 * 银行返回码
	 * @param returnCode
	 */
	public void setReturnCode(java.lang.String returnCode) {
		this.returnCode = returnCode;
	}
	
	/**
	 * 银行返回码
	 * @return
	 */
	public java.lang.String getReturnCode() {
		return this.returnCode;
	}
	/**
	 * 银行返回码描述
	 * @param returnMsg
	 */
	public void setReturnMsg(java.lang.String returnMsg) {
		this.returnMsg = returnMsg;
	}
	
	/**
	 * 银行返回码描述
	 * @return
	 */
	public java.lang.String getReturnMsg() {
		return this.returnMsg;
	}
	/**
	 * 返回时间
	 * @param returnTime
	 */
	public void setReturnTime(java.util.Date returnTime) {
		this.returnTime = returnTime;
	}
	
	/**
	 * 返回时间
	 * @return
	 */
	public java.util.Date getReturnTime() {
		return this.returnTime;
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
	 * 扩展1
	 * @param expand1
	 */
	public void setExpand1(java.lang.String expand1) {
		this.expand1 = expand1;
	}
	
	/**
	 * 扩展1
	 * @return
	 */
	public java.lang.String getExpand1() {
		return this.expand1;
	}
	/**
	 * 扩展2
	 * @param expand2
	 */
	public void setExpand2(java.lang.String expand2) {
		this.expand2 = expand2;
	}
	
	/**
	 * 扩展2
	 * @return
	 */
	public java.lang.String getExpand2() {
		return this.expand2;
	}
	/**
	 * 扩展3
	 * @param expand3
	 */
	public void setExpand3(java.lang.String expand3) {
		this.expand3 = expand3;
	}
	
	/**
	 * 扩展3
	 * @return
	 */
	public java.lang.String getExpand3() {
		return this.expand3;
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