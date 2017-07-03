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
			'<th width="13%">交易订单号</th>' +
			'<th width="5%">批次号</th>' +
			'<th width="5%">挂账条目</th>' +
			'<th width="5%">机构代码</th>' +
			'<th width="5%">产品号</th>' +
			'<th width="15%">用户ID</th>' +
			'<th width="10%">第三方账户</th>' +
			'<th width="8%">挂账金额</th>' +
			'<th width="8%">挂账类型</th>' +
			'<th width="7%">记账日期</th>' +
			'<th width="10%">状态</th>' +
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
					if(0==list[i].billType){
						list[i].billType = "差错处理";
					}else if(1==list[i].billType){
						list[i].billType = "退款";
					}
					if(0==list[i].statusId){
						list[i].statusId = "未进行";
					}else if(1==list[i].statusId){
						list[i].statusId = "已进行";
					}else if(2==list[i].statusId){
						list[i].statusId = "不处理";
					}
					
					var date = new Date();
					date.setTime(list[i].accountDate);
					var accountDate = date.toLocaleDateString();
					trdata.push('<tr>');
					trdata.push('<td>');
					trdata.push(list[i].transBillId);
					trdata.push('</td><td>');
					trdata.push(list[i].orderNo);
					trdata.push('</td>');
					trdata.push('<td title="'+list[i].batchNo+'">');
					trdata.push(list[i].batchNo);
					trdata.push('</td><td>');
					trdata.push(list[i].billNo);
					trdata.push('</td><td>');
					trdata.push("<label title='"+list[i].rootInstCd+"'>"+list[i].rootInstCd+"</label>");
					trdata.push('</td><td>');
					trdata.push("<label title='"+list[i].productId+"'>"+list[i].productId+"</label>");
					trdata.push('</td><td>');
					trdata.push("<label title='"+list[i].userId+"'>"+list[i].userId+"</label>");
					trdata.push('</td><td>');
					trdata.push(list[i].referUserId);
					trdata.push('</td><td>');
					trdata.push(list[i].billAmount);
					trdata.push('</td><td>');
					trdata.push(list[i].billType);
					trdata.push('</td><td>');
					trdata.push(accountDate);
					trdata.push('</td><td>');
					trdata.push(list[i].statusId);
					trdata.push('</td><td>');
					trdata.push('<a href=transbill/bill_open_edit?id='+list[i].transBillId+' class="margr4"');
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