/***
 * 
 */
$(function() {
	$('#btnReturn').click(function() {//返回按钮单击事件
		openManager();
	});
});
/***
 * 修改信息的表单提交
 */
function formSubmit() {
	$.post(
		$('#dataForm').attr("action"),			//发送请求的url
		$('#dataForm').serialize(),				//表单信息
		function(data) {						//回调函数
			art.dialog.tips(data.msg, 1.5, openManager);
		},
		'json'
	);
}
/***
 * 跳转到管理页面
 */
function openManager() {
	location = CONTEXT_PATH + 'profitrule/rule_manager';
}