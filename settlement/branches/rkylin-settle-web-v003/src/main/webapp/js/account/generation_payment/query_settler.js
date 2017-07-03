/***
 * 
 */
$(function() {
	funSearch(1);
	$("#btnSubmit").click(function() {
		valieQueryTime();	
	});
	//将表格标题行固定在浏览器顶部
	//var iframe1 = document.getElementById('b_iframe');
	var messenger = new Messenger('iframe1','projectName');
	messenger.listen(function (msg) {
		console.log(msg);
		fixedTableTitleForTop("user_table1", msg);
	});
	/**
	 * 构建机构号下拉菜单
	 */
	getRootInstCdSelect();
});




/***
 * 
 */
function valieQueryTime() {
	var $checkStartTime = $("#checkStartTime");
	var $checkEndTime = $("#checkEndTime");
	if($checkStartTime && $checkEndTime && $checkStartTime.val() && $checkEndTime.val()) {
		getOrdInfoAndSearch(1);
	} else {
		var $div = $("#valiDiv");
		$div.css("font-style", "italic");
		$div.css("color", "#ff0000");
		$div.html("&nbsp;请输入交易时间再进行读入操作&nbsp;");
	}
}
/***
 * 
 */
function loadingFun() {
	var butt = document.getElementById("btnSubmit");
	butt.onclick = function() {};
	butt.style.width = "100px";
	butt.innerHTML = "正在读取... ...";
}
function loadedFun() {
	var butt = document.getElementById("btnSubmit");
	butt.onclick = function(){funSearch(1);};
	butt.style.width = "60px";
	butt.innerHTML = "读取交易";
}
/***
 * ajax获取分页信息和显示数据
 */
function getOrdInfoAndSearch(pageIndex, index) {
	loadingFun();
	var pageSize = $("#dataForm #pageSize option:selected").val();		//显示条数
	$('#dataForm #pageIndex').val(pageIndex);							//赋值当前页
	$('#dataForm #pageSize').val(pageSize);								//赋值显示条数
	$.post(
		$('#dataForm').attr("action"),									//发送请求 url
		$('#dataForm').serialize(),										//表单数据
		function(data) {
			showDataTr(data, pageSize, pageIndex, index);
			loadedFun();
		},
		'json'
	);
}
/***
 * 生成代收付文件
 */
function createGenPayFile() {
	if(valiCheckForm()) {
		$("#pay_trans_form #idsStr").val(getIds());
		$.ajax({
            type: "POST",
            url: $('#pay_trans_form').attr("action"),
            data: $('#pay_trans_form').serialize(),
            dataType: "text",
            success: function(data){
				alert(data);
            },
            error:function(data){
            	alert("上传代收付系统异常！");
            }
         });
	} else {
		return;
	}
}
/***
 * 校验选择交易和验证内容
 * @returns {Boolean}
 */
function valiCheckForm() {
	var checkTransCount = document.getElementById("checkTransCount").value;
	var checkTransAmount = document.getElementById("checkTransAmount").value;
	var sumAmount = getSumAmount();
	var transCount = getTransCount();
	if(!transCount) {
		alert("请选择交易信息!");
		return false;
	} else if(!checkTransCount) {
		alert("请填写 验证内容:交易总条数!");
		return false;
	} else if(!checkTransAmount) {
		alert("请填写 验证内容:交易总金额!");
		return false;
	} else if(transCount != checkTransCount) {
		alert("交易总条数与验证内容不匹配!");
		return false;
	} else if(sumAmount != checkTransAmount) {
		alert("交易总金额与验证内容不匹配!");
		return false;
	}
	if (confirm("是否确认进行付款？")) {
		return true;
	}
}
/***
 * 获取所有选中交易ID
 */
function getIds() {
	var idArr = $(".ids_box:checked");
	var idsStr = "";
	
	for(var i = 0; i < idArr.length; i ++) {
		idsStr += ","+idArr[i].value;
	}
	idsStr = idsStr.substring(1);
	
	return idsStr;
}
/***
 * 获取所有交易金额并求和
 */
function getSumAmount() {
	//var amountsStr = document.getElementsByName("amounts");
	var amountsStr = $(".ids_box:checked ~ input[type=hidden]");
	var sumAmount = 0;
	for(var i = 0; i < amountsStr.length; i ++) {
		var amount = Number(amountsStr[i].value);
		sumAmount += amount;
	}
	return sumAmount;
}
/***
 * 获取所有选中交易总数
 */
function getTransCount() {
	return $(".ids_box:checked").length;
}
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
		function(data) {
			showDataTr(data, pageSize, pageIndex, index);
		},
		'json'
	);
}
function showDataTr(data ,pageSize, pageIndex, index) {
	//回调函数
	//jq在页面中生成展示数据
	var tableHead = 
	'<thead><tr class="tb-head-no" id="head-no">' +
	'<th ><input type="checkbox" id="all_check_box" value="" onclick="checkAll()" />ID</th>' +
	'<th>用户ID</th>' +
	'<th >业务代码</th>' +
	'<th >管理机构代码</th>' +
	'<th >订单号</th>' +
	'<th >订单类型</th>' +

	'<th >账号</th>' +
	'<th >账号名</th>' +

	'<th >开户行名称</th>' +
	'<th >金额（元）</th>' +
	'<th >发送类型</th>' +
	'<th >状态</th>' +
	'<th >错误编码</th>' +
	'<th >账号属性</th>' +
	'<th >银行代码</th>' +
	'<th >账期</th>' +
	'<th >备注</th>' +
	'<th >记录创建时间</th>' +
	'<th>记录更新时间</th>' +
	'<th >操作</th>' +
	'</tr></thead>';
	var trdata = [tableHead];
	if (data.result == "ok") {
		var list = data.dataList;
		if (list == null || list.length == 0) {
			var nodata = "<tr><td colspan='14'>没有符合条件的数据</td></tr>";
			trdata.push(nodata);
			$("#user_table1").html(trdata.join(''));
			drawPage(1, 1, 9);
			return false;
		}
		
		trdata.push('<tbody>');
		for (var i = 0; i < list.length; i++) {
			var date = new Date();
			date.setTime(list[i].accountDate);
			var createdTime = new Date();
			createdTime.setTime(list[i].createdTime);
			var updatedTime = new Date();
			updatedTime.setTime(list[i].updatedTime);
			list[i].accountDate = date.toLocaleDateString();
			list[i].createdTime = date.toLocaleDateString();
			list[i].updatedTime = date.toLocaleDateString();
			var sendType = "支付中";
			if (list[i].sendType == "1") {
				sendType = "支付失败";
			} else if (list[i].sendType == "0") {
				sendType = "支付成功";
			}
			var property = "对私";
			if (list[i].accountProperty=="1") {
				property = "对公";
			}

			if (list[i].orderType == "2") {
				list[i].orderType = "提现";
			} else if (list[i].orderType == "6") {
				list[i].orderType = "代付";
			}
			
			trdata.push('<tr onclick="selectCheck(this)">');
			trdata.push('<td>');
			trdata.push('<input type="checkbox" class="ids_box" value="'+list[i].geneId+'" name="ids" />');
			trdata.push('<input type="hidden" value="'+list[i].amount+'" name="amounts" />');
			trdata.push("<label title='"+list[i].geneId+"'>"+list[i].geneId+"</label>");
			trdata.push('</td><td>');
			trdata.push(list[i].userId);
			trdata.push('</td><td>');
			trdata.push("<label title='"+list[i].bussinessCode+"'>"+list[i].bussinessCode+"</label>");
			trdata.push('</td><td>');
			trdata.push(list[i].rootInstCd);
			trdata.push('</td><td>');
			trdata.push(list[i].orderNo);
			trdata.push('</td><td>');
			trdata.push(list[i].orderType);
			trdata.push('</td><td>');

			trdata.push(list[i].accountNo);
			trdata.push('</td><td>');
			trdata.push("<label title='"+list[i].accountName+"'>"+list[i].accountName+"</label>");
			trdata.push('</td><td>');

			trdata.push("<label title='"+list[i].openBankName+"'>"+list[i].openBankName+"</label>");
			trdata.push('</td><td>');
			trdata.push(fmoney(Number(list[i].amount) / 100, 2));
			trdata.push('</td><td>');
			trdata.push(sendType);
			trdata.push('</td><td>');
			trdata.push(list[i].statusId);
			trdata.push('</td><td>');
			trdata.push(list[i].errorCode);
			trdata.push('</td><td>');
			trdata.push(property);
			trdata.push('</td><td>');
			trdata.push(list[i].bankCode);
			trdata.push('</td><td>');
			trdata.push(list[i].accountDate);
			trdata.push('</td><td>');
			trdata.push("<label title='"+list[i].remark+"'>"+list[i].remark+"</label>");
			trdata.push('</td><td>');
			trdata.push(list[i].createdTime);
			trdata.push('</td><td>');
			trdata.push(list[i].updatedTime);
			trdata.push('</td><td>暂无');
			trdata.push('</td>');
			trdata.push('</tr>');
		}
		trdata.push('</tbody>');
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



}
/***
 * 刷新页面
 */
function resetForm(){
	funSearch(1);
}


function selectCheck(obj){
	var checkbox = $(obj).find("td").eq(0).find("input");
	if(checkbox.is(':checked')){
		checkbox.prop("checked", false);
		$(obj).find("td").removeClass("tb_style");
	}else{
		checkbox.prop("checked",true);

		$(obj).find("td").addClass("tb_style");
	}
}
/**
 * 金额格式
 * @param s 金额(元)
 * @param n 保留n位小数
 * @returns {String}
 */
function fmoney(s, n) {   
   n = n > 0 && n <= 20 ? n : 2;   
   s = parseFloat((s + "").replace(/[^\d\.-]/g, "")).toFixed(n) + "";   
   var l = s.split(".")[0].split("").reverse(),   
   r = s.split(".")[1];   
   t = "";   
   for(var i = 0; i < l.length; i ++ )   
   {   
      t += l[i] + ((i + 1) % 3 == 0 && (i + 1) != l.length ? "," : "");   
   }   
   return t.split("").reverse().join("") + "." + r;   
}
/**
 * ajax 获取机构名称和编码
 */
function getRootInstCdSelect() {
	var url = $("#getRootInstCdSelect").val();
	var isSuccess = function(data) {
		$(data).each(function() {
			$("#rootInstCd").append("<option value='"+this.parameterCode+"'>"+this.obligate3+"</option>");
		});
	};
	var dataType = "json";
	$.get(url, isSuccess, dataType);
} 
/**
 * 全部到处excl
 */
function doAllExport() {
	if(!confirm("您确定要 导出全部数据到excl文件吗?")) return;
	setTimeout(function() {
		var win = window.open($("#my_download_a").attr("href"));
		$.post(
			$('#doAllExport').val(),	//发送请求 url
			$('#dataForm').serialize(),	//表单数据
			function(data) {
				alert(data);
				win && win.close();
				window.open($("#my_download_a").attr("href"));
			},
			'text');
	 }, 1000);
}
/**
全选/反选
*/
function checkAll() {
	var isChecked = document.getElementById("all_check_box").checked;
	var checkArr = document.getElementsByName('ids');
	for(var i = 0; i < checkArr.length; i ++) {
		document.getElementsByName('ids')[i].checked = isChecked;
		
		if(isChecked){
			$("#user_table1 tbody tr").find("td").addClass("tb_style");
		}else{
			$("#user_table1 tbody tr").find("td").removeClass("tb_style");
		}
	}
}
