String.prototype.trim = function() {
	
	return this.replace(/^\s\s*/, '').replace(/\s\s*$/, '');
}

$(document).ready(function() {

	// table中td显示...内容的方法
	function getBytesCount(str) {
		if (str == null) {
			return 0;
		} else {
			return (str.length + str.replace(/[\u0000-\u00ff]/g, "").length);
		}
	}
	// select点击下拉菜单的方法
	$("a.icon_arrow_down").click(function(event) {
				event.stopPropagation();
				$("a.icon_arrow_down").each(function() {
							if ($(this).attr("id") != undefined) {
								var oId = $(this).attr("id").split('_')[0];
								$("#" + oId + "_sel2_con2").hide();
							}

						});
				if ($(this).attr("id") != undefined) {
					var aId = $(this).attr("id").split('_')[0];
					$("#" + aId + "_sel2_con2").show();
				}
			});

	$("#cb_input1").click(function() {
				if ($(this).attr("class") == "left tb_all_checkbox") {
					$("#user_table1 .tb_input_checkbox").addClass("chan_check");
					$(this).addClass("chan_check");
					$("#user_table1 input:checkbox").attr("checked", "checked");
				} else {
					$("#user_table1 .tb_input_checkbox")
							.removeClass("chan_check");
					$(this).removeClass("chan_check");
					$("#user_table1 input:checkbox").removeAttr("checked");
				}

			});

	$(".tb_input_checkbox:not([nocheck])").bind("click", function() {
		if ($("#typeId").val() == "check") {

		} else {
			if ($(this).attr("class") == "left tb_input_checkbox"
					|| $(this).attr("class") == "left tb_input_checkbox margt5") {
				$(this).addClass("chan_check");
				$(this).siblings("input:checkbox")[0].checked = true;
				var a = 1;
			} else {
				$(this).removeClass("chan_check");
				$(this).siblings("input:checkbox").removeAttr("checked");
				$(this).siblings("input:checkbox")[0].checked = false;
				var a = 1;
			}
		}

	});

	//点击up和down图标显示和隐藏内容的方法
	$("#reg2_arrow").click(function() {
				if ($(this).attr("class") == "margt10 btn_arrow_up") {
					$("#reg2_arrow_con").css("display", "none");
					$(this).removeClass("btn_arrow_up")
							.addClass("btn_arrow_down");
				} else if ($(this).attr("class") == "margt10 btn_arrow_down") {
					$("#reg2_arrow_con").css("display", "block");
					$(this).removeClass("btn_arrow_down")
							.addClass("btn_arrow_up");
				}
			});
	
	//点击up和down图标显示和隐藏内容的方法
	$(".up_or_down_button").click(function() {
				if ($(this).attr("class") == "up_or_down_button margt10 btn_arrow_up") {
					$(this).parent("div").next(".up_or_down_con").css("display", "none");
					$(this).removeClass("btn_arrow_up")
							.addClass("btn_arrow_down");
				} else if ($(this).attr("class") == "up_or_down_button margt10 btn_arrow_down") {
					$(this).parent("div").next(".up_or_down_con").css("display", "block");
					$(this).removeClass("btn_arrow_down")
							.addClass("btn_arrow_up");
				}
			});
});
/**
 * 获取字符串字节数
 * 
 * @return {}
 */
String.prototype.getBytesLength = function() {
	return this.replace(/[^\x00-\xff]/gi, "--").length;
}
/*
 * 表格复选框的全选或取消 参数是全选复选框对应label的this
 */
function checkBoxAll(e, tableName) {
	// var src = e.target || window.event.srcElement;
	var tbName = "user_table1";
	if (tableName && tableName != "") {
		tbName = tableName;
	}
	if ($(e).attr("class") == "left tb_input_checkbox") {
		$("#" + tbName).find(".tb_input_checkbox").addClass("chan_check");
		$(e).addClass("chan_check");
		$("#" + tbName).find("input:checkbox").each(function() {
					this.checked = true;
				});
	} else {
		$("#" + tbName + " .tb_input_checkbox").removeClass("chan_check");
		$(e).removeClass("chan_check");
		$("#" + tbName).find("input:checkbox").each(function() {
					$(this).removeAttr("checked");
					this.checked = false;
				});
	}

}
/*
 * 表格复选框的选中或取消 参数是复选框对应label的this
 */
function checkBoxSel(obj) {

	if ($(obj).attr("class") == "left tb_input_checkbox"
			|| $(obj).attr("class") == "left tb_input_checkbox margt5") {
		$(obj).addClass("chan_check");
		$(obj).siblings("input:checkbox")[0].checked = true;
	} else {
		$(obj).removeClass("chan_check");
		$(obj).siblings("input:checkbox").removeAttr("checked");
		$(obj).siblings("input:checkbox")[0].checked = false;
	}
}

/*
 * 绘制分页 total:总记录数而不是总页数 pageIndex:请求的当前页 pageSize:每页显示条数
 * funSearch(1):需要有配套的请求数据的方法,第一个参数是请求的页码,方法里再次调用drawPage绘制分页
 * div.chan_table_page:分页代码绘制到含该类样式的div层 chan_page_check样式表示当前页的样式 pageId
 * :如果指定分页显示的层的话传层的id type
 * ：若一个页面有多个分页可以在指定分页层后指定是哪个的分页，在funSearch(pageIndex,type)中区分执行的方法
 */

