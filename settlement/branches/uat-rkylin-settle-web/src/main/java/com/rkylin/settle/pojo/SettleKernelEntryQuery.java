/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2016
 */

package com.rkylin.settle.pojo;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

/**
 * SettleKernelEntryQuery
 * @author code-generator
 *
 */
public class SettleKernelEntryQuery implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private java.lang.String transEntrySaId;
	private java.util.Date transDate;
	private java.lang.String requestIdFrom;
	private java.lang.Long transId;
	private Integer transNumber;
	private Integer transNo;
	private java.lang.String funcCode;			//查询条件
	private java.lang.String finAccountId1;		//查询条件
	private java.lang.Long paymentAmount1;
	private java.lang.String currency1;
	private java.lang.String finAccountId2;		//查询条件
	private java.lang.Long paymentAmount2;
	private java.lang.String currency2;
	private Integer accountingStatus;
	private java.lang.String reverseNumber;
	private java.lang.String referEntryId;
	private java.util.Date accountDate;
	private java.lang.String remark1;
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
	private java.lang.String remark2;
	private Integer statusId;					//查询条件
	private Integer readStatusId;
	private java.lang.String rsMsg;
	private java.lang.String obligate1;
	private java.lang.String obligate2;
	private java.lang.String obligate3;
	private java.util.Date createdTime;
	private java.util.Date updatedTime;
	// 分页
    private java.lang.String orderBy;
    private Integer offset;
    private java.lang.String limit;
    private Integer pageIndex;
    private Integer pageSize;
    // 查询条件
    private String accountDateStr;				//查询条件
    
	/**
	 * 会计交易流水ID
	 * @param transEntrySaId
	 */
	public void setTransEntrySaId(java.lang.String transEntrySaId) {
		this.transEntrySaId = StringUtils.isBlank(transEntrySaId) ? null : transEntrySaId;
	}
	
	/**
	 * 会计交易流水ID
	 * @return
	 */
	public java.lang.String getTransEntrySaId() {
		return this.transEntrySaId;
	}
	/**
	 * 交易日期
	 * @param transDate
	 */
	public void setTransDate(java.util.Date transDate) {
		this.transDate = transDate;
	}
	
	/**
	 * 交易日期
	 * @return
	 */
	public java.util.Date getTransDate() {
		return this.transDate;
	}
	/**
	 * 交易记录来源
	 * @param requestIdFrom
	 */
	public void setRequestIdFrom(java.lang.String requestIdFrom) {
		this.requestIdFrom = requestIdFrom;
	}
	
	/**
	 * 交易记录来源
	 * @return
	 */
	public java.lang.String getRequestIdFrom() {
		return this.requestIdFrom;
	}
	/**
	 * 交易流水ID
	 * @param transId
	 */
	public void setTransId(java.lang.Long transId) {
		this.transId = transId;
	}
	
	/**
	 * 交易流水ID
	 * @return
	 */
	public java.lang.Long getTransId() {
		return this.transId;
	}
	/**
	 * 交易流水条数
	 * @param transNumber
	 */
	public void setTransNumber(Integer transNumber) {
		this.transNumber = transNumber;
	}
	
	/**
	 * 交易流水条数
	 * @return
	 */
	public Integer getTransNumber() {
		return this.transNumber;
	}
	/**
	 * 流水序号
	 * @param transNo
	 */
	public void setTransNo(Integer transNo) {
		this.transNo = transNo;
	}
	
	/**
	 * 流水序号
	 * @return
	 */
	public Integer getTransNo() {
		return this.transNo;
	}
	/**
	 * 基础功能码
	 * @param funcCode
	 */
	public void setFuncCode(java.lang.String funcCode) {
		this.funcCode = StringUtils.isBlank(funcCode) ? null : funcCode;
	}
	
	/**
	 * 基础功能码
	 * @return
	 */
	public java.lang.String getFuncCode() {
		return this.funcCode;
	}
	/**
	 * 账户ID1
	 * @param finAccountId1
	 */
	public void setFinAccountId1(java.lang.String finAccountId1) {
		this.finAccountId1 = StringUtils.isBlank(finAccountId1) ? null : finAccountId1;
	}
	
	/**
	 * 账户ID1
	 * @return
	 */
	public java.lang.String getFinAccountId1() {
		return this.finAccountId1;
	}
	
	/**
	 * 币种1
	 * @param currency1
	 */
	public void setCurrency1(java.lang.String currency1) {
		this.currency1 = currency1;
	}
	
	/**
	 * 币种1
	 * @return
	 */
	public java.lang.String getCurrency1() {
		return this.currency1;
	}
	/**
	 * 账户ID2
	 * @param finAccountId2
	 */
	public void setFinAccountId2(java.lang.String finAccountId2) {
		this.finAccountId2 = StringUtils.isBlank(finAccountId2) ? null : finAccountId2;
	}
	
	/**
	 * 账户ID2
	 * @return
	 */
	public java.lang.String getFinAccountId2() {
		return this.finAccountId2;
	}
	
	public java.lang.Long getPaymentAmount1() {
		return paymentAmount1;
	}

	public void setPaymentAmount1(java.lang.Long paymentAmount1) {
		this.paymentAmount1 = paymentAmount1;
	}

	public java.lang.Long getPaymentAmount2() {
		return paymentAmount2;
	}

	public void setPaymentAmount2(java.lang.Long paymentAmount2) {
		this.paymentAmount2 = paymentAmount2;
	}

	/**
	 * 币种2
	 * @param currency2
	 */
	public void setCurrency2(java.lang.String currency2) {
		this.currency2 = currency2;
	}
	
	/**
	 * 币种2
	 * @return
	 */
	public java.lang.String getCurrency2() {
		return this.currency2;
	}
	/**
	 * 分录状态
	 * @param accountingStatus
	 */
	public void setAccountingStatus(Integer accountingStatus) {
		this.accountingStatus = accountingStatus;
	}
	
	/**
	 * 分录状态
	 * @return
	 */
	public Integer getAccountingStatus() {
		return this.accountingStatus;
	}
	/**
	 * 冲正流水号
	 * @param reverseNumber
	 */
	public void setReverseNumber(java.lang.String reverseNumber) {
		this.reverseNumber = reverseNumber;
	}
	
	/**
	 * 冲正流水号
	 * @return
	 */
	public java.lang.String getReverseNumber() {
		return this.reverseNumber;
	}
	/**
	 * 套录号
	 * @param referEntryId
	 */
	public void setReferEntryId(java.lang.String referEntryId) {
		this.referEntryId = referEntryId;
	}
	
	/**
	 * 套录号
	 * @return
	 */
	public java.lang.String getReferEntryId() {
		return this.referEntryId;
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
	 * 备注1
	 * @param remark1
	 */
	public void setRemark1(java.lang.String remark1) {
		this.remark1 = remark1;
	}
	
	/**
	 * 备注1
	 * @return
	 */
	public java.lang.String getRemark1() {
		return this.remark1;
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
	 * 备注2
	 * @param remark2
	 */
	public void setRemark2(java.lang.String remark2) {
		this.remark2 = remark2;
	}
	
	/**
	 * 备注2
	 * @return
	 */
	public java.lang.String getRemark2() {
		return this.remark2;
	}
	/**
	 * 清结算状态ID
	 * @param statusId
	 */
	public void setStatusId(Integer statusId) {
		this.statusId = statusId == -99 ? null : statusId;
	}
	
	/**
	 * 清结算状态ID
	 * @return
	 */
	public Integer getStatusId() {
		return this.statusId;
	}
	/**
	 * 会计系统状态ID
	 * @param readStatusId
	 */
	public void setReadStatusId(Integer readStatusId) {
		this.readStatusId = readStatusId;
	}
	
	/**
	 * 会计系统状态ID
	 * @return
	 */
	public Integer getReadStatusId() {
		return this.readStatusId;
	}
	/**
	 * 备注2
	 * @param rsMsg
	 */
	public void setRsMsg(java.lang.String rsMsg) {
		this.rsMsg = rsMsg;
	}
	
	/**
	 * 备注2
	 * @return
	 */
	public java.lang.String getRsMsg() {
		return this.rsMsg;
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

	public java.lang.String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(java.lang.String orderBy) {
		this.orderBy = orderBy;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public java.lang.String getLimit() {
		return limit;
	}

	public void setLimit(java.lang.String limit) {
		this.limit = limit;
	}

	public Integer getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(Integer pageIndex) {
		this.pageIndex = pageIndex;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public String getAccountDateStr() {
		return accountDateStr;
	}

	public void setAccountDateStr(String accountDateStr) {
		this.accountDateStr = StringUtils.isBlank(accountDateStr) ? null : accountDateStr;
	}
}