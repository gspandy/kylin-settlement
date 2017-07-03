<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>上传对账文件操作页面</title>

<jsp:include page="../../base/base_page.jsp"/>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/settlement/collate/upload.js"></script>
</head>
<div class="area">
	<form name="dataForm" id="dataForm" method="post" action="${pageContext.request.contextPath}/collate/upload_collate_file_ajax">
		<div class="main">
			<div class="blank15"></div>
			<div class="wrap">
				<div class="con">
					<div class="con_title">
						<h3 class="left fs14 marglr3020">上传对账文件操作</h3>
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
								<!-- <label class="label_txt wid135 txtright margr8 margt11 margl16">ROP文件类型编码： </label>
								<div class="input_con_type_top wid190 margt4">
									<input type="text" class="wid190" name="ropFileType" id="ropFileType"/>
								</div> -->
								<label class="label_txt wid135 txtright margr8 margt11 margl16">交易类型： </label>
								<div class="input_con_type_top wid190 margt4">
									<select style="width:189px;height:28px;" name="readType">
										<option style="height:25px" value="01">网关对账</option>
										<option style="height:25px" value="02">代收付对账</option>
									</select>
								</div>
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
							</div>
							<div class="con_article_title02 tit_zindex6" id="reg2_arrow_con">
								<div class="con_article_title02 margt10 tit_zindex5 paddl52" style="z-index:10;width:90%">
									<label class="label_txt wid135 txtright margr8 margt11 margl16">
										批次号： </label>
									<div class="input_con_type_top wid3 margt4">
										<input type="text" class="wid190" name="batch" id="batch"/>
									</div>
									<div id="valiDiv" class="label_txt txtright margr8 margt11"></div>
									<a href="javascript:void(0);" id="autoBtn" class="left chan_btn wid80 margr30 margt5">随机生成</a>
								</div>
							</div>
							<div class="con_article_title02 margt10 tit_zindex3">
								<a href="javascript:void(0);" class="right chan_btn wid100 margr30 margt5" onclick="makefile();">生成对账文件</a>
								<input name="makefilebtn" id="makefilebtn" type="hidden" value="${pageContext.request.contextPath}/collate/create_collate_file_ajax" />
								<a href="javascript:void(0);" id="btnSubmit" class="right chan_btn wid100 margr30 margt5">开始上传</a>
							</div>
						</div>

						<div class="bord11 margt10"></div>
					</div>
				</div>
			</div>
		</div>
	</form>
</div>
</body>
</html>