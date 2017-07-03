<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>新增文件列信息</title>
<jsp:include page="../../../base/base_page.jsp"></jsp:include>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/settlement/file/column/edit.js"></script>
<link rel="stylesheet" type="text/css" media="screen" href="${pageContext.request.contextPath}/js/common/jquery-validate/demo/css/screen.css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/js/common/jquery-validate/jquery.validate.js"></script>
<script type="text/javascript">
$(function() {
	jQuery.validator.addMethod("integer", function(value, element) {
	    return this.optional(element) || /^-?[1-9]\d*$/.test(value);
	});
	 var validator = $("#dataForm").validate({
		messages:{
			fileSubId: {
			    required: "请输入文件列信息码",
			    integer: "请输入数字"
			   },
		   fileColumnNo: {
			    required: "请输入子ID",
			    digits: "请输入数字"
			   }
		},
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
		<form name="dataForm" id="dataForm" method="post" action="${pageContext.request.contextPath}/filecolumn/save_ajax">
			<div class="main">
				<div class="blank15"></div>
				<div class="wrap">	
					<div class="con">
						<div class="con_title">
							<h3 class="left fs14 marglr3020">新增文件列信息</h3>
							<div class="attr_con">
								<a href="javascript:void(0);" class="chan_btn right wid93 foc_cur margr40 margt10" id="btnSubmit">保存</a> 
								<a href="javascript:gotoUrl(document.referrer);" class="chan_btn right wid93 margr40 margt10">返回</a> 
							</div>
						</div>
						
						<div class="main_con">
							<div class="con_article_title02 paddtb20">
								<!-- 文件列信息码 -->
								<div class="blank10"></div>
								<div class="con_core_info_div11" style="width:600px;">
									<label class="label_txt wid130 txtright margt10 margr8" for="fileSubId">
										<span class="right">文件列信息码：</span>
										<span class="right fs18 margr8 red01">*</span>
									</label>
									<div class="input_con_type_top wid190">
										<input type="text" id="fileSubId" name="fileSubId" class="wid190 required integer"/>
									</div>
									<span class="wid130 margr8 red01" style="line-height:28px;float:right;">缺省值：请填写-1</span>
								</div>
								<!-- // 文件列信息码 -->
								<!-- 列号、列名称 -->
								<div class="blank10"></div>
								<div class="con_core_info_div11">
									<label class="label_txt wid130 txtright margt10 margr8" for="fileColumnNo">
										<span class="right">列号：</span>
										<span class="right fs18 margr8 red01">*</span>
									</label>
									<div class="input_con_type_top wid190">
										<input type="text" id="fileColumnNo" name="fileColumnNo" class="wid190 required digits"/>
									</div>
								</div>
								<div class="con_core_info_div11">
									<label class="label_txt wid130 txtright margt10 margr8" for="fileColumnTitle">
										<span class="right">列名称：</span>
										<span class="right fs18 margr8 red01">*</span>
									</label>
									<div class="input_con_type_top wid190">
										<input type="text" id="fileColumnTitle" name="fileColumnTitle" class="wid190 required" title="请输入列名称"/>
									</div>
								</div>
								<!-- // 列号、列名称 -->
								<!-- 交易信息字段、交易信息字段说明 -->
								<div class="blank10"></div>
								<div class="con_core_info_div11">
									<label class="label_txt wid130 txtright margt10 margr8" for="fileColumnKeyCode">
										<span class="right">交易信息字段：</span>
										<span class="right fs18 margr8 red01">*</span>
									</label>
									<div class="input_con_type_top wid190">
										<input type="text" id="fileColumnKeyCode" name="fileColumnKeyCode" class="wid190 required" title="请输入交易信息字段"/>
									</div>
								</div>
								<div class="con_core_info_div11">
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">交易信息字段说明：</span>
									</label>
									<div class="input_con_type_top wid190">
										<input type="text" id="fileColumnKeyName" name="fileColumnKeyName" class="wid190"/>
									</div>
								</div>
								<!-- // 交易信息字段、交易信息字段说明 -->
								<!-- 是否特殊列、数据类型 -->
								<div class="blank10"></div>
								<div class="con_core_info_div11">
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">是否特殊列：</span>
									</label>
									<div class="input_con_type_top wid190">
										<select style="width:189px;height:28px;" name="isSpecialColumn">
											<option style="height:25px" value="0">否</option>
											<option style="height:25px" value="1">是</option>
										</select>
									</div>
								</div>
								<div class="con_core_info_div11">
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">数据类型：</span>
									</label>
									<div class="input_con_type_top wid190">
										<select style="width:189px;height:28px;" name="dataType">
											<option style="height:25px" value="1">字符串</option>
											<option style="height:25px" value="2">数字</option>
											<option style="height:25px" value="3">日期</option>
										</select>
									</div>
								</div>
								<!-- // 是否特殊列、数据类型 -->
								<!-- 数据格式、状态 -->
								<div class="blank10"></div>
								<div class="con_core_info_div11">
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">数据格式：</span>
									</label>
									<div class="input_con_type_top wid190">
										<input type="text" id="dataFormat" name="dataFormat" class="wid190"/>
									</div>
								</div>
								<div class="con_core_info_div11">
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">状态：</span>
									</label>
									<div class="input_con_type_top wid190">
										<select style="width:189px;height:28px;" name="statusId">
											<option style="height:25px" value="1">有效</option>
											<option style="height:25px" value="0">无效</option>
										</select>
									</div>
								</div>
								<!-- // 数据格式、状态 -->
								<!-- 生效时间、失效时间 -->
								<div class="blank10"></div>
								<div class="con_core_info_div05" style="padding-left:50px;">
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">生效-失效时间：</span>
									</label>
									<div class="input_time_type wid190 margt4">
										<input id="checkStartTime" type="text" readonly="readonly"
											name="startTime"
											class="wid140 hasDatepicker" value=""> 
											<a href="javascript:;"
											onclick="WdatePicker({el:'checkStartTime',isShowClear:true,readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'#F{$dp.$D(\'checkEndTime\')}'})"
											class="right icon_timer timer_calender margtr36"></a>
									</div>
									<div class="left wid10 paddtrl184">
										<span class="left wid10 bord19"></span>
									</div>
									<div class="input_time_type wid190 margt4">
										<input id="checkEndTime" type="text" readonly="readonly"
											name="endTime"
											class="wid140 hasDatepicker" value=""> 
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
										<textarea style="width:343px; height:111px" name="remark"></textarea>
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