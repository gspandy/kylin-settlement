var webPluginPage ={};
webPluginPage = {};
webPluginPage.page[0] = {
	rootId:"",//列表表头TR的ID
	checkBoxId:"",//有复选框时定义
	radioId:"",//有单选按钮时定义
	pageFoot:"",//页脚定义
	colspanNum:0,
	operate:[{
		name:"",//按钮名称
		method:"",//popDiv(弹出层)、modeWindow()、flushPage(刷新页面)、newOpenWindow(新页卡)、自定义
		check:"",//判断按钮是否显示
		url:"",//除弹出层和自定义方法外都需要定义此参数，定位页面的URL
		param:""//除弹出层和自定义方法外都需要定义此参数，定位页面的URL后面跟的参数
		
	}],
	link:[{
		method:"",
		check:"",
		url:"",
		param:""
	}],
	selfMode:false,//定义列表的自定义模式，不是正规列表
	items:[], //列表数据
	currentPage:1, //当前页，如果为0查询全部数据
	pageNumber:2, //每页显示数量
	pageNumberItem:[10,20,50], // 可供选择的每页显示数据量（默认，可自定义）
	totalPage:0, // 总页数
	totalCount:0, // 总记录数 
	pageFootNum:10, // 页码数多的时候，最多显示的页码数个数
	prePageNum:4, // 页码数多的时候，当前页之前显示的页码个数
	behPageNum:5, // 页码数多的时候，当前页之后显示的页码个数
	orderBy:"", // 排序字段
	queryString:"", // 封装的查询参数
	showPageNumberItem:false, // 默认不显示
	url:"", // 获取数据地址
	totalCountShowId:"", // 总的记录数显示ID
	noDataWaring:"", // 无信息时的提示语
	accumulate:false // 是否积累显示记录
}

// 设置默认参数的数据
webPluginPage.setDefaultValue = function(pageObj){
	if(web_plugin_common.isEmpty(pageObj.currentPage)){
		pageObj.currentPage = 1;
	}
	if(web_plugin_common.isEmpty(pageObj.pageNumberItem)){
		pageObj.pageNumberItem = [10,20,50];
	}
	if(web_plugin_common.isEmpty(pageObj.pageNumber)){
		pageObj.pageNumber = pageObj.pageNumberItem[0];
	}
	if(web_plugin_common.isEmpty(pageObj.pageFootNum)){
		pageObj.pageFootNum = 10;
	}
	if(web_plugin_common.isEmpty(pageObj.prePageNum)){
		pageObj.prePageNum = 4;
	}
	if(web_plugin_common.isEmpty(pageObj.behPageNum)){
		pageObj.behPageNum = 5;
	}
	if(pageObj.showPageNumberItem == undefined){
		pageObj.showPageNumberItem = false;
	}
	if(web_plugin_common.isEmpty(pageObj.selfMode)){
		pageObj.selfMode = false;
	}
	if(web_plugin_common.isEmpty(pageObj.queryString)){
		pageObj.queryString = "";
	}
	if(web_plugin_common.isEmpty(pageObj.orderBy)){
		pageObj.orderBy = "";
	}
}
// 搜索查询
webPluginPage.queryData = function(index,formId){
	var queryString = $("#"+formId).serialize();
	queryString = web_plugin_common.serialize(queryString);
	this.page[index-1].queryString = queryString;
	// 加载列表数据
	return false;
}

webPluginPage.loadData = function(pageIndex,flushTableWithoutAjax){
	if(this.page.length < pageIndex || web_plugin_common.isEmpty(this.page[pageIndex-1])){
		jQuery("body").append("&nbsp;&nbsp;<font color='red'>参数定义有误或您要加载的数据索引有误！</font>");
		return ;
	}
	var pageObj = this.page[pageIndex-1];
	// 清理原来的数据信息
	if(!pageObj.selfMode){
		jQuery("#"+pageObj.rootId).nextAll().remove();
	}
	// 重置复选框
	if(!web_plugin_common.isEmpty(pageObj.checkBoxId)){
		jQuery("#"+pageObj.checkBoxId).attr("checked",false);
	}
	// 清除分页页码
	jQuery("#"+pageObj.pageFoot).html("");
	// 设置默认信息	
	this.setDefaultValue(pageObj);
	// 拼接查询条件
	var params = this.appendQueryString(pageObj);
	// 判断请求数据是否异步
	if(flushTableWithoutAjax){
		this.flushTableWithoutAjax(pageIndex);
	}else{
		// 异步查询数据
		web_plugin_common.findDataByType(pageObj.url,params,web_plugin_common.constant.PAGE_CALLBACK,pageIndex);
	}
}

