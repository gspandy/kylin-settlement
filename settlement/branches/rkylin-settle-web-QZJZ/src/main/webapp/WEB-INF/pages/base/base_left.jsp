<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
						<div class="con_aside wid190 cir_left_tb">
							<div class="con_aside_title cir_cornerl">
								<span class="left margl10">我的应用</span>
								<span class="tit_grey"></span>
							</div>
							<div class="blank10 bord22"></div>
							<ul class="con_aside_list2">
								<!-- 资金看板 -->
								<li>
									<span class="icon_cal_small_zjkb"></span>
									<c:if test="${BoardUrl==null||BoardUrl==''}">
										<a href="pages/product_center/Funds_Kanban.jsp" title="资金看板介绍" >资金看板</a>
									</c:if>
									<c:if test="${BoardUrl!=null||BoardUrl!=''}">
									<a href="${BoardUrl}" title="进入资金看板" >资金看板</a>
									</c:if>
								</li>
								<!-- 手机充值 -->
								<li>
									<span class="icon_cal_small_sjcz"></span>
									<c:if test="${sessionScope.security_context.userSession.loginName==null}">
									<a href="pages/product_center/Phone_Recharge.jsp" title="手机充值介绍">手机充值</a>
									</c:if>
									<c:if test="${sessionScope.security_context.userSession.loginName!=null}">
									<a href="common/homeInfoManage.do?action=toPhoneRechargePage" title="进入手机充值">手机充值</a>
									</c:if>
								</li>
								<!-- 在线进销存 -->
								<c:if test="${SMBUrl!=null&&SMBUrl!=''}">
								<li>
									<span class="icon_cal_small_zxjxc"></span>
									<a href="${SMBUrl}" title="进入在线进销存">在线进销存</a>									
								</li>
								</c:if>
								<!-- POS伴侣 -->
								<li style="display: none;">
									<span class="icon_cal_small_pos"></span>
									<a href="pages/product_center/Pos_Companion.jsp" title="POS伴侣">POS伴侣</a>
								</li>
								<!-- 在线支付 -->
								<li style="display: none;">
									<span class="icon_cal_small_zxzf"></span>
									<a href="pages/product_center/Online_Pay.jsp" title="POS伴侣">在线支付</a>
								</li>
							</ul>
						</div>