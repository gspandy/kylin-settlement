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
})
</script>
</head>
<body onclick="closeSelect()">
	<div class="area">
		<form name="dataForm" id="dataForm" method="post" action="${pageContext.request.contextPath}/file/edit_ajax">
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
						<input type="hidden" name="fileId" value="${settleFile.fileId}"/>
						<div class="main_con">
							<div class="con_article_title02 paddtb20">
								<!-- 文件模板名称、管理机构代码 -->
								<div class="blank10"></div>
								<div class="con_core_info_div11">
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">文件模板名称：</span>
									</label>
									<div class="input_con_type_top wid190">
										<input type="text" id="fileName" name="fileName" class="wid190" value="${settleFile.fileName}"/>
									</div>
								</div>
								<div class="con_core_info_div11">
									<label class="label_txt wid130 txtright margt10 margr8" for="rootInstCd">
										<span class="right">管理机构代码：</span>
										<span class="right fs18 margr8 red01">*</span>
									</label>
									<div class="input_con_type_top wid190">
										<input type="text" id="rootInstCd" name="rootInstCd" value="${settleFile.rootInstCd}" class="wid190 required" title="请输入管理机构代码"/>
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
										<input type="text" id="funcCodes" name="funcCodes" value="${settleFile.funcCodes}" class="wid190 required" title="请输入功能编码"/>
									</div>
								</div>
								<div class="con_core_info_div11">
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">功能码关系：</span>
									</label>
									<div class="input_con_type_top wid190">
										<select style="width:189px;height:28px;" name="funcRelation">
											<option style="height:25px" value="">所有</option>
											<option style="height:25px" value="0" ${settleFile.funcRelation==0?'selected':''}>全部</option>
											<option style="height:25px" value="1" ${settleFile.funcRelation==1?'selected':''}>包含</option>
											<option style="height:25px" value="2" ${settleFile.funcRelation==2?'selected':''}>差集</option>
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
										<select style="width:189px;height:28px;" name="payChannelId">
											<option style="height:25px" value="01" ${settleFile.payChannelId=='01'?'selected':''}>通联</option>
											<option style="height:25px" value="04" ${settleFile.payChannelId=='04'?'selected':''}>连连</option>
											<option style="height:25px" value="05" ${settleFile.payChannelId=='05'?'selected':''}>联动优势</option>
											<option style="height:25px" value="S01" ${settleFile.payChannelId=='S01'?'selected':''}>畅捷</option>
										</select>
									</div>
								</div>
								<div class="con_core_info_div11">
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">读入交易类型：</span>
									</label>
									<div class="input_con_type_top wid190">
										<select style="width:189px;height:28px;" name="readType">
											<option style="height:25px" value="">全部</option>
											<option style="height:25px" value="01" ${settleFile.readType=='01'?'selected':''}>网关支付</option>
											<option style="height:25px" value="02" ${settleFile.readType=='02'?'selected':''}>代收付</option>
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
										<input type="text" id="filePrefix" name="filePrefix" value="${settleFile.filePrefix}" class="wid190 required" title="请输入文件名称前缀"/>
									</div>
								</div>
								<div class="con_core_info_div11">
									<label class="label_txt wid130 txtright margt10 margr8" for="filePostfix">
										<span class="right">文件后缀：</span>
										<span class="right fs18 margr8 red01">*</span>
									</label>
									<div class="input_con_type_top wid190">
										<input type="text" id="filePostfix" name="filePostfix" value="${settleFile.filePostfix}" class="wid190 required" title="请输入文件后缀"/>
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
											<option style="height:25px" value="head" ${settleFile.fileType=='head'?'selected':''}>head</option>
											<option style="height:25px" value="body" ${settleFile.fileType=='body'?'selected':''}>body</option>
											<option style="height:25px" value="tail" ${settleFile.fileType=='tail'?'selected':''}>tail</option>
										</select>
									</div>
								</div>
								<div class="con_core_info_div11">
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">文件作用：</span>
									</label>
									<div class="input_con_type_top wid190">
										<select style="width:189px;height:28px;" name="fileActive">
											<option style="height:25px" value="1" ${settleFile.fileActive==1?'selected':''}>写入文件模板</option>
											<option style="height:25px" value="2" ${settleFile.fileActive==2?'selected':''}>读取文件模板</option>
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
										<input type="text" id="fileSubId" name="fileSubId" value="${settleFile.fileSubId}" class="wid190 required" title="请输入文件列信息码"/>
									</div>
								</div>
								<div class="con_core_info_div11">
									<label class="label_txt wid130 txtright margt10 margr8" for="fileEncode">
										<span class="right">文件编码：</span>
										<span class="right fs18 margr8 red01">*</span>
									</label>
									<div class="input_con_type_top wid190">
										<input type="text" id="fileEncode" name="fileEncode" value="${settleFile.fileEncode}" class="wid190 required" title="请输入文件编码"/>
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
										<input type="text" id="splitStr" name="splitStr" value="${settleFile.splitStr}" class="wid190"/>
									</div>
								</div>
								<div class="con_core_info_div11">
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">上传文件密钥名称：</span>
									</label>
									<div class="input_con_type_top wid190">
										<input type="text" id="uploadKeyName" name="uploadKeyName" value="${settleFile.uploadKeyName}" class="wid190"/>
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
										<input type="text" id="transStatusIds" name="transStatusIds" value="${settleFile.transStatusIds}" class="wid190"/>
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
										<input type="text" id="uploadKeyFlg" name="uploadKeyFlg" value="${settleFile.uploadKeyFlg}" class="wid190"/>
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
										<input type="text" id="ropBatchNo" name="ropBatchNo" value="${settleFile.ropBatchNo}" class="wid190"/>
									</div>
								</div>
								<div class="con_core_info_div11">
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">状态：</span>
									</label>
									<div class="input_con_type_top wid190">
										<select style="width:189px;height:28px;" name="statusId">
											<option style="height:25px" value="1" ${settleFile.statusId==1?'selected':''}>有效</option>
											<option style="height:25px" value="0" ${settleFile.statusId==0?'selected':''}>无效</option>
										</select>
									</div>
								</div>
								<div class="blank10"></div>
								<div class="con_core_info_div11">
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">rop文件类型：</span>
									</label>
									<div class="input_con_type_top wid190">
										<input type="text" id="ropFileType" name="ropFileType" value="${settleFile.ropFileType}" class="wid190"/>
									</div>
								</div>
								<div class="con_core_info_div11">
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">账期STEP：</span>
									</label>
									<div class="input_con_type_top wid190">
										<input type="text" id="dateStep" name="dateStep" value="${settleFile.dateStep}" class="wid190"/>
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
											class="wid140 hasDatepicker" value="<fmt:formatDate value="${settleFile.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"> 
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
											class="wid140 hasDatepicker" value="<fmt:formatDate value="${settleFile.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"> 
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
										<textarea style="width:343px; height:111px" name="remark">${settleFile.remark}</textarea>
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