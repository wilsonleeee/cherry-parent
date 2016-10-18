<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<s:i18n name="i18n.mb.BINOLMBRPT07">
<div id="aaData">
<s:iterator value="campaignInfoList" id="campaignInfoMap" status="status">
<ul>
<li><span><s:property value="campaignOrderTime"/></span></li>
<li><span><s:property value="finishTime"/></span></li>
<li><span><s:property value="nikeName"/></span></li>
<li><span><s:property value="memName"/></span></li>
<li><span><s:property value="memCode"/></span></li>
<li><span><s:property value="mobile"/></span></li>
<li><span>
<s:if test="%{state==1}">
<s:text name="mbrpt07_state1" />
</s:if>
<s:else>
<s:text name="mbrpt07_state0" />
</s:else>
</span></li>
<li><span>
<s:if test="%{memFlag==1}">
<s:text name="mbrpt07_memFlag1" />
</s:if>
<s:elseif test="%{memFlag==2}">
<s:text name="mbrpt07_memFlag2" />
</s:elseif>
<s:else>
<s:text name="mbrpt07_memFlag0" />
</s:else>
</span></li>
<li><span><s:property value="cityName"/></span></li>
<li><span><s:property value="counterCode"/></span></li>
<li><span><s:property value="nameForeign"/></span></li>
<li><span><s:property value="counterName"/></span></li>
<li><span>
<s:if test="%{#campaignInfoMap.saleAmount != null}">
<s:url action="BINOLMBRPT07_saleDetailInit" id="saleDetailInitUrl">
<s:param name="memberInfoId" value="memberInfoId"></s:param>
<s:param name="startDate" value="startDate"></s:param>
<s:param name="endDate" value="endDate"></s:param>
<s:param name="campaignCode" value="campaignCode"></s:param>
</s:url>
<a href="${saleDetailInitUrl }" class="popup" onclick="javascript:openWin(this);return false;">
<s:property value="saleAmount"/>
</a>
</s:if>
</span></li>
<li><span>
<s:if test="%{backFlag==1}">
<s:text name="mbrpt07_backFlag1" />
</s:if>
<s:else>
<s:text name="mbrpt07_backFlag0" />
</s:else>
</span></li>
</ul>
</s:iterator>
</div>
</s:i18n>