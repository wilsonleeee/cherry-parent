<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.mo.BINOLMORPT03">
<div id="aaData">
	<s:iterator value="checkAnswerList" id="checkAnswerMap">
		<ul>
			<%-- No. --%>
			<li><s:property value="RowNumber"/></li>
			<%-- 问卷名称 --%>
			<li><span><s:property value="paperName"/></span></li>
			<%-- 问卷类型 --%>
			<li><span><s:property value='#application.CodeTable.getVal("1107", #checkAnswerMap.paperType)' /></span></li>
			<%-- 柜台 --%>
			<li><span>
			  <s:if test='%{#checkAnswerMap.departName != null && !"".equals(#checkAnswerMap.departName)}'>
			  	(<s:property value="departCode"/>)<s:property value="departName"/>
			  </s:if>
			</span></li>
			<%-- 回答人员 --%>
			<li><span>
		      <s:if test='%{#checkAnswerMap.employeeName != null && !"".equals(#checkAnswerMap.employeeName)}'>
			  	(<s:property value="employeeCode"/>)<s:property value="employeeName"/>
			  </s:if>
			</span></li>
			<%-- 考核时间 --%>
			<li><span><s:property value="answerDate"/></span></li>
			<%-- 考核总分 --%>
			<li><span><s:property value="totalPoint"/></span></li>
			<%-- 查看详细 --%>
			<li><span>
			  <s:url action="BINOLMORPT04_init" id="BINOLMORPT04_initUrl">
			  	<s:param name="paperAnswerId" value="%{#checkAnswerMap.paperAnswerId}"></s:param>
			  </s:url>	
			  <a class="delete" href="${BINOLMORPT04_initUrl }" onclick="javascript:openWin(this);return false;">
				<span class="ui-icon ui-icon-document"></span><span class="button-text"><s:text name="binolmorpt03.detail" /></span>
			  </a>
			</span></li>
		</ul>
	</s:iterator>
</div>
</s:i18n>
