<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<link rel="stylesheet" href="css/zTreeStyle/zTreeStyle.css"
			type="text/css">
		<%--<script src="../../js/common/jquery-1.9.0.js"></script>
		<script src="../../js/common/jquery.ztree.all-3.5.min.js"></script>
		--%>
<script type="text/javascript">
	jQuery(document).ready(function() {
		$(".new_header_nav li").hover(function() {
			$(this).children("ul").show();
			/* var i = $(".mainnav_a").index(this);
			if(i != 0){
				$(".mainnav_a").removeClass("hnav_check");
			} */
		},function() {
			$(this).children("ul").hide();
		});
		
	});
	function clearS(){
		var url = "/officeInstitution.do?action=saveSelect&menutype=menutype";
		$.ajaxSetup({  
		    async : false  
		});
		$.post(url,function(data) {
					nologinRedirect(data);
					data = $.parseJSON(data);
				});
		
	}
	
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
	<div class="wrap">
		<div class="right">
			<span class="nav_info margr8">你好，${requestScope.login_personName }</span>
			<span class="bord08 margt7"></span>
			<a href="/login.do?action=loginOut"
				class="nav_txt_layout nav_txt_style">退出</a>
		</div>
	</div>
</div>
<div class="new_header navbg02">
	<div class="wrap">
		<a href="/login.do?action=navigateIndex" class="left margr40">
			<img src="images/logo.jpg" alt="畅捷支付" />
		</a>
		<ul class="new_header_nav">
			<li>
				<a href="javascript:;" class="mainnav_a">我的畅捷支付</a>
				<ul>
					<li>
						<a href="/login.do?action=navigateIndex">首页</a>
					</li>
					<!-- <li>
						<a href="/login.do?action=toModifyPassword">修改密码</a>
					</li> -->
				</ul>
			</li>
			<c:forEach var="caidan" items="${menuListNoPathTop}" varStatus="count">
			<li>
				<a class="mainnav_a" href="javascript:void(0);" id="${caidan.menuId}">${caidan.menuName}</a>
				<ul>
					<c:forEach var="ziji" items="${menuListTop}" varStatus="count">
					<c:if test="${caidan.menuId==ziji.menuParentId}">
						<li>
							<a href="${ziji.menuPath}" id="${ziji.menuId}" onclick="clearS()">${ziji.menuName}</a>
						</li>
					</c:if>
					</c:forEach>
				</ul>
			</li>
			</c:forEach>
		</ul>
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
<script type="text/javascript" src="js/common/base_top2.js"></script>