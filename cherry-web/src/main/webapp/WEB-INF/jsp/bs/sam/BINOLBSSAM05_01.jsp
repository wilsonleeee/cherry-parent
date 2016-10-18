<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<div id="sEcho"><s:property value="sEcho" /></div>
<div id="iTotalRecords"><s:property value="iTotalRecords" /></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords" /></div>
<s:i18n name="i18n.bs.BINOLBSSAM05">
<div id="aaData">
    <s:iterator value="assessmentScoreList" status="status">
     <ul>
     	<li><s:property value="#status.index+iDisplayStart+1" /></li>
     	<li><s:property value="employeeID"/></li>
       	<li><s:property value="assessmentYear"/></li>
       	<li><s:property value="assessmentMonth"/></li>
       	<li><s:property value="assessmentEmployee"/></li>
       	<li><s:property value="score"/></li>
       	<li><s:property value="assessmentDate"/></li>
       	<li><s:property value="memo"/></li>
       	<li>
			<a href="/Cherry/basis/BINOLBSSAM05_editInit.action?recordId=${recordId}" class="delete" onclick="javascript:openWin(this);return false;">
                <span class="ui-icon icon-edit"></span>
                <span class="button-text"><s:text name="BSSAM05_edit"/></span>
            </a>
            <a href="javascript:void(0)" onclick="BINOLBSSAM05.delete('${recordId}');return false;" class="delete">
                <span class="ui-icon icon-delete"></span>
                <span class="button-text"><s:text name="BSSAM05_delete"/></span>
            </a>
		</li>
     </ul>
    </s:iterator>
</div>
</s:i18n>