<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.ss.BINOLSSPRM71">
<div id="aaData">
	<s:iterator value="prmList" id="prmListMap">
		<ul>
			<li>
				<s:checkbox name="groupIdx" fieldValue="%{#prmListMap.groupId}" onclick="BINOLSSPRM71.checkRecord(this,'#dataTable_Cloned');"></s:checkbox>
				<s:url action="BINOLSSPRM71_searchConjunctionDetail" id="searchConjunctionDetailUrl">
					<s:param name="groupId" value="%{#prmListMap.groupId}"></s:param>
				</s:url>
				<input name="searchConjunctionDetailUrl" id="searchConjunctionDetailUrl" value="${searchConjunctionDetailUrl }" type="hidden"/>
			</li>
			<li><s:property value="groupId"/></li>		
			<li>
				<s:iterator value="%{#prmListMap.nameTotalList}" id="nameTotal" status="status" var="var">
					<s:if test="#status.last != true">
						<s:property value="var"/>
						<s:if test="(#status.index+1)%3 == 0">
							<br/>
						</s:if>
						<s:else>
							<span style="color:red">/</span>
						</s:else>
					</s:if>
					<s:else>
						<s:property value="var"/>
					</s:else>
				</s:iterator>
			</li>
		</ul>
	</s:iterator>
</div>
</s:i18n>
