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
			'<th width="10%">汇总规则名称</th>' +
			'<th width="5%">机构代码</th>' +
			'<th width="5%">渠道代码</th>' +
			'<th width="10%">功能码</th>' +
			'<th width="10%">会计功能码</th>' +
			'<th width="9%">科目名称1</th>' +
			'<th width="5%">账户ID1</th>' +
			'<th width="9%">科目名称2</th>' +
			'<th width="5%">账户ID2</th>' +
			'<th width="5%">汇总类型</th>' +
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
					var collectTypeStr = "";
					var statusIdStr = "";
					if(list[i].collectType == "1") {
						collectTypeStr = "结算（日切）汇总";
					} else if(list[i].collectType == "2") {
						collectTypeStr = "对账汇总";
					}
					if(list[i].statusId == "0") {
						statusIdStr = "无效";
					} else if(list[i].statusId == "1") {
						statusIdStr = "有效";
					}
					trdata.push('<tr>');
						trdata.push('<td>');
						trdata.push(list[i].id);
						trdata.push('</td><td title="'+list[i].profitRuleName+'">');
						trdata.push(list[i].profitRuleName);
						trdata.push('</td><td title="'+list[i].rootInstCd+'">');
						trdata.push(list[i].rootInstCd);
						trdata.push('</td><td title="'+list[i].payChannelId+'">');
						trdata.push(list[i].payChannelId);
						trdata.push('</td><td title="'+list[i].funcCode+'">');
						trdata.push(list[i].funcCode);
						trdata.push('</td><td title="'+list[i].kernelFuncCode+'">');
						trdata.push(list[i].kernelFuncCode);
						trdata.push('</td><td title="'+list[i].accountName1+'">');
						trdata.push(list[i].accountName1);
						trdata.push('</td><td title="'+list[i].finAccountId1+'">');
						trdata.push(list[i].finAccountId1);
						trdata.push('</td><td title="'+list[i].accountName2+'">');
						trdata.push(list[i].accountName2);
						trdata.push('</td><td title="'+list[i].finAccountId2+'">');
						trdata.push(list[i].finAccountId2);
						trdata.push('</td><td title="'+collectTypeStr+'">');
						trdata.push(collectTypeStr);
						trdata.push('</td><td title="'+statusIdStr+'">');
						trdata.push(statusIdStr);
						trdata.push('</td><td title="'+list[i].remark+'">');
						trdata.push(list[i].remark);
						trdata.push('</td><td>');
						trdata.push('<a href=collect_rule/rule_open_edit?id='+list[i].id+' class="margr4"');
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