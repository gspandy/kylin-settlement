/***
 * 
 */
$(function() {
	$("#btnSubmit").click(function() {
		valieQueryTime();	
	});
	artDialog.tip1s = function (content, time) {
	    return artDialog({
	        id: 'Tips',
	        title: false,
	        cancel: false,
	        fixed: true,
	        lock: false
	    })
	    .content('<div style="padding: 0 1em;">' + content + '</div>')
	    .time(time || 1);
	};
	
	$("#autoBtn").click(function() {
		$("#batch").val(autoBatchNo());
	});
});
/***
 * 
 */
function valieQueryTime() {
	var $accountDate = $("#accountDate");
	if($accountDate && $accountDate.val()) {
		getOrdInfoAndSearch(1);
	} else {
		var $div = $("#valiDiv");
		$div.css("font-style", "italic");
		$div.css("color", "#ff0000");
		$div.html("&nbsp;请输入账期再进行对账操作&nbsp;");
	}
}
/***
 * 
 */
function loadingFun() {
	var butt = document.getElementById("btnSubmit");
	butt.onclick = function() {};
	butt.style.width = "100px";
	butt.innerHTML = "正在上传... ...";
}
function successedFun() {
	var butt = document.getElementById("btnSubmit");
	butt.style.width = "60px";
	butt.innerHTML = "开始上传";
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
			successedFun();
			art.dialog.tip1s(data.msg,2);
		},
		'json'
	);
}

/***
 * 生成对账文件 ajax请求
 */
function makefile() {
	var url = $("#makefilebtn").val();
	$.post(
			url,			//发送请求的url
			$('#dataForm').serialize(),				//表单信息
			function(data) {						//回调函数
				art.dialog.tips(data.msg, 1.5, openManager);
			},
			'json'
		);
}
/***
 * 自动生产批次号
 */
function autoBatchNo() {
	var batchNO = "";
	var result = "";
	var date = new Date();
	batchNO += date.getTime();
	
	for(var i = 0; i < 2; i ++ ) {
		var ranNum = Math.ceil(Math.random() * 25); //生成一个0到25的数字
        //大写字母'A'的ASCII是65,A~Z的ASCII码就是65 + 0~25;然后调用String.fromCharCode()传入ASCII值返回相应的字符并push进数组里
        result += String.fromCharCode(65+ranNum);
    }
	return result + batchNO;
}
