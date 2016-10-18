<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<s:i18n name="i18n.mb.BINOLMBPTM04">
<div id="aaData">
   <s:iterator value="pointList" id="pointInfo">
    <ul >
    	<li>${RowNumber}</li><%-- 编号 --%> 
    	<%--积分导入流水号 --%>
        <li>
        <s:url action="BINOLMBPTM04_detailInit" id="pointDetailUrl">
			<s:param name="memPointImportId" value="%{#pointInfo.memPointImportId}"></s:param>
			<s:param name="pointImportCode" value="%{#pointInfo.pointImportCode}"></s:param>
		</s:url>
         <a class="popup" href="${pointDetailUrl}" onclick="openWin(this);return false;"><s:property value="pointImportCode"/></a></li>
        <%--导入名称--%>
        <li><s:property value="pointBillName"/></li>
        <%--积分类型--%>
        <li><s:property value="#application.CodeTable.getVal('1214',pointType)"/></li>
         <%--导入方式 --%>
        <li><s:property value="#application.CodeTable.getVal('1251',importType)"/></li>
        <%--导入时间 --%>
        <li><s:property value="businessTime"/></li>
        <%--导入时间 --%>
        <li><s:property value="employeeName"/></li>
        <%--导入原因 --%>
        <li>
         <s:if test="%{null!=reason&&reason.length()>20}">
         <a class="description" style="cursor: pointer;"  title="<s:text name="binolmbptm04_reason" /> | <s:property value="reason" />">
          <s:property value="%{reason.substring(0, 20)}" />...
         </a>
         </s:if>
         <s:else>
	         <a><s:property value="reason" /></a> 
	   	</s:else>
        </li>
    </ul>  
    </s:iterator>
</div>
</s:i18n>