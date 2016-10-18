<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.mo.BINOLMOCIO09">

<div id="aaData">
	<s:iterator value="paperList" status="status">
		<s:url id="detailsUrl" value="/mo/BINOLMOCIO11_init">
			<s:param name="paperId">${paperId}</s:param>
		</s:url>
		<ul>
			<%--选择 --%>
			<li> 
           <s:checkbox id="%{paperId}" name="validFlag" value="false" fieldValue="%{paperStatus+'*'+issuedStatus}" onclick="checkSelect(this);"/>
        	</li>
			<%-- No. --%>
			<li>${RowNumber}</li>
			<%-- 问卷名称 --%>
			<li>
				<a href="${detailsUrl}" class="popup" onclick="javascript:popupWind(this);return false;" title="点击查看问卷详细">
					<span><s:property value="paperName"/></span>
				</a>
			</li>
			<%-- 问卷类型 --%>
			<li><span><s:property value='#application.CodeTable.getVal("1126", paperRight)'/></span></li>
			<%-- 问卷状态  --%>
			<li><span><s:property value='#application.CodeTable.getVal("1108", paperStatus)'/></span></li>
			<%-- 开始时间--%>
			<li><span>${startDate}</span></li>
			<%-- 结束时间--%>
			<li><span>${endDate}</span></li>
			<%-- 发布人 --%>
			<li><span><s:property value="publisher"/></span></li>
			<%-- 发布时间 --%>
			<li><span><s:property value="publishTime"/></span></li>
			<%-- 操作 --%>
			<li>
				<s:if test='"0".equals(issuedStatus)'>
					<cherry:show domId="BINOLMOCIO0904">
		                <a id="${paperId}" class="delete" onclick="cio09EditCheckPaper(this);return false;">
		                <span class="ui-icon icon-edit"></span>
		                <span class="button-text"><s:text name="CIO09_editPaper"/></span>
		                </a>
	                </cherry:show>
				</s:if>
			</li>
		</ul>
	</s:iterator>
</div>
</s:i18n>