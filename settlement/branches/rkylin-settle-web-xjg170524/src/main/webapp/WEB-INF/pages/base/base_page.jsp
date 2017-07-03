<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<base href="<%=basePath%>"/>
<script type="text/javascript">
	var CONTEXT_PATH = '<%=basePath %>';
</script>
<link type="text/css" href="${pageContext.request.contextPath}/css/global.css" rel="Stylesheet" />
<link type="text/css" href="${pageContext.request.contextPath}/css/layout.css" rel="Stylesheet" />
<link type="text/css" href="${pageContext.request.contextPath}/css/lightbox.css" rel="Stylesheet" />
<link type="text/css" href="${pageContext.request.contextPath}/css/1/style.css" rel="Stylesheet" />
<link type="text/css" href="${pageContext.request.contextPath}/css/flexslider.css" rel="Stylesheet" />
<link type="text/css" href="${pageContext.request.contextPath}/js/artDialog/skins/idialog.css" rel="Stylesheet" />
<script type="text/javascript" src="${pageContext.request.contextPath}/js/common/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/common/jquery.cookie.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/common/loginRedirect.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/common/formValidatorRegex.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/common/formValidator-4.1.1.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/common/common-constants.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/common/web-common.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/common/select-common.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/common/CONSTANTS.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/common/jquery.flexslider-min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/common/datePicker/WdatePicker.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/artDialog/artDialog.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/artDialog/plugins/iframeTools.source.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/common/base_utils.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/common/web-tip.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/common/messenger.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/common/jquery.base64.excl.output.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/common/tableExport.js"></script>
<jsp:include page="createIF.jsp" />
<script type="text/javascript">
	$(function() {
		getheight();
	});
</script>