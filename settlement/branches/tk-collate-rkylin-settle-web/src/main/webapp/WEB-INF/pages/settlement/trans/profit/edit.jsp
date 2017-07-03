<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>修改清分交易信息</title>
<jsp:include page="../../../base/base_page.jsp"></jsp:include>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/settlement/trans/profitEdit.js"></script>
</head>
<body onclick="closeSelect()">
	<div class="area">
		<form name="dataForm" id="dataForm" method="post" action="${pageContext.request.contextPath}/transprofit/edit_ajax">
			<div class="main">
				<div class="blank15"></div>
				<div class="wrap">	
					<div class="con">
						<div class="con_title">
							<h3 class="left fs14 marglr3020">修改清分交易信息</h3>
							<div class="attr_con">
								<a href="javascript:void(0);" class="chan_btn right wid93 foc_cur margr40 margt10" id="btnSubmit">保存</a> 
								<a href="javascript:gotoUrl(document.referrer);" class="chan_btn right wid93 margr40 margt10">返回</a> 
							</div>
						</div>
						
						<div class="main_con">
							<div class="con_article_title02 paddtb20">
								<!-- 交易分润ID、订单号 -->
							<div class="blank10"></div>
							<div class="con_core_info_div05">
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">交易分润ID：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleTransProfit.transProfitId}
									<input type="hidden" value="${settleTransProfit.transProfitId}" name="transProfitId" />
								</label>
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">订单号：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleTransProfit.orderNo}
								</label>
							</div>
							<!-- // 交易分润ID、订单号 -->
							<!-- 用户ID、商户编码 -->
							<div class="blank10"></div>
							<div class="con_core_info_div05">
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">用户ID：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleTransProfit.userId}
								</label>
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">商户编码：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleTransProfit.merchantCode}
								</label>
							</div>
							<!-- // 用户ID、商户编码 -->
							<!-- 管理机构代码、产品号 -->
							<div class="blank10"></div>
							<div class="con_core_info_div05">
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">管理机构代码：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleTransProfit.rootInstCd}
								</label>
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">产品号：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleTransProfit.productId}
								</label>
							</div>
							<!-- // 管理机构代码、产品号 -->
							<!-- 清分类型、分润金额 -->
							<div class="blank10"></div>
							<div class="con_core_info_div05">
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">清分类型：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${(settleTransProfit.profitType==0)?'分润':''}
									${(settleTransProfit.profitType==1)?'代收':''}
									${(settleTransProfit.profitType==2)?'代付':''}
								</label>
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">分润金额：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleTransProfit.profitAmount}
								</label>
							</div>
							<!-- // 清分类型、分润金额 -->
							<!-- 是否必须、记账日期 -->
							<div class="blank10"></div>
							<div class="con_core_info_div05">
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">是否必须：</span>
								</label>
								<label class="label_txt wid190 margt11">
									<c:choose>
									<c:when test="${settleTransProfit.isMust==0}">非必须</c:when>
									<c:when test="${settleTransProfit.isMust!=0}">必须</c:when>
									</c:choose>
								</label>
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">记账日期：</span>
								</label>
								<label class="label_txt wid190 margt11">
									<fmt:formatDate value="${settleTransProfit.accountDate}" pattern="yyyy-MM-dd"/>
								</label>
							</div>
							<!-- // 是否必须、记账日期 -->
							<!-- 状态 -->
							<div class="blank10"></div>
							<div class="con_core_info_div05">
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">状态：</span>
								</label>
								<div class="input_con_type_top wid190 margt4">
									<select style="width:189px;height:28px;" name="statusId">
										<option style="height:25px" value="0" ${(settleTransProfit.statusId==0)?'selected':''}>未进行</option>
										<option style="height:25px" value="1" ${(settleTransProfit.statusId==1)?'selected':''}>已进行</option>
										<option style="height:25px" value="2" ${(settleTransProfit.statusId==2)?'selected':''}>不处理</option>
									</select>
								</div>
							</div>
							<!-- // 状态 -->
							
							<!--记录创建时间、记录更新时间 -->
							<div class="blank10"></div>
							<div class="con_core_info_div05">
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">记录创建时间：</span>
								</label>
								<label class="label_txt wid190 margt11">
									<fmt:formatDate value="${settleTransProfit.createdTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
								</label>
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">记录更新时间：</span>
								</label>
								<label class="label_txt wid190 margt11">
									<fmt:formatDate value="${settleTransProfit.updatedTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
								</label>
							</div>
							<!-- // 记录创建时间、记录更新时间 -->
							<!-- 备注 -->
							<div class="blank10"></div>
							<div class="con_core_info_div05">
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">备注：</span>
								</label>
								<div >
									<textarea style="width:343px; height:111px" name="remark">${settleTransProfit.remark}</textarea>
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