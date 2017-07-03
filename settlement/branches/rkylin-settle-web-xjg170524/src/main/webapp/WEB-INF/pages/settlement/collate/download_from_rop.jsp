<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>从ROP下载文件操作页面</title>

<jsp:include page="../../base/base_page.jsp"/>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/settlement/collate/download_from_rop.js"></script>
</head>
<div class="area">
	<form name="dataForm" id="dataForm" method="post" action="${pageContext.request.contextPath}/rop/rop_download">
		<div class="main">
			<div class="blank15"></div>
			<div class="wrap">
				<div class="con">
					<div class="con_title">
						<h3 class="left fs14 marglr3020">从ROP下载文件操作</h3>
						<span class="grey"></span>
					</div>
					<div class="main_con" style="display: ">
						<div class="con_article_title tit_zindex3 linehei30">
							<b class="left paddl67 paddr10">内容筛选</b> 
							<a href="javascript:;" class="margt10 btn_arrow_up" id="reg2_arrow"></a>
						</div>
						<div class="con_article_title02 tit_zindex6" id="reg2_arrow_con">
							<div class="con_article_title02 margt10 tit_zindex5 paddl52" style="width:90%">
								<label class="label_txt wid135 txtright margr8 margt11 margl16">文件类型： </label>
								<div class="input_con_type_top wid190 margt4">
									<input type="text" class="wid190" name="type" id="type"/>
								</div>
								<label class="label_txt wid135 txtright margr8 margt11 margl16">公钥/私钥： </label>
								<div class="input_con_type_top wid190 margt4">
									<input type="text" class="wid190" name="priOrPubKey" id="priOrPubKey"/>
								</div>
								<label class="label_txt wid135 txtright margr8 margt11 margl16">
									账期： </label>
								<div class="input_time_type wid190 margt4">
									<input id="invoicedate" type="text" readonly="readonly"
										name="invoicedate"
										class="wid140 hasDatepicker" value=""> <a
										href="javascript:;"
										onclick="WdatePicker({el:'invoicedate',isShowClear:true,readOnly:true,dateFmt:'yyyy-MM-dd 00:00:00'})"
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
									<label class="label_txt wid135 txtright margr8 margt11 margl16">
										后缀： </label>
									<div class="input_con_type_top wid3 margt4">
										<input type="text" class="wid190" name="fileType" id="fileType"/>
									</div>
								</div>
							</div>
							<div class="con_article_title02 margt10 tit_zindex3">
								<a href="javascript:void(0);" id="btnSubmit" class="right chan_btn wid100 margr30 margt5">开始下载</a>
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