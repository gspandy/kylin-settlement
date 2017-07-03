/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.settle.pojo;

import java.io.Serializable;

import com.rop.utils.StringUtils;

/**
 * SettleFileColumnQuery
 * @author code-generator
 *
 */
public class SettleFileColumnQuery implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private java.lang.Integer fileColumnId;
	private Integer fileSubId;
	private Integer fileColumnNo;
	private java.lang.String fileColumnTitle;
	private java.lang.String fileColumnKeyCode;
	private java.lang.String fileColumnKeyName;
	private Integer isSpecialColumn;
	private Integer dataType;
	private java.lang.String dataFormat;
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
	 * 列ID
	 * @param fileColumnId
	 */
	public void setFileColumnId(java.lang.Integer fileColumnId) {
		this.fileColumnId = fileColumnId;
	}
	
	/**
	 * 列ID
	 * @return
	 */
	public java.lang.Integer getFileColumnId() {
		return this.fileColumnId;
	}
	/**
	 * 此字段关联文件信息表
	 * @param fileSubId
	 */
	public void setFileSubId(Integer fileSubId) {
		this.fileSubId = fileSubId;
	}
	
	/**
	 * 此字段关联文件信息表
	 * @return
	 */
	public Integer getFileSubId() {
		return this.fileSubId;
	}
	/**
	 * 列号
	 * @param fileColumnNo
	 */
	public void setFileColumnNo(Integer fileColumnNo) {
		this.fileColumnNo = fileColumnNo;
	}
	
	/**
	 * 列号
	 * @return
	 */
	public Integer getFileColumnNo() {
		return this.fileColumnNo;
	}
	/**
	 * 列名称
	 * @param fileColumnTitle
	 */
	public void setFileColumnTitle(java.lang.String fileColumnTitle) {
		this.fileColumnTitle = StringUtils.isEmpty(fileColumnTitle)?null:fileColumnTitle;
	}
	
	/**
	 * 列名称
	 * @return
	 */
	public java.lang.String getFileColumnTitle() {
		return this.fileColumnTitle;
	}
	/**
	 * 列对应的交易信息字段
	 * @param fileColumnKeyCode
	 */
	public void setFileColumnKeyCode(java.lang.String fileColumnKeyCode) {
		this.fileColumnKeyCode = StringUtils.isEmpty(fileColumnKeyCode)?null:fileColumnKeyCode;
	}
	
	/**
	 * 列对应的交易信息字段
	 * @return
	 */
	public java.lang.String getFileColumnKeyCode() {
		return this.fileColumnKeyCode;
	}
	/**
	 * 列对应的交易信息字段说明
	 * @param fileColumnKeyName
	 */
	public void setFileColumnKeyName(java.lang.String fileColumnKeyName) {
		this.fileColumnKeyName = fileColumnKeyName;
	}
	
	/**
	 * 列对应的交易信息字段说明
	 * @return
	 */
	public java.lang.String getFileColumnKeyName() {
		return this.fileColumnKeyName;
	}
	/**
	 * 是否是特殊列 0:否, 1:是
	 * @param isSpecialColumn
	 */
	public void setIsSpecialColumn(Integer isSpecialColumn) {
		this.isSpecialColumn = isSpecialColumn==-1?null:isSpecialColumn;
	}
	
	/**
	 * 是否是特殊列 0:否, 1:是
	 * @return
	 */
	public Integer getIsSpecialColumn() {
		return this.isSpecialColumn;
	}
	/**
	 * 数据类型 1:字符串, 2:数字, 3:日期, 4: 特殊列
	 * @param dataType
	 */
	public void setDataType(Integer dataType) {
		this.dataType = dataType==-1?null:dataType;
	}
	
	/**
	 * 数据类型 1:字符串, 2:数字, 3:日期, 4: 特殊列
	 * @return
	 */
	public Integer getDataType() {
		return this.dataType;
	}
	/**
	 * 数据格式 例如DATA_TYPE=3 DATA_FORMAT='yyyyMMdd'
	 * @param dataFormat
	 */
	public void setDataFormat(java.lang.String dataFormat) {
		this.dataFormat = dataFormat;
	}
	
	/**
	 * 数据格式 例如DATA_TYPE=3 DATA_FORMAT='yyyyMMdd'
	 * @return
	 */
	public java.lang.String getDataFormat() {
		return this.dataFormat;
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