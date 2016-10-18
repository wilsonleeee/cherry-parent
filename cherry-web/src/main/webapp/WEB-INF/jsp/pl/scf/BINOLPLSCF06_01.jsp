<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.pl.BINOLPLSCF06">
<div id="aaData">
	<s:iterator value="codeList" id="codeM">
		<s:url id="detailsUrl" value="/pl/BINOLPLSCF07_init.action">
			<s:param name="codeManagerID">${codeM.codeManagerID}</s:param>
		</s:url>
		<ul>
			<%-- CodeType --%>
			<li><a href="${detailsUrl}" class="popup" onclick="javascript:openWin(this);return false;"><s:property value="#codeM.codeType"/></a></li>
			<%-- CodeName--%>
			<li>			
				<s:if test='#codeM.codeName != null && !"".equals(#codeM.codeName)'>
				      <s:property value="#codeM.codeName"/>
				</s:if>
				<s:else>&nbsp;</s:else>
			</li>
			<%-- KeyDescription --%>
			<li>
				<s:if test='#codeM.keyDescription !=null && !"".equals(#codeM.keyDescription)'><s:property value="#codeM.keyDescription"/></s:if>
				<s:else>&nbsp;</s:else>
			</li>
			<%-- Value1Description --%>
			<li>
				<s:if test='#codeM.value1Description !=null && !"".equals(#codeM.value1Description)'><s:property value="#codeM.value1Description"/></s:if>
				<s:else>&nbsp;</s:else>
			</li>
			<%-- Value2Description --%>
		    <li>
				<s:if test='#codeM.value2Description !=null && !"".equals(#codeM.value2Description)'><s:property value="#codeM.value2Description"/></s:if>
				<s:else>&nbsp;</s:else>
			</li>
			<%-- Value3Description --%>
		    <li>
				<s:if test='#codeM.value3Description !=null && !"".equals(#codeM.value3Description)'><s:property value="#codeM.value3Description"/></s:if>
				<s:else>&nbsp;</s:else>
			</li>
		</ul>
	</s:iterator>
</div>
</s:i18n>
