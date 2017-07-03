/***
 * 
 */
$(function() {
	funSearch(1,"index");
	
	//给交易类型赋初始值-1，指全部
	$("#readType").val("-1");
	
	//监听支付渠道的change事件，级联出相应的交易类型
	$("#payChannelId").on("change",function(){
		var payChannelId = $("#payChannelId").val();
		if(payChannelId =="-1"){
			$("#allreadTypeDiv").show();
			$("#tldiv").hide();
			$("#lldiv").hide();
			$("#readType").val($("#allreadType").val());
		}else if(payChannelId =="01" || payChannelId=="05" || payChannelId =="S01" ){
			$("#allreadTypeDiv").hide();
			$("#tldiv").show();
			$("#lldiv").hide();
			$("#readType").val($("#tlreadType").val());
		}else if(payChannelId =="04"){
			$("#allreadTypeDiv").hide();
			$("#tldiv").hide();
			$("#lldiv").show();
			$("#readType").val($("#llreadType").val());
		}
		
	});
	
	//通联的交易类型change时，将值赋给隐藏域readType(交易类型)
	$("#tlreadType").on("change",function(){
		$("#readType").val($("#tlreadType").val());
	});
	//连连的交易类型change时，将值赋给隐藏域readType(交易类型)
	$("#llreadType").on("change",function(){
		$("#readType").val($("#llreadType").val());
	});
	//将选中的交易类型的值赋给隐藏域readType(交易类型)
	$("#allreadType").on("change",function(){
		$("#readType").val($("#allreadType").val());
	});
	fixedTableTitleForTop("user_table1");
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
			'<tr class="tb-head-no" id="head-no">' +
			'<th >ID</th>' +
			'<th >支付渠道</th>' +
			'<th >交易类型</th>' +
			'<th >商户编码</th>' +
			'<th >交易请求时间</th>' +
			'<th >交易/结算单订单号</th>' +
			'<th >统一交易流水号</th>' +
			'<th >交易类型</th>' +
			'<th >交易金额</th>' +
			'<th >手续费金额</th>' +
			'<th >状态</th>' +
			'<th >操作</th>' +
			'</tr>';
			var trdata = [tableHead];
			if (data.result == "ok") {
				var list = data.dataList;
				if (list == null || list.length == 0) {
					var nodata = "<tr><td colspan='20'>没有符合条件的数据</td></tr>";
					trdata.push(nodata);
					$("#user_table1").html(trdata.join(''));
					drawPage(1, 1, 9);
					return false;
				}
				for (var i = 0; i < list.length; i++) {
					if("01"==list[i].payChannelId){
						list[i].payChannelId = "通联";
					}else if("04"==list[i].payChannelId){
						list[i].payChannelId = "连连";
					}else if("05"==list[i].payChannelId){
						list[i].payChannelId = "联动优势";
					}else if("S01"==list[i].payChannelId){
						list[i].payChannelId = "畅捷";
					}
					if(list[i].readType=="01"){
						list[i].readType = "网关支付";
					}else if(list[i].readType=="02"){
						list[i].readType = "代收付";
					}else{
						list[i].readType = "无";
					}
					if("0"==list[i].statusId){
						list[i].statusId = "【交易】失败";
					}else if("1"==list[i].statusId){
						list[i].statusId = "【交易】成功";
					}else if("2"==list[i].statusId){
						list[i].statusId = "【交易】不明";
					}else if("21"==list[i].statusId){
						list[i].statusId = "【对账】成功";
					}else if("22"==list[i].statusId){
						list[i].statusId = "【对账】错账";
					}else if("23"==list[i].statusId){
						list[i].statusId = "【对账】长款";
					}else if("211"==list[i].statusId){
						list[i].statusId = "【对账】其他环境";
					}
					
					var date = new Date();
					date.setTime(list[i].requestTime);
					var requestDate = date.toLocaleString();
					trdata.push('<tr>');
					trdata.push('<td>');
					trdata.push(list[i].transAccountId);
					trdata.push('</td><td>');
					trdata.push(list[i].payChannelId);
					trdata.push('</td>');
					trdata.push('<td title="'+list[i].readType+'">');
					trdata.push(list[i].readType);
					trdata.push('</td><td>');
					trdata.push(list[i].merchantCode);
					trdata.push('</td><td>');
					trdata.push("<label title='"+requestDate+"'>"+requestDate+"</label>");
					trdata.push('</td><td>');
					trdata.push("<label title='"+list[i].orderNo+"'>"+list[i].orderNo+"</label>");
					trdata.push('</td><td>');
					trdata.push("<label title='"+list[i].transFlowNo+"'>"+list[i].transFlowNo+"</label>");
					trdata.push('</td><td>');
					trdata.push(list[i].transType);
					trdata.push('</td><td>');
					trdata.push(list[i].transAmount);
					trdata.push('</td><td>');
					trdata.push(list[i].feeAmount);
					trdata.push('</td><td>');
					trdata.push(list[i].statusId);
					trdata.push('</td><td>');
					trdata.push('<a href=transaccount/account_open_view?id='+list[i].transAccountId+'>详情</a>&nbsp;');
					trdata.push('<a href=transaccount/account_open_edit?id='+list[i].transAccountId+' class="margr4"');
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
				var nodata = "<tr><td colspan='20'>没有符合条件的数据</td></tr>";
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
