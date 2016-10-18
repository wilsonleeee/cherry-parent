<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.mo.BINOLMOWAT10">
<div id="aaData">
	<s:iterator value="jobRunList" id="jobRunMap">
		<ul>
            <%-- No. --%>
			<li><s:property value="RowNumber"/></li>
			
			<%-- 品牌 --%>
			<%-- <li><span><s:property value="brandCode"/></span></li> --%>
			
			<%-- JobCode --%>
			<li><span><s:property value="jobCode"/></span></li>
			<%-- 运行结果 --%>
			<li><span>
			<s:if test='%{#jobRunMap.result != null && "S".equals(#jobRunMap.result)}'>
			<s:text name="binolmowat10_result0" />
			</s:if>
			<s:elseif test='%{#jobRunMap.result != null && "W".equals(#jobRunMap.result)}'>
			<s:text name="binolmowat10_result1" />
			</s:elseif>
			<s:elseif test='%{#jobRunMap.result != null && "E".equals(#jobRunMap.result)}'>
			<s:text name="binolmowat10_result2" />
			</s:elseif>
			</span></li>
			<%-- 信息描述 --%>
			<li><span>
			  <s:set value="%{#jobRunMap.comments }" var="comments"></s:set>
			  <a title="" rel='<s:property value="comments"/>' onclick="binolmowat10.showDetail(this);return false;" style="cursor:pointer;">
				<cherry:cut length="45" value="${comments }"></cherry:cut>
			  </a>
			</span></li>
			<%-- 开始时间 --%>
			<li><span><s:property value="runStartTime"/></span></li>
			<%-- 结束时间 --%>
			<li><span><s:property value="runEndTime"/></span></li>
		</ul>
	</s:iterator>
</div>
</s:i18n>
