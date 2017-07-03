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
			'filematch/match_operation_ajax',
			"matchVal="+param+"&id="+$("#fileSubId").val(),
			function(data){
				art.dialog.tips(data.msg, 1.5, searchColumn);
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
			'<th width="10%">ID</th>' +
			'<th width="30%">文件模板名称</th>' +
			'<th width="20%">管理机构代码</th>' +
			'<th width="25%">功能码</th>' +
			'<th width="15%">列信息码</th>' +
			'</tr>';
			var trdata = [tableHead];
			
			if (data.result == "ok") {
				var list = data.dataList;
				if (list == null || list.length == 0) {
					var nodata = "<tr><td colspan='5'>没有符合条件的数据</td></tr>";
					trdata.push(nodata);
					$("#user_table1").html(trdata.join(''));
					drawPage(1, 1, 9);
					return false;
				}
				for (var i = 0; i < list.length; i++) {
					trdata.push('<tr>');
						trdata.push('<td><a href="javascript:searchColumn(\''+list[i].fileSubId+'\');">');
						trdata.push(list[i].fileId);
						trdata.push('</a></td><td title="'+list[i].fileName+'">');
						trdata.push(list[i].fileName);
						trdata.push('</td><td title="'+list[i].rootInstCd+'">');
						trdata.push(list[i].rootInstCd);
						trdata.push('</td><td title="'+list[i].funcCodes+'">');
						trdata.push(list[i].funcCodes);
						trdata.push('</td><td>');
						trdata.push(list[i].fileSubId);
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
				var nodata = "<tr><td colspan='5'>没有符合条件的数据</td></tr>";
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
function searchColumn(id){
	if(id==null){
		id = $("#fileSubId").val();
	}else{
		$("#fileSubId").val(id);
	}
	$.post(
		'filematch/search_ajax',
		'id='+id,
		function(data){
			var tableHead = "<tr>"+$("#user_table2 tr").eq(0).html()+"</tr>";
			var trdata = [tableHead];
			if (data.result == "ok") {
				var list = data.dataList;
				if (list == null || list.length == 0) {
					var nodata = "<tr><td colspan='11'>没有符合条件的数据</td></tr>";
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
					var fileSubId = '';
					if(list[i].fileSubId!='-1'){
						fileSubId = 'checked';
					}
					trdata.push('<tr>');
						trdata.push('<td>');
						trdata.push('<input type="checkbox" name="matchBox" '+fileSubId+' value="'+list[i].fileColumnId+'"/>');
						trdata.push('</td><td title="'+list[i].fileColumnId+'">');
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
						trdata.push('</td><td>');
						trdata.push(list[i].statusId);
						trdata.push('</td><td>');
						trdata.push('<a href=filecolumn/column_open_edit?id='+list[i].fileColumnId+' class="margr4"');
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