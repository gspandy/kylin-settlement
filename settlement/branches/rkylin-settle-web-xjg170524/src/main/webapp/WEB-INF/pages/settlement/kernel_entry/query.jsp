<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>汇总信息</title>
 <jsp:include page="../../base/base_page.jsp"/>
<style type="text/css">
	.divhide {display:none}
</style>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/settlement/kernel_entry/query.js"></script>
</head>
<div class="area">
	<form name="dataForm" id="dataForm" method="post"
		action="${pageContext.request.contextPath}/settle_kernel_entry/query_ajax">
		<input type="hidden" id="readType" name="readType" />
		<input type="hidden" id="doSettleKernelEntry" 
			name="doSettleKernelEntry" 
			value="${pageContext.request.contextPath}/settle_kernel_entry/do_settle_kernel_entry" />
		<input type="hidden" id="doCollectByRule" 
			name="doCollectByRule" 
			value="${pageContext.request.contextPath}/settle_kernel_entry/do_collect_by_rule" />
		<div class="main">
			<div class="blank15"></div>
			<div class="wrap">
				<div class="con">
					<div class="con_title">
						<h3 class="left fs14 marglr3020">汇总信息</h3>
						<span class="grey"></span>
					</div>
					<div class="main_con" style="display: ">
						<div class="con_article_title tit_zindex3 linehei30">
							<b class="left paddl67 paddr10">内容筛选</b> <a href="javascript:;"
								class="margt10 btn_arrow_up" id="reg2_arrow"></a>
						</div>
						<div class="con_article_title02 tit_zindex6" id="reg2_arrow_con">
							<div class="con_article_title02 margt10 tit_zindex5 paddl52" style="width:90%">
								<label class="label_txt wid135 txtright margr8 margt11">交易类型： </label>
								<div class="input_con_type_top wid190 margt4">
									<input type="text" class="wid190" name="funcCode" id="funcCode" />
								</div>
								<label class="label_txt wid135 txtright margr8 margt11">用户ID_1： </label>
								<div class="input_con_type_top wid190 margt4">
									<input type="text" class="wid190" name="finAccountId1" id="finAccountId1" />
								</div>
								<label class="label_txt wid135 txtright margr8 margt11">用户ID_2： </label>
								<div class="input_con_type_top wid190 margt4">
									<input type="text" class="wid190" name="finAccountId2" id="finAccountId2" />
								</div>
							</div>
							<div class="con_article_title02 margt10 tit_zindex5 paddl52" style="width:90%">
								<label class="label_txt wid135 txtright margr8 margt11">状态：</label>
								<div class="input_con_type_top wid190 margt4">
									<select style="width:189px;height:28px;" name="statusId">
										<option style="height:25px" value="-99">全部</option>
										<option style="height:25px" value="1">成功</option>
										<option style="height:25px" value="0">失败</option>
										<option style="height:25px" value="-1">不发送</option>
									</select>
								</div>
								
								<label class="label_txt wid135 txtright margr8 margt11">账期： </label>
									<div class="input_time_type wid190 margt4">
										<input id="accountDate" type="text" readonly="readonly"
											name="accountDateStr"
											class="wid140 hasDatepicker" value=""> <a
											href="javascript:;"
											onclick="WdatePicker({el:'accountDate',isShowClear:true,readOnly:true,dateFmt:'yyyy-MM-dd 00:00:00'})"
											class="right icon_timer timer_calender margtr36" ></a>
									</div>
							</div>
							<div class="con_article_title02 margt10 tit_zindex3">
								<a href="javascript:void(0);" class="right chan_btn wid60 margr66 margt5" onclick="resetForm();">清除</a> 
								<a href="javascript:void(0);" class="right chan_btn wid60 margr30 margt5" onclick="funSearch(1);">查询</a>
								<a href="javascript:void(0);" class="right chan_btn wid120 margr30 margt5" onclick="doKernelEntry(this);" disable>记会计账</a>
							</div>
						</div>
						
						<div class="con_article_title tit_zindex3 linehei30">
							<b class="left paddl67 paddr10">交易汇总</b> <a href="javascript:;"
								class="up_or_down_button margt10 btn_arrow_up" id="reg2_arrow"></a>
						</div>
						<div class="up_or_down_con con_article_title02 tit_zindex6" id="reg2_arrow_con">
							<div class="con_article_title02 margt10 tit_zindex5 paddl52" style="width:90%">
								<label class="label_txt wid135 txtright margr8 margt11">汇总类型： </label>
								<div class="input_con_type_top wid190 margt4">
									<select style="width:189px;height:28px;" name="collectType" id="collectType">
										<option style="height:25px" value="1">多渠道数据</option>
										<option style="height:25px" value="2">账务数据</option>
									</select>
								</div>
								<label class="label_txt wid135 txtright margr8 margt11">机构号： </label>
								<div class="input_con_type_top wid190 margt4">
									<input type="text" class="wid190" name="merchantCode" id="merchantCode" />
								</div>
								<label class="label_txt wid135 txtright margr8 margt11">渠道号： </label>
								<div class="input_con_type_top wid190 margt4">
									<input type="text" class="wid190" name="payChannelId" id="payChannelId" />
								</div>
							</div>
							<div class="con_article_title02 margt10 tit_zindex5 paddl52" style="width:90%">
								<label class="label_txt wid135 txtright margr8 margt11">交易类型： </label>
								<div class="input_con_type_top wid190 margt4">
									<input type="text" class="wid190" name="collectFuncCode" id="collectFuncCode" />
								</div>
								<label class="label_txt wid135 txtright margr8 margt11">账期： </label>
									<div class="input_time_type wid190 margt4">
										<input id="collectAccountDate" type="text" readonly="readonly"
											name="collectAccountDate"
											class="wid140 hasDatepicker" value=""> <a
											href="javascript:;"
											onclick="WdatePicker({el:'collectAccountDate',isShowClear:true,readOnly:true,dateFmt:'yyyy-MM-dd 00:00:00'})"
											class="right icon_timer timer_calender margtr36" ></a>
									</div>
							</div>
							<div class="con_article_title02 margt10 tit_zindex3">
								<a href="javascript:void(0);" class="right chan_btn wid60 margr66 margt5" onclick="doCollectByRule(this)">开始汇总</a>
							</div>
						</div>	

						<div class="bord11 margt10"></div>
						<div class="con_core_info_main wid1125 paddl10 margt10 overflow-ui">
							<table class="chan_table tb_bord01 nohover" id="user_table1">
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
							<div class="chan_table_page"></div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!-- 分页隐藏域 -->
		<input type="hidden" id="pageIndex" name="pageIndex" value="1" /> <input
			type="hidden" id="pageSize" name="pageSize" value="10" />
		<!-- // 分页隐藏域 -->
	</form>
	<div id="test"></div>
</div>
</body>
<jsp:include page="../../base/createIF.jsp" />
<script type="text/javascript">
$(function() {
	getheight();
});
</script>
</html>