<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>同步上游和下游的交易信息【手续费】和【协议,渠道】</title>

<jsp:include page="../../base/base_page.jsp"/>
<style type="text/css">
   .divhide {display:none}
</style>
</head>
<div class="area">
	<input type="hidden" value="${pageContext.request.contextPath}/update_acc_to_detail/do_update" name="do_update" id="do_update" />
	<input type="hidden" value="${pageContext.request.contextPath}/update_acc_to_detail/do_update_pos" name="do_update_pos" id="do_update_pos" />
	
	<form name="dataForm" id="dataForm" method="post" action="">
		<div class="main">
			<div class="blank15"></div>
			<div class="wrap">
				<div class="con">
					<div class="con_title">
						<h3 class="left fs14 marglr3020">同步上游和下游的交易信息【手续费】和【协议,渠道】</h3>
						<span class="grey"></span>
					</div>
					<div class="main_con" style="display: ">
						<div class="con_article_title tit_zindex3 linehei30">
							<b class="left paddl67 paddr10">内容筛选</b> 
							<a href="javascript:;" class="margt10 btn_arrow_up" id="reg2_arrow"></a>
						</div>
						
						<div class="con_article_title02 tit_zindex6" id="reg2_arrow_con">
							<div class="con_article_title02 margt10 tit_zindex5 paddl52" style="width:90%">
								<label class="label_txt wid135 txtright margr8 margt11 margl16">同步表： </label>
								<div class="label_txt txtright margr8 margt11 margl16" id="tldivU">
									<label>DETAIL</label><input type="radio" name="tableName" value="DETAIL" checked />&nbsp;&nbsp;
									<label>POS</label><input type="radio" name="tableName" value="POS" />&nbsp;&nbsp;
								</div>
							</div>
							<div class="con_article_title02 margt10 tit_zindex5 paddl52" style="width:90%">
								<label class="label_txt wid135 txtright margr8 margt11 margl16">同步时间： </label>
								<div class="label_txt txtright margr8 margt11 margl16" id="tldivU">
									<label>日期</label><input type="radio" name="updateType" value="day" checked />&nbsp;&nbsp;
									<label>月份</label><input type="radio" name="updateType" value="month" />&nbsp;&nbsp;
									<label>年份</label><input type="radio" name="updateType" value="year" />&nbsp;&nbsp;
									<label>日期区间</label><input type="radio" name="updateType" value="begin_end" />&nbsp;&nbsp;
								</div>
							</div>
							<!-- day -->
							<div id="day_div"  class="the_update_div con_article_title02 margt10 tit_zindex5 paddl52" style="width:90%">
								<label class="label_txt wid135 txtright margr8 margt11 margl16">
									同步日期： </label>
								<div class="input_time_type wid190">
									<input id="accountDate" type="text" readonly="readonly" name="accountDate" class="wid140 hasDatepicker" value="">
									<a href="javascript:;" onclick="WdatePicker({el:'accountDate',isShowClear:true,readOnly:true,dateFmt:'yyyy-MM-dd 00:00:00'})"
										class="right icon_timer timer_calender margtr36" title="请输入账期再进行读入操作"></a>
								</div>
								<div id="valiDiv" class="label_txt txtright margr8 margt11"></div>
								<div class="con_article_title02 margt10 tit_zindex5 paddl52" style="z-index:10;width:90%"></div>
							</div>
							<!-- // day -->
							<!-- month -->
							<div id="month_div" class="the_update_div con_article_title02 margt10 tit_zindex5 paddl52" style="width:90%">
								<label class="label_txt wid135 txtright margr8 margt11 margl16">
									同步月份： </label>
								<div class="input_con_type_top wid90">
									<select style="width:60px;height:28px;" name="month" id="month_select">
										<option value="1">一月</option>
										<option value="2">二月</option>
										<option value="3">三月</option>
										<option value="4">四月</option>
										<option value="5">五月</option>
										<option value="6">六月</option>
										<option value="7">七月</option>
										<option value="8">八月</option>
										<option value="9">九月</option>
										<option value="10">十月</option>
										<option value="11">十一月</option>
										<option value="12">十二月</option>
									</select>
								</div>
								<div id="valiDiv" class="label_txt txtright margr8 margt11"></div>
								<div class="con_article_title02 margt10 tit_zindex5 paddl52" style="z-index:10;width:90%"></div>
							</div>
							<!-- // month -->
							<!-- year -->
							<div id="year_div" class="the_update_div con_article_title02 margt10 tit_zindex5 paddl52" style="width:90%">
								<label class="label_txt wid135 txtright margr8 margt11 margl16">
									同步年份： </label>
								<div class="input_con_type_top wid90">
									<select style="width:60px;height:28px;" name="year" id="year_select"></select>
								</div>
								<div id="valiDiv" class="label_txt txtright margr8 margt11"></div>
								<div class="con_article_title02 margt10 tit_zindex5 paddl52" style="z-index:10;width:90%"></div>
							</div>
							<!-- // year -->
							<!-- begin_end -->
							<div id="begin_end_div" class="the_update_div con_article_title02 margt10 tit_zindex5 paddl52" style="width:90%">
								<label class="label_txt wid135 txtright margr8 margt11 margl16">
									同步时间段： </label>
								<div class="input_time_type wid190">
									<input id="beginDate" type="text" readonly="readonly" name="beginDate" class="wid140 hasDatepicker" value="">
									<a href="javascript:;" onclick="WdatePicker({el:'beginDate',isShowClear:true,readOnly:true,dateFmt:'yyyy-MM-dd 00:00:00'})"
										class="right icon_timer timer_calender margtr36" title="请输入账期再进行读入操作"></a>
								</div>
								<div class="left wid10 paddtrl184">
									<span class="left wid10 bord19"></span>
								</div>
								<div class="input_time_type wid190">
									<input id="endDate" type="text" readonly="readonly" name="endDate" class="wid140 hasDatepicker" value="">
									<a href="javascript:;" onclick="WdatePicker({el:'endDate',isShowClear:true,readOnly:true,dateFmt:'yyyy-MM-dd 00:00:00'})"
										class="right icon_timer timer_calender margtr36" title="请输入账期再进行读入操作"></a>
								</div>
								<div id="valiDiv" class="label_txt txtright margr8 margt11"></div>
								<div class="con_article_title02 margt10 tit_zindex5 paddl52" style="z-index:10;width:90%"></div>
							</div>
							<!-- // begin_end -->
							<div class="con_article_title02 margt10 tit_zindex3">
								<a href="javascript:void(0);" id="btnSubmit" class="right chan_btn wid100 margr30 margt5">开始同步</a>
								<div class="con_article_title02 margt10 tit_zindex5 paddl52" style="z-index:10;width:90%"></div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</form>
