<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>修改挂账交易信息</title>
<jsp:include page="../../../base/base_page.jsp"></jsp:include>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/settlement/trans/billEdit.js"></script>
</head>
<body onclick="closeSelect()">
	<div class="area">
		<form name="dataForm" id="dataForm" method="post" action="${pageContext.request.contextPath}/transbill/edit_ajax">
			<div class="main">
				<div class="blank15"></div>
				<div class="wrap">	
					<div class="con">
						<div class="con_title">
							<h3 class="left fs14 marglr3020">修改挂账交易信息</h3>
							<div class="attr_con">
								<a href="javascript:void(0);" class="chan_btn right wid93 foc_cur margr40 margt10" id="btnSubmit">保存</a> 
								<a href="javascript:gotoUrl(document.referrer);" class="chan_btn right wid93 margr40 margt10">返回</a> 
							</div>
						</div>
						
						<div class="main_con">
							<div class="con_article_title02 paddtb20">
								<!-- 挂账交易ID、交易订单号 -->
							<div class="blank10"></div>
							<div class="con_core_info_div05">
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">挂账交易ID：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleTransBill.transBillId}
									<input type="hidden" value="${settleTransBill.transBillId}" name="transBillId" />
								</label>
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">交易订单号：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleTransBill.orderNo}
								</label>
							</div>
							<!-- // 挂账交易ID、交易订单号 -->
							<!-- 批次号、挂账条目 -->
							<div class="blank10"></div>
							<div class="con_core_info_div05">
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">批次号：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleTransBill.batchNo}
								</label>
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">挂账条目：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleTransBill.billNo}
								</label>
							</div>
							<!-- // 批次号、挂账条目 -->
							<!-- 管理机构代码、产品号 -->
							<div class="blank10"></div>
							<div class="con_core_info_div05">
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">管理机构代码：</span>
								</label>
								<label class="label_txt wid190 margt11">
								${settleTransBill.rootInstCd}
								</label>
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">产品号：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleTransBill.productId}
								</label>
							</div>
							<!-- // 管理机构代码、产品号 -->
							<!-- 用户ID、第三方账户ID -->
							<div class="blank10"></div>
							<div class="con_core_info_div05">
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">用户ID：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleTransBill.userId}
								</label>
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">第三方账户ID：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleTransBill.referUserId}
								</label>
							</div>
							<!-- // 用户ID、第三方账户ID -->
							<!-- 挂账金额、挂账类型 -->
							<div class="blank10"></div>
							<div class="con_core_info_div05">
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">挂账金额：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleTransBill.billAmount}
								</label>
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">挂账类型：</span>
								</label>
								<label class="label_txt wid190 margt11">
									<c:choose>
										<c:when test="${settleTransBill.billType==0}">差错处理</c:when>
										<c:when test="${settleTransBill.billType==1}">退款</c:when>
										<c:otherwise>${settleTransBill.billType}</c:otherwise>
									</c:choose>
								</label>
							</div>
							<!-- // 挂账金额、挂账类型 -->
							<!--记账日期、状态 -->
							<div class="blank10"></div>
							<div class="con_core_info_div05">
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">记账日期：</span>
								</label>
								<label class="label_txt wid190 margt11">
									<fmt:formatDate value="${settleTransBill.accountDate}" pattern="yyyy-MM-dd"/>
								</label>
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">状态：</span>
								</label>
								<div class="input_con_type_top wid190 margt4">
									<select style="width:189px;height:28px;" name="statusId">
										<option style="height:25px" value="0" ${(settleTransBill.statusId==0)?'selected':''}>未进行</option>
										<option style="height:25px" value="1" ${(settleTransBill.statusId==1)?'selected':''}>已进行</option>
										<option style="height:25px" value="2" ${(settleTransBill.statusId==2)?'selected':''}>不处理</option>
									</select>
								</div>
							</div>
							<!-- // 记账日期、状态 -->
							<!-- 备注 -->
							<div class="blank10"></div>
							<div class="con_core_info_div05">
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">备注：</span>
								</label>
								<div >
									<textarea style="width:343px; height:111px" name="remark">${settleTransBill.remark}</textarea>
								</div>
							</div>
							<!-- // 备注 -->
							<div class="blank20"></div>
								
							</div>		
						</div>
					</div>
				</div>
			</div>
		</form>
	</div>
</body>