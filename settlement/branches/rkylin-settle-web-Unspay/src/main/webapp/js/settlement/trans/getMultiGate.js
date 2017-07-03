/***
 * 
 */
$(function() {
	$("#btnSubmit").click(function() {
		valieQueryTime();	
	});
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
	};
});
/***
 * 
 */
function valieQueryTime() {
	var $accountDate = $("#accountDate");
	if($accountDate && $accountDate.val()) {
		getOrdInfoAndSearch(1);
	} else {
		var $div = $("#valiDiv");
		$div.css("font-style", "italic");
		$div.css("color", "#ff0000");
		$div.html("&nbsp;请输入账期再进行读入操作&nbsp;");
	}
}
/***
 * 
 */
function loadingFun() {
	var butt = document.getElementById("btnSubmit");
	butt.onclick = function() {};
	butt.style.width = "100px";
	butt.innerHTML = "正在读取... ...";
}
function successedFun() {
	var butt = document.getElementById("btnSubmit");
	butt.onclick = function(){funSearch(1);};
	butt.style.width = "60px";
	butt.innerHTML = "读取交易";
}
/***
 * ajax获取分页信息和显示数据
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
			successedFun();
			showDataTr(data, pageSize, pageIndex, index);
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
	art.dialog.tip1s(data.msg,2);
	//回调函数
	//jq在页面中生成展示数据
	var tableHead = "<tr>"+$("#user_table1 tr").eq(0).html()+"</tr>";
	var trdata = [tableHead];
	
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
			case 3: list[i].dataFrom = "多渠道"; break;
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
}
/***
 * 刷新页面
 */
function resetForm(){
	funSearch(1);
}
