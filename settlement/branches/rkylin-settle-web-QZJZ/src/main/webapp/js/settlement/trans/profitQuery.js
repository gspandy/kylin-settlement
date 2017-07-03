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
			'<th width="13%">订单号</th>' +
			'<th width="13%">用户ID</th>' +
			'<th width="7%">商户编码</th>' +
			'<th width="10%">管理机构代码</th>' +
			'<th width="7%">产品号</th>' +
			'<th width="5%">清分类型</th>' +
			'<th width="5%">分润金额</th>' +
			'<th width="5%">是否必须</th>' +
			'<th width="16%">备注</th>' +
			'<th width="7%">记账日期</th>' +
			'<th width="7%">状态</th>' +
			'<th width="5%">操作</th>' +
			'</tr>';
			var trdata = [tableHead];
			if (data.result == "ok") {
				var list = data.dataList;
				if (list == null || list.length == 0) {
					var nodata = "<tr><td colspan='12'>没有符合条件的数据</td></tr>";
					trdata.push(nodata);
					$("#user_table1").html(trdata.join(''));
					drawPage(1, 1, 9);
					return false;
				}
				for (var i = 0; i < list.length; i++) {
					if(0==list[i].profitType){
						list[i].profitType = "分润";
					}else if(1==list[i].profitType){
						list[i].profitType = "代收";
					}else if(2==list[i].profitType){
						list[i].profitType = "代付";
					}
					if(0==list[i].isMust){
						list[i].isMust = "非必须";
					}else{
						list[i].isMust = "必须";
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
					trdata.push(list[i].transProfitId);
					trdata.push('</td><td>');
					trdata.push(list[i].orderNo);
					trdata.push('</td>');
					trdata.push('<td title="'+list[i].userId+'">');
					trdata.push(list[i].userId);
					trdata.push('</td>');
					trdata.push('<td title="'+list[i].merchantCode+'">');
					trdata.push(list[i].merchantCode);
					trdata.push('</td>');
					trdata.push('<td title="'+list[i].rootInstCd+'">');
					trdata.push(list[i].rootInstCd);
					trdata.push('</td>');
					trdata.push('<td title="'+list[i].productId+'">');
					trdata.push(list[i].productId);
					trdata.push('</td><td>');
					trdata.push(list[i].profitType);
					trdata.push('</td><td>');
					trdata.push(list[i].profitAmount);
					trdata.push('</td><td>');
					trdata.push(list[i].isMust);
					trdata.push('</td>');
					trdata.push('<td title="'+list[i].remark+'">');
					trdata.push(list[i].remark);
					trdata.push('</td><td>');
					trdata.push(accountDate);
					trdata.push('</td><td>');
					trdata.push(list[i].statusId);
					trdata.push('</td><td>');
					trdata.push('<a href=transprofit/profit_open_edit?id='+list[i].transProfitId+' class="margr4"');
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
				var nodata = "<tr><td colspan='12'>没有符合条件的数据</td></tr>";
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