// 拼接查询条件
webPluginPage.appendQueryString=function(pageObj){
	var queryString = "pageNumber="+pageObj.currentPage+"&pageSize="+pageObj.pageNumber;
	queryString += "&" + pageObj.queryString;
	return queryString;
}

webPluginPage.flushTableWithoutAjax=function(pageIndex){
	var pageObj = this.page[pageIndex-1];
	// 显示总记录数
	if(!web_plugin_common.isEmpty(pageObj.totalCountShowId)){
		jQuery("#"+pageObj.totalCountShowId).html(pageObj.totalCount);
	}
	// 判断数据
	if(web_plugin_common.isEmpty(pageObj.items) || pageObj.items.length <= 0){
		if(!pageObj.selfMode){
			var htmlStr = "<tr><td colspan='"+pageObj.colspanNum+"'"+web_plugin_common.noDataWarningStyle+"</td></tr>";
		}else{
			if(web_plugin_common.isEmpty(pageObj.noDataWarning)){
				jQuery("#"+pageObj.rootId).html(web_plugin_common.noDataWarningStyle);
			}else{
				jQuery("#"+pageObj.rootId).html(pageObj.noDataWarning);
			}
		}
		return ;
	}else{
		var htmlStr = '';
		for(var i = 0;i<pageObj.items.length;i++){
			// 判断是普通列表还是自定义内容的列表
			if(!pageObj.selfMode){
				htmlStr += this.parseData(pageObj,items[i],i,pageIndex);
			}else{
				var selfParam = jQuery("#"+pageObj.rootId).attr("param").split(".");
				if(selfParam.length == 1){
					htmlStr+=eval( "(" +jQuery("#"+pageObj.rootId).attr("param")+"("+i+","+pageIndex+")"+")" );
				}else if(selfParam.length == 2){
					htmlStr+=eval( "(" +selfParam[0]+")" )[selfParam[1]](i,pageIndex);
				}
			}
		}
		if(!pageObj.selfMode){
			jQuery("#"+pageObj.rootId).after(htmlStr);
		}else{
			if(pageObj.accumulate && pageObj.currentPage>1){
				jQuery("#"+pageObj.rootId).append(htmlStr);
			}else{
				jQuery("#"+pageObj.rootId).html(htmlStr);
			}
		}
		//拼接页码部分
		if(!web_plugin_common.isEmpty(pageObj.pageFoot) && pageObj.totalPage > 1){
			webPluginPage.appendPageFoot(pageObj,i,pageIndex);
		}
		//创建复选框选择事件
		if(!web_plugin_common.isEmpty(pageObj.checkBoxId)){
			jQuery("#"+pageObj.checkBoxId).click(function(){
				jQuery("[name='webPluginPage_checkBox"+pageIndex+"']").each(function(){
					jQuery(this).attr("checked",jQuery("#"+pageObj.checkBoxId).attr("checked"));
				})
			})
		}
		//添加隔行换色的样式
		if (!pageObj.selfMode) {
			webPluginPage.addLineChangeColor(pageObj.rootId);
		}
		//预留接口，方便需要列表加载完成之后调用方法的时候调用
		webPluginPage.loadFinish();
	}
}

