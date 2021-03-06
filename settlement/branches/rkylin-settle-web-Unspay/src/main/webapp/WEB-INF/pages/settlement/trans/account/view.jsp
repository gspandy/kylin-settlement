<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>上游对账交易信息详情</title>
<jsp:include page="../../../base/base_page.jsp"></jsp:include>
</head>
<body onclick="closeSelect()">
	<div class="area">
		<input type="hidden" value="no" id="isAdd">
		<div class="main">
			<div class="blank15"></div>
			<div class="wrap">	
				<div class="con">
					<div class="con_title">
						<h3 class="left fs14 marglr3020">上游对账交易信息详情</h3>
						<div class="attr_con">
							<a href="javascript:gotoUrl(document.referrer);" class="chan_btn right wid93 margr40 margt10" id="btnReturn">返回</a> 
						</div>
					</div>
					
					<div class="main_con">
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
			</div>
		</div>
	</div>
</body>