<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<s:i18n name="i18n.cp.BINOLCPACT01">
<div id="aaData">
	  <s:iterator value="subList" id="active">
    <ul> 
    	<li>${RowNumber}</li><%-- 编号 --%>
    	<%--活动名称--%>
        <li><s:property value="subCampName"/></li>
         <%--主题活动名称--%>
        <li><s:property value="subCampaignCode"/></li>
         <%--主题活动名称--%>
        <li><s:property value="campName"/></li>
        <%--主题活动类型 --%>
        <li><s:property value="#application.CodeTable.getVal('1174',campType)"/></li>
        <%--活动类型 --%>
        <li>
	        <s:if test='"LYHD".equals(campType)'>
	            	<s:property value="#application.CodeTable.getVal('1247',subcampType)"/>
	     	</s:if>
         	<s:if test='"DHHD".equals(campType)'>
         		<s:property value="#application.CodeTable.getVal('1229',subcampType)"/>
         	</s:if>
         	<s:if test='"CXHD".equals(campType)'>
         		<s:property value="#application.CodeTable.getVal('1228',subcampType)"/>
         	</s:if>
        </li>
        <%--活动状态 --%>
         <li>
        <s:if test='subCampState==0'>
        <span class="verified_unsubmit">
        <span><s:property value="#application.CodeTable.getVal('1113',subCampState)"/></span>
        </span>
        </s:if>
         <s:if test='subCampState==1'>
        <span  class="task-verified" >
        <span><s:property value="#application.CodeTable.getVal('1113',subCampState)"/></span>
        </span>
        </s:if>
         <s:if test='subCampState==2'>
        <span class="task-verified_rejected">
        <span><s:property value="#application.CodeTable.getVal('1113',subCampState)"/></span>
        </span>
        </s:if>
         <s:if test='subCampState==3'>
        <span class="verifying" >
        <span><s:property value="#application.CodeTable.getVal('1113',subCampState)"/></span>
        </span>
        </s:if>
        </li>
        <%--开始时间 --%>
        <li>
        <s:if test='reseStartDate!=null'>
        <s:property value="reseStartDate"/>
       	</s:if>
       	<s:else>
       	 <s:property value="obtainStartDate"/>
       	</s:else>
		</li>
        <%--结束时间 --%>
        <li>
 		<s:if test='reseEndDate!=null'>
        <s:property value="reseEndDate"/>
       	</s:if>
       	<s:else>
       	 <s:property value="obtainEndDate"/>
       	</s:else>
		</li>
		<li>
			<s:if test="validFlag == 0">
            	<span class="ui-icon icon-invalid"></span>
     		</s:if>
         	<s:if test="validFlag == 1">
         		<span class="ui-icon icon-valid"></span>
         	</s:if>
	    </li>
        <%--操作--%>
        <li>
        <cherry:show domId="CPACT01DISABLE">
        <s:if test='"1".equals(validFlag)'>
		<a class="delete"  onclick = "BINOLCPACT01.stopCampDialog('${campId}','${subCampId}');">
			<span class="ui-icon icon-delete"></span><span class="button-text"><s:text name="global.page.disable" /></span>
		</a>
		</s:if>
		</cherry:show>
		</li>
    </ul>  
    </s:iterator>
</div>
</s:i18n>
