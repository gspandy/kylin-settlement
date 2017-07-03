/***
 * 
 */
$(function() {
	funSearch(1,"index");
});
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
			'<tr class="tb-head-no">' +
			'<th width="15%">ID</th>' +
			'<th width="5%">账期</th>' +
			'<th width="5%">交易日期</th>' +
			'<th width="5%">交易流水ID</th>' +
			'<th width="5%">基础功能码</th>' +
			'<th width="5%">账户ID1</th>' +
			'<th width="5%">发生额1</th>' +
			'<th width="5%">账户ID2</th>' +
			'<th width="5%">发生额2</th>' +
			'<th width="5%">T+n</th>' +
			'<th width="5%">结算日</th>' +
			'<th width="5%">结算单号</th>' +
			'<th width="5%">备注2</th>' +
			'<th width="5%">清结算状态ID</th>' +
			'<th width="5%">会计系统状态ID</th>' +
			'<th width="5%">操作</th>' +
			'</tr>';
			var trdata = [tableHead];
			
			if (data.result == "ok") {
				var list = data.dataList;
				if (list == null || list.length == 0) {
					var nodata = "<tr><td colspan='16'>没有符合条件的数据</td></tr>";
					trdata.push(nodata);
					$("#user_table1").html(trdata.join(''));
					drawPage(1, 1, 9);
					return false;
				}
				for (var i = 0; i < list.length; i++) {
					if(list[i].statusId == 0) {
						trdata.push('<tr onclick="selectCheck(this)">');
					} else {
						trdata.push('<tr>');
					}
						trdata.push('<td>');
						if(list[i].statusId == 0) {
						trdata.push('<input onclick="selectCheck($(this).parent().parent())" type="checkbox" class="ids_box" value="'+list[i].id+'" name="ids" />');
						}
						trdata.push(list[i].id);
						trdata.push('</td><td title="'+list[i].accountDate+'">');
						trdata.push(list[i].accountDate);
						trdata.push('</td><td title="'+list[i].transDate+'">');
						trdata.push(list[i].transDate);
						trdata.push('</td><td title="'+list[i].transId+'">');
						trdata.push(list[i].transId);
						trdata.push('</td><td title="'+list[i].funcCode+'">');
						trdata.push(list[i].funcCode);
						trdata.push('</td><td title="'+list[i].finAccountId1+'">');
						trdata.push(list[i].finAccountId1);
						trdata.push('</td><td title="'+list[i].paymentAmout1+'">');
						trdata.push(list[i].paymentAmout1);
						trdata.push('</td><td title="'+list[i].finAccountId2+'">');
						trdata.push(list[i].finAccountId2);
						trdata.push('</td><td title="'+list[i].paymentAmout2+'">');
						trdata.push(list[i].paymentAmout2);
						trdata.push('</td><td title="'+list[i].tn+'">');
						trdata.push(list[i].tn);
						trdata.push('</td><td title="'+list[i].settleDay+'">');
						trdata.push(list[i].settleDay);
						trdata.push('</td><td title="'+list[i].settleNo+'">');
						trdata.push(list[i].settleNo);
						trdata.push('</td><td title="'+list[i].remark2+'">');
						trdata.push(list[i].remark2);
						trdata.push('</td><td title="'+list[i].statusId+'">');
						trdata.push(list[i].statusId);
						trdata.push('</td><td title="'+list[i].readStatusId+'">');
						trdata.push(list[i].readStatusId);
						trdata.push('</td><td>');
						trdata.push('<a href=settle_kernel_entry/settle_kernel_entry_open_edit?id='+list[i].id+' class="margr4"');
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
				var nodata = "<tr><td colspan='26'>没有符合条件的数据</td></tr>";
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
/***
 * 删除信息的表单提交
 */
function delSubmit(id) {
	art.dialog({
	    content: '你确定要删除这掉消息吗？',
	    ok: function () {
	    	$.post(
        		'rule/del_ajax',						//发送请求的url
        		'id='+id,								//表单信息
        		function(data) {						//回调函数
        			art.dialog.tips(data.msg, 1.5, resetForm);
        		},
        		'json'
        	);
	        return true;
	    },
	    cancelVal: '关闭',
	    cancel: true //为true等价于function(){}
	});
}
/**
 * 选中行变色
 * @param obj
 */
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
 * 未成功汇总信息重发MQ
 */
function doKernelEntry(butt) {
	var oldButt = loadingFun(butt);
	var checkedBox = $(".ids_box[name=ids]:checked");
	var idsStr = "";
	checkedBox.each(function() {
		idsStr += "," + this.value;
	});
	idsStr = idsStr.substring(1);
	if(!idsStr) {
		alert("请选择交易信息!");
		successedFun(butt, oldButt);
		return;
	}
	$.ajax({
        type: "POST",
        url: $("#doSettleKernelEntry").val(),
        data: "idsStr=" + idsStr,
        dataType: "text",
        success: function(data){
			alert(data);
			funSearch(1,"index");
			successedFun(butt, oldButt);
        },
        error:function(data){
        	alert("记会计账系统异常！");
        	successedFun(butt, oldButt);
        }
     });
}
/**
 * 根据汇总规则生成汇总信息
 */
function doCollectByRule(butt) {
	var collectType = $("#collectType").val();
	var merchantCode = $("#merchantCode").val();
	var payChannelId = $("#payChannelId").val();
	var funcCode = $("#collectFuncCode").val();
	var accountDate = $("#collectAccountDate").val();
	
	if(!(collectType && merchantCode && payChannelId && funcCode && accountDate)) {
		alert("请输入完整的汇总信息!");
		return;
	}
	
	var oldButt = loadingFun(butt);
	
	var para = "collectType="+collectType+"&merchantCode="+merchantCode+"&payChannelId="+payChannelId+"&funcCode="+funcCode+"&accountDateStr="+accountDate;

	$.ajax({
        type: "POST",
        url: $("#doCollectByRule").val(),
        data: para,
        dataType: "text",
        success: function(data){
			alert("操作完成");
			funSearch(1,"index");
			successedFun(butt, oldButt);
        },
        error:function(data){
        	alert("记会计账系统异常！");
        	successedFun(butt, oldButt);
        }
     });
}
