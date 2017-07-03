jQuery(document).ready(function(){
	var timeFlag = $("#timeFlag").val();
	if(timeFlag==true || timeFlag=='true'){
		$("#btnClick").html("立即跳转");
		commonShow.comTimeNum = 3;
		clearTimeout(commonShow.comTimeNum);
		commonShow.comDisplayTime();
	}
	$("#btnClick").click(function(){
		window.location=CONTEXT_PATH+$("#returnUrl").val();
	});
});

var commonShow={
	comTimeNum:3,
	comTimeOutId:"",
	comDisplayTime:function(){
		if(this.comTimeNum == 0){
			$("#btnClick").click();
		}else{
			this.comTimeNum = this.comTimeNum - 1;
			$("#timeInfoShow").html(this.comTimeNum +"秒后将自动跳转至我的账户");
			commonShow.comTimeOutId = setTimeout("commonShow.comDisplayTime()", 1000);
		}
	}
}