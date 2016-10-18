<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<s:i18n name="i18n.mb.BINOLMBMBM04">
<div id="aaData">
<s:iterator value="pointDetailList" id="pointInfo">
<ul>
<li><span>
<s:url action="BINOLWPMBM01_searchPointDetail" id="searchSaleDetailUrl">
<s:param name="pointChangeId" value="%{#pointInfo.pointChangeId}"></s:param>
</s:url>
<input value="${searchSaleDetailUrl }" type="hidden"/>
<s:property value="memCode"/>
</span></li>
<li><span><s:property value="billCode"/></span></li>
<li><span><s:property value='#application.CodeTable.getVal("1213", #pointInfo.billType)' /></span></li>
<cherry:show domId="BINOLMBMBM10_29">
<li><span><s:property value="departCode"/></span></li>
</cherry:show>
<li><span><s:property value="changeDate"/></span></li>
<li><span>
<s:if test='%{#pointInfo.amount != null && !"".equals(#pointInfo.amount)}'>
<s:text name="format.price"><s:param value="amount"></s:param></s:text>
</s:if>
</span></li>
<li><span>
<s:if test='%{#pointInfo.quantity != null && !"".equals(#pointInfo.quantity)}'>
<s:text name="format.number"><s:param value="quantity"></s:param></s:text>
</s:if>
</span></li>
<li><span><s:property value="point"/></span></li>
<li><span>
<s:if test='%{#pointInfo.srCode != null && !"".equals(#pointInfo.srCode)}'>
<s:text name="binolmbmbm04_hasRelevantSRCode1" />
</s:if>
<s:else>
<s:text name="binolmbmbm04_hasRelevantSRCode2" />
</s:else>
</span></li>
</ul>
</s:iterator>
</div>
</s:i18n>