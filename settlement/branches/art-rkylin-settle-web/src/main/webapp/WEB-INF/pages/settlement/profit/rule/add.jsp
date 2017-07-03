<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>新增清分规则明细信息</title>
<jsp:include page="../../../base/base_page.jsp"></jsp:include>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/settlement/profit/ruleEdit.js"></script>
<link rel="stylesheet" type="text/css" media="screen" href="${pageContext.request.contextPath}/js/common/jquery-validate/demo/css/screen.css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/js/common/jquery-validate/jquery.validate.js"></script>
<script type="text/javascript">
$(function() {
	jQuery.validator.addMethod("integer", function(value, element) {
	    return this.optional(element) || /^-?[1-9]\d*$/.test(value);
	});
	 var validator = $("#dataForm").validate({
		 messages:{
			 profitDetailId: {
				    required: "请输入明细ID",
				    integer: "请输入数字"
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
		<form name="dataForm" id="dataForm" method="post" action="${pageContext.request.contextPath}/profitrule/save_ajax">
			<div class="main">
				<div class="blank15"></div>
				<div class="wrap">	
					<div class="con">
						<div class="con_title">
							<h3 class="left fs14 marglr3020">新增清分规则明细信息</h3>
							<div class="attr_con">
								<a href="javascript:void(0);" class="chan_btn right wid93 foc_cur margr40 margt10" id="btnSubmit">保存</a> 
								<a href="javascript:gotoUrl(document.referrer);" class="chan_btn right wid93 margr40 margt10">返回</a> 
							</div>
						</div>
						
						<div class="main_con">
							<div class="con_article_title02 paddtb20">
								<!-- 明细ID、子ID -->
								<div class="blank10"></div>
								<div class="con_core_info_div11 wid600">
	                                <label class="label_txt wid130 txtright margt10 margr8" for="profitDetailId">
	                                    <span class="right">明细ID：</span>
	                                    <span class="right fs18 margr8 red01">*</span>
	                                </label>
	                                <div class="input_con_type_top wid190">
	                                    <input type="text" id="profitDetailId" name="profitDetailId" class="wid190 required integer"/>
	                                </div>
	                                <span class="wid130 margr8 red01" style="line-height:28px;float:right;">缺省值：请填写-1</span>
	                            </div>
	                            <div class="blank10"></div>
								<div class="con_core_info_div11">
									<label class="label_txt wid130 txtright margt10 margr8" for="subId">
	                                    <span class="right">子ID：</span>
	                                    <span class="right fs18 margr8 red01">*</span>
	                                </label>
									<div class="input_con_type_top wid190">
										<input type="text" id="subId" name="subId" class="wid190 required" title="请输入子ID"/>
									</div>
								</div>
								<!-- // 明细ID、子ID -->
								<!-- 分润对象形态、管理机构代码 -->
								<div class="blank10"></div>
								<div class="con_core_info_div11">
									<label class="label_txt wid130 txtright margt10 margr8" for="profitObject">
	                                    <span class="right">分润对象形态：</span>
	                                </label>
									<div class="input_con_type_top wid190">
										<select style="width:189px;height:28px;" name="profitObject">
											<option style="height:25px" value="1">固定</option>
											<option style="height:25px" value="2">可变</option>
										</select>
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
								<!-- // 分润对象形态、管理机构代码 -->
								<!-- 产品号、分润对象 -->
								<div class="blank10"></div>
								<div class="con_core_info_div11">
									<label class="label_txt wid130 txtright margt10 margr8" for="productId">
										<span class="right">产品号：</span>
										<span class="right fs18 margr8 red01">*</span>
									</label>
									<div class="input_con_type_top wid190">
										<input type="text" id="productId" name="productId" class="wid190 required" title="请输入产品号"/>
									</div>
								</div>
								<div class="con_core_info_div11">
									<label class="label_txt wid130 txtright margt10 margr8" for="userId">
										<span class="right">分润对象：</span>
										<span class="right fs18 margr8 red01">*</span>
									</label>
									<div class="input_con_type_top wid190">
										<input type="text" id="userId" name="userId" class="wid190 required" title="请输入分润对象"/>
									</div>
								</div>
								<!-- // 产品号、分润对象 -->
								<!-- 清分类型、是否必须 -->
								<div class="blank10"></div>
								<div class="con_core_info_div11">
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">清分类型：</span>
									</label>
									<div class="input_con_type_top wid190">
										<select style="width:189px;height:28px;" name="profitType">
											<option style="height:25px" value="0">分润</option>
											<option style="height:25px" value="1">代收</option>
											<option style="height:25px" value="2">代付</option>
										</select>
									</div>
								</div>
								<div class="con_core_info_div11">
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">是否必须：</span>
									</label>
									<div class="input_con_type_top wid190">
										<select style="width:189px;height:28px;" name="isMust">
											<option style="height:25px" value="0">否</option>
											<option style="height:25px" value="1">是</option>
										</select>
									</div>
								</div>
								<!-- // 清分类型、是否必须 -->
								<!-- 分润模式、模式内容	 -->
								<div class="blank10"></div>
								<div class="con_core_info_div11">
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">分润模式：</span>
									</label>
									<div class="input_con_type_top wid190">
										<select style="width:189px;height:28px;" name="profitMd">
											<option style="height:25px" value="0">不启用</option>
											<option style="height:25px" value="1">比例</option>
											<option style="height:25px" value="2">固定</option>
										</select>
									</div>
								</div>
								<div class="con_core_info_div11">
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">模式内容：</span>
									</label>
									<div class="input_con_type_top wid190">
										<input type="text" id="profitFee" name="profitFee" class="wid190"/>
									</div>
								</div>
								<!-- // 分润模式、模式内容 -->
								<!-- 封顶值、封底值 -->
								<div class="blank10"></div>
								<div class="con_core_info_div11">
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">封顶值：</span>
									</label>
									<div class="input_con_type_top wid190">
										<input type="text" id="feilvUp" name="feilvUp" class="wid190"/>
									</div>
								</div>
								<div class="con_core_info_div11">
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">封底值：</span>
									</label>
									<div class="input_con_type_top wid190">
										<input type="text" id="feilvBelow" name="feilvBelow" class="wid190"/>
									</div>
								</div>
								<!-- // 封顶值、封底值 -->
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
								<!-- // 状态 -->
								<!-- 备注 -->
								<div class="blank10"></div>
								<div class="con_core_info_div02">
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