function lastStart(pageIndex, size) {
	for (i = pageIndex - 1; i >= 0; i--) {
		if (i % size == 0) {
			return i + 1;
		}
	}
}
function firstEnd(pageIndex, totalPage, size) {
	for (i = pageIndex; i < totalPage; i++) {
		if (i % size == 0) {
			return i;
		}
	}
	return totalPage;
}
function drawPage(total, pageIndex, pageSize, pageId, type) {
	if (!total || total == "" || total == 0) {
		total = 1;
	}
	var totalPage = parseInt(((total - 1) / pageSize) + 1);
	if (pageIndex > totalPage) {
		pageIndex = totalPage;
	}

	var pageHtml = ['<a href="javascript:;" '];
	if (type == null || type == "") {
		pageHtml.push(' onclick="funSearch(1)">首页</a>');
	} else {
		pageHtml.push(' onclick="funSearch(1,\'' + type + '\')">首页</a>');
	}
	if (pageIndex == 1) {
		pageHtml
				.push('<a href="javascript:;" class="page_turn_btn over_page">&lt;</a>');
	} else {
		pageHtml
				.push('<a href="javascript:;" class="page_turn_btn" onclick="funSearch(');
		pageHtml.push(pageIndex - 1);

		if (null != type && "" != type) {
			pageHtml.push(",'");
			pageHtml.push(type);
			pageHtml.push("'");
		}

		pageHtml.push(')">&lt;</a>');
	}

	if (totalPage < 6) {
		for (var i = 1; i <= totalPage; i++) {
			if (pageIndex == i) {
				pageHtml
						.push('<a href="javascript:;" class="chan_page_check" onclick="funSearch(');
				pageHtml.push(i);

				if (null != type && "" != type) {
					pageHtml.push(",'");
					pageHtml.push(type);
					pageHtml.push("'");
				}

				pageHtml.push(')">');
				pageHtml.push(i);
				pageHtml.push('</a>');
			} else {
				pageHtml.push('<a href="javascript:;" onclick="funSearch(');
				pageHtml.push(i);

				if (null != type && "" != type) {
					pageHtml.push(",'");
					pageHtml.push(type);
					pageHtml.push("'");
				}

				pageHtml.push(')">');
				pageHtml.push(i);
				pageHtml.push('</a>');
			}
		}
	} else {
		// var start = lastStart(pageIndex, 5);
		// var end = firstEnd(pageIndex, totalPage, 5);
		var start = pageIndex * 1 - 2;

		var end = pageIndex * 1 + 2;

		if (start < 1) {
			if (pageIndex * 1 == 2)
				end = pageIndex * 1 + 3;
			else
				end = pageIndex * 1 + 4;

			start = 1;
		}
		if (end > totalPage) {
			end = totalPage;
			start = totalPage * 1 - 4;
		}

		if (start > 1) {
			if (null == type || "" == type) {
				pageHtml.push('<a href="javascript:;" onclick="funSearch('
						+ (Number(start) - 1) + ')">…</a>');
			} else {
				pageHtml.push('<a href="javascript:;" onclick="funSearch('
						+ (Number(start) - 1) + ',\'' + type + '\')">…</a>');
			}
		}
		for (var i = start; i <= end; i++) {
			if (pageIndex == i) {
				pageHtml
						.push('<a href="javascript:;" class="chan_page_check" onclick="funSearch(');
				pageHtml.push(i);

				if (null != type && "" != type) {
					pageHtml.push(",'");
					pageHtml.push(type);
					pageHtml.push("'");
				}

				pageHtml.push(')">');
				pageHtml.push(i);
				pageHtml.push('</a>');
			} else {
				pageHtml.push('<a href="javascript:;" onclick="funSearch(');
				pageHtml.push(i);

				if (null != type && "" != type) {
					pageHtml.push(",'");
					pageHtml.push(type);
					pageHtml.push("'");
				}

				pageHtml.push(')">');
				pageHtml.push(i);
				pageHtml.push('</a>');
			}
		}
		if (end < totalPage) {
			if (type == null || type == "") {
				pageHtml.push('<a href="javascript:;" onclick="funSearch('
						+ (Number(end) + 1) + ')">…</a>');
			} else {
				pageHtml.push('<a href="javascript:;" onclick="funSearch('
						+ (Number(end) + 1) + ',\'' + type + '\')">…</a>');
			}
		}
	}

	if (pageIndex < totalPage) {
		pageHtml
				.push('<a href="javascript:;" class="page_turn_btn" onclick="funSearch(');
		pageHtml.push(Number(pageIndex) + 1);

		if (null != type && "" != type) {
			pageHtml.push(",'");
			pageHtml.push(type);
			pageHtml.push("'");
		}

		pageHtml.push(')">&gt;</a>');
	} else {
		pageHtml
				.push('<a href="javascript:;" class="page_turn_btn over_page">&gt;</a>');
	}
	pageHtml.push('<a href="javascript:;" onclick="funSearch(');
	pageHtml.push(totalPage);

	if (null != type && "" != type) {
		pageHtml.push(",'");
		pageHtml.push(type);
		pageHtml.push("'");
	}

	pageHtml.push(')">尾页</a>');

	pageHtml
			.push('<div class="chant_page_num"><span>第</span><input type="text" class="input_page_num" toNum="toPage"  value="'
					+ pageIndex
					+ '" /><span>页</span></div><a href="javascript:;" class="right chan_btn wid60 margr65" goBtn="goPage">确定</a>');
	
	var pageObj;
	if (pageId) {
		$("#" + pageId).html(pageHtml.join(''));
		pageObj = $("#" + pageId);
	} else {
		$("div.chan_table_page:not([id])").html(pageHtml.join(''));
		pageObj = $("div.chan_table_page:not([id])");
	}

	$('a[goBtn="goPage"]',$(pageObj)).click(function() {
		// var toPageIndex = $("#toPage").val();
		var toPageIndex = $(this).siblings('div').find('input[toNum="toPage"]')
				.val();
		if (!toPageIndex || !/^\d+$/.test(toPageIndex)
				|| toPageIndex > totalPage || toPageIndex < 1) {
			// if(null==type||""==type){
			// funSearch(1);
			// }else{
			// funSearch(1,type);
			// }
			// $("#toPage").val("");
			$(this).siblings('div').find('input[toNum="toPage"]').val("");
		} else {
			if (null == type || "" == type) {
				funSearch(toPageIndex);
			} else {
				funSearch(toPageIndex, type);
			}
		}
	});
	/*
	 * 获取页面高度
	 */
	getheight();
}
// #start
// function drawPage(total, pageIndex, pageSize) {
// if (!total || total == "" || total == 0) {
// total = 1;
// }
// var totalPage = parseInt(((total - 1) / pageSize) + 1);
// var length = totalPage - pageIndex;
// // var temp = pageIndex;
// var pageHtml = ['<a href="javascript:;" onclick="funSearch(1)">首页</a>'];
// if (pageIndex == 1) {
// pageHtml
// .push('<a href="javascript:;" class="page_turn_btn over_page">&lt;</a>');
// } else {
// pageHtml
// .push('<a href="javascript:;" class="page_turn_btn" onclick="funSearch(');
// pageHtml.push(pageIndex - 1);
// pageHtml.push(')">&lt;</a>');
// }
//
// if (totalPage <= 6) {
// for (var i = 1; i <= totalPage; i++) {
// if (pageIndex == i) {
// pageHtml
// .push('<a href="javascript:;" class="chan_page_check" onclick="funSearch(');
// pageHtml.push(i);
// pageHtml.push(')">');
// pageHtml.push(i);
// pageHtml.push('</a>')
// } else {
// pageHtml.push('<a href="javascript:;" onclick="funSearch(');
// pageHtml.push(i);
// pageHtml.push(')">');
// pageHtml.push(i);
// pageHtml.push('</a>')
// }
// }
// } else {
// var temp = pageIndex > 2 ? pageIndex - 2 : 1;
// var plen = null;
// if (pageIndex < 3) {
// plen = 3;
// } else if (pageIndex == totalPage) {
// plen = pageIndex;
// } else {
// plen = pageIndex + 1;
// }
// for (var i = temp; i <= plen; i++) {
// if (pageIndex == i) {
// pageHtml
// .push('<a href="javascript:;" class="chan_page_check" onclick="funSearch(');
// pageHtml.push(i);
// pageHtml.push(')">');
// pageHtml.push(i);
// pageHtml.push('</a>');
// } else {
// pageHtml.push('<a href="javascript:;" onclick="funSearch(');
// pageHtml.push(i);
// pageHtml.push(')">');
// pageHtml.push(i);
// pageHtml.push('</a>');
// }
// }
// if (pageIndex + 1 < totalPage) {
// pageHtml.push("<span class='left'>……</span>");
// }
// var star = totalPage - 1;
// if (star <= pageIndex + 1) {
// star = pageIndex + 2;
// }
// for (var i = star; i <= totalPage; i++) {
// pageHtml.push('<a href="javascript:;" onclick="funSearch(');
// pageHtml.push(i);
// pageHtml.push(')">');
// pageHtml.push(i);
// pageHtml.push('</a>');
// }
// }
//
// if (pageIndex < totalPage) {
// pageHtml
// .push('<a href="javascript:;" class="page_turn_btn" onclick="funSearch(');
// pageHtml.push(Number(pageIndex) + 1);
// pageHtml.push(')">&gt;</a>');
// } else {
// pageHtml
// .push('<a href="javascript:;" class="page_turn_btn over_page">&gt;</a>');
// }
// pageHtml.push('<a href="javascript:;" onclick="funSearch(');
// pageHtml.push(totalPage);
// pageHtml.push(')">尾页</a>');
//
// pageHtml
// .push('<div class="chant_page_num"><span>第</span><input type="text"
// class="input_page_num" id="toPage" /><span>页</span></div><a
// href="javascript:;" class="right chan_btn wid60 margr65"
// id="goPage">确定</a>');
// $("div.chan_table_page").html(pageHtml.join(''));
//
// $("#goPage").click(function() {
// var toPageIndex = $("#toPage").val();
// if (!toPageIndex || !/^\d+$/.test(toPageIndex)
// || toPageIndex > totalPage) {
// alert("您输入的页码不在范围内");
// } else {
// funSearch(toPageIndex);
// }
// });
//
// }
// #end
// old
/*
 * function drawPage(total, pageIndex, pageSize) { if (!total || total == "" ||
 * total == 0) { total = 1; } var totalPage = parseInt(((total - 1) / pageSize) +
 * 1); var length = totalPage - pageIndex; // var temp = pageIndex; var pageHtml = ['<a
 * href="javascript:;" onclick="funSearch(1)">首页</a>']; if (pageIndex == 1) {
 * pageHtml .push('<a href="javascript:;" class="page_turn_btn over_page">&lt;</a>'); }
 * else { pageHtml .push('<a href="javascript:;" class="page_turn_btn"
 * onclick="funSearch('); pageHtml.push(pageIndex - 1); pageHtml.push(')">&lt;</a>'); }
 * 
 * if (totalPage <= 6) { for (var i = 1; i <= totalPage; i++) { if (pageIndex ==
 * i) { pageHtml .push('<a href="javascript:;" class="chan_page_check"
 * onclick="funSearch('); pageHtml.push(i); pageHtml.push(')">');
 * pageHtml.push(i); pageHtml.push('</a>') } else { pageHtml.push('<a
 * href="javascript:;" onclick="funSearch('); pageHtml.push(i);
 * pageHtml.push(')">'); pageHtml.push(i); pageHtml.push('</a>') } } } else {
 * var temp = pageIndex > 2 ? pageIndex - 2 : 1; var plen=null; if(pageIndex<3){
 * plen=3; }else{ plen=pageIndex; } for (var i = temp; i <= plen; i++) { if
 * (pageIndex == i) { pageHtml .push('<a href="javascript:;"
 * class="chan_page_check" onclick="funSearch('); pageHtml.push(i);
 * pageHtml.push(')">'); pageHtml.push(i); pageHtml.push('</a>'); } else {
 * pageHtml.push('<a href="javascript:;" onclick="funSearch(');
 * pageHtml.push(i); pageHtml.push(')">'); pageHtml.push(i); pageHtml.push('</a>'); } }
 * if(pageIndex<totalPage-1){ pageHtml.push("<span class='left'>……</span>"); }
 * var star=totalPage - 2; if(star<=pageIndex){ star=pageIndex+1; } for (var i =
 * star; i <= totalPage; i++) { pageHtml.push('<a href="javascript:;"
 * onclick="funSearch('); pageHtml.push(i); pageHtml.push(')">');
 * pageHtml.push(i); pageHtml.push('</a>'); } }
 * 
 * if (pageIndex < totalPage) { pageHtml .push('<a href="javascript:;"
 * class="page_turn_btn" onclick="funSearch('); pageHtml.push(Number(pageIndex) +
 * 1); pageHtml.push(')">&gt;</a>'); } else { pageHtml .push('<a
 * href="javascript:;" class="page_turn_btn over_page">&gt;</a>'); }
 * pageHtml.push('<a href="javascript:;" onclick="funSearch(');
 * pageHtml.push(totalPage); pageHtml.push(')">尾页</a>');
 * 
 * pageHtml .push('<div class="chant_page_num"><span>第</span><input
 * type="text" class="input_page_num" id="toPage" /><span>页</span></div><a
 * href="javascript:;" class="right chan_btn wid60 margr65" id="goPage">确定</a>');
 * $("div.chan_table_page").html(pageHtml.join(''));
 * 
 * $("#goPage").click(function() { var toPageIndex = $("#toPage").val(); if
 * (!toPageIndex || !/^\d+$/.test(toPageIndex) || toPageIndex > totalPage) {
 * alert("您输入的页码不在范围内"); } else { funSearch(toPageIndex); } }); }
 */


