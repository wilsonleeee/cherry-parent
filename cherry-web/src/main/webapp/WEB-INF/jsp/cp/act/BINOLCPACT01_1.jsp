<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<s:i18n name="i18n.cp.BINOLCPACT01">
<div id="aaData">
	  <s:iterator value="mainList" id="active">
	<s:url id="detailUrl" value="/cp/BINOLCPACT02_actDetailInit.action">
			<s:param name="campaignId">${campId}</s:param>
	</s:url>
	<s:url id="importInit_url" action="/cp/BINOLCPACT13_init"/>
    <ul id="campNameId">
    	<li>${RowNumber}</li><%-- 编号 --%> 
    	<%--活动名称 --%>
        <li>
		<a href="${detailUrl}" onclick="openWin(this,{width:window.screen.width-10,height:window.screen.height-35});return false;"
		<s:if test='campState==0 && empValidFlag==0'>class="gray" title="|<s:text name="global.page.actMessageTip"/>"</s:if>
				><s:property value="campName"/></a>
        </li>
        <%--活动编码 --%>
        <li><s:property value="campaignCode"/></li>
        <%--活动类型--%>
        <li><s:property value="#application.CodeTable.getVal('1174',campType)"/></li>
         <%--活动状态 --%>
         <li>
        <s:if test='campState==0'>
        <span class="verified_unsubmit">
        <span><s:property value="#application.CodeTable.getVal('1113',campState)"/></span>
        </span>
        </s:if>
         <s:if test='campState==1'>
        <span  class="task-verified" >
        <span><s:property value="#application.CodeTable.getVal('1113',campState)"/></span>
        </span>
        </s:if>
         <s:if test='campState==2'>
        <span class="task-verified_rejected">
        <span><s:property value="#application.CodeTable.getVal('1113',campState)"/></span>
        </span>
        </s:if>
         <s:if test='campState==3'>
        <span class="verifying" >
        <span><s:property value="#application.CodeTable.getVal('1113',campState)"/></span>
        </span>
        </s:if>
        </li>
        <%--开始时间 --%>
        <li><s:property value="startDate"/></li>
        <%--结束时间 --%>
        <li><s:property value="endDate"/></li>
        <%--停用时间 --%>
        <li><s:property value="updateTime"/></li>
        <%--活动创建者 --%>
        <li><s:property value="employeeName"/></li>
	    <li>
			<s:if test="validFlag == 0">
            	<span class="ui-icon icon-invalid"></span>
     		</s:if>
         	<s:if test="validFlag == 1">
         		<span class="ui-icon icon-valid"></span>
         	</s:if>
	    </li>
        <%--操作 --%>
        <li>
        <s:url id="edit_url" action="BINOLCPCOM02_editInit" namespace="/cp">
			<s:param name="campaignId">${campId}</s:param>
		</s:url>
		<s:url id="communication_url" action="BINOLCTCOM01_init.action" namespace="/ct">
			<s:param name="campaignCode"><s:property value="campaignCode"/></s:param>
		</s:url>
		<cherry:show domId="CPACT01DISABLE">
		<s:if test='"1".equals(validFlag)'>
		<a class="delete" onclick = "BINOLCPACT01.stopCampDialog('${campId}','${subCampId}',0);">
			<span class="ui-icon icon-delete"></span><span class="button-text"><s:text name="global.page.disable" /></span>
		</a>
		</s:if>
		<s:else>
		<a class="delete" onclick = "BINOLCPACT01.stopCampDialog('${campId}','${subCampId}',1);">
			<span class="ui-icon icon-enable"></span><span class="button-text"><s:text name="global.page.enable" /></span>
		</a>
		</s:else>
		</cherry:show>
		<s:if test="createUserId != 0">
			<s:if test='campState!=3 && editFlag == 1'>
				<cherry:show domId="CPACT01COPY">
				<a class="delete" href="${edit_url}&copyFlag=1" onclick="openWin(this);return false;">
					<span class="ui-icon icon-copy"></span><span class="button-text"><s:text name="ACT01_copy" /></span>
				</a>
				</cherry:show>
			</s:if>
			<s:if test='campState!=2 && "1".equals(validFlag) && loginUserId.equals(createUserId) && editFlag == 1'>
			<cherry:show domId="CPACT01EDIT">
			<a class="delete" href="${edit_url}" onclick="openWin(this);return false;">
				<span class="ui-icon icon-edit"></span><span class="button-text"><s:text name="ACT01_edit" /></span>
			</a>
			</cherry:show>
			</s:if>
			<s:if test='campState!=2 && campState!=3 && "1".equals(validFlag) && loginUserId.equals(createUserId)'>
			<cherry:show domId="CPACT01SETUPCOM">
			<a class="delete" href="${communication_url}" onclick="openWin(this);return false;">
				<span class="ui-icon icon-setting"></span><span class="button-text"><s:text name="ACT01_setUpCom" /></span>
			</a>
			</cherry:show>
			</s:if>
			<s:if test='manageGift ==1'>
				 <a onclick="javascript:openWin(this);return false;" class="add"  href="${importInit_url}">
			 		<span class="button-text"><s:text name="ACT01_import"></s:text></span>
			 		<span class="ui-icon icon-add"></span>
		    	</a>
			</s:if>
		</s:if>
		</li>
    </ul>  
    </s:iterator>
</div>
</s:i18n>