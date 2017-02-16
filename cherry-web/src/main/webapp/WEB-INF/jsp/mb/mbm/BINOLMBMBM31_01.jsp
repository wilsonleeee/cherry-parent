<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho" /></div>
<div id="iTotalRecords"><s:property value="iTotalRecords" /></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords" /></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.mb.BINOLMBMBM31">
    <s:iterator value="ruleList" id="rule">
        <s:url id="showDetail_url" action="BINOLMBMBM31_getRuleDetail">
            <s:param name="completeDegreeRuleID"><s:property value='completeDegreeRuleID'/></s:param>
        </s:url>
        <s:url id="editRuleInit_url" action="BINOLMBMBM31_initEditMemComplete">
            <s:param name="completeDegreeRuleID"><s:property value='completeDegreeRuleID'/></s:param>
        </s:url>
        <div id="aaData">
            <ul>
                <li>${RowNumber}</li><%-- 编号 --%>
                <%-- 规则名称 --%>
                <li><a href="${showDetail_url}" onclick="BINOLMBMBM31.getRuleDetailInfo(this);return false;"><s:property value="ruleName" /></a></li>
                <li><%-- 开始时间  --%>
                    <s:property	value="startTime" /></li>
                <li><%-- 结束时间  --%>
                    <s:property	value="endTime" /></li>
                <li><%-- 操作  --%>
                    <s:if test='"1".equals(isValid) || "2".equals(isValid)'>
                        <a href="${editRuleInit_url}" class="add" onclick="BINOLMBMBM31.popEditDialog(this,<s:property value="isValid" />);return false;">
                            <span class="ui-icon icon-edit"></span><span class="button-text"><s:text name="global.page.edit" /></span>
                        </a>
                    </s:if>
                    <s:if test='"2".equals(isValid)'>
                        <a class="delete" onclick="BINOLMBMBM31.popDeleteDialog(this);return false;">
                            <span class="ui-icon icon-disable"></span><span class="button-text"><s:text name="MBM31_delete" /></span>
                        </a>
                    </s:if>
                    <div>
                        <input type="hidden" id="ruleName" name="ruleName" value="<s:property value='ruleName' />"/>
                        <input type="hidden" id="startTime" name="startTime" value="<s:property value='startTime' />"/>
                        <input type="hidden" id="endTime" name="endTime" value="<s:property value='endTime' />"/>
                        <input type="hidden" id="totalPoint" name="totalPoint" value="<s:property value='totalPoint' />"/>
                        <input type="hidden" id="ruleCondition" name="ruleCondition" value="<s:property value='ruleCondition' />"/>
                        <input type="hidden" id="memo" name="memo" value="<s:property value='memo' />"/>
                        <input type="hidden" id="completeDegreeRuleID" name="ruleName" value="<s:property value='completeDegreeRuleID' />"/>
                    </div>
                </li>
                <li><%-- 备注  --%>
                     <s:property	value="memo" /></li>
            </ul>
        </s:iterator></div>
</s:i18n>