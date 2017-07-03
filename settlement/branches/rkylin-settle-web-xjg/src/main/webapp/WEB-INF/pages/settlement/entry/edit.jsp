<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>修改对账结果&分润结果</title>
<jsp:include page="../../base/base_page.jsp"></jsp:include>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/settlement/entry/edit.js"></script>
</head>
<body onclick="closeSelect()">
	<div class="area">
		<form name="dataForm" id="dataForm" method="post" action="${pageContext.request.contextPath}/entry/edit_ajax">
			<input type="hidden" value="no" id="isAdd">
			<div class="main">
				<div class="blank15"></div>
				<div class="wrap">	
					<div class="con">
						<div class="con_title">
							<h3 class="left fs14 marglr3020">修改对账结果&分润结果</h3>
							<div class="attr_con">
								<a href="javascript:void(0);" class="chan_btn right wid93 foc_cur margr40 margt10" id="btnSubmit">保存</a> 
								<a href="javascript:gotoUrl(document.referrer);" class="chan_btn right wid93 margr40 margt10">返回</a> 
							</div>
						</div>
						
						<div class="main_con">
							<div class="con_core_info margt20">
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
									<div class="input_con_type_top wid190 margt4">
										<select style="width:189px;height:28px;" name="statusId">
											<option style="height:25px" value="1" ${(settleBalanceEntry.statusId==1)?'selected':''}>【对账】 平账</option>
											<option style="height:25px" value="0" ${(settleBalanceEntry.statusId==0)?'selected':''}>【对账】 错账</option>
											<option style="height:25px" value="2" ${(settleBalanceEntry.statusId==2)?'selected':''}>【对账】 长款</option>
											<option style="height:25px" value="3" ${(settleBalanceEntry.statusId==3)?'selected':''}>【对账】 短款</option>
											<option style="height:25px" value="11" ${(settleBalanceEntry.statusId==11)?'selected':''}>【分润】 成功</option>
											<option style="height:25px" value="10" ${(settleBalanceEntry.statusId==10)?'selected':''}>【分润】 失败</option>
										</select>
									</div>
								</div>
								<!-- // 状态 -->
								<!-- 备注 -->
								<div class="blank10"></div>
								<div class="con_core_info_div02">
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">备注：</span>
									</label>
									<div >
										<textarea style="width:343px; height:111px" name="remark">${settleBalanceEntry.remark}</textarea>
									</div>
								</div>
								<!-- // 备注 -->
							</div>		
						</div>
						<div class="blank20"></div>
					</div>
				</div>
			</div>
		</form>
	</div>
</body>