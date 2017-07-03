<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>贷款还款信息</title>

<jsp:include page="../../../base/base_page.jsp"/>
<jsp:include page="../../../base/createIF.jsp"/>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/settlement/trans/loanQuery.js"></script>
	<script>
		$(function(){
			$("checkbox").bind("click",function(){
				alert(1);
				//alert($(this).find(".ids_box").val());
			});
			$("input.ids_box").change(function(){
				alert("11");
			})
		});
	</script>
</head>
<div class="area">
	<form name="dataForm" id="dataForm" method="post" action="${pageContext.request.contextPath}/loandetail/query_ajax">
		<!-- 生成代付数据请求URL -->
		<input type="hidden" id="correctUrl" value="${pageContext.request.contextPath}/loandetail/do_correct" />
		<!-- 抹账和交易后退款请求URL -->
		<input type="hidden" id="accOfOrRefundUrl" value="${pageContext.request.contextPath}/loandetail/do_accof_or_refund" />
		<!-- 清分 -->
		<input type="hidden" id="profit" value="${pageContext.request.contextPath}/loandetail/profit" />
		<!-- 分润结算 -->
		<input type="hidden" id="profitBalance" value="${pageContext.request.contextPath}/loandetail/profit_balance" />
		<!-- 取消挂账 -->
		<input type="hidden" id="cancelBill" value="${pageContext.request.contextPath}/loandetail/cancel_bill" />
		<!-- 挂账 -->
		<input type="hidden" id="bill" value="${pageContext.request.contextPath}/loandetail/billDate" />
		<div class="main">
			<div class="blank15"></div>
			<div class="wrap">
				<div class="con">
					<div class="con_title">
						<h3 class="left fs14 marglr3020">贷款还款信息</h3>
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
									<label class="label_txt wid135 txtright margr8 margt11">机构号： </label>
									<div class="input_con_type_top wid190 margt4">
										<input type="text" class="wid190" name=rootInstCd id="rootInstCd" />
									</div>
									<label class="label_txt wid135 txtright margr8 margt11">
										贷款还款Id： </label>
									<div class="input_con_type_top wid190 margt4">
										<input type="text" class="wid190" name=loanId id="loanId" />
									</div>
									<label class="label_txt wid135 txtright margr8 margt11">
										交易状态： </label>
									<div class="input_con_type_top wid190 margt4">
										<select style="width:189px;height:28px;" name="statusId">
											<option style="height:25px" value="">全部</option>
											<option style="height:25px" value="1">读入成功</option>
											<option style="height:25px" value="11">清分成功</option>
											<option style="height:25px" value="10">清分失败</option>
										</select>
									</div>
								</div>
							</div>
							<div class="con_article_title02 margt10 tit_zindex3">
								<a id="resetFormButt" href="javascript:void(0);" class="right chan_btn wid60 margr30 margt5" onclick="resetForm();">清除</a>
								<a id="searchButt"  href="javascript:void(0);" class="right chan_btn wid60 margr30 margt5" onclick="funSearch(1);">查询</a> 
								<a id="cancelBillButt" href="javascript:void(0);" class="right chan_btn wid120 margr66 margt5" onclick="doInvoiceCash(this);">发送头寸交易</a>
								<a id="accOfOrRefundButt" href="javascript:void(0);" class="right chan_btn wid120 margr66 margt5" onclick="doInvoiceSettle(this)">发送代付交易</a>
								<a id="correctButt" href="javascript:void(0);" class="right chan_btn wid60 margr30 margt5" onclick="doSettleInvoice(this);">生成代付数据</a>
								<a id="profitBalanceButt" href="javascript:void(0);" class="right chan_btn wid120 margr66 margt5" onclick="doProfitBalance(this);">【分润】结算</a>
								<a id="profitButt" href="javascript:void(0);" class="right chan_btn wid60 margr30 margt5" onclick="doProfit(this);">清分</a>
								<a id="billButt" href="javascript:void(0);" class="right chan_btn wid60 margr30 margt5" onclick="doBill(this);">读取贷款系统交易</a>
							</div>
						</div>

						<div class="bord11 margt10"></div>
						<div class="con_core_info_main wid1125 paddl10 margt10 overflow-ui">
							<table class="chan_table tb_bord01 tb_style" id="user_table1">
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