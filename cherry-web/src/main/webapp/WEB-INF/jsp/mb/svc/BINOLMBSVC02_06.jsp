<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<s:i18n name="i18n.mb.BINOLMBSVC02">
<body>
	<div style="width:100%;">
		<div style="text-align:center;background:#BDFCC9;height:30px;"><span style="text-align:center;font-size:20px">请输入储值卡密码</span></div>
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
		<span style="font-size:15px;"><s:text name="SVC02_oldPassword"></s:text></span>&nbsp;&nbsp;&nbsp;&nbsp;
		<input type="password" id="oldPassword" name="oldPassword" autocomplete="off"/>
		<s:hidden name="cardCode" value="%{cardCode}"/>
		<s:hidden name="counterCode" value="%{counterCode}"/>
		<p></p>
		</form>
	</div>
<!-- URL start -->
<s:url id="sendMessageUrl" value="/mb/BINOLMBSVC02_sendMessage"></s:url>
<s:hidden name="sendMessage" value="%{sendMessageUrl}"/>
<!-- URL END -->
</body>
</s:i18n>