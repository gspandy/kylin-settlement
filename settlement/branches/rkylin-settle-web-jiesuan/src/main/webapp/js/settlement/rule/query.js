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
			'<tr >' +
			'<th width="5%">ID</th>' +
			'<th width="10%">对账规则名称</th>' +
			'<th width="5%">规则种类</th>' +
			'<th width="5%">上游交易类型</th>' +
			'<th width="10%">清算方对账项目</th>' +
			'<th width="10%">上游渠道对账项目</th>' +
			'<th width="9%">对账名</th>' +
			'<th width="5%">ROP文件批次号</th>' +
			'<th width="5%">对账类型</th>' +
			'<th width="20%">有效时间</th>' +
			'<th width="5%">状态</th>' +
			'<th width="5%">备注</th>' +
			'<th width="6%">操作</th>' +
			'</tr>';
			var trdata = [tableHead];
			
			if (data.result == "ok") {
				var list = data.dataList;
				if (list == null || list.length == 0) {
					var nodata = "<tr><td colspan='13'>没有符合条件的数据</td></tr>";
					trdata.push(nodata);
					$("#user_table1").html(trdata.join(''));
					drawPage(1, 1, 9);
					return false;
				}
				for (var i = 0; i < list.length; i++) {
					if(list[i].ruleType=="01"){
						list[i].ruleType = "渠道";
					}else if(list[i].ruleType=="02"){
						list[i].ruleType = "机构";
					}
					if(list[i].readType=="01"){
						list[i].readType = "网关支付";
					}else if(list[i].readType=="02"){
						list[i].readType = "代收付";
					}
					if("0"==list[i].keyType){
						list[i].keyType = "对账KEY";
					}else if("1"==list[i].keyType){
						list[i].keyType = "对账项目";
					}
					if("0"==list[i].statusId){
						list[i].statusId = "无效";
					}else if("1"==list[i].statusId){
						list[i].statusId = "有效";
					}
					var date = new Date();
					var startTime = "";
					var endTime = "";
					if(list[i].startTime != null){
						date.setTime(list[i].startTime);
						startTime = date.toLocaleDateString();
					}
					if(list[i].endTime != null){
						date.setTime(list[i].endTime);
						endTime = date.toLocaleDateString();
					}
					trdata.push('<tr>');
						trdata.push('<td>');
						trdata.push(list[i].ruleId);
						trdata.push('</td><td title="'+list[i].ruleName+'">');
						trdata.push(list[i].ruleName);
						trdata.push('</td><td title="'+list[i].ruleType+'">');
						trdata.push(list[i].ruleType);
						trdata.push('</td><td title="'+list[i].readType+'">');
						trdata.push(list[i].readType);
						trdata.push('</td><td title="'+list[i].detKeyCode+'">');
						trdata.push(list[i].detKeyCode);
						trdata.push('</td><td title="'+list[i].accKeyCode+'">');
						trdata.push(list[i].accKeyCode);
						trdata.push('</td><td title="'+list[i].settleKeyName+'">');
						trdata.push(list[i].settleKeyName);
						trdata.push('</td><td title="'+list[i].ropBatchNo+'">');
						trdata.push(list[i].ropBatchNo);
						trdata.push('</td><td title="'+list[i].keyType+'">');
						trdata.push(list[i].keyType);
						trdata.push('</td><td>');
						trdata.push(startTime);
						trdata.push(" - ");
						trdata.push(endTime);
						trdata.push('</td><td>');
						trdata.push(list[i].statusId);
						trdata.push('</td><td title="'+list[i].remark+'">');
						trdata.push(list[i].remark);
						trdata.push('</td><td>');
						trdata.push('<a href=rule/rule_open_edit?id='+list[i].ruleId+' class="margr4"');
						trdata.push('\')">修改</a>');
						trdata.push('<a href="javascript:delSubmit(\''+list[i].ruleId+'\')" class="margr4"');
						trdata.push('\')">删除</a>');
					trdata.push('</td>');
					trdata.push('</tr>');
				}
				$("#user_table1").html(trdata.join(''));
				var totalNum = data.totalNum;
				//调用绘制分页的方法
				drawPageAgain(totalNum, pageIndex, pageSize, index);
				$('#div_table_page').show();
			}else{
				var nodata = "<tr><td colspan='13'>没有符合条件的数据</td></tr>";
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
        		'rule/del_ajax',			//发送请求的url
        		'id='+id,				//表单信息
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