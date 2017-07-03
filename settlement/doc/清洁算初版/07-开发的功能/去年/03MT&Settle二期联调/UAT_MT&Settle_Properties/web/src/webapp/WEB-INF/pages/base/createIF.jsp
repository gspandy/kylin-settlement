<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<script type="text/javascript">
	function getheight(){
		var b_height = Math.max(document.body.scrollHeight,document.body.clientHeight); 
	//alert("ww"+b_height);
		document.getElementById("b_iframe").src =  document.getElementById("b_iframe").src+"?b_height="+b_height; 
	}
</script>
<!-- pro
<iframe id="b_iframe"  width="100%"  src="http://cm.rongcapital.cn:8095/agentIframe" style="display:none"> -->
<iframe id="b_iframe"  width="100%"  src="http://uatrsjf.rongcapital.com.cn:8095/agentIframe" style="display:none">
</iframe>