/*
 * 显示alert 需要给包含按钮的div加id等于alertButton closeFun表示关闭窗体执行的方法,可为空
 * closeParam表示关闭窗体执行方法的参数
 */
function showAlert(message, closeFun, closeParam) {
	if (self.frameElement && self.frameElement.tagName == "IFRAME") { // 判断是否在IFram中		
		var heig = $(parent.document).height() + "px";
		var screenH = screen.height;
		var yScroll;
		// 取滚动条高度
		if (self.pageYOffset) {
			yScroll = self.pageYOffset;
		} else if (parent.document.documentElement
				&& parent.document.documentElement.scrollTop) {
			yScroll = parent.document.documentElement.scrollTop;
		} else if (parent.document.body) {
			yScroll = parent.document.body.scrollTop;
		}
		$("#lightboxOverlay", $(parent.document)).css({
					width : "100%",
					height : heig,
					display : "block"
				});
		$("#confirm_message", $(parent.document)).html(message);
		$("#alertButton", $(parent.document)).hide();
		$("#confirm_ok", $(parent.document)).unbind("click");
		if (closeFun && closeFun != "") {
			if (!closeParam || closeParam == "") {
				closeParam = {};
			}
			$("#closeWinBtn", $(parent.document)).bind("click", closeParam,
					closeFun);
		}
		$("#delete_win", $(parent.document))
				.css(
						"top",
						(screenH / 2 - 40
								- $("#delete_win", $(parent.document)).height()
								/ 2 + yScroll)
								+ "px");
		$("#delete_win", $(parent.document)).show();

	} else {
		var heig = $(document).height() + "px";
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
		$("#lightboxOverlay").css({
					width : "100%",
					height : heig,
					display : "block"
				});
		$("#confirm_message").html(message);
		$("#alertButton").hide();
		$("#confirm_ok").unbind("click");
		if (closeFun && closeFun != "") {
			if (!closeParam || closeParam == "") {
				closeParam = {};
			}
			$("#closeWinBtn").bind("click", closeParam, closeFun);
		}
		$("#delete_win").css(
				"top",
				(screenH / 2 - 40 - $("#delete_win").height() / 2 + yScroll)
						+ "px");
		$("#delete_win").show();
	}

}
function showAlert1(message, closeFun, closeParam) {
	if (self.frameElement && self.frameElement.tagName == "IFRAME") { // 判断是否在IFram中
		var heig = $(parent.document).height() + "px";
		var screenH = screen.height;
		var yScroll;
		// 取滚动条高度
		if (self.pageYOffset) {
			yScroll = self.pageYOffset;
		} else if (parent.document.documentElement
				&& parent.document.documentElement.scrollTop) {
			yScroll = parent.document.documentElement.scrollTop;
		} else if (parent.document.body) {
			yScroll = parent.document.body.scrollTop;
		}
		$("#lightboxOverlay", $(parent.document)).css({
					width : "100%",
					height : heig,
					display : "block"
				});
		$("#confirm_message", $(parent.document)).html(message);
		$("#alertButton", $(parent.document)).hide();
		$("#confirm_ok", $(parent.document)).unbind("click");
		if (closeFun && closeFun != "") {
			if (!closeParam || closeParam == "") {
				closeParam = {};
			}
			$("#closeWinBtn", $(parent.document)).bind("click", closeParam,
					closeFun);
		}
		$("#delete_win", $(parent.document))
				.css(
						"top",
						(screenH / 2 - 40
								- $("#delete_win", $(parent.document)).height()
								/ 2 + yScroll)
								+ "px");
		$("#delete_win b", $(parent.document)).html("");
		$("#delete_win span", $(parent.document)).removeClass("icon_warning");
		$("#delete_win", $(parent.document)).show();

	} else {
		var heig = $(document).height() + "px";
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
		$("#lightboxOverlay").css({
					width : "100%",
					height : heig,
					display : "block"
				});
		$("#confirm_message").html(message);
		$("#alertButton").hide();
		$("#confirm_ok").unbind("click");
		if (closeFun && closeFun != "") {
			if (!closeParam || closeParam == "") {
				closeParam = {};
			}
			$("#closeWinBtn").bind("click", closeParam, closeFun);
		}
		$("#delete_win").css(
				"top",
				(screenH / 2 - 40 - $("#delete_win").height() / 2 + yScroll)
						+ "px");
		$("#delete_win b").html("");
		$("#delete_win span").removeClass("icon_warning");
		$("#delete_win").show();
	}

};
/*
 * message:显示confirm message:要现实的消息,需要给显示文字的label加id等于confirm_message
 * opra:确定按钮显示的文字,需要给取消按钮的<a/>加id等于confirm_cancel
 * cancelFun取消按钮绑定的事件方法名,如closeConfirm cancelParam绑定的取消事件的参数,json格式
 * okFun确定按钮绑定的事件方法名,需要给确定按钮的<a/>加idconfirm_ok okParam绑定的确定时间的参数,json格式
 * 
 * 事件方法中获取参数格式为 function doModify(event){ var jsonKey=event.data.jsonKey; var
 * jsonKey2=event.data.jsonKey2; }
 */
