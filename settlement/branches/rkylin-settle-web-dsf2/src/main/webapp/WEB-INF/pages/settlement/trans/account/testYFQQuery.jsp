<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>查询银企直联测试交易并汇总金额</title>

<jsp:include page="../../../base/base_page.jsp"/>
<style type="text/css">
	.divhide {display:none}
</style>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/settlement/trans/testYFQQuery.js"></script>
</head>
<div class="area">
	<form name="dataForm" id="dataForm" action="${pageContext.request.contextPath}/transaccount/testYFQSummary" >
		<div class="main">
			<div class="blank15"></div>
			<div class="wrap">
				<div class="con">
					<div class="con_title">
						<h3 class="left fs14 marglr3020">查询银企直联测试交易并汇总金额</h3>
						<span class="grey"></span>
					</div>
					<div class="main_con" style="display: ">
							<div class="con_article_title02 margt10 tit_zindex5 paddl52" style="z-index:10;width:90%">
								<label class="label_txt wid135 txtright margr8 margt11">账期： </label>
								<div class="input_time_type wid190 margt4">
									<input id="accountDate" type="text" readonly="readonly"
										name="accountDate"
										class="wid140 hasDatepicker" value=""> <a
										href="javascript:;"
										onclick="WdatePicker({el:'accountDate',isShowClear:true,readOnly:true,dateFmt:'yyyy-MM-dd 00:00:00'})"
										class="right icon_timer timer_calender margtr36"></a>
								</div>
							</div>
							<div class="con_article_title02 margt10 tit_zindex3">
								<a href="javascript:void(0);" class="right chan_btn wid60 margr30 margt5" onclick="summary();">汇总</a>
							</div>
					</div>
				</div>
			</div>
		</div>
	</form>
</div>
</body>
<jsp:include page="../../../base/createIF.jsp" />
<script type="text/javascript">
$(function() {
	getheight();
});
</script>
</html>