<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>结算信息详情</title>
<jsp:include page="../../../base/base_page.jsp"></jsp:include>
</head>
<body onclick="closeSelect()">
	<div class="area">
		<div class="main">
			<div class="blank15"></div>
			<div class="wrap">	
				<div class="con">
					<div class="con_title">
						<h3 class="left fs14 marglr3020">结算信息详情</h3>
						<div class="attr_con">
							<a href="javascript:gotoUrl(document.referrer);" class="chan_btn right wid93 margr40 margt10">返回</a> 
						</div>
					</div>
					
					<div class="main_con">
						<div class="con_article_title02 paddtb20">
							<!-- 交易ID、交易请求号 -->
							<div class="blank10"></div>
							<div class="con_core_info_div05">
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">交易ID：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleTransInvoice.invoiceNo}
								</label>
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">交易批次号：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleTransInvoice.requestNo}
								</label>
							</div>
							<!-- // 交易ID、交易请求号 -->
							<!-- 交易请求时间、统一交易流水号 -->
							<div class="blank10"></div>
							<div class="con_core_info_div05">
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">业务代码：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleTransInvoice.bussinessCode}
								</label>
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">机构号：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleTransInvoice.rootInstCd}
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
									${settleTransInvoice.orderNo}
								</label>
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">内部批次号：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleTransInvoice.batchNo}
								</label>
							</div>
							<!-- // 订单号、订单包号 -->
							<!-- 订单日期、订单金额 -->
							<div class="blank10"></div>
							<div class="con_core_info_div05">
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">银行代码：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleTransInvoice.bankCode}
								</label>
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">账户类型：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleTransInvoice.accountType}
								</label>
							</div>
							<!-- // 订单日期、订单金额 -->
							<!-- 订单数量、订单类型 -->
							<div class="blank10"></div>
							<div class="con_core_info_div05">
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">用户ID：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleTransInvoice.userId}
								</label>
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">账号：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleTransInvoice.accountNo}
								</label>
							</div>
							<!-- // 订单数量、订单类型 -->
							<!-- 功能编码、用户ID -->
							<div class="blank10"></div>
							<div class="con_core_info_div05">
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">账号名：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleTransInvoice.accountName}
								</label>
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">账号属性：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleTransInvoice.accountProperty}
								</label>
							</div>
							<!-- // 功能编码、用户ID -->
							<!-- 中间商户编码、商户编码 -->
							<div class="blank10"></div>
							<div class="con_core_info_div05">
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">开户行所在省：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleTransInvoice.province}
								</label>
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">开户行所在市：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleTransInvoice.city}
								</label>
							</div>
							<!-- // 中间商户编码、商户编码 -->
							<!-- 入账金额、手续费金额 -->
							<div class="blank10"></div>
							<div class="con_core_info_div05">
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">开户行名称：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleTransInvoice.openBankName}
								</label>
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">支付行号：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleTransInvoice.payBankCode}
								</label>
							</div>
							<!-- // 入账金额、手续费金额 -->
							<!-- 用户手续费、业务类型 -->
							<div class="blank10"></div>
							<div class="con_core_info_div05">
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">金额：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleTransInvoice.amount}
								</label>
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">币种：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleTransInvoice.currency}
								</label>
							</div>
							<!-- // 用户手续费、业务类型 -->
							<!-- 支付渠道ID、银行联行编码 -->
							<div class="blank10"></div>
							<div class="con_core_info_div05">
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">开户证件类型：</span>
								</label>
								<label class="label_txt wid190 margt11">
										${settleTransInvoice.certificateType}
								</label>
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">证件号：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleTransInvoice.certificateNumber}
								</label>
							</div>
							<!-- // 支付渠道ID、银行联行编码 -->
							<!-- 消费者IP地址、产品号 -->
							<div class="blank10"></div>
							<div class="con_core_info_div05">
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">数据来源：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleTransInvoice.dataSource}
								</label>
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">处理结果：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleTransInvoice.processResult}
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
									${settleTransInvoice.errorCode}
								</label>
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">发送类型：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleTransInvoice.sendType}
								</label>
							</div>
							<!-- // 错误编码、错误信息 -->
							<!-- 被付方产品号、冲正/撤销标记 -->
							<div class="blank10"></div>
							<div class="con_core_info_div05">
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">发送次数：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleTransInvoice.sendTimes}
								</label>
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">状态：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleTransInvoice.statusId}
								</label>
							</div>
							<!-- // 被付方产品号、冲正/撤销标记 -->
							<!-- 数据来源、发送状态 -->
							<div class="blank10"></div>
							<div class="con_core_info_div05">
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">记账日期：</span>
								</label>
								<label class="label_txt wid190 margt11">
								    <fmt:formatDate value="${settleTransInvoice.accountDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
								</label>
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">用户备注：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleTransInvoice.remark}
								</label>
							</div>
							<!-- // 数据来源、发送状态 -->
							<!--结算订单号、状态 -->
							<div class="blank10"></div>
							<div class="con_core_info_div05">
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">记录创建时间：</span>
								</label>
								<label class="label_txt wid190 margt11">
									<fmt:formatDate value="${settleTransInvoice.createdTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
								</label>
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">记录更新时间：</span>
								</label>
								<label class="label_txt wid190 margt11">
									<fmt:formatDate value="${settleTransInvoice.updatedTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
								</label>
							</div>
						</div>		
					</div>
				</div>
			</div>
		</div>
	</div>
</body>