// 解析每行的数据
webPluginPage.parseData = function(pageObj,item,itemIndex,pageIndex){
	var htmlStr = '<tr>';
	
	// 解析列表属性
	jQuery("#"+pageObj.rootId+">td").each(function(){
		var thObj = jQuery(this);
		var thParam = thObj.attr("param");
		// 行属性包含：序号列，普通列，复选框列和操作列
		if(thParam == "page_sequence"){
			// 序号列
			var num = (pageObj.currentPage-1)*pageObj.pageNumber+(itemIndex+1);
			htmlStr += '<td>'+num+'</td>';
		}else if(thParam == "page_checkBox"){
			// 复选框列
			var propertyArr = jQuery(this).attr("property").split("@");
			var values = "";
			for(var i=0;i<propertyArr.length;i++){
				if(i == propertyArr.length-1){
					values += item[propertyArr[i]];
				}else{
					values += item[propertyArr[i]] + "@";
				}
			}
			htmlStr += '<td><input type="checkbox" name="webPluginPage_checkBox'+pageIndex+'" value="'+values+'"/></td>';
		}else if(thParam == "page_radio"){
			// 单选列
			var propertyArr = jQuery(this).attr("property").split("@");
			var values = "";
			for(var i=0;i<propertyArr.length;i++){
				if(i == propertyArr.length-1){
					values += item[propertyArr[i]];
				}else{
					values += item[propertyArr[i]] + "@";
				}
			}
			htmlStr += '<td><input type="checkbox" name="webPluginPage_radio'+pageIndex+'" value="'+values+'"/></td>';
		}else if(thParam == "page_operate"){
			// 操作列
			var operateArr = pageObj.operate;
			if(web_plugin_common.isEmpty(operateArr)){
				jQuery("body").append("&nbsp;&nbsp;<font color='red'>operate参数未定义！</font>");
				return "";
			}else{
				htmlStr += "<td>";
				for(var i=0; i<operateArr.length;i++){
					// 拼接操作列
					htmlStr += webPluginPage.appendOperate(operateArr[i],item,itemIndex,pageIndex);
				}
				htmlStr += "</td>";
			}
		}else if(web_plugin_common.isEmpty(thParam)){
			htmlStr += webPluginPage.appendNormalCol(pageObj,item,itemIndex,pageIndex,thObj);
		}else{
			jQuery("body").append("&nbsp;&nbsp;<font color='red'>属性param未定义！</font>");
			return "";
		}
	});
	htmlStr += "</tr>";
	return htmlStr;
}

// 解析操作栏数据信息
webPluginPage.appendOperate = function(operate,item,itemIndex,pageIndex){
	var htmlStr = '';
	var url = '';
	if(!web_plugin_common.isEmpty(operate.check)){
		var selfParam = operate.check.split(".");
		if(selfParam.length ==1){
			 if(!eval("("+operate.check+"("+itemIndex+","+pageIndex+")"+")")){
			 	if(operate.checkShow == "hidden"){
			 		return "";
			 	}else{
			 		if(!web_plugin_common.isEmpty(operate.imgUrl)){
			 			var lastIndex = operate.imgUrl.lastIndexOf(".");
			 			var grep_img_url = operate.imgUrl.substring(0,lastIndex)+"_grey";
			 			grep_img_url += "."+operate.imgUrl.sbstring(lastIndex+1);
			 			htmlStr += '<a ><img src="'+grep_img_url+'"/></a>';
			 		}
			 		htmlStr += '<a>'+operate.name+'</a>';
			 		return htmlStr;
			 	}
			 }
		}else if(selfParam.length ==2){
			if(!eval("("+selfParam[0]+")" )[selfParam[1]](itemIndex,pageIndex)){
				if(operate.checkShow == "hidden"){
			 		return "";
				}else{
					if(!web_plugin_common.isEmpty(operate.imgUrl)){
			 			var lastIndex = operate.imgUrl.lastIndexOf(".");
			 			var grep_img_url = operate.imgUrl.substring(0,lastIndex)+"_grey";
			 			grep_img_url += "."+operate.imgUrl.sbstring(lastIndex+1);
			 			htmlStr += '<a ><img src="'+grep_img_url+'"/></a>';
			 		}
			 		htmlStr += '<a>'+operate.name+'</a>';
			 		return htmlStr;
				}
			}
		}
	}
	
	//拼接URL
	if(!web_plugin_common.isEmpty(operate.url)){
		if(operate.url.indexOf("?")==-1){
			url = operate.url+"?";
			if(!web_plugin_common.isEmpty(operate.param)){
				for(var i =0;i<operate.param.length;i=i+2){
					if(i==0){
						url += operate.param[i]+"="+item[operate.param[i+1]];
					}else{
						url += "&"+operate.param[i]+"="+item[operate.param[i+1]];
					}
				}
			}
		}else{
			url = operate.url;
			if(!web_plugin_common.isEmpty(operate.param)){
				for(var i =0;i<operate.param.length;i=i+2){
					url += "&"+operate.param[i]+"="+item[operate.param[i+1]];
				}
			}
		}
	}
	if(!web_plugin_common.isEmpty(operate.imgUrl)){
		htmlStr += '<a><img src="'+operate.imgUrl+'" /></a>';
	}
	if(web_plugin_common.isEmpty(operate.method)){
		jQuery("body").append("&bsp;&nbsp;<font color='red'>method参数未定义！</font>");
	}else if(operate.method == "popDiv"){
		htmlStr += '<a onclick="web_plugin_popDiv.show()">'+operate.name+'</a>&nbsp;';
	}else if(operate.method == "modeWindow"){
		htmlStr += '<a onclick="window.showModalDialog(\"'+url+'\)">'+operate.name+'</a>&nbsp;';
	}else if(operate.method == "newOpenWindow"){
		htmlStr += '<a onclick="window.open(\"'+url+'\)">'+operate.name+'</a>&nbsp;';
	}else if(operate.method == "flushPage"){
		htmlStr += '<a onclick="window.location.href=\"'+url+'\">'+operate.name+'</a>&nbsp;';
	}else{
		htmlStr += '<a onclick="'+operate.method+'('+itemIndex+','+pageIndex+',\"'+url+'\">'+operate.name+'</a>&nbsp;';
	}
	return htmlStr;
}

