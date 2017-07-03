/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.settle.pojo;

import java.io.Serializable;

import com.rop.utils.StringUtils;

/**
 * SettleTransAccountQuery
 * @author code-generator
 *
 */
public class SettleTransAccountQuery implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private java.lang.Integer transAccountId;
	private java.lang.String payChannelId;
	private java.lang.String readType;
	private java.lang.String userId;
	private java.lang.String interMerchantCode;
	private java.lang.String merchantCode;
	private java.util.Date requestTime;
	private java.lang.String orderNo;
	private java.lang.String transFlowNo;
	private java.lang.String transType;
	private java.lang.Long transAmount;
	private java.lang.Long feeAmount;
	private java.lang.Long settleAmount;
	private java.lang.Long originalAmount;
	private java.lang.String currency;
	private java.util.Date settleTime;
	private Integer fileType;
	private java.lang.String batchNo;
	private Integer isInvoice;
	private java.lang.String obligate1;
	private java.lang.String obligate2;
	private Integer statusId;
	private Integer readStatusId;
	private java.lang.String remark;
	private java.util.Date createdTime;
	private java.util.Date updatedTime;
	// 分页
    private java.lang.String orderBy;
    private Integer offset;
    private java.lang.String limit;
    private Integer pageIndex;
    private Integer pageSize;
    
    //交易请求时间开始
    private java.lang.String requestDateStrBegin;
    //交易请求时间结束
    private java.lang.String requestDateStrEnd;

	/**
	 * 交易id
	 * @param transAccountId
	 */
	public void setTransAccountId(java.lang.Integer transAccountId) {
		this.transAccountId = transAccountId;
	}
	
	/**
	 * 交易id
	 * @return
	 */
	public java.lang.Integer getTransAccountId() {
		return this.transAccountId;
	}
	/**
	 * 支付渠道ID
	 * @param payChannelId
	 */
	public void setPayChannelId(java.lang.String payChannelId) {
		this.payChannelId = StringUtils.isEmpty(payChannelId)||"-1".equals(payChannelId)?null:payChannelId;
	}
	/**
	 * 支付渠道ID
	 * @return
	 */
	public java.lang.String getPayChannelId() {
		return this.payChannelId;
	}
	/**
	 * 读入交易类型 网关支付:01, 代收付:02,连连移动快捷支付04
	 * @param readType
	 */
	public void setReadType(java.lang.String readType) {
		this.readType = StringUtils.isEmpty(readType)||"-1".equals(readType)?null:readType;
	}
	
	/**
	 * 读入交易类型 网关支付:01, 代收付:02,连连移动快捷支付04
	 * @return
	 */
	public java.lang.String getReadType() {
		return this.readType;
	}
	/**
	 * 用户ID
	 * @param userId
	 */
	public void setUserId(java.lang.String userId) {
		this.userId = StringUtils.isEmpty(userId)?null:userId;
	}
	
	/**
	 * 用户ID
	 * @return
	 */
	public java.lang.String getUserId() {
		return this.userId;
	}
	/**
	 * 中间商户编码
	 * @param interMerchantCode
	 */
	public void setInterMerchantCode(java.lang.String interMerchantCode) {
		this.interMerchantCode = interMerchantCode;
	}
	
	/**
	 * 中间商户编码
	 * @return
	 */
	public java.lang.String getInterMerchantCode() {
		return this.interMerchantCode;
	}
	/**
	 * 商户编码
	 * @param merchantCode
	 */
	public void setMerchantCode(java.lang.String merchantCode) {
		this.merchantCode = StringUtils.isEmpty(merchantCode)?null:merchantCode;
	}
	
	/**
	 * 商户编码
	 * @return
	 */
	public java.lang.String getMerchantCode() {
		return this.merchantCode;
	}
	/**
	 * 交易请求时间
	 * @param requestTime
	 */
	public void setRequestTime(java.util.Date requestTime) {
		this.requestTime = requestTime;
	}
	
	/**
	 * 交易请求时间
	 * @return
	 */
	public java.util.Date getRequestTime() {
		return this.requestTime;
	}
	/**
	 * 交易/结算单订单号
	 * @param orderNo
	 */
	public void setOrderNo(java.lang.String orderNo) {
	    this.orderNo = orderNo == null || orderNo.isEmpty() ? null : orderNo;
	}
	
	/**
	 * 交易/结算单订单号
	 * @return
	 */
	public java.lang.String getOrderNo() {
		return this.orderNo;
	}
	/**
	 * 统一交易流水号
	 * @param transFlowNo
	 */
	public void setTransFlowNo(java.lang.String transFlowNo) {
		this.transFlowNo = transFlowNo;
	}
	
	/**
	 * 统一交易流水号
	 * @return
	 */
	public java.lang.String getTransFlowNo() {
		return this.transFlowNo;
	}
	/**
	 * 交易类型
	 * @param transType
	 */
	public void setTransType(java.lang.String transType) {
		this.transType = transType;
	}
	
	/**
	 * 交易类型
	 * @return
	 */
	public java.lang.String getTransType() {
		return this.transType;
	}
	/**
	 * 交易金额
	 * @param transAmount
	 */
	public void setTransAmount(java.lang.Long transAmount) {
		this.transAmount = transAmount;
	}
	
	/**
	 * 交易金额
	 * @return
	 */
	public java.lang.Long getTransAmount() {
		return this.transAmount;
	}
	/**
	 * 手续费金额
	 * @param feeAmount
	 */
	public void setFeeAmount(java.lang.Long feeAmount) {
		this.feeAmount = feeAmount;
	}
	
	/**
	 * 手续费金额
	 * @return
	 */
	public java.lang.Long getFeeAmount() {
		return this.feeAmount;
	}
	/**
	 * 清算金额
	 * @param settleAmount
	 */
	public void setSettleAmount(java.lang.Long settleAmount) {
		this.settleAmount = settleAmount;
	}
	
	/**
	 * 清算金额
	 * @return
	 */
	public java.lang.Long getSettleAmount() {
		return this.settleAmount;
	}
	/**
	 * 原始金额
	 * @param originalAmount
	 */
	public void setOriginalAmount(java.lang.Long originalAmount) {
		this.originalAmount = originalAmount;
	}
	
	/**
	 * 原始金额
	 * @return
	 */
	public java.lang.Long getOriginalAmount() {
		return this.originalAmount;
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
	 * 结算日期
	 * @param settleTime
	 */
	public void setSettleTime(java.util.Date settleTime) {
		this.settleTime = settleTime;
	}
	
	/**
	 * 结算日期
	 * @return
	 */
	public java.util.Date getSettleTime() {
		return this.settleTime;
	}
	/**
	 * 文件类型(充值,代收,代付)
	 * @param fileType
	 */
	public void setFileType(Integer fileType) {
		this.fileType = fileType;
	}
	
	/**
	 * 文件类型(充值,代收,代付)
	 * @return
	 */
	public Integer getFileType() {
		return this.fileType;
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
	 * 0交易,1结算单
	 * @param isInvoice
	 */
	public void setIsInvoice(Integer isInvoice) {
		this.isInvoice = isInvoice;
	}
	
	/**
	 * 0交易,1结算单
	 * @return
	 */
	public Integer getIsInvoice() {
		return this.isInvoice;
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
	    this.statusId = statusId < 0 ? null : statusId;
	}
	
	/**
	 * 状态
	 * @return
	 */
	public Integer getStatusId() {
		return this.statusId;
	}
	/**
     * 读入交易状态
     * @param readStatusId
     */
    public void setReadStatusId(Integer readStatusId) {
        this.readStatusId = readStatusId;
    }
    
    /**
     * 读入交易状态
     * @return
     */
    public Integer getReadStatusId() {
        return this.readStatusId;
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

    public java.lang.String getRequestDateStrBegin() {
        return requestDateStrBegin;
    }

    public void setRequestDateStrBegin(java.lang.String requestDateStrBegin) {
        this.requestDateStrBegin = StringUtils.isEmpty(requestDateStrBegin)?null:requestDateStrBegin;
    }

    public java.lang.String getRequestDateStrEnd() {
        return requestDateStrEnd;
    }

    public void setRequestDateStrEnd(java.lang.String requestDateStrEnd) {
        this.requestDateStrEnd = StringUtils.isEmpty(requestDateStrEnd)?null:requestDateStrEnd;
    }

}