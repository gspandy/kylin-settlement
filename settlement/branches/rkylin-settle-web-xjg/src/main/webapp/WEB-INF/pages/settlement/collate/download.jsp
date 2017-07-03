<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>下载对账文件</title>

<jsp:include page="../../base/base_page.jsp"/>
<style type="text/css">
   .divhide {display:none}
</style>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/settlement/collate/download.js"></script>
</head>
<div class="area">
	<form name="dataForm" id="dataForm" method="post" action="${pageContext.request.contextPath}/collate/query_ajax">
		<input type="hidden" value="${pageContext.request.contextPath}/collate/query_download_collatefile2merchant" id="download_collate_ajax" />
		<input type="hidden" value="${pageContext.request.contextPath}" id="root_path" />
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
							<div class="con_article_title02 margt10 tit_zindex5 paddl52">
								<label class="label_txt wid135 txtright margr8 margt11 margl16">请选择对账文件的类型： </label>
								<div class="input_con_type_top wid190">
									<select select style="width:189px;height:28px;" name="fileType" id="fileType">
										<option style="height:25px" value="up" selected>下游对账文件</option>
										<option style="height:25px" value="down">上游对账文件</option>
									</select>
								</div>
							</div>
							<div class="con_article_title02 margt10 tit_zindex5 paddl52" style="width:90%" id="up_div">
								<label class="label_txt wid135 txtright margr8 margt11 margl16">机构： </label>
								<div class="input_con_type_top wid190 margt4">
									<select style="width:189px;height:28px;" name="merchantName" id="merchantName">
										<option style="height:25px" value="">全部</option>
										<option style="height:25px" value="FN">丰年</option>
										<option style="height:25px" value="HT">会堂</option>
										<option style="height:25px" value="KZ">课栈</option>
										<option style="height:25px" value="JRD">君融代</option>
										
										<option style="height:25px" value="SQSM">食全食美</option>
										<option style="height:25px" value="MZ">棉庄</option>
										<option style="height:25px" value="ZK">展酷</option>
										<option style="height:25px" value="ZJ">指尖</option>
										<option style="height:25px" value="MJY">卖家云</option>
										<option style="height:25px" value="TXYW">通信运维</option>
										
										<option style="height:25px" value="TXFC">天下房仓</option>
										<option style="height:25px" value="QYBT">企业白条</option>
										<option style="height:25px" value="BBZX">帮帮助学</option>
										
										<option style="height:25px" value="LKKJ">领客科技</option>
										<option style="height:25px" value="YSJ">领客科技</option>
										
										<option style="height:25px" value="RSQB">融数钱包</option>
									</select>
								</div>
								<label class="label_txt wid135 txtright margr8 margt11 margl16">对账类型： </label>
								<div class="input_con_type_top wid190 margt4" id="tldiv">
									<select style="width:189px;height:28px;" name="transType" id="transType">
										<option style="height:25px" value="">全部</option>
										<option style="height:25px" value="_CZ">充值</option>
										<option style="height:25px" value="FCZ">非充值</option>
										<option style="height:25px" value="DSF">代收付</option>
									</select>
								</div>
							</div>
							
							
							<div class="con_article_title02 margt10 tit_zindex5 paddl52" style="width:90%" id="down_div">
								<label class="label_txt wid135 txtright margr8 margt11 margl16">渠道： </label>
								<div class="input_con_type_top wid190 margt4">
									<select style="width:189px;height:28px;" name="channelCodeD" id="channelCodeD">									
										<option style="height:25px" value="">全部</option>
										<option style="height:25px" value="TL_">通联</option>
										<option style="height:25px" value="LL_">连连</option>
										<option style="height:25px" value="CJ">畅捷支付</option>
										<option style="height:25px" value="LD_">联动优势</option>
										<option style="height:25px" value="_PAB_BankPayment">平安银行</option>
										<option style="height:25px" value="_CMBC_BankPayment">民生银行</option>
									</select>
								</div>
								<label class="label_txt wid135 txtright margr8 margt11 margl16">协议： </label>
								<div class="input_con_type_top wid190 margt4" id="tldivU">
									<select style="width:189px;height:28px;" name="merchantCodeD" id="merchantCodeD">
										<option style="height:25px" value="">全部</option>
										<option style="height:25px" value="_M00000X">融数</option>
										<option style="height:25px" value="_M000001">丰年</option>
										<option style="height:25px" value="_M000003">会堂</option>
										<option style="height:25px" value="_M000004">课栈</option>
										<option style="height:25px" value="_M000005">君融代</option>
										<option style="height:25px" value="_M000006">食全食美</option>
										<option style="height:25px" value="_M000007">棉庄</option>
										<option style="height:25px" value="_M000011">展酷</option>
										<option style="height:25px" value="_M000012">指尖</option>
										<option style="height:25px" value="_M000013">卖家云</option>
										<option style="height:25px" value="_M000014">通信运维</option>
										<option style="height:25px" value="_RONGDE">融德保理</option>
										
										<option style="height:25px" value="_M000016">天下房仓</option>
										<option style="height:25px" value="_M000017">企业白条</option>
										<option style="height:25px" value="_M000020">帮帮助学</option>
										<option style="height:25px" value="_M000024">领客科技</option>
										<option style="height:25px" value="_M000025">悦视觉</option>
										<option style="height:25px" value="_M666666">融数钱包</option>
									</select>
								</div>
								<label class="label_txt wid135 txtright margr8 margt11 margl16">对账类型： </label>
								<div class="input_con_type_top wid190 margt4" id="tldivU">
									<select style="width:189px;height:28px;" name="transTypeD" id="transTypeD">
										<option style="height:25px" value="">全部</option>
										<option style="height:25px" value="_ZF">代收付</option>
										<option style="height:25px" value="_WG">网关支付</option>
										<option style="height:25px" value="_KJZF">移动快捷支付</option>
										<option style="height:25px" value="_WX">收银宝(微信)</option>
										<option style="height:25px" value="_TLSDK">SDK(通联)</option>
										<option style="height:25px" value="_CMBC">民生银行-银企直联</option>
										<option style="height:25px" value="_PAB">民生银行-银企直联</option>
									</select>
								</div>
							</div>
							
							<div class="con_article_title02 margt10 tit_zindex5 paddl52" style="width:90%" id="account_date_div">
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
								<div class="con_article_title02 margt10 tit_zindex5 paddl52" style="z-index:10;width:90%"></div>
							</div>
							
							<div class="con_article_title02 margt10 tit_zindex3">
								<a href="javascript:void(0);" id="btnSubmit" class="right chan_btn wid100 margr30 margt5">查询</a>
							</div>
						</div>
						
						<div class="bord11 margt10"></div>
						<div class="con_core_info_main wid1125 paddl10 margt10" style="width:1225px;">
							<table class="chan_table tb_bord01" id="user_table1">
								<tr>
									<th width="5%">序号</th>
									<th width="95%">文件名称</th>
								</tr>
								<tr><td colspan="7">没有符合条件的数据</td></tr>
							</table>
						</div>
						<div id="div_table_page" class="chan_table_page_wrap wid620 margt10 paddr36">
							<!--  -->
							<div class="chan_table_page"></div>
						</div>		
					</div>
				</div>
			</div>
		</div>
	</form>
</div>
</body>
</html>