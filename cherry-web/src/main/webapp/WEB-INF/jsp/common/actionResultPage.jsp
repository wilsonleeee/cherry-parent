<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<link rel="stylesheet" href="/Cherry/css/common/blueprint/screen.css" type="text/css" media="screen, projection">
<link rel="stylesheet" href="/Cherry/css/common/sunny/jquery-ui-1.8.5.custom.css" type="text/css" media="screen, projection">
<div class="header container clearfix">
<BR />
<h1 class="logo left"><a href="#">WITPOS 店务通后台管理系统</a></h1>	
</div> 
<script type="text/javascript">
<!--
window.onbeforeunload = function(){
	if (window.opener) {
			window.opener.unlockParentWindow();
			// 刷新父页面
			window.opener.search();
	}
};
//-->
</script>

<div class="main container clearfix">
<s:if test="hasActionErrors()">
<div class="error" id="actionResultBody">
  <div class="center">
	<div class="ui-state-error clearfix">
	  	<s:actionerror/>
	</div>
    <button class="close" onclick="window.close();return false;"><span class="ui-icon icon-close"></span><span class="button-text"><s:text name="global.page.close"/></span></button>
  </div>
</div>
</s:if>

<s:if test="hasActionMessages()">
<div class="success" id="actionResultBody">
  <div class="center">
	<div class="ui-state-success clearfix">
	  	<s:actionmessage/>
	</div>
    <button class="close" onclick="window.close();return false;"><span class="ui-icon icon-close"></span><span class="button-text"><s:text name="global.page.close"/></span></button>
  </div>
</div>
</s:if>
</div>

<div class="footer">
© 2010 上海秉坤 店务通业务支撑系统 版权所有 All rights reserved.
</div>

