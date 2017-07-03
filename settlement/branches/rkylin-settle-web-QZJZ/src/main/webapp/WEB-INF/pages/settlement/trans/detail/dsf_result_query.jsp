<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>从代收付系统读取结果并入清结算库</title>

<jsp:include page="../../../base/base_page.jsp"/>
<jsp:include page="../../../base/createIF.jsp"/>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/settlement/trans/dsfResultQuery.js"></script>
</head>
<div class="area">
	<form name="dataForm" id="dataForm" method="post" action="">
		
		<!-- 代收付结果推送给账户系统 -->
		<input type="hidden" id="getResultFromDSF" value="${pageContext.request.contextPath}/invoice/getResultFromDSF" />
		
		<div class="main">
			<div class="blank15"></div>
			<div class="wrap">
				<div class="con">
					<div class="con_title">
						<h3 class="left fs14 marglr3020">从代收付系统读结果并入清结算库</h3>
						<span class="grey"></span>
					</div>
					<div class="main_con" style="display: ">
						<div class="con_article_title tit_zindex3 linehei30">
							<b class="left paddl67 paddr10">条件</b> <a href="javascript:;"
								class="margt10 btn_arrow_up" id="reg2_arrow"></a>
						</div>
						<div class="con_article_title02 tit_zindex6" id="reg2_arrow_con">
							<div class="con_article_title02 margt10 tit_zindex5 paddl52" style="width:90%">
							
								<label class="label_txt wid135 txtright margr8 margt11">转化后的机构号： </label>
								<div class="input_con_type_top wid190 margt4">
									<input type="text" class="wid190" name="rootInstCd" id="rootInstCd" />
								</div>
							
								<label class="label_txt wid135 txtright margr8 margt11">
									账期： </label>
								<div class="input_time_type wid190 margt4">
									<input id="accountDate" type="text" readonly="readonly"
										name="accountDate"
										class="wid140 hasDatepicker" value=""> <a
										href="javascript:;"
										onclick="WdatePicker({el:'accountDate',isShowClear:true,readOnly:true,dateFmt:'yyyy-MM-dd'})"
										class="right icon_timer timer_calender margtr36"></a>
								</div>
								
							</div>
							<div class="con_article_title02 margt10 tit_zindex5 paddl52" style="width:90%">
								<label class="label_txt wid135 txtright margr8 margt11">交易批次号： </label>
								<div class="input_con_type_top wid190 margt4">
									<input type="text" class="wid190" name="requestNo" id="requestNo" />
								</div>
								
							    <label class="label_txt wid135 txtright margr8 margt11">订单号： </label>
								<div class="input_con_type_top wid190 margt4">
									<input type="text" class="wid190" name="orderNos" id="orderNos" />
								</div>
							</div>
							
							<div class="con_article_title02 margt10 tit_zindex3">
								<a id="sendId"  href="javascript:void(0);" class="right chan_btn wid70 margr30 margt5" onclick="getDsfResult();">读取代收付结果</a>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</form>
</div>
</body>
</html>