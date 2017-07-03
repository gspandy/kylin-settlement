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
			'<th width="7%">参数类型</th>' +
			'<th width="5%">产品ID</th>' +
			'<th width="15%">参数编码</th>' +
			'<th width="20%">参数值</th>' +
			'<th width="10%">预留一</th>' +
			'<th width="4%">状态</th>' +
			'<th width="23%">备注</th>' +
			'<th width="6%">创建时间</th>' +
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
					if("0"==list[i].statusId){
						list[i].statusId = "无效";
					}else if("1"==list[i].statusId){
						list[i].statusId = "有效";
					}
					var date = new Date();
					var createdTime = "";
					if(list[i].createdTime != null){
						date.setTime(list[i].createdTime);
						createdTime = date.toLocaleDateString();
					}
					trdata.push('<tr>');
						trdata.push('<td>');
						trdata.push(list[i].parameterId);
						trdata.push('</td><td title="'+list[i].parameterType+'">');
						trdata.push(list[i].parameterType);
						trdata.push('</td><td title="'+list[i].productId+'">');
						trdata.push(list[i].productId);
						trdata.push('</td><td title="'+list[i].parameterCode+'">');
						trdata.push(list[i].parameterCode);
						trdata.push('</td><td title="'+list[i].parameterValue+'">');
						trdata.push(list[i].parameterValue);
						trdata.push('</td><td title="'+list[i].obligate1+'">');
						trdata.push(list[i].obligate1);
						trdata.push('</td><td title="'+list[i].statusId+'">');
						trdata.push(list[i].statusId);
						trdata.push('</td><td title="'+list[i].remark+'">');
						trdata.push(list[i].remark);
						trdata.push('</td><td title="'+createdTime+'">');
						trdata.push(createdTime);
						trdata.push('</td><td>');
						trdata.push('<a href=parameterinfo/open_edit?id='+list[i].parameterId+' class="margr4"');
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
