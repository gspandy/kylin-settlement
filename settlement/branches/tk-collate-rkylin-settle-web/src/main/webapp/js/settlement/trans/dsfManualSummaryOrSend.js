
/***
 * 汇总
 */
var summaryIdFlag = true;
function summary() {
	var dataSource = $("#dataSource").val();
	if("0" == dataSource){
		art.dialog.tips("<span style='color:red'>数据源是“文件导入”无需汇总!</span>", 2.5, function(){});
		return;
	}
	var url = $("#summary").val();
	var rootInstCds = $("#rootInstCds").val();
	var flag = $("#flag").val();
	if(flag == "2" && rootInstCds !="M000004"){
		art.dialog.tips("<span style='color:green'>仅限机构是'课栈'的具有T0提现汇总功能！！</span>", 2.5, function(){});
		return;
	}
	if(summaryIdFlag){
		summaryIdFlag = false;
		var data = {"inRootInstCds":rootInstCds,"inFlag":flag};
		var callBack = function(data) {
			summaryIdFlag = true;
			if(data == 'success') {
				art.dialog.tips("<span style='color:green'>汇总操作执行成功!</span>", 2.5, function(){});
			} else {
				art.dialog.tips("<span style='color:red'>汇总操作执行失败!</span>", 2.5, function(){});
			}
		};
		var type = "text";
		$.get(url, data, callBack, type);
	}else{
		art.dialog.tips("<span style='color:green'>不能重复提交!!!</span>", 2.5, function(){});
	}
	
}
/***
 * 发送代收付
 */
var sendDsfFlag = true;
var validateResult = true;
function sendDsf() {
	if(sendDsfFlag){
		sendDsfFlag = false;
		var url = $("#sendDsf").val();
		var dataSource = $("#dataSource").val();
		var rootInstCds = $("#rootInstCds").val();
		var flag = $("#flag").val();
		var data = {"dataSource":dataSource,"inRootInstCds":rootInstCds,"inFlag":flag};
		
		var money = $("#money").val();
		var totalNum = $("#totalNum").val();
		if(!money){
            art.dialog.tips("<span style='color:green'>人工确认金额不能为空！</span>", 2.5, function(){});
			return false;
		}
		if(!totalNum){
			art.dialog.tips("<span style='color:green'>人工确认的条数不能为空！</span>", 2.5, function(){});
			return false;
		}
		
		var validateUrl = $("#validateUrl").val();
		var validateData = {"dataSource":dataSource,"inRootInstCds":rootInstCds,"inFlag":flag,"totalNum":totalNum,"money":money};
		
		var validateCallBack = function(data) {
			if(data != 'success') {
				sendDsfFlag = true;
				validateResult = false;
				art.dialog.tips("<span style='color:green'>手工输入的金额或条数跟系统统计的不匹配!</span>", 2, function(){});
			}else{
				var callBack = function(data) {
					sendDsfFlag = true;
					if(data == 'success') {
						art.dialog.tips("<span style='color:green'>发送代收付成功!</span>", 2.5, function(){});
					} else {
						art.dialog.tips("<span style='color:red'>发送代收付失败!</span>", 2.5, function(){});
					}
				};
				$.get(url, data, callBack, type);
			}
		};
		var type = "text";
		$.get(validateUrl, validateData, validateCallBack, type);//校验人工确认金额和人工确认条数是否正确
		
		if(!validateResult){
			return false;
		}
	}else{
		art.dialog.tips("<span style='color:red'>不能重复发送!</span>", 2.5, function(){});
	}
	
}
