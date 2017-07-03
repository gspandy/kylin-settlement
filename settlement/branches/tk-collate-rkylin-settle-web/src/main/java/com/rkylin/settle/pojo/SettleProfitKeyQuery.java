/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.settle.pojo;

import java.io.Serializable;

import com.rop.utils.StringUtils;

/**
 * SettleProfitKeyQuery
 * @author code-generator
 *
 */
public class SettleProfitKeyQuery implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private java.lang.Integer profitKeyId;
    private java.lang.String profitRuleName;
    private java.lang.String rootInstCd;
    private java.lang.String funcCode;
    private java.lang.String keyName1;
    private java.lang.String keyValue1;
    private java.lang.String keyName2;
    private java.lang.String keyValue2;
    private java.lang.String keyName3;
    private java.lang.String keyValue3;
    private java.lang.String profitDetailId;
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
	 * 规则ID
	 * @param profitKeyId
	 */
	public void setProfitKeyId(java.lang.Integer profitKeyId) {
		this.profitKeyId = profitKeyId;
	}
	
	/**
	 * 规则ID
	 * @return
	 */
	public java.lang.Integer getProfitKeyId() {
		return this.profitKeyId;
	}
	/**
     * 分润规则名称
     * @param profitRuleName
     */
    public void setProfitRuleName(java.lang.String profitRuleName) {
        this.profitRuleName = profitRuleName;
    }
    
    /**
     * 分润规则名称
     * @return
     */
    public java.lang.String getProfitRuleName() {
        return this.profitRuleName;
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
	 * @param funcCode
	 */
	public void setFuncCode(java.lang.String funcCode) {
		this.funcCode = StringUtils.isEmpty(funcCode)?null:funcCode;
	}
	
	/**
	 * 产品号
	 * @return
	 */
	public java.lang.String getFuncCode() {
		return this.funcCode;
	}
	/**
	 * 规则key1名称
	 * @param keyName1
	 */
	public void setKeyName1(java.lang.String keyName1) {
		this.keyName1 = StringUtils.isEmpty(keyName1)?null:keyName1;
	}
	
	/**
	 * 规则key1名称
	 * @return
	 */
	public java.lang.String getKeyName1() {
		return this.keyName1;
	}
	/**
	 * 规则key1值
	 * @param keyValue1
	 */
	public void setKeyValue1(java.lang.String keyValue1) {
		this.keyValue1 = StringUtils.isEmpty(keyValue1)?null:keyValue1;
	}
	
	/**
	 * 规则key1值
	 * @return
	 */
	public java.lang.String getKeyValue1() {
		return this.keyValue1;
	}
	/**
	 * 规则key2名称
	 * @param keyName2
	 */
	public void setKeyName2(java.lang.String keyName2) {
		this.keyName2 = keyName2;
	}
	
	/**
	 * 规则key2名称
	 * @return
	 */
	public java.lang.String getKeyName2() {
		return this.keyName2;
	}
	/**
	 * 规则key2值
	 * @param keyValue2
	 */
	public void setKeyValue2(java.lang.String keyValue2) {
		this.keyValue2 = keyValue2;
	}
	
	/**
	 * 规则key2值
	 * @return
	 */
	public java.lang.String getKeyValue2() {
		return this.keyValue2;
	}
	/**
	 * 规则key3名称
	 * @param keyName3
	 */
	public void setKeyName3(java.lang.String keyName3) {
		this.keyName3 = keyName3;
	}
	
	/**
	 * 规则key3名称
	 * @return
	 */
	public java.lang.String getKeyName3() {
		return this.keyName3;
	}
	/**
	 * 规则key3值
	 * @param keyValue3
	 */
	public void setKeyValue3(java.lang.String keyValue3) {
		this.keyValue3 = keyValue3;
	}
	
	/**
	 * 规则key3值
	 * @return
	 */
	public java.lang.String getKeyValue3() {
		return this.keyValue3;
	}
	/**
	 * 对应规则明细
	 * @param profitDetailId
	 */
	public void setProfitDetailId(java.lang.String profitDetailId) {
		this.profitDetailId = StringUtils.isEmpty(profitDetailId)?null:profitDetailId;
	}
	
	/**
	 * 对应规则明细
	 * @return
	 */
	public java.lang.String getProfitDetailId() {
		return this.profitDetailId;
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