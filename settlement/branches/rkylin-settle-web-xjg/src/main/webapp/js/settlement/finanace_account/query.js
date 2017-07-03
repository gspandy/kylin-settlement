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
			'<tr class="tb-head-no">' +
			'<th>管理机构代码</th>' +
			'<th>账户ID</th>' +
			'<th>账户类型ID</th>' +
			'<th>账户名称</th>' +
			'<th>账户编码</th>' +
			'<th>账户ID</th>' +
			'<th>管理分组</th>' +
			'<th>核算分组</th>' +
			'<th>第三方账户ID</th>' +
			'<th>币种</th>' +
			'<th>账户余额(分)</th>' +
			'<th>可用余额</th>' +
			'<th>清算余额/可提现余额</th>' +
			'<th>冻结余额</th>' +
			'<th>透支额度</th>' +
			'<th>贷记余额</th>' +
			'<th>生效时间</th>' +
			'<th>失效时间</th>' +
			'<th>账户业务控制</th>' +
			'<th>开户备注</th>' +
			'<th>账户状态,0失效,1生效</th>' +
			'<th>记录安全码</th>' +
			'<th>读取账期</th>' +
			'<th>记录创建时间</th>' +
			'<th>记录更新时间</th>' +
			'</tr>';
			var trdata = [tableHead];
			
			if (data.result == "ok") {
				var list = data.dataList;
				if (list == null || list.length == 0) {
					var nodata = "<tr><td colspan='25'>没有符合条件的数据</td></tr>";
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
					
					trdata.push('<tr>');
						trdata.push('<td title="'+list[i].rootInstCd+'">');
						trdata.push(list[i].rootInstCd);
						trdata.push('</td><td title="'+list[i].finAccountId+'">');
						trdata.push(list[i].finAccountId);
						trdata.push('</td><td title="'+list[i].finAccountTypeId+'">');
						trdata.push(list[i].finAccountTypeId);
						trdata.push('</td><td title="'+list[i].finAccountName+'">');
						trdata.push(list[i].finAccountName);
						trdata.push('</td><td title="'+list[i].accountCode+'">');
						trdata.push(list[i].accountCode);
						trdata.push('</td><td title="'+list[i].accountRelateId+'">');
						trdata.push(list[i].accountRelateId);
						trdata.push('</td><td title="'+list[i].groupManage+'">');
						trdata.push(list[i].groupManage);
						trdata.push('</td><td title="'+list[i].groupSettle+'">');
						trdata.push(list[i].groupSettle);
						trdata.push('</td><td title="'+list[i].referUserId+'">');
						trdata.push(list[i].referUserId);
						trdata.push('</td><td title="'+list[i].currency+'">');
						trdata.push(list[i].currency);
						trdata.push('</td><td title="'+list[i].amount+'">');
						trdata.push(list[i].amount);
						trdata.push('</td><td title="'+list[i].balanceUsable+'">');
						trdata.push(list[i].balanceUsable);
						trdata.push('</td><td title="'+list[i].balanceSettle+'">');
						trdata.push(list[i].balanceSettle);
						trdata.push('</td><td title="'+list[i].balanceFrozon+'">');
						trdata.push(list[i].balanceFrozon);
						trdata.push('</td><td title="'+list[i].balanceOverLimit+'">');
						trdata.push(list[i].balanceOverLimit);
						trdata.push('</td><td title="'+list[i].balanceCredit+'">');
						trdata.push(list[i].balanceCredit);
						trdata.push('</td><td title="'+toLocaleStringByTime(list[i].startTime)+'">');
						trdata.push(toLocaleStringByTime(list[i].startTime));
						trdata.push('</td><td title="'+toLocaleStringByTime(list[i].endTime)+'">');
						trdata.push(toLocaleStringByTime(list[i].endTime));
						trdata.push('</td><td title="'+list[i].bussControl+'">');
						trdata.push(list[i].bussControl);
						trdata.push('</td><td title="'+list[i].remark+'">');
						trdata.push(list[i].remark);
						trdata.push('</td><td title="'+list[i].statusId+'">');
						trdata.push(list[i].statusId);
						trdata.push('</td><td title="'+list[i].recordMap+'">');
						trdata.push(list[i].recordMap);
						trdata.push('</td><td title="'+toLocaleStringByTime(list[i].accountDate)+'">');
						trdata.push(toLocaleStringByTime(list[i].accountDate));
						trdata.push('</td><td title="'+toLocaleStringByTime(list[i].createdTime)+'">');
						trdata.push(toLocaleStringByTime(list[i].createdTime));
						trdata.push('</td><td title="'+toLocaleStringByTime(list[i].updatedTime)+'">');
						trdata.push(toLocaleStringByTime(list[i].updatedTime));
						trdata.push('</td>');
					trdata.push('</tr>');
				}
				$("#user_table1").html(trdata.join(''));
				var totalNum = data.totalNum;
				//调用绘制分页的方法
				drawPageAgain(totalNum, pageIndex, pageSize, index);
				$('#div_table_page').show();
			}else{
				var nodata = "<tr><td colspan='25'>没有符合条件的数据</td></tr>";
				trdata.push(nodata);
				$("#user_table1").html(trdata.join(''));
				drawPage(1, 1, 9);
				return false;
			}
		},
		'json'
	);
}
function toLocaleStringByTime(time) {
	var date = new Date();
	if(time != null){
		date.setTime(time);
		return date.toLocaleDateString();
	} else {
		return '';
	}
}
/***
 * 刷新页面
 */
function resetForm(){
	funSearch(1);
}

/***
 * 再次汇总40142交易生成代付交易 ajax请求
 */
function withholdfor40142() {
	var url = $("#withholdfor40142btn").val();
	var data = '';
	var callBack = function(data) {
		if(data == 'success') {
			art.dialog.tips("<span style='color:green'>40142汇总执行成功!</span>", 2.5, function(){});
		} else {
			art.dialog.tips("<span style='color:red'>40142汇总执行失败!</span>", 2.5, function(){});
		}
	};
	var type = "text";
	$.post(url, data, callBack, type);
}


