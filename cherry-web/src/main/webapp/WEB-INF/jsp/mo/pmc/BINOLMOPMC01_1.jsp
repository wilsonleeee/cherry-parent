<%-- 菜单组管理Datatable --%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho" /></div>
<div id="iTotalRecords"><s:property value="iTotalRecords" /></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords" /></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.mo.BINOLMOPMC01">
<div id="aaData">
    <s:iterator value="menuGrpList" id="menuGrpMap">
    <s:url id="updMenuGrpInitUrl" action="BINOLMOPMC01_updateMenuGrpInit">
    	<%-- 菜单组ID --%>
        <s:param name="menuGrpID">${menuGrpMap.menuGrpID}</s:param>
    </s:url>
    <s:url id="deleteMenuGrpUrl" action="BINOLMOPMC01_deleteMenuGrp">
        <%-- 菜单组ID --%>
        <s:param name="menuGrpID">${menuGrpMap.menuGrpID}</s:param>
    </s:url>
    <s:url id="menuConfigInitUrl" action="BINOLMOPMC02_init">
        <%-- 菜单组ID --%>
        <s:param name="menuGrpID">${menuGrpMap.menuGrpID}</s:param>
        <s:param name="pastStatus">${menuGrpMap.pastStatus}</s:param>
    </s:url>
    <s:url id="menuCntConfInitUrl" action="BINOLMOPMC03_init">
        <%-- 菜单组ID --%>
        <s:param name="menuGrpID">${menuGrpMap.menuGrpID}</s:param>
    </s:url>
    <s:url id="getPublishTreeUrl" action="BINOLMOPMC01_getPublishTree">
        <%-- 菜单组ID --%>
        <s:param name="menuGrpID">${menuGrpMap.menuGrpID}</s:param>
    </s:url>
    <ul>
        <%-- No. --%>
        <li><s:property value="RowNumber"/></li>
        <li>
        <%-- 菜单组名称 --%>
            <s:if test='menuGrpName != null && !"".equals(menuGrpName)'>
                <span><s:property value="menuGrpName"/></span>
            </s:if>
            <s:else>&nbsp;</s:else>
        </li>
        <li>
        	<span><s:property value="startDate"/></span>
        </li>
        <li>
        	<span><s:property value="endDate"/></span>
        </li>
        <li>
        	<%-- 发布日期 --%>
            <s:if test='publishDate != null && !"".equals(publishDate)'>
                <span><s:property value="publishDate"/></span>
            </s:if><s:else>
            	<span><label class="highlight"><s:text name="PMC01_unPublish"></s:text></label></span>
			</s:else>
        </li>
        <li>
        	<span><s:property value='#application.CodeTable.getVal("1284", machineType)'/></span>
        </li>
        <li>
        	<%-- 操作 --%>
        	<!-- 未下发到柜台的菜单组才能编辑与删除 -->
        	<s:if test='"0".equals(publishFlag)'>
	        	<a href="${updMenuGrpInitUrl}" class="add" id="updateBtn" onclick="BINOLMOPMC01.popEditDialog(this);return false;">
	               <span class="ui-icon icon-edit"></span>
	               <span class="button-text"><s:text name="PMC01_eidt"></s:text></span>
	           	</a>
	           	<a href="${deleteMenuGrpUrl}" id="deleteBtn" class="delete" onclick="BINOLMOPMC01.popDeleteDialog(this);return false;">
	                <span class="ui-icon icon-disable"></span>
	                <span class="button-text"><s:text name="PMC01_delete"></s:text></span>
	            </a>
            </s:if>
            <a class="authority" href="${menuConfigInitUrl }" onclick="BINOLMOPMC01.clearActionHtml();javascript:openWin(this);return false;">
				<span class="ui-icon icon-authority"></span><span class="button-text"><s:text name="PMC01_menuConfig" /></span>
			</a>
			<!-- 未过期的菜单组才能下发 -->
			<s:if test='"0".equals(pastStatus)'>
            <a class="delete" href="${menuCntConfInitUrl }" onclick="BINOLMOPMC01.clearActionHtml();javascript:openWin(this);return false;">
				<span class="ui-icon icon-down "></span>
				<span class="button-text"><s:text name="PMC01_publishMenuCounter" /></span>
			</a>
			</s:if>
			<!-- 只有下发过才能【查看发布】 -->
			<s:if test='"1".equals(publishFlag)'>
				<a href="${getPublishTreeUrl }" class="delete" onclick="BINOLMOPMC01.clearActionHtml();BINOLMOPMC01.popViewPublish(this);return false;">
				<span class="ui-icon icon-publish "></span>
				<span class="button-text"><s:text name="PMC01_viewPublish" /></span>
				</a>
			</s:if>
			<span class="hide">
			<s:hidden name="modifyTime" value="%{#menuGrpMap.updateTime}"></s:hidden>
			<s:hidden name="modifyCount" value="%{#menuGrpMap.modifyCount}"></s:hidden>
			<!-- 用按渠道查看发布 -->
       		<s:hidden id="currentMenuGrpID" name="currentMenuGrpID" value="%{#menuGrpMap.menuGrpID}"></s:hidden>
			</span>
        </li>
    </ul>
    </s:iterator>
</div>
</s:i18n>