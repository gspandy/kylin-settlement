/***
 * onload自动执行
 */
$(function() {
	queryParameterInfo("getPayChannelIdAndName","payChannelId");//获取渠道
	queryParameterInfo("getProtocol","merchantCode");//获取协议
});

/**
 * 从settle_parameter_info获取code和中文的对应关系
 */
function queryParameterInfo(urlId,selectId){
	var url = $('#'+urlId).val();
	$.post(
		url, 
		{}, 
		function(data) {
			if(data !="" && data != null){
				var oData = JSON.parse(data);
				var arr = oData.dataList;
				var htmlSelect ="<option value=''>全部</option>";
				for(var i in arr){
					htmlSelect += "<option value="+arr[i].parameterCode+">"+arr[i].obligate3+"</option>";
				}
				$("#"+selectId).html(htmlSelect);
			}
		}, 
		'text'
	);
}

var idArr= [];
function query(){
	idArr = [];
	var duankuanArr= [];
	var url = $('#query_ajax').val();
	var accountDate = $("#accountDate").val();
	if(accountDate ==null || accountDate==""){
		art.dialog.tips("<span style='color:red'>账期不能为空！</span>", 3, function(){});
		return false;
	}
	var merchantCode = $("#merchantCode").val();
	var payChannelId = $("#payChannelId").val();
	$.post(
			url,									//发送请求 url
			{"accountDate":accountDate,"merchantCode":merchantCode,"payChannelId":payChannelId},										//表单数据
			function(data) {
				//回调函数
				//jq在页面中生成展示数据
				var tableHead = 
				'<tr  class="tb-head-no" id="head-no">' +
				'<th >ID</th>' +
				'<th >账期</th>' +
				'<th >渠道</th>' +
				'<th >协议</th>' +
				'<th >支付方式</th>' +
				'<th >退款笔数</th>' +
				'<th >退款金额(元)</th>' +
				'<th >支付笔数</th>' +
				'<th >支付金额(元)</th>'+
				'<th >渠道结算额(元)</th>'+
				'<th >手续费</th>'+
				'<th >对账结果</th>'+
				'<th >差额(元)</th>'+
				'<th >备注</th>'+
				'</tr>';
				var trdata = [tableHead];
				if (data != "" && data !=null) {
					data = JSON.parse(data);
					var list = data.dataList;
					if (list == null || list.length == 0) {
						var nodata = "<tr><td colspan='9'>没有符合条件的数据</td></tr>";
						trdata.push(nodata);
						$("#user_table1").html(trdata.join(''));
						drawPage(1, 1, 9);
						return false;
					}
					for (var i = 0; i < list.length; i++) {
						var obj = list[i];
						var date = new Date();
						date.setTime(obj.accountDate);
						var id = obj.id;
						idArr.push(id);
						trdata.push('<tr>');
						trdata.push('<td>');
						trdata.push(obj.id);
						trdata.push('</td><td>');
						trdata.push(date.toLocaleDateString() );
						trdata.push('</td><td>');
						trdata.push(obj.payChannelId);
						trdata.push('</td><td>');
						trdata.push(obj.merchantCode);
						trdata.push('</td><td>');
						trdata.push(obj.readType);
						trdata.push('</td><td>');
						if(obj.count1 ==null || obj.count1==""){
							trdata.push(0);
						}else{
							trdata.push(obj.count1);
						}
						trdata.push('</td><td>');
						if(obj.amount1 ==null || obj.amount1==""){
							trdata.push("0.00");
						}else{
							trdata.push(parseFloat(obj.amount1/100).toFixed(2));
						}
						trdata.push('</td><td>');
						trdata.push(obj.count2);
						trdata.push('</td><td>');
						if(obj.amount2 !=null && obj.amount2 != ""){
							trdata.push('<span name="amount2" id="amount2'+id+'">'+parseFloat(obj.amount2/100).toFixed(2)+'</span>');
						}else{
							trdata.push('<span name="amount2" id="amount2'+id+'">'+"0.00"+'</span>');
						}
					
						trdata.push('</td><td>');
						if(obj.amount3 !="" && obj.amount3 !=null && obj.amount3 !="null"){
							trdata.push('<input type="text" style="border:1px solid" value="'+parseFloat(obj.amount3/100).toFixed(2)+'" onchange="collate('+id+')" name="amount3" id="amount3'+id+'"/>');
						}else{
							trdata.push('<input type="text" style="border:1px solid" value="" onchange="collate('+id+')" name="amount3" id="amount3'+id+'"/>');
						}
						
						trdata.push('</td><td>');
						if(obj.feeAmount !="" && obj.feeAmount !=null){
							trdata.push(parseFloat(obj.feeAmount/100).toFixed(2));
						}else{
							trdata.push("0.00");
						}
						
						trdata.push('</td><td>');
						if(obj.accountResult !=null && obj.accountResult !=""){
							trdata.push('<span  value="" name="accountResult" id="accountResult'+id+'">'+obj.accountResult+'</span>');
						    if("短款" == obj.accountResult){
						    	duankuanArr.push("accountResult"+id);
						    }
						}else{
							trdata.push('<span  value="" name="accountResult" id="accountResult'+id+'"/>');
						}
						trdata.push('</td><td>');
						if(obj.amount4 !=null && obj.amount4 !=""){
							trdata.push('<span  value="" name="amount4" id="amount4'+id+'">'+parseFloat(obj.amount4/100).toFixed(2)+'</span>');
						}else{
							trdata.push('<span  value="" name="amount4" id="amount4'+id+'"/>');
						}
						
						trdata.push('</td><td>');
						if(obj.remark !="" && obj.remark !=null){
							trdata.push('<input type="text" style="border:1px solid" value="'+obj.remark+'" name="remark" id="remark'+id+'"/>');
						}else{
							trdata.push('<input type="text" style="border:1px solid" value="" name="remark" id="remark'+id+'"/>');
						}
						
						trdata.push('</td>');
						trdata.push('</tr>');
					}
					$("#user_table1").html(trdata.join(''));
					var totalNum = data.totalNum;
					//调用绘制分页的方法
					drawPageAgain(totalNum, 1, 100, "index");
					$('#div_table_page').show();
					
					$(".tip").each(function(){
						$(this).attr("class","");
					});
					
					for(var m in duankuanArr){
						var daccountResultId = duankuanArr[m];
						$("#"+daccountResultId).attr("class","tip");
					}
				}else{
					var nodata = "<tr><td colspan='14'>没有符合条件的数据</td></tr>";
					trdata.push(nodata);
					$("#user_table1").html(trdata.join(''));
					drawPage(1, 1, 9);
					return false;
				}
				fixedTableTitleForTop("user_table1");
			},
			'text'
		);
}

