
/**
 * 通用方法调用对象
 * 
 * fun1 isEmpty  判断对象是否为空，不存在，空字符
 * param 任何对象
 * return boolean
 * 
 * fun2 trim     去掉字串的前后空格
 * 
 * fun3 findData 普通异步调用方法
 * param url：异步地址  params：异步参数 methodFlag：方法标识，用来区分同个页面里其他的异步方法
 * return 实现callback方法
 *
 * fun4 findDataByType 特殊组件异步调用方法
 * param url：异步地址  params：异步参数 methodType：特殊组件类型 methodFlag：方法标识，用来区分同个页面里其他的异步方法 
 * return 实现pageCallback方法 
 *
 */
//table中td显示...内容的方法
	function getBytesCount(str) 
	{ 
		if (str == null) 
		{ 
			return 0; 
		} 
			else 
		{ 
			return (str.length + str.replace(/[\u0000-\u00ff]/g, "").length); 
		} 
	} 

	$("td").bind("mouseover",function(){
		var ht = $(this).html().trim();
		var w = $(this).width();
		var l = getBytesCount(ht) * 8;
		if($(this).parents("table").html().trim().indexOf("TH") > -1 || $(this).parents("table").html().indexOf("th") > -1){
			if(l > w && ht.indexOf("A") < 0 &&  ht.indexOf("a") < 0){
				$(this).attr("title",ht);
			}
		}
	});