// 解析正常列的数据
webPluginPage.appendNormalCol = function(operate,item,itemIndex,pageIndex,thObj){
	var htmlStr = '';
	// 判断是否需要调用自定义方法
	if(!web_plugin_common.isEmpty(thObj.attr("selfDefind"))){
		if(thObj.attr("multiple") == "true"){
			htmlStr = '<td>';
			var selfParam = thObj.attr("selfDefind").split(".");
			if(selfParam.length ==1){
				htmlStr += eval("("+thObj.attr("selfDefind")+"("+itemIndex+","+pageIndex+")"+")");
			}else if(selfParam.length == 2 ){
				htmlStr += eval("("+selfParam[0]+")" )[selfParam[1]](itemIndex,pageIndex);
			}
			htmlStr += '</td>';
			return htmlStr;
		}else{
			htmlStr += '<td><p style="height:28px;overflow:hidden;"'+thObj.width()+'">';
			var selfParam = thObj.attr("selfDefind").split(".");
			if(selfParam.length ==1){
				htmlStr += eval("("+thObj.attr("selfDefind")+"("+itemIndex+","+pageIndex+")"+")");
			}else if(selfParam.length == 2 ){
				htmlStr += eval("("+selfParam[0]+")" )[selfParam[1]](itemIndex,pageIndex);
			}
			htmlStr += '</p></td>';
			return htmlStr;
		}
	}
	
	// 判断是否是数据字典列
	var paramContent = item[thObj.attr("param")];
	if(!web_plugin_common.isEmpty(thObj.attr("dictType"))){
		if(!web_plugin_common.isEmpty(paramContent)){
			paramContent = "--";
		}else{
			var dictType = eval("("+thObj.attr("dictType")+")");
			paramContent = dictType[paramContent];
		}
	}
	
	// 判断是否是格式化
	if(!web_plugin_common.isEmpty(thObj.attr("format"))){
		if(web_plugin_common.isEmpty(paramContent)){
			paramContent = "--";
		}else{
			paramContent = web_plugin_common.parseDate(paramContent,thObj.attr("formar"));
		}
	}
	if(web_plugin_common.isEmpty(paramContent)){
		if(paramContent != 0){
			paramContent = "--";
		}
	}
	if(thObj.attr("multiple") == "true"){
		htmlStr = '<td title="'+paramContent+'">';
	}else{
		htmlStr = '<td title="'+paramContent+'"><p style="height:25px;overflow:hidden;'+thObj.width()+'">';
	}
	
	// 判断是否需要加链接
	if(!web_plugin_common.isEmpty(thObj.attr("link"))){
		htmlStr += paramContent;
	}else if(web_plugin_common.isEmpty(pageObj.link) || web_plugin_common.isEmpty(pageObj.link[thObj.attr("link")-1])){
		jQuery("body").append("&bsp;&nbsp;<font color='red'>link参数未定义！</font>");
	}else{
		var link = pageObj.link[thObj.attr("link")-1];
		if(!web_plugin_common.isEmpty(link.check)){
			var selfParam = link.check.split(".");
			if(selfParam.length ==1 ){
				if(!eval("("+link.check+"("+itemIndex+","+pageIndex+")"+")")){
					return "";
				}
			}else if(selfParam.length == 2){
				if(!eval("("+selfParam[0]+")")[selfParam[1]](itemIndex,pageIndex)){
					return "";
				}
			}
		}
		
		// 拼接Url
		if(!web_plugin_common.isEmpty(link.url)){
			if(link.url.indexOf("?")==-1){
				url = link.url+"?";
				if(!web_plugin_common.isEmpty(link.param)){
					for(var i =0;i<link.param.length;i++){
						if(i==0){
							url += link.param[i]+"="+item[link.param[i+1]];
						}else{
							url += "&"+link.param[i]+"="+item[link.param[i+1]];
						}
					}
				}
			}else{
				url = link.url;
				if(!web_plugin_common.isEmpty(link.param)){
					for(var i =0;i<link.param.length;i=i+2){
						url += "&"+link.param[i]+"="+item[link.param[i+1]];
					}
				}
			}
		}
		if(web_plugin_common.isEmpty(link.method)){
			jQuery("body").append("&bsp;&nbsp;<font color='red'>method参数未定义！</font>");
		}else if(link.method == "popDiv"){
			htmlStr += '<a onclick="web_plugin_popDiv.show()">'+paramContent+'</a>&nbsp;';
		}else if(link.method == "modeWindow"){
			htmlStr += '<a onclick="window.showModalDialog(\"'+url+'\)">'+paramContent+'</a>&nbsp;';
		}else if(link.method == "newOpenWindow"){
			htmlStr += '<a onclick="window.open(\"'+url+'\)">'+paramContent+'</a>&nbsp;';
		}else if(link.method == "flushPage"){
			htmlStr += '<a onclick="window.location.href=\"'+url+'\">'+paramContent+'</a>&nbsp;';
		}else{
			htmlStr += '<a onclick="'+link.method+'('+itemIndex+','+pageIndex+',\"'+url+'\">'+paramContent+'</a>&nbsp;';
		}
	}
	if(thObj.attr("multiple")=="true"){
		htmlStr += "</td>";
	}else{
		htmlStr += '</p></td>';
	}
	return htmlStr;
}