function showConfirm(message, opra, cancelFun, cancelParam, okFun, okParam) {
	var heig = $(document).height() + "px";
	var screenH = screen.height;
	var yScroll;
	// 取滚动条高度
	if (self.pageYOffset) {
		yScroll = self.pageYOffset;
	} else if (document.documentElement && document.documentElement.scrollTop) {
		yScroll = document.documentElement.scrollTop;
	} else if (document.body) {
		yScroll = document.body.scrollTop;
	}
	$("#lightboxOverlay").css({
				width : "100%",
				height : heig,
				display : "block"
			});

	$("#confirm_cancel").unbind("click");
	$("#confirm_ok").unbind("click");
	$("#confirm_message").html(message);
	$("#confirm_cancel").bind("click", cancelParam, cancelFun);
	$("#confirm_ok").text(opra);
	$("#confirm_ok").bind("click", okParam, okFun);
	$("#alertButton").show();
	$("#delete_win")
			.css(
					"top",
					(screenH / 2 - 60 - $("#delete_win").height() / 2 + yScroll)
							+ "px");
	$("#delete_win").show();
}
/*
 * 关闭弹窗的方法
 * 
 */
function closeConfirm(event) {
	// event.preventDefault();
	closePop('#delete_win');
}
function closePop(obj) {
	var moreLight = false;
	$(".lightbox").each(function() {
		if ($(this).css("display") == "block"
				&& "#" + $(this).attr("id") != obj) {
			moreLight = true;
			return false;
		}
	});

	if (moreLight) {
		return;
	} else {
		$("#lightboxOverlay").css("display", "none");
		$(obj).css("display", "none");
	}
}
/*
 * 带参数post跳转 url:action指向的跳转地址
 * params:提交的字段参数json数组[{"name":"a","value":"1"},{"name":"b","value":"2"}]
 */
function postLocation(url, params) {
	var formHtml = ['<form id="redirectForm" method="post" action="'];
	formHtml.push(url);
	formHtml.push('">');
	if (params) {
		for (p in params) {
			formHtml.push('<input type="hidden" name="');
			formHtml.push(params[p].name);
			formHtml.push('" value="');
			formHtml.push(params[p].value);
			formHtml.push('"/>');
		}
	}

	formHtml.push('</form>');
	$("body").append(formHtml.join(''));
	$("#redirectForm").submit();
}
/**
 * 展开或折叠ztree树的根节点
 * 
 * @param {}
 *            treeid ztree的id
 * @param {}
 *            flag true为展开节点,false为折叠节点
 */
function expandFirstNode(treeid, flag) {
	var treeObj = $.fn.zTree.getZTreeObj(treeid);
	treeObj.expandNode(treeObj.getNodes()[0], flag);
}
/**
 * 展开或折叠ztree树的所有节点
 * 
 * @param {}
 *            treeid ztree的id
 * @param {}
 *            flag true为展开节点,false为折叠节点
 */
function expandAllNode(treeid, flag) {
	var treeObj = $.fn.zTree.getZTreeObj(treeid);
	treeObj.expandAll(flag);
}
/**
 * 返回弹窗提示 注意:需要在触发事件的按钮上没有默认行为,可用javascript:;或javascript:void(0);
 * url为要返回的地址，为空则执行后退
 */
function returnAlert(url) {
	var message = "您还没有保存对当前页面的编辑，是否要放弃保存并离开此页面？<br/>选择'离开'，放弃并离开当前页面<br/>选择'取消'，继续编辑或保存当前页面";
	showConfirm(message, "离开", closeConfirm, {}, redirectReturn, {
				"url" : url
			});
}
// 后退
function redirectReturn(event) {
	var url = event.data.url;
	if (url && url != "") {
		// window.location=url;
		gotoUrl(url);
	} else {
		window.history.go(-1);
	}

}

// 关闭下拉层
function closeSelect() {
	$(".down_pop_select").hide();
}

/**
 * 日志table
 * 
 * @param {}
 *            data,是ajax请求返回的值
 * 
 * @param pageIndex：页码
 *            url：查询日志列表的action 明细查询的时候，需要获取logId，属性名必须是logId
 *            divId：绘制分页的div的class名称，如果div的class名称是chan_table_page，这个参数可以不传
 * 
 * 需要注意的：绘制table的 id必须是：user_table2
 */
function drawLogsTable(data, pageIndex, url, divId) {

	var list = data.result;
	if (list && 0 < list.length) {
		var trdata = ['<tr> <th width="8%" >序号 </th>   <th width="20%">操作用户  </th><th width="22%">操作时间   </th><th width="35%">操作内容</th> <th width="15%">查看明细 </th> </tr>'];
		for (var i = 0; i < list.length; i++) {

			trdata.push('<tr><td> ');
			trdata.push(i + 1);
			trdata.push('</td> <td>');
			trdata.push(list[i].personName);
			trdata.push('</td> <td>');
			trdata.push(list[i].lastUpdatedTime.slice(0, 19));
			trdata.push('</td> <td>');
			trdata.push(list[i].logTypeId);
			trdata.push('</td> <td>');
			// trdata.push('<a
			// href="depatMentManager.do?action=todetailLog&logId='+list[i].logId
			// +'" target="_blank" ');

			if (list[i].logTypeId == "修改") {
				trdata.push('<a href=');
				trdata.push(url + '&logId=' + list[i].logId);
				trdata.push(' target="_blank" ');
				trdata.push('>明细</a>');
			}

			trdata.push('</td></tr> ');
		}

		$("#user_table2").html(trdata.join(''));

		var total = data.totalNum;
		drawPage(total, pageIndex, 5, divId, 'log');

	} else {

		var nodata = "<tr><td>没有符合条件的数据</td></tr>";
		$("#user_table2").html(nodata);
		drawPage(1, pageIndex, 5, divId, 'log');
		return false;
	}

}

// 判断某个值在权限集合中(json数组)是否存在

function existsInCollection(list, val) {
	for (var i = 0; i < list.length; i++) {
		var node = list[i];
		if (node.rightCode == val) {
			return true;
		}
	}
	return false;
}

function ShowWindow(obj) {
	// 显示弹窗
	var heig = $(document).height() + "px";
	var screenH = screen.height;
	var yScroll;
	// 取滚动条高度
	if (self.pageYOffset) {
		yScroll = self.pageYOffset;
	} else if (document.documentElement && document.documentElement.scrollTop) {
		yScroll = document.documentElement.scrollTop;
	} else if (document.body) {
		yScroll = document.body.scrollTop;
	}
	$("#lightboxOverlay").css({
				width : "100%",
				height : heig,
				display : "block"
			});
	$(obj).css("display", "block");
	$(obj)
			.css("top",
					(screenH / 2 - 40 - $(obj).height() / 2 + yScroll) + "px");
}
/**
 * 使焦点定位到显示错误的项
 * 
 * @param {}
 *            suffix 在错误项上加的后缀，适用于显示错误的元素id是错误项的id+后缀
 * @param {}
 *            type 如果不是上述情况，用type区别，type==attr表示从属性error中取错误项的id
 */
function errFocus(suffix, type) {
	var spans = $("span[class='label_txt wid343 margt10 red01'],span[class='label_txt wid190 margt10 red01']");
	spans.each(function() {
				if ($(this).text() != "") {
					var id = null;

					if (type == "attr") {
						id = $(this).attr("error");
					} else if ($(this).is("[id]")) {
						id = $(this).attr("id").replace(suffix, "");
					}

					$("#" + id).focus();
					return false;
				}
			});
}
/**
 * 自动关闭提示窗口(showAlert、showConfirm)
 * 
 * @param {}
 *            msec 等待关闭的时间，单位毫秒
 * @param {}
 *            url可选，如果传入url则表示自动关闭窗口后跳转到的地址
 */
