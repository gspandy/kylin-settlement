$(function() {

	$("img").bind("click", function() {
		var imgSrcValue = $(this).attr("src");
		if ($(this).attr("title") != undefined
				&& $(this).attr("title").indexOf("点击") > -1) {
			if (imgSrcValue != undefined && imgSrcValue != "") {
				if (self.frameElement && self.frameElement.tagName == "IFRAME") { // 判断是否在IFram中
					$("#img_Show", $(parent.document)).attr("src", imgSrcValue);
					rDrag.init($("#div_showImg", $(parent.document)).get(0));

				} else {
					$("#img_Show").attr("src", imgSrcValue);
					rDrag.init(document.getElementById("div_showImg"));
				}
				showXqWin("div_showImg");
			}
		}
	});

});

/**
 * 关闭
 */
function closeXqWin1(divId) {
	$("#"+divId).hide();
	// 判断如果有弹窗显示的时候，不关闭背景蒙层
	var isLightBox =true;
	$(".lightbox").each(function(){
		if($(this).css("display") == "block"){
			isLightBox = false;
			return false;
		}
	});
	if(isLightBox){
		$("#lightboxOverlay").hide();
	} 
		
}
/**
 * 弹窗
 */
function showXqWin(divId) {
	if (self.frameElement && self.frameElement.tagName == "IFRAME") { // 判断是否在IFram中
		// 显示弹窗
		var heig = $(parent.document).height();
		var wid = $(parent.document).width();

		var screenH = screen.height;
		var yScroll;
		// 取滚动条高度
		if (self.pageYOffset) {
			yScroll = self.pageYOffset;
		} else if (document.documentElement
				&& document.documentElement.scrollTop) {
			yScroll = document.documentElement.scrollTop;
		} else if (document.body) {
			yScroll = document.body.scrollTop;
		}
		var top = (heig - $("#" + divId, $(parent.document)).height()) / 2;
		top = top < 0 ? 5 : top;
		var left = (wid - $("#" + divId, $(parent.document)).width()) / 2;
		left = left < 0 ? 5 : left;
		$("#" + divId, $(parent.document)).css({
					"display" : "block",
					"z-index" : "100000",
					"top" : top + "px",
					"left" : left + "px"
				});
	} else {
		// 显示弹窗
		var heig = $(document).height();
		var wid = $(document).width();
		var screenH = screen.height;
		var yScroll;
		// 取滚动条高度
		if (self.pageYOffset) {
			yScroll = self.pageYOffset;
		} else if (document.documentElement
				&& document.documentElement.scrollTop) {
			yScroll = document.documentElement.scrollTop;
		} else if (document.body) {
			yScroll = document.body.scrollTop;
		}
		var top = (heig - $("#" + divId).height()) / 2;
		top = top < 0 ? 5 : top;
		var left = (wid - $("#" + divId).width()) / 2;
		left = left < 0 ? 5 : left;

		$("#" + divId).css({
					"display" : "block",
					"z-index" : "100000",
					"top" : top + "px",
					"left" : left + "px"
				});

	}

}