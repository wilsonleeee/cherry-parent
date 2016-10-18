<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.mo.BINOLMORPT01">
<div id="aaData">
	<s:iterator value="checkAnswerList" id="checkAnswerMap">
		<ul>
			<%-- No. --%>
			<li><s:property value="RowNumber"/></li>
			<%-- 问卷名称 --%>
			<li><span><s:property value="paperName"/></span></li>
			<%-- 柜台 --%>
			<li><span>
			  <s:if test='%{#checkAnswerMap.departCode != null && !"".equals(#checkAnswerMap.departCode)}'>
			  	(<s:property value="departCode"/>)<s:property value="departName"/>
			  </s:if>
			</span></li>
			<%-- 考核对象 --%>
			<li><span>
				<s:if test='%{#checkAnswerMap.employeeCode != null && !"".equals(#checkAnswerMap.employeeCode)}'>
			  	(<s:property value="employeeCode"/>)<s:property value="employeeName"/>
			  </s:if>
			</span></li>
			<%-- 考核人 --%>
			<li><span><s:property value="ownerName"/></span></li>
			<%-- 考核时间 --%>
			<li><span><s:property value="checkDate"/></span></li>
			<%-- 得分 --%>
			<li><span><s:property value="totalPoint"/></span></li>
			<%-- 查看详细 --%>
			<li><span>
			  <s:url action="BINOLMORPT02_init" id="BINOLMORPT02_initUrl">
			  	<s:param name="checkAnswerId" value="%{#checkAnswerMap.checkAnswerId}"></s:param>
			  </s:url>	
			  <a class="delete" href="${BINOLMORPT02_initUrl }" onclick="javascript:openWin(this);return false;">
				<span class="ui-icon ui-icon-document"></span><span class="button-text"><s:text name="binolmorpt01.detail" /></span>
			  </a>
			</span></li>
		</ul>
	</s:iterator>
</div>
</s:i18n>
