<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<div id="aaData">
<s:iterator value="memberInfoList" id="memberInfo" status="status">
<ul>
<li><span>
<s:url action="BINOLMBMBM10_init" id="memberDetailUrl" namespace="/mb">
	<s:param name="memberInfoId" value="%{#memberInfo.memberInfoId}"></s:param>
</s:url>
<a href="${memberDetailUrl }" class="popup" onclick="javascript:openWin(this,{width:window.screen.width-10,height:window.screen.height-35});return false;">
	<s:property value="memberCode"/>
</a>
</span></li>
<li><span><s:property value="mobilePhone"/></span></li>
<li><span><s:property value="memberName"/></span></li>
<li><s:property value='#application.CodeTable.getVal("1006", #memberInfo.gender)' /></li>
<li><span>
  <s:if test='%{birthYear != null && !"".equals(birthYear) && birthMonth != null && !"".equals(birthMonth) && birthDay != null && !"".equals(birthDay)}'>
	<s:property value="birthYear"/>-<s:property value="birthMonth"/>-<s:property value="birthDay"/>
  </s:if>
</span></li>
<li><span><s:property value="levelName"/></span></li>
<li><span><s:property value="changablePoint"/></span></li>
<li><span><s:property value="employeeName"/></span></li>
</ul>
</s:iterator>
</div>
<s:if test="saleCountInfo != null && saleCountInfo.size() != 0">
	<s:i18n name="i18n.wp.BINOLWRMRP03">
	<div id="headInfo">
		<s:text name="WRMRP03_sumQuantity"/>
		<span class="<s:if test='saleCountInfo.sumQuantity < 0'>highlight</s:if><s:else>green </s:else>" style='margin-left:0px;'>
			<strong><s:text name="format.number"><s:param value="saleCountInfo.sumQuantity"></s:param></s:text></strong>
		</span>
		<s:text name="WRMRP03_sumAmount"/>
	    <span class="<s:if test='saleCountInfo.sumAmount < 0'>highlight</s:if><s:else>green</s:else>" style='margin-left:0px;margin-right:2px;'>
	    	<strong><s:text name="format.price"><s:param value="saleCountInfo.sumAmount"></s:param></s:text></strong>
	    </span>
	</div>
	</s:i18n>
</s:if> 