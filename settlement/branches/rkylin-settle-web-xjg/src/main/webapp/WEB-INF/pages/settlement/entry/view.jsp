<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>对账结果详情</title>
<jsp:include page="../../base/base_page.jsp"></jsp:include>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/settlement/entry/view.js"></script>
</head>
<body onclick="closeSelect()">
	<div class="area">
		<input type="hidden" value="no" id="isAdd">
		<div class="main">
			<div class="blank15"></div>
			<div class="wrap">	
				<div class="con_height0">
					<div class="con_title">
						<a href="javascript:void(0);">
							<h3 class="left fs14 marglr3020">对账结果&分润结果</h3></a>
						</a>
					</div>
					<div class="main_con" style="background:#FFF;">
						<div class="con_article_title02 paddtb20">
							<!-- 主键ID -->
							<div class="blank10"></div>
							<div class="con_core_info_div02">
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">主键ID：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleBalanceEntry.balanceEntryId}
									<input type="hidden" value="${settleBalanceEntry.balanceEntryId}" name="balanceEntryId" />
								</label>
							</div>
							<!-- // 主键ID -->
							<!-- 订单编号 -->
							<div class="blank10"></div>
							<div class="con_core_info_div02">
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">订单编号：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleBalanceEntry.orderNo}
								</label>
							</div>
							<!-- // 订单编号 -->
							<!-- 结果类型 -->
							<div class="blank10"></div>
							<div class="con_core_info_div02">
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">结果类型：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${(settleBalanceEntry.balanceType==3)? '【对账】 多渠道系统':''}
									${(settleBalanceEntry.balanceType==0)? '【分润】 账户系统':''}
								</label>
							</div>
							<!-- // 结果类型 -->
							<!-- 结果类型 -->
							<div class="blank10"></div>
							<div class="con_core_info_div02">
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">对账key：</span>
								</label>
								<label class="label_txt wid290 margt11">
									${(settleBalanceEntry.obligate1)}
								</label>
							</div>
							<!-- // 结果类型 -->
							<!-- 记账日期 -->
							<div class="blank10"></div>
							<div class="con_core_info_div02">
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">记账日期：</span>
								</label>
								<label class="label_txt wid190 margt11">
									<fmt:formatDate value="${settleBalanceEntry.accountDate}" pattern="yyyy-MM-dd" />
								</label>
							</div>
							<!-- // 记账日期 -->
							<!-- 创建时间 -->
							<div class="blank10"></div>
							<div class="con_core_info_div02">
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">创建时间：</span>
								</label>
								<label class="label_txt wid190 margt11">
									<fmt:formatDate value="${settleBalanceEntry.createdTime}" pattern="yyyy-MM-dd hh:mm:ss" />
								</label>
							</div>
							<!-- // 创建时间 -->
							<!-- 修改时间 -->
							<div class="blank10"></div>
							<div class="con_core_info_div02">
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">修改时间：</span>
								</label>
								<label class="label_txt wid190 margt11">
									<fmt:formatDate value="${settleBalanceEntry.updatedTime}" pattern="yyyy-MM-dd hh:mm:ss" />
								</label>
							</div>
							<!-- // 修改时间 -->	
							<!-- 状态 -->
							<div class="blank10"></div>
							<div class="con_core_info_div02">
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">状态：</span>
								</label>
								<label class="label_txt wid190 margt11">	
									${(settleBalanceEntry.statusId==1)?'【对账】 平账':''}
									${(settleBalanceEntry.statusId==0)?'【对账】 错账':''}
									${(settleBalanceEntry.statusId==2)?'【对账】 长款':''}
									${(settleBalanceEntry.statusId==3)?'【对账】 短款':''}
									${(settleBalanceEntry.statusId==11)?'【分润】 成功':''}
									${(settleBalanceEntry.statusId==10)?'【分润】 失败':''}
								</label>
							</div>
							<!-- // 状态 -->
							<!-- 备注 -->
							<div class="blank10"></div>
							<div class="con_core_info_div02">
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">备注：</span>
								</label>
								<label class="label_txt wid290 margt11">
									${settleBalanceEntry.remark}
								</label>
							</div>
							<!-- // 备注 -->
						</div>		
					</div>
				</div>
					
				<div class="con_height0">
					<div class="con_title">
						<a href="javascript:void(0);">
							<h3 class="left fs14 marglr3020">上游对账交易信息详情</h3>
						</a>
					</div>
					<div class="main_con" style="background:#FFF;">
						<div class="con_article_title02 paddtb20">
							<!-- 主键ID、支付渠道ID -->
							<div class="blank10"></div>
							<div class="con_core_info_div05">
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">主键ID：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleTransAccount.transAccountId}
								</label>
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">支付渠道：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${(settleTransAccount.payChannelId=='01')? '通联':''}
									${(settleTransAccount.payChannelId=='04')? '连连':''}
									${(settleTransAccount.payChannelId=='05')? '联动优势':''}
									${(settleTransAccount.payChannelId=='S01')? '畅捷':''}
								</label>
							</div>
							<!-- // 主键ID、支付渠道ID -->
							<!-- 交易类型、用户ID -->
							<div class="blank10"></div>
							<div class="con_core_info_div05">
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">交易类型：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${(settleTransAccount.readType=='01')?'网关支付':''}
									${(settleTransAccount.readType=='02')?'代收付':''}
								</label>
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">用户ID：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleTransAccount.userId}
								</label>
							</div>
							<!-- // 交易类型、用户ID -->
							<!-- 中间商户编码、商户编码 -->
							<div class="blank10"></div>
							<div class="con_core_info_div05">
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">中间商户编码ID：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleTransAccount.interMerchantCode}
								</label>
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">商户编码：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleTransAccount.merchantCode}
								</label>
							</div>
							<!-- // 中间商户编码、商户编码 -->
							<!-- 交易请求时间、订单号 -->
							<div class="blank10"></div>
							<div class="con_core_info_div05">
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">交易请求时间：</span>
								</label>
								<label class="label_txt wid190 margt11">
									<fmt:formatDate value="${settleTransAccount.requestTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
								</label>
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">订单号：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleTransAccount.orderNo}
								</label>
							</div>
							<!-- // 交易请求时间、订单号 -->
							<!-- 统一交易流水号、交易类型 -->
							<div class="blank10"></div>
							<div class="con_core_info_div05">
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">统一交易流水号：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleTransAccount.transFlowNo}
								</label>
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">交易类型：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleTransAccount.transType}
								</label>
							</div>
							<!-- // 统一交易流水号、交易类型 -->
							<!-- 交易金额、手续费金额 -->
							<div class="blank10"></div>
							<div class="con_core_info_div05">
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">交易金额：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleTransAccount.transAmount}
								</label>
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">手续费金额：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleTransAccount.feeAmount}
								</label>
							</div>
							<!-- // 交易金额、手续费金额 -->
							<!-- 清算金额、原始金额 -->
							<div class="blank10"></div>
							<div class="con_core_info_div05">
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">清算金额：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleTransAccount.settleAmount}
								</label>
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">原始金额：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleTransAccount.originalAmount}
								</label>
							</div>
							<!-- // 清算金额、原始金额 -->
							<!-- 币种、结算日期 -->
							<div class="blank10"></div>
							<div class="con_core_info_div05">
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">币种：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleTransAccount.currency}
								</label>
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">结算日期：</span>
								</label>
								<label class="label_txt wid190 margt11">
									<fmt:formatDate value="${settleTransAccount.settleTime}" pattern="yyyy-MM-dd"/>
								</label>
							</div>
							<!-- // 币种、结算日期 -->
							<!-- 文件类型、批次号 -->
							<div class="blank10"></div>
							<div class="con_core_info_div05">
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">文件类型：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleTransAccount.fileType}
								</label>
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">批次号：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleTransAccount.batchNo}
								</label>
							</div>
							<!-- // 文件类型、批次号 -->
							<!-- 创建时间、更新时间 -->
							<div class="blank10"></div>
							<div class="con_core_info_div05">
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">创建时间：</span>
								</label>
								<label class="label_txt wid190 margt11">
									<fmt:formatDate value="${settleTransAccount.createdTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
								</label>
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">更新时间：</span>
								</label>
								<label class="label_txt wid190 margt11">
									<fmt:formatDate value="${settleTransAccount.updatedTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
								</label>
							</div>
							<!-- // 创建时间、更新时间 -->
							<!-- 状态 -->
							<div class="blank10"></div>
							<div class="con_core_info_div05">
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">状态：</span>
								</label>
								<label class="label_txt wid190 margt11">
										${(settleTransAccount.statusId==0)?'【交易】失败':''}
										${(settleTransAccount.statusId==1)?'【交易】成功':''}
										${(settleTransAccount.statusId==2)?'【交易】不明':''}
										${(settleTransAccount.statusId==21)?'【对账】成功':''}
										${(settleTransAccount.statusId==22)?'【对账】 错账':''}
										${(settleTransAccount.statusId==23)?'【对账】 长款':''}
								</label>
							</div>
							<!-- // 状态 -->
							
							<!-- 备注 -->
							<div class="blank10"></div>
							<div class="con_core_info_div05">
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">备注：</span>
								</label>
								<label style="width:343px; height:111px">${settleTransAccount.remark}</label>
							</div>
							<!-- // 备注 -->
							<div class="blank20"></div>
						</div>		
					</div>
				</div>
				
				<div class="con_height0">
					<div class="con_title">
						<a href="javascript:void(0);">
							<h3 class="left fs14 marglr3020">账户交易信息详情</h3>
						</a>
					</div>
					<div class="main_con" style="background:#FFF;">
						<div class="con_article_title02 paddtb20">
							<!-- 交易ID、交易请求号 -->
							<div class="blank10"></div>
							<div class="con_core_info_div05">
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">交易ID：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleTransDetail.transDetailId}
								</label>
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">交易请求号：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleTransDetail.requestNo}
								</label>
							</div>
							<!-- // 交易ID、交易请求号 -->
							<!-- 交易请求时间、统一交易流水号 -->
							<div class="blank10"></div>
							<div class="con_core_info_div05">
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">交易请求时间：</span>
								</label>
								<label class="label_txt wid190 margt11">
									<fmt:formatDate value="${settleTransDetail.requestTime}" pattern="yyyy-MM-hh HH:mm:ss"/>
								</label>
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">统一交易流水号：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleTransDetail.transFlowNo}
								</label>
							</div>
							<!-- // 交易请求时间、统一交易流水号 -->
							<!-- 订单号、订单包号 -->
							<div class="blank10"></div>
							<div class="con_core_info_div05">
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">订单号：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleTransDetail.orderNo}
								</label>
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">订单包号：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleTransDetail.orderPackageNo}
								</label>
							</div>
							<!-- // 订单号、订单包号 -->
							<!-- 订单日期、订单金额 -->
							<div class="blank10"></div>
							<div class="con_core_info_div05">
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">订单日期：</span>
								</label>
								<label class="label_txt wid190 margt11">
									<fmt:formatDate value="${settleTransDetail.orderDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
								</label>
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">订单金额：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleTransDetail.orderAmount}
								</label>
							</div>
							<!-- // 订单日期、订单金额 -->
							<!-- 订单数量、订单类型 -->
							<div class="blank10"></div>
							<div class="con_core_info_div05">
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">订单数量：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleTransDetail.orderCount}
								</label>
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">订单类型：</span>
								</label>
								<label class="label_txt wid190 margt11">
									<c:choose>
										<c:when test="${settleTransDetail.orderType==0}">交易</c:when>
										<c:when test="${settleTransDetail.orderType==1}">结算单</c:when>
										<c:otherwise>${settleTransDetail.orderType}</c:otherwise>
									</c:choose>
								</label>
							</div>
							<!-- // 订单数量、订单类型 -->
							<!-- 功能编码、用户ID -->
							<div class="blank10"></div>
							<div class="con_core_info_div05">
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">功能编码：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleTransDetail.funcCode}
								</label>
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">用户ID：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleTransDetail.userId}
								</label>
							</div>
							<!-- // 功能编码、用户ID -->
							<!-- 中间商户编码、商户编码 -->
							<div class="blank10"></div>
							<div class="con_core_info_div05">
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">中间商户编码：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleTransDetail.interMerchantCode}
								</label>
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">商户编码：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleTransDetail.merchantCode}
								</label>
							</div>
							<!-- // 中间商户编码、商户编码 -->
							<!-- 入账金额、手续费金额 -->
							<div class="blank10"></div>
							<div class="con_core_info_div05">
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">入账金额：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleTransDetail.amount}
								</label>
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">手续费金额：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleTransDetail.feeAmount}
								</label>
							</div>
							<!-- // 入账金额、手续费金额 -->
							<!-- 用户手续费、业务类型 -->
							<div class="blank10"></div>
							<div class="con_core_info_div05">
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">用户手续费：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleTransDetail.userFee}
								</label>
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">业务类型：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleTransDetail.businessType}
								</label>
							</div>
							<!-- // 用户手续费、业务类型 -->
							<!-- 支付渠道ID、银行联行编码 -->
							<div class="blank10"></div>
							<div class="con_core_info_div05">
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">支付渠道：</span>
								</label>
								<label class="label_txt wid190 margt11">
									<c:choose>
										<c:when test="${settleTransDetail.payChannelId=='01'}">通联</c:when>
										<c:when test="${settleTransDetail.payChannelId=='04'}">连连</c:when>
										<c:when test="${settleTransDetail.payChannelId=='05'}">联动优势</c:when>
										<c:when test="${settleTransDetail.payChannelId=='S01'}">畅捷</c:when>
										<c:otherwise>${settleTransDetail.payChannelId}</c:otherwise>
									</c:choose>
								</label>
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">银行联行编码：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleTransDetail.bankCode}
								</label>
							</div>
							<!-- // 支付渠道ID、银行联行编码 -->
							<!-- 消费者IP地址、产品号 -->
							<div class="blank10"></div>
							<div class="con_core_info_div05">
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">消费者IP地址：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleTransDetail.userIpAddress}
								</label>
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">产品号：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleTransDetail.productId}
								</label>
							</div>
							<!-- // 消费者IP地址、产品号 -->
							<!-- 错误编码、错误信息 -->
							<div class="blank10"></div>
							<div class="con_core_info_div05">
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">错误编码：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleTransDetail.errorCode}
								</label>
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">错误信息：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleTransDetail.errorMsg}
								</label>
							</div>
							<!-- // 错误编码、错误信息 -->
							<!-- 被付方产品号、冲正/撤销标记 -->
							<div class="blank10"></div>
							<div class="con_core_info_div05">
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">被付方产品号：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleTransDetail.productWid}
								</label>
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">冲正/撤销标记：</span>
								</label>
								<label class="label_txt wid190 margt11">
									<c:choose>
										<c:when test="${settleTransDetail.cancelInd==0}">正常</c:when>
										<c:when test="${settleTransDetail.cancelInd==1}">冲正</c:when>
										<c:when test="${settleTransDetail.cancelInd==2}">撤销</c:when>
										<c:otherwise>${settleTransDetail.cancelInd}</c:otherwise>
									</c:choose>
								</label>
							</div>
							<!-- // 被付方产品号、冲正/撤销标记 -->
							<!-- 数据来源、发送状态 -->
							<div class="blank10"></div>
							<div class="con_core_info_div05">
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">数据来源：</span>
								</label>
								<label class="label_txt wid190 margt11">
									<c:choose>
										<c:when test="${settleTransDetail.dataFrom==0}">账户</c:when>
										<c:when test="${settleTransDetail.dataFrom==1}">收银台</c:when>
										<c:when test="${settleTransDetail.dataFrom==2}">代收付</c:when>
										<c:otherwise>${settleTransDetail.dataFrom}</c:otherwise>
									</c:choose>
								</label>
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">发送状态：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleTransDetail.deliverStatusId}
								</label>
							</div>
							<!-- // 数据来源、发送状态 -->
							<!--结算订单号、状态 -->
							<div class="blank10"></div>
							<div class="con_core_info_div05">
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">结算订单号：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleTransDetail.invoiceNo}
								</label>
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">状态：</span>
								</label>
								<label class="label_txt wid190 margt11">
									<c:choose>
										<c:when test="${settleTransDetail.statusId==0}">【交易】失败</c:when>
										<c:when test="${settleTransDetail.statusId==1}">【交易】成功</c:when>
										<c:when test="${settleTransDetail.statusId==2}">【交易】不明</c:when>
										<c:when test="${settleTransDetail.statusId==10}">【清分】失败</c:when>
										<c:when test="${settleTransDetail.statusId==11}">【清分】成功</c:when>
										<c:when test="${settleTransDetail.statusId==20}">【对账】异常</c:when>
										<c:when test="${settleTransDetail.statusId==21}">【对账】平账</c:when>
										<c:when test="${settleTransDetail.statusId==22}">【对账】错帐</c:when>
										<c:when test="${settleTransDetail.statusId==24}">【对账】短款</c:when>
										<c:when test="${settleTransDetail.statusId==40}">【清算】失败</c:when>
										<c:when test="${settleTransDetail.statusId==41}">【清算】成功</c:when>
										<c:otherwise>${settleTransDetail.statusId}</c:otherwise>
									</c:choose>
								</label>
							</div>
							<!--结算订单号、状态 -->
							<div class="blank10"></div>
							<div class="con_core_info_div05">
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">代收付状态：</span>
								</label>
								<label class="label_txt wid190 margt11">
									<c:choose>
										<c:when test="${settleTransDetail.dflag==0}">【代收付】未处理</c:when>
										<c:when test="${settleTransDetail.dflag==1}">【代收付】处理中</c:when>
										<c:when test="${settleTransDetail.dflag==13}">【代收付】失败</c:when>
										<c:when test="${settleTransDetail.dflag==15}">【代收付】成功</c:when>
										<c:otherwise>${settleTransDetail.dflag}</c:otherwise>
									</c:choose>
								</label>
							</div>
							<!-- // 结算订单号、状态 -->
							<!--记账日期 -->
							<div class="blank10"></div>
							<div class="con_core_info_div05">
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">读入交易状态：</span>
								</label>
								<label class="label_txt wid190 margt11">
									<c:choose>
										<c:when test="${settleTransDetail.readStatusId==0}">失效</c:when>
										<c:when test="${settleTransDetail.readStatusId==1}">正常</c:when>
										<c:when test="${settleTransDetail.readStatusId==2}">对账成功</c:when>
										<c:when test="${settleTransDetail.readStatusId==3}">对账失败</c:when>
										<c:when test="${settleTransDetail.readStatusId==4}">付款成功</c:when>
										<c:when test="${settleTransDetail.readStatusId==5}">付款失败</c:when>
										<c:when test="${settleTransDetail.readStatusId==6}">冲正</c:when>
										<c:when test="${settleTransDetail.readStatusId==7}">已汇总</c:when>
										<c:when test="${settleTransDetail.readStatusId==8}">已分润</c:when>
										<c:when test="${settleTransDetail.readStatusId==9}">退票</c:when>
										<c:when test="${settleTransDetail.readStatusId==10}">受理</c:when>
										<c:when test="${settleTransDetail.readStatusId==11}">发送异常</c:when>
										<c:when test="${settleTransDetail.readStatusId==12}">银行已受理</c:when>
										<c:when test="${settleTransDetail.readStatusId==13}">银行未受理</c:when>
										<c:when test="${settleTransDetail.readStatusId==14}">处理失败</c:when>
										<c:when test="${settleTransDetail.readStatusId==15}">部分成功</c:when>
										<c:when test="${settleTransDetail.readStatusId==16}">成功</c:when>
										<c:when test="${settleTransDetail.readStatusId==17}">退票</c:when>
										<c:otherwise>${settleTransDetail.readStatusId}</c:otherwise>
									</c:choose>
								</label>
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">记账日期：</span>
								</label>
								<label class="label_txt wid190 margt11">
									<fmt:formatDate value="${settleTransDetail.accountDate}" pattern="yyyy-MM-dd"/>
								</label>
							</div>
							<!-- // 记账日期 -->
							<!--记录创建时间、记录更新时间 -->
							<div class="blank10"></div>
							<div class="con_core_info_div05">
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">记录创建时间：</span>
								</label>
								<label class="label_txt wid190 margt11">
									<fmt:formatDate value="${settleTransDetail.createdTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
								</label>
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">记录更新时间：</span>
								</label>
								<label class="label_txt wid190 margt11">
									<fmt:formatDate value="${settleTransDetail.updatedTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
								</label>
							</div>
							<!-- // 记录创建时间、记录更新时间-->
							
							<!-- 备注 -->
							<div class="blank10"></div>
							<div class="con_core_info_div05">
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">备注：</span>
								</label>
								<label style="width:343px; height:111px">${settleTransDetail.remark}</label>
							</div>
							<!-- // 备注 -->
							<div class="blank20"></div>
						</div>		
					</div>
				</div>
			</div>
		</div>
	</div>
</body>