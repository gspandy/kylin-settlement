<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
	
<div class="footer">
	<div class="wrap">
		<p class="footer_info">
			Copyright © 2013 畅捷支付&nbsp;&nbsp;&nbsp;&nbsp; 京ICP证130428号
			&nbsp;&nbsp;&nbsp;&nbsp; 京公网安备11010802012596号
		</p>
	</div>
</div>
<div id="lightboxOverlay" style="display: none;"></div>
<div class="lightbox" id="add_menu" style="display: none; top: 20%;">
	<div class="chan_pop_div wid632">
		<div class="chan_pop_title">
			<b class="left paddl20">运营常用功能设置</b>
			<a href="javascript:;" class="right icon_btn_close2 margtr1516" onclick="closePop('#add_menu')"> </a>
		</div>
		<div class="chan_pop_con">
			<div class="blank30">
			</div>
			<div class="pop_con_div">
				<div class="con_core_info_div">
					<div class="set_menu">
					    <div class="set_menu_title margb10"><b class="left">所有系统功能</b></div>
					    <div class="set_menu_ul">
					        <%--<jsp:include page="../other/quickMenuTree.jsp"></jsp:include>
					    --%></div>
					    <span class="set_menu_title margt10 red01">注：最多可设置10个常用系统功能</span>
					</div>
					<a href="javascript:;" class="left icon_arrow_left margt110"></a>
					<div class="set_menu">
					    <div class="set_menu_title margb10"><b class="left">常用系统功能</b><a href="javascript:;" class="right red01">移除全部</a></div>
					    <ul class="set_menu_ul ulright">
					        <li><span class="left paddl20">商户入网审核</span><a href="javascript:;" class="icon_btn_close margt10 margr20"></a></li>
					    </ul>
					</div>
				</div>				
				<div class="bord11 margtb20">
				</div>
				<div class="con_core_info_div">
					<div class="attr_con">
						<a href="javascript:;" class="chan_btn02 wid93 margr200"
							onClick="closePop('#add_menu')">关闭</a>
					</div>
					<div class="attr_con">
						<a href="javascript:;" class="chan_btn02 wid93 margr40"
							onClick="">保存</a>
					</div>
				</div>
			</div>
			<div class="blank10">
			</div>
		</div>
	</div>
</div>
<div class="lightbox" id="delete_win" style="display: none;z-index:1000000">
	<div class="chan_pop_div wid500">
		<div class="chan_pop_title">
			<b class="left paddl20">提示</b>
			<a href="javascript:;" class="right icon_btn_close2 margtr1516" onclick="closePop1('#delete_win')" id="closeWinBtn"> </a>
		</div>
		<div class="chan_pop_con">
			<div class="blank30">
			</div>
			<div class="pop_con_div">				
				<div class="con_core_info_div">
				    <span class="left icon_warning margt5 margr8"></span>
					<label class="label_txt wid400 margt10" id="confirm_message">
						
					</label>
				</div>
				<div class="bord11 margtb20">
				</div>
				<div class="con_core_info_div03" id="alertButton">
					<div class="attr_con">
						<a href="javascript:;" class="chan_btn02 wid93 margr200" id="confirm_cancel"
							onClick="">取消</a>
					</div>
					<div class="attr_con">
						<a href="javascript:;" class="chan_btn02 wid93 margr40" id="confirm_ok"
							onClick="">删除</a>
					</div>
				</div>
			</div>
			<div class="blank10">
			</div>
		</div>
	</div>
</div>
<%--<div id="lightboxOverlay"
			style="width: 100%; height: 100%; display: none;"></div>
--%><script type="text/javascript">
        
	function addMenu() {
	    var heig = $(document).height() + "px";
		var screenH = screen.height;
		var yScroll;
		//取滚动条高度
		if (self.pageYOffset) {
			yScroll = self.pageYOffset;
		} else if (document.documentElement
				&& document.documentElement.scrollTop) {
			yScroll = document.documentElement.scrollTop;
		} else if (document.body) {
			yScroll = document.body.scrollTop;
		}		
		$("#lightboxOverlay").css( {
			width : "100%",
			height : heig,
			display : "block"
		});
		$("#add_menu").attr("style", "display: block;");
		$("#add_menu").css(
				"top",
				(screenH / 2 - 40 - $("#add_menu").height() / 2 + yScroll)
						+ "px");
	}
	
	function closePop1(obj){
	   var moreLight = false;
		$(".lightbox").each(function(){
			if($(this).css("display") == "block"&& "#"+$(this).attr("id") != obj){
				moreLight = true;
				return false;
			}
		});
		
		if(!moreLight){
			$("#lightboxOverlay").css("display", "none");
		}
		$(obj).css("display", "none");
	}
	
	function closePop(obj){
	   var moreLight = false;
		$(".lightbox").each(function(){
			if($(this).css("display") == "block"&& "#"+$(this).attr("id") != obj){
				moreLight = true;
				return false;
			}
		});
		
		if(moreLight){
			return;
		}else{
			$("#lightboxOverlay").css("display", "none");
			$(obj).css("display", "none");
		}
	}
</script>

