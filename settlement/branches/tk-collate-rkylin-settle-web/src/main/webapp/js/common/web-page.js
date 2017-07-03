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



/**
 * @author Lidan
 * @time 2013-12-18
 * @description 分页插件
 */
 var webPluginPage = {};
 webPluginPage.page = [];
 webPluginPage.page[0] ={
 	trRootId:"",                                       //列表表头tr的Id
 	checkboxId:"" ,                                   //有复选框时定义
 	radioId:"", 
	pageFoot:"",                                   //页脚Id
	operate:[{                                     //操作栏定义
		name:"",                                   //按钮名称 
		method:"" ,//"popDiv(弹出层),modeWindow(模态窗口),newOpenWindow(新页卡),flushPage(刷新页面),自定义(自定义js方法)", //确定点击按钮后的展现形式
		check:"",                                //判断按钮是否显示
		url:"",                                    //除弹出层和自定义方法外都需要定义此参数，定位页面的url
		param:[]                                   //除弹出层和自定义方法外都需要定义此参数，定位页面的url后面跟的参数 
	}],
	link:[{                                        //普通属性栏添加链接，和操作栏基本类似
		method:"",//"popDiv(弹出层),modeWindow(模态窗口),newOpenWindow(新页卡),flushPage(刷新页面),自定义(自定义js方法)", //确定点击按钮后的展现形式
		check:"",                                //判断按钮是否显示
		url:"",                                    //除弹出层和自定义方法外都需要定义此参数，定位页面的url
		param:[]                                   //除弹出层和自定义方法外都需要定义此参数，定位页面的url后面跟的参数 
	}],
	//以下参数不是必须定义的
	selfMode:false  ,                               //定义列表的自定义模式，不是正规列表    
	items:[],                                      //列表数据
	currentPage:1,                                 //当前页,如果为O查询全部数据
	pageNumber:1,                                 //每页显示数据量
	pageNumberItem:[10,20,30],                     //可供选择的每页显示数据量（默认，可自定义）
	totalPage:0,                                   //总页数
	totalCount:0,                                  //总记录数 
	pageFootNum:5,                                //页码数多的时候，最多显示的页码数个数
	prePageNum:2,                                  //页码过多时，当前页之前显示的页码个数
	behPageNum:2,                                  //页码过多时，当前页之后显示的页码个数
	orderBy:"",                                    //排序字段
	queryString:"",                                     //封装的查询参数
	showPageNumberItem:false,                       //默认不显示
	url:"",											//获取数据地址  
 	totalCountShowId:"",                             //总的记录数显示id
 	noDataWarning:"",                                 //无信息时的提示语
 	accumulate:false                                  //是否累积显示记录
 }
 //设置默认参数的数据
 webPluginPage.setDefaultValue = function (pageObj){
 	if (web_plugin_common.isEmpty(pageObj.currentPage)) {
		pageObj.currentPage = 1;
	}
	if (web_plugin_common.isEmpty(pageObj.pageNumberItem)) {
		pageObj.pageNumberItem = [10,20,50];
	}
	if (web_plugin_common.isEmpty(pageObj.pageNumber)) {
		pageObj.pageNumber = pageObj.pageNumberItem[0];
	}
	
	if (web_plugin_common.isEmpty(pageObj.pageFootNum)) {
		pageObj.pageFootNum = 5;
	}
	
	if (web_plugin_common.isEmpty(pageObj.prePageNum)) {
		pageObj.prePageNum = 2;
	}
	
	if (web_plugin_common.isEmpty(pageObj.behPageNum)) {
		pageObj.behPageNum = 2;
	}
	if (pageObj.showPageNumberItem == undefined) {
		pageObj.showPageNumberItem = false;
	}
	
	if (web_plugin_common.isEmpty(pageObj.selfMode)) {
		pageObj.selfMode = false;
	}
	
	if (web_plugin_common.isEmpty(pageObj.queryString)) {
		pageObj.queryString = "";
	}
	
	if (web_plugin_common.isEmpty(pageObj.orderBy)) {
		pageObj.orderBy = "";
	}
 }
 
 //查询的时候调用的方法,index区别是哪个列表oForm是form对象
 webPluginPage.queryData = function(index,formId,currentPageVa){
 	var queryString = $("#"+formId).serialize();
 	queryString = web_plugin_common.serialize(queryString);
	this.page[index-1].queryString = queryString;
	// 如果是单独的提交查询方式，设置加载第几页
	this.page[index-1].currentPage = currentPageVa;
 	this.loadData(index);
 	return false;
 };
 

 //加载列表数据方法入口，对外接口
 webPluginPage.loadData = function (pageIndex,flushTableWithoutAjax){
 	if(this.page.length < pageIndex || web_plugin_common.isEmpty(this.page[pageIndex-1])){
		jQuery("body").append("&nbsp;&nbsp;<font color='red'>参数定义有误或您要加载的数据索引有误</span>");
		return ;
	}
	var pageObj = this.page[pageIndex-1];
	//清除原有数据
	if(!pageObj.selfMode){
		jQuery("#"+pageObj.trRootId).nextAll().remove();
	}
	//重置复选框
	if(!web_plugin_common.isEmpty(pageObj.checkboxId)){
		jQuery("#"+pageObj.checkboxId).attr("checked",false);
	}
	//清除分页页码
	jQuery("#"+pageObj.pageFoot).html("");
	this.setDefaultValue(pageObj);   
	//拼接查询条件
	var params = this.appendQueryString(pageObj);
	if(flushTableWithoutAjax){
		this.flushTableWithoutAjax(pageIndex);
	}else{
		//异步查询数据
		web_plugin_common.findDataByType(pageObj.url,params,web_plugin_common.constant.PAGE_CALLBACK,pageIndex);
	}
 };
 
 //解析每行数据
 webPluginPage.parseData = function(pageObj,item,itemIndex,pageIndex){
	var htmlstr = '<tr>';
	
	//解析列表属性
	jQuery("#"+pageObj.trRootId+">th").each(function(){
		var thObj = jQuery(this);
		
		//分为序号列，操作列，普通列，复选框列
		if(thObj.attr("param") == "web_plugin_sequence"){
			htmlstr += '<td>'+((pageObj.currentPage-1)*pageObj.pageNumber+(itemIndex+1))+'</td>';
		
		}else if(thObj.attr("param") == "web_plugin_checkbox"){
			var propertyArr=jQuery(this).attr("property").split("@");
				var values="";
				for(var i=0;i<propertyArr.length;i++){
					if(i==propertyArr.length-1){
						values+=item[propertyArr[i]];
					}else{
						values+=item[propertyArr[i]]+"@";
					}
				}
			htmlstr += '<td ><input type="checkbox" name="webPluginPage_checkbox'+pageIndex+'" value="'+values+'"/></td>';
		
		}else if(thObj.attr("param") == "web_plugin_radio"){
			var propertyArr=jQuery(this).attr("property").split("@");
				var values="";
				for(var i=0;i<propertyArr.length;i++){
					if(i==propertyArr.length-1){
						values+=item[propertyArr[i]];
					}else{
						values+=item[propertyArr[i]]+"@";
					}
				}
			htmlstr += '<td ><input type="radio" name="webPluginPage_radio'+pageIndex+'" value="'+values+'"/></td>';
		
		}else if(thObj.attr("param") == "web_plugin_operate"){
			var operateArr = pageObj.operate;
			if(web_plugin_common.isEmpty(operateArr)){
				jQuery("body").append("&nbsp;&nbsp;<font color='red'>opeate参数未定义</font>");
				return "";
			}
			htmlstr += '<td>';
			for(var i=0;i<operateArr.length;i++){
				htmlstr += webPluginPage.appendOperate(operateArr[i],item,itemIndex,pageIndex);
			}
			htmlstr += '</td>';
		 
		}else if(thObj.attr("param") == "web_plugin_other"){
			htmlstr += '<td >'+thObj.attr("paramValue")+'</td>';
		}else if(!web_plugin_common.isEmpty(thObj.attr("param"))){
			htmlstr += webPluginPage.appendNormalCol(pageObj,item,itemIndex,pageIndex,thObj);
		}else{
			jQuery("body").append("&nbsp;&nbsp;<font color='red'>属性param未定义</span>");
			return "";
		}
	});
	
	htmlstr += '</tr>';
	return htmlstr;
 }
 
 //拼接正常列的数据
 webPluginPage.appendNormalCol = function(pageObj,item,itemIndex,pageIndex,thObj){
 	var htmlstr = '';
 	 
 	//判断是否需要调用自定义方法
 	if(!web_plugin_common.isEmpty(thObj.attr("selfDefind"))){
 		if(thObj.attr("multiple") == "true"){
 			htmlstr = '<td>';
	 		
	 		var selfParam = thObj.attr("selfDefind").split(".");
			if(selfParam.length == 1){
				htmlstr += eval( "(" +thObj.attr("selfDefind")+"("+itemIndex+","+pageIndex+")"+")" );
			}else if(selfParam.length == 2){
				htmlstr += eval( "(" +selfParam[0]+")" )[selfParam[1]](itemIndex,pageIndex);
			}
	 		
	 		htmlstr += '</td>';
			return htmlstr;
 		}else{
 			htmlstr = '<td><p style="padding:0px 10px;'+thObj.width()+'">';
	 		
			var selfParam = thObj.attr("selfDefind").split(".");
			if(selfParam.length == 1){
				htmlstr += eval( "(" +thObj.attr("selfDefind")+"("+itemIndex+","+pageIndex+")"+")" );
			}else if(selfParam.length == 2){
				htmlstr += eval( "(" +selfParam[0]+")" )[selfParam[1]](itemIndex,pageIndex);
			}

	 		htmlstr += '</p></td>';
			return htmlstr;
 		}
 		
 	}
 	
	//判断是否是数据字典列
	var paramContent = item[thObj.attr("param")];
	
	
	if(!web_plugin_common.isEmpty(thObj.attr("dictType"))){
		if(web_plugin_common.isEmpty(paramContent)){
			paramContent = "--";
		}else{
			var dictType = eval( "(" + thObj.attr("dictType") + ")" );
			paramContent = dictType[paramContent];
		}
		
	}
	
	if(!web_plugin_common.isEmpty(thObj.attr("format"))){
		if(web_plugin_common.isEmpty(paramContent)){
			paramContent = "--";
		}else{
			paramContent = web_plugin_common.parseDate(paramContent,thObj.attr("format"));
		}
	}
	
	if(web_plugin_common.isEmpty(paramContent)){
		if(paramContent != 0){
			paramContent = "--";
		}
	}
	
	if(thObj.attr("multiple") == "true"){
		htmlstr = '<td title="'+paramContent+'">';
	}else{
		htmlstr = '<td title="'+paramContent+'"><p style="padding:0px 10px;'+thObj.width()+'">';
	}
	
	//判断是否需要加链接
	if(web_plugin_common.isEmpty(thObj.attr("link"))){
		htmlstr += paramContent;
	}else if(web_plugin_common.isEmpty(pageObj.link) || web_plugin_common.isEmpty(pageObj.link[thObj.attr("link")-1])){
		jQuery("body").append("&nbsp;&nbsp;<font color='red'>link参数未定义</span>");
		return "";
	}else{
		var link = pageObj.link[thObj.attr("link")-1];
		
		if(!web_plugin_common.isEmpty(link.check)){
			var selfParam = link.check.split(".");
			if(selfParam.length == 1){
				if(!eval("("+link.check+"("+itemIndex+","+pageIndex+")"+")")){
					return "";
				}
			}else if(selfParam.length == 2){
				if(!eval( "(" +selfParam[0]+")" )[selfParam[1]](itemIndex,pageIndex)){
					return "";
				}
			}
		}
		
		//拼接url
		if(!web_plugin_common.isEmpty(link.url)){
			if(link.url.indexOf("?") == -1){
				url = link.url+"?" ;
				if(!web_plugin_common.isEmpty(link.param)){
					for(var i = 0 ; i < link.param.length ; i=i+2){
						if (i == 0) {
							url += link.param[i] + "=" + item[link.param[i + 1]];
						}else{
							url += "&" + link.param[i] + "=" + item[link.param[i + 1]];
						}
					}
				}
			}else{
				url = link.url;
				if(!web_plugin_common.isEmpty(link.param)){
					for(var i = 0 ; i < link.param.length ; i=i+2){
						url += "&" + link.param[i] + "=" + item[link.param[i + 1]];
					}
				}
			}
		}
		
		if(web_plugin_common.isEmpty(link.method)){
			jQuery("body").append("&nbsp;&nbsp;<font color='red'>method参数未定义</span>");
			return "";
		}else if(link.method == "modeWindow"){
			htmlstr += '<a onclick="window.showModalDialog(\''+url+'\')">'+paramContent+'</a>&nbsp;';
		}else if(link.method == "newOpenWindow"){
			htmlstr += '<a onclick="window.open(\''+url+'\')">'+paramContent+'</a>&nbsp;';
		}else if(link.method == "flushPage"){
			htmlstr += '<a onclick="window.location.href=\''+url+'\'">'+paramContent+'</a>&nbsp;';
		}else{
			htmlstr += '<a onclick="'+link.method+'('+itemIndex+','+pageIndex+',\''+url+'\')">'+paramContent+'</a>&nbsp;';
		}
		/*
		 *else if(link.method == "popDiv"){
			htmlstr += '<a onclick="web_plugin_popDiv.show()">'+paramContent+'</a>&nbsp;';
		}
		*/
	}
	if(thObj.attr("multiple") == "true"){
		htmlstr += '</td>'
	}else{
		htmlstr += '</p></td>'
	}
	return htmlstr;
 }
 
 //解析操作栏数据
 webPluginPage.appendOperate = function(operate,item,itemIndex,pageIndex){
 	var htmlstr = '';
	var url = '';
	if(!web_plugin_common.isEmpty(operate.check)){
		var selfParam = operate.check.split(".");
		if(selfParam.length == 1){
			if(!eval("("+operate.check+"("+itemIndex+","+pageIndex+")"+")")){
				if(operate.checkShow == "hidden"){
					return "";
				}else{
					if(!web_plugin_common.isEmpty(operate.imgUrl)){
						var lastIndex = operate.imgUrl.lastIndexOf(".");
						var grep_img_url = operate.imgUrl.substring(0, lastIndex)+"_grey";
						grep_img_url += "." + operate.imgUrl.substring(lastIndex+1);
						
						htmlstr += '<a><img src="'+grep_img_url+'" /></a>';
					}
					//htmlstr += '<a>'+operate.name+'</a>';
					htmlstr += '<a class="left icon_detail"></a><a class="left icon_detail"></a><a class="left icon_detail"></a><a class="left icon_detail"></a>';
					return htmlstr;
				}
			}
		}else if(selfParam.length == 2){
			if(!eval( "(" +selfParam[0]+")" )[selfParam[1]](itemIndex,pageIndex)){
				if(operate.checkShow == "hidden"){
					return "";
				}else{
					if(!web_plugin_common.isEmpty(operate.imgUrl)){
						var lastIndex = operate.imgUrl.lastIndexOf(".");
						var grep_img_url = operate.imgUrl.substring(0, lastIndex)+"_grey";
						grep_img_url += "." + operate.imgUrl.substring(lastIndex+1);
						
						htmlstr += '<a><img src="'+grep_img_url+'" /></a>';
					}
					//htmlstr += '<a>'+operate.name+'</a>';
					htmlstr += '<a class="left icon_detail"></a><a class="left icon_detail"></a><a class="left icon_detail"></a><a class="left icon_detail"></a>';
					return htmlstr;
				}
			}
		}
		
	}
	//拼接url
	if(!web_plugin_common.isEmpty(operate.url)){
		if(operate.url.indexOf("?") == -1){
			url = operate.url+"?" ;
			if(!web_plugin_common.isEmpty(operate.param)){
				for(var i = 0 ; i < operate.param.length ; i=i+2){
					if (i == 0) {
						url += operate.param[i] + "=" + item[operate.param[i + 1]];
					}else{
						url += "&" + operate.param[i] + "=" + item[operate.param[i + 1]];
					}
				}
			}
		}else{
			url = operate.url;
			if(!web_plugin_common.isEmpty(operate.param)){
				for(var i = 0 ; i < operate.param.length ; i=i+2){
					url += "&" + operate.param[i] + "=" + item[operate.param[i + 1]];
				}
			}
		}
	}
	// 
	if(!web_plugin_common.isEmpty(operate.imgUrl)){
		htmlstr += '<a><img src="'+operate.imgUrl+'" /></a>';
	}
	if(web_plugin_common.isEmpty(operate.method)){
		jQuery("body").append("&nbsp;&nbsp;<font color='red'>method参数未定义</span>");
		return "";
	}else if(operate.method == "modeWindow"){
		htmlstr += '<span onclick="window.showModalDialog(\''+url+'\')">'+operate.name+'</span>';
	}else if(operate.method == "newOpenWindow"){
		htmlstr += '<span class="left" onclick="window.open(\''+url+'\')">'+operate.name+'</span>';
		//htmlstr += operate.name
	}else if(operate.method == "flushPage"){
		htmlstr += '<span onclick="window.location.href=\''+url+'\'">'+operate.name+'</span>';
	}else{
		htmlstr += '<span onclick="javascript:'+operate.method+'('+itemIndex+','+pageIndex+',\''+url+'\')">'+operate.name+'</span>';
	}
	/*
	 else if(operate.method == "popDiv"){
		htmlstr += '<a onclick="web_plugin_popDiv.show()">'+operate.name+'</a>';
	}
	 */
	return htmlstr;
	alert(htmlstr);
 }
 
 //拼接页码数据
 webPluginPage.appendPageFoot = function(pageObj,itemIndex,pageIndex){
 	var htmlstr = '';
	if(pageObj.totalPage>1){
		htmlstr += '<a href="javascript:;" onclick="webPluginPage.fenye('+pageIndex+',1)">首页</a>';
		if(pageObj.currentPage==1){
			htmlstr += '<a href="javascript:;" class="page_turn_btn over_page">&lt;</a>';
		}else{
			var numValue = pageObj.currentPage - 1;
			htmlstr += '<a href="javascript:;" class="page_turn_btn" onclick="webPluginPage.fenye('+pageIndex+','+numValue+')">&lt;</a>';
		}
		//显示的页码数，按规定的数量显示，并且以当前页为中心，前面显示4,后面显示5页
		if(pageObj.totalPage>pageObj.pageFootNum){
			if(pageObj.currentPage>pageObj.behPageNum && pageObj.currentPage<=pageObj.totalPage-pageObj.behPageNum){
				for(var i=(pageObj.currentPage-pageObj.prePageNum);i<=(pageObj.currentPage+pageObj.behPageNum);i++){
					//当前页显示不同样式
					if(pageObj.currentPage==i){
						htmlstr+='<a class="chan_page_check" onclick="webPluginPage.fenye('+pageIndex+','+i+')">'+i+'</a>';
					}else{
						htmlstr+='<a  style="cursor: pointer;" onclick="webPluginPage.fenye('+pageIndex+','+i+')">'+i+'</a>';
					}
				}	
			}else if(pageObj.currentPage>pageObj.totalPage-pageObj.behPageNum){
				for(var i=(pageObj.totalPage-pageObj.pageFootNum+1);i<=pageObj.totalPage;i++){
					//当前页显示不同样式
					if(pageObj.currentPage==i){
						htmlstr+='<a class="chan_page_check" onclick="webPluginPage.fenye('+pageIndex+','+i+')">'+i+'</a>';
					}else{
						htmlstr+='<a style="cursor: pointer;" onclick="webPluginPage.fenye('+pageIndex+','+i+')">'+i+'</a>';
					}
				}	
			}else if(pageObj.currentPage<=pageObj.behPageNum){
				for(var i=1;i<=pageObj.pageFootNum;i++){
					//当前页显示不同样式
					if(pageObj.currentPage==i){
						htmlstr+='<a class="chan_page_check" onclick="webPluginPage.fenye('+pageIndex+','+i+')">'+i+'</a>';
					}else{
						htmlstr+='<a style="cursor: pointer;" onclick="webPluginPage.fenye('+pageIndex+','+i+')">'+i+'</a>';
					}
				}
			}
		}else{
			for(var i=1;i<=pageObj.totalPage;i++){
				//当前页显示不同样式
				if(pageObj.currentPage==i){
					htmlstr+='<a class="chan_page_check" onclick="webPluginPage.fenye('+pageIndex+','+i+')">'+i+'</a>';
				}else{
					htmlstr+='<a style="cursor: pointer;" onclick="webPluginPage.fenye('+pageIndex+','+i+')">'+i+'</a>';
				}
			}
		}
		if(pageObj.currentPage == pageObj.totalPage){
			htmlstr += '<a href="javascript:;" class="page_turn_btn over_page">&gt;</a>';
		}else{
			var numValue = pageObj.currentPage + 1;
			if(pageObj.currentPage == pageObj.totalPage){
				htmlstr += '<a href="javascript:;" class="page_turn_btn over_page">&gt;</a>';
			}else{
				htmlstr += '<a href="javascript:;" class="page_turn_btn" style="cursor: pointer;" onclick="webPluginPage.fenye('+pageIndex+','+numValue+')">&gt;</a>';
			}
			
		}
		htmlstr += '<a href="javascript:;" onclick="webPluginPage.fenye('+pageIndex+','+pageObj.totalPage+')">末页</a>';
		//显示总页数
		htmlstr += '<a>共'+pageObj.totalPage+'页</a>';
		htmlstr += '<div class="chant_page_num"><span>第</span><input type="text" class="input_page_num" id="webPluginPage_ser'+pageIndex+'"/><span>页</span></div>';
		htmlstr += '<a href="javascript:;" class="funds_btn wid60 margr20" onclick="webPluginPage.prefenye('+pageIndex+')">确定</a>';
		//如果实际页码数大于最大能显示的页码数，则提供选择页数跳转的插件
	}
	jQuery("#"+pageObj.pageFoot).append(htmlstr);
 }
 
 //点击页码查询数据方法
 webPluginPage.fenye = function (pageIndex,currentPage){
 	var pageObj = this.page[pageIndex-1];
 	if(this.page[pageIndex-1].currentPage != currentPage){
 		this.page[pageIndex-1].currentPage = currentPage;
		var scrollTopVal = jQuery("#"+pageObj.trRootId).offset().top;
		jQuery('html, body').animate({scrollTop:scrollTopVal-100}, 1000);
		this.loadData(pageIndex);
 	}
 }
 
 //输入页码查询数据方法
 webPluginPage.prefenye = function (pageIndex){
 	var cPage = jQuery("#webPluginPage_ser"+pageIndex).val();
 	var pageObj = this.page[pageIndex-1];
 	var scrollTopVal = jQuery("#"+pageObj.trRootId).offset().top;
	jQuery('html, body').animate({ scrollTop:scrollTopVal-100}, 1000);
 	if((/^([+]?)\d*\.?\d+$/).test(cPage) && cPage <=  pageObj.totalPage && cPage != pageObj.currentPage){
 		this.page[pageIndex-1].currentPage = cPage;
 		this.loadData(pageIndex);
 	}else{
 		alert("请输入正确的页码");
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
	jQuery("[name='webPluginPage_checkbox"+pageIndex+"']").each(function(){
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
 
 webPluginPage.addLineChangeColor = function (trRootId){
 	jQuery("#"+trRootId).siblings().each(function(index){
		/*
		 *if(index%2 == 0){
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
		 */
		
	});
 }
 
 webPluginPage.ajaxCallback = function(data,pageIndex){
 	
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
			var htmlstr = "<tr style='background:white'><td colspan='15' height='80' align='center'>"+web_plugin_common.constant.noDataWarningStyle+"</td></tr>";
			jQuery("#"+pageObj.trRootId).after(htmlstr);
		}else{
			if(web_plugin_common.isEmpty(pageObj.noDataWarning)){
				jQuery("#"+pageObj.trRootId).html(web_plugin_common.constant.noDataWarningStyle);
			}else{
				jQuery("#"+pageObj.trRootId).html(pageObj.noDataWarning);
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
				var selfParam = jQuery("#"+pageObj.trRootId).attr("param").split(".");
				if(selfParam.length == 1){
					htmlstr+=eval( "(" +jQuery("#"+pageObj.trRootId).attr("param")+"("+i+","+pageIndex+")"+")" );
				}else if(selfParam.length == 2){
					htmlstr+=eval( "(" +selfParam[0]+")" )[selfParam[1]](i,pageIndex);
				}
			}
			
		}
		if(!pageObj.selfMode){
			//jQuery("#"+pageObj.trRootId).html("sss");
			jQuery("#"+pageObj.trRootId).after(htmlstr);
		}else{
			if(pageObj.accumulate && pageObj.currentPage>1){
				jQuery("#"+pageObj.trRootId).append(htmlstr);
			}else{
				jQuery("#"+pageObj.trRootId).html(htmlstr);
			}
			
		}
		
		//拼接页码部分
		if(!web_plugin_common.isEmpty(pageObj.pageFoot) && pageObj.totalPage > 1){
			this.appendPageFoot(pageObj,i,pageIndex);
		}
		
		//创建复选框选择事件
		if(!web_plugin_common.isEmpty(pageObj.checkboxId)){
			jQuery("#"+pageObj.checkboxId).click(function(){
				jQuery("[name='webPluginPage_checkbox"+pageIndex+"']").each(function(){
					jQuery(this).attr("checked",jQuery("#"+pageObj.checkboxId).attr("checked"));
				})
			})
		}
		
		//添加隔行换色的样式
		if (!pageObj.selfMode) {
			this.addLineChangeColor(pageObj.trRootId);
		}
		
		//预留接口，方便需要列表加载完成之后调用方法的时候调用
		webPluginPage.loadFinish();
	}	
 }
 
 webPluginPage.flushTableWithoutAjax = function(pageIndex){
 	var pageObj = this.page[pageIndex-1];
	
	if(!web_plugin_common.isEmpty(pageObj.totalCountShowId)){
		jQuery("#"+pageObj.totalCountShowId).html(pageObj.totalCount);
	}
	
	//判断数据
	if(web_plugin_common.isEmpty(pageObj.items) || pageObj.items.length <= 0){
		if(!pageObj.selfMode){
			htmlstr = "<tr><td colspan='15'>"+web_plugin_common.constant.noDataWarningStyle+"</td></tr>";
			jQuery("#"+pageObj.trRootId).after(htmlstr);
		}else{
			if(web_plugin_common.isEmpty(pageObj.noDataWarning)){
				jQuery("#"+pageObj.trRootId).html(web_plugin_common.constant.noDataWarningStyle);
			}else{
				jQuery("#"+pageObj.trRootId).html(pageObj.noDataWarning);
			}
			
		}	
		return;
	}else {
		var htmlstr = '';
		for(var i = 0; i < pageObj.items.length ; i++){
			//判断是否是普通列表还是要自定义内容的列表
			if(!pageObj.selfMode){
				htmlstr += this.parseData(pageObj,items[i],i,pageIndex);
			}else{
				var selfParam = jQuery("#"+pageObj.trRootId).attr("param").split(".");
				if(selfParam.length == 1){
					htmlstr+=eval( "(" +jQuery("#"+pageObj.trRootId).attr("param")+"("+i+","+pageIndex+")"+")" );
				}else if(selfParam.length == 2){
					htmlstr+=eval( "(" +selfParam[0]+")" )[selfParam[1]](i,pageIndex);
				}
				
			}
		}
		
		if(!pageObj.selfMode){
			jQuery("#"+pageObj.trRootId).after(htmlstr);
		}else{
			if(pageObj.accumulate && pageObj.currentPage>1){
				jQuery("#"+pageObj.trRootId).append(htmlstr);
			}else{
				jQuery("#"+pageObj.trRootId).html(htmlstr);
			}
		}
		
		//拼接页码部分
		if(!web_plugin_common.isEmpty(pageObj.pageFoot) && pageObj.totalPage > 1){
			this.appendPageFoot(pageObj,i,pageIndex);
		}
		
		//创建复选框选择事件
		if(!web_plugin_common.isEmpty(pageObj.checkboxId)){
			jQuery("#"+pageObj.checkboxId).click(function(){
				jQuery("[name='webPluginPage_checkbox"+pageIndex+"']").each(function(){
					jQuery(this).attr("checked",jQuery("#"+pageObj.checkboxId).attr("checked"));
				})
			})
		}
		
		//添加隔行换色的样式
		if (!pageObj.selfMode) {
			this.addLineChangeColor(pageObj.trRootId);
		}
		
		//预留接口，方便需要列表加载完成之后调用方法的时候调用
		webPluginPage.loadFinish();
	}	
 }
 webPluginPage.appendQueryString = function(pageObj){
 	var queryString = "currentPage="+pageObj.currentPage+"&pageSize="+pageObj.pageNumber;
	queryString += "&" + pageObj.queryString;
	return queryString;
 }
 
 //实现异步调用后的回调函数
 web_plugin_common.pageCallback = function(data,methodFlag){
	webPluginPage.ajaxCallback(data,methodFlag);
 };
 
 //预留接口，方便需要列表加载完成之后调用方法的时候调用
 webPluginPage.loadFinish = function(){
 	
 };
 
 
 /*
 * 序号栏       <th param="web_plugin_sequence" width="5%" nowrap="nowrap">序号</th>
 * 操作栏       <th param="web_plugin_operate" width="20%" nowrap="nowrap">操作</th>
 * 复选框栏     <th param="web_plugin_radio" property="shoppingCartId@productId" width="10%" nowrap="nowrap"><input type="checkbox" id="radio"/></th>
 * 复选框栏     <th param="web_plugin_checkbox" property="shoppingCartId@productId" width="10%" nowrap="nowrap"><input type="checkbox" id="checkbox"/></th>
 * 普通属性栏   <th param="productName" nowrap="nowrap" width="30%">产品名称</th>
 * 数据字典栏   <th param="publishStatus" dictType="publishStatus" width="12%" nowrap="nowrap">发布状态</th>
 * 时间栏       <th param="shoppingTime" format="yyyy-MM-dd HH:mm" nowrap="nowrap" width="20%">收藏时间</th>
 * 自拼接栏     <th param="bigImage" selfDefind="getProductImage" multiple="true" nowrap="nowrap" width="10%">产品配图</th>
 * multiple 是否可以多行显示 默认false,其它栏也可以加该属性
 */
