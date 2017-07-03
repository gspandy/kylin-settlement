<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>修改文件信息</title>
<jsp:include page="../../base/base_page.jsp"></jsp:include>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/settlement/rule/edit.js"></script>
<link rel="stylesheet" type="text/css" media="screen" href="${pageContext.request.contextPath}/js/common/jquery-validate/demo/css/screen.css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/js/common/jquery-validate/jquery.validate.js"></script>
<script type="text/javascript">
$(function() {
	 var validator = $("#dataForm").validate({
		debug:true,
		errorPlacement:function(error,element) {
			error.css({"line-height":"28px"});
			element.parent("div").after(error);
	   }
	});
	$('#btnSubmit').click(function() {//提交按钮单击事件
		if(validator.form()){
			formSubmit();
		}
	});
})
</script>
</head>
<body onclick="closeSelect()">
	<div class="area">
		<form name="dataForm" id="dataForm" method="post" action="${pageContext.request.contextPath}/rule/edit_ajax">
			<div class="main">
				<div class="blank15"></div>
				<div class="wrap">	
					<div class="con">
						<div class="con_title">
							<h3 class="left fs14 marglr3020">修改文件信息</h3>
							<div class="attr_con">
								<a href="javascript:void(0);" class="chan_btn right wid93 foc_cur margr40 margt10" id="btnSubmit">保存</a> 
								<a href="javascript:gotoUrl(document.referrer);" class="chan_btn right wid93 margr40 margt10">返回</a> 
							</div>
						</div>
						<input type="hidden" name="ruleId" value="${settleRule.ruleId}"/>
						<div class="main_con">
							<div class="con_article_title02 paddtb20">
								<!-- 对账规则名称、规则种类 -->
								<div class="blank10"></div>
								<div class="con_core_info_div11">
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">对账规则名称：</span>
									</label>
									<div class="input_con_type_top wid190">
										<input type="text" id="ruleName" name="ruleName" class="wid190" value="${settleRule.ruleName}"/>
									</div>
								</div>
								<div class="con_core_info_div11">
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">规则种类：</span>
									</label>
									<div class="input_con_type_top wid190">
										<div class="input_con_type_top wid190">
											<input type="text" id="ruleType" name="ruleType" value="${settleRule.ruleType }" class="wid190 required" />
										</div>
									</div>
								</div>
								<!-- // 对账规则名称、规则种类 -->
								<!-- 上游交易类型、清算方对账项目 -->
								<div class="blank10"></div>
								<div class="con_core_info_div11">
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">上游交易类型：</span>
									</label>
									<div class="input_con_type_top wid190">
										<div class="input_con_type_top wid190">
											<input type="text" id="readType" name="readType" value="${settleRule.readType }" class="wid190 required" />
										</div>
									</div>
								</div>
								<div class="con_core_info_div11">
									<label class="label_txt wid130 txtright margt10 margr8" for="detKeyCode">
										<span class="right">清算方对账项目：</span>
										<span class="right fs18 margr8 red01">*</span>
									</label>
									<div class="input_con_type_top wid190">
										<input type="text" id="detKeyCode" name="detKeyCode" value="${settleRule.detKeyCode }" class="wid190 required" title="请输入清算方对账项目"/>
									</div>
								</div>
								<!-- // 上游交易类型、清算方对账项目 -->
								<!-- 上游渠道对账项目、对账名 -->
								<div class="blank10"></div>
								
								<div class="con_core_info_div11" style="padding-left:40px;">
									<label class="label_txt wid140 txtright margt10 margr8" for="accKeyCode">
										<span class="right">上游渠道对账项目：</span>
										<span class="right fs18 margr8 red01">*</span>
									</label>
									<div class="input_con_type_top wid190">
										<input type="text" id="accKeyCode" name="accKeyCode" value="${settleRule.accKeyCode }" class="wid190 required" title="请输入上游渠道对账项目"/>
									</div>
								</div>
								<div class="con_core_info_div11">
									<label class="label_txt wid130 txtright margt10 margr8" for="settleKeyName">
										<span class="right">对账名：</span>
										<span class="right fs18 margr8 red01">*</span>
									</label>
									<div class="input_con_type_top wid190">
										<input type="text" id="settleKeyName" name="settleKeyName" value="${settleRule.settleKeyName }" class="wid190 required" title="请输入对账名"/>
									</div>
								</div>
								<!-- // 上游渠道对账项目、对账名 -->
								<!-- ROP文件批次号、对账类型 -->
								<div class="blank10"></div>
								<div class="con_core_info_div11">
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">ROP文件批次号：</span>
									</label>
									<div class="input_con_type_top wid190">
										<input type="text" id="ropBatchNo" name="ropBatchNo" class="wid190" value="${settleRule.ropBatchNo }"/>
									</div>
								</div>
								<div class="con_core_info_div11">
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">对账类型：</span>
									</label>
									<div class="input_con_type_top wid190">
										<select style="width:189px;height:28px;" name="keyType">
										<option style="height:25px" value="0" ${settleRule.keyType==0?'selected':''}>对账KEY</option>
										<option style="height:25px" value="1" ${settleRule.keyType==1?'selected':''}>对账项目</option>
									</select>
									</div>
								</div>
								<!-- // ROP文件批次号、对账类型 -->
								<!-- 状态 -->
								<div class="con_core_info_div11">
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">状态：</span>
									</label>
									<div class="input_con_type_top wid190">
										<select style="width:189px;height:28px;" name="statusId">
											<option style="height:25px" value="1" ${settleRule.statusId==1?'selected':''}>有效</option>
											<option style="height:25px" value="0" ${settleRule.statusId==0?'selected':''}>无效</option>
										</select>
									</div>
								</div>
								<!-- // 状态 -->
								<!-- 生效时间、失效时间 -->
								<div class="blank10"></div>
								<div class="con_core_info_div05" style="padding-left:50px;">
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">生效-失效时间：</span>
									</label>
									<div class="input_time_type wid190">
										<input id="checkStartTime" type="text" readonly="readonly"
											name="startTime"
											class="wid140 hasDatepicker" value="<fmt:formatDate value="${settleRule.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"> 
											<a href="javascript:;"
											onclick="WdatePicker({el:'checkStartTime',isShowClear:true,readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'#F{$dp.$D(\'checkEndTime\')}'})"
											class="right icon_timer timer_calender margtr36"></a>
									</div>
									<div class="left wid10 paddtrl184">
										<span class="left wid10 bord19"></span>
									</div>
									<div class="input_time_type wid190">
										<input id="checkEndTime" type="text" readonly="readonly"
											name="endTime"
											class="wid140 hasDatepicker" value="<fmt:formatDate value="${settleRule.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"> 
											<a href="javascript:;"
											onclick="WdatePicker({el:'checkEndTime',isShowClear:true,readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'checkStartTime\')}'})"
											class="right icon_timer timer_calender margtr36"></a>
									</div>
								</div>
								<!-- // 生效时间、失效时间  -->
								<!-- 备注 -->
								<div class="blank10"></div>
								<div class="con_core_info_div11">
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">备注：</span>
									</label>
									<div >
										<textarea style="width:343px; height:111px" name="remark">${settleRule.remark}</textarea>
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