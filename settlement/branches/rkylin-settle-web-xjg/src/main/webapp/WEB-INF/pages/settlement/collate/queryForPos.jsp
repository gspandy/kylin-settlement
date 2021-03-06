<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>对账操作</title>

<jsp:include page="../../base/base_page.jsp"/>
<style type="text/css">
   .divhide {display:none}
</style>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/settlement/collate/queryForPos.js"></script>
</head>
<div class="area">
	<form name="dataForm" id="dataForm" method="post" action="${pageContext.request.contextPath}/collate/query_ajax">
		<input type="hidden" value="${pageContext.request.contextPath}/collate/collateForPos_ajax" id="collate_ajax" />
		<input type="hidden" value="${pageContext.request.contextPath}/collate/settleForPos_ajax" id="settle_ajax" />
		<!-- <input type="hidden" id="accountType" name="accountType" />  -->
		<div class="main">
			<div class="blank15"></div>
			<div class="wrap">
				<div class="con">
					<div class="con_title">
						<h3 class="left fs14 marglr3020">对账操作</h3>
						<span class="grey"></span>
					</div>
					<div class="main_con" style="display: ">
						<div class="con_article_title tit_zindex3 linehei30">
							<b class="left paddl67 paddr10">内容筛选</b> 
							<a href="javascript:;" class="margt10 btn_arrow_up" id="reg2_arrow"></a>
						</div>
						<div class="con_article_title02 tit_zindex6" id="reg2_arrow_con">
							<div class="con_article_title02 margt10 tit_zindex5 paddl52" style="width:90%">
								<label class="label_txt wid135 txtright margr8 margt11 margl16">机构号： </label>
								<div class="input_con_type_top wid190 margt4">
									<input type="text" class="wid190" name="merchantCode" id="merchantCode"/>
								</div>
								<label class="label_txt wid135 txtright margr8 margt11">支付渠道： </label>
								<div class="input_con_type_top wid190 margt4">
									<select style="width:189px;height:28px;" name="payChannelId" id="payChannelId">
										<option style="height:25px" value="P01">银联商务</option>
									</select>
								</div>
								<label class="label_txt wid135 txtright margr8 margt11 margl16">对账类型： </label>
								<div class="input_con_type_top wid190 margt4" id="tldiv">
									<select style="width:189px;height:28px;" name="accountType" id="accountType">
										<option style="height:25px" class="o_P01" value="10">Pos收单</option>
									</select>
								</div>
							</div>
							<div class="con_article_title02 tit_zindex6" id="reg2_arrow_con">
								<div class="con_article_title02 margt10 tit_zindex5 paddl52" style="z-index:10;width:90%">
									<label class="label_txt wid135 txtright margr8 margt11 margl16">
										账期： </label>
									<div class="input_time_type wid190 margt4">
										<input id="accountDate" type="text" readonly="readonly"
											name="accountDateStr"
											class="wid140 hasDatepicker" value=""> <a
											href="javascript:;"
											onclick="WdatePicker({el:'accountDate',isShowClear:true,readOnly:true,dateFmt:'yyyy-MM-dd 00:00:00'})"
											class="right icon_timer timer_calender margtr36"
											title="请输入账期再进行读入操作"
											></a>
									</div>
									<div id="valiDiv" class="label_txt txtright margr8 margt11"></div>
								</div>
							</div>
							<div class="con_article_title02 margt10 tit_zindex3">
								<a href="javascript:void(0);" id="settleSubmit" class="right chan_btn wid100 margr30 margt5">结算</a>
								<a href="javascript:void(0);" id="btnSubmit" class="right chan_btn wid100 margr30 margt5">开始对账</a>
							</div>
						</div>

						<div class="bord11 margt10"></div>
						<div class="con_core_info_main wid1125 paddl10 margt10" style="width:1225px;">
							<table class="chan_table tb_bord01" id="user_table1">
								<tr>
									<th width="5%">ID</th>
									<th width="15%">订单号</th>
									<th width="10%">对账类型</th>
									<th width="15%">规则</th>
									<th width="5%">状态</th>
									<th width="10%">账期</th>
									<th width="45%">备注</th>
								</tr>
								<tr><td colspan="7">没有符合条件的数据</td></tr>
							</table>
						</div>
						
						<div class="left wid500 paddl60 margt10 rel_pos"
							style="height:42px;">
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