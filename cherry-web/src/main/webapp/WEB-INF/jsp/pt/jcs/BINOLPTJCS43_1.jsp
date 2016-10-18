<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.pt.BINOLPTJCS43">
    <div id="aaData">
	    <s:iterator value="prtList" id="prtListMap">
		    <ul>
		        <li>
					<s:checkbox name="groupIdx" fieldValue="%{#prtListMap.groupId}" onclick="BINOLPTJCS43.checkRecord(this,'#dataTable_Cloned');"></s:checkbox>
					<s:url action="BINOLPTJCS43_searchConjunctionDetail" id="searchConjunctionDetailUrl">
						<s:param name="groupId" value="%{#prtListMap.groupId}"></s:param>
					</s:url>
					<input name="searchConjunctionDetailUrl" id="searchConjunctionDetailUrl" value="${searchConjunctionDetailUrl }" type="hidden"/>
				</li>
				<li><s:property value="groupId"/></li>		
				<li>
					<s:iterator value="%{#prtListMap.nameTotalList}" id="nameTotal" status="status" var="var">
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