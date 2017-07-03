/***
 * 
 */
$(function() {
	funSearch(1,"index");
	$("#matchBtn").click(function(){
		var param = "";
		$("input[name='matchBox']:checkbox").each(function(i){
			if($(this).is(':checked')==true){
				param += $(this).val()+":true,";
			}else{
				param += $(this).val()+":false,";
			}
		});
		param = param.substring(0,param.length-1);
		$.post(
			'profitmatch/match_operation_ajax',
			"matchVal="+param+"&id="+$("#profitDetailId").val(),
			function(data){
				art.dialog.tips(data.msg, 1.5, searchRule);
			},"json"
		);
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
			'<tr >' +
			'<th width="10%">规则ID</th>' +
			'<th width="40%">分润规则名称</th>' +
			'<th width="30%">管理机构代码</th>' +
			'<th width="20%">产品号</th>' +
			'</tr>';
			var trdata = [tableHead];
			
			if (data.result == "ok") {
				var list = data.dataList;
				if (list == null || list.length == 0) {
					var nodata = "<tr><td colspan='4'>没有符合条件的数据</td></tr>";
					trdata.push(nodata);
					$("#user_table1").html(trdata.join(''));
					drawPage(1, 1, 9);
					return false;
				}
				for (var i = 0; i < list.length; i++) {
					trdata.push('<tr>');
						trdata.push('<td><a href="javascript:searchRule(\''+list[i].profitDetailId+'\');">');
						trdata.push(list[i].profitKeyId);
						trdata.push('</a></td><td title="'+list[i].profitRuleName+'">');
						trdata.push(list[i].profitRuleName);
						trdata.push('</td><td title="'+list[i].rootInstCd+'">');
						trdata.push(list[i].rootInstCd);
						trdata.push('</td><td title="'+list[i].funcCode+'">');
						trdata.push(list[i].funcCode);
					trdata.push('</td>');
					trdata.push('</tr>');
				}
				$("#user_table1").html(trdata.join(''));
				var totalNum = data.totalNum;
				//调用绘制分页的方法
				drawPageAgain(totalNum, pageIndex, pageSize, index);
				$('#div_table_page').show();
				$(".chant_page_num").css("display","none");
				$(".chan_table_page a[gobtn='goPage']").css("display","none");
			}else{
				var nodata = "<tr><td colspan='4'>没有符合条件的数据</td></tr>";
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
function searchRule(id){
	if(id==null){
		id = $("#profitDetailId").val();
	}else{
		$("#profitDetailId").val(id);
	}
	$.post(
		'profitmatch/search_ajax',
		'id='+id,
		function(data){
			var tableHead = "<tr>"+$("#user_table2 tr").eq(0).html()+"</tr>";
			var trdata = [tableHead];
			if (data.result == "ok") {
				var list = data.dataList;
				if (list == null || list.length == 0) {
					var nodata = "<tr><td colspan='11'>没有符合条件的数据</td></tr>";
					trdata.push(nodata);
					$("#user_table2").html(trdata.join(''));
					return false;
				}
				for (var i = 0; i < list.length; i++) {
					var profitDetailId = '';
					if(list[i].profitDetailId!='-1'){
						profitDetailId = 'checked';
					}
					if("1"==list[i].profitObject){
						list[i].profitObject = "固定";
					}else if("2"==list[i].profitObject){
						list[i].profitObject = "可变";
					}
					if("0"==list[i].profitMd){
						list[i].profitMd = "不启用";
					}else if("1"==list[i].profitMd){
						list[i].profitMd = "比例";
					}else if("2"==list[i].profitMd){
						list[i].profitMd = "固定";
					}
					if("0"==list[i].statusId){
						list[i].statusId = "无效";
					}else if("1"==list[i].statusId){
						list[i].statusId = "有效";
					}
					var matchvalue = list[i].profitDetailId+":"+list[i].subId+":"+list[i].createdTime;
					trdata.push('<tr>');
					trdata.push('<td>');
					trdata.push('<input type="checkbox" name="matchBox" '+profitDetailId+' value="'+matchvalue+'"/>');
					trdata.push('</td><td title="'+list[i].profitDetailId+'">');
					trdata.push(list[i].profitDetailId);
					trdata.push('</td><td title="'+list[i].subId+'">');
					trdata.push(list[i].subId);
					trdata.push('</td><td title="'+list[i].profitObject+'">');
					trdata.push(list[i].profitObject);
					trdata.push('</td><td title="'+list[i].rootInstCd+'">');
					trdata.push(list[i].rootInstCd);
					trdata.push('</td><td title="'+list[i].productId+'">');
					trdata.push(list[i].productId);
					trdata.push('</td><td title="'+list[i].userId+'">');
					trdata.push(list[i].userId);
					trdata.push('</td><td title="'+list[i].profitMd+'">');
					trdata.push(list[i].profitMd);
					trdata.push('</td><td title="'+list[i].profitFee+'">');
					trdata.push(list[i].profitFee);
					trdata.push('</td><td>');
					trdata.push(list[i].statusId);
					trdata.push('</td><td>');
					trdata.push('<a href=profitrule/rule_open_edit?profitDetailId='+list[i].profitDetailId+'&subId='+list[i].subId+' class="margr4"');
					trdata.push('\')">修改</a>');
					trdata.push('</td>');
					trdata.push('</tr>');
				}
				$("#user_table2").html(trdata.join(''));
			}else{
				var nodata = "<tr><td colspan='11'>没有符合条件的数据</td></tr>";
				trdata.push(nodata);
				$("#user_table2").html(trdata.join(''));
				return false;
			}
		},
		'json'
	);
}