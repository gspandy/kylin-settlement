/***
 * 锁按键
 */
function loadingFun(butt) {
	var oldButt = {
		value:butt.innerHTML,
		width:butt.style.width,
		clickFun:butt.onclick
	};
	
	butt.onclick = function() {};
	butt.style.width = "100px";
	butt.innerHTML = "正在操作... ...";
	return oldButt;
}
/***
 * 解锁按键
 */
function successedFun(butt, oldButt) {
	butt.innerHTML = oldButt.value;
	butt.style.width = oldButt.width;
	butt.onclick = oldButt.clickFun;
}
/**
 * 根据汇总规则生成汇总信息
 */
function doCollectByTrans(butt) {
	var collectType = $("#collectType").val();
	var idsStr = getIds();
	
	if(!collectType) {
		art.dialog.tips("<span style='color:red'>请输入 汇总类型!</span>", 2.5, function(){});
		return;
	}
	
	if(!idsStr) {
		art.dialog.tips("<span style='color:red'>请输 选择汇总交易!</span>", 2.5, function(){});
		return;
	}
	
	var oldButt = loadingFun(butt);
	
	var para = "collectType="+collectType+"&idsStr="+idsStr;

	$.ajax({
        type: "POST",
        url: $("#doCollectByTrans").val(),
        data: para,
        dataType: "text",
        success: function(data){
        	art.dialog.tips("<span style='color:green'>操作完成!</span>", 2.5, function(){});
			funSearch(1,"index");
			successedFun(butt, oldButt);
        },
        error:function(data){
        	art.dialog.tips("<span style='color:red'>记会计账系统异常！</span>", 2.5, function(){});
        	successedFun(butt, oldButt);
        }
     });
}
/***
 * onload自动执行
 */
$(function() {
	funSearch(1,"index");
	//将表格标题行固定在浏览器顶部

	//var iframe1 = document.getElementById('b_iframe');
	var messenger = new Messenger('iframe1','projectName');
	messenger.listen(function (msg) {
		console.log(msg);
		fixedTableTitleForTop("user_table1", msg);
	});
});
/***
 * 验证是否选择了交易
 */
function getIds() {
	var idsStr = "";
	if($(".ids_box:checked").length>0) {
		$(".ids_box:checked").each(function() {
			idsStr += "," + $(this).val();
		});
		idsStr = idsStr.substring(1);
		return idsStr;
	} else {
		art.dialog.tips("<span style='color:red'>请选择交易信息!</span>", 2.5, function(){});
		return false;
	}
}
/***
 * 请求冲正
 */
function doCorrect(butt) {
	var idsStr = getIds();
	var isSure = confirmButtAct(butt);
	if(idsStr && isSure) correctAjax(idsStr);
}
/***
 * 请求抹账或者交易后退款
 */
function doAccOfOrRefund(butt) { 
	var idsStr = getIds();
	var isSure = confirmButtAct(butt);
	if(idsStr && isSure) accOfOrRefundAjax(idsStr);
}
/***
 * 清分
 */
function doProfit(butt) {
	var isSure = confirmButtAct(butt);
	if(isSure) profit();
}
/***
 * 分润后结算
 */
function doProfitBalance(butt) {
	var idsStr = getIds();
	var isSure = confirmButtAct(butt);
	if(idsStr && isSure) profitBalance();
}
/***
 * 挂账
 */
function doBill(butt) {
	var idsStr = getIds();
	var isSure = confirmButtAct(butt);
	if(idsStr && isSure){
		var checkFlag = checkIds();
		if(checkFlag) bill();
	}
}
/***
 * 取消挂账
 */
function doCancelBill(butt) {
	var idsStr = getIds();
	var isSure = confirmButtAct(butt);
	if(idsStr && isSure){
		var checkFlag = checkIds("cancel");
		if(checkFlag) cancelBill();
	}
}
/***
 * 冲正ajax发送请求
 * @param idsStr
 */
function correctAjax(idsStr) {
	var buttModel = buttLoadingFun(document.getElementById("correctButt"));
	var url = $('#correctUrl').val();
	$.post(
		url, 
		$('#dataForm').serialize(), 
		function(data) {
			buttLoadedFun(buttModel);
			funSearch(1, "index");
			art.dialog.tips("<span style='color:green'>"+ data +", 请在2分钟后查看冲正结果!</span>", 3.5, function(){});
		}, 
		'text'
	);
}
/***
 * 抹账或交易后退款ajax发送请求
 * @param idsStr
 */
function accOfOrRefundAjax(idsStr) {
	var buttModel = buttLoadingFun(document.getElementById("accOfOrRefundButt"));
	var url = $('#accOfOrRefundUrl').val();
	$.post(
		url, 
		$('#dataForm').serialize(), 
		function(data) {
			buttLoadedFun(buttModel);
			funSearch(1, "index");
			art.dialog.tips("<span style='color:green'>"+ data +", 请在2分钟后查看 抹账或交易后退款  结果!</span>", 3.5, function(){});
		}, 
		'text'
	);
}
/***
 * 清分 ajax请求
 */
