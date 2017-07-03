<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>分润结果信息详情</title>
<jsp:include page="../../../base/base_page.jsp"></jsp:include>
</head>
<body onclick="closeSelect()">
	<div class="area">
		<div class="main">
			<div class="blank15"></div>
			<div class="wrap">	
				<div class="con">
					<div class="con_title">
						<h3 class="left fs14 marglr3020">分润结果信息详情</h3>
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
									<span class="right">贷款还款ID：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleLoanDetail.loanId}
									<input type="hidden" value="${settleLoanDetail.loanId}" name="loanId" />
								</label>
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">产品号：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleLoanDetail.productId}
								</label>
							</div>
							<!-- // 交易ID、交易请求号 -->
							<!-- 交易请求时间、统一交易流水号 -->
							<div class="blank10"></div>
							<div class="con_core_info_div05">
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">机构号：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleLoanDetail.rootInstCd}
								</label>
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">用户ID：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleLoanDetail.userId}
								</label>
							</div>
							<!-- // 交易请求时间、统一交易流水号 -->
							<!-- 订单号、订单包号 -->
							<div class="blank10"></div>
							<div class="con_core_info_div05">
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">保理ID：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleLoanDetail.factorId}
								</label>
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">培训机构ID：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleLoanDetail.outfitId}
								</label>
							</div>
							<!-- // 订单号、订单包号 -->
							<!-- 订单日期、订单金额 -->
							<div class="blank10"></div>
							<div class="con_core_info_div05">
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">代理商ID：</span>
								</label>
								<label class="label_txt wid190 margt11">
										${settleLoanDetail.agentId}
								</label>
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">利息承担方ID：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleLoanDetail.intrestPartyId}
								</label>
							</div>
							<!-- // 订单日期、订单金额 -->
							<!-- 订单数量、订单类型 -->
							<div class="blank10"></div>
							<div class="con_core_info_div05">
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">利息承担方ID2：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleLoanDetail.intrestPartyId2}
								</label>
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">利息承担方ID3：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleLoanDetail.intrestPartyId3}
								</label>
							</div>
							<!-- // 订单数量、订单类型 -->
							<!-- 功能编码、用户ID -->
							<div class="blank10"></div>
							<div class="con_core_info_div05">
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">费率模板ID：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleLoanDetail.rateId}
								</label>
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">贷款订单号：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleLoanDetail.loanOrderNo}
								</label>
							</div>
							<!-- // 功能编码、用户ID -->
							<!-- 中间商户编码、商户编码 -->
							<div class="blank10"></div>
							<div class="con_core_info_div05">
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">首付标识：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleLoanDetail.onePaymentFlg}
								</label>
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">还款期数：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleLoanDetail.repaymentCnt}
								</label>
							</div>
							<!-- // 中间商户编码、商户编码 -->
							<!-- 入账金额、手续费金额 -->
							<div class="blank10"></div>
							<div class="con_core_info_div05">
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">还款结果：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleLoanDetail.repaymentResult}
								</label>
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">还款时间：</span>
								</label>
								<label class="label_txt wid190 margt11">
									<fmt:formatDate value="${settleLoanDetail.repaymentDate}" pattern="yyyy-MM-hh HH:mm:ss"/>
								</label>
							</div>
							<!-- // 入账金额、手续费金额 -->
							<!-- 用户手续费、业务类型 -->
							<div class="blank10"></div>
							<div class="con_core_info_div05">
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">逾期标识：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleLoanDetail.overdueFlg}
								</label>
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">提前还款：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleLoanDetail.prepaymentFlg}
								</label>
							</div>
							<!-- // 用户手续费、业务类型 -->
							<!-- 支付渠道ID、银行联行编码 -->
							<div class="blank10"></div>
							<div class="con_core_info_div05">
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">退课标识：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleLoanDetail.dropClassFlg}
								</label>
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">日期标识：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleLoanDetail.dateFlg}
								</label>
							</div>
							<!-- // 支付渠道ID、银行联行编码 -->
							<!-- 消费者IP地址、产品号 -->
							<div class="blank10"></div>
							<div class="con_core_info_div05">
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">还款金额：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleLoanDetail.amount}
								</label>
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">特殊标识1：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleLoanDetail.specialFlg1}
								</label>
							</div>
							<!-- // 消费者IP地址、产品号 -->
							<!-- 错误编码、错误信息 -->
							<div class="blank10"></div>
							<div class="con_core_info_div05">
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">特殊标识2：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleLoanDetail.specialFlg2}
								</label>
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">特殊标识3：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleLoanDetail.specialFlg3}
								</label>
							</div>
							<!-- // 错误编码、错误信息 -->
							<!-- 被付方产品号、冲正/撤销标记 -->
							<div class="blank10"></div>
							<div class="con_core_info_div05">
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">预留1：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleLoanDetail.obligate1}
								</label>
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">预留2：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleLoanDetail.obligate2}
								</label>
							</div>
							<!-- // 被付方产品号、冲正/撤销标记 -->
							<!-- 数据来源、发送状态 -->
							<div class="blank10"></div>
							<div class="con_core_info_div05">
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">清算用KEY：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleLoanDetail.settleFlg}
								</label>
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">状态：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleLoanDetail.statusId}
								</label>
							</div>
							<!-- // 数据来源、发送状态 -->
							<!--结算订单号、状态 -->
							<div class="blank10"></div>
							<div class="con_core_info_div05">
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">失败原因：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleLoanDetail.errorMsg}
								</label>
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">接口返回信息：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleLoanDetail.returnMsg}
								</label>
							</div>
							<!-- // 记录创建时间、记录更新时间-->
							
							<!-- 备注 -->
							<div class="blank10"></div>
							<div class="con_core_info_div05">
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">备注：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleLoanDetail.remark}
								</label>
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