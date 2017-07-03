<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>代收付信息手工汇总或发送</title>

<jsp:include page="../../../base/base_page.jsp"/>
<jsp:include page="../../../base/createIF.jsp"/>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/settlement/trans/dsfManualSummaryOrSend.js"></script>
</head>
<div class="area">
	<form name="dataForm" id="dataForm" method="post" action="">
		
		<!-- 代收付结果推送给账户系统 -->
		<input type="hidden" id="summary" value="${pageContext.request.contextPath}/transdetail/summary" />
		<input type="hidden" id="sendDsf" value="${pageContext.request.contextPath}/invoice/send_dsf" />
		<input type="hidden" id="validateUrl" value="${pageContext.request.contextPath}/invoice/validate_before_send_dsf" />
		
		<div class="main">
			<div class="blank15"></div>
			<div class="wrap">
				<div class="con">
					<div class="con_title">
						<h3 class="left fs14 marglr3020">代收付信息手工汇总或发送</h3>
						<span class="grey"></span>
					</div>
					<div class="main_con" style="display: ">
						<div class="con_article_title tit_zindex3 linehei30">
							<b class="left paddl67 paddr10">条件</b> <a href="javascript:;"
								class="margt10 btn_arrow_up" id="reg2_arrow"></a>
						</div>
						<div class="con_article_title02 tit_zindex6" id="reg2_arrow_con">
							<div class="con_article_title02 margt10 tit_zindex5 paddl52" style="width:90%">
							
								<label class="label_txt wid135 txtright margr8 margt11">数据来源： </label>
								<div class="input_con_type_top wid190 margt4">
									<select style="width:189px;height:28px;" name="dataSource" id="dataSource">
										<option style="height:25px" value="1" selected="selected" >系统推送</option>
										<option style="height:25px" value="0" >文件导入</option>
									</select>
								</div>
							
								<label class="label_txt wid135 txtright margr8 margt11">机构号： </label>
								<div class="input_con_type_top wid190 margt4">
									<input type="text" class="wid190" name="rootInstCds" id="rootInstCds" />
								</div>
								<label class="label_txt wid135 txtright margr8 margt11">功能： </label>
								<div class="input_con_type_top wid190 margt4">
									<select style="width:189px;height:28px;" name="flag" id="flag">
										<option style="height:25px" value="0" >代付和提现</option>
										<option style="height:25px" value="1" >代收</option>
										<option style="height:25px" value="2" >T0提现(仅限课栈)</option>
										<option style="height:25px" value="4" >一分钱代付</option>
										<option style="height:25px" value="5" >生成支付文件</option>
									</select>
								</div>
							</div>
							
							<div class="con_article_title02 margt10 tit_zindex5 paddl52" style="width:90%">
							
								<label class="label_txt wid135 txtright margr12 margt11">人工确认金额(单位分)： </label>
								<div class="input_con_type_top wid190 margt4">
									<input type="text" class="wid190" name="money" id="money" />
								</div>
							
								<label class="label_txt wid135 txtright margr8 margt11">人工确认的条数: </label>
								<div class="input_con_type_top wid190 margt4">
									<input type="text" class="wid190" name="totalNum" id="totalNum" />
								</div>
							</div>
							
							<div class="con_article_title02 margt10 tit_zindex5 paddl52" style="width:90%">
								<label style="margin-left=30px;margin-top=5px;"><font color="red">注意："人工确认的金额"和"人工确认的条数"是"发送代收付"必填项 </font></label>
							</div>
							
							<div class="con_article_title02 margt10 tit_zindex3">
								<a id="sendId"  href="javascript:void(0);" class="right chan_btn wid70 margr30 margt5" onclick="sendDsf();">发送代收付</a>
								<a id="summaryId"  href="javascript:void(0);" class="right chan_btn wid60 margr30 margt5" onclick="summary();">汇总</a> 
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