<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.mo.BINOLMOWAT11">
<div id="aaData">
	<s:iterator value="jobRunFaildList" id="jobRunFaildMap">
		<ul>
            <%-- No. --%>
			<li><s:property value="RowNumber"/></li>
			
			<%-- 品牌 --%>
			<%-- <li><span><s:property value="brandCode"/></span></li> --%>
			
			<%-- JobCode --%>
			<li><span><s:property value="jobCode"/></span></li>
			<%-- unionIndex --%>
			<li><span><s:property value="unionIndex"/></span></li>
			<%-- unionIndex1 --%>
			<li><span><s:property value="unionIndex1"/></span></li>
			<%-- unionIndex2 --%>
			<li><span><s:property value="unionIndex2"/></span></li>
			<%-- unionIndex3 --%>
			<li><span><s:property value="unionIndex3"/></span></li>
			<!-- 错误信息ErrorMsg -->
			<li><span>
			  <s:set value="%{#jobRunFaildMap.errorMsg }" var="errorMsg"></s:set>
			  <a title="" rel='<s:property value="errorMsg"/>' onclick="binolmowat11.showDetail(this);return false;" style="cursor:pointer;">
				<cherry:cut length="45" value="${errorMsg }"></cherry:cut>
			  </a>
			</span></li>
			<!-- 运行信息 -->
			<li><span>
			  <s:set value="%{#jobRunFaildMap.comments }" var="comments"></s:set>
			  <a title="" rel='<s:property value="comments"/>' onclick="binolmowat11.showDetail(this);return false;" style="cursor:pointer;">
				<cherry:cut length="50" value="${comments }"></cherry:cut>
			  </a>
			</span></li>
		</ul>
	</s:iterator>
</div>
</s:i18n>