function autoCloseWindow(msec, url) {
	setTimeout(function() {
				closeConfirm();
				if (url && url != "") {
					gotoUrl(url);
				}
			}, msec);
}
function returnIndexWithPage(url, page) {
	postLocation(url, [{
						"name" : "pageIndex",
						"value" : page
					}]);
}
// 下载文件
function downloadFile(url) {
	url = url.replace(/\\/g, "/");
	try {
		var elemIF = document.createElement("iframe");
		elemIF.src = url;
		elemIF.style.display = "none";
		document.body.appendChild(elemIF);
		return true;
	} catch (e) {
		showAlert("文件下载失败，请稍后再试！");
		return false;
	}
}

/**
 * 根据参数不同改变页面显示数据
 * 
 * @param numPage
 */
function initPage(numPage) {
	var pageSize = 10;
	var num = numPage;
	var initialIndex = (num - 1) * pageSize /* + 1 */;
	var lastIndex = (num * pageSize) - 1;
	$("#tb_TerminalList tr").show();
	$("#tb_TerminalList tr:lt(" + initialIndex + ")").hide();
	$("#tb_TerminalList tr:gt(" + lastIndex + ")").hide();
	var numLastIndex = $("#numIndex a").length;
	if (num >= 2 && num != numLastIndex) {
		var upnum = num - 2;
		var downnum = num;
		var nownum = downnum - 1;
		$("#numIndex a").show();
		$("#numIndex a:eq(" + nownum + ")").addClass("anow").siblings()
				.removeClass("anow");
		$("#numIndex a:lt(" + upnum + ")").hide();
		$("#numIndex a:gt(" + downnum + ")").hide();
	} else if (num == numLastIndex) {
		var tupnum = num - 3;
		var nownum = num - 1;
		$("#numIndex a").show();
		$("#numIndex a:eq(" + nownum + ")").addClass("anow").siblings()
				.removeClass("anow");
		$("#numIndex a:lt(" + tupnum + ")").hide();
	} else if (num == 1) {
		var supnum = num + 1;
		$("#numIndex a").show();
		$("#numIndex a:eq(0)").addClass("anow").siblings().removeClass("anow");
		$("#numIndex a:gt(" + supnum + ")").hide();
	}
};
/**
 * 最后页的显示
 */
function lastPage() {
	var tbLength = $("#tb_TerminalList tr").length;
	var lastPage = Math.ceil(tbLength / 10);
	initPage(lastPage);
}
/**
 * 添加分页按钮
 */
function numIndex() {
	$("#numIndex").html("");
	var tbLength = $("#tb_TerminalList tr").length;
	var lastPage = Math.ceil(tbLength / 10);
	var maxPage = lastPage;
	var str = "";
	for (var i = 1; i <= maxPage; i++) {
		if (i > 3) {
			str = str + "<a id=page" + i
					+ " href='javascript:;' onclick='initPage(" + i
					+ ")' style='display:none'>" + i + "</a>";
		} else {
			str = str + "<a id=page" + i
					+ " href='javascript:;' onclick='initPage(" + i + ")'>" + i
					+ "</a>";
		}
	}
	$("#numIndex").append(str);
}

// 获取url参数
function getUrlParam(name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); // 构造一个含有目标参数的正则表达式对象
	var r = window.location.search.substr(1).match(reg); // 匹配目标参数
	if (r != null)
		return unescape(r[2]);
	return null; // 返回参数值

}
/**
 * 返回弹窗提示 注意:需要在触发事件的按钮上没有默认行为,可用javascript:;或javascript:void(0);
 * url为要返回的地址，为空则执行后退 params key=value&key2=value2形式
 */
function returnAlertPost(url, params) {
	var message = "您还没有保存对当前页面的编辑，是否要放弃保存并离开此页面？<br/>选择'离开'，放弃并离开当前页面<br/>选择'取消'，继续编辑或保存当前页面";
	showConfirm(message, "离开", closeConfirm, {}, redirectReturnPost, {
				"url" : url,
				"params" : params
			});
}
/**
 * 没有提示 返回指定的URL地址，可携带参数 url为要返回的地址，为空则执行后退 params key=value&key2=value2形式
 */
function returnPost(url, params) {
	redirectReturnPost(null, url, params);
}
// 后退
function redirectReturnPost(event, url2, params2) {

	var url;
	var params;
	if (event) {
		url = event.data.url;
		params = event.data.params;
	} else {
		url = url2;
		params = params2;
	}
	if (url && url != "") {
		var postParams = ["["];// 组合跳转到页面携带的参数
		if (params && params != "") {
			var items = params.split("&");
			if (items && items.length > 0) {
				for (var i = 0; i < items.length; i++) {
					var item = items[i];
					var keyvalue = item.split("=");
					var key = "";
					var value = "";
					if (keyvalue.length > 0) {
						key = keyvalue[0];
					}
					if (keyvalue.length > 1) {
						value = keyvalue[1];
					}
					postParams.push("{\"name\":\"");
					postParams.push(key);
					postParams.push("\",\"value\":\"");
					postParams.push(value);
					postParams.push("\"}");
					postParams.push(",")
				}
			}
		}
		if (postParams.slice(-1) == ",") {
			postParams.pop();
		}
		postParams.push("]");
		postLocation(url, $.parseJSON(postParams.join("")));
	} else {
		window.history.go(-1);
	}

}

/**
 * 
 * @author yy
 * 
 * 日志table
 * 
 * @param {}
 *            data,是ajax请求返回的值
 * 
 * @param pageIndex：页码
 *            url：查询日志列表的action 明细查询的时候，需要获取logId，属性名必须是logId
 *            divId：绘制分页的div的class名称，如果div的class名称是chan_table_page，这个参数可以不传
 * 
 * 需要注意的：绘制table的 id必须是：user_table2
 */
