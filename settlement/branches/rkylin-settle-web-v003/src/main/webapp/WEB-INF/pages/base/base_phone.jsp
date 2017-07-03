<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%><form name="sendPhoneForm" id="sendPhoneForm" action="" method="post">
	<div class="lightbox" id="vertify_user" style="display: none; top: 20%;">
        <div class="chan_pop_div wid632">
            <div class="chan_pop_title">
                <b class="left marglr2020">验证账号名</b>
                <a href="javascript:;" class="right icon_btn_close margtr1516" id="userDivClose"></a>
            </div>
            <div class="chan_pop_con">
                <div class="pop_con_div">
                    <span class="left icon_tips01 margt10 marglr2010"></span>
                    <b class="left pop_con_txt20 margt11">为确认您的手机可用，校验码已发送到您的手机，10分钟内输入有效。</b>
                </div>
                <div class="con_core_info_div">
                	<b class="label_txt wid130 txtright margt10 margr8">&nbsp;</b>
                	<span class="label_txt wid343 margt10 red01" style="height: auto" id="phoneCodeVTip"></span>
                </div>
                <!-- 
                <div class="con_core_info_div" style="height: 10px;">
                	<b class="label_txt wid130 txtright margt10 margr8">&nbsp;</b>
                	<span class="label_txt wid343 margt10 grey" style="height: auto" id="phoneCodeCheckTip"></span>
                </div>
                 -->
                <div class="blank5"></div>
                <div class="pop_con_div">
                    <div class="con_core_info_div">
                        <label class="label_txt wid130 txtright margt10 margr8">手机号码：</label>
                        <label class="label_txt wid343 margt10 margr8" id="phoneValue"></label>
                    </div>
                    <div class="blank15"></div>
                    <div class="con_core_info_div">
                        <label class="label_txt wid130 txtright margt10 margr8">校验码：</label>
                        <div class="input_con_type wid343">
                            <input type="text" class="wid333 ip_input_con lightgrey" maxlength="6" value="" id="phoneCode" name="phoneCode" />
                        </div>
                    </div>
                    <div class="blank5"></div>
                    <div class="con_core_info_div" style="height:15px;">
                    	<b class="label_txt wid130 txtright margt10 margr8">&nbsp;</b>
                    	<span class="label_txt wid343 margt10 grey" style="height: auto" id="phoneCodeTip"></span>
                    </div>
                    <div class="blank10"></div>
                    <div class="con_core_info_div">
                        <div class="attr_con wid444">
                            <a href="javascript:;" class="chan_btn wid343" id="phoneCodeNext">下一步</a>
                        </div>
                    </div>
                </div>
                <div class="blank15 bord13"></div>
                <div class="blank15"></div>
                <div class="pop_con_div"><b class="left pop_con_txt20 margl60">没收到短信？</b></div>
                <ul class="pop_con_list wid480 margl62">
                    <li>
                    	<span class="left margr8">1. </span>
                    	<span class="left margr8">1分钟内未收到效验码短信，请点击</span>
                    	<a href="javascript:;" class="regmail_btn wid130" id="phoneCodeAgain1">重新发效验码短信</a>
                    	<a href="javascript:;" class="regmail_btn wid130" id="phoneCodeAgain">重新发效验码短信</a>
                    </li>
                    <li>
                    	<span class="left margr8">2.</span>
                    	一直没收到？建议您
                    	<a href="javascript:;" class="red03" id="changeEmailBtn">使用邮箱注册</a>
                    </li>
                </ul>
            </div>
        </div>
    </div>
</form>