<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<script type="text/javascript" src="/Cherry/js/pt/jcs/BINOLPTJCS23.js"></script>

<div class="header container clearfix">
<BR />
<h1 class="logo left"><a href="#">WITPOS 店务通后台管理系统</a></h1>	
</div> 

<div class="main container clearfix">
<form id="soluDetailForm" action="BINOLPTJCS19_init" method="post">
	<input type="hidden" id = "productPriceSolutionID" name="productPriceSolutionID" value='<s:property value="productPriceSolutionID"/>'/>
	<input type="hidden" name="csrftoken" id="parentCsrftoken"/>
</form>
<s:if test="hasActionErrors()">
<div class="error" id="actionResultBody">
  <div class="center">
	<div class="ui-state-error clearfix">
	  	<s:actionerror/>
	</div>
     <button id="back" class="back" type="button" onclick="BINOLPTJCS23.toSoluDetail()" title="返回到门店产品列表设置画面"> <%-- 返回方案明细 --%>
      	<span class="ui-icon icon-back"></span>
      	<span class="button-text"><s:text name="global.page.back"/></span>
     </button>
    <button class="close" onclick="window.close();return false;">
    	<span class="ui-icon icon-close"></span>
    	<span class="button-text"><s:text name="global.page.close"/></span>
   	</button>
  </div>
</div>
</s:if>

<s:if test="hasActionMessages()">
<div class="success" id="actionResultBody">
  <div class="center">
	<div class="ui-state-success clearfix">
	  	<s:actionmessage/>
	</div>
     <button id="back" class="back" type="button" onclick="BINOLPTJCS23.toSoluDetail()" title="返回到门店产品列表设置画面"> <%-- 返回方案明细 --%>
      	<span class="ui-icon icon-back"></span>
      	<span class="button-text"><s:text name="global.page.back"/></span>
     </button>

    <button class="close" onclick="window.close();return false;">
    	<span class="ui-icon icon-close"></span>
    	<span class="button-text"><s:text name="global.page.close"/></span>
    </button>
  
  </div>
</div>
</s:if>
</div>

<div class="footer">
© 2010 上海秉坤 店务通业务支撑系统 版权所有 All rights reserved.
</div>