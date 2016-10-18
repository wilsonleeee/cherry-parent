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
        <s:url action="BINOLMBMBM18_detailInit" id="detailUrl">
			<s:param name="profileImportId" value="%{#importInfo.profileImportId}"></s:param>
			<s:param name="profileBillNo" value="%{#importInfo.profileBillNo}"></s:param>
			<s:param name="importType" value="%{#importInfo.importType}"></s:param>
		</s:url>
         <a class="popup" href="${detailUrl}" onclick="openWin(this);return false;"><s:property value="profileBillNo"/></a></li>
        <%--导入名称--%>
        <li><s:property value="importName"/></li>
        <%--导入类型 --%>
        <li>
        <s:if test='importType=="1"'> <s:text name="binolmbmbm18_import_Type_1"></s:text> </s:if>
        <s:if test='importType=="2"'> <s:text name="binolmbmbm18_import_Type_2"></s:text></s:if>
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