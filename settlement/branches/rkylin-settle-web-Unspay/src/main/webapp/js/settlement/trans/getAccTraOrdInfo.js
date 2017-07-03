/***
 * 
 */
$(function() {
	$("#btnSubmit").click(function() {
		valieQueryTime();	
	});
	$("#btnSubmit2").click(function() {
		valieQueryTime2();	
	});
});
/***
 * 验证日期空间然后获取二期交易
 */
function valieQueryTime() {
	var $checkStartTime = $("#checkStartTime");
	var $checkEndTime = $("#checkEndTime");
	if($checkStartTime && $checkEndTime && $checkStartTime.val() && $checkEndTime.val()) {
		getOrdInfoAndSearch(1);
	} else {
		var $div = $("#valiDiv");
		$div.css("font-style", "italic");
		$div.css("color", "#ff0000");
		$div.html("&nbsp;请输入交易时间再进行读入操作&nbsp;");
	}
}
/***
 * 验证日期空间然后获取一期交易
 */
function valieQueryTime2() {
	var $checkStartTime = $("#checkStartTime");
	var $checkEndTime = $("#checkEndTime");
	if($checkStartTime && $checkEndTime && $checkStartTime.val() && $checkEndTime.val()) {
		getOldOrdInfoAndSearch(1);
	} else {
		var $div = $("#valiDiv");
		$div.css("font-style", "italic");
		$div.css("color", "#ff0000");
		$div.html("&nbsp;请输入交易时间再进行读入操作&nbsp;");
	}
}
/***
 * 
 */
function loadingFun() {
	var butt = document.getElementById("btnSubmit");
	var butt2 = document.getElementById("btnSubmit2");
	butt2.onclick = butt.onclick = function() {};
	butt2.style.width = butt.style.width = "300px";
	butt2.innerHTML = butt.innerHTML = "正在读取... ...";
}
function loadedFun() {
	var butt = document.getElementById("btnSubmit");
	var butt2 = document.getElementById("btnSubmit2");
	butt.onclick = function(){valieQueryTime();};
	butt2.onclick = function(){valieQueryTime2();};
	butt.style.width = "300px";
	butt.innerHTML = "读取【账户系统二期】交易";
	butt2.innerHTML = "读取【账户系统一期】交易";
}
/***
 * ajax调用读取账户 一期 交易的存储过程
 */
function getAccOldTraOrdInfoBySP() {
	if(!window.confirm("您正在执行【获取T-1日所有账户系统 一期 交易信息】，点击【确认】继续!")) {
		return
	}
	$.get(
		$("#getAccOldTraOrdInfoBySP").val(),										//发送请求 url
		null,																	//入参
		function(data) {
			alert(data);
			//showDataTr(data, pageSize, pageIndex, index);
			//loadedFun();
		},
		'text'
	);
}
/***
 * ajax调用读取账户 二期 交易的存储过程
 */
function getAccTraOrdInfoBySP() {
	if(!window.confirm("您正在执行【获取T-1日所有账户系统 二期 交易信息】，点击【确认】继续!")) {
		return
	}
	$.get(
		$("#getAccTraOrdInfoBySP").val(),										//发送请求 url
		null,																	//入参
		function(data) {
			alert(data);
			//showDataTr(data, pageSize, pageIndex, index);
			//loadedFun();
		},
		'text'
	);
}
/***
 * 获取 账户系统一期 数据
 */
function getOldOrdInfoAndSearch(pageIndex, index) {
	loadingFun();
	var pageSize = $("#dataForm #pageSize option:selected").val();		//显示条数
	$('#dataForm #pageIndex').val(pageIndex);							//赋值当前页
	$('#dataForm #pageSize').val(pageSize);								//赋值显示条数
	$.post(
		$('#getAccOldTraOrdInfo').val(),									//发送请求 url
		$('#dataForm').serialize(),										//表单数据
		function(data) {
			showDataTr(data, pageSize, pageIndex, index);
			loadedFun();
		},
		'json'
	);
}
/***
 * 获取 账户系统二期 数据
 */
function getOrdInfoAndSearch(pageIndex, index) {
	loadingFun();
	var pageSize = $("#dataForm #pageSize option:selected").val();		//显示条数
	$('#dataForm #pageIndex').val(pageIndex);							//赋值当前页
	$('#dataForm #pageSize').val(pageSize);								//赋值显示条数
	$.post(
		$('#dataForm').attr("action"),									//发送请求 url
		$('#dataForm').serialize(),										//表单数据
		function(data) {
			showDataTr(data, pageSize, pageIndex, index);
			loadedFun();
		},
		'json'
	);
}
/***
 * ajax获取分页信息和显示数据
 */