function profit() {
	var buttModel = buttLoadingFun(document.getElementById("profitButt"));
	var url = $("#profit").val();
	var data = null;
	var callBack = function(data) {
		buttLoadedFun(buttModel);
		if(data == 'success') {
			art.dialog.tips("<span style='color:green'>清分操作执行成功!</span>", 2.5, function(){});
		} else {
			art.dialog.tips("<span style='color:red'>清分操作执行失败!</span>", 2.5, function(){});
		}
	};
	var type = "text";
	$.get(url, data, callBack, type);
}
/***
 * 清分结算 ajax请求
 */
function profitBalance() {
	var url = $("#profitBalance").val();
	var data = $('#dataForm').serialize();
	var callBack = function(data) {
		art.dialog.tips("<span style='color:green'>"+ data +"</span>", 2.5, function(){});
	};
	var type = "text";
	$.post(url, data, callBack, type);
}
/***
 * 验证选中的交易是否允许挂账
 */
function checkIds(operation) {
	var flag = true;
	$(".ids_box:checked").each(function() {
		var msg = $(this).val();
		var statusId = $(this).next("input[type='hidden']").val();
		if((operation=="cancel" && "99"!=statusId)){
			art.dialog.tips("<span style='color:red'>ID为"+msg+"的交易,不允许此操作!</span>", 2.5, function(){});
			flag = false;
			return;
		}
	});
	return flag;
}
/***
 * 挂账 ajax请求
 */
function bill() {
	var buttModel = buttLoadingFun(document.getElementById("billButt"));
	var url = $("#bill").val();
	var data = $('#dataForm').serialize();
	var callBack = function(data) {
		buttLoadedFun(buttModel);
		if(data.result == 'success') {
			art.dialog.tips("<span style='color:green'>挂账操作执行成功!</span>", 2.5, function(){});
		} else {
			
			art.dialog.tips("<span style='color:red'>"+data.msg+"</span>", 2.5, function(){});
		}
		resetForm();
	};
	var type = "json";
	$.get(url, data, callBack, type);
}
/***
 * 取消挂账 ajax请求
 */
function cancelBill() {
	var url = $("#cancelBill").val();
	var data = $('#dataForm').serialize();
	var callBack = function(data) {
		if(data.result == 'success') {
			art.dialog.tips("<span style='color:green'>取消挂账操作执行成功!</span>", 2.5, function(){});
		} else {
			art.dialog.tips("<span style='color:red'>"+data.msg+"</span>", 2.5, function(){});
		}
		resetForm();
	};
	var type = "json";
	$.post(url, data, callBack, type);
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
		function(data) {												//回调函数
			//jq在页面中生成展示数据
			var tableHead = 
			'<tr class="tb-head-no" id="head-no" >' +
			'<th >ID</th>' +
			'<th >交易请求时间</th>' +
			'<th >订单号</th>' +
			'<th >订单金额</th>' +
			'<th >订单类型</th>' +
			'<th >功能编码</th>' +
			'<th >用户ID</th>' +
			'<th >商户编码</th>' +
			'<th >入账金额</th>' +
			'<th >手续费金额</th>' +
			'<th >支付渠道</th>' +
			'<th >数据来源</th>' +
			'<th >状态</th>' +
			'<th >操作</th>' +
			'</tr>';
			var trdata = [tableHead];
			if (data.result == "ok") {
				var list = data.dataList;
				if (list == null || list.length == 0) {
					var nodata = "<tr ><td colspan='14'>没有符合条件的数据</td></tr>";
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
					var statusId = list[i].statusId;
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
					}else if(99==list[i].statusId){
						list[i].statusId = "挂账中";
					}
					
					var date = new Date();
					date.setTime(list[i].requestTime);
					list[i].requestTime = date.toLocaleString();
					trdata.push('<tr onclick="selectCheck(this)" >');
					trdata.push('<td>');
					trdata.push('<input type="checkbox" class="ids_box" value="'+list[i].transDetailId+'" name="ids" />');
					trdata.push('<input type="hidden" value="'+statusId+'" name="statusIds" />');
					trdata.push(list[i].transDetailId);
					trdata.push('</td><td>');
					trdata.push(list[i].requestTime);
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
					trdata.push(list[i].userFee);
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
		},
		'json'
	);

}
/***
 * 刷新页面
 */
function resetForm(){
	funSearch(1);
}



function selectCheck(obj){
	var checkbox = $(obj).find("td").eq(0).find("input");
	if(checkbox.is(':checked')){
		checkbox.prop("checked", false);
		$(obj).find("td").removeClass("tb_style");
	}else{
		checkbox.prop("checked",true);

		$(obj).find("td").addClass("tb_style");
	}


}


