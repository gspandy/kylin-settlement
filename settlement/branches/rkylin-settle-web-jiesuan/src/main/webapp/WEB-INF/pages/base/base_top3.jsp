<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<link rel="stylesheet" href="../../css/zTreeStyle/zTreeStyle.css"
			type="text/css">
		<%--<script src="../../js/common/jquery-1.9.0.js"></script>
		--%><script src="../../js/common/jquery.ztree.all-3.5.min.js"></script>
<script type="text/javascript">
	jQuery(document).ready(function() {
		$(".new_header_nav li").hover(function() {
			$(this).children("ul").show();
		}, function() {
			$(this).children("ul").hide();
		});
	});
</script>
 <!--<script type="text/javascript">
	jQuery(document).ready(function() {
		$(".new_header_nav li").click(function() {
			$(this).children("ul").show();
		});
	});
</script>!-->
<!--公共头部Begin-->
<div class="nav navbg01">
</div>
<div class="new_header navbg02">
	<div class="wrap">
		<a href="/index.jsp" class="left margr40"> <img
				src="../../images/logo.png" alt="畅捷支付" />
		</a>		
	</div>
</div>
<div class="nav navbg03" style="display: none;">
	<div class="wrap">
		<div class="left bord01 padd13">
			<a href="javascript:;" class="icon_add" onclick="addMenu()"></a>
		</div>
		<div class="left wid1100 margr10 overhide">
			<a href="/common/loginManage.do?action=logout"
				class="nav_txt_layout nav_txt_style" target="_blank">部门管理</a>
			<span class="bord09 margt15"></span>
			<a href="/common/loginManage.do?action=logout"
				class="nav_txt_layout nav_txt_style">操作员管理</a>
			<span class="bord09 margt15"></span>
			<a href="/common/loginManage.do?action=logout"
				class="nav_txt_layout nav_txt_style">角色权限管理</a>
				<span class="bord09 margt15"></span>
			<a href="/common/loginManage.do?action=logout"
				class="nav_txt_layout nav_txt_style">角色权限管理</a>
				<span class="bord09 margt15"></span>
			<a href="/common/loginManage.do?action=logout"
				class="nav_txt_layout nav_txt_style">角色权限管理</a>
				<span class="bord09 margt15"></span>
			<a href="/common/loginManage.do?action=logout"
				class="nav_txt_layout nav_txt_style">角色权限管理</a>
				<span class="bord09 margt15"></span>
			<a href="/common/loginManage.do?action=logout"
				class="nav_txt_layout nav_txt_style">角色权限管理</a>
				<span class="bord09 margt15"></span>
			<a href="/common/loginManage.do?action=logout"
				class="nav_txt_layout nav_txt_style">角色权限管理</a>
				<span class="bord09 margt15"></span>
			<a href="/common/loginManage.do?action=logout"
				class="nav_txt_layout nav_txt_style">角色权限管理</a>
				<span class="bord09 margt15"></span>
			<a href="/common/loginManage.do?action=logout"
				class="nav_txt_layout nav_txt_style">角色权限管理</a>
				<span class="bord09 margt15"></span>
			<a href="/common/loginManage.do?action=logout"
				class="nav_txt_layout nav_txt_style">角色权限管理</a>
				<span class="bord09 margt15"></span>
			<a href="/common/loginManage.do?action=logout"
				class="nav_txt_layout nav_txt_style">角色权限管理</a>
				<span class="bord09 margt15"></span>
			<a href="/common/loginManage.do?action=logout"
				class="nav_txt_layout nav_txt_style">角色权限管理</a>
				<span class="bord09 margt15"></span>
			<a href="/common/loginManage.do?action=logout"
				class="nav_txt_layout nav_txt_style">角色权限管理</a>
				<span class="bord09 margt15"></span>
			<a href="/common/loginManage.do?action=logout"
				class="nav_txt_layout nav_txt_style">角色权限管理</a>
		</div>		
	</div>
</div>

<!--公共头部End-->