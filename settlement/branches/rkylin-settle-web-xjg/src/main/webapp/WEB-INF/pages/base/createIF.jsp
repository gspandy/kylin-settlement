<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<script type="text/javascript">
	function getheight(){
		var b_height = Math.max(document.body.scrollHeight,document.body.clientHeight); 
	//alert("ww"+b_height);
		document.getElementById("b_iframe").src =  document.getElementById("b_iframe").src+"?b_height="+b_height; 
	}
</script>
<!-- pro -->
<iframe id="b_iframe"  width="100%"  src="http://cm.rongcapital.cn:8095/agentIframe" style="display:none">
<!-- UAT
<iframe id="b_iframe"  width="100%"  src="http://120.26.104.20:8095/agentIframe" style="display:none"> -->
<!-- test58
<iframe id="b_iframe"  width="100%"  src="http://123.56.79.58:8095/agentIframe" style="display:none"> -->
</iframe>
