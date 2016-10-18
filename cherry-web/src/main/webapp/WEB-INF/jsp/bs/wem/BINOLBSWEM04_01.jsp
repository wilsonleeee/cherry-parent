<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho" /></div>
<div id="iTotalRecords"><s:property value="iTotalRecords" /></div>
<div id="iTotalDisplayRecords"><s:property
	value="iTotalDisplayRecords" /></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.bs.BINOLBSWEM04">
	<div id="aaData">
		<s:iterator value="wechatMerchantList" id="resultMap" status="status">
			<ul>
				<li><s:property value="#status.index+iDisplayStart+1" /></li>
				<li>
					<s:url id="editUrl" value="/basis/BINOLBSWEM04_edit">
						<s:param name="agentMobile" value="agentMobile"/>
					</s:url>
					<a href="${editUrl }" onclick="javascript:openWin(this);return false;"><s:property value="agentMobile"/></a>
				</li>
				<li><s:property value="agentName"/></li>
				<li><s:property value="departName"/></li>
				<li><s:property value='#application.CodeTable.getVal("1000", #resultMap.agentLevel)' /></li>
				<li><s:property value="superMobile"/></li>
				<li><s:property value="agentProvince"/></li>
				<li><s:property value="agentCity"/></li>
				<li>
					<s:if test='"1".equals(validFlag)'>
						<span class='ui-icon icon-valid'></span>
					</s:if>
					<s:else>
						<span class='ui-icon icon-invalid'></span>
					</s:else>
				</li>
			</ul>
		</s:iterator>
	</div>
</s:i18n>