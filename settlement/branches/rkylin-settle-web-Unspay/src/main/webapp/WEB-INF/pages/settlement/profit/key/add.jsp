<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>新增清分规则</title>
<jsp:include page="../../../base/base_page.jsp"></jsp:include>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/settlement/profit/keyEdit.js"></script>
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
		<form name="dataForm" id="dataForm" method="post" action="${pageContext.request.contextPath}/profitkey/save_ajax">
			<div class="main">
				<div class="blank15"></div>
				<div class="wrap">	
					<div class="con">
						<div class="con_title">
							<h3 class="left fs14 marglr3020">新增清分规则</h3>
							<div class="attr_con">
								<a href="javascript:void(0);" class="chan_btn right wid93 foc_cur margr40 margt10" id="btnSubmit">保存</a> 
								<a href="javascript:gotoUrl(document.referrer);" class="chan_btn right wid93 margr40 margt10">返回</a> 
							</div>
						</div>
						
						<div class="main_con">
							<div class="con_article_title02 paddtb20">
								<!-- 分润规则名称、管理机构代码 -->
								<div class="blank10"></div>
								<div class="con_core_info_div11">
									<label class="label_txt wid130 txtright margt10 margr8" for="profitRuleName">
										<span class="right">分润规则名称：</span>
									</label>
									<div class="input_con_type_top wid190">
										<input type="text" id="profitRuleName" name="profitRuleName" class="wid190"/>
									</div>
								</div>
								<div class="con_core_info_div11">
									<label class="label_txt wid130 txtright margt10 margr8" for="rootInstCd">
										<span class="right">管理机构代码：</span>
										<span class="right fs18 margr8 red01">*</span>
									</label>
									<div class="input_con_type_top wid190">
										<input type="text" id="rootInstCd" name="rootInstCd" class="wid190 required" title="请输入管理机构代码"/>
									</div>
								</div>
								<!-- // 分润规则名称、管理机构代码 -->
								<!-- 产品号、对应规则明细ID -->
								<div class="blank10"></div>
								<div class="con_core_info_div11">
									<label class="label_txt wid130 txtright margt10 margr8" for="funcCode">
										<span class="right">产品号：</span>
										<span class="right fs18 margr8 red01">*</span>
									</label>
									<div class="input_con_type_top wid190">
										<input type="text" id="funcCode" name="funcCode" class="wid190 required" title="请输入产品号"/>
									</div>
								</div>
								<div class="con_core_info_div11">
									<label class="label_txt wid130 txtright margt10 margr8" for="profitDetailId">
										<span class="right">对应规则明细ID：</span>
										<span class="right fs18 margr8 red01">*</span>
									</label>
									<div class="input_con_type_top wid190">
										<input type="text" id="profitDetailId" name="profitDetailId" class="wid190 required" title="请输入对应规则明细ID"/>
									</div>
								</div>
								<!-- // 产品号、对应规则明细ID -->
								<!-- 规则key1名称、规则key1值 -->
								<div class="blank10"></div>
								<div class="con_core_info_div11">
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">规则key1名称：</span>
									</label>
									<div class="input_con_type_top wid190">
										<input type="text" id="keyName1" name="keyName1" class="wid190"/>
									</div>
								</div>
								<div class="con_core_info_div11">
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">规则key1值：</span>
									</label>
									<div class="input_con_type_top wid190">
										<input type="text" id="keyValue1" name="keyValue1" class="wid190"/>
									</div>
								</div>
								<!-- // 规则key1名称、规则key1值 -->
								<!-- 规则key2名称、规则key2值 -->
								<div class="blank10"></div>
								<div class="con_core_info_div11">
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">规则key2名称：</span>
									</label>
									<div class="input_con_type_top wid190">
										<input type="text" id="keyName2" name="keyName2" class="wid190"/>
									</div>
								</div>
								<div class="con_core_info_div11">
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">规则key2值：</span>
									</label>
									<div class="input_con_type_top wid190">
										<input type="text" id="keyValue2" name="keyValue2" class="wid190"/>
									</div>
								</div>
								<!-- // 规则key2名称、规则key2值 -->
								<!-- 规则key3名称、规则key3值 -->
								<div class="blank10"></div>
								<div class="con_core_info_div11">
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">规则key3名称：</span>
									</label>
									<div class="input_con_type_top wid190">
										<input type="text" id="keyName3" name="keyName3" class="wid190"/>
									</div>
								</div>
								<div class="con_core_info_div11">
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">规则key3值：</span>
									</label>
									<div class="input_con_type_top wid190">
										<input type="text" id="keyValue3" name="keyValue3" class="wid190"/>
									</div>
								</div>
								<!-- // 规则key1名称、规则key1值 -->
								<!-- 状态 -->
								<div class="blank10"></div>
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
								<!-- 状态 -->
								<!-- 生效时间、失效时间 -->
								<div class="blank10"></div>
								<div class="con_core_info_div05" style="padding-left:50px;">
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">生效-失效时间：</span>
									</label>
									<div class="input_time_type wid190">
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
									<div class="input_time_type wid190">
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