<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<div id="aaData">
<s:iterator value="pointDetailList" id="pointInfo">
<ul>
<li><span><s:property value="memCode"/></span></li>
<li><span><s:property value="proName"/></span></li>
<li><span><s:property value="unitCode"/></span></li>
<li><span><s:property value="barCode"/></span></li>
<li><span><s:property value='#application.CodeTable.getVal("1106", #pointInfo.saleType)' /></span></li>
<li><span>
<s:if test='%{#pointInfo.price != null && !"".equals(#pointInfo.price)}'>
<s:text name="format.price"><s:param value="price"></s:param></s:text>
</s:if>
</span></li>
<li><span>
<s:if test='%{#pointInfo.quantity != null && !"".equals(#pointInfo.quantity)}'>
<s:text name="format.number"><s:param value="quantity"></s:param></s:text>
</s:if>
</span></li>
<li><span><s:property value="point"/></span></li>
<li><span><s:property value='#application.CodeTable.getVal("1214", #pointInfo.pointType)' /></span></li>
<cherry:show domId="BINOLMBMBM10_29">
<li><span>
<s:if test='%{#pointInfo.departName != null && !"".equals(#pointInfo.departName)}'>
(<s:property value="departCode"/>)<s:property value="departName"/>
</s:if>
<s:else>
<s:property value="departCode"/>
</s:else>
</span></li>
</cherry:show>
<li><span><s:property value="changeDate"/></span></li>
<li><span><s:property value="combCampaignName"/></span></li>
<li><span><s:property value="mainCampaignName"/></span></li>
<li><span><s:property value="subCampaignName"/></span></li>
<li><span><s:property value="reason"/></span></li>
<li><span><s:property value="billCode"/></span></li>
<li><span><s:property value="srCode"/></span></li>
<li><span><s:property value="srTime"/></span></li>
</ul>
</s:iterator>
</div>