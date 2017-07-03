<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>修改清分规则明细信息</title>
<jsp:include page="../../../base/base_page.jsp"></jsp:include>
<link rel="stylesheet" type="text/css" media="screen" href="${pageContext.request.contextPath}/js/common/jquery-validate/demo/css/screen.css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/js/settlement/profit/ruleEdit.js"></script>
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
		<form name="dataForm" id="dataForm" method="post" action="${pageContext.request.contextPath}/profitrule/edit_ajax">
			<div class="main">
				<div class="blank15"></div>
				<div class="wrap">	
					<div class="con">
						<div class="con_title">
							<h3 class="left fs14 marglr3020">修改清分规则明细信息</h3>
							<div class="attr_con">
								<a href="javascript:void(0);" class="chan_btn right wid93 foc_cur margr40 margt10" id="btnSubmit">保存</a> 
								<a href="javascript:gotoUrl(document.referrer);" class="chan_btn right wid93 margr40 margt10">返回</a> 
							</div>
						</div>
						
						<div class="main_con">
							<div class="con_article_title02 paddtb20">
								<!-- 明细ID、子ID -->
								<div class="blank10"></div>
								<div class="con_core_info_div11">
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">明细ID：</span>
									</label>
									<div class="input_con_type_top wid190">
										<input type="text" disabled="disabled" value="${settleProfitRule.profitDetailId}" class="wid190"/>
										<input type="text" id="profitDetailId" name="profitDetailId" value="${settleProfitRule.profitDetailId}"/>
									</div>
								</div>
								<div class="con_core_info_div11">
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">子ID：</span>
									</label>
									<div class="input_con_type_top wid190">
										<input type="text" disabled="disabled" value="${settleProfitRule.subId}" class="wid190"/>
										<input type="text" id="subId" name="subId" value="${settleProfitRule.subId}"/>
									</div>
								</div>
								<!-- // 明细ID、子ID -->
								<!-- 分润对象形态、管理机构代码 -->
								<div class="blank10"></div>
								<div class="con_core_info_div11">
									<label class="label_txt wid130 txtright margt10 margr8" for="subId">
										<span class="right">分润对象形态：</span>
									</label>
									<div class="input_con_type_top wid190 margt4">
										<select style="width:189px;height:28px;" name="profitObject">
											<option style="height:25px" value="1" ${settleProfitRule.profitObject==1?'selected':''}>固定</option>
											<option style="height:25px" value="2" ${settleProfitRule.profitObject==2?'selected':''}>可变</option>
										</select>
									</div>
								</div>
								<div class="con_core_info_div11">
									<label class="label_txt wid130 txtright margt10 margr8" for="rootInstCd">
										<span class="right">管理机构代码：</span>
										<span class="right fs18 margr8 red01">*</span>
									</label>
									<div class="input_con_type_top wid190 margt4">
										<input type="text" id="rootInstCd" name="rootInstCd" value="${settleProfitRule.rootInstCd}" class="wid190 required" title="请输入管理机构代码"/>
									</div>
								</div>
								<!-- // 分润对象形态、管理机构代码 -->
								<!-- 产品号、分润对象 -->
								<div class="blank10"></div>
								<div class="con_core_info_div11">
									<label class="label_txt wid130 txtright margt10 margr8" for="rootInstCd">
										<span class="right">产品号：</span>
										<span class="right fs18 margr8 red01">*</span>
									</label>
									<div class="input_con_type_top wid190 margt4">
										<input type="text" id="productId" name="productId" value="${settleProfitRule.productId}" class="wid190 required" title="请输入产品号"/>
									</div>
								</div>
								<div class="con_core_info_div11">
									<label class="label_txt wid130 txtright margt10 margr8" for="rootInstCd">
										<span class="right">分润对象：</span>
										<span class="right fs18 margr8 red01">*</span>
									</label>
									<div class="input_con_type_top wid190 margt4">
										<input type="text" id="userId" name="userId" value="${settleProfitRule.userId}" class="wid190 required" title="请输入分润对象"/>
									</div>
								</div>
								<!-- // 产品号、分润对象 -->
								<!-- 清分类型、是否必须 -->
								<div class="blank10"></div>
								<div class="con_core_info_div11">
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">清分类型：</span>
									</label>
									<div class="input_con_type_top wid190 margt4">
										<select style="width:189px;height:28px;" name="profitType">
											<option style="height:25px" value="1" ${settleProfitRule.profitType==1?'selected':''}>【insert分润信息 】并调用【分润接口】</option>
											<option style="height:25px" value="2" ${settleProfitRule.profitType==2?'selected':''}>【update】原交易</option>
										</select>
									</div>
								</div>
								<div class="con_core_info_div11">
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">是否必须：</span>
									</label>
									<div class="input_con_type_top wid190 margt4">
										<select style="width:189px;height:28px;" name="isMust">
											<option style="height:25px" value="0" ${settleProfitRule.isMust==0?'selected':''}>否</option>
											<option style="height:25px" value="1" ${settleProfitRule.isMust==1?'selected':''}>是</option>
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
									<div class="input_con_type_top wid190 margt4">
										<select style="width:189px;height:28px;" name="profitMd">
											<option style="height:25px" value="0" ${settleProfitRule.profitMd==0?'selected':''}>不启用</option>
											<option style="height:25px" value="1" ${settleProfitRule.profitMd==1?'selected':''}>比例</option>
											<option style="height:25px" value="2" ${settleProfitRule.profitMd==2?'selected':''}>固定</option>
										</select>
									</div>
								</div>
								<div class="con_core_info_div11">
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">模式内容：</span>
									</label>
									<div class="input_con_type_top wid190 margt4">
										<input type="text" id="profitFee" name="profitFee" class="wid190" value="${settleProfitRule.profitFee}"/>
									</div>
								</div>
								<!-- // 分润模式、模式内容 -->
								<!-- 封顶值、封底值 -->
								<div class="blank10"></div>
								<div class="con_core_info_div11">
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">封顶值：</span>
									</label>
									<div class="input_con_type_top wid190 margt4">
										<input type="text" id="feilvUp" name="feilvUp" class="wid190" value="${settleProfitRule.feilvUp}"/>
									</div>
								</div>
								<div class="con_core_info_div11">
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">封底值：</span>
									</label>
									<div class="input_con_type_top wid190 margt4">
										<input type="text" id="feilvBelow" name="feilvBelow" class="wid190" value="${settleProfitRule.feilvBelow}"/>
									</div>
								</div>
								<!-- // 封顶值、封底值 -->
								<!-- 生效时间、失效时间 -->
								<div class="blank10"></div>
								<div class="con_core_info_div05" style="padding-left:50px;">
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">生效-失效时间：</span>
									</label>
									<div class="input_time_type wid190 margt4">
										<input id="checkStartTime" type="text" readonly="readonly"
											name="startTime"
											class="wid140 hasDatepicker" value="<fmt:formatDate value="${settleProfitRule.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"> 
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
											class="wid140 hasDatepicker" value="<fmt:formatDate value="${settleProfitRule.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"> 
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
									<div class="input_con_type_top wid190 margt4">
										<select style="width:189px;height:28px;" name="statusId">
											<option style="height:25px" value="1"${settleProfitRule.statusId==1?'selected':''}>有效</option>
											<option style="height:25px" value="0"${settleProfitRule.statusId==0?'selected':''}>无效</option>
										</select>
									</div>
								</div>
								<!-- // 状态 -->
								<!-- 备注 -->
								<div class="blank10"></div>
								<div class="con_core_info_div11">
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">备注：</span>
									</label>
									<div >
										<textarea style="width:343px; height:111px" name="remark"> ${settleProfitRule.remark}</textarea>
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