jQuery(document).ready(function(){
});

var commonTipFun={
	div_z_index : 1000,                   //弹出层的层级关系
	pop : [{
		divId:"",                          //弹出层div
		width:0,                           //宽
		height:0,                          //高
		shade:true,                        //是否遮罩，默认true显示
		scrollMove:false,                  //是否随滚动条移动,默认false不随滚动条移动,
		timeOver:false,					   //是否倒计时
		timOverValue:3                    //倒计时
	}],
	popTimeNum:3,
	popOutId:"",
	tipNum:3,
	tipOutId:"",
	windowWidth : jQuery(window).width(),
	windowHeight : jQuery(window).height(),
	shadeDivId : "web_plugin_shadeDiv",
	displayTipTime:function(){
		if(this.tipNum == 0){
			$("#lightboxOverlay")[0].style.display = 'none';
			$("#showSpanTip")[0].style.display = 'none';
			$("#msgShowSpan").html("");
		}else{
			this.tipNum = this.tipNum - 1;
			$("#seconda").html("自动关闭倒计时："+this.tipNum+"&nbsp;");
			commonTipFun.tipOutId = setTimeout("commonTipFun.displayTipTime()", 1000);
		}
	},displayPopTime:function(index){
		if(this.popTimeNum == 0){
			commonTipFun.closePop(index);
		}else{
			this.popTimeNum = this.popTimeNum - 1;
			commonTipFun.popOutId = setTimeout("commonTipFun.displayPopTime('"+index+"')", 1000);
		}
	},showTipDiv : function(popIndex){
		var popObj = this.pop[popIndex];
		this.pop[popIndex] = this.setDefaultValue(popObj);
		//判断是否需要遮罩层
		if(popObj.shade){
			commonTipFun.shadeTipDiv(popIndex);
		}
		jQuery("#"+popObj.divId).show();
		
		//是否弹出层要随浏览器滚动而移动
		var divLeft = (commonTipFun.windowWidth-popObj.width)/2;
		var divTop = (commonTipFun.windowHeight - popObj.height)/2;
		if(divTop > 40){
			divTop = divTop - 30;
		}
		var divPosition = "fixed";
		
		//如果是ie6，因为不支持fixed定位属性，所以不给于绝对居中定位，会随着滚动条移动而移动
		if(popObj.scrollMove || (jQuery.browser.msie && jQuery.browser.version == "6.0")){
			divPosition = "absolute";
			divLeft = (commonTipFun.commonTipFun.windowWidth-popObj.width)/2;
			divTop = (jQuery(document).scrollTop()+(commonTipFun.windowHeight-popObj.height)/2);
			if(divTop > 40){
				divTop = divTop - 30;
			}
			//divTop = (windowHeight - popObj.height/2);
		}
		
		//设置div样式，位置 居中显示
		jQuery("#"+popObj.divId).css({
			"position":divPosition,
			"width":popObj.width+"px",
			"height":popObj.height+"px",
			"left":divLeft+"px",
			"top":divTop+"px",
			"z-index":commonTipFun.div_z_index++
		});
		if(popObj.timeOver){
			this.popTimeNum =popObj.timeOverValue;
			clearTimeout(commonTipFun.popOutId);
			commonTipFun.displayPopTime(popIndex);
		}
	},shadeTipDiv:function(popIndex){
		var shadeDiv = '<div id="'+this.shadeDivId+popIndex+'"></div>';
		jQuery("body").prepend(shadeDiv);
		jQuery("#"+this.shadeDivId+popIndex).css({
			"position":"absolute",
			"background-color":"#000000",
			"width":jQuery(window).width()+"px",
			"height":jQuery(document).height()+"px",
			"filter":"alpha(opacity = 30)",
			"opacity":"0.3",
			"z-index":commonTipFun.div_z_index++
		});
	},closePop:function(popIndex){
		var popObj = this.pop[popIndex];
		jQuery("#"+popObj.divId).hide();	
		if(popObj.closeBtn){
			jQuery("#"+popObj.closeId).remove();
		}
		if(popObj.shade){
			jQuery("#"+this.shadeDivId+popIndex).remove();
			jQuery("#"+this.shadeDivId+popIndex).remove();
		}
	},setDefaultValue:function(popObj){
		if(popObj.shade != false){
			popObj.shade = true;
		}
		/*if(popObj.closeBtn != false){
			popObj.closeBtn = true;
			popObj.closeId = popObj.divId+"close";
		}*/
		if(popObj.scrollMove != true){
			popObj.scrollMove = false;
		}
		if(popObj.timerOver != true){
			popObj.timerOver = false;
		}
		if(popObj.timerOverValue==null||popObj.timerOverValue==''){
			popObj.timerOverValue = 3;
		}
		return popObj;
	}
}
