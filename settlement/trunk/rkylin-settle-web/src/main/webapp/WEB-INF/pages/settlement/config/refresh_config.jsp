<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>清结算业务规则配置</title>
<style type="text/css">
   .divhide {display:none}
</style>
<jsp:include page="../../base/base_page.jsp"/>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/"></script>
</head>
<div class="area">
		<input type="hidden" id="readType" name="readType" />
		<div class="main">
			<div class="blank15"></div>
			<div class="wrap">
				<div class="con">
					<div class="con_title">
						<h3 class="left fs14 marglr3020">清结算业务规则配置</h3>
						<span class="grey"></span>
					</div>
					<div class="main_con" style="display: ">
						<div class="con_core_info_main wid1125 paddl10 margt10" style="width:1225px;">
							<ul class="table_menu">
								<li>
									<a class="left chan_btn wid100 margl10 margt5" id="reflash_all" href="javascript:void(0);">刷新选中</a>
								</li>
							</ul>
							<table class="chan_table tb_bord01" id="user_table1">
								<tr>
									<th width="7%">
									<input type="checkbox" id="all_check_box" class="ids_box" value="profit_rule" />
									序号
									</th>
									<th>规则名称</th>
									<th>规则描述</th>
									<th width="7%">操作</th>
								</tr>
								<tr>
									<td>
									<input type="checkbox" class="ids_box" value="profit_rule" name="ids" />
									1
									</td>
									<td>分润规则</td>
									<td>暂无描述</td>
									<td><a href="javascript:doRefreshProfitRule();">刷新</a></td>
								</tr>
								<tr>
									<td>
									<input type="checkbox" class="ids_box" value="order_no" name="ids" />
									2
									</td>
									<td>订单号对应关系规则</td>
									<td>暂无描述</td>
									<td><a href="javascript:doRefreshOrderNo();">刷新</a></td>
								</tr>
								<tr>
									<td>
									<input type="checkbox" class="ids_box" value="amount" name="ids" />
									3
									</td>
									<td>金额对应关系规则</td>
									<td>暂无描述</td>
									<td><a href="javascript:doRefreshAmount();">刷新</a></td>
								</tr>
								<tr>
									<td>
									<input type="checkbox" class="ids_box" value="func_code" name="ids" />
									4
									</td>
									<td>功能编码对应关系规则</td>
									<td>暂无描述</td>
									<td><a href="javascript:doRefreshFuncCodeRule();">刷新</a></td>
								</tr>
								<tr>
									<td>
									<input type="checkbox" class="ids_box" value="func_code" name="ids" />
									5
									</td>
									<td>渠道名称（channelHome & payChannelId）和渠道编码对应关系</td>
									<td>暂无描述</td>
									<td><a href="javascript:doRefreshMultiGateChannelHome2PayChannelId();">刷新</a></td>
								</tr>
								<tr>
									<td>
									<input type="checkbox" class="ids_box" value="func_code" name="ids" />
									6
									</td>
									<td>'func_code'与deal_product_code的对应关系</td>
									<td>暂无描述</td>
									<td><a href="javascript:doRefreshDealProductCodeTofuncCode();">刷新</a></td>
								</tr>
							</table>
							<script type="text/javascript">
								$(function() {
									$('#all_check_box').click(function() {checkAll();});
									$('#reflash_all').click(function() {doCheckedFun();});
								});
								/**
								执行选中方法
								*/
								function doCheckedFun() {
									if($('input[name=ids]:checked').length < 1) {
										art.dialog.tips("<span style='color:red'>请选择刷新操作!</span>", 2.5, function(){});
									}
									
									$('input[name=ids]:checked').each(function() {
										var href = $(this).parent("td").siblings("td:last").children("a").attr("href");
										var funName = href.replace("javascript:", "");
										eval(funName);
									});
								}
								/**
								全选/反选
								*/
								function checkAll() {
									var isChecked = document.getElementById("all_check_box").checked;
									var checkArr = document.getElementsByName('ids');
									for(var i = 0; i < checkArr.length; i ++) {
										document.getElementsByName('ids')[i].checked = isChecked;
									}
								}
								/**
								业务方法
								*/
								function doRefreshProfitRule() {
									$.get('${pageContext.request.contextPath}/config/refresh_profit_rule',
											null,
											function(data) {art.dialog.tips("<span>"+ data +"</span>", 2.5, function(){});},
											'text'
										);
								}
								function doRefreshOrderNo() {
									$.get('${pageContext.request.contextPath}/config/refresh_order_no',
											null,
											function(data) {art.dialog.tips("<span>"+ data +"</span>", 2.5, function(){});},
											'text'
										);
								}
								function doRefreshAmount() {
									$.get('${pageContext.request.contextPath}/config/refresh_amount',
											null,
											function(data) {art.dialog.tips("<span>"+ data +"</span>", 2.5, function(){});},
											'text'
										);
								}
								function doRefreshFuncCodeRule() {
									$.get('${pageContext.request.contextPath}/config/refresh_func_code',
											null,
											function(data) {art.dialog.tips("<span>"+ data +"</span>", 2.5, function(){});},
											'text'
										);
								}
								function doRefreshMultiGateChannelHome2PayChannelId() {
									$.get('${pageContext.request.contextPath}/config/refresh_multigatechannelhome_2_paychannelid',
											null,
											function(data) {art.dialog.tips("<span>"+ data +"</span>", 2.5, function(){});},
											'text'
										);
								}
								function doRefreshDealProductCodeTofuncCode() {
									$.get('${pageContext.request.contextPath}/config/refresh_deal_product_code_to_funccode',
											null,
											function(data) {art.dialog.tips("<span>"+ data +"</span>", 2.5, function(){});},
											'text'
										);
								}
							</script>
							<div class="left wid500 paddl60 margt10 rel_pos"
							style="height:42px;"></div>
						</div>
					</div>
				</div>
			</div>
		</div>
</div>
</body>
</html>