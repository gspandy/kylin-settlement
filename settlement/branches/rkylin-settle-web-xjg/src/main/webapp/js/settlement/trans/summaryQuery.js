/***
 * onload自动执行
 */
$(function() {
	funSearch(1,"index");
	fixedTableTitleForTop("user_table1");
});

/***
 * ajax获取分页信息和显示数据
 */
function funSearch(pageIndex, index) {
	var pageSize = $("#dataForm #pageSize option:selected").val();		//显示条数
	$('#dataForm #pageIndex').val(pageIndex);							//赋值当前页
	$('#dataForm #pageSize').val(pageSize);								//赋值显示条数
	var pathHead = $("#pathHead").val();
	$.post(
		$('#dataForm').attr("action"),									//发送请求 url
		$('#dataForm').serialize(),										//表单数据
		function(data) {												//回调函数
			//jq在页面中生成展示数据
			var tableHead = 
			'<tr  class="tb-head-no" id="head-no">' +
			'<th >ID</th>' +
			'<th >批次号</th>' +
			'<th >订单号</th>' +
			'<th >业务代码</th>' +
			'<th >机构号</th>' +
			'<th >功能编码</th>' +
			'<th >用户ID</th>' +
			'<th >金额</th>' +
			'<th >状态</th>'
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
					var statusId = list[i].statusId;
					if(1==statusId){
						list[i].statusId = "处理中";
					}else if(13==statusId){
						list[i].statusId = "失败";
					}else if(15==statusId){
						list[i].statusId = "成功";
					}else if(99==statusId){
						list[i].statusId = "异常";
					}
//					
//					var funcCode = list[i].funcCode;
//					if(4013==funcCode){
//						list[i].funcCode = "代收";
//					}else if(4014==funcCode){
//						list[i].funcCode = "代付";
//					}else if(4016==funcCode){
//						list[i].funcCode = "提现";
//					}else if(40130==funcCode){
//						list[i].funcCode = "贷款还款";
//					}
//					
					var date = new Date();
					date.setTime(list[i].requestTime);
					list[i].requestTime = date.toLocaleString();
					trdata.push('<tr>');
					trdata.push('<td>');
					trdata.push('<input type="hidden" value="'+ list[i].statusId +'" name="statusId" />');
					trdata.push(list[i].transSumId);
					trdata.push('</td><td>');
					trdata.push(list[i].batchNo);
					trdata.push('</td><td>');
					trdata.push("<label title='"+list[i].orderNo+"'>"+list[i].orderNo+"</label>");
					trdata.push('</td><td>');
					trdata.push(list[i].businessType);
					trdata.push('</td><td>');
					trdata.push(list[i].rootInstCd);
					trdata.push('</td><td>');
					trdata.push(list[i].funcCode);
					trdata.push('</td><td>');
					trdata.push(list[i].userId);
					trdata.push('</td><td>');
					trdata.push(list[i].amount);
					trdata.push('</td><td>');
					trdata.push(list[i].statusId);
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