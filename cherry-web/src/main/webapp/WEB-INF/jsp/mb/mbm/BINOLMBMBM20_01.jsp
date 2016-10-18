<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<s:i18n name="i18n.mb.BINOLMBMBM20">
<div id="aaData">
<s:iterator value="memWarnInfoList" id="memWarnInfoMap">
<ul>
<li><s:checkbox name="id" fieldValue="%{#memWarnInfoMap.id}" value="false" onclick="binolmbmbm20_checkRecord(this,'#memWarnInfoDataTable');"></s:checkbox></li>
<%-- 业务类型 --%>
<li><span><s:property value="TradeType"/></span></li>
<%-- 单据号 --%>
<li><span><s:property value="TradeNoIF"/></span></li>
<%-- 错误类型 --%>
<li><span>
<s:if test='%{#memWarnInfoMap.ErrType != null && "1".equals(#memWarnInfoMap.ErrType)}'>
<s:text name="binolmbmbm20_errType1" />
</s:if>
<s:else>
<s:text name="binolmbmbm20_errType0" />
</s:else>
</span></li>
<%-- 错误信息 --%>
<li><span>
  <s:set value="%{#memWarnInfoMap.ErrInfo }" var="ErrInfo"></s:set>
  <a title="" rel='<s:property value="ErrInfo"/>' onclick="binolmbmbm20.showDetail(this);return false;" style="cursor:pointer;">
	<cherry:cut length="30" value="${ErrInfo }"></cherry:cut>
  </a>
</span></li>
<%-- 时间 --%>
<li><span><s:property value="PutTime"/></span></li>
</ul>
</s:iterator>
</div>
</s:i18n>