$(function() {
	$('#collateButt').click(function() {
		doCollecte($(this));
	});
});
/***
 * 对账
 */
function doCollecte($butt) {
	//防止重复点击
	if($butt.html() != "对账") return;
	//操作确认
	if(!confirm("您确定要执行【账务系统&多渠道系统】的内部对账吗?")) return;
	
	var url = $("#collecte").val();
	var inRootInstCd = $("#inRootInstCd").val();
	var accountDate = $("#accountDate").val();
	var data = {"inRootInstCd":inRootInstCd,"accountDate":accountDate};
	$butt.html("操作中 ...");
	var callBack = function(data) {
		$butt.html("对账");
		
		if(data == 'success') {
			art.dialog.tips("<span style='color:green'>账户跟多渠道对账成功!</span>", 2.5, function(){});
		} else {
			art.dialog.tips("<span style='color:red'>"+ data +"</span>", 2.5, function(){});
		}
	};
	var type = "text";
	
	
	$.get(url, data, callBack, type);
}
