var select_common = {
	fillDataTypeInfoFun:function(type,otherParams){
		var url = "common/dataManage.do?action=getCommonData";
		var params = "type="+type+"Data"+"&"+otherParams;
		web_plugin_common.findDataNotSession(url, params, "select_common.fileDataFuns","false",type);
	},fileDataFuns:function(data,type){
		var msg = data.message;
		var nameValue = $("#"+type).val();
		var jsonObj = eval(msg);
		var html = "";
		$("#"+type+"Div").html(html);
		for (var i = 0; i < jsonObj.length; i++) {
			if(jsonObj[i].name == nameValue){
				html = html + '<a style="cursor:pointer" id="'+type+i+'" value="'+jsonObj[i].code+'" class="sel_check" onclick=javascript:select_common.checkDateTypeFun("'+type+'","'+type+i+'")>'+jsonObj[i].name+'</a>';
			}else{
				html = html + '<a style="cursor:pointer" id="'+type+i+'" value="'+jsonObj[i].code+'" onclick=javascript:select_common.checkDateTypeFun("'+type+'","'+type+i+'")>'+jsonObj[i].name+'</a>';
			}
		}
		$("#"+type+"Div").html(html);
	},checkDateTypeFun:function(type,id){
		$("#"+type).val($("#"+id).html());
		$("#"+type+"H").val($("#"+id).attr("value"));
		$("#"+type+"Div").hide();
		$("#"+type).attr("class","wid284 ip_span_con");
		$("#"+type).blur();
		if(type == "province"){
			$("#city").val("请选择");
			var type = "city";
			var html = '<a id="'+type+'0" value="" class="sel_check" onclick=javascript:select_common.checkDateTypeFun("'+type+'","'+type+'0")>请选择</a>';
			$("#cityDiv").html(html);
			$("#cityH").val("");
		}
	}
}