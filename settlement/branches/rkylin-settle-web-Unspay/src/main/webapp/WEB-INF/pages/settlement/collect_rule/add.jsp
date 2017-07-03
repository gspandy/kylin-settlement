<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>新增对账规则</title>
<jsp:include page="../../base/base_page.jsp"></jsp:include>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/settlement/collect_rule/edit.js"></script>
<link rel="stylesheet" type="text/css" media="screen" href="${pageContext.request.contextPath}/js/common/jquery-validate/demo/css/screen.css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/js/common/jquery-validate/jquery.validate.js"></script>
<script type="text/javascript">
$(function() {
	 var validator = $("#dataForm").validate({
		errorPlacement:function(error,element) {
			error.css({"line-height":"28px"});
			element.parent("div").after(error);
	    }
	});
	$('#btnSubmit').click(function() {//提交按钮单击事件
		if(validator.form()){
			formSubmit();
		}
	});
})
</script>
</head>
<body onclick="closeSelect()">
	<div class="area">
		<form name="dataForm" id="dataForm" method="post" action="${pageContext.request.contextPath}/collect_rule/save_ajax">
			<div class="main">
				<div class="blank15"></div>
				<div class="wrap">	
					<div class="con">
						<div class="con_title">
							<h3 class="left fs14 marglr3020">新增汇总规则</h3>
							<div class="attr_con">
								<a href="javascript:void(0);" class="chan_btn right wid93 foc_cur margr40 margt10" id="btnSubmit">保存</a> 
								<a href="javascript:gotoUrl(document.referrer);" class="chan_btn right wid93 margr40 margt10">返回</a> 
							</div>
						</div>
						
						<div class="main_con">
							<div class="con_article_title02 paddtb20">
								<div class="blank10"></div>
								<div class="con_core_info_div11">
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">汇总规则名称：</span>
									</label>
									<div class="input_con_type_top wid190">
										<input type="text" id="profitRuleName" name="profitRuleName" class="wid190" />
									</div>
								</div>
								<div class="con_core_info_div11">
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">机构代码：</span>
										<span class="right fs18 margr8 red01">*</span>	
									</label>
									<div class="input_con_type_top wid190">
										<input type="text" id="rootInstCd" name="rootInstCd" class="wid190 required" title="请输入机构代码" />
									</div>
								</div>
								<div class="blank10"></div>
								<div class="con_core_info_div11">
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">渠道代码：</span>
										<span class="right fs18 margr8 red01">*</span>
									</label>
									<div class="input_con_type_top wid190">
										<input type="text" id="payChannelId" name="payChannelId" class="wid190 required" title="请输入渠道代码" />
									</div>
								</div>
								<div class="con_core_info_div11">
									<label class="label_txt wid130 txtright margt10 margr8" for="detKeyCode">
										<span class="right">功能码：</span>
										<span class="right fs18 margr8 red01">*</span>
									</label>
									<div class="input_con_type_top wid190">
										<input type="text" id="funcCode" name="funcCode" class="wid190 required"  title="请输入功能码" />
									</div>
								</div>
								<div class="blank10"></div>
								<div class="con_core_info_div11" style="padding-left:40px;">
									<label class="label_txt wid140 txtright margt10 margr8" for="accKeyCode">
										<span class="right">会计功能码：</span>
										<span class="right fs18 margr8 red01">*</span>
									</label>
									<div class="input_con_type_top wid190">
										<input type="text" id="kernelFuncCode" name="kernelFuncCode" class="wid190 required" title="请输入会计功能吗" />
									</div>
								</div>
								
								<div class="blank10"></div>
								
								<div class="con_core_info_div11">
									<label class="label_txt wid130 txtright margt10 margr8" for="settleKeyName">
										<span class="right">科目名称1：</span>
										<span class="right fs18 margr8 red01">*</span>
									</label>
									<div class="input_con_type_top wid190">
										<input type="text" id="accountName1" name="accountName1" class="wid190 required" title="请输入科目名称1" />
									</div>
								</div>
								
								<div class="con_core_info_div11">
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">账户ID1：</span>
										<span class="right fs18 margr8 red01">*</span>
									</label>
									<div class="input_con_type_top wid190">
										<input type="text" id="finAccountId1" name="finAccountId1" class="wid190 required" title="请输入账户ID1" />
									</div>
								</div>
								<div class="blank10"></div>
								<div class="con_core_info_div11">
									<label class="label_txt wid130 txtright margt10 margr8" for="settleKeyName">
										<span class="right">科目名称2：</span>
										<span class="right fs18 margr8 red01">*</span>
									</label>
									<div class="input_con_type_top wid190">
										<input type="text" id="accountName2" name="accountName2"  class="wid190 required" title="请输入科目名称2" />
									</div>
								</div>

								<div class="con_core_info_div11">
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">账户ID2：</span>
										<span class="right fs18 margr8 red01">*</span>
									</label>
									<div class="input_con_type_top wid190">
										<input type="text" id="finAccountId2" name="finAccountId2" class="wid190 required" title="请输入账户ID2" />
									</div>
								</div>
								<div class="blank10"></div>
								<div class="con_core_info_div11">
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">扩展字段1：</span>
									</label>
									<div class="input_con_type_top wid190">
										<input type="text" id="obligate1" name="obligate1" class="wid190" />
									</div>
								</div>
								
								<div class="con_core_info_div11">
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">扩展字段2：</span>
									</label>
									<div class="input_con_type_top wid190">
										<input type="text" id="obligate2" name="obligate2" class="wid190" />
									</div>
								</div>
								<div class="blank10"></div>
								<div class="con_core_info_div11">
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">扩展字段3：</span>
									</label>
									<div class="input_con_type_top wid190">
										<input type="text" id="obligate3" name="obligate3" class="wid190" />
									</div>
								</div>
								<div class="blank10"></div>
								<div class="con_core_info_div11">
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">汇总类型：</span>
									</label>
									<div class="input_con_type_top wid190">
										<select style="width:189px;height:28px;" name="collectType">
											<option style="height:25px" value="1" >结算（日切）汇总</option>
											<option style="height:25px" value="2" >对账汇总</option>
										</select>
									</div>
								</div>
								
								<div class="con_core_info_div11">
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">状态：</span>
									</label>
									<div class="input_con_type_top wid190">
										<select style="width:189px;height:28px;" name="statusId">
											<option style="height:25px" value="1" >有效</option>
											<option style="height:25px" value="0" >无效</option>
										</select>
									</div>
								</div>
								<div class="blank10"></div>
								<div class="con_core_info_div11">
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">备注：</span>
									</label>
									<div >
										<textarea style="width:343px; height:111px" name="remark"></textarea>
									</div>
								</div>
								<!-- // 备注 -->
								<div class="blank20"></div>
							</div>		
						</div>
					</div>
				</div>
			</div>
		</form>
	</div>
</body>