function funSearch(pageIndex, index) {
	var pageSize = $("#dataForm #pageSize option:selected").val();		//显示条数
	$('#dataForm #pageIndex').val(pageIndex);							//赋值当前页
	$('#dataForm #pageSize').val(pageSize);								//赋值显示条数
	$.post(
		$('#query_ajax_url').val(),										//发送请求 url
		$('#dataForm').serialize(),										//表单数据
		function(data) {
			showDataTr(data, pageSize, pageIndex, index);
		},
		'json'
	);
}
function showDataTr(data ,pageSize, pageIndex, index) {
	//回调函数
	//jq在页面中生成展示数据
	var tableHead = 
	'<tr >' +
	'<th width="5%">ID</th>' +
	'<th width="13%">账期</th>' +
	'<th width="13%">订单号</th>' +
	'<th width="8%">订单金额</th>' +
	'<th width="5%">订单类型</th>' +
	'<th width="5%">功能编码</th>' +
	'<th width="10%">用户ID</th>' +
	'<th width="5%">商户编码</th>' +
	'<th width="7%">入账金额</th>' +
	'<th width="7%">手续费金额</th>' +
	'<th width="5%">支付渠道</th>' +
	'<th width="5%">数据来源</th>' +
	'<th width="7%">状态</th>' +
	'<th width="5%">操作</th>' +
	'</tr>';
	var trdata = [tableHead];
	if (data.result == "ok") {
		var list = data.dataList;
		if (list == null || list.length == 0) {
			var nodata = "<tr><td colspan='14'>没有符合条件的数据</td></tr>";
			trdata.push(nodata);
			$("#user_table1").html(trdata.join(''));
			drawPage(1, 1, 9);
			return false;
		}
		for (var i = 0; i < list.length; i++) {
			switch(list[i].orderType) {
				case 0: list[i].orderType = "交易"; break;
				case 1: list[i].orderType = "结算单"; break;
				default:list[i].orderType = "暂无记录";
			}
			if("01"==list[i].payChannelId){
				list[i].payChannelId = "通联";
			}else if("02"==list[i].payChannelId){
				list[i].payChannelId = "支付宝";
			}else if("04"==list[i].payChannelId){
				list[i].payChannelId = "连连";
			}else if("05"==list[i].payChannelId){
				list[i].payChannelId = "联动优势";
			}else if("S01"==list[i].payChannelId){
				list[i].payChannelId = "畅捷";
			}
			
			switch(list[i].dataFrom) {
				case 0: list[i].dataFrom = "账户"; break;
				case 1: list[i].dataFrom = "收银台"; break;
				case 2: list[i].dataFrom = "代收付"; break;
			}
			if(0==list[i].statusId){
				list[i].statusId = "【交易】失败";
			}else if(1==list[i].statusId){
				list[i].statusId = "【交易】成功";
			}else if(2==list[i].statusId){
				list[i].statusId = "【交易】不明";
			}else if(10==list[i].statusId){
				list[i].statusId = "【清分】失败";
			}else if(11==list[i].statusId){
				list[i].statusId = "【清分】成功";
			}else if(20==list[i].statusId){
				list[i].statusId = "【对账】异常";
			}else if(21==list[i].statusId){
				list[i].statusId = "【对账】平账";
			}else if(22==list[i].statusId){
				list[i].statusId = "【对账】错帐";
			}else if(24==list[i].statusId){
				list[i].statusId = "【对账】短款";
			}else if(40==list[i].statusId){
				list[i].statusId = "【清算】失败";
			}else if(41==list[i].statusId){
				list[i].statusId = "【清算】成功";
			}
			
			var date = new Date();
			date.setTime(list[i].accountDate);
			list[i].accountDate = date.toLocaleDateString();
			trdata.push('<tr>');
			trdata.push('<td>');
			trdata.push(list[i].transDetailId);
			trdata.push('</td><td>');
			trdata.push(list[i].accountDate);
			trdata.push('</td><td>');
			trdata.push("<label title='"+list[i].orderNo+"'>"+list[i].orderNo+"</label>");
			trdata.push('</td><td>');
			trdata.push(list[i].orderAmount);
			trdata.push('</td><td>');
			trdata.push(list[i].orderType);
			trdata.push('</td><td>');
			trdata.push(list[i].funcCode);
			trdata.push('</td><td>');
			trdata.push(list[i].userId);
			trdata.push('</td><td>');
			trdata.push(list[i].merchantCode);
			trdata.push('</td><td>');
			trdata.push(list[i].amount);
			trdata.push('</td><td>');
			trdata.push(list[i].feeAmount);
			trdata.push('</td><td>');
			trdata.push(list[i].payChannelId);
			trdata.push('</td><td>');
			trdata.push(list[i].dataFrom);
			trdata.push('</td><td>');
			trdata.push(list[i].statusId);
			trdata.push('</td><td>');
			trdata.push('<a href=transdetail/detail_open_view?id='+list[i].transDetailId+'>详情</a>&nbsp;');
			trdata.push('<a href=transdetail/detail_open_edit?id='+list[i].transDetailId+' class="margr4"');
			trdata.push('\')">修改</a>');
			trdata.push('</td>');
			trdata.push('</tr>');
		}
		$("#user_table1").html(trdata.join(''));
		var totalNum = data.totalNum;
		//调用绘制分页的方法
		drawPageAgain(totalNum, pageIndex, pageSize, index);
		$('#div_table_page').show();
	}else{
		var nodata = "<tr><td colspan='14'>没有符合条件的数据</td></tr>";
		trdata.push(nodata);
		$("#user_table1").html(trdata.join(''));
		drawPage(1, 1, 9);
		return false;
	}
}
/***
 * 刷新页面
 */
function resetForm(){
	funSearch(1);
}