<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%!
%><%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    <title>测试 主页</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">

	<%--<link rel="stylesheet" type="text/css" href="styles.css">--%>

  </head>
  
  <body>
  	<h1>业务相关</h1>
	<ul>
		<li>
			<ol>
				<li><a href="<%=path %>/transdetail/openGetAccTraOrdInfo" target="_blank">读取账户系统交易并录入清结算库中</a></li>
				<li><a href="<%=path %>/transdetail/multi_gate_open_view" target="_blank">从多渠道读取交易信息并存入'清算'DB</a></li>
				<li><a href="<%=path %>/transaccount/collate_file_open_view" target="_blank">下载、读取上游对账文件</a></li>
				<li><a href="<%=path %>/collate/open_download_collatefile2merchant" target="_blank">下载 服务器 对账文件</a></li>
				<li><a href="<%=path %>/collate/upload_collate_file_view" target="_blank">上传下游对账文件到ROP</a></li>
				<li><a href="<%=path %>/collate/collate_open_view" target="_blank">对账</a></li>
				<li><a href="<%=path %>/accGenPay/openQueryInfo" target="_blank">根据失败数据生成代付交易（入账前）</a></li>
				<li><a href="<%=path %>/accTranOrderInfo/openQueryInfo" target="_blank">根据失败数据生成代付交易（入账后）</a></li>
				<li><a href="<%=path %>/transdetail/dsf_manager" target="_blank">交易表代收付业务</a></li>
				<li><a href="<%=path %>/transdetail/dsf_manual_summary_or_send" target="_blank">代收付手工触发汇总或发送</a></li>
				<li><a href="<%=path %>/transdetail/collecte_acc_and_multi_query" target="_blank">账户跟多渠道对账</a></li>
				<li><a href="<%=path %>/transdetail/dsf_result_query" target="_blank">从代收付系统读取结果并入清结算库</a></li>
				<li><a href="<%=path %>/my_download/download" target="_blank">我的下载</a></li>
				<li><a href="<%=path %>/posdetail/order_open_view" target="_blank">pos-读取交易</a></li>
				<li><a href="<%=path %>/collate/collateForPos_open_view" target="_blank">pos-对账</a></li>
				<li><a href="<%=path %>/collate/open_download_from_rop" target="_blank">pos-从ROP下载对账文件</a></li>
			</ol>
		</li>
	</ul>
	<h1>交易信息相关</h1>
    	<ul>
    		<li>
    			<ol>
    				<li>
    					<a href="<%=path %>/loandetail/loan_manager" target="_blank">还款信息表 settle_loan_detail</a>
    				</li>
    				<li>
    					<a href="<%=path %>/profit_invoice/profit_invoice_manager" target="_blank">分润结算表 settle_profit_invoice</a>
    				</li>
    				<li>
    					<a href="<%=path %>/entry/entry_manager" target="_blank">交易信息结果表 settle_balance_entry</a>
    				</li>
    				<li>
    					<a href="<%=path %>/settle_kernel_entry/settle_kernel_entry_manager" target="_blank">汇总信息表 settle_kernel_entry</a>
    				</li>
    				<li>
    					<a href="<%=path %>/transaccount/account_manager" target="_blank">交易信息表 settle_trans_account</a>
    				</li>
    				<li>
    					<a href="<%=path %>/transdetail/detail_manager" target="_blank">交易信息表 settle_trans_detail</a>
    				</li>
    				<li>
    					<a href="<%=path %>/transdetail/detail_manager_settler" target="_blank">交易信息表 (业务按钮) settle_trans_detail</a>
    				</li>
    				<li>
    					<a href="<%=path %>/transbill/bill_manager" target="_blank">交易信息挂账表 settle_trans_bill</a>
    				</li>
    				<li>
    					<a href="<%=path %>/transprofit/profit_manager" target="_blank">交易信息清分表 settle_trans_profit</a>
    				</li>
    				<li>
    					<a href="<%=path %>/invoice/invoice_manager" target="_blank">代收付结算表 settle_trans_invoice</a>
    				</li>
    				<li>
    					<a href="<%=path %>/summary/summary_manager" target="_blank">代收付汇总表 settle_trans_summary</a>
    				</li>
    			</ol>
    		</li>
    	</ul>
    	<h1>配置信息相关</h1>
    	<ul>
    		<li>
    			<ol>
    				<li>
    					<a href="<%=path %>/file/file_manager" target="_blank">文件信息表 settle_file 查询</a>
    				</li>
    				<li>
    					<a href="<%=path %>/file/file_open_add" target="_blank">文件信息表 settle_file 添加</a>
    				</li>
    				<li>
    					<a href="<%=path %>/filecolumn/column_manager" target="_blank">文件列信息表 settle_file_column 查询</a>
    				</li>
    				<li>
    					<a href="<%=path %>/filecolumn/column_open_add" target="_blank">文件列信息表 settle_file_column 添加</a>
    				</li>
    				<li>
    					<a href="<%=path %>/profitkey/key_manager" target="_blank">清分规则信息表 settle_profit_key 查询</a>
    				</li>
    				<li>
    					<a href="<%=path %>/profitkey/key_open_add" target="_blank">清分规则信息表 settle_profit_key 添加</a>
    				</li>
    				<li>
    					<a href="<%=path %>/profitrule/rule_manager" target="_blank">清分规则详细信息表 settle_profit_rule 查询</a>
    				</li>
    				<li>
    					<a href="<%=path %>/profitrule/rule_open_add" target="_blank">清分规则详细信息表 settle_profit_rule 添加</a>
    				</li>
    				<li>
    					<a href="<%=path %>/rule/rule_manager" target="_blank">对账规则信息表 settle_rule 查询</a>
    				</li>
    				<li>
    					<a href="<%=path %>/rule/rule_open_add" target="_blank">对账规则信息表 settle_rule 添加</a>
    				</li>
    				<li>
    					<a href="<%=path %>/parameterinfo/query_manager" target="_blank">系统参数信息表 settle_parameter_info 查询</a>
    				</li>
    				<li>
    					<a href="<%=path %>/collect_rule/rule_manager" target="_blank">对账规则信息表 settle_collect_rule 查询</a>
    				</li>
    				<li>
    					<a href="<%=path %>/collect_rule/rule_open_add" target="_blank">对账规则信息表 settle_collect_rule 添加</a>
    				</li>
    			</ol>
    		</li>
    	</ul>
    	<h1>配置信息分配相关</h1>
    	<ul>
    		<li>
    			<ol>
    				<li>
    					<a href="<%=path %>/profitmatch/match_manager" target="_blank">清分规则-明细分配</a>
    				</li>
    				<li>
    					<a href="<%=path %>/filematch/match_manager" target="_blank">文件-文件列分配</a>
    				</li>
    				<li>
    					<a href="<%=path %>/config/open_refresh_config" target="_blank">刷新-清结算配置信息</a>
    				</li>
    			</ol>
    		</li>
    	</ul>
  </body>
</html>
