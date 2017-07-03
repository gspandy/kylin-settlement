<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>读取‘账户系统’交易信息</title>

<jsp:include page="../../../base/base_page.jsp"/>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/settlement/trans/getAccTraOrdInfo.js"></script>
</head>
<div class="area">
	<form name="dataForm" id="dataForm" method="post" action="${pageContext.request.contextPath}/transdetail/getAccTraOrdInfo">
		<input type="hidden" value="${pageContext.request.contextPath}/transdetail/query_ajax" id="query_ajax_url" />
		<input type="hidden" value="${pageContext.request.contextPath}/transdetail/getAccTraOrdInfoBySP" id="getAccTraOrdInfoBySP" />
		<input type="hidden" value="${pageContext.request.contextPath}/transdetail/getAccOldTraOrdInfoBySP" id="getAccOldTraOrdInfoBySP" />
		<input type="hidden" value="${pageContext.request.contextPath}/transdetail/getAccOldTraOrdInfo" id="getAccOldTraOrdInfo" />
		<div class="main">
			<div class="blank15"></div>
			<div class="wrap">
				<div class="con">
					<div class="con_title">
						<h3 class="left fs14 marglr3020">读取‘账户系统’交易信息</h3>
						<span class="grey"></span>
					</div>
					<div class="main_con" style="display: ">
						<div class="con_article_title tit_zindex3 linehei30">
							<b class="left paddl67 paddr10">获取T-1日所有账户系统交易信息</b> 
						</div>
						<div class="con_article_title02 tit_zindex6" id="reg2_arrow_con">
							<div class="con_article_title02 margt10 tit_zindex3">
								<a title="使用存储过程" href="javascript:void(0);" onclick="getAccOldTraOrdInfoBySP()" class="right chan_btn wid300 margr30 margt5">获取T-1日所有账户系统交易信息【一期】</a>
							</div>
							<div class="con_article_title02 margt10 tit_zindex3">
								<a title="使用存储过程" href="javascript:void(0);" onclick="getAccTraOrdInfoBySP()" class="right chan_btn wid300 margr30 margt5">获取T-1日所有账户系统交易信息 【二期】</a>
							</div>
						</div>
						
						<div class="con_article_title tit_zindex3 linehei30">
							<b class="left paddl67 paddr10">请输入账期</b> <a href="javascript:;"
								class="margt10 btn_arrow_up" id="reg2_arrow"></a>
						</div>
						<div class="con_article_title02 tit_zindex6" id="reg2_arrow_con">
							<div class="con_article_title02 tit_zindex6" id="reg2_arrow_con">
								<div class="con_article_title02 margt10 tit_zindex5 paddl52" style="z-index:10;width:90%">
									<label class="label_txt wid135 txtright margr8 margt11">
										交易请求时间： </label>
									<div class="input_time_type wid190 margt4">
										<input id="checkStartTime" type="text" readonly="readonly"
											name="requestDateStrBegin"
											class="wid140 hasDatepicker" value=""> <a
											href="javascript:;"
											onclick="WdatePicker({el:'checkStartTime',isShowClear:true,readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'#F{$dp.$D(\'checkEndTime\')}'})"
											class="right icon_timer timer_calender margtr36"
											title="请输入交易时间段再进行读入操作"
											></a>
									</div>
									<div class="left wid10 paddtrl184">
										<span class="left wid10 bord19"></span>
									</div>
									<div class="input_time_type wid190 margt4">
										<input id="checkEndTime" type="text" readonly="readonly"
											name="requestDateStrEnd"
											class="wid140 hasDatepicker" value=""> <a
											href="javascript:;"
											onclick="WdatePicker({el:'checkEndTime',isShowClear:true,readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'checkStartTime\')}'})"
											class="right icon_timer timer_calender margtr36"
											title=" "
											></a>
									</div>
									<div id="valiDiv" class="label_txt txtright margr8 margt11"></div>
								</div>
							</div>
							<div class="con_article_title02 margt10 tit_zindex3">
								<a href="javascript:void(0);" id="btnSubmit2" class="right chan_btn wid200 margr30 margt5">读取【账户系统一期】交易</a>
							</div>
							<div class="con_article_title02 margt10 tit_zindex3">
								<a href="javascript:void(0);" id="btnSubmit" class="right chan_btn wid200 margr30 margt5">读取【账户系统二期】交易</a>
							</div>
						</div>

						<div class="bord11 margt10"></div>
						<div class="con_core_info_main wid1125 paddl10 margt10" style="width:1225px;">
							<table class="chan_table tb_bord01" id="user_table1">
							</table>
						</div>
						
						<div class="left wid500 paddl60 margt10 rel_pos"
							style="height:42px;">
							<!-- -->
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
							<!--  -->
							<div class="chan_table_page"></div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!-- 分页隐藏域  -->
		<input type="hidden" id="pageIndex" name="pageIndex" value="1" /> <input
			type="hidden" id="pageSize" name="pageSize" value="10" />
		<!-- // 分页隐藏域 -->
	</form>
</div>
</body>
</html>