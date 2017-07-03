<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>代收付交易信息</title>


<jsp:include page="../../base/base_page.jsp"/>
<%-- 
<jsp:include page="../../base/createIF.jsp"/> --%>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/account/generation_payment/query_settler.js"></script>

</head>
<div class="area">
	<input type="hidden" name="getRootInstCdSelect" id="getRootInstCdSelect" value="${pageContext.request.contextPath}/parameterinfo/get_root_inst_cd_select" />
	<input type="hidden" name="doAllExport" id="doAllExport" value="${pageContext.request.contextPath}/accGenPay/query_and_output_excl" />
	<form method="post" name="pay_trans_form" id="pay_trans_form" action="${pageContext.request.contextPath}/accGenPay/create_gen_pay_file">
		<input type="hidden" name="idsStr" id="idsStr" value="" />
	</form>
	<form name="dataForm" id="dataForm" method="post" action="${pageContext.request.contextPath}/accGenPay/query_ajax">
		<div class="main">

			<div class="blank15"></div>
			<div class="wrap">
				<div class="con">
					<div class="con_title">
						<h3 class="left fs14 marglr3020">代收付交易信息</h3>
						<span class="grey"></span>
					</div>
					
					<div class="main_con" style="display: ">
						<div class="con_article_title tit_zindex3 linehei30">
							<b class="left paddl67 paddr10">验证内容</b> <a href="javascript:;"
								class="margt10 btn_arrow_up" id="reg2_arrow"></a>
						</div>
						<div class="con_article_title02 tit_zindex6" id="reg2_arrow_con">
							<div class="con_article_title02 margt10 tit_zindex5 paddl52" style="z-index:10;width:90%">						
								<label class="label_txt wid135 txtright margr8 margt11">交易总条数： </label>
								<div class="input_con_type_top wid190 margt4">
									<input type="text" class="wid190" name="checkTransCount" id="checkTransCount" />
								</div>
								<label class="label_txt wid135 txtright margr8 margt11">交易总金额： </label>
								<div class="input_con_type_top wid190 margt4">
									<input type="text" class="wid190" name="checkTransAmount" id="checkTransAmount" />
								</div>
								
								<a id="resetFormButt" href="javascript:void(0);" class="right chan_btn wid100 margr30 margt5" onclick="createGenPayFile();">生成代付交易</a>
							</div>
						</div>
					</div>
					
					<div class="main_con" style="display: ">
						<div class="con_article_title tit_zindex3 linehei30">
							<b class="left paddl67 paddr10">内容筛选</b> <a href="javascript:;"
								class="margt10 btn_arrow_up" id="reg2_arrow"></a>
						</div>
						<div class="con_article_title02 tit_zindex6" id="reg2_arrow_con">				
							<div class="con_article_title02 tit_zindex6" id="reg2_arrow_con">
								<div class="con_article_title02 margt10 tit_zindex5 paddl52" style="z-index:10;width:90%">
									<label class="label_txt wid135 txtright margr8 margt11">管理机构代码： </label>
									<div class="input_con_type_top wid190 margt4">
										<select style="width:189px;height:28px;" name="rootInstCd" id="rootInstCd">
											<option value="" selected>全部</option>
										</select>
									</div>
									
									<label class="label_txt wid135 txtright margr8 margt11">银行代码： </label>
									<div class="input_con_type_top wid190 margt4">
										<input type="text" class="wid190" name="bankCode" id="bankCode" />
									</div>
									
									<label class="label_txt wid135 txtright margr8 margt11">账号： </label>
									<div class="input_con_type_top wid190 margt4">
										<input type="text" class="wid190" name="accountNo" id="accountNo" />
									</div>
									
									<label class="label_txt wid135 txtright margr8 margt11">账号名： </label>
									<div class="input_con_type_top wid190 margt4">
										<input type="text" class="wid190" name="accountName" id="accountName" />
									</div>
									
									<label class="label_txt wid135 txtright margr8 margt11">发送类型： </label>
									<div class="input_con_type_top wid190 margt4">
										<select style="width:189px;height:28px;" name="sendType" id="sendType">
											<option value="-1" selected>全部</option>
											<option value="0">支付成功</option>
											<option value="1">支付失败</option>
											<option value="2">支付中</option>
										</select>
									</div>
								</div>
							</div>

							<div class="con_article_title02 margt10 tit_zindex3">
								<a id="resetFormButt" href="javascript:void(0);" class="right chan_btn wid60 margr30 margt5" onclick="$('#dataForm')[0].reset();">清除</a>
								<a id="searchButt"  href="javascript:void(0);" class="right chan_btn wid60 margr30 margt5" onclick="funSearch(1);">查询</a> 
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