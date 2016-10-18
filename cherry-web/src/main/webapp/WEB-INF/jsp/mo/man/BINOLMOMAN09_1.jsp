<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ page import="com.cherry.cm.core.CherryConstants" %>
<s:set id="DATE_PATTERN_24_HOURS"><%=CherryConstants.DATE_PATTERN_24_HOURS%></s:set>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.mo.BINOLMOMAN09">
    <div id="aaData">
    <s:iterator value="posMemuInfoList" id="machineinfo">
    <ul>
    	<li>
    	<s:checkbox name="posMenuIDs" fieldValue="%{#machineinfo.posMenuID}" value="false" onclick="BINOLMOMAN09.checkRecord(this,'#dataTable');"></s:checkbox>
        </li>
        <li><s:property value="RowNumber"/></li><%-- No. --%>
        <li><s:property value="MenuCode"/></li>
        <li><s:property value="MenuType"/></li>
        <li>
        <span>
			  <s:set value="%{#posMemuInfoList.MenuLink}" var="MenuLink"></s:set>
			  <a title="" rel='<s:property value="MenuLink"/>' onclick="BINOLMOMAN09.showDetail(this);return false;" style="cursor:pointer;">
				<cherry:cut length="25" value="${MenuLink }"></cherry:cut>
			  </a>
			</span>
        <li><s:property value="Comment"/></li>
        <li>
        	<s:if test='IsLeaf.equals("1")'>
              <s:text name="MAN09_yesr"/>
            </s:if>
            <s:else>
              <s:text name="MAN09_no"/>
            </s:else>
        <li>
        <a id="51" class="delete" onclick="BINOLMOMAN09.MAN09EditPaper(<s:property value="posMenuID"/>);return false;">
		<span class="ui-icon icon-edit"></span>
		<span class="button-text"><s:text name="MAN09_editPaper"/></span>
		</a>
        </li>                          
    </ul>
    </s:iterator>
    </div>
</s:i18n>
