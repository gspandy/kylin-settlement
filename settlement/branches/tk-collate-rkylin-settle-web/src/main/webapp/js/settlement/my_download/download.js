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
	valieQueryTime();
});
/***
 * 
 */
function valieQueryTime() {
	var $accountDate = $("#downloadDate");
	if($accountDate && $accountDate.val()) {
		getOrdInfoAndSearch(1);
	} else {
		var $div = $("#valiDiv");
		$div.css("font-style", "italic");
		$div.css("color", "#ff0000");
		$div.html("&nbsp;请输入账期再进行对账操作&nbsp;");
	}
}
/***
 * 
 */
function loadingFun() {
	var butt = document.getElementById("btnSubmit");
	butt.onclick = function() {};
	butt.style.width = "100px";
	butt.innerHTML = "正在查询... ...";
}
function successedFun() {
	var butt = document.getElementById("btnSubmit");
	butt.style.width = "60px";
	butt.innerHTML = "查询";
}
/***
 * ajax获取分页信息和显示数据
 */
function getOrdInfoAndSearch(pageIndex, index) {
	loadingFun();	
	$.post(
		$('#download_collate_ajax').val(),								//发送请求 url
		$('#dataForm').serialize(),										//表单数据
		function(data) {
			successedFun();
			showDataTr(data);
		},
		'json'
	);
}
function showDataTr(data ,pageSize, pageIndex, index) {
	//回调函数
	//jq在页面中生成展示数据
	var tableHead = "<tr>"+$("#user_table1 tr").eq(0).html()+"</tr>";
	var trdata = [tableHead];
	if (data.result == "ok") {
		var list = data.dataList;
		if (list == null || list.length == 0) {
			var nodata = "<tr><td colspan='7'>没有符合条件的数据</td></tr>";
			trdata.push(nodata);
			$("#user_table1").html(trdata.join(''));
			drawPage(1, 1, 9);
			return false;
		}
		
		var date = new Date();
		for (var i = 0; i < list.length; i++) {
			var file = list[i];
			var name = file.name;
			var lastModified = file.lastModified;
			date.setTime(Number(lastModified));
			trdata.push('<tr>');
			trdata.push('<td>');
			trdata.push(i + 1);
			trdata.push('</td><td>');
			
			if(name.indexOf("文件生成中") > -1) {
				trdata.push("<strong>" + name + "</strong>&nbsp;&nbsp;<img style='width:22px;height:22px' src='./././images/loading.gif'>");
			} else {
				trdata.push("<a href='" + document.getElementById("root_path").value + "/collate/download_collatefile2merchant?filePath="+ data.filePath +"&filename=" + name + "'>" + name + "</a>");
				
			}
			trdata.push('</td><td>');
			trdata.push(date.toLocaleString());
			trdata.push('</td>');
			trdata.push('</tr>');
		}
		$("#user_table1").html(trdata.join(''));
		$('#div_table_page').show();
	}else{
		var nodata = "<tr><td colspan='7'>没有符合条件的数据</td></tr>";
		trdata.push(nodata);
		$("#user_table1").html(trdata.join(''));
		drawPage(1, 1, 9);
		art.dialog.tip1s(data.msg,2);
		return false;
	}
}
