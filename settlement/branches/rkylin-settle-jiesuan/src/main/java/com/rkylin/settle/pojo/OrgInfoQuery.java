/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2016
 */

package com.rkylin.settle.pojo;

import java.io.Serializable;

/**
 * OrgInfoQuery
 * @author code-generator
 *
 */
public class OrgInfoQuery implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private java.lang.Integer orgId;
	private java.lang.String orgNo;
	private java.lang.String orgCode;
	private java.lang.String orgNum;
	private java.lang.String orgRemark;
	private java.lang.String channelHome;
	private java.lang.String privateKey;
	private java.lang.String publicKey;
	private java.lang.String channelPkey;
	private java.lang.String channelWsdl;
	private java.lang.String channelUrl1;
	private java.lang.String channelUrl2;
	private java.lang.String notifyType;
	private java.lang.String notifyUrl1;
	private java.lang.String notifyUrl2;
	private java.lang.String username;
	private java.lang.String password;
	private java.lang.String dataFormat;
	private java.lang.Integer timeout;
	private java.lang.String version;
	private java.lang.String charset;
	private java.lang.String language;
	private java.lang.String signType;
	private java.lang.String accountNo;
	private Integer accountType;
	private java.lang.String accountName;
	private java.lang.String bankNo;
	private java.lang.String bankName;
	private java.lang.String districtCode;
	private java.lang.String province;
	private java.lang.String city;
	private java.lang.String expand1;
	private java.lang.String expand2;
	private java.lang.String expand3;
	private java.lang.String expand4;
	private Integer statusId;
	private java.util.Date createdTime;
	private java.util.Date updatedTime;

	/**
	 * 机构ID
	 * @param orgId
	 */
	public void setOrgId(java.lang.Integer orgId) {
		this.orgId = orgId;
	}
	
	/**
	 * 机构ID
	 * @return
	 */
	public java.lang.Integer getOrgId() {
		return this.orgId;
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
	 * 
	 * @param orgNum
	 */
	public void setOrgNum(java.lang.String orgNum) {
		this.orgNum = orgNum;
	}
	
	/**
	 * 
	 * @return
	 */
	public java.lang.String getOrgNum() {
		return this.orgNum;
	}
	/**
	 * 
	 * @param orgRemark
	 */
	public void setOrgRemark(java.lang.String orgRemark) {
		this.orgRemark = orgRemark;
	}
	
	/**
	 * 
	 * @return
	 */
	public java.lang.String getOrgRemark() {
		return this.orgRemark;
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
	 * 机构私钥
	 * @param privateKey
	 */
	public void setPrivateKey(java.lang.String privateKey) {
		this.privateKey = privateKey;
	}
	
	/**
	 * 机构私钥
	 * @return
	 */
	public java.lang.String getPrivateKey() {
		return this.privateKey;
	}
	/**
	 * 机构公钥
	 * @param publicKey
	 */
	public void setPublicKey(java.lang.String publicKey) {
		this.publicKey = publicKey;
	}
	
	/**
	 * 机构公钥
	 * @return
	 */
	public java.lang.String getPublicKey() {
		return this.publicKey;
	}
	/**
	 * 渠道公钥
	 * @param channelPkey
	 */
	public void setChannelPkey(java.lang.String channelPkey) {
		this.channelPkey = channelPkey;
	}
	
	/**
	 * 渠道公钥
	 * @return
	 */
	public java.lang.String getChannelPkey() {
		return this.channelPkey;
	}
	/**
	 * 渠道WSDL
	 * @param channelWsdl
	 */
	public void setChannelWsdl(java.lang.String channelWsdl) {
		this.channelWsdl = channelWsdl;
	}
	
	/**
	 * 渠道WSDL
	 * @return
	 */
	public java.lang.String getChannelWsdl() {
		return this.channelWsdl;
	}
	/**
	 * 渠道URL1
	 * @param channelUrl1
	 */
	public void setChannelUrl1(java.lang.String channelUrl1) {
		this.channelUrl1 = channelUrl1;
	}
	
	/**
	 * 渠道URL1
	 * @return
	 */
	public java.lang.String getChannelUrl1() {
		return this.channelUrl1;
	}
	/**
	 * 渠道URL2
	 * @param channelUrl2
	 */
	public void setChannelUrl2(java.lang.String channelUrl2) {
		this.channelUrl2 = channelUrl2;
	}
	
	/**
	 * 渠道URL2
	 * @return
	 */
	public java.lang.String getChannelUrl2() {
		return this.channelUrl2;
	}
	/**
	 * 通知类型
	 * @param notifyType
	 */
	public void setNotifyType(java.lang.String notifyType) {
		this.notifyType = notifyType;
	}
	
	/**
	 * 通知类型
	 * @return
	 */
	public java.lang.String getNotifyType() {
		return this.notifyType;
	}
	/**
	 * 通知URL1
	 * @param notifyUrl1
	 */
	public void setNotifyUrl1(java.lang.String notifyUrl1) {
		this.notifyUrl1 = notifyUrl1;
	}
	
	/**
	 * 通知URL1
	 * @return
	 */
	public java.lang.String getNotifyUrl1() {
		return this.notifyUrl1;
	}
	/**
	 * 通知URL2
	 * @param notifyUrl2
	 */
	public void setNotifyUrl2(java.lang.String notifyUrl2) {
		this.notifyUrl2 = notifyUrl2;
	}
	
	/**
	 * 通知URL2
	 * @return
	 */
	public java.lang.String getNotifyUrl2() {
		return this.notifyUrl2;
	}
	/**
	 * 用户
	 * @param username
	 */
	public void setUsername(java.lang.String username) {
		this.username = username;
	}
	
	/**
	 * 用户
	 * @return
	 */
	public java.lang.String getUsername() {
		return this.username;
	}
	/**
	 * 密码
	 * @param password
	 */
	public void setPassword(java.lang.String password) {
		this.password = password;
	}
	
	/**
	 * 密码
	 * @return
	 */
	public java.lang.String getPassword() {
		return this.password;
	}
	/**
	 * 数据格式
	 * @param dataFormat
	 */
	public void setDataFormat(java.lang.String dataFormat) {
		this.dataFormat = dataFormat;
	}
	
	/**
	 * 数据格式
	 * @return
	 */
	public java.lang.String getDataFormat() {
		return this.dataFormat;
	}
	/**
	 * 超时时间
	 * @param timeout
	 */
	public void setTimeout(java.lang.Integer timeout) {
		this.timeout = timeout;
	}
	
	/**
	 * 超时时间
	 * @return
	 */
	public java.lang.Integer getTimeout() {
		return this.timeout;
	}
	/**
	 * 版本号
	 * @param version
	 */
	public void setVersion(java.lang.String version) {
		this.version = version;
	}
	
	/**
	 * 版本号
	 * @return
	 */
	public java.lang.String getVersion() {
		return this.version;
	}
	/**
	 * 字符集
	 * @param charset
	 */
	public void setCharset(java.lang.String charset) {
		this.charset = charset;
	}
	
	/**
	 * 字符集
	 * @return
	 */
	public java.lang.String getCharset() {
		return this.charset;
	}
	/**
	 * 语言
	 * @param language
	 */
	public void setLanguage(java.lang.String language) {
		this.language = language;
	}
	
	/**
	 * 语言
	 * @return
	 */
	public java.lang.String getLanguage() {
		return this.language;
	}
	/**
	 * 签名类型
	 * @param signType
	 */
	public void setSignType(java.lang.String signType) {
		this.signType = signType;
	}
	
	/**
	 * 签名类型
	 * @return
	 */
	public java.lang.String getSignType() {
		return this.signType;
	}
	/**
	 * 账户
	 * @param accountNo
	 */
	public void setAccountNo(java.lang.String accountNo) {
		this.accountNo = accountNo;
	}
	
	/**
	 * 账户
	 * @return
	 */
	public java.lang.String getAccountNo() {
		return this.accountNo;
	}
	/**
	 * 账户类型
	 * @param accountType
	 */
	public void setAccountType(Integer accountType) {
		this.accountType = accountType;
	}
	
	/**
	 * 账户类型
	 * @return
	 */
	public Integer getAccountType() {
		return this.accountType;
	}
	/**
	 * 账户名
	 * @param accountName
	 */
	public void setAccountName(java.lang.String accountName) {
		this.accountName = accountName;
	}
	
	/**
	 * 账户名
	 * @return
	 */
	public java.lang.String getAccountName() {
		return this.accountName;
	}
	/**
	 * 银行号
	 * @param bankNo
	 */
	public void setBankNo(java.lang.String bankNo) {
		this.bankNo = bankNo;
	}
	
	/**
	 * 银行号
	 * @return
	 */
	public java.lang.String getBankNo() {
		return this.bankNo;
	}
	/**
	 * 银行名称
	 * @param bankName
	 */
	public void setBankName(java.lang.String bankName) {
		this.bankName = bankName;
	}
	
	/**
	 * 银行名称
	 * @return
	 */
	public java.lang.String getBankName() {
		return this.bankName;
	}
	/**
	 * 地区码
	 * @param districtCode
	 */
	public void setDistrictCode(java.lang.String districtCode) {
		this.districtCode = districtCode;
	}
	
	/**
	 * 地区码
	 * @return
	 */
	public java.lang.String getDistrictCode() {
		return this.districtCode;
	}
	/**
	 * 省份
	 * @param province
	 */
	public void setProvince(java.lang.String province) {
		this.province = province;
	}
	
	/**
	 * 省份
	 * @return
	 */
	public java.lang.String getProvince() {
		return this.province;
	}
	/**
	 * 城市
	 * @param city
	 */
	public void setCity(java.lang.String city) {
		this.city = city;
	}
	
	/**
	 * 城市
	 * @return
	 */
	public java.lang.String getCity() {
		return this.city;
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
	 * 扩展4
	 * @param expand4
	 */
	public void setExpand4(java.lang.String expand4) {
		this.expand4 = expand4;
	}
	
	/**
	 * 扩展4
	 * @return
	 */
	public java.lang.String getExpand4() {
		return this.expand4;
	}
	/**
	 * 状态:1可用,0停用
	 * @param statusId
	 */
	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}
	
	/**
	 * 状态:1可用,0停用
	 * @return
	 */
	public Integer getStatusId() {
		return this.statusId;
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