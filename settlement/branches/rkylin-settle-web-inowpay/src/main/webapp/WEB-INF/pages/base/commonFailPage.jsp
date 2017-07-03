<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>信息提示</title>
    <jsp:include page="../base/base_page.jsp"></jsp:include>
</head>
<body>
    <div class="area">
    	<jsp:include page="../base/base_top3.jsp"></jsp:include>
        <div class="main">
            <div class="blank15">
            </div>
            <div class="wrap">
                <div class="con">
                    <div class="con_title">
                        <h3 class="left fs14 marglr3020">信息提示</h3>
                    </div>
                    <div class="main_con">
                        <div class="blank38"></div>
                        <div class="con_core_succ heig185 bord12 paddt50">
                            <span class="icon_fail margl180"></span>
                            <span class="succ_info fs14 margl16 margtb20">${errorMsg}</span>
                        </div>
                        <div class="blank98"></div>
                    </div>
                </div>
            </div>
            <div class="blank30"></div>
        </div>
        <div class="blank10 bg_grey02 bord_shadow"></div>
        <jsp:include page="../base/base_foot.jsp"></jsp:include>
    </div>
</body>
</html>