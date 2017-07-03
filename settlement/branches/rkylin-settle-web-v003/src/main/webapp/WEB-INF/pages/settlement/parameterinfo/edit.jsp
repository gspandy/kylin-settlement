<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>修改系统参数信息</title>
<jsp:include page="../../base/base_page.jsp"></jsp:include>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/settlement/parameterinfo/edit.js"></script>
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
		<form name="dataForm" id="dataForm" method="post" action="${pageContext.request.contextPath}/parameterinfo/edit_ajax">
			<div class="main">
				<div class="blank15"></div>
				<div class="wrap">	
					<div class="con">
						<div class="con_title">
							<h3 class="left fs14 marglr3020">修改系统参数信息</h3>
							<div class="attr_con">
								<a href="javascript:void(0);" class="chan_btn right wid93 foc_cur margr40 margt10" id="btnSubmit">保存</a> 
								<a href="javascript:gotoUrl(document.referrer);" class="chan_btn right wid93 margr40 margt10">返回</a> 
							</div>
						</div>
						<input type="hidden" name="parameterId" value="${settleParameterInfo.parameterId}"/>
						<div class="main_con">
							<div class="con_article_title02 paddtb20">
								<!-- 参数类型、产品ID -->
								<div class="blank10"></div>
								<div class="con_core_info_div11">
									<label class="label_txt wid130 txtright margt10 margr8" for="parameterType">
										<span class="right">参数类型：</span>
										<span class="right fs18 margr8 red01">*</span>
									</label>
									<div class="input_con_type_top wid190">
										<input type="text" id="parameterType" name="parameterType" value="${settleParameterInfo.parameterType}" class="wid190 required" title="请输入参数类型"/>
									</div>
								</div>
								<div class="con_core_info_div11">
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">产品ID：</span>
									</label>
									<div class="input_con_type_top wid190">
										<input type="text" id="productId" name="productId" value="${settleParameterInfo.productId}" class="wid190"/>
									</div>
								</div>
								<!-- // 参数类型、产品ID -->
								<!-- 参数编码、参数值 -->
								<div class="blank10"></div>
								<div class="con_core_info_div11">
									<label class="label_txt wid130 txtright margt10 margr8" for="parameterCode">
										<span class="right">参数编码：</span>
										<span class="right fs18 margr8 red01">*</span>
									</label>
									<div class="input_con_type_top wid190">
										<input type="text" id="parameterCode" name="parameterCode" value="${settleParameterInfo.parameterCode}" class="wid190 required" title="请输入参数编码"/>
									</div>
								</div>
								<div class="con_core_info_div11">
									<label class="label_txt wid130 txtright margt10 margr8" for="parameterValue">
										<span class="right">参数值：</span>
										<span class="right fs18 margr8 red01">*</span>
									</label>
									<div class="input_con_type_top wid190">
										<input type="text" id="parameterValue" name="parameterValue" value="${settleParameterInfo.parameterValue}" class="wid190 required" title="请输入参数值"/>
									</div>
								</div>
								<!-- // 参数编码、参数值 -->
								<!-- 预留1、预留2 -->
								<div class="blank10"></div>
								
								<div class="con_core_info_div11">
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">预留1：</span>
									</label>
									<div class="input_con_type_top wid190">
										<input type="text" id="obligate1" name="obligate1" value="${settleParameterInfo.obligate1}" class="wid190"/>
									</div>
								</div>
								<div class="con_core_info_div11">
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">预留2：</span>
									</label>
									<div class="input_con_type_top wid190">
										<input type="text" id="obligate2" name="obligate2" value="${settleParameterInfo.obligate2}" class="wid190"/>
									</div>
								</div>
								<!-- // 预留1、预留2 -->
								<!-- 预留3、状态 -->
								<div class="blank10"></div>
								<div class="con_core_info_div11">
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">预留3：</span>
									</label>
									<div class="input_con_type_top wid190">
										<input type="text" id="obligate3" name="obligate3" value="${settleParameterInfo.obligate3}" class="wid190"/>
									</div>
								</div>
								<div class="con_core_info_div11">
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">状态：</span>
									</label>
									<div class="input_con_type_top wid190">
										<select style="width:189px;height:28px;" name="statusId">
											<option style="height:25px" value="1" ${settleParameterInfo.statusId==1?'selected':''}>有效</option>
											<option style="height:25px" value="0" ${settleParameterInfo.statusId==0?'selected':''}>无效</option>
										</select>
									</div>
								</div>
								<!-- // 预留3、状态 -->
								<!-- 备注 -->
								<div class="blank10"></div>
								<div class="con_core_info_div11">
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">备注：</span>
									</label>
									<div >
										<textarea style="width:343px; height:111px" name="remark">${settleParameterInfo.remark}</textarea>
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