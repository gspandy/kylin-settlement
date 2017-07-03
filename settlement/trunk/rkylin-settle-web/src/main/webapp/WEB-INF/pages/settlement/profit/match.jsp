<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>清分规则-详细分配信息</title>

<jsp:include page="../../base/base_page.jsp"/>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/settlement/profit/matchQuery.js"></script>
</head>
<div class="area">
	<div class="main">
		<div class="blank15"></div>
		<div class="wrap">
			<div class="con">
				<div class="con_title">
					<h3 class="left fs14 marglr3020">清分规则-详细分配信息</h3>
					<span class="grey"></span>
				</div>
				<input type="hidden" id="profitDetailId"/>
				<form name="dataForm" id="dataForm" method="post" action="${pageContext.request.contextPath}/profitkey/query_ajax">
					<div class="con_aside wid400 bord03">
						<div class="main_con">
							<div class="con_core_info_main wid400">
								<ul class="table_menu">
									<li style="float:none;">
										<h3 class="left fs14 marglr3020">清分规则信息</h3>
										<a class="right chan_btn wid100 margr10 margt5" id="addBtn" href="profitkey/key_open_add">新增清分规则</a>
									</li>
								</ul>
								<table class="chan_table tb_bord01" id="user_table1">
								</table>
							</div>
							<div class="left wid400 paddl60 margt10 rel_pos" style="height:42px;display:none;">
								<label class="label_txt wid60 txtright margr8 margt11">
									每页显示 </label>
								<div class="input_con_type_top wid95 margt4">
									<select style="width:94px;height:27px" name="pageSize" id="pageSize">
										<option style="height:25px" value="10">10</option>
										<option style="height:25px" value="50">50</option>
										<option style="height:25px" value="100">100</option>
									</select>
								</div>
								<label class="label_txt wid60 txtleft margl6 margt11">
									条记录 </label> 
									<input id="saveSelectData" type="hidden" value='${sessionDetail }' name="saveSelectData"/>
							</div>
							<div id="div_table_page" class="chan_table_page_wrap wid400 margt10 paddr36">
								<div class="chan_table_page"></div>
							</div>
						</div>
					</div>
					<!-- 分页隐藏域 -->
					<input type="hidden" id="pageIndex" name="pageIndex" value="1" /> 
					<input type="hidden" id="pageSize" name="pageSize" value="10" />
					<!-- // 分页隐藏域 -->
				</form>
				<div class="con_aside wid800 bord03 margl20">
					<div class="main_con">
						<div class="con_core_info_main wid800">
							<ul class="table_menu">
								<li style="float:none;">
									<h3 class="left fs14 marglr3020">规则详细信息</h3>
									<a class="right chan_btn wid160 margr10 margt5" href="profitrule/rule_open_add">新增清分规则明细信息</a>
									<a class="right chan_btn wid100 margr10 margt5" id="matchBtn">分配</a>
								</li>
							</ul>
							<table class="chan_table tb_bord01" id="user_table2">
								<tr>
									<th width="5%"></th>
									<th width="10%">明细ID</th>
									<th width="10%">子ID</th>
									<th width="10%">分润对象形态</th>
									<th width="10%">管理机构代码</th>
									<th width="10%">产品号</th>
									<th width="15%">分润对象</th>
									<th width="10%">分润模式</th>
									<th width="10%">分润内容</th>
									<th width="5%">状态</th>
									<th width="5%">操作</th>
								</tr>
								<tr><td colspan='11'>没有符合条件的数据</td></tr>
							</table>
						</div>
					</div>
				</div>
				<div class="blank15"></div>
			</div>
		</div>
	</div>
</div>
</body>
</html>