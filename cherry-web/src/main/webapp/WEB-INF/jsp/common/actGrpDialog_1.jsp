<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%> 
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<div id="aaData">
<%-- flag --%>
	<s:iterator value="prmActiveGrpList" status="status">
		<ul>
			<li>
			<s:if test='"".equals(prmActGrpID)||null==(prmActGrpID)'>
				<input name="prmActGrps" class="checkbox" value='<s:property value="BIN_PromotionActGrpID"/>'
				type="<s:if test='%{checkType == null || checkType == "checkbox"}'>checkbox</s:if><s:else>radio</s:else>"/>
			</s:if>	
			</li>
			<li><s:property value="GroupCode"/></li>
			<li>
				<span><s:property value="GroupName"/></span>
				<span class="hide"><s:property value='dateEditFlag'/></span>
			</li>
			<li><s:property value="#application.CodeTable.getVal('1174',ActivityType)"/></li>
			<li><span><s:property value="ActivityBeginDate"/></span>
			<li><span><s:property value="ActivityEndDate"/></span>
			<li>
			<cherry:show domId="BINOLSTJCS0605">
			<a href="#" id="btn_1_${status.index}" class="left" style="margin-left:5px;color:#3366FF;" onclick="editActGropItem(this,<s:property value="BIN_PromotionActGrpID"/>);">
				<s:text name="global.page.edit"/>
			</a>
			<a href="#" id="btn_2_${status.index}" class="left" style="display:none; margin-left:5px;color:#3366FF;" onclick="editActGropItem(this,<s:property value="BIN_PromotionActGrpID"/>);">
				<s:text name="global.page.save"/>
			</a>
			<a href="#" id="btn_3_${status.index}" class="left" style="display:none;margin-left:10px;color:#3366FF;" onclick="editActGropItem(this,<s:property value="BIN_PromotionActGrpID"/>);">
				<s:text name="global.page.cancle"/>
			</a>
			</cherry:show>
			</li>
		</ul>
	</s:iterator>
</div>