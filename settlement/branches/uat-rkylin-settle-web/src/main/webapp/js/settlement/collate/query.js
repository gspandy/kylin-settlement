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
	/**
	 * payChannelId 下拉菜单和 accountType菜单二级联动
	 */
	payChannelIdAndSelect("payChannelId", "accountType");
	
	$("#otherEnvironBtn").click(function(){
		var bool = true;
		if($("#otherEnvironAccountDate").val() == ""){
			$("#otherEnvironAccountDate").parent().css('border','2px solid #FF4500');
			bool = false;
		}else{
			$("#otherEnvironAccountDate").parent().css('border','1px solid #d5d6ce');
		}
		if($("#otherEnviron").val() == "0"){
			$("#otherEnviron").parent().css('border','2px solid #FF4500');
			bool = false;
		}else{
			$("#otherEnviron").parent().css('border','1px solid #d5d6ce');
		}
		if(bool && window.confirm("是否开始与其他环境对账？")){
			$.post(
				CONTEXT_PATH + 'collate/oeBalanceAccount_ajax',					//发送请求 url
				{date:$("#otherEnvironAccountDate").val(),
					otherEnviron:$("#otherEnviron").val(),
					rule:$("#oeRule").val()},
				function(data) {
					art.dialog.tips("<span style='color:green'>对账完成！</span>", 2.5, function(){});
				},
				'json'
			);
		}
	});
	
	var today = new Date(); 
	var time = new Date(today.getTime() - 24*60*60*1000).dateFormat("yyyy-MM-dd");
	$("#otherEnvironAccountDate").val(time+" 00:00:00");
});
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
/***
 * 
 */
function loadingFun() {
	var butt = document.getElementById("btnSubmit");
	butt.onclick = function() {};
	butt.style.width = "100px";
	butt.innerHTML = "正在对账... ...";
}
function successedFun() {
	var butt = document.getElementById("btnSubmit");
	butt.style.width = "60px";
	butt.innerHTML = "对账";
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
		$('#collate_ajax').val(),									//发送请求 url
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
		$('#dataForm').attr("action"),										//发送请求 url
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
		for (var i = 0; i < list.length; i++) {
			switch(list[i].balanceType) {
				case 0: list[i].balanceType = "账户"; break;
				case 1: list[i].balanceType = "收银台"; break;
				case 2: list[i].balanceType = "代收付"; break;
				case 3: list[i].balanceType = "多渠道"; break;
				case 4: list[i].balanceType = "分润结果"; break;
				default:list[i].balanceType = "暂无记录";
			}
			if(0==list[i].statusId){
				list[i].statusId = "错账";
			}else if(1==list[i].statusId){
				list[i].statusId = "平账";
			}else if(2==list[i].statusId){
				list[i].statusId = "长款";
			}else if(3==list[i].statusId){
				list[i].statusId = "短款";
			}else if(10==list[i].statusId){
				list[i].statusId = "异常";
			}else if(11==list[i].statusId){
				list[i].statusId = "成功";
			}
			
			var date = new Date();
			date.setTime(list[i].accountDate);
			list[i].accountDate = date.toLocaleDateString();
			trdata.push('<tr>');
			trdata.push('<td>');
			trdata.push(list[i].balanceEntryId);
			trdata.push('</td><td>');
			trdata.push(list[i].orderNo);
			trdata.push('</td><td>');
			trdata.push(list[i].balanceType);
			trdata.push('</td><td title="'+list[i].obligate1+'">');
			trdata.push(list[i].obligate1);
			trdata.push('</td><td>');
			trdata.push(list[i].statusId);
			trdata.push('</td><td>');
			trdata.push(list[i].accountDate);
			trdata.push('</td><td title="'+list[i].remark+'">');
			trdata.push(list[i].remark);
			trdata.push('</td>');
			trdata.push('</tr>');
		}
		$("#user_table1").html(trdata.join(''));
		var totalNum = data.totalNum;
		//调用绘制分页的方法
		drawPageAgain(totalNum, pageIndex, pageSize, index);
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

/**格式化Date对象*/
Date.prototype.dateFormat = function (fmt) { //author: meizz 
    var o = {
        "M+": this.getMonth() + 1, //月份 
        "d+": this.getDate(), //日 
        "h+": this.getHours(), //小时 
        "m+": this.getMinutes(), //分 
        "s+": this.getSeconds(), //秒 
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
        "S": this.getMilliseconds() //毫秒 
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}