function getLogsTable(data, pageIndex, url, divId) {
	var list = data.result;
	if (list && 0 < list.length) {
		// var len = pageIndex * 5;
		// len = len > list.length ? list.length : len;

		var trdata = ['<tr> <th width="8%" >序号 </th>   <th width="20%">操作用户  </th><th width="22%">操作时间   </th><th width="35%">操作内容</th> <th width="15%">查看明细 </th> </tr>'];
		// for (var i = (pageIndex - 1) * 5; i < len; i++) {
		for (var i = 0; i < list.length; i++) {
			trdata.push('<tr><td> ');
			trdata.push(i + 1);
			trdata.push('</td> <td>');
			trdata.push(list[i].personName);
			trdata.push('</td> <td>');
			trdata.push(list[i].lastUpdatedTime.slice(0, 19));
			trdata.push('</td> <td>');
			trdata.push(list[i].logTypeId);
			trdata.push('</td> <td>');
			// trdata.push('<a
			// href="depatMentManager.do?action=todetailLog&logId='+list[i].logId
			// +'" target="_blank" ');

			if (list[i].logTypeId == "修改") {
				trdata.push('<a href=');
				trdata.push(url + '&logId=' + list[i].logId);
				trdata.push(' target="_blank" ');
				trdata.push('>明细</a>');
			}
			trdata.push('</td></tr> ');
		}
		$("#user_table2").html(trdata.join(''));
		var total = data.totalNum;
		// var total = list.length;
		drawFenPage(total, pageIndex, 5, divId, 'log');
	} else {
		var nodata = "<tr><td>没有符合条件的数据</td></tr>";
		$("#user_table2").html(nodata);
		drawFenPage(1, pageIndex, 5, divId, 'log');
		return false;
	}
}
/* 绘制分页 -yy */
function drawFenPage(total, pageIndex, pageSize, pageId, type) {
	if (!total || total == "" || total == 0) {
		total = 1;
	}
	var totalPage = parseInt(((total - 1) / pageSize) + 1);
	if (pageIndex > totalPage) {
		pageIndex = totalPage;
	}

	var pageHtml = ['<a href="javascript:;" '];
	if (type == null || type == "") {
		pageHtml.push(' onclick="getLogsList(1)">首页</a>');
	} else {
		pageHtml.push(' onclick="getLogsList(1,\'' + type + '\')">首页</a>');
	}
	if (pageIndex == 1) {
		pageHtml
				.push('<a href="javascript:;" class="page_turn_btn over_page">&lt;</a>');
	} else {
		pageHtml
				.push('<a href="javascript:;" class="page_turn_btn" onclick="getLogsList(');
		pageHtml.push(pageIndex - 1);

		if (null != type && "" != type) {
			pageHtml.push(",'");
			pageHtml.push(type);
			pageHtml.push("'");
		}

		pageHtml.push(')">&lt;</a>');
	}

	if (totalPage < 6) {
		for (var i = 1; i <= totalPage; i++) {
			if (pageIndex == i) {
				pageHtml
						.push('<a href="javascript:;" class="chan_page_check" onclick="getLogsList(');
				pageHtml.push(i);

				if (null != type && "" != type) {
					pageHtml.push(",'");
					pageHtml.push(type);
					pageHtml.push("'");
				}

				pageHtml.push(')">');
				pageHtml.push(i);
				pageHtml.push('</a>');
			} else {
				pageHtml.push('<a href="javascript:;" onclick="getLogsList(');
				pageHtml.push(i);

				if (null != type && "" != type) {
					pageHtml.push(",'");
					pageHtml.push(type);
					pageHtml.push("'");
				}

				pageHtml.push(')">');
				pageHtml.push(i);
				pageHtml.push('</a>');
			}
		}
	} else {
		var start = lastStart(pageIndex, 5);
		var end = firstEnd(pageIndex, totalPage, 5);
		if (start > 1) {
			if (null == type || "" == type) {
				pageHtml.push('<a href="javascript:;" onclick="getLogsList('
						+ (start - 1) + ')">…</a>');
			} else {
				pageHtml.push('<a href="javascript:;" onclick="getLogsList('
						+ (start - 1) + ',\'' + type + '\')">…</a>');
			}
		}
		for (var i = start; i <= end; i++) {
			if (pageIndex == i) {
				pageHtml
						.push('<a href="javascript:;" class="chan_page_check" onclick="getLogsList(');
				pageHtml.push(i);

				if (null != type && "" != type) {
					pageHtml.push(",'");
					pageHtml.push(type);
					pageHtml.push("'");
				}

				pageHtml.push(')">');
				pageHtml.push(i);
				pageHtml.push('</a>');
			} else {
				pageHtml.push('<a href="javascript:;" onclick="getLogsList(');
				pageHtml.push(i);

				if (null != type && "" != type) {
					pageHtml.push(",'");
					pageHtml.push(type);
					pageHtml.push("'");
				}

				pageHtml.push(')">');
				pageHtml.push(i);
				pageHtml.push('</a>');
			}
		}
		if (end < totalPage) {
			if (type == null || type == "") {
				pageHtml.push('<a href="javascript:;" onclick="getLogsList('
						+ (end + 1) + ')">…</a>');
			} else {
				pageHtml.push('<a href="javascript:;" onclick="getLogsList('
						+ (end + 1) + ',\'' + type + '\')">…</a>');
			}
		}
	}

	if (pageIndex < totalPage) {
		pageHtml
				.push('<a href="javascript:;" class="page_turn_btn" onclick="getLogsList(');
		pageHtml.push(Number(pageIndex) + 1);

		if (null != type && "" != type) {
			pageHtml.push(",'");
			pageHtml.push(type);
			pageHtml.push("'");
		}

		pageHtml.push(')">&gt;</a>');
	} else {
		pageHtml
				.push('<a href="javascript:;" class="page_turn_btn over_page">&gt;</a>');
	}
	pageHtml.push('<a href="javascript:;" onclick="getLogsList(');
	pageHtml.push(totalPage);

	if (null != type && "" != type) {
		pageHtml.push(",'");
		pageHtml.push(type);
		pageHtml.push("'");
	}

	pageHtml.push(')">尾页</a>');

	pageHtml
			.push('<div class="chant_page_num"><span>第</span><input type="text" class="input_page_num" toNum="toPage" value="'
					+ pageIndex
					+ '" /><span>页</span></div><a href="javascript:;" class="right chan_btn wid60 margr65" goBtn="goPage">确定</a>');

	if (pageId) {
		$("#" + pageId).html(pageHtml.join(''));
	} else {
		$("div.chan_table_page:not([id])").html(pageHtml.join(''));
	}

	$('a[goBtn="goPage"]').live("click", function() {
		// var toPageIndex = $("#toPage").val();
		var toPageIndex = $(this).siblings('div').find('input[toNum="toPage"]')
				.val();
		if (!toPageIndex || !/^\d+$/.test(toPageIndex)
				|| toPageIndex > totalPage || toPageIndex < 1) {
			// if(null==type||""==type){
			// funSearch(1);
			// }else{
			// funSearch(1,type);
			// }
			// $("#toPage").val("");
			$(this).siblings('div').find('input[toNum="toPage"]').val("");
		} else {
			if (null == type || "" == type) {
				getLogsList(toPageIndex);
			} else {
				getLogsList(toPageIndex, type);
			}
		}
	});
}
// 判断jquery的版本；来调用on和live方法
$(function() {

			var jq19 = false;
			$("script").each(function() {
				var src = $(this).attr("src");
				if (src != undefined) {
					if (src.indexOf("jquery-1.9.0.js") > -1
							|| src.indexOf("jquery-1.9.1.min.js") > -1) {
						jq19 = true;
						return false;
					}
				}
			});

			if (jq19) {
				$("td").on("mouseover", function() {
					var ht = $(this).text().trim();
					var htm =$(this).html().trim();
					var w = $(this).width();
					var l = getBytesCount(ht) * 8;
					if ($(this).parents("table").html().trim().indexOf("TH") > -1
							|| $(this).parents("table").html().indexOf("th") > -1) {
						if (l > w && htm.indexOf("A") < 0 && htm.indexOf("a") < 0) {
							$(this).attr("title", ht);
						}else if( htm.indexOf("A") > 0 && htm.indexOf("a") > 0){
							$(this).removeAttr("title");
						}
					}
				});
			} else {
				$("td").live("mouseover", function() {
					var ht = $(this).text().trim();
					var htm =$(this).html().trim();
					var w = $(this).width();
					var l = getBytesCount(ht) * 8;
					if ($(this).parents("table").html().trim().indexOf("TH") > -1
							|| $(this).parents("table").html().indexOf("th") > -1) {
						if (l > w && htm.indexOf("A") < 0 && htm.indexOf("a") < 0) {
							$(this).attr("title", ht);
						}else if( htm.indexOf("A") > 0 && htm.indexOf("a") > 0){
							$(this).removeAttr("title");
						}
					}
				});
			}

		});
/**
 * 获取cookie
 * 
 * @param {}
 *            c_name
 * @return {String}
 */
function getCookie(c_name) {
	if (document.cookie.length > 0) { // 先查询cookie是否为空，为空就return ""
		c_start = document.cookie.indexOf(c_name + "=") // 通过String对象的indexOf()来检查这个cookie是否存在，不存在就为
		// -1
		if (c_start != -1) {
			c_start = c_start + c_name.length + 1 // 最后这个+1其实就是表示"="号啦，这样就获取到了cookie值的开始位置
			c_end = document.cookie.indexOf(";", c_start) // 其实我刚看见indexOf()第二个参数的时候猛然有点晕，后来想起来表示指定的开始索引的位置...这句是为了得到值的结束位置。因为需要考虑是否是最后一项，所以通过";"号是否存在来判断
			if (c_end == -1)
				c_end = document.cookie.length
			return unescape(document.cookie.substring(c_start, c_end)) // 通过substring()得到了值。想了解unescape()得先知道escape()是做什么的，都是很重要的基础，想了解的可以搜索下，在文章结尾处也会进行讲解cookie编码细节
		}
	}
	return ""
}
/**
 * js获得参数
 * 
 * @param {}
 *            param
 * @return {String}
 */
function getParameter(param) {
	var query = window.location.search;
	var iLen = param.length;
	var iStart = query.indexOf(param);
	if (iStart == -1)
		return "";
	iStart += iLen + 1;
	var iEnd = query.indexOf("&", iStart);
	if (iEnd == -1)
		return query.substring(iStart);
	return query.substring(iStart, iEnd);
}

