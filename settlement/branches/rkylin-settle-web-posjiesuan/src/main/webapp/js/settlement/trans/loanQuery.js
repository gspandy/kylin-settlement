/***
 * onload自动执行
 */
$(function() {
	funSearch(1,"index");
	fixedTableTitleForTop("user_table1");
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
function doSettleInvoice(butt) {
	var isSure = confirmButtAct(butt);
	if(isSure) correctAjax();
}
/***
 * 发送代付交易
 */
function doInvoiceSettle(butt) { 
	var isSure = confirmButtAct(butt);
	if(isSure) accOfOrRefundAjax();
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
	var isSure = confirmButtAct(butt);
	if(isSure) profitBalance();
}
/***
 * 挂账
 */
function doBill(butt) {
	var isSure = confirmButtAct(butt);
	if(isSure){
		bill();
	}
}
/***
 * 取消挂账
 */
function doInvoiceCash(butt) {
	var isSure = confirmButtAct(butt);
	if(isSure){ cancelBill();
	}
}
/***
 * 冲正ajax发送请求
 * @param idsStr
 */
function correctAjax() {
	var buttModel = buttLoadingFun(document.getElementById("correctButt"));
	var url = $('#correctUrl').val();
	$.post(
		url, 
		null, 
		function(data) {
			buttLoadedFun(buttModel);
			art.dialog.tips("<span style='color:green'>"+ data +", 请在2分钟后查看冲正结果!</span>", 3.5, function(){});
		}, 
		'text'
	);
}
/***
 * 发送代付交易ajax发送请求
 * @param idsStr
 */
function accOfOrRefundAjax() {
	var buttModel = buttLoadingFun(document.getElementById("accOfOrRefundButt"));
	var url = $('#accOfOrRefundUrl').val();
	$.post(
		url, 
		null, 
		function(data) {
			buttLoadedFun(buttModel);
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
	var data = null;
	var callBack = function(data) {
		if(data == 'success') {
			art.dialog.tips("<span style='color:green'>【分润】结算操作执行成功!</span>", 2.5, function(){});
		} else {
			art.dialog.tips("<span style='color:red'>【分润】结算操作执行失败!</span>", 2.5, function(){});
		}
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
	var url = $("#bill").val();
	var data = null;
	var callBack = function(data) {
		if(data.result == 'success') {
			art.dialog.tips("<span style='color:green'>读取操作执行成功!</span>", 2.5, function(){});
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
	var data = null;
	var callBack = function(data) {
		if(data.result == 'success') {
			art.dialog.tips("<span style='color:green'>执行成功!</span>", 2.5, function(){});
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
			'<th >产品号</th>' +
			'<th >融数机构代码</th>' +
			'<th >学生ID</th>' +
			'<th >保理ID</th>' +
			'<th >培训机构ID</th>' +
			'<th >代理商ID</th>' +
			'<th >利息承担方ID</th>' +
			'<th >利息承担方ID2</th>' +
			'<th >利息承担方ID3</th>' +
			'<th >费率模板ID</th>' +
			'<th >贷款订单号</th>' +
			'<th >首付标识</th>' +
			'<th >还款期数</th>' +
			'<th >还款结果</th>' +
			'<th >还款时间</th>' +
			'<th >逾期标识</th>' +
			'<th >提前还款标识</th>' +
			'<th >退课标识</th>' +
			'<th >日期标识</th>' +
			'<th >还款金额</th>' +
			'<th >特殊标识1</th>' +
			'<th >特殊标识2</th>' +
			'<th >特殊标识3</th>' +
			'<th >预留1</th>' +
			'<th >预留2</th>' +
			'<th >预留3</th>' +
			'<th >预留4</th>' +
			'<th >预留5</th>' +
			'<th >预留6</th>' +
			'<th >预留7</th>' +
			'<th >预留8</th>' +
			'<th >预留9</th>' +
			'<th >预留10</th>' +
			'<th >预留11</th>' +
			'<th >预留12</th>' +
			'<th >清算用KEY</th>' +
			'<th >状态</th>' +
			'<th >失败原因</th>' +
			'<th >返回信息</th>' +
			'<th >备注</th>' +
			'<th >创建时间</th>' +
			'<th >更新时间</th>' +
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
					switch(list[i].onePaymentFlg) {
						case 1: list[i].onePaymentFlg = "首付"; break;
						case 0: list[i].onePaymentFlg = "非首付"; break;
						default:list[i].onePaymentFlg = "其他";
					}
					switch(list[i].repaymentResult) {
						case "0": list[i].repaymentResult = "失败"; break;
						case "1": list[i].repaymentResult = "成功"; break;
						default: list[i].repaymentResult = "无结果";
					}
					switch(list[i].overdueFlg) {
					case "0": list[i].overdueFlg = "未逾期"; break;
					case "1": list[i].overdueFlg = "逾期"; break;
					default: list[i].overdueFlg = "其他";
					}
					switch(list[i].prepaymentFlg) {
					case "1": list[i].prepaymentFlg = "提前还款"; break;
					default: list[i].prepaymentFlg = "其他";
					}
					switch(list[i].dropClassFlg) {
					case "1": list[i].dropClassFlg = "退课"; break;
					default: list[i].dropClassFlg = "其他";
					}
					var statusId = list[i].statusId;
					if(0==list[i].statusId){
						list[i].statusId = "【交易】失败";
					}else if(1==list[i].statusId){
						list[i].statusId = "【交易】成功";
					}else if(10==list[i].statusId){
						list[i].statusId = "【清分】失败";
					}else if(11==list[i].statusId){
						list[i].statusId = "【清分】成功";
					}else if(2==list[i].statusId){
						list[i].statusId = "【交易】暂停";
					}
					
					var date = new Date();
					date.setTime(list[i].createdTime);
					var createdTime = date.toLocaleString();
					date.setTime(list[i].updatedTime);
					var updatedTime = date.toLocaleString();
					date.setTime(list[i].repaymentDate);
					var repaymentDate = date.toLocaleString();
					
					trdata.push('<tr onclick="selectCheck(this)" >');
					trdata.push('<td>');
					trdata.push('<input type="checkbox" class="ids_box" value="'+list[i].loanId+'" name="ids" />');
					trdata.push('<input type="hidden" value="'+statusId+'" name="statusIds" />');
					trdata.push(list[i].loanId);
					trdata.push('</td><td>');
					trdata.push(list[i].productId);
					trdata.push('</td><td>');
					trdata.push(list[i].rootInstCd);
					trdata.push('</td><td>');
					trdata.push(list[i].userId);
					trdata.push('</td><td>');
					trdata.push(list[i].factorId);
					trdata.push('</td><td>');
					trdata.push(list[i].outfitId);
					trdata.push('</td><td>');
					trdata.push(list[i].agentId);
					trdata.push('</td><td>');
					trdata.push(list[i].intrestPartyId);
					trdata.push('</td><td>');
					trdata.push(list[i].intrestPartyId2);
					trdata.push('</td><td>');
					trdata.push(list[i].intrestPartyId3);
					trdata.push('</td><td>');
					trdata.push(list[i].rateId);
					trdata.push('</td><td>');
					trdata.push(list[i].loanOrderNo);
					trdata.push('</td><td>');
					trdata.push(list[i].onePaymentFlg);
					trdata.push('</td><td>');
					trdata.push(list[i].repaymentCnt);
					trdata.push('</td><td>');
					trdata.push(list[i].repaymentResult);
					trdata.push('</td><td>');
					trdata.push(repaymentDate);
					trdata.push('</td><td>');
					trdata.push(list[i].overdueFlg);
					trdata.push('</td><td>');
					trdata.push(list[i].prepaymentFlg);
					trdata.push('</td><td>');
					trdata.push(list[i].dropClassFlg);
					trdata.push('</td><td>');
					trdata.push(list[i].dateFlg);
					trdata.push('</td><td>');
					trdata.push(list[i].amount);
					trdata.push('</td><td>');
					trdata.push(list[i].specialFlg1);
					trdata.push('</td><td>');
					trdata.push(list[i].specialFlg2);
					trdata.push('</td><td>');
					trdata.push(list[i].specialFlg3);
					trdata.push('</td><td>');
					trdata.push(list[i].obligate1);
					trdata.push('</td><td>');
					trdata.push(list[i].obligate2);
					trdata.push('</td><td>');
					trdata.push(list[i].obligate3);
					trdata.push('</td><td>');
					trdata.push(list[i].obligate4);
					trdata.push('</td><td>');
					trdata.push(list[i].obligate5);
					trdata.push('</td><td>');
					trdata.push(list[i].obligate6);
					trdata.push('</td><td>');
					trdata.push(list[i].obligate7);
					trdata.push('</td><td>');
					trdata.push(list[i].obligate8);
					trdata.push('</td><td>');
					trdata.push(list[i].obligate9);
					trdata.push('</td><td>');
					trdata.push(list[i].obligate10);
					trdata.push('</td><td>');
					trdata.push(list[i].obligate11);
					trdata.push('</td><td>');
					trdata.push(list[i].obligate12);
					trdata.push('</td><td>');
					trdata.push(list[i].settleFlg);
					trdata.push('</td><td>');
					trdata.push(list[i].statusId);
					trdata.push('</td><td>');
					trdata.push(list[i].errorMsg);
					trdata.push('</td><td>');
					trdata.push(list[i].returnMsg);
					trdata.push('</td><td>');
					trdata.push(list[i].remark);
					trdata.push('</td><td>');
					
					trdata.push(createdTime);
					trdata.push('</td><td>');
					trdata.push(updatedTime);
					trdata.push('</td><td>');
					trdata.push('<a href=loandetail/detail_open_view?id='+list[i].loanId+'>详情</a>&nbsp;');
					trdata.push('<a href=loandetail/detail_open_edit?id='+list[i].loanId+' class="margr4"');
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
				var nodata = "<tr><td colspan='43'>没有符合条件的数据</td></tr>";
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
		checkbox.prop("checked", false)
		$(obj).find("td").removeClass("tb_style");
	}else{
		checkbox.prop("checked",true);

		$(obj).find("td").addClass("tb_style");
	}


}


