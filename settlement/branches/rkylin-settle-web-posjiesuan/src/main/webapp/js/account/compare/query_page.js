/***
 * onload自动执行
 */
$(function() {
	queryPayChannelIdAndName();
	queryProtocal();
	funSearch(1);
	fixedTableTitleForTop("user_table1");
	$("#searchButt").click(function() {
		funSearch(1);
	});
});

/**
 * 获取支付渠道
 */
function queryPayChannelIdAndName(){
	var url = $('#getPayChannelIdAndName').val();
	$.getJSON(
		url, 
		"", 
		function(data) {
			if(data !="" && data != null){
				var arr = data.dataList;
				var htmlSelect ="<option value=''>全部</option>";
				for(var i in arr){
					htmlSelect += "<option value="+arr[i].parameterCode+">"+arr[i].obligate3+"</option>";
				}
				$("#payChannelId").html(htmlSelect);
			}
		}
	);
}
/**
 * 获取协议
 */
function queryProtocal(){
	var url = $('#getProtocol').val();
	$.getJSON(
		url, 
		"", 
		function(data) {
			if(data !="" && data != null){
				var arr = data.dataList;
				var htmlSelect ="<option value=''>全部</option>";
				for(var i in arr){
					htmlSelect += "<option value="+arr[i].parameterCode+">"+arr[i].obligate3+"</option>";
				}
				$("#merchantCode").html(htmlSelect);
			}
		}
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
		$('#dataForm').attr("action"),									//发送请求 url
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
	'<thead><tr class="tb-head-no" id="head-no">' +
	'<th >ID</th>' +
	'<th >账期</th>' +
	'<th >渠道</th>' +
	'<th >协议</th>' +
	'<th >支付方式</th>' +
	'<th >退款笔数</th>' +
	'<th >退款金额</th>' +
	'<th >支付笔数</th>' +
	'<th >支付金额</th>'+
	'<th >渠道结算额</th>'+
	'<th >手续费</th>'+
	'<th >对账结果</th>'+
	'<th >差额</th>'+
	'<th >备注</th>'+
	'<th >状态</th>'+
	'<th >创建日期</th>'+
	'<th >修改日期</th>'+
	'</tr></thead>';
	var trdata = [tableHead];
	if (data.result == "ok") {
		var list = data.dataList;
		if (list == null || list.length == 0) {
			var nodata = "<tr><td colspan='28'>没有符合条件的数据</td></tr>";
			trdata.push(nodata);
			$("#user_table1").html(trdata.join(''));
			drawPage(1, 1, 9);
			return false;
		}
		var date = new Date();
		
		trdata.push('<tbody>');
		for (var i = 0; i < list.length; i++) {
			trdata.push('<tr>');
			trdata.push('<td>');
			trdata.push(list[i].id);
			trdata.push('</td><td>');
			date.setTime(list[i].accountDate);
			trdata.push(date.toLocaleDateString());
			trdata.push('</td><td>');
			trdata.push(list[i].payChannelId);
			trdata.push('</td><td>');
			trdata.push(list[i].merchantCode);
			trdata.push('</td><td>');
			trdata.push(list[i].readType);
			trdata.push('</td><td>');
			trdata.push(list[i].count1);
			trdata.push('</td><td>');
			trdata.push(!isNaN(list[i].amount1)?fmoney(Number(list[i].amount1) / 100, 2):"");
			trdata.push('</td><td>');
			trdata.push(list[i].count2);
			trdata.push('</td><td>');
			trdata.push(!isNaN(list[i].amount2)?fmoney(Number(list[i].amount2) / 100, 2):"");
			trdata.push('</td><td>');
			trdata.push(!isNaN(list[i].amount3)?fmoney(Number(list[i].amount3) / 100, 2):"");
			trdata.push('</td><td>');
			trdata.push(!isNaN(list[i].feeAmount)?fmoney(Number(list[i].feeAmount) / 100, 2):"");
			trdata.push('</td><td>');
			trdata.push(list[i].accountResult);
			trdata.push('</td><td>');
			trdata.push(!isNaN(list[i].amount4)?fmoney(Number(list[i].amount4) / 100, 2):"");
			trdata.push('</td><td>');
			trdata.push(list[i].remark);
			trdata.push('</td><td>');
			trdata.push(list[i].statusId);
			trdata.push('</td><td>');
			date.setTime(list[i].createdTime);
			trdata.push(date.toLocaleString());
			trdata.push('</td><td>');
			date.setTime(list[i].updatedTime);
			trdata.push(date.toLocaleString());
			trdata.push('</td>');
			trdata.push('</tr>');
		}
		trdata.push('</tbody>');
		$("#user_table1").html(trdata.join(''));
		var totalNum = data.totalNum;
		//调用绘制分页的方法
		drawPageAgain(totalNum, pageIndex, pageSize, index);
		$('#div_table_page').show();
	}else{
		var nodata = "<tr><td colspan='28'>没有符合条件的数据</td></tr>";
		trdata.push(nodata);
		$("#user_table1").html(trdata.join(''));
		drawPage(1, 1, 9);
		return false;
	}
}
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
		$div.html("&nbsp;请输入账期再进行对账操作&nbsp;");
	}
}
/**
 * 全部到处excl
 */
function doAllExport() {
	if(!confirm("您确定要 导出全部数据到excl文件吗?")) return;
	setTimeout(function() {
		var win = window.open($("#my_download_a").attr("href"));
		$.post(
			$('#doAllExport').val(),	//发送请求 url
			$('#dataForm').serialize(),	//表单数据
			function(data) {
				alert(data);
				win && win.close();
				window.open($("#my_download_a").attr("href"));
			},
			'text');
	 }, 1000);
}
/**
 * 金额格式
 * @param s 金额(元)
 * @param n 保留n位小数
 * @returns {String}
 */
function fmoney(s, n) {   
   n = n > 0 && n <= 20 ? n : 2;   
   s = parseFloat((s + "").replace(/[^\d\.-]/g, "")).toFixed(n) + "";   
   var l = s.split(".")[0].split("").reverse(),   
   r = s.split(".")[1];   
   t = "";   
   for(var i = 0; i < l.length; i ++ )   
   {   
      t += l[i] + ((i + 1) % 3 == 0 && (i + 1) != l.length ? "," : "");   
   }   
   return t.split("").reverse().join("") + "." + r;   
}