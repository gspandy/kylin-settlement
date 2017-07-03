/*
 * Powered By chanjetpay-code-generator
 * Web Site: http://www.chanjetpay.com
 * Since 2014 - 2016
 */

package com.rkylin.settle.pojo;

import java.io.Serializable;

/**
 * OrderInfoQuery
 * @author code-generator
 *
 */
public class OrderInfoQuery implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private java.lang.String orderId;
	private java.lang.String orderTypeId;
	private java.util.Date orderDate;
	private java.util.Date orderTime;
	private java.lang.String providerId;
	private java.lang.String productId;
	private java.lang.String rootInstCd;
	private java.lang.String userId;
	private java.lang.String userOrderId;
	private java.lang.String userRelateId;
	private java.lang.Long amount;
	private java.lang.String remark;
	private java.lang.String orderControl;
	private java.lang.String respCode;
	private java.lang.String statusId;
	private Integer checkCode;
	private java.lang.String goodsName;
	private java.lang.String goodsDetail;
	private java.lang.Integer goodsCnt;
	private java.lang.Long unitPrice;
	private java.lang.String fromStockSubjectCode;
	private java.lang.String toStockSubjectCode;
	private java.lang.Long channelCodeFk;
	private java.lang.Long orderType;
	private java.lang.Boolean lostType;
	private java.lang.Integer lostProductNum;
	private java.lang.Long lostProductAmount;
	private java.lang.Boolean paryType;
	private java.lang.Long payAmount;
	private java.lang.Long materialFlowAmount;
	private java.lang.String otherDutyOrderCode;
	private java.lang.Integer orderStatus;
	private java.lang.Integer payNum;
	private java.lang.String payOrderCode;
	private java.lang.String platformOrder;
	private java.util.Date createdTime;
	private java.util.Date updatedTime;

	/**
	 * 订单ID
	 * @param orderId
	 */
	public void setOrderId(java.lang.String orderId) {
		this.orderId = orderId;
	}
	
	/**
	 * 订单ID
	 * @return
	 */
	public java.lang.String getOrderId() {
		return this.orderId;
	}
	/**
	 * 业务类型ID
	 * @param orderTypeId
	 */
	public void setOrderTypeId(java.lang.String orderTypeId) {
		this.orderTypeId = orderTypeId;
	}
	
	/**
	 * 业务类型ID
	 * @return
	 */
	public java.lang.String getOrderTypeId() {
		return this.orderTypeId;
	}
	/**
	 * 订单日期
	 * @param orderDate
	 */
	public void setOrderDate(java.util.Date orderDate) {
		this.orderDate = orderDate;
	}
	
	/**
	 * 订单日期
	 * @return
	 */
	public java.util.Date getOrderDate() {
		return this.orderDate;
	}
	/**
	 * 订单时间
	 * @param orderTime
	 */
	public void setOrderTime(java.util.Date orderTime) {
		this.orderTime = orderTime;
	}
	
	/**
	 * 订单时间
	 * @return
	 */
	public java.util.Date getOrderTime() {
		return this.orderTime;
	}
	/**
	 * 授信提供方ID
	 * @param providerId
	 */
	public void setProviderId(java.lang.String providerId) {
		this.providerId = providerId;
	}
	
	/**
	 * 授信提供方ID
	 * @return
	 */
	public java.lang.String getProviderId() {
		return this.providerId;
	}
	/**
	 * 产品ID
	 * @param productId
	 */
	public void setProductId(java.lang.String productId) {
		this.productId = productId;
	}
	
	/**
	 * 产品ID
	 * @return
	 */
	public java.lang.String getProductId() {
		return this.productId;
	}
	/**
	 * 管理机构代码
	 * @param rootInstCd
	 */
	public void setRootInstCd(java.lang.String rootInstCd) {
		this.rootInstCd = rootInstCd;
	}
	
	/**
	 * 管理机构代码
	 * @return
	 */
	public java.lang.String getRootInstCd() {
		return this.rootInstCd;
	}
	/**
	 * 用户ID
	 * @param userId
	 */
	public void setUserId(java.lang.String userId) {
		this.userId = userId;
	}
	
	/**
	 * 用户ID
	 * @return
	 */
	public java.lang.String getUserId() {
		return this.userId;
	}
	/**
	 * 用户订单流水号
	 * @param userOrderId
	 */
	public void setUserOrderId(java.lang.String userOrderId) {
		this.userOrderId = userOrderId;
	}
	
	/**
	 * 用户订单流水号
	 * @return
	 */
	public java.lang.String getUserOrderId() {
		return this.userOrderId;
	}
	/**
	 * 关联用户ID
	 * @param userRelateId
	 */
	public void setUserRelateId(java.lang.String userRelateId) {
		this.userRelateId = userRelateId;
	}
	
	/**
	 * 关联用户ID
	 * @return
	 */
	public java.lang.String getUserRelateId() {
		return this.userRelateId;
	}
	/**
	 * 订单金额(分)
	 * @param amount
	 */
	public void setAmount(java.lang.Long amount) {
		this.amount = amount;
	}
	
	/**
	 * 订单金额(分)
	 * @return
	 */
	public java.lang.Long getAmount() {
		return this.amount;
	}
	/**
	 * 订单备注
	 * @param remark
	 */
	public void setRemark(java.lang.String remark) {
		this.remark = remark;
	}
	
	/**
	 * 订单备注
	 * @return
	 */
	public java.lang.String getRemark() {
		return this.remark;
	}
	/**
	 * 订单控制（当订单类型为M20001贷款申请时，为利息承担方标识：1. 企业 2.用户）
	 * @param orderControl
	 */
	public void setOrderControl(java.lang.String orderControl) {
		this.orderControl = orderControl;
	}
	
	/**
	 * 订单控制（当订单类型为M20001贷款申请时，为利息承担方标识：1. 企业 2.用户）
	 * @return
	 */
	public java.lang.String getOrderControl() {
		return this.orderControl;
	}
	/**
	 * 应答码
	 * @param respCode
	 */
	public void setRespCode(java.lang.String respCode) {
		this.respCode = respCode;
	}
	
	/**
	 * 应答码
	 * @return
	 */
	public java.lang.String getRespCode() {
		return this.respCode;
	}
	/**
	 * 订单状态,0失效,1生效,2过期
	 * @param statusId
	 */
	public void setStatusId(java.lang.String statusId) {
		this.statusId = statusId;
	}
	
	/**
	 * 订单状态,0失效,1生效,2过期
	 * @return
	 */
	public java.lang.String getStatusId() {
		return this.statusId;
	}
	/**
	 * 是否校验对账，0 未对账 1 已对账
	 * @param checkCode
	 */
	public void setCheckCode(Integer checkCode) {
		this.checkCode = checkCode;
	}
	
	/**
	 * 是否校验对账，0 未对账 1 已对账
	 * @return
	 */
	public Integer getCheckCode() {
		return this.checkCode;
	}
	/**
	 * 商品名字
	 * @param goodsName
	 */
	public void setGoodsName(java.lang.String goodsName) {
		this.goodsName = goodsName;
	}
	
	/**
	 * 商品名字
	 * @return
	 */
	public java.lang.String getGoodsName() {
		return this.goodsName;
	}
	/**
	 * 商品描述
	 * @param goodsDetail
	 */
	public void setGoodsDetail(java.lang.String goodsDetail) {
		this.goodsDetail = goodsDetail;
	}
	
	/**
	 * 商品描述
	 * @return
	 */
	public java.lang.String getGoodsDetail() {
		return this.goodsDetail;
	}
	/**
	 * 商品数量
	 * @param goodsCnt
	 */
	public void setGoodsCnt(java.lang.Integer goodsCnt) {
		this.goodsCnt = goodsCnt;
	}
	
	/**
	 * 商品数量
	 * @return
	 */
	public java.lang.Integer getGoodsCnt() {
		return this.goodsCnt;
	}
	/**
	 * 商品单价
	 * @param unitPrice
	 */
	public void setUnitPrice(java.lang.Long unitPrice) {
		this.unitPrice = unitPrice;
	}
	
	/**
	 * 商品单价
	 * @return
	 */
	public java.lang.Long getUnitPrice() {
		return this.unitPrice;
	}
	/**
	 * 从仓库编号
	 * @param fromStockSubjectCode
	 */
	public void setFromStockSubjectCode(java.lang.String fromStockSubjectCode) {
		this.fromStockSubjectCode = fromStockSubjectCode;
	}
	
	/**
	 * 从仓库编号
	 * @return
	 */
	public java.lang.String getFromStockSubjectCode() {
		return this.fromStockSubjectCode;
	}
	/**
	 * 到仓库编号
	 * @param toStockSubjectCode
	 */
	public void setToStockSubjectCode(java.lang.String toStockSubjectCode) {
		this.toStockSubjectCode = toStockSubjectCode;
	}
	
	/**
	 * 到仓库编号
	 * @return
	 */
	public java.lang.String getToStockSubjectCode() {
		return this.toStockSubjectCode;
	}
	/**
	 * 渠道编号
	 * @param channelCodeFk
	 */
	public void setChannelCodeFk(java.lang.Long channelCodeFk) {
		this.channelCodeFk = channelCodeFk;
	}
	
	/**
	 * 渠道编号
	 * @return
	 */
	public java.lang.Long getChannelCodeFk() {
		return this.channelCodeFk;
	}
	/**
	 * 订单类型
	 * @param orderType
	 */
	public void setOrderType(java.lang.Long orderType) {
		this.orderType = orderType;
	}
	
	/**
	 * 订单类型
	 * @return
	 */
	public java.lang.Long getOrderType() {
		return this.orderType;
	}
	/**
	 * 损耗类型(0 无损耗 1 我司责任 2 仓库责任)
	 * @param lostType
	 */
	public void setLostType(java.lang.Boolean lostType) {
		this.lostType = lostType;
	}
	
	/**
	 * 损耗类型(0 无损耗 1 我司责任 2 仓库责任)
	 * @return
	 */
	public java.lang.Boolean getLostType() {
		return this.lostType;
	}
	/**
	 * 亏损商品总数量
	 * @param lostProductNum
	 */
	public void setLostProductNum(java.lang.Integer lostProductNum) {
		this.lostProductNum = lostProductNum;
	}
	
	/**
	 * 亏损商品总数量
	 * @return
	 */
	public java.lang.Integer getLostProductNum() {
		return this.lostProductNum;
	}
	/**
	 * 亏损商品总金额
	 * @param lostProductAmount
	 */
	public void setLostProductAmount(java.lang.Long lostProductAmount) {
		this.lostProductAmount = lostProductAmount;
	}
	
	/**
	 * 亏损商品总金额
	 * @return
	 */
	public java.lang.Long getLostProductAmount() {
		return this.lostProductAmount;
	}
	/**
	 * 支付类型(0 未支付 1 全部支付 2 部分支付)
	 * @param paryType
	 */
	public void setParyType(java.lang.Boolean paryType) {
		this.paryType = paryType;
	}
	
	/**
	 * 支付类型(0 未支付 1 全部支付 2 部分支付)
	 * @return
	 */
	public java.lang.Boolean getParyType() {
		return this.paryType;
	}
	/**
	 * 支付金额(分)
	 * @param payAmount
	 */
	public void setPayAmount(java.lang.Long payAmount) {
		this.payAmount = payAmount;
	}
	
	/**
	 * 支付金额(分)
	 * @return
	 */
	public java.lang.Long getPayAmount() {
		return this.payAmount;
	}
	/**
	 * 物流费(分)
	 * @param materialFlowAmount
	 */
	public void setMaterialFlowAmount(java.lang.Long materialFlowAmount) {
		this.materialFlowAmount = materialFlowAmount;
	}
	
	/**
	 * 物流费(分)
	 * @return
	 */
	public java.lang.Long getMaterialFlowAmount() {
		return this.materialFlowAmount;
	}
	/**
	 * 第三方责任界定编号
	 * @param otherDutyOrderCode
	 */
	public void setOtherDutyOrderCode(java.lang.String otherDutyOrderCode) {
		this.otherDutyOrderCode = otherDutyOrderCode;
	}
	
	/**
	 * 第三方责任界定编号
	 * @return
	 */
	public java.lang.String getOtherDutyOrderCode() {
		return this.otherDutyOrderCode;
	}
	/**
	 * 库单状态
	 * @param orderStatus
	 */
	public void setOrderStatus(java.lang.Integer orderStatus) {
		this.orderStatus = orderStatus;
	}
	
	/**
	 * 库单状态
	 * @return
	 */
	public java.lang.Integer getOrderStatus() {
		return this.orderStatus;
	}
	/**
	 * 付款数量
	 * @param payNum
	 */
	public void setPayNum(java.lang.Integer payNum) {
		this.payNum = payNum;
	}
	
	/**
	 * 付款数量
	 * @return
	 */
	public java.lang.Integer getPayNum() {
		return this.payNum;
	}
	/**
	 * 付款单号
	 * @param payOrderCode
	 */
	public void setPayOrderCode(java.lang.String payOrderCode) {
		this.payOrderCode = payOrderCode;
	}
	
	/**
	 * 付款单号
	 * @return
	 */
	public java.lang.String getPayOrderCode() {
		return this.payOrderCode;
	}
	/**
	 * 平台订单号
	 * @param platformOrder
	 */
	public void setPlatformOrder(java.lang.String platformOrder) {
		this.platformOrder = platformOrder;
	}
	
	/**
	 * 平台订单号
	 * @return
	 */
	public java.lang.String getPlatformOrder() {
		return this.platformOrder;
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