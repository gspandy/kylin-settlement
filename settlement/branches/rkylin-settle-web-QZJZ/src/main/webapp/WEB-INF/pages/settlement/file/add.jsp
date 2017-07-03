<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>新增文件信息</title>
<jsp:include page="../../base/base_page.jsp"></jsp:include>
<style type="text/css">
   .divhide {display:none}
</style>

<script type="text/javascript" src="${pageContext.request.contextPath}/js/settlement/file/edit.js"></script>
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
	
	//监听支付渠道，如果change了，级联出对应的交易类型
	$("#payChannelId").on("change",function(){
		if($("#payChannelId").val()=="04"){
			$("#readType").val($("#llreadType").val());
			$("#tldiv").hide();
			$("#lldiv").show();
		}else{
			$("#readType").val($("#tlreadType").val());
			$("#tldiv").show();
			$("#lldiv").hide();
		}
		
	});
	
	//将通联的交易类型的值赋给readType(交易类型)
	$("#tlreadType").on("change",function(){
		$("#readType").val($("#tlreadType").val());
	});
	
	//将连连的交易类型的值赋给readType(交易类型)
	$("#llreadType").on("change",function(){
		$("#readType").val($("#llreadType").val());
	});
})


	

</script>
</head>
<body onclick="closeSelect()">
	<div class="area">
		<form name="dataForm" id="dataForm" method="post" action="${pageContext.request.contextPath}/file/save_ajax">
			<input type="hidden" id="readType" name="readType" />
			<div class="main">
				<div class="blank15"></div>
				<div class="wrap">	
					<div class="con">
						<div class="con_title">
							<h3 class="left fs14 marglr3020">新增文件信息</h3>
							<div class="attr_con">
								<a href="javascript:void(0);" class="chan_btn right wid93 foc_cur margr40 margt10" id="btnSubmit">保存</a> 
								<a href="javascript:gotoUrl(document.referrer);" class="chan_btn right wid93 margr40 margt10">返回</a> 
							</div>
						</div>
						
						<div class="main_con">
							<div class="con_article_title02 paddtb20">
								<!-- 文件模板名称、管理机构代码 -->
								<div class="blank10"></div>
								<div class="con_core_info_div11">
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">文件模板名称：</span>
									</label>
									<div class="input_con_type_top wid190">
										<input type="text" id="fileName" name="fileName" class="wid190"/>
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
								<!-- // 文件模板名称、管理机构代码 -->
								<!-- 功能编码、功能码关系 -->
								<div class="blank10"></div>
								<div class="con_core_info_div11">
									<label class="label_txt wid130 txtright margt10 margr8" for="funcCodes">
										<span class="right">功能编码：</span>
										<span class="right fs18 margr8 red01">*</span>
									</label>
									<div class="input_con_type_top wid190">
										<input type="text" id="funcCodes" name="funcCodes" class="wid190 required" title="请输入功能编码"/>
									</div>
								</div>
								<div class="con_core_info_div11">
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">功能码关系：</span>
									</label>
									<div class="input_con_type_top wid190">
										<select style="width:189px;height:28px;" name="funcRelation">
											<option style="height:25px" value="">所有</option>
											<option style="height:25px" value="0">全部</option>
											<option style="height:25px" value="1">包含</option>
											<option style="height:25px" value="2">差集</option>
										</select>
									</div>
								</div>
								<!-- // 功能编码、功能码关系 -->
								<!-- 支付渠道、交易状态/HEAD行数 -->
								<div class="blank10"></div>
								
								<div class="con_core_info_div11">
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">支付渠道：</span>
									</label>
									<div class="input_con_type_top wid190">
										<select style="width:189px;height:28px;" name="payChannelId" id="payChannelId">
											<option style="height:25px" value="01">通联</option>
											<option style="height:25px" value="04">连连</option>
											<option style="height:25px" value="05">联动优势</option>
											<option style="height:25px" value="S01">畅捷</option>
										</select>
									</div>
								</div>
								<div class="con_core_info_div11">
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">读入交易类型：</span>
									</label>
									<div class="input_con_type_top wid190" id="tldiv">
										<select style="width:189px;height:28px;" name="tlreadType" id="tlreadType" >
											<option style="height:25px" value="">全部</option>
											<option style="height:25px" value="01">网关支付</option>
											<option style="height:25px" value="02">代收付</option>
										</select>
									</div>
									<div class="input_con_type_top wid190 divhide" id="lldiv">
										<select style="width:189px;height:28px;" name="llreadType" id="llreadType">
											<option style="height:25px" value="">全部</option>
											<option style="height:25px" value="04">移动快捷支付</option>
										</select>
									</div>
								</div>
								<!-- // 支付渠道、交易状态/HEAD行数 -->
								<!-- 文件名称前缀、文件后缀 -->
								<div class="blank10"></div>
								<div class="con_core_info_div11">
									<label class="label_txt wid130 txtright margt10 margr8" for="filePrefix">
										<span class="right">文件名称前缀：</span>
										<span class="right fs18 margr8 red01">*</span>
									</label>
									<div class="input_con_type_top wid190">
										<input type="text" id="filePrefix" name="filePrefix" class="wid190 required" title="请输入文件名称前缀"/>
									</div>
								</div>
								<div class="con_core_info_div11">
									<label class="label_txt wid130 txtright margt10 margr8" for="filePostfix">
										<span class="right">文件后缀：</span>
										<span class="right fs18 margr8 red01">*</span>
									</label>
									<div class="input_con_type_top wid190">
										<input type="text" id="filePostfix" name="filePostfix" class="wid190 required" title="请输入文件后缀"/>
									</div>
								</div>
								<!-- // 文件名称前缀、文件后缀 -->
								<!-- 文件类型、文件作用 -->
								<div class="blank10"></div>
									<div class="con_core_info_div11">
										<label class="label_txt wid130 txtright margt10 margr8">
											<span class="right">文件类型：</span>
										</label>
										<div class="input_con_type_top wid190">
										<select style="width:189px;height:28px;" name="fileType">
											<option style="height:25px" value="head">head</option>
											<option style="height:25px" value="body">body</option>
											<option style="height:25px" value="foot">foot</option>
										</select>
									</div>
								</div>
								<div class="con_core_info_div11">
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">文件作用：</span>
									</label>
									<div class="input_con_type_top wid190">
										<select style="width:189px;height:28px;" name="fileActive">
											<option style="height:25px" value="1">写入文件模板</option>
											<option style="height:25px" value="2">读取文件模板</option>
										</select>
									</div>
								</div>
								<!-- // 文件类型、文件作用 -->
								<!-- 文件列信息码、文件编码 -->
								<div class="blank10"></div>
								<div class="con_core_info_div11">
									<label class="label_txt wid130 txtright margt10 margr8" for="fileSubId">
										<span class="right">文件列信息码：</span>
										<span class="right fs18 margr8 red01">*</span>
									</label>
									<div class="input_con_type_top wid190">
										<input type="text" id="fileSubId" name="fileSubId" class="wid190 required" title="请输入文件列信息码"/>
									</div>
								</div>
								<div class="con_core_info_div11">
									<label class="label_txt wid130 txtright margt10 margr8" for="fileEncode">
										<span class="right">文件编码：</span>
										<span class="right fs18 margr8 red01">*</span>
									</label>
									<div class="input_con_type_top wid190">
										<input type="text" id="fileEncode" name="fileEncode" class="wid190 required" title="请输入文件编码"/>
									</div>
								</div>
								<!-- // 文件列信息码、文件编码 -->
								<!-- 信息分隔符、上传文件密钥名称 -->
								<div class="blank10"></div>
								<div class="con_core_info_div11">
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">信息分隔符：</span>
									</label>
									<div class="input_con_type_top wid190">
										<input type="text" id="splitStr" name="splitStr" class="wid190"/>
									</div>
								</div>
								<div class="con_core_info_div11">
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">上传文件密钥名称：</span>
									</label>
									<div class="input_con_type_top wid190">
										<input type="text" id="uploadKeyName" name="uploadKeyName" class="wid190"/>
									</div>
								</div>
								<!-- // 信息分隔符、上传文件密钥名称 -->
								<!-- 交易状态/HEAD行数、上传文件密钥/是否入库 -->
								<div class="blank10"></div>
								<div class="con_core_info_div05 margt10" style="padding-left:40px;">
									<label class="label_txt wid140 txtright margt10 margr8">
										<span class="right">交易状态/HEAD行数：</span>
									</label>
									<div class="input_con_type_top wid190">
										<input type="text" id="transStatusIds" name="transStatusIds" class="wid190"/>
									</div>
									<span class="label_txt wid450 margt11 red01">
										写入时:[此文件显示的是交易信息'状态', 多个状态用','隔开]; 读取时:[对账文件HEAD行数]
									</span>
								</div>
								<div class="con_core_info_div05 margt10" style="padding-left:50px;">
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">密钥类型/是否入库：</span>
									</label>
									<div class="input_con_type_top wid190">
										<input type="text" id="uploadKeyFlg" name="uploadKeyFlg" class="wid190"/>
									</div>
									<label class="label_txt wid600 margt11 red01">
										写入时:[上传文件 所用的密钥类型 私钥1 公钥0]; 读取时[是否录入数据库0:不录入,1:录入]
									</label>
								</div>
								<!-- // 交易状态/HEAD行数、上传文件密钥/是否入库 -->
								<!-- rop文件批次号、状态 -->
								<div class="blank10"></div>
								<div class="con_core_info_div11">
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">rop文件批次号：</span>
									</label>
									<div class="input_con_type_top wid190">
										<input type="text" id="ropBatchNo" name="ropBatchNo" class="wid190"/>
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
								<div class="blank10"></div>
								<div class="con_core_info_div11">
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">rop文件类型：</span>
									</label>
									<div class="input_con_type_top wid190">
										<input type="text" id="ropFileType" name="ropFileType" value="" class="wid190"/>
									</div>
								</div>
								<div class="con_core_info_div11">
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">账期STEP：</span>
									</label>
									<div class="input_con_type_top wid190">
										<input type="text" id="dateStep" name="dateStep" value="" class="wid190"/>
									</div>
								</div>
								<!-- // rop文件批次号、状态 -->
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