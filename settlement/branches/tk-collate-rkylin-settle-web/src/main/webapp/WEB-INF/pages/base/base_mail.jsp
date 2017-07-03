<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<form name="sendMailForm" id="sendMailForm" action="" method="post">
<div class="lightbox" id="vertify_email" style="display: none; top: 20%;">
        <div class="chan_pop_div wid632">
            <div class="chan_pop_title">
                <b class="left marglr2020">验证账号名</b>
                <a href="javascript:;" class="right icon_btn_close margtr1516" id="sendMailClose"></a>
            </div>
            <div class="chan_pop_con">
                <div class="pop_con_div">
                    <span class="left icon_tips01 margt10 marglr2010"></span>
                    <b class="left pop_con_txt20 margt11">请在24小时内点击邮件中的链接继续完成注册。</b></div>
                <div class="blank15">
                </div>
                <div class="pop_con_div pop_con_txt20 txtcenter">
                    <span>邮件已发送到邮箱：</span><span id="emailValue"></span></div>
                <div class="blank15"></div>
                <div class="pop_con_div">
                    <div class="attr_con wid480">
                        <a href="javascript:;" class="chan_btn wid343" id="reditMailBtn">立即查收邮件</a>
                    </div>
                </div>
                <div class="blank15 bord13"></div>
                <div class="blank15"></div>
                <div class="pop_con_div"><b class="left pop_con_txt20 margl60">没收到邮件？</b></div>
                <ul class="pop_con_list wid480 margl62">
                    <li><span class="left margr8">1.</span>请检查是否在垃圾邮件中</li>
                    <li id="mailSendAgainLi">
                    	<span class="left margr8">2.</span>
                    	<a href="javascript:;" class="regmail_btn wid130 " id="mailSendAgain">重新发送邮件</a>
                    	<a href="javascript:;" class="regmail_btn wid130 " id="mailSendAgain1">重新发送邮件</a>
                    </li>
                    <!-- 
                    <li id="emailChangeLi">
                    	<span class="left margr8">3.</span> 重新发送邮件，还未收到？试试
                    	<a href="javascript:;" class="red03" id="emailChangeBtn">更换邮箱</a>
                    </li>
                     -->
                    <li id="phoneChangeLi">
                    	<span class="left margr8">3.</span> 
                    	如果一直未收到邮件，请试试使用
                    	<a href="javascript:;" class="red03" id="changePhoneBtn">手机号注册</a>
                    </li>
                </ul>
            </div>
        </div>
    </div>
</form>