var web_plugin_common = {

	constant:{
		//分页列表的回调方法标志
		PAGE_CALLBACK:"page",
		noDataWarningStyle:"<p class='lnosam'>暂无数据</p>"
	},
	isEmpty:function(obj){//0 false 都返回true
		if(obj == undefined || obj == "" || obj == null){
			return true;
		}
		return false;		
	},
	isEmptyByXml:function(obj){
		if(obj === 0){
			return false;
		}
		if(obj === false){
			return false;
		}
		if(obj == undefined || obj == "" || obj == null){
			return true;
		}
		return false;		
	},
	trim:function(str){
		if(!this.isEmpty(str)){
			return str.replace(/(^\s*)|(\s*$)/g, "");
		}else{
			return "";
		}
	},
	findDataNotSession:function(url, params, methodFlag, errorPrompt, otherParams){
		//异步标识
		var searchParameter = "async_flag=true";
		if(!this.isEmpty(this.trim(params))){
			searchParameter += ("&" + params);
		}
		//默认为错误提示
		if(web_plugin_common.isEmpty(errorPrompt)){
			errorPrompt = true;
		}else{
			errorPrompt = false;
		}
		jQuery.post(url,searchParameter,function call(data){
			var msg = data.message;
			if(msg){
				var targetUrl = web_plugin_common.trim(data.target_url);
				if(msg == "access_denied" && !web_plugin_common.isEmpty(targetUrl)){
					window.location.href = targetUrl;
					return;
				}else if(msg == "repeat_login" && !web_plugin_common.isEmpty(targetUrl)){
					//alert("您的账号已经在其他地方登录");
					window.location.href = targetUrl;
					return;
				}else if(msg == "no_login" && !web_plugin_common.isEmpty(targetUrl)){
					alert("用户已过期，请重新登录!");
					window.location.href = targetUrl;
					return;
				}
				if(errorPrompt){
					alert(msg);
					return;
				}
			}
			if(!web_plugin_common.isEmpty(methodFlag)){
				//回调方法
				var selfParam = methodFlag.split(".");
				if(selfParam.length == 1){
					eval( "(" +methodFlag+"("+data+","+otherParams+")"+")" );
				}else if(selfParam.length == 2){
					eval( "(" +selfParam[0]+")" )[selfParam[1]](data,otherParams);
				}
			}
			
		},"json");
	},
	findData:function(url, params, methodFlag, errorPrompt, otherParams){
		//异步标识
		var searchParameter = "async_flag=true";
		if(!this.isEmpty(this.trim(params))){
			searchParameter += ("&" + params);
		}
		//默认为错误提示
		if(web_plugin_common.isEmpty(errorPrompt)){
			errorPrompt = true;
		}else{
			errorPrompt = false;
		}
		jQuery.post(url,searchParameter,function call(data){
			var msg = data.message;
			if(msg){
				var targetUrl = web_plugin_common.trim(data.target_url);
				if(msg == "access_denied" && !web_plugin_common.isEmpty(targetUrl)){
					window.location.href = targetUrl;
					return;
				}else if(msg == "repeat_login" && !web_plugin_common.isEmpty(targetUrl)){
					window.location.href = targetUrl;
					return;
				}else if(msg == "no_login" && !web_plugin_common.isEmpty(targetUrl)){
					window.location.href = targetUrl;
					return;
				}
				if(errorPrompt){
					alert(msg);
					return;
				}
			}
			if(!web_plugin_common.isEmpty(methodFlag)){
				//回调方法
				var selfParam = methodFlag.split(".");
				if(selfParam.length == 1){
					eval( "(" +methodFlag+"("+data+","+otherParams+")"+")" );
				}else if(selfParam.length == 2){
					eval( "(" +selfParam[0]+")" )[selfParam[1]](data,otherParams);
				}
			}
		},"json");
	},
	findDataByType:function(url, params, methodType, methodFlag){
		if(this.isEmpty(methodType)){
			alert("methodType参数传递有误");
			return;
		}
		//异步标识
		var searchParameter = "async_flag=true";
		if(!this.isEmpty(this.trim(params))){
			searchParameter += ("&" + params);
		}
		jQuery.post(url,searchParameter,function call(data){
			var msg = data.message;
			if(msg){
				var targetUrl = web_plugin_common.trim(data.target_url);
				if(msg == "access_denied" && !web_plugin_common.isEmpty(targetUrl)){
					alert("对不起，您没有该操作的权限");
					//window.location.href = targetUrl;
				}else if(msg == "repeat_login" && !web_plugin_common.isEmpty(targetUrl)){
					alert("您的账号已经在其他地方登陆");
					//window.location.href = targetUrl;
				}else if(msg == "no_login" && !web_plugin_common.isEmpty(targetUrl)){
					alert("用户已过期,请重新登录！");
					window.location.href = targetUrl;
					return;
				}else{
					alert(msg);
				}
				return;
			}
			//判断调用次方法的类型
			if(methodType == web_plugin_common.constant.PAGE_CALLBACK){//列表调用
				web_plugin_common.pageCallback(data, methodFlag);
			}
		},"json");
	},
	
	//解析序列化转码
	serialize:function(params){
		var parmArray = params.split("&");
		var parmStringNew="";
		$.each(parmArray,function(index,data){
			var li_pos = data.indexOf("=");	
			if(li_pos >0){
				var name = data.substring(0,li_pos);
				var value = encodeURIComponent(data.substr(li_pos+1));
				var parm = name+"="+value;
				parmStringNew = parmStringNew=="" ? parm : parmStringNew + '&' + parm;
			}
		});
		return parmStringNew;
	},
	
	//得到日期字符串 yyyyMMdd
	getDayStr:function(today){
		var year = today.getFullYear();
		var monthStr = today.getMonth()+1;
		var month = monthStr>9?monthStr:"0"+monthStr;
		var date = today.getDate();
		if(date< 10){
			date = "0"+date;
		}
		var theDate = year+""+month+""+date;
		//alert(theDate);
		return theDate;
	},
	
	//得到当前日期字符串 yyyy-MM-dd
	getTodayStr:function(today){
		var year = today.getFullYear();
		var monthStr = today.getMonth()+1;
		var month = monthStr>9?monthStr:"0"+monthStr;
		var date = today.getDate();
		if(date< 10){
			date = "0"+date;
		}
		var theDate = year+"-"+month+"-"+date;
		return theDate;
	},
	
	//得到日期字符串 yyyy-MM-dd
	getNextDayStr:function(today){
		var year = today.getFullYear();
		var monthStr = today.getMonth()+1;
		var month = monthStr>9?monthStr:"0"+monthStr;
		var date = today.getDate()+1;
		if(date< 10){
			date = "0"+date;
		}
		var theDate = year+"-"+month+"-"+date;
		return theDate;
	},
	
	//得到时间字符串 HHmmss
	getTimestampStr:function(today){
		var hourStr = today.getHours();
		var minuteStr = today.getMinutes();
		var secondStr = today.getSeconds();
		var hour = hourStr>9?hourStr:"0"+hourStr;
		var minute = minuteStr>9?minuteStr:"0"+minuteStr;
		var second = secondStr>9?secondStr:"0"+secondStr;
		var timestamp = hour+""+minute+""+second;
		return timestamp;
	},
	
	//得到时间字符串yyyyMMddHHmmSS
	getDayAndTimestampStr:function(today){
		var theDate = web_plugin_common.getDayStr(today);
		var timestamp = web_plugin_common.getTimestampStr(today);
		var str = theDate + timestamp;
		return str;
	},
	
	//得到日期字符串 yyyyMMddHHmmSSss
	getPrettyDateStr:function(today){
		var theDate = web_plugin_common.getDayStr(today);
		var timestamp = web_plugin_common.getTimestampStr(today);
		var millsecondStr = today.getMilliseconds();
		var millsecond = "";
		if(millsecondStr<10){
			millsecond = "00"+millsecondStr;
		}else if(millsecondStr<100){
			millsecond = "0"+millsecondStr;
		}else{
			millsecond = millsecondStr;
		}
		var prettyDate = theDate+timestamp+millsecond
		return prettyDate;
	},
	
	
	//普通回调方法
	callback:function(data, methodFlag){},
	
	//分页列表回调方法
	pageCallback:function(data, methodFlag){},
	
	changeMoneyStyle:function(money){
		var value = money;
		//如果传递过来的是XX或X或XXX
		if(money.indexOf(".") == -1){
			value = money+".00";
		}else if(money.indexOf(".") == 0){
			//如果传递过来的是.XX
			if(money.length < 2){
				value = "0"+money+"00";
			}else if(money.length == 2){
				value = "0"+money+"0";
			}else{
				value = "0"+money;
			}
		}else if(money.indexOf(".") > 0){
			//如果传递过来的是0.2或0.22或22.2或22.22
			if(money.substring(money.indexOf(".")+1).length == 1){
				value = money+"0";
			}
		}
		return value;
	},
	showErrorTipFun:function(tipName,inputId,className,msgValue){
		var tip = $("#"+tipName);
		var html = onErrorHtml;
		var showclass = className;
		html = html.replace(/\$class\$/g, showclass).replace(/\$data\$/g, msgValue);
		tip.html(html).show();
		var jqobj = $("#"+inputId);
		jqobj.removeClass(onShowClass).removeClass(onFocusClass).removeClass(onCorrectClass)
			.addClass(onErrorClass);
	}
};