/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.settle.pojo;

import java.io.Serializable;

import com.rop.utils.StringUtils;

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
	private java.lang.String roleCode;
	private java.lang.String userIpAddress;
	private java.lang.String obligate1;
	private java.lang.String obligate2;
	private Integer statusId;
	private java.lang.String remark;
	private java.util.Date accountDate;
	private java.util.Date createdTime;
	private java.util.Date updatedTime;
	
	// 分页
    private java.lang.String orderBy;
    private Integer offset;
    private java.lang.String limit;
    private Integer pageIndex;
    private Integer pageSize;
    
    //记账日期开始
    private java.lang.String accountDateBegin;
    //记账日期结束
    private java.lang.String accountDateEnd;

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
		this.orderNo = StringUtils.isEmpty(orderNo)?null:orderNo;
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
	 * 管理机构代码
	 * @param rootInstCd
	 */
	public void setRootInstCd(java.lang.String rootInstCd) {
		this.rootInstCd = StringUtils.isEmpty(rootInstCd)?null:rootInstCd;
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
		this.productId = StringUtils.isEmpty(productId)?null:productId;
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
		this.profitType = (-1==profitType)?null:profitType;
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
	
	public java.lang.String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(java.lang.String roleCode) {
		this.roleCode = roleCode;
	}

	public java.lang.String getUserIpAddress() {
		return userIpAddress;
	}

	public void setUserIpAddress(java.lang.String userIpAddress) {
		this.userIpAddress = userIpAddress;
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
	 * 状态,0未进行,1已进行,2不处理
	 * @param statusId
	 */
	public void setStatusId(Integer statusId) {
		this.statusId = (-1==statusId)?null:statusId;
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

    public java.lang.String getAccountDateBegin() {
        return accountDateBegin;
    }

    public void setAccountDateBegin(java.lang.String accountDateBegin) {
        this.accountDateBegin = StringUtils.isEmpty(accountDateBegin)?null:accountDateBegin;
    }

    public java.lang.String getAccountDateEnd() {
        return accountDateEnd;
    }

    public void setAccountDateEnd(java.lang.String accountDateEnd) {
        this.accountDateEnd = StringUtils.isEmpty(accountDateEnd)?null:accountDateEnd;
    }
	
}