// 分页查询回调方法
webPluginPage.ajaxCallback=function(data,pageIndex){
	var pageObj = this.page[pageIndex-1];
 	var items = "";
 	if(!web_plugin_common.isEmpty(data.items)){
 		items = eval("("+data.items+")");
 	}
	pageObj.currentPage = data.currentPage;
	pageObj.totalPage = data.maxPage;
	
	if(!web_plugin_common.isEmpty(data.items)){
 		pageObj.items = eval("("+data.items+")");
 	}else{
 		pageObj.items = "";
 	}
	
	pageObj.totalCount = data.maxRowCount;
	if(!web_plugin_common.isEmpty(pageObj.totalCountShowId)){
		jQuery("#"+pageObj.totalCountShowId).html(pageObj.totalCount);
	}
	//判断数据
	if(web_plugin_common.isEmpty(items) || items.length <= 0){
		if(!pageObj.selfMode){
			var htmlstr = "<tr><td colspan='15' height='80' align='center'>"+web_plugin_common.constant.noDataWarningStyle+"</td></tr>";
			jQuery("#"+pageObj.rootId).after(htmlstr);
		}else{
			if(web_plugin_common.isEmpty(pageObj.noDataWarning)){
				jQuery("#"+pageObj.rootId).html(web_plugin_common.constant.noDataWarningStyle);
			}else{
				jQuery("#"+pageObj.rootId).html(pageObj.noDataWarning);
			}
			
		}
		//预留接口，方便需要列表加载完成之后调用方法的时候调用
		webPluginPage.loadFinish();
		return;
	}else {
		pageObj.items = items;
		var htmlstr = '';
		for(var i = 0; i < items.length ; i++){
			//判断是否是普通列表还是要自定义内容的列表
			if(!pageObj.selfMode){
				htmlstr += this.parseData(pageObj,items[i],i,pageIndex);
			}else{
				var selfParam = jQuery("#"+pageObj.rootId).attr("param").split(".");
				if(selfParam.length == 1){
					htmlstr+=eval( "(" +jQuery("#"+pageObj.rootId).attr("param")+"("+i+","+pageIndex+")"+")" );
				}else if(selfParam.length == 2){
					htmlstr+=eval( "(" +selfParam[0]+")" )[selfParam[1]](i,pageIndex);
				}
			}
		}
		if(!pageObj.selfMode){
			jQuery("#"+pageObj.rootId).after(htmlstr);
		}else{
			if(pageObj.accumulate && pageObj.currentPage>1){
				jQuery("#"+pageObj.rootId).append(htmlstr);
			}else{
				jQuery("#"+pageObj.rootId).html(htmlstr);
			}
			
		}
		//拼接页码部分
		if(!web_plugin_common.isEmpty(pageObj.pageFoot) && pageObj.totalPage > 1){
			webPluginPage.appendPageFoot(pageObj,i,pageIndex);
		}
		
		//创建复选框选择事件
		if(!web_plugin_common.isEmpty(pageObj.checkBoxId)){
			jQuery("#"+pageObj.checkBoxId).click(function(){
				jQuery("[name='web_plugin_page_checkbox"+pageIndex+"']").each(function(){
					jQuery(this).attr("checked",jQuery("#"+pageObj.checkBoxId).attr("checked"));
				})
			})
		}
		
		
		//添加隔行换色的样式
		if (!pageObj.selfMode) {
			webPluginPage.addLineChangeColor(pageObj.rootId);
		}
		
		//预留接口，方便需要列表加载完成之后调用方法的时候调用
		webPluginPage.loadFinish();
	}
}

