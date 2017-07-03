<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>充值结算对账功能</title>

<jsp:include page="../../base/base_page.jsp"/>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/account/compare/query.js"></script>
<style type="text/css">
   span.tip {
	font-weight:bold;
	color:#FF0000;
	}
</style>
</head>
<body>
<div class="area">
	<form name="dataForm" id="dataForm">
		<input type="hidden" id="getPayChannelIdAndName" value="${pageContext.request.contextPath}/compare/getPayChannelIdAndName" />
		<input type="hidden" id="getProtocol" value="${pageContext.request.contextPath}/compare/getProtocol" />
		<input type="hidden" id="query_ajax" value="${pageContext.request.contextPath}/compare/query_ajax" />
		<input type="hidden" id="save_ajax" value="${pageContext.request.contextPath}/compare/save_ajax" />
		<div class="main">
			<div class="blank15"></div>
			<div class="wrap">
					<div class="main_con" style="display: ">
						<div class="con_article_title tit_zindex3 linehei30">
							<b class="left paddl67 paddr10">内容筛选</b> <a href="javascript:;"
								class="margt10 btn_arrow_up" id="reg2_arrow"></a>
						</div>
						<div class="con_article_title02 tit_zindex6" id="reg2_arrow_con">				
							<div class="con_article_title02 tit_zindex6" id="reg2_arrow_con">
								<div class="con_article_title02 margt10 tit_zindex5 paddl52" style="z-index:10;width:90%">
									<label class="label_txt wid135 txtright margr8 margt11">*账期： </label>
									<div class="input_con_type_top wid190 margt4">
										<input id="accountDate" type="text" readonly="readonly"
											name="accountDate"
											class="wid140 hasDatepicker" value=""> <a
											href="javascript:;"
											onclick="WdatePicker({el:'accountDate',isShowClear:true,readOnly:true,dateFmt:'yyyy-MM-dd'})"
											class="right icon_timer timer_calender margtr36"
											title="请输入账期再进行读入操作"
											></a>
									</div>
									
									<label class="label_txt wid135 txtright margr8 margt11">协议： </label>
									<div class="input_con_type_top wid190 margt4">
										<select style="width:189px;height:28px;" name="merchantCode" id="merchantCode">
										</select>
									</div>
									
									<label class="label_txt wid135 txtright margr8 margt11">渠道： </label>
									<div class="input_con_type_top wid190 margt4">
										<select style="width:189px;height:28px;" name="payChannelId" id="payChannelId">
										</select>
									</div>
								</div>
							</div>

							<div class="con_article_title02 margt10 tit_zindex3">
								<a id="resetFormButt" href="javascript:void(0);" class="right chan_btn wid60 margr30 margt5" onclick="save();">保存</a>
								<a id="searchButt"  href="javascript:void(0);" class="right chan_btn wid60 margr30 margt5" onclick="duizhang();">对账</a>
								<a id="searchButt"  href="javascript:void(0);" class="right chan_btn wid60 margr30 margt5" onclick="query();">查询</a> 
							</div>
						</div>
						
						<div class="bord11 margt10"></div>
						<div class="con_core_info_main wid1125 paddl10 margt10 overflow-ui">
							<table class="chan_table tb_bord01 nohover" id="user_table1" >
							</table>
						</div>
						
					</div>
				</div>
			</div>
	</form>
</div>
</body>
</html>