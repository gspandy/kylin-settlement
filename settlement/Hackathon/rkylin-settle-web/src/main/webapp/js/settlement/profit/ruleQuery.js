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
			'<th width="5%">明细ID</th>' +
			'<th width="5%">子ID</th>' +
			'<th width="8%">分润对象形态</th>' +
			'<th width="10%">管理机构代码</th>' +
			'<th width="9%">产品号</th>' +
			'<th width="9%">分润对象</th>' +
			'<th width="5%">清分类型</th>' +
			'<th width="5%">必须</th>' +
			'<th width="5%">分润模式</th>' +
			'<th width="5%">模式内容</th>' +
			'<th width="5%">封顶值</th>' +
			'<th width="5%">封底值</th>' +
			'<th width="14%">生效-失效时间</th>' +
			'<th width="4%">状态</th>' +
			'<th width="5%">备注</th>' +
			'<th width="6%">操作</th>' +
			'</tr>';
			var trdata = [tableHead];
			
			if (data.result == "ok") {
				var list = data.dataList;
				if (list == null || list.length == 0) {
					var nodata = "<tr><td colspan='16'>没有符合条件的数据</td></tr>";
					trdata.push(nodata);
					$("#user_table1").html(trdata.join(''));
					drawPage(1, 1, 9);
					return false;
				}
				for (var i = 0; i < list.length; i++) {
					if("1"==list[i].profitObject){
						list[i].profitObject = "固定";
					}else if("2"==list[i].profitObject){
						list[i].profitObject = "可变";
					}
					if("0"==list[i].profitType){
						list[i].profitType = "分润";
					}else if("1"==list[i].profitType){
						list[i].profitType = "代收";
					}else if("2"==list[i].profitType){
						list[i].profitType = "代付";
					}
					if("0"==list[i].isMust){
						list[i].isMust = "否";
					}else if("1"==list[i].isMust){
						list[i].isMust = "是";
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
						trdata.push(list[i].profitDetailId);
						trdata.push('</td><td>');
						trdata.push(list[i].subId);
						trdata.push('</td><td title="'+list[i].profitObject+'">');
						trdata.push(list[i].profitObject);
						trdata.push('</td><td title="'+list[i].rootInstCd+'">');
						trdata.push(list[i].rootInstCd);
						trdata.push('</td><td title="'+list[i].productId+'">');
						trdata.push(list[i].productId);
						trdata.push('</td><td title="'+list[i].userId+'">');
						trdata.push(list[i].userId);
						trdata.push('</td><td title="'+list[i].profitType+'">');
						trdata.push(list[i].profitType);
						trdata.push('</td><td title="'+list[i].isMust+'">');
						trdata.push(list[i].isMust);
						trdata.push('</td><td title="'+list[i].profitMd+'">');
						trdata.push(list[i].profitMd);
						trdata.push('</td><td title="'+list[i].profitFee+'">');
						trdata.push(list[i].profitFee);
						trdata.push('</td><td title="'+list[i].feilvUp+'">');
						trdata.push(list[i].feilvUp);
						trdata.push('</td><td title="'+list[i].feilvBelow+'">');
						trdata.push(list[i].feilvBelow);
						trdata.push('</td><td>');
						trdata.push(startTime);
						trdata.push(" - ");
						trdata.push(endTime);
						trdata.push('</td><td>');
						trdata.push(list[i].statusId);
						trdata.push('</td><td title="'+list[i].remark+'">');
						trdata.push(list[i].remark);
						trdata.push('</td><td>');
						trdata.push('<a href=profitrule/rule_open_edit?profitDetailId='+list[i].profitDetailId+'&subId='+list[i].subId+' class="margr4"');
						trdata.push('\')">修改</a>');
						trdata.push('<a href="javascript:delSubmit(\''+list[i].profitDetailId+'\',\''+list[i].subId+'\')" class="margr4"');
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
				var nodata = "<tr><td colspan='16'>没有符合条件的数据</td></tr>";
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
function delSubmit(detailId,subId) {
	art.dialog({
	    content: '你确定要删除这掉消息吗？',
	    ok: function () {
	    	$.post(
        		'profitrule/del_ajax',			//发送请求的url
        		'profitDetailId='+detailId+'&subId='+subId,				//表单信息
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