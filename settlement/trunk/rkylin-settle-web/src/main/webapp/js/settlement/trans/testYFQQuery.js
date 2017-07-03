

/***
 * 查询银企直联测试交易并汇总金额
 */
function summary() {
	var accountDate = $('#accountDate').val();
	if(accountDate){
		$.post(
				$('#dataForm').attr("action"),//发送请求 url
				{"accountDate":$('#accountDate').val()},
				function(data) {
					if(data.msg){
						art.dialog.tips("<span style='color:green'>汇总成功!!!</span>", 2.5, function(){});
					}else{
						art.dialog.tips("<span style='color:green'>汇总失败!!!</span>", 2.5, function(){});
					}
				},
				'json'
			);
	}else{
		art.dialog.tips("<span style='color:green'>账期不能为空!!!</span>", 2.5, function(){});
	}
	
}