</div>
</body>
<script>
	$(function() {
		var date = new Date();
		var htmlCode = "";
		for(var i = date.getFullYear(); i >= 2014; i --) {
			htmlCode += "<option value='"+ i +"'>"+ i +"</option>";
		}
		$('#year_select').html(htmlCode);
		showTheDiv();
		$("input[name='updateType']").click(function() {
			showTheDiv();
		});
		$("#btnSubmit").click(function() {
			doUpdateAccToDetail();
		});
	});
	function showTheDiv() {
		var id = $("input[name='updateType']:checked").val();
		$(".the_update_div").hide();
		$("#" + id + "_div").show();
	}
	function doUpdateAccToDetail() {
		if(!window.confirm("您确定要  同步上游和下游的交易信息【手续费】和【协议,渠道】?")) return;
		
		if($('input[name=updateType]:checked').val()=="day" && !$('#accountDate').val()) {
			alert("请输入账期!");
			return;
		}
		if($('input[name=updateType]:checked').val()=="begin_end" && !$('#beginDate').val() && !$('#endDate').val()) {
			alert("请输入开始日期和结束日期!");
			return;
		}
		
		var url = "";
		var tableName = $("input[name=tableName]:checked").val();
		if(tableName == "DETAIL") {
			url = $('#do_update').val();	
		} else if(tableName == "POS") {
			url = $('#do_update_pos').val();
		}
		$.post(
			url,					//发送请求 url
			$('#dataForm').serialize(),									//表单数据
			function(data) {
				alert(data);
			},
			'text'
		);
	}
</script>
</html>