//拼接页码数据
 webPluginPage.appendPageFoot = function(pageObj,itemIndex,pageIndex){
 	var htmlstr = '';
	if(pageObj.totalPage>1){
		//显示的页码数，按规定的数量显示，并且以当前页为中心，前面显示4,后面显示5页
		if(pageObj.totalPage>pageObj.pageFootNum){
			if(pageObj.currentPage>pageObj.behPageNum && pageObj.currentPage<=pageObj.totalPage-pageObj.behPageNum){
				for(var i=(pageObj.currentPage-pageObj.prePageNum);i<=(pageObj.currentPage+pageObj.behPageNum);i++){
					//当前页显示不同样式
					if(pageObj.currentPage==i){
						htmlstr+='<a onclick="webPluginPage.fenye('+pageIndex+','+i+')" class="lnormber lbg3">'+i+'</a>';
					}else{
						htmlstr+='<a onclick="webPluginPage.fenye('+pageIndex+','+i+')" class="lnormber">'+i+'</a>';
					}
				}	
			}else if(pageObj.currentPage>pageObj.totalPage-pageObj.behPageNum){
				for(var i=(pageObj.totalPage-pageObj.pageFootNum+1);i<=pageObj.totalPage;i++){
					//当前页显示不同样式
					if(pageObj.currentPage==i){
						htmlstr+='<a onclick="webPluginPage.fenye('+pageIndex+','+i+')" class="lnormber lbg3">'+i+'</a>';
					}else{
						htmlstr+='<a onclick="webPluginPage.fenye('+pageIndex+','+i+')" class="lnormber">'+i+'</a>';
					}
				}	
			}else if(pageObj.currentPage<=pageObj.behPageNum){
				for(var i=1;i<=pageObj.pageFootNum;i++){
					//当前页显示不同样式
					if(pageObj.currentPage==i){
						htmlstr+='<a onclick="webPluginPage.fenye('+pageIndex+','+i+')" class="lnormber lbg3">'+i+'</a>';
					}else{
						htmlstr+='<a onclick="webPluginPage.fenye('+pageIndex+','+i+')" class="lnormber">'+i+'</a>';
					}
				}
			}
		}else{
			for(var i=1;i<=pageObj.totalPage;i++){
				//当前页显示不同样式
				if(pageObj.currentPage==i){
					htmlstr+='<a onclick="webPluginPage.fenye('+pageIndex+','+i+')" class="lnormber lbg3">'+i+'</a>';
				}else{
					htmlstr+='<a onclick="webPluginPage.fenye('+pageIndex+','+i+')" class="lnormber">'+i+'</a>';
				}
			}
		}
		//显示总页数
		htmlstr +='<a class="first">共'+pageObj.totalPage+'页</a>';
		
		//如果实际页码数大于最大能显示的页码数，则提供选择页数跳转的插件
		if(pageObj.pageFootNum < pageObj.totalPage){
			htmlstr +='<a class="total">跳转到第 <input type="text" id="web_plugin_page_ser'+pageIndex+'" class="ltext" />&nbsp;页</a>';
	        htmlstr +='<a onclick="webPluginPage.prefenye('+pageIndex+')" class="lbtn9">搜索</a>';
		}
	}
	jQuery("#"+pageObj.pageFoot).append(htmlstr);
 }
 
