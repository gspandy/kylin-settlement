/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2017
 */

package com.rkylin.settle.pojo;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.allinpay.ets.client.StringUtil;
import com.rkylin.settle.util.StringUtils;

/**
 * SettlewebAccountsumCompareQuery
 * @author code-generator
 *
 */
public class SettlewebAccountsumCompareQuery implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private java.lang.String payChannelId;
	private java.lang.String merchantCode;
	private java.lang.String rootInstCd;
	private java.lang.String readType;
	private java.lang.String accountType;
	private java.lang.String count1;
	private java.lang.String amount1;
	private java.lang.String count2;
	private java.lang.String amount2;
	private java.lang.String count3;
	private java.lang.String amount3;
	private java.lang.String count4;
	private java.lang.String amount4;
	private java.lang.String requestNo;
	private java.lang.String feeAmount;
	private java.lang.String feeAmount1;
	private java.lang.String feeAmount2;
	private java.lang.String feeAmount3;
	private java.lang.String accountResult;
	private java.lang.String remark;
	private java.lang.String obligate1;
	private java.lang.String obligate2;
	private java.lang.String obligate3;
	private Integer statusId;
	private String accountDateStr;
	private java.util.Date accountDate;
	private java.util.Date createdTime;
	private java.util.Date updatedTime;
	//条件查询
	private String accountDateStrBegin;
	private String accountDateStrEnd;
	// 分页
	private java.lang.String orderBy;
	private Integer offset;
	private java.lang.String limit;
	private Integer pageIndex;
	private Integer pageSize;
	/**
	 * 支付渠道ID
	 * @param payChannelId
	 */
	public void setPayChannelId(java.lang.String payChannelId) {
		this.payChannelId = payChannelId;
	}
	
	/**
	 * 支付渠道ID
	 * @return
	 */
	public java.lang.String getPayChannelId() {
		return StringUtil.isEmpty(this.payChannelId) ? null : this.payChannelId;
	}
	/**
	 * 协议
	 * @param merchantCode
	 */
	public void setMerchantCode(java.lang.String merchantCode) {
		this.merchantCode = merchantCode;
	}
	
	/**
	 * 协议
	 * @return
	 */
	public java.lang.String getMerchantCode() {
		return StringUtil.isEmpty(this.merchantCode) ? null : this.merchantCode;
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
	 * 读入交易类型
	 * @param readType
	 */
	public void setReadType(java.lang.String readType) {
		this.readType = readType;
	}
	
	/**
	 * 读入交易类型
	 * @return
	 */
	public java.lang.String getReadType() {
		return this.readType;
	}
	/**
	 * 订单类型0充值对账
	 * @param accountType
	 */
	public void setAccountType(java.lang.String accountType) {
		this.accountType = accountType;
	}
	
	/**
	 * 订单类型0充值对账
	 * @return
	 */
	public java.lang.String getAccountType() {
		return this.accountType;
	}
	/**
	 * 退款笔数
	 * @param count1
	 */
	public void setCount1(java.lang.String count1) {
		this.count1 = count1;
	}
	
	/**
	 * 退款笔数
	 * @return
	 */
	public java.lang.String getCount1() {
		return this.count1;
	}
	/**
	 * 退款金额
	 * @param amount1
	 */
	public void setAmount1(java.lang.String amount1) {
		this.amount1 = amount1;
	}
	
	/**
	 * 退款金额
	 * @return
	 */
	public java.lang.String getAmount1() {
		return this.amount1;
	}
	/**
	 * 支付笔数
	 * @param count2
	 */
	public void setCount2(java.lang.String count2) {
		this.count2 = count2;
	}
	
	/**
	 * 支付笔数
	 * @return
	 */
	public java.lang.String getCount2() {
		return this.count2;
	}
	/**
	 * 支付金额
	 * @param amount2
	 */
	public void setAmount2(java.lang.String amount2) {
		this.amount2 = amount2;
	}
	
	/**
	 * 支付金额
	 * @return
	 */
	public java.lang.String getAmount2() {
		return this.amount2;
	}
	/**
	 * 备用笔数
	 * @param count3
	 */
	public void setCount3(java.lang.String count3) {
		this.count3 = count3;
	}
	
	/**
	 * 备用笔数
	 * @return
	 */
	public java.lang.String getCount3() {
		return this.count3;
	}
	/**
	 * 结算金额
	 * @param amount3
	 */
	public void setAmount3(java.lang.String amount3) {
		this.amount3 = amount3;
	}
	
	/**
	 * 结算金额
	 * @return
	 */
	public java.lang.String getAmount3() {
		return this.amount3;
	}
	/**
	 * 备用笔数
	 * @param count4
	 */
	public void setCount4(java.lang.String count4) {
		this.count4 = count4;
	}
	
	/**
	 * 备用笔数
	 * @return
	 */
	public java.lang.String getCount4() {
		return this.count4;
	}
	/**
	 * 差额
	 * @param amount4
	 */
	public void setAmount4(java.lang.String amount4) {
		this.amount4 = amount4;
	}
	
	/**
	 * 差额
	 * @return
	 */
	public java.lang.String getAmount4() {
		return this.amount4;
	}
	/**
	 * 支付订单号
	 * @param requestNo
	 */
	public void setRequestNo(java.lang.String requestNo) {
		this.requestNo = requestNo;
	}
	
	/**
	 * 支付订单号
	 * @return
	 */
	public java.lang.String getRequestNo() {
		return this.requestNo;
	}
	/**
	 * 手续费
	 * @param feeAmount
	 */
	public void setFeeAmount(java.lang.String feeAmount) {
		this.feeAmount = feeAmount;
	}
	
	/**
	 * 手续费
	 * @return
	 */
	public java.lang.String getFeeAmount() {
		return this.feeAmount;
	}
	/**
	 * 手续费1
	 * @param feeAmount1
	 */
	public void setFeeAmount1(java.lang.String feeAmount1) {
		this.feeAmount1 = feeAmount1;
	}
	
	/**
	 * 手续费1
	 * @return
	 */
	public java.lang.String getFeeAmount1() {
		return this.feeAmount1;
	}
	/**
	 * 手续费2
	 * @param feeAmount2
	 */
	public void setFeeAmount2(java.lang.String feeAmount2) {
		this.feeAmount2 = feeAmount2;
	}
	
	/**
	 * 手续费2
	 * @return
	 */
	public java.lang.String getFeeAmount2() {
		return this.feeAmount2;
	}
	/**
	 * 手续费3
	 * @param feeAmount3
	 */
	public void setFeeAmount3(java.lang.String feeAmount3) {
		this.feeAmount3 = feeAmount3;
	}
	
	/**
	 * 手续费3
	 * @return
	 */
	public java.lang.String getFeeAmount3() {
		return this.feeAmount3;
	}
	/**
	 * 对账结果
	 * @param accountResult
	 */
	public void setAccountResult(java.lang.String accountResult) {
		this.accountResult = accountResult;
	}
	
	/**
	 * 对账结果
	 * @return
	 */
	public java.lang.String getAccountResult() {
		return this.accountResult;
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
		this.accountDateStr = accountDateStr;
		
		if(StringUtils.isEmpty(accountDateStr)) {
			return;
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		try {
			this.accountDate = sdf.parse(accountDateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public String getAccountDateStrBegin() {
		return accountDateStrBegin;
	}

	public void setAccountDateStrBegin(String accountDateStrBegin) {
		this.accountDateStrBegin = StringUtil.isEmpty(accountDateStrBegin) ? null : accountDateStrBegin;
	}

	public String getAccountDateStrEnd() {
		return accountDateStrEnd;
	}

	public void setAccountDateStrEnd(String accountDateStrEnd) {
		this.accountDateStrEnd = StringUtil.isEmpty(accountDateStrEnd) ? null : accountDateStrEnd;
	}
}