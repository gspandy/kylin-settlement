<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>上游对账文件操作页面</title>
<jsp:include page="../../../base/base_page.jsp"/>
<style type="text/css">
	.divhide {display:none}
</style>
<script type="text/javascript">
/*
 * 读取上游对账文件
 */
function doReadfile(butt) {
	var isSure = confirmButtAct(butt);
	if(isSure) readfile();
}
/*
 *下载上游对账文件
 */
function doDownloadFile(butt) {
	var isSure = confirmButtAct(butt);
	if(isSure) downloadFile();
}
function readfile() {
	$.post(
		"transaccount/read_ajax",										//发送请求 url
		$('#dataForm').serialize(),										//表单数据
		function(data) {												//回调函数
			art.dialog.tip1s(data.msg, 1.5);
		},
		'json'
	);
}
function downloadFile() {
	$.post(
		"transaccount/download_ajax",									//发送请求 url
		$('#dataForm').serialize(),										//表单数据
		function(data) {												//回调函数
			art.dialog.tip1s(data.msg, 1.5);
		},
		'json'
	);
}
artDialog.tip1s = function (content, time) {
    return artDialog({
        id: 'Tips',
        title: false,
        cancel: false,
        fixed: true,
        lock: false
    })
    .content('<div style="padding: 0 1em;">' + content + '</div>')
    .time(time || 1);
}

/* $(function(){
	$("#readType").val("01");
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
	
	$("#tlreadType").on("change",function(){
		$("#readType").val($("#tlreadType").val());
	});
	$("#llreadType").on("change",function(){
		$("#readType").val($("#llreadType").val());
	});
}); */

function payChannelIdAndSelect(payChannelIdSelectId, select2Id) {
	var payChannelId = $("#"+ payChannelIdSelectId +" option:selected").val();
	$("#"+ select2Id +" option").hide();
	$("#"+ select2Id +" option").attr("selected",false);
	$("#"+ select2Id +" option[class*='o_"+payChannelId+"']").show();
	$("#"+ select2Id +" option[class*='o_"+payChannelId+"']:first").attr("selected",true);
	
	$("#" + payChannelIdSelectId).on("change",function(){
		var payChannelId = $("#"+ payChannelIdSelectId +" option:selected").val();
		$("#"+ select2Id +" option").hide();
		$("#"+ select2Id +" option").attr("selected",false);
		$("#"+ select2Id +" option[class*='o_"+payChannelId+"']").show();
		$("#"+ select2Id +" option[class*='o_"+payChannelId+"']:first").attr("selected",true);
	});
}
$(function() {
	/**
	 * payChannelId 下拉菜单和 accountType菜单二级联动
	 */
	payChannelIdAndSelect("payChannelId", "readType");
});
</script>
</head>
<div class="area">
	<form name="dataForm" id="dataForm" method="post">
	<!-- <input type="hidden" id="readType" name="readType" /> -->
		<div class="main">
			<div class="blank15"></div>
			<div class="wrap">
				<div class="con">
					<div class="con_title">
						<h3 class="left fs14 marglr3020">上游对账文件操作页面</h3>
						<span class="grey"></span>
					</div>
					<div class="main_con">
						<div class="con_article_title02 tit_zindex6" id="reg2_arrow_con">
							<div class="con_article_title02 margt10 tit_zindex5 paddl52" style="width:90%">
								<label class="label_txt wid135 txtright margr8 margt11 margl16">机构号： </label>
								<div class="input_con_type_top wid190 margt4">
									<input type="text" class="wid190" name="merchantCode" id="merchantCode"/>
								</div>
								<label class="label_txt wid135 txtright margr8 margt11">支付渠道： </label>
								<div class="input_con_type_top wid190 margt4">
									<select style="width:189px;height:28px;" name="payChannelId" id="payChannelId">
										<option style="height:25px" value="01">通联</option>
										<option style="height:25px" value="04">连连</option>
										<option style="height:25px" value="05">联动优势</option>
										<option style="height:25px" value="S01">畅捷</option>
										<option style="height:25px" value="Y01">平安银行</option>
										<option style="height:25px" value="Y02">民生银行</option>
										<option style="height:25px" value="P01">银联商务</option>
									</select>
								</div>
								<label class="label_txt wid135 txtright margr8 margt11 margl16">交易类型： </label>
								<div class="input_con_type_top wid190 margt4" id="tldiv">
									<select style="width:189px;height:28px;" name="readType" id="readType">
										<option style="height:25px" class="o_01 o_05" value="01">网关对账</option>
										<option style="height:25px" class="o_01 o_04 o_05 o_S01" value="02">代收付对账</option>
										<option style="height:25px" class="o_01" value="01">移动对账</option>
										<option style="height:25px" class="o_Y01" value="05">平安银企直联</option>
										<option style="height:25px" class="o_01" value="06">收银宝(微信)</option>
										<option style="height:25px" class="o_Y02" value="07">民生银企直联</option>
										<option style="height:25px" class="o_04" value="04">移动快捷支付</option>
										<option style="height:25px" class="o_01" value="08">SDK(通联)</option>
										<option style="height:25px" class="o_P01" value="08">Pos收单</option>
										<option style="height:25px" class="o_01" value="09">微信APP</option>
									</select>
								</div>
							</div>
							<div class="con_article_title02 tit_zindex6" id="reg2_arrow_con">
								<div class="con_article_title02 margt10 tit_zindex5 paddl52" style="width:90%">
									<label class="label_txt wid135 txtright margr8 margt11 margl16">账期： </label>
									<div class="input_time_type wid190 margt4">
										<input id="checkStartTime" type="text" readonly="readonly"
											name="accountDate" class="wid140 hasDatepicker">
											<a href="javascript:;"
											onclick="WdatePicker({el:'checkStartTime',isShowClear:true,readOnly:true,dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-{%d-1}'})"
											class="right icon_timer timer_calender margtr36"></a>
									</div>
								</div>
							</div>
							<div class="con_article_title02 margt10 tit_zindex3">
								<a href="javascript:void(0);" class="right chan_btn wid160 margr30 margt5" id="readfile" onclick="doReadfile(this)">读取上游对账文件</a>
								<a href="javascript:void(0);" class="right chan_btn wid160 margr66 margt5" id="download" onclick="doDownloadFile(this)">下载上游对账文件</a> 
							</div>
						</div>

						<div class="bord11 margt10"></div>
					</div>
				</div>
			</div>
		</div>
	</form>
</div>
</body>
</html>