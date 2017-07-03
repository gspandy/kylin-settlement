/***
 * onload自动执行
 */
$(function() {
	funSearch(1,"index");
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
 * caoyang挂账
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
 * caoyang取消挂账
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

		if((operation=="cancel" && ("挂账"!=statusId && "98"!=statusId))){
			art.dialog.tips("<span style='color:red'>ID为"+msg+"的交易,不允许此操作!</span>", 2.5, function(){});
			flag = false;
			return;
		}
	});
	return flag;
}
/***
 * caoyang 挂账 ajax请求
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
 * caoyang 取消挂账 ajax请求
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

function sendDsf(){
	 var totalChecked = $("#totalChecked").val();//手工输入的总条数
	 var totalAmount = $("#totalAmount").val();//手工输入的总金额
	
	var count =0;//选中的总条数
	var ids = "";
    $('input:checkbox[name=ids]:checked').each(function(){
    	count++;
        ids += parseInt($(this).val()) +",";
    });
    
    //如果选中的条数和输入的条数不匹配，弹出提示信息，不在走下边的逻辑
    if(totalChecked !=count){
    	art.dialog.tips("<span style='color:red'>条数不配,选中的是:"+count+"(条),  输入的是:"+totalChecked+"(条)</span>", 2.5, function(){});
    	return false;
    }
    
    ids = ids.substring(0,ids.length-1);
    var url = $("#queryTotalAmount").val();
	var data =  { "ids": ids };
	var callBack = function(data) {
		 //如果选中的金额和输入的金额不匹配，弹出提示信息
	    if(data != totalAmount){
	    	art.dialog.tips("<span style='color:red'>金额不匹配,选中的金额是:"+data+"(分),   输入的是:"+totalAmount+"(分)</span>", 3, function(){});
	        return false;
	    }
	    
	    //ajax请求后台，将数据
	    url = $("#sendDsf").val();
	    data = { "ids": ids };
		var callBack2 = function(data) {
		    if(data == "success"){
		    	art.dialog.tips("<span style='color:red'>选中项已发送代收付系统！</span>", 3, function(){});
		    }
		};
		$.post(url, data, callBack2, type);
	    
	    
	};
	var type = "text";
	$.post(url, data, callBack, type);
    
}

function writeResult(){
	var ids = "";
	var count =0;//选中的总条数
    $('input:checkbox[name=ids]:checked').each(function(){
    	count++;
        ids += parseInt($(this).val()) +",";
    });
    
    ids = ids.substring(0,ids.length-1);
    
    //ajax请求后台，将数据
    url = $("#writeResultUrl").val();
    data = { "ids": ids };
	var callBack2 = function(data) {
	    if(data == "success"){
	    	art.dialog.tips("<span style='color:red'>选中项已推送给订单系统！</span>", 3, function(){});
	    }
	};
	var type = "text";
	$.post(url, data, callBack2, type);
}

/***
 * ajax获取分页信息和显示数据
 */
function funSearch(pageIndex, index) {
	var pageSize = $("#dataForm #pageSize option:selected").val();		//显示条数
	$('#dataForm #pageIndex').val(pageIndex);							//赋值当前页
	$('#dataForm #pageSize').val(pageSize);								//赋值显示条数
	var pathHead = $("#pathHead").val();
	var excelQueryPath = pathHead+"/invoice/import_excel_query";
	$.post(
		$('#dataForm').attr("action"),									//发送请求 url
		$('#dataForm').serialize(),										//表单数据
		function(data) {												//回调函数
			//jq在页面中生成展示数据
			var tableHead = 
			'<tr class="tb-head-no" id="head-no">' +
			'<th width="5%">ID</th>' +
			'<th width="13%">交易批次号</th>' +
			'<th width="5%">业务代码</th>' +
			'<th width="8%">机构号</th>' +
			'<th width="5%">功能</th>' +
			'<th width="13%">批次号</th>' +
			'<th width="10%">用户ID</th>' +
			'<th width="5%">处理结果</th>' +
			'<th width="11%">账号</th>' +
			'<th width="7%">金额</th>' +
			'<th width="3%">代收付状态</th>' +
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
					var statusId = list[i].statusId;
					if(0==statusId){
						list[i].statusId = "未处理";
					}else if(1==statusId){
						list[i].statusId = "发送成功";
					}else if(2==statusId){
						list[i].statusId = "发送失败";
					}else if(5==statusId){
						list[i].statusId = "已经返回";
					}else if(98==statusId){
						list[i].statusId = "挂账";
					}else if(99==statusId){
						list[i].statusId = "异常";
					}
					
					var funcCode = list[i].funcCode;
					if("4013"==funcCode){
						list[i].funcCode = "代收";
					}else if("4014"==funcCode){
						list[i].funcCode = "代付";
					}else if("4016"==funcCode){
						list[i].funcCode = "提现";
					}else if("4014_1"==funcCode){
						list[i].funcCode = "一分钱代付";
					}
					
					var date = new Date();
					date.setTime(list[i].requestTime);
					list[i].requestTime = date.toLocaleString();
					trdata.push('<tr onclick="selectCheck(this)" >');
					trdata.push('<td>');
					trdata.push('<input type="checkbox" class="ids_box" value="'+list[i].invoiceNo+'" name="ids" />');
					trdata.push('<input type="hidden" value="'+ list[i].statusId +'" name="statusId" />');
					trdata.push(list[i].invoiceNo);
					trdata.push('</td><td>');
					trdata.push(list[i].requestNo);
					trdata.push('</td><td>');
					trdata.push("<label title='"+list[i].bussinessCode+"'>"+list[i].bussinessCode+"</label>");
					trdata.push('</td><td>');
					trdata.push(list[i].rootInstCd);
					trdata.push('</td><td>');
					trdata.push(list[i].funcCode);
					trdata.push('</td><td>');
					trdata.push(list[i].batchNo);
					trdata.push('</td><td>');
					trdata.push(list[i].userId);
					trdata.push('</td><td>');
					trdata.push(list[i].processResult);
					trdata.push('</td><td>');
					trdata.push(list[i].accountNo);
					trdata.push('</td><td>');
					trdata.push(list[i].amount);
					trdata.push('</td><td>');
					trdata.push(list[i].statusId);
					trdata.push('</td><td>');
					trdata.push('<a href=invoice/invoice_open_view?id='+list[i].invoiceNo+'>详情</a>&nbsp;');
					trdata.push('<a href=invoice/invoice_open_edit?id='+list[i].invoiceNo+' class="margr4"');
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
		checkbox.prop("checked", false)
		$(obj).find("td").removeClass("tb_style");
	}else{
		checkbox.prop("checked",true);

		$(obj).find("td").addClass("tb_style");
	}


}