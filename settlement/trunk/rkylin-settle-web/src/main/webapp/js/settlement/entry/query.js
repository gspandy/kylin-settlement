/***
 * 
 */
$(function() {
	funSearch(1,"index");
	var messenger = new Messenger('iframe1','projectName');
	messenger.listen(function (msg) {

		fixedTableTitleForTop("user_table1", msg);
	});
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
			'<tr class="tb-head-no" id="head-no" >' +
			'<th ><input type="checkbox" id="all" value="all" onclick="checkAllInfo(this)" />ID</th>' +
			'<th >订单编号</th>' +
			'<th >结果类型</th>' +
			'<th >状态</th>' +
			'<th >备注</th>' +
			'<th >对账key</th>' +
			'<th >记账日期</th>' +
			'<th >创建时间</th>' +
			'<th >修改时间</th>' +
			'<th >操作</th>' +
			'</tr>';
			var trdata = [tableHead];
			
			if (data.result == "ok") {
				var list = data.dataList;
				if (list == null || list.length == 0) {
					var nodata = "<tr><td colspan='9'>没有符合条件的数据</td></tr>";
					trdata.push(nodata);
					$("#user_table1").html(trdata.join(''));
					drawPage(1, 1, 9);
					return false;
				}
				for (var i = 0; i < list.length; i++) {
					var balanceType = list[i].balanceType;
					switch(balanceType) {
						case 1: balanceType = "【对账】 外部对账结果"; break;
						case 2: balanceType = "【对账】 账户&多渠道对账结果"; break;
						case 0: balanceType = "【分润】 分润失败结果 "; break;
						default:balanceType = "暂无记录";
					}
					var status = list[i].statusId;
					switch(status) {
						case 1: status = "【对账】 平账"; break;
						case 0: status = "【对账】 错账"; break;
						case 2: status = "【对账】 长款"; break;
						case 3: status = "【对账】 短款"; break;
						case 4: status = "【对账】 其他环境"; break;
						case 11: status = "【分润】 成功"; break;
						case 10: status = "【分润】 失败"; break;
						default:status = "暂无记录";
					}
					
					var date = new Date();
					date.setTime(list[i].accountDate);
					var accountDate = date.toLocaleDateString();
					date.setTime(list[i].createdTime);
					var createdTime = date.toLocaleString();
					date.setTime(list[i].updatedTime);
					var updatedTime = date.toLocaleString();
					var theTr = '<tr onclick="selectCheck(this)">';
					if(list[i].obligate2) {
						theTr = '<tr onclick="selectCheck(this)" onmouseover="openTitle(this, \''+list[i].obligate2.replace(/#/, ',')+'\')">';
					}
					trdata.push(theTr);
						trdata.push('<td><input type="checkbox" name="ids" value="'+list[i].balanceEntryId+'"  onclick="checkBoxOne(this)"/>');
						trdata.push(list[i].balanceEntryId);
						trdata.push('</td><td>');
						trdata.push(list[i].orderNo);
						trdata.push('</td>');
						trdata.push('<td>');
						trdata.push(balanceType);
						trdata.push('</td><td>');
						trdata.push(status);
						trdata.push('</td>');
						trdata.push('<td>');
						trdata.push(list[i].remark);
						trdata.push('</td><td>');
						trdata.push(list[i].obligate1);
						trdata.push('</td><td>');
						trdata.push(accountDate);
						trdata.push('</td><td>');
						trdata.push(createdTime);
						trdata.push('</td><td>');
						trdata.push(updatedTime);
						trdata.push('</td><td>');
						//if(list[i].obligate2) trdata.push('<a target="blank" href=entry/entry_open_view?ids='+list[i].obligate2.replace(/#/, ',')+'&entryId='+list[i].balanceEntryId+'>详情</a>&nbsp;');
						if(list[i].obligate2) trdata.push('<a target="blank" href="javascript:openView(\''+list[i].obligate2.replace(/#/, ',')+'\','+list[i].balanceEntryId+');">详情</a>&nbsp;');
						trdata.push('<a href=entry/entry_open_edit?action=&id='+list[i].balanceEntryId+' class="margr4"');
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
				var nodata = "<tr><td colspan='9'>没有符合条件的数据</td></tr>";
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
 * 全选反选
 */
function checkAllInfo(obj) {
	var allButt = document.getElementById("all");
	var checkbox = $(obj);
	var ids = document.getElementsByName("ids");
	var len = ids.length;
	for(var i = 0; i < len; i ++) {
		ids[i].checked = allButt.checked;

		if(checkbox.is(':checked')){
			$("#user_table1 tbody tr").find("td").addClass("tb_style");
		}else{
			$("#user_table1 tbody tr").find("td").removeClass("tb_style");
		}
	}
}
/***
 * 使用artDialog显示 entry,detail和account表的交易信息
 * @param ids
 * @param balanceEntryId
 */
function openView(ids, balanceEntryId) {
	art.dialog.open(
		'entry/entry_open_view?ids='+ids+'&entryId='+balanceEntryId+'', 
		{
			width:"1000px",
			height:"600px",
			lock:true
		}
	);
}
/***
 * 使用title显示 entry,detail和account表的交易信息
 * @param ids
 * @param balanceEntryId
 */
function openTitle(tr, ids) {
	if('' !== tr.title) return;
	$.ajax({
			url:'entry/entry_open_title?ids='+ids, 
			type:'get',
			success:function(returnMap) {
				var settleTransDetailAmount = returnMap.settleTransDetailAmount ? returnMap.settleTransDetailAmount : '【无此交易】';
				var settleTransAccountAmount = returnMap.settleTransAccountAmount ? returnMap.settleTransAccountAmount : '【无此交易】';
				var titleTxt = "清结算交易金额:"+ settleTransDetailAmount +"，" + "渠道交易金额:"+ settleTransAccountAmount;
				tr.title = titleTxt;
			},
			dataType:'json',
			async:false
	});
}
/***
 * 导出TXT
 */
function outputEXCL() {
	var idsStr = "";
	var $idsCheckBoxArr = $('input[name="ids"]:checked');
	$idsCheckBoxArr.each(function() {idsStr += "," + this.value;});
	idsStr = idsStr.substring(1);
//	$.ajax({
//		type: "get",
//		url : $('#outputTxtURL').val(),
//		data : "ids=" + idsStr,
//		dataType : "text",
//		succcess : function(data) {
//		}
//	});
	location = $('#outputTxtURL').val() + "?ids=" + idsStr;
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
function checkBoxOne(obj){
	$(obj).parent("td").addClass("tb_style")
}