/** ***拖动***** */
/*
 * var rDrag = { o:null, init:function(o){ o.onmousedown = this.start; },
 * start:function(e){ var o; e = rDrag.fixEvent(e); e.preventDefault &&
 * e.preventDefault(); rDrag.o = o = this; o.x = e.clientX - rDrag.o.offsetLeft;
 * o.y = e.clientY - rDrag.o.offsetTop; document.onmousemove = rDrag.move;
 * document.onmouseup = rDrag.end; }, move:function(e){ e = rDrag.fixEvent(e);
 * var oLeft,oTop; oLeft = e.clientX - rDrag.o.x; oTop = e.clientY - rDrag.o.y;
 * rDrag.o.style.left = oLeft + 'px'; rDrag.o.style.top = oTop + 'px'; },
 * end:function(e){ e = rDrag.fixEvent(e); rDrag.o = document.onmousemove =
 * document.onmouseup = null; }, fixEvent: function(e){ if (!e) { e =
 * window.event; e.target = e.srcElement; e.layerX = e.offsetX; e.layerY =
 * e.offsetY; } return e; } }
 */
/** ***拖动***** */
var rDrag = {
	o : null,
	init : function(o) {
		o.onmousedown = this.start;
	},
	start : function(e) {
		var o;
		e = rDrag.fixEvent(e);
		e.preventDefault && e.preventDefault();
		rDrag.o = o = this;
		o.x = e.clientX - rDrag.o.offsetLeft;
		o.y = e.clientY - rDrag.o.offsetTop;
		parent.document.onmousemove = rDrag.move;
		parent.document.onmouseup = rDrag.end;
	},
	move : function(e) {
		e = rDrag.fixEvent(e);
		var oLeft, oTop;
		oLeft = e.clientX - rDrag.o.x;
		oTop = e.clientY - rDrag.o.y;

		/** 在当前可视窗口拖动* */

		var maxLeft = $(parent.document).width() - rDrag.o.offsetWidth;
		var maxTop = $(parent.document).height() - rDrag.o.offsetHeight;
		oLeft = oLeft < 0 ? 0 : oLeft;
		oTop = oTop < 0 ? 0 : oTop;
		oLeft = oLeft > maxLeft ? maxLeft : oLeft;
		oTop = oTop > maxTop ? maxTop : oTop;

		/** 在当前可视窗口拖动* */

		rDrag.o.style.left = oLeft + 'px';
		rDrag.o.style.top = oTop + 'px';
	},
	end : function(e) {
		e = rDrag.fixEvent(e);
		rDrag.o = parent.document.onmousemove = parent.document.onmouseup = null;
	},
	fixEvent : function(e) {
		/* alert(e || window.event) */
		/*
		 * if (!e) { e = window.event; e.target = e.srcElement; e.layerX =
		 * e.offsetX; e.layerY = e.offsetY; } return e;
		 */
		// alert(window.document);
		var event = e || window.event || window.parent.event;
		var target = event.target || event.srcElement;
		var x = event.layerX || event.offsetX;
		var y = event.layerY || event.offsetY;

		event.target = target;
		event.layerX = x;
		event.layerY = y;
		return event;
	}
}

// 求2个日期天数差值
function dateDiff(d1, d2) {

	if (d1 == null) {
		d1 = "";
	}
	if (d2 == null) {
		d2 = "";
	}
	if (d1 == "" && d2 == "") {
		return -1;
	}

	var day = 24 * 60 * 60 * 1000;
	try {
		var dateArr = d1.split("-");
		var checkDate = new Date();
		checkDate.setFullYear(dateArr[0], dateArr[1] - 1, dateArr[2]);
		var checkTime = checkDate.getTime();

		var dateArr2 = d2.split("-");
		var checkDate2 = new Date();
		checkDate2.setFullYear(dateArr2[0], dateArr2[1] - 1, dateArr2[2]);
		var checkTime2 = checkDate2.getTime();
		var cha = (checkTime2 - checkTime) / day;
		return parseInt(cha) ;;
	} catch (e) {
		return false;
	}
}

/**
 * yy 保存分页信息
 * 
 */

// 获取查询条件
function getSelectData(selectArray, index) {
	var saveSelectMap = $("#saveSelectData").val();
	 //alert(saveSelectMap);
	// 保存条件赋值到页面
	if (saveSelectMap != null && saveSelectMap != "" && index == "index") {
		data = $.parseJSON(saveSelectMap);
		for (var i = 0; i < selectArray.length; i++) {
			var name = selectArray[i].split("_")[1];
			if (selectArray[i].split("_")[0] == "input") {
				$("#" + name).val(data[name]);
				if (name == "pageSize") {
					$("#pageSize_input2").text(data[name]);
				}
			} else if (selectArray[i].split("_")[0] == "select") {
				$("#" + name).val(data[name]);
				$("#" + name + "_input2").text(data[name + "_input2"]);
			}else if(selectArray[i].split("_")[0]=="radio"){
				$("input[name="+selectArray[i].split("_")[1]+"]").each(function(){
					if($(this).val()==data[name]){
						$(this).attr("checked","checked");
					}
				});
			}else if(selectArray[i].split("_")[0]=="linkage"){
				$("#" + name).val(data[name]);
				$("#" + name + "_input2").text(data[name + "_input2"]);
				$("#" + name + "_sel2_con2").html(data[name + "_sel2_con2"]);
			}
		}
	}
}

// 保存查询条件
function saveSelectData(selectArray, totalNum, formName,index) {
	if(index=="index"){
		return false;
	}
	// 保存下拉列表文字
	if ($("#saveSelect").length > 0) {
		$("#saveSelect").text("");
	} else {
		$('form').append('<div id="saveSelect"></div>');
	}
	for (var i = 0; i < selectArray.length; i++) {
		if (selectArray[i].split("_")[0] == "select") {
			var value = $("#" + selectArray[i].split("_")[1] + "_input2")
					.text();
			$('#saveSelect').append('<input type="hidden" id="'
					+ selectArray[i].split("_")[1] + 'Text"  name="'
					+ selectArray[i].split("_")[1] + 'Text" value="' + value
					+ '"/>');
		}else if(selectArray[i].split("_")[0] == "linkage"){
			var value1 = $("#" + selectArray[i].split("_")[1] + "_input2")
			.text();
			$('#saveSelect').append('<input type="hidden" id="'
					+ selectArray[i].split("_")[1] + 'Text"  name="'
					+ selectArray[i].split("_")[1] + 'Text" value="' + value1
					+ '"/>');
			
			var value = $("#" + selectArray[i].split("_")[1] + "_sel2_con2")
					.html();
			$('#saveSelect').append('<input type="hidden" id="'
					+ selectArray[i].split("_")[1] + 'TextDiv"  name="'
					+ selectArray[i].split("_")[1] + 'TextDiv" value=\'' + value
					+ '\'/>');
		}
	}
	$('#saveSelect')
			.append('<input type="hidden" id="totalNum"  name="totalNum" value="'
					+ totalNum + '"/>');
	var url = "/officeInstitution.do?action=saveSelect";
	$.post(url, $("#" + formName).serialize() + "&selectArray=" + selectArray,
			function(data) {
				nologinRedirect(data);
				data = $.parseJSON(data);;
			});
}

// 画分页
function drawPageAgain(totalNum, pageIndex, pageSize, index) {
	var saveSelectMap = $("#saveSelectData").val();
	if (saveSelectMap != null && saveSelectMap != "" && index == "index") {
		data = $.parseJSON(saveSelectMap);
		drawPage(data.totalNum, data.pageIndex, data.pageSize);
	} else {
		drawPage(totalNum, pageIndex, pageSize);
	}
}

/**
 * 要传递参数的控件需含 saveddata_data属性 
 * 值等于val input value取值的 
 * 值等于attr 属性取值的 
 * 值等于txt 文本取值的
 * @param {}url 要跳转到的URL
 * @param {}attrname 获取属性的值统一的属性名，默认idKey
 */