//点击页码查询数据方法
webPluginPage.fenye = function (pageIndex,currentPage){
	var pageObj = this.page[pageIndex-1];
 	if(this.page[pageIndex-1].currentPage != currentPage){
 		this.page[pageIndex-1].currentPage = currentPage;
		var scrollTopVal = jQuery("#"+pageObj.rootId).offset().top;
		jQuery('html, body').animate({scrollTop:scrollTopVal-100}, 1000);
		this.loadData(pageIndex);
 	}
}

 //输入页码查询数据方法
 webPluginPage.prefenye = function (pageIndex){
 	var cPage = jQuery("#web_plugin_page_ser"+pageIndex).val();
 	var pageObj = this.page[pageIndex-1];
 	var scrollTopVal = jQuery("#"+pageObj.rootId).offset().top;
	jQuery('html, body').animate({ scrollTop:scrollTopVal-100}, 1000);
 	if((/^([+]?)\d*\.?\d+$/).test(cPage) && cPage <=  pageObj.totalPage && cPage != pageObj.currentPage){
 		this.page[pageIndex-1].currentPage = cPage;
 		this.loadData(pageIndex);
 	}
 }
 
 //拼接列表显示数量下拉框
 webPluginPage.appendPageNumberItem = function(pageObj,pageIndex){
 	var htmlstr = '';
	htmlstr+='<span style="float:right;margin-right:10px;">每页记录:<select onchange="webPluginPage.selectPageNumber('+pageIndex+',this.value)">';
	for(var i=0;i<pageObj.pageNumberItem.length;i++){
		
		//将下拉菜单的每页显示数据量和page中的pageNumber匹配一致
		if(pageObj.pageNumber==pageObj.pageNumberItem[i]){
			htmlstr+="<option value='"+pageObj.pageNumber+"' selected>"+pageObj.pageNumber+"</option>";
		}else{
			htmlstr+="<option value='"+pageObj.pageNumberItem[i]+"'>"+pageObj.pageNumberItem[i]+"</option>";
		}
	}
	htmlstr+="</select></span>";
	jQuery("#"+pageObj.pageFoot).append(htmlstr) ;
 }
 
//选择列表显示数据量
webPluginPage.selectPageNumber = function(pageIndex,pageNumber){
	this.page[pageIndex-1].pageNumber = pageNumber;
	this.loadData(pageIndex);
}
 
//获复选框选中数据,对外接口
webPluginPage.getCheckboxValue = function(pageIndex){
 	var values = '';
	jQuery("[name='webPluginPage_checkBox"+pageIndex+"']").each(function(){
		if(jQuery(this).attr("checked")){
			values+=jQuery(this).val()+":";
		}
	});
	return values.substring(0,values.length-1);
} 
 
//获单选框选中数据,对外接口
webPluginPage.getRadioValue = function(pageIndex){
 	var values = '';
	jQuery("[name='webPluginPage_radio"+pageIndex+"']").each(function(){
		if(jQuery(this).attr("checked")){
			values+=jQuery(this).val()+":";
		}
	});
	return values.substring(0,values.length-1);
}
//行区间颜色
webPluginPage.addLineChangeColor = function (rootId){
 	jQuery("#"+rootId).siblings().each(function(index){
		if(index%2 == 0){
			//jQuery(this).addClass("pageEven"); 
		}else{
			jQuery(this).addClass("ltake"); 
		}
		jQuery(this).hover(function(){
			jQuery(this).addClass("tr_hover"); 
		},function(){
			jQuery(this).removeClass("tr_hover");
			if(index%2 == 0){
				//jQuery(this).addClass("pageEven"); 
			}else{
				jQuery(this).addClass("ltake"); 
			}
		});
		
	});
 }
 
//实现异步调用后的回调函数
web_plugin_common.pageCallback = function(data,methodFlag){
	webPluginPage.ajaxCallback(data,methodFlag);
};

//预留接口，方便需要列表加载完成之后调用方法的时候调用
webPluginPage.loadFinish = function(){
 	
};