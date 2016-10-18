<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/wp/mc/BINOLWPMC01.js"></script>
<s:i18n name="i18n.wp.BINOLWPMBM01">
	<div style="margin:0px;border:0px;padding:0px">
		<div style="text-align:center;background:#BDFCC9;height:30px;"><span style="text-align:center;font-size:20px">请获取验证码</span></div>
		<hr style=" height:2px;border:none;" />
		<div id="errorMessageDiv" class="hide">
			<div class="actionError">
				<ul>
					<li><span><s:text name="WPMBM01_inputRightNewCode"></s:text></span></li>
				</ul>
			</div>
		</div>
		<div id="errorMessageDiv2" class="hide">
			<div class="actionError">
				<ul>
					<li><span><s:text name="WPMBM01_inputRightOldCode"></s:text></span></li>
				</ul>
			</div>
		</div>
		<p></p>
		<span style="font-size:15px;"><s:text name="WPMBM01_mobilePhoneOld"></s:text></span>&nbsp;&nbsp;&nbsp;&nbsp;
		<span style="font-size:15px;font-style:italic;"><s:property value="%{mobilePhoneOld}"/></span><br>
		<s:hidden name="mobilePhoneOldC" value="%{mobilePhoneOld}"/>
		<hr style=" height:2px;border:none;" />
		<span style="font-size:15px;"><s:text name="WPMBM01_verificationCode"></s:text></span>
		<span><input type="text" class="text ac_input" id="couponCodeOld" maxlength="6"  style="width:80px;height:22px;-moz-border-radius: 4px; -webkit-border-radius: 4px;border-radius: 2px;  border: 1px solid black; background: #ffffff url(images/ui-bg_gloss-wave_60_ffffff_500x100.png) left bottom repeat-x; font-weight: normal; color: #333333;display: inline-block; position: relative; padding: 0; margin-left: .5em;;"/></span>
		<button id="CheckOldButton" onclick="binolwpmc01.mobilePhoneOldCheck();return false;" style="-moz-border-radius: 4px; -webkit-border-radius: 4px; border-radius: 4px;  border: 1px solid #7d8791; background: #ffffff url(images/ui-bg_gloss-wave_60_ffffff_500x100.png) left bottom repeat-x; font-weight: normal; color: #333333;display: inline-block; position: relative; padding: 0; margin-left: .5em; text-decoration: none !important; cursor: pointer; text-align: center; zoom: 1; overflow: visible; vertical-align:middle;">
			<span class="ui-icon icon-mover"></span>
   			<span class="button-text"><s:text name="WPMBM01_sendCode"></s:text></span>
		</button>
		<p></p>
		<span style="font-size:15px;"><s:text name="WPMBM01_mobilePhoneNew"></s:text></span>&nbsp;&nbsp;&nbsp;&nbsp;
		<span style="font-size:15px;font-style:italic;"><s:property value="%{mobilePhone}"/></span><br>
		<s:hidden name="mobilePhoneC" value="%{mobilePhone}"/>
		<hr style=" height:2px;border:none;" />
		<span style="font-size:15px;"><s:text name="WPMBM01_verificationCode"></s:text></span>
		<span><input type="text" class="text ac_input" id="couponCode" maxlength="6"  style="width:80px;height:22px;-moz-border-radius: 4px; -webkit-border-radius: 4px;border-radius: 2px;  border: 1px solid black; background: #ffffff url(images/ui-bg_gloss-wave_60_ffffff_500x100.png) left bottom repeat-x; font-weight: normal; color: #333333;display: inline-block; position: relative; padding: 0; margin-left: .5em;;"/></span>
		<button id="CheckButton" onclick="binolwpmc01.mobilePhoneCheck();return false;" style="-moz-border-radius: 4px; -webkit-border-radius: 4px; border-radius: 4px;  border: 1px solid #7d8791; background: #ffffff url(images/ui-bg_gloss-wave_60_ffffff_500x100.png) left bottom repeat-x; font-weight: normal; color: #333333;display: inline-block; position: relative; padding: 0; margin-left: .5em; text-decoration: none !important; cursor: pointer; text-align: center; zoom: 1; overflow: visible; vertical-align:middle;">
			<span class="ui-icon icon-mover"></span>
   			<span class="button-text"><s:text name="WPMBM01_sendCode"></s:text></span>
		</button>
	</div>
<!-- URL start -->
<s:url id="sendMessageUrl" value="/wp/BINOLWPMBM01_sendMessage"></s:url>
<s:hidden name="sendMessage" value="%{sendMessageUrl}"/>
<!-- URL END -->
</s:i18n>
