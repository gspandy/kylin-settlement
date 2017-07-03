<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>我的下载</title>

<jsp:include page="../../base/base_page.jsp"/>
<style type="text/css">
   .divhide {display:none}
</style>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/settlement/my_download/download.js"></script>
</head>
<div class="area">
	<form name="dataForm" id="dataForm" method="post" action="${pageContext.request.contextPath}/collate/query_ajax">
		<input type="hidden" value="${pageContext.request.contextPath}/my_download/query_download_file" id="download_collate_ajax" />
		<input type="hidden" value="${pageContext.request.contextPath}" id="root_path" />
		<div class="main">
			<div class="blank15"></div>
			<div class="wrap">
				<div class="con">
					<div class="con_title">
						<h3 class="left fs14 marglr3020">我的下载</h3>
						<span class="grey"></span>
					</div>
					<div class="main_con" style="display: ">
						<div class="con_article_title tit_zindex3 linehei30">
							<b class="left paddl67 paddr10">内容筛选</b> 
							<a href="javascript:;" class="margt10 btn_arrow_up" id="reg2_arrow"></a>
						</div>
						
						<div class="con_article_title02 tit_zindex6" id="reg2_arrow_con">
							<div class="con_article_title02 margt10 tit_zindex5 paddl52" style="width:90%" id="down_div">
								<label class="label_txt wid135 txtright margr8 margt11 margl16">文件类型： </label>
								<div class="input_con_type_top wid190 margt4" id="tldivU">
									<select style="width:189px;height:28px;" name="transType" id="transType">
										<option style="height:25px" value="-1">全部</option>
										<option style="height:25px" value="accGenPay">失败代付</option>
										<option style="height:25px" value="SWAccCpr">充值对账</option>
									</select>
								</div>
								<label class="label_txt wid135 txtright margr8 margt11 margl16">
									下载日期： </label>
								<div class="input_time_type wid190 margt4">
									<input id="downloadDate" type="text" readonly="readonly"
										name="downloadDate"
										class="wid140 hasDatepicker" value=""> <a
										href="javascript:;"
										onclick="WdatePicker({el:'downloadDate',isShowClear:true,readOnly:true,dateFmt:'yyyy-MM-dd 00:00:00'})"
										class="right icon_timer timer_calender margtr36"
										title="请输入账期再进行读入操作"
										></a>
									<script>
										var date = new Date();
										var month = date.getMonth() + 1 < 10 ? "0" + (date.getMonth() + 1) : date.getMonth() + 1 < 10;
										var dateNum = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
										$("#downloadDate").val(date.getFullYear()+"-"+month+"-"+dateNum+" 00:00:00");
									</script>
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
									<th width="75%">文件名称</th>
									<th width="20%">生成时间</th>
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