/***
 * 
 */
$(function() {
	$('#btnSubmit').click(function() {//提交按钮单击事件
		formSubmit();
	});
});
/***
 * 验证修改 金额必须是
 */
function valiAmount(theAmount) {
	if(theAmount==0) {
		return true;
	} else {
		return theAmount.match(new RegExp(/^[0-9]*[1-9][0-9]*$/));
	}
}
/***
 * 修改信息的表单提交
 */
function formSubmit() {
	if(!valiAmount(document.getElementById('feeAmount').value) || !valiAmount(document.getElementById('userFee').value)) {
		art.dialog.tips("<lable style='color:red'>'金额'必须是 0 或者 正整数!</lable>", 2.5, function() {});
		return;
	} else {
		$.post(
			$('#dataForm').attr("action"),			//发送请求的url
			$('#dataForm').serialize(),				//表单信息
			function(data) {						//回调函数
				art.dialog.tips(data.msg, 1.5, openManager);
			},
			'json'
		);
	}
}
/***
 * 跳转到管理页面
 */
function openManager() {
	location = CONTEXT_PATH + 'transdetail/detail_manager';
}