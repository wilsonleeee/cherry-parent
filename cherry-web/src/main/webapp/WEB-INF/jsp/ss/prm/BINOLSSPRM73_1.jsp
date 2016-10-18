<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<s:i18n name="i18n.mb.BINOLSSPRM73">
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<div id="aaData">
<s:iterator value="ruleList" id="ruleInfo">
<ul>
<li><span><s:property value="RowNumber"/></span></li>
<li><span>
	<s:property value="ruleName"/>
	<input type="hidden" name="ruleCode" value='<s:property value="#ruleInfo.ruleCode"/>'>
</span></li>
<li><span><s:property value="ruleCode"/></span></li>
<li><span><s:property value="sendStartTime"/></span></li>
<li><span><s:property value="sendEndTime"/></span></li>
<li><span>
<s:property value="#application.CodeTable.getVal('1380',status)"/>
</span></li>
<li><span>
<s:if test="#ruleInfo.validFlag ==1"><span class='ui-icon icon-valid'></span></s:if>
<s:elseif test="#ruleInfo.validFlag ==0"><span class='ui-icon icon-invalid'></span></s:elseif>
</span></li>
<li>
<s:if test='#ruleInfo.validFlag ==1'>
	 <s:url id="edit_url" action="BINOLSSPRM73_editInit" namespace="/ss">
			<s:param name="ruleCode">${ruleCode}</s:param>
	</s:url>
	<a class="delete" href="${edit_url}" onclick="openWin(this);return false;">
		<span class="ui-icon icon-edit"></span><span class="button-text">编辑</span>
	</a>
	<s:if test='#ruleInfo.status ==1'>
		<a class="delete" href="javascript:void(0)" onclick="binolssprm73.popBatchDialog(this);return false;">
			<span class="ui-icon icon-edit"></span><span class="button-text">批量生成券</span>
		</a>
	</s:if>
	<s:else>
	<s:url id="check_url" action="BINOLSSPRM73_checkRule" namespace="/ss">
			<s:param name="ruleCode">${ruleCode}</s:param>
	</s:url>
	<a class="search" href ="javascript:void(0)" onclick="binolssprm73.checkCouponRule('${check_url}');return false;">
		<span class="ui-icon icon-export"></span><span class="button-text"><s:text name="审核通过"/></span>
	</a>
	</s:else>
</s:if>
</li>
</ul>
</s:iterator>
</div>
</s:i18n>