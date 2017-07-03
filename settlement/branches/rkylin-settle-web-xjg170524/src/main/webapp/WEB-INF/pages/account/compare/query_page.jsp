<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>充值结算对账查询</title>
<script src="http://cdn.static.runoob.com/libs/angular.js/1.4.6/angular.min.js"></script>
<jsp:include page="../../base/base_page.jsp"/>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/account/compare/query_page.js"></script>
<style type="text/css">
   span.tip {
	font-weight:bold;
	color:#FF0000;
	}
</style>
</head>
<body>
<div class="area">
	<input type="hidden" id="getPayChannelIdAndName" value="${pageContext.request.contextPath}/compare/getPayChannelIdAndName" />
	<input type="hidden" id="getProtocol" value="${pageContext.request.contextPath}/compare/getProtocol" />
	<input type="hidden" name="doAllExport" id="doAllExport" value="${pageContext.request.contextPath}/compare/query_and_output_excl" />
	<form action="${pageContext.request.contextPath}/compare/query_page_ajax" name="dataForm" id="dataForm">
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
									<!-- <label class="label_txt wid135 txtright margr8 margt11">账期： </label>
									<div class="input_con_type_top wid190 margt4">
										<input id="accountDate" type="text" readonly="readonly"
											name="accountDateStr"
											class="wid140 hasDatepicker" value=""> <a
											href="javascript:;"
											onclick="WdatePicker({el:'accountDate',isShowClear:true,readOnly:true,dateFmt:'yyyy-MM-dd'})"
											class="right icon_timer timer_calender margtr36"
											title="请输入账期再进行读入操作"
											></a>
									</div> 
									-->
									<label class="label_txt wid135 txtright margr8 margt11">账期：</label>
									<div class="input_time_type wid190 margt4">
										<input id="checkStartTime" type="text" readonly="readonly"
											name="accountDateStrBegin"
											class="wid140 hasDatepicker" value=""> <a
											href="javascript:;"
											onclick="WdatePicker({el:'checkStartTime',isShowClear:true,readOnly:true,dateFmt:'yyyy-MM-dd 00:00:00',maxDate:'#F{$dp.$D(\'checkEndTime\')}'})"
											class="right icon_timer timer_calender margtr36"></a>
									</div>
									<div class="left wid10 paddtrl184">
										<span class="left wid10 bord19"></span>
									</div>
									<div class="input_time_type wid190 margt4">
										<input id="checkEndTime" type="text" readonly="readonly"
											name="accountDateStrEnd"
											class="wid140 hasDatepicker" value=""> <a
											href="javascript:;"
											onclick="WdatePicker({el:'checkEndTime',isShowClear:true,readOnly:true,dateFmt:'yyyy-MM-dd 00:00:00',minDate:'#F{$dp.$D(\'checkStartTime\')}'})"
											class="right icon_timer timer_calender margtr36"></a>
									</div>							
								</div>
								<div class="con_article_title02 margt10 tit_zindex5 paddl52" style="z-index:10;width:90%">
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
									<label class="label_txt wid135 txtright margr8 margt11">类型： </label>
									<div class="input_con_type_top wid190 margt4">
										<select style="width:189px;height:28px;" name="accountType" id="accountType">
											<option value="0">充值/退款交易</option>
										</select>
									</div>
								</div>
							</div>
							<div class="con_article_title02 margt10 tit_zindex3">
								<a id="searchButt"  href="javascript:void(0);" class="right chan_btn wid60 margr30 margt5">查询</a> 
							</div>
						</div>
						<div class="bord11 margt10"></div>
						<div class="con_core_info_main wid1125 paddl10 margt10 overflow-ui">
							<table class="chan_table tb_bord01 nohover" id="user_table1" >
							</table>
						</div>
						<div class="left wid500 paddl20 margt10 rel_pos"
							style="height:42px;">
							<a href="javascript:void(0);" class="left wid60 margr5 margt5" onclick="$('#user_table1').tableExport({type: 'excel', separator: ';', escape: 'false'});">导出本页</a>
							<a href="javascript:void(0);" class="left wid60 margr5 margt5" onclick="doAllExport();">导出全部</a>
							<a href="${pageContext.request.contextPath}/my_download/download" target="_blank" class="left wid100 margr5 margt5" id="my_download_a">【我的下载】</a>
							<label class="label_txt wid60 txtright margr8 margt11">
								每页显示 </label>
							<div class="input_con_type_top wid95 margt4">
								<select style="width:94px;height:27px" name="pageSize" id="pageSize">
									<option style="height:25px" value="10">10</option>
									<option style="height:25px" value="50">50</option>
									<option style="height:25px" value="100">100</option>
								</select>
							</div>
							<label class="label_txt wid60 txtleft margl6 margt11">
								条记录 </label> 
								<input id="saveSelectData" type="hidden"
								value='${sessionDetail }' name="saveSelectData"/>
						</div>
						<div id="div_table_page" class="chan_table_page_wrap wid620 margt10 paddr36">
							<div class="chan_table_page"></div>
						</div>
						<!-- 分页隐藏域 -->
						<input type="hidden" id="pageIndex" name="pageIndex" value="1" /> 
						<input type="hidden" id="pageSize" name="pageSize" value="10" />
						<!-- // 分页隐藏域 -->
					</div>
				</div>
			</div>
		</div>
	</form>
</div>
</body>
</html>