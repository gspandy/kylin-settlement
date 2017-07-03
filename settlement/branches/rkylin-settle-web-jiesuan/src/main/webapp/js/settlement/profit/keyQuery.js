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
			'<th width="5%">规则ID</th>' +
			'<th width="10%">分润规则名称</th>' +
			'<th width="8%">管理机构代码</th>' +
			'<th width="5%">产品号</th>' +
			'<th width="9%">KEY1名称</th>' +
			'<th width="5%">KEY1值</th>' +
			'<th width="9%">KEY2名称</th>' +
			'<th width="5%">KEY2值</th>' +
			'<th width="5%">KEY3名称</th>' +
			'<th width="5%">KEY3值</th>' +
			'<th width="5%">明细ID</th>' +
			'<th width="14%">生效-失效时间</th>' +
			'<th width="4%">状态</th>' +
			'<th width="5%">备注</th>' +
			'<th width="6%">操作</th>' +
			'</tr>';
			var trdata = [tableHead];
			
			if (data.result == "ok") {
				var list = data.dataList;
				if (list == null || list.length == 0) {
					var nodata = "<tr><td colspan='15'>没有符合条件的数据</td></tr>";
					trdata.push(nodata);
					$("#user_table1").html(trdata.join(''));
					drawPage(1, 1, 9);
					return false;
				}
				for (var i = 0; i < list.length; i++) {
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
						trdata.push(list[i].profitKeyId);
						trdata.push('</td><td title="'+list[i].profitRuleName+'">');
						trdata.push(list[i].profitRuleName);
						trdata.push('</td><td title="'+list[i].rootInstCd+'">');
						trdata.push(list[i].rootInstCd);
						trdata.push('</td><td title="'+list[i].funcCode+'">');
						trdata.push(list[i].funcCode);
						trdata.push('</td><td title="'+list[i].keyName1+'">');
						trdata.push(list[i].keyName1);
						trdata.push('</td><td title="'+list[i].keyValue1+'">');
						trdata.push(list[i].keyValue1);
						trdata.push('</td><td title="'+list[i].keyName2+'">');
						trdata.push(list[i].keyName2);
						trdata.push('</td><td title="'+list[i].keyValue2+'">');
						trdata.push(list[i].keyValue2);
						trdata.push('</td><td title="'+list[i].keyName3+'">');
						trdata.push(list[i].keyName3);
						trdata.push('</td><td title="'+list[i].keyValue3+'">');
						trdata.push(list[i].keyValue3);
						trdata.push('</td><td title="'+list[i].profitDetailId+'">');
						trdata.push(list[i].profitDetailId);
						trdata.push('</td><td>');
						trdata.push(startTime);
						trdata.push(" - ");
						trdata.push(endTime);
						trdata.push('</td><td>');
						trdata.push(list[i].statusId);
						trdata.push('</td><td title="'+list[i].remark+'">');
						trdata.push(list[i].remark);
						trdata.push('</td><td>');
						trdata.push('<a href=profitkey/key_open_edit?id='+list[i].profitKeyId+' class="margr4"');
						trdata.push('\')">修改</a>');
						trdata.push('<a href="javascript:delSubmit(\''+list[i].profitKeyId+'\')" class="margr4"');
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
				var nodata = "<tr><td colspan='15'>没有符合条件的数据</td></tr>";
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
        		'profitkey/del_ajax',			//发送请求的url
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