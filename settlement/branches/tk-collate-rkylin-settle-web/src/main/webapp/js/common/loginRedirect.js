// ajax异步请求过程中session过期后返回跳转
function nologinRedirect(param) {
	try {
		if (!param.message) {
			if(typeof param=="string")
			{
				param = $.parseJSON(param);
			}
//			param = JSON.stringify(param);
			// alert(param.location);
			if (param == null) {
				showAlert("用户已过期，请重新登录");
				autoCloseWindow(4000,"/index.jsp");
				return false;
			}
//			 else if ("location" in param) {
//				if (param.location == null) {
//					showAlert("用户已过期，请重新登录");
//					autoCloseWindow(2000);
//					gotoUrl("/index.jsp");
//					return false;
//				}
//			}
		}
		if (param.message == "no_login") {
			showAlert("用户已过期，请重新登录");
			autoCloseWindow(4000,param.target_url);
			return false;
		}
	} catch (err) {
	}
	return true;
}

// 关闭页面
function windowclose() {
	var browserName = navigator.appName;
	if (browserName == "Netscape") {
		window.open('', '_self', '');
		window.close();
	} else {
		if (browserName == "Microsoft Internet Explorer") {
			window.opener = "whocares";
			window.opener = null;
			window.open('', '_top');
			window.close();
		}
	}
}
// 使用<a>跳转页面
function gotoUrl(url, target) {
	if (document.all) {
		var gotoLink = document.createElement('a');

		gotoLink.href = url;

		if (target != undefined && target != "") {
			gotoLink.target = target;
		} else {
			// stopDefault(target);
			gotoLink.target = "_self";
		}

		document.body.appendChild(gotoLink);

		gotoLink.click();

	} else
		window.location.href = url;
}
// 清除浏览器默认行为
function stopDefault(e) {
	// 如果提供了事件对象，则这是一个非IE浏览器
	if (e && e.preventDefault) {
		e.preventDefault();
	} else {
		window.event.returnValue = false;
	}
	return false;
}
// 阻止事件冒泡
function stopBubble(e) {
	// 如果提供了事件对象，则这是一个非IE浏览器
	if (e && e.stopPropagation) {
		e.stopPropagation();
	} else {
		window.event.cancelBubble = true;
	}
	return false;
}
