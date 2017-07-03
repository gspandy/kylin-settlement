
/***
 *从代收付系统读取代收付结果
 */
var sendDsfFlag = true;
function getDsfResult(){
	
	if(sendDsfFlag){
		sendDsfFlag = false;
		var url = $("#getResultFromDSF").val();
		var rootInstCd = $("#rootInstCd").val();
		//var businessCode = $("#businessCode").val();
		var requestNo = $("#requestNo").val();
		var orderNos = $("#orderNos").val();
		var accountDate = $("#accountDate").val();
		
		var data = {"rootInstCd":rootInstCd,"businessCode":null,"requestNo":requestNo,"orderNos":orderNos,"accountDate":accountDate};
		
		var callBack = function(data) {
			if(data != 'success') {
				sendDsfFlag = true;
				art.dialog.tips("<span style='color:green'>从代收付系统读取结果失败</span>", 2, function(){});
			}else{
				var callBack = function(data) {
					sendDsfFlag = true;
					if(data == 'success') {
						art.dialog.tips("<span style='color:green'>从代收付系统读取结果成功!</span>", 2.5, function(){});
					} else {
						art.dialog.tips("<span style='color:red'>从代收付系统读取结果失败!</span>", 2.5, function(){});
					}
				};
				$.get(url, data, callBack, type);
			}
		};
		var type = "text";
		$.get(url, data, callBack, type);
		
	}else{
		art.dialog.tips("<span style='color:red'>上次读取结果未返回，请耐心等待!</span>", 2.5, function(){});
	}
	
}
