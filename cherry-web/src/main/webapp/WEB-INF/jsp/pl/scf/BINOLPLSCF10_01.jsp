<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.pl.BINOLPLSCF07">
<div id="aaData">
	<s:iterator value="coderList" id="coder">
		<ul>
			<%-- CodeType --%>
			<li><s:property value="#coder.codeType"/></li>
			<li>
			<%-- CodeKey--%>
			<a href="${detailsUrl}" class="popup" onclick="plscf10_coderDetail('${coder.coderId}');return false;">		
				<s:if test='#coder.codeKey != null && !"".equals(#coder.codeKey)'>
				      <s:property value="#coder.codeKey"/>
				</s:if>
				<s:else>&nbsp;</s:else>
			</a>
			</li>
			<%-- Value1 --%>
			<li>
				<s:if test='#coder.value1 !=null && !"".equals(#coder.value1)'><s:property value="#coder.value1"/></s:if>
				<s:else>&nbsp;</s:else>
			</li>
			<%-- value2 --%>
			<li>
				<s:if test='#coder.value2 !=null && !"".equals(#coder.value2)'><s:property value="#coder.value2"/></s:if>
				<s:else>&nbsp;</s:else>
			</li>
			<%-- value3 --%>
		    <li>
				<s:if test='#coder.value3 !=null && !"".equals(#coder.value3)'><s:property value="#coder.value3"/></s:if>
				<s:else>&nbsp;</s:else>
			</li>
			<%-- Grade --%>
		    <li>
				<s:if test='#coder.grade !=null && !"".equals(#coder.grade)'><s:property value="#coder.grade"/></s:if>
				<s:else>&nbsp;</s:else>
			</li>
			<%-- CodeOrder --%>
		    <li>
				<s:if test='#coder.codeOrder !=null && !"".equals(#coder.codeOrder)'><s:property value="#coder.codeOrder"/></s:if>
				<s:else>&nbsp;</s:else>
			</li>
			
		</ul>
	</s:iterator>
</div>
</s:i18n>
