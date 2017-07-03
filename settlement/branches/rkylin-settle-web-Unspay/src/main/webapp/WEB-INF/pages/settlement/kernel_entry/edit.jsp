<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>修改汇总信息</title>
<jsp:include page="../../base/base_page.jsp"></jsp:include>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/settlement/kernel_entry/edit.js"></script>
</head>
<body onclick="closeSelect()">
	<div class="area">
		<form name="dataForm" id="dataForm" method="post" action="${pageContext.request.contextPath}/settle_kernel_entry/edit_ajax">
			<input type="hidden" value="no" id="isAdd">
			<div class="main">
				<div class="blank15"></div>
				<div class="wrap">	
					<div class="con">
						<div class="con_title">
							<h3 class="left fs14 marglr3020">修改汇总信息</h3>
							<div class="attr_con">
								<a href="javascript:void(0);" class="chan_btn right wid93 foc_cur margr40 margt10" id="btnSubmit">保存</a> 
								<a href="javascript:gotoUrl(document.referrer);" class="chan_btn right wid93 margr40 margt10">返回</a> 
							</div>
						</div>
						
						<div class="main_con">
							<div class="con_article_title02 paddtb20">
								<!-- 主键ID、支付渠道ID -->
								<div class="blank10"></div>
								<div class="con_core_info_div05">
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">主键ID：</span>
									</label>
									<label class="label_txt wid190 margt11">
										${settleKernelEntry.id}
										<input type="hidden" value="${settleKernelEntry.id}" name="id" />
									</label>
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">会计交易流水号ID：</span>
									</label>
									<label class="label_txt wid190 margt11">
										${settleKernelEntry.transEntrySaId}
									</label>
								</div>
								
								<div class="blank10"></div>
								<div class="con_core_info_div05">
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">交易日期：</span>
									</label>
									<label class="label_txt wid190 margt11">
										<fmt:formatDate value="${settleKernelEntry.transDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
									</label>
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">交易记录来源：</span>
									</label>
									<label class="label_txt wid190 margt11">
										${settleKernelEntry.requestIdFrom}
									</label>
								</div>
							
								<div class="blank10"></div>
								<div class="con_core_info_div05">
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">交易流水号：</span>
									</label>
									<label class="label_txt wid190 margt11">
										${settleKernelEntry.transId}
									</label>
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">交易流水条数：</span>
									</label>
									<label class="label_txt wid190 margt11">
										${settleKernelEntry.transNumber}
									</label>
								</div>
								
								<div class="blank10"></div>
								<div class="con_core_info_div05">
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">流水序号：</span>
									</label>
									<label class="label_txt wid190 margt11">
										${settleKernelEntry.transNo}
									</label>
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">基础功能码：</span>
									</label>
									<label class="label_txt wid190 margt11">
										${settleKernelEntry.funcCode}
									</label>
								</div>
								
								<div class="blank10"></div>
								<div class="con_core_info_div05">
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">账户ID1：</span>
									</label>
									<label class="label_txt wid190 margt11">
										${settleKernelEntry.finAccountId1}
									</label>
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">发生额1：</span>
									</label>
									<label class="label_txt wid190 margt11">
										${settleKernelEntry.paymentAmount1}
									</label>
								</div>
								
								<div class="blank10"></div>
								<div class="con_core_info_div05">
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">币种1：</span>
									</label>
									<label class="label_txt wid190 margt11">
										${settleKernelEntry.currency1}
									</label>
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">账户ID2：</span>
									</label>
									<label class="label_txt wid190 margt11">
										${settleKernelEntry.finAccountId2}
									</label>
								</div>
							
								<div class="blank10"></div>
								<div class="con_core_info_div05">
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">发生额2：</span>
									</label>
									<label class="label_txt wid190 margt11">
										${settleKernelEntry.paymentAmount2}
									</label>
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">币种2：</span>
									</label>
									<label class="label_txt wid190 margt11">
										${settleKernelEntry.currency2}
									</label>
								</div>
								
								<div class="blank10"></div>
								<div class="con_core_info_div05">
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">分录状态：</span>
									</label>
									<label class="label_txt wid190 margt11">
										${settleKernelEntry.accountingStatus}
									</label>
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">冲正流水号：</span>
									</label>
									<label class="label_txt wid190 margt11">
										${settleKernelEntry.reverseNumber}
									</label>
								</div>
								
								<div class="blank10"></div>
								<div class="con_core_info_div05">
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">套录号：</span>
									</label>
									<label class="label_txt wid190 margt11">
										${settleKernelEntry.referEntryId}
									</label>
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">账期：</span>
									</label>
									<label class="label_txt wid190 margt11">
										<fmt:formatDate value="${settleKernelEntry.accountDate}"  pattern="yyyy-MM-dd"/>
									</label>
								</div>
								
								<div class="blank10"></div>
								<div class="con_core_info_div05">
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">备注1：</span>
									</label>
									<label class="label_txt wid190 margt11">
										${settleKernelEntry.remark1}
									</label>
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">机构号：</span>
									</label>
									<label class="label_txt wid190 margt11">
										${settleKernelEntry.rootInstCd}
									</label>
								</div>
							
								<div class="blank10"></div>
								<div class="con_core_info_div05">
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">业务种类编码1：</span>
									</label>
									<label class="label_txt wid190 margt11">
										${settleKernelEntry.busiTypeCode}
									</label>
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">商户号：</span>
									</label>
									<label class="label_txt wid190 margt11">
										${settleKernelEntry.merchantNo}
									</label>
								</div>
								
								<div class="blank10"></div>
								<div class="con_core_info_div05">
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">账号：</span>
									</label>
									<label class="label_txt wid190 margt11">
										${settleKernelEntry.accountNo}
									</label>
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">结算类型：</span>
									</label>
									<label class="label_txt wid190 margt11">
										${settleKernelEntry.settleType}
									</label>
								</div>
								
								<div class="blank10"></div>
								<div class="con_core_info_div05">
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">付款类型：</span>
									</label>
									<label class="label_txt wid190 margt11">
										${settleKernelEntry.payType}
									</label>
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">结算周期：</span>
									</label>
									<label class="label_txt wid190 margt11">
										${settleKernelEntry.settleCycle} 
									</label>
								</div>
								
								<div class="blank10"></div>
								<div class="con_core_info_div05">
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">T + N：</span>
									</label>
									<label class="label_txt wid190 margt11">
										${settleKernelEntry.tn}
									</label>
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">结算日：</span>
									</label>
									<label class="label_txt wid190 margt11">
										<fmt:formatDate value="${settleKernelEntry.settleDay}" pattern="yyyy-MM-dd HH:mm:ss"/>
									</label>
								</div>
								
								<div class="blank10"></div>
								<div class="con_core_info_div05">
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">实际结算日：</span>
									</label>
									<label class="label_txt wid190 margt11">
										<fmt:formatDate value="${settleKernelEntry.actualSettleDay}" pattern="yyyy-MM-dd HH:mm:ss"/>
									</label>
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">结算单号：</span>
									</label>
									<label class="label_txt wid190 margt11">
										${settleKernelEntry.settleNo}
									</label>
								</div>
								
								<div class="blank10"></div>
								<div class="con_core_info_div05">
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">备注2：</span>
									</label>
									<label class="label_txt wid190 margt11">
										${settleKernelEntry.remark2}
									</label>
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">清算状态ID：</span>
									</label>
									<label class="label_txt wid190 margt11">
										${settleKernelEntry.statusId}
									</label>
								</div>
								
								<div class="blank10"></div>
								<div class="con_core_info_div05">
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">会计系统状态ID：</span>
									</label>
									<label class="label_txt wid190 margt11">
										${settleKernelEntry.readStatusId}
									</label>
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">响应信息：</span>
									</label>
									<label class="label_txt wid190 margt11">
										${settleKernelEntry.rsMsg}
									</label>
								</div>
								<!--  -->
								<div class="blank10"></div>
								<div class="con_core_info_div05">
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">预留1：</span>
									</label>
									<label class="label_txt wid190 margt11">
										${settleKernelEntry.obligate1}
									</label>
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">预留2：</span>
									</label>
									<label class="label_txt wid190 margt11">
										${settleKernelEntry.obligate2}
									</label>
								</div>
								<div class="blank10"></div>
								<div class="con_core_info_div05">
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">创建时间：</span>
									</label>
									<label class="label_txt wid190 margt11">
										<fmt:formatDate value="${settleKernelEntry.createdTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
									</label>
								</div>
								<div class="blank10"></div>
								<div class="con_core_info_div05">
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">修改时间：</span>
									</label>
									<label class="label_txt wid190 margt11">
										<fmt:formatDate value="${settleKernelEntry.updatedTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
									</label>
								</div>
								<div class="blank10"></div>
								<div class="con_core_info_div05">
									<label class="label_txt wid130 txtright margt10 margr8">
										<span class="right">状态：</span>
									</label>
									<div class="input_con_type_top wid190 margt4">
										<select style="width:189px;height:28px;" name="statusId">
											<option style="height:25px" value="1" ${(settleKernelEntry.statusId==1)?'selected':''}>成功</option>
											<option style="height:25px" value="0" ${(settleKernelEntry.statusId==0)?'selected':''}>失败</option>
											<option style="height:25px" value="-1" ${(settleKernelEntry.statusId==-1)?'selected':''}>不发送</option>
										</select>
									</div>
								</div>
								<!-- // 状态 -->	
							</div>		
						</div>
					</div>
				</div>
			</div>
		</form>
	</div>
</body>