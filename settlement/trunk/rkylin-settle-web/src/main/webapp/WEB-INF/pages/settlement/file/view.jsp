<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>文件信息详情</title>
<jsp:include page="../../base/base_page.jsp"></jsp:include>
</head>
<body onclick="closeSelect()">
	<div class="area">
		<input type="hidden" value="no" id="isAdd">
		<div class="main">
			<div class="blank15"></div>
			<div class="wrap">	
				<div class="con">
					<div class="con_title">
						<h3 class="left fs14 marglr3020">文件信息详情</h3>
						<div class="attr_con">
							<a href="javascript:gotoUrl(document.referrer);" class="chan_btn right wid93 margr40 margt10" id="btnReturn">返回</a> 
						</div>
					</div>
					
					<div class="main_con">
						<div class="con_article_title02 paddtb20">
							<!-- 规则ID、文件模板名称 -->
							<div class="blank10"></div>
							<div class="con_core_info_div05">
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">规则ID：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleFile.fileId}
								</label>
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">文件模板名称：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleFile.fileName}
								</label>
							</div>
							<!-- // 规则ID、文件模板名称 -->
							<!-- 管理机构代码、功能编码 -->
							<div class="blank10"></div>
							<div class="con_core_info_div05">
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">管理机构代码：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleFile.rootInstCd}
								</label>
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">功能编码：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleFile.funcCodes}
								</label>
							</div>
							<!-- // 管理机构代码、功能编码 -->
							<!-- 功能码关系、支付渠道 -->
							<div class="blank10"></div>
							<div class="con_core_info_div05">
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">功能码关系：</span>
								</label>
								<label class="label_txt wid190 margt11">
									<c:choose>
										<c:when test="${settleFile.funcRelation==0}">全部</c:when>
										<c:when test="${settleFile.funcRelation==1}">包含</c:when>
										<c:when test="${settleFile.funcRelation==2}">差集</c:when>
									</c:choose>
								</label>
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">支付渠道：</span>
								</label>
								<label class="label_txt wid190 margt11">
									<c:choose>
										<c:when test="${settleFile.payChannelId=='01'}">通联</c:when>
										<c:when test="${settleFile.payChannelId=='04'}">连连</c:when>
										<c:when test="${settleFile.payChannelId=='05'}">联动优势</c:when>
										<c:when test="${settleFile.payChannelId=='S01'}">畅捷</c:when>
									</c:choose>
								</label>
							</div>
							<!-- // 功能码关系、支付渠道 -->
							<!-- 读入交易类型、交易状态/HEAD行数 -->
							<div class="blank10"></div>
							<div class="con_core_info_div05" style="width:1100px;">
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">读入交易类型：</span>
								</label>
								<label class="label_txt wid190 margt11">
									<c:choose>
										<c:when test="${settleFile.readType=='01'}">网关支付</c:when>
										<c:when test="${settleFile.readType=='02'}">代收付</c:when>
									</c:choose>
								</label>
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">交易状态/HEAD行数：</span>
								</label>
									<label class="label_txt wid130 margt11">
										${settleFile.transStatusIds}
									</label>
									<span class="label_txt wid450 margt11 red01">
										写入时:[此文件显示的是交易信息'状态', 多个状态用','隔开]; 读取时:[对账文件HEAD行数]
									</span>
							</div>
							<!-- // 读入交易类型、交易状态/HEAD行数 -->
							<!-- 文件名称前缀、文件后缀 -->
							<div class="blank10"></div>
							<div class="con_core_info_div05">
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">文件名称前缀：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleFile.filePrefix}
								</label>
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">文件后缀：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleFile.filePostfix}
								</label>
							</div>
							<!-- // 文件名称前缀、文件后缀 -->
							<!-- 文件类型、文件作用 -->
							<div class="blank10"></div>
							<div class="con_core_info_div05">
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">文件类型：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleFile.fileType}
								</label>
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">文件作用：</span>
								</label>
								<label class="label_txt wid190 margt11">
									<c:choose>
										<c:when test="${settleFile.fileActive==1}">写入信息文件模板</c:when>
										<c:when test="${settleFile.fileActive==2}">读取信息文件模板</c:when>
									</c:choose>
								</label>
							</div>
							<!-- // 文件类型、文件作用 -->
							<!-- 文件列信息码、文件编码 -->
							<div class="blank10"></div>
							<div class="con_core_info_div05">
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">文件列信息码：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleFile.fileSubId}
								</label>
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">文件编码：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleFile.fileEncode}
								</label>
							</div>
							<!-- // 文件列信息码、文件编码 -->
							<!-- 信息分隔符、上传文件密钥名称 -->
							<div class="blank10"></div>
							<div class="con_core_info_div05">
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">信息分隔符：</span>
								</label>
								<label class="label_txt wid190 margt11">
									<c:choose>
										<c:when test="${settleFile.splitStr==' '}">空格</c:when>
										<c:otherwise>${settleFile.splitStr}</c:otherwise>
									</c:choose>
								</label>
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">上传文件密钥名称：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleFile.uploadKeyName}
								</label>
							</div>
							<!-- // 信息分隔符、上传文件密钥名称 -->
							<!-- 上传文件密钥/是否入库 -->
							<div class="blank10"></div>
							<div class="con_core_info_div05">
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">密钥类型/是否入库：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleFile.uploadKeyFlg}
								</label>
								<label class="label_txt wid500 margt11 red01">
									写入时:[上传文件 所用的密钥类型 私钥1 公钥0]; 读取时[是否录入数据库0:不录入,1:录入]
								</label>
							</div>
							<!-- // 上传文件密钥/是否入库 -->
							<!-- rop文件批次号、状态 -->
							<div class="blank10"></div>
							<div class="con_core_info_div05">
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">rop文件批次号：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleFile.ropBatchNo}
								</label>
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">状态：</span>
								</label>
								<label class="label_txt wid190 margt11">
									<c:choose>
										<c:when test="${settleFile.statusId==0}">无效</c:when>
										<c:when test="${settleFile.statusId==1}">有效</c:when>
									</c:choose>
								</label>
							</div>
							<div class="blank10"></div>
							<div class="con_core_info_div05">
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">rop文件类型：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleFile.ropFileType}
								</label>
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">账期STEP：</span>
								</label>
								<label class="label_txt wid190 margt11">
									${settleFile.dateStep}
								</label>
							</div>
							<!-- // rop文件批次号、状态 -->
							<!-- 生效时间、失效时间 -->
							<div class="blank10"></div>
							<div class="con_core_info_div05">
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">生效时间：</span>
								</label>
								<label class="label_txt wid190 margt11">
									<fmt:formatDate value="${settleFile.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
								</label>
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">失效时间：</span>
								</label>
								<label class="label_txt wid190 margt11">
									<fmt:formatDate value="${settleFile.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
								</label>
							</div>
							<!-- // 生效时间、失效时间  -->
							<!-- 创建时间、更新时间 -->
							<div class="blank10"></div>
							<div class="con_core_info_div05">
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">创建时间：</span>
								</label>
								<label class="label_txt wid190 margt11">
									<fmt:formatDate value="${settleFile.createdTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
								</label>
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">更新时间：</span>
								</label>
								<label class="label_txt wid190 margt11">
									<fmt:formatDate value="${settleFile.updatedTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
								</label>
							</div>
							<!-- // 创建时间、更新时间 -->
							<!-- 备注 -->
							<div class="blank10"></div>
							<div class="con_core_info_div05">
								<label class="label_txt wid130 txtright margt10 margr8">
									<span class="right">备注：</span>
								</label>
								<label style="width:343px; height:111px">${settleFile.remark}</label>
							</div>
							<!-- // 备注 -->
							<div class="blank20"></div>
						</div>		
					</div>
				</div>
			</div>
		</div>
	</div>
</body>