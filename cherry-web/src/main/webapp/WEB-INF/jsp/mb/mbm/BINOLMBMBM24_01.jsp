<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<s:i18n name="i18n.mb.BINOLMBMBM18">
<div id="aaData">
   <s:iterator value="memImportList" id="importInfo">
    <ul >
    	<li>${RowNumber}</li><%-- 编号 --%> 
    	<%--导入流水号 --%>
        <li>
        <s:url action="BINOLMBMBM24_detailInit" id="detailUrl">
			<s:param name="keyAttrImportId" value="%{#importInfo.keyAttrImportId}"></s:param>
			<s:param name="serialNo" value="%{#importInfo.serialNo}"></s:param>
		</s:url>
         <a class="popup" href="${detailUrl}" onclick="openWin(this);return false;"><s:property value="serialNo"/></a></li>
        <%--导入名称--%>
        <li><s:property value="importName"/></li>
        <%--导入时间 --%>
        <li><s:property value="importTime"/></li>
        <%--导入员工--%>
        <li><s:property value="employeeName"/></li>
        <%--导入原因 --%>
        <li>
         <s:if test="%{null!=importReason&&importReason.length()>20}">
         <a class="description" style="cursor: pointer;"  title="<s:text name="binolmbmbm18_reason" /> | <s:property value="importReason" />">
          <s:property value="%{importReason.substring(0, 20)}" />...
         </a>
         </s:if>
         <s:else>
	         <a><s:property value="importReason" /></a> 
	   	</s:else>
        </li>
    </ul>  
    </s:iterator>
</div>
</s:i18n>