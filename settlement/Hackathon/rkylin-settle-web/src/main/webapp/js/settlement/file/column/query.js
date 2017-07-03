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
			'<th width="5%">列ID</th>' +
			'<th width="5%">文件列信息码</th>' +
			'<th width="5%">列号</th>' +
			'<th width="5%">列名称</th>' +
			'<th width="13%">交易信息字段</th>' +
			'<th width="10%">字段说明</th>' +
			'<th width="5%">特殊列</th>' +
			'<th width="5%">数据类型</th>' +
			'<th width="10%">数据格式</th>' +
			'<th width="22%">有效时间</th>' +
			'<th width="5%">状态</th>' +
			'<th width="5%">备注</th>' +
			'<th width="5%">操作</th>' +
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
					if("0"==list[i].isSpecialColumn){
						list[i].isSpecialColumn = "否";
					}else if("1"==list[i].isSpecialColumn){
						list[i].isSpecialColumn = "是";
					}
					if("1"==list[i].dataType){
						list[i].dataType = "字符串";
					}else if("2"==list[i].dataType){
						list[i].dataType = "数字";
					}else if("3"==list[i].dataType){
						list[i].dataType = "日期";
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
						startTime = date.toLocaleString();
					}
					if(list[i].endTime != null){
						date.setTime(list[i].endTime);
						endTime = date.toLocaleString();
					}
					trdata.push('<tr>');
						trdata.push('<td>');
						trdata.push(list[i].fileColumnId);
						trdata.push('</td><td>');
						trdata.push(list[i].fileSubId);
						trdata.push('</td><td title="'+list[i].fileColumnNo+'">');
						trdata.push(list[i].fileColumnNo);
						trdata.push('</td><td title="'+list[i].fileColumnTitle+'">');
						trdata.push(list[i].fileColumnTitle);
						trdata.push('</td><td title="'+list[i].fileColumnKeyCode+'">');
						trdata.push(list[i].fileColumnKeyCode);
						trdata.push('</td><td title="'+list[i].fileColumnKeyName+'">');
						trdata.push(list[i].fileColumnKeyName);
						trdata.push('</td><td title="'+list[i].isSpecialColumn+'">');
						trdata.push(list[i].isSpecialColumn);
						trdata.push('</td><td title="'+list[i].dataType+'">');
						trdata.push(list[i].dataType);
						trdata.push('</td><td title="'+list[i].dataFormat+'">');
						trdata.push(list[i].dataFormat);
						trdata.push('</td><td>');
						trdata.push(startTime);
						trdata.push(" - ");
						trdata.push(endTime);
						trdata.push('</td><td>');
						trdata.push(list[i].statusId);
						trdata.push('</td><td title="'+list[i].remark+'">');
						trdata.push(list[i].remark);
						trdata.push('</td><td>');
						trdata.push('<a href=filecolumn/column_open_edit?id='+list[i].fileColumnId+' class="margr4"');
						trdata.push('\')">修改</a>');
						trdata.push('<a href=\'javascript:delSubmit("'+list[i].fileColumnId+'")\' class="margr4"');
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
    			'filecolumn/del_ajax',			//发送请求的url
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