/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2015
 */

package com.rkylin.settle.pojo;

import java.io.Serializable;

import com.rop.utils.StringUtils;

/**
 * SettleFileQuery
 * @author code-generator
 *
 */
public class SettleFileQuery implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private java.lang.Integer fileId;
	private java.lang.String fileName;
	private java.lang.String rootInstCd;
	private java.lang.String funcCodes;
	private Integer funcRelation;
	private java.lang.String payChannelId;
	private java.lang.String readType;
	private java.lang.String transStatusIds;
	private java.lang.String filePrefix;
	private java.lang.String filePostfix;
	private java.lang.String fileType;
	private Integer fileActive;
	private Integer fileSubId;
	private java.lang.String fileEncode;
	private java.lang.String splitStr;
	private java.lang.String uploadKeyName;
	private Integer uploadKeyFlg;
	private java.lang.String ropBatchNo;
	private java.util.Date startTime;
	private java.util.Date endTime;
	private java.lang.String obligate1;
	private java.lang.String obligate2;
	private Integer statusId;
	private java.lang.String remark;
	private java.util.Date createdTime;
	private java.util.Date updatedTime;
	private Integer ropFileType;
	private Integer dateStep;
	
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
    //文件模板名称，分页查询页面传递
    private java.lang.String fileNameStr;
    //文件模板名称，分页查询页面传递
    private java.lang.String funcCodesStr;
    //文件名称前缀，分页查询页面传递
    private java.lang.String filePrefixStr;

	/**
	 * 规则ID
	 * @param fileId
	 */
	public void setFileId(java.lang.Integer fileId) {
		this.fileId = fileId;
	}
	
	/**
	 * 规则ID
	 * @return
	 */
	public java.lang.Integer getFileId() {
		return this.fileId;
	}
	/**
     * 文件模板名称
     * @return
     */
    public void setFileName(java.lang.String fileName) {
        this.fileName = StringUtils.isEmpty(fileName) ? null : fileName;
    }
	/**
     * 文件模板名称
     * @return
     */
	public java.lang.String getFileName() {
        return fileName;
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
	 * 多个'功能编码'用','隔开
	 * @param funcCodes
	 */
	public void setFuncCodes(java.lang.String funcCodes) {
		this.funcCodes = funcCodes;
	}
	
	/**
	 * 多个'功能编码'用','隔开
	 * @return
	 */
	public java.lang.String getFuncCodes() {
		return this.funcCodes;
	}
	/**
	 * 0: 全部功能码, 1:包含include, 2:差集difference
	 * @param funcRelation
	 */
	public void setFuncRelation(Integer funcRelation) {
		this.funcRelation = funcRelation==-1?null:funcRelation;
	}
	
	/**
	 * 0: 全部功能码, 1:包含include, 2:差集difference
	 * @return
	 */
	public Integer getFuncRelation() {
		return this.funcRelation;
	}
	/**
	 * 支付渠道ID
	 * @param payChannelId
	 */
	public void setPayChannelId(java.lang.String payChannelId) {
		this.payChannelId = (StringUtils.isEmpty(payChannelId)||"-1".equals(payChannelId))?null:payChannelId;
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
		this.readType = StringUtils.isEmpty(readType)||"-1".equals(readType)?null:readType;;
	}
	
	/**
	 * 读入交易类型 网关支付:01, 代收付:02,连连移动快捷支付04
	 * @return
	 */
	public java.lang.String getReadType() {
		return this.readType;
	}
	/**
	 * 写入时:[此文件显示的交易信息'状态', 多个状态用','隔开]; 读取时:[对账文件HEAD行数]
	 * @param transStatusIds
	 */
	public void setTransStatusIds(java.lang.String transStatusIds) {
		this.transStatusIds = transStatusIds;
	}
	
	/**
	 * 写入时:[此文件显示的交易信息'状态', 多个状态用','隔开]; 读取时:[对账文件HEAD行数]
	 * @return
	 */
	public java.lang.String getTransStatusIds() {
		return this.transStatusIds;
	}
	/**
	 * 文件名称前缀
	 * @param filePrefix
	 */
	public void setFilePrefix(java.lang.String filePrefix) {
		this.filePrefix = filePrefix;
	}
	
	/**
	 * 文件名称前缀
	 * @return
	 */
	public java.lang.String getFilePrefix() {
		return this.filePrefix;
	}
	/**
	 * 文件后缀txt,xlsx,pdf
	 * @param filePostfix
	 */
	public void setFilePostfix(java.lang.String filePostfix) {
		this.filePostfix = filePostfix;
	}
	
	/**
	 * 文件后缀txt,xlsx,pdf
	 * @return
	 */
	public java.lang.String getFilePostfix() {
		return this.filePostfix;
	}
	/**
	 * 文件类型head,body,foot
	 * @param fileType
	 */
	public void setFileType(java.lang.String fileType) {
		this.fileType = StringUtils.isEmpty(fileType)||"-1".equals(fileType)?null:fileType;
	}
	
	/**
	 * 文件类型head,body,foot
	 * @return
	 */
	public java.lang.String getFileType() {
		return this.fileType;
	}
	/**
	 * 文件作用1:'写入'信息文件模板,2:'读取'信息文件模板
	 * @param fileActive
	 */
	public void setFileActive(Integer fileActive) {
		this.fileActive = (fileActive==null||fileActive==-1)?null:fileActive;
	}
	
	/**
	 * 文件作用1:'写入'信息文件模板,2:'读取'信息文件模板
	 * @return
	 */
	public Integer getFileActive() {
		return this.fileActive;
	}
	/**
	 * 文件列信息 关联 此字段
	 * @param fileSubId
	 */
	public void setFileSubId(Integer fileSubId) {
		this.fileSubId = fileSubId;
	}
	
	/**
	 * 文件列信息 关联 此字段
	 * @return
	 */
	public Integer getFileSubId() {
		return this.fileSubId;
	}
	/**
	 * 文件编码
	 * @param fileEncode
	 */
	public void setFileEncode(java.lang.String fileEncode) {
		this.fileEncode = StringUtils.isEmpty(fileEncode)?null:fileEncode;
	}
	
	/**
	 * 文件编码
	 * @return
	 */
	public java.lang.String getFileEncode() {
		return this.fileEncode;
	}
	/**
	 * 信息分隔符
	 * @param splitStr
	 */
	public void setSplitStr(java.lang.String splitStr) {
		this.splitStr = StringUtils.isEmpty(splitStr)?null:splitStr;
	}
	
	/**
	 * 信息分隔符
	 * @return
	 */
	public java.lang.String getSplitStr() {
		return this.splitStr;
	}
	/**
	 * 上传文件 所用的密钥名称
	 * @param uploadKeyName
	 */
	public void setUploadKeyName(java.lang.String uploadKeyName) {
		this.uploadKeyName = uploadKeyName;
	}
	
	/**
	 * 上传文件 所用的密钥名称
	 * @return
	 */
	public java.lang.String getUploadKeyName() {
		return this.uploadKeyName;
	}
	/**
	 * 写入时:[上传文件 所用的密钥类型 私钥1 公钥0]; 读取时[是否录入数据库0:不录入,1:录入]
	 * @param uploadKeyFlg
	 */
	public void setUploadKeyFlg(Integer uploadKeyFlg) {
		this.uploadKeyFlg = uploadKeyFlg;
	}
	
	/**
	 * 写入时:[上传文件 所用的密钥类型 私钥1 公钥0]; 读取时[是否录入数据库0:不录入,1:录入]
	 * @return
	 */
	public Integer getUploadKeyFlg() {
		return this.uploadKeyFlg;
	}
	/**
	 * rop 文件批次号
	 * @param ropBatchNo
	 */
	public void setRopBatchNo(java.lang.String ropBatchNo) {
		this.ropBatchNo = ropBatchNo;
	}
	
	/**
	 * rop 文件批次号
	 * @return
	 */
	public java.lang.String getRopBatchNo() {
		return this.ropBatchNo;
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
		this.statusId = (statusId==null||-1==statusId)?null:statusId;
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
	
    public Integer getRopFileType() {
		return ropFileType;
	}

	public void setRopFileType(Integer ropFileType) {
		this.ropFileType = ropFileType;
	}

	public Integer getDateStep() {
		return dateStep;
	}

	public void setDateStep(Integer dateStep) {
		this.dateStep = dateStep;
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

    public java.lang.String getFileNameStr() {
        return fileNameStr;
    }

    public void setFileNameStr(java.lang.String fileNameStr) {
        this.fileNameStr = StringUtils.isEmpty(fileNameStr)?null:fileNameStr;
    }

    public java.lang.String getFuncCodesStr() {
        return funcCodesStr;
    }

    public void setFuncCodesStr(java.lang.String funcCodesStr) {
        this.funcCodesStr = StringUtils.isEmpty(funcCodesStr)?null:funcCodesStr;
    }

    public java.lang.String getFilePrefixStr() {
        return filePrefixStr;
    }

    public void setFilePrefixStr(java.lang.String filePrefixStr) {
        this.filePrefixStr = StringUtils.isEmpty(filePrefixStr)?null:filePrefixStr;
    }
	
}