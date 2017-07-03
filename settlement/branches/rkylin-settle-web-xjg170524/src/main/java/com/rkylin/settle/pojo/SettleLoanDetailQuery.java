/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2016
 */

package com.rkylin.settle.pojo;

import java.io.Serializable;

import com.allinpay.ets.client.StringUtil;

/**
 * SettleLoanDetailQuery
 * @author code-generator
 *
 */
public class SettleLoanDetailQuery implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private java.lang.Long loanId;
	private java.lang.String productId;
	private java.lang.String rootInstCd;
	private java.lang.String userId;
	private java.lang.String factorId;
	private java.lang.String outfitId;
	private java.lang.String agentId;
	private java.lang.String intrestPartyId;
	private java.lang.String intrestPartyId2;
	private java.lang.String intrestPartyId3;
	private java.lang.String rateId;
	private java.lang.String loanOrderNo;
	private Integer onePaymentFlg;
	private java.lang.String repaymentCnt;
	private java.lang.String repaymentResult;
	private java.util.Date repaymentDate;
	private java.lang.String overdueFlg;
	private java.lang.String prepaymentFlg;
	private java.lang.String dropClassFlg;
	private java.lang.String dateFlg;
	private java.lang.Long amount;
	private java.lang.Long shouldCapital;
	private java.lang.Long shouldInterest;
	private java.lang.Long overdueFine;
	private java.lang.Long overdueInterest;
	private java.lang.String specialFlg1;
	private java.lang.String specialFlg2;
	private java.lang.String specialFlg3;
	private java.lang.String obligate1;
	private java.lang.String obligate2;
	private java.lang.String obligate3;
	private java.lang.String obligate4;
	private java.lang.String obligate5;
	private java.lang.String obligate6;
	private java.lang.String obligate7;
	private java.lang.String obligate8;
	private java.lang.String obligate9;
	private java.lang.String obligate10;
	private java.lang.String obligate11;
	private java.lang.String obligate12;
	private java.lang.String settleFlg;
	private Integer statusId;
	private java.lang.String errorMsg;
	private java.lang.String returnMsg;
	private java.lang.String remark;
	private java.util.Date createdTime;
	private java.util.Date updatedTime;

	private java.lang.String orderBy;
	private Integer offset;
	private java.lang.String limit;
	private Integer pageIndex;
	private Integer pageSize;
	
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

	/**
	 * 贷款还款明细ID
	 * @param loanId
	 */
	public void setLoanId(java.lang.Long loanId) {
		this.loanId = loanId;
	}
	
	/**
	 * 贷款还款明细ID
	 * @return
	 */
	public java.lang.Long getLoanId() {
		return this.loanId;
	}
	/**
	 * 产品号(零垫资:LOAN0)
	 * @param productId
	 */
	public void setProductId(java.lang.String productId) {
		this.productId = productId;
	}
	
	/**
	 * 产品号(零垫资:LOAN0)
	 * @return
	 */
	public java.lang.String getProductId() {
		return this.productId;
	}
	/**
	 * 融数机构代码
	 * @param rootInstCd
	 */
	public void setRootInstCd(java.lang.String rootInstCd) {
		this.rootInstCd = StringUtil.isEmpty(rootInstCd) ? null : rootInstCd;
	}
	
	/**
	 * 融数机构代码
	 * @return
	 */
	public java.lang.String getRootInstCd() {
		return this.rootInstCd;
	}
	/**
	 * 学生ID
	 * @param userId
	 */
	public void setUserId(java.lang.String userId) {
		this.userId = userId;
	}
	
	/**
	 * 学生ID
	 * @return
	 */
	public java.lang.String getUserId() {
		return this.userId;
	}
	/**
	 * 保理ID
	 * @param factorId
	 */
	public void setFactorId(java.lang.String factorId) {
		this.factorId = factorId;
	}
	
	/**
	 * 保理ID
	 * @return
	 */
	public java.lang.String getFactorId() {
		return this.factorId;
	}
	/**
	 * 培训机构ID
	 * @param outfitId
	 */
	public void setOutfitId(java.lang.String outfitId) {
		this.outfitId = outfitId;
	}
	
	/**
	 * 培训机构ID
	 * @return
	 */
	public java.lang.String getOutfitId() {
		return this.outfitId;
	}
	/**
	 * 代理商ID
	 * @param agentId
	 */
	public void setAgentId(java.lang.String agentId) {
		this.agentId = agentId;
	}
	
	/**
	 * 代理商ID
	 * @return
	 */
	public java.lang.String getAgentId() {
		return this.agentId;
	}
	/**
	 * 利息承担方ID
	 * @param intrestPartyId
	 */
	public void setIntrestPartyId(java.lang.String intrestPartyId) {
		this.intrestPartyId = intrestPartyId;
	}
	
	/**
	 * 利息承担方ID
	 * @return
	 */
	public java.lang.String getIntrestPartyId() {
		return this.intrestPartyId;
	}
	/**
	 * 利息承担方ID2
	 * @param intrestPartyId2
	 */
	public void setIntrestPartyId2(java.lang.String intrestPartyId2) {
		this.intrestPartyId2 = intrestPartyId2;
	}
	
	/**
	 * 利息承担方ID2
	 * @return
	 */
	public java.lang.String getIntrestPartyId2() {
		return this.intrestPartyId2;
	}
	/**
	 * 利息承担方ID3
	 * @param intrestPartyId3
	 */
	public void setIntrestPartyId3(java.lang.String intrestPartyId3) {
		this.intrestPartyId3 = intrestPartyId3;
	}
	
	/**
	 * 利息承担方ID3
	 * @return
	 */
	public java.lang.String getIntrestPartyId3() {
		return this.intrestPartyId3;
	}
	/**
	 * 费率模板ID
	 * @param rateId
	 */
	public void setRateId(java.lang.String rateId) {
		this.rateId = rateId;
	}
	
	/**
	 * 费率模板ID
	 * @return
	 */
	public java.lang.String getRateId() {
		return this.rateId;
	}
	/**
	 * 贷款订单号
	 * @param loanOrderNo
	 */
	public void setLoanOrderNo(java.lang.String loanOrderNo) {
		this.loanOrderNo = loanOrderNo;
	}
	
	/**
	 * 贷款订单号
	 * @return
	 */
	public java.lang.String getLoanOrderNo() {
		return this.loanOrderNo;
	}
	/**
	 * 此还款是否为首付(是:1,否:0)
	 * @param onePaymentFlg
	 */
	public void setOnePaymentFlg(Integer onePaymentFlg) {
		this.onePaymentFlg = onePaymentFlg;
	}
	
	/**
	 * 此还款是否为首付(是:1,否:0)
	 * @return
	 */
	public Integer getOnePaymentFlg() {
		return this.onePaymentFlg;
	}
	/**
	 * 还款期数(初始为0)
	 * @param repaymentCnt
	 */
	public void setRepaymentCnt(java.lang.String repaymentCnt) {
		this.repaymentCnt = repaymentCnt;
	}
	
	/**
	 * 还款期数(初始为0)
	 * @return
	 */
	public java.lang.String getRepaymentCnt() {
		return this.repaymentCnt;
	}
	/**
	 * 本期还款结果(成功:1,失败:0,无结果:NULL)
	 * @param repaymentResult
	 */
	public void setRepaymentResult(java.lang.String repaymentResult) {
		this.repaymentResult = repaymentResult;
	}
	
	/**
	 * 本期还款结果(成功:1,失败:0,无结果:NULL)
	 * @return
	 */
	public java.lang.String getRepaymentResult() {
		return this.repaymentResult;
	}
	/**
	 * 还款时间(还款结果为成功:还款交易时间,还款结果为失败:每月19号)
	 * @param repaymentDate
	 */
	public void setRepaymentDate(java.util.Date repaymentDate) {
		this.repaymentDate = repaymentDate;
	}
	
	/**
	 * 还款时间(还款结果为成功:还款交易时间,还款结果为失败:每月19号)
	 * @return
	 */
	public java.util.Date getRepaymentDate() {
		return this.repaymentDate;
	}
	/**
	 * 逾期标识(逾期:1,未逾期0)
	 * @param overdueFlg
	 */
	public void setOverdueFlg(java.lang.String overdueFlg) {
		this.overdueFlg = overdueFlg;
	}
	
	/**
	 * 逾期标识(逾期:1,未逾期0)
	 * @return
	 */
	public java.lang.String getOverdueFlg() {
		return this.overdueFlg;
	}
	/**
	 * 提前还款标识(提前还款:1,其他:0)
	 * @param prepaymentFlg
	 */
	public void setPrepaymentFlg(java.lang.String prepaymentFlg) {
		this.prepaymentFlg = prepaymentFlg;
	}
	
	/**
	 * 提前还款标识(提前还款:1,其他:0)
	 * @return
	 */
	public java.lang.String getPrepaymentFlg() {
		return this.prepaymentFlg;
	}
	/**
	 * 退课标识(退课:1,其他:0)
	 * @param dropClassFlg
	 */
	public void setDropClassFlg(java.lang.String dropClassFlg) {
		this.dropClassFlg = dropClassFlg;
	}
	
	/**
	 * 退课标识(退课:1,其他:0)
	 * @return
	 */
	public java.lang.String getDropClassFlg() {
		return this.dropClassFlg;
	}
	/**
	 * 日期标识
	 * @param dateFlg
	 */
	public void setDateFlg(java.lang.String dateFlg) {
		this.dateFlg = dateFlg;
	}
	
	/**
	 * 日期标识
	 * @return
	 */
	public java.lang.String getDateFlg() {
		return this.dateFlg;
	}
	/**
	 * 还款金额
	 * @param amount
	 */
	public void setAmount(java.lang.Long amount) {
		this.amount = amount;
	}
	
	/**
	 * 还款金额
	 * @return
	 */
	public java.lang.Long getAmount() {
		return this.amount;
	}
	/**
	 * 应还本金(分)
	 * @param shouldCapital
	 */
	public void setShouldCapital(java.lang.Long shouldCapital) {
		this.shouldCapital = shouldCapital;
	}
	
	/**
	 * 应还本金(分)
	 * @return
	 */
	public java.lang.Long getShouldCapital() {
		return this.shouldCapital;
	}
	/**
	 * 应还利息(分)
	 * @param shouldInterest
	 */
	public void setShouldInterest(java.lang.Long shouldInterest) {
		this.shouldInterest = shouldInterest;
	}
	
	/**
	 * 应还利息(分)
	 * @return
	 */
	public java.lang.Long getShouldInterest() {
		return this.shouldInterest;
	}
	/**
	 * 逾期罚金(分)
	 * @param overdueFine
	 */
	public void setOverdueFine(java.lang.Long overdueFine) {
		this.overdueFine = overdueFine;
	}
	
	/**
	 * 逾期罚金(分)
	 * @return
	 */
	public java.lang.Long getOverdueFine() {
		return this.overdueFine;
	}
	/**
	 * 逾期利息(分)
	 * @param overdueInterest
	 */
	public void setOverdueInterest(java.lang.Long overdueInterest) {
		this.overdueInterest = overdueInterest;
	}
	
	/**
	 * 逾期利息(分)
	 * @return
	 */
	public java.lang.Long getOverdueInterest() {
		return this.overdueInterest;
	}
	/**
	 * 特殊标识1(初始为0)
	 * @param specialFlg1
	 */
	public void setSpecialFlg1(java.lang.String specialFlg1) {
		this.specialFlg1 = specialFlg1;
	}
	
	/**
	 * 特殊标识1(初始为0)
	 * @return
	 */
	public java.lang.String getSpecialFlg1() {
		return this.specialFlg1;
	}
	/**
	 * 特殊标识2(初始为0)
	 * @param specialFlg2
	 */
	public void setSpecialFlg2(java.lang.String specialFlg2) {
		this.specialFlg2 = specialFlg2;
	}
	
	/**
	 * 特殊标识2(初始为0)
	 * @return
	 */
	public java.lang.String getSpecialFlg2() {
		return this.specialFlg2;
	}
	/**
	 * 特殊标识3(初始为0)
	 * @param specialFlg3
	 */
	public void setSpecialFlg3(java.lang.String specialFlg3) {
		this.specialFlg3 = specialFlg3;
	}
	
	/**
	 * 特殊标识3(初始为0)
	 * @return
	 */
	public java.lang.String getSpecialFlg3() {
		return this.specialFlg3;
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
	 * 预留4
	 * @param obligate4
	 */
	public void setObligate4(java.lang.String obligate4) {
		this.obligate4 = obligate4;
	}
	
	/**
	 * 预留4
	 * @return
	 */
	public java.lang.String getObligate4() {
		return this.obligate4;
	}
	/**
	 * 预留5
	 * @param obligate5
	 */
	public void setObligate5(java.lang.String obligate5) {
		this.obligate5 = obligate5;
	}
	
	/**
	 * 预留5
	 * @return
	 */
	public java.lang.String getObligate5() {
		return this.obligate5;
	}
	/**
	 * 预留6
	 * @param obligate6
	 */
	public void setObligate6(java.lang.String obligate6) {
		this.obligate6 = obligate6;
	}
	
	/**
	 * 预留6
	 * @return
	 */
	public java.lang.String getObligate6() {
		return this.obligate6;
	}
	/**
	 * 预留7
	 * @param obligate7
	 */
	public void setObligate7(java.lang.String obligate7) {
		this.obligate7 = obligate7;
	}
	
	/**
	 * 预留7
	 * @return
	 */
	public java.lang.String getObligate7() {
		return this.obligate7;
	}
	/**
	 * 预留8
	 * @param obligate8
	 */
	public void setObligate8(java.lang.String obligate8) {
		this.obligate8 = obligate8;
	}
	
	/**
	 * 预留8
	 * @return
	 */
	public java.lang.String getObligate8() {
		return this.obligate8;
	}
	/**
	 * 预留9
	 * @param obligate9
	 */
	public void setObligate9(java.lang.String obligate9) {
		this.obligate9 = obligate9;
	}
	
	/**
	 * 预留9
	 * @return
	 */
	public java.lang.String getObligate9() {
		return this.obligate9;
	}
	/**
	 * 预留10
	 * @param obligate10
	 */
	public void setObligate10(java.lang.String obligate10) {
		this.obligate10 = obligate10;
	}
	
	/**
	 * 预留10
	 * @return
	 */
	public java.lang.String getObligate10() {
		return this.obligate10;
	}
	/**
	 * 预留11
	 * @param obligate11
	 */
	public void setObligate11(java.lang.String obligate11) {
		this.obligate11 = obligate11;
	}
	
	/**
	 * 预留11
	 * @return
	 */
	public java.lang.String getObligate11() {
		return this.obligate11;
	}
	/**
	 * 预留12
	 * @param obligate12
	 */
	public void setObligate12(java.lang.String obligate12) {
		this.obligate12 = obligate12;
	}
	
	/**
	 * 预留12
	 * @return
	 */
	public java.lang.String getObligate12() {
		return this.obligate12;
	}
	/**
	 * 清算用KEY
	 * @param settleFlg
	 */
	public void setSettleFlg(java.lang.String settleFlg) {
		this.settleFlg = settleFlg;
	}
	
	/**
	 * 清算用KEY
	 * @return
	 */
	public java.lang.String getSettleFlg() {
		return this.settleFlg;
	}
	/**
	 * 状态初始为1,清分成功为11,清分失败为12
	 * @param statusId
	 */
	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}
	
	/**
	 * 状态初始为1,清分成功为11,清分失败为12
	 * @return
	 */
	public Integer getStatusId() {
		return this.statusId;
	}
	/**
	 * 失败原因
	 * @param errorMsg
	 */
	public void setErrorMsg(java.lang.String errorMsg) {
		this.errorMsg = errorMsg;
	}
	
	/**
	 * 失败原因
	 * @return
	 */
	public java.lang.String getErrorMsg() {
		return this.errorMsg;
	}
	/**
	 * 接口返回信息
	 * @param returnMsg
	 */
	public void setReturnMsg(java.lang.String returnMsg) {
		this.returnMsg = returnMsg;
	}
	
	/**
	 * 接口返回信息
	 * @return
	 */
	public java.lang.String getReturnMsg() {
		return this.returnMsg;
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