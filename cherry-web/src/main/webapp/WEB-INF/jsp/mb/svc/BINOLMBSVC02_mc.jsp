<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/mb/svc/BINOLMBSVC02_mc.js?v=20160926"></script>
<s:i18n name="i18n.wp.BINOLWPMBM01">
<body>
	<div style="width:100%;">
		<div style="text-align:center;background:#BDFCC9;height:30px;"><span style="text-align:center;font-size:20px">请获取验证码</span></div>
		<hr style=" height:2px;border:none;" />
		<div id="errorMessageDiv" class="hide">
			<div class="actionError">
				<ul>
					<li><span></span></li>
				</ul>
			</div>
		</div>
		<p></p>
		<form id="modifyForm">
		<span style="font-size:15px;"><s:text name="WPMBM01_mobilePhoneQ"></s:text></span>&nbsp;&nbsp;&nbsp;&nbsp;
		<span style="font-size:15px;font-style:italic;"><s:property value="%{mobilePhone}"/></span><br>
		<s:hidden name="mobilePhone" value="%{mobilePhone}"/>
		<s:hidden name="cardCode" value="%{cardCode}"/>
		<s:hidden name="counterCode" value="%{counterCode}"/>
		<p></p>
		<span style="font-size:15px;"><s:text name="WPMBM01_verificationCode"></s:text></span>
		<span>
		<input type="password" style="position: absolute; top: -999px"/>
		<input type="text" class="text ac_input" id="verificationCode"  name="verificationCode" maxlength="6"  autocomplete="off" style="width:80px;height:22px;-moz-border-radius: 4px; -webkit-border-radius: 4px;border-radius: 2px;  border: 1px solid black; background: #ffffff url(images/ui-bg_gloss-wave_60_ffffff_500x100.png) left bottom repeat-x; font-weight: normal; color: #333333;display: inline-block; position: relative; padding: 0; margin-left: .5em;;"/>
		</span>
		<button id="CheckButton" onclick="BINOLMBSVC02_mc.sendMessage(this);return false;" style="-moz-border-radius: 4px; -webkit-border-radius: 4px; border-radius: 4px;  border: 1px solid #7d8791; background: #ffffff url(images/ui-bg_gloss-wave_60_ffffff_500x100.png) left bottom repeat-x; font-weight: normal; color: #333333;display: inline-block; position: relative; padding: 0; margin-left: .5em; text-decoration: none !important; cursor: pointer; text-align: center; zoom: 1; overflow: visible; vertical-align:middle;">
			<span class="ui-icon icon-mover"></span>
   			<span class="button-text"><s:text name="WPMBM01_sendCode"></s:text></span>
		</button>
		<p></p>
		</form>
	</div>
<!-- URL start -->
<s:url id="sendMessageUrl" value="/mb/BINOLMBSVC02_sendMessage"></s:url>
<s:hidden name="sendMessage" value="%{sendMessageUrl}"/>
<!-- URL END -->
</body>
</s:i18n>