/***
 * 
 */
$(function() {
	funSearch(1,"index");
	
	//交易类型赋初值-1，指全部
	$("#readType").val("-1");
	
	//监听支付渠道的change事件，当支付渠道变化时，级联出对应的交易类型
	$("#payChannelId").on("change",function(){
		if($("#payChannelId").val()=="04"){
			$("#readType").val($("#llreadType").val());
			$("#tldiv").hide();
			$("#lldiv").show();
		}else{
			$("#readType").val($("#tlreadType").val());
			$("#tldiv").show();
			$("#lldiv").hide();
		}
	});
	
	//通联的交易类型变化时，将值赋给readType(交易类型)
	$("#tlreadType").on("change",function(){
		$("#readType").val($("#tlreadType").val());
	});
	//连连的交易类型变化时，将值赋给readType(交易类型)
	$("#llreadType").on("change",function(){
		$("#readType").val($("#llreadType").val());
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
			'<th width="5%">ID</th>' +
			'<th width="10%">文件模板名称</th>' +
			'<th width="8%">管理机构代码</th>' +
			'<th width="10%">功能码</th>' +
			'<th width="5%">支付渠道</th>' +
			'<th width="5%">交易类型</th>' +
			'<th width="13%">文件名称前缀</th>' +
			'<th width="5%">文件后缀</th>' +
			'<th width="5%">文件作用</th>' +
			'<th width="22%">有效时间</th>' +
			'<th width="5%">状态</th>' +
			'<th width="7%">操作</th>' +
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
					if("01"==list[i].payChannelId){
						list[i].payChannelId = "通联";
					}else if("02"==list[i].payChannelId){
						list[i].payChannelId = "支付宝";
					}
					if(list[i].readType=="01"){
						list[i].readType = "网关支付";
					}else if(list[i].readType=="02"){
						list[i].readType = "代收付";
					}else{
						list[i].readType = "无";
					}
					if("1"==list[i].fileType){
						list[i].fileType = "写入文件模板";
					}else if("2"==list[i].payChannelId){
						list[i].fileType = "读取文件模板";
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
						trdata.push(list[i].fileId);
						trdata.push('</td><td title="'+list[i].fileName+'">');
						trdata.push(list[i].fileName);
						trdata.push('</td><td title="'+list[i].rootInstCd+'">');
						trdata.push(list[i].rootInstCd);
						trdata.push('</td><td title="'+list[i].funcCodes+'">');
						trdata.push(list[i].funcCodes);
						trdata.push('</td><td title="'+list[i].payChannelId+'">');
						trdata.push(list[i].payChannelId);
						trdata.push('</td><td title="'+list[i].readType+'">');
						trdata.push(list[i].readType);
						trdata.push('</td><td title="'+list[i].filePrefix+'">');
						trdata.push(list[i].filePrefix);
						trdata.push('</td><td title="'+list[i].filePostfix+'">');
						trdata.push(list[i].filePostfix);
						trdata.push('</td><td title="'+list[i].fileType+'">');
						trdata.push(list[i].fileType);
						trdata.push('</td><td>');
						trdata.push(startTime);
						trdata.push(" - ");
						trdata.push(endTime);
						trdata.push('</td><td>');
						trdata.push(list[i].statusId);
						trdata.push('</td><td>');
						trdata.push('<a href=file/file_open_view?id='+list[i].fileId+' class="margr4"');
						trdata.push('\')">详情</a>');
						trdata.push('<a href=file/file_open_edit?id='+list[i].fileId+' class="margr4"');
						trdata.push('\')">修改</a>');
						trdata.push('<a href="javascript:delSubmit(\''+list[i].fileId+'\')" class="margr4"');
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
/***
 * 删除信息的表单提交
 */
function delSubmit(id) {
	art.dialog({
	    content: '你确定要删除这掉消息吗？',
	    ok: function () {
	    	$.post(
        		'file/del_ajax',			//发送请求的url
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