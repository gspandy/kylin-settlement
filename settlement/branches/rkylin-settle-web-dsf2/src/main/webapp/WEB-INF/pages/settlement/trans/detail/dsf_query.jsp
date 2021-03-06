<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>交易表代收付信息</title>

<jsp:include page="../../../base/base_page.jsp"/>
<jsp:include page="../../../base/createIF.jsp"/>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/settlement/trans/dsfQuery.js"></script>
</head>
<div class="area">
	<form name="dataForm" id="dataForm" method="post" action="${pageContext.request.contextPath}/transdetail/query_dsf_ajax">
		<!-- 冲正请求URL -->
		<input type="hidden" id="correctUrl" value="${pageContext.request.contextPath}/transdetail/do_correct" />
		<!-- 抹账和交易后退款请求URL -->
		<input type="hidden" id="accOfOrRefundUrl" value="${pageContext.request.contextPath}/transdetail/do_accof_or_refund" />
		<!-- 清分 -->
		<input type="hidden" id="profit" value="${pageContext.request.contextPath}/transdetail/profit" />
		<!-- 分润结算 -->
		<input type="hidden" id="profitBalance" value="${pageContext.request.contextPath}/transdetail/profit_balance" />
		<!-- 挂账 -->
		<input type="hidden" id="bill" value="${pageContext.request.contextPath}/transdetail/bill" />
		<!-- 取消挂账 -->
		<input type="hidden" id="cancelBill" value="${pageContext.request.contextPath}/transdetail/cancel_bill" />
		<!-- 代收付 请求后台查询总金额 -->
		<input type="hidden" id="queryTotalAmount" value="${pageContext.request.contextPath}/transdetail/query_total_amount" />
		<!-- 选中的数据发送代收付系统 -->
		<input type="hidden" id="summaryByIds" value="${pageContext.request.contextPath}/transdetail/summaryByIds" />
		
		<!-- 代收付结果推送给订单系统 -->
		<input type="hidden" id="sendOrderUrl" value="${pageContext.request.contextPath}/transdetail/send_order" />
		<!-- 代收付结果推送给账户系统 -->
		<input type="hidden" id="sendAccountTPUrl" value="${pageContext.request.contextPath}/transdetail/send_account_tp" />
		
		<div class="main">
			<div class="blank15"></div>
			<div class="wrap">
				<div class="con">
					<div class="con_title">
						<h3 class="left fs14 marglr3020">交易表代收付信息</h3>
						<span class="grey"></span>
					</div>
					<div class="main_con" style="display: ">
						<div class="con_article_title tit_zindex3 linehei30">
							<b class="left paddl67 paddr10">内容筛选</b> <a href="javascript:;"
								class="margt10 btn_arrow_up" id="reg2_arrow"></a>
						</div>
						<div class="con_article_title02 tit_zindex6" id="reg2_arrow_con">
							<div class="con_article_title02 tit_zindex6" id="reg2_arrow_con">
								<div class="con_article_title02 margt10 tit_zindex5 paddl52" style="z-index:10;width:90%">
									<label class="label_txt wid135 txtright margr8 margt11">交易请求号： </label>
									<div class="input_con_type_top wid190 margt4">
										<input type="text" class="wid190" name="requestNo" id="requestNo" />
									</div>
									<label class="label_txt wid135 txtright margr8 margt11">
										交易请求时间： </label>
									<div class="input_time_type wid190 margt4">
										<input id="checkStartTime" type="text" readonly="readonly"
											name="requestDateStrBegin"
											class="wid140 hasDatepicker" value=""> <a
											href="javascript:;"
											onclick="WdatePicker({el:'checkStartTime',isShowClear:true,readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'#F{$dp.$D(\'checkEndTime\')}'})"
											class="right icon_timer timer_calender margtr36"></a>
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
											class="right icon_timer timer_calender margtr36"></a>
									</div>
								</div>
							</div>
							<div class="con_article_title02 margt10 tit_zindex5 paddl52" style="width:90%">
								<label class="label_txt wid135 txtright margr8 margt11">订单号： </label>
								<div class="input_con_type_top wid190 margt4">
									<input type="text" class="wid190" name="orderNo" id="orderNo" />
								</div>
								<label class="label_txt wid135 txtright margr8 margt11">功能： </label>
								<div class="input_con_type_top wid190 margt4">
									<select style="width:189px;height:28px;" name="funcCode">
										<option style="height:25px" value="">全部</option>
										<option style="height:25px" value="4013" >代收</option>
										<option style="height:25px" value="4014" >代付</option>
										<option style="height:25px" value="4014_1" >一分钱代付</option>
										<option style="height:25px" value="4016" >提现</option>
									</select>
								</div>
								<label class="label_txt wid135 txtright margr8 margt11">
									代收付状态： 
								</label>
								<div class="input_con_type_top wid190 margt4">
									<select style="width:189px;height:28px;" name="dflag">
										<option style="height:25px" value="">全部</option>
										<option style="height:25px" value="0" >未处理</option>
										<option style="height:25px" value="1" >处理中</option>
										<option style="height:25px" value="4" >代收付失败</option>
										<option style="height:25px" value="40" >代收付失败，结果推送账户失败</option>
										<option style="height:25px" value="41" >代收付失败，结果推送账户成功</option>
										<option style="height:25px" value="6" >代收付成功</option>
										<option style="height:25px" value="60" >代收付成功，结果推送账户失败</option>
										<option style="height:25px" value="61" >代收付成功，结果推送账户成功</option>
										<option style="height:25px" value="99" >异常</option>
									</select>
								</div>
							</div>
							<div class="con_article_title02 margt10 tit_zindex5 paddl52" style="width:90%">
								<label class="label_txt wid135 txtright margr8 margt11">用户ID： </label>
								<div class="input_con_type_top wid190 margt4">
									<input type="text" class="wid190" name="userId" id="userId" />
								</div>
								<label class="label_txt wid135 txtright margr8 margt11">机构号： </label>
								<div class="input_con_type_top wid190 margt4">
									<input type="text" class="wid190" name="merchantCode" id="merchantCode" />
								</div>
								<label class="label_txt wid135 txtright margr8 margt11">
									退票状态： 
								</label>
								<div class="input_con_type_top wid190 margt4">
									<select style="width:189px;height:28px;" name="deliverStatusId">
										<option style="height:25px" value="">全部</option>
										<option style="height:25px" value="0" >退票成功</option>
										<option style="height:25px" value="1" >退票失败</option>
										<option style="height:25px" value="99" >退票异常</option>
									</select>
								</div>
							
							</div>
							<div class="con_article_title02 margt10 tit_zindex3">
							<!-- 	<a id="resetFormButt" href="javascript:void(0);" class="right chan_btn wid60 margr30 margt5" onclick="resetForm();">重置</a> -->
								<a id="searchButt"  href="javascript:void(0);" class="right chan_btn wid60 margr30 margt5" onclick="funSearch(1);">查询</a> 
								<!-- 
								<a id="accOfOrRefundButt" href="javascript:void(0);" class="right chan_btn wid120 margr66 margt5" onclick="doAccOfOrRefund(this)">抹账 || 交易后退款</a>
								<a id="correctButt" href="javascript:void(0);" class="right chan_btn wid60 margr30 margt5" onclick="doCorrect(this);">冲正</a>
								<a id="profitBalanceButt" href="javascript:void(0);" class="right chan_btn wid120 margr66 margt5" onclick="doProfitBalance(this);">【分润】结算</a>
								<a id="profitButt" href="javascript:void(0);" class="right chan_btn wid60 margr30 margt5" onclick="doProfit(this);">清分</a>
								<a id="cancelBillButt" href="javascript:void(0);" class="right chan_btn wid120 margr66 margt5" onclick="doCancelBill(this);">取消挂账</a>
								<a id="billButt" href="javascript:void(0);" class="right chan_btn wid60 margr30 margt5" onclick="doBill(this);">挂账</a>
								 -->
							</div>
						</div>

						<div class="bord11 margt10"></div>
						<div class="con_core_info_main wid1125 paddl10 margt10" style="width:1225px;">
						   <table class="chan_table tb_bord01" id="user_table3">
							    <tr style="height=40px;" ><td colspan=13><a id="sendAccountTP" href="javascript:void(0);" class="right chan_btn wid165 margr30 margt5" onclick="sendAccountTP();">代收付或退票推送账户</a><a id="sendOrder" href="javascript:void(0);" class="right chan_btn wid100 margr30 margt5" onclick="sendOrder();">推送给订单</a><a id="searchButt"  href="javascript:void(0);" class="right chan_btn wid100 margr30 margt5" onclick="summaryByIds1();">汇总</a> </td></tr>
						   </table>
						   <table class="chan_table tb_bord01" id="user_table1">
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
		<input type="hidden" id="pageIndex" name="pageIndex" value="1" /> 
		<input type="hidden" id="pageSize" name="pageSize" value="10" />
		<!-- // 分页隐藏域 -->
	</form>
</div>
</body>
</html>