//监听手工输入的结算金额的变化调整差额和对账结果
function collate(id){
	$("#accountResult"+id).attr("class","");
	var amount2 = $("#amount2"+id).text();//支付金额
	var amount3 = $("#amount3"+id).val();;//结算金额
	if(amount3 !="" && amount3 !=null){
		$("#amount3"+id).val(parseFloat(amount3).toFixed(2));
	}
	var accountResult = "";
	var amount4 = amount2 - amount3;//差额
	if(amount4 == 0){
		accountResult = "平账";
	}
	if(amount4 >0){
		accountResult = "短款";
		$("#accountResult"+id).attr("class","tip");
	}
	if(amount4 < 0){
		accountResult = "长款";
	}
	$("#accountResult"+id).text(accountResult);
	
	$("#amount4"+id).text(amount4.toFixed(2));
}

/**
 * 对账
 */
function duizhang(){
	$(".tip").each(function(){
		$(this).attr("class","");
	});
	var id =null;
	var amount3=null;
	if(idArr !=null && idArr.length>0){
		for(var index in idArr){
			id = idArr[index];
			amount3 = $("#amount3"+id).val();
			if(amount3 !=null && amount3 !="" && amount3 !="null"){
				$("#amount3"+id).val(parseFloat(amount3).toFixed(2));
				var amount2 = $("#amount2"+id).text();//支付金额
				var amount3 = $("#amount3"+id).val();;//结算金额
				var accountResult = "";
				var amount4 = amount2 - amount3;//差额
				if(amount4 == 0){
					accountResult = "平账";
				}
				if(amount4 >0){
					accountResult = "短款";
					$("#accountResult"+id).attr("class","tip");
				}
				if(amount4 < 0){
					accountResult = "长款";
				}
				$("#accountResult"+id).text(accountResult);
				
				$("#amount4"+id).text(amount4.toFixed(2));
			}
		}
	}
	art.dialog.tips("<span style='color:red'>页面对账完成(注意：未保存到数据库）！</span>", 3, function(){});
}

/**
 * 保存
 */
function save(){
	var url = $('#save_ajax').val();
	var updateArr =[];
	var id =null;
	var amount3=null;
	if(idArr !=null && idArr.length>0){
		for(var index in idArr){
			id = idArr[index];
			amount3 = $("#amount3"+id).val();
			if(amount3 !=null && amount3 !="" && amount3 !="null"){
				var obj ={};
				obj.id=id;
				obj.accountResult = $("#accountResult"+id).text();
				var amount4 = $("#amount4"+id).text();
				if(amount4=="" || amount4==null ){
					amount4 = 0;
				}
				obj.amount4 = amount4*100;
				obj.remark = $("#remark"+id).val();
				obj.amount3 = amount3*100;
				updateArr.push(obj);
			}
		}
	}

	if(updateArr !=null && updateArr.length>0){
		$.post(
				url,									//发送请求 url
				{"updateData":JSON.stringify(updateArr)},
				function(data) {
					if("success" == data){
						art.dialog.tips("<span style='color:red'>保存成功</span>", 2.5, function(){});
					}else{
						art.dialog.tips("<span style='color:red'>哎呀，保存失败啦！</span>", 2.5, function(){});
					}
				},
				'text'
		 );
	}else{
		art.dialog.tips("<span style='color:red'>渠道金额未进行人工输入或更改！</span>", 2.5, function(){});
	}
}
