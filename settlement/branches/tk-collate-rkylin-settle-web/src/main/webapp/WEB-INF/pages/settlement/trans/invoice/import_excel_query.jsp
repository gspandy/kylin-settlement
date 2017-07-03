
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

<head>

<title>结算表导入excel文件</title>
<jsp:include page="../../../base/base_page.jsp"></jsp:include>

<style type="text/css">
</style>

</head>
<body style="padding: 10px;">
	<input type="hidden" id="url" name="url" value="${pageContext.request.contextPath}/invoice/importExcel"/>
	<form id="f_form">
		<div id="editForm1" align="center" style="margin-top: 20px">
				<table border="0">
					<tr>
						<td align="right" valign="top"> 选择导入文件：</td>
						<td align="left"  valign="top">
							<table border="0">
								<tr>
									<td valign="top"><input type="file" name="uploadFile" id="uploadFile"/></td>
									<td width="100"><a id="import"  href= '${pageContext.request.contextPath}/invoice/down_excel_model'>下载模板</a></td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</div>
			<br/>
			<div style="margin-left:700px;">
			 <input type="button" value="提交" onclick="doUpload()"/>
			</div>
	</form>
	
	
  <script type="text/javascript">
	  function doUpload() {  
		     var formData = new FormData($( "#f_form" )[0]);  
		     $.ajax({  
		          url: $("#url").val() ,  
		          type: 'POST',  
		          data: formData,  
		          async: false,  
		          cache: false,  
		          contentType: false,  
		          processData: false,  
		          success: function (returndata) {  
		        	  if("0" == returndata){
		        		  alert("导入失败，数据重复！！！");  
		        	  }else if("1" == returndata){
		        		  alert("导入成功");  
		        	  }else{
		        		  alert("发现重复数据！ userId:"+returndata);  
		        	  }
		          },  
		          error: function (returndata) {  
		              alert("导入excel发生异常！！！！");  
		          }  
		     });  
		}  
  </script>
</body>


</html>