function pubSavedRedirect(url, attrname) {
	var $element = $("[saveddata_data]");
	if ($element) {
		if (!attrname) {
			attrname = "idKey";
		}
		var postParams = ["{"];
		$element.each(function() {
					var value = '';
					var name = $(this).attr("name");
					if ($(this).attr("saveddata_data")=="val") {
						value = $(this).val();
					} else if ($(this).attr("saveddata_data")=="attr") {
						value = $(this).attr(attrname);
					} else if ($(this).attr("saveddata_data")=="txt") {
						value = $(this).text();
					}

					if (name) {
						postParams.push('"');
						postParams.push(name);
						postParams.push('":"');
						postParams.push(value);
						postParams.push('"');
						postParams.push(',');
					}
				});
		if (postParams.slice(-1) == ",") {
			postParams.pop();
		}
		postParams.push("}");
		$.post(url,$.parseJSON(postParams.join("")));
	}
}
/**
 * 确认
 * @param	{String}	消息内容
 * @param	{Function}	确定按钮回调函数
 * @param	{Function}	取消按钮回调函数
 */
artDialog.confirm = function (content, yes, no) {
    return artDialog({
        id: 'Confirm',
        icon: 'question',
        fixed: true,
        lock: true,
        opacity: .1,
        content: content,
        ok: function (here) {
            return yes.call(this, here);
        },
        cancel: function (here) {
            return no && no.call(this, here);
        }
    });
};
/**
 * 短暂提示
 * @param	{String}	提示内容
 * @param	{Number}	显示时间 (默认1.5秒)
 */
artDialog.tips = function (content, time, closeFunc) {
    return artDialog({
        id: 'Tips',
        title: false,
        cancel: false,
        fixed: true,
        lock: false,
        close:function() {
        	closeFunc();
        }
    })
    .content('<div style="padding: 0 1em;">' + content + '</div>')
    .time(time || 1);
};
/***
 * 按钮被单击后, 显示'正在操作... ...'
 */
function buttLoadingFun(butt) {
	var htmlCode = butt.innerHTML;
	var width = butt.style.width;
	var fun = butt.onclick;
	var buttModel = {
			buttId: 	butt.id,
			htmlCode:	htmlCode,
			width:		width,
			fun:		fun
		};
	butt.onclick = function() {};
	butt.style.width = "100px";
	butt.innerHTML = "正在操作... ...";
	return buttModel;
}
/***
 * 按钮动作执行结束后还原
 * @param buttModel
 */
function buttLoadedFun(buttModel) {
	var butt = document.getElementById(buttModel.buttId);
	butt.onclick = buttModel.fun;
	butt.style.width = buttModel.width;
	butt.innerHTML = buttModel.htmlCode;
}
/***
 * 点击按钮执行功能之前,让用户确认要执行此操作
 * 类似js中的window.confirm();
 * return  boolean
 */
function confirmButtAct(butt) {
	var mess = $(butt).html() ? $(butt).html() : $(butt).val();
	return window.confirm("您确定要执行 ‘" + mess + "’ 操作吗?");
}

/**
 * 将表格标题行固定在浏览器顶部
 * 当界面向上滚动时，让表格的标题行固定在浏览器顶部
 * @param tableId 要固定的表格ID
 */
function fixedTableTitleForTop1(tableId, scrollT){
	console.log(scrollT);
	//数据表对象
	var tableObj = $("#"+tableId);
	//数据表父级div对象
	var tableParent = tableObj.parent();

	//检查页面是否包含class=tab-box-fixed 的对象
	if(tableParent.find(".tab-box-fixed").size() == 0){
		var divHtml='<div class="tab-box-fixed "><table class="chan_table tb_bord01 " ></table></div>';
		tableParent.append(divHtml);
	}
	var newTableParent = tableParent.find(".tab-box-fixed");
	var newTable = newTableParent.find("table");

	//设置浏览器滚动条事件，判断表格顶到浏览器头时显示class=tab-box-fixed 的对象
	var win = $(window);
	var sc = $(document);
		if (scrollT >= 350) {
			//获取标题内容
			var title = tableObj.find("tbody tr:eq(0)");
			//设置标题宽度
			var tdHead = title.find("th");
			//设置标题每一列的宽度
			$(tdHead).each(function () {
				var width = $(this).innerWidth()-1;
				$(this).css("width",width);
			});
			newTable.html("<tr>"+title.html()+"</tr>");
			//newTable.find("th").css("display","inline-block");
			newTableParent.show();
		} else {
			newTableParent.hide();
		}

	//表格滚动条移动事件
	tableParent.scroll(function(){
		//scrollSize 当前滚动条距离起始点的总距离
		var scrollSize = $(this)[0].scrollLeft;
		console.log(scrollSize);
		//上次滚动条距离起始点的总距离
		// scrollSize - oldScrollSize = 本次滚动条移动的距离
		var oldScrollSize = parseInt(newTable.attr("old-scroll-size"));

		var margLeft = parseInt(newTable.css("marginLeft"));
		newTable.css("marginLeft" ,margLeft - (scrollSize - oldScrollSize));
		newTable.attr("old-scroll-size",scrollSize);
	});
}

/**
 * 将表格标题行固定在浏览器顶部
 * 当界面向上滚动时，让表格的标题行固定在浏览器顶部
 * @param tableId 要固定的表格ID
 */
 /**
 * 将表格标题行固定在浏览器顶部
 * 当界面向上滚动时，让表格的标题行固定在浏览器顶部
 * @param tableId 要固定的表格ID
 */

function fixedTableTitleForTop(tableId, scrollT){
	
	//设置浏览器滚动条事件，判断表格顶到浏览器头时显示class=tab-box-fixed 的对象
	//数据表对象
	var tableObj = $("#"+tableId);
	//数据表父级div对象
	var tableParent = tableObj.parent();
	var top = tableParent.offset().top;

	if (scrollT >= (top+90)) {
   		//检查页面是否包含class=tab-box-fixed 的对象
		if(tableParent.find(".tab-box-fixed").size() == 0){
			var divHtml='<div class="tab-box-fixed"><table class="chan_table tb_bord01"></table></div>';
			tableParent.append(divHtml);
			divTableScroll(tableId);
			var newTable = tableParent.find(".tab-box-fixed").css("position","absolute").find("table");
			//获取标题内容
			var title = tableObj.find("tbody tr:eq(0)");
			//设置标题宽度
			var tdHead = title.find("th");
			//设置标题每一列的宽度
			$(tdHead).each(function () {
				var width = $(this).innerWidth()-1;
				$(this).css("width",width);
			});
			newTable.html("<tr>"+title.html()+"</tr>");
		}

		var newTableParent = tableParent.find(".tab-box-fixed");
		newTableParent.hide();	
		var scrollSize = parseInt(scrollT);
		newTableParent.css("top",(scrollSize-100)+"px");
		if(newTableParent.is(":hidden")){
			newTableParent.show(600);	
		}
		
	} else  {
		var newTableParent = tableParent.find(".tab-box-fixed")
		if(newTableParent.size() != 0){
			newTableParent.hide();
			newTableParent.remove();
		}
		
	}

	
}


function divTableScroll(tableId){
	var tableObj = $("#"+tableId);
		//数据表父级div对象
		var tableParent = tableObj.parent();
		var newTableParent = tableParent.find(".tab-box-fixed");
		var newTable = newTableParent.find("table");
//表格滚动条移动事件
	tableParent.scroll(function(){
		//scrollSize 当前滚动条距离起始点的总距离
		var scrollSize = $(this)[0].scrollLeft;
	
		//上次滚动条距离起始点的总距离
		// scrollSize - oldScrollSize = 本次滚动条移动的距离
		var oldScrollSize = parseInt(newTable.attr("old-scroll-size"));

		var margLeft = parseInt(newTable.css("marginLeft"));
		newTable.css("marginLeft" ,margLeft - (scrollSize - oldScrollSize));
		newTable.attr("old-scroll-size",scrollSize);
	});
}