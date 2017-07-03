<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>生成头寸交易</title>

<jsp:include page="../../base/base_page.jsp"/>
<style type="text/css">
    .divhide {display:none}
</style>
<script type="text/javascript">
</script>
</head>
<div class="area">
    <form name="dataForm" id="dataForm" action="${pageContext.request.contextPath}/position/create" >
        <div class="main">
            <div class="blank15"></div>
            <div class="wrap">
                <div class="con">
                    <div class="con_title">
                        <h3 class="left fs14 marglr3020">生成头寸交易</h3>
                        <span class="grey"></span>
                    </div>
                    <div class="main_con" style="display: ">
                           <div class="con_article_title02 margt10 tit_zindex5 paddl52" style="width:90%">
                                <label class="label_txt wid135 txtright margr8 margt11">头寸类型： </label>
                                <div class="input_con_type_top wid190 margt4">
                                    <select style="width:189px;height:28px;" name="isAll" id="isAll">
                                        <option value="true">全规则头寸</option>
                                        <option value="false" selected>查询规则头寸</option>
                                    </select>
                                </div>
                           </div>
                           <div class="con_article_title02 margt10 tit_zindex5 paddl52" style="width:90%">
                                <label class="label_txt wid135 txtright margr8 margt11">机构号： </label>
                                <div class="input_con_type_top wid190 margt4">
                                    <input type="text" class="wid190" name="rootInstCd" id="rootInstCd" />
                                </div>
                                <label class="label_txt wid135 txtright margr8 margt11">交易码： </label>
                                <div class="input_con_type_top wid190 margt4">
                                    <input type="text" class="wid190" name="funcCode" id="funcCode" />
                                </div>
                            </div>
                            <div class="con_article_title02 margt10 tit_zindex5 paddl52" style="width:90%">
                                <label class="label_txt wid135 txtright margr8 margt11">用户ID： </label>
                                <div class="input_con_type_top wid190 margt4">
                                    <input type="text" class="wid190" name="userId" id="userId" />
                                </div>
                                <label class="label_txt wid135 txtright margr8 margt11">管理分组： </label>
                                <div class="input_con_type_top wid190 margt4">
                                    <input type="text" class="wid190" name="productId" id="productId" />
                                </div>
                            </div>
                            <div class="con_article_title02 margt10 tit_zindex3">
                                <a href="javascript:void(0);" class="right chan_btn wid60 margr30 margt5" onclick="createPostionInfo();">生成头寸</a>
                            </div>
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>
</body>
<jsp:include page="../../base/createIF.jsp" />
<script type="text/javascript">
$(function() {
	$("#isAll").change(function() {disabldQuery();});
    getheight();
});
function disabldQuery() {
	var isAll = $("#isAll option:selected").val();
	if(isAll == 'true') {
		$("#dataForm input[id!='isAll']").attr("disabled", true);
	} else {
		$("#dataForm input[id!='isAll']").attr("disabled", false);
	}
}
function createPostionInfo() {
	if(!confirm("您确定要执行:[生成头寸交易] 操作吗?")) return;
	
	var isAll = $("#isAll option:selected").val();
	
	if(isAll == 'false') {
		if(!$('#rootInstCd').val()) {alert("机构号不能为空!"); return;}
	    if(!$('#funcCode').val()) {alert("交易码不能为空!"); return;}
	    if(!$('#userId').val()) {alert("用户ID不能为空!"); return;}
	    if(!$('#productId').val()) {alert("管理分组不能为空"); return;}
	}
	var url = $('#dataForm').attr('action');
	var data = $('#dataForm').serialize();
	var success = function(data) {
		art.dialog.tips("<span style='color:green'>"+ data +"</span>", 3.5, function(){});
	};
	$.post(
        url, 
        data, 
        success, 
        'text'
    );
}
</script>
</html>