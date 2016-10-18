<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<s:i18n name="i18n.ct.BINOLCTPMS01">
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<div id="aaData">
<s:iterator value="paramList" id="param" status="status">
<ul>
<li><s:property value="RowNumber"/></li>
<li><s:property value="paramName"/><input class="hide" name="supplierType" value="${supplierType}"><input class="hide" name="paramCode" value="${paramCode}"><input class="hide" name="paramType" value="${paramType}"></li>
<li><s:property value="paramCode"/></li>
<s:if test="#param.supplierType == 1">
	<li><span><s:property value='#application.CodeTable.getVal("1351", #param.configGroup)' /></span></li>
</s:if>
<s:if test="#param.supplierType == 3">
	<li><span><s:property value='#application.CodeTable.getVal("1352", #param.configGroup)' /></span></li>
</s:if>
<s:if test="#param.paramType == 1">
	<li><span><input class="text disabled" name="paramKey" value="${main_paramKey}" readonly="readonly"></span></li>
	<li><span><input class="text" name="paramValue" value="${paramValue}"></span></li>
</s:if>
<s:if test="#param.paramType == 2">
	<li><span><input class="text" name="paramKey" value="${paramKey}"></span></li>
	<li><span><input class="text" name="paramValue" value="${paramValue}"></span></li>
</s:if>
<s:if test="#param.paramType == 3">
	<li><span><input class="text" name="paramKey" value="${paramKey}"></span></li>
	<li><span><input class="text disabled" name="paramValue"  readonly="readonly"></span></li>
</s:if>
<li>
	<a class="edit" onclick="BINOLCTPMS01.editParam(2,this);return false;">
       		<span class="ui-icon icon-edit"></span>
       		<span class="button-text">
			<s:text name="PMS01_edit" />
        	</span>
    </a>
</li>
</ul>
</s:iterator>
</div>
</s:i18n>