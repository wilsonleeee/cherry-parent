<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<div id="aaData">
	<s:iterator value="searchRequestList" id="searchRequestMap">
		<ul>
			<li><input name="reqContent" type="radio" class="checkbox" value='<s:property value="reqContent"/>'/></li>
			<li><s:property value="requestName"/></li>
			<li>
			  <s:set value="%{#searchRequestMap.description }" var="descriptionCut"></s:set>
			  <a title='|<s:property value="description"/>' class="description">
				<cherry:cut length="20" value="${descriptionCut }"></cherry:cut>
			  </a>
			</li>
			<li>
			<s:url action="BINOLCM33_del" id="delSearchRequestUrl">
				<s:param name="searchRequestId" value="%{#searchRequestMap.searchRequestId}"></s:param>
			</s:url>
			<a class="delete" href="${delSearchRequestUrl }" onclick="popmemsearch.delSearchRequest(this);return false;">
				<span class="ui-icon icon-delete"></span><span class="button-text"><s:text name="global.page.delete" /></span>
			</a>
			</li>
		</ul>
	</s:iterator>
</div>