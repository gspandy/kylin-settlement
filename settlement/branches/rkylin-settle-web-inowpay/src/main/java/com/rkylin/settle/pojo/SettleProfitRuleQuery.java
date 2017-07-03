/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.settle.pojo;

import java.io.Serializable;

import com.rop.utils.StringUtils;

/**
 * SettleProfitRuleQuery
 * @author code-generator
 *
 */
public class SettleProfitRuleQuery implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private java.lang.String profitDetailId;
	private java.lang.String subId;
	private Integer profitObject;
	private java.lang.String rootInstCd;
	private java.lang.String productId;
	private java.lang.String userId;
	private Integer profitType;
	private Integer isMust;
	private Integer profitMd;
	private java.lang.String profitFee;
	private java.lang.Long feilvUp;
	private java.lang.Long feilvBelow;
	private java.util.Date startTime;
	private java.util.Date endTime;
	private java.lang.String obligate1;
	private java.lang.String obligate2;
	private Integer statusId;
	private java.lang.String remark;
	private java.util.Date createdTime;
	private java.util.Date updatedTime;
	
	// 分页
    private java.lang.String orderBy;
    private Integer offset;
    private java.lang.String limit;
    private Integer pageIndex;
    private Integer pageSize;
    //生效时间，分页查询页面传递
    private java.lang.String startTimeStr;
    //失效时间，分页查询页面传递
    private java.lang.String endTimeStr;

	/**
	 * 规则明细ID
	 * @param profitDetailId
	 */
	public void setProfitDetailId(java.lang.String profitDetailId) {
		this.profitDetailId = StringUtils.isEmpty(profitDetailId)?null:profitDetailId;
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
		this.subId = StringUtils.isEmpty(subId)?null:subId;
	}
	
	/**
	 * 子ID
	 * @return
	 */
	public java.lang.String getSubId() {
		return this.subId;
	}
	/**
	 * 分润对象形态1固定,2可变
	 * @param profitObject
	 */
	public void setProfitObject(Integer profitObject) {
		this.profitObject = profitObject==-1?null:profitObject;
	}
	
	/**
	 * 分润对象形态1固定,2可变
	 * @return
	 */
	public Integer getProfitObject() {
		return this.profitObject;
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
	 * 分润对象
	 * @param userId
	 */
	public void setUserId(java.lang.String userId) {
		this.userId = StringUtils.isEmpty(userId)?null:userId;
	}
	
	/**
	 * 分润对象
	 * @return
	 */
	public java.lang.String getUserId() {
		return this.userId;
	}
	/**
	 * 清分类型:0分润，1代收，2代付等
	 * @param profitType
	 */
	public void setProfitType(Integer profitType) {
		this.profitType = profitType==-1?null:profitType;
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
		this.isMust = isMust==-1?null:isMust;
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
		this.profitMd = profitMd==-1?null:profitMd;
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
		this.feilvUp = feilvUp==0?null:feilvUp;
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
		this.feilvBelow = feilvBelow==0?null:feilvBelow;
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
		this.statusId = statusId==-1?null:statusId;
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

    public java.lang.String getStartTimeStr() {
        return startTimeStr;
    }

    public void setStartTimeStr(java.lang.String startTimeStr) {
        this.startTimeStr = StringUtils.isEmpty(startTimeStr)?null:startTimeStr;
    }

    public java.lang.String getEndTimeStr() {
        return endTimeStr;
    }

    public void setEndTimeStr(java.lang.String endTimeStr) {
        this.endTimeStr = StringUtils.isEmpty(endTimeStr)?null:endTimeStr